package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class State extends TypeDenoter {

    public State(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitState(this, arg);
    }
}
