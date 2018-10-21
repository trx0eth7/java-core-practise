package com.trx0eth7.practise.template;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;
import java.util.Random;

@Deprecated
public class ReflectionTemplate<T extends Number> {

    private static final String CONST = "const";
    @NotNull
    @XmlAttribute
    protected int primitive;
    public List<T> numbers;

    @Ignore
    public ReflectionTemplate(int primitive, @NotNull List<T> numbers) {
        this.primitive = primitive;
        this.numbers = numbers;
    }

    @NotNull
    public int getPrimitive() {
        return primitive;
    }

    public void setPrimitive(int primitive) {
        this.primitive = primitive;
    }

    @NotNull
    @Getter
    public static Integer getRandomNumber() {
        return new Random().nextInt();
    }
}
