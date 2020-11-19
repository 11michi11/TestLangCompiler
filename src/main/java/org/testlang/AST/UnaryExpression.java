package org.testlang.AST;

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
}
