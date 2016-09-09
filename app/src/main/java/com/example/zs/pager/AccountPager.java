package com.example.zs.pager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.myaccount.MainActivity;
import com.example.zs.myaccount.R;
import com.example.zs.myaccount.ShowBudgetStateAcivity;
import com.example.zs.utils.DensityUtil;
import com.example.zs.view.PinnedHeaderExpandableListView;
import com.example.zs.view.StickyLayout;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 *
 * 该类维护了整个个明细标签页的显示页面布局以及显示的内容
 * Created by Administrator on 2016/9/3 0003.
 * @author  shine-zhang
 */
public class AccountPager extends BasePager implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener, StickyLayout.OnGiveUpTouchEventListener{

    @ViewInject(R.id.lv_accountpager_showaccounts)

    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private  ArrayList<ArrayList<AccountChildItemBean>> childItems;
    private  ArrayList<AccountGroupItemBean> groupItems;

    private MyexpandableListAdapter adapter;
    private RelativeLayout accountPagerBudgetSta;


    public AccountPager(Activity activity) {
        super(activity);
    }

    /**
     * 通过填充布局文件的方式，从而得到整个明细r页面的view以及其中的的各个子控件
     * @return 返回通过布局文件填充得到的View对象
     */
    @Override
    public View initView() {
        mrootView = View.inflate(mActivity,R.layout.account_pager_layout,null);
        expandableListView = (PinnedHeaderExpandableListView) mrootView.findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout) mrootView.findViewById(R.id.sticky_layout);
        stickyLayout.setPinnedHeaderExpandableListView(expandableListView);

        View footView = View.inflate(mActivity,R.layout.account_pager_footview,null);
        expandableListView.initFootView(footView);

