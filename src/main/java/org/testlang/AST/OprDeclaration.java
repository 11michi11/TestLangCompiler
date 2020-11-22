package org.testlang.AST;


import org.testlang.Visitor;

public class OprDeclaration extends Declaration {

    public OprDeclaration(Identifier id, TypeDenoter t) {
        super(id, t);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitOperationDeclaration(this, arg);
    }
}
