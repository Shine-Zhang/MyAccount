package com.example.zs.myaccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.view.CircleImageView;

import java.io.File;

public class AddWishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddWishActivity";
    private TextView et_addwishactivity_mywish;
    private TextView et_addwishactivity_wishfund;
    private TextView et_addwishactivity_notes;
    private PopupWindow popupwindow_getphoto;
    private CircleImageView civ_addwishactivity_image;
    private Button bt_addwishactivity_addwish;
    private SharedPreferences wish;
    private ImageView iv_addwishactivity_photo;
    private static final int PHOTO_REQUEST_CAREMA = 100;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 101;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 102;// 结果
    private Uri photouri;
    private PopupWindow popupwindow_showphoto;
    private PopupWindow popupwindow_showkeyboard;

    //该变量用于存放愿望基金的字符串
    private StringBuffer wishFundString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        getSupportActionBar().hide();

        wish = getSharedPreferences("wish", MODE_PRIVATE);

        wishFundString = new StringBuffer();
        //wishFundString.append("0");

        et_addwishactivity_mywish = (TextView) findViewById(R.id.et_addwishactivity_mywish);
        et_addwishactivity_wishfund = (TextView) findViewById(R.id.et_addwishactivity_wishfund);
        et_addwishactivity_notes = (TextView) findViewById(R.id.et_addwishactivity_notes);
        civ_addwishactivity_image = (CircleImageView) findViewById(R.id.civ_addwishactivity_image);
        bt_addwishactivity_addwish = (Button) findViewById(R.id.bt_addwishactivity_addwish);
        iv_addwishactivity_photo = (ImageView) findViewById(R.id.iv_addwishactivity_photo);

        //给civ_addwishactivity_image设置点击事件
        //当用户尚未选择图片的时候，点击它会弹框让用户选择显示图片的路径
        //当用户已经选择图片之后，点击它，浏览图片，用户可以选择删除图片
        civ_addwishactivity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断用户是否
                if(iv_addwishactivity_photo.getVisibility()==View.INVISIBLE){
                    //显示图片详情
                    showPhoto(view);
                }else{
                    civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
                    iv_addwishactivity_photo.setVisibility(View.VISIBLE);
                    //点击相机，弹出一个popupwindow让用户选择获取图片的方式
                    getPhoto(view);
                    if(popupwindow_getphoto.isShowing()){
                        backgroundAlpha(0.5f);
                    }else{
                        backgroundAlpha(1.0f);
                    }
                }

            }
        });

        //给edittext设置监听，当我的愿望和愿望资金都填写时，“许下愿望”按钮变蓝，可以点击
        et_addwishactivity_mywish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当我的愿望和愿望资金都填写时，按钮变蓝
                if(!et_addwishactivity_mywish.getText().toString().isEmpty() &&
                        !et_addwishactivity_wishfund.getText().toString().isEmpty()){
                    Log.i(TAG,"mywish="+et_addwishactivity_mywish.getText().toString()+" wishfund="+et_addwishactivity_wishfund.getText());
                    bt_addwishactivity_addwish.setClickable(true);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(31,185,236));
                }else{
                    bt_addwishactivity_addwish.setClickable(false);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(229,229,229));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //愿望资金
        et_addwishactivity_wishfund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当我的愿望和愿望资金都填写时，按钮变蓝
                if(!et_addwishactivity_mywish.getText().toString().isEmpty() &&
                        !et_addwishactivity_wishfund.getText().equals("0")){
                    bt_addwishactivity_addwish.setClickable(true);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(31,185,236));
                }else{
                    bt_addwishactivity_addwish.setClickable(false);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(229,229,229));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //et_addwishactivity_wishfund的焦点变化事件，获取焦点时弹出popupwindow
        et_addwishactivity_wishfund.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showNumberKeyboard(view);
                }
            }
        });

    }

    /**
     * popupwindow 的三个button点击时的callback
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            case R.id.bt_addwishactivity_addwish:
                addWish();
                break;
            case R.id.tv_popupwindowshowphoto_delete:
                //删除图片则退出图片显示，图片恢复默认
                popupwindow_showphoto.dismiss();
                civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
                iv_addwishactivity_photo.setVisibility(View.VISIBLE);
                break;
            case R.id.et_addwishactivity_wishfund:
                showNumberKeyboard(view);
                break;
            case R.id.bt_popupwindowkekeyboard_0:
                if(et_addwishactivity_wishfund.getText().length()==0) {
                    break;
                }
                wishFundString.append("0");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_1:
                if(wishFundString.equals("0")) {
                    wishFundString.replace(0,1,"1");
                }else{
                    wishFundString.append("1");
                }
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_2:
                if(wishFundString.equals("0")) {
                    wishFundString.replace(0,1,"2");
                }else{
                    wishFundString.append("2");
                }
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_3:
                wishFundString.append("3");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_4:
                wishFundString.append("4");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_5:
                wishFundString.append("5");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_6:
                wishFundString.append("6");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_7:
                wishFundString.append("7");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_8:
                wishFundString.append("8");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_9:
                wishFundString.append("9");
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.bt_popupwindowkekeyboard_dot:
                String wishfundstring = et_addwishactivity_wishfund.getText().toString();
                if(wishfundstring.isEmpty()){
                    wishFundString.append("0.");
                }else{
                    wishFundString.append(".");
                }
                et_addwishactivity_wishfund.setText(wishFundString);
                break;
            case R.id.tv_bt_popupwindowkekeyboard_delete:
                //删除wishFundString的最后一个字符
                if(wishFundString!=null){
                    wishFundString.deleteCharAt(wishFundString.length()-1);
                    et_addwishactivity_wishfund.setText(wishFundString);
                }else {
                    break;
                }
                break;
            case R.id.bt_bt_popupwindowkekeyboard_confirm:
                et_addwishactivity_wishfund.setText(wishFundString);
                popupwindow_showkeyboard.dismiss();
        }

    }

    private void showNumberKeyboard(View view) {
        //初始化popupwindow
        popupwindow_showkeyboard = new PopupWindow();
        //加载popupwindow的界面
        View view_showkeyboard = View.inflate(AddWishActivity.this, R.layout.popupwindow_keyboard_addwish, null);

        //获得焦点
        popupwindow_showkeyboard.setFocusable(true);
        popupwindow_showkeyboard.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showkeyboard.setContentView(view_showkeyboard);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showkeyboard.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow_showkeyboard.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showkeyboard.showAtLocation(view,Gravity.BOTTOM,0,0);

        Button bt_popupwindowkekeyboard_0 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_0);
        Button bt_popupwindowkekeyboard_1 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_1);
        Button bt_popupwindowkekeyboard_2 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_2);
        Button bt_popupwindowkekeyboard_3 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_3);
        Button bt_popupwindowkekeyboard_4 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_4);
        Button bt_popupwindowkekeyboard_5 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_5);
        Button bt_popupwindowkekeyboard_6 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_6);
        Button bt_popupwindowkekeyboard_7 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_7);
        Button bt_popupwindowkekeyboard_8 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_8);
        Button bt_popupwindowkekeyboard_9 = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_9);
        Button bt_popupwindowkekeyboard_dot = (Button) view_showkeyboard.findViewById(R.id.bt_popupwindowkekeyboard_dot);
        TextView tv_popupwindowkekeyboard_delete = (TextView) view_showkeyboard.findViewById(R.id.tv_bt_popupwindowkekeyboard_delete);
        Button bt_popupwindowkekeyboard_confirm = (Button) view_showkeyboard.findViewById(R.id.bt_bt_popupwindowkekeyboard_confirm);

        //给键盘上的button添加点击事件
        bt_popupwindowkekeyboard_0.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_1.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_2.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_3.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_4.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_5.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_6.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_7.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_8.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_9.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_dot.setOnClickListener(AddWishActivity.this);
        tv_popupwindowkekeyboard_delete.setOnClickListener(AddWishActivity.this);
        bt_popupwindowkekeyboard_confirm.setOnClickListener(AddWishActivity.this);

        //给popupwindow_showkeyboard设置监听
        popupwindow_showkeyboard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                et_addwishactivity_wishfund.setText(wishFundString);
                wishFundString.replace(0,wishFundString.length()-1,"0");
            }
        });
    }

    /**
     * 该方法用于点击图片是，显示图片详情，用户可以选择删除操作
     * 实现效果由popupwindow来完成
     */
    private void showPhoto(View view) {
        //初始化popupwindow
        popupwindow_showphoto = new PopupWindow();
        //加载popupwindow的界面
        View view_showphoto = View.inflate(AddWishActivity.this, R.layout.popupwindow_showphoto_addwish, null);
        ImageView iv_popupwindowshowphoto_photo = (ImageView) view_showphoto.findViewById(R.id.iv_popupwindowshowphoto_photo);
        TextView tv_popupwindowshowphoto_delete =  (TextView) view_showphoto.findViewById(R.id.tv_popupwindowshowphoto_delete);

        //获得焦点
        popupwindow_showphoto.setFocusable(true);
        popupwindow_showphoto.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showphoto.setContentView(view_showphoto);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showphoto.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupwindow_showphoto.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showphoto.showAtLocation(view,Gravity.TOP|Gravity.LEFT,0,0);
        //加载图片
        iv_popupwindowshowphoto_photo.setImageURI(photouri);
        //给“删除”添加点击事件，
        tv_popupwindowshowphoto_delete.setOnClickListener(AddWishActivity.this);

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
        View view_getphoto = View.inflate(AddWishActivity.this, R.layout.popupwindow_getphoto_addwish, null);
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
        popupwindow_getphoto.showAtLocation(view,Gravity.BOTTOM,0,0);

        //给三个按钮添加点击事件
        bt_addwishpopupwindow_camera.setOnClickListener(AddWishActivity.this);
        bt_addwishpopupwindow_gallery.setOnClickListener(AddWishActivity.this);
        bt_addwishpopupwindow_cancel.setOnClickListener(AddWishActivity.this);

        //给popupwindow添加监听
        popupwindow_getphoto.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);

            }
        });
    }



    private void addWish() {
        //将输入的信息保存到文件中
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
        //启动相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
       /* if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }*/
        // 参数常量为自定义的requestcode, 在取返回结果时有用
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
        //去图库获取到的数据
        if(requestCode==PHOTO_REQUEST_GALLERY){
            //获取图片的全路径uri
            photouri = data.getData();
            civ_addwishactivity_image.setImageURI(photouri);
            iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
        }
        if(requestCode==PHOTO_REQUEST_CAREMA){
            //使用相机获取到的数据
            if ( resultCode == RESULT_OK) {
                // 从相机返回的数据
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //保存图片

                //显示图片
                civ_addwishactivity_image.setImageBitmap(bitmap);
                iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 判断sdcard是否被挂载
     * @return true表示sdcard可用，false表示sdcard不可用
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
