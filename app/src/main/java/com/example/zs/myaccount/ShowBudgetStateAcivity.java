package com.example.zs.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.utils.KeyboardUtil;
import com.example.zs.view.DynamicWave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Shine-Zhang
 */
public class ShowBudgetStateAcivity extends Activity implements View.OnClickListener{

    private DynamicWave myWave;
    private ImageButton ibShowBudgetStaBack;
    private ImageButton ibShowBudgetStaSetting;
    private ViewPager mVpShowBudgetSta;
    private List<View> pagers;
    private float currentRatio;
    private EditText et_show_budeget_sta_setbudget;
    private EditText et_show_budeget_sta_set_deadline;
    private RecyclerView.ViewHolder mViewHolder;
    HashMap<RecyclerView.ViewHolder,Integer> mMapping;
    private String last;
    private String[] daysOfMonth;
    private RecyclerView rv_show_budget_sta_recyclerview;
    private boolean isfold=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_budget_state_acivity);

        Intent intent = getIntent();
        //如果没有拿到从
        currentRatio = intent.getFloatExtra("currentHight",-1);
        // Log.i("haha","!!!!!!!!!!!!!!!!! :"+currentRatio);


        mVpShowBudgetSta = (ViewPager) findViewById(R.id.vp_show_budget_sta);

        pagers = new ArrayList<>();
        pagers.add(View.inflate(this, R.layout.vp_show_budget_sta,null));
        pagers.add(View.inflate(this, R.layout.vp_show_budget_state_setting,null));

        mVpShowBudgetSta.setAdapter(new ContentAdapter());
        mVpShowBudgetSta.setCurrentItem(0);


        mMapping = new HashMap<>();


        //数据
        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        last = dateFormater.format(calendar.getTime());

        //本月天数
        String substring = last.substring(8,10);
        final int monthdays = Integer.parseInt(substring);

        daysOfMonth = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29","30"};



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ib_show_budeget_sta_setting_back:
                mVpShowBudgetSta.setCurrentItem(0);
            break;


           case  R.id.bt_show_budget_sta_finish:
               finish();
            break;


           case  R.id.rl_show_budget_sta_set_budget:
               //弹出键盘

               int inputback = et_show_budeget_sta_setbudget.getInputType();
               et_show_budeget_sta_setbudget.setInputType(InputType.TYPE_NULL);
               KeyboardUtil keyboardUtil = new KeyboardUtil(this, this, et_show_budeget_sta_setbudget);
               keyboardUtil.setNumberFormat(7);
               keyboardUtil.showKeyboard();
               et_show_budeget_sta_setbudget.setInputType(inputback);

            break;



           case  R.id.rl_show_budeget_sta_set_deadline:
               //弹出日期选择的indicator
               if(isfold) {
                   rv_show_budget_sta_recyclerview.setVisibility(View.VISIBLE);
                   isfold = false;
               }else{
                   isfold = true;
                   rv_show_budget_sta_recyclerview.setVisibility(View.GONE);
               }
            break;


        }

    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagers.get(position));
            switch (position){
                case 0:

                    ibShowBudgetStaBack = (ImageButton) pagers.get(0).findViewById(R.id.ib_show_budeget_sta_back);
                    ibShowBudgetStaSetting = (ImageButton)pagers.get(0). findViewById(R.id.ib_show_budeget_sta_setting);
                    myWave = (DynamicWave) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.dynamicWave_show_budget_activity_mywave);
                    myWave.setmCurentRatio(currentRatio);

                    ibShowBudgetStaBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });

                    ibShowBudgetStaSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //实现页面切换
                            mVpShowBudgetSta.setCurrentItem(1);
                        }
                    });

                    break;

                case 1:

                     ImageButton ib_show_budeget_sta_setting_back = (ImageButton) pagers.get(1).findViewById(R.id.ib_show_budeget_sta_setting_back);
                     Button bt_show_budget_sta_finish = (Button) pagers.get(1).findViewById(R.id.bt_show_budget_sta_finish);
                     RelativeLayout rl_show_budget_sta_set_budget = (RelativeLayout) pagers.get(1).findViewById(R.id.rl_show_budget_sta_set_budget);
                     RelativeLayout rl_show_budeget_sta_set_deadline = (RelativeLayout) pagers.get(1).findViewById(R.id.rl_show_budeget_sta_set_deadline);
                    et_show_budeget_sta_setbudget = (EditText) pagers.get(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                    et_show_budeget_sta_set_deadline = (EditText) pagers.get(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                    rv_show_budget_sta_recyclerview = (RecyclerView) pagers.get(1).findViewById(R.id.rv_show_budget_sta_recyclerview);

                    ib_show_budeget_sta_setting_back.setOnClickListener(ShowBudgetStateAcivity.this);
                    bt_show_budget_sta_finish.setOnClickListener(ShowBudgetStateAcivity.this);
                    rl_show_budget_sta_set_budget.setOnClickListener(ShowBudgetStateAcivity.this);
                    rl_show_budeget_sta_set_deadline.setOnClickListener(ShowBudgetStateAcivity.this);
                    et_show_budeget_sta_setbudget.setOnClickListener(ShowBudgetStateAcivity.this);
                    et_show_budeget_sta_set_deadline.setOnClickListener(ShowBudgetStateAcivity.this);


                    //创建默认的线性LayoutManager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ShowBudgetStateAcivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rv_show_budget_sta_recyclerview.setLayoutManager(layoutManager);
                    //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                    rv_show_budget_sta_recyclerview.setHasFixedSize(true);
                    //初始化自定义的适配器
                    final MyAdapter myAdapter = new MyAdapter(daysOfMonth);
                    //为rcv_wishpager_wishes设置适配器
                    rv_show_budget_sta_recyclerview.setAdapter(myAdapter);
                    //给拿到RecyclerView添加条目点击事件
                    rv_show_budget_sta_recyclerview.addOnItemTouchListener(new OnItemTouchListener(rv_show_budget_sta_recyclerview) {
                        @Override
                        public void onItemClick(RecyclerView.ViewHolder vh) {
                            //点击时做的事
                            int adapterPosition = vh.getAdapterPosition();

                            et_show_budeget_sta_set_deadline.setText(daysOfMonth[adapterPosition]+"天");
                            View tv_itemsetbudgetdays_day = vh.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                            tv_itemsetbudgetdays_day.setEnabled(false);
                           // Toast.makeText(ShowBudgetStateAcivity.this, "click"+daysOfMonth[adapterPosition], Toast.LENGTH_SHORT).show();
                            if(mViewHolder!=null){
                                Log.i("haha","mViewHoder="+mViewHolder);
                                TextView tmpTextView = (TextView) mViewHolder.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                                tmpTextView.setEnabled(true);
                                myAdapter.notifyItemChanged(mMapping.get(mViewHolder));
                                Log.i("haha","mPosition: "+mViewHolder.getAdapterPosition()+"---"+"position: "+adapterPosition);
                            }

                            mViewHolder = vh;
                            mMapping.put(vh,adapterPosition);
                        }
                    });

                    break;


            }
            return pagers.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


    /**
     * 该类为RecyclerView的Adapter
     * RecyclerView与ListView类似，都需要Adapter
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        /**
         * 这里创建一个数组，准备接收传过来的数据
         */
        public String[] datas = null;

        //构造方法

        /**
         * 这里调用在创建MyAdapter实例的时候，可以将数据传过来
         *
         * @param datas
         */
        public MyAdapter(String[] datas) {
            this.datas = datas;
            Log.i("TAG","datas="+datas.length);
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
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_setbudgetdays, viewGroup, false);
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
            String s = datas[position];
            viewHolder.tv_itemsetbudgetdays_day.setText(s);

        }

        //获取数据的数量

        /**
         * 这里返回item数量
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return datas.length;
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        /**
         * ViewHolder类，注意要继承RecyclerView.ViewHolder
         */
        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_itemsetbudgetdays_day;

            public MyViewHolder(View view) {
                super(view);
                tv_itemsetbudgetdays_day = (TextView) view.findViewById(R.id.tv_itemsetbudgetdays_day);
            }
        }

    }

}

/**
 * RecyclerView的触摸监听
 * 重写了RecyclerView.OnItemTouchListener的四个方法
 * 加了一个抽象方法，可以自行处理当前点击该Item
 */
abstract class ShowBudgetStateOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public ShowBudgetStateOnItemTouchListener(RecyclerView recyclerView) {
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




