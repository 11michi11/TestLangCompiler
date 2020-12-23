package org.testlang.codegen;

import TAM.Instruction;
import TAM.Machine;
import org.testlang.AST.*;
import org.testlang.AST.Number;
import org.testlang.AST.Void;
import org.testlang.Visitor;
import org.testlang.types.CollectionType;
import org.testlang.types.LetterType;
import org.testlang.types.NumberType;
import org.testlang.types.Type;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Encoder implements Visitor {

    private int nextAdr = Machine.CB;
    private int currentLevel = 0;

    private void emit(int op, int n, int r, int d) {
        if (n > 255) {
            System.out.println("Operand too long");
            n = 255;
        }

        Instruction instr = new Instruction();
        instr.op = op;
        instr.n = n;
        instr.r = r;
        instr.d = d;

        if (nextAdr >= Machine.PB)
            System.out.println("Program too large");
        else
            Machine.code[nextAdr++] = instr;
    }

    private void patch(int adr, int d) {
        Machine.code[adr].d = d;
    }

    private int displayRegister(int currentLevel, int entityLevel) {
        if (entityLevel == 0)
            return Machine.SBr;
        else if (currentLevel - entityLevel <= 6)
            return Machine.LBr + currentLevel - entityLevel;
        else {
            System.out.println("Accessing across to many levels");
            return Machine.L6r;
        }
    }

    public void saveTargetProgram(String fileName) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

            for (int i = Machine.CB; i < nextAdr; ++i)
                Machine.code[i].write(out);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Trouble writing " + fileName);
        }
    }


    public void encode(Program program) {
        program.visit(this, null);
    }


    @Override
    public Object visitProgram(Program program, Object arg) {
        int before = nextAdr;
//        emit(Machine.JUMPop, 0, Machine.CB, 0);
        int size = (int) program.getDeclarationList().visit(this, new Frame());
        patch(before, nextAdr);

        if (size > 0) {
            emit(Machine.PUSHop, 0, 0, size);
        }
        program.getStatementList().visit(this, new Frame());

        emit(Machine.HALTop, 0, 0, 0);
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        int before = nextAdr;
        emit(Machine.JUMPop, 0, Machine.CB, 0);
        int size = (int) block.getDeclarationList().visit(this, arg);
        patch(before, nextAdr);

        if (size > 0) {
            emit(Machine.PUSHop, 0, 0, size);
        }
        block.getStatementList().visit(this, arg);
        return size;
    }

    @Override
    public Object visitDeclarationList(DeclarationList declarationList, Object arg) {
        Frame frame = (Frame) arg;
        int startDisplacement = frame.getSize();
        int extraSize;


        for (Declaration declaration : declarationList.getDeclarationList()) {
            extraSize = (int) declaration.visit(this, arg);
            arg = new Frame(frame, extraSize);
        }

        return frame.getSize() - startDisplacement;
    }

    @Override
    public Object visitVariableDeclaration(VarDeclaration varDeclaration, Object arg) {
        Frame frame = (Frame) arg;
        int extraSize;

        extraSize = (int) varDeclaration.getType().visit(this, null);
        emit(Machine.PUSHop, 0, 0, extraSize);
        varDeclaration.setEntity(new KnownAddress(Machine.addressSize, frame.getLevel(), frame.getSize()));

        return extraSize;
    }

    @Override
    public Object visitOperationDeclaration(OprDeclaration oprDeclaration, Object arg) {
        return null;
    }

    @Override
    public Object visitStatementList(StatementList statementList, Object arg) {
        Frame frame = (Frame) arg;
        int startDisplacement = frame.getSize();
        int extraSize;


        for (Statement statement : statementList.getStatementList()) {
            extraSize = (int) statement.visit(this, arg);
            arg = new Frame(frame, extraSize);
        }

        return frame.getSize() - startDisplacement;
    }

    @Override
    public Object visitExpressionStatement(ExpressionStatement expressionStatement, Object arg) {
        return expressionStatement.getExpression().visit(this, arg);
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        Frame frame = (Frame) arg;
        int jumpIfAddr, jumpAddr;

        // Evaluate if expression
        int valSize = (int) ifStatement.getExpression().visit(this, frame);
        jumpIfAddr = nextAdr;
        emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, 0);

        // Evaluate if block
        ifStatement.getBlock().visit(this, frame);
        jumpAddr = nextAdr;

        patch(jumpIfAddr, nextAdr);
