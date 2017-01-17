package io.micro.adt.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 悬浮球
 */
public class FloatBallView extends View {

    // 固定宽高dp
    public static final float WIDTH = 33;

    private Paint mPaint;
    private float radius;

    // 记录View的坐标
    private int lastX, lastY;
    private int startX, startY;
    private int touchSlop;

    private OnTouchViewUpdater callback;

    public FloatBallView(Context context) {
        this(context, null);
    }

    public FloatBallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH, getResources().getDisplayMetrics());
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(width, width);
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }

    public void setOnTouchViewUpdater(OnTouchViewUpdater callback) {
        this.callback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int tempX = (int) event.getRawX();
        int tempY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = startX = tempX;
                lastY = startY = tempY;
                break;
            case MotionEvent.ACTION_MOVE:
                callback.onTouchMoved(tempX - lastX, tempY - lastY);
                lastX = tempX;
                lastY = tempY;
                break;
            case MotionEvent.ACTION_UP:
                return Math.abs(lastX - startX) > touchSlop
                        || Math.abs(lastY - startY) > touchSlop
                        || super.onTouchEvent(event); // 不会吃掉click事件
            default:
                // no-op
        }
        return super.onTouchEvent(event);
    }

    public interface OnTouchViewUpdater {
        /**
         * @param offsetX 触摸水平方向偏移量
         * @param offsetY 触摸垂直方向偏移量
         */
        void onTouchMoved(int offsetX, int offsetY);
    }
}
