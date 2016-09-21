package com.example.zs.pager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zs.application.MyAplication;
import com.example.zs.bean.PayoutContentInfo;
import com.example.zs.bean.WishInfo;
import com.example.zs.dao.CompleteWishDAO;
import com.example.zs.dao.IncomeContentDAO;
import com.example.zs.dao.OnGoingWishDao;
import com.example.zs.dao.PayOutContentDAO;
import com.example.zs.myaccount.AddWishActivity;
import com.example.zs.myaccount.CompleteWishActivity;
import com.example.zs.myaccount.R;
import com.example.zs.utils.ScreenUtils;
import com.example.zs.utils.ShowPopupWindowUtils;
import com.example.zs.view.CircleImageView;
import com.example.zs.view.RoundProgressBar;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 韦宇 on 2016/9/2.
 * 该类实现愿望页面的业务逻辑。
 * 主要展示用户的愿望，使用的布局文件是 wishpager.xml
 * @author 韦宇
 */

public class WishPager extends BasePager {

    private static final String TAG = "WishPager";
    private int wishcount;
    private View view_wishpager;
    //设置两个常量，页面的标识
    public static final String FROMADD = "add";
    public static final String FROMEDIT = "edit";

    private MyOnGoingRecyclerViewAdapter myAdapter;
    private RecyclerView rcv_wishpager_wishes;
    private PopupWindow popupwindow_showwishdetail;
    private PopupWindow popupwindow_showcompletedialog;
    private int allOnGoingWishNumber;
    private List<WishInfo> allOnGoingWishInfo;
    private int allCompleteWishNumber;
    private Float availableWishFund;
    private OnGoingWishDao onGoingWishDAO;
    private String totalWishFund;
    private CompleteWishDAO completeWishDAO;
    private LinearLayout ll_showwish_ongoingwishes;
    private LinearLayout ll_showwish_noongoingwishes;
    private LinearLayoutManager layoutManager;
    private TextView tv_showwish_wishfund;


