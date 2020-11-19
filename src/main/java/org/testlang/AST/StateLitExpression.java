package org.testlang.AST;

public class StateLitExpression extends LiteralExpression {

    StateLiteral literal;

    public StateLitExpression(StateLiteral literal) {
        this.literal = literal;
    }

    public StateLiteral getLiteral() {
        return literal;
    }
}
