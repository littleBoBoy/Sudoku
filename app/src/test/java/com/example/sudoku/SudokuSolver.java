package com.example.sudoku;

import com.example.sudoku.module.Sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class SudokuSolver {

    //随机递归求所有解
    private static void getAllSolutionRandom(Sudoku sudoku, List<Sudoku> arrSudoku, ArrayList<Integer> arrayList, int index) {
        if (index == 81) {
            Sudoku s = new Sudoku(sudoku);
            arrSudoku.add(s);
            return;
        }
        int num = arrayList.get(index);
        int x = (int) Math.ceil(num / 9.0 - 1);
        int y = (int) (num % 9.0);

        if (sudoku.getNum(x, y) == 0) {
            List usableNumber = MyList.UsableNumberList(sudoku, x, y);
            if (usableNumber.isEmpty()) return;

            for (Object anUsableNumber : usableNumber) {
                sudoku.setNum(x, y, (Integer) anUsableNumber);
                getAllSolutionRandom(sudoku, arrSudoku, arrayList, index + 1);
            }
            sudoku.setNum(x, y, 0);
        } else {
            getAllSolutionRandom(sudoku, arrSudoku, arrayList, index + 1);
        }
    }

    //顺序递归求所有解
    private static void getAllSolution(Sudoku sudoku, int x, int y, List<Sudoku> arrSudoku) {//深度遍历 全遍历
        if (x == 9) { //如果矩阵X坐标为9 则遍历到底，保存结果  回溯
            Sudoku s = new Sudoku(sudoku);
            arrSudoku.add(s);
            return;
        }
        //计算下一个坐标
        int next_x, next_y;
        if (y == 8) {
            next_x = x + 1;
            next_y = 0;
        } else {
            next_x = x;
            next_y = y + 1;
        }


        if (sudoku.getNum(x, y) == 0) {//矩阵坐标处为0 则计算所有可用值 依次填入 进行递归
            List usableNumber = MyList.UsableNumberList(sudoku, x, y);
            for (Object anUsableNumber : usableNumber) {//尝试所有符合约束的数
                sudoku.setNum(x, y, (Integer) anUsableNumber);
                getAllSolution(sudoku, next_x, next_y, arrSudoku);
            }
            sudoku.setNum(x, y, 0);//遍历结束后 将该位置的值归零，因为所有的遍历操作用的同一个矩阵 而需要填数但判断是sudoku.getNum(x, y)
        } else {//矩阵坐标处不为0 继续下一个坐标
            getAllSolution(sudoku, next_x, next_y, arrSudoku);
        }
    }

    //递归获得一个解
    private static boolean getOneSolution(Sudoku sudoku, int x, int y) {//深度遍历
        boolean haveResult = false;
        if (x == 9) {//如果矩阵X坐标为9 则遍历结束
            return true;
        }
        //计算下一个坐标
        int next_x, next_y;
        if (y == 8) {
            next_x = x + 1;
            next_y = 0;
        } else {
            next_x = x;
            next_y = y + 1;
        }

        if (sudoku.getNum(x, y) == 0) {
            List usableNumber = MyList.UsableNumberList(sudoku, x, y);
            Iterator it = usableNumber.iterator();

            while (!haveResult && it.hasNext()) {//不进行全遍历 遍历到底即havaResult==true后不再测试接下来的值
                sudoku.setNum(x, y, (Integer) it.next());
                haveResult = getOneSolution(sudoku, next_x, next_y);
            }
            if (!it.hasNext() && !haveResult) sudoku.setNum(x, y, 0);//haveResult==false即尝试失败 归零 回溯
        } else {
            haveResult = getOneSolution(sudoku, next_x, next_y);
        }
        return haveResult;
    }

    //挖洞  策略：只有该位置只能填这个数时才将该数挖去 保证有唯一解
    @SuppressWarnings("unchecked")
    public static Sudoku getSudokuGame(Sudoku sudoku, int count) {//传入一个终盘

        //随机挖洞 生成随机顺序
        LinkedList<Integer> randomNumber = new LinkedList<>(MyList.RandomSortNumberList(81));

        while (count != 0) {
            //获取要挖的坐标
            int index = randomNumber.get(0);
            int x = (int) Math.ceil(index / 9.0 - 1);
            int y = (int) (index % 9.0);
            //获取要挖出是否还有除当前数字外其他符合约束的数
            List usableNumber = MyList.UsableNumberList(sudoku, x, y);
            if (usableNumber.isEmpty()) {
                sudoku.setNum(x, y, 0);
                randomNumber.remove(0);
                count--;
            } else {
                Iterator iterator = usableNumber.iterator();
                boolean haveOtherSolution = false;
                while (iterator.hasNext()) {
                    Sudoku s = new Sudoku(sudoku);
                    s.setNum(x, y, (Integer) iterator.next());
                    if (getOneSolution(s, 0, 0)) {
                        haveOtherSolution = true;
                        break;
                    }
                }

                if (!haveOtherSolution) {
                    sudoku.setNum(x, y, 0);
                    randomNumber.remove(0);
                    count--;
                } else {
                    randomNumber.remove(0);
                    randomNumber.add(index);
                }
            }
        }
        return sudoku;
    }

    //挖洞  策略：先随机挖一定数目的的洞 检查结果是否有唯一解
    @SuppressWarnings("unchecked")
    public static Sudoku getGame(Sudoku sudoku, int count) {
        Sudoku endSudoku;
        ArrayList<Sudoku> arrayList;
        do {
            LinkedList<Integer> randomNumber = new LinkedList<>(MyList.RandomSortNumberList(81));
            Iterator iterator = randomNumber.iterator();

            Sudoku _sudoku = new Sudoku(sudoku);
            int _count = count;

            while (_count != 0) {
                int index = (Integer) iterator.next();
                int x = (int) Math.ceil(index / 9.0 - 1);
                int y = (int) (index % 9.0);
                _sudoku.setNum(x, y, 0);
                _count--;
            }

            endSudoku = new Sudoku(_sudoku);
            arrayList = new ArrayList<>();
            getAllSolution(_sudoku, 0, 0, arrayList);

        } while (arrayList.size() != 1);
        return endSudoku;
    }

    //获得一个数独终盘
    public static Sudoku getEndSudoku() {
        Sudoku sudoku = new Sudoku();

        for (int i = 1; i <= 3; i++) {
            ArrayList randomNumber = MyList.RandomSortNumberList(9);
            Iterator iterator = randomNumber.iterator();
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    sudoku.setNum(j + (i - 1) * 3 - 1, k + (i - 1) * 3 - 1, (Integer) iterator.next());
                }
            }
        }
        getOneSolution(sudoku, 0, 0);
        return sudoku;
    }

    //获得该数独所有解的Array
    public static List<Sudoku> getSolutionArr(Sudoku sudoku) {
        List<Sudoku> arrSudoku = new ArrayList<>();
        getAllSolution(sudoku, 0, 0, arrSudoku);
        return arrSudoku;
    }

    //获得该数独所有解的Array  Random
    public static List<Sudoku> getSolutionArrRandom(Sudoku sudoku) {
        List<Sudoku> arrSudoku = new ArrayList<>();
        ArrayList<Integer> arrayList = MyList.RandomSortNumberList(81);
        getAllSolutionRandom(sudoku, arrSudoku, arrayList, 0);
        return arrSudoku;
    }

}
