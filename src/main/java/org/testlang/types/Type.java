package org.testlang.types;

import org.testlang.AST.TypeDenoter;

public abstract class Type {

    private TypeDenoter typeDenoter;

    protected Type(TypeDenoter typeDenoter) {
        this.typeDenoter = typeDenoter;
    }

    @Override
    public boolean equals(Object obj) {
        // Treat Types as equal whenever they are objects of the same class
        return this.getClass() == obj.getClass();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public TypeDenoter getTypeDenoter() {
        return typeDenoter;
    }

    public void setTypeDenoter(TypeDenoter typeDenoter) {
        this.typeDenoter = typeDenoter;
    }
}

