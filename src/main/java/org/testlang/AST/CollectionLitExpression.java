package org.testlang.AST;

public class CollectionLitExpression extends LiteralExpression {

    CollectionLiteral literal;

    public CollectionLitExpression(CollectionLiteral literal) {
        this.literal = literal;
    }

    public CollectionLiteral getLiteral() {
        return literal;
    }
}
