package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class AboutUsActivity extends AppCompatActivity {

    public ImageButton ib_aboutusactivity_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //隐藏标题栏
        //getSupportActionBar().hide();
        //初始化回到上一页的ImageButton控件ib_aboutusactivity_back
        ib_aboutusactivity_back = (ImageButton) findViewById(R.id.ib_aboutusactivity_back);
    }
    //点击back按钮finish本页面
    public void from_AboutUsActivity_to_MyBalanceActivity(View view) {
        //startActivity(new Intent().setClass(this, MyBalanceActivity.class));
        finish();
    }
}