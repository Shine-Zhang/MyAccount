package com.example.zs.myaccount;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.addPage.AddBasePage;
import com.example.zs.addPage.IncomePage;
import com.example.zs.addPage.PayOutPage;
import com.example.zs.addPage.ReportFormIncome;
import com.example.zs.application.MyAplication;
import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.pager.BasePager;
import com.example.zs.utils.KeyboardUtil;
import com.example.zs.utils.ScreenUtils;
import com.mob.tools.gui.PullToRequestBaseAdapter;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author  wuqi
 * 此activity为“+”后跳转的activity
 * 处理逻辑：用户支出和收入信息的输入
 */

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private int VIEWPAGE_NUMBER = 2;
    private List<AddBasePage> addBasePageInfos;
    private String TAG="AddRecordActivity";
    private int year;
    private int month;
    private int day;
    private ViewPager vp_addRecordActivity_content;
    private int getResourceID;
    private String  getCategoryName;
    private PayOutPage payOutPage;
    private DatePicker datePicker;
    private Button btn_addRecordActivity_time;
    private String stringNumber;
    private EditText tv_addRecordActivity_inputNumber;
    private boolean isIncomePage;
    private PayOutContentDAO payOutContentDAO;
    public LinearLayout ll_addRecordActivity_downRegion;
    public LinearLayout ll_addRecordActivity_keyboard;
    private IncomePage incomePage;
    private IncomeContentDAO incomeContentDAO;
    private RelativeLayout rl_addRecordActivity_remarklayout;
    private RelativeLayout rl_addRecordActivity_photolayout;
    private EditText et_addCategory_markContent;
    private InputMethodManager inputMethodManager;
    private boolean isJumpActivity;
    private int idFromOther;
    private String photo="";
    private String remarkContent="";
    private ImageView iv_addRecordActivity_remarkIcon;
    private TextView tv_addRecordActivity_jumpRemark;
    public KeyboardUtil keyboardUtil;
    private String beforeHindBoardNumber;


    private Handler mHandler;
    private int detchTime = 5;
    private ImageView iv_addRecordActivity_photo;
    private PopupWindow popupwindow_getphoto;
    private static final int ADD_CATEGORY_REQUSET = 100;//跳到类别添加页面请求码
    private static final int ADD_CATEGORY_RESUIT = 10;//类别添加页面回传码
    private static final int PHOTO_REQUEST_CAREMA = 103;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 104;// 从相册中选择
    private static final int JUMP_ACCOUNT_MODIFY_REQUST =110;//从明细page跳过来修改的请求码
    private static final int JUMP_ACCOUNT_PHOTO_ADDCONTENT =111;//从明细page拍照过来的请求码

    private Uri photoUri;
    private MyViewPagerAdapter myViewPagerAdapter;
    public boolean isDeleteState;//显示page是否为删除修改状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        //隐藏标题栏
        getSupportActionBar().hide();
        RadioGroup rg_addRecordActivity_singleChoice = (RadioGroup) findViewById(R.id.rg_addRecordActivity_singleChoice);
        btn_addRecordActivity_time = (Button) findViewById(R.id.btn_addRecordActivity_time);
        ImageView iv_addRecordActivity_finish = (ImageView) findViewById(R.id.iv_addRecordActivity_finish);
        ll_addRecordActivity_keyboard = (LinearLayout) findViewById(R.id.ll_addRecordActivity_keyboard);
        rl_addRecordActivity_remarklayout = (RelativeLayout) findViewById(R.id.rl_addRecordActivity_remarklayout);
        rl_addRecordActivity_photolayout = (RelativeLayout) findViewById(R.id.rl_addRecordActivity_photolayout);
        et_addCategory_markContent = (EditText) findViewById(R.id.et_addCategory_markContent);
        tv_addRecordActivity_inputNumber = (EditText) findViewById(R.id.tv_addRecordActivity_inputNumber);
        iv_addRecordActivity_photo = (ImageView) findViewById(R.id.iv_addRecordActivity_photo);
        //
        int inputback = tv_addRecordActivity_inputNumber.getInputType();
        tv_addRecordActivity_inputNumber.setInputType(InputType.TYPE_NULL);
        keyboardUtil =  new KeyboardUtil(this, this, tv_addRecordActivity_inputNumber,false);
        keyboardUtil.setNumberFormat(7);
        keyboardUtil.setNumberFormat(7);
        // showPopwindow();
        keyboardUtil.showKeyboardAsNormal();
        keyboardUtil.setOnkeyBoardConfirmListener(new KeyboardUtil.KeyBoardConfirmListener() {
            @Override
            public void toConfirm() {
                Log.i(TAG,"toConfirm");
                stringNumber = tv_addRecordActivity_inputNumber.getText().toString();
                commitAndsave();
                MyAplication application = (MyAplication) getApplication();
                BasePager accountPager = application.getAccountPager();
                BasePager ownerPager = application.getOwnerPager();
                BasePager reportFormPager = application.getReportFormPager();
                if(accountPager!=null){
                    Log.i("lalalala","获取了");
                    accountPager.initData();
                }
                if(ownerPager!=null){
                    ownerPager.initData();
                }
                if(reportFormPager!=null){
                    //reportFormPager.toString();
                    Log.i("kkkkkk","kkkkkkkreportFormPager.toString()");
                    reportFormPager.initData();


                   /* View inflate = View.inflate(AddRecordActivity.this, R.layout.reportformpager_content, null);
                    FrameLayout fl_reportform_shouru = (FrameLayout) inflate.findViewById(R.id.fl_reportform_shouru);

                    LinearLayout ll_reportform_shouru = (LinearLayout) inflate.findViewById(R.id.ll_reportform_shouru);

                    ReportFormIncome reportFormIncome = new ReportFormIncome(AddRecordActivity.this);

                    ll_reportform_shouru.setVisibility(View.VISIBLE);
                    fl_reportform_shouru.removeAllViews();
                    fl_reportform_shouru.addView(reportFormIncome.pieChart);
                    Log.i("xxxxxxx","reportFormIncome.to5555555String()");
                    Log.i("jjijijii","reportFormIncome.to5555555String()");*/
                }
            }
        });
        tv_addRecordActivity_inputNumber.setInputType(inputback);
        //关闭当前页面按钮
        iv_addRecordActivity_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //键盘位置的点击事件实现
        if (!isJumpActivity){
            stringNumber= "";
        }
        keyBoard();
        //默认为支出page
        rg_addRecordActivity_singleChoice.check(R.id.btn_addRecordActivity_payout);
        //设置radioGroup点击事件，目的点击可以切换page
        rg_addRecordActivity_singleChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.btn_addRecordActivity_income){
                    if ( payOutPage.isTouchHindkeyBoard){
                        //退出删除或修改状态
                        if (isDeleteState){
                            payOutPage.backFromDeleteState();
                        }
                        if (payOutPage.isTouchHindkeyBoard){
                            incomePage.isHindBeforeChangePage = true;
                        }
                    }
                    incomePage.changePage();
                    vp_addRecordActivity_content.setCurrentItem(1,false);
                    isIncomePage = true;
                }else {
                    if (isDeleteState){
                        incomePage.backFromDeleteState();
                    }
                    if ( incomePage.isTouchHindkeyBoard){
                        payOutPage.isHindBeforeChangePage = true;
                    }
                    payOutPage.changePage();
                    vp_addRecordActivity_content.setCurrentItem(0,false);

                }
            }
        });

        //找到viewpager控件
        vp_addRecordActivity_content = (ViewPager) findViewById(R.id.vp_addRecordActivity_content);
        //
        addBasePageInfos = new ArrayList<AddBasePage>();
        payOutContentDAO = new PayOutContentDAO(this);
        incomeContentDAO = new IncomeContentDAO(this);
        //得到跳转前页面携带的数据
        getInfoFromActivity();
        //默认page为支出page
        payOutPage = new PayOutPage(this,isJumpActivity);
        incomePage = new IncomePage(this,isJumpActivity);
        //点亮对应的item
        setPageItemEnable(isJumpActivity,getCategoryName);
        addBasePageInfos.add(payOutPage);
        addBasePageInfos.add(incomePage);

        //显示日期
        setDate(isJumpActivity);
        myViewPagerAdapter = new MyViewPagerAdapter();
        vp_addRecordActivity_content.setAdapter(myViewPagerAdapter);


    }
    //重写back键
    @Override
    public void onBackPressed() {
        Log.i(TAG,"onBackPressed");
        Log.i(TAG,"isDeleteState="+isDeleteState);
        if (isDeleteState){
            Log.i(TAG,"isDeleteState");
            //标志位，表示page退出删除或修改，下次使用back可以正常的finish（）页面
            isDeleteState = false;
            //调用page方法退出删除修改状态
            payOutPage.backFromDeleteState();
            incomePage.backFromDeleteState();
        }else {
            //调用父的，退出页面
            super.onBackPressed();
        }
    }

    private void showPopwindow(){

        mHandler = new Handler();

        Runnable showPopWindowRunnable = new Runnable() {

            @Override
            public void run() {
                // 得到activity中的根元素
                View view = findViewById(R.id.ll_addRecordActivity_keyboard_parent);
                // 如何根元素的width和height大于0说明activity已经初始化完毕
                if( view != null && view.getWidth() > 0 && view.getHeight() > 0) {
                    // 显示popwindow
                    keyboardUtil.showKeyboard(view);
                    // 停止检测
                    mHandler.removeCallbacks(this);
                } else {
                    // 如果activity没有初始化完毕则等待5毫秒再次检测
                    mHandler.postDelayed(this, detchTime);
                }
            }
        };
        // 开始检测
        mHandler.post(showPopWindowRunnable);
    }

    private void getInfoFromActivity() {
        Intent intent = getIntent();
        if(intent!=null){
            //跳转过来的都不为空。不管是startActivity还是startActivityForResult
            Log.i(TAG,"intent="+intent.toString());//intent=Intent { cmp=com.example.zs.myaccount/.AddRecordActivity }
            String money = intent.getStringExtra("money");
            photo = intent.getStringExtra("photoUriString");
            Log.i(TAG,"intent="+photo);
            if (money!=null){
                isJumpActivity = true;
                setDate(isJumpActivity);
                isIncomePage = intent.getBooleanExtra("isIncome", false);
                idFromOther = intent.getIntExtra("id", 0);
                getResourceID= intent.getIntExtra("resourceID", R.drawable.ic_yiban_default);
                getCategoryName = intent.getStringExtra("categoryName");
                year = intent.getIntExtra("year", 0);
                month = intent.getIntExtra("month", 0);
                day = intent.getIntExtra("day", 0);
                remarkContent = intent.getStringExtra("remarks");
                if (remarkContent!=null){
                    Log.i(TAG,"remarkContent="+remarkContent);
                    iv_addRecordActivity_remarkIcon.setVisibility(View.GONE);
                    tv_addRecordActivity_jumpRemark.setVisibility(View.VISIBLE);
                    tv_addRecordActivity_jumpRemark.setText(remarkContent);
                }
                stringNumber = money;
                //photo = intent.getStringExtra("photoUriString");
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                btn_addRecordActivity_time.setText(month+"月"+day+"日");
                if (!TextUtils.isEmpty(photo)){
                    iv_addRecordActivity_photo.setImageURI(Uri.parse(photo));
                }
            }else if(photo!=null){
                Log.i(TAG,"remarkContent="+photo);
                //直接从Acount拍照过来的没有id
                //直接显示照片即可
                iv_addRecordActivity_photo.setImageURI(Uri.parse(photo));
            }

        }
    }

    private void setPageItemEnable(boolean flag,String getCategoryName) {
        if (flag){
            int resourceIDFromName =  payOutContentDAO.getResourceIDFromName(getCategoryName);
            if (isIncomePage) {
                incomePage.setItemEnable(resourceIDFromName,getCategoryName);
            } else {
                payOutPage.setItemEnable(resourceIDFromName,getCategoryName);
            }
        }
    }

    private void keyBoard() {
        //找到键盘位置控件
        tv_addRecordActivity_jumpRemark = (TextView) findViewById(R.id.tv_addRecordActivity_jumpRemark);
       // tv_addRecordActivity_remarkShow = (TextView) findViewById(R.id.tv_addRecordActivity_remarkShow);

        iv_addRecordActivity_remarkIcon = (ImageView) findViewById(R.id.iv_addRecordActivity_remarkIcon);

        Button btn_addCategory_markConfirm = (Button) findViewById(R.id.btn_addCategory_markConfirm);
        //设置点击事件

        tv_addRecordActivity_jumpRemark.setOnClickListener(this);
       // tv_addRecordActivity_remarkShow.setOnClickListener(this);
        iv_addRecordActivity_remarkIcon.setOnClickListener(this);
        btn_addCategory_markConfirm.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Log.i(TAG,"onClick"+view.getId());
        switch (view.getId()) {
            case R.id.iv_addRecordActivity_remarkIcon:
                payOutPage.remarksInFlag = true;
                //备注时静止弹出自定义键盘
                payOutPage.isTouchHindkeyBoard = false;
                //布局变化就得保存用户以前输入的金
                incomePage.remarksInFlag = true;
                incomePage.isTouchHindkeyBoard = false;
                saveuserInputNumberBeforeHindKeyBoard();
                stringNumber = tv_addRecordActivity_inputNumber.getText().toString();
                Log.i(TAG,"88");
                //照相区隐藏，显示备注区
                rl_addRecordActivity_photolayout.setVisibility(View.GONE);
                Log.i(TAG,"---1");
                rl_addRecordActivity_remarklayout.setVisibility(View.VISIBLE);
                Log.i(TAG,"---2");
                //弹出键盘
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                //获取焦点。并弹出软键盘
                //et_addCategory_markContent.setFocusable(true);
                keyboardUtil.hideKeyboardAsNormal();
                et_addCategory_markContent.requestFocus();
                inputMethodManager.showSoftInput(et_addCategory_markContent, 0);
                Log.i(TAG,"---3");//执行完123 这整段代码，才开始执行适配器刷新
                break;
            case R.id.tv_addRecordActivity_jumpRemark:
                //目的记录用户以前选中的item背景色依然为选中状态
                payOutPage.remarksInFlag = true;
                //备注时静止弹出自定义键盘
                payOutPage.isTouchHindkeyBoard = false;
                incomePage.remarksInFlag = true;
                incomePage.isTouchHindkeyBoard = false;
                //布局变化就得保存用户以前输入的金额
                saveuserInputNumberBeforeHindKeyBoard();
                //照相区隐藏，显示备注区
                //键盘消失
                keyboardUtil.hideKeyboardAsNormal();
                rl_addRecordActivity_photolayout.setVisibility(View.GONE);
                rl_addRecordActivity_remarklayout.setVisibility(View.VISIBLE);
                et_addCategory_markContent.setText(remarkContent);
                //弹出键盘
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                //获取焦点。并弹出软键盘
                //et_addCategory_markContent.setFocusable(true);
                et_addCategory_markContent.requestFocus();
                inputMethodManager.showSoftInput(et_addCategory_markContent, 0);
                Log.i(TAG,"showSoftInput");
                break;
            case R.id.btn_addCategory_markConfirm:
                //目的记录用户以前选中的item背景色依然为选中状态
                //不需要因为下次点击的时候才会把标志位置为fasle
                //payOutPage.isNeedRefresh = true;//适配器只刷新一次,但有可能刷新多次，专门设置一个系键盘消失的标志位
                //incomePage.isNeedRefresh = true;
                payOutPage.remarksExitFlag = true;
                incomePage.remarksExitFlag = true;
                //隐藏软键盘
                inputMethodManager.hideSoftInputFromWindow(et_addCategory_markContent.getWindowToken(), 0);
                //照相区显示，备注区隐藏
                Log.i(TAG,stringNumber+"88");
                //目的记录用户以前选中的item背景色依然为选中状态
                keyboardUtil.showKeyboardAsNormal();
                remarkContent = et_addCategory_markContent.getText().toString();
                rl_addRecordActivity_remarklayout.setVisibility(View.GONE);
                rl_addRecordActivity_photolayout.setVisibility(View.VISIBLE);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                Log.i(TAG,"submit");
                if (!remarkContent.isEmpty()){
                    //照相区备注图标消失，显示备注的内容
                    iv_addRecordActivity_remarkIcon.setVisibility(View.GONE);
                    tv_addRecordActivity_jumpRemark.setVisibility(View.VISIBLE);
                    tv_addRecordActivity_jumpRemark.setText(remarkContent);
                }else {
                    tv_addRecordActivity_jumpRemark.setVisibility(View.GONE);
                    iv_addRecordActivity_remarkIcon.setVisibility(View.VISIBLE);
                }
                //重新显示用户以前输入的金额，布局变化会使以前输入 的消失掉
                showUserInputNumber();
                Log.i(TAG,"--00");
                break;
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

    private void commitAndsave() {
        //保存数据数据库中
        if (isJumpActivity){
            //传回数据给mainactivity
            Intent intent = new Intent();
            intent.putExtra("id",idFromOther);
            intent.putExtra("year",year);
            intent.putExtra("mouth",month);
            intent.putExtra("day",day);
            intent.putExtra("money",stringNumber.toString());
            intent.putExtra("marks",remarkContent);
            intent.putExtra("photoUriString",photo);
            if (!isIncomePage){
                savePayoutInfoToDB();
                intent.putExtra("resourceID",payOutPage.selectResourceID);
                intent.putExtra("categoryName",payOutPage.selectCategoryName);
                setResult(555,intent);
                finish();
            }else {
                saveIncomeInfoToDB();
                //传回数据给mainactivity
                intent.putExtra("resourceID",incomePage.selectResourceID);
                intent.putExtra("categoryName",incomePage.selectCategoryName);
                setResult(444,intent);
                finish();
            }
        }else {
            if (isIncomePage){
                saveIncomeInfoToDB();
            }else {
                savePayoutInfoToDB();
            }
        }
    }


    private void saveIncomeInfoToDB() {
        //id为自增，这里随便填充即可
        IncomeContentInfo incomeContentInfo = new IncomeContentInfo(0, incomePage.selectResourceID, incomePage.selectCategoryName,
                year, month, day, stringNumber, remarkContent, photo);
            if (!stringNumber.toString().isEmpty()) {
                if (!isJumpActivity){
                    /*idNumberIn++;
                    incomeContentInfo.id = idNumberIn;*/
                    incomeContentDAO.addIncomeContentToDB(incomeContentInfo);
                    finish();
                }else {
                    incomeContentDAO.updataIncomeContentDB(idFromOther,incomeContentInfo);
                }
            }else {
                Toast.makeText(this,"输入金额不能为空",Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * 保存数据到数据库中，供主页面显示
     */
    private void savePayoutInfoToDB() {
        Log.i(TAG,"savePayoutInfoToDB");
        PayoutContentInfo payouContentInfo = new PayoutContentInfo(0,payOutPage.selectResourceID, payOutPage.selectCategoryName,
                year, month, day, stringNumber, remarkContent, photo);
                 if (!stringNumber.isEmpty()){
                     if (!isJumpActivity){
                         //id不自增的原因是，修改时不需要自增
                        /* idNumberPay++;
                         payouContentInfo.id = idNumberPay;*/
                         payOutContentDAO.addPayoutContentToDB(payouContentInfo);
                         Log.i(TAG,"addPayoutContentToDB");
                         finish();
                     }else {
                         //根据id保存数据
                         Log.i(TAG,idFromOther+"idFromOther");
                         Log.i(TAG,"updataPayoutContentDB");
                         payOutContentDAO.updataPayoutContentDB(idFromOther,payouContentInfo);
                     }
             }else {
                     Toast.makeText(this, "输入金额不能为空", Toast.LENGTH_SHORT).show();
                 }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"requestCode"+requestCode);
        //直接返回时，intent为null
        if(resultCode==ADD_CATEGORY_RESUIT&&data!=null){
            //确认按钮返回的,标志位置为true
            //接收由intent携带的数据
            getResourceID= data.getIntExtra("resourceID", R.drawable.ic_yiban_default);
            getCategoryName = data.getStringExtra("categoryName");
            Log.i(TAG,getResourceID+"--"+getCategoryName);
            if(requestCode==ADD_CATEGORY_REQUSET){
                //传给payOutPage
                payOutPage.getActivityResult(getResourceID,getCategoryName);
            }else {
                incomePage.getActivityResult(getResourceID,getCategoryName);
            }
        }//去图库获取到的数据
        else if(requestCode==PHOTO_REQUEST_GALLERY){

                if(resultCode==RESULT_OK){
                    if(data!=null){
                        if(data.hasExtra("data")){
                            /*Bundle extras = data.getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");*/
                            Bitmap bitmap = data.getParcelableExtra("data");
                            iv_addRecordActivity_photo.setImageBitmap(bitmap);
                        }
                        //获取图片的全路径uri
                        photoUri = data.getData();
                        photo = photoUri.toString();
                        Log.i("wwwwwwww","调用图库  uri="+photoUri);
                        iv_addRecordActivity_photo.setImageURI(photoUri);
                       // iv_addRecordActivity_photo.setVisibility(View.INVISIBLE);
                    }else{
                        return;
                    }
                }
            }
        //照相
        else if(requestCode==PHOTO_REQUEST_CAREMA){
            if(resultCode==RESULT_OK){
                iv_addRecordActivity_photo.setImageURI(photoUri);
                //iv_addwishactivity_photo.setVisibility(View.INVISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setDate(boolean b) {
        datePicker = new DatePicker(this);
        //Calendar calendar = Calendar.getInstance();
        if (!b){
            //从+号加入此activity
            //得到当日的日期
            year = datePicker.getYear();
            //获取的月份要加1，月份的区间为0-11，转换为正常的月份是1-12月
            month = datePicker.getMonth()+1;
            day = datePicker.getDayOfMonth();
            btn_addRecordActivity_time.setText(month+"月"+day+"日");
            Log.i(TAG,year+"-"+month+"-"+day);
        }
    }

    /**
     * 内部类为viewPager的适配填充类，确定内部page的个数和对应的布局
     * 组成为2个page
     */
    class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return VIEWPAGE_NUMBER;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //确定关系
            return view ==(View) object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //确定page的布局
            View mView = addBasePageInfos.get(position).mView;
            container.addView(mView);
            return mView;//super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }

   /* *//**
     * 收入和支出page的切换
     * @param v
     *//*
    public void switchPage(View v){
        if(v.getId()==R.id.btn_addRecordActivity_income){
            //false 表示切换page时无动画效果
            vp_addRecordActivity_content.setCurrentItem(1,false);
            return;
        }
        if(v.getId()==R.id.btn_addRecordActivity_payout){
            vp_addRecordActivity_content.setCurrentItem(0,false);
            return;
        }
    }*/
    /**
     * button点击事件，弹出日期选择器，获取用户选择的日期
     * @param v
     */
    public void choiceTime(View v){
        //使用系统提供的日期选择器
        //api已经封装了dialog 并设置了其的宽高
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //用户点击dialog确认时调用
                Log.i(TAG, i + "--" + i1 + "--" + "--" + i2);
                year = i;
                month = i1 + 1;
                day = i2;
                btn_addRecordActivity_time.setText(month + "月" + day + "日");
            }
        }, year, month - 1, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        //new MaxDay();
        Calendar calendar = Calendar.getInstance();
        //天数加1，
        calendar.add(calendar.DAY_OF_YEAR,1);
        //设置日期选择器的最大可选日期，当天是不能选中的，所以在上面天数加1
        datePicker.setMaxDate(calendar.getTime().getTime());
        datePickerDialog.show();
       /* //不用指定位置，就不需要使用popupwindow
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // View inflate = View.inflate(this, R.layout.date_choice, null);
        //设置监听事件
        datePicker.init(year, month-1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Log.i(TAG,i+"--"+i1+"--"+"--"+i2);
                year = i;
                month = i1;
                day = i2;
                btn_addRecordActivity_time.setText(month+"月"+day+"日");
                PayOutContentDAO payOutContentDAO = new PayOutContentDAO(AddRecordActivity.this);
                int moneySum = payOutContentDAO.getMoneySum();
                Log.i(TAG, "moneySum="+moneySum);
              *//*  //test数据
                payOutContentDAO.deletePayoutContentItemFromDB(1);
                PayouContentInfo test = new PayouContentInfo(2, 12, "test类", 15, 3, 3, "1", "----", "--");
                payOutContentDAO.updataPayoutContentDB(2,test);
                ArrayList<PayouContentInfo> allPayoutContentFromDB = payOutContentDAO.getAllPayoutContentFromDB();
                Log.i(TAG,allPayoutContentFromDB.get(0).toString());*//*
            }
        });*/
       //The specified child already has a parent. You must call removeView() on the child's parent first.

        /*builder.setView(datePicker)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //保存到数据库中
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .show();*/
    }
    /**
     * 设置监听事件，用户点击iamgeview控件就可以弹出popwindow 选择拍照或是从图库获取照片
     * @param v
     */
    public void getPhoto(View v){
        //初始化popupwindow
        popupwindow_getphoto = new PopupWindow();

        //popupWindow的界面，三个button
        View view_getphoto = View.inflate(AddRecordActivity.this, R.layout.popupwindow_getphoto_addwish, null);
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
        popupwindow_getphoto.showAtLocation(v, Gravity.BOTTOM,0,0);

        if (popupwindow_getphoto.isShowing()){
            ScreenUtils.backgroundAlpha(AddRecordActivity.this,0.5f);
        }else {
            ScreenUtils.backgroundAlpha(AddRecordActivity.this,1.0f);
        }
        //给三个按钮添加点击事件
        bt_addwishpopupwindow_camera.setOnClickListener(AddRecordActivity.this);
        bt_addwishpopupwindow_gallery.setOnClickListener(AddRecordActivity.this);
        bt_addwishpopupwindow_cancel.setOnClickListener(AddRecordActivity.this);

        //给popupwindow添加监听,点击其它位置popupwindow会消失，activity背景还原
        popupwindow_getphoto.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ScreenUtils.backgroundAlpha(AddRecordActivity.this,1.0f);
            }
        });
    }
    /**
     * 从图库中获取图片
     */
    private void toGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
    }
    /**
     * 拍照获取图片
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
        photo = photoUri.toString();
        Log.i("wwwwwwww","使用相机前  uri="+photoUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /**
     * 处理用户先输入金额，键盘消失后出现以前的金额不见的bug
     */
    public void saveuserInputNumberBeforeHindKeyBoard(){
        beforeHindBoardNumber = tv_addRecordActivity_inputNumber.getText().toString();
        Log.i(TAG,"saveuserInputNumberPreviousHindKeyBoard--"+ beforeHindBoardNumber);
    }
    public void showUserInputNumber(){
        //键盘的原因 隐藏后出现吧editText的值变为0了
        String s = tv_addRecordActivity_inputNumber.getText().toString();
        Log.i(TAG,s);
        tv_addRecordActivity_inputNumber.setText(beforeHindBoardNumber);
    }
}

