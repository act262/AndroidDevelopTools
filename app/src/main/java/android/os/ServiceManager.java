package android.os;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 仿照Framework 隐藏类 "ServiceManager"的封装
 *
 * @author act262@gmail.com
 */
public class ServiceManager {

    private static Class<?> clz() {
        try {
            return Class.forName("android.os.ServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] listServices() {
        String[] result = null;
        try {
            Method method = clz().getMethod("listServices");
            result = (String[]) method.invoke(null);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static IBinder checkService(String name) {
        try {
            Method method = clz().getMethod("checkService", String.class);
            return (IBinder) method.invoke(null, name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
