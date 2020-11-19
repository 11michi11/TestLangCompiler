package org.testlang.AST;

public class IfStatement extends Statement {

    private Expression expression;
    private Block block;

    public IfStatement(Expression expression, Block block) {
        this.expression = expression;
        this.block = block;
    }

    public Expression getExpression() {
        return expression;
    }

    public Block getBlock() {
        return block;
    }
}
