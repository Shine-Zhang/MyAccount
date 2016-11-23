package com.example.zs.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zs.listener.OnArriveTopListener;
import com.example.zs.myaccount.R;
import com.example.zs.utils.SyncBackgroudUtils;
import com.example.zs.view.HeaderRecyclerView;

import it.gmariotti.recyclerview.itemanimator.BaseItemAnimator;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class MycustomAnimation extends BaseItemAnimator {

    float mOriginalY;
    public int mRid;
    public String mCataString;
    public String mAccountString;
    public int mColor;
    private int baseHeight = -1;
    private int dis;
    public int position;

    public void setContainers(ImageView img,TextView tx1,TextView tx2) {
        this.img = img;
        this.tx1 = tx1;
        this.tx2 = tx2;
    }



    public ImageView img;
    public TextView tx1;
    public TextView tx2;
    public void setArriveTopListener(OnArriveTopListener arriveTopListener) {
        this.arriveTopListener = arriveTopListener;
    }

    private OnArriveTopListener arriveTopListener;

    public MycustomAnimation(RecyclerView recyclerView) {
        super(recyclerView);
    }

    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        int hight = view.getHeight();
        int adapterPosition = position+1;
        Log.i("haha","adapterPosition = "+adapterPosition+"---"+holder.getOldPosition());
        dis = hight*adapterPosition;
        //mOriginalY -= (view.getHeight()+view.getPaddingBottom()+view.getPaddingTop());
        Log.i("lala",")))))))"+(view.getHeight()+view.getPaddingBottom()+view.getPaddingTop()));
        Log.i("lala","7777mOriginalY = "+mOriginalY);
        //retrieveItemHeight(holder);
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
        animation.setDuration(400)
                .alpha(0.1f)
                .translationY(-dis)
                .setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchRemoveStarting(holder);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        ViewCompat.setAlpha(view, 1);
                        //ViewCompat.setTranslationY(view, -dis);
                        dispatchRemoveFinished(holder);
                        dispatchFinishedWhenDone();
                        img.setImageResource(mRid);
                        SyncBackgroudUtils.setTimeLineBackgroud(mRid, img, mColor);
                        tx1.setText(mCataString);
                        tx2.setText(mAccountString);
                        HeaderRecyclerView tmp = (HeaderRecyclerView) mRecyclerView;
                        if(tmp.getHeaderView(0).getVisibility() == View.GONE||mRecyclerView.getVisibility() == View.GONE) {
                            tmp.getHeaderView(0).setVisibility(View.VISIBLE);
                        }
                            mRemoveAnimations.remove(holder);


                    }
                }).start();
    }

    @Override
    protected void prepareAnimateAdd(RecyclerView.ViewHolder holder) {
        retrieveItemHeight(holder);
        ViewCompat.setTranslationY(holder.itemView, -mOriginalY);
    }

    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animation.translationY(0)
                .alpha(1)
                .setDuration(getAddDuration())
                .setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        ViewCompat.setAlpha(view, 1);
                        ViewCompat.setTranslationY(view, 0);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        ViewCompat.setTranslationY(view, 0);
                        ViewCompat.setAlpha(view, 1);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
    }


    private void retrieveItemHeight(final RecyclerView.ViewHolder holder) {
        mOriginalY = mRecyclerView.getLayoutManager().getDecoratedBottom(holder.itemView);
    }

    public void setParams(int mRid,String mCataString,String mAccountString,int color,int position){
        this.mRid = mRid;
        this.mCataString = mCataString;
        this.mAccountString = mAccountString;
        this.mColor = color;
        this.position = position;
    }


}

