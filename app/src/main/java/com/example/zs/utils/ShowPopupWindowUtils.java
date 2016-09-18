package com.example.zs.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.bean.WishInfo;
import com.example.zs.myaccount.AddWishActivity;
import com.example.zs.myaccount.R;
import com.example.zs.view.CircleImageView;
import com.example.zs.view.RoundProgressBar;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ShowPopupWindowUtils {

    //设置两个常量，页面的标识
    public static final String FROMADD = "add";
    public static final String FROMEDIT = "edit";
    private static PopupWindow popupwindow_showwishdetail;
    private static  Activity mActivity;

    /**
     * 显示愿望详情的页面
     * @param info
     * @param view
     * @param adapterPosition
     */
    public static void showWishDetail(Activity activity,final WishInfo info, View view, final int adapterPosition) {
        mActivity = activity;
        //初始化popupwindow
        popupwindow_showwishdetail = new PopupWindow();
        //加载popupwindow的界面
        View view_wishdetail = View.inflate(mActivity, R.layout.popupwindow_wishdetail, null);

        //找控件
        LinearLayout ll_popupwindowwishdetail_detailtop = (LinearLayout) view_wishdetail.findViewById(R.id.ll_popupwindowwishdetail_detailtop);
        LinearLayout ll_popupwindowwishdetail_detailbottom = (LinearLayout) view_wishdetail.findViewById(R.id.ll_popupwindowwishdetail_detailbottom);
        TextView tv_popupwindowwishdetail_title = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_title);
        CircleImageView civ_popupwindowwishdetail_close = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_close);
        TextView tv_popupwindowwishdetail_wishtitle = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_wishtitle);
        TextView tv_popupwindowwishdetail_progress = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_progress);
        TextView tv_popupwindowwishdetail_wishdescription = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_wishdescription);
        RoundProgressBar rpb_popupwindowwishdetail_progress = (RoundProgressBar) view_wishdetail.findViewById(R.id.rpb_popupwindowwishdetail_progress);
        CircleImageView civ_popupwindowwishdetail_edit = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_edit);
        CircleImageView civ_popupwindowwishdetail_pen = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_pen);
        ImageView iv_popupwindowwishdetail_photo = (ImageView) view_wishdetail.findViewById(R.id.iv_popupwindowwishdetail_photo);

        Log.i("wwwwwwwww","wishdetail():"+info.toString());
        //愿望标题
        tv_popupwindowwishdetail_wishtitle.setText(info.wishTitle);
        //愿望备注的显示
        if(info.wishDescription.isEmpty()){
            tv_popupwindowwishdetail_wishdescription.setVisibility(View.INVISIBLE);
        }else{
            tv_popupwindowwishdetail_wishdescription.setText(info.wishDescription);
        }
        //显示编辑框
        civ_popupwindowwishdetail_edit.setVisibility(View.VISIBLE);
        civ_popupwindowwishdetail_pen.setVisibility(View.VISIBLE);
        //进度条最大值为设置的愿望资金
        rpb_popupwindowwishdetail_progress.setMax(info.process);
        //愿望进度 textview和圆形进度条
        tv_popupwindowwishdetail_progress.setText(info.process+"%");
        Log.i("wwwwwwww","wishdetail setmax max = "+info.wishFund);
        rpb_popupwindowwishdetail_progress.setMax(100);
        rpb_popupwindowwishdetail_progress.setProgress(info.process);
        rpb_popupwindowwishdetail_progress.getProgress();
        //显示图片
        if(info.wishphotoUri.isEmpty() || info.wishphotoUri.equals("0") || info.wishphotoUri.equals("null")){
            iv_popupwindowwishdetail_photo.setVisibility(View.GONE);
        }else {
            iv_popupwindowwishdetail_photo.setVisibility(View.VISIBLE);
            iv_popupwindowwishdetail_photo.setImageURI(Uri.parse(info.wishphotoUri));
        }

        //获得焦点
        popupwindow_showwishdetail.setFocusable(true);
        popupwindow_showwishdetail.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showwishdetail.setContentView(view_wishdetail);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showwishdetail.setHeight(ScreenUtils.getScreenHeight(mActivity)- ScreenUtils.getStatusBarHeight(mActivity));
        popupwindow_showwishdetail.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showwishdetail.showAtLocation(view, Gravity.BOTTOM,0,0);

        //给关闭添加点击事件，关闭popupwindow
        civ_popupwindowwishdetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭愿望详情页
                popupwindow_showwishdetail.dismiss();
            }
        });

        //点击编辑，携带数据跳转到添加愿望按钮
        civ_popupwindowwishdetail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity,AddWishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from",FROMEDIT);
                bundle.putString("title", info.wishTitle);
                bundle.putString("description", info.wishDescription);
                bundle.putString("wishfund", info.wishFund);
                bundle.putString("photoUri", info.wishphotoUri);
                bundle.putInt("wishid",info.wishid);
                bundle.putInt("position",adapterPosition);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

    }
}
