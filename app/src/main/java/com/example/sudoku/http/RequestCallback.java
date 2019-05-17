package com.example.sudoku.http;

import com.example.sudoku.SudokuApplication;
import com.example.sudoku.base.BaseBean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author sunxin
 * @Description 自定义 Observer
 */

public abstract class RequestCallback<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseBean) {
            if (((BaseBean) t).code == 200) {
                onSuccess(((BaseBean) t).data);
            } else {
                onFailer(((BaseBean) t).msg);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
//        Toast.makeText(MyApplication.AppContext, e.toString(), Toast.LENGTH_SHORT).show();
        ErrorHelper.onError(SudokuApplication.AppContext, e);
        onFailer(e.toString());
    }


    @Override
    public void onComplete() {

    }


    /**
     * 请求成功
     *
     * @param object
     */
    protected abstract void onSuccess(Object object);

    /**
     * 请求失败
     *
     * @param errStr 失败原因
     */
    protected abstract void onFailer(String errStr);


}
