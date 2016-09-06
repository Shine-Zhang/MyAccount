package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackAgent;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class FeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //设置您应用的 Application ID 和 Key，实现初始化
        AVOSCloud.initialize(this, "XdUn7xGGuBkC15wYhCcU8NaH-gzGzoHsz", "qiHBHxT1TscRXpmKmapTsz4t");
        //启用用户反馈模块
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.startDefaultThreadActivity();
        //当用户收到开发者的新回复时，就会产生一个新的消息通知。
        agent.sync();
        finish();

    }
}
