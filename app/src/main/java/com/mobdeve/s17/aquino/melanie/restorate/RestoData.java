package com.mobdeve.s17.aquino.melanie.restorate;

public class RestoData {
    String name;
    String food_type;
    String image;
    double rating=0.0;
    public RestoData(){}
    public RestoData(String food_type, String name, String image,double rating) {
        this.name = name;
        this.food_type = food_type;
        this.image = image;
        this.rating= rating;

    }
    public RestoData(String food_type, String name, String image) {
        this.name = name;
        this.food_type = food_type;
        this.image = image;

    }
    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.food_type;
    }

    public String getImage(){
        return this.image;
    }
    public double getRating(){
        return this.rating;
    }
}
