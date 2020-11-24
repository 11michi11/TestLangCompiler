package org.testlang.types;

import org.testlang.AST.Void;
import org.testlang.Token;
import org.testlang.TokenKind;

public class VoidType extends Type {
    public VoidType() {
        super(new Void(new Token(TokenKind.VOID_TYPE, TokenKind.VOID_TYPE.getSpelling())));
    }
}
