package com.example.sudoku;

public class SudokuGameItem {
    int num;
    int background;
    boolean isSelected;

    public int getNum() {
        return num;
    }

    public int getBackground() {
        return background;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getTextColor() {
        return textColor;
    }

    int textColor;

    public SudokuGameItem(int num, int background, int textColor, boolean isSelected) {
        this.num = num;
        this.background = background;
        this.textColor = textColor;
        this.isSelected = isSelected;
    }
}
