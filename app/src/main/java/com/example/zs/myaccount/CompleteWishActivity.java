package com.example.zs.myaccount;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CompleteWishActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_wish);
        getSupportActionBar().hide();

        ImageButton imgbt_completewishactivity_back = (ImageButton) findViewById(R.id.imgbt_completewishactivity_back);
        imgbt_completewishactivity_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbt_completewishactivity_back:
                finish();
        }


    }
}
