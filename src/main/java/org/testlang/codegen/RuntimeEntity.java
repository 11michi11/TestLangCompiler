package org.testlang.codegen;

public abstract class RuntimeEntity {

    private final static int maxRoutineLevel = 7;
    private int size;


    public RuntimeEntity () {
        size = 0;
    }

    public RuntimeEntity (int size) {
        this.size = size;
    }

    public static int getMaxRoutineLevel() {
        return maxRoutineLevel;
    }

    public int getSize() {
        return size;
    }
}
