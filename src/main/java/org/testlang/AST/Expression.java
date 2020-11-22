package org.testlang.AST;

import org.testlang.types.Type;

public abstract class Expression extends AST {

    private Type type;

    public Expression() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
