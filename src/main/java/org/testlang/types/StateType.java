package org.testlang.types;

import org.testlang.AST.State;
import org.testlang.Token;
import org.testlang.TokenKind;

public class StateType extends Type {
    public StateType() {
        super(new State(new Token(TokenKind.STATE_TYPE, TokenKind.STATE_TYPE.getSpelling())));
    }
}
