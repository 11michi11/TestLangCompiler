package org.testlang.codegen;

public class ObjectAddress {

    private int level;
    private int displacement;

    public ObjectAddress(int level, int displacement) {
        this.level = level;
        this.displacement = displacement;
    }

    public int getLevel() {
        return level;
    }

    public int getDisplacement() {
        return displacement;
    }
}
