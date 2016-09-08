package com.example.zs.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/7.
 */
public class WishItemCardView extends CardView {
    private static final String TAG = "WishItemCardView";

    public WishItemCardView(Context context) {
        super(context);
    }

    public WishItemCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WishItemCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //getParent().requestDisallowInterceptTouchEvent(true);
        Log.i(TAG,"dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
