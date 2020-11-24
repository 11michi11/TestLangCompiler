package org.testlang.codegen;

public class UnknownAddress extends RuntimeEntity {

    private ObjectAddress address;

    public UnknownAddress() {
        super();
        this.address = null;
    }

    public UnknownAddress(int size, int level, int displacement) {
        super(size);
        this.address = new ObjectAddress(level, displacement);
    }

}
