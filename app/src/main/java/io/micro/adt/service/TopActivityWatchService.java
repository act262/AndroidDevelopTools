package io.micro.adt.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.android.shell.CmdResult;
import io.android.shell.ResultCallback;
import io.android.shell.cmd.CmdSet;

/**
 * 实时跟踪当前显示的Activity
 * 参考:https://github.com/109021017/android-TopActivity
 */
public class TopActivityWatchService extends Service {
    public TopActivityWatchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String[] cmds = {"dumpsys activity | grep mFocusedActivity"};
        CmdSet.execSuAsync(cmds, new ResultCallback<CmdResult>() {
            @Override
            public void onReceiveResult(@NonNull CmdResult result) {
                String successResult = result.successResult;
                if (!TextUtils.isEmpty(successResult)) {
                    //  mFocusedActivity: ActivityRecord{426c48d0 u0 io.micro.adt/.ui.MainActivity t29}
                    String[] split = successResult.trim().split(" ");
                    String pkg = split[3];
                    ComponentName componentName = ComponentName.unflattenFromString(pkg);
                }
            }
        });
    }
}
