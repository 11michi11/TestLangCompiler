package org.testlang.AST;

import org.testlang.Token;

public class Terminal extends AST {

	public Terminal(Token token) {
		this.setSpelling(token.spelling);
	}
	private String spelling;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getSpelling();
		
	}
	public String getSpelling() {
		return spelling;
	}
	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}
}
