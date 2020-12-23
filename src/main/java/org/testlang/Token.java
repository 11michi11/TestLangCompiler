package org.testlang;

import static org.testlang.TokenKind.*;


public class Token {

    public TokenKind kind;
    public String spelling;


    public Token(TokenKind kind, String spelling) {
        this.kind = kind;
        this.spelling = spelling;

        if (kind == IDENTIFIER) {
            for (TokenKind tk : KEYWORDS) {
                if (spelling.equals(tk.getSpelling())) {
                    this.kind = tk;
                    break;
                }
            }
        }
    }

    public boolean isAssignOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, ASSIGN_OPS);
        else
            return false;
    }

    public boolean isAddOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, ADD_OPS);
        else
            return false;
    }

    public boolean isMulOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, MUL_OPS);
        else
            return false;
    }

    public boolean isBoolOperator() {
        // Check spelling without checking if it's token to include < and > as those are not only operators
        return containsOperator(spelling, BOOL_OPS);
    }

    public boolean isArrOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, ARRAY_OPS);
        else
            return false;
    }

    public boolean isIOOperator() {
        if (kind == OPERATOR)
            return containsOperator(spelling, IO_OPS);
        else
            return false;
    }
    
    private boolean containsOperator(String spelling, String[] OPS) {
        for (String op : OPS) {
            if (spelling.equals(op))
                return true;
        }
        return false;
    }

    private static final TokenKind[] KEYWORDS = {OPR, VAR, SEND, OUT, IN, UNTIL, TRUE, FALSE, NUMBER_TYPE,
            LETTER_TYPE, STATE_TYPE, COL_TYPE, VOID_TYPE};

    // TODO Check if those are all operators
    private static final String[] ASSIGN_OPS =
            {
                    "=",
            };

    private static final String[] ADD_OPS =
            {
                    "+",
                    "-",
            };

    private static final String[] MUL_OPS =
            {
                    "*",
                    "/",
            };

    private static final String[] BOOL_OPS =
            {
                    "&",
                    "|",
                    "!",
                    "==",
                    ">",
                    "<"
            };

    private static final String[] ARRAY_OPS =
            {
                    "<<",
                    ">>",
                    "//",
                    "--"
            };

    private static final String[] IO_OPS =
            {
                    "<=",
                    "=>"
            };




}