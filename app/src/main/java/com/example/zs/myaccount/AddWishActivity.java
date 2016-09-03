package com.example.zs.myaccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.view.CircleImageView;

public class AddWishActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView et_addwishactivity_mywish;
    private TextView et_addwishactivity_wishfund;
    private TextView et_addwishactivity_notes;
    private PopupWindow popupWindow;
    private CircleImageView civ_addwishactivity_image;
    private Button bt_addwishactivity_addwish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        getSupportActionBar().hide();

        et_addwishactivity_mywish = (TextView) findViewById(R.id.et_addwishactivity_mywish);
        et_addwishactivity_wishfund = (TextView) findViewById(R.id.et_addwishactivity_wishfund);
        et_addwishactivity_notes = (TextView) findViewById(R.id.et_addwishactivity_notes);
        civ_addwishactivity_image = (CircleImageView) findViewById(R.id.civ_addwishactivity_image);
        bt_addwishactivity_addwish = (Button) findViewById(R.id.bt_addwishactivity_addwish);

        //给civ_addwishactivity_image设置点击事件
        civ_addwishactivity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击相机，弹出一个popupwindow让用户选择获取图片的方式
                getPhoto(view);
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
                        !et_addwishactivity_wishfund.getText().equals("0")){
                    bt_addwishactivity_addwish.setClickable(true);
                    bt_addwishactivity_addwish.setBackgroundColor(Color.rgb(31,185,236));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 该函数用于获取图片，可以使用相机，图片库。
     * 点击页面上的相机弹出提示框，可以选择相机、图片库、取消功能
     * 获取图片之后带回图片显示在页面上
     * @param view
     */
    public void getPhoto(View view){

        //初始化popupwindow
        popupWindow = new PopupWindow();
        //popupWindow的界面
        View inflate = View.inflate(AddWishActivity.this, R.layout.popupwindow_getphoto_addwish, null);
        Button bt_addwishpopupwindow_camera =  (Button) inflate.findViewById(R.id.bt_addwishpopupwindow_camera);
        Button bt_addwishpopupwindow_gallery = (Button) inflate.findViewById(R.id.bt_addwishpopupwindow_gallery);
        Button bt_addwishpopupwindow_cancel = (Button) inflate.findViewById(R.id.bt_addwishpopupwindow_cancel);

        //获得焦点
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置popupwindow弹出和退出时的动画效果
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //将popup_view部署到popupWindow上
        popupWindow.setContentView(inflate);
        //设置popupWindow的宽高（必须要设置）
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置popupwindow显示的位置

        popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);

        //给三个按钮添加点击事件
        bt_addwishpopupwindow_camera.setOnClickListener(AddWishActivity.this);
        bt_addwishpopupwindow_gallery.setOnClickListener(AddWishActivity.this);
        bt_addwishpopupwindow_cancel.setOnClickListener(AddWishActivity.this);

    }

    /**
     * popupwindow 的三个button点击时的callback
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_addwishpopupwindow_camera:
                toCamera();
                break;
            case R.id.bt_addwishpopupwindow_gallery:
                toGallery();
                break;
            case R.id.bt_addwishpopupwindow_cancel:
                popupWindow.dismiss();
                break;
            case R.id.bt_addwishactivity_addwish:
                addWish();
        }

    }

    private void addWish() {
        //将输入的信息保存到文件中
    }

    /**
     * 该该方法用于去图库获取图片
     */
    private void toGallery() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent,100);
    }

    /**
     * 该函数用于调用系统的相机，并将拍好的照片传回来
     */
    private void toCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //系统常量， 启动相机的关键
        startActivityForResult(openCameraIntent, 200); // 参数常量为自定义的request code, 在取返回结果时有用
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
        switch (requestCode) {
            //去图库获取到的数据
            case 100:
                //获取图片的uri
                Uri uri = data.getData();
                //保存图片

                //使用自定义控件显示获取到的图片
                civ_addwishactivity_image.setImageURI(uri);
                break;

            //使用相机获取到的数据
            case 200:
                if ( resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //保存图片

                    //显示图片
                    civ_addwishactivity_image.setImageBitmap(bitmap);
                }
                break;
        }
    }

}
