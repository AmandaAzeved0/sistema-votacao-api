package org.sicredi.enums;

public enum VotoEnum {
    SIM,
    NAO;

    public static VotoEnum fromOrdinal(int ordinal) {
        return VotoEnum.values()[ordinal];
    }
}
