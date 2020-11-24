package org.testlang.codegen;

public class UnknownValue extends RuntimeEntity {

    private ObjectAddress address;

    public UnknownValue() {
        super();
        address = null;
    }

    public UnknownValue(int size, int level, int displacement) {
        super(size);
        address = new ObjectAddress(level, displacement);
    }

    public ObjectAddress getAddress() {
        return address;
    }
}
