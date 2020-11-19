package org.testlang.AST;

import org.testlang.Token;

public class Collection extends Type {

	private Type collectionType;

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
}
