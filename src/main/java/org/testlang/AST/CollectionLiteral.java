package org.testlang.AST;


import org.testlang.Visitor;

import java.util.ArrayList;
import java.util.List;

public class CollectionLiteral extends AST {

	List<Literal> literalList;

	public CollectionLiteral() {
        literalList = new ArrayList<>();
    }

    public List<Literal> getLiteralList() {
        return literalList;
    }

    public void addLiteral(Literal literal) {
        literalList.add(literal);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCollectionLiteral(this, arg);
    }
}
