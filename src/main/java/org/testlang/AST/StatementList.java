package org.testlang.AST;

import org.testlang.Visitor;

import java.util.ArrayList;
import java.util.List;

public class StatementList extends AST {

    public List<Statement> getStatementList() {
        return statementList;
    }

    List<Statement> statementList;

    public StatementList() {
        statementList = new ArrayList<>();
    }


    public void add(Statement s) {
        this.statementList.add(s);
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitStatementList(this, arg);
    }
}
