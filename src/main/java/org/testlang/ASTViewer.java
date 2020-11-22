package org.testlang;


import org.testlang.AST.*;
import org.testlang.AST.Number;
import org.testlang.AST.Void;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;


public class ASTViewer
        extends JFrame {
    private static final Font NODE_FONT = new Font("Verdana", Font.PLAIN, 24);


    public ASTViewer(AST ast) {
        super("Abstract Syntax Tree");

        DefaultMutableTreeNode root = createTree(ast);
        JTree tree = new JTree(root);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setFont(NODE_FONT);
        tree.setCellRenderer(renderer);

        add(new JScrollPane(tree));

        setSize(1024, 768);
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    private DefaultMutableTreeNode createTree(AST ast) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("*** WHAT??? ***");
        System.out.println("AST viewer: " + ast.getClass());
        if (ast == null)
            node.setUserObject("*** NULL ***");
        else if (ast instanceof Program) {
            node.setUserObject("Program");
            node.add(createTree(((Program) ast).getDeclarationList()));
            node.add(createTree(((Program) ast).getStatementList()));
        } else if (ast instanceof Block) {
            node.setUserObject("Block");
            node.add(createTree(((Block) ast).getDeclarationList()));
            node.add(createTree(((Block) ast).getStatementList()));
        } else if (ast instanceof DeclarationList) {
            node.setUserObject("DeclarationList");

            for (Declaration d : ((DeclarationList) ast).getDeclarationList())
                node.add(createTree(d));
        } else if (ast instanceof VarDeclaration) {
            node.setUserObject("VarDeclaration");
            node.add(createTree(((VarDeclaration) ast).getIdentifier()));
            node.add(createTree(((VarDeclaration) ast).getType()));
        } else if (ast instanceof Number) {
            node.setUserObject("Number");
        } else if (ast instanceof State) {
            node.setUserObject("State");
        } else if (ast instanceof Collection) {
            var collection = ((Collection) ast);
            node.setUserObject("Collection<" + collection.getCollectionType().getSpelling() + ">{" + collection.getSize().getSpelling() + "}");
        } else if (ast instanceof Letter) {
            node.setUserObject("Letter");
        } else if (ast instanceof Void) {
            node.setUserObject("Void");
        } else if (ast instanceof OprDeclaration) {
            node.setUserObject("FunctionDeclaration");
            node.add(createTree(((OprDeclaration) ast).getIdentifier()));
            node.add(createTree(((OprDeclaration) ast).getType()));
//			node.add( createTree( ((FunctionDeclaration)ast).params ) );
//			node.add( createTree( ((FunctionDeclaration)ast).block ) );
//			node.add( createTree( ((FunctionDeclaration)ast).retExp ) );
        } else if (ast instanceof StatementList) {
            node.setUserObject("StatementList");

            for (Statement s : ((StatementList) ast).getStatementList())
                node.add(createTree(s));
        } else if (ast instanceof ExpressionStatement) {
            node.setUserObject("ExpressionStatement");
            node.add(createTree(((ExpressionStatement) ast).getExpression()));
        } else if (ast instanceof IfStatement) {
            node.setUserObject("IfStatement");
            node.add(createTree(((IfStatement) ast).getExpression()));
            node.add(createTree(((IfStatement) ast).getBlock()));
        } else if (ast instanceof UntilStatement) {
            node.setUserObject("UntilStatement");
//			node.add( createTree( ((UntilStatement)ast).exp ) );
//			node.add( createTree( ((UntilStatement)ast).stats ) );
        } else if (ast instanceof OutStatement) {
            node.setUserObject("OutStatement");
            node.add(createTree(((OutStatement) ast).getExpression()));
        } else if (ast instanceof BinaryExpression) {
            node.setUserObject("BinaryExpression");
            node.add(createTree(((BinaryExpression) ast).getOperator()));
            node.add(createTree(((BinaryExpression) ast).getOperand1()));
            node.add(createTree(((BinaryExpression) ast).getOperand2()));
        } else if (ast instanceof CallExpression) {
            node.setUserObject("CallExpression");
            node.add(createTree(((CallExpression) ast).getIdentifier()));
            node.add(createTree(((CallExpression) ast).getParameterList()));
        } else if (ast instanceof UnaryExpression) {
            node.setUserObject("UnaryExpression");
            node.add(createTree(((UnaryExpression) ast).getOperator()));
            node.add(createTree(((UnaryExpression) ast).getExpression()));
        } else if (ast instanceof VarExpression) {
            node.setUserObject("VarExpression");
            node.add(createTree(((VarExpression) ast).getIdentifier()));
        } else if (ast instanceof NumberLitExpression) {
            node.setUserObject("NumberLitExpression");
            node.add(createTree(((NumberLitExpression) ast).getLiteral()));
        } else if (ast instanceof StateLitExpression) {
            node.setUserObject("StateLitExpression");
            node.add(createTree(((StateLitExpression) ast).getLiteral()));
        } else if (ast instanceof LetterLitExpression) {
            node.setUserObject("LetterLitExpression");
            node.add(createTree(((LetterLitExpression) ast).getLiteral()));
        } else if (ast instanceof CollectionLitExpression) {
            node.setUserObject("CollectionLitExpression");
            node.add(createTree(((CollectionLitExpression) ast).getLiteral()));
        } else if (ast instanceof NumberLiteral) {
            node.setUserObject("NumberLiteral " + ((NumberLiteral) ast).getSpelling());
        } else if (ast instanceof LetterLiteral) {
            node.setUserObject("LetterLiteral " + ((LetterLiteral) ast).getSpelling());
        } else if (ast instanceof StateLiteral) {
            node.setUserObject("StateLiteral " + ((StateLiteral) ast).getSpelling());
        } else if (ast instanceof CollectionLiteral) {
            node.setUserObject("CollectionLiteral");

            for (Literal d : ((CollectionLiteral) ast).getLiteralList())
                node.add(createTree(d));
        } else if (ast instanceof Identifier) {
            node.setUserObject("Identifier " + ((Identifier) ast).getSpelling());
        } else if (ast instanceof Operator) {
            node.setUserObject("Operator " + ((Operator) ast).getSpelling());
        }

        return node;
    }
}