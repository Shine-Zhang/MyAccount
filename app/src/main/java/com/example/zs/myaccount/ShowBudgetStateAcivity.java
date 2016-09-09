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
import android.text.TextUtils;
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
import android.widget.Toast;

import com.example.zs.application.MyAplication;
import com.example.zs.utils.KeyboardUtil;
import com.example.zs.view.DynamicWave;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextView tvShowBudgetStateActivityNumTip;
    private int adapterPosition;
    private int monthdays;
    private Calendar calendar;

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
        calendar = Calendar.getInstance();
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        last = dateFormater.format(calendar.getTime());

        //本月天数
        String substring = last.substring(8,10);
        monthdays = Integer.parseInt(substring);

        daysOfMonth = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25","26","27","28","29","30"};

        mVpShowBudgetSta.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==0){
                    String myBudget;
                   if((!TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget")))) {
                       Log.i("haha","***************************"+MyAplication.getStringFromSp("myBudget"));
                       myBudget =MyAplication.getStringFromSp("myBudget");
                       TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                       text.setText(myBudget);
                   }else{

                       TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                       text.setText("3000");
                   }

                    if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        String src = "2016-09-10";
                        String des = "2016-09-"+budget_deadline;
                        int span = Integer.parseInt(getSpan(des,src));
                        if(span>=0){

                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }else{

                            des = "2016-10-"+budget_deadline;
                            span =  Integer.parseInt(getSpan(des,src));
                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }

                    }else{

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                        text.setText("未知");

                    }

                }

                else if(position==1){

                    String myBudget;
                    if((!TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget")))) {
                        myBudget =MyAplication.getStringFromSp("myBudget");
                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                        text.setText(myBudget);
                    }else{

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_setbudget);
                        text.setText("3000");
                    }

                    if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                        text.setText(budget_deadline+"");
                    }else {

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(1).findViewById(R.id.et_show_budeget_sta_set_deadline);
                        text.setText("1");

                    }

                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        String budget;
        String rule;
        String rule1;
        Pattern p1;
        Matcher m1;
        switch (view.getId()){

            case R.id.ib_show_budeget_sta_setting_back:

                    MyAplication.saveStringToSp("budget_deadline",et_show_budeget_sta_set_deadline.getText().toString());

                budget =  et_show_budeget_sta_setbudget.getText().toString(); ;
                if(TextUtils.isEmpty(budget)){
                    Toast.makeText(ShowBudgetStateAcivity.this,"预算金额不能为空!",Toast.LENGTH_SHORT).show();
                }else {
                    rule = "^\\d{1,7}|^\\d{1,7}\\.\\d{1,2}";
                    Pattern p = Pattern.compile(rule);
                    Matcher m = p.matcher(budget);
                    if (m.matches()) {
                        MyAplication.saveStringToSp("myBudget", budget);
                        mVpShowBudgetSta.setCurrentItem(0);
                    } else {

                        Toast.makeText(ShowBudgetStateAcivity.this, "请设置正确的预算!", Toast.LENGTH_SHORT).show();
                    }
                }


                    break;


                    case R.id.bt_show_budget_sta_finish:

                        MyAplication.saveStringToSp("budget_deadline",et_show_budeget_sta_set_deadline.getText().toString());

                        budget = et_show_budeget_sta_setbudget.getText().toString();
                        Log.i("xuxu",budget);
                        if (TextUtils.isEmpty(budget)) {
                            Toast.makeText(ShowBudgetStateAcivity.this, "预算金额不能为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            rule = "^\\d{1,7}|^\\d{1,7}\\.\\d{1,2}";

                            Pattern p = Pattern.compile(rule);
                            Matcher m = p.matcher(budget);
                            if (m.matches()) {
                                MyAplication.saveStringToSp("myBudget", budget);
                                mVpShowBudgetSta.setCurrentItem(0);
                            } else {

                                Toast.makeText(ShowBudgetStateAcivity.this, "请设置正确的预算!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        break;



           case  R.id.rl_show_budget_sta_set_budget:
               //弹出键盘
               if(!TextUtils.isEmpty(et_show_budeget_sta_setbudget.getText().toString())){
                   et_show_budeget_sta_setbudget.setText("");
               }
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
                    tvShowBudgetStateActivityNumTip = (TextView) pagers.get(0).findViewById(R.id.tv_show_budget_state_activity_num_tip);
                    myWave = (DynamicWave) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.dynamicWave_show_budget_activity_mywave);
                    myWave.setmCurentRatio(currentRatio);
                    if(TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget"))){
                        //没数据就给默认值
                        tvShowBudgetStateActivityNumTip.setText("3000");

                    }else{

                        tvShowBudgetStateActivityNumTip.setText(MyAplication.getStringFromSp("myBudget"));
                    }

                    if(!TextUtils.isEmpty(MyAplication.getStringFromSp("budget_deadline"))) {
                        String budget_deadline =MyAplication.getStringFromSp("budget_deadline");


                        String src = "2016-09-10";
                        String des = "2016-09-"+budget_deadline;
                        int span = Integer.parseInt(getSpan(des,src));
                        if(span>=0){

                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }else{

                             des = "2016-10-"+budget_deadline;
                            span =  Integer.parseInt(getSpan(des,src));
                            TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                            text.setText(span+"");
                        }

                    }else{

                        TextView text = (TextView) mVpShowBudgetSta.getChildAt(0).findViewById(R.id.tv_show_budget_state_activity_deadSpan);
                        text.setText("未知");

                    }


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
                     String deadLine = MyAplication.getStringFromSp("budget_deadline");
                    if(TextUtils.isEmpty(deadLine)){
                        et_show_budeget_sta_set_deadline.setText("1");
                    }else{
                        et_show_budeget_sta_set_deadline.setText(deadLine+"");
                    }
//                    et_show_budeget_sta_setbudget.
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
                            adapterPosition = vh.getAdapterPosition();
                            et_show_budeget_sta_set_deadline.setText(daysOfMonth[adapterPosition]);
                            View tv_itemsetbudgetdays_day = vh.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                            tv_itemsetbudgetdays_day.setEnabled(false);
                           // Toast.makeText(ShowBudgetStateAcivity.this, "click"+daysOfMonth[adapterPosition], Toast.LENGTH_SHORT).show();
                            if(mViewHolder!=null){
                                Log.i("haha","mViewHoder="+mViewHolder);
                                TextView tmpTextView = (TextView) mViewHolder.itemView.findViewById(R.id.tv_itemsetbudgetdays_day);
                                tmpTextView.setEnabled(true);
                                myAdapter.notifyItemChanged(mMapping.get(mViewHolder));
                                Log.i("haha","mPosition: "+mViewHolder.getAdapterPosition()+"---"+"position: "+ adapterPosition);
                            }

                            mViewHolder = vh;
                            mMapping.put(vh, adapterPosition);
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

    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }


    public  String  getSpan(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
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




