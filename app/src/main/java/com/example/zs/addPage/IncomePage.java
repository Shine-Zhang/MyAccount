package com.example.zs.addPage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zs.myaccount.R;

/**
 * Created by wuqi on 2016/9/4 0004.
 */
public class IncomePage extends AddBasePage {
    private int COLUMS_NUMBER = 5;
    public IncomePage(Context ctx) {
        super(ctx);
    }

    @Override
    public View initView() {
        GridView gridView = new GridView(ctx);
        gridView.setNumColumns(COLUMS_NUMBER);
        gridView.setAdapter(new MyGridViewAdapter());
        return gridView;
    }
    int[] icons = {R.drawable.ic_default_wish,R.drawable.ic_default_wish,R.drawable.ic_default_wish,
            R.drawable.ic_default_wish,R.drawable.ic_default_wish,R.drawable.ic_default_wish,
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
