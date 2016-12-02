package com.example.zs.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zs.adapter.WrapperRecyclerViewAdapter;
import com.example.zs.myaccount.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class HeaderRecyclerView extends RecyclerView {


    public ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    public ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();
    public static final int BASE_HEADER_VIEW_TYPE = -1 << 10;
    public static final int BASE_FOOTER_VIEW_TYPE = -1 << 11;
    public Adapter mAdapter, adapter;
    private boolean isShouldSpan;
    private int lastItem;
    private boolean isLoad=false;
    private int firstVisible;
    private boolean isTop;
    private int totalCount;
    private int[] into;
    private int[] firstInto;
    private float startY;
    private boolean isRefresh;
    private Context mContext;
    private int viewHight;

    public HeaderRecyclerView(Context context) {
        super(context);
        mContext = context;
        initView();
      //  initListener();
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
       // initListener();
    }

    public HeaderRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        mContext = context;
       // initListener();
    }


    void initView(){
        //添加头布局
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_recycler_header, null);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_for_holdplace, null);
        footerView.setVisibility(VISIBLE);
        addHeaderView(headerView);
        addFooterView(footerView);

    }


    public void addHeaderView(View view){

        FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        info.viewType = BASE_HEADER_VIEW_TYPE+mHeaderViewInfos.size();
        mHeaderViewInfos.add(info);
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public View getHeaderView(int pos){

        if(mHeaderViewInfos==null){
            throw  new IllegalStateException("you must add a headerview before!!");
        }
        return mHeaderViewInfos.get(pos).view;
    }


    public void addFooterView(View view){

        FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        info.viewType = BASE_FOOTER_VIEW_TYPE+mFooterViewInfos.size();
        mFooterViewInfos.add(info);
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public View getFooterView(int pos){

        if(mFooterViewInfos==null){
            throw  new IllegalStateException("you must add a footerview before!!");
        }
        return mFooterViewInfos.get(pos).view;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if(!(adapter instanceof WrapperRecyclerViewAdapter))
            mAdapter = new WrapperRecyclerViewAdapter(mHeaderViewInfos,mFooterViewInfos,adapter);
       // Log.i("lala","&&&&&&&&&&&&&&&&&&&&&&&&&&");
            super.setAdapter(mAdapter);
     //   Log.i("lala","&&&&&&&&&&&&&&&&&&&&&&&&&&");
        if(isShouldSpan){
            ((WrapperRecyclerViewAdapter)mAdapter).adjustSpanSize(this);
        }
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if(layout instanceof GridLayoutManager ||layout instanceof StaggeredGridLayoutManager){
            isShouldSpan = true;
        }
        super.setLayoutManager(layout);
    }

    //自定义类型，为了增加头部
    public  class FixedViewInfo{
        public View view;
        public int viewType;
    }


}



