package org.testlang.AST;

import java.util.ArrayList;
import java.util.List;

public class DeclarationList extends AST {

    List<Declaration> declarationList;

    public DeclarationList() {
        declarationList = new ArrayList<>();
    }

    public void add(Declaration d) {
//		System.out.println("new declaration " + d.toString());
        this.declarationList.add(d);
    }

    public List<Declaration> getDeclarationList() {
        return this.declarationList;
    }
}
