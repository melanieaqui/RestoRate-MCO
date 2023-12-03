package com.mobdeve.s17.aquino.melanie.restorate;

import static java.lang.String.valueOf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.ViewHolder> {
    ArrayList<RestoData> restoData =new ArrayList<>();
    ArrayList<RestoData> filteredNameList =new ArrayList<>();

    Context context;

    public RestoAdapter(ArrayList<RestoData> restoData, Activity activity) {
        this.restoData = restoData;
        this.context = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.resto_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RestoData RestoDataList = restoData.get(position);
        holder.restoName.setText(RestoDataList.getName());
        holder.restoType.setText(RestoDataList.getFoodtype());
       // Log.i("get_type result",RestoDataList.getType().toString());
        if (RestoDataList.getImage()!=null)
            Picasso.with(context).load(Uri.parse(RestoDataList.getImage())).into(holder.restoImage);
        holder.restoRating.setText(valueOf(RestoDataList.getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, ViewRestoActivity.class);
                intent.putExtra("RESTO_NAME",RestoDataList.getName());
                intent.putExtra("RESTO_TYPE",RestoDataList.getFoodtype());
                intent.putExtra("RESTO_IMAGE",RestoDataList.getImage());
                intent.putExtra("RESTO_RATING",valueOf(RestoDataList.getRating()));
                context.startActivity(intent);

                /* Remove this and replace it with an intent call*/
                //Toast.makeText(context, "")
            }
        });
    }

    public void setFilteredList(ArrayList<RestoData>filtered){
        this.restoData = filtered;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return restoData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView restoImage;
        TextView restoName;
        TextView restoType;
        TextView restoRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restoImage = itemView.findViewById(R.id.resto_image);
            restoName = itemView.findViewById(R.id.resto_name);
            restoType = itemView.findViewById(R.id.txt_resto_type);
            restoRating = itemView.findViewById(R.id.resto_rating);
        }
    }

}