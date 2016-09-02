package com.example.zs.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String tag="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(tag,"wennm");
        Log.i(tag,"wennm");

    }
    public void test(){

        Log.i("haha","云中歌");

    }

    public  void  test2(){


    }
}
