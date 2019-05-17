package com.example.sudoku;

import com.example.sudoku.module.game.Sudoku;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {

        long stime = System.currentTimeMillis();
        for (int i = 1; i <= 1; i++) {
            Sudoku endSudoku = SudokuSolverT.getEndSudoku();
            System.out.println(i + "   终盘！！！！！！！！！！！");
//            System.out.println(endSudoku.toString());
//            Sudoku sudokuGame = SudokuSolver.getSudokuGame(new Sudoku(endSudoku), 54);
            Sudoku sudokuGame = SudokuSolverT.getGame(endSudoku, 58);
//            System.out.println("游戏！！！！！！！！！！！");
            System.out.println(sudokuGame.toString());

//            List arrayList = SudokuSolver.getSolutionArr(sudokuGame);
//            Iterator iterator = arrayList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println("顺序解得!!!!!!!!!!!!!!!!");
//                System.out.println(iterator.next().toString());
//            }
//
//            List arrayList2 = SudokuSolverT.getSolutionArr(sudokuGame);
//            Iterator iterator2 = arrayList2.iterator();
//            while (iterator2.hasNext()) {
//                System.out.println("随机解得!!!!!!!!!!!!!!!!");
//                System.out.println(iterator2.next().toString());
//            }
        }
        long etime = System.currentTimeMillis();
        System.out.println(stime - etime);
    }

}