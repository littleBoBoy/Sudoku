package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.sudoku.base.BaseActivity;

public class GameActivity extends BaseActivity {
    private static final String TAG = "GameActivity";

    private Sudoku sudokuEnd;
    private Sudoku sudokuGame;
    private SudokuGameController sudokuGameController;
    private SudokuAdapter sudokuAdapter;
    private final String[] keyBroad = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Intent intent;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GridView gameGridView = findViewById(R.id.MainActivityGrid);
        gameGridView.setAdapter(sudokuAdapter);
        gameGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.setIsSelected(position);
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


        GridView keyBroadGridView = findViewById(R.id.KeyBroadGrid);
        keyBroadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sudokuGameController.update(Integer.parseInt(keyBroad[position]));
                sudokuAdapter.notifyDataSetChanged();
            }
        });
        ArrayAdapter<String> keyBroadAdapter = new ArrayAdapter<>(this, R.layout.keybroad_item, keyBroad);
        keyBroadGridView.setAdapter(keyBroadAdapter);
        keyBroadGridView.setSelector(ContextCompat.getDrawable(this, R.drawable.selected));
    }

    @Override
    public void initData() {
        intent = getIntent();
        sudokuEnd = (Sudoku) intent.getSerializableExtra("sudokuEnd");
        sudokuGame = (Sudoku) intent.getSerializableExtra("sudokuGame");
        Log.w(TAG, sudokuEnd.toString());
        Log.w(TAG, "hahahha ");
        sudokuGameController = new SudokuGameController(sudokuGame);
        sudokuAdapter = new SudokuAdapter(this, R.layout.game_item, sudokuGameController);
    }
}
