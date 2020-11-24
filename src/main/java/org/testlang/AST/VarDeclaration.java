package org.testlang.AST;

import org.testlang.codegen.Frame;
import org.testlang.Visitor;

public class VarDeclaration extends Declaration {

    private Frame frame;

    public VarDeclaration(Identifier id, TypeDenoter t) {
        super(id, t);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitVariableDeclaration(this, arg);
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Frame getFrame() {
        return frame;
    }
}
