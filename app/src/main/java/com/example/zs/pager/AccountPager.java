package com.example.zs.pager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.zs.application.MyAplication;
import com.example.zs.bean.AccountChildItemBean;
import com.example.zs.bean.AccountGroupItemBean;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.dao.TimeLineDAO;
import com.example.zs.myaccount.AddRecordActivity;
import com.example.zs.myaccount.MainActivity;
import com.example.zs.myaccount.R;
import com.example.zs.myaccount.ShowBudgetStateAcivity;
import com.example.zs.utils.DensityUtil;
import com.example.zs.view.CircleImageView;
import com.example.zs.view.PinnedHeaderExpandableListView;
import com.example.zs.view.StickyLayout;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * 该类维护了整个个明细标签页的显示页面布局以及显示的内容
 * Created by Administrator on 2016/9/3 0003.
 * @author  shine-zhang
 */
public class AccountPager extends BasePager implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener, StickyLayout.OnGiveUpTouchEventListener,View.OnClickListener{

    @ViewInject(R.id.lv_accountpager_showaccounts)

    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private  ArrayList<ArrayList<AccountChildItemBean>> childItems;
    private  ArrayList<AccountGroupItemBean> groupItems;
    private MyexpandableListAdapter adapter;
    private RelativeLayout accountPagerBudgetSta;

    private static final int PHOTO_REQUEST_CAREMA = 100;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 101;// 从相册中选择
    private static final int PHOTO_REQUEST_CAREMA_FROM_ACCOUNT = 201;
    private static final int PHOTO_REQUEST_GALLERY_FROM_ACCOUNT = 202;
    private PopupWindow popupwindow_getphoto;
    private Uri photoUri;
    private TimeLineDAO timeDao;
    private Calendar now;
    private int today;
    private TextView tvAccountPagerTotalIncome;
    private TextView tvAccountPagerTotalCost;
    private float totalIncome;
    private float totalCost;
    private String myBudget;
    private TextView tvAccountPagerBudget;
    private boolean[] groupExpandSta;
    private  int month;
    private TextView tvAccountPagerMonthIncome;
    private TextView tvAccountPagerMonthOutcome;
    private TmpHolder[] groups;
    public AccountPager(Activity activity) {
        super(activity);
        TmpHolder tmp = null;
        groups = new TmpHolder[31];
        for(int i=0;i<31;i++){
             tmp =new TmpHolder();
            tmp.income=null;
            tmp.outcome = null;
            groups[i] = tmp;
        }
        groupExpandSta = new boolean[31];
        for(int i = 0;i<groupExpandSta.length;i++){
            groupExpandSta[i] = true;
        }


      //  Log.i("lalalalal","month: "+month);
    }

    /**
     * 通过填充布局文件的方式，从而得到整个明细r页面的view以及其中的的各个子控件
     * @return 返回通过布局文件填充得到的View对象
     */
    @Override
    public View initView() {
//        Log.i("jjjjjjjjjj","********************************");

        mrootView = View.inflate(mActivity,R.layout.account_pager_layout,null);
        expandableListView = (PinnedHeaderExpandableListView) mrootView.findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout) mrootView.findViewById(R.id.sticky_layout);
        stickyLayout.setPinnedHeaderExpandableListView(expandableListView);
        tvAccountPagerMonthIncome = (TextView) mrootView.findViewById(R.id.tv_account_pager_month_income_tip);
        tvAccountPagerMonthOutcome = (TextView) mrootView.findViewById(R.id.tv_account_pager_month_cost_tip);
        View footView = View.inflate(mActivity,R.layout.account_pager_footview,null);
        expandableListView.initFootView(footView);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

                groupExpandSta[groupItems.get(i).getDayOfMonth()-1] = true;
                Log.i("lalal","g**roupExpandSta[groupItems.get(i).getDayOfMonth()]:"+groupExpandSta[groupItems.get(i).getDayOfMonth()]);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

