package com.trx0eth7.practise.reflection.analyzer;

import com.trx0eth7.practise.reflection.analyzer.config.CustomConfig;
import com.trx0eth7.practise.reflection.analyzer.exception.ObjectAnalyzerIllegalAccessException;
import com.trx0eth7.practise.template.ReflectionTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ObjectAnalyzerTest {

    private ObjectAnalyzer analyzer;
    private CustomConfig config;
    private ReflectionTemplate reflectionTemplate;

    @Before
    public void setUp() {
        reflectionTemplate = new ReflectionTemplate(20, Arrays.asList(10, 5, 2, 3));
        config = new CustomConfig();
        analyzer = new ObjectAnalyzer(reflectionTemplate, config);
    }

    @Test
    public void shouldAnalyzeConstructors() {
        String expectedResult = "@Ignore\n" +
                "public ReflectionTemplate(int arg0 List arg1 )\n";
        String actualResult = analyzer.analyzeConstructors();
        Assert.assertEquals("Object was analyzed incorrectly", expectedResult, actualResult);
    }

    @Test
    public void shouldAnalyzeFields() throws ObjectAnalyzerIllegalAccessException {
        config.setAccessPrivateFields(true);
        config.setValueFieldRepresented(true);

        String expectedResult = "\n" +
                "private static final String CONST = const;\n" +
                "@XmlAttribute\n" +
                "protected int primitive = 20;\n" +
                "\n" +
                "public List numbers = [10, 5, 2, 3];\n";
        String actualResult = analyzer.analyzeFields();
        Assert.assertEquals("Object was analyzed incorrectly", expectedResult, actualResult);
    }

    @Test
    public void shouldThrowAccessException() throws ObjectAnalyzerIllegalAccessException {
        config.setAccessPrivateFields(false);
        analyzer.analyzeFields();
    }
}