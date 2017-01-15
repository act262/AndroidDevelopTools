package android.os;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 仿照Framework 隐藏类SystemProperties 封装
 *
 * @author act262@gmail.com
 */
public class SystemProperties {

    private static Class<?> clz() {
        try {
            return Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void set(String key, String val) {
        try {
            Method method = clz().getMethod("set", String.class, String.class);
            method.invoke(null, key, val);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        try {
            Method method = clz().getMethod("get", String.class);
            return (String) method.invoke(null, key);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String key, String def) {
        try {
            Method method = clz().getMethod("get", String.class, String.class);
            return (String) method.invoke(null, key, def);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            Method method = clz().getMethod("getBoolean", String.class, boolean.class);
            return (boolean) method.invoke(null, key, def);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return def;
    }
}
