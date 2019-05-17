package com.example.sudoku.module.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sudoku.R;
import com.example.sudoku.base.BaseActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity {
    private static final String TAG = "GameActivity";

    private final String[] keyBroad = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Intent intent;
    private int screenWidth;
    private TextView timer;
    private GridView gameGridView;
    private GridView keyBroadGridView;
    private ArrayAdapter<String> keyBroadAdapter;
    private SudokuGameController sudokuGameController;
    private SudokuAdapter sudokuAdapter;

    private long startTime = System.currentTimeMillis();
    ;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (timer != null) {
                timer.setText((String) msg.obj);
            }
        }
    };

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        gameGridView = findViewById(R.id.MainActivityGrid);
        gameGridView.setAdapter(sudokuAdapter);
        gameGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.setCurrentSelect(position);
                sudokuAdapter.notifyDataSetChanged();
            }
        });

        gameGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.update(0);
                sudokuAdapter.notifyDataSetChanged();
                return true;
            }
        });


        keyBroadGridView = findViewById(R.id.KeyBroadGrid);
        keyBroadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.update(Integer.parseInt(keyBroad[position]));
                sudokuAdapter.notifyDataSetChanged();
            }
        });
        keyBroadAdapter = new ArrayAdapter<>(this, R.layout.keybroad_item, keyBroad);
        keyBroadGridView.setAdapter(keyBroadAdapter);
        keyBroadGridView.setSelector(ContextCompat.getDrawable(this, R.drawable.selected));

        timer = findViewById(R.id.tv_timer);
        new Timer("计时器").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int time = (int) ((System.currentTimeMillis() - startTime) / 1000);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = new String("用时：" + mm + ":" + ss);
                Message msg = new Message();
                msg.obj = timeFormat;
                handler.sendMessage(msg);
            }

        }, 0, 1000L);
    }

    @Override
    public void initData() {
        intent = getIntent();
        if (intent.getStringExtra("game").equals("new")) {
            sudokuGameController = new SudokuGameController(intent.getIntExtra("level", 25), 0);
        } else if (intent.getStringExtra("game").equals("continue")) {
            readSudokuGameController();
        }
//        screenWidth = ScreenUtil.getScreenWidth(this);
//        screenWidth = ScreenUtil.px2dip(this, screenWidth);
        sudokuAdapter = new SudokuAdapter(this, R.layout.game_item, sudokuGameController, screenWidth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSudokuGameController();
    }

    private void saveSudokuGameController() {
        FileOutputStream fos;
        ObjectOutputStream oos;
        sudokuGameController.setTime(System.currentTimeMillis() - startTime);
        try {
            fos = getApplicationContext().openFileOutput("lastGame", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(sudokuGameController);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSudokuGameController() {
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fis = getApplicationContext().openFileInput("lastGame");
            ois = new ObjectInputStream(fis);
            sudokuGameController = (SudokuGameController) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sudokuGameController == null) {
            sudokuGameController = new SudokuGameController(25, 0);
        }
        startTime = System.currentTimeMillis() - sudokuGameController.getTime();
    }
}
