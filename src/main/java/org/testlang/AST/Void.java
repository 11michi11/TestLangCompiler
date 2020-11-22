package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Void extends TypeDenoter {

    public Void(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitVoid(this, arg);
    }
}
