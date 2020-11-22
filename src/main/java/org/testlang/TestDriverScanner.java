package org.testlang;


import org.testlang.AST.AST;
import org.testlang.AST.Program;

public class TestDriverScanner {

    private static final String EXAMPLES_DIR = "src\\main\\resources\\";


    public static void main(String[] args) {
        SourceFile in = new SourceFile(EXAMPLES_DIR + "loop.tl");
        Scanner scanner = new Scanner(in);
        Parser parser = new Parser(scanner);
        AST ast = parser.parse();
        new ASTViewer(ast);
        Checker checker = new Checker();
        checker.check((Program) ast);
    }
}