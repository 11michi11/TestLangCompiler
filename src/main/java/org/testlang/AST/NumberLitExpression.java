package org.testlang.AST;

public class NumberLitExpression extends LiteralExpression {

    private NumberLiteral literal;

    public NumberLitExpression(NumberLiteral literal) {
        this.literal = literal;
    }

    public NumberLiteral getLiteral() {
        return literal;
    }
}
