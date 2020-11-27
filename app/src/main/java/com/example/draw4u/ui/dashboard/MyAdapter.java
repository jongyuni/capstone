package com.example.draw4u.ui.dashboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.draw4u.DiaryDayView;
import com.example.draw4u.R;
import com.example.draw4u.ResultDiary;


import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private static ArrayList<ResultDiary> mDataset;
    public MyAdapter(ArrayList<ResultDiary> searchDataSet) { mDataset = searchDataSet; }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String keywords = null;
        holder.dateView.setText(mDataset.get(position).getResultdiary().getDate());
        if(mDataset.get(position).getResultdiary().getKeyword1() == null){
            keywords = "  ";
        }
        else{
            keywords = "#" + mDataset.get(position).getResultdiary().getKeyword1();
        }
        if(mDataset.get(position).getResultdiary().getKeyword2() == null){
            keywords = keywords + "";
        }
        else{
            keywords = keywords
                    +" #" + mDataset.get(position).getResultdiary().getKeyword2();
        }
        if(mDataset.get(position).getResultdiary().getKeyword3() == null){
            keywords = keywords + "";
        }
        else{
            keywords = keywords
                    +" #" + mDataset.get(position).getResultdiary().getKeyword3();
        }

        holder.keywordView.setText(keywords);
        Glide.with(holder.imageView.getContext())
                .load(mDataset.get(position).getResultdiary().getImageURL())
                .into(holder.imageView);//이미지 출력
    }

    @Override
    public int getItemCount() {
            return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView dateView;
        public TextView keywordView;
        public CardView cv;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            dateView = (TextView) view.findViewById(R.id.date);
            keywordView = (TextView) view.findViewById(R.id.keywords);
            cv = (CardView) view.findViewById(R.id.cardview);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),DiaryDayView.class);
                    intent.putExtra("fname",mDataset.get(getAdapterPosition()).getResultdiary().getDate());
                    startActivity(v.getContext(),intent,null);
                }
            });
        }
    }


}


