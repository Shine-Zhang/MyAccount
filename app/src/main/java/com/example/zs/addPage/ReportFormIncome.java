package com.example.zs.addPage;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.myaccount.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangxudong on 2016/9/8 0008.
 */
public class ReportFormIncome {
    public Activity activity;
    //饼状图各部分代表的内容
    public List<String> shouruDataType = new ArrayList<String>(){{add("工资");add("兼职");add("零花钱");add("红包");add("理财收益");}};;
    //各个收入类型代表的颜色
    public int[] shouruColors = new int[]{IncomeColors.salaryColor,IncomeColors.jianzhiColor,IncomeColors.linhuaqianColor,
            IncomeColors.hongbaoColor,IncomeColors.licaiColor};
    public  ArrayList<Entry> shouruValueList;
    public  ArrayList<IncomeContentInfo> allIncomeContentFromDB;
    public  int allShouRuNumber;
    public  float allShouRuAccount;
    public PieChart pieChart;
    public int[] shourLeixing;
    private IncomeNumAndAccount incomeNumAndAccount;
    public float[] allAccountDDIncome;
    public List<String> reportformIncomeData;
    public TextView tv_reportform_incomedetail;
    public HashMap<String, Float> otherShouruAllAccountDD;
    public HashMap<String, Integer> reportformfShouruIcon;
    public String[] keysString;
    public final ImageView iv_reportform_incomedetail;

    public ReportFormIncome(Activity activity) {
        this.activity = activity;

        initView();
        initData();
        initChart();

        View inflate = View.inflate(activity, R.layout.reportformpager_content, null);
        tv_reportform_incomedetail = (TextView) inflate.findViewById(R.id.tv_reportform_incomedetail);
        iv_reportform_incomedetail = (ImageView) inflate.findViewById(R.id.iv_reportform_incomedetail);
    }


    public View initView() {
        pieChart = new PieChart(activity);
        return pieChart;
    }

