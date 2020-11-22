package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class StateLiteral extends Literal {

    public StateLiteral(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitStateLiteral(this, arg);
    }
}
