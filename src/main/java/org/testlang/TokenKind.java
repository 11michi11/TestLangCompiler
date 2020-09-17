package org.testlang;


public enum TokenKind {
    IDENTIFIER,
    OPERATOR,

    NUMBER_LITERAL,
    LETTER_LITERAL,
    STATE_LITERAL,
    TYPE,

    OPR("opr"),
    IF("?"),
    SEND("send"),
    UNTIL("until"),

    OUT("out"),
    IN("in"),

    LEFT_PARAN("("),
    RIGHT_PARAN(")"),
    LEFT_SQUARE("["),
    RIGHT_SQUARE("]"),
    LEFT_CURLY("{"),
    RIGHT_CURLY("}"),
    LEFT_DIAMOND("<"),
    RIGHT_DIAMON(">"),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    EOT,

    ERROR;


    private String spelling = null;


    private TokenKind() {
    }


    private TokenKind(String spelling) {
        this.spelling = spelling;
    }


    public String getSpelling() {
        return spelling;
    }
}