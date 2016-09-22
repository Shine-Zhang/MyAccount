package com.example.zs.myaccount;


import android.database.ContentObserver;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.WishInfo;
import com.example.zs.dao.CompleteWishDAO;
import com.example.zs.utils.ScreenUtils;
import com.example.zs.view.CircleImageView;
import com.example.zs.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

public class CompleteWishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CompleteWishActivity";
    private RecyclerView rcv_completewishactivity_wishes;
    private RelativeLayout rl_completewishactivity_norecord;
    private PopupWindow popupwindow_showwishdetail;
    private CompleteWishDAO completeWishDAO;
    private int allCompleteWishNumber;
    private List<WishInfo> allCompleteWishInfo;
    private MyCompleteWishRecyclerViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_wish);
        getSupportActionBar().hide();

        ImageButton imgbt_completewishactivity_back = (ImageButton) findViewById(R.id.imgbt_completewishactivity_back);
        imgbt_completewishactivity_back.setOnClickListener(this);

        rl_completewishactivity_norecord = (RelativeLayout) findViewById(R.id.rl_completewishactivity_norecord);
        rcv_completewishactivity_wishes = (RecyclerView) findViewById(R.id.rcv_completewishactivity_wishes);

        completeWishDAO = new CompleteWishDAO(this);
        allCompleteWishNumber = completeWishDAO.getAllCompleteWishNumber();
        Log.i("wwwwwww","allCompleteWishNumber="+allCompleteWishNumber );

        initView();

        initData();

    }

    private void initView() {

        if (allCompleteWishNumber!=0) {
            //有已完成愿望
            rl_completewishactivity_norecord.setVisibility(View.INVISIBLE);
            rcv_completewishactivity_wishes.setVisibility(View.VISIBLE);

            //创建默认的线性LayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rcv_completewishactivity_wishes.setLayoutManager(layoutManager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            rcv_completewishactivity_wishes.setHasFixedSize(true);

        } else {
            //没有已完成愿望
            rl_completewishactivity_norecord.setVisibility(View.VISIBLE);
            rcv_completewishactivity_wishes.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {

        //当数据库变化的时候重新更新内存中的数据,当数据库变化的时候通知内容观察者数据库变化了,然后在内容观察者中去更新最新的数据
        Uri uri = Uri.parse("content://com.example.zs.dao.completewish.insertchange");
        //notifyForDescendents:匹配规则,true:精确匹配  false:模糊匹配
        getContentResolver().registerContentObserver(uri, true, new ContentObserver(null) {
            public void onChange(boolean selfChange) {
                Log.i("wwwwwww","registerContentObserver======"+selfChange );

                //更新数据
                allCompleteWishNumber = completeWishDAO.getAllCompleteWishNumber();
                if(allCompleteWishNumber!=0) {
                    allCompleteWishInfo = completeWishDAO.getAllCompleteWishInfo();
                }
            };
        });

        Log.i("wwwwwww","allCompleteWishNumber="+allCompleteWishNumber );

        if(allCompleteWishNumber!=0) {
            allCompleteWishInfo = completeWishDAO.getAllCompleteWishInfo();
            //初始化自定义的适配器
            myAdapter = new MyCompleteWishRecyclerViewAdapter(allCompleteWishInfo);
            //为rcv_wishpager_wishes设置适配器
            rcv_completewishactivity_wishes.setAdapter(myAdapter);
            //给拿到RecyclerView添加条目点击事件
            rcv_completewishactivity_wishes.addOnItemTouchListener(new OnItemTouchListener(rcv_completewishactivity_wishes) {
                @Override
                public void onItemClick(RecyclerView.ViewHolder vh) {
                    Log.i(TAG, "click" + vh.itemView + " " + vh.getItemViewType());
                    View viewParent = (View) vh.itemView.getParent();
                    int adapterPosition = vh.getAdapterPosition();
                    String title = allCompleteWishInfo.get(adapterPosition).wishTitle;
                    String description = allCompleteWishInfo.get(adapterPosition).wishDescription;
                    String photouri = allCompleteWishInfo.get(adapterPosition).wishphotoUri;
                    Log.i(TAG, "title=" + title + "description=" + description);

                    WishInfo wishInfo = new WishInfo(title, description, photouri);
                    showWishDetail(wishInfo, viewParent);

                }
            });
        }
    }

    private void showWishDetail(WishInfo wishInfo,View view) {
        //初始化popupwindow
        popupwindow_showwishdetail = new PopupWindow();
        //加载popupwindow的界面
        View view_wishdetail = View.inflate(CompleteWishActivity.this, R.layout.popupwindow_wishdetail, null);
        CircleImageView civ_popupwindowwishdetail_close = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_close);
        TextView tv_popupwindowwishdetail_wishtitle = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_wishtitle);
        TextView tv_popupwindowwishdetail_progress = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_progress);
        TextView tv_popupwindowwishdetail_wishdescription = (TextView) view_wishdetail.findViewById(R.id.tv_popupwindowwishdetail_wishdescription);
        RoundProgressBar rpb_popupwindowwishdetail_progress = (RoundProgressBar) view_wishdetail.findViewById(R.id.rpb_popupwindowwishdetail_progress);
        CircleImageView civ_popupwindowwishdetail_edit = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_edit);
        CircleImageView civ_popupwindowwishdetail_pen = (CircleImageView) view_wishdetail.findViewById(R.id.civ_popupwindowwishdetail_pen);
        ImageView iv_popupwindowwishdetail_photo = (ImageView) view_wishdetail.findViewById(R.id.iv_popupwindowwishdetail_photo);

        //愿望标题
        tv_popupwindowwishdetail_wishtitle.setText(wishInfo.wishTitle);
        //愿望备注的显示
        if(wishInfo.wishDescription.isEmpty()){
            tv_popupwindowwishdetail_wishdescription.setVisibility(View.GONE);
        }else{
            tv_popupwindowwishdetail_wishdescription.setVisibility(View.VISIBLE);
            tv_popupwindowwishdetail_wishdescription.setText(wishInfo.wishDescription);
        }
        //隐藏编辑框，因为是已完成愿望，不可编辑
        civ_popupwindowwishdetail_edit.setVisibility(View.GONE);
        civ_popupwindowwishdetail_pen.setVisibility(View.GONE);

        //愿望进度 text默认100%,不再修改，修改圆形进度条
        tv_popupwindowwishdetail_progress.setText("100%");
        rpb_popupwindowwishdetail_progress.setMax(100);
        rpb_popupwindowwishdetail_progress.setProgress(100);
        rpb_popupwindowwishdetail_progress.getProgress();

        //显示图片  如果有图，显示图片，没有则隐藏控件
        if(wishInfo.wishphotoUri.isEmpty() || wishInfo.wishphotoUri.equals("null") || wishInfo.wishphotoUri.equals("0") ){
            //没有图片，隐藏ImageView控件
            iv_popupwindowwishdetail_photo.setVisibility(View.GONE);
        }else {
            //显示图片
            iv_popupwindowwishdetail_photo.setVisibility(View.VISIBLE);
            iv_popupwindowwishdetail_photo.setImageURI(Uri.parse(wishInfo.wishphotoUri));
        }

        //获得焦点
        popupwindow_showwishdetail.setFocusable(true);
        popupwindow_showwishdetail.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showwishdetail.setContentView(view_wishdetail);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showwishdetail.setHeight(ScreenUtils.getScreenHeight(this)- ScreenUtils.getStatusBarHeight(this));
        popupwindow_showwishdetail.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_showwishdetail.showAtLocation(view, Gravity.BOTTOM,0,0);

        //给关闭添加点击事件，
        civ_popupwindowwishdetail_close.setOnClickListener(CompleteWishActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbt_completewishactivity_back:
                //关掉已完成愿望列表页面
                finish();
                break;
            case R.id.civ_popupwindowwishdetail_close:
                //关掉愿望详情页面
                popupwindow_showwishdetail.dismiss();
                break;
        }

    }

}

