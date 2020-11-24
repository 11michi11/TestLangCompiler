package org.testlang.AST;

import org.testlang.Visitor;
import org.testlang.codegen.RuntimeEntity;

public abstract class AST {

    private RuntimeEntity entity;

    public abstract Object visit(Visitor visitor, Object arg);

    public RuntimeEntity getEntity() {
        return entity;
    }

    public void setEntity(RuntimeEntity entity) {
        this.entity = entity;
    }
}