    /**
     * 该方法用于初始化WishPager的页面
     * @return 返回值为页面要显示的view
     */
    public WishPager(Activity activity){
        super(activity);

    }
    @Override
    public View initView() {

        //获取数据库中愿望的数目
        //未完成愿望的数目和详细信息
        onGoingWishDAO = new OnGoingWishDao(mActivity);
        allOnGoingWishNumber = onGoingWishDAO.getAllOnGoingWishNumber();
        //已完成愿望的数目
        completeWishDAO = new CompleteWishDAO(mActivity);
        allCompleteWishNumber = completeWishDAO.getAllCompleteWishNumber();

       /* if(allOnGoingWishNumber!=0) {
            allOnGoingWishInfo = onGoingWishDAO.getAllOnGoingWishInfo();
        }*/
        //计算可用的愿望资金
        totalWishFund = calculateWishFund();
        Log.i("yyyyyyyyyy","totalWishFund" +totalWishFund);

        //判断数据库中的用户愿望数目
        if(allOnGoingWishNumber==0 && allCompleteWishNumber==0){
            //用户愿望条目为0 ，显示愿望空白页
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
            //用户愿望条目不为0 ，显示愿望列表页
            //加载布局文件wishpager_showwish.xml
            view_wishpager = View.inflate(mActivity, R.layout.wishpager_showwish, null);
            ll_showwish_ongoingwishes = (LinearLayout) view_wishpager.findViewById(R.id.ll_showwish_ongoingwishes);
            ll_showwish_noongoingwishes = (LinearLayout) view_wishpager.findViewById(R.id.ll_showwish_noongoingwishes);
            tv_showwish_wishfund = (TextView) view_wishpager.findViewById(R.id.tv_showwish_wishfund);
            //显示总的愿望基金
            tv_showwish_wishfund.setText(totalWishFund);

            //点击“添加”，跳转到添加愿望的页面
            View tv_showwish_addwish = view_wishpager.findViewById(R.id.tv_showwish_addwish);
            tv_showwish_addwish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //去添加愿望的页面添加愿望
                    toAddWish();
                }
            });

            //判断是否有正在进行的愿望，有，显示未完成愿望列表；没有，显示当前没有未完成愿望。
            if(allOnGoingWishNumber!=0){

                //有未完成的愿望
                ll_showwish_ongoingwishes.setVisibility(View.VISIBLE);
                ll_showwish_noongoingwishes.setVisibility(View.INVISIBLE);

              /*  //拿到RecyclerView
                rcv_wishpager_wishes = (RecyclerView) view_wishpager.findViewById(R.id.rcv_wishpager_wishes);
                //创建默认的线性LayoutManager
                layoutManager = new LinearLayoutManager(mActivity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                //rcv_wishpager_wishes.setLayoutManager(layoutManager);
                //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                rcv_wishpager_wishes.setHasFixedSize(true);*/
                //initData();


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

            }

        }
        return view_wishpager;
    }

    @Override
    public void initData() {
        //计算可用的愿望资金
        totalWishFund = calculateWishFund();

        //已完成愿望的数目
        completeWishDAO = new CompleteWishDAO(mActivity);
        allCompleteWishNumber = completeWishDAO.getAllCompleteWishNumber();

        //当数据库变化的时候重新更新内存中的数据,当数据库变化的时候通知内容观察者数据库变化了,然后在内容观察者中去更新最新的数据
/*        Uri uri = Uri.parse("content://com.example.zs.dao.ongoingwish.changed");
        //notifyForDescendents:匹配规则,true:精确匹配  false:模糊匹配
        mActivity.getContentResolver().registerContentObserver(uri, true, new ContentObserver(null) {
            public void onChange(boolean selfChange) {
                Log.i("wwwwwwwwww","wishpager onchange ----"+selfChange);
                //更新数据
                allOnGoingWishNumber = onGoingWishDAO.getAllOnGoingWishNumber();
                if(allOnGoingWishNumber!=0) {
                    allOnGoingWishInfo = onGoingWishDAO.getAllOnGoingWishInfo();
                }
                initView();
            };
        });*/

        if(allOnGoingWishNumber!=0){

            //显示总的愿望基金
            tv_showwish_wishfund.setText(totalWishFund);

            ll_showwish_ongoingwishes.setVisibility(View.VISIBLE);
            ll_showwish_noongoingwishes.setVisibility(View.INVISIBLE);

            allOnGoingWishInfo = onGoingWishDAO.getAllOnGoingWishInfo();
            Log.i("wwwwwwww"," initdata() allOnGoingWishInfo="+allOnGoingWishInfo.toString());

            availableWishFund = Float.valueOf(totalWishFund);
            Log.i("wwwwwwww"," initdata() availableWishFund="+availableWishFund);

            for(int i=0;i<allOnGoingWishNumber;i++){
                Float infowishfund = Float.valueOf(allOnGoingWishInfo.get(i).wishFund);
                Log.i("wwwwww","chusheshi initdata()  infowishfund="+infowishfund);

                if(availableWishFund>infowishfund){
                    allOnGoingWishInfo.get(i).setProcess(100);
                    availableWishFund -= infowishfund;
                    Log.i("wwwwww","availableWishFund>infowishfund"+allOnGoingWishInfo.get(i).process);

                }else if(availableWishFund<infowishfund && availableWishFund>0){
                    Log.i("wwwwww","availableWishFund<infowishfund && availableWishFund>0   "+availableWishFund/infowishfund);

                    allOnGoingWishInfo.get(i).setProcess((int) (availableWishFund/infowishfund*100));
                    availableWishFund = 0f;
                    Log.i("wwwwww","availableWishFund<infowishfund && availableWishFund>0"+allOnGoingWishInfo.get(i).process);

                }else{
                    allOnGoingWishInfo.get(i).setProcess(0);
                    Log.i("wwwwww","else"+allOnGoingWishInfo.get(i).process);

                }
                Log.i("wwwwww","chusheshi initdata()  process="+allOnGoingWishInfo.get(i).process);
            }
            initRecyclerViewAdapter();
        }
    }

    private void initRecyclerViewAdapter() {
        //拿到RecyclerView
        rcv_wishpager_wishes = (RecyclerView) view_wishpager.findViewById(R.id.rcv_wishpager_wishes);
        //创建默认的线性LayoutManager
        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rcv_wishpager_wishes.setHasFixedSize(true);
        //初始化自定义的适配器
        myAdapter = new MyOnGoingRecyclerViewAdapter(allOnGoingWishInfo);
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        //为RecyclerView添加FooterView
        View view_footer = layoutInflater.inflate(R.layout.item_wish_footer, null);
        myAdapter.addFootView(view_footer);
        rcv_wishpager_wishes.setLayoutManager(layoutManager);
        //为rcv_wishpager_wishes设置适配器
        rcv_wishpager_wishes.setAdapter(myAdapter);

        //给拿到RecyclerView添加条目点击事件
        rcv_wishpager_wishes.addOnItemTouchListener(new OnItemTouchListener(rcv_wishpager_wishes) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Log.i(TAG, "click"+vh.itemView+" "+vh.getItemViewType());
                //获取item所在的view
                final View viewParent = (View) vh.itemView.getParent();
                //获取item所在的位置
                final int adapterPosition = vh.getAdapterPosition();

                //如果是RecyclerView的最后一条，即“已完成的愿望”
                if(adapterPosition==allOnGoingWishInfo.size()){
                    //跳转到已完成的愿望页面
                    Log.i("wwwww","数据库中有已完成愿望，跳转到已完成愿望页面");
                    TextView tv_itemwishfooter_completewishes = (TextView) vh.itemView.findViewById(R.id.tv_itemwishfooter_completewishes);
                    tv_itemwishfooter_completewishes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mActivity.startActivity(new Intent(mActivity,CompleteWishActivity.class));
                        }
                    });
                }else {
                    //获取相应的item的具体信息
                    //愿望标题
                    String title = allOnGoingWishInfo.get(adapterPosition).wishTitle.toString();
                    //愿望备注
                    final String description = allOnGoingWishInfo.get(adapterPosition).wishDescription.toString();
                    //愿望资金
                    final String wishfund = allOnGoingWishInfo.get(adapterPosition).wishFund;
                    //愿望照片的uri
                    final String photoUri = allOnGoingWishInfo.get(adapterPosition).wishphotoUri;
                    Log.i(TAG,"title="+title+"description="+description+"wishfund="+wishfund+"photoUri="+photoUri);
                    //Log.i(TAG,"Integer.parseInt(wishfund)="+Float.parseFloat(wishfund));

                    //id,process
                    final int wishid = allOnGoingWishInfo.get(adapterPosition).getWishid();
                    final int process = allOnGoingWishInfo.get(adapterPosition).getProcess();
                    //将item的信息封装，方便传给下一个页面
                    final WishInfo info = new WishInfo(wishid,title,description,wishfund,photoUri,process);

                    //如果完成的话，需要封装info
                    //获取当前的日期
                    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    //获取当前日期:
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH);
                    final int day = calendar.get(Calendar.DATE);
                    final WishInfo compinfo = new WishInfo(year,month,day,title,description,photoUri);

                    //寻找控件
                    RelativeLayout rl_itemwish_top = (RelativeLayout) vh.itemView.findViewById(R.id.rl_itemwish_top);
                    PercentRelativeLayout prl_itemwish_middle = (PercentRelativeLayout) vh.itemView.findViewById(R.id.prl_itemwish_middle);
                    View view_itemview_line = vh.itemView.findViewById(R.id.view_itemview_line);
                    ImageView iv_itemwish_delete = (ImageView) vh.itemView.findViewById(R.id.iv_itemwish_delete);
                    TextView tv_itemwish_complete = (TextView) vh.itemView.findViewById(R.id.tv_itemwish_complete);

                    //给相应控件设置点击事件
                    rl_itemwish_top.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //显示愿望详情
                            ShowPopupWindowUtils.showWishDetail(mActivity,info,viewParent);
                        }
                    });
                    prl_itemwish_middle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //显示愿望详情
                            ShowPopupWindowUtils.showWishDetail(mActivity,info,viewParent);
                        }
                    });
                    view_itemview_line.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //显示愿望详情
                            //showWishDetail(info,viewParent, adapterPosition);
                            ShowPopupWindowUtils.showWishDetail(mActivity,info,viewParent);

                        }
                    });
                    iv_itemwish_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //提示用户是否删除愿望
                            showConfirmDeleteDialog(wishid,adapterPosition);
                        }
                    });
                    tv_itemwish_complete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(process==100){
                                //进度条已满，
                                //从未完成愿望列表中删除此愿望，加入到已完成愿望的列表中
                                onGoingWishDAO.deleteOnGoingWishInfo(wishid);
                                completeWishDAO.addCompleteWishInfo(compinfo);
                                //加入到支出数据表
                                PayoutContentInfo payoutContentInfo = new PayoutContentInfo(R.drawable.ic_yiban_default,year,month,day,"梦想基金",wishfund,description,photoUri);
                                PayOutContentDAO payOutContentDAO = new PayOutContentDAO(mActivity);
                                payOutContentDAO.addPayoutContentToDB(payoutContentInfo);
                                //从列表中移除数据源
                                allOnGoingWishInfo.remove(adapterPosition);
                                allOnGoingWishNumber--;
                                //刷新数据列表
                                myAdapter.notifyDataSetChanged();
                                if(allOnGoingWishNumber==0){
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
                                }
                                //刷新页面
                                MyAplication application = (MyAplication) mActivity.getApplication();
                                if(application.getWishPager()!=null){
                                    //application.getWishPager().initData();
                                }
                                //弹出完成页面
                                showCompleteDialog(view);

                                if(popupwindow_showcompletedialog.isShowing()){
                                    //popupwindow显示时屏幕透明度变暗
                                    ScreenUtils.backgroundAlpha(mActivity,0.4f);
                                }else{
                                    //popupwindow不显示时屏幕透明度恢复正常
                                    ScreenUtils.backgroundAlpha(mActivity,1.0f);
                                }
                            }else{
                                //提示愿望没完成
                                showNoCompleteDialog(view);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 当愿望没有完成时，点击“完成”提示愿望没完成
     * @param view
     */
    private void showNoCompleteDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setMessage("梦想还未实现，不能完成哟～～")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    /**
     * 显示完成愿望的弹框
     * @param view
     */
    private void showCompleteDialog(View view) {
        //初始化popupwindow
        popupwindow_showcompletedialog = new PopupWindow();
        //加载popupwindow的界面
        View view_showcomplete = View.inflate(mActivity, R.layout.popupwindow_wish_complete, null);
        ImageView iv_popupwindowwishcomplete_close = (ImageView) view_showcomplete.findViewById(R.id.iv_popupwindowwishcomplete_close);
        Button bt_popupwindowwishcomplete_chcekcomplwish = (Button) view_showcomplete.findViewById(R.id.bt_popupwindowwishcomplete_chcekcomplwish);

        //获得焦点
        popupwindow_showcompletedialog.setFocusable(true);
        popupwindow_showcompletedialog.setBackgroundDrawable(new BitmapDrawable());
        //将popup_view部署到popupWindow上
        popupwindow_showcompletedialog.setContentView(view_showcomplete);
        //设置popupWindow的宽高（必须要设置）
        popupwindow_showcompletedialog.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow_showcompletedialog.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置popupwindow显示的位置
        popupwindow_showcompletedialog.showAtLocation(view,Gravity.CENTER,0,0);

        //右上角的关闭。点击时关闭popupwindow
        iv_popupwindowwishcomplete_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popupwindow消失
                popupwindow_showcompletedialog.dismiss();
            }
        });

        //“查看已完成那愿望”按钮
        bt_popupwindowwishcomplete_chcekcomplwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到已完成愿望的页面
                popupwindow_showcompletedialog.dismiss();
                mActivity.startActivity(new Intent(mActivity,CompleteWishActivity.class));
            }
        });

        //监听popupwindow，消失时使屏幕恢复原来的透明度
        popupwindow_showcompletedialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复屏幕透明度
                ScreenUtils.backgroundAlpha(mActivity,1.0f);
            }
        });
    }

    /**
     * 显示是否确认删除愿望的弹窗
     * @param position
     * @param wishid
     */
    private void showConfirmDeleteDialog(final int position, final int wishid) {
        new AlertDialog.Builder(mActivity)
                .setTitle("坚持梦想吧")
                .setMessage("坚持一下更好哟～～")
                .setPositiveButton("坚持", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //删除愿望
                        deleteWish(wishid,position);

                    }
                })
                .show();
    }

    /**
     * 删除正在进行的愿望
     * @param position
     * @param wishid
     */
    private void deleteWish(int position, int wishid) {
        //从数据库中删除
        onGoingWishDAO.deleteOnGoingWishInfo(wishid);
        //从列表中移除数据源
        allOnGoingWishInfo.remove(position);
        allOnGoingWishNumber--;
        //刷新数据列表
        if(allOnGoingWishNumber==0){
            ll_showwish_ongoingwishes.setVisibility(View.INVISIBLE);
            ll_showwish_noongoingwishes.setVisibility(View.VISIBLE);
        }
        myAdapter.notifyDataSetChanged();
        /*//刷新页面
        MyAplication application = (MyAplication) mActivity.getApplication();
        if(application.getWishPager()!=null){
            application.getWishPager().initData();
        }*/
    }


    /**
     *该方法用户跳转到用户添加愿望的页面AddWishAvtivity
     */
    private void toAddWish() {
        //带个数据过去表示自己是从哪跳过去的
        Intent intent = new Intent(mActivity,AddWishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("from",FROMADD);
        intent.putExtras(bundle);
        mActivity.startActivityForResult(intent,111);
        Log.i("wwwwwwww","跳转到添加愿望页面---startActivityForResult(intent,111)");
    }

    /**
     * 该方法用于计算愿望资金
     * 愿望资金的计算公式为：（月收入-月支出）/本月天数  *本月已过天数
     * @return 返回当前日期的愿望资金
     */
    private String calculateWishFund() {
        //获取当前的日期
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //获取当前日期:
        int month = calendar.get(Calendar.MONTH)+1;
        int day =  calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(TAG,"day = "+day);
        //获取当前月最后一天,即计算当前月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = dateFormater.format(calendar.getTime());
        Log.i(TAG,"last="+last);  //last=2016-09-30
        //本月天数
        String substring = last.substring(8,10);
        int monthdays = Integer.parseInt(substring);
        Log.i(TAG,"monthday="+monthdays);

        //获取当前的月收入、月支出
        float income = 0;
        float expense = 0;
        IncomeContentDAO incomeContentDAO = new IncomeContentDAO(mActivity);
        String income_month= incomeContentDAO.getIncomeForMonth(month);
        Log.i(TAG,"monthincome="+income_month);
        if(income_month!=null){
            income = Float.parseFloat(income_month);
        }

        PayOutContentDAO payOutContentDAO = new PayOutContentDAO(mActivity);
        String expense_month = payOutContentDAO.getExpenseForMonth(month);
        Log.i(TAG,"monthexpense="+expense_month);
        if(income_month!=null){
            expense = Float.parseFloat(income_month);
        }

        //计算愿望资金
        /*import java.text.DecimalFormat;
        DecimalFormat df = new DecimalFormat("###.00");
        System.out.println(df.format(double_value));
        这样就是保留小数点后两位小数,如果想保留三位,则为
        DecimalFormat df = new DecimalFormat("###.000");*/
        float result = (income-expense)/monthdays*(day-1);
        DecimalFormat df = new DecimalFormat("###0.00");
        String wishfund = df.format(result);

        return wishfund;
    }

    /**
     * 给RecyclerView设置footer
     * @param view
     */
    /*private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(mActivity).inflate(R.layout.item_wish_footer, view, false);
        myAdapter.setFooterView(footer);
    }*/

}

