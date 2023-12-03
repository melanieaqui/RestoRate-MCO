package com.mobdeve.s17.aquino.melanie.restorate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    com.mobdeve.s17.aquino.melanie.restorate.ReviewsData[] ReviewsData;
    Context context;
    public ReviewsAdapter(com.mobdeve.s17.aquino.melanie.restorate.ReviewsData[] ReviewsData, Activity activity) {
        this.ReviewsData = ReviewsData;
        this.context = activity;
    }
    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.resto_reviews,parent,false);
        ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        final ReviewsData ReviewsDataList = ReviewsData[position];
        holder.reviews_user.setText(ReviewsDataList.getUser().getEmail());
        //holder.reviews_user_image.setImageResource(ReviewsDataList.getUser().getProfile());

        holder.reviews_location.setText(ReviewsDataList.getLoc());
        holder.reviews_quality.setText("Qty: "+ReviewsDataList.getQuality());
        holder.reviews_service.setText("Service: "+ReviewsDataList.getService());
        holder.reviews_environment.setText("Environment: "+ReviewsDataList.getEnv());
        holder.reviews_overall.setText("Overall: "+ReviewsDataList.getOverall());

        //holder.reviews_image.setImageResource(ReviewsDataList.getImage());

        /**holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, ViewRestoActivity.class);


                context.startActivity(intent);

            }
        });*/
    }

    public int getItemCount() {
        return ReviewsData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView reviews_image;
        ImageView reviews_user_image;
        TextView reviews_user;
        TextView reviews_location;
        TextView reviews_quality;
        TextView reviews_service;
        TextView reviews_environment;
        TextView reviews_overall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviews_image = itemView.findViewById(R.id.image_review);
            reviews_user_image = itemView.findViewById(R.id.image_profile);

            reviews_user = itemView.findViewById(R.id.txt_user);
            reviews_location = itemView.findViewById(R.id.txt_location);
            reviews_quality = itemView.findViewById(R.id.txt_food);
            reviews_service = itemView.findViewById(R.id.txt_service);
            reviews_environment = itemView.findViewById(R.id.txt_environment);
            reviews_overall = itemView.findViewById(R.id.txt_overall);




        }
    }

}
