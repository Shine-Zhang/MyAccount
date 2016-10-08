package com.example.zs.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AnswerActivity_05 extends AppCompatActivity {

    private WebView wv_answeractivity_05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_activity_05);
        //隐藏标题栏
        //getSupportActionBar().hide();
        //初始化WebView控件
        wv_answeractivity_05 = (WebView) findViewById(R.id.wv_answeractivity_05);
        WebSettings webSettings = wv_answeractivity_05.getSettings();
        //设置可读取JavaScript网页
        webSettings.setJavaScriptEnabled(true);
        //设置可读取文件
        webSettings.setAllowFileAccess(true);
        //要跳转的网址
        wv_answeractivity_05.loadUrl("https://jizhang.yixin.com/#!/FAQ/android/5");
        //
        wv_answeractivity_05.setWebViewClient(new webViewClient());
    }
    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //给左上角回退图标设置点击方法,在XMLonClick属性调用
    public void Back_FromAnsweActivity(View view){
        finish();
    }
}
