package org.testlang.AST;


import org.testlang.Visitor;

import java.util.ArrayList;
import java.util.List;

public class CollectionLiteral extends AST {

	List<LiteralExpression> literalList;

	public CollectionLiteral() {
        literalList = new ArrayList<>();
    }

    public List<LiteralExpression> getLiteralList() {
        return literalList;
    }

    public void addLiteral(LiteralExpression literal) {
        literalList.add(literal);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCollectionLiteral(this, arg);
    }
}
