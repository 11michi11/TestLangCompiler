package org.testlang.AST;

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
//		System.out.println("new statement " + s.toString());
        this.statementList.add(s);
    }
}
