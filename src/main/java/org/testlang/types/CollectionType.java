package org.testlang.types;

import org.testlang.AST.Collection;
import org.testlang.AST.NumberLiteral;
import org.testlang.Token;
import org.testlang.TokenKind;

import java.util.Objects;

public class CollectionType extends Type {
    private int size;
    private Type collectionType;

    public CollectionType(int size, Type collectionType) {
        super(new Collection(new Token(TokenKind.COL_TYPE,TokenKind.COL_TYPE.getSpelling()),
                collectionType.getTypeDenoter(),
                new NumberLiteral(new Token(TokenKind.NUMBER_LITERAL,Integer.toString(size)))));
        this.size = size;
        this.collectionType = collectionType;
    }

    public int getSize() {
        return size;
    }

    public Type getCollectionType() {
        return collectionType;
    }

    @Override
    public String toString() {
        return "CollectionType{" +
                "size=" + size +
                ", collectionType=" + collectionType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionType that = (CollectionType) o;

        if (size != that.size) return false;
        return Objects.equals(collectionType, that.collectionType);
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + (collectionType != null ? collectionType.hashCode() : 0);
        return result;
    }
}
