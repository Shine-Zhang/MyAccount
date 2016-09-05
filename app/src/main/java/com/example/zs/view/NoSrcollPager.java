package com.example.zs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wuqi on 2016/9/5 0005.
 * 重写viewpager的方法，静止其处理处理事件
 */
public class NoSrcollPager extends ViewPager {
    public NoSrcollPager(Context context) {
        super(context);
    }

    public NoSrcollPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * If we return true, onMotionEvent will be called and we do the actual
     * scrolling there.
     * 不拦截事件，且防止滑动bug
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
