package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Number extends TypeDenoter {

    public Number(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumber(this, arg);
    }
}
