package org.testlang.AST;


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
}
