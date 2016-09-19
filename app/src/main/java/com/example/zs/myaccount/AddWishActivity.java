package com.example.zs.myaccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.bean.WishInfo;
import com.example.zs.dao.OnGoingWishDao;
import com.example.zs.pager.WishPager;
import com.example.zs.utils.KeyboardUtil;
import com.example.zs.utils.ScreenUtils;
import com.example.zs.utils.ShowPopupWindowUtils;
import com.example.zs.view.CircleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.zs.application.MyAplication.setWishInfo;

public class AddWishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddWishActivity";
    private TextView et_addwishactivity_mywish;
    private EditText et_addwishactivity_wishfund;
    private TextView et_addwishactivity_description;
    private PopupWindow popupwindow_getphoto;
    private CircleImageView civ_addwishactivity_image;
    private Button bt_addwishactivity_addwish;
    private ImageView iv_addwishactivity_photo;
    private static final int PHOTO_REQUEST_CAREMA = 100;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 101;// 从相册中选择
    private PopupWindow popupwindow_showphoto;
    private PopupWindow popupwindow_showkeyboard;

    //该变量用于存放愿望基金的字符串
    private StringBuffer wishFundString;
    private FileOutputStream fileOutputStream = null;
    private InputMethodManager imm;
    private Uri photoUri;
    private int wishid;
    private OnGoingWishDao onGoingWishDAO;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        getSupportActionBar().hide();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        onGoingWishDAO = new OnGoingWishDao(this);

        wishFundString = new StringBuffer();

        et_addwishactivity_mywish = (TextView) findViewById(R.id.et_addwishactivity_mywish);
        et_addwishactivity_wishfund = (EditText) findViewById(R.id.et_addwishactivity_wishfund);
        et_addwishactivity_description = (TextView) findViewById(R.id.et_addwishactivity_description);
        civ_addwishactivity_image = (CircleImageView) findViewById(R.id.civ_addwishactivity_image);
        bt_addwishactivity_addwish = (Button) findViewById(R.id.bt_addwishactivity_addwish);
        iv_addwishactivity_photo = (ImageView) findViewById(R.id.iv_addwishactivity_photo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String from = bundle.getString("from");
        if(from.equals("add")){
            //点击“添加”过来的
            iv_addwishactivity_photo.setVisibility(View.VISIBLE);
            civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
        }else {
            //从愿望详情页过来的
            String title = bundle.getString("title","");
            String description = bundle.getString("description","");
            String wishfund = bundle.getString("wishfund","");
            String photoUri = bundle.getString("photoUri","");
            wishid = bundle.getInt("wishid");
            position = bundle.getInt("position");

            et_addwishactivity_mywish.setText(title);
            Log.i("wwwwwwwww","addwish  wishtitle="+title);
            et_addwishactivity_description.setText(description);
            et_addwishactivity_wishfund.setText(wishfund);

            Log.i("wwwwwwwww","addwish  photoid="+photoUri);
            if(photoUri.isEmpty() || photoUri.equals("0") || photoUri.equals("null")){
                iv_addwishactivity_photo.setVisibility(View.VISIBLE);
                civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
            }else {
                iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
                civ_addwishactivity_image.setImageURI(Uri.parse(photoUri));
            }
            bt_addwishactivity_addwish.setClickable(true);
            bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(31,185,236));
            bt_addwishactivity_addwish.setOnClickListener(AddWishActivity.this);

        }

        //给civ_addwishactivity_image设置点击事件
        //当用户尚未选择图片的时候，点击它会弹框让用户选择显示图片的路径
        //当用户已经选择图片之后，点击它，浏览图片，用户可以选择删除图片
        civ_addwishactivity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断用户是否已经选择图片
                if(iv_addwishactivity_photo.getVisibility()==View.INVISIBLE){
                    //显示图片详情
                    showPhoto(view, photoUri);
                }else{
                    civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
                    iv_addwishactivity_photo.setVisibility(View.VISIBLE);
                    //点击相机，弹出一个popupwindow让用户选择获取图片的方式
                    getPhoto(view);
                    if(popupwindow_getphoto.isShowing()){
                        ScreenUtils.backgroundAlpha(AddWishActivity.this,0.5f);
                    }else{
                        ScreenUtils.backgroundAlpha(AddWishActivity.this,1.0f);
                    }
                }

            }
        });

        //给edittext设置监听，当我的愿望和愿望资金都填写时，“许下愿望”按钮变蓝，可以点击
        //我的愿望
        et_addwishactivity_mywish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当我的愿望和愿望资金都填写时，按钮变蓝
                if(!et_addwishactivity_mywish.getText().toString().isEmpty() &&
                        !et_addwishactivity_wishfund.getText().toString().isEmpty()){
                    Log.i(TAG,"mywish="+et_addwishactivity_mywish.getText().toString()
                            +" wishfund="+et_addwishactivity_wishfund.getText());
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
                        !et_addwishactivity_wishfund.getText().toString().isEmpty()){
                    bt_addwishactivity_addwish.setClickable(true);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(31,185,236));
                    //给“许下愿望”按钮添加点击监听
                    bt_addwishactivity_addwish.setOnClickListener(AddWishActivity.this);
                }else{
                    bt_addwishactivity_addwish.setClickable(false);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(229,229,229));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //et_addwishactivity_wishfund的焦点变化事件，获取焦点时弹出自定义键盘
        et_addwishactivity_wishfund.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    //禁用系统键盘
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(et_addwishactivity_wishfund.getWindowToken(), 0);
                    }
                    //显示自定义键盘
                    int inputback = et_addwishactivity_wishfund.getInputType();
                    et_addwishactivity_wishfund.setInputType(InputType.TYPE_NULL);
                   //KeyboardUtil keyboardUtil = new KeyboardUtil(AddWishActivity.this, AddWishActivity.this, et_addwishactivity_wishfund);
                   //keyboardUtil.setNumberFormat(7);
                   // keyboardUtil.showKeyboard();
                    et_addwishactivity_wishfund.setInputType(inputback);

                }
            }
        });
        et_addwishactivity_wishfund.setOnClickListener(this);



    }

    /**
     * OnClickListener的callback
     * 点击事件
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
                //关闭当前页面，刷新数据库
                finish();
                break;
            case R.id.tv_popupwindowshowphoto_delete:
                //删除图片则退出图片显示，图片恢复默认
                /*if(tempFile.exists()){
                    tempFile.delete();
                }*/
                popupwindow_showphoto.dismiss();
                civ_addwishactivity_image.setImageResource(R.drawable.blankrect);
                iv_addwishactivity_photo.setVisibility(View.VISIBLE);
                break;
            case R.id.et_addwishactivity_wishfund:
                //showNumberKeyboard(view);
                break;

        }

    }

    /**
     * 点击标题栏的返回键
     * 直接关闭activity
     * @param view
     */
    public void back(View view){
        Intent intent = new Intent();
        intent.getBooleanExtra("hasaddwish",false);
        setResult(112,intent);
        finish();
    }

    /**
     * 添加愿望，放入数据库
     */
    private void addWish() {
        if(wishid!=0){
            //如果是从愿望详情页过来的
            //删除之前的数据，再新增
            onGoingWishDAO.deleteOnGoingWishInfo(wishid);
        }
        //将输入的信息保存到数据库中
        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //获取当前日期:
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        Log.i(TAG,"year="+year+",month="+month+",day="+day);

        String wishtitle = et_addwishactivity_mywish.getText().toString();
        String wishfund = et_addwishactivity_wishfund.getText().toString();
        String wishdescription = et_addwishactivity_description.getText().toString();
        String photouri = String.valueOf(photoUri);

        WishInfo wishInfo = new WishInfo(year,month,day,wishtitle,wishdescription,wishfund,photouri);
        //添加到数据库
        onGoingWishDAO.addOnGoingWishInfo(wishInfo);

        Intent intent = new Intent();
        intent.getBooleanExtra("hasaddwish",true);
        setResult(112,intent);
        Log.i("wwwwwwww","当前为添加愿望页面，给前一个页面回传数据---setResult(112,intent)");

    }

    /**
     * 当愿望资金的EditText获得焦点时，弹出自定义的键盘。
     * @param view
     */
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
                wishFundString.replace(0,wishFundString.length(),"0");
            }
        });
    }

    /**
     * 该方法用于点击图片是，显示图片详情，用户可以选择删除操作
     * 实现效果由popupwindow来完成
     */
    private void showPhoto(View view,Uri photouri) {

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
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showphoto.setHeight(ScreenUtils.getScreenHeight(this)- ScreenUtils.getStatusBarHeight(this));
        popupwindow_showphoto.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showphoto.showAtLocation(view, Gravity.BOTTOM,0,0);
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
                ScreenUtils.backgroundAlpha(AddWishActivity.this,1.0f);
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
        //启动相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            if(resultCode==RESULT_OK){
                if(data!=null){
                    if(data.hasExtra("data")){
                        Bitmap bitmap = data.getParcelableExtra("data");
                        civ_addwishactivity_image.setImageBitmap(bitmap);
                    }
                }else{

                }
            }
            //获取图片的全路径uri
            photoUri = data.getData();
            civ_addwishactivity_image.setImageURI(photoUri);
            iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
        }
        if(requestCode==PHOTO_REQUEST_CAREMA){
            // 获取相机返回的数据，并转换为bitmap图片格式
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //保存图片
            //创建存储照片的文件夹
            File file = new File(Environment.getExternalStorageDirectory() + "/MyAccount/");
            file.mkdirs();
            //创建照片的名称
            //图片名称,根据生成时间命名
            String fileName = Environment.getExternalStorageDirectory() + "/MyAccount/" + System.currentTimeMillis() + ".jpg";

                try {
                    fileOutputStream = new FileOutputStream(fileName);
                    // 把数据写入文件
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 1000, fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        // 关闭流
                        if(fileOutputStream!=null)
                            fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            // 从文件中创建uri
            //photoUri = Uri.fromFile(fileName);
            // Log.i(TAG,"photouri="+photoUri);
            //显示图片
            //civ_addwishactivity_image.setImageURI(photoUri);
            iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
            civ_addwishactivity_image.setImageBitmap(bitmap);

        }
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
