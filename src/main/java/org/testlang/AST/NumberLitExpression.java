package org.testlang.AST;

import org.testlang.Visitor;

public class NumberLitExpression extends LiteralExpression {

    private NumberLiteral literal;

    public NumberLitExpression(NumberLiteral literal) {
        this.literal = literal;
    }

    public NumberLiteral getLiteral() {
        return literal;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumberLitExpression(this, arg);
    }
}
