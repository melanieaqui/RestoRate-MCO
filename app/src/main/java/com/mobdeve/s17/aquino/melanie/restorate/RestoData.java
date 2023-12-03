package com.mobdeve.s17.aquino.melanie.restorate;

import android.util.Log;

public class RestoData {

    String foodtype;
    String image;
    String name;
    double rating=0.0;
    public RestoData(){

    }
    public RestoData(String foodtype,  String image,String name) {
        this.name = name;
        this.foodtype = foodtype;
        this.image = image;

        Log.d("food_type",foodtype);

    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getName(){
        return this.name;
    }

    public String getFoodtype(){
        return this.foodtype;
    }

    public String getImage(){
        return this.image;
    }
    public double getRating(){
        return this.rating;
    }
}
