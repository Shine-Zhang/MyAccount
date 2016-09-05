package com.example.zs.pager;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zs.myaccount.AddWishActivity;
import com.example.zs.myaccount.CompleteWishActivity;
import com.example.zs.myaccount.MainActivity;
import com.example.zs.myaccount.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 韦宇 on 2016/9/2.
 * 该类实现愿望页面的业务逻辑。
 * 主要展示用户的愿望，使用的布局文件是 wishpager.xml
 * @author 韦宇
 */

public class WishPager extends BasePager {

    private int wishcount;
    private View view_wishpager;


    /*测试数据，用于测试*/
    String[] titles = new String[]{"我的愿望1","我的愿望2","我的愿望3","我的愿望4","我的愿望5","我的愿望6"};
    String[] descriptions = new String[]{"备注1","备注2","备注3","备注4","备注5","备注6"};
    float[] wishfunds = new float[]{100,233,12,12,244,80};
    int[] photoids = new int[]{R.drawable.ic_arrow_default,R.drawable.ic_completed_wish,R.drawable.ic_default_wish,
            R.drawable.ic_detail_blue,R.drawable.ic_pen2_default,R.drawable.ic_yue_default};
    private List<TestData> datas;

    class TestData{
        String title;
        String description;
        float wishfund;
        int photoid;
        public TestData() {
        }
        public TestData(String title, String description, float wishfund, int photoid) {
            this.title = title;
            this.description = description;
            this.wishfund = wishfund;
            this.photoid = photoid;
        }
    }


    /**
     * 该方法用于初始化WishPager的页面
     * @return 返回值为页面要显示的view
     */
    public WishPager(Activity activity){
        super(activity);
        initData();
    }
    @Override
    public View initView() {

        //去数据库中拿数据，判断数据库中的用户愿望数目
        //测试数据
        wishcount =3;
        //用户愿望条目为0 ，显示愿望空白页
        if(wishcount ==0){
            //加载布局文件wishpager_blankwish.xml
            view_wishpager = View.inflate(mActivity, R.layout.wishpager_blankwish,null);
            //添加愿望
            TextView tv_blankwish_addwish = (TextView) view_wishpager.findViewById(R.id.tv_blankwish_addwish);
            tv_blankwish_addwish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toAddWish();
                }
            });
        }else {
            //加载布局文件wishpager_showwish.xml
            view_wishpager = View.inflate(mActivity, R.layout.wishpager_showwish, null);
            LinearLayout ll_showwish_ongoingwishes = (LinearLayout) view_wishpager.findViewById(R.id.ll_showwish_ongoingwishes);
            LinearLayout ll_showwish_noongoingwishes = (LinearLayout) view_wishpager.findViewById(R.id.ll_showwish_noongoingwishes);

            //点击“添加”，跳转到添加愿望的页面
            View tv_showwish_addwish = view_wishpager.findViewById(R.id.tv_showwish_addwish);
            tv_showwish_addwish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toAddWish();
                }
            });


            //判断是否有正在进行的愿望，有，显示未完成愿望列表；没有，显示当前没有未完成愿望。
            if(hasOnGoingWishes()){
                //
                ll_showwish_ongoingwishes.setVisibility(View.VISIBLE);
                ll_showwish_noongoingwishes.setVisibility(View.INVISIBLE);
            }else{
                //没有正在进行的愿望
                ll_showwish_ongoingwishes.setVisibility(View.INVISIBLE);
                ll_showwish_noongoingwishes.setVisibility(View.VISIBLE);
                TextView tv_showwish_noongoingwish_completewishes = (TextView) view_wishpager.findViewById(R.id.tv_showwish_noongoingwish_completewishes);
                //点击“已完成的愿望”
                tv_showwish_noongoingwish_completewishes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //显示已完成愿望页面
                        mActivity.startActivity(new Intent(mActivity,CompleteWishActivity.class));
                    }
                });

                //拿到RecyclerView
                RecyclerView rcv_wishpager_wishes = (RecyclerView) view_wishpager.findViewById(R.id.rcv_wishpager_wishes);
                //创建默认的线性LayoutManager
                LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
                rcv_wishpager_wishes.setLayoutManager(layoutManager);
                //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                rcv_wishpager_wishes.setHasFixedSize(true);
                //初始化自定义的适配器
                MyOnGoingRecyclerViewAdapter myAdapter = new MyOnGoingRecyclerViewAdapter();
                //为rcv_wishpager_wishes设置适配器
                rcv_wishpager_wishes.setAdapter(myAdapter);
            }

        }
        return view_wishpager;
    }

    @Override
    public void initData() {

        datas = new ArrayList<TestData>();
        for(int i = 0;i<5;i++){
            datas.add(new TestData(titles[i],descriptions[i],wishfunds[i],photoids[i]));
        }
    }

    /**
     *
     * @return true表示有正在进行的愿望，false表示没有正在进行的愿望
     */
    private boolean hasOnGoingWishes() {
        return false;
    }

    /**
     *该方法用户跳转到用户添加愿望的页面AddWishAvtivity
     */
    private void toAddWish() {
        mActivity.startActivity(new Intent(mActivity, AddWishActivity.class));
    }

    /**
     * 该方法用于计算愿望资金
     * 愿望资金的计算公式为：（月收入-月支出）/本月天数  *本月已过天数
     * @return 返回当前日期的愿望资金
     */
    private float calculateWishFund() {
        //获取当前的月收入、月支出

        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //获取当前日期:
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = dateFormater.format(calendar.getTime());

        //计算愿望资金

        return 0;
    }

    /**
     * 该类为RecyclerView的Adapter
     * RecyclerView与ListView类似，都需要Adapter
     */
    class MyOnGoingRecyclerViewAdapter extends RecyclerView.Adapter<MyOnGoingRecyclerViewAdapter.ViewHolder> {

        public MyOnGoingRecyclerViewAdapter() {
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // 给ViewHolder设置布局文件
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wish,viewGroup,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // 给ViewHolder设置元素
            TestData testData = datas.get(position);
            viewHolder.tv_itemwish_title.setText(testData.title);
            viewHolder.tv_itemwish_description.setText(testData.description);
            viewHolder.tv_itemwish_amount.setText(testData.wishfund+"");
            viewHolder.iv_itemwish_pic.setImageResource(testData.photoid);

        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_itemwish_title;
            public TextView tv_itemwish_amount;
            public TextView tv_itemwish_description;
            public ImageView iv_itemwish_pic;

            public ViewHolder(View view){
                super(view);
                tv_itemwish_title = (TextView) view.findViewById(R.id.tv_itemwish_title);
                tv_itemwish_amount =  (TextView) view.findViewById(R.id.tv_itemwish_amount);
                tv_itemwish_description = (TextView) view.findViewById(R.id.tv_itemwish_description);
                iv_itemwish_pic = (ImageView) view.findViewById(R.id.iv_itemwish_pic);
            }
        }
    }

}
