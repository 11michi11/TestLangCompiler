package org.testlang.AST;

import org.testlang.Visitor;

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

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitIfStatement(this, arg);
    }
}
