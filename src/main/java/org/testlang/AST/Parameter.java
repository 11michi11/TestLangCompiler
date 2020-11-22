package org.testlang.AST;

import org.testlang.Visitor;

public class Parameter extends AST {

    public Parameter() {
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitParameter(this, arg);
    }
}
