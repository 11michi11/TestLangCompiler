package org.testlang;

import static org.testlang.TokenKind.*;


public class Token {

    public TokenKind kind;
    public String spelling;


    public Token(TokenKind kind, String spelling) {
        this.kind = kind;
        this.spelling = spelling;

        if (kind == IDENTIFIER)
            for (TokenKind tk : KEYWORDS) {
                if (spelling.equals(tk.getSpelling())) {
                    this.kind = tk;
                    break;
                }
            }
    }


    public boolean isAssignOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, ASSIGNOPS);
        else
            return false;
    }

    public boolean isAddOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, ADDOPS);
        else
            return false;
    }

    public boolean isMulOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, MULOPS);
        else
            return false;
    }


    private boolean containsOperator(String spelling, String OPS[]) {
        for (String op : OPS) {
            if (spelling.equals(op))
                return true;
        }
        return false;
    }


    private static final TokenKind[] KEYWORDS = {OPR, IF, SEND, OUT, IN, UNTIL};


    private static final String ASSIGNOPS[] =
            {
                    ":=",
            };

    private static final String ADDOPS[] =
            {
                    "+",
                    "-",
            };

    private static final String MULOPS[] =
            {
                    "*",
                    "/",
            };
}