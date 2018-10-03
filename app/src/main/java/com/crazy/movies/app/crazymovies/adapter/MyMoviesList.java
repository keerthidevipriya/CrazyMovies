package com.crazy.movies.app.crazymovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crazy.movies.app.crazymovies.utils.HttpUrlResponse;
import com.crazy.movies.app.crazymovies.MainActivity;
import com.crazy.movies.app.crazymovies.pojo.MyUserJson;
import com.crazy.movies.app.crazymovies.R;
import com.crazy.movies.app.crazymovies.SecondMovieDetails;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;


public class MyMoviesList extends RecyclerView.Adapter<MyMoviesList.MyMovieView> {
    Context c;
    ArrayList<MyUserJson> jsonMvImages;
    ArrayList<String> jsonMvDetails = new ArrayList<String>();

    public MyMoviesList(MainActivity mainActivity, ArrayList<MyUserJson> jsonMovieImages) {
        this.c = mainActivity;
        this.jsonMvImages = jsonMovieImages;
    }

    @Override
    public MyMoviesList.MyMovieView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.activity_first, parent, false);
        return new MyMovieView(v);
    }

    @Override
    public void onBindViewHolder(MyMoviesList.MyMovieView holder, int position) {
        URL s = HttpUrlResponse.buildImageUrl(jsonMvImages.get(position).getPoster_path());
        Picasso.with(c).load(s.toString()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return jsonMvImages.size();
    }

    public class MyMovieView extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyMovieView(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.first_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    jsonMvDetails.add(0, jsonMvImages.get(pos).getOriginal_title());
                    jsonMvDetails.add(1, jsonMvImages.get(pos).getBackdrop_path());
                    jsonMvDetails.add(2, jsonMvImages.get(pos).getPoster_path());
                    jsonMvDetails.add(3, Double.toString(jsonMvImages.get(pos).getVote_average()));
                    jsonMvDetails.add(4, jsonMvImages.get(pos).getOverview());
                    jsonMvDetails.add(5, jsonMvImages.get(pos).getRelease_date());
                    jsonMvDetails.add(6, "" + jsonMvImages.get(pos).getId());
                    Intent i = new Intent(c, SecondMovieDetails.class);
                    i.putStringArrayListExtra("jTitle", jsonMvDetails);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
