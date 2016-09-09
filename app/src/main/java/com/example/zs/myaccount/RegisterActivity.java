package com.example.zs.myaccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zs.application.MyAplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText et_registeractivity_username;
    private EditText et_registeractivity_pwd;
    private EditText et_registeractivity_pwdAgain;
    private EditText et_registeractivity_mail;
    private SharedPreferences UserInfosp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        et_registeractivity_username = (EditText) findViewById(R.id.et_registeractivity_username);
        et_registeractivity_pwd = (EditText) findViewById(R.id.et_registeractivity_Pwd);
        et_registeractivity_pwdAgain = (EditText) findViewById(R.id.et_registeractivity_PwdAgain);
        et_registeractivity_mail = (EditText) findViewById(R.id.et_registeractivity_Mail);

    }

    public void register(View view){

        String username = et_registeractivity_username.getText().toString();
        String password = et_registeractivity_pwd.getText().toString();
        String passwordAgain = et_registeractivity_pwdAgain.getText().toString();
        String mail = et_registeractivity_mail.getText().toString();

        Log.i(TAG,"username ="+username+"  password ="+password+" passwordAgain="+passwordAgain);

        if(username.isEmpty()||password.isEmpty()||passwordAgain.isEmpty()||mail.isEmpty()){
            Toast.makeText(RegisterActivity.this, "每一项都必须填写！", Toast.LENGTH_SHORT).show();
        }else {
            if(MyAplication.getUserInfoFromSp(username).isEmpty()){
                //用户名不存在
                if(password.length()<6){
                    Toast.makeText(RegisterActivity.this, "密码长度至少为6！", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(passwordAgain)){
                        //还需判断正则表达式判断邮箱格式
                        if(isEmail(mail)){
                            Log.i(TAG,"是邮箱格式");
                            MyAplication.saveUserInfoToSp(username,password);
                            Toast.makeText(RegisterActivity.this, "注册成功，请重新登录！", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Log.i(TAG,"邮箱格式不正确");
                            Toast.makeText(RegisterActivity.this, "邮箱格式不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Log.i(TAG,"两次密码不同，请重新输入！");
                    }
                }
            }else {
                Toast.makeText(RegisterActivity.this, "用户名已经存在，请重新输入！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 正则表达式 判断邮箱格式是否正确
     * */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
