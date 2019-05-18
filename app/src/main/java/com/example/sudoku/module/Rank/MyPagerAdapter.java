package com.example.sudoku.module.Rank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sudoku.R;
import com.example.sudoku.bean.Rank;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<List<Rank>> lists;
    private String [] strings;

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }

    public MyPagerAdapter(Context mContext, List<List<Rank>> lists){
        this.mContext=mContext;
        this.lists=lists;
        strings=new String[] {"测试","初级", "中级", "高级"};
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_viewpager,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.lv_rank);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        RankAdapter rankAdapter=new RankAdapter(lists.get(position));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rankAdapter);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
