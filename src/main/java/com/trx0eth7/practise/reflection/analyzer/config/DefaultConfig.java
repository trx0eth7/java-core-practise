package com.trx0eth7.practise.reflection.analyzer.config;

public class DefaultConfig implements Config{

    @Override
    public Boolean isAccessPrivateFields() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isValueFieldRepresented() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isExtendedInformation() {
        return Boolean.TRUE;
    }
}
