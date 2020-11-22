package org.testlang.AST;

import org.testlang.Token;
import org.testlang.Visitor;

public class LetterLiteral extends Literal {

    public LetterLiteral(Token token) {
        super(token);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitLetterLiteral(this, arg);
    }
}
