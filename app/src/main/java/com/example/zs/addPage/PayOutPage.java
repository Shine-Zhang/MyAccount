package com.example.zs.addPage;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zs.myaccount.AddCategoryActivity;
import com.example.zs.myaccount.R;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;


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

    public PayOutPage(Context ctx) {
        super(ctx);
    }

    @Override
    public View initView() {
        icons = new int[]{R.drawable.ic_2_yellow,R.drawable.ic_3_yellow,R.drawable.ic_default_wish,
                R.drawable.ic_4_yellow,R.drawable.ic_5_yellow,R.drawable.ic_default_wish,
                R.drawable.ic_default_wish,R.drawable.ic_default_wish,R.drawable.ic_default_wish};
        contents = new String[]{"一般","一般","一般","一般","一般","一般","一般","一般",
                "一般","一般","一般","一般","一般","一般","一般","一般"};
       // GridView gridView = new GridView(ctx);
        View inflate = View.inflate(ctx, R.layout.gridview_layout, null);
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
                    ctx.startActivity(new Intent(ctx, AddCategoryActivity.class));
                }
                Log.i(TAG,"--"+i);
            }
        });
        return gridView;
    }

    class MyGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return icons.length+1;
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
            View inflate = View.inflate(ctx, R.layout.page_addrecord_detail, null);
            ImageView iv_addPage_catagoryIcon = (ImageView) inflate.findViewById(R.id.iv_addPage_catagoryIcon);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);
            if(i<icons.length){
                iv_addPage_catagoryIcon.setImageResource(icons[i]);
                tv_addPage_catagoryContent.setText(contents[i]+"");
            }
            //最后一个为默认item，作用为跳转到addCategory页面
            if(i==icons.length){
                iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                tv_addPage_catagoryContent.setText("添加");
            }
            return inflate;
        }
    }

}

