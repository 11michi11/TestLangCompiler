package org.testlang.codegen;

public class KnownAddress extends RuntimeEntity {

    private ObjectAddress address;

    public KnownAddress() {
        super();
        this.address = null;
    }

    public KnownAddress(int size, int level, int displacement) {
        super(size);
        this.address = new ObjectAddress(level, displacement);
    }

    public ObjectAddress getAddress() {
        return address;
    }
}
