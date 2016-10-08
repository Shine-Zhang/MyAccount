package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.dao.TimeLineDAO;
import com.example.zs.utils.SeletorUtils;
import com.example.zs.view.CircleImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class MyBalanceActivity extends AppCompatActivity {

    private static final String TAG = "MyBalanceActivity";
    private TextView tv_mybalanceactivity_titleDate;
    private ListView lv_mybalanceactivity_recordlist;

    List<PayoutContentInfo> recordlist;//用于存放消费信息的集合
    private ArrayList<PayoutContentInfo> allPayoutContentFromDBList;
    private ArrayList<IncomeContentInfo> allIncomeContentFromDBList;
    private RelativeLayout rl_mybalanceactivity_norecord;
    private TextView tv_mybalance_income;
    private TextView tv_mybalance_zhichu;
    private ArrayList<PayoutContentInfo> datePayoutContentFromDB;
    private ExpandableListView detailList;
    private String[] groupStrings;
    private String[][] childStrings;
    private ArrayList<ArrayList<AccountChildItemBean>> childData;
    private ArrayList<AccountGroupItemBean> groupData;
    private int month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        getSupportActionBar().hide();

        tv_mybalanceactivity_titleDate = (TextView) findViewById(R.id.tv_mybalanceactivity_titleDate);
        rl_mybalanceactivity_norecord = (RelativeLayout) findViewById(R.id.rl_mybalanceactivity_norecord);
        tv_mybalance_income = (TextView) findViewById(R.id.tv_mybalance_income);
        tv_mybalance_zhichu = (TextView) findViewById(R.id.tv_mybalanceactivity_pay);
        detailList = (ExpandableListView) findViewById(R.id.expandablelv_mybalanceactivity_detailList);
        initData();

        detailList.setAdapter(new MyExpandableListAdapter());
        if(!groupData.isEmpty()){
            //rl_mybalanceactivity_norecord.setVisibility(View.GONE);
            Log.i("???","有数据，展开");
            detailList.expandGroup(0);//默认展开第0个组
        }else {
            rl_mybalanceactivity_norecord.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        //获取此时的年月
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        Log.i("?????","month="+ month);
        tv_mybalanceactivity_titleDate.setText(year+"年"+ month +"月的账单详情");

        //获取需要显示的数据（测试数据）
        if (childData!=null) {
            childData.clear();
        }
        if (groupData!=null) {
            groupData.clear();
        }
        TimeLineDAO detailDao = new TimeLineDAO(this);
        childData = detailDao.getTimeLinePayOutChildData(month);
        groupData = detailDao.getTimeLineGroupData(month);
        Log.i("zzzzz",childData.toString());
        Log.i("zzzzz",groupData.toString());

        //查询收入和支出
        IncomeContentDAO incomeContentDAO = new IncomeContentDAO(this);
        String incomeForMonth = incomeContentDAO.getIncomeForMonth(month);
        PayOutContentDAO payOutContentDAO = new PayOutContentDAO(this);
        String payoutForMonth = payOutContentDAO.getExpenseForMonth(month);
        tv_mybalance_income.setText(incomeForMonth);
        tv_mybalance_zhichu.setText(payoutForMonth);
       /* groupStrings = new String[]{"西游记", "水浒传", "三国演义", "红楼梦"};
        childStrings = new String[][]{
                {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
                {"宋江", "林冲", "李逵", "鲁智深"},
                {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
                {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
        };*/

    }

    public void back(View view){
        finish();
    }

    private class MyExpandableListAdapter extends BaseExpandableListAdapter {
        //获取分组（父列表）的个数
        @Override
        public int getGroupCount() {
            return groupData.size();
        }

        //获取指定分组中的子选项的个数
        @Override
        public int getChildrenCount(int i) {
            return childData.get(i).size();
        }

        //获取指定的分组数据
        @Override
        public Object getGroup(int i) {
            return groupData.get(i);
        }

        //获取指定分组中的指定子选项数据
        @Override
        public Object getChild(int i, int j) {
            return childData.get(i).get(j);
        }

        //获取指定分组的ID, 这个ID必须是唯一的
        @Override
        public long getGroupId(int groupId) {
            return groupId;
        }

        //获取子选项的ID, 这个ID必须是唯一的
        @Override
        public long getChildId(int groupId, int childId) {
            return childId;
        }

        //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
        @Override
        public boolean hasStableIds() {
            return true;
        }

        //获取显示指定分组的视图
        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {

            GroupViewHolder groupViewHolder;
            View view = null;
            if(convertView!=null){
                view = convertView;
                groupViewHolder = (GroupViewHolder) view.getTag();
            }else {
                view = LayoutInflater.from(MyBalanceActivity.this).inflate(R.layout.groupitem_expandablelistview,null);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTitle = (TextView) view.findViewById(R.id.label_expand_group);
                view.setTag(groupViewHolder);
            }

            groupViewHolder.tvTitle.setText("2016年"+month+"月"+groupData.get(i).getDayOfMonth()+"号");
            return view;
        }

        //获取显示指定分组中的指定子选项的视图
        @Override
        public View getChildView(int i, int j, boolean b, View convertView, ViewGroup viewGroup) {

            ChildViewHolder childViewHolder;
            View view = null;
            if(convertView!=null){
                view = convertView;
                childViewHolder = (ChildViewHolder) view.getTag();
            }else {
                view = LayoutInflater.from(MyBalanceActivity.this).inflate(R.layout.childitem_expandablelistview,null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.iv_myactivity_childItem_Icon = (ImageView) view.findViewById(R.id.iv_myactivity_childItem_icon);
                childViewHolder.tv_myactivity_childItem_describe = (TextView) view.findViewById(R.id.tv_myactivity_childItem_describe);
                childViewHolder.tv_myactivity_childItem_howMuch = (TextView) view.findViewById(R.id.tv_myactivity_childItem_howMuch);
                view.setTag(childViewHolder);
            }

            childViewHolder.iv_myactivity_childItem_Icon.setImageResource(childData.get(i).get(j).getIcon());
            //childViewHolder.iv_myactivity_childItem_Icon.setBackgroundResource(R.drawable.account_pager_group_today_icon);
            int iconId = childData.get(i).get(j).getIcon();
            SeletorUtils.setBackgroud(iconId,childViewHolder.iv_myactivity_childItem_Icon);

            childViewHolder.tv_myactivity_childItem_describe.setText(childData.get(i).get(j).getItemDescribe());
            if(childData.get(i).get(j).isIncome()){
                childViewHolder.tv_myactivity_childItem_howMuch.setText("+ "+childData.get(i).get(j).getHowmuch());
            }else {
                childViewHolder.tv_myactivity_childItem_howMuch.setText("- "+childData.get(i).get(j).getHowmuch());
            }

            return view;
        }

        //指定位置上的子元素是否可选中
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    private class GroupViewHolder {
        TextView tvTitle;
    }

    private class ChildViewHolder {
        ImageView iv_myactivity_childItem_Icon;
        TextView tv_myactivity_childItem_describe;
        TextView tv_myactivity_childItem_howMuch;
    }
}
