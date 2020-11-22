package org.testlang.AST;

import org.testlang.Visitor;

public class OutStatement extends Statement {

    private Expression expression;

    public OutStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitOutStatement(this, arg);
    }
}
