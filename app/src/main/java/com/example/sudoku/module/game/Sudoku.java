package com.example.sudoku.module.game;

import java.io.Serializable;

public class Sudoku implements Serializable {
    private int[][] checkerBoard = new int[9][9];

    public Sudoku() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Sudoku) {
            Sudoku s = (Sudoku) obj;
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    if (this.checkerBoard[i][j] != s.getNum(i, j)) {
                        return false;
                    }
                }
        }

        return true;
    }

    public Sudoku(Sudoku sudoku) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                this.checkerBoard[i][j] = sudoku.getNum(i, j);
    }

    public int getNum(int x, int y) {
        return checkerBoard[x][y];
    }

    public void setNum(int x, int y, int num) {
        checkerBoard[x][y] = num;
    }

    @Override
    public String toString() {

        int count = 0;
        StringBuffer s = new StringBuffer("");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (checkerBoard[i][j] == 0) count++;
                s.append(checkerBoard[i][j] + " ");
            }
            s.append("\n");
        }
        s.append("空白数量" + count);
        return s.toString();
    }

}
