package com.example.sudoku.base;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.sudoku.common.ActivityController;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
//import com.uber.autodispose.AutoDispose;
//import com.uber.autodispose.AutoDisposeConverter;
//import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * @author sunxin
 * @date 2019-04-29 14:39
 * @desc
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        // Activity 管理类
        ActivityController.addActivity(this);
        // 初始化View
        initView(savedInstanceState);
        // 初始化数据
        initData();

    }

    protected abstract int getContentLayoutId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initData();

    /**
     * 绑定Activity的周期，自动解除RxJava的订阅关系，防止内存泄漏
     */
    public <X> AutoDisposeConverter<X> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
    }

}
