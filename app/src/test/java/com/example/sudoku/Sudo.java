package com.example.sudoku;

import org.junit.Test;

import java.util.Scanner;

/**
 * @author chnmagnus
 * @description 数独生成和求解
 * @limit 支持从1-80的数字提示数量
 * @method 深度优先搜索/回溯法
 */
public class Sudo {
    private int[][] data = new int[9][9]; //muti_array
    private int lef; //the number of zero in array
    private int tip; //the number of nozero_digit in array

    /**
     * 构造函数
     * 初始化变量
     */
    public Sudo() {
        lef = 0;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                data[i][j] = 0;
            }
        }
    }

    /**
     * 生成数独
     * 方法：挖洞法
     */
    public void genSudo() {
        System.out.println("Please input the number of digits provided:");
        Scanner scan = new Scanner(System.in);
        tip = scan.nextInt();
        scan.close();
        /*将1-9 9个数字放在二维数组中随机位置*/
        lef = 81 - 9;
        for (int i = 0; i < 9; ++i) {
            data[0][i] = i + 1;
        }
        for (int i = 0; i < 9; ++i) {
            int ta = (int) (Math.random() * 10) % 9;
            int tb = (int) (Math.random() * 10) % 9;
            int tem = data[0][ta];
            data[0][ta] = data[0][tb];
            data[0][tb] = tem;
        }
        for (int i = 0; i < 9; ++i) {
            int ta = (int) (Math.random() * 10) % 9;
            int tb = (int) (Math.random() * 10) % 9;
            int tem = data[0][i];
            data[0][i] = data[ta][tb];
            data[ta][tb] = tem;
        }
        /*通过9个数字求出一个可行解*/
        solveSudo();
        lef = 81 - tip;
        for (int i = 0; i < lef; ++i) {
            int ta = (int) (Math.random() * 10) % 9;
            int tb = (int) (Math.random() * 10) % 9;
            if (data[ta][tb] != 0)
                data[ta][tb] = 0;
            else
                i--;
        }
    }

    /**
     * 求解数独
     *
     * @return 是否有解的boolean标识
     */
    public boolean solveSudo() {
        if (dfs()) {
            System.out.println("Solve completed.");
            return true;
        } else {
            System.out.println("Error:There are no solution.");
            return false;
        }
    }

    /**
     * 输出数独数组
     */
    public void printSudo() {
        System.out.println("-----------------");
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (data[i][j] > 0)
                    System.out.print(data[i][j] + " ");
                else
                    System.out.print("* ");
            }
            System.out.print('\n');
        }
        System.out.println("-----------------");
    }

    /**
     * 计算某格子的可填数字个数，即不确定度
     *
     * @param r
     * @param c
     * @param mark
     * @return 不确定度
     */
    private int calcount(int r, int c, int[] mark) {
        for (int ti = 0; ti < 10; ++ti)
            mark[ti] = 0;
        for (int i = 0; i < 9; ++i) {
            mark[data[i][c]] = 1;
            mark[data[r][i]] = 1;
        }
        int rs = (r / 3) * 3;
        int cs = (c / 3) * 3;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                mark[data[rs + i][cs + j]] = 1;
            }
        }
        int count = 0;
        for (int i = 1; i <= 9; ++i) {
            if (mark[i] == 0)
                count++;
        }
        return count;
    }

    /**
     * 供solve调用的深度优先搜索
     *
     * @return 是否有解的boolean标识
     */
    private boolean dfs() {
        if (lef == 0) return true;
        int mincount = 10;
        int mini = 0, minj = 0;
        int[] mark = new int[10];
        /*找到不确定度最小的格子*/
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (data[i][j] != 0) continue;

                int count = calcount(i, j, mark);
                if (count == 0) return false;
                if (count < mincount) {
                    mincount = count;
                    mini = i;
                    minj = j;
                }
            }
        }
        /*优先处理不确定度最小的格子*/
        calcount(mini, minj, mark);
        for (int i = 1; i <= 9; ++i) {
            if (mark[i] == 0) {
                data[mini][minj] = i;
                lef--;
                dfs();
                if (lef == 0) return true;
                data[mini][minj] = 0;//回溯法
                lef++;
            }
        }
        return true;
    }

    /**
     * main函数
     *
     * @param args
     */
    @Test
    public static void main(String[] args) {
        Sudo su = new Sudo();
        su.genSudo();
        su.printSudo();
        su.solveSudo();
        su.printSudo();
    }
}

