package org.testlang.AST;

import org.testlang.Visitor;

public class CallExpression extends Expression {

    private Identifier identifier;
    private ParameterList parameterList;

    public CallExpression(Identifier identifier, ParameterList parameterList) {
        this.identifier = identifier;
        this.parameterList = parameterList;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCallExpression(this, arg);
    }
}
