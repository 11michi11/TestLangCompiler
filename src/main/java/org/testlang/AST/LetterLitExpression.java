package org.testlang.AST;

import org.testlang.Visitor;

public class LetterLitExpression extends LiteralExpression {

    LetterLiteral literal;

    public LetterLitExpression(LetterLiteral literal) {
        this.literal = literal;
    }

    public LetterLiteral getLiteral() {
        return literal;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitLetterLitExpression(this, arg);
    }
}
