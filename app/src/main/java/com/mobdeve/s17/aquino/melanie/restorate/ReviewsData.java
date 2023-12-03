package com.mobdeve.s17.aquino.melanie.restorate;

import com.google.firebase.auth.FirebaseUser;

public class ReviewsData {
    //String name;
    String username;
    String loc;
    String quality;
    float rating;
    String service;
    String image;
    String userimg;
    String environment;

    public ReviewsData(){}
    public ReviewsData(String username,String userimg,String loc, String quality,String service,String environment, Float rating, String image){
        this.loc=loc;
        this.quality=quality;
        this.rating=rating;
        this.image = image;
        this.environment=environment;
        this.username = username;
        this.service =service;
        this.userimg =userimg;
    }

    public String getImage(){
        return this.image;
    }

    public String getUsername(){
        return this.username;
    }
    public String getUserImg(){return this.userimg;}
    public String getLoc(){
        return this.loc;
    }
    public String getQuality(){
        return this.quality;
    }
    public String getEnvironment(){
        return this.environment;
    }

    public Float getRating(){
        return this.rating;
    }
    public String getService(){
        return this.service;
    }
}
