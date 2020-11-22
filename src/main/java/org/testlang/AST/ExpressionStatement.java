package org.testlang.AST;

import org.testlang.Visitor;

public class ExpressionStatement extends Statement {

    private Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitExpressionStatement(this, arg);
    }
}
