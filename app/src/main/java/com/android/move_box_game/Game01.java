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

public class Game01 extends Activity implements View.OnClickListener {

    private String string = "map01";
    private TopBar topBar;
    private Button reSet;
    private GameView gameView;
    private Button back;

    private MySqLiteHelper helper;

    int[][] map01 = { //0是空格，1是墙壁，2是旗子(7,3)(8,3)(3,4)(3,5)，3是箱子，4是人(6,5)
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 0, 2, 2, 3, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 3, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 3, 4, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 2, 3, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] oldMap = {  //最初的地图
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 0, 2, 2, 3, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 3, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 0, 3, 4, 1, 0, 1, 0, 0, 0},
            {0, 0, 1, 2, 3, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game01);
        //getMap();
        Init();
        topBar.setTitle("第一关");
        topBar.setOnTopBarListener(new TopBar.TopBarOnClickListener() {
            @Override
            public void leftClick() {
                Intent intent = new Intent(Game01.this, List.class);
                startActivity(intent);
            }

            @Override
            public void rightCilck() {
                Toast.makeText(Game01.this, "NoMore", Toast.LENGTH_SHORT).show();
            }
        });
        gameView.setPassListener(new GameView.PassListener() {
            @Override
            public void exit() {
                Intent intent = new Intent(Game01.this, move_box_game.class);
                startActivity(intent);
            }

            @Override
            public void next() {
                Intent intent1 = new Intent(Game01.this, Game02.class);
                startActivity(intent1);
            }
        });
        back.setOnClickListener(this);
        reSet.setOnClickListener(this);

    }
    protected void onStart() {
        super.onStart();
        getMap();
    }

    //onResume()方法在Activity即将与用户交互时调用z
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
    public void saveMap() {
        SharedPreferences preferences = getSharedPreferences("myMap", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        string = "map01";
        int[][] currentMap = gameView.map;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                string = string + i + j;
                editor.putInt(string, currentMap[i][j]);
                if (i == 0 && j == 0)
                    Log.i("tag", string);
            }
        }
        editor.commit();

    }

    //用ShanredPerference进行数据的提取并设置地图
    public void getMap() {
        SharedPreferences preferences = getSharedPreferences("myMap", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        string = "map01";
        int i = 0;
        int j = 0;
        String s = string + i + j;
        int k = preferences.getInt(s, 10);
        if (k != 10) {
            int[][] map = new int[12][12];
            for (i = 0; i < 12; i++) {
                for (j = 0; j < 12; j++) {
                    string = string + i + j;
                    map[i][j] = preferences.getInt(string, 0);
                    if (i == 0 && j == 1)
                        Log.i("tag", string);
                }
            }
            gameView.setNewMap(map);
        }
    }

    public void Init() {
        topBar = (TopBar) findViewById(R.id.topBar);
        reSet = (Button) findViewById(R.id.reSet);
        gameView = (GameView) findViewById(R.id.gameView);
        back = (Button) findViewById(R.id.back);
    }

/*    public int[][] getSavedMap(){

    }

    public void saveMap(){
        helper = DBManager.getInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i = 0;i<12;i++){
            for(int j=0;j<12;j++){
                String sql = "insert into"+Constant.TABLE_NAME+"value("+gameView.map[i][j]+")";
                DBManager.execSQL(db,sql);
            }
        }
        db.close();
    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gameView.getBeforeMap();
                    }
                });
                thread1.start();
                break;
            case R.id.reSet:
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        gameView.reSet(oldMap);
                    }
                });
                thread2.start();
                break;
        }
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
