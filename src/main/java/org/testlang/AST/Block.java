package org.testlang.AST;

import org.testlang.Visitor;

public class Block extends AST {

    private DeclarationList declarationList;
    private StatementList statementList;

    public Block(DeclarationList declarationList, StatementList statementList) {
        this.declarationList = declarationList;
        this.statementList = statementList;
    }

    public DeclarationList getDeclarationList() {
        return declarationList;
    }

    public StatementList getStatementList() {
        return statementList;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitBlock(this, arg);
    }
}
