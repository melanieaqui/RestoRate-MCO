package com.mobdeve.s17.aquino.melanie.restorate;

public class RestoData {
    String restoName;
    String restoType;
    Integer image;
    double rating;
    public RestoData(String restoName, String restoType, int image, double rating) {
        this.restoName = restoName;
        this.restoType = restoType;
        this.image = image;
        this.rating= rating;

    }
    public String getName(){
        return this.restoName;
    }

    public String getType(){
        return this.restoType;
    }

    public int getImage(){
        return this.image;
    }
    public double getRating(){
        return this.rating;
    }
}
