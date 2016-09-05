package com.example.zs.addPage;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;

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
    public PayOutPage(Context ctx) {
        super(ctx);
    }

    @Override
    public View initView() {
       // GridView gridView = new GridView(ctx);
        View inflate = View.inflate(ctx, R.layout.gridview_layout, null);
        GridView gridView = (GridView) inflate.findViewById(R.id.gv_addRecord_gridView);
        //得到屏幕的宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        //设置GridView控件参数
        //gridView.setColumnWidth(40);
        //gridView.setNumColumns(COLUMS_NUMBER);
        gridView.setAdapter(new MyGridViewAdapter());
        return gridView;
    }
    int[] icons = {R.drawable.ic_2_yellow,R.drawable.ic_3_yellow,R.drawable.ic_default_wish,
            R.drawable.ic_4_yellow,R.drawable.ic_5_yellow,R.drawable.ic_default_wish,
            R.drawable.ic_default_wish,R.drawable.ic_default_wish,R.drawable.ic_default_wish};
    String [] contents ={"一般","一般","一般","一般","一般","一般","一般","一般",
            "一般","一般","一般","一般","一般","一般","一般","一般"};
    class MyGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 8;
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
            iv_addPage_catagoryIcon.setImageResource(icons[i]);
            tv_addPage_catagoryContent.setText(contents[i]+"");
            return inflate;
        }
    }
}

