package com.czc.max.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author maxczc
 * @since 13/7/16
 */

public class RippleView extends View implements Runnable {
    private final static int MESSAGE_START = 1;
    private final static int MESSAGE_PAUSE = 2;

    private final static int DEFAULT_CONTENT_CIRCLE_RADIUS = 40;
    private final static int CIRCLE_ONE_DURATION = 1200;//ms
    private final static float CIRCLE_ONE_SCALE = 0.8F;
    private final static int CIRCLE_TWO_DURATION = 900;//ms
    private final static float CIRCLE_TWO_SCALE = 0.6F;
    private int runDelay = 0;


    private Paint contentCirclePaint;
    private Paint circleOnePaint;
    private Paint circleTwoPaint;
    private int circleOneAlpha = 255;
    private int circleTwoAlpha = 255;
    private float circleOneScale = 1;
    private float circleTwoScale = 1;

    private int circleRadius;
    private int interval;//ms
    private int circleColor;
    private boolean isAnimationStart;
    private Thread thread;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_PAUSE:
                    isAnimationStart = false;
                    initCircle();
                    invalidate();
                    break;
                case MESSAGE_START:
                    invalidate();
                    break;
            }
        }
    };

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.RippleView_rb_circle_radius
                , DEFAULT_CONTENT_CIRCLE_RADIUS);
        interval = typedArray.getInt(R.styleable.RippleView_rb_animation_interval, 1000);
        circleColor = typedArray.getColor(R.styleable.RippleView_rb_circle_color, Color.parseColor("#55AAFF"));
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        contentCirclePaint = new Paint();
        contentCirclePaint.setAntiAlias(true);
        contentCirclePaint.setColor(circleColor);
        circleOnePaint = new Paint();
        circleOnePaint.setColor(circleColor);
        circleOnePaint.setAntiAlias(true);
        circleTwoPaint = new Paint();
        circleTwoPaint.setColor(circleColor);
        circleTwoPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w, h;
        w = getMeasuredWidth();
        h = getMeasuredHeight();
        circleOnePaint.setAlpha(circleOneAlpha);
        circleTwoPaint.setAlpha(circleTwoAlpha);
        canvas.drawCircle(w / 2, h / 2, circleRadius * circleOneScale, circleOnePaint);
        canvas.drawCircle(w / 2, h / 2, circleRadius * circleTwoScale, circleTwoPaint);
        canvas.drawCircle(w / 2, h / 2, circleRadius, contentCirclePaint);
    }

    public void start() {
        if (isAnimationStart)
            return;
        isAnimationStart = true;
        thread = new Thread(this);
        thread.start();
    }


    private void initCircle() {
        circleOneAlpha = 255;
        circleTwoAlpha = 255;
        circleOneScale = 1.0f;
        circleTwoScale = 1.0f;
        runDelay = 0;
    }

    public void pauseAnimation() {
        if (!isAnimationStart || thread == null)
            return;
        thread.interrupt();
        thread = null;
        isAnimationStart = false;
        initCircle();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int w, h;
        if (widthMode == MeasureSpec.EXACTLY) {
            w = width;
        } else {
            w = (int) (circleRadius * 2 + circleRadius * 2 * 0.8f + getPaddingLeft() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            h = height;
        } else {
            h = (int) (circleRadius * 2 + circleRadius * 2 * 0.8f + getPaddingBottom() + getPaddingTop());
        }
        setMeasuredDimension(w, h);
    }

    private void setCircleParams() {
            if (circleTwoScale >= 1.6F)
                circleTwoScale = 1.6F;
            else {
                circleTwoScale += CIRCLE_TWO_SCALE / (CIRCLE_TWO_DURATION / 5);
            }
            if (circleTwoAlpha == 0 || circleTwoScale >= 1.6F)
                circleTwoAlpha = 0;
            else {
                circleTwoAlpha -= (255F / CIRCLE_TWO_DURATION) * 5;
            }
        if (runDelay >= 200) {
            if (circleOneScale >= 1.8F)
                circleOneScale = 1.8F;
            else
                circleOneScale += CIRCLE_ONE_SCALE / (CIRCLE_ONE_DURATION / 5);
            if (circleOneAlpha == 0 || circleOneScale >= 1.8F)
                circleOneAlpha = 0;
            else
                circleOneAlpha -= (255F / CIRCLE_ONE_DURATION) * 5;
        }

        handler.sendEmptyMessage(MESSAGE_START);
        try {
            if (circleOneScale >= 1.8F) {
                Thread.sleep(interval);
                initCircle();
            } else {
                runDelay += 5;
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isAnimationStart && !thread.isInterrupted()) {
            setCircleParams();
        }
    }
}
