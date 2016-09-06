package com.example.zs.addPage;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.myaccount.AddCategoryActivity;
import com.example.zs.myaccount.AddRecordActivity;
import com.example.zs.myaccount.R;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by wuqi on 2016/9/4 0004.
 */
public class PayOutPage extends AddBasePage {
    private int COLUMS_NUMBER = 5;
    private GridView gridView;
    private String TAG="PayOutPage";
    private int DEFAULT_GRIDVIEW_ITEM = 0;
    private int[] icons;
    private String[] contents;
    private AddRecordActivity addRecordActivity;
    private ArrayList<UserAddCategoryInfo> payoutCategoryToDB;

    public PayOutPage(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        Log.i(TAG,"initView");
        initData();
        addRecordActivity = (AddRecordActivity) activity;
        icons = new int[]{R.drawable.ic_2_yellow,R.drawable.ic_3_yellow,R.drawable.ic_default_wish,
                R.drawable.ic_4_yellow,R.drawable.ic_5_yellow,R.drawable.ic_default_wish,
                R.drawable.ic_default_wish,R.drawable.ic_default_wish,R.drawable.ic_default_wish};
        contents = new String[]{"一般","一般","一般","一般","一般","一般","一般","一般",
                "一般","一般","一般","一般","一般","一般","一般","一般"};
       // GridView gridView = new GridView(activity);
        View inflate = View.inflate(activity, R.layout.gridview_layout, null);
        gridView = (GridView) inflate.findViewById(R.id.gv_addRecord_gridView);
        //得到屏幕的宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        //设置GridView控件参数
        //gridView.setColumnWidth(40);
        //gridView.setNumColumns(COLUMS_NUMBER);
        gridView.setAdapter(new MyGridViewAdapter());
        //设置gridviewItem监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==icons.length){
                    //跳转到addCategory页面
                    activity.startActivityForResult(new Intent(activity, AddCategoryActivity.class),100);
                    getActivityResult();
                }
                Log.i(TAG,"--"+i);
            }
        });
        return gridView;
    }

    /**
     * 主要为进入page时查询数据，填充gridview
     */
    private void initData() {
        Log.i(TAG,"initData");
        PayoutCategoryDAO payoutCategoryDAO = new PayoutCategoryDAO(activity);
        payoutCategoryToDB = payoutCategoryDAO.getPayoutCategoryToDB();
        Log.i(TAG, payoutCategoryToDB.toString());
        Log.i(TAG, payoutCategoryToDB.get(0).toString());
    }

    private void getActivityResult() {
        if (addRecordActivity.ActivityResult_FLAG){
            //AddCategoryActivity传回了数据
            //调用addRecordActivity的方法获取返回的数据
            UserAddCategoryInfo activityResult = addRecordActivity.getActivityResult();
            //gridview刷新数据

        }
    }


    class MyGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return payoutCategoryToDB.size()+1;
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
            //
            View inflate = View.inflate(activity, R.layout.page_addrecord_detail, null);
            ImageView iv_addPage_catagoryIcon = (ImageView) inflate.findViewById(R.id.iv_addPage_catagoryIcon);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);
            if(i<payoutCategoryToDB.size()){
               /* iv_addPage_catagoryIcon.setImageResource(icons[i]);
                tv_addPage_catagoryContent.setText(contents[i]+"");*/
                iv_addPage_catagoryIcon.setImageResource(payoutCategoryToDB.get(i).getResourceID());
                tv_addPage_catagoryContent.setText(payoutCategoryToDB.get(i).getCategoryName());

            }else
            //最后一个为默认item，作用为跳转到addCategory页面
            if(i==payoutCategoryToDB.size()){
                iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                tv_addPage_catagoryContent.setText("添加");
            }
            return inflate;
        }
    }

}

