package com.example.zs.addPage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import com.example.zs.dao.IncomeCategoryDAO;
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
public class IncomePage extends AddBasePage {
    private GridView gridView;
    private String TAG = "IncomePage";
    private AddRecordActivity addRecordActivity;
    private ArrayList<UserAddCategoryInfo> incomeCategoryToDB;
    private MyGridViewAdapter myGridViewAdapter;
    public String selectCategoryName;
    public int selectResourceID;
    public CircleImageView previous;
    public CircleImageView firstCircle;
    public boolean isFirstOnclick;
    public int jumpItemEnable;
    public int currentClickItem;
    public boolean isTouchHindkeyBoard;
    public boolean isClickShowKeyBoard;
    public boolean isHaveAddCategoty;
    public boolean isHindBeforeChangePage;
    private boolean isShowDelteIcon;
    private IncomeCategoryDAO incomeCategoryDAO;
    private boolean isDeleteOrModify;//标志位，目的在修改或删除状态时，点击item键盘不弹出

    public IncomePage(Activity activity, boolean isJump) {
        super(activity, isJump);
    }
    @Override
    public View initView() {
        //初始化父类构造器时多态执行子类的执行initView（）时tag还为初始化
        TAG = "IncomePage";
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
                if (i == incomeCategoryToDB.size()) {
                    //跳转到addCategory页面
                    activity.startActivityForResult(new Intent(activity, AddCategoryActivity.class), 90);
                } else {
                    currentClickItem = i;
                    //选中背景色变化
                    CircleImageView iv = (CircleImageView) view.findViewById(R.id.cv_addPage_recordIcon);
                    CircleImageView cv_addPage_delete = (CircleImageView) view.findViewById(R.id.cv_addPage_delete);
                    //为删除小图标设置点击事件，当小图标为visible时，点击可以调用
                    cv_addPage_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"delete is callback");
                            //点击删除小图标，删除此类别，并更新集合
                            int id = incomeCategoryToDB.get(currentClickItem).getId();
                            incomeCategoryDAO.deletePayoutCategoryItemFromDB(id);
                            //更新集合
                            incomeCategoryToDB.remove(currentClickItem);
                            myGridViewAdapter.notifyDataSetChanged();
                        }
                    });
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
                    if (isTouchHindkeyBoard&&!isShowDelteIcon){
                        //保存下滑消失键盘 用户选中的item
                        isClickShowKeyBoard = true;
                        //isTouchHindkeyBoard = false;
                        addRecordActivity.keyboardUtil.showKeyboardAsNormal();
                        addRecordActivity.showUserInputNumber();
                    }else {
                        iv.setEnabled(false);
                        previous = iv;
                    }
                    if (isHindBeforeChangePage){
                        isClickShowKeyBoard = true;
                        addRecordActivity.keyboardUtil.showKeyboardAsNormal();
                        addRecordActivity.showUserInputNumber();
                        isHindBeforeChangePage = false;
                    }
                    selectResourceID = incomeCategoryToDB.get(i).getResourceID();
                    selectCategoryName = incomeCategoryToDB.get(i).getCategoryName();
                }
            }
        });
        //设置长按事件，检测到则销售显示小图标
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //标志位删除修改状态，供addRecordActivity重写back键
                addRecordActivity.isDeleteState = true;
                //标志位，表示page在删除或修改状态，点击item时键盘不弹出
                isDeleteOrModify = true;
                //显示删除小按钮
                Log.i(TAG,"setOnItemLongClickListener");
                isShowDelteIcon = true;
                if (!isTouchHindkeyBoard){
                    //标志位，表示键盘已经被隐藏
                    isTouchHindkeyBoard = true;
                    addRecordActivity.keyboardUtil.hideKeyboardAsNormal();
                }else {
                    //刷新适配器
                    myGridViewAdapter.notifyDataSetChanged();
                }
                // addRecordActivity.keyboardUtil.hideKeyboardAsNormal();
                //myGridViewAdapter.notifyDataSetChanged();键盘消失布局变化，适配器自动刷新
                //返回true则 检测到长按事件只到长按，false表长按执行到 再执行点击
                return true;
            }
        });
        return gridView;
    }
    //退出删除修改状态
    public void backFromDeleteState(){
        //隐藏删除小图标
        isShowDelteIcon = false;
        //标志位表示退出删除或修改状态，再点击item可以弹出键盘
        isDeleteOrModify = false;
        myGridViewAdapter.notifyDataSetChanged();
    }

    public void setItemEnable(int resID,String name){
        isJump = true;
        //通过categoryName，在数据库中查找对应的resourceID
        for (int i=0;i<incomeCategoryToDB.size();i++){
            if (incomeCategoryToDB.get(i).getResourceID()==resID){
                if (incomeCategoryToDB.get(i).getCategoryName().equals(name)){
                    jumpItemEnable = i;
                    return;
                }
            }
        }
    }

    float startY;
    float endY;
    private void slideGridView() {

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(TAG,"onTouch");
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG,"ACTION_DOWN");
                        startY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG,"ACTION_MOVE");
                        endY = motionEvent.getRawY();
                        Log.i(TAG, "ACTION_MOVE" + Math.abs(endY - startY));
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG,"ACTION_UP");
                        Log.i(TAG, "ACTION_UP" + Math.abs(endY - startY));
                        //如果是点击事件，有可能只有down和up，endY的值就为0；
                        if (endY!=0){
                            if (Math.abs(endY - startY) > 100) {
                                Log.i(TAG,"ACTION_UP_shoushi");
                                //动画隐藏掉键盘
                                // keyAnimationInVisble();
                                isTouchHindkeyBoard = true;
                                addRecordActivity.keyboardUtil.hideKeyboardAsNormal();
                                addRecordActivity.saveuserInputNumberBeforeHindKeyBoard();
                            }
                        }
                        //初始化endY
                        endY = 0;
                        break;
                }
                //设置了点击事件情况下，true，点击事件不会执行到
                //没有设置点击事件情况下，false则下次不会再接收事件分发
                return false;
            }
        });
    }

    /**
     * 主要为进入page时查询数据，填充gridview
     */
    private void initData() {
        //默认为一般种类
        selectResourceID = R.drawable.ic_yiban_default;
        selectCategoryName = "一般";
        Log.i(TAG, "initData");
        incomeCategoryDAO = new IncomeCategoryDAO(activity);
        incomeCategoryToDB = incomeCategoryDAO.getIncomeCategoryToDB();
        // Log.i(TAG, payoutCategoryToDB.toString());
        // Log.i(TAG, payoutCategoryToDB.get(0).toString());
    }

    public void getActivityResult(int id, String name) {
        Log.i(TAG, "getActivityResult");
        isHaveAddCategoty = true;
        UserAddCategoryInfo categoryInfo = new UserAddCategoryInfo(0,id, name);
        IncomeCategoryDAO incomeCategoryDAO = new IncomeCategoryDAO(activity);
        incomeCategoryDAO.addIncomeCategoryToDB(id, name);
        //gridview刷新数据
        incomeCategoryToDB.add(categoryInfo);
        //isJump = true;
        //firstCircle.setEnabled(true);
        //jumpItemEnable = payoutCategoryToDB.size();
        myGridViewAdapter.notifyDataSetChanged();
    }
    //切换page 初始化 刷新gridview 并默认为itme=0为选中状态
    public void changePage(){
        currentClickItem = 0;
        isFirstOnclick = false;
        previous =null;
        myGridViewAdapter.notifyDataSetChanged();
    }

    class MyGridViewAdapter extends BaseAdapter {
        CircleImageView cv = null;
        @Override
        public int getCount() {
            return incomeCategoryToDB.size() + 1;
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
            CircleImageView cv_addPage_delete = (CircleImageView) inflate.findViewById(R.id.cv_addPage_delete);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);

            if (i < incomeCategoryToDB.size()) {
                //长按显示小图标
                if (isShowDelteIcon){
                    Log.i(TAG,"isShowDelteIcon");
                    cv_addPage_delete.setVisibility(View.VISIBLE);
                    //item 动画效果,因为要实现用户点击，所以使用属性动画
                    //动画效果为，已圆心为旋转中心，从左4°旋转8°
                    ObjectAnimator rotation = ObjectAnimator.ofFloat(inflate, "rotation", -4f, 4f);
                    //一次动画的执行时间，100毫秒
                    rotation.setDuration(100);
                    //动画重复的次数
                    rotation.setRepeatCount(10000);
                    //摆动的原理为，一次方向执行完，下次重复是自动从反方向执行的
                    rotation.setRepeatMode(ValueAnimator.REVERSE);
                    //动画开始
                    rotation.start();
                }
                if (i==0){
                    //第一个item设置false,在不是从其它页面跳转过来修改和从addCategory页面跳转过来
                    //其它页面跳转过来修改,需指定item为选中状态
                    //从addCategory页面跳转过来，默认倒数第二个位选中状态
                    if (!isFirstOnclick&&!isJump&&!isHaveAddCategoty) {
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
                //下滑 键盘消失布局，再次点击item键盘出现，布局会发生变化，
                //gridview会重新刷新，获取的item对象会失效
                if(isClickShowKeyBoard){
                    //因为刷新在点击事件之后，所以点击事件里的item实例无法改变背景色，需要在适配器中更改
                    if (currentClickItem==i){
                        Log.i(TAG,"currentClickItem="+i);
                        iv_addPage_catagoryIcon.setEnabled(false);
                        previous = iv_addPage_catagoryIcon;
                        isClickShowKeyBoard = false;
                        if (currentClickItem!=0){
                            //把第一个变为不选中状态
                            firstCircle.setEnabled(true);
                        }
                    }

                }
                //点击item
                if (isTouchHindkeyBoard){
                    Log.i(TAG,"isTouchHindkeyBoard1"+i);
                    //选中的item在键盘消失 布局变化适配器重新刷新时，依然是选中的状态
                    //没有点击过为0，currentClickItem初始值也为0,所以不用单独的判断
                    if (previous!=null){
                        if (currentClickItem==i){
                            Log.i(TAG,"isTouchHindkeyBoard2"+i);
                            iv_addPage_catagoryIcon.setEnabled(false);
                            previous = iv_addPage_catagoryIcon;
                        }
                    }
                }
                //从addCategory页面跳转过来默认新添加为选中状态
                if (isHaveAddCategoty){
                    if (i==incomeCategoryToDB.size()-1){
                        iv_addPage_catagoryIcon.setEnabled(false);
                        previous = iv_addPage_catagoryIcon;
                        isHaveAddCategoty = false;
                    }
                }
               /* if (isChangePage){
                    firstCircle.setEnabled(false);
                    previous = null;
                    isChangePage = false;
                }*/
                iv_addPage_catagoryIcon.setImageResource(incomeCategoryToDB.get(i).getResourceID());
                tv_addPage_catagoryContent.setText(incomeCategoryToDB.get(i).getCategoryName());
            } else
                //最后一个为默认item，作用为跳转到addCategory页面
                if (i == incomeCategoryToDB.size()) {
                    iv_addPage_catagoryIcon.setBackgroundColor(Color.WHITE);
                    iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                    tv_addPage_catagoryContent.setText("添加");
                    if (isDeleteOrModify){
                        inflate.setVisibility(View.GONE);
                    }
                }
            return inflate;
        }
    }

}

