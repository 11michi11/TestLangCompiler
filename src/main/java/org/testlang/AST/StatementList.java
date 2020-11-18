package org.testlang.AST;

import java.util.ArrayList;
import java.util.List;

public class StatementList extends AST {

	List<Statement> sl;
	public StatementList() {
		sl = new ArrayList<>();
	}

	

	

	public void add(Statement s) {
		System.out.println("new declaration " + s.toString());
		this.sl.add(s);
	}
	
	public DeclarationList getDl() {
		return (DeclarationList) this.sl;
	}
}
