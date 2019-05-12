package com.example.sudoku;

import com.example.sudoku.module.Sudoku;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MyList {

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

    //生成随机顺序的1-9
    public static ArrayList RandomSortNumberList(int count) {
        ArrayList<Integer> orderNumber = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            orderNumber.add(i);
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

        int cenNumX = (int) (Math.ceil((x + 1) / 3.0) * 3 - 1 - 1);
        int cenNumY = (int) (Math.ceil((y + 1) / 3.0) * 3 - 1 - 1);
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                removeOfValue(usableNumber, sudoku.getNum(cenNumX + i - 2, cenNumY + j - 2));
            }
        }
        return usableNumber;
    }

}
