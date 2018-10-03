package com.crazy.movies.app.crazymovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazy.movies.app.crazymovies.pojo.MyYoutubeUserJson;
import com.crazy.movies.app.crazymovies.R;
import com.crazy.movies.app.crazymovies.SecondMovieDetails;

import java.util.ArrayList;

public class MyVideoList extends RecyclerView.Adapter<MyVideoList.MyVideoView> {
    Context c;
    ArrayList<MyYoutubeUserJson> jsonVideoList;

    public MyVideoList(SecondMovieDetails secondMovieDetails, ArrayList<MyYoutubeUserJson> jsonYoutubeLinks) {
        this.c = secondMovieDetails;
        this.jsonVideoList = jsonYoutubeLinks;
    }

    @Override
    public MyVideoList.MyVideoView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.youtubelink, parent, false);
        return new MyVideoView(v);
    }

    @Override
    public void onBindViewHolder(MyVideoList.MyVideoView holder, int position) {
        final MyYoutubeUserJson myYoutubeUserJson = jsonVideoList.get(position);
        holder.textView.setText(myYoutubeUserJson.getName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(myYoutubeUserJson.getVideoUrl()));
                c.startActivity(i);
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(myYoutubeUserJson.getVideoUrl()));
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonVideoList.size();
    }

    public class MyVideoView extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyVideoView(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trailerName);
            imageView = itemView.findViewById(R.id.videoplay);
        }
    }
}
