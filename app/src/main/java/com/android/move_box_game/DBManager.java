package com.android.move_box_game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by quxia on 2017/4/30.
 */


public class DBManager {

    private  static MySqLiteHelper helper;



    //获取实例
    public static MySqLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySqLiteHelper(context);

        }
        return helper;
    }

    public static void execSQL(SQLiteDatabase db,String sql){
        if(db!=null){
            if(sql != null && !"".equals(sql)){
                db.execSQL(sql);
            }
        }
    }
/*
    //保存信息map
    public void saveGameView(String string,GameView gameView) {
        if (gameView!=null) {
            ContentValues contentValues = new ContentValues();
            int[][] map = gameView.getMap();
            for(int i=0;i<12;i++){
                for(int j=0;j<12;j++){
                   // String s = string+(i*12+j);
                    contentValues.put("one",map[i][j]);
                    helper.insert("move_box_game",null,contentValues);
                    contentValues.clear();
                }
            }

        }
    }

    //返回信息map
    public int[][] getSavedView(String string){
        int[][] map = null;
        Cursor cursor = helper.query("move_box_game",null,null,null,null,null,null,null);
             if (cursor.moveToFirst()) {
                    do {
                        for(int i=0;i<12;i++) {
                            for (int j = 0; j < 12; j++) {
                                map[i][j] =cursor.getInt(cursor.getColumnIndex(string));
                            }
                        }
                    } while (cursor.moveToNext());
                }
        return map;
    }
*/

}
