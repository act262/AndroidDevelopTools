package io.micro.adt.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import io.micro.adt.ui.MainActivity;
import io.micro.adt.widget.FloatBallView;

/**
 * 悬浮球后台服务，保证悬浮球的刷新
 */
public class FloatBallService extends Service {

    private FloatBallView floatBallView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    @Override
    public void onCreate() {
        super.onCreate();
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
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.x = 100;
        layoutParams.y = 100;

        floatBallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFloatBall();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        floatBallView.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY;
            float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        lastX = startX;
                        lastY = startY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float tempX = event.getRawX();
                        float tempY = event.getRawY();
                        layoutParams.x += (int) (tempX - lastX);
                        layoutParams.y += (int) (tempY - lastY);
                        lastX = tempX;
                        lastY = tempY;
                        windowManager.updateViewLayout(floatBallView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        return Math.abs(event.getRawX() - startX) > 10 && Math.abs(event.getRawY() - startY) > 10;
                }
                return false;
            }
        });
    }

    private void hideFloatBall() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (floatBallView != null && floatBallView.isAttachedToWindow()) {
            windowManager.removeView(floatBallView);
        }
        Intent intent = new Intent(MainActivity.ACTION_FLOAT_BALL_SWITCH_CHANGED);
        intent.putExtra("switch", false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
