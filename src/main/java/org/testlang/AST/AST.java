package org.testlang.AST;

import org.testlang.Visitor;

public abstract class AST {

    public abstract Object visit(Visitor visitor, Object arg);
}
