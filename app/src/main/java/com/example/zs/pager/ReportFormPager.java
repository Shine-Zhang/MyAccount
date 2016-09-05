package com.example.zs.pager;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.zs.view.CircleImageView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zs.myaccount.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 本类是五个pager中的“报表类”，继承至基类BasePager,主要用饼状图的形式分别呈现出各种类型的收入和支出所占的比重
 */
public class ReportFormPager extends BasePager{
    //报表视图
    private View reportformpager_content_view;
    //时间段
    private TextView tv_reportform_time;
    //对应”收入“和”支出“按钮的RadioGroup
    private RadioGroup rg_reportform;
    //饼状图各部分对应的颜色，暂时先定为这样，后面根据实际情况动态调整
    private int[] colors = new int[]{Color.CYAN,Color.DKGRAY,Color.BLUE,Color.GREEN,Color.RED};
    //饼状图各部分代表的内容，暂时先定为这样，后面根据实际情况动态调整
    private String[] datas = new String[]{"一般","电影","吃饭","衣服","零食"};
    private PieChart pc_reportform_piechart;
    private ListView lv_reportform_detail;
    //ListView用的到的数据集,这里要根据以后的实际情况进行调整
    public List<String> mDatas;

    public ReportFormPager(Activity activity) {
        super(activity);
        initChart();
    }

    @Override
    public View initView() {
        reportformpager_content_view = View.inflate(mActivity, R.layout.reportformpager_content, null);
        //时间
        tv_reportform_time = (TextView) reportformpager_content_view.findViewById(R.id.tv_reportform_time);
        //最上面的两个按钮，收入和支出
        rg_reportform = (RadioGroup) reportformpager_content_view.findViewById(R.id.rg_reportform);
        //中间的圆饼图
        pc_reportform_piechart = (PieChart) reportformpager_content_view.findViewById(R.id.pc_reportform_piechart);
        //最下面的ListView
        lv_reportform_detail = (ListView) reportformpager_content_view.findViewById(R.id.lv_reportform_detail);
        //绑定适配器
        lv_reportform_detail.setAdapter(new ReportFormAdapter());
        return reportformpager_content_view;
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<String>();
        for(int i = 0;i < 50; i ++){
            mDatas.add("item" + i);
        }
    }

    private void initChart() {
        //设置饼状图是否接受点击事件，默认为true
        pc_reportform_piechart.setTouchEnabled(true);
        //设置图饼是否显示百分比
        pc_reportform_piechart.setUsePercentValues(false);
        //是否显示圆盘中间文字
        pc_reportform_piechart.setDrawCenterText(true);
        //设置圆盘中间的颜色
        pc_reportform_piechart.setHoleColor(Color.WHITE);
        //饼状图中间的文字，通过调用generateCenterSpannableText()来动态的调整显示内容
        //pc_reportform_piechart.setCenterText(generateCenterSpannableText());
        //设置中间圆盘的半径,值为所占饼图的百分比
        pc_reportform_piechart.setHoleRadius(60);
        //设置中间透明圈的半径,值为所占饼图的百分比
        //pc_reportform_piechart.setTransparentCircleRadius(65);
        //设置圆盘是否可以转动
        pc_reportform_piechart.setRotationEnabled(true);
        //旋转角度
        //pc_reportform_piechart.setRotationAngle(100);
        //pc_reportform_piechart.animateXY(1000, 1000);  //设置动画
        //将在下方显示的比例视图隐藏掉
        customizeLegend();
        //设置饼图右下角的文字描述
        pc_reportform_piechart.setDescription("");
        //设置饼图右下角的文字描述文字位置
        pc_reportform_piechart.setDescriptionPosition(300f,455f);
        pc_reportform_piechart.setDescriptionColor(Color.BLACK);
        //设置饼图右下角的文字大小
        pc_reportform_piechart.setDescriptionTextSize(16);
        //pc_reportform_piechart.setExtraOffsets(15, -20, 15, 0);//left   top  right  bottom
        //绑定数据,括号中的内容代表的饼状图将被分为几部分
        bindData(5);
        // 设置一个选中区域监听
        pc_reportform_piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                pc_reportform_piechart.setDescription(datas[e.getXIndex()] + " " + (int)e.getVal() + "元");
                Log.i("dataSetIndex",e.getXIndex() + "");

                float rotationAngle = pc_reportform_piechart.getRotationAngle();
                Log.i("dataSetIndex",rotationAngle + "");

                //动画暂时实现不了
                /*RotateAnimation rotateAnimation =
                        new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //设置旋转时间为1000毫秒
                rotateAnimation.setDuration(1000);
                rotateAnimation.setFillAfter(true);
                pc_reportform_piechart.startAnimation(rotateAnimation);*/
                //显示动画效果
                //showAmination();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void customizeLegend() {
        Legend legend = pc_reportform_piechart.getLegend();//设置比例图
        legend.setEnabled(false);//图例不显示
    }

    private void bindData(int count) {
        //nameList用来表示饼状图每部分的文字内容，如第一部分，第二部分...
        ArrayList<String> nameList = new ArrayList<String>();
        for(int i = 0;i < count;i ++){
            nameList.add("");
        }

        /**
         * valueList中的元素个数决定了将圆饼分为几部分
         * Entry的构造函数中第一个值代表所占比值，第二个代表在饼状图中的位置，
         * 还可以有第三个，表示携带的数据Object，这里没用到
         */
        ArrayList<Entry> valueList = new ArrayList<Entry>();
        valueList.add(new Entry(40,0));
        valueList.add(new Entry(30,1));
        valueList.add(new Entry(15,2));
        valueList.add(new Entry(10,3));
        valueList.add(new Entry(5,4));

        //显示在比例图上
        PieDataSet dataSet = new PieDataSet(valueList, "");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(0f);
        //各个区域不显示具体的数字，即所占百分比
        dataSet.setValueTextColor(Color.TRANSPARENT);
        //被选中部分高出的长度
        dataSet.setSelectionShift(10f);

        //设置各个区域的颜色
        ArrayList<Integer> color = new ArrayList<>();
        color.add(colors[0]);
        color.add(colors[1]);
        color.add(colors[2]);
        color.add(colors[3]);
        color.add(colors[4]);
        dataSet.setColors(color);

        PieData pieData = new PieData(nameList, dataSet);
       /* //设置以百分比显示
        pieData.setValueFormatter(new PercentFormatter());
        //区域文字的大小
        dataSet.setValueTextSize(11f);
        //设置区域文字颜色
        dataSet.setValueTextColor(Color.WHITE);
        //字体
        dataSet.setValueTypeface(Typeface.DEFAULT);*/
        pc_reportform_piechart.setData(pieData);
    }

    //ListView的适配器
    class ReportFormAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item_view = View.inflate(mActivity, R.layout.item_reportform_listview, null);
            CircleImageView iv_itemreport_icon = (CircleImageView) item_view.findViewById(R.id.civ_itemreport_icon);
            TextView tv_itemreport_name = (TextView) item_view.findViewById(R.id.tv_itemreport_name);
            TextView tv_itemreport_percent = (TextView) item_view.findViewById(R.id.tv_itemreport_percent);
            TextView tv_reportform_money = (TextView) item_view.findViewById(R.id.tv_reportform_money);

            return item_view;
        }
    }
}
