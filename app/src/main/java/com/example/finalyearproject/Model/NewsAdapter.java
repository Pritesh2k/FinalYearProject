package com.example.finalyearproject.Model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private final LayoutInflater layoutInflater;
    private final Activity activity;
    private final ArrayList<NewsModel> newsModelArrayList;


    public NewsAdapter( Activity activity, ArrayList<NewsModel> newsModelArrayList) {
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.newsModelArrayList = newsModelArrayList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_recyclerview,parent,false);
        NewsHolder newsHolder=new NewsHolder(view,viewType);
        return newsHolder;
    }
    /*For same return item*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

        tvTitle.setText(newsModelArrayList.get(position).getTitle());
        tvPublishAt.setText(newsModelArrayList.get(position).getPublisDate());
        Glide.with(activity).load(newsModelArrayList.get(position).getUrlToImage()).into(img);
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }
    TextView tvTitle,tvContent,tvPublishAt;
    ImageView img;
    public class NewsHolder extends RecyclerView.ViewHolder {
        public NewsHolder(@NonNull View itemView,int viewtype) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvPublishAt=itemView.findViewById(R.id.tvPublishAt);
            img=itemView.findViewById(R.id.img);
            img.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity,DetailsActivity.class);
                    intent.putExtra("URL",newsModelArrayList.get(viewtype).getUrl());
                    activity.startActivity(intent);
                }
            });

        }
    }
}
