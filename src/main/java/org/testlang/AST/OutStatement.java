package org.testlang.AST;

public class OutStatement extends Statement {

	private Expression expression;

	public OutStatement(Expression expression) {
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}
}
