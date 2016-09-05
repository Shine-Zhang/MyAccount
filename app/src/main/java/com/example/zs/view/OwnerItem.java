package com.example.zs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zs.myaccount.R;

/**
 * Created by 钟云婷 on 2016/9/3.
 *
 * 该类重写了RelativeLayout，实现自定义控件该类重写了RelativeLayout的布局初始化
 */
public class OwnerItem extends RelativeLayout {
    private static final String TAG = "OwnerItem";
    private Context ctx;//整个全局域
    private String ownerItemTitle;
    private Drawable ownerItemIcon;

    private MyOwnerItemOnClickListener OwnerItemOnClickListener;

    public OwnerItem(Context context) {
        super(context);
        ctx = context;
        init(null);
    }

    public OwnerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init(attrs);
    }

    /*
    * 此方法用于初始化“我的”页面的*/
    private void init(AttributeSet attrs) {

        if(attrs!=null){//当attrs不为空，则取出属性值
            //获取在引用布局时在attr.xml文件中定义的属性值
            TypedArray typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.OwnerPagerItem);
            ownerItemTitle = typedArray.getString(R.styleable.OwnerPagerItem_ownerItemTitle);
            ownerItemIcon = typedArray.getDrawable(R.styleable.OwnerPagerItem_ownerItemIcon);
            typedArray.recycle();//回收TypedArray，以便后面重用
        }

        //使用单个Item的布局文件填充view
        View view = View.inflate(ctx, R.layout.item_ownerpager,null);
        addView(view);
        //将获取到的值设置到相应位置
        ImageView iv_ownerpager_itemicon = (ImageView) view.findViewById(R.id.iv_ownerpager_itemicon);
        TextView tv_ownerpager_itemtitle = (TextView) view.findViewById(R.id.tv_ownerpager_itemtitle);

        iv_ownerpager_itemicon.setImageDrawable(ownerItemIcon);
        tv_ownerpager_itemtitle.setText(ownerItemTitle);

        //实现OwnerItem的点击
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onclick");
                //判断Item 有被设置自定义的监听，则调用caiiback函数
                if(OwnerItemOnClickListener!=null){
                    OwnerItemOnClickListener.onItemClick();
                }

            }
        });
    }

    //创建点击事件的回调接口
    public interface MyOwnerItemOnClickListener{
        void onItemClick();
    }

    //声明接口对象
    public void setMyOwnerItemOnClickListener(MyOwnerItemOnClickListener listener){

         OwnerItemOnClickListener = listener;
    }


}
