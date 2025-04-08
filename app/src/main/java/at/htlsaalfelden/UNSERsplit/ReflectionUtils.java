package at.htlsaalfelden.UNSERsplit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class ReflectionUtils {
    private ReflectionUtils() {}

    public static <T> T get(Object o, String name) {
        return get(o,name,o.getClass());
    }

    public static <T> T get(Object o, String name, Class<?> c) {
        try {
            Field field = c.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Object o, String name, Object v) {
        set(o,name,o.getClass(), v);
    }

    public static void set(Object o, String name, Class<?> c, Object v) {
        try {
            Field field = c.getDeclaredField(name);
            field.setAccessible(true);
            field.set(o,v);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T call(Object o, String name, Object ...args) {
        return call(o,name,o.getClass(), args);
    }

    public static <T> T call(Object o, String name, Class<?> c, Object ...args) {
        Class<?>[] argClasses = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            argClasses[i] = args[i].getClass();
        }

        return call(o,name,c, argClasses, args);
    }

    public static <T> T call(Object o, String name, Class<?> c, Class<?>[] argsClasses, Object ...args) {
        try {
            Method m = c.getDeclaredMethod(name, argsClasses);
            return (T) m.invoke(o, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    public static void showMembers(Class<?> c) {
        System.out.println("Logging for Class '" + c.getSimpleName() + "'");
        System.out.println("Fields: ");
        for(Field field : c.getDeclaredFields()) {
            System.out.println("\t" + field.getName() + " : " + field.getType().getSimpleName());
        }
        System.out.println("Methods: ");
        for(Method m : c.getDeclaredMethods()) {
            StringJoiner sj = new StringJoiner(",");
            for(Class<?> param : m.getParameterTypes()) {
                sj.add(param.getSimpleName());
            }
            System.out.println("\t" + m.getName() + " : " + m.getReturnType().getSimpleName() + " <- (" + sj + ")");
        }
        if(c.getSuperclass() != null && !Objects.equals(c.getSuperclass(), Object.class)) {
            showMembers(c.getSuperclass());
        }
    }

    public static void showMembers(Object o) {
        showMembers(o.getClass());
    }
}
