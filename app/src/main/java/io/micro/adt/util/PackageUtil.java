package io.micro.adt.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * PackageManager Utility
 *
 * @author act262@gmail.com
 */
public class PackageUtil {

    /**
     * 启动指定包的默认Activity
     */
    public static void startPkg(Context context, String pkg) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pkg);
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * android.permission.CLEAR_APP_USER_DATA
     */
    public static void clearAppData(Context context, String pkg) {
        PackageManager packageManager = context.getPackageManager();
        try {
            Class<?> observerCls = Class.forName("android.content.pm.IPackageDataObserver");
            Method clearApplicationUserData = PackageManager.class.getMethod("clearApplicationUserData", String.class, observerCls);
            clearApplicationUserData.invoke(packageManager, pkg, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
