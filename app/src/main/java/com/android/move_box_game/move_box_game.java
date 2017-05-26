package com.android.move_box_game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class move_box_game extends AppCompatActivity implements View.OnClickListener {

    private Button bt1;
    private Button bt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_box_game);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);


        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);

//		  Set from XML, possible to programmatically set
       float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());

       menu.setSatelliteDistance((int) distance);
       menu.setExpandDuration(500);
      // menu.setCloseItemsOnClick(false);
       menu.setTotalSpacingDegree(90);

        java.util.List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(6, R.drawable.ic_1));

        items.add(new SatelliteMenuItem(5, R.drawable.ic_3));

        items.add(new SatelliteMenuItem(4, R.drawable.ic_4));

        items.add(new SatelliteMenuItem(3, R.drawable.ic_5));

        items.add(new SatelliteMenuItem(2, R.drawable.ic_6));

        items.add(new SatelliteMenuItem(1, R.drawable.ic_2));

    items.add(new SatelliteMenuItem(0, R.drawable.sat_item));

        menu.addItems(items);

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {
                switch(id){
                    case 1:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(move_box_game.this);
                        builder1.setIcon(R.drawable.icon);
                        builder1.setMessage(R.string.function1);
                        builder1.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.dismiss();
                            }
                        });
                        builder1.setTitle("功能1");
                        builder1.show();
                        break;
                    case 0:
                        Toast.makeText(move_box_game.this,"close menu",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(move_box_game.this);
                        builder2.setIcon(R.drawable.icon);
                        builder2.setMessage(R.string.function2);
                        builder2.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder2.setTitle("功能2");
                        builder2.show();
                        break;
                    case 3:
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(move_box_game.this);
                        builder3.setIcon(R.drawable.icon);
                        builder3.setMessage(R.string.function3);
                        builder3.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder3.setTitle("功能3");
                        builder3.show();
                        break;
                    case 4:
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(move_box_game.this);
                        builder4.setIcon(R.drawable.icon);
                        builder4.setMessage(R.string.function4);
                        builder4.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder4.setTitle("功能4");
                        builder4.show();
                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                }
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                Intent i = new Intent(this, List.class);
                startActivity(i);
                break;
            case R.id.bt2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.icon);
                builder.setMessage(R.string.rule);
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setTitle("游戏规则");
                builder.show();

                break;
        }
    }


    @Override
    public void onBackPressed() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("信息提示")
                .setMessage("再玩一会吧！")
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