/**
 * 该类为RecyclerView的Adapter
 * RecyclerView与ListView类似，都需要Adapter
 */
class MyOnGoingRecyclerViewAdapter extends RecyclerView.Adapter<MyOnGoingRecyclerViewAdapter.MyViewHolder>{


    private View mFooterView;
    public List<WishInfo> wishInfos;
    //item类型
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    private int footViewSize = 0;
    private boolean isAddFoot = false;


    //构造方法
    public MyOnGoingRecyclerViewAdapter(List<WishInfo> wishInfos) {
        this.wishInfos = wishInfos;
    }

    public void addFootView(View view) {
        mFooterView = view;
        footViewSize = 1;
        isAddFoot = true;
    }


    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (footViewSize == 0){
            return ITEM_TYPE_CONTENT;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return ITEM_TYPE_BOTTOM;
        }
        return ITEM_TYPE_CONTENT;
    }

    //创建新View，被LayoutManager所调用
    //如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        switch (viewType) {
            case ITEM_TYPE_CONTENT:
                view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wish,viewGroup,false);
                break;

            case ITEM_TYPE_BOTTOM:
                view = mFooterView;
                break;
        }
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    //将数据与界面进行绑定的操作
    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

        if(getItemViewType(position) == ITEM_TYPE_CONTENT){

            if(viewHolder instanceof MyViewHolder) {
                WishInfo wishInfo = wishInfos.get(position);
                Log.i("wwwww"," item wishinfo = "+wishInfo.toString());
                //愿望标题
                viewHolder.tv_itemwish_title.setText(wishInfo.wishTitle);
                //愿望备注
                if(wishInfo.wishDescription.isEmpty()){
                    viewHolder.tv_itemwish_description.setVisibility(View.GONE);
                }else {
                    viewHolder.tv_itemwish_description.setVisibility(View.VISIBLE);
                    viewHolder.tv_itemwish_description.setText(wishInfo.wishDescription);
                }
                //愿望资金
                viewHolder.tv_itemwish_amount.setText(wishInfo.wishFund);
                //愿望图片
                Log.i("wwwww","Item photouri="+wishInfo.wishphotoUri);
                if(wishInfo.wishphotoUri.equals("null") || wishInfo.wishphotoUri.equals("0") || wishInfo.wishphotoUri.isEmpty()){
                    Log.i("wwww","Item photoset null");
                    viewHolder.iv_itemwish_pic.setImageResource(R.drawable.ic_default_wish);
                }else {
                    viewHolder.iv_itemwish_pic.setImageURI(Uri.parse(wishInfo.wishphotoUri));
                }
                //愿望进度文本
                viewHolder.tv_itemwish_progresstip.setText("愿望进度"+wishInfo.process+"%");
                //愿望进度条
                viewHolder.pb_itemwish_progress.setMax(100);
                viewHolder.pb_itemwish_progress.setProgress(wishInfo.process);
                Log.i("wwww","item process="+wishInfo.process);
                viewHolder.pb_itemwish_progress.getProgress();

                //“完成字体颜色”，进度100%，为蓝色，否则为灰色。
                if(wishInfo.process==100){
                    viewHolder.tv_itemwish_complete.setTextColor(Color.rgb(31,185,236));
                }else{
                    viewHolder.tv_itemwish_complete.setTextColor(Color.GRAY);
                }
                return;
            }
            return;
        }else{
            return;
        }

    }

    //获取数据的数量
    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
       return wishInfos.size()+footViewSize;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_itemwish_title;
        public TextView tv_itemwish_amount;
        public TextView tv_itemwish_description;
        public ImageView iv_itemwish_pic;
        public ImageView iv_itemwish_delete;
        public TextView tv_itemwish_complete;
        public ProgressBar pb_itemwish_progress;
        public TextView tv_itemwish_progresstip;

        public MyViewHolder(View view){
            super(view);
            //如果是footerview,直接返回
            if (view == mFooterView){
                return;
            }
            tv_itemwish_title = (TextView) view.findViewById(R.id.tv_itemwish_title);
            tv_itemwish_amount =  (TextView) view.findViewById(R.id.tv_itemwish_amount);
            tv_itemwish_description = (TextView) view.findViewById(R.id.tv_itemwish_description);
            iv_itemwish_pic = (ImageView) view.findViewById(R.id.iv_itemwish_pic);
            iv_itemwish_delete = (ImageView) view.findViewById(R.id.iv_itemwish_delete);
            tv_itemwish_complete = (TextView) view.findViewById(R.id.tv_itemwish_complete);
            pb_itemwish_progress = (ProgressBar) view.findViewById(R.id.pb_itemwish_progress);
            tv_itemwish_progresstip =  (TextView) view.findViewById(R.id.tv_itemwish_progresstip);
        }
    }

}


