package com.example.sudoku;

import android.app.Application;
import android.content.Context;

public class SudokuApplication extends Application {
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
    }
}
