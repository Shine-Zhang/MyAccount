package com.example.zs.addPage;

import android.app.Activity;
import android.view.View;

/**
 * Created by wuqi on 2016/9/4 0004.
 */
public abstract class AddBasePage {
    public View mView;
    public Activity activity;
    public boolean isJump;
    public AddBasePage(Activity activity,boolean isJump) {
        this.activity = activity;
        this.isJump = isJump;
        mView = initView();
    }
    public abstract View initView();
}
