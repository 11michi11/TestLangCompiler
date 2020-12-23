package org.testlang;

import org.testlang.AST.*;
import org.testlang.AST.Number;
import org.testlang.AST.Void;

public class Parser {
    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    private Token currentToken;


    public AST parse() {
        var program = parseProgram();
        if (currentToken.kind != TokenKind.EOT) {
            throw new IllegalArgumentException("There is no EOT at the end of file");
        }
        return program;
    }

    public void acceptIt() {
        currentToken = scanner.scan();
    }

    private void accept(TokenKind expectedKind) {
        if (currentToken.kind == expectedKind) {
            currentToken = scanner.scan();
        } else {
            throw new IllegalArgumentException(currentToken.kind + " is not the expected token kind " + expectedKind);
        }
    }

    private Program parseProgram() {
        Program program;
        currentToken = scanner.scan();
        DeclarationList declarationList = new DeclarationList();
        StatementList statementList = new StatementList();
        accept(TokenKind.LEFT_SQUARE);
        while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
            switch (currentToken.kind) {
                case VAR -> {
                    accept(TokenKind.VAR);
                    declarationList.add(parseVarDeclaration());
                }
                case OPR -> {
                    accept(TokenKind.OPR);
                    declarationList.add(parseOprDeclaration());
                }
                default -> statementList.add(parseStatement());
            }
        }
        accept(TokenKind.RIGHT_SQUARE);
        program = new Program(declarationList, statementList);
        return program;
    }

    private Declaration parseVarDeclaration() {
        Identifier identifier = parseIdentifier();
        accept(TokenKind.COLON);
        TokenKind tokenKind = currentToken.kind;
        TypeDenoter typeDenoter = parseTypeDenoter();

        if (tokenKind == TokenKind.COL_TYPE) {
            accept(TokenKind.LEFT_DIAMOND);
            Collection collectionVarType = (Collection) typeDenoter;
            TypeDenoter collectionTypeDenoter = parseTypeDenoter();
            collectionVarType.setCollectionType(collectionTypeDenoter);
            accept(TokenKind.RIGHT_DIAMOND);
            if (currentToken.kind == TokenKind.LEFT_CURLY) {
                accept(TokenKind.LEFT_CURLY);
                NumberLiteral size;
                if (currentToken.kind == TokenKind.NUMBER_LITERAL) { // || currentToken.kind == TokenKind.IDENTIFIER) { add this when it can work in checker
                    size = (NumberLiteral) parseLiteral(TokenKind.NUMBER_TYPE);
                } else {
                    // Collection with size zero
                    size = new NumberLiteral(new Token(TokenKind.NUMBER_LITERAL, "0"));
                }
                collectionVarType.setSize(size);
                accept(TokenKind.RIGHT_CURLY);
            } else {
                throw new IllegalArgumentException("Expected { in collection declaration");
            }
        }
        accept(TokenKind.SEMICOLON);
        return new VarDeclaration(identifier, typeDenoter);
    }

    // TODO this is not done
    private Declaration parseOprDeclaration() {
        Identifier identifier;
        TypeDenoter typeDenoter;
        Literal literal;
        typeDenoter = parseTypeDenoter();
        accept(TokenKind.OPR);
        identifier = parseIdentifier();
        accept(TokenKind.LEFT_PARAN);
        parseParameter();
        while (currentToken.kind == TokenKind.COMMA) {
            acceptIt();
            parseParameter();
        }
        accept(TokenKind.RIGHT_PARAN);
        parseBlock();
        if (currentToken.kind == TokenKind.VOID_TYPE) {
            throw new UnsupportedOperationException("Token " + currentToken.spelling + " is currently not handled");
        } else {
            accept(TokenKind.SEND);
            switch (currentToken.kind) {
                case LETTER_LITERAL:
                case NUMBER_LITERAL:
                case STATE_LITERAL:
                    parseLiteral(currentToken.kind);
                    break;
                case IDENTIFIER:
                    parseIdentifier();
                    break;
                default:
                    break;
            }
            accept(TokenKind.SEMICOLON);
        }
        return new OprDeclaration(identifier, typeDenoter);
    }

    private Block parseBlock() {
        DeclarationList declarationList = new DeclarationList();
        StatementList statementList = new StatementList();
        accept(TokenKind.LEFT_SQUARE);
        while (currentToken.kind != TokenKind.RIGHT_SQUARE) {
            if (currentToken.kind == TokenKind.VAR) {
                accept(TokenKind.VAR);
                declarationList.add(parseVarDeclaration());
            } else {
                statementList.add(parseStatement());
            }
        }
        accept(TokenKind.RIGHT_SQUARE);
        return new Block(declarationList, statementList);
    }

    private void parseParameter() {
        parseIdentifier();
        accept(TokenKind.COLON);
        parseTypeDenoter();
    }

    private Statement parseStatement() {
        switch (currentToken.kind) {
            case IDENTIFIER -> {
                Expression expression = parseExpression();
                accept(TokenKind.SEMICOLON);
                return new ExpressionStatement(expression);
            }
            case IF -> {
                accept(TokenKind.IF);
                accept(TokenKind.LEFT_PARAN);
                Expression expression = parseExpression();
                accept(TokenKind.RIGHT_PARAN);
                Block block = parseBlock();
                return new IfStatement(expression, block);
            }
            case UNTIL -> {
                accept(TokenKind.UNTIL);
                accept(TokenKind.LEFT_PARAN);
                Expression expression = parseExpression();
                accept(TokenKind.RIGHT_PARAN);
                Block block = parseBlock();
                return new UntilStatement(expression, block);
            }
            case IN -> {
                accept(TokenKind.IN);
                accept(TokenKind.OPERATOR);
                Expression expression = parseExpression();
                accept(TokenKind.SEMICOLON);
                return new InStatement(expression);
            }
            case OUT -> {
                accept(TokenKind.OUT);
                accept(TokenKind.OPERATOR);
                Expression expression = parseExpression();
                accept(TokenKind.SEMICOLON);
                return new OutStatement(expression);
            }
            default -> throw new UnsupportedOperationException("Token " + currentToken.spelling + " is not legal in this place");
        }
    }


    private Expression parseExpression() {
        Expression operand1 = parseExpression1();
        if (currentToken.isAssignOperator()) {
            Operator operator = parseOperator();
            Expression operand2 = parseExpression();
            return new BinaryExpression(operator, operand1, operand2);
        }
        return operand1;
    }

    private Operator parseOperator() {
        if (currentToken.kind == TokenKind.OPERATOR || currentToken.isBoolOperator()) {
            Operator op = new Operator(currentToken);
            currentToken = scanner.scan();
            return op;
        } else {
            System.out.println("Operator expected");

            return new Operator(new Token(TokenKind.OPERATOR, "?!?"));
        }
    }

    private Expression parseExpression1() {
        Expression expression = parseExpression2();
        while (currentToken.isBoolOperator()) {
            Operator operator = parseOperator();
            Expression operand = parseExpression2();
            expression = new BinaryExpression(operator, expression, operand);
        }
        return expression;
    }

    private Expression parseExpression2() {
        Expression exp = parseExpression3();
        while (currentToken.isAddOperator()) {
            Operator op = parseOperator();
            Expression tmp = parseExpression3();
            exp = new BinaryExpression(op, exp, tmp);
        }
        return exp;
    }

    private Expression parseExpression3() {
        Expression exp = parseExpression4();
        while (currentToken.isMulOperator()) {
            Operator op = parseOperator();
            Expression tmp = parseExpression4();
            exp = new BinaryExpression(op, exp, tmp);
        }
        return exp;
    }

    private Expression parseExpression4() {
        Expression exp = parsePrimaryExpression();
        while (currentToken.isArrOperator()) {
            Operator op = parseOperator();
            Expression tmp = parsePrimaryExpression();
            exp = new BinaryExpression(op, exp, tmp);
        }
        return exp;
    }

    private Expression parsePrimaryExpression() {
        switch (currentToken.kind) {
            case IDENTIFIER -> {
                Identifier identifier = parseIdentifier();
                if (currentToken.kind == TokenKind.LEFT_PARAN) {
                    acceptIt();
                    ParameterList parameterList = parseExpressionList();
                    accept(TokenKind.RIGHT_PARAN);
                    return new CallExpression(identifier, parameterList);
                }
//                else if (currentToken.kind == TokenKind.LEFT_DIAMOND) {
//                    acceptIt();
//                    return new ArrayAccessExpression();
//                    accept(TokenKind.RIGHT_DIAMOND);
//                }
                else {
                    return new VarExpression(identifier);
                }
            }
            case OPERATOR -> {
                Operator operator = parseOperator();
                Expression expression = parsePrimaryExpression();
                return new UnaryExpression(operator, expression);
            }
            case SINGLE_QUOTE -> {
                accept(TokenKind.SINGLE_QUOTE);
                LetterLitExpression letterLitExpression = new LetterLitExpression((LetterLiteral) parseLiteral(TokenKind.LETTER_TYPE));
                accept(TokenKind.SINGLE_QUOTE);
                return letterLitExpression;
            }
            case NUMBER_LITERAL -> {
                return new NumberLitExpression((NumberLiteral) parseLiteral(TokenKind.NUMBER_TYPE));
            }
            case FALSE, TRUE -> {
                return new StateLitExpression((StateLiteral) parseLiteral(TokenKind.STATE_TYPE));
            }
            case LEFT_PARAN -> {
                acceptIt();
                Expression expression = parseExpression();
                accept(TokenKind.RIGHT_PARAN);
                return expression;
            }
            case LEFT_DIAMOND -> {
                return new CollectionLitExpression(parseCollectionLiteral());
            }
            default -> {
            }
        }
        throw new UnsupportedOperationException("Token " + currentToken.spelling + " is currently not handled");
    }

    private ParameterList parseExpressionList() {
        parseExpression();
        while (currentToken.kind == TokenKind.COMMA) {
            acceptIt();
            parseExpression();
        }
        return null;
    }

    private Literal parseLiteral(TokenKind tokenKind) {
        Literal literal;
        switch (currentToken.kind) {
            // Case for letter literal
            case IDENTIFIER:
                if (tokenKind == TokenKind.LETTER_TYPE) {
                    literal = new LetterLiteral(currentToken);
                    acceptIt();
                } else {
                    throw new IllegalArgumentException("Token " + currentToken.spelling + " is not a letter literal");
                }
                break;
            case NUMBER_LITERAL:
                if (tokenKind == TokenKind.NUMBER_TYPE) {
                    literal = new NumberLiteral(currentToken);
                    acceptIt();
                } else if (tokenKind == TokenKind.LETTER_TYPE) {
                    literal = new LetterLiteral(currentToken);
                    acceptIt();
                } else {
                    throw new IllegalArgumentException("Token " + currentToken.spelling + " is not a number literal or letter literal");
                }
                break;
            case FALSE:
            case TRUE:
                if (tokenKind == TokenKind.STATE_TYPE) {
                    literal = new StateLiteral(currentToken);
                    acceptIt();
                } else {
                    throw new IllegalArgumentException("Token " + currentToken.spelling + " is not a state literal");
                }
                break;
            default:
                throw new UnsupportedOperationException("Token " + currentToken.spelling + " is currently not handled");
        }
        return literal;
    }

    private CollectionLiteral parseCollectionLiteral() {
        CollectionLiteral collectionLiteral = new CollectionLiteral();
        accept(TokenKind.LEFT_DIAMOND);
        switch (currentToken.kind) {
            case SINGLE_QUOTE:
                accept(TokenKind.SINGLE_QUOTE);
                collectionLiteral.addLiteral(new LetterLitExpression((LetterLiteral) parseLiteral(TokenKind.LETTER_TYPE)));
                accept(TokenKind.SINGLE_QUOTE);
                break;
            case NUMBER_LITERAL:
                collectionLiteral.addLiteral(new NumberLitExpression((NumberLiteral) parseLiteral(TokenKind.NUMBER_TYPE)));
                break;
            case STATE_LITERAL:
                collectionLiteral.addLiteral(new StateLitExpression((StateLiteral) parseLiteral(TokenKind.STATE_TYPE)));
                break;
            default:
                break;
        }
        while (currentToken.kind == TokenKind.COMMA) {
            acceptIt();
            switch (currentToken.kind) {
                case SINGLE_QUOTE:
                    accept(TokenKind.SINGLE_QUOTE);
                    collectionLiteral.addLiteral(new LetterLitExpression((LetterLiteral) parseLiteral(TokenKind.LETTER_TYPE)));
                    accept(TokenKind.SINGLE_QUOTE);
                    break;
                case NUMBER_LITERAL:
                    collectionLiteral.addLiteral(new NumberLitExpression((NumberLiteral) parseLiteral(TokenKind.NUMBER_TYPE)));
                    break;
                case STATE_LITERAL:
                    collectionLiteral.addLiteral(new StateLitExpression((StateLiteral) parseLiteral(TokenKind.STATE_TYPE)));
                    break;
                default:
                    break;
            }
        }
        accept(TokenKind.RIGHT_DIAMOND);
        return collectionLiteral;
    }

    private TypeDenoter parseTypeDenoter() {
        TypeDenoter typeDenoter = switch (currentToken.kind) {
            case NUMBER_TYPE -> new Number(currentToken);
            case LETTER_TYPE -> new Letter(currentToken);
            case STATE_TYPE -> new State(currentToken);
            case VOID_TYPE -> new Void(currentToken);
            case COL_TYPE -> new Collection(currentToken);
            default -> throw new IllegalArgumentException("Token " + currentToken.spelling + " is not a type denoter");
        };
        acceptIt();
        return typeDenoter;
    }

    private Identifier parseIdentifier() {
        if (currentToken.kind == TokenKind.IDENTIFIER) {
            Identifier identifier = new Identifier(currentToken);
            currentToken = scanner.scan();
            return identifier;
        } else {
            throw new IllegalArgumentException("Token " + currentToken.spelling + " is not an identifier");
        }
    }
}