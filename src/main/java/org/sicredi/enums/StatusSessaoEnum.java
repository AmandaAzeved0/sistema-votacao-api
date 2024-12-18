package org.sicredi.enums;

public enum StatusSessaoEnum {
    ABERTA,
    ENCERRADA;

    public static StatusSessaoEnum fromOrdinal(int ordinal) {
        return StatusSessaoEnum.values()[ordinal];

    }
}
