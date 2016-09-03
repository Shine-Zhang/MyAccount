package com.example.zs.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author  wuqi
 * 此activity为“+”后跳转的activity
 * 处理逻辑：用户支出和收入信息的输入
 */

public class AddRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        //隐藏标题栏
        getSupportActionBar().hide();
    }
}
