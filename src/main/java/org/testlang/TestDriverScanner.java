package org.testlang;


public class TestDriverScanner {

    private static final String EXAMPLES_DIR = "src\\main\\resources\\";


    public static void main(String[] args) {
        SourceFile in = new SourceFile(EXAMPLES_DIR + "loop.tl");
        Scanner s = new Scanner(in);
        
        Token t = s.scan();
        while (t.kind != TokenKind.EOT) {
            System.out.println(t.kind + " " + t.spelling);

            t = s.scan();
        }
    }
}