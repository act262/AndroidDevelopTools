package io.micro.adt.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import io.android.shell.CmdResult;
import io.android.shell.ResultCallback;
import io.android.shell.cmd.CmdSet;

/**
 * 实时跟踪当前显示的Activity
 * 参考:https://github.com/109021017/android-TopActivity
 *
 * @author act262@gmail.com
 */
public class TopActivityWatchService extends Service {

    private TextView mTextView;
    private WindowManager mWindowManager;

    private Handler mHandler = new Handler();

    private static final String[] CMDS = {"dumpsys activity | grep mFocusedActivity"};

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initView();
        startTracking();
    }

    private void initView() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mTextView = new TextView(this);
        mTextView.setPadding(10, 10, 10, 10);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setBackgroundColor(Color.GRAY);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP | GravityCompat.START;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.alpha = 0.8f;
        layoutParams.format = PixelFormat.TRANSPARENT;

        mWindowManager.addView(mTextView, layoutParams);
    }

    private void startTracking() {
        mHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                trackTopActivity();

                mHandler.postDelayed(this, 1000L);
            }
        }, 1000L);
    }

    private void trackTopActivity() {
        CmdSet.execSuAsync(CMDS, new ResultCallback<CmdResult>() {
            @Override
            public void onReceiveResult(@NonNull CmdResult result) {
                String successResult = result.successResult;
                if (!TextUtils.isEmpty(successResult)) {
                    processResult(successResult);
                }
            }
        });
    }

    private void processResult(String result) {
        //  mFocusedActivity: ActivityRecord{426c48d0 u0 io.micro.adt/.ui.MainActivity t29}
        String[] split = result.trim().split(" ");
        String pkg = split[3];
        ComponentName componentName = ComponentName.unflattenFromString(pkg);
        mTextView.setText(componentName.flattenToString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mWindowManager.removeViewImmediate(mTextView);
    }

}
