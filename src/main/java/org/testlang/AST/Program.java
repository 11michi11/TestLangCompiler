package org.testlang.AST;


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
}

