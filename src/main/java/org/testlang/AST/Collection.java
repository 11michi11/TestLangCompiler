package org.testlang.AST;

import org.testlang.Token;

public class Collection extends Type {

	private Type collectionType;
	private NumberLiteral size;

	public Collection(Token token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public Type getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(Type collectionType) {
		this.collectionType = collectionType;
	}

	public NumberLiteral getSize() {
		return size;
	}

	public void setSize(NumberLiteral size) {
		this.size = size;
	}
}