        accountPagerBudgetSta = (RelativeLayout) mrootView.findViewById(R.id.rl_account_pager_budget_state);
        accountPagerBudgetSta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ShowBudgetStateAcivity.class);
                float currentHight = DensityUtil.dip2px(mActivity,150);
                intent.putExtra("currentHight",0.5f);
                Log.i("haha","&&&&&&&&&&&&&&&&&&&&:"+currentHight);
                mActivity.startActivity(intent);
            }
        });

        childItems = new  ArrayList<ArrayList<AccountChildItemBean>> ();
        groupItems = new ArrayList<>();
        return mrootView;

    }


    /**
     * 初始化明细页面所要显示的数据，也就是实际所需要现实的内容
     */
    @Override
    public void initData() {
        if(childItems!=null){
            childItems.clear();
        }

        if(groupItems!=null){
            groupItems.clear();
        }

      /*  Log.i("haha","************进入initData***********");
        //手动写的测试数据
        AccountChildItemBean itemBean1 = new AccountChildItemBean(9,1,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","1200",true);
        AccountChildItemBean itemBean2 = new AccountChildItemBean(9,1,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","1300",false);
        AccountChildItemBean itemBean3 = new AccountChildItemBean(9,1,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","1400",false);
        //第一个Group
        ArrayList<AccountChildItemBean> tmp1 = new ArrayList<>();
        tmp1.add(itemBean1);
        tmp1.add(itemBean2);
        tmp1.add(itemBean3);
        childItems.add(tmp1);
        //第二个Group
        AccountChildItemBean itemBean4 = new AccountChildItemBean(9,2,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","1900",true);
        AccountChildItemBean itemBean5 = new AccountChildItemBean(9,2,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","200",true);
        AccountChildItemBean itemBean6 = new AccountChildItemBean(9,2,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","300",false);
        ArrayList<AccountChildItemBean> tmp2 = new ArrayList<>();
        tmp2.add(itemBean4);
        tmp2.add(itemBean5);
        tmp2.add(itemBean6);
        childItems.add(tmp2);
        //第三个Group
        AccountChildItemBean itemBean7 = new AccountChildItemBean(9,3,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","1900",true);
        AccountChildItemBean itemBean8 = new AccountChildItemBean(9,3,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","200",true);
        AccountChildItemBean itemBean9 = new AccountChildItemBean(9,3,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","300",false);
        ArrayList<AccountChildItemBean> tmp3 = new ArrayList<>();
        tmp3.add(itemBean7);
        tmp3.add(itemBean8);
        tmp3.add(itemBean9);
        childItems.add(tmp3);


        AccountGroupItemBean groupItemBean1 = new AccountGroupItemBean(1,100000,200);
        AccountGroupItemBean groupItemBean2 = new AccountGroupItemBean(2,200000,300);
        AccountGroupItemBean groupItemBean3 = new AccountGroupItemBean(3,300000,400);

        groupItems.add(groupItemBean1);
        groupItems.add(groupItemBean2);
        groupItems.add(groupItemBean3);
        Log.i("haha","************初始化数据完毕***********");*/

        adapter = new MyexpandableListAdapter(mActivity);
        expandableListView.setAdapter(adapter);


        // 展开所有group
/*        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            Log.i("haha","************展开所有数据完毕***********"+i);
            expandableListView.expandGroup(i);
        }*/
        expandableListView.expandGroup(0);
        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        stickyLayout.setOnGiveUpTouchEventListener(this);


    }

    /***
     * 数据源
     *
     * @author Administrator
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ChildViewHolder holder;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupItems.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childItems.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupItems.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childItems.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }



        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupViewHoler holder;
            View view = null;
            if (convertView != null) {
                view = convertView;
                holder = (GroupViewHoler) view.getTag();
            } else {
                view = createGroupView();
                holder = new GroupViewHoler();
                holder.iv_account_pager_item_img_describe = (TextView) view.findViewById(R.id.iv_account_pager_item_img_describe);
                holder.tv_account_pager_income_how_much = (TextView) view.findViewById(R.id.tv_account_pager_income_how_much);
                holder.tv_account_pager_outcome_how_much = (TextView) view.findViewById(R.id.tv_account_pager_outcome_how_much);
                view.setTag(holder);
            }


            AccountGroupItemBean groupItemBean = groupItems.get(groupPosition);
            holder.iv_account_pager_item_img_describe.setText(groupItemBean.getDayOfMonth()+"号");
            holder.tv_account_pager_outcome_how_much.setText(String.format("%.2f",groupItemBean.getTotalCosts()));
            holder.tv_account_pager_income_how_much.setText(String.format("%.2f",groupItemBean.getTotalIncome()));
            return view;
        }

        private View createChildrenView() {
            return inflater.inflate(R.layout.childitem, null);
        }

        private View createGroupView() {
            return inflater.inflate(R.layout.groupitem, null);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            Log.i("haha","tmpGroupPosition: "+groupPosition+"--childPosition: "+childPosition);
            final int tmpGroupPosition = groupPosition;
            final int tmpChildPosition = childPosition;
            View view = null;
            if (convertView != null) {
                view = convertView;
                holder = (ChildViewHolder) convertView.getTag();
            } else {
                view = createChildrenView();
                holder = new ChildViewHolder();
                holder.ib_account_pager_item_img_describe = (ImageButton) view.findViewById(R.id.ib_account_pager_item_img_describe);
                holder.tv_account_pager_how_much = (TextView) view.findViewById(R.id.tv_account_pager_how_much);
                holder.iv_account_pager_item_photo = (ImageView) view.findViewById(R.id.iv_account_pager_item_photo);
                holder.tv_account_pager_word_describe = (TextView) view.findViewById(R.id.tv_account_pager_word_describe);
                holder.ib_account_pager_item_edit = (ImageButton) view.findViewById(R.id.ib_account_pager_item_edit);
                holder.ib_account_pager_item_delete = (ImageButton) view.findViewById(R.id.ib_account_pager_item_delete);
                holder.isIncome = false;
                view.setTag(holder);
            }

            AccountChildItemBean itemBean1 = new AccountChildItemBean(9,2,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","100",true);
            AccountChildItemBean itemBean2 = new AccountChildItemBean(9,2,R.drawable.ic_yiban_yellow,R.drawable.ic_yue_default,"一般","800",false);
            if(childPosition%2==0)
                setChildItemBean(itemBean1, holder);
            else
                setChildItemBean(itemBean2, holder);
            final ChildViewHolder tmpHolder = holder;
            //设置监听
            tmpHolder.ib_account_pager_item_img_describe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(MainActivity.this,"hahahaha",Toast.LENGTH_SHORT).show();
                    //开始属性动画
/*                    if(holder==null){
                        Log.i("haha","******************");
                    }*/
                    //开始设置两个ImageButtion的属性动画
                    boolean isFold = childItems.get(tmpGroupPosition).get(tmpChildPosition).isFold();
                    if(!isFold) {
                        //如果当前是展开状态
                        tmpHolder.ib_account_pager_item_edit.setVisibility(View.VISIBLE);
                        tmpHolder.ib_account_pager_item_delete.setVisibility(View.VISIBLE);
                        tmpHolder.tv_account_pager_how_much.setVisibility(View.INVISIBLE);
                        tmpHolder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
                        tmpHolder.tv_account_pager_word_describe.setVisibility(View.INVISIBLE);
                        AnimatorSet set = new AnimatorSet();
                        ObjectAnimator editorAnimator = ObjectAnimator.ofFloat(tmpHolder.ib_account_pager_item_edit, "TranslationX", 0, DensityUtil.dip2px(mActivity, 100));
                        ObjectAnimator deleteAnimator = ObjectAnimator.ofFloat(tmpHolder.ib_account_pager_item_delete, "TranslationX", 0, -DensityUtil.dip2px(mActivity, 100));
                        set.playTogether(editorAnimator, deleteAnimator);
                        set.setDuration(500);
                        set.start();
                        childItems.get(tmpGroupPosition).get(tmpChildPosition).setFold(!isFold);
                        //同时，在编辑和删除两个图标可见的时候，也要给他们设置监听
                        //编辑设置监听
                        tmpHolder.ib_account_pager_item_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //跳转到第二个标签页
                                Toast.makeText(mActivity,"点击了edit",Toast.LENGTH_SHORT).show();
                            }
                        });

                        //给删除图标设置监听
                        tmpHolder.ib_account_pager_item_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //删除该条
                                Toast.makeText(mActivity,"点击了delete",Toast.LENGTH_SHORT).show();
                                Log.i("haha","position: "+(tmpChildPosition));
                                childItems.get(tmpGroupPosition).remove(tmpChildPosition);
                                //通知更新
                                adapter.notifyDataSetChanged();
                            }
                        });


                    }else{
                        //否则当前是，收缩状态
                        tmpHolder.ib_account_pager_item_edit.setVisibility(View.GONE);
                        tmpHolder.ib_account_pager_item_delete.setVisibility(View.GONE);
                        tmpHolder.tv_account_pager_how_much.setVisibility(View.VISIBLE);
                        tmpHolder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                        tmpHolder.tv_account_pager_word_describe.setVisibility(View.VISIBLE);
                        AnimatorSet set = new AnimatorSet();
                        ObjectAnimator editorAnimator = ObjectAnimator.ofFloat(tmpHolder.ib_account_pager_item_edit, "TranslationX",  DensityUtil.dip2px(mActivity, 100),0);
                        ObjectAnimator deleteAnimator = ObjectAnimator.ofFloat(tmpHolder.ib_account_pager_item_delete, "TranslationX",-DensityUtil.dip2px(mActivity, 100), 0 );
                        set.playTogether(editorAnimator, deleteAnimator);
                        set.setDuration(500);
                        set.start();
                        childItems.get(tmpGroupPosition).get(tmpChildPosition).setFold(!isFold);
                    }


                }
            });
            holder=null;
            return view;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Toast.makeText(mActivity,
                "haha", Toast.LENGTH_SHORT)
                .show();

        return false;
    }

    /**
     * 该类为TimeLime中ChildView的复用的标记
     */
    class ChildViewHolder{
        boolean isIncome;
        ImageButton ib_account_pager_item_img_describe;
        ImageView iv_account_pager_item_photo;
        TextView tv_account_pager_word_describe;
        TextView tv_account_pager_how_much;
        ImageButton ib_account_pager_item_edit;
        ImageButton ib_account_pager_item_delete;
    }

    /**
     * 该类为TimeLine中GroupView复用的标记
     */
    class GroupViewHoler{
        TextView iv_account_pager_item_img_describe;
        TextView tv_account_pager_income_how_much;
        TextView tv_account_pager_outcome_how_much;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.groupitem, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headerView.setBackgroundColor(Color.WHITE);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        AccountGroupItemBean firstVisibleGroup = (AccountGroupItemBean) adapter.getGroup(firstVisibleGroupPos);
        TextView MonthOfDay = (TextView) headerView.findViewById(R.id.iv_account_pager_item_img_describe);
        TextView income = (TextView) headerView.findViewById(R.id.tv_account_pager_income_how_much);
        TextView outcome = (TextView) headerView.findViewById(R.id.tv_account_pager_outcome_how_much);
        MonthOfDay.setText(firstVisibleGroup.getDayOfMonth()+"号");
        income.setText(firstVisibleGroup.getTotalIncome()+"");
        outcome.setText(firstVisibleGroup.getTotalCosts()+"");
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }




    public void setChildItemBean(AccountChildItemBean childItemBean, ChildViewHolder holder){
        // Log.i("haha","**************************************");
        //首先设置，条目的图标，因为无论是收入还是支出，其位置都是不变的，都在正中间的位置
        holder.ib_account_pager_item_img_describe.setBackgroundResource(childItemBean.getIcon());
        if(childItemBean.isIncome()){
            //如果当前添加的条目是收入
            //  Log.i("haha","收入*************************");
            if(!holder.isIncome) {
                //该view的布局变成了income的
                holder.isIncome = true;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_account_pager_item_photo.getLayoutParams();
                //清除之前的布局，否则不会生效
                layoutParams.removeRule(RelativeLayout.LEFT_OF);
                //设置图片的布局
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.ib_account_pager_item_img_describe);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                layoutParams.setMargins(DensityUtil.dip2px(mActivity, 5), 0, 0, 0);
                //设置布局参数
                holder.iv_account_pager_item_photo.setLayoutParams(layoutParams);
                if (childItemBean.getPhotoResId() > 0) {
                    //如果用户设置了图片,就在改变为之后的ImageView中显示该图片
                    holder.iv_account_pager_item_photo.setImageResource(childItemBean.getPhotoResId());
                    holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                } else {
                    //如果没有图片就设置imageView为不可见,同时为了方便用户之后可能会存在的添加行为,同时也要改变ImageView的布局
                    holder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
                }

                //设置账目的语言描述和支出/收入金额
                RelativeLayout.LayoutParams wordLayoutParams = (RelativeLayout.LayoutParams) holder.tv_account_pager_how_much.getLayoutParams();
                wordLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                //首先清除之前的布局，否则新的布局不会生效不会生效
                wordLayoutParams.removeRule(RelativeLayout.RIGHT_OF);
                wordLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.ib_account_pager_item_img_describe);
                wordLayoutParams.setMargins(0, 0, DensityUtil.dip2px(mActivity, 5), 0);
                holder.tv_account_pager_how_much.setLayoutParams(wordLayoutParams);
                holder.tv_account_pager_how_much.setText(childItemBean.getHowmuch());

                RelativeLayout.LayoutParams countlayoutParams = (RelativeLayout.LayoutParams) holder.tv_account_pager_word_describe.getLayoutParams();
                countlayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                countlayoutParams.removeRule(RelativeLayout.RIGHT_OF);
                countlayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.tv_account_pager_how_much);
                /*holder.tv_account_pager_word_describe.setPadding(0, 0, DensityUtil.dip2px(this, 5), 0);*/
                holder.tv_account_pager_word_describe.setLayoutParams(countlayoutParams);
                holder.tv_account_pager_word_describe.setText(childItemBean.getItemDescribe());
            }else{
                //当前服用的listView的Item的布局是收入的布局，此时其布局参数不用变，我们直接赋值就可以了
                holder.iv_account_pager_item_photo.setImageResource(childItemBean.getPhotoResId());
                holder.tv_account_pager_word_describe.setText(childItemBean.getItemDescribe());
                holder.tv_account_pager_how_much.setText(childItemBean.getHowmuch());
            }

        }else{
            //否则当前添加的条目是支出
            if(holder.isIncome){
                //但是此时listView中的item的布局是收入的布局，此时需要改变相应的参数位置
                holder.isIncome = false;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_account_pager_item_photo.getLayoutParams();
                //清除之前的布局，否则不会生效
                layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                //设置图片的布局
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.ib_account_pager_item_img_describe);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                layoutParams.setMargins(0,0,DensityUtil.dip2px(mActivity, 5), 0);
                //设置布局参数
                holder.iv_account_pager_item_photo.setLayoutParams(layoutParams);
                if (childItemBean.getPhotoResId() > 0) {
                    //如果用户设置了图片,就在改变为之后的ImageView中显示该图片
                    holder.iv_account_pager_item_photo.setImageResource(childItemBean.getPhotoResId());
                    holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                } else {
                    //如果没有图片就设置imageView为不可见,同时为了方便用户之后可能会存在的添加行为,同时也要改变ImageView的布局
                    holder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
                }

                //设置账目的语言描述和支出/收入金额
                RelativeLayout.LayoutParams wordLayoutParams = (RelativeLayout.LayoutParams) holder.tv_account_pager_word_describe.getLayoutParams();
                wordLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                //首先清除之前的布局，否则新的布局不会生效不会生效
                wordLayoutParams.removeRule(RelativeLayout.LEFT_OF);
                wordLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.ib_account_pager_item_img_describe);
                wordLayoutParams.setMargins( DensityUtil.dip2px(mActivity, 5),0,0, 0);
                holder.tv_account_pager_word_describe.setLayoutParams(wordLayoutParams);
                holder.tv_account_pager_word_describe.setText(childItemBean.getItemDescribe());

                RelativeLayout.LayoutParams countlayoutParams = (RelativeLayout.LayoutParams) holder.tv_account_pager_how_much.getLayoutParams();
                countlayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                countlayoutParams.removeRule(RelativeLayout.LEFT_OF);
                countlayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_account_pager_word_describe);
                    /*holder.tv_account_pager_word_describe.setPadding(DensityUtil.dip2px(this, 5), 0,0, 0);*/
                holder.tv_account_pager_how_much.setLayoutParams(countlayoutParams);
                holder.tv_account_pager_how_much.setText(childItemBean.getHowmuch());
            } else{
                //  Log.i("haha","支出*************************");
                holder.iv_account_pager_item_photo.setImageResource(childItemBean.getPhotoResId());
                holder.tv_account_pager_word_describe.setText(childItemBean.getItemDescribe());
                holder.tv_account_pager_how_much.setText(childItemBean.getHowmuch());
            }
        }
    }


}
