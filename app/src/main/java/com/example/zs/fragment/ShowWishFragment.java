package com.example.zs.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zs.myaccount.R;

/**
 * Created by 韦宇 on 2016/9/3.
 */
public class ShowWishFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view_show = View.inflate(getActivity(), R.layout.fragment_wish_show, null);
        RecyclerView rcv_container = (RecyclerView) view_show.findViewById(R.id.rcv_container);

        return view_show;

    }
}
