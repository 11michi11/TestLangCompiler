package org.testlang.AST;

public class LetterLitExpression extends LiteralExpression {

    LetterLiteral literal;

    public LetterLitExpression(LetterLiteral literal) {
        this.literal = literal;
    }

    public LetterLiteral getLiteral() {
        return literal;
    }
}
