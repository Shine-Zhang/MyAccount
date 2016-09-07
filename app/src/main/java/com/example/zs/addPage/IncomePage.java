package com.example.zs.addPage;

import android.app.Activity;
import android.content.Intent;
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
import com.example.zs.dao.IncomeCategoryDAO;
import com.example.zs.dao.PayoutCategoryDAO;
import com.example.zs.myaccount.AddCategoryActivity;
import com.example.zs.myaccount.AddRecordActivity;
import com.example.zs.myaccount.R;
import com.example.zs.view.CircleImageView;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by wuqi on 2016/9/4 0004.
 */
public class IncomePage extends AddBasePage {
    private GridView gridView;
    private String TAG="IncomePage";
    private AddRecordActivity addRecordActivity;
    private ArrayList<UserAddCategoryInfo> incomeCategoryToDB;
    private MyGridViewAdapter myGridViewAdapter;
    public  String selectCategoryName;
    public  int selectResourceID;

    public IncomePage(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        //初始化父类构造器时多态执行子类的执行initView（）时tag还为初始化
        TAG="IncomePage";
        Log.i(TAG,"initView");
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //显示键盘区
               // keyAnimationVisble();
                if(i==incomeCategoryToDB.size()){
                    //跳转到addCategory页面
                    activity.startActivityForResult(new Intent(activity, AddCategoryActivity.class),50);
                }else {
                    selectResourceID = incomeCategoryToDB.get(i).getResourceID();
                    selectCategoryName = incomeCategoryToDB.get(i).getCategoryName();
                }
                Log.i(TAG,"--"+i);
            }
        });
        return gridView;
    }
    float startY;
    private void slideGridView() {

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float rawY = motionEvent.getRawY();
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float endY = motionEvent.getRawY();
                        Log.i(TAG,"ACTION_MOVE"+Math.abs(endY-startY));
                        if (Math.abs(endY-startY)>100){
                            //动画隐藏掉键盘
                           // keyAnimationInVisble();
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
        Log.i(TAG,x+"-"+y+"-"+height+"-");
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,height);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        addRecordActivity.ll_addRecordActivity_downRegion.startAnimation(translateAnimation);
    }
    private void keyAnimationVisble() {
        float x = addRecordActivity.ll_addRecordActivity_downRegion.getX();
        float y = addRecordActivity.ll_addRecordActivity_downRegion.getMeasuredHeight();
        int height = addRecordActivity.ll_addRecordActivity_keyboard.getMeasuredHeight();
        Log.i(TAG,x+"-"+y+"-"+height+"-");
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,y,y-height);
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
        Log.i(TAG,"initData");
        IncomeCategoryDAO incomeCategoryDAO = new IncomeCategoryDAO(activity);
        incomeCategoryToDB = incomeCategoryDAO.getIncomeCategoryToDB();
        // Log.i(TAG, incomeCategoryToDB.toString());
        // Log.i(TAG, incomeCategoryToDB.get(0).toString());
    }

    public void getActivityResult(int id,String name) {
        Log.i(TAG,"getActivityResult");
        UserAddCategoryInfo categoryInfo = new UserAddCategoryInfo(id, name);
        IncomeCategoryDAO incomeCategoryDAO = new IncomeCategoryDAO(activity);
        incomeCategoryDAO.addIncomeCategoryToDB(id,name);
        //gridview刷新数据
        incomeCategoryToDB.add(categoryInfo);
        myGridViewAdapter.notifyDataSetChanged();
    }


    class MyGridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return incomeCategoryToDB.size()+1;
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
            View inflate = View.inflate(activity, R.layout.page_addrecord_detail, null);
            CircleImageView iv_addPage_catagoryIcon = (CircleImageView) inflate.findViewById(R.id.iv_addPage_catagoryIcon);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);
            if(i<incomeCategoryToDB.size()){
               /* iv_addPage_catagoryIcon.setImageResource(icons[i]);
                tv_addPage_catagoryContent.setText(contents[i]+"");*/
                //Log.i(TAG,incomeCategoryToDB.get(i).getResourceID()+"getResourceID");
                iv_addPage_catagoryIcon.setImageResource(incomeCategoryToDB.get(i).getResourceID());
                tv_addPage_catagoryContent.setText(incomeCategoryToDB.get(i).getCategoryName());

            }else
                //最后一个为默认item，作用为跳转到addCategory页面
                if(i==incomeCategoryToDB.size()){
                    iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                    tv_addPage_catagoryContent.setText("添加");
                }
            return inflate;
        }
    }

}

