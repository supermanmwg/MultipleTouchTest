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

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint);
    }

    private int mLastX;
    private int mLastY;
    private int mFirstPointerId;
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
                    }
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up, action index is " + event.getActionIndex()+ ", pointerId:" + event.getPointerId(event.getActionIndex()));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "action up pointer, action index is " + event.getActionIndex()+ ", pointerId:" + event.getPointerId(event.getActionIndex()));
                break;
        }
        return true;
    }
}
