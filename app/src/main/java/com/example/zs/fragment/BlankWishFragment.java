package com.example.zs.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zs.myaccount.AddWishActivity;
import com.example.zs.myaccount.R;


/**
 * Created by 韦宇 on 2016/9/3.
 * 该类用于显示还没有添加任何愿望的愿望页面
 * @author 韦宇
 */

public class BlankWishFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //给当前页面添加布局
        View view_blank = View.inflate(getActivity(), R.layout.fragment_wish_blank, null);
        //找到“添加”,设置点击事件
        TextView tv_blankwishfragment_addwish = (TextView) view_blank.findViewById(R.id.tv_blankwishfragment_addwish);
        tv_blankwishfragment_addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到添加愿望的activity
                startActivity(new Intent(getActivity(), AddWishActivity.class));
            }
        });

        return view_blank;
    }
}
