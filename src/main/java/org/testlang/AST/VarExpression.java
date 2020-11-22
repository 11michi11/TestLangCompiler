package org.testlang.AST;

import org.testlang.Visitor;

public class VarExpression extends Expression {

    private Identifier identifier;
    private VarDeclaration declaration;

    public VarExpression(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitVarExpression(this, arg);
    }

    public VarDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(VarDeclaration declaration) {
        this.declaration = declaration;
    }
}
