package org.testlang;

import org.testlang.AST.*;
import org.testlang.AST.Number;
import org.testlang.AST.Void;

public interface Visitor {


    // TODO add more

    public Object visitProgram(Program program, Object arg);

    public Object visitBlock(Block block, Object arg);

    public Object visitDeclarationList(DeclarationList declarationList, Object arg);

    public Object visitVariableDeclaration(VarDeclaration varDeclaration, Object arg);

    public Object visitOperationDeclaration(OprDeclaration oprDeclaration, Object arg);

    public Object visitStatementList(StatementList statementList, Object arg);

    public Object visitExpressionStatement(ExpressionStatement expressionStatement, Object arg);

    public Object visitIfStatement(IfStatement ifStatement, Object arg);

    public Object visitUntilStatement(UntilStatement untilStatement, Object arg);

    public Object visitOutStatement(OutStatement outStatement, Object arg);

    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg);

    public Object visitVarExpression(VarExpression varExpression, Object arg);

    public Object visitCallExpression(CallExpression callExpression, Object arg);

    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg);

    public Object visitNumberLitExpression(NumberLitExpression numberLitExpression, Object arg);

    public Object visitIdentifier(Identifier identifier, Object arg);

    public Object visitNumberLiteral(NumberLiteral numberLiteral, Object arg);

    public Object visitOperator(Operator operator, Object arg);

    public Object visitParameterList(ParameterList parameterList, Object arg);

    public Object visitLetterLitExpression(LetterLitExpression letterLitExpression, Object arg);

    public Object visitParameter(Parameter parameter, Object arg);

    public Object visitStateLitExpression(StateLitExpression stateLitExpression, Object arg);

    public Object visitCollectionLiteral(CollectionLiteral collectionLiteral, Object arg);

    public Object visitCollectionLitExpression(CollectionLitExpression collectionLitExpression, Object arg);

    public Object visitStateLiteral(StateLiteral stateLiteral, Object arg);

    public Object visitLetterLiteral(LetterLiteral letterLiteral, Object arg);

    public Object visitCollection(Collection collection, Object arg);

    public Object visitState(State state, Object arg);

    public Object visitVoid(Void aVoid, Object arg);

    public Object visitInStatement(InStatement inStatement, Object arg);

    public Object visitLetter(Letter letter, Object arg);

    public Object visitNumber(Number number, Object arg);
}
