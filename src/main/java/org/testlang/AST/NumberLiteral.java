package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class NumberLiteral extends Literal {

    public NumberLiteral(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumberLiteral(this, arg);
    }
}
