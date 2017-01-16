package io.micro.adt.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import io.micro.adt.ui.MainActivity;
import io.micro.adt.widget.FloatBallView;

/**
 * 悬浮球后台服务，保证悬浮球的刷新
 */
public class FloatBallService extends Service implements View.OnClickListener, FloatBallView.OnTouchViewUpdater {

    private FloatBallView floatBallView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatBall();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        hideFloatBall();
        super.onDestroy();
    }

    private void showFloatBall() {
        if (floatBallView == null) {
            initFloatBall();
        }

        if (floatBallView.getParent() == null) {
            windowManager.addView(floatBallView, layoutParams);
        }
    }

    private void initFloatBall() {
        floatBallView = new FloatBallView(this);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP | GravityCompat.START;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        // 显示半透明
        layoutParams.alpha = 0.5f;
        layoutParams.format = PixelFormat.TRANSPARENT;

        // 使用最后一次显示过的位置
        layoutParams.x = preferences.getInt(LAST_POSITION_X, 100);
        layoutParams.y = preferences.getInt(LAST_POSITION_Y, 100);

        floatBallView.setOnClickListener(this);
        floatBallView.setOnTouchViewUpdater(this);
    }

    private void hideFloatBall() {
        if (floatBallView != null) {
            applyLastPosition();
            windowManager.removeView(floatBallView);
        }
    }

    @Override
    public void onClick(View v) {
        MainActivity.goHome(getApplicationContext());
    }

    @Override
    public void onTouchMoved(int offsetX, int offsetY) {
        layoutParams.x += offsetX;
        layoutParams.y += offsetY;
        windowManager.updateViewLayout(floatBallView, layoutParams);
    }

    // 保存最后一次显示的位置
    private void applyLastPosition() {
        preferences.edit()
                .putInt(LAST_POSITION_X, layoutParams.x)
                .putInt(LAST_POSITION_Y, layoutParams.y)
                .apply();
    }

    public static final String LAST_POSITION_X = "float_ball_position_X";
    public static final String LAST_POSITION_Y = "float_ball_position_Y";
}