    private void initChart() {
        //设置饼状图是否接受点击事件，默认为true
        pieChart.setTouchEnabled(true);
        //设置图饼是否显示百分比
        pieChart.setUsePercentValues(false);
        //是否显示圆盘中间文字
        pieChart.setDrawCenterText(true);
        //设置圆盘中间的颜色
        pieChart.setHoleColor(Color.WHITE);
        //设置圆盘中间文字
        pieChart.setCenterText(generateCenterSpannableText());
        //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.setHoleRadius(60);
        //设置圆盘是否可以转动
        pieChart.setRotationEnabled(true);
        pieChart.setRotationAngle(90);
        //设置动画
        pieChart.animateX(1000, Easing.EasingOption.EaseInOutQuad);

        //设置饼图右下角的文字描述
        pieChart.setDescription("");

        //设置比例图(图例，即那种颜色代表那种消费类型)
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        //绑定数据,括号中的内容代表的饼状图将被分为几部分
        bindData(shouruDataType.size());
        // 设置一个选中区域监听
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float rotationAngle = pieChart.getRotationAngle();

                Log.i("rotationAngle",rotationAngle + "");

                float[] drawAngles = pieChart.getDrawAngles();

                for(int i = 0; i < drawAngles.length;i ++){
                    Log.i("drawAngles",drawAngles[i] + "");
                }

                float[] absoluteAngles = pieChart.getAbsoluteAngles();

                for(int i = 0; i < absoluteAngles.length;i ++){
                    Log.i("absoluteAngles",absoluteAngles[i] + "");
                }

                float y = h.getY();

                int x = (int) h.getX();
                float v = drawAngles[x] / 2;
                float end = 450f - (absoluteAngles[x] - v);
                pieChart.spin(500,rotationAngle,end,Easing.EasingOption.EaseInOutQuad);

                tv_reportform_incomedetail.setText(shouruDataType.get(x) +": "  + y + "元");
                tv_reportform_incomedetail.setTextColor(shouruColors[x%shouruColors.length]);

                iv_reportform_incomedetail.setBackgroundResource(R.drawable.account_pager_group_today_icon);
                iv_reportform_incomedetail.setImageResource(reportformfShouruIcon.get(shouruDataType.get(x)));

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void bindData(int count){
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < count; i++) {
            if(i < 5){
                entries.add(new PieEntry(allAccountDDIncome[i],""));
                Log.i("jsjsjsj",shouruDataType.get(i) + allAccountDDIncome[i]);
            }else {
                entries.add(new PieEntry(otherShouruAllAccountDD.get(keysString[i - 5]),""));
                Log.i("jsjsjsj",shouruDataType.get(i) +"**" + keysString[i - 5] + otherShouruAllAccountDD.get(keysString[i - 5]) + "");
            }
        }

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(entries,"");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(2f);
        //各个区域不显示具体的数字，即所占百分比
        dataSet.setValueTextColor(Color.TRANSPARENT);
        //被选中部分高出的长度
        dataSet.setSelectionShift(10f);

        //设置各个区域的颜色
        ArrayList<Integer> color = new ArrayList<Integer>();
        for(int c : shouruColors){
            color.add(c);
        }
        dataSet.setColors(color);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);

        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private void initData() {

        allShouRuAccount = 0;
        IncomeContentDAO incomeContentDAO = new IncomeContentDAO(activity);
        //从数据库获取收入数据
        allIncomeContentFromDB = incomeContentDAO.getAllIncomeContentFromDB();

        otherShouruAllAccountDD = new HashMap<>();
        reportformfShouruIcon = new HashMap<>();

        incomeNumAndAccount = new IncomeNumAndAccount();

        for(int i = 0; i < allIncomeContentFromDB.size();i++){
                if(allIncomeContentFromDB.get(i).category.equals("工资")) {
                    incomeNumAndAccount.salaryNumber = 1;
                    incomeNumAndAccount.salaryAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);

                    Log.i("xixixix",allIncomeContentFromDB.get(i).category +"-----" + incomeNumAndAccount.salaryAccount + "");

                    reportformfShouruIcon.put("工资",allIncomeContentFromDB.get(i).resourceID);

                }else if(allIncomeContentFromDB.get(i).category.equals("兼职")){
                    incomeNumAndAccount.jianzhiNumber = 1;
                    incomeNumAndAccount.jianzhiAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);

                    Log.i("xixixix",allIncomeContentFromDB.get(i).category +"-----" +incomeNumAndAccount.jianzhiAccount + "");

                    reportformfShouruIcon.put("兼职",allIncomeContentFromDB.get(i).resourceID);

                }else if(allIncomeContentFromDB.get(i).category.equals("零花钱")){
                    incomeNumAndAccount.linhuaqianNumber = 1;
                    incomeNumAndAccount.lihuaqianAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);
                    reportformfShouruIcon.put("零花钱",allIncomeContentFromDB.get(i).resourceID);
                }else if(allIncomeContentFromDB.get(i).category.equals("红包")){
                    incomeNumAndAccount.hongbaoNumber = 1;
                    incomeNumAndAccount.hongbaoAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);

                    Log.i("xixixix",allIncomeContentFromDB.get(i).category +"-----" +incomeNumAndAccount.hongbaoAccount + "");

                    reportformfShouruIcon.put("红包",allIncomeContentFromDB.get(i).resourceID);

                }else if(allIncomeContentFromDB.get(i).category.equals("理财收益")){
                    incomeNumAndAccount.licaiNumber = 1;
                    incomeNumAndAccount.licaiAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);

                    Log.i("xixixix",allIncomeContentFromDB.get(i).category +"-----" +incomeNumAndAccount.licaiAccount + "");

                    reportformfShouruIcon.put("理财收益",allIncomeContentFromDB.get(i).resourceID);

                }else {
                    String category = allIncomeContentFromDB.get(i).category;
                    boolean flag = true;
                    for(int j = 0; j < shouruDataType.size();j++){

                        Set<String> keyWords = otherShouruAllAccountDD.keySet();
                        keysString = new String[keyWords.size()];
                        Object[] keys = keyWords.toArray();

                        for(int k = 0; k < keys.length; k++){
                            String keyString =  keys[k].toString();
                            keysString[k] = keyString;
                        }

                        if(!category.equals(shouruDataType.get(j))){
                            if(j == (shouruDataType.size()-1)){
                                shouruDataType.add(category);
                                allShouRuAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);
                                reportformfShouruIcon.put(category,allIncomeContentFromDB.get(i).resourceID);
                                otherShouruAllAccountDD.put(category,Float.valueOf(allIncomeContentFromDB.get(i).money));
                                Log.i("kkkkkdkdkddk","h会不会显示1" + "-----" + category + otherShouruAllAccountDD.get(category));
                                flag = false;
                            }else {
                                continue;
                            }
                        }else {
                                if(flag){
                                    reportformfShouruIcon.put(category,allIncomeContentFromDB.get(i).resourceID);
                                    for(int h = 0; h < keysString.length; h++){
                                        if(keysString[h].equals(category)){
                                            Float integer = otherShouruAllAccountDD.get(category);
                                            otherShouruAllAccountDD.put(keysString[h], integer + Float.valueOf(allIncomeContentFromDB.get(i).money));
                                        }
                                    }

                                    Log.i("kkkkkdkdkddk","h会不会显示2" + "-----" +category + otherShouruAllAccountDD.get(category));

                                    allShouRuAccount += Float.valueOf(allIncomeContentFromDB.get(i).money);
                                    break;

                                }
                                }

                    }
                    Log.i("kkkkkdkdkddk","h会不会显示3" + "-----" +category + otherShouruAllAccountDD.get(category) + "");
                }
        }

        /*allShouRuNumber = incomeNumAndAccount.salaryNumber + incomeNumAndAccount.jianzhiNumber
                + incomeNumAndAccount.lihuaqianAccount +  incomeNumAndAccount.hongbaoNumber
                + incomeNumAndAccount.licaiNumber;*/

        allShouRuAccount += incomeNumAndAccount.licaiAccount + incomeNumAndAccount.hongbaoAccount
                + incomeNumAndAccount.lihuaqianAccount + incomeNumAndAccount.jianzhiAccount
                + incomeNumAndAccount.salaryAccount;


        allAccountDDIncome = new float[]{incomeNumAndAccount.salaryAccount,incomeNumAndAccount.jianzhiAccount,
                incomeNumAndAccount.lihuaqianAccount , incomeNumAndAccount.hongbaoAccount,
                incomeNumAndAccount.licaiAccount
                };

        reportformIncomeData = new ArrayList<String>();
        for(int i = 0;i < 5; i ++) {
            if (allAccountDDIncome[i] != 0) {
                reportformIncomeData.add(allAccountDDIncome[i] + "---" + shouruDataType.get(i % shouruDataType.size()));
            }
        }

        initChart();
    }
    public void refreshIncome(IncomeContentInfo iii){
        allIncomeContentFromDB.add(iii);
        initView();
        initData();
        initChart();
    }

    /*public void refreshIncome(){
        //new ReportFormIncome(activity);
        initView();
        initData();
        initChart();
    }*/


    //中间显示的文字数据
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("总收入\n" + String.format("%.2f",allShouRuAccount));
        s.setSpan(new RelativeSizeSpan(1.2f), 0, 4, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 4, 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, 0);
        s.setSpan(new RelativeSizeSpan(2.0f), 4, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 4, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 4, s.length(), 0);
        return s;
    }
}

class IncomeColors{
    public static int salaryColor = Color.rgb(145, 189, 254);
    public static int jianzhiColor = Color.rgb(92, 228, 162);
    public static int linhuaqianColor = Color.rgb(252, 218, 49);
    public static int hongbaoColor = Color.rgb(253, 53, 100);
    public static int licaiColor = Color.rgb(145, 189, 254);
}

class IncomeNumAndAccount{
    //工资
    float salaryNumber = 0;
    float salaryAccount = 0;
    //兼职
    float jianzhiNumber = 0;
    float jianzhiAccount = 0;
    //零花钱
    float linhuaqianNumber = 0;
    float lihuaqianAccount = 0;

    //红包
    float hongbaoNumber = 0;
    float hongbaoAccount = 0;

    //理财收益
    float licaiNumber = 0;
    float licaiAccount = 0;

}
