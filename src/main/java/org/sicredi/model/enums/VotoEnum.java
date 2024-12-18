package org.sicredi.model.enums;

public enum VotoEnum {
    SIM,
    NAO;

    public static VotoEnum fromOrdinal(int ordinal) {
        return VotoEnum.values()[ordinal];
    }
}
