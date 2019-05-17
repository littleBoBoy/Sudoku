package com.example.sudoku.module.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SudokuSolver {

    private static int getX(int position) {
        return position / 9;
    }

    private static int getY(int position) {
        return position % 9;
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
            List usableNumber = UsableNumberList(sudoku, x, y);
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
            List usableNumber = UsableNumberList(sudoku, x, y);
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

        //随机挖洞 生成随机挖洞的顺序
        ArrayList<Integer> randomNumber = RandomSortNumberList(81);
        Iterator digHoleOrder = randomNumber.iterator();

        while (count != 0 && digHoleOrder.hasNext()) {
            //获取要挖的坐标
            int position = (Integer) digHoleOrder.next() - 1;
            int x = getX(position);
            int y = getY(position);

            //获取要挖出是否还有除当前数字外其他符合约束的数
            List usableNumber = UsableNumberList(sudoku, x, y);
            if (usableNumber.isEmpty()) {
                sudoku.setNum(x, y, 0);
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
                    count--;
                }
            }
        }
        return sudoku;
    }

    //获得一个数独终盘
    public static Sudoku EndSudoku() {
        Sudoku sudoku = new Sudoku();

        for (int i = 0; i <= 2; i++) {
            ArrayList randomNumber = RandomSortNumberList(9);
            Iterator iterator = randomNumber.iterator();
            for (int j = 0; j <= 2; j++) {
                for (int k = 0; k <= 2; k++) {
                    sudoku.setNum(j + i * 3, k + i * 3, (Integer) iterator.next());
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


    //删除某一值
    public static void removeOfValue(List list, int value) {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (value == (Integer) iterator.next()) {
                iterator.remove();
                break;
            }
        }
    }

    //生成随机顺序的数字 1 -> count
    public static ArrayList RandomSortNumberList(int count) {
        ArrayList<Integer> orderNumber = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orderNumber.add(i + 1);
        }
        Random rand = new Random();
        for (int i = count - 1; i > -1; i--) {
            int randInt = rand.nextInt(count);
            if (randInt != i) {
                int temp = orderNumber.get(i);
                orderNumber.set(i, orderNumber.get(randInt));
                orderNumber.set(randInt, temp);
            }
        }
        return orderNumber;
    }

    //获得每个位置的可填数字
    public static ArrayList UsableNumberList(Sudoku sudoku, int x, int y) {
        ArrayList usableNumber = RandomSortNumberList(9);

        for (int i = 0; i < 9; i++)
            removeOfValue(usableNumber, sudoku.getNum(x, i));
        for (int i = 0; i < 9; i++)
            removeOfValue(usableNumber, sudoku.getNum(i, y));

        int cenNumX = x / 3 * 3 + 1;
        int cenNumY = y / 3 * 3 + 1;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                removeOfValue(usableNumber, sudoku.getNum(cenNumX + i - 2, cenNumY + j - 2));
            }
        }
        return usableNumber;
    }
}
