package com.example.sudoku;

import com.example.sudoku.module.Sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SudokuSolverT {
    private static int cccc = 0;

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
            Iterator iterator = usableNumber.iterator();
//            if (usableNumber.isEmpty()) return;//前某次递归的结果不合适导致无数可填 回溯
//            for (Object anUsableNumber : usableNumber) {
//                sudoku.setNum(x, y, (Integer) anUsableNumber);
//                getAllSolution(sudoku, next_x, next_y, arrSudoku);
//            }
            while (iterator.hasNext()) {
                sudoku.setNum(x, y, (Integer) iterator.next());
                getAllSolution(sudoku, next_x, next_y, arrSudoku);

            }
            sudoku.setNum(x, y, 0);//遍历结束后 将该位置的值归零，因为所有的遍历操作用的同一个矩阵 而需要填数但判断是sudoku.getNum(x, y)
        } else {//矩阵坐标处不为0 继续下一个坐标
            getAllSolution(sudoku, next_x, next_y, arrSudoku);
        }
    }

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
//        System.out.println(index);


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

    //递归获得一个解
    private static boolean getOneSolution(Sudoku sudoku, int x, int y) {
        boolean haveResult = false;
        if (x == 9) {
            return true;
        }
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

            while (!haveResult && it.hasNext()) {
                sudoku.setNum(x, y, (Integer) it.next());
                haveResult = getOneSolution(sudoku, next_x, next_y);
            }
            if (!haveResult) sudoku.setNum(x, y, 0);
        } else {
            haveResult = getOneSolution(sudoku, next_x, next_y);
        }
        return haveResult;
    }

    public static Sudoku getGame(Sudoku sudoku, int count) {
        ArrayList<Integer> randomNumber = MyList.RandomSortNumberList(81);
        Iterator iterator = randomNumber.iterator();
        digHole(sudoku, count, randomNumber, -1);
        return sudoku;
    }

    //挖洞
    @SuppressWarnings("unchecked")
    private static boolean digHole(Sudoku sudoku, int holeCount, ArrayList<Integer> randomNumber, int _index) {//传入一个终盘

        boolean haveResult = false;

        if (holeCount == 0) return true;

        int index = _index + 1;

        while (!haveResult && index != _index) {

            if (index == 81) index = 0;
            int nextHole = randomNumber.get(index);
            if (nextHole == 0) {
                index++;
                continue;
            }
//            System.out.println(index);
            int x = (nextHole - 1) / 9;
            int y = (nextHole - 1) % 9;
            int num = sudoku.getNum(x, y);

            List usableNumber = MyList.UsableNumberList(sudoku, x, y);
            if (usableNumber.isEmpty()) {
                sudoku.setNum(x, y, 0);
                randomNumber.set(index, 0);
                System.out.println(cccc++ + "!!!!!!!!!!!" + nextHole);
                return digHole(sudoku, holeCount - 1, randomNumber, index);
            } else {
                Iterator iterator = usableNumber.iterator();
                boolean haveOtherSolution = false;
                while (!haveOtherSolution && iterator.hasNext()) {
                    Sudoku s = new Sudoku(sudoku);
                    s.setNum(x, y, (Integer) iterator.next());
                    haveOtherSolution = getOneSolution(s, 0, 0);
                }
                if (!haveOtherSolution) {
                    sudoku.setNum(x, y, 0);
                    randomNumber.set(index, 0);
                    randomNumber.set(index, 0);
                    System.out.println(cccc++ + "$$$$$$$$$$$$$$" + nextHole);
                    return digHole(sudoku, holeCount - 1, randomNumber, index);
                } else {
                    randomNumber.set(index, 0);
                    System.out.println(cccc++ + "%%%%%%%%%%%%%%" + nextHole);
                    haveResult = digHole(sudoku, holeCount, randomNumber, index);
                }
            }

            if (!haveResult) {
                sudoku.setNum(x, y, num);
                randomNumber.set(index, nextHole);
                index++;
                System.out.println(cccc++ + "##################################" + nextHole);
            }

        }
        return haveResult;
    }

    @SuppressWarnings("unchecked")
//    public static Sudoku getGame(Sudoku sudoku, int count) {
//
//        Sudoku _sudoku;
//        Sudoku endSudoku;
//        List<Sudoku> arrayList;
//        do{
//            LinkedList<Integer> randomNumber = new LinkedList<>(MyList.RandomSortNumberList(81));
//            Iterator iterator = randomNumber.iterator();
//            _sudoku=new Sudoku(sudoku);
//            int _count = count;
//            while (_count != 0) {
//                int index = (Integer) iterator.next();
//                int x = (int) Math.ceil(index / 9.0 - 1);
//                int y = (int) (index % 9.0);
//                _sudoku.setNum(x, y, 0);
//                _count--;
//            }
//            endSudoku=new Sudoku(_sudoku);
//            arrayList=getSolutionArr(_sudoku);
//
//        }while (arrayList.size()!=1);
//        return endSudoku;
//    }

    //获得一个数独终盘
    public static Sudoku getEndSudoku() {
        Sudoku sudoku = new Sudoku();

        ;
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
        ArrayList<Integer> arrayList = MyList.RandomSortNumberList(81);
        getAllSolutionRandom(sudoku, arrSudoku, arrayList, 0);
        return arrSudoku;
    }

}
