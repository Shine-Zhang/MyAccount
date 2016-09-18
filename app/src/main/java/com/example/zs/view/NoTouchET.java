package com.example.zs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by wuqi on 2016/9/9 0009.
 */
public class NoTouchET extends EditText {
    public NoTouchET(Context context) {
        super(context);
    }

    public NoTouchET(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchET(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;//super.onTouchEvent(event);
    }
}
