package org.testlang;


public enum TokenKind {
    IDENTIFIER,
    OPERATOR,

    NUMBER_LITERAL,
    LETTER_LITERAL,
    STATE_LITERAL,

    NUMBER_TYPE("number"),
    LETTER_TYPE("letter"),
    STATE_TYPE("state"),
    COL_TYPE("col"),
    VOID_TYPE("void"),

    OPR("opr"),
    VAR("var"),
    IF("?"),
    SEND("send"),
    UNTIL("until"),

    TRUE("true"),
    FALSE("false"),

    OUT("out"),
    IN("in"),

    LEFT_PARAN("("),
    RIGHT_PARAN(")"),
    LEFT_SQUARE("["),
    RIGHT_SQUARE("]"),
    LEFT_CURLY("{"),
    RIGHT_CURLY("}"),
    LEFT_DIAMOND("<"),
    RIGHT_DIAMOND(">"),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    APOSTROPHE("`"),
    SINGLE_QUOTE("'"),
    EXCLAMATION("!"),
    EOT,

    ERROR;


    private String spelling = null;


    private TokenKind() {
    }


    TokenKind(String spelling) {
        this.spelling = spelling;
    }


    public String getSpelling() {
        return spelling;
    }
}