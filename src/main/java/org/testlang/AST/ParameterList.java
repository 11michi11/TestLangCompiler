package org.testlang.AST;

import org.testlang.Visitor;

public class ParameterList extends AST {

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitParameterList(this, arg);
    }
}
