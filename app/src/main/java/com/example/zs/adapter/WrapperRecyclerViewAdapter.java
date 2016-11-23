package com.example.zs.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.example.zs.view.HeaderRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class WrapperRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    public List<HeaderRecyclerView.FixedViewInfo> mHeaderViewInfos;
    public List<HeaderRecyclerView.FixedViewInfo> mFooterViewInfos;
    public RecyclerView.Adapter mInnerAdapter;
    private boolean isStaggered;
    private boolean isFooter;
    public WrapperRecyclerViewAdapter(List<HeaderRecyclerView.FixedViewInfo> mHeaderViewInfos, List<HeaderRecyclerView.FixedViewInfo>
                                            mFooterViewInfos, RecyclerView.Adapter adapter){

        this.mFooterViewInfos = mFooterViewInfos;
        this.mHeaderViewInfos = mHeaderViewInfos;
        this.mInnerAdapter = adapter;
        Log.i("lala","mFooterViewInfos.size(): "+mFooterViewInfos.size());
       // Log.i("lala","header: "+mHeaderViewInfos.size());
        //Log.i("lala","adapter: "+adapter.getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // Log.i("lala","444444444444444444444444444444");
        if(viewType>=HeaderRecyclerView.BASE_HEADER_VIEW_TYPE&&viewType<=HeaderRecyclerView.BASE_HEADER_VIEW_TYPE
                +mHeaderViewInfos.size()){

           View view = mHeaderViewInfos.get(viewType-HeaderRecyclerView.BASE_HEADER_VIEW_TYPE).view;
            return viewHolder(view);
        }else if(viewType>=HeaderRecyclerView.BASE_FOOTER_VIEW_TYPE&&viewType<HeaderRecyclerView.BASE_FOOTER_VIEW_TYPE+mFooterViewInfos.size()){

            View view = mFooterViewInfos.get(viewType-HeaderRecyclerView.BASE_FOOTER_VIEW_TYPE).view;
            Log.i("797979797","hahahahahahahahahah**"+view);
             return viewHolder(view);
        }


        return mInnerAdapter.onCreateViewHolder(parent,viewType) ;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(isHeaderPos(position)){

            return;
        }else if(isFooterPos(position)){

            return;
        }
        int realPos = position-getHeaderCount();
        if(mInnerAdapter!=null) {

                mInnerAdapter.onBindViewHolder(holder, realPos);

        }
    }

    public int getHeaderCount(){

        return mHeaderViewInfos.size();
    }

    public int getFooterCount(){

        return mFooterViewInfos.size();
    }

    public int getRealCount(){

        return mInnerAdapter.getItemCount();
    }

    public boolean isHeaderPos(int pos){

           return pos<getHeaderCount();

    }

    public boolean isFooterPos(int pos){

        return pos>=getHeaderCount()+getRealCount();

    }


    public int getItemViewType(int pos){
        if(isHeaderPos(pos)){

            return mHeaderViewInfos.get(pos).viewType;
        }

        if(isFooterPos(pos)){
            int realPos = pos-getHeaderCount()-getRealCount();
            return mFooterViewInfos.get(realPos).viewType;
        }
        return mInnerAdapter.getItemViewType(pos);
    }



    @Override
    public int getItemCount() {

        if(mInnerAdapter!=null){

            return getHeaderCount()+getFooterCount()+getRealCount();
        }else{

            return getHeaderCount()+getFooterCount();
        }
    }


    public void adjustSpanSize(RecyclerView recyclerView){
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(isHeaderPos(position)||isFooterPos(position)){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
            isStaggered=true;
        }
    }

    public ViewHolder viewHolder(View itemView){
        if(isStaggered){
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            itemView.setLayoutParams(params);
        }
        return new ViewHolder(itemView){
        };
    }



}
