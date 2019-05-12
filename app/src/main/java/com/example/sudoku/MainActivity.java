package com.example.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.common.ActivityController;
import com.example.sudoku.module.GameActivity;
import com.example.sudoku.util.ScreenUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    AlertDialog levelSelector;
    private int count = 25;

    private Button gameLevel;
    private Button startGame;
    private Button continueGame;

    private String[] levelName = new String[]{"入门", "初级", "中级", "高级"};
    private int[] level = new int[]{25, 35, 45, 53};


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        startGame = findViewById(R.id.start_game);
        gameLevel = findViewById(R.id.level_game);
        continueGame=findViewById(R.id.continue_game);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("game","new");
                bundle.putInt("level", count);
                ActivityController.skipActivity(MainActivity.this, GameActivity.class, bundle);
            }
        });

        gameLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelector.show();
            }
        });

        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("game","continue");
                ActivityController.skipActivity(MainActivity.this, GameActivity.class, bundle);
            }
        });

    }

    @Override
    public void initData() {
        levelSelector = new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择难度")
                .setItems(levelName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count = level[which];
                        gameLevel.setText("游戏难度：" + levelName[which]);
                    }
                }).create();
    }
}