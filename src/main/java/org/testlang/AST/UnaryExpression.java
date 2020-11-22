package org.testlang.AST;

import org.testlang.Visitor;

public class UnaryExpression extends Expression {

    private Operator operator;
    private Expression expression;

    public UnaryExpression(Operator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitUnaryExpression(this, arg);
    }
}
