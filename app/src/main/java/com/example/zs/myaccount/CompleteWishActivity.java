package com.example.zs.myaccount;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompleteWishActivity extends AppCompatActivity implements View.OnClickListener {

    private List<TestData> datas;
    private RecyclerView rcv_completewishactivity_wishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_wish);
        getSupportActionBar().hide();

        ImageButton imgbt_completewishactivity_back = (ImageButton) findViewById(R.id.imgbt_completewishactivity_back);
        imgbt_completewishactivity_back.setOnClickListener(this);

        RelativeLayout rl_completewishactivity_norecord = (RelativeLayout) findViewById(R.id.rl_completewishactivity_norecord);
        rcv_completewishactivity_wishes = (RecyclerView) findViewById(R.id.rcv_completewishactivity_wishes);

        if(hasOnGoingWishes()){
            //有已完成愿望
            rl_completewishactivity_norecord.setVisibility(View.INVISIBLE);
            rcv_completewishactivity_wishes.setVisibility(View.VISIBLE);

            //创建默认的线性LayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rcv_completewishactivity_wishes.setLayoutManager(layoutManager);
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            rcv_completewishactivity_wishes.setHasFixedSize(true);


        }else{
            //没有已完成愿望
            rl_completewishactivity_norecord.setVisibility(View.VISIBLE);
            rcv_completewishactivity_wishes.setVisibility(View.INVISIBLE);
        }

        initData();

    }

    private void initData() {
        /*测试数据，用于测试*/
        String[] titles = new String[]{"我的愿望1","我的愿望2","我的愿望3","我的愿望4","我的愿望5","我的愿望6"};
        String[] descriptions = new String[]{"备注1","备注2","备注3","备注4","备注5","备注6"};
        datas = new ArrayList<TestData>();
        for(int i = 0;i<5;i++){
            datas.add(new TestData(titles[i],descriptions[i]));
        }

        //初始化自定义的适配器
        MyCompleteWishRecyclerViewAdapter myAdapter= new MyCompleteWishRecyclerViewAdapter(datas);
        //为rcv_wishpager_wishes设置适配器
        rcv_completewishactivity_wishes.setAdapter(myAdapter);
        //给拿到RecyclerView添加条目点击事件
    }

    class TestData{
        String title;
        String description;
        public TestData() {
        }
        public TestData(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbt_completewishactivity_back:
                finish();
        }


    }

    /**
     *
     * @return true表示有正在进行的愿望，false表示没有正在进行的愿望
     */
    private boolean hasOnGoingWishes() {
        return true;
    }

    /**
     * 该类为RecyclerView的Adapter
     * RecyclerView与ListView类似，都需要Adapter
     */
    class MyCompleteWishRecyclerViewAdapter extends RecyclerView.Adapter<MyCompleteWishRecyclerViewAdapter.MyViewHolder> {
        public List<TestData> datas = null;
        //构造方法
        public MyCompleteWishRecyclerViewAdapter(List<TestData> datas) {
            this.datas = datas;
        }
        //创建新View，被LayoutManager所调用
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_completewish,viewGroup,false);
            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int position) {
            // 给ViewHolder设置元素
            TestData testData = datas.get(position);
            viewHolder.tv_itemcompletewish_title.setText(testData.title);
            viewHolder.tv_itemcompletewish_description.setText(testData.description);
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_itemcompletewish_title;
            public TextView tv_itemcompletewish_description;
            public MyViewHolder(View view){
                super(view);
                tv_itemcompletewish_title = (TextView) view.findViewById(R.id.tv_itemcompletewish_title);
                tv_itemcompletewish_description = (TextView) view.findViewById(R.id.tv_itemcompletewish_description);
            }
        }
    }
}
