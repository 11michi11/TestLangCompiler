package org.testlang.AST;

public class VarExpression extends Expression {

    private Identifier identifier;

    public VarExpression(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
