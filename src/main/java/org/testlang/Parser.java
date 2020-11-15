package org.testlang;



public class Parser {
	private Scanner scanner;
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	private Token currentToken;
	

	public void parse() {
		currentToken = scanner.scan();
	System.out.println( " parse start" );
		parseProgram();
		if (currentToken.kind != TokenKind.EOT) {
			//report syntactic error
		}
	}

	public void acceptIt() {
		currentToken = scanner.scan();
	}

	private void accept (TokenKind expectedKind) {
		if (currentToken.kind == expectedKind) {
			currentToken = scanner.scan();
		} 
		else {
			// report syntactic error
		}
	}

	private Program parseProgram() {
		Program program;
		DeclarationList dl;
		accept(TokenKind.LEFT_SQUARE);
		while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
			parseDeclarationList();
			parseStatement();
		}
		accept(TokenKind.RIGHT_SQUARE);
		program = new Program(dl);
		return program;
	}

	private void parseDeclarationList() {
		parseDeclaration();
	}

	private void parseDeclaration() {
		if (currentToken.kind == TokenKind.IDENTIFIER) {
			parseVarDeclaration();
		}
		else {
			TokenKind oprType = currentToken.kind;
			parseOprDeclaration(oprType);
		}
	}

	private void parseVarDeclaration() {
		parseIdentifier();
		accept(TokenKind.COLON);
		TokenKind type = currentToken.kind;
		switch (type) {
		case NUMBER_TYPE: 
		case LETTER_TYPE: 
		case STATE_TYPE: 
			acceptIt();
			accept(TokenKind.OPERATOR);
			parseLiteral(type);
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
	}

	private void parseOprDeclaration(TokenKind type) {
		parseTypeDenoter();
		accept(TokenKind.OPR);
		parseIdentifier();
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
				parseIdentifier();
				break;
			default:
				break;
			}
			accept(TokenKind.SEMICOLON);
		}
	}

	private void parseBlock() {
		accept(TokenKind.LEFT_SQUARE);
		while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
			parseVarDeclaration();
			parseStatement();
		}
		accept(TokenKind.RIGHT_SQUARE);
	}

	private void parseParameter() {
		parseIdentifier();
		accept(TokenKind.COLON);
		parseTypeDenoter();
	}

	private void parseStatement() {
		parseSingleStatement();
		while (currentToken.kind == TokenKind.SEMICOLON) {
			acceptIt();
			parseSingleStatement();
		}
	}

	private void parseSingleStatement() {
		switch (currentToken.kind) {
		case IDENTIFIER: 
			parseIdentifier();
			parseExpresion();
			break;
		case IF:
		case UNTIL:
			acceptIt();
			accept(TokenKind.LEFT_PARAN);
			parseExpresion();
			accept(TokenKind.RIGHT_PARAN);
			parseBlock();
			break;
		case IN:	
		case OUT:
			acceptIt();
			accept(TokenKind.OPERATOR);
			parseExpresion();
			break;
		case LEFT_SQUARE:
			parseBlock();
		default:
			break;
		}
	}

	
	private void parseExpresion() {
		parseExpresion1();
		if (currentToken.isAssignOperator()) {
			acceptIt();
			parseExpresion();
		}
	}

	private void parseExpresion1() {
		parseExpresion2();
		while (currentToken.isBoolOperator()) {
			acceptIt();
			parseExpresion2();
		}
	}
	
	private void parseExpresion2() {
		parseExpresion3();
		while (currentToken.isAddOperator()) {
			acceptIt();
			parseExpresion3();
		}
	}
	
	private void parseExpresion3() {
		parseExpresion4();
		while (currentToken.isMulOperator()) {
			acceptIt();
			parseExpresion4();
		}
	}
	
	private void parseExpresion4() {
		parsePrimaryExpresion();
		while (currentToken.isArrOperator()) {
			acceptIt();
			parsePrimaryExpresion();
		}

	}

	private void parsePrimaryExpresion() {
		switch (currentToken.kind) {
		case IDENTIFIER:
			parseIdentifier();
			if (currentToken.kind == TokenKind.LEFT_PARAN) {
				acceptIt();
				parseExpresionList();
				accept(TokenKind.RIGHT_PARAN);
			}
			break;
		case OPERATOR:
			acceptIt();
			parsePrimaryExpresion();
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
			parseExpresion();
			accept(TokenKind.RIGHT_PARAN);
			break;
		default:
			break;
		}
	}

	private void parseExpresionList() {
		parseExpresion();
		while (currentToken.kind == TokenKind.COMMA) {
			acceptIt();
			parseExpresion();
		}
		
	}

	private void parseLiteral(TokenKind type) {
		switch (type) {
		case LETTER_LITERAL:
			if (type == TokenKind.LETTER_TYPE) {
				acceptIt();
			} else {
				// report syntactic error
			}
			break;
	    case NUMBER_LITERAL: 
			if (type == TokenKind.NUMBER_TYPE) {
				acceptIt();
			} else {
				// report syntactic error
			}
			break;
		case STATE_LITERAL: 
			if (type == TokenKind.STATE_TYPE) {
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
	}

	private void parseColectionLiteral() {
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
	}

	private void parseTypeDenoter() {
		switch (currentToken.kind) {
		case NUMBER_TYPE:
		case LETTER_TYPE:
		case STATE_TYPE:
		case VOID_TYPE: 
			acceptIt();
			break;
		default:
			// report syntactic error
			break;
		}

	}

	private void parseIdentifier() {
		if (currentToken.kind == TokenKind.IDENTIFIER) {
			acceptIt();
		}

	}


}