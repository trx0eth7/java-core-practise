package com.trx0eth7.practise.reflection.analyzer;

import com.trx0eth7.practise.reflection.analyzer.config.Config;
import com.trx0eth7.practise.reflection.analyzer.config.DefaultConfig;
import com.trx0eth7.practise.reflection.analyzer.exception.ObjectAnalyzerIllegalAccessException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import static java.lang.System.out;

public class ObjectAnalyzer {
    private Object analysisObject;
    private Config config;

    public ObjectAnalyzer(Object obj) {
        analysisObject = obj;
        config = new DefaultConfig();
    }

    public ObjectAnalyzer(Object analysisObject, Config config) {
        this.analysisObject = analysisObject;
        this.config = config;
    }

    public String analyzeObject() throws ObjectAnalyzerIllegalAccessException {
        return new StringBuilder()
                .append(analyzeClass())
                .append(analyzeFields())
                .append(analyzeConstructors())
                .append(analyzeMethods())
                .append("\n}")
                .toString();

    }

    public String analyzeClass() {
        StringBuilder dataClass = new StringBuilder();

        Class aClass = analysisObject.getClass();
        int mod = aClass.getModifiers();

        return dataClass
                .append("package ")
                .append(aClass.getPackage().getName())
                .append("\n")
                .append(getAnnotations(aClass.getDeclaredAnnotations()))
                .append("\n")
                .append(Modifier.toString(mod))
                .append(" class ")
                .append(aClass.getSimpleName())
                .append(" {")
                .append(" // is super class to ")
                .append(aClass.getSuperclass().getCanonicalName())
                .toString();
    }

    public String analyzeFields() throws ObjectAnalyzerIllegalAccessException {
        StringBuilder dataFields = new StringBuilder();

        Class aClass = analysisObject.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(config.isAccessPrivateFields());
            Class fieldType = field.getType();
            String name = field.getName();
            dataFields.append(getAnnotations(field.getDeclaredAnnotations()))
                    .append("\n")
                    .append(Modifier.toString(field.getModifiers()))
                    .append(" ")
                    .append(field.getType().getSimpleName())
                    .append(" ")
                    .append(name);

            String value = "";
            try {
                if (config.isValueFieldRepresented()) {
                    if (fieldType.isPrimitive()) {
                        value += field.get(analysisObject);
                    } else {
                        value += field.get(analysisObject).toString();
                    }
                    dataFields.append(" = ")
                            .append(value)
                            .append(";");
                }
            } catch (IllegalAccessException e) {
                throw new ObjectAnalyzerIllegalAccessException(e.getLocalizedMessage(), field);
            }
            dataFields.append("\n");
        }
        return dataFields.toString();
    }


    //TODO: show name of construction parameter
    public String analyzeConstructors() {
        StringBuilder dataConstructors = new StringBuilder();

        Class aClass = analysisObject.getClass();
        Constructor[] constructors = aClass.getConstructors();

        for (Constructor constructor : constructors) {
            dataConstructors.append(getAnnotations(constructor.getDeclaredAnnotations()))
                    .append("\n")
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append(" ")
                    .append(aClass.getSimpleName())
                    .append(getParameters(constructor.getParameters()))
                    .append("\n");
        }
        return dataConstructors.toString();
    }

    public String analyzeMethods() {
        StringBuilder dataMethods = new StringBuilder();

        Class aClass = analysisObject.getClass();
        Method[] methods = aClass.getDeclaredMethods();

        for (Method method : methods) {
            dataMethods.append(getAnnotations(method.getDeclaredAnnotations()))
                    .append("\n")
                    .append(Modifier.toString(method.getModifiers()))
                    .append(" ")
                    .append(method.getReturnType().getSimpleName())
                    .append(" ")
                    .append(method.getName())
                    .append(" ")
                    .append(getParameters(method.getParameters()))
                    .append("\n");
        }
        return dataMethods.toString();
    }

    private String getAnnotations(Annotation... annotations) {
        StringBuilder annotationData = new StringBuilder();
        for (Annotation annotation : annotations) {
            if (annotationData.length() > 0) {
                annotationData.append("\n");
            }
            annotationData.append("@");
            annotationData.append(annotation.annotationType().getSimpleName());
        }
        return annotationData.toString();
    }

    private String getParameters(Parameter... parameters) {
        StringBuilder annotationData = new StringBuilder();
        annotationData.append("(");
        for (Parameter parameter : parameters) {
            annotationData.append(getAnnotations(parameter.getAnnotations()));
            annotationData.append(parameter.getType().getSimpleName());
            annotationData.append(" ");
            annotationData.append(parameter.getName());
            annotationData.append(" ");
        }
        annotationData.append(")");
        return annotationData.toString();
    }

    public void showInfoObject() throws ObjectAnalyzerIllegalAccessException {
        out.println(analyzeObject());
    }
}
