package org.testlang.types;

import org.testlang.AST.Letter;
import org.testlang.Token;
import org.testlang.TokenKind;

public class LetterType extends Type {
    public LetterType() {
        super(new Letter(new Token(TokenKind.LETTER_TYPE, TokenKind.LETTER_TYPE.getSpelling())));
    }
}
