package com.example.zs.myaccount;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zs.addPage.AddBasePage;
import com.example.zs.addPage.IncomePage;
import com.example.zs.addPage.PayOutPage;
import com.example.zs.bean.UserAddCategoryInfo;

import java.util.ArrayList;
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
    private   int getResourceID;
    private   String  getCategoryName;
    private PayOutPage payOutPage;
    private DatePicker datePicker;
    private Button btn_addRecordActivity_time;
    private StringBuffer stringNumber;
    private TextView tv_addRecordActivity_inputNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        //隐藏标题栏
        getSupportActionBar().hide();
        RadioGroup rg_addRecordActivity_singleChoice = (RadioGroup) findViewById(R.id.rg_addRecordActivity_singleChoice);
        btn_addRecordActivity_time = (Button) findViewById(R.id.btn_addRecordActivity_time);
        //键盘位置的点击事件实现
        stringNumber= new StringBuffer();
        keyBoard();
        //默认为支出page
        rg_addRecordActivity_singleChoice.check(R.id.btn_addRecordActivity_payout);
        rg_addRecordActivity_singleChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.btn_addRecordActivity_income){
                    vp_addRecordActivity_content.setCurrentItem(1,false);
                }else {
                    vp_addRecordActivity_content.setCurrentItem(0,false);
                }
            }
        });

        //找到viewpager控件
        vp_addRecordActivity_content = (ViewPager) findViewById(R.id.vp_addRecordActivity_content);
        //
        addBasePageInfos = new ArrayList<AddBasePage>();
        //默认page为支出page
        payOutPage = new PayOutPage(this);
        IncomePage incomePage = new IncomePage(this);
        addBasePageInfos.add(payOutPage);
        addBasePageInfos.add(incomePage);
        //显示日期
        setDate();
        vp_addRecordActivity_content.setAdapter(new MyViewPagerAdapter());
    }

    private void keyBoard() {
        //找到键盘位置控件
        TextView viewById0 =  (TextView) findViewById(R.id.tv_addCategory_0);
        TextView viewById1 = (TextView) findViewById(R.id.tv_addCategory_1);
        TextView viewById2 = (TextView) findViewById(R.id.tv_addCategory_2);
        TextView viewById3 = (TextView) findViewById(R.id.tv_addCategory_3);
        TextView viewById4 = (TextView) findViewById(R.id.tv_addCategory_4);
        TextView viewById5 = (TextView) findViewById(R.id.tv_addCategory_5);
        TextView viewById6 = (TextView) findViewById(R.id.tv_addCategory_6);
        TextView viewById7 = (TextView) findViewById(R.id.tv_addCategory_7);
        TextView viewById8 = (TextView) findViewById(R.id.tv_addCategory_8);
        TextView viewById9 = (TextView) findViewById(R.id.tv_addCategory_9);
        TextView viewById10 = (TextView) findViewById(R.id.tv_addCategory_10);
        tv_addRecordActivity_inputNumber = (TextView) findViewById(R.id.tv_addRecordActivity_inputNumber);
        ImageView tv_addCategory_removeNumber = (ImageView) findViewById(R.id.tv_addCategory_removeNumber);
        TextView tv_addCategory_submit = (TextView) findViewById(R.id.tv_addCategory_submit);
        ImageView iv_addRecordActivity_remark = (ImageView) findViewById(R.id.iv_addRecordActivity_remark);
        //设置点击事件
        viewById0.setOnClickListener(this);
        viewById1.setOnClickListener(this);
        viewById2.setOnClickListener(this);
        viewById3.setOnClickListener(this);
        viewById4.setOnClickListener(this);
        viewById5.setOnClickListener(this);
        viewById6.setOnClickListener(this);
        viewById7.setOnClickListener(this);
        viewById8.setOnClickListener(this);
        viewById9.setOnClickListener(this);
        viewById10.setOnClickListener(this);
        tv_addCategory_removeNumber.setOnClickListener(this);
        tv_addCategory_submit.setOnClickListener(this);
        iv_addRecordActivity_remark.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Log.i(TAG,"onClick"+view.getId());
        switch (view.getId()){
            case R.id.tv_addCategory_0:
                stringNumber.append(0);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_1:
                stringNumber.append(1);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_2:
                stringNumber.append(2);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_3:
                stringNumber.append(3);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_4:
                stringNumber.append(4);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_5:
                stringNumber.append(5);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_6:
                stringNumber.append(6);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_7:
                stringNumber.append(7);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_8:
                stringNumber.append(8);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_9:
                stringNumber.append(9);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_10:
                stringNumber.append(".");
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_removeNumber:
                stringNumber.deleteCharAt(stringNumber.length()-1);
                tv_addRecordActivity_inputNumber.setText(stringNumber);
                break;
            case R.id.tv_addCategory_submit:
                //保存数据数据库中
                savePayoutInfoToDB();
                finish();
                break;
            case R.id.iv_addRecordActivity_remark:
                break;

        }
    }

    /**
     * 保存数据到数据库中，供主页面显示
     */
    private void savePayoutInfoToDB() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"requestCode"+requestCode);
        //直接返回时，intent为null
        if(requestCode==100&&data!=null){
            //确认按钮返回的,标志位置为true
            //接收由intent携带的数据
            getResourceID= data.getIntExtra("resourceID", R.drawable.ic_yiban_default);
            getCategoryName = data.getStringExtra("categoryName");
            Log.i(TAG,getResourceID+"--"+getCategoryName);
            //传给payOutPage
            payOutPage.getActivityResult(getResourceID,getCategoryName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setDate() {
        //得到当日的日期
        datePicker = new DatePicker(this);
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
        btn_addRecordActivity_time.setText(month+"月"+day+"日");
        Log.i(TAG,year+"-"+month+"-"+day);
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

        //不用指定位置，就不需要使用popupwindow
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // View inflate = View.inflate(this, R.layout.date_choice, null);
        //设置监听事件
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Log.i(TAG,i+"--"+i1+"--"+"--"+i2);
                AddRecordActivity.this.year = i;
                AddRecordActivity.this.month =i1;
                day = i2;
            }
        });
        builder.setView(datePicker)
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
        .show()

            ;
    }

}

