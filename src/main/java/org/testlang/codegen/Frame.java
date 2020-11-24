package org.testlang.codegen;

public class Frame {
    private int level;
    private int size;


    public Frame() {
        level = 0;
        size = 0;
    }


    public Frame(int level, int size) {
        this.level = level;
        this.size = size;
    }


    public Frame(Frame a, int increment) {
        this.level = a.level;
        this.size = a.size + increment;
    }


    public Frame(Frame a) {
        this.level = a.level + 1;
        this.size = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public String toString() {
        return "level=" + level + " displacement=" + size;
    }
}
