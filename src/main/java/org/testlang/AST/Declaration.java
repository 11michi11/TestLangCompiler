package org.testlang.AST;

public abstract class Declaration extends AST {

    private Identifier identifier;
    private TypeDenoter typeDenoter;

    public Declaration(Identifier identifier, TypeDenoter typeDenoter) {
        this.identifier = identifier;
        this.typeDenoter = typeDenoter;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public TypeDenoter getType() {
        return typeDenoter;
    }
}
