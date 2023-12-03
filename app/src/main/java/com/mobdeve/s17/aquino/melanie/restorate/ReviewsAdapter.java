package com.mobdeve.s17.aquino.melanie.restorate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    ArrayList<ReviewsData> reviewsData;
    Context context;
    public ReviewsAdapter(ArrayList<ReviewsData>  reviewsData, Activity activity) {
        this.reviewsData = reviewsData;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ReviewsData ReviewsDataList = reviewsData.get(position);
        holder.reviews_user.setText(ReviewsDataList.getUsername());

        holder.reviews_location.setText(ReviewsDataList.getLoc());
        holder.reviews_quality.setText("Qty: "+ReviewsDataList.getQuality());
        holder.reviews_service.setText("Service: "+ReviewsDataList.getService());
        holder.reviews_environment.setText("Environment: "+ReviewsDataList.getEnvironment());
        holder.reviews_overall.setRating(ReviewsDataList.getRating());

        if(ReviewsDataList.getImage()!=null)
            Picasso.with(context).load(Uri.parse(ReviewsDataList.getUserImg())).into(holder.reviews_user_image);
        else
            holder.reviews_user_image.setImageResource(R.drawable.ic_profile);
        if(ReviewsDataList.getImage()!=null)
            Picasso.with(context).load(Uri.parse(ReviewsDataList.getImage())).into(holder.reviews_image);

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
        return reviewsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView reviews_image;
        ImageView reviews_user_image;
        TextView reviews_user;
        TextView reviews_location;
        TextView reviews_quality;
        TextView reviews_service;
        TextView reviews_environment;
        RatingBar reviews_overall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviews_image = itemView.findViewById(R.id.image_review);
            reviews_user_image = itemView.findViewById(R.id.image_profile);

            reviews_user = itemView.findViewById(R.id.txt_user);
            reviews_location = itemView.findViewById(R.id.txt_location);
            reviews_quality = itemView.findViewById(R.id.txt_food);
            reviews_service = itemView.findViewById(R.id.txt_service);
            reviews_environment = itemView.findViewById(R.id.txt_environment);
            reviews_overall = itemView.findViewById(R.id.ratingBar);


        }
    }

}
