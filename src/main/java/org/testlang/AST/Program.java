package org.testlang.AST;


import org.testlang.Visitor;

public class Program extends AST {

    DeclarationList declarationList;
    StatementList statementList;

    public Program(DeclarationList declarationList, StatementList statementList) {
        this.declarationList = declarationList;
        this.statementList = statementList;
    }

    public DeclarationList getDeclarationList() {
        return declarationList;
    }

    public StatementList getStatementList() {
        return statementList;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitProgram(this, arg);
    }
}

