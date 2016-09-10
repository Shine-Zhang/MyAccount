package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zs.application.MyAplication;

public class ModifyUsernameActivity extends AppCompatActivity {

    private TextView tv_modifyusernameactivity_complete;
    private EditText et_modifyusernameactivity_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_username);
        getSupportActionBar().hide();

        tv_modifyusernameactivity_complete = (TextView) findViewById(R.id.tv_modifyusernameactivity_complete);
        et_modifyusernameactivity_username = (EditText) findViewById(R.id.et_modifyusernameactivity_username);

        //回显
        String username = MyAplication.getCurUsernameFromSp("username");
        et_modifyusernameactivity_username.setText(username);
    }

    public void complete(View view) {

        String username = et_modifyusernameactivity_username.getText().toString();

        Intent intent = getIntent();
        intent.putExtra("new_username",username);
        setResult(200,intent);
        finish();

    }
}
