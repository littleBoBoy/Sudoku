package com.example.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.common.ActivityController;
import com.example.sudoku.module.Rank.RankActivity;
import com.example.sudoku.module.game.GameActivity;
import com.example.sudoku.module.login.LoginActivity;
import com.example.sudoku.util.AppUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    AlertDialog levelSelector;

    private Button gameLevel;
    private Button startGame;
    private Button continueGame;
    private Button login;
    private Button logout;
    private Button rank;

    private String[] levelName = new String[]{"入门", "初级", "中级", "高级"};
    private int level = 0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        startGame = findViewById(R.id.start_game);
        gameLevel = findViewById(R.id.level_game);
        continueGame = findViewById(R.id.continue_game);
        logout = findViewById(R.id.logout);
        login = findViewById(R.id.login);
        rank = findViewById(R.id.btn_rank);


        startGame.setOnClickListener(this);
        gameLevel.setOnClickListener(this);
        continueGame.setOnClickListener(this);
        login.setOnClickListener(this);
        logout.setOnClickListener(this);
        rank.setOnClickListener(this);

        levelSelector = new AlertDialog.Builder(MainActivity.this)
                .setTitle("选择难度")
                .setItems(levelName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = which;
                        gameLevel.setText("游戏难度：" + levelName[which]);
                    }
                }).create();


    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_game:
                Bundle bundleNew = new Bundle();
                bundleNew.putString("game", "new");
                bundleNew.putInt("level", level);
                ActivityController.skipActivity(MainActivity.this, GameActivity.class, bundleNew);
                break;
            case R.id.continue_game:
                Bundle bundleContinue = new Bundle();
                bundleContinue.putString("game", "continue");
                ActivityController.skipActivity(MainActivity.this, GameActivity.class, bundleContinue);
                break;
            case R.id.level_game:
                levelSelector.show();
                break;
            case R.id.logout:
                AppUtil.saveId("");
                break;
            case R.id.login:
                if (AppUtil.readId().length() == 13)
                    AppUtil.ToastShort("您已登录");
                ActivityController.skipActivity(MainActivity.this, LoginActivity.class);
                break;
            case R.id.btn_rank:
                if (AppUtil.readId() == "") {
                    AppUtil.ToastShort("您还未登录。");
                } else {
                    ActivityController.skipActivity(MainActivity.this, RankActivity.class);
                }
                break;

        }
    }
}