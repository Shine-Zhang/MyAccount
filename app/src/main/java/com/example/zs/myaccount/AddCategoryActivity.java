package com.example.zs.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.zs.bean.MyAllCatoryInfo;
import com.example.zs.dao.AllCategoryDAO;
import com.example.zs.view.CircleImageView;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {

    private ArrayList<MyAllCatoryInfo> cateoryList;
    private String TAG="AddCategoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        //隐藏标题栏
        getSupportActionBar().hide();
        //找到gridview的布局
        GridView gv_addCategory_content = (GridView) findViewById(R.id.gv_addCategory_content);
        final CircleImageView cv_addaddCategory_choiceIcon = (CircleImageView) findViewById(R.id.cv_addaddCategory_choiceIcon);
        getInfoFromDB();
        //设置适配器
        gv_addCategory_content.setAdapter(new MyGridViewAdapter());
        //设置GridView item监听事件
        gv_addCategory_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //用户点击item后在上方显示当前选中的Category
                cv_addaddCategory_choiceIcon.setImageResource(cateoryList.get(i).getResourceID());
            }
        });

    }

    /**
     * 从数据库中获取AllCategoryDB表格的信息
     */
    private void getInfoFromDB() {
        AllCategoryDAO allCategoryDAO = new AllCategoryDAO(this);
        cateoryList = allCategoryDAO.getCateoryList();
        Log.i(TAG,"--"+cateoryList.size());
        Log.i(TAG,"--"+cateoryList.toString());

    }

    /**
     * 适配器类
     */
    class MyGridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return cateoryList.size();//icons.length+1;
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
            View inflate = View.inflate(AddCategoryActivity.this, R.layout.page_addrecord_detail, null);
            CircleImageView iv_addPage_catagoryIcon = (CircleImageView) inflate.findViewById(R.id.iv_addPage_catagoryIcon);
            iv_addPage_catagoryIcon.setImageResource(cateoryList.get(i).getResourceID());
            //最后一个为默认item，作用为跳转到addCategory页面
            return inflate;
        }
    }

}
