package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.IncomeContentInfo;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.PayOutContentDAO;
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
    private TextView tv_mybalanceactivity_choiceDate;
    private ListView lv_mybalanceactivity_recordlist;

    List<PayoutContentInfo> recordlist;//用于存放消费信息的集合
    private ArrayList<PayoutContentInfo> allPayoutContentFromDBList;
    private ArrayList<IncomeContentInfo> allIncomeContentFromDBList;
    private RelativeLayout rl_mybalanceactivity_norecord;
    private TextView tv_mybalance_income;
    private TextView tv_mybalance_zhichu;
    private ArrayList<PayoutContentInfo> datePayoutContentFromDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        getSupportActionBar().hide();

        //tv_mybalanceactivity_choiceDate = (TextView) findViewById(R.id.tv_mybalanceactivity_choiceDate);
        lv_mybalanceactivity_recordlist = (ListView) findViewById(R.id.lv_mybalanceactivity_recordlist);
        rl_mybalanceactivity_norecord = (RelativeLayout) findViewById(R.id.rl_mybalanceactivity_norecord);
        tv_mybalance_income = (TextView) findViewById(R.id.tv_mybalance_income);
        tv_mybalance_zhichu = (TextView) findViewById(R.id.tv_mybalanceactivity_pay);
        initData();

        //从sp文件中取出已经选择是日期数据，用于回显
        String choiceDate = MyAplication.getStringFromSp("choiceDate");
        if(!choiceDate.isEmpty()){
         //   tv_mybalanceactivity_choiceDate.setText(choiceDate);
        }

        MyRecordListAdapter myRecordListAdapter = new MyRecordListAdapter();
        lv_mybalanceactivity_recordlist.setAdapter(myRecordListAdapter);

    }

    private void initData() {
        //查询有无收入或支出记录
        PayOutContentDAO payOutContentDAO = new PayOutContentDAO(this);
        allPayoutContentFromDBList = payOutContentDAO.getAllPayoutContentFromDB();
        //得到
        datePayoutContentFromDB = payOutContentDAO.getDatePayoutContentFromDB(2016, 9);
        //比较日期
        compareDate();
        float moneySum = payOutContentDAO.getMoneySum();
        tv_mybalance_zhichu.setText(moneySum+"");
        IncomeContentDAO incomeContentDAO = new IncomeContentDAO(this);
        allIncomeContentFromDBList = incomeContentDAO.getAllIncomeContentFromDB();
        float moneySum1 = incomeContentDAO.getMoneySum();
        tv_mybalance_income.setText(moneySum1+"");
        if (allPayoutContentFromDBList.size()!=0||allIncomeContentFromDBList.size()!=0){
            Log.i(TAG,"allIncomeContentFromDBList="+allIncomeContentFromDBList);
            Log.i(TAG,"allPayoutContentFromDBList="+allPayoutContentFromDBList);
            rl_mybalanceactivity_norecord.setVisibility(View.GONE);
        }
    }
    ArrayList<PayoutContentInfo> pList;
    private void compareDate() {

            }
    /**
     * TextView点击事件，弹出日期选择器，获取日期
     * @param view
     */
    public void settingtime(View view){
        startActivityForResult(new Intent(MyBalanceActivity.this,ChoiceDateActivity.class),100);
    }

    /**
     * 获取从下个页面传回来的日期信息，并修改TextView
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String choiceDate = data.getStringExtra("choiceDate");
        Log.i(TAG,"requestCode="+requestCode+" resultCode="+resultCode+" choiceDate="+choiceDate);

        if(requestCode==100&&resultCode==200 && !choiceDate.isEmpty()){
            tv_mybalanceactivity_choiceDate.setText(choiceDate);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back(View view){
        finish();
    }

    private class MyRecordListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            Log.i(TAG,allIncomeContentFromDBList.size()+"--"+allPayoutContentFromDBList.size());
            //return allIncomeContentFromDBList.size()+allPayoutContentFromDBList.size()+1;
            Log.i(TAG,"plist"+pList.size());
            return pList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View recordView = View.inflate(MyBalanceActivity.this,R.layout.item_recordlist,null);
            CircleImageView iv_recordlist_icon = (CircleImageView) recordView.findViewById(R.id.iv_recordlist_icon);
            TextView tv_recordlist_category = (TextView) recordView.findViewById(R.id.tv_recordlist_category);
            TextView tv_recordlist_remarks = (TextView) recordView.findViewById(R.id.tv_recordlist_remarks);
            TextView tv_recordlist_money = (TextView) recordView.findViewById(R.id.tv_recordlist_money);
            int paysize = allPayoutContentFromDBList.size();
            int insize = allIncomeContentFromDBList.size();
            //收入和支出分开排列
            if (pList.size()!=0){
                //有收入或支出
                if (pList.get(i).day==40){
                    Log.i(TAG,i+"=="+pList.get(i).day);
                    Log.i(TAG,"fei 40");
                    View inflate = View.inflate(MyBalanceActivity.this, R.layout.item_timeshow, null);
                    TextView tv_record_time = (TextView) inflate.findViewById(R.id.tv_record_time);
                    tv_record_time.setText("2016年9月"+pList.get(i+1).day+"日");
                    return inflate;
                }else {
                    Log.i(TAG,"fei 0");
                    recordView.setVisibility(View.VISIBLE);
                    iv_recordlist_icon.setImageResource(pList.get(i).resourceID);
                    tv_recordlist_category.setText(pList.get(i).category);
                    tv_recordlist_remarks.setText(pList.get(i).remarks);
                    tv_recordlist_money.setText("-"+pList.get(i).money);
                    return recordView;
                }
            }else {
                return null;
            }
           /* if (i==0){
                View inflate = View.inflate(MyBalanceActivity.this, R.layout.item_timeshow, null);
                TextView tv_record_time = (TextView) inflate.findViewById(R.id.tv_record_time);
                String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
                Calendar cal = Calendar.getInstance();
                int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
                if(week_index<0){
                    week_index = 0;
                }
                Log.i(TAG,""+cal.YEAR+"pp--"+cal.MONTH+"--"+cal.DAY_OF_MONTH);
                tv_record_time.setText(""+cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日  "+weeks[week_index]);
                return inflate;
            }else {
                if (i<paysize+1){
                    PayoutContentInfo payoutContentInfo = allPayoutContentFromDBList.get(paysize-i);
                    Log.i(TAG,payoutContentInfo.toString());
                    iv_recordlist_icon.setImageResource(payoutContentInfo.resourceID);
                    tv_recordlist_category.setText(payoutContentInfo.category);
                    if (!payoutContentInfo.remarks.isEmpty()){
                        tv_recordlist_remarks.setVisibility(View.VISIBLE);
                        tv_recordlist_remarks.setText(payoutContentInfo.remarks);
                    }
                    tv_recordlist_money.setText("-"+payoutContentInfo.money);
                }else if (i<paysize+insize+1){
                    IncomeContentInfo incomeContentInfo = allIncomeContentFromDBList.get(insize+paysize-i);
                    iv_recordlist_icon.setImageResource(incomeContentInfo.resourceID);
                    tv_recordlist_category.setText(incomeContentInfo.category);
                    if (!incomeContentInfo.remarks.isEmpty()){
                        tv_recordlist_remarks.setVisibility(View.VISIBLE);
                        tv_recordlist_remarks.setText(incomeContentInfo.remarks);
                    }
                    tv_recordlist_money.setText("+"+incomeContentInfo.money);
                }
            }*/

        }
    }
}
