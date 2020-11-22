package org.testlang.AST;

import org.testlang.Visitor;

import java.util.ArrayList;
import java.util.List;

public class DeclarationList extends AST {

    List<Declaration> declarationList;

    public DeclarationList() {
        declarationList = new ArrayList<>();
    }

    public void add(Declaration d) {
        this.declarationList.add(d);
    }

    public List<Declaration> getDeclarationList() {
        return this.declarationList;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitDeclarationList(this, arg);
    }
}
