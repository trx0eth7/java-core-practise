package com.trx0eth7.practise.reflection;

import com.trx0eth7.practise.template.Employee;
import com.trx0eth7.practise.template.Manager;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ReflectionTest {

    Employee employee;
    Manager manager;

    @Before
    public void setUp() throws Exception {
        employee = new Employee("Alex", "Frost", (byte) 38);
        manager = new Manager("Jonh", "Costa", (byte) 38);
    }

    @Test
    public void shouldMatchClass() throws ClassNotFoundException {
        String className = "com.trx0eth7.practise.template.Manager";
        Class cl = Class.forName(className);
        Class superCl = cl.getSuperclass();
        String modifiers = Modifier.toString(cl.getModifiers());

        assertEquals("Class name doesn't match", className, cl.getName());
        assertEquals("Super class doesn't match", Employee.class, superCl);
        assertEquals("Modifier must be public", "public", modifiers + "");
        assertEquals("Manager must be extended from Employee", "Employee", superCl.getSimpleName());
    }

    @Test
    public void shouldHaveConstructors() {
        Class cl = manager.getClass();
        Constructor constructor = cl.getConstructors()[0];
        String modifiers = Modifier.toString(constructor.getModifiers());
        Class[] types = constructor.getParameterTypes();

        assertEquals("Modifier must be public", "public", modifiers + "");
        assertEquals("Parameter count doesn't match", 3, constructor.getParameterCount());
        assertArrayEquals("Parameter types don't match", new Class[]{String.class, String.class, byte.class}, types);

    }

    @Test
    public void shouldHaveMethods() throws NoSuchMethodException {
        Class cl = employee.getClass();
        Method[] methods = cl.getDeclaredMethods();
        Method employeeMethod = cl.getMethod("getSalary");

        assertEquals("Methods count doesn't match", 8, methods.length);
        assertEquals("Parameters count doesn't match", 0, employeeMethod.getParameterCount());
        for (Method method : methods) {
            String modifiers = Modifier.toString(method.getModifiers());
            Class returnType = method.getReturnType();
            if (method.getName().contains("set")) {
                assertEquals("Return type [" + method + "] must be void", void.class, returnType);
            }
            assertEquals("Modifier [" + method + "] must be public", "public", modifiers);
        }

    }

    @Test
    public void shouldHaveField() {
        Class cl = employee.getClass();
        Field[] fields = cl.getDeclaredFields();

        assertEquals("Fields count doesn't match", 4, fields.length);
    }
}
