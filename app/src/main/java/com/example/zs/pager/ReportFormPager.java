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
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zs.addPage.ReportFormIncome;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.myaccount.RportFormDatePickerActivity;
import com.example.zs.utils.ScreenUtils;
import com.example.zs.utils.SeletorUtils;
import com.example.zs.utils.SyncBackgroudUtils;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.myaccount.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 本类是五个pager中的“报表类”，继承至基类BasePager,主要用饼状图的形式分别呈现出各种类型的收入和支出所占的比重
 */
public class ReportFormPager extends BasePager {
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
            PayoutColors.rentColor, PayoutColors.canyinColor, PayoutColors.normalColor,PayoutColors.hongbaoColor};
    //饼状图各部分代表的内容
   /* public ArrayList<String> zhichuDataType = new ArrayList<String>(){{add("电影"); add("话费");add("护肤彩妆"); add("酒水饮料");
        add("礼物"); add("运动");add("衣服鞋包"); add("学习");add("药品"); add("水果");add("生活用品"); add("旅行");
        add("零食"); add("交通");add("腐败聚会"); add("房租");add("餐饮"); add("一般");add("红包");}};*/


    //各项支出对应的图标
    public HashMap<String,Integer> reportformfIcon ;
    //支出饼状图
    public PieChart pc_reportform_piechart;
    public FrameLayout fl_reportform_shouru;
    public FrameLayout fl_reportform;
    public FrameLayout fl_reportform_zhichu;
    public LinearLayout ll_reportform_shouru;
    public LinearLayout ll_reportform_zhichu;
    //把收入对应的饼状图抽为一个类，具体对数据的处理都在这个类中进行
    public ReportFormIncome reportFormincome;

    //从数据库拿到的支出数据
    public ArrayList<PayoutContentInfo> allPayoutCategory;
    //这个数决定了圆饼被分为几部分
    public  int allNumber;
    //总的消费金额
    public float allAccount;
    //专门用于记录最基础的那19个支出类型的支出金额和数量
    public PayoutNumAndAccount payoutNumAndAccount;
    //专门记录最基本的那19类支出类型的消费金额数，创建这个数组主要是为了在添加到饼状图的时候方便操作
    public float[] allAccountDD;
    //自定义添加的消费项目
    public HashMap<String,Float> otherAllAccountDD;

    //点击的时候，下方显示的的各项具体金额数
    public TextView tv_reportform_detail;
    //没用到
    public RecyclerView rv_reportformpager_recyclerview;
    //没用到
    public List<String> reportformData;

    public RelativeLayout rl_reportform_detail;
    //对应显示图标的地方
    public ImageView iv_reportform_detail;
    //存放自定义添加的消费项目otherAllAccountDD中各个“键”。
    public String[] keysString;
    /**
     * 点击收入饼状图时，在下面显示对应的收入金额。必须放到这里（ReportFormPager）进行UI的刷新
     * 如果在ReportFormIncome进行UI的刷新，不会显示出来
     */
    public TextView tv_reportform_incomedetail;
    public ImageView iv_reportform_incomedetail;
    public ArrayList<String> zhichuDataType;

    public ReportFormPager(Activity activity) {
        super(activity);

        initData();
        //initChart();
    }

    public  void refreshPauout(PayoutContentInfo iii){
        allPayoutCategory.add(iii);
        //initView();
        initData();
        //initChart();
    }

    @Override
    public View initView() {

        reportformpager_content_view = View.inflate(mActivity, R.layout.reportformpager_content, null);

        //支出对应的饼图
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


        tv_reportform_detail = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_detail);
        rl_reportform_detail = (RelativeLayout) reportformpager_content_view.findViewById(R.id.rl_reportform_detail);
        iv_reportform_detail = (ImageView) reportformpager_content_view.findViewById(R.id.iv_reportform_detail);

        tv_reportform_incomedetail = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_incomedetail);
        iv_reportform_incomedetail = (ImageView) reportformpager_content_view.findViewById(R.id.iv_reportform_incomedetail);
        //tv_reportform_incomedetail = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_incomedetail);

        //rv_reportformpager_recyclerview = (RecyclerView) reportformpager_content_view.findViewById(R.id.rv_reportformpager_recyclerview);
        //设置recyclerview的显示规则
        //rv_reportformpager_recyclerview.setLayoutManager(new SyLinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false));
        //rv_reportformpager_recyclerview.setAdapter(new ReportformRecyclerViewAdapter(reportformData,mActivity));
        /*rv_reportformpager_recyclerview.addItemDecoration(new DividerItemDecoration(mActivity,
                DividerItemDecoration.VERTICAL_LIST));*/


        //接收由RportFormDataPickerActivity回传的数据
        /*Intent intent = mActivity.getIntent();
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
        }*/

        //为显示时间阶段的TextView设置监听器
        /*tv_reportform_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, RportFormDatePickerActivity.class));
            }
        });*/

        //为两个按钮，收入和支出设置监听器，通过点击不同按钮切换收入和支出报表状况
        rg_reportform.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //支出对应报表页面
                    case R.id.rb_reportform_expenditure:
                        ll_reportform_shouru.setVisibility(View.GONE);
                        ll_reportform_zhichu.setVisibility(View.VISIBLE);
                        //在显示支出饼状图的时候，把收入对应的饼状图移除
                        fl_reportform_shouru.removeView(reportFormincome.pieChart);
                        break;
                    //收入对应报表页面
                    case R.id.rb_reportform_income:
                        //加载收入页面
                        reportFormincome = new ReportFormIncome(mActivity);
                        ll_reportform_zhichu.setVisibility(View.GONE);
                        ll_reportform_shouru.setVisibility(View.VISIBLE);
                        fl_reportform_shouru.addView(reportFormincome.pieChart);
                        //在这里为收入饼状图设置点击监听事件，如果在ReportFormincome中设置，UI不会刷新
                        reportFormincome.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {

                                //的到整个饼状图当前的一个标准化的角度
                                float rotationAngle = reportFormincome.pieChart.getRotationAngle();
                                //获取饼状图中每一部分所占角度的大小
                                float[] drawAngles = reportFormincome.pieChart.getDrawAngles();
                                //获取饼状图每一部分的绝度角度
                                float[] absoluteAngles = reportFormincome.pieChart.getAbsoluteAngles();
                                //饼状图中每一部分所代表的实际金额（被点击的slice）
                                float y = h.getY();
                                //被点击的slice是饼状图中的第几部分
                                int x = (int) h.getX();

                                float v = drawAngles[x] / 2;
                                float end = 450f - (absoluteAngles[x] - v);
                                //圆饼旋转动画
                                reportFormincome.pieChart.spin(500,rotationAngle,end,Easing.EasingOption.EaseInOutQuad);
                                //同时在点击某个slice的同时，在下方显示与其对应的金额和图标
                                /*
                                 tv_reportform_detail.setText(  zhichuDataType.get(x) +":       "  + y + "元" );
                               //colors[x % colors.length]
                                tv_reportform_detail.setTextColor(colors[x % colors.length]);

                                Log.i("lllllllll",colors[x % colors.length] + "");
                                //iv_reportform_detail.setBackgroundResource(R.drawable.account_pager_group_today_icon);
                                SyncBackgroudUtils.setTimeLineBackgroud(reportformfIcon.get(zhichuDataType.get(x)),iv_reportform_detail,colors[x % colors.length]);
                                Log.i("lllllllll",colors[x % colors.length] + "");

                                iv_reportform_detail.setImageResource(reportformfIcon.get(zhichuDataType.get(x)));
                                 */
                                    tv_reportform_incomedetail.setText(reportFormincome.shouruDataType.get(x) +":       "  + y + "元");

                                    tv_reportform_incomedetail.setTextColor(reportFormincome.shouruColors[x%reportFormincome.shouruColors.length]);
                                    SyncBackgroudUtils.setTimeLineBackgroud(reportFormincome.reportformfShouruIcon.get(reportFormincome.shouruDataType.get(x)),iv_reportform_incomedetail,reportFormincome.shouruColors[x%reportFormincome.shouruColors.length]);
                                   Log.i("kkkkkkk","");
                                    //iv_reportform_incomedetail.setBackgroundResource(R.drawable.account_pager_group_today_icon);
                                    iv_reportform_incomedetail.setImageResource(reportFormincome.reportformfShouruIcon.get(reportFormincome.shouruDataType.get(x)));


                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });
                }
            }
        });

        return reportformpager_content_view;
    }

    @Override
    public void initData() {
        zhichuDataType = new ArrayList<String>(){{add("电影"); add("话费");add("护肤彩妆"); add("酒水饮料");
           add("礼物"); add("运动");add("衣服鞋包"); add("学习");add("药品"); add("水果");add("生活用品"); add("旅行");
           add("零食"); add("交通");add("腐败聚会"); add("房租");add("餐饮"); add("一般");add("红包");}};
        //每次初始化数据的时候，将总支出金额置位0，否则总支出或与实际金额不对应，甚至成倍的增加
        allAccount = 0;
        //从数据库拿数据
        PayOutContentDAO payoutCotnetDAO =  new PayOutContentDAO(mActivity);
        allPayoutCategory = payoutCotnetDAO.getAllPayoutContentFromDB();

        payoutNumAndAccount = new PayoutNumAndAccount();
        reportformfIcon = new HashMap<>();
        //自定义支出类型和支出金额
        otherAllAccountDD = new HashMap<>();

        //将支出类型一样的归为一类
        for(int i = 0;i < allPayoutCategory.size();i ++){
            if(allPayoutCategory.get(i).category.equals("一般")){
                payoutNumAndAccount.normalNumber = 1;
                payoutNumAndAccount.normalAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("一般",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.normalAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("餐饮")){
                payoutNumAndAccount.canyinNumber = 1;
                payoutNumAndAccount.canyinAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("餐饮",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.canyinAccount);


                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("交通")){
                payoutNumAndAccount.transportNumber = 1;
                payoutNumAndAccount.transportAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("交通",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.transportAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("零食")){
                payoutNumAndAccount.linshiNumber = 1;
                payoutNumAndAccount.linshiAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("零食",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.linshiAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("水果")){
                payoutNumAndAccount.fruitNumber = 1;
                payoutNumAndAccount.fruitAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("水果",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.fruitAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("腐败聚会")){
                payoutNumAndAccount.juhuiNumber = 1;
                payoutNumAndAccount.juhuiAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("腐败聚会",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.juhuiAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("酒水饮料")){
                payoutNumAndAccount.jiushuiNumber = 1;
                payoutNumAndAccount.jiushuiAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("酒水饮料",allPayoutCategory.get(i).resourceID);

               //allAccountDD.add(payoutNumAndAccount.jiushuiAccount);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("电影")){
                payoutNumAndAccount.movieNumber = 1;
                payoutNumAndAccount.movieAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("电影",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.movieAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("衣服鞋包")){
                payoutNumAndAccount.clothNumber = 1;
                payoutNumAndAccount.clothAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("衣服鞋包",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.clothAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("生活用品")){
                payoutNumAndAccount.lifeNumber = 1;
                payoutNumAndAccount.lifeAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("生活用品",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.lifeAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("话费")){
                payoutNumAndAccount.huafeiNumber = 1;
                payoutNumAndAccount.huafeiAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("话费",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.huafeiAccount);

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("房租")){
                payoutNumAndAccount.rentNumber = 1;
                payoutNumAndAccount.rentAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("房租",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.rentAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("护肤彩妆")){
                payoutNumAndAccount.hufuNumber = 1;
                Log.i("chaizhuang",Float.valueOf(allPayoutCategory.get(i).money) + "");
                payoutNumAndAccount.hufuAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("护肤彩妆",allPayoutCategory.get(i).resourceID);

               // allAccountDD.add(payoutNumAndAccount.hufuAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("药品")){
                payoutNumAndAccount.yaopinNumber = 1;
                payoutNumAndAccount.yaopinAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("药品",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.yaopinAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("旅行")){
                payoutNumAndAccount.lvxingNumber = 1;
                payoutNumAndAccount.lvxingAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("旅行",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.lvxingAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category + "----" +allPayoutCategory.get(i).money );

            }else if(allPayoutCategory.get(i).category.equals("礼物")){
                payoutNumAndAccount.liwuNumber = 1;
                payoutNumAndAccount.liwuAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("礼物",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.liwuAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("运动")){
                payoutNumAndAccount.sportNumber = 1;
                payoutNumAndAccount.sportAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("运动",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.sportAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");
                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("学习")){
                payoutNumAndAccount.xuexiNumber = 1;
                payoutNumAndAccount.xuexiAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("学习",allPayoutCategory.get(i).resourceID);

                //allAccountDD.add(payoutNumAndAccount.xuexiAccount);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");

                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);

            }else if(allPayoutCategory.get(i).category.equals("红包")){
                payoutNumAndAccount.hongbaoNumber = 1;
                payoutNumAndAccount.hongbaoAccount += Float.valueOf(allPayoutCategory.get(i).money);


                reportformfIcon.put("红包",allPayoutCategory.get(i).resourceID);

                Log.i("allPayoutCategory",allPayoutCategory.get(i).resourceID + "");
                Log.i("payoutNumAndAccount",allPayoutCategory.get(i).category+ "----" +allPayoutCategory.get(i).money);
            }else {//对应的是自定义的支出类型
                String category = allPayoutCategory.get(i).category;
                boolean flag = true;
                //循环的遍历支出集合
                for(int j = 0; j < zhichuDataType.size(); j++){
/*
                    Set<String> keyWords = otherAllAccountDD.keySet();
                    keysString = new String[keyWords.size()];
                    Object[] keys = keyWords.toArray();
                    for(int k = 0; k < keys.length; k++){
                        String keyString =  keys[k].toString();
                        keysString[k] = keyString;
                    }*/

                    Log.i("jijiji","----------------------------------------------------");
                    if(!category.equals(zhichuDataType.get(j))){
                        if(j == (zhichuDataType.size()-1)){
                            zhichuDataType.add(category);
                            allAccount += Float.valueOf(allPayoutCategory.get(i).money);
                            reportformfIcon.put(category,allPayoutCategory.get(i).resourceID);
                            otherAllAccountDD.put(category,Float.valueOf(allPayoutCategory.get(i).money));

                            Set<String> keyWords = otherAllAccountDD.keySet();
                            keysString = new String[keyWords.size()];
                            Object[] keys = keyWords.toArray();
                            for(int k = 0; k < keys.length; k++){
                                String keyString =  keys[k].toString();
                                keysString[k] = keyString;
                            }

                            flag = false;
                            Log.i("jijiji",category + "1.----" + otherAllAccountDD.get(category));
                        }else {
                            continue;
                        }
                    }else {
                        if(flag){
                            reportformfIcon.put(category,allPayoutCategory.get(i).resourceID);


                            Set<String> keyWords = otherAllAccountDD.keySet();
                            keysString = new String[keyWords.size()];
                            Object[] keys = keyWords.toArray();
                            for(int k = 0; k < keys.length; k++){
                                String keyString =  keys[k].toString();
                                keysString[k] = keyString;
                            }

                            for(int h = 0; h < keysString.length; h++){
                                if(keysString[h].equals(category)){
                                    Float integer = otherAllAccountDD.get(keysString[h]);
                                    otherAllAccountDD.put(keysString[h], integer +Float.valueOf(allPayoutCategory.get(i).money));
                                }
                            }
                            allAccount += Float.valueOf(allPayoutCategory.get(i).money);
                            Log.i("jijiji",category + "22.----" + otherAllAccountDD.get(category));
                            break;
                        }
                        Log.i("jijiji",category + "2.----" + otherAllAccountDD.get(category));
                    }
                }
                Log.i("jijiji",category + "3.----" + otherAllAccountDD.get(category));
            }
        }

        allAccount += payoutNumAndAccount.normalAccount + payoutNumAndAccount.canyinAccount + payoutNumAndAccount.transportAccount
                + payoutNumAndAccount.linshiAccount + payoutNumAndAccount.fruitAccount + payoutNumAndAccount.juhuiAccount
                + payoutNumAndAccount.jiushuiAccount + payoutNumAndAccount.movieAccount + payoutNumAndAccount.clothAccount
                + payoutNumAndAccount.lifeAccount + payoutNumAndAccount.huafeiAccount + payoutNumAndAccount.rentAccount
                + payoutNumAndAccount.hufuAccount + payoutNumAndAccount.yaopinAccount + payoutNumAndAccount.lvxingAccount
                + payoutNumAndAccount.liwuAccount + payoutNumAndAccount.sportAccount + payoutNumAndAccount.xuexiAccount
                + payoutNumAndAccount.hongbaoAccount;

        Log.i("payoutNumAndAccount",allAccount + "");

        allAccountDD = new float[]{payoutNumAndAccount.movieAccount,payoutNumAndAccount.huafeiAccount ,
                payoutNumAndAccount.hufuAccount ,payoutNumAndAccount.jiushuiAccount , payoutNumAndAccount.liwuAccount ,
                payoutNumAndAccount.sportAccount , payoutNumAndAccount.clothAccount, payoutNumAndAccount.xuexiAccount,
                payoutNumAndAccount.yaopinAccount ,  payoutNumAndAccount.fruitAccount , payoutNumAndAccount.lifeAccount ,
                payoutNumAndAccount.lvxingAccount,payoutNumAndAccount.linshiAccount ,payoutNumAndAccount.transportAccount,
                payoutNumAndAccount.juhuiAccount,payoutNumAndAccount.rentAccount,payoutNumAndAccount.canyinAccount,
                payoutNumAndAccount.normalAccount,payoutNumAndAccount.hongbaoAccount};

        reportformData = new ArrayList<String>();
        for(int i = 0;i < 19; i ++){
            if(allAccountDD[i] != 0){
                //reportformData.add(allAccountDD[i] +"---" + dataType[i % dataType.length] );
                reportformData.add(allAccountDD[i] +"---" + zhichuDataType.get(i % zhichuDataType.size()));
            }
        }

        for(int i = 0;i <reportformData.size(); i ++){
            Log.i("reportformData",reportformData.get(i));
        }

       // rv_reportformpager_recyclerview.setAdapter(new ReportformRecyclerViewAdapter(reportformData,mActivity));
        initChart();
    }

    public void initChart() {
        Log.i("3333333333","55555555555555");
        //设置饼状图是否接受点击事件，默认为true
        pc_reportform_piechart.setTouchEnabled(true);
        //设置图饼是否显示百分比
        pc_reportform_piechart.setUsePercentValues(false);
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

        //设置饼图右下角的文字描述
        pc_reportform_piechart.setDescription("");

        //设置比例图(图例，即那种颜色代表那种消费类型)
        Legend legend = pc_reportform_piechart.getLegend();
        //设置比例图显示在饼图的哪个位置
        legend.setEnabled(false);

        //绑定数据,括号中的内容代表的饼状图将被分为几部分
        bindData(zhichuDataType.size());

        for(int i = 0;i <zhichuDataType.size();i++ ){
            Log.i("zhichuDataType555",zhichuDataType.get(i));
        }

        Log.i("zhichuDataType",zhichuDataType.size() + "");

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

                tv_reportform_detail.setText(  zhichuDataType.get(x) +":       "  + y + "元" );
               //colors[x % colors.length]
                tv_reportform_detail.setTextColor(colors[x % colors.length]);

                Log.i("lllllllll",colors[x % colors.length] + "");
                //iv_reportform_detail.setBackgroundResource(R.drawable.account_pager_group_today_icon);
                SyncBackgroudUtils.setTimeLineBackgroud(reportformfIcon.get(zhichuDataType.get(x)),iv_reportform_detail,colors[x % colors.length]);
                Log.i("lllllllll",colors[x % colors.length] + "");

                iv_reportform_detail.setImageResource(reportformfIcon.get(zhichuDataType.get(x)));

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


    //中间显示的文字数据
    public SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("总支出\n" + String.format("%.2f",allAccount));
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
        for (int h = 0; h <count; h++) {
            if(h < 19){
                entries.add(new PieEntry(allAccountDD[h],""));
            }else{
                Log.i("tmdtmd",h + "");
                Log.i("tmdtmd",zhichuDataType.size() + "");
                entries.add(new PieEntry(otherAllAccountDD.get(keysString[h - 19]),""));
            }
        }

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(entries, "");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(5f);
        //各个区域不显示具体的数字，即所占百分比
        dataSet.setValueTextColor(Color.TRANSPARENT);
        //被选中部分高出的长度
        dataSet.setSelectionShift(10f);

        //设置各个区域的颜色
        ArrayList<Integer> color = new ArrayList<Integer>();
        for(int c : colors){
            color.add(c);
        }
        dataSet.setColors(color);
        PieData pieData = new PieData(dataSet);

        pc_reportform_piechart.setData(pieData);

        pc_reportform_piechart.highlightValues(null);

        pc_reportform_piechart.invalidate();
    }
}

class PayoutNumAndAccount{
    float normalNumber = 0;
    float normalAccount = 0;

    float canyinNumber = 0;
    float canyinAccount =0;

    float transportNumber = 0;
    float transportAccount =0;

    float linshiNumber = 0;
    float linshiAccount =0;

    float fruitNumber = 0;
    float fruitAccount =0;

    float juhuiNumber = 0;
    float juhuiAccount =0;

    float jiushuiNumber = 0;
    float jiushuiAccount =0;

    float movieNumber = 0;
    float movieAccount =0;

    float lifeNumber = 0;
    float lifeAccount =0;

    float clothNumber = 0;
    float clothAccount =0;

    float huafeiNumber = 0;
    float huafeiAccount =0;

    float rentNumber = 0;
    float rentAccount =0;

    float hufuNumber = 0;
    float hufuAccount =0;

    float yaopinNumber = 0;
    float yaopinAccount =0;

    float lvxingNumber = 0;
    float lvxingAccount =0;

    float liwuNumber = 0;
    float liwuAccount =0;

    float sportNumber = 0;
    float sportAccount =0;

    float xuexiNumber = 0;
    float xuexiAccount =0;

    float hongbaoNumber = 0;
    float hongbaoAccount = 0;
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
    public  static int hongbaoColor = Color.rgb(255, 41, 12);
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
            RecyclerView v = new RecyclerView(parent.getContext());
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

//recyclerview的item的高度自适应
class SyLinearLayoutManager extends LinearLayoutManager {

    private static final int CHILD_WIDTH = 0;
    private static final int CHILD_HEIGHT = 1;
    private static final int DEFAULT_CHILD_SIZE = 100;

    private final int[] childDimensions = new int[2];

    private int childSize = DEFAULT_CHILD_SIZE;
    private boolean hasChildSize;

    @SuppressWarnings("UnusedDeclaration")
    public SyLinearLayoutManager(Context context) {
        super(context);
    }

    @SuppressWarnings("UnusedDeclaration")
    public SyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];


    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;


        for (int i = 0; i < getItemCount(); i++) {

            try {
                measureScrapChild(recycler, i,
                        widthSpec,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);
            } catch (IndexOutOfBoundsException e) {

                e.printStackTrace();
            }

            if (getOrientation() == HORIZONTAL) {
                width = width + mMeasuredDimension[0];
                if (i == 0) {
                    height = mMeasuredDimension[1];
                }
            } else {
                height = height + mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
            }
        }


//        Logger.d("ll width:"+width+";widthSize:"+widthSize+";widthSpec:"+widthSpec);
//        Logger.d("ll height:"+width+";heightSize:"+heightSize+";heightSpec:"+heightSpec);
//        Logger.d("ll widthMode:"+widthMode+";heightMode:"+heightMode);

        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
//                    width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }
        setMeasuredDimension(widthSpec, height);

    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);

        // For adding Item Decor Insets to view
//        super.measureChildWithMargins(view, 0, 0);

        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        Logger.e("SyLinearLayoutManager state:" + state.toString());
        super.onLayoutChildren(recycler, state);
    }
}