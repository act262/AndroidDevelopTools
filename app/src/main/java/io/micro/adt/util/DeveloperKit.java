package io.micro.adt.util;

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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.android.shell.cmd.SettingsCmds;
import io.android.shell.cmd.SystemPropertiesCmds;

/**
 * 开发者选项控制
 *
 * @author act262@gmail.com
 */
@SuppressWarnings("unused")
public class DeveloperKit {

    private static final String FALSE = "false";
    private static final String ENABLED = "1";
    private static final String DISABLED = "0";

    /* copy from View.DEBUG_LAYOUT_PROPERTY */
    private static final String DEBUG_LAYOUT_PROPERTY = "debug.layout";
    /* copy from ThreadedRenderer.DEBUG_OVERDRAW_PROPERTY */
    private static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
    private static final String OVERDRAW_PROPERTY_SHOW = "show";
    /* copy from ThreadedRenderer.PROFILE_PROPERTY */
    private static final String PROFILE_PROPERTY = "debug.hwui.profile";
    private static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";

    /* copy from StrictMode.VISUAL_PROPERTY */
    private static final String STRICT_MODE_VISUAL_PROPERTY = "persist.sys.strictmode.visual";

    /* copy from Settings.System.POINTER_LOCATION */
    private static final String POINTER_LOCATION = "pointer_location";
    /* copy from Settings.System.SHOW_TOUCHES */
    public static final String SHOW_TOUCHES = "show_touches";

    private DeveloperKit() {
    }

    /**
     * USB调试开关是否开启,API17+
     */
    public static boolean isAdbEnabled(Context context) {
        int enabled = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            enabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, enabled);
        }
        return enabled == 1;
    }

    /**
     * USB调试开关,需要root权限,API17+
     */
    public static void setAdbEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                SettingsCmds.putStringGlobal(Settings.Global.ADB_ENABLED, enabled ? ENABLED : DISABLED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否保持屏幕唤醒状态,API17+
     */
    public static boolean isKeepScreenOn(Context context) {
        int enabled = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            enabled = Settings.Global.getInt(context.getContentResolver(), Settings.Global.STAY_ON_WHILE_PLUGGED_IN, enabled);
        }
        return enabled == (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB);
    }

    /**
     * 保持屏幕唤醒状态,需要root权限,API17+
     */
    public static void keepScreenOn(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                SettingsCmds.putStringGlobal(Settings.Global.STAY_ON_WHILE_PLUGGED_IN, enabled ? (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB) + "" : DISABLED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public static void setDebugLayout(boolean enabled) {
        try {
            String state = Boolean.toString(enabled);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SystemProperties.set(DEBUG_LAYOUT_PROPERTY, state);
            } else {
                SystemPropertiesCmds.set(DEBUG_LAYOUT_PROPERTY, state);
            }
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public static void setDebugOverdraw(boolean enabled) {
        try {
            String state = enabled ? OVERDRAW_PROPERTY_SHOW : FALSE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SystemProperties.set(DEBUG_OVERDRAW_PROPERTY, state);
            } else {
                SystemPropertiesCmds.set(DEBUG_OVERDRAW_PROPERTY, state);
            }
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * GPU呈现模式分析
     */
    public static boolean debugGPUProfile() {
        return PROFILE_PROPERTY_VISUALIZE_BARS.equals(SystemProperties.get(PROFILE_PROPERTY));
    }

    /**
     * 打开/关闭:GPU呈现模式分析(在屏幕中显示为条)
     */
    public static void setGPUProfile(boolean enabled) {
        try {
            String state = enabled ? PROFILE_PROPERTY_VISUALIZE_BARS : FALSE;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                SystemProperties.set(PROFILE_PROPERTY, state);
            } else {
                SystemPropertiesCmds.set(PROFILE_PROPERTY, state);
            }
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启严格模式
     */
    public static boolean isStrictMode() {
        return ENABLED.equals(SystemProperties.get(STRICT_MODE_VISUAL_PROPERTY));
    }

    /**
     * 严格模式
     */
    public static void setStrictMode(boolean enabled) {
        try {
            Class<?> clz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = clz.getMethod("asInterface", IBinder.class);
            Object windowManager = asInterface.invoke(null, ServiceManager.getService("window"));
            Method method = clz.getMethod("setStrictModeVisualIndicatorPreference", String.class);
            method.invoke(windowManager, enabled ? ENABLED : "");
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
     * 是否开启不保留Activity,API21+
     */
    public static boolean isDestroyActivities(Context context) {
        int enabled = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            enabled = Settings.Global.getInt(
                    context.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, enabled);
        }
        return enabled != 0;
    }

    /**
     * 不保留Activity,用户离开后即销毁每个Activity,API21+
     */
    public static void setDestroyActivities(boolean enabled) {
        // 需要系统系统签名的权限
//        try {
//            Class<?> clz = Class.forName("android.app.ActivityManagerNative");
//            Method getDefault = clz.getMethod("getDefault");
//            Object activityManagerNative = getDefault.invoke(null);
//
//            Method setAlwaysFinish = clz.getMethod("setAlwaysFinish", boolean.class);
//            setAlwaysFinish.invoke(activityManagerNative, enabled);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                SettingsCmds.putStringGlobal(Settings.Global.ALWAYS_FINISH_ACTIVITIES, enabled ? ENABLED : DISABLED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启指针位置
     */
    public static boolean isPointerLocation(Context context) {
        int enabled = 0;
        try {
            enabled = Settings.System.getInt(context.getContentResolver(), POINTER_LOCATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return enabled != 0;
    }

    /**
     * 开启指针位置
     */
    public static void setPointerLocation(boolean enabled) {
        try {
            SettingsCmds.putStringSystem(POINTER_LOCATION, enabled ? ENABLED : DISABLED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否显示点击操作反馈
     */
    public static boolean isShowTouches(Context context) {
        int enabled = 0;
        try {
            enabled = Settings.System.getInt(context.getContentResolver(), SHOW_TOUCHES);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return enabled != 0;
    }

    /**
     * 显示点击操作反馈
     */
    public static void setShowTouches(boolean enabled) {
        try {
            SettingsCmds.putStringSystem(SHOW_TOUCHES, enabled ? ENABLED : DISABLED);
        } catch (IOException e) {
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
    private static class SystemPropPoker extends AsyncTask<Void, Void, Void> {

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
