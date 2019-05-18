package com.example.sudoku.module.Rank;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.sudoku.R;
import com.example.sudoku.base.BaseActivity;
import com.example.sudoku.base.BaseBean;
import com.example.sudoku.bean.Rank;
import com.example.sudoku.http.RequestCallback;
import com.example.sudoku.http.RetrofitManager;
import com.example.sudoku.http.RxSchedulers;
import com.example.sudoku.http.service.ApiService;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends BaseActivity {

    private ArrayList<List<Rank>> rankArrayList;

    private ViewPager vp_rank;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rank;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        vp_rank=findViewById(R.id.vp_rank);
    }

    @Override
    public void initData() {
        rankArrayList=new ArrayList<>();
        getScore();
    }

    private void getScore(){
        RetrofitManager
                .getRetrofit()
                .create(ApiService.class)
                .getScore()
                .compose(RxSchedulers.applySchedulers())
                .as(bindAutoDispose())
                .subscribe(new RequestCallback<BaseBean<List<List<Rank>>>>() {
                    @Override
                    protected void onSuccess(Object object) {
                       rankArrayList=(ArrayList<List<Rank>>)object;
                       vp_rank.setAdapter(new MyPagerAdapter(RankActivity.this,rankArrayList));
                    }
                    @Override
                    protected void onFailer(String errStr) {
                    }
                });
    }
}
