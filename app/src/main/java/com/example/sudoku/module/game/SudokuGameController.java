package com.example.sudoku.module.game;

import com.example.sudoku.R;

import java.io.Serializable;

public class SudokuGameController implements Serializable {

    private static final int ERROR = R.color.error;
    private static final int BACKGROUND_0 = R.color.colorBackground0;
    private static final int BACKGROUND_1 = R.color.colorBackground1;
    private static final int BACKGROUND_0_CONSTANT = R.color.colorBackground0Constant;
    private static final int BACKGROUND_1_CONSTANT = R.color.colorBackground1Constant;
    private static final int TEXT_COLOR_0 = R.color.colorText0;
    private static final int TEXT_COLOR_1 = R.color.colorText1;

    private long time;
    private Sudoku endSudoku;
    private Sudoku constantSudoku;
    private Sudoku viewSudoku;

    private int[][] background;
    private boolean[][] isSelected;
    private int[][] textColor;
    private int currentSelect;

    public SudokuGameController(int level, long time) {
        endSudoku = SudokuSolver.EndSudoku();
        constantSudoku = SudokuSolver.getSudokuGame(endSudoku, level);
        viewSudoku = new Sudoku(constantSudoku);
        this.time = time;
        init();
    }

    private void init() {
        background = new int[9][9];
        isSelected = new boolean[9][9];
        textColor = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                background[i][j] = backgroundType(i, j);
                isSelected[i][j] = false;
                textColor[i][j] = textColorType(i, j);
            }
        }
    }

    public SudokuGameItem getSudokuGameItem(int position) {
        int x = getX(position);
        int y = getY(position);
        return new SudokuGameItem(viewSudoku.getNum(x, y), background[x][y], textColor[x][y], isSelected[x][y]);
    }

    public void update(int num) {

        int x = getX(currentSelect);
        int y = getY(currentSelect);
        if (constantSudoku.getNum(x, y) != 0) return;
        viewSudoku.setNum(x, y, num);

        for (int h = 0; h < 9; h++) {
            for (int v = 0; v < 9; v++) {
                int _num = viewSudoku.getNum(h, v);
                if (_num == 0) continue;
                boolean isTrue = true;
                int cenNumX = h / 3 * 3 + 1;
                int cenNumY = v / 3 * 3 + 1;
                for (int i = 1; isTrue && i <= 3; i++) {
                    for (int j = 1; isTrue && j <= 3; j++) {
                        if (h == cenNumX + i - 2 && v == cenNumY + j - 2) continue;
                        isTrue = (viewSudoku.getNum(cenNumX + i - 2, cenNumY + j - 2) != _num);
                    }
                }
                for (int i = 0; isTrue && i < 9; i++) {
                    if (i == v) continue;
                    isTrue = (viewSudoku.getNum(h, i) != _num);
                }
                for (int i = 0; isTrue && i < 9; i++) {
                    if (i == h) continue;
                    isTrue = (viewSudoku.getNum(i, v) != _num);
                }

                if (!isTrue) textColor[h][v] = ERROR;
                else textColor[h][v] = textColorType(h, v);
            }
        }
    }


    private int getX(int position) {
        return position / 9;
    }

    private int getY(int position) {
        return position % 9;
    }

    private int backgroundType(int x, int y) {
        return constantSudoku.getNum(x, y) == 0 ? ((x / 3) * 3 + (y / 3)) % 2 == 0 ? BACKGROUND_0 : BACKGROUND_1 : ((x / 3) * 3 + (y / 3)) % 2 == 0 ? BACKGROUND_0_CONSTANT : BACKGROUND_1_CONSTANT;
    }

    private int textColorType(int x, int y) {
        return ((x / 3) * 3 + (y / 3)) % 2 == 0 ? TEXT_COLOR_0 : TEXT_COLOR_1;
    }

    public void setCurrentSelect(int currentSelect) {
        this.currentSelect = currentSelect;

        int x = getX(currentSelect);
        int y = getY(currentSelect);
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                this.isSelected[i][j] = false;
            }
        this.isSelected[x][y] = true;

    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public static class SudokuGameItem {
        int num;
        int background;
        boolean isSelected;
        int textColor;

        public SudokuGameItem(int num, int background, int textColor, boolean isSelected) {
            this.num = num;
            this.background = background;
            this.textColor = textColor;
            this.isSelected = isSelected;
        }

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

    }
}

