package com.multipletouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by weiguangmeng on 16/5/17.
 */
public class MultipleTouchView extends View {
    private static final String TAG = "MultipleTouchView";
    private Paint mPaint;
    private float scale = 1.0f;
    private float radius = 40;

    public MultipleTouchView(Context context) {
        this(context, null);
    }

    public MultipleTouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        radius = radius * scale;
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint);
    }

    private int mLastX;
    private int mLastY;
    private int mFirstPointerId;
    private float mFirstDistance;
    private float mNowDistance;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;

      //  Log.d(TAG, "point count is " + event.getPointerCount());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFirstPointerId = event.getPointerId(0);
                Log.d(TAG, "first point id : " + mFirstPointerId);
                Log.d(TAG, "action down , action index: " + event.getActionIndex() + ", pointerId:" + event.getPointerId(event.getActionIndex()));
                mLastX = (int) event.getX(event.findPointerIndex(mFirstPointerId));
                mLastY = (int) event.getY(event.findPointerIndex(mFirstPointerId));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount() >= 2) {
                    float x = (event.getX(0) - event.getX(1));
                    float y = (event.getY(0) - event.getY(1));
                    mFirstDistance = x * x + y * y;
                    mFirstDistance = Math.max(1, mFirstDistance);
                    Log.d(TAG, "first scale : " + scale + ", x : " + x + ", y：" + y);
                }
              Log.d(TAG, "action down pointer, action index:" + event.getActionIndex()+ ", pointerId:" + event.getPointerId(event.getActionIndex()));
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerId(event.getActionIndex()) == mFirstPointerId) {
                    int x = (int) event.getX(event.findPointerIndex(mFirstPointerId));
                    int y = (int) event.getY(event.findPointerIndex(mFirstPointerId));
                    int offsetX = x - mLastX;
                    int offsetY = y - mLastY;
                    if (event.getPointerCount() == 1) {
                        scrollBy(-offsetX, -offsetY);
                    } else if(event.getPointerCount() >= 2) {
                        float disX = (event.getX(0) - event.getX(1));
                        float disY = (event.getY(0) - event.getY(1));
                        mNowDistance = disX * disX + disY * disY;
                        mNowDistance = Math.max(1, mNowDistance);

                        scale = (float) Math.sqrt(mNowDistance / mFirstDistance);
                        Log.d(TAG, "scale : " + scale + ", x : " + x + ", y：" + y);
                   //     Log.d(TAG, "scale : " + scale + ", mNowDistance : " + mNowDistance + ", mFirstDistance："+mFirstDistance);
                        invalidate();
                        mFirstDistance = mNowDistance;

                    }
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up, action index is " + event.getActionIndex()+ ", pointerId:" + event.getPointerId(event.getActionIndex()));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                scale = 1;
                Log.d(TAG, "action up pointer, action index is " + event.getActionIndex()+ ", pointerId:" + event.getPointerId(event.getActionIndex()));
                break;
        }
        return true;
    }
}
