package com.example.draw4u.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import static androidx.core.content.ContextCompat.getColor;
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
        String keyword1 = null;
        String keyword2 = null;
        String keyword3 = null;
        holder.dateView.setText(mDataset.get(position).getResultdiary().getDate());
        if(mDataset.get(position).getResultdiary().getKeyword1() == null){
            keyword1 = "";
            holder.keywordView1.setVisibility(View.GONE);
        }
        else{
            keyword1 = "#" + mDataset.get(position).getResultdiary().getKeyword1();
            holder.keywordView1.setVisibility(View.VISIBLE);
        }
        if(mDataset.get(position).getResultdiary().getKeyword2() == null){
            keyword2 = "";
            holder.keywordView2.setVisibility(View.GONE);
        }
        else{
            keyword2 = "#" + mDataset.get(position).getResultdiary().getKeyword2();
            holder.keywordView2.setVisibility(View.VISIBLE);
        }
        if(mDataset.get(position).getResultdiary().getKeyword3() == null){
            keyword3 = "";
            holder.keywordView3.setVisibility(View.GONE);
        }
        else{
            keyword3 = "#" + mDataset.get(position).getResultdiary().getKeyword3();
            holder.keywordView3.setVisibility(View.VISIBLE);
        }

        holder.keywordView1.setText(keyword1);
        holder.keywordView2.setText(keyword2);
        holder.keywordView3.setText(keyword3);
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
        public TextView keywordView1;
        public TextView keywordView2;
        public TextView keywordView3;

        public CardView cv;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            dateView = (TextView) view.findViewById(R.id.date);
            keywordView1 = (TextView) view.findViewById(R.id.keyword1);
            keywordView2 = (TextView) view.findViewById(R.id.keyword2);
            keywordView3 = (TextView) view.findViewById(R.id.keyword3);
            cv = (CardView) view.findViewById(R.id.cardview);
            cv.setBackgroundColor(Color.argb(0,0,0,0));

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