//        emit(Machine.JUMPop, 0, Machine.CBr, 0);
//        patch(jumpAddr, nextAdr);

        return 0;
    }

    @Override
    public Object visitUntilStatement(UntilStatement untilStatement, Object arg) {
        Frame frame = (Frame) arg;
        int jumpAdr, loopAdr;

        jumpAdr = nextAdr;
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        loopAdr = nextAdr;
        untilStatement.getBlock().visit(this, frame);
        patch(jumpAdr, nextAdr);

        untilStatement.getExpression().visit(this, frame);
        emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, loopAdr);
        return 0;
    }

    @Override
    public Object visitOutStatement(OutStatement outStatement, Object arg) {
        Frame frame = (Frame) arg;
        Type type = outStatement.getExpression().getType();
        if (type instanceof NumberType) {
            outStatement.getExpression().visit(this, arg);
            emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
        } else if (type instanceof LetterType) {
            outStatement.getExpression().visit(this, arg);
            emit(Machine.CALLop, 0, Machine.PBr, Machine.putDisplacement);
        } else if (type instanceof CollectionType) {
            // Get collection address
            var declaration = ((VarExpression) outStatement.getExpression()).getDeclaration();
            var address = ((KnownAddress) declaration.getEntity()).getAddress();
            int typeSize = (int) ((Collection) declaration.getType()).getCollectionType().visit(this, arg);
            // Print n time value from stack as the collection's elements are on the stack
            var elements = ((CollectionType) type).getSize();
            // Decide what type of put to use
            int machinePutType;
            if (((CollectionType) type).getCollectionType() instanceof LetterType) {
                machinePutType = Machine.putDisplacement;
            } else {
                machinePutType = Machine.putintDisplacement;
            }
            for (int i = 0; i < elements; i++) {
                emit(Machine.LOADop, typeSize, displayRegister(frame.getLevel(),
                        address.getLevel()), address.getDisplacement() + i);
                emit(Machine.CALLop, 0, Machine.PBr, machinePutType);
            }
        }
        // Add this line when you want to add EOL after each print
        emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
        return 0;
    }


    @Override
    public Object visitInStatement(InStatement inStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) {
        Frame frame = (Frame) arg;
        // Get result type size
        int valSize = (int) binaryExpression.getType().getTypeDenoter().visit(this, null);
        String operator = (String) binaryExpression.getOperator().getSpelling();

        // Handle assignment
        if (operator.equals("=")) {
            var varDeclaration = ((VarExpression) binaryExpression.getOperand1()).getDeclaration();
            if (binaryExpression.getOperand2() instanceof CollectionLitExpression collectionLitExpression) {
                // Store collection expression
                var elements = collectionLitExpression.getLiteral().getLiteralList();
                var address = (KnownAddress) varDeclaration.getEntity();


                for (int i = 0; i < elements.size(); i++) {
                    LiteralExpression literal = elements.get(i);

                    int elementSize = (int) literal.visit(this, frame);//literal.getType().getTypeDenoter().visit(this, null);
                    emit(Machine.STOREop, elementSize, displayRegister(frame.getLevel(),
                            address.getAddress().getLevel()), address.getAddress().getDisplacement() + i);
                }
            } else {
                // Store single variable
                int valSize1 = (int) binaryExpression.getOperand2().visit(this, frame);
                encodeStore(varDeclaration, new Frame(frame, valSize1), valSize1);
            }
        } else {
            // Process first expression and get used size
            int valSize1 = (int) binaryExpression.getOperand1().visit(this, frame);
            Frame frame1 = new Frame(frame, valSize1);
            // Process second expression and get used size
            int valSize2 = (int) binaryExpression.getOperand2().visit(this, frame1);
            // Move frame after both expressions
            Frame frame2 = new Frame(frame.getLevel(), valSize1 + valSize2);

            operator = (String) binaryExpression.getOperator().visit(this, frame2);
        }
        return valSize;
    }

    @Override
    public Object visitVarExpression(VarExpression varExpression, Object arg) {
        Frame frame = (Frame) arg;
        int valSize = (int) varExpression.getType().getTypeDenoter().visit(this, null);
        encodeFetch(varExpression.getDeclaration(), frame, valSize);
        return valSize;
    }


    @Override
    public Object visitCallExpression(CallExpression callExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) {
        return null;
    }

    @Override
    public Object visitNumberLitExpression(NumberLitExpression numberLitExpression, Object arg) {
        Frame frame = (Frame) arg;
        int valSize = (int) numberLitExpression.getType().getTypeDenoter().visit(this, null);
        int literal = (int) numberLitExpression.getLiteral().visit(this, null);
        emit(Machine.LOADLop, 1, 0, literal);
        return valSize;
    }

    @Override
    public Object visitIdentifier(Identifier identifier, Object arg) {
        Frame frame = (Frame) arg;
        return null;
    }

    @Override
    public Object visitNumberLiteral(NumberLiteral numberLiteral, Object arg) {
        return Integer.valueOf(numberLiteral.getSpelling());
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        Frame frame = (Frame) arg;
        switch (operator.getSpelling()) {
            case "+" -> emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
            case "-" -> emit(Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement);
            case "*" -> emit(Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement);
            case "/" -> emit(Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement);
            case "==" -> {
                emit(Machine.LOADLop, 0, 0, frame.getSize() / 2);
                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.eqDisplacement);
            }
            case ">" -> {
                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.gtDisplacement);
            }
            case "<" -> {
                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.ltDisplacement);
            }

        }

        return operator.getSpelling();
    }

    @Override
    public Object visitParameterList(ParameterList parameterList, Object arg) {
        return null;
    }

    @Override
    public Object visitLetterLitExpression(LetterLitExpression letterLitExpression, Object arg) {
        Frame frame = (Frame) arg;
        int valSize = (int) letterLitExpression.getType().getTypeDenoter().visit(this, null);
        char literal = (char) letterLitExpression.getLiteral().visit(this, null);
        emit(Machine.LOADLop, 1, 0, literal);
        return valSize;
    }

    @Override
    public Object visitParameter(Parameter parameter, Object arg) {
        return null;
    }

    @Override
    public Object visitStateLitExpression(StateLitExpression stateLitExpression, Object arg) {
        Frame frame = (Frame) arg;
        int valSize = (int) stateLitExpression.getType().getTypeDenoter().visit(this, null);
        int literal = (int) stateLitExpression.getLiteral().visit(this, null);
        emit(Machine.LOADLop, 1, 0, literal);
        return valSize;
    }

    @Override
    public Object visitCollectionLiteral(CollectionLiteral collectionLiteral, Object arg) {
        Frame frame = (Frame) arg;
        int startDisplacement = frame.getSize();
        int extraSize;

        for (LiteralExpression literal : collectionLiteral.getLiteralList()) {
            extraSize = (int) literal.visit(this, arg);
            arg = new Frame(frame, extraSize);
        }
        return frame.getSize() - startDisplacement;
    }

    @Override
    public Object visitCollectionLitExpression(CollectionLitExpression collectionLitExpression, Object arg) {
        collectionLitExpression.getType().getTypeDenoter().visit(this, null);
        return collectionLitExpression.getLiteral().visit(this, arg);
    }

    @Override
    public Object visitStateLiteral(StateLiteral stateLiteral, Object arg) {
        return stateLiteral.getSpelling().equals("true") ? 1 : 0;
    }

    @Override
    public Object visitLetterLiteral(LetterLiteral letterLiteral, Object arg) {
        return letterLiteral.getSpelling().charAt(0);
    }

    @Override
    public Object visitCollection(Collection collection, Object arg) {
        int collectionSize;
        if (collection.getEntity() == null) {
            int elemSize = (int) collection.getCollectionType().visit(this, null);
            collectionSize = Integer.parseInt(collection.getSize().getSpelling()) * elemSize;
            collection.setEntity(new TypeRepresentation(collectionSize));
        } else {
            collectionSize = collection.getEntity().getSize();
        }
        return collectionSize;
    }

    @Override
    public Object visitState(State state, Object arg) {
        if (state.getEntity() == null) {
            state.setEntity(new TypeRepresentation(Machine.booleanSize));
        }
        return Machine.booleanSize;
    }

    @Override
    public Object visitLetter(Letter letter, Object arg) {
        if (letter.getEntity() == null) {
            letter.setEntity(new TypeRepresentation(Machine.characterSize));
        }
        return Machine.characterSize;
    }

    @Override
    public Object visitNumber(Number number, Object arg) {
        if (number.getEntity() == null) {
            number.setEntity(new TypeRepresentation(Machine.integerSize));
        }
        return Machine.integerSize;
    }

    @Override
    public Object visitVoid(Void aVoid, Object arg) {
        return null;
    }

    private void encodeFetch(VarDeclaration V, Frame frame, int valSize) {

        RuntimeEntity baseObject = (RuntimeEntity) V.getEntity();
        // If indexed = true, code will have been generated to load an index value.
        if (valSize > 255) {
//            reporter.reportRestriction("can't load values larger than 255 words");
            valSize = 255; // to allow code generation to continue
        }
        if (baseObject instanceof KnownValue) {
            // presumably offset = 0 and indexed = false
            int value = ((KnownValue) baseObject).value;
            emit(Machine.LOADLop, 0, 0, value);
        } else if ((baseObject instanceof UnknownValue) ||
                (baseObject instanceof KnownAddress)) {
            ObjectAddress address = (baseObject instanceof UnknownValue) ?
                    ((UnknownValue) baseObject).getAddress() :
                    ((KnownAddress) baseObject).getAddress();
            if (V.getType() instanceof Collection) {
                int collectionSize = (int) ((Collection) V.getType()).getSize().visit(this, frame);
                int typeSize = ((Collection) V.getType()).getCollectionType().getEntity().getSize();
                address = ((KnownAddress) V.getEntity()).getAddress();
                for (int i = 0; i < collectionSize; i++) {
                    emit(Machine.LOADop, typeSize, displayRegister(frame.getLevel(),
                            address.getLevel()), address.getDisplacement() + i);
                }
            } else
                emit(Machine.LOADop, valSize, displayRegister(frame.getLevel(),
                        address.getLevel()), address.getDisplacement());
        }
//        else if (baseObject instanceof UnknownAddress) {
//            ObjectAddress address = ((UnknownAddress) baseObject).address;
//            emit(Machine.LOADop, Machine.addressSize, displayRegister(frame.level,
//                    address.level), address.displacement);
//            if (V.indexed)
//                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
//            if (V.offset != 0) {
//                emit(Machine.LOADLop, 0, 0, V.offset);
//                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
//            }
//            emit(Machine.LOADIop, valSize, 0, 0);
//        }
    }

    private void encodeStore(VarDeclaration V, Frame frame, int valSize) {

        RuntimeEntity baseObject = V.getEntity();
        // If indexed = true, code will have been generated to load an index value.
        if (valSize > 255) {
            valSize = 255; // to allow code generation to continue
        }
        if (baseObject instanceof KnownAddress) {
            ObjectAddress address = ((KnownAddress) baseObject).getAddress();
            if (V.getType() instanceof Collection) {
                emit(Machine.LOADAop, 0, displayRegister(frame.getLevel(), address.getLevel()),
                        address.getDisplacement());
                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
                emit(Machine.STOREIop, valSize, 0, 0);
            } else {
                emit(Machine.STOREop, valSize, displayRegister(frame.getLevel(),
                        address.getLevel()), address.getDisplacement());
            }
        }
//        else if (baseObject instanceof UnknownAddress) {
//            ObjectAddress address = ((UnknownAddress) baseObject).address;
//            emit(Machine.LOADop, Machine.addressSize, displayRegister(frame.level,
//                    address.level), address.displacement);
//            if (V.indexed)
//                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
//            if (V.offset != 0) {
//                emit(Machine.LOADLop, 0, 0, V.offset);
//                emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
//            }
//            emit(Machine.STOREIop, valSize, 0, 0);
//        }
    }
}
