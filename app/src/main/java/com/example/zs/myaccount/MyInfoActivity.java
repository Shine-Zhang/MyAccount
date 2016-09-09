package com.example.zs.myaccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.utils.ScreenUtils;
import com.example.zs.view.CircleImageView;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyInfoActivity";
    private String password;
    private TextView tv_myinfoactivity_username;
    private static final int PHOTO_REQUEST_CAREMA = 100;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 101;// 从相册中选择
    private PopupWindow popupwindow_getphoto;
    private Uri photoUri;
    private PopupWindow popupwindow_showphoto;
    private CircleImageView iv_myinfoactivity_touxiang;
    private CircleImageView civ_myinfoactivity_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        getSupportActionBar().hide();

        tv_myinfoactivity_username = (TextView) findViewById(R.id.tv_myinfoactivity_username);
        iv_myinfoactivity_touxiang = (CircleImageView) findViewById(R.id.iv_myinfoactivity_touxiang);
        civ_myinfoactivity_img = (CircleImageView) findViewById(R.id.civ_myinfoactivity_img);
        //回显
        String username = MyAplication.getCurUsernameFromSp("username");
        tv_myinfoactivity_username.setText(username);
        Uri photoUri = Uri.parse(MyAplication.getCurUsernameFromSp("photoUri"));
        iv_myinfoactivity_touxiang.setImageURI(photoUri);
    }

    public void modTouxiang(View view){
        Log.i(TAG,"修改头像");

        //判断用户是否已经选择图片
        if(iv_myinfoactivity_touxiang.getVisibility()==View.INVISIBLE){
            //显示图片详情
            showPhoto(view, photoUri);
        }else{
            civ_myinfoactivity_img.setImageResource(R.drawable.blankrect);
            iv_myinfoactivity_touxiang.setVisibility(View.VISIBLE);
            //点击相机，弹出一个popupwindow让用户选择获取图片的方式
            getPhoto(view);
            if(popupwindow_getphoto.isShowing()){
                ScreenUtils.backgroundAlpha(MyInfoActivity.this,0.5f);
            }else{
                ScreenUtils.backgroundAlpha(MyInfoActivity.this,1.0f);
            }
        }

    }
    public void modUsername(View view){
        Log.i(TAG,"修改用户名");
        //获取当前用户名
        String uaername = MyAplication.getStringFromSp("username");
        //根据用户名获取密码
        password = MyAplication.getUserInfoFromSp(uaername);
        Log.i(TAG,"uaername="+uaername+" password="+ password);

        startActivityForResult(new Intent(this,ModifyUsernameActivity.class),100);

    }

    public void exit(View view){
        Log.i(TAG,"退出登录");
        String filePath = "/data/data/"+getPackageName()+"/shared_prefs/currentUsername.xml";
        File file = new File(filePath);
        Log.i(TAG,file.exists()+"");
        if(file.exists()){
            file.delete();
            Log.i(TAG,"currentUsername删除成功");
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    public void back(View v){
        startActivity(new Intent(this,MainActivity.class));
    }

    /**
     * 该函数用于获取图片，可以使用相机，图片库。
     * 点击页面上的相机弹出提示框，可以选择相机、图片库、取消功能
     * 获取图片之后带回图片显示在页面上
     * @param view
     */
    public void getPhoto(View view){

        //初始化popupwindow
        popupwindow_getphoto = new PopupWindow();
        //popupWindow的界面
        View view_getphoto = View.inflate(MyInfoActivity.this, R.layout.popupwindow_getphoto_addwish, null);
        Button bt_addwishpopupwindow_camera =  (Button) view_getphoto.findViewById(R.id.bt_addwishpopupwindow_camera);
        Button bt_addwishpopupwindow_gallery = (Button) view_getphoto.findViewById(R.id.bt_addwishpopupwindow_gallery);
        Button bt_addwishpopupwindow_cancel = (Button) view_getphoto.findViewById(R.id.bt_addwishpopupwindow_cancel);

        //获得焦点
        popupwindow_getphoto.setFocusable(true);
        popupwindow_getphoto.setBackgroundDrawable(new BitmapDrawable());
        //设置popupwindow弹出和退出时的动画效果
        popupwindow_getphoto.setAnimationStyle(R.style.AnimationBottomFade);
        //将popup_view部署到popupWindow上
        popupwindow_getphoto.setContentView(view_getphoto);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_getphoto.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow_getphoto.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_getphoto.showAtLocation(view, Gravity.BOTTOM,0,0);

        //给三个按钮添加点击事件

        bt_addwishpopupwindow_camera.setOnClickListener(MyInfoActivity.this);
        bt_addwishpopupwindow_gallery.setOnClickListener(MyInfoActivity.this);
        bt_addwishpopupwindow_cancel.setOnClickListener(MyInfoActivity.this);

        //给popupwindow添加监听
        popupwindow_getphoto.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ScreenUtils.backgroundAlpha(MyInfoActivity.this,1.0f);
            }
        });
    }




    /**
     * 该方法用于去图库获取图片
     */
    private void toGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
    }

    /**
     * 该函数用于调用系统的相机，并将拍好的照片传回来
     */
    private void toCamera() {

        String path = Environment.getExternalStorageDirectory() + "/MyAccount/";
        String fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        new DateFormat();
        fileName= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
        photoUri =  Uri.fromFile(new File(path + fileName));
        Log.i("wwwwwwww","使用相机前  uri="+photoUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);


    }

    /**
     * 该函数用于获取传回来的数据。
     * 即 跳转到其他地方之后获取到想要的信息，将信息传回来
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==100&&resultCode==200) {
            String username = data.getStringExtra("new_username");
            tv_myinfoactivity_username.setText(username);

            //将新的用户名和密码保存到config文件中
            MyAplication.saveUserInfoToSp(username,password);
            //将新的用户名保存到当前用户名中
            MyAplication.saveCurUsernaemToSp("username",username);

        }

        Log.i("wwwwwwwwwwwwwww","onActivityResult requestCode="+requestCode+"  resultCode="+resultCode+"   data="+data);
        //去图库获取到的数据
        if(requestCode==PHOTO_REQUEST_GALLERY){
            if(resultCode==RESULT_OK){
                if(data!=null){
                    if(data.hasExtra("data")){
                        Bitmap bitmap = data.getParcelableExtra("data");
                        civ_myinfoactivity_img.setImageBitmap(bitmap);
                    }
                    //获取图片的全路径uri
                    photoUri = data.getData();
                    Log.i("wwwwwwww","调用图库  uri="+photoUri);
                    civ_myinfoactivity_img.setImageURI(photoUri);
                    iv_myinfoactivity_touxiang.setVisibility(View.INVISIBLE);
                    MyAplication.saveCurUsernaemToSp("photoUri",photoUri+"");

                }else{
                    return;
                }
            }
        }
        //照相
        if(requestCode==PHOTO_REQUEST_CAREMA){
            if(resultCode==RESULT_OK){
                civ_myinfoactivity_img.setImageURI(photoUri);
                iv_myinfoactivity_touxiang.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 该方法用于点击图片是，显示图片详情，用户可以选择删除操作
     * 实现效果由popupwindow来完成
     */
    private void showPhoto(View view,Uri photouri) {

        //初始化popupwindow
        popupwindow_showphoto = new PopupWindow();
        //加载popupwindow的界面
        View view_showphoto = View.inflate(MyInfoActivity.this, R.layout.popupwindow_showphoto_addwish, null);
        ImageView iv_popupwindowshowphoto_photo = (ImageView) view_showphoto.findViewById(R.id.iv_popupwindowshowphoto_photo);
        TextView tv_popupwindowshowphoto_delete =  (TextView) view_showphoto.findViewById(R.id.tv_popupwindowshowphoto_delete);

        //获得焦点
        popupwindow_showphoto.setFocusable(true);
        popupwindow_showphoto.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showphoto.setContentView(view_showphoto);
        //设置popupWindow的宽高（必须要设置）
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showphoto.setHeight(ScreenUtils.getScreenHeight(this)- ScreenUtils.getStatusBarHeight(this));
        popupwindow_showphoto.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showphoto.showAtLocation(view, Gravity.BOTTOM,0,0);
        //加载图片
        iv_popupwindowshowphoto_photo.setImageURI(photouri);
        //给“删除”添加点击事件，
        tv_popupwindowshowphoto_delete.setOnClickListener(MyInfoActivity.this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_addwishpopupwindow_camera:
                toCamera();
                popupwindow_getphoto.dismiss();
                break;
            case R.id.bt_addwishpopupwindow_gallery:
                toGallery();
                popupwindow_getphoto.dismiss();
                break;
            case R.id.bt_addwishpopupwindow_cancel:
                popupwindow_getphoto.dismiss();
                break;
        }
    }
}
