package org.testlang;


import org.testlang.AST.AST;
import org.testlang.AST.Program;
import org.testlang.codegen.Encoder;

public class TestDriverScanner {

    private static final String EXAMPLES_DIR = "src\\main\\resources\\";


    public static void main(String[] args) {
        String sourceName = "loop.tl";
        SourceFile in = new SourceFile(EXAMPLES_DIR + sourceName);
        Scanner scanner = new Scanner(in);
        Parser parser = new Parser(scanner);
        AST ast = parser.parse();
//        new ASTViewer(ast);
        Checker checker = new Checker();
        checker.check((Program) ast);
        Encoder encoder = new Encoder();
        encoder.encode((Program) ast);
        String targetName;
        if (sourceName.endsWith(".tl")) {
            targetName = sourceName.substring(0, sourceName.length() - 3) + ".tam";
        } else {
            targetName = sourceName + ".tam";
        }
        encoder.saveTargetProgram(targetName);
        System.out.println("Compilation successful");
    }
}