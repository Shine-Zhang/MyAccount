package com.example.zs.myaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.bean.MyAllCatoryInfo;
import com.example.zs.dao.AllCategoryDAO;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.view.CircleImageView;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {

    private ArrayList<MyAllCatoryInfo> cateoryList;
    private String TAG="AddCategoryActivity";
    private CircleImageView cv_addaddCategory_choiceIcon;
    private EditText et_addCategory_categoryName;
    private PayoutCategoryDAO payoutCategoryDAO;
    private int userChoiceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        //隐藏标题栏
        getSupportActionBar().hide();
        //找到gridview的布局
        GridView gv_addCategory_content = (GridView) findViewById(R.id.gv_addCategory_content);
        cv_addaddCategory_choiceIcon = (CircleImageView) findViewById(R.id.cv_addCategory_choiceIcon);
        et_addCategory_categoryName = (EditText) findViewById(R.id.et_addCategory_categoryName);

        //从AllCategoryDB表格获取全部的Category信息
        getInfoFromDB();

        //设置适配器
        gv_addCategory_content.setAdapter(new MyGridViewAdapter());
        //设置GridView item监听事件
        gv_addCategory_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //用户点击item后在上方显示当前选中的Category
                userChoiceID = cateoryList.get(i).getResourceID();
                cv_addaddCategory_choiceIcon.setImageResource(userChoiceID);
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
     * 返回和确定按钮监听实现
     * @param v
     */
    public void  backorconfirm(View v){
        if(v.getId()==R.id.ib_addCategory_back){
            finish();
            Log.i(TAG,"-----");
            return;
        }
        if (v.getId()==R.id.tv_addCategory_confirm){
            Log.i(TAG,"test");
            String resultName = et_addCategory_categoryName.getText().toString();
            Log.i(TAG,"--"+resultName);
            if (resultName.isEmpty()){
                //用户没有输入
                Toast.makeText(this,"类别不能为空",Toast.LENGTH_SHORT).show();
            }else {
                //确认后写入到收入或输出的Category数据库表格中
                Log.i(TAG,""+userChoiceID);
                Log.i(TAG,resultName);
                if(userChoiceID==0){
                    //用户没有选择，设置默认的图标
                    userChoiceID = R.drawable.ic_yiban_default;
                }
                //payoutCategoryDAO.addPayoutCategoryToDB(userChoiceID,resultName);
                //携带数据到addRecord页面
                Intent intent = new Intent();
                intent.putExtra("resourceID",userChoiceID);
                intent.putExtra("categoryName",resultName);
                setResult(10,intent);
                finish();
            }

        }

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
            View inflate = View.inflate(AddCategoryActivity.this, R.layout.page_addcategory_detail, null);
            CircleImageView iv_addCategory_catagoryIcon = (CircleImageView) inflate.findViewById(R.id.iv_addCategory_catagoryIcon);
            iv_addCategory_catagoryIcon.setImageResource(cateoryList.get(i).getResourceID());
            //最后一个为默认item，作用为跳转到addCategory页面
            return inflate;
        }
    }

}
