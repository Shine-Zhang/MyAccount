package com.example.zs.pager;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zs.addPage.ReportFormIncome;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.myaccount.RportFormDatePickerActivity;
import com.example.zs.view.CircleImageView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zs.myaccount.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 本类是五个pager中的“报表类”，继承至基类BasePager,主要用饼状图的形式分别呈现出各种类型的收入和支出所占的比重
 */
public class ReportFormPager extends BasePager {
    //各个支出类型所占比重

    //报表视图
    public View reportformpager_content_view;
    //时间段
    public TextView tv_reportform_time;
    //对应”收入“和”支出“按钮的RadioGroup
    public RadioGroup rg_reportform;

    //饼状图各部分对应的颜色数组
    public int[] colors = new int[]{PayoutColors.movieColor,
            PayoutColors.phonefeeColor, PayoutColors.hufuColor, PayoutColors.spriteColor, PayoutColors.liwuColor,
            PayoutColors.sportColor, PayoutColors.clothColor, PayoutColors.stardyColor,
            PayoutColors.medchineColor, PayoutColors.fruitColor, PayoutColors.lifeColor, PayoutColors.travelColor,
            PayoutColors.lingshiColor, PayoutColors.transportColor, PayoutColors.fubaijuhuiColor,
            PayoutColors.rentColor, PayoutColors.canyinColor, PayoutColors.normalColor};
    //饼状图各部分代表的内容
    public String[] dataType = new String[]{"电影", "话费", "护肤彩妆", "酒水饮料", "礼物", "运动", "衣服鞋包",
            "学习", "药品", "水果", "生活用品", "旅行", "零食", "交通", "腐败聚餐",
            "房租", "餐饮", "一般"};
    public PieChart pc_reportform_piechart;
    public FrameLayout fl_reportform_shouru;
    public FrameLayout fl_reportform;
    public FrameLayout fl_reportform_zhichu;
    public LinearLayout ll_reportform_shouru;
    public LinearLayout ll_reportform_zhichu;


    public SharedPreferences reporFormsp;

    public ReportFormIncome reportFormincome;

    //从数据库拿到的支出数据
    public ArrayList<PayoutContentInfo> allPayoutCategory;
    //这个数决定了圆饼被分为几部分
    public  int allNumber;
    //总的消费金额
    public int allAccount;
    public PayoutNumAndAccount payoutNumAndAccount;

    public float[] allAccountDD;
    public TextView tv_reportform_detail;
    public RecyclerView rv_reportformpager_recyclerview;
    public List<String> reportformData;

    public ReportFormPager(Activity activity) {
        super(activity);

        initData();
        initChart();
    }

    @Override
    public View initView() {

        reportformpager_content_view = View.inflate(mActivity, R.layout.reportformpager_content, null);

        //收入和支出对应的饼图
        pc_reportform_piechart = (PieChart) reportformpager_content_view.findViewById(R.id.pc_reportform_piechart);

        //最上方的RadioGroup和显示时间的控件
        tv_reportform_time = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_time);
        rg_reportform = (RadioGroup) reportformpager_content_view.findViewById(R.id.rg_reportform);

        //收入报表对应的id
        ll_reportform_shouru = (LinearLayout) reportformpager_content_view.findViewById(R.id.ll_reportform_shouru);
        fl_reportform_shouru = (FrameLayout) reportformpager_content_view.findViewById(R.id.fl_reportform_shouru);

        //支出报表对应的id
        ll_reportform_zhichu = (LinearLayout) reportformpager_content_view.findViewById(R.id.ll_reportform_zhichu);
        fl_reportform_zhichu = (FrameLayout) reportformpager_content_view.findViewById(R.id.fl_reportform_zhichu);

        //整体的framlauout包含了支出和收入
        fl_reportform = (FrameLayout) reportformpager_content_view.findViewById(R.id.fl_reportform);


