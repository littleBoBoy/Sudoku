package com.example.sudoku.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.sudoku.R;


import static android.content.ContentValues.TAG;

public class SudokuAdapter extends BaseAdapter {

    private Context mContext;
    private final SudokuGameController mSudokuGameController;
    private int mItem;
    private int wh;

    public SudokuAdapter(Context context, int item, SudokuGameController sudokuGameController,int screenWidth) {
        super();
        this.mContext = context;
        this.mItem = item;
        this.mSudokuGameController = sudokuGameController;
        this.wh=screenWidth/9;
        Log.w(TAG, "Adapter: "+wh );
    }

    @Override
    public int getCount() {
        return 81;
    }

    @Override
    public Object getItem(int position) {
        return mSudokuGameController.getSudokuGameItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(mContext).inflate(mItem, parent, false);
        Button button = view.findViewById(R.id.game_button);
        GradientDrawable item_background = (GradientDrawable) button.getBackground();

        SudokuGameController.SudokuGameItem sudokuGameItem = mSudokuGameController.getSudokuGameItem(position);

        int text = sudokuGameItem.getNum();
        button.setText(text == 0 ? "" : String.valueOf(text));
        button.setTextColor(ContextCompat.getColor(mContext, sudokuGameItem.getTextColor()));
        button.setHeight(wh);
        item_background.setColor(ContextCompat.getColor(mContext, sudokuGameItem.getBackground()));

        if (sudokuGameItem.isSelected())
            view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected));
        else view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.noSelected));

        return view;
    }
}
