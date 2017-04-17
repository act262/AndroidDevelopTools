package io.micro.adt.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.android.shell.cmd.CmdSet;
import io.android.shell.cmd.SystemPropertiesCmds;

/**
 * App 相关设置功能
 */
public class AppUtil {

    /**
     * 判断指定的Activity 是否为Top Activity
     *
     * @param activityName com.xx.yy.activity.A
     */
    public static boolean isTopActivity(Context context, String activityName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

        ActivityManager.RunningTaskInfo topTask = null;
        if (tasks != null && !tasks.isEmpty()) {
            // TopTask
            topTask = tasks.get(0);
        }

        if (topTask != null) {
            ComponentName topActivity = topTask.topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName()) &&
                    topActivity.getClassName().equals(activityName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断指定的Activity 是否为 Top Activity
     */
    public static boolean isTopActivity(Context context, Class<? extends Activity> clazz) {
        return isTopActivity(context, clazz.getName());
    }

    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 没有权限
     * android.permission.CLEAR_APP_USER_DATA
     */
    public static void clearAppData(Context context, String pkg) {
        ActivityManager activityManager = getActivityManager(context);
        try {
            Class<?> cls = Class.forName("android.app.ActivityManager");
            Class<?> observerCls = Class.forName("android.content.pm.IPackageDataObserver");
            Method clearApplicationUserData = cls.getMethod("clearApplicationUserData", String.class, observerCls);
            // ActivityManager.clearApplicationUserData(pkg,observer)
            clearApplicationUserData.invoke(activityManager, pkg, null);
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

    /**
     * 使用pm命令清除指定包名的数据，不用权限即可处理
     */
    public static void clearAppData(String pkg) {
        try {
            CmdSet.execSu("pm clear " + pkg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void restartApp(String pkg, String aty) {
        try {
            CmdSet.execSu("am start " + pkg + " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否启用WiFi Adb 调试
     */
    public static void wifiAdb(boolean enabled) {
        try {
            if (enabled) {
                // setprop service.adb.tcp.port 5555
                // stop adbd
                // start adbd
                SystemPropertiesCmds.set("service.adb.tcp.port", "5555");
            } else {
                SystemPropertiesCmds.set("service.adb.tcp.port", "-1");
            }

            CmdSet.execSu("stop adbd");
            CmdSet.execSu("start adbd");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
