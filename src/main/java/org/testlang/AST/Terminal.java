package org.testlang.AST;

import org.testlang.Token;

public class Terminal extends AST {

    private String spelling;

    public Terminal(Token token) {
        this.spelling = token.spelling;
    }

    public String getSpelling() {
        return spelling;
    }
}
