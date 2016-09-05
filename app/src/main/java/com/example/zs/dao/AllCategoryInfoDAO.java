package com.example.zs.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zs.dataBase.AllCategoryInfoDB;

/**
 * Created by wuqi on 2016/9/5 0005.
 * allCategoryInfo数据库的查询接口,
 * 因allCategoryInfo为初始化数据，只支持查询，不能修改
 */
public class AllCategoryInfoDAO {
    public Context ctx;
    private final SQLiteDatabase db;

    /**
     * 构造器中得到SQLiteDatabase实例
     * @param ctx
     */
    public AllCategoryInfoDAO(Context ctx) {
        this.ctx = ctx;
        //数据操作的准备工作
        AllCategoryInfoDB initCategory = new AllCategoryInfoDB(ctx, "initCategory", null, 1);
        db = initCategory.getReadableDatabase();
    }
    //查询接口

}
