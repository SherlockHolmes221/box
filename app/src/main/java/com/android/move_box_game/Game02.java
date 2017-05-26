package com.android.move_box_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by quxia on 2017/4/30.
 */
public class Game02 extends Activity{
    private String string = "map02";
    private TopBar topBar;
    private Button reSet;
    private GameView gameView;
    private Button back;


    int[][]map02 ={ //0是空格，1是墙壁，2是旗子(7,3)(8,3)(3,4)(3,5)，3是箱子，4是人(6,5)
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,1,1,1,0,0,0},
            {0,0,1,1,0,0,0,0,1,0,0,0},
            {0,0,1,0,3,0,3,3,0,1,0,0},
            {0,0,1,2,0,2,0,2,0,1,0,0},
            {0,0,1,0,3,0,0,0,2,1,0,0},
            {0,0,1,1,1,0,0,4,1,1,0,0},
            {0,0,0,0,1,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},

    };

    int[][]oldMap ={ //0是空格，1是墙壁，2是旗子(7,3)(8,3)(3,4)(3,5)，3是箱子，4是人(6,5)
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,1,1,1,0,0,0},
            {0,0,1,1,0,0,0,0,1,0,0,0},
            {0,0,1,0,3,0,3,3,0,1,0,0},
            {0,0,1,2,0,2,0,2,0,1,0,0},
            {0,0,1,0,3,0,0,0,2,1,0,0},
            {0,0,1,1,1,0,0,4,1,1,0,0},
            {0,0,0,0,1,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},

    };


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.game01);
        Init();
        topBar.setTitle("第二关");
        gameView.setNewMap(map02);



        topBar.setOnTopBarListener(new TopBar.TopBarOnClickListener() {
            @Override
            public void leftClick() {
                Intent intent = new Intent(Game02.this,List.class);
                startActivity(intent);
            }

            @Override
            public void rightCilck() {
                Toast.makeText(Game02.this,"NoMore",Toast.LENGTH_SHORT).show();
            }
        });

        gameView.setPassListener(new GameView.PassListener() {
            @Override
            public void exit() {
                Intent intent = new Intent(Game02.this,move_box_game.class);
                startActivity(intent);
            }

            @Override
            public void next() {
                Toast.makeText(Game02.this,"尚未开发，敬请期待",Toast.LENGTH_SHORT).show();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                reSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameView.reSet(oldMap);
                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameView.getBeforeMap();
                    }
                });
            }
        });

        thread2.start();

    /*    //打开或创建move_box.db数据库
        SQLiteDatabase db = openOrCreateDatabase("move_box.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP View IF EXISTS move_box");
        db.execSQL("CREATE View move_box");
        //插入数据
        db.execSQL("INSERT INTO GameView VALUES (NULL, ?, ?)", new Object[]{gameView});
        //关闭当前数据库
        db.close();*/
    }

    protected void onStart() {
        super.onStart();
        getMap();
    }

    //onResume()方法在Activity即将与用户交互时调用
    protected void onResume() {
        super.onResume();
        getMap();
    }

    //当Activity从stopped状态启动时会调用onRestart(),后面总是调用onStart()方法
    protected void onRestart() {
        super.onRestart();
        getMap();
    }

    protected void onPause() {
        super.onPause();
        saveMap();
    }

    protected void onStop() {
        super.onStop();
        saveMap();
    }
    protected void onDestroy() {
        super.onDestroy();
        saveMap();
    }


    //用ShanredPerference进行数据的存储
    public void saveMap(){
        SharedPreferences preferences  =getSharedPreferences("myMap02",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        string = "map02";
        int[][] currentMap = gameView.map;
        for(int i = 0;i< 12;i++){
            for(int j = 0;j<12;j++){
                string = string+i+j;
                editor.putInt(string,currentMap[i][j]);
                if(i==0 && j==0)
                    Log.i("tag",string);
            }
        }
        editor.commit();

    }

    //用ShanredPerference进行数据的提取并设置地图
    public void getMap(){
        SharedPreferences preferences  =getSharedPreferences("myMap02",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        string = "map02";
        int i = 0;
        int j = 0;
        String s = string+i+j;
        int k = preferences.getInt(s,10);
        if(k!=10){
            int[][]map = new int[12][12];
            for( i = 0;i< 12;i++){
                for(j = 0;j<12;j++) {
                    string = string+i+j;
                    map[i][j] = preferences.getInt(string,0);
                    if(i==0 && j==1)
                        Log.i("tag",string);
                }
            }
            gameView.setNewMap(map);
        }
    }
    public void Init(){
        topBar = (TopBar) findViewById(R.id.topBar);
        reSet  =(Button)findViewById(R.id.reSet);
        gameView = (GameView)findViewById(R.id.gameView);
        back=(Button)findViewById(R.id.back);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("信息提示")
                .setMessage("确定要退出游戏界面吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create();
        dialog.show();
    }

}
