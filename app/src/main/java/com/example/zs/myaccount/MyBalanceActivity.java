package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.PayoutContentInfo;

import java.util.List;

/**
 * Created by 钟云婷 on 2016/8/30.
 */
public class MyBalanceActivity extends AppCompatActivity {

    private static final String TAG = "MyBalanceActivity";
    private TextView tv_mybalanceactivity_choiceDate;
    private ListView lv_mybalanceactivity_recordlist;

    List<PayoutContentInfo> recordlist;//用于存放消费信息的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        getSupportActionBar().hide();

        recordlist = null;

        tv_mybalanceactivity_choiceDate = (TextView) findViewById(R.id.tv_mybalanceactivity_choiceDate);
        lv_mybalanceactivity_recordlist = (ListView) findViewById(R.id.lv_mybalanceactivity_recordlist);
        RelativeLayout rl_mybalanceactivity_norecord = (RelativeLayout) findViewById(R.id.rl_mybalanceactivity_norecord);


        //从sp文件中取出已经选择是日期数据，用于回显
        String choiceDate = MyAplication.getStringFromSp("choiceDate");
        if(!choiceDate.isEmpty()){
            tv_mybalanceactivity_choiceDate.setText(choiceDate);
        }

        /*if (!(recordlist.size()==0)) {
            lv_mybalanceactivity_recordlist.setVisibility(View.VISIBLE);
            rl_mybalanceactivity_norecord.setVisibility(View.INVISIBLE);
            //当集合中有数据时，设置ListView可见，并为ListView设置Adapter
            lv_mybalanceactivity_recordlist.setAdapter(new MyRecordListAdapter());
        }
*/

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
            return 1;
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

            if (recordlist.size()==0){

            }

            View recordView = View.inflate(MyBalanceActivity.this,R.layout.item_recordlist,null);


            return recordView;
        }
    }
}
