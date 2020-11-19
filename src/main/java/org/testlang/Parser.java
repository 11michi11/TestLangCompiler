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


    public AST parse() {
        var program = parseProgram();
        if (currentToken.kind != TokenKind.EOT) {
            //report syntactic error
            System.out.println("Syntax error, no EOT at the end of file");
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
                case VAR:
                    accept(TokenKind.VAR);
                    declarationList.add(parseVarDeclaration());
                    break;
                case OPR:
                    accept(TokenKind.OPR);
//                    declarationList.add(parseOprDeclaration());
                    break;
                default:
                    statementList.add(parseStatement());
                    break;
            }
        }
        accept(TokenKind.RIGHT_SQUARE);
        program = new Program(declarationList, statementList);
        return program;
    }

    private Declaration parseDeclaration() {
        Declaration declaration = null;
        switch (currentToken.kind) {
            case IDENTIFIER:
                declaration = parseVarDeclaration();
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

    private Declaration parseVarDeclaration() {
        Identifier identifier = parseIdentifier();
        accept(TokenKind.COLON);
        TokenKind tokenKind = currentToken.kind;
        Type type = parseTypeDenoter();
        // TODO remove
        switch (tokenKind) {
            case NUMBER_TYPE:
            case LETTER_TYPE:
            case STATE_TYPE:
                if (currentToken.isAssignOperator()) {
                    accept(TokenKind.OPERATOR);
                    Literal l = parseLiteral(tokenKind);
                }
                accept(TokenKind.SEMICOLON);

                break;
            case COL_TYPE:
                accept(TokenKind.LEFT_DIAMOND);
                Type collectionType = parseTypeDenoter();
                ((Collection) type).setCollectionType(collectionType);
                // TODO Add collection type
                accept(TokenKind.RIGHT_DIAMOND);
                switch (currentToken.kind) {
                    case LEFT_CURLY:
                        acceptIt();
                        if (currentToken.kind == TokenKind.NUMBER_LITERAL || currentToken.kind == TokenKind.IDENTIFIER) {
                            acceptIt();
                        } else {
                            // report syntactic error
                        }
                        accept(TokenKind.RIGHT_CURLY);
                        break;
                    case OPERATOR:
                        acceptIt();
                        parseCollectionLiteral();
                        break;
                    default:
                        break;
                }
                accept(TokenKind.SEMICOLON);
                break;
            default:
                break;
        }
//        System.out.println(type + " here " + currentToken.kind);
        return new VarDeclaration(identifier, type);
    }

    private Declaration parseOprDeclaration(TokenKind tokenKind) {
        Identifier identifier;
        Type type;
        Literal literal;
        type = parseTypeDenoter();
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
        if (tokenKind == TokenKind.VOID_TYPE) {
        } else {
            accept(TokenKind.SEND);
            switch (currentToken.kind) {
                case LETTER_LITERAL:
                case NUMBER_LITERAL:
                case STATE_LITERAL:
                    parseLiteral(tokenKind);
                    break;
                case IDENTIFIER:
                    parseIdentifier();
                    break;
                default:
                    break;
            }
            accept(TokenKind.SEMICOLON);
        }
        return new OprDeclaration(identifier, type);
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

    private void parseStatementList() {
        parseStatement();
        while (currentToken.kind == TokenKind.SEMICOLON) {
            acceptIt();
            parseStatement();
        }
    }

    private Statement parseStatement() {
        switch (currentToken.kind) {
            case IDENTIFIER -> {
                Expression expression = parseExpression();
                accept(TokenKind.SEMICOLON);
                ExpressionStatement expressionStatement = new ExpressionStatement(expression);
                return expressionStatement;
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
                acceptIt();
                accept(TokenKind.LEFT_PARAN);
                Expression expression = parseExpression();
                accept(TokenKind.RIGHT_PARAN);
                Block block = parseBlock();
                return new UntilStatement(expression, block);
            }
            case IN, OUT -> {
                acceptIt();
                accept(TokenKind.OPERATOR);
                Expression expression = parseExpression();
                // TODO implement OUT statement
                return new InStatement(expression);
            }
            default -> throw new UnsupportedOperationException("Token " + currentToken.spelling + " is currently not handled");
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
        if (currentToken.kind == TokenKind.OPERATOR) {
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
                } else {
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
        return null;
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
        literal = new Literal(currentToken);
        switch (currentToken.kind) {
            // Case for letter literal
            case IDENTIFIER:
                if (tokenKind == TokenKind.LETTER_TYPE) {
                    literal = new LetterLiteral(currentToken);
                    acceptIt();
                } else {
                    // report syntactic error
                }
                break;
            case NUMBER_LITERAL:
                if (tokenKind == TokenKind.NUMBER_TYPE) {
                    literal = new NumberLiteral(currentToken);
                    acceptIt();
                } else {
                    // report syntactic error
                }
                break;
            case FALSE:
            case TRUE:
                if (tokenKind == TokenKind.STATE_TYPE) {
                    literal = new StateLiteral(currentToken);
                    acceptIt();
                } else {
                    // report syntactic error
                }
                break;
            // TODO remove it if it works
//            case COL_TYPE:
//                acceptIt();
//                literal = parseCollectionLiteral();
//                break;
            default:

                break;
        }
        return literal;
    }

    private CollectionLiteral parseCollectionLiteral() {
        CollectionLiteral collectionLiteral = new CollectionLiteral();
        accept(TokenKind.LEFT_DIAMOND);
        switch (currentToken.kind) {
            case LETTER_LITERAL:
                collectionLiteral.addLiteral(parseLiteral(TokenKind.LETTER_TYPE));
                break;
            case NUMBER_LITERAL:
                collectionLiteral.addLiteral(parseLiteral(TokenKind.NUMBER_TYPE));
                break;
            case STATE_LITERAL:
                collectionLiteral.addLiteral(parseLiteral(TokenKind.STATE_TYPE));
                break;
            default:
                break;
        }
        while (currentToken.kind == TokenKind.COMMA) {
            acceptIt();
            switch (currentToken.kind) {
                case LETTER_LITERAL:
                    collectionLiteral.addLiteral(parseLiteral(TokenKind.LETTER_TYPE));
                    break;
                case NUMBER_LITERAL:
                    collectionLiteral.addLiteral(parseLiteral(TokenKind.NUMBER_TYPE));
                    break;
                case STATE_LITERAL:
                    collectionLiteral.addLiteral(parseLiteral(TokenKind.STATE_TYPE));
                    break;
                default:
                    break;
            }
        }
        accept(TokenKind.RIGHT_DIAMOND);
        return collectionLiteral;
    }

    private Type parseTypeDenoter() {
        Type type = null;
        switch (currentToken.kind) {
            case NUMBER_TYPE:
                type = new Number(currentToken);
                acceptIt();
                break;
            case LETTER_TYPE:
                type = new Letter(currentToken);
                acceptIt();
                break;
            case STATE_TYPE:
                type = new State(currentToken);
                acceptIt();
                break;
            case VOID_TYPE:
                type = new Void(currentToken);
                acceptIt();
                break;
            case COL_TYPE:
                type = new Collection(currentToken);
                acceptIt();
                break;
            default:
                // report syntactic error
                break;
        }
        return type;
    }

    private Identifier parseIdentifier() {
        if (currentToken.kind == TokenKind.IDENTIFIER) {
            Identifier identifier = new Identifier(currentToken);
            currentToken = scanner.scan();
            return identifier;
        } else {
            System.out.println(currentToken.spelling + " is not an identifier");
            return null;
        }
    }


}