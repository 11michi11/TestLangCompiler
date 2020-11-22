package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class Letter extends TypeDenoter {

    public Letter(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitLetter(this, arg);
    }
}
