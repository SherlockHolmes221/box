package com.android.move_box_game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqLiteHelper extends SQLiteOpenHelper {

    public MySqLiteHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null,Constant.DATABASE_VERSION);
        Log.e("tag","onCreate");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String sql = "create table"+Constant.TABLE_NAME+"("+Constant.id+"Integer primary key,"+Constant.MAP+"Integer)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.e("tag","onUpgrade");
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行
    }


}