package org.testlang.types;

public abstract class Type {

    @Override
    public boolean equals(Object obj) {
        // Treat Types as equal whenever they are objects of the same class
        return this.getClass() == obj.getClass();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

