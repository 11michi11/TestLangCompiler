package org.testlang.AST;

import java.util.ArrayList;
import java.util.List;

public class DeclarationList extends AST {
	
	List<Declaration> dl;
	public DeclarationList() {
		dl = new ArrayList<>();
	}

	public void add(Declaration d) {
		System.out.println("new declaration " + d.toString());
		this.dl.add(d);
	}
	
	public DeclarationList getDl() {
		return (DeclarationList) this.dl;
	}
}
