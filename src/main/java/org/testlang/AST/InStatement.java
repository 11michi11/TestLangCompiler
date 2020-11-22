package org.testlang.AST;

import org.testlang.Visitor;

public class InStatement extends Statement {

    private final Expression expression;

    public InStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitInStatement(this, arg);
    }
}
