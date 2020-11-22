package org.testlang.AST;

import org.testlang.Token;

public abstract class Literal extends Terminal {

    public Literal(Token token) {
        super(token);
    }

}
