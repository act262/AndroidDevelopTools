package io.micro.adt;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 开发者选项控制
 *
 * @author act262@gmail.com
 */
public class Developer {

    private static final String FALSE = "false";
    /* copy from View.DEBUG_LAYOUT_PROPERTY */
    private static final String DEBUG_LAYOUT_PROPERTY = "debug.layout";
    /* copy from ThreadedRenderer.DEBUG_OVERDRAW_PROPERTY */
    private static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
    private static final String OVERDRAW_PROPERTY_SHOW = "show";
    /* copy from ThreadedRenderer.PROFILE_PROPERTY */
    private static final String PROFILE_PROPERTY = "debug.hwui.profile";
    private static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";

    /* copy from StrictMode.VISUAL_PROPERTY */
    public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";

    private Developer() {
    }

    /**
     * 显示布局边界
     */
    public static boolean debugLayout() {
        return SystemProperties.getBoolean(DEBUG_LAYOUT_PROPERTY, false);
    }

    /**
     * 开启/关闭:显示布局边界
     */
    public static void setDebugLayout(boolean bool) {
        SystemProperties.set(DEBUG_LAYOUT_PROPERTY, Boolean.toString(bool));
        refresh();
    }

    /**
     * 显示GPU过度绘制
     */
    public static boolean debugOverdraw() {
        return OVERDRAW_PROPERTY_SHOW.equals(SystemProperties.get(DEBUG_OVERDRAW_PROPERTY));
    }

    /**
     * 打开/关闭:显示GPU过度绘制
     */
    public static void setDebugOverdraw(boolean bool) {
        SystemProperties.set(DEBUG_OVERDRAW_PROPERTY, bool ? OVERDRAW_PROPERTY_SHOW : FALSE);
        refresh();
    }

    /**
     * GPU呈现模式分析
     */
    public static boolean debugProfile() {
        return PROFILE_PROPERTY_VISUALIZE_BARS.equals(SystemProperties.get(PROFILE_PROPERTY));
    }

    /**
     * 打开/关闭:GPU呈现模式分析(在屏幕中显示为条)
     */
    public static void setProfile(boolean bool) {
        SystemProperties.set(PROFILE_PROPERTY, bool ? PROFILE_PROPERTY_VISUALIZE_BARS : FALSE);
        refresh();
    }

    /**
     * USB调试开关,AndroidManifest配置android:sharedUserId="android.uid.system",
     * 使用权限:<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
     */
    public static void setAdbEnable(Context context, boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                Settings.Global.putInt(context.getContentResolver(),
                        Settings.Global.ADB_ENABLED, enable ? 1 : 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保持唤醒状态,配置同AdbEnable方法
     */
    public static void keepScreenOn(Context context, boolean check) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                Settings.Global.putInt(context.getContentResolver(),
                        Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                        check ? (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB) : 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 严格模式
     */
    public static void setStrictMode(boolean enable) {
        try {
            Class<?> clz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = clz.getMethod("asInterface", IBinder.class);
            Object windowManager = asInterface.invoke(null, ServiceManager.getService("window"));
            Method method = clz.getMethod("setStrictModeVisualIndicatorPreference", String.class);
            method.invoke(windowManager, enable ? "1" : "");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不保留活动,用户离开后即销毁每个活动
     */
    public static void setDestroyActivities(boolean check) {
        try {
            Class<?> clz = Class.forName("android.app.ActivityManagerNative");
            Method getDefault = clz.getMethod("getDefault");
            Object activityManagerNative = getDefault.invoke(null);

            Method setAlwaysFinish = clz.getMethod("setAlwaysFinish", boolean.class);
            setAlwaysFinish.invoke(activityManagerNative, check);
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
     * 打开开发者选项页面
     */
    public static void openDevelopmentSettings(Context context) {
        Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 刷新全局,如果没有调用这个刷新,只会在新的页面中才会有所变化.
     */
    private static void refresh() {
        new SystemPropPoker().execute();
    }

    /* copy from DevelopmentSettings$SystemPropPoker */
    public static class SystemPropPoker extends AsyncTask<Void, Void, Void> {

        // copy from IBinder.SYSPROPS_TRANSACTION
        private static final int SYSPROPS_TRANSACTION = ('_' << 24) | ('S' << 16) | ('P' << 8) | 'R';

        private static final String TAG = "SystemPropPoker";

        @Override
        protected Void doInBackground(Void... params) {
            String[] services = ServiceManager.listServices();
            for (String service : services) {
                IBinder obj = ServiceManager.checkService(service);
                if (obj != null) {
                    Parcel data = Parcel.obtain();
                    try {
                        obj.transact(SYSPROPS_TRANSACTION, data, null, 0);
                    } catch (RemoteException e) {
                    } catch (Exception e) {
                        Log.i(TAG, "Someone wrote a bad service '" + service
                                + "' that doesn't like to be poked: " + e);
                    }
                    data.recycle();
                }
            }
            return null;
        }
    }

}
