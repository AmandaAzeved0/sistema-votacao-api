package org.sicredi.model.enums;

public enum StatusSessaoEnum {
    ABERTA,
    ENCERRADA;

    public static StatusSessaoEnum fromOrdinal(int ordinal) {
        return StatusSessaoEnum.values()[ordinal];

    }
}
