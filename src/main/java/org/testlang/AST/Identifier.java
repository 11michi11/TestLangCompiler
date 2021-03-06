package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Identifier extends Terminal {

	public Identifier(Token token) {
		super(token);
	}


    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitIdentifier(this, arg);
    }
}
