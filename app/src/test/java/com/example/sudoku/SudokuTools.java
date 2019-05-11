package com.example.sudoku;

public class SudokuTools {
    //通过0-80 获取对应的X  Y坐标
    public static int getX(int position) {
        return (int) Math.ceil((position + 1) / 9.0 - 1);
    }

    public static int getY(int position) {
        return (int) ((position + 1) % 9.0);
    }
}
