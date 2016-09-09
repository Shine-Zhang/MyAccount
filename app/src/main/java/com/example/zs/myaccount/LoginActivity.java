package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.application.MyAplication;
import com.example.zs.pager.OwnerPager;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private String usernameStr;
    private String passwordStr;
    private EditText et_loginactivity_username;
    private EditText et_loginactivity_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_loginactivity_username = (EditText) findViewById(R.id.et_loginactivity_username);
        et_loginactivity_pwd = (EditText) findViewById(R.id.et_loginactivity_pwd);

    }

    public void login(View view){
        usernameStr = et_loginactivity_username.getText().toString();
        passwordStr = et_loginactivity_pwd.getText().toString();
        Log.i(TAG,"usernameStr=**"+usernameStr+" passwordStr=**"+passwordStr);

        if (usernameStr.isEmpty()||passwordStr.isEmpty()){
            Toast.makeText(LoginActivity.this, "用户名和密码都不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            if(MyAplication.getUserInfoFromSp(usernameStr).isEmpty()){
                Toast.makeText(LoginActivity.this, "用户名不存在，请重新输入！", Toast.LENGTH_SHORT).show();
            }else {
                if (MyAplication.getUserInfoFromSp(usernameStr).equals(passwordStr)) {
                    Log.i(TAG,"登录成功！");
                    //带用户名传回OwnerPager
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("usernameStr",usernameStr);
                    startActivity(intent);

                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "密码不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void register(View view){
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void forgetPwd(View view){
        Toast.makeText(LoginActivity.this, "本版本暂不提供密码修改功能", Toast.LENGTH_SHORT).show();
    }

}