        //tv_reportform_detail = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_detail);
        rv_reportformpager_recyclerview = (RecyclerView) reportformpager_content_view.findViewById(R.id.rv_reportformpager_recyclerview);
        //设置recyclerview的显示规则
        rv_reportformpager_recyclerview.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));
        //rv_reportformpager_recyclerview.setAdapter(new ReportformRecyclerViewAdapter(reportformData,mActivity));
        rv_reportformpager_recyclerview.addItemDecoration(new DividerItemDecoration(mActivity,
                DividerItemDecoration.VERTICAL_LIST));


        //接收由RportFormDataPickerActivity回传的数据
        Intent intent = mActivity.getIntent();
        String date = intent.getStringExtra("date");
        //将数据保存到SharedPreferences,以便下次进入的时候回写
        reporFormsp = mActivity.getSharedPreferences("reportformData", mActivity.MODE_PRIVATE);
        SharedPreferences.Editor edit = reporFormsp.edit();
        edit.putString("time", date);
        edit.commit();
        if (date != null) {
            tv_reportform_time.setText(date);
        } else {
            String string = reporFormsp.getString("time", "还没有设定时间");
            tv_reportform_time.setText(string);
        }

        //为显示时间阶段的TextView设置监听器
        tv_reportform_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, RportFormDatePickerActivity.class));
            }
        });

        //为两个按钮，收入和支出设置监听器，通过点击不同按钮切换收入和支出报表状况
        rg_reportform.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //支出对应报表页面
                    case R.id.rb_reportform_expenditure:
                        ll_reportform_shouru.setVisibility(View.GONE);
                        ll_reportform_zhichu.setVisibility(View.VISIBLE);
                        fl_reportform_shouru.removeView(reportFormincome.pieChart);
                        break;
                    //收入对应报表页面
                    case R.id.rb_reportform_income:
                        //加载收入页面
                        reportFormincome = new ReportFormIncome(mActivity);
                        ll_reportform_zhichu.setVisibility(View.GONE);
                        ll_reportform_shouru.setVisibility(View.VISIBLE);
                        fl_reportform_shouru.addView(reportFormincome.pieChart);
                }
            }
        });

        return reportformpager_content_view;
    }

    @Override
    public void initData() {
        //从数据库拿数据
        PayOutContentDAO payoutCotnetDAO =  new PayOutContentDAO(mActivity);
        allPayoutCategory = payoutCotnetDAO.getAllPayoutContentFromDB();
        payoutNumAndAccount = new PayoutNumAndAccount();
        //将支出类型一样的归为一类
        for(int i = 0;i < allPayoutCategory.size();i ++){
            if(allPayoutCategory.get(i).category.equals("一般")){
                payoutNumAndAccount.normalNumber = 1;
                payoutNumAndAccount.normalAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("餐饮")){
                payoutNumAndAccount.canyinNumber = 1;
                payoutNumAndAccount.canyinAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("交通")){
                payoutNumAndAccount.transportNumber = 1;
                payoutNumAndAccount.transportAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("零食")){
                payoutNumAndAccount.linshiNumber = 1;
                payoutNumAndAccount.linshiAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("水果")){
                payoutNumAndAccount.fruitNumber = 1;
                payoutNumAndAccount.fruitAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("腐败聚会")){
                payoutNumAndAccount.juhuiNumber = 1;
                payoutNumAndAccount.juhuiAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("酒水饮料")){
                payoutNumAndAccount.jiushuiNumber = 1;
                payoutNumAndAccount.jiushuiAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("电影")){
                payoutNumAndAccount.movieNumber = 1;
                payoutNumAndAccount.movieAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("衣服鞋包")){
                payoutNumAndAccount.clothNumber = 1;
                payoutNumAndAccount.clothAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("生活用品")){
                payoutNumAndAccount.lifeNumber = 1;
                payoutNumAndAccount.lifeAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("话费")){
                payoutNumAndAccount.huafeiNumber = 1;
                payoutNumAndAccount.huafeiAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("房租")){
                payoutNumAndAccount.rentNumber = 1;
                payoutNumAndAccount.rentAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("护肤彩妆")){
                payoutNumAndAccount.hufuNumber = 1;
                payoutNumAndAccount.hufuAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("药品")){
                payoutNumAndAccount.yaopinNumber = 1;
                payoutNumAndAccount.yaopinAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("旅行")){
                payoutNumAndAccount.lvxingNumber = 1;
                payoutNumAndAccount.lvxingAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category + "----" +allPayoutCategory.get(i).money );

            }else if(allPayoutCategory.get(i).category.equals("礼物")){
                payoutNumAndAccount.liwuNumber = 1;
                payoutNumAndAccount.liwuAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("运动")){
                payoutNumAndAccount.sportNumber = 1;
                payoutNumAndAccount.sportAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("学习")){
                payoutNumAndAccount.xuexiNumber = 1;
                payoutNumAndAccount.xuexiAccount += Integer.parseInt(allPayoutCategory.get(i).money);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }
        }

        allNumber = payoutNumAndAccount.xuexiNumber + payoutNumAndAccount.liwuNumber + payoutNumAndAccount.lifeNumber
                + payoutNumAndAccount.lvxingNumber + payoutNumAndAccount.yaopinNumber + payoutNumAndAccount.hufuNumber
                + payoutNumAndAccount.rentNumber + payoutNumAndAccount.huafeiNumber + payoutNumAndAccount.linshiNumber
                + payoutNumAndAccount.clothNumber + payoutNumAndAccount.movieNumber + payoutNumAndAccount.jiushuiNumber
                + payoutNumAndAccount.fruitNumber + payoutNumAndAccount.normalNumber + payoutNumAndAccount.sportNumber
                + payoutNumAndAccount.juhuiNumber + payoutNumAndAccount.transportNumber + payoutNumAndAccount.canyinNumber;

        Log.i("payoutNumAndAccount",allNumber + "");

        allAccount = payoutNumAndAccount.normalAccount + payoutNumAndAccount.canyinAccount + payoutNumAndAccount.transportAccount
                + payoutNumAndAccount.linshiAccount + payoutNumAndAccount.fruitAccount + payoutNumAndAccount.juhuiAccount
                + payoutNumAndAccount.jiushuiAccount + payoutNumAndAccount.movieAccount + payoutNumAndAccount.clothAccount
                + payoutNumAndAccount.lifeAccount + payoutNumAndAccount.huafeiAccount + payoutNumAndAccount.rentAccount
                + payoutNumAndAccount.hufuAccount + payoutNumAndAccount.yaopinAccount + payoutNumAndAccount.lvxingAccount
                + payoutNumAndAccount.liwuAccount + payoutNumAndAccount.sportAccount + payoutNumAndAccount.xuexiAccount;

        Log.i("payoutNumAndAccount",allAccount + "");

        allAccountDD = new float[]{payoutNumAndAccount.movieAccount,payoutNumAndAccount.huafeiAccount ,
                payoutNumAndAccount.hufuAccount ,payoutNumAndAccount.jiushuiAccount , payoutNumAndAccount.liwuAccount ,
                payoutNumAndAccount.sportAccount , payoutNumAndAccount.clothAccount, payoutNumAndAccount.xuexiAccount,
                payoutNumAndAccount.yaopinAccount ,  payoutNumAndAccount.fruitAccount , payoutNumAndAccount.lifeAccount ,
                payoutNumAndAccount.lvxingAccount,payoutNumAndAccount.linshiAccount ,payoutNumAndAccount.transportAccount,
                payoutNumAndAccount.juhuiAccount,payoutNumAndAccount.rentAccount,payoutNumAndAccount.canyinAccount,
                payoutNumAndAccount.normalAccount};

        reportformData = new ArrayList<String>();
        for(int i = 0;i < 18; i ++){
            if(allAccountDD[i] != 0){
                reportformData.add(allAccountDD[i] +"---" + dataType[i % dataType.length] );
            }
        }

        for(int i = 0;i <reportformData.size(); i ++){
            Log.i("reportformData",reportformData.get(i));
        }

        rv_reportformpager_recyclerview.setAdapter(new ReportformRecyclerViewAdapter(reportformData,mActivity));
        initChart();
    }

    public void initChart() {
        //设置饼状图是否接受点击事件，默认为true
        pc_reportform_piechart.setTouchEnabled(true);
        //设置图饼是否显示百分比
        pc_reportform_piechart.setUsePercentValues(true);
        //是否显示圆盘中间文字
        pc_reportform_piechart.setDrawCenterText(true);
        //设置圆盘中间的颜色
        pc_reportform_piechart.setHoleColor(Color.WHITE);
        //设置圆盘中间文字
        pc_reportform_piechart.setCenterText(generateCenterSpannableText());
        //设置中间圆盘的半径,值为所占饼图的百分比
        pc_reportform_piechart.setHoleRadius(60);
        //设置圆盘是否可以转动
        pc_reportform_piechart.setRotationEnabled(true);
        pc_reportform_piechart.setRotationAngle(90);
        //设置动画
        pc_reportform_piechart.animateX(1000, Easing.EasingOption.EaseInOutQuad);
        //将在下方显示的比例视图
        //customizeLegend();
        //设置饼图右下角的文字描述
        pc_reportform_piechart.setDescription("");
        //文字描述的颜色
        pc_reportform_piechart.setDescriptionColor(Color.BLACK);
        //设置饼图右下角的文字大小
        pc_reportform_piechart.setDescriptionTextSize(16);

        //设置比例图(图例，即那种颜色代表那种消费类型)
        Legend legend = pc_reportform_piechart.getLegend();
        //设置比例图显示在饼图的哪个位置
        legend.setEnabled(false);
        /*legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        //设置比例图的形状，默认是方形,可为方形、圆形、线性,这里我们设置为圆形
        legend.setForm(Legend.LegendForm.CIRCLE);
        //设置距离饼图的距离，防止与饼图重合
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(7f);*/
        //legend.setYOffset(10f);
        //设置比例块换行...
        legend.setWordWrapEnabled(true);
        //绑定数据,括号中的内容代表的饼状图将被分为几部分
        bindData(18);

        // 设置一个选中区域监听
        pc_reportform_piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                float rotationAngle = pc_reportform_piechart.getRotationAngle();

                Log.i("rotationAngle",rotationAngle + "");

                float[] drawAngles = pc_reportform_piechart.getDrawAngles();

                for(int i = 0; i < drawAngles.length;i ++){
                    Log.i("drawAngles",drawAngles[i] + "");
                }

                float[] absoluteAngles = pc_reportform_piechart.getAbsoluteAngles();

                for(int i = 0; i < absoluteAngles.length;i ++){
                    Log.i("absoluteAngles",absoluteAngles[i] + "");
                }

                float y = h.getY();

                int x = (int) h.getX();
                float v = drawAngles[x] / 2;
                float end = 450f - (absoluteAngles[x] - v);
                pc_reportform_piechart.spin(500,rotationAngle,end,Easing.EasingOption.EaseInOutQuad);
                Log.i("rotationAngle",x + "");

                //tv_reportform_detail.setText( dataType[x] +": "  + y + "元" );
                //tv_reportform_detail.setTextColor(colors[x]);


            }

            @Override
            public void onNothingSelected() {

            }
        });
    }



    public void customizeLegend() {
        Legend legend = pc_reportform_piechart.getLegend();//设置比例图
        legend.setEnabled(false);//图例显示
    }

    //中间显示的文字数据
    public SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("总支出\n" + allAccount);
        s.setSpan(new RelativeSizeSpan(1.2f), 0, 4, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 4, 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, 0);
        s.setSpan(new RelativeSizeSpan(2.0f), 4, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 4, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 4, s.length(), 0);
        return s;
    }

    public void bindData(int count) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < count; i++) {
            //dataType[i % dataType.length]
            if(allAccountDD[i] == 0){
                entries.add(new PieEntry(allAccountDD[i],""  ));
            }else{
                entries.add(new PieEntry(allAccountDD[i],dataType[i % dataType.length]  ));
            }
        }

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(entries, "");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(3f);
        //各个区域不显示具体的数字，即所占百分比
        dataSet.setValueTextColor(Color.BLACK);
        //被选中部分高出的长度
        dataSet.setSelectionShift(10f);

        //设置各个区域的颜色
        ArrayList<Integer> color = new ArrayList<Integer>();
        for(int c : colors){
            color.add(c);
        }
        dataSet.setColors(color);
        PieData pieData = new PieData(dataSet);
        //设置显示百分比
        //if (allAccountDD[i] == 0)
        //pieData.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        pieData.setValueTextSize(11f);
        //设置区域文字的颜色
        pieData.setValueTextColor(Color.TRANSPARENT);
        //设置区域文字的字体
        pieData.setValueTypeface(Typeface.DEFAULT);
        pc_reportform_piechart.setData(pieData);

        pc_reportform_piechart.highlightValues(null);

        pc_reportform_piechart.invalidate();
    }
}

