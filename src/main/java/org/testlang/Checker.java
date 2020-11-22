package org.testlang;


import org.testlang.AST.Number;
import org.testlang.AST.Void;
import org.testlang.AST.*;
import org.testlang.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Checker implements Visitor {

    private final IdentificationTable idTable = new IdentificationTable();
    private final List<String> UNARY_OPERATORS = Collections.unmodifiableList(List.of("//", "`", "--", "!"));

    public void check(Program program) {
        program.visit(this, null);
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        idTable.openScope();
        program.getDeclarationList().visit(this, null);
        program.getStatementList().visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitDeclarationList(DeclarationList declarationList, Object arg) {
        for (Declaration declaration : declarationList.getDeclarationList()) {
            declaration.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitVariableDeclaration(VarDeclaration varDeclaration, Object arg) {
        String id = (String) varDeclaration.getIdentifier().visit(this, null);
        idTable.enter(id, varDeclaration);
        return null;
    }

    @Override
    public Object visitOperationDeclaration(OprDeclaration oprDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        idTable.openScope();
        block.getDeclarationList().visit(this, null);
        block.getStatementList().visit(this, null);
        idTable.closeScope();
        return null;
    }

    @Override
    public Object visitStatementList(StatementList statementList, Object arg) {
        for (Statement statement : statementList.getStatementList()) {
            statement.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitExpressionStatement(ExpressionStatement expressionStatement, Object arg) {
        expressionStatement.getExpression().visit(this, null);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        Type type = (Type) ifStatement.getExpression().visit(this, null);
        if (!(type instanceof StateType)) {
            throw new IllegalArgumentException("Condition expression in IfStatement must be of State type");
        }
        ifStatement.getBlock().visit(this, null);
        return null;
    }

    @Override
    public Object visitUntilStatement(UntilStatement untilStatement, Object arg) {
        untilStatement.getExpression().visit(this, null);
        untilStatement.getBlock().visit(this, null);
        return null;
    }

    @Override
    public Object visitOutStatement(OutStatement outStatement, Object arg) {
        outStatement.getExpression().visit(this, arg);
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        Type operand1Type = (Type) binaryExpression.getOperand1().visit(this, null);
        Type operand2Type = (Type) binaryExpression.getOperand2().visit(this, null);
        String operator = (String) binaryExpression.getOperator().visit(this, null);

        if (operator.equals("=") && !(binaryExpression.getOperand1() instanceof VarExpression)) {
            throw new IllegalArgumentException("Left side of assign operator must be a variable");
        }
        if (!operator.equals("<<") && !operator.equals(">>") && !operand1Type.equals(operand2Type)) {
            throw new IllegalArgumentException("Both sides of " + operator + " must be the same type. "
                    + operand1Type.toString() + " != " + operand2Type.toString());
        }

        if (operator.equals("<<") || operator.equals(">>")) {
            if (operand1Type instanceof CollectionType) {
                throw new IllegalArgumentException("Left side of " + operator + " operator must be a collection");
            }
            Collection collectionTypeDenoter = (Collection) ((VarExpression) binaryExpression.getOperand1()).getDeclaration().getType();
            Type collectionType = (Type) collectionTypeDenoter.getCollectionType().visit(this, null);

            if (collectionType != operand2Type) {
                throw new IllegalArgumentException("Right side of " + operator + " operator must be the same as collection type."
                        + collectionType.toString() + " != " + operand2Type.toString());
            }
        }

        if (operator.equals("==")) {
            binaryExpression.setType(new StateType());
        } else {
            binaryExpression.setType(operand1Type);
        }
        return binaryExpression.getType();
    }

    @Override
    public Object visitVarExpression(VarExpression varExpression, Object arg) {
        String id = (String) varExpression.getIdentifier().visit(this, null);
        var declaration = idTable.retrieve(id);

        if (declaration == null) {
            throw new IllegalArgumentException("Variable " + id + " is not declared");
        } else if (!(declaration instanceof VarDeclaration)) {
            throw new IllegalArgumentException(id + " is not a variable");
        } else {
            varExpression.setDeclaration((VarDeclaration) declaration);
            varExpression.setType((Type) declaration.getType().visit(this, arg));
        }

        return varExpression.getType();
    }

    @Override
    public Object visitCallExpression(CallExpression callExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) {
        Type type = (Type) unaryExpression.getExpression().visit(this, null);
        String operator = (String) unaryExpression.getOperator().visit(this, null);

        if (!UNARY_OPERATORS.contains(operator)) {
            throw new IllegalArgumentException(operator + " is not a unary operator. Allowed unary operators are: "
                    + String.join(", ", UNARY_OPERATORS));
        }

        unaryExpression.setType(type);
        return unaryExpression.getType();
    }

    @Override
    public Object visitNumberLitExpression(NumberLitExpression numberLitExpression, Object arg) {
        numberLitExpression.getLiteral().visit(this, true);
        numberLitExpression.setType(new NumberType());
        return numberLitExpression.getType();
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        return identifier.getSpelling();
    }

    @Override
    public Object visitNumberLiteral(NumberLiteral numberLiteral, Object arg) {
        return new NumberType();
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return operator.getSpelling();
    }

    @Override
    public Object visitParameterList(ParameterList parameterList, Object arg) {
        return null;
    }

    @Override
    public Object visitLetterLitExpression(LetterLitExpression letterLitExpression, Object arg) {
        letterLitExpression.getLiteral().visit(this, true);
        letterLitExpression.setType(new LetterType());
        return letterLitExpression.getType();
    }

    @Override
    public Object visitParameter(Parameter parameter, Object arg) {
        return null;
    }

    @Override
    public Object visitStateLitExpression(StateLitExpression stateLitExpression, Object arg) {
        stateLitExpression.getLiteral().visit(this, true);
        stateLitExpression.setType(new StateType());
        return stateLitExpression.getType();
    }

    @Override
    public Object visitCollectionLiteral(CollectionLiteral collectionLiteral, Object arg) {
        List<Type> types = new ArrayList<>();

        for (Literal literal : collectionLiteral.getLiteralList()) {
            types.add((Type) literal.visit(this, null));
        }
        return types;
    }

    @Override
    public Object visitCollectionLitExpression(CollectionLitExpression collectionLitExpression, Object arg) {
        List<Type> types = (List<Type>) collectionLitExpression.getLiteral().visit(this, true);

        // Remove indistinct types
        List<? extends Class<? extends Type>> distinct = types
                .stream()
                .map(Type::getClass)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() != 1) {
            throw new IllegalArgumentException("All elements of collection literal must have the same type." +
                    " Types present in collection literal: "
                    + distinct.stream().map(Class::getSimpleName).collect(Collectors.joining(", ")));
        }
        // All types in the list are the same, so assign first to expression type

        collectionLitExpression.setType(new CollectionType(types.size(), types.get(0)));
        return collectionLitExpression.getType();
    }

    @Override
    public Object visitStateLiteral(StateLiteral stateLiteral, Object arg) {
        return new StateType();
    }

    @Override
    public Object visitLetterLiteral(LetterLiteral letterLiteral, Object arg) {
        return new LetterType();
    }

    @Override
    public Object visitCollection(Collection collection, Object arg) {
        collection.getSize().visit(this, null);
        int size = Integer.parseInt(collection.getSize().getSpelling());
        Type collectionType = (Type) collection.getCollectionType().visit(this, null);
        return new CollectionType(size, collectionType);
    }

    @Override
    public Object visitState(State state, Object arg) {
        return new StateType();
    }

    @Override
    public Object visitVoid(Void aVoid, Object arg) {
        return new VoidType();
    }

    @Override
    public Object visitInStatement(InStatement inStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitLetter(Letter letter, Object arg) {
        return new LetterType();
    }

    @Override
    public Object visitNumber(Number number, Object arg) {
        return new NumberType();
    }
}
