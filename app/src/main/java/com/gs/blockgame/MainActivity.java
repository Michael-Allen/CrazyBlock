package com.gs.blockgame;

import com.gs.blockgame.Klotski.KlotskiActivity;
import com.gs.blockgame.m2048.M2048Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button start2048;
    private Button startKlotski;
    private Context mContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        start2048 = (Button) findViewById(R.id.button1);
        start2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, M2048Activity.class);
                startActivity(i);
            }
        });
        startKlotski = (Button) findViewById(R.id.button2);
        startKlotski.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent();
                i.setClass(MainActivity.this, KlotskiActivity.class);
                //enterPictureInPictureMode();
                final String[] items = getResources().getStringArray(
                        R.array.Klotski_level);
                new AlertDialog.Builder(MainActivity.this).setTitle("请选择关卡")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int select_level = which;
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("确定开始" + items[select_level] + "吗？")
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        i.putExtra("level", select_level);
                                                        startActivity(i);
                                                    }
                                                })
                                        .setNegativeButton("取消",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {

                                                    }
                                                }).show();
                            }
                        }).show();
            }
        });
    }
}
