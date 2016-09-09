package com.example.zs.addPage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.myaccount.AddCategoryActivity;
import com.example.zs.myaccount.AddRecordActivity;
import com.example.zs.myaccount.R;
import com.example.zs.view.CircleImageView;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by wuqi on 2016/9/4 0004.
 */
public class PayOutPage extends AddBasePage {
    private GridView gridView;
    private String TAG = "PayOutPage";
    private AddRecordActivity addRecordActivity;
    private ArrayList<UserAddCategoryInfo> payoutCategoryToDB;
    private MyGridViewAdapter myGridViewAdapter;
    public String selectCategoryName;
    public int selectResourceID;
    private CircleImageView previous;
    private CircleImageView firstCircle;
    private boolean isFirstOnclick;
    private int jumpItemEnable;

    public PayOutPage(Activity activity, boolean isJump) {
        super(activity, isJump);
    }
    @Override
    public View initView() {
        //初始化父类构造器时多态执行子类的执行initView（）时tag还为初始化
        TAG = "PayOutPage";
        Log.i(TAG, "initView");
        initData();
        addRecordActivity = (AddRecordActivity) activity;
        // GridView gridView = new GridView(activity);
        View inflate = View.inflate(activity, R.layout.gridview_layout, null);
        gridView = (GridView) inflate.findViewById(R.id.gv_addRecord_gridView);
        //得到屏幕的宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        //设置GridView控件参数
        //gridView.setColumnWidth(40);
        //gridView.setNumColumns(COLUMS_NUMBER);
        myGridViewAdapter = new MyGridViewAdapter();
        gridView.setAdapter(myGridViewAdapter);
        //girdview区滑动监听事件实现
        slideGridView();
        //设置gridviewItem监听事件
        //去掉默认的点击背景色
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //显示键盘区
                // keyAnimationVisble();
                if (i == payoutCategoryToDB.size()) {
                    //跳转到addCategory页面
                    activity.startActivityForResult(new Intent(activity, AddCategoryActivity.class), 100);
                } else {
                    //选中背景色变化
                    CircleImageView iv = (CircleImageView) view.findViewById(R.id.cv_addPage_recordIcon);


                        if (previous != null) {

                            Log.i(TAG,"previous="+previous.toString());
                            previous.setEnabled(true);
                         } else {
                            Log.i(TAG, "first--");
                            isFirstOnclick = true;
                            firstCircle.setEnabled(true);
                            Log.i(TAG,"firstCircle="+firstCircle.toString());
                            Log.i(TAG,"current iv="+iv.toString());

                            //myGridViewAdapter.notifyDataSetChanged();
                    }
                    iv.setEnabled(false);
                    previous = iv;
                    selectResourceID = payoutCategoryToDB.get(i).getResourceID();
                    selectCategoryName = payoutCategoryToDB.get(i).getCategoryName();
                }
                Log.i(TAG, "--" + i);
            }
        });
        return gridView;
    }
    public void setItemEnable(int resID,String name){
        isJump = true;
        //通过categoryName，在数据库中查找对应的resourceID
        for (int i=0;i<payoutCategoryToDB.size();i++){
            if (payoutCategoryToDB.get(i).getResourceID()==resID){
                if (payoutCategoryToDB.get(i).getCategoryName().equals(name)){
                    jumpItemEnable = i;
                    return;
                }
            }
        }
    }

    float startY;

    private void slideGridView() {

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float rawY = motionEvent.getRawY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endY = motionEvent.getRawY();
                        Log.i(TAG, "ACTION_MOVE" + Math.abs(endY - startY));
                        if (Math.abs(endY - startY) > 100) {
                            //动画隐藏掉键盘
                           keyAnimationInVisble();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startY = motionEvent.getRawY();
                        break;
                }
                //优先控件内部点击事件处理，如不处理，touch消耗掉事件
                return false;
            }
        });
    }

    private void keyAnimationInVisble() {
        float x = addRecordActivity.ll_addRecordActivity_downRegion.getX();
        float y = addRecordActivity.ll_addRecordActivity_downRegion.getY();
        int height = addRecordActivity.ll_addRecordActivity_keyboard.getMeasuredHeight();
        Log.i(TAG, x + "-" + y + "-" + height + "-");
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, height);
        // TranslateAnimation translateAnimation = new TranslateAnimation(0, y, , height);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        addRecordActivity.ll_addRecordActivity_downRegion.startAnimation(translateAnimation);
    }

    private void keyAnimationVisble() {
        float x = addRecordActivity.ll_addRecordActivity_downRegion.getX();
        float y = addRecordActivity.ll_addRecordActivity_downRegion.getMeasuredHeight();
        int height = addRecordActivity.ll_addRecordActivity_keyboard.getMeasuredHeight();
        Log.i(TAG, x + "-" + y + "-" + height + "-");
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, y, y - height);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        addRecordActivity.ll_addRecordActivity_downRegion.startAnimation(translateAnimation);
    }

    /**
     * 主要为进入page时查询数据，填充gridview
     */
    private void initData() {
        //默认为一般种类
        selectResourceID = R.drawable.ic_yiban_default;
        selectCategoryName = "一般";
        Log.i(TAG, "initData");
        PayoutCategoryDAO payoutCategoryDAO = new PayoutCategoryDAO(activity);
        payoutCategoryToDB = payoutCategoryDAO.getPayoutCategoryToDB();
        // Log.i(TAG, payoutCategoryToDB.toString());
        // Log.i(TAG, payoutCategoryToDB.get(0).toString());
    }

    public void getActivityResult(int id, String name) {
        Log.i(TAG, "getActivityResult");
        UserAddCategoryInfo categoryInfo = new UserAddCategoryInfo(id, name);
        PayoutCategoryDAO payoutCategoryDAO = new PayoutCategoryDAO(activity);
        payoutCategoryDAO.addPayoutCategoryToDB(id, name);
        //gridview刷新数据
        payoutCategoryToDB.add(categoryInfo);
        myGridViewAdapter.notifyDataSetChanged();
    }


    class MyGridViewAdapter extends BaseAdapter {
        CircleImageView cv = null;
        @Override
        public int getCount() {
            return payoutCategoryToDB.size() + 1;
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
            Log.i(TAG, "getView: " + i);
            View inflate = View.inflate(activity, R.layout.page_addrecord_detail, null);
            CircleImageView iv_addPage_catagoryIcon = (CircleImageView) inflate.findViewById(R.id.cv_addPage_recordIcon);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);

            if (i < payoutCategoryToDB.size()) {
                if (i==0){
                    //第一个item设置false
                    if (!isFirstOnclick) {
                        Log.i(TAG, "---");
                        iv_addPage_catagoryIcon.setEnabled(false);
                    }
                }
                //记录第一个item之前的CircleImageView实例
                if (i == 1&&!isJump) {
                    firstCircle = cv;
                    Log.i(TAG,firstCircle.toString());
                    //刷新就会变化，所有不适合在适配器中设置enable
                    // iv_addPage_catagoryIcon.setEnabled(false);
                }else if (!isJump){
                    cv = iv_addPage_catagoryIcon;
                }
                //从明细，报表跳转过来点亮对应的item
                if (isJump&&jumpItemEnable ==i){
                    iv_addPage_catagoryIcon.setEnabled(false);
                     previous = iv_addPage_catagoryIcon;
                }
                iv_addPage_catagoryIcon.setImageResource(payoutCategoryToDB.get(i).getResourceID());
                tv_addPage_catagoryContent.setText(payoutCategoryToDB.get(i).getCategoryName());
            } else
                //最后一个为默认item，作用为跳转到addCategory页面
                if (i == payoutCategoryToDB.size()) {
                    iv_addPage_catagoryIcon.setBackgroundColor(Color.WHITE);
                    iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                    tv_addPage_catagoryContent.setText("添加");
                }
            return inflate;
        }
    }

}

