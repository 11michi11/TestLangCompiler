package org.testlang.AST;

import org.testlang.Visitor;

public class StateLitExpression extends LiteralExpression {

    StateLiteral literal;

    public StateLitExpression(StateLiteral literal) {
        this.literal = literal;
    }

    public StateLiteral getLiteral() {
        return literal;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitStateLitExpression(this, arg);
    }
}