/**
 * 该类为RecyclerView的Adapter
 * RecyclerView与ListView类似，都需要Adapter
 */
class MyCompleteWishRecyclerViewAdapter extends RecyclerView.Adapter<MyCompleteWishRecyclerViewAdapter.MyViewHolder> {
    /**
     * 这里创建一个数组，准备接收传过来的数据
     */
    public List<WishInfo> wishInfos = null;

    //构造方法

    /**
     * 这里调用在创建MyAdapter实例的时候，可以将数据传过来
     *
     * @param wishInfos
     */
    public MyCompleteWishRecyclerViewAdapter(List<WishInfo> wishInfos) {
        this.wishInfos = wishInfos;
    }

    //创建新View，被LayoutManager所调用

    /**
     * 这里加载加载Item，并且创建ViewHolder对象，把加载的Item（View）传给viewholder
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_completewish, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    //将数据与界面进行绑定的操作

    /**
     * 这里给item中的子View绑定数据
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        // 给ViewHolder设置元素
        WishInfo wishInfo = wishInfos.get(position);
        viewHolder.tv_itemcompletewish_title.setText(wishInfo.wishTitle);
        viewHolder.tv_itemcompletewish_description.setText(wishInfo.wishDescription);
    }

    //获取数据的数量

    /**
     * 这里返回item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return wishInfos.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素

    /**
     * ViewHolder类，注意要继承RecyclerView.ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_itemcompletewish_title;
        public TextView tv_itemcompletewish_description;

        public MyViewHolder(View view) {
            super(view);
            tv_itemcompletewish_title = (TextView) view.findViewById(R.id.tv_itemcompletewish_title);
            tv_itemcompletewish_description = (TextView) view.findViewById(R.id.tv_itemcompletewish_description);
        }
    }

}

/**
 * RecyclerView的触摸监听
 * 重写了RecyclerView.OnItemTouchListener的四个方法
 * 加了一个抽象方法，可以自行处理当前点击该Item
 */
abstract class OnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public OnItemTouchListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),
                new MyGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    /**
     * 手势判断
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childe = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childe != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(childe);
                onItemClick(VH);
            }
        }
    }
}




