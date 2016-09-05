package com.example.zs.myaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AddCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        //隐藏标题栏
        getSupportActionBar().hide();
        //找到gridview的布局
        GridView gv_addCategory_content = (GridView) findViewById(R.id.gv_addCategory_content);
        //设置适配器
        gv_addCategory_content.setAdapter(new MyGridViewAdapter());

    }

    /**
     * 适配器类
     */
    class MyGridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 6;//icons.length+1;
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
           /* View inflate = View.inflate(AddCategoryActivity.this, R.layout.page_addrecord_detail, null);
            ImageView iv_addPage_catagoryIcon = (ImageView) inflate.findViewById(R.id.iv_addPage_catagoryIcon);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);
                iv_addPage_catagoryIcon.setImageResource(icons[i]);
                tv_addPage_catagoryContent.setText(contents[i]+"");*/

            //最后一个为默认item，作用为跳转到addCategory页面
            return null;
        }
    }

}
