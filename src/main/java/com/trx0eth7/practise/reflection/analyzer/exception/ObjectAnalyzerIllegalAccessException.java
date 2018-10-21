package com.trx0eth7.practise.reflection.analyzer.exception;

import java.lang.reflect.Field;

import static java.lang.String.format;

public class ObjectAnalyzerIllegalAccessException extends IllegalAccessException {

    public ObjectAnalyzerIllegalAccessException(String message) {
        super(message);
    }

    public ObjectAnalyzerIllegalAccessException(String message, Field context) {
        super(format("%s%n Field '%s' is not access. Config property 'accessPrivateFields' is set as 'false'",
                message, context.getName()));
    }
}
