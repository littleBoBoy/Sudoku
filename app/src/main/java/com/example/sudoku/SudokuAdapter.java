package com.example.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class SudokuAdapter extends BaseAdapter {
    private Context mContext;
    private final SudokuGameController mSudokuGameController;
    private int mItem;

    private static final String TAG = "SudokuAdapter";


    public SudokuAdapter(Context context, int item, SudokuGameController sudokuGameController) {
        super();
        this.mContext = context;
        this.mItem = item;
        this.mSudokuGameController = sudokuGameController;

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

        SudokuGameItem sudokuGameItem = mSudokuGameController.getSudokuGameItem(position);

        int text = sudokuGameItem.getNum();
        button.setText(text == 0 ? "" : String.valueOf(text));
        button.setTextColor(ContextCompat.getColor(mContext, sudokuGameItem.getTextColor()));
        item_background.setColor(ContextCompat.getColor(mContext, sudokuGameItem.getBackground()));

        if (sudokuGameItem.isSelected)
            view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected));
        else view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.noSelected));

        return view;
    }
}
