package org.testlang.AST;

public abstract class Declaration extends AST {

    public Identifier identifier;
    public Type type;

    public Declaration(Identifier identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Type getType() {
        return type;
    }
}
