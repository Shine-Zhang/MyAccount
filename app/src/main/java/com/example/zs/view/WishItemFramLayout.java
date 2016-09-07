package com.example.zs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/9/7.
 */
public class WishItemFramLayout extends FrameLayout{
    public WishItemFramLayout(Context context) {
        super(context);
    }

    public WishItemFramLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