                groupExpandSta[groupItems.get(i).getDayOfMonth()-1] = false;
                Log.i("lalal","***groupExpandSta[groupItems.get(i).getDayOfMonth()]:"+groupExpandSta[groupItems.get(i).getDayOfMonth()]);
            }
        });
        Log.i("haha","*************");
        accountPagerBudgetSta = (RelativeLayout) mrootView.findViewById(R.id.rl_account_pager_budget_state);
        ImageButton ib_account_pager_camera = (ImageButton) mrootView.findViewById(R.id.ib_account_pager_camera);
        ib_account_pager_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   showPhotoPopWindow();
            }
        });
        accountPagerBudgetSta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ShowBudgetStateAcivity.class);
                float budget = Float.parseFloat(myBudget);
                float balance = budget-totalCost;
                float currentHight = balance/budget;
                intent.putExtra("currentHight",currentHight);
                intent.putExtra("balance",balance);
                intent.putExtra("totalIncome",totalIncome);
               // Log.i("haha","&&&&&&&&&&&&&&&&&&&&:"+currentHight);
                mActivity.startActivity(intent);
            }
        });
        now = Calendar.getInstance();
        today = now.get(Calendar.DAY_OF_MONTH);
        month = now.get(Calendar.MONTH)+1;
        tvAccountPagerMonthIncome.setText(month+"月收入");
        tvAccountPagerMonthOutcome.setText(month+"月支出");
        tvAccountPagerBudget = (TextView) mrootView.findViewById(R.id.tv_account_pager_buget);
        tvAccountPagerTotalIncome = (TextView) mrootView.findViewById(R.id.account_pager_total_income);
        tvAccountPagerTotalCost = (TextView) mrootView.findViewById(R.id.tv_account_pager_month_cost);


  /*      childItems = new  ArrayList<ArrayList<AccountChildItemBean>> ();
        groupItems = new ArrayList<>();*/

        return mrootView;

    }

    private void showPhotoPopWindow() {

        //弹出PopWindow供用户选择
        View contentView= View.inflate(mActivity,R.layout.popupwindow_getphoto_addwish,null);
        Button btAddWishPopwindowCamera = (Button) contentView.findViewById(R.id.bt_addwishpopupwindow_camera);
        Button btAddWishPopwindowGallery = (Button) contentView.findViewById(R.id.bt_addwishpopupwindow_gallery);
        Button btAddWishPopwindowCancle = (Button) contentView.findViewById(R.id.bt_addwishpopupwindow_cancel);
        btAddWishPopwindowCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCamera();
                popupwindow_getphoto.dismiss();
            }
        });

        btAddWishPopwindowGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGallery();
                popupwindow_getphoto.dismiss();
            }
        });

        btAddWishPopwindowCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow_getphoto.dismiss();
            }
        });

        //初始化popupwindow
        popupwindow_getphoto = new PopupWindow();
        //获得焦点
        popupwindow_getphoto.setFocusable(true);
        popupwindow_getphoto.setBackgroundDrawable(new BitmapDrawable());
        //设置popupwindow弹出和退出时的动画效果
        popupwindow_getphoto.setAnimationStyle(R.style.AnimationBottomFade);
        //将popup_view部署到popupWindow上
        popupwindow_getphoto.setContentView(contentView);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_getphoto.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow_getphoto.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置popupwindow显示的位置
        popupwindow_getphoto.showAtLocation(mrootView,Gravity.BOTTOM,0,0);

    }

    private void hidePopuwindow() {
        if (popupwindow_getphoto != null) {
            popupwindow_getphoto.dismiss();//隐藏气泡
            popupwindow_getphoto = null;
        }
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
        timeDao = new TimeLineDAO(mActivity);
        childItems = timeDao.getTimeLinePayOutChildData(month);
        groupItems = timeDao.getTimeLineGroupData(month);

        if((!TextUtils.isEmpty(MyAplication.getStringFromSp("myBudget")))) {
            //Log.i("haha","***************************"+MyAplication.getStringFromSp("myBudget"));
            myBudget =MyAplication.getStringFromSp("myBudget");
        }else{
            myBudget ="3000.00";
        }
        totalIncome = 0;
        totalCost = 0;
        for(int i=0;i<groupItems.size();i++){

            totalIncome +=groupItems.get(i).getTotalIncome();
            totalCost += groupItems.get(i).getTotalCosts();
        }
        Log.i("haha","totalIncome****: "+totalIncome);
        float remain =  Float.parseFloat(myBudget)-totalCost;
        tvAccountPagerBudget.setText(String.format("%.2f", remain));
        tvAccountPagerTotalIncome.setText(String.format("%.2f", totalIncome));
        tvAccountPagerTotalCost.setText(String.format("%.2f", totalCost));


        if(adapter==null) {
            adapter = new MyexpandableListAdapter(mActivity);
        }

            expandableListView.setAdapter(adapter);



        for ( int i = 0;i<groupItems.size(); i++) {
          //  Log.i("haha","************展开所有数据完毕***********"+i);
            if(groupExpandSta[groupItems.get(i).getDayOfMonth()-1]){
                expandableListView.expandGroup(i);
            }else{
                expandableListView.collapseGroup(i);
            }

            //expandableListView.get
        }


        expandableListView.setOnHeaderUpdateListener(this);
        //Log.i("huibuhui","**************************************************");
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        stickyLayout.setOnGiveUpTouchEventListener(this);

    }



    @Override
    public void onClick(View view) {

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
        private int tmpGroupPosition =-1;
        private int tmpChildPosition = -1;
        private ChildViewHolder preHolder = null;
        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            if(preHolder!=null){
                preHolder=null;
            }
            expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    if(preHolder!=null){
                        unFold(preHolder);
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {


                }
            });
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
                groups[groupItems.get(groupPosition).getDayOfMonth()-1].income = holder.tv_account_pager_income_how_much;
                holder.tv_account_pager_outcome_how_much = (TextView) view.findViewById(R.id.tv_account_pager_outcome_how_much);
                groups[groupItems.get(groupPosition).getDayOfMonth()-1].outcome = holder.tv_account_pager_outcome_how_much;
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

            View view = null;
            tmpGroupPosition = groupPosition;
            tmpChildPosition = childPosition;
            if (convertView != null) {
                view = convertView;
                holder = (ChildViewHolder) convertView.getTag();
            } else {
                view = createChildrenView();
                holder = new ChildViewHolder();
                holder.ib_account_pager_item_img_describe = (ImageButton) view.findViewById(R.id.ib_account_pager_item_img_describe);
                holder.tv_account_pager_how_much = (TextView) view.findViewById(R.id.tv_account_pager_how_much);
                holder.iv_account_pager_item_photo = (CircleImageView) view.findViewById(R.id.iv_account_pager_item_photo);
                holder.tv_account_pager_word_describe = (TextView) view.findViewById(R.id.tv_account_pager_word_describe);
                holder.ib_account_pager_item_edit = (ImageButton) view.findViewById(R.id.ib_account_pager_item_edit);
                holder.ib_account_pager_item_delete = (ImageButton) view.findViewById(R.id.ib_account_pager_item_delete);
                holder.isIncome = false;

            }

            holder.child = childPosition;
            holder.group = groupPosition;
            final ChildViewHolder tmpHolder = holder;
            view.setTag(holder);
            if(childPosition%2==0) {
                setChildItemBean(childItems.get(groupPosition).get(childPosition), tmpHolder);
            }

            else {

                setChildItemBean(childItems.get(groupPosition).get(childPosition), tmpHolder);
            }

            tmpHolder.ib_account_pager_item_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到第二个标签页
                    //Toast.makeText(mActivity,"******点击了edit: "+tmpHolder.group+"----"+tmpHolder.child,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mActivity,AddRecordActivity.class);
                    intent.putExtra("isIncome",childItems.get(tmpGroupPosition).get(tmpChildPosition).isIncome());
                    intent.putExtra("id",childItems.get(tmpGroupPosition).get(tmpChildPosition).getId());
                    intent.putExtra("year",2016);
                    intent.putExtra("month",childItems.get(tmpGroupPosition).get(tmpChildPosition).getMonth());
                    intent.putExtra("day",childItems.get(tmpGroupPosition).get(tmpChildPosition).getDayOfMonth());
                    intent.putExtra("money",childItems.get(tmpGroupPosition).get(tmpChildPosition).getHowmuch());
                    intent.putExtra("remarks","备注");
                    intent.putExtra("photoUriString","");
                    intent.putExtra("resourceID",childItems.get(tmpGroupPosition).get(tmpChildPosition).getIcon());
                    intent.putExtra("categoryName",childItems.get(tmpGroupPosition).get(tmpChildPosition).getItemDescribe());
                    mActivity.startActivityForResult(intent,110);
                    unFold(tmpHolder);

                }
            });

            //给删除图标设置监听
            tmpHolder.ib_account_pager_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除该条
                   // Toast.makeText(mActivity,"点击了delete",Toast.LENGTH_SHORT).show();
                    // Log.i("haha","position: "+(tmpChildPosition));
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
                    dialogBuilder.setMessage("你确定要删除所选账目吗？");
                    dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int group = tmpHolder.group;
                            int child = tmpHolder.child;
                            Log.i("nima","goup : "+ group +"child: "+ child);
                           // Log.i("nima","*****************content: "+ childItems.get(group).get(child).getId()+":::::"+childItems.get(group).remove(child).getHowmuch());

                            unFold(tmpHolder);
                            if(childItems.get(group).get(child).isIncome()){
                                IncomeContentDAO inDao = new IncomeContentDAO(mActivity);
                                inDao.deleteIncomeContentItemFromDB(childItems.get(group).get(child).getId());
                                float total = totalIncome - Float.parseFloat(childItems.get(group).get(child).getHowmuch());
                                totalIncome = total;
                                float dayIncome = groupItems.get(group).getTotalIncome()-Float.parseFloat(childItems.get(group).get(child).getHowmuch());
                                TmpHolder groupView = groups[ groupItems.get(group).getDayOfMonth()-1];
                                groupItems.get(group).setTotalIncome(dayIncome);
                                tvAccountPagerTotalIncome.setText(String.format("%.2f",total));
                                groupView.income.setText(String.format("%.2f",dayIncome));
                              //  Log.i("lalalala","outid: "+childItems.get(group).get(child).getId());
                            }else{
                                PayOutContentDAO outDao =new PayOutContentDAO(mActivity);
                                outDao.deletePayoutContentItemFromDB(childItems.get(group).get(child).getId());
                                float total = totalCost - Float.parseFloat(childItems.get(group).get(child).getHowmuch());
                                totalCost = total;
                                float dayCost = groupItems.get(group).getTotalCosts()-Float.parseFloat(childItems.get(group).get(child).getHowmuch());
                                TmpHolder groupView = groups[ groupItems.get(group).getDayOfMonth()-1];
                                groupView.outcome.setText(String.format("%.2f",dayCost));
                                groupItems.get(group).setTotalCosts(dayCost);
                                tvAccountPagerTotalCost.setText(String.format("%.2f",total));
                              //  Log.i("lalalala","inid: "+childItems.get(group).get(child).getId());
                            }
                           // Log.i("haha","删除前groupsize: "+childItems.get(group).size());
                            childItems.get(group).remove(child);
                           // Log.i("haha","删除后groupsize: "+childItems.get(group).size());
                            if( childItems.get(group).size()==0){
                             //   Log.i("haha","删除前（）："+groupItems.size());
                                groupItems.remove(group);
                              // Log.i("haha","删除后（）："+groupItems);
                                if(groupItems.size()==0){
                                    expandableListView.mHeaderView=null;
                                }
                            }
                            //通知更新
                            adapter.notifyDataSetChanged();
                            preHolder = null;
                        }
                    });
                    dialogBuilder.setNegativeButton("取消",null);
                    dialogBuilder.create().show();
                }
            });

            //设置监听
            tmpHolder.ib_account_pager_item_img_describe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Log.i("nima","*******tmpGroupPosition: "+tmpGroupPosition+"--childPosition: "+tmpChildPosition);
                    // Toast.makeText(MainActivity.this,"hahahaha",Toast.LENGTH_SHORT).show();
                    //开始属性动画
