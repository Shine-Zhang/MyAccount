package com.example.zs.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zs.myaccount.R;

/**
 * Created by wuqi on 2016/9/6 0006.
 */
public class PayoutCategoryDB extends SQLiteOpenHelper {
    public PayoutCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PayoutCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table payoutCategoryInfo (id Integer primary key autoincrement,id_allcatrgory Integer,content var(5));");
        initData(sqLiteDatabase);
    }


    private void initData(SQLiteDatabase sqLiteDatabase) {
        int [] resource ={R.drawable.ic_yiban_default,R.drawable.ic_canyin_default,
                R.drawable.ic_jiaotong_default, R.drawable.ic_lingshi_default,
                R.drawable.ic_shuiguo_default, R.drawable.ic_jucan_default,R.drawable.ic_jiushui_default,
                R.drawable.ic_dianying_default, R.drawable.ic_yifu_default,R.drawable.ic_shegnhuo_default,
                R.drawable.ic_huafei_default,R.drawable.ic_fangzu_default, R.drawable.ic_hufu_default,
                R.drawable.ic_hongbao_default,R.drawable.ic_yaopin_default, R.drawable.ic_lvyou_default,
                R.drawable.ic_liwu_default,R.drawable.ic_yundong_default, R.drawable.ic_xuexi_default
        };
        String [] name = {"一般","餐饮","交通","零食","水果","腐败聚会","酒水饮料",
                "电影","衣服鞋包", "生活用品","话费","房租", "护肤彩妆",
                "红包","药品","旅行","礼物","运动","学习"};
        for (int i=0;i<19;i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_allcatrgory",resource[i]);
            contentValues.put("content",name[i]);
            sqLiteDatabase.insert("payoutCategoryInfo",null,contentValues);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
