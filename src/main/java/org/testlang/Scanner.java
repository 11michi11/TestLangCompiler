package org.testlang;

import static org.testlang.TokenKind.*;


public class Scanner {
    private SourceFile source;

    private char currentChar;
    private StringBuffer currentSpelling = new StringBuffer();


    public Scanner(SourceFile source) {
        this.source = source;

        currentChar = source.getSource();
    }


    private void takeIt() {
        currentSpelling.append(currentChar);
        currentChar = source.getSource();
    }


    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }


    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    private void scanSeparator() {
        switch (currentChar) {
            case '#' -> {
                takeIt();
                while (currentChar != SourceFile.EOL && currentChar != SourceFile.EOT)
                    takeIt();
                if (currentChar == SourceFile.EOL)
                    takeIt();
            }
            case ' ', '\n', '\r', '\t' -> takeIt();
        }
    }


    private TokenKind scanToken() {
        if (isLetter(currentChar)) {
            takeIt();
            while (isLetter(currentChar) || isDigit(currentChar))
                takeIt();

            return IDENTIFIER;

        } else if (isDigit(currentChar)) {
            takeIt();
            while (isDigit(currentChar))
                takeIt();

            return NUMBER_LITERAL;

        }
        switch (currentChar) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
                takeIt();
                return OPERATOR;

            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return OPERATOR;
                } else
                    return ERROR;

            case ',':
                takeIt();
                return COMMA;

            case ';':
                takeIt();
                return SEMICOLON;

            case '(':
                takeIt();
                return LEFT_PARAN;

            case ')':
                takeIt();
                return RIGHT_PARAN;

            case SourceFile.EOT:
                return EOT;

            default:
                takeIt();
                return ERROR;
        }
    }


    public Token scan() {
        while (currentChar == '#' || currentChar == '\n' || currentChar == '\r'
                || currentChar == '\t' || currentChar == ' ') {
            scanSeparator();
        }

        currentSpelling = new StringBuffer();
        TokenKind kind = scanToken();

        return new Token(kind, new String(currentSpelling));
    }
}
