package org.testlang.AST;


public class Program extends AST {

	public Program(DeclarationList dl2) {
		setDl(dl2);
	}
	DeclarationList dl;
	public void setDl(DeclarationList dl) {
		this.dl = dl;
	}
	
	public DeclarationList getDl() {
		return dl;
	}
}