/*                    if(holder==null){
                        Log.i("haha","******************");
                    }*/
                    //开始设置两个ImageButtion的属性动画
                    if(preHolder ==null){

                        fold(tmpHolder,500);
                        preHolder = tmpHolder;
                    }else{
                        int group = preHolder.group;
                        int child = preHolder.child;
                        unFold(preHolder);
                        if(preHolder!=tmpHolder) {
                           // Log.i("nima", "******************************************");

                            fold(tmpHolder,500);
                            childItems.get(group).get(child).setFold(false);
                            preHolder = tmpHolder;
                        }else{
                            preHolder=null;
                            childItems.get(group).get(child).setFold(true);
                        }

                    }
                }
            });
            holder=null;
            return view;
        }

        private void fold(ChildViewHolder holder,int duration) {

            holder.ib_account_pager_item_edit.setVisibility(View.VISIBLE);
            holder.ib_account_pager_item_delete.setVisibility(View.VISIBLE);
            holder.tv_account_pager_how_much.setVisibility(View.INVISIBLE);
            holder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
            holder.tv_account_pager_word_describe.setVisibility(View.INVISIBLE);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator editorAnimator = ObjectAnimator.ofFloat(holder.ib_account_pager_item_edit, "TranslationX",0, DensityUtil.dip2px(mActivity, 100));
            ObjectAnimator deleteAnimator = ObjectAnimator.ofFloat(holder.ib_account_pager_item_delete, "TranslationX", 0,-DensityUtil.dip2px(mActivity, 100));
            set.playTogether(editorAnimator, deleteAnimator);
            set.setDuration(duration);
            set.start();
        }

        private void unFold(ChildViewHolder holder) {

            AnimatorSet set = new AnimatorSet();
            ObjectAnimator editorAnimator = ObjectAnimator.ofFloat(holder.ib_account_pager_item_edit, "TranslationX", DensityUtil.dip2px(mActivity, 100),0);
            ObjectAnimator deleteAnimator = ObjectAnimator.ofFloat(holder.ib_account_pager_item_delete, "TranslationX", -DensityUtil.dip2px(mActivity, 100),0);
            set.playTogether(editorAnimator, deleteAnimator);
            set.setDuration(0);
            set.start();
            holder.ib_account_pager_item_edit.setVisibility(View.GONE);
            holder.ib_account_pager_item_delete.setVisibility(View.GONE);
            holder.tv_account_pager_word_describe.setVisibility(View.VISIBLE);

            if( holder.group>0&&holder.child>0&&!TextUtils.isEmpty(childItems.get(holder.group).get(holder.child).getPhotoResUrl())) {
                holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
            }
            holder.tv_account_pager_how_much.setVisibility(View.VISIBLE);

        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {
      //  Log.i("hahaha","&*&&*&*&*&*&*&*&*&*&*&*");
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
        CircleImageView iv_account_pager_item_photo;
        TextView tv_account_pager_word_describe;
        TextView tv_account_pager_how_much;
        ImageButton ib_account_pager_item_edit;
        ImageButton ib_account_pager_item_delete;
        int child;
        int group;
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
       // Log.i("xuanyuan","&*&&*&*&*&");
        View headerView = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.groupitem, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headerView.setBackgroundColor(Color.WHITE);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
       // Log.i("xuanyuan","1212121212:"+firstVisibleGroupPos);
        if(firstVisibleGroupPos!=-1) {
            AccountGroupItemBean firstVisibleGroup = (AccountGroupItemBean) adapter.getGroup(firstVisibleGroupPos);
            TextView MonthOfDay = (TextView) headerView.findViewById(R.id.iv_account_pager_item_img_describe);
            TextView income = (TextView) headerView.findViewById(R.id.tv_account_pager_income_how_much);
            TextView outcome = (TextView) headerView.findViewById(R.id.tv_account_pager_outcome_how_much);
            MonthOfDay.setText(firstVisibleGroup.getDayOfMonth() + "号");
            if (today == firstVisibleGroup.getDayOfMonth()) {
                MonthOfDay.setText("今天");
                MonthOfDay.setBackground(mActivity.getResources().getDrawable(R.drawable.account_pager_group_today_icon));
            }else{
                MonthOfDay.setBackground(mActivity.getResources().getDrawable(R.drawable.account_pager_group_icon));
            }
            income.setText(String.format("%.2f", firstVisibleGroup.getTotalIncome()));
            outcome.setText(String.format("%.2f", firstVisibleGroup.getTotalCosts()));
        }
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
        /*holder.ib_account_pager_item_img_describe.setBackground(mActivity.getResources().getDrawable(childItemBean.getIcon()));*/
        holder.ib_account_pager_item_img_describe.setImageResource(childItemBean.getIcon());
        holder.ib_account_pager_item_img_describe.setBackgroundResource(R.drawable.account_pager_group_today_icon);
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
                if (!TextUtils.isEmpty(childItemBean.getPhotoResUrl())) {
                    //如果用户设置了图片,就在改变为之后的ImageView中显示该图片
                   // Glide.with(mActivity).load(childItemBean.getPhotoResUrl()).into(holder.iv_account_pager_item_photo);
                   // holder.iv_account_pager_item_photo.setImageURI(Uri.parse(childItemBean.getPhotoResUrl()));
                    Glide.with(mActivity)
                            .load(childItemBean.getPhotoResUrl())
                            .listener(mRequestListener)//配置监听器
                            .into(holder.iv_account_pager_item_photo);
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
                //当前服用的listView的Item的布局是支出的布局，此时其布局参数不用变，我们直接赋值就可以了
                if (!TextUtils.isEmpty(childItemBean.getPhotoResUrl())) {
                    holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                   // Glide.with(mActivity).load(childItemBean.getPhotoResUrl()).into(holder.iv_account_pager_item_photo);
                    Glide.with(mActivity)
                            .load(childItemBean.getPhotoResUrl())
                            .listener(mRequestListener)//配置监听器
                            .into(holder.iv_account_pager_item_photo);
                }else{
                    holder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
                }
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
                if (!TextUtils.isEmpty(childItemBean.getPhotoResUrl())) {
                    //如果用户设置了图片,就在改变为之后的ImageView中显示该图片
                    holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                   // Glide.with(mActivity).load(childItemBean.getPhotoResUrl()).into(holder.iv_account_pager_item_photo);
                    Glide.with(mActivity)
                            .load(childItemBean.getPhotoResUrl())
                            .listener(mRequestListener)//配置监听器
                            .into(holder.iv_account_pager_item_photo);

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
                if (!TextUtils.isEmpty(childItemBean.getPhotoResUrl())) {
                    holder.iv_account_pager_item_photo.setVisibility(View.VISIBLE);
                    //Glide.with(mActivity).load(childItemBean.getPhotoResUrl()).into(holder.iv_account_pager_item_photo);
                    Glide.with(mActivity)
                            .load(childItemBean.getPhotoResUrl())
                            .listener(mRequestListener)//配置监听器
                            .into(holder.iv_account_pager_item_photo);
                }else{
                    holder.iv_account_pager_item_photo.setVisibility(View.INVISIBLE);
                }
                holder.tv_account_pager_word_describe.setText(childItemBean.getItemDescribe());
                holder.tv_account_pager_how_much.setText(childItemBean.getHowmuch());
            }
        }
    }


    /**
     * 该方法用于去图库获取图片
     */
    private void toGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        mActivity.startActivityForResult(intent,PHOTO_REQUEST_GALLERY_FROM_ACCOUNT);
    }

    /**
     * 该函数用于调用系统的相机，并将拍好的照片传回来
     */
    private void toCamera() {

/*        String path = Environment.getExternalStorageDirectory() + "/MyAccount/";
        String fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }*/
       /* new DateFormat();
        fileName= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
        photoUri =  Uri.fromFile(new File(path + fileName));
        Log.i("wwwwwwww","使用相机前  uri="+photoUri);*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        mActivity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA_FROM_ACCOUNT);

    }

    private RequestListener<String, GlideDrawable> mRequestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            //显示错误信息
            Log.w("haha", "onException: ", e);
            //打印请求URL
            Log.d("haha", "onException: " + model);
            //打印请求是否还在进行
            Log.d("haha", "onException: " + target.getRequest().isRunning());
            return true;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };
    class TmpHolder{
        TextView income;
        TextView outcome;
    }

}
