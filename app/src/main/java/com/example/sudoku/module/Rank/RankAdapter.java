package com.example.sudoku.module.Rank;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sudoku.R;
import com.example.sudoku.bean.Rank;

import java.text.SimpleDateFormat;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<Rank> list;

    public RankAdapter(List<Rank> list){
        this.list=list;
    }
    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ranklist,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder viewHolder, int i) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm分ss秒SS毫秒");
        Rank rank=list.get(i);
        viewHolder.name.setText(rank.getName());
        viewHolder.score.setText(simpleDateFormat.format(Long.valueOf(rank.getScore())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            score=itemView.findViewById(R.id.score);
        }
    }
}
