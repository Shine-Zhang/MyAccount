package com.example.zs.addPage;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;

import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.myaccount.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by zhangxudong on 2016/9/8 0008.
 */
public class ReportFormIncome extends AddBasePage{
    //饼状图各部分代表的内容
    public String[] shouruDataType = new String[]{"工资","兼职","零花钱","红包","理财收益"};
    //各个收入类型代表的颜色
    public int[] shouruColors = new int[]{IncomeColors.salaryColor,IncomeColors.jianzhiColor,IncomeColors.linhuaqianColor,
                                            IncomeColors.hongbaoColor,IncomeColors.licaiColor};
    public  ArrayList<Entry> shouruValueList;
    public  ArrayList<IncomeContentInfo> allIncomeContentFromDB;
    public  int allShouRuNumber;
    public  int allShouRuAccount;
    public  float gongzhiPercent;
    public  float jianzhiPercent;
    public  float linghuaqianPercent;
    public  float hongbaoPercent;
    public  float licaiPercent;
    public PieChart pieChart;

    public ReportFormIncome(Activity activity, boolean isJump) {
        super(activity, isJump);
    }


    @Override
    public View initView() {
        pieChart = new PieChart(activity);
        initChart();
        return pieChart;
    }

    private void initChart() {
        //设置饼状图是否接受点击事件，默认为true
        pieChart.setTouchEnabled(true);
        //设置图饼是否显示百分比
        pieChart.setUsePercentValues(true);
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
        //将在下方显示的比例视图
        customizeLegend();
        //设置饼图右下角的文字描述
        pieChart.setDescription("");
        //文字描述的颜色
        pieChart.setDescriptionColor(Color.BLACK);
        //设置饼图右下角的文字大小
        pieChart.setDescriptionTextSize(16);

        //设置比例图(图例，即那种颜色代表那种消费类型)
        Legend legend = pieChart.getLegend();
        //设置比例图显示在饼图的哪个位置
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        //设置比例图的形状，默认是方形,可为方形、圆形、线性,这里我们设置为圆形
        legend.setForm(Legend.LegendForm.CIRCLE);
        //设置距离饼图的距离，防止与饼图重合
        legend.setXEntrySpace(10f);
        legend.setYEntrySpace(7f);
        //legend.setYOffset(10f);
        //设置比例块换行...
        legend.setWordWrapEnabled(true);

        //绑定数据,括号中的内容代表的饼状图将被分为几部分
        bindData(allShouRuNumber);
        // 设置一个选中区域监听
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                pieChart.setDescription(shouruDataType[e.getXIndex()] + " " + (int) e.getVal() + "元");
                Log.i("e.getXIndex()", e.getXIndex() + "");
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void bindData(int count){
        ArrayList<String> shouruNameList = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            shouruNameList.add(shouruDataType[i]);
        }

        shouruValueList = new ArrayList<Entry>();
        shouruValueList.add(new Entry(gongzhiPercent * 100,0));
        shouruValueList.add(new Entry(jianzhiPercent * 100,1));
        shouruValueList.add(new Entry(linghuaqianPercent * 100,2));
        shouruValueList.add(new Entry(hongbaoPercent * 100,3));
        shouruValueList.add(new Entry(licaiPercent * 100,4));

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(shouruValueList, "");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(1f);
        //各个区域不显示具体的数字，即所占百分比
        dataSet.setValueTextColor(Color.TRANSPARENT);
        //被选中部分高出的长度
        dataSet.setSelectionShift(10f);

        //设置各个区域的颜色
        ArrayList<Integer> shouruColor = new ArrayList<>();
        shouruColor.add(shouruColors[0]);
        shouruColor.add(shouruColors[1]);
        shouruColor.add(shouruColors[2]);
        shouruColor.add(shouruColors[3]);
        shouruColor.add(shouruColors[4]);
        dataSet.setColors(shouruColor);

        PieData pieData = new PieData(shouruNameList, dataSet);
        //设置显示百分比
        pieData.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        pieData.setValueTextSize(11f);
        //设置区域文字的颜色
        pieData.setValueTextColor(Color.BLACK);
        //设置区域文字的字体
        pieData.setValueTypeface(Typeface.DEFAULT);
        pieChart.setData(pieData);
    }
    
    private void initData() {
        IncomeNumAndAccount incomeNumAndAccount = new IncomeNumAndAccount();
        IncomeContentDAO incomeContentDAO = new IncomeContentDAO(activity);
        //从数据库获取收入数据
        allIncomeContentFromDB = incomeContentDAO.getAllIncomeContentFromDB();
        for(int i = 0; i < allIncomeContentFromDB.size();i++){
            switch (allIncomeContentFromDB.get(i).category){
                case "工资":
                    incomeNumAndAccount.salaryNumber = 1;
                    incomeNumAndAccount.salaryAccount = Integer.parseInt(allIncomeContentFromDB.get(i).money);
                    break;
                case "兼职":
                    incomeNumAndAccount.jianzhiNumber = 1;
                    incomeNumAndAccount.jianzhiAccount = Integer.parseInt(allIncomeContentFromDB.get(i).money);
                    break;
                case "零花钱":
                    incomeNumAndAccount.lihuaqianAccount = 1;
                    incomeNumAndAccount.lihuaqianAccount = Integer.parseInt(allIncomeContentFromDB.get(i).money);
                    break;
                case "红包":
                    incomeNumAndAccount.hongbaoNumber = 1;
                    incomeNumAndAccount.hongbaoAccount = Integer.parseInt(allIncomeContentFromDB.get(i).money);
                    break;
                case "理财收益":
                    incomeNumAndAccount.licaiNumber = 1;
                    incomeNumAndAccount.licaiAccount = Integer.parseInt(allIncomeContentFromDB.get(i).money);
                    break;
            }
        }

        allShouRuNumber = incomeNumAndAccount.salaryNumber + incomeNumAndAccount.jianzhiNumber
                + incomeNumAndAccount.lihuaqianAccount +  incomeNumAndAccount.hongbaoNumber
                + incomeNumAndAccount.licaiNumber;

        allShouRuAccount = incomeNumAndAccount.licaiAccount + incomeNumAndAccount.hongbaoAccount
                + incomeNumAndAccount.lihuaqianAccount + incomeNumAndAccount.jianzhiAccount
                + incomeNumAndAccount.salaryAccount;

        //工资对应的收入
        gongzhiPercent = incomeNumAndAccount.salaryAccount / allShouRuAccount;
        //兼职
        jianzhiPercent = incomeNumAndAccount.jianzhiAccount / allShouRuAccount;
        //零花钱
        linghuaqianPercent = incomeNumAndAccount.linhuaqianNumber / allShouRuAccount;
        //红包
        hongbaoPercent = incomeNumAndAccount.hongbaoAccount / allShouRuAccount;
        //理财收益
        licaiPercent = incomeNumAndAccount.licaiAccount / allShouRuAccount;
    }

    private void customizeLegend() {
        Legend legend = pieChart.getLegend();//设置比例图
        legend.setEnabled(true);//图例显示
    }

    //中间显示的文字数据
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("总支出\n" + allShouRuAccount);
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
    int salaryNumber = 0;
    int salaryAccount = 0;
    //兼职
    int jianzhiNumber = 0;
    int jianzhiAccount = 0;
    //零花钱
    int linhuaqianNumber = 0;
    int lihuaqianAccount = 0;
    
    //红包
    int hongbaoNumber = 0;
    int hongbaoAccount = 0;
    
    //理财收益
    int licaiNumber = 0;
    int licaiAccount = 0;
    
}
