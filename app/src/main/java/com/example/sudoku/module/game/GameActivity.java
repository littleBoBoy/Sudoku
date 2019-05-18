package com.example.sudoku.module.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.base.BaseBean;
import com.example.sudoku.common.ActivityController;
import com.example.sudoku.http.RequestCallback;
import com.example.sudoku.http.RetrofitManager;
import com.example.sudoku.http.RxSchedulers;
import com.example.sudoku.http.service.ApiService;
import com.example.sudoku.util.AppUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity {
    private static final String TAG = "GameActivity";

    private final String[] keyBroad = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Intent intent;
    private Timer timer;
    private TextView tv_time;
    private TextView tv_blabkSpace;
    private GridView gameGridView;
    private GridView keyBroadGridView;
    private ArrayAdapter<String> keyBroadAdapter;
    private SudokuGameController sudokuGameController;
    private SudokuAdapter sudokuAdapter;
    private int[] level={2,35,45,53};
    private String[] levelName = new String[]{"test", "初级", "中级", "高级"};
    private String[] levelUrl = new String[]{"test", "simple", "ordinary", "difficulties"};
    private long startTime = System.currentTimeMillis();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (tv_time != null) {
                tv_time.setText((String) msg.obj);
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
                tv_blabkSpace.setText("剩余空格："+sudokuGameController.getBlankSpace());
                return true;
            }
        });


        keyBroadGridView = findViewById(R.id.KeyBroadGrid);
        keyBroadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.update(Integer.parseInt(keyBroad[position]));

                tv_blabkSpace.setText("剩余空格："+sudokuGameController.getBlankSpace());
                sudokuAdapter.notifyDataSetChanged();
                if(sudokuGameController.getBlankSpace()==0)endGame();
            }
        });
        keyBroadAdapter = new ArrayAdapter<>(this, R.layout.keybroad_item, keyBroad);
        keyBroadGridView.setAdapter(keyBroadAdapter);
        keyBroadGridView.setSelector(ContextCompat.getDrawable(this, R.drawable.selected));

        tv_time = findViewById(R.id.tv_timer);
        tv_blabkSpace=findViewById(R.id.tv_blankspace);

    }

    @Override
    public void initData() {
        intent = getIntent();
        if (intent.getStringExtra("game").equals("new")) {
            sudokuGameController = new SudokuGameController(level[intent.getIntExtra("level", 0)], 0);
        } else if (intent.getStringExtra("game").equals("continue")) {
            readSudokuGameController();
        }
        sudokuAdapter = new SudokuAdapter(this, R.layout.game_item, sudokuGameController);
        gameGridView.setAdapter(sudokuAdapter);
        tv_blabkSpace.setText("剩余空格："+sudokuGameController.getBlankSpace());
        timer= new Timer("计时器");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int time = (int) ((System.currentTimeMillis() - startTime) );
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("用时：mm:ss");
                String timeFormat = simpleDateFormat.format(time);
                Message msg = new Message();
                msg.obj = timeFormat;
                handler.sendMessage(msg);
            }

        }, 0, 1000L);

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

    private void endGame(){

        if(!sudokuGameController.hasEnd())
            return;

        Long l=System.currentTimeMillis()-startTime;
        timer.cancel();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("用时：mm:ss:SS");
        String timeFormat = simpleDateFormat.format(l);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("太棒了！！！");
        builder.setMessage("级别："+levelName[intent.getIntExtra("level", 0)]+"\n"+timeFormat);
        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityController.skipActivityAndFinish(GameActivity.this,MainActivity.class);
            }
        });
        AlertDialog a=builder.create();
        a.setCanceledOnTouchOutside(false);
        a.setOnKeyListener(new DialogInterface.OnKeyListener() {
                               @Override
                               public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                   ActivityController.skipActivityAndFinish(GameActivity.this,MainActivity.class);
                                   return true;
                               }
                           }
        );
        a.show();

        Map<String ,String> map=new HashMap<>();
        map.put("level",levelUrl[intent.getIntExtra("level", 0)]);
        map.put("userId",AppUtil.readId());
        map.put("score",l.toString());
        RetrofitManager
                .getRetrofit()
                .create(ApiService.class)
                .submit(map)
                .compose(RxSchedulers.applySchedulers())
                .as(bindAutoDispose())
                .subscribe(new RequestCallback<BaseBean>() {
                    @Override
                    protected void onSuccess(Object object) {
                    }
                    @Override
                    protected void onFailer(String errStr) {
                    }
                });
    }
}
