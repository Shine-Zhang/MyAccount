package com.example.zs.addPage;

import android.content.Context;
import android.view.View;

/**
 * Created by wuqi on 2016/9/4 0004.
 */
public abstract class AddBasePage {
    public View mView;
    Context ctx;

    public AddBasePage(Context ctx) {
        this.ctx = ctx;
        mView = initView();
    }
    public abstract View initView();
}
