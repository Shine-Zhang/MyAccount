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
    public MyGridViewAdapter myGridViewAdapter;
    public String selectCategoryName;
    public int selectResourceID;
    public CircleImageView previous;
    public CircleImageView firstCircle;
    public boolean isFirstOnclick;
    public int jumpItemEnable;
    public int currentClickItem;
    public boolean isTouchHindkeyBoard;//手势隐藏键盘的标志位，方便显示以前选中的item
    public boolean isClickShowKeyBoard;//键盘在消失的情况下，点击item显示键盘的标志位，方便显示以前选中的item
    public boolean isHaveAddCategoty;
    public boolean isHindBeforeChangePage;
    private boolean isShowDelteIcon;
    private PayoutCategoryDAO payoutCategoryDAO;
    private boolean isDeleteOrModify;//标志位，目的在修改或删除状态时，点击item键盘不弹出

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
        /*背景色的设置依然是setEnable，而出现第一个不消失和下滑时背景色消失的bug
        * 解决方向1.获取的item实例不对 造成view.setEnable本身的view不对
        * 方向2.键盘的消失与出现gridview的布局发生变化，会刷新item实例，以前获取的item实例则会失效
        * bug1：在gridview布局不发生变化 的情况下，点击item第一个item背景色还是enable的状态：已解决——获取第一item实例不正确
        * bug2：上滑或下滑布局发生变化时，item实例全部刷新变化了，适配器类重新执行到
        * */
        //清除gridview的点击时自带的背景颜色
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //设置gridview item点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"onItemClick");
                if (i == payoutCategoryToDB.size()) {
                    //跳转到addCategory页面
                    activity.startActivityForResult(new Intent(activity, AddCategoryActivity.class), 100);
                } else {
                    currentClickItem = i;
                    Log.i(TAG,"currentClickItem="+i);
                    //选中背景色变化
                    CircleImageView iv = (CircleImageView) view.findViewById(R.id.cv_addPage_recordIcon);
                    CircleImageView cv_addPage_delete = (CircleImageView) view.findViewById(R.id.cv_addPage_delete);
                    //为删除小图标设置点击事件，当小图标为visible时，点击可以调用
                    cv_addPage_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"delete is callback");
                            //点击删除小图标，删除此类别，并更新集合
                            int id = payoutCategoryToDB.get(currentClickItem).getId();
                            payoutCategoryDAO.deletePayoutCategoryItemFromDB(id);
                            //更新集合
                            payoutCategoryToDB.remove(currentClickItem);
                            myGridViewAdapter.notifyDataSetChanged();
                        }
                    });
                    if (previous != null) {

                            Log.i(TAG,"previous="+previous.toString());
                            Log.i(TAG,"current iv="+iv.toString());
                            previous.setEnabled(true);
                         } else {
                            Log.i(TAG, "first--");
                            isFirstOnclick = true;
                            firstCircle.setEnabled(true);
                            Log.i(TAG,"firstCircle="+firstCircle.toString());
                            Log.i(TAG,"current iv="+iv.toString());

                            //myGridViewAdapter.notifyDataSetChanged();
                    }
                    Log.i(TAG,"isTouchHindkeyBoard="+isTouchHindkeyBoard);
                    if (isTouchHindkeyBoard&&!isShowDelteIcon){
                        Log.i("ppppp","00");
                        //保存下滑消失键盘 用户选中的item
                        isClickShowKeyBoard = true;
                        isTouchHindkeyBoard = false;
                        addRecordActivity.keyboardUtil.showKeyboardAsNormal();
                        addRecordActivity.showUserInputNumber();
                    }else {
                        iv.setEnabled(false);
                        previous = iv;
                    }
                    if (isHindBeforeChangePage){
                        isClickShowKeyBoard = true;
                        //isTouchHindkeyBoard = false;
                        addRecordActivity.keyboardUtil.showKeyboardAsNormal();
                        addRecordActivity.showUserInputNumber();
                        isHindBeforeChangePage = false;
                    }
                    selectResourceID = payoutCategoryToDB.get(i).getResourceID();
                    selectCategoryName = payoutCategoryToDB.get(i).getCategoryName();
                }
                //previous=com.example.zs.view.CircleImageView{3fed4c3d V..D.... ........ 68,53-147,132 #7f0e012b app:id/cv_addPage_recordIcon}
                // current iv=com.example.zs.view.CircleImageView{4a70d83 V.ED.... ........ 68,53-147,132 #7f0e012b app:id/cv_addPage_recordIcon}
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
                    //addRecordActivity.saveuserInputNumberBeforeHindKeyBoard();
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
    //重写back键，当为修改弹出删除小图标时，点击back退出修改
    //需要在addRecordActivity中重写

    //退出删除修改状态
    public void backFromDeleteState(){
        //隐藏删除小图标
        isShowDelteIcon = false;
        //标志位表示退出删除或修改状态，再点击item可以弹出键盘
        isDeleteOrModify = false;
        myGridViewAdapter.notifyDataSetChanged();
    }

    //其他页面跳转过来修改的，点亮其对应的item
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
                      /* 因为在滑动的过程中会一直调用move，如果在这里判断用户是否滑动超过100，会出现对次调用的情况
                      造成isTouchHindkeyBoard又为true的bug
                       if (Math.abs(endY - startY) > 100) {
                            Log.i(TAG,"ACTION_MOVE_shoushi");
                            //动画隐藏掉键盘
                            // keyAnimationInVisble();
                            isTouchHindkeyBoard = true;
                            addRecordActivity.keyboardUtil.hideKeyboardAsNormal();
                            addRecordActivity.saveuserInputNumberBeforeHindKeyBoard();
                        }*/
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
        payoutCategoryDAO = new PayoutCategoryDAO(activity);
        payoutCategoryToDB = payoutCategoryDAO.getPayoutCategoryToDB();
        // Log.i(TAG, payoutCategoryToDB.toString());
        // Log.i(TAG, payoutCategoryToDB.get(0).toString());
    }

    public void getActivityResult(int id, String name) {
        Log.i(TAG, "getActivityResult");
        isHaveAddCategoty = true;
        UserAddCategoryInfo categoryInfo = new UserAddCategoryInfo(0,id, name);
        PayoutCategoryDAO payoutCategoryDAO = new PayoutCategoryDAO(activity);
        payoutCategoryDAO.addPayoutCategoryToDB(id, name);
        //gridview刷新数据
        payoutCategoryToDB.add(categoryInfo);
        //isJump = true;
        //firstCircle.setEnabled(true);
        //jumpItemEnable = payoutCategoryToDB.size();
        myGridViewAdapter.notifyDataSetChanged();
    }
    //切换page 初始化 刷新gridview 并默认为itme=0为选中状态
    public void changePage(){
        currentClickItem = 0;
        isFirstOnclick = false;
        previous = null;
        myGridViewAdapter.notifyDataSetChanged();
    }
    //目的，点击弹出备注区后关闭备注区时，将用户最近一次点击的item为选中状态
    public void closeRemarks(){
        isClickShowKeyBoard =true;
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
            CircleImageView cv_addPage_delete = (CircleImageView) inflate.findViewById(R.id.cv_addPage_delete);
            TextView tv_addPage_catagoryContent = (TextView) inflate.findViewById(R.id.tv_addPage_catagoryContent);
            Log.i(TAG, "getView: " + iv_addPage_catagoryIcon.toString());
            if (i < payoutCategoryToDB.size()) {
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
                    //第一个item设置false
                    if (!isFirstOnclick&&!isJump&&!isHaveAddCategoty) {
                        Log.i(TAG, "firstItem set false");
                        iv_addPage_catagoryIcon.setEnabled(false);
                    }
                }
                //记录第一个item之前的CircleImageView实例
                //为所需的item=0实现使用的实例
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
                //点击 键盘出现布局变化，再次点击item键盘出现，布局会发生变化，
                //gridview会重新刷新，获取的item对象会失效
                if(isClickShowKeyBoard){
                    //因为刷新在点击事件之后，所以点击事件里的item实例无法改变背景色，需要在适配器中更改
                    if (currentClickItem==i){
                        iv_addPage_catagoryIcon.setEnabled(false);
                        previous = iv_addPage_catagoryIcon;
                        isClickShowKeyBoard = false;
                        if (currentClickItem!=0){
                            //把第一个变为不选中状态
                            firstCircle.setEnabled(true);
                        }
                    }
                }
                //
                //手势，键盘消失，适配器重新刷新，显示以前用户选中的
                if (isTouchHindkeyBoard){
                    Log.i(TAG,"isTouchHindkeyBoard1"+i);
                    //选中的item在键盘消失 布局变化适配器重新刷新时，依然是选中的状态
                    //没有点击过为0，currentClickItem初始值也为0,所以不用单独的判断
                    if (previous!=null){
                        if (currentClickItem==i){
                            Log.i(TAG,"isTouchHindkeyBoard2"+isTouchHindkeyBoard);
                            iv_addPage_catagoryIcon.setEnabled(false);
                            previous = iv_addPage_catagoryIcon;
                            //isTouchHindkeyBoard = false;
                            Log.i(TAG,"isTouchHindkeyBoard2"+isTouchHindkeyBoard);
                        }
                    }
                }
                //从addCategory页面跳转过来，新添加为的默认为选中状态
                if (isHaveAddCategoty){
                    if (i==payoutCategoryToDB.size()-1){
                        iv_addPage_catagoryIcon.setEnabled(false);
                        previous = iv_addPage_catagoryIcon;
                        isHaveAddCategoty = false;
                    }
                }
                iv_addPage_catagoryIcon.setImageResource(payoutCategoryToDB.get(i).getResourceID());
                tv_addPage_catagoryContent.setText(payoutCategoryToDB.get(i).getCategoryName());
            } else
                //最后一个为默认item，作用为跳转到addCategory页面
                if (i == payoutCategoryToDB.size()) {
                    iv_addPage_catagoryIcon.setBackgroundColor(Color.WHITE);
                    iv_addPage_catagoryIcon.setImageResource(R.drawable.ic_jia_default);
                    tv_addPage_catagoryContent.setText("添加");
                    //如果为删除或修改状态，则隐藏最后一个item,不让跳转到addCategory页面
                    if (isDeleteOrModify){
                       inflate.setVisibility(View.GONE);
                    }
                }
            return inflate;
        }
    }

}

