package org.testlang.AST;

public class Declaration extends AST {

	public Identifier id;
	public Type t;
	public Declaration() {
		// TODO Auto-generated constructor stub
	}
	public Declaration(Identifier id, Type t) {
		this.id=id;
		this.t=t;
		toString();
	}
	
	@Override
	public String toString() {
		return "new " + this.id.getSpelling() + " " + this.t;
	}
}
