package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Collection extends TypeDenoter {

    private TypeDenoter collectionTypeDenoter;
    private NumberLiteral size;

    public Collection(Token token) {
        super(token);
    }

    public Collection(Token token, TypeDenoter collectionTypeDenoter, NumberLiteral size) {
        super(token);
        this.collectionTypeDenoter = collectionTypeDenoter;
        this.size = size;
    }

    public TypeDenoter getCollectionType() {
        return collectionTypeDenoter;
    }

    public void setCollectionType(TypeDenoter collectionTypeDenoter) {
        this.collectionTypeDenoter = collectionTypeDenoter;
    }

    public NumberLiteral getSize() {
        return size;
    }

    public void setSize(NumberLiteral size) {
        this.size = size;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCollection(this, arg);
    }
}
