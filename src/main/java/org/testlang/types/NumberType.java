package org.testlang.types;

import org.testlang.AST.Number;
import org.testlang.Token;
import org.testlang.TokenKind;

public class NumberType extends Type {
    public NumberType() {
        super(new Number(new Token(TokenKind.NUMBER_TYPE, TokenKind.NUMBER_TYPE.getSpelling())));
    }
}
