package com.example.zs.myaccount;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zs.application.MyAplication;
import com.example.zs.pager.BasePager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        PlatformActionListener, Handler.Callback, TextWatcher {

    private static final String TAG = "LoginActivity";
    private static final int MSG_ACTION_CCALLBACK = 1;
    private static final int MSG_TOAST = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private String usernameStr;
    private String passwordStr;
    private EditText et_loginactivity_username;
    private EditText et_loginactivity_pwd;
    private LinearLayout ll_loginactivity_weiboLogin;
    private LinearLayout ll_loginactivity_QQLogin;
    private Button bt_loginactivity_login;
    private String userName = "";
    private String userIcon = "";
    private Handler mhandler;
    private Activity mActivity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        et_loginactivity_username = (EditText) findViewById(R.id.et_loginactivity_username);
        et_loginactivity_pwd = (EditText) findViewById(R.id.et_loginactivity_pwd);
        ll_loginactivity_weiboLogin = (LinearLayout) findViewById(R.id.ll_loginactivity_weiboLogin);
        ll_loginactivity_QQLogin = (LinearLayout) findViewById(R.id.ll_loginactivity_QQLogin);
        bt_loginactivity_login = (Button) findViewById(R.id.bt_loginactivity_login);

        ll_loginactivity_weiboLogin.setOnClickListener(this);
        ll_loginactivity_QQLogin.setOnClickListener(this);

        //监听EditText的长度，修改登录按钮的背景颜色
        et_loginactivity_username.addTextChangedListener(this);
        et_loginactivity_pwd.addTextChangedListener(this);

        //Mob平台授权
        ShareSDK.initSDK(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_loginactivity_weiboLogin:
                Log.i(TAG,"weiboLogin");
                thirdSinaLogin();
                break;
            case R.id.ll_loginactivity_QQLogin:
                Log.i(TAG,"QQLogin");
                thirdQQLogin();
                break;
        }
    }

    /** 新浪微博授权、获取用户信息页面 */
    private void thirdSinaLogin() {
        //初始化新浪平台
        Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
        weibo.SSOSetting(false);//设置false表示使用SSO授权方式
        //设置监听
        weibo.setPlatformActionListener(this);
        //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
        weibo.authorize();
    }

    /** QQ空间授权、获取用户信息页面 */
    private void thirdQQLogin() {
        //初始化QQ空间平台
        Platform qZone = ShareSDK.getPlatform(this, QZone.NAME);
        qZone.SSOSetting(false);//设置false表示使用SSO授权方式
        //设置监听
        qZone.setPlatformActionListener(this);
        //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
        qZone.authorize();
        finishCurrentActivity();
    }

    /** 授权成功回调页面 */
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        /** res是返回的数据，例如showUser(null),返回用户信息，对其解析就行
         *   http://sharesdk.cn/androidDoc/cn/sharesdk/framework/PlatformActionListener.html
         *
         */

        PlatformDb platDB = platform.getDb();//获取数平台数据DB
        //通过DB获取各种数据
        String userId = platDB.getUserId(); //获取用户id
        //获取用户名称
        userName = platDB.getUserName();
        //获取用户头像
        userIcon = platDB.getUserIcon();
        Log.i(TAG,"授权成功"+"\n"+"用户id:" + userId + "\n" +
                "获取用户名称：" + userName + "\n" +
                "获取用户头像：" + userIcon);

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;

        UIHandler.sendMessage(msg, this);
        //finish();
    }

    private void finishCurrentActivity() {
        //创建handler
      new Thread() {
            @Override
            public void run() {
                Log.i("lalalalala","执行到子线程啦！");
               /* try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                SystemClock.sleep(5);
                LoginActivity.this.finish();
            }
        }.start();

    }

    /** 取消授权 */
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }
    /** 授权失败 */
    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        t.getMessage();
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1:
                        Log.i(TAG+"zzzzzz","登录成功！");
                        //登录成功就保存用户信息到当前用户文件中
                        MyAplication.saveCurUsernaemToSp("username",userName);
                        MyAplication.saveUserInfoToSp(userName+"PhotoUri",userIcon);

                        MyAplication myAplication = (MyAplication) getApplication();
                        BasePager ownerPager = myAplication.getOwnerPager();
                        if (ownerPager!=null) {
                            ownerPager.initData();
                            Log.i(TAG+"zzzzz","第三方登录刷新");
                        }

                        break;
                    case 2: {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case 3: {
                        // 取消, cancel notification
                        Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }


    public void login(View view){
        if (usernameStr==""||passwordStr==""){
            Toast.makeText(LoginActivity.this, "用户名和密码都不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            if(MyAplication.getUserInfoFromSp(usernameStr).isEmpty()){
                Toast.makeText(LoginActivity.this, "用户名不存在，请重新输入！", Toast.LENGTH_SHORT).show();
            }else {
                //如果根据用户名取得的密码与用户输入的密码相同，则登录成功，否则显示密码不正确
                if (MyAplication.getUserInfoFromSp(usernameStr).equals(passwordStr)) {
                    Log.i(TAG,"登录成功！");

                    //登录成功就立即将用户名保存到当前用户的临时文件中，用于回显
                    MyAplication.saveCurUsernaemToSp("username",usernameStr);
                    MyAplication myAplication = (MyAplication) getApplication();
                    BasePager ownerPager = myAplication.getOwnerPager();
                    if (ownerPager!=null) {
                        ownerPager.initData();
                        Log.i(TAG+"zzzz","LoginActivity login!");
                    }

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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(TAG,"beforeTextChanged");

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(TAG,"onTextChanged");

        usernameStr = et_loginactivity_username.getText().toString();
        passwordStr = et_loginactivity_pwd.getText().toString();
        Log.i(TAG,"usernameStr=**"+usernameStr+" passwordStr=**"+passwordStr);
        //当用户输入的字符串长度大于5时，登录Button的背景颜色更改
        if(passwordStr.length()>5){
            bt_loginactivity_login.setBackgroundColor(Color.parseColor("#30C9F2"));
        }else {
            bt_loginactivity_login.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i(TAG,"afterTextChanged");

    }

    public void back(View v){
        //点击返回Button，返回到指定的viewPager页面
        MainActivity.vp_mainactivity.setCurrentItem(3);
        finish();
    }

}
