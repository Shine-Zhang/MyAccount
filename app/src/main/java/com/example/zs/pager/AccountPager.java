package com.example.zs.pager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.zs.myaccount.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *
 * 该类维护了整个个明细标签页的显示页面布局以及显示的内容
 * Created by Administrator on 2016/9/3 0003.
 * @author  shine-zhang
 */
public class AccountPager extends BasePager{

    @ViewInject(R.id.lv_accountpager_showaccounts)
    private ListView mLvShowAccounts;

    private  String[] testDataSource = new String[]{"徐凤年","邓太阿","轩辕青锋","南宫仆射","姜娰","洛阳","孙寅","苟有方","陈芝报",
                                                        "楼荒","与新郎","宫半阙","李淳罡"};
    /**
     * 通过填充布局文件的方式，从而得到整个明细r页面的view以及其中的的各个子控件
     * @return 返回通过布局文件填充得到的View对象
     */
    @Override
    public View initView() {
        return null;
    }


    /**
     * 初始化明细页面所要显示的数据，也就是实际所需要现实的内容
     */
    @Override
    public void initData() {

    }

    /**
     * mLvShowAccounts的适配器
     */
    class AccountAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return testDataSource.length;
        }

        @Override
        public String getItem(int i) {
            return testDataSource[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView!=null){


            }
            return convertView;
        }

    }

    class ViewHolder{

    }
}
