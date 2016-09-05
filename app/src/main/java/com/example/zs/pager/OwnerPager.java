package com.example.zs.pager;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 钟云婷 on 2016/9/2.
 */

public class OwnerPager extends BasePager {

    public OwnerPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

        TextView textView = new TextView(mActivity);
        textView.setText("HelloWorld");
        return textView;
    }

    @Override
    public void initData() {

    }
}
