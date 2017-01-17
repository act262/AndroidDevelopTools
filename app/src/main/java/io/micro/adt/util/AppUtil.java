package io.micro.adt.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 *
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
}
