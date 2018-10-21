package com.trx0eth7.practise.reflection.analyzer.config;

public class CustomConfig implements Config {

    private Boolean accessPrivateFields = Boolean.FALSE;
    private Boolean valueFieldRepresented = Boolean.FALSE;

    public void setAccessPrivateFields(Boolean accessPrivateFields) {
        this.accessPrivateFields = accessPrivateFields;
    }

    @Override
    public Boolean isAccessPrivateFields() {
        return accessPrivateFields;
    }

    public void setValueFieldRepresented(Boolean valueFieldRepresented) {
        this.valueFieldRepresented = valueFieldRepresented;
    }

    @Override
    public Boolean isValueFieldRepresented() {
        return valueFieldRepresented;
    }

    @Override
    public Boolean isExtendedInformation() {
        return Boolean.TRUE;
    }
}
