package org.testlang.AST;

import org.testlang.Visitor;

public class CollectionLitExpression extends LiteralExpression {

    CollectionLiteral literal;

    public CollectionLitExpression(CollectionLiteral literal) {
        this.literal = literal;
    }

    public CollectionLiteral getLiteral() {
        return literal;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCollectionLitExpression(this, arg);
    }
}
