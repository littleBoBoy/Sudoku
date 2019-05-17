package com.example.sudoku.util;

import android.content.Context;
import android.widget.Toast;

import com.example.sudoku.SudokuApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AppUtil {
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
     * 17+除9的任意数 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void ToastLong(String s) {
        Toast.makeText(SudokuApplication.AppContext, s, Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(String s) {
        Toast.makeText(SudokuApplication.AppContext, s, Toast.LENGTH_SHORT).show();
    }

    public static void saveId(String pk) {
        FileOutputStream fos;
        ObjectOutputStream osw;
        try {
            fos = SudokuApplication.AppContext.openFileOutput("pk", Context.MODE_PRIVATE);
            osw = new ObjectOutputStream(fos);
            osw.writeObject(pk);
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readId() {
        FileInputStream fis;
        ObjectInputStream ois;
        String pk = "";
        try {
            fis = SudokuApplication.AppContext.openFileInput("pk");
            ois = new ObjectInputStream(fis);
            pk = (String) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pk;
    }
}
