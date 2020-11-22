package com.example.draw4u.ui.dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.draw4u.DiaryInfo;
import com.example.draw4u.R;
import com.example.draw4u.ResultDiary;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<ResultDiary> mDataset;
    public MyAdapter(ArrayList<ResultDiary> searchDataSet) {
            mDataset = searchDataSet;
            }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
            }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateView.setText(mDataset.get(position).getResultdiary().getDate());
        String keywords = "#" + mDataset.get(position).getResultdiary().getKeyword1() + " #"
                + mDataset.get(position).getResultdiary().getKeyword2()
                + " #" + mDataset.get(position).getResultdiary().getKeyword3();
        holder.keywordView.setText(keywords);

        holder.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click 시 필요한 동작 정의
            }
        });
    }

    @Override
    public int getItemCount() {
            return mDataset.size();
            }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateView;
        public TextView keywordView;

        public ViewHolder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.date);
            keywordView = (TextView) view.findViewById(R.id.keywords);

        }
    }


}


