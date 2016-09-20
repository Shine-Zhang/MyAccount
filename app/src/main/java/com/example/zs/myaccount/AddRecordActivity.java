package com.example.zs.myaccount;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.addPage.AddBasePage;
import com.example.zs.addPage.IncomePage;
import com.example.zs.addPage.PayOutPage;
import com.example.zs.application.MyAplication;
import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.pager.BasePager;
import com.example.zs.utils.KeyboardUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private String photo;
    private String remarkContent="";
    private ImageView iv_addRecordActivity_remarkIcon;
    private TextView tv_addRecordActivity_jumpRemark;
    public KeyboardUtil keyboardUtil;
    private Handler mHandler;
    private int detchTime = 5;
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

        int inputback = tv_addRecordActivity_inputNumber.getInputType();
        tv_addRecordActivity_inputNumber.setInputType(InputType.TYPE_NULL);
        keyboardUtil =  new KeyboardUtil(this, this, tv_addRecordActivity_inputNumber,false);
        keyboardUtil.setNumberFormat(7);
       // showPopwindow();
        keyboardUtil.showKeyboardAsNormal();
        keyboardUtil.setOnkeyBoardConfirmListener(new KeyboardUtil.KeyBoardConfirmListener() {
            @Override
            public void toConfirm() {
                Log.i(TAG,"toConfirm");
                stringNumber = tv_addRecordActivity_inputNumber.getText().toString();
                if (stringNumber.isEmpty()){
                    //为空
                }


                commitAndsave();
                MyAplication application = (MyAplication) getApplication();
                BasePager accountPager = application.getAccountPager();
                if(accountPager!=null){
                    accountPager.initData();
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
        rg_addRecordActivity_singleChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.btn_addRecordActivity_income){
                    vp_addRecordActivity_content.setCurrentItem(1,false);
                    isIncomePage = true;
                }else {
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
        vp_addRecordActivity_content.setAdapter(new MyViewPagerAdapter());


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
            String money = intent.getStringExtra("money");
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
                photo = intent.getStringExtra("photo");
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                btn_addRecordActivity_time.setText(month+"月"+day+"日");
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
                stringNumber = tv_addRecordActivity_inputNumber.getText().toString();
                Log.i(TAG,"88");
                //照相区隐藏，显示备注区
                rl_addRecordActivity_photolayout.setVisibility(View.GONE);
                rl_addRecordActivity_remarklayout.setVisibility(View.VISIBLE);
                //弹出键盘
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                //获取焦点。并弹出软键盘
                //et_addCategory_markContent.setFocusable(true);
                keyboardUtil.hideKeyboard();
                et_addCategory_markContent.requestFocus();
                inputMethodManager.showSoftInput(et_addCategory_markContent, 0);

                break;
            case R.id.tv_addRecordActivity_jumpRemark:
                //照相区隐藏，显示备注区
                //键盘消失
                keyboardUtil.hideKeyboard();
                rl_addRecordActivity_photolayout.setVisibility(View.GONE);
                rl_addRecordActivity_remarklayout.setVisibility(View.VISIBLE);
                et_addCategory_markContent.setText(remarkContent);
                //弹出键盘
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                //获取焦点。并弹出软键盘
                //et_addCategory_markContent.setFocusable(true);
                et_addCategory_markContent.requestFocus();
                inputMethodManager.showSoftInput(et_addCategory_markContent, 0);

                break;
            case R.id.btn_addCategory_markConfirm:
                //照相区显示，备注区隐藏
                Log.i(TAG,stringNumber+"88");
               // keyboardUtil.showKeyboard();
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
                //隐藏软键盘
                inputMethodManager.hideSoftInputFromWindow(et_addCategory_markContent.getWindowToken(), 0);
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
            intent.putExtra("photo","this is photo");
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
                year, month, day, stringNumber, remarkContent, "");
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
                year, month, day, stringNumber, remarkContent, "");
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
        if(resultCode==10&&data!=null){
            //确认按钮返回的,标志位置为true
            //接收由intent携带的数据
            getResourceID= data.getIntExtra("resourceID", R.drawable.ic_yiban_default);
            getCategoryName = data.getStringExtra("categoryName");
            Log.i(TAG,getResourceID+"--"+getCategoryName);
            if(requestCode==100){
                //传给payOutPage
                payOutPage.getActivityResult(getResourceID,getCategoryName);
            }else {
                incomePage.getActivityResult(getResourceID,getCategoryName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setDate(boolean b) {
        datePicker = new DatePicker(this);
       /* Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);*/
       // datePicker.init(year, month, day,null);
/*        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                if (isDateAfter(view)) {
                    Calendar mCalendar = Calendar.getInstance();
                    view.init(mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH),
                            mCalendar.get(Calendar.DAY_OF_MONTH), this);
                }
            }

            private boolean isDateAfter(DatePicker tempView) {
                Calendar mCalendar = Calendar.getInstance();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(tempView.getYear(), tempView.getMonth(),
                        tempView.getDayOfMonth(), 0, 0, 0);
                if (tempCalendar.after(mCalendar))
                    return true;
                else
                    return false;
            }
        });*/

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
        new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //用户点击dialog确认时调用
                Log.i(TAG,i+"--"+i1+"--"+"--" +i2);
                year = i;
                month = i1+1;
                day = i2;
                btn_addRecordActivity_time.setText(month+"月"+day+"日");
            }
        },year,month-1,day).show();
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
}