/*class MyOnGoingRecyclerViewAdapter extends RecyclerView.Adapter<MyOnGoingRecyclerViewAdapter.MyViewHolder>{

    //item类型
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    private View mFooterView;
    public List<WishInfo> wishInfos;

    //构造方法
    public MyOnGoingRecyclerViewAdapter(List<WishInfo> wishInfos) {
        this.wishInfos = wishInfos;
    }

    //FooterView的get和set函数
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }


    *//**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     * @param position
     * @return
     *//*
    @Override
    public int getItemViewType(int position) {
        if (mFooterView == null){
            return ITEM_TYPE_CONTENT;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return ITEM_TYPE_BOTTOM;
        }
        return ITEM_TYPE_CONTENT;
    }

    //创建新View，被LayoutManager所调用
    //如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // 给ViewHolder设置布局文件
        if(mFooterView != null && viewType == ITEM_TYPE_BOTTOM){
            return new MyViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wish,viewGroup,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    //将数据与界面进行绑定的操作
    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

        if(getItemViewType(position) == ITEM_TYPE_CONTENT){

            if(viewHolder instanceof MyViewHolder) {
                WishInfo wishInfo = wishInfos.get(position);
                Log.i("wwwww"," item wishinfo = "+wishInfo.toString());
                //愿望标题
                viewHolder.tv_itemwish_title.setText(wishInfo.wishTitle);
                //愿望备注
                if(wishInfo.wishDescription.isEmpty()){
                    viewHolder.tv_itemwish_description.setVisibility(View.GONE);
                }else {
                    viewHolder.tv_itemwish_description.setVisibility(View.VISIBLE);
                    viewHolder.tv_itemwish_description.setText(wishInfo.wishDescription);
                }
                //愿望资金
                viewHolder.tv_itemwish_amount.setText(wishInfo.wishFund);
                //愿望图片
                Log.i("wwwww","Item photouri="+wishInfo.wishphotoUri);
                if(wishInfo.wishphotoUri.equals("null") || wishInfo.wishphotoUri.equals("0") || wishInfo.wishphotoUri.isEmpty()){
                    Log.i("wwww","Item photoset null");
                    viewHolder.iv_itemwish_pic.setImageResource(R.drawable.ic_default_wish);
                }else {
                    viewHolder.iv_itemwish_pic.setImageURI(Uri.parse(wishInfo.wishphotoUri));
                }
                //愿望进度文本
                viewHolder.tv_itemwish_progresstip.setText("愿望进度"+wishInfo.process+"%");
                //愿望进度条
                viewHolder.pb_itemwish_progress.setMax(100);
                viewHolder.pb_itemwish_progress.setProgress(wishInfo.process);
                Log.i("wwww","item process="+wishInfo.process);
                viewHolder.pb_itemwish_progress.getProgress();

                //“完成字体颜色”，进度100%，为蓝色，否则为灰色。
                if(wishInfo.process==100){
                    viewHolder.tv_itemwish_complete.setTextColor(Color.rgb(31,185,236));
                }else{
                    viewHolder.tv_itemwish_complete.setTextColor(Color.GRAY);
                }
                return;
            }
            return;
        }else{
            return;
        }

    }

    //获取数据的数量
    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if( mFooterView == null){
            return wishInfos.size();
        }else {
            return wishInfos.size() + 1;
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_itemwish_title;
        public TextView tv_itemwish_amount;
        public TextView tv_itemwish_description;
        public ImageView iv_itemwish_pic;
        public ImageView iv_itemwish_delete;
        public TextView tv_itemwish_complete;
        public ProgressBar pb_itemwish_progress;
        public TextView tv_itemwish_progresstip;

        public MyViewHolder(View view){
            super(view);
            //如果是footerview,直接返回
            if (view == mFooterView){
                return;
            }
            tv_itemwish_title = (TextView) view.findViewById(R.id.tv_itemwish_title);
            tv_itemwish_amount =  (TextView) view.findViewById(R.id.tv_itemwish_amount);
            tv_itemwish_description = (TextView) view.findViewById(R.id.tv_itemwish_description);
            iv_itemwish_pic = (ImageView) view.findViewById(R.id.iv_itemwish_pic);
            iv_itemwish_delete = (ImageView) view.findViewById(R.id.iv_itemwish_delete);
            tv_itemwish_complete = (TextView) view.findViewById(R.id.tv_itemwish_complete);
            pb_itemwish_progress = (ProgressBar) view.findViewById(R.id.pb_itemwish_progress);
            tv_itemwish_progresstip =  (TextView) view.findViewById(R.id.tv_itemwish_progresstip);
        }
    }

}*/


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
