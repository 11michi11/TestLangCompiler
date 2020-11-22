package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Operator extends Terminal {

    public Operator(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitOperator(this, arg);
    }
}
