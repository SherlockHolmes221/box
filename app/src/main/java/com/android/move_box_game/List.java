package com.android.move_box_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by quxia on 2017/4/25.
 */
public class List extends Activity {
    private ListView listView;
    private TopBar topBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        topBar = (TopBar)findViewById(R.id.topBar1);
        topBar.setOnTopBarListener(new TopBar.TopBarOnClickListener() {
            @Override
            public void leftClick() {
                Intent intent = new Intent(List.this,move_box_game.class);
                startActivity(intent);
            }

            @Override
            public void rightCilck() {
                Toast.makeText(List.this,"NoMore",Toast.LENGTH_SHORT).show();
            }
        });


        int MAX = 3;  //设置最大关卡数
        listView = (ListView) findViewById(R.id.list);
        java.util.List<String> list = new ArrayList<String>();
        for (int i = 1; i <= MAX; i++) {
            list.add("第" + i + "关");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        LayoutAnimationController animation = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.zoon_in));
        animation.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(animation);
        listView.startLayoutAnimation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent  intent =new Intent(List.this,Game01.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent  intent1 =new Intent(List.this,Game02.class);
                        startActivity(intent1);
                       // Toast.makeText(List.this,"尚未开发，敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(List.this,"尚未开发，敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("信息提示")
                .setMessage("确定要返回吗?")
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
