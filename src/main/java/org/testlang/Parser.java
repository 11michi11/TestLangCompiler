package org.testlang;

import org.testlang.AST.*;
import org.testlang.AST.Number;
import org.testlang.AST.Void;

public class Parser {
	private Scanner scanner;
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	private Token currentToken;


	public void parse() {
		System.out.println( " parse start" );
		parseProgram();
		if (currentToken.kind != TokenKind.EOT) {
			//report syntactic error
		}
	}

	public void acceptIt() {
		System.out.print("acceptIt() "+ currentToken.kind);
		currentToken = scanner.scan();
		System.out.println("   next token is: "+ currentToken.kind);
	}

	private void accept (TokenKind expectedKind) {
		if (currentToken.kind == expectedKind) {
			System.out.print("accept( "+ expectedKind + " ) ");
			currentToken = scanner.scan();
			System.out.println("next token is: "+ currentToken.kind);
		} 
		else {
			System.out.println(currentToken.kind + " is not the expected token kind "+ expectedKind);
		}
	}

	private Program parseProgram() {
		Program program;
		currentToken = scanner.scan();
		DeclarationList dl = new DeclarationList();
		StatementList sl = new StatementList();
		accept(TokenKind.LEFT_SQUARE);
		while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
			Token first = currentToken;
			switch (first.kind ) {
			case IDENTIFIER:
				acceptIt();
				switch (currentToken.kind) {
				case COLON:
					parseDeclarationList(first,  dl);
					break;
				case OPERATOR:
					parseStatementList(first);
				default:
					break;
				}
				break;
			default:
				break;
			}
			//parseDeclarationList(first,  dl);
			//parseStatement();
		}
		accept(TokenKind.RIGHT_SQUARE);
		program = new Program(dl);
		return program;
	}

	private DeclarationList parseDeclarationList(Token first, DeclarationList dl) {
		Declaration d = parseDeclaration(first);
		if (!(d == null)) {
			System.out.println("add declaration "+d.toString());
			dl.add(d);
		}else {
			System.out.println("null- parse dl");
		}

		return dl;	
	}

	private Declaration parseDeclaration(Token first) {
		Declaration declaration = new Declaration();
		switch (first.kind ) {
		case IDENTIFIER:
			declaration = parseVarDeclaration(first);
			break;
		case NUMBER_TYPE:
		case LETTER_TYPE:
		case STATE_TYPE:
		case COL_TYPE:
		case VOID_TYPE:
			declaration = parseOprDeclaration(currentToken.kind);
			break;
		default:
			break;
		}
		return declaration;
	}

	private Declaration parseVarDeclaration(Token first) {
		Identifier i = parseIdentifier(first);
		accept(TokenKind.COLON);
		TokenKind type = currentToken.kind;
		Type t =parseTypeDenoter();
		switch (type) {
		case NUMBER_TYPE: 
		case LETTER_TYPE: 
		case STATE_TYPE: 
			if (currentToken.isAssignOperator()) {
				accept(TokenKind.OPERATOR);
				Literal l = parseLiteral(type);
			}
			accept(TokenKind.SEMICOLON);

			break;
		case COL_TYPE:
			acceptIt();
			accept(TokenKind.LEFT_DIAMOND);
			parseTypeDenoter();
			accept(TokenKind.RIGHT_DIAMOND);
			switch (currentToken.kind) {
			case LEFT_CURLY: 
				acceptIt();
				if (currentToken.kind == TokenKind.NUMBER_LITERAL || currentToken.kind ==TokenKind.IDENTIFIER ) {
					acceptIt();
				} else {
					// report syntactic error
				}
				accept(TokenKind.RIGHT_CURLY);
				break;
			case OPERATOR:  
				acceptIt();
				parseColectionLiteral();
				break;
			default:
				break;
			}
			accept(TokenKind.SEMICOLON);
			break;
		default:
			break;
		}
		System.out.println(t+" here "+currentToken.kind);
		Declaration declaration = new Declaration(i, t);
		return declaration;
	}

	private Declaration parseOprDeclaration(TokenKind type) {
		Identifier i;
		Type t;
		Literal l;		
		t = parseTypeDenoter();
		accept(TokenKind.OPR);
		i = parseIdentifier(currentToken);
		accept(TokenKind.LEFT_PARAN);
		parseParameter();
		while (currentToken.kind == TokenKind.COMMA) {
			acceptIt();
			parseParameter();
		}
		accept(TokenKind.RIGHT_PARAN); 
		parseBlock();
		if (type == TokenKind.VOID_TYPE) {
		} else {
			accept(TokenKind.SEND);
			switch (currentToken.kind) {
			case LETTER_LITERAL: 
			case NUMBER_LITERAL: 
			case STATE_LITERAL: 
				parseLiteral(type);
				break;
			case IDENTIFIER: 
				parseIdentifier(currentToken);
				break;
			default:
				break;
			}
			accept(TokenKind.SEMICOLON);
		}
		Declaration declaration = new Declaration(i, t);
		return declaration;
	}

	private Block parseBlock() {
		Block block = null;
		accept(TokenKind.LEFT_SQUARE);
		while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
			parseVarDeclaration(currentToken);
			parseStatementList(currentToken);
		}
		accept(TokenKind.RIGHT_SQUARE);
		return block;
	}

	private void parseParameter() {
		parseIdentifier(currentToken);
		accept(TokenKind.COLON);
		parseTypeDenoter();
	}

	private void parseStatementList(Token first) {
		parseSingleStatement(first);
		while (currentToken.kind == TokenKind.SEMICOLON) {
			acceptIt();
			parseSingleStatement(first);
		}
	}

	private void parseSingleStatement(Token first) {
		switch (first.kind) {
		case IDENTIFIER: 
			parseIdentifier(currentToken);
			parseExpression();
			break;
		case IF:
		case UNTIL:
			acceptIt();
			accept(TokenKind.LEFT_PARAN);
			parseExpression();
			accept(TokenKind.RIGHT_PARAN);
			parseBlock();
			break;
		case IN:	
		case OUT:
			acceptIt();
			accept(TokenKind.OPERATOR);
			parseExpression();
			break;
		case LEFT_SQUARE:
			parseBlock();
		default:
			break;
		}
	}


	private Expression parseExpression() {
		Expression exp = parseExpression1();
		if (currentToken.isAssignOperator()) {
			Operator op = parseOperator();
			Expression tmp = parseExpression();
			exp = new BinaryExpression( op, exp, tmp );
		}
		return exp;
	}

	private Operator parseOperator() {
		if( currentToken.kind == TokenKind.OPERATOR ) {
			Operator op = new Operator( currentToken );
			currentToken = scanner.scan();
			return op;
		} else {
			System.out.println( "Operator expected" );

			return new Operator(new Token(TokenKind.OPERATOR, "?!?"));
		}
	}

	private Expression parseExpression1() {
		Expression exp = parseExpression2();
		while (currentToken.isBoolOperator()) {
			Operator op = parseOperator();
			Expression tmp = parseExpression2();
			exp = new BinaryExpression( op, exp, tmp );
		}
		return exp;
	}

	private Expression parseExpression2() {
		Expression exp = parseExpression3();
		while (currentToken.isAddOperator()) {
			Operator op = parseOperator();
			Expression tmp = parseExpression3();
			exp = new BinaryExpression( op, exp, tmp );
		}
		return exp;
	}

	private Expression parseExpression3() {
		Expression exp = parseExpression4();
		while (currentToken.isMulOperator()) {
			Operator op = parseOperator();
			Expression tmp = parseExpression4();
			exp = new BinaryExpression( op, exp, tmp );
		}
		return exp;
	}

	private Expression parseExpression4() {
		Expression exp = parsePrimaryExpression();
		while (currentToken.isArrOperator()) {
			Operator op = parseOperator();
			Expression tmp = parsePrimaryExpression();
			exp = new BinaryExpression( op, exp, tmp );
		}
		return exp;
	}

	private Expression parsePrimaryExpression() {
		switch (currentToken.kind) {
		case IDENTIFIER:
			parseIdentifier(currentToken);
			if (currentToken.kind == TokenKind.LEFT_PARAN) {
				acceptIt();
				parseExpressionList();
				accept(TokenKind.RIGHT_PARAN);
			}
			break;
		case OPERATOR:
			acceptIt();
			parsePrimaryExpression();
			break;
		case LETTER_LITERAL:
			parseLiteral(TokenKind.LETTER_TYPE);
			break;
		case NUMBER_LITERAL:
			parseLiteral(TokenKind.NUMBER_TYPE);
			break;
		case STATE_LITERAL:
			parseLiteral(TokenKind.STATE_TYPE);
			break;
		case LEFT_PARAN:
			acceptIt();
			parseExpression();
			accept(TokenKind.RIGHT_PARAN);
			break;
		default:
			break;
		}
		return null;
	}

	private ParameterList parseExpressionList() {
		parseExpression();
		while (currentToken.kind == TokenKind.COMMA) {
			acceptIt();
			parseExpression();
		}

	}

	private Literal parseLiteral(TokenKind type) {
		Literal l;
		l = new Literal(currentToken);
		switch (currentToken.kind) {
		case LETTER_LITERAL:
			if (type == TokenKind.LETTER_TYPE) {
				l = new Literal(currentToken);
				acceptIt();
			} else {
				// report syntactic error
			}
			break;
		case NUMBER_LITERAL: 
			if (type == TokenKind.NUMBER_TYPE) {
				l = new Literal(currentToken);
				acceptIt();
			} else {
				// report syntactic error
			}
			break;
		case STATE_LITERAL: 
			if (type == TokenKind.STATE_TYPE) {
				l = new Literal(currentToken);
				acceptIt();
			} else {
				// report syntactic error
			}
			break;
		case COL_TYPE: 
			acceptIt();
			parseColectionLiteral();
			break;
		default:

			break;
		}
		return l;
	}

	private CollectionLiteral parseColectionLiteral() {
		CollectionLiteral colLit = null;
		accept(TokenKind.LEFT_DIAMOND);
		switch (currentToken.kind) {
		case IDENTIFIER: 
			acceptIt();
			break;
		case LETTER_LITERAL:
		case NUMBER_LITERAL:
		case STATE_LITERAL: 
			acceptIt();
			break;
		default:
			break;
		}
		while (currentToken.kind == TokenKind.COMMA) {
			acceptIt();
			switch (currentToken.kind) {
			case IDENTIFIER: 
				acceptIt();
				break;
			case LETTER_LITERAL:
			case NUMBER_LITERAL:
			case STATE_LITERAL: 
				acceptIt();
				break;
			default:
				break;
			}
		}
		accept(TokenKind.RIGHT_DIAMOND);
		return colLit;
	}

	private Type parseTypeDenoter() {
		Type t = null;
		switch (currentToken.kind) {
		case NUMBER_TYPE:
			t = new Number(currentToken);
			acceptIt();
			break;
		case LETTER_TYPE:
			t = new Letter(currentToken);
			acceptIt();
			break;
		case STATE_TYPE:
			t = new State(currentToken);
			acceptIt();
			break;
		case VOID_TYPE: 
			t = new Void(currentToken);
			acceptIt();
			break;
		case COL_TYPE: 
			t = new Collection(currentToken);
			acceptIt();
			break;
		default:
			// report syntactic error
			break;
		}
		return t;
	}

	private Identifier parseIdentifier(Token token) {
		Identifier i = null;
		if (token.kind == TokenKind.IDENTIFIER) {
			i = new Identifier(token);
		}else {
			System.out.println(currentToken.spelling);
		}
		return i;
	}


}