class PayoutNumAndAccount{
    int normalNumber = 0;
    int normalAccount = 0;

    int canyinNumber = 0;
    int canyinAccount =0;

    int transportNumber = 0;
    int transportAccount =0;

    int linshiNumber = 0;
    int linshiAccount =0;

    int fruitNumber = 0;
    int fruitAccount =0;

    int juhuiNumber = 0;
    int juhuiAccount =0;

    int jiushuiNumber = 0;
    int jiushuiAccount =0;

    int movieNumber = 0;
    int movieAccount =0;

    int lifeNumber = 0;
    int lifeAccount =0;

    int clothNumber = 0;
    int clothAccount =0;

    int huafeiNumber = 0;
    int huafeiAccount =0;

    int rentNumber = 0;
    int rentAccount =0;

    int hufuNumber = 0;
    int hufuAccount =0;

    int yaopinNumber = 0;
    int yaopinAccount =0;

    int lvxingNumber = 0;
    int lvxingAccount =0;

    int liwuNumber = 0;
    int liwuAccount =0;

    int sportNumber = 0;
    int sportAccount =0;

    int xuexiNumber = 0;
    int xuexiAccount =0;
}

class PayoutColors{
    public  static int movieColor = Color.rgb(243, 59, 151);
    public  static int phonefeeColor = Color.rgb(92, 228, 162);
    public  static int hufuColor = Color.rgb(248, 137, 235);
    public  static int spriteColor = Color.rgb(247, 181, 45);
    public  static int liwuColor = Color.rgb(243, 59, 151);
    public  static int sportColor = Color.rgb(75, 206, 241);
    public  static int clothColor = Color.rgb(0, 137, 230);
    public  static int stardyColor = Color.rgb(251, 143, 51);
    public  static int medchineColor = Color.rgb(145, 189, 254);
    public  static int fruitColor = Color.rgb(253, 53, 100);
    public  static int lifeColor = Color.rgb(252, 218, 49);
    public  static int travelColor = Color.rgb(170, 233, 61);
    public  static int lingshiColor = Color.rgb(247, 181, 45);
    public  static int transportColor = Color.rgb(92, 228, 162);
    public  static int fubaijuhuiColor = Color.rgb(170, 233, 61);
    public  static int rentColor = Color.rgb(75, 206, 241);
    public  static int canyinColor = Color.rgb(75, 206, 241);
    public  static int normalColor = Color.rgb(251, 143, 51);
}

//RecyclerView的适配器
class ReportformRecyclerViewAdapter extends RecyclerView.Adapter<ReportformRecyclerViewAdapter.ReportformViewHolder>{
    public Context context;
    private List<String> dataList;

    public ReportformRecyclerViewAdapter(List<String> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ReportformViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReportformViewHolder reportformViewHolder = new ReportformViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_reportform_recyclerview, parent, false));
        return reportformViewHolder;
        //return new ReportformViewHolder(View.inflate(context,R.layout.item_reportform_recyclerview,null));
    }

    @Override
    public void onBindViewHolder(ReportformViewHolder holder, int position) {
        Log.i("dataList", dataList.get(position) + dataList.size());
        Log.i("position",position + "");

        holder.mTextView.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ReportformViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public ReportformViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_reportform_rvitem);
        }
    }
}

//RecyclerView分割线
class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public Drawable mDivider;

    public int mOrientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
