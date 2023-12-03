package com.mobdeve.s17.aquino.melanie.restorate;

import com.google.firebase.auth.FirebaseUser;

public class ReviewsData {
    //String name;
    FirebaseUser user;
    String loc;
    String quality;
    String overall;
    String env;
    String service;
    String image;


    public ReviewsData(FirebaseUser user,String loc, String quality,String service,String environment, String overall, String image){
        this.loc=loc;
        this.quality=quality;
        this.overall=overall;
        this.image = image;
        this.env=environment;
        this.user =user;
        this.service =service;
    }

    public String getImage(){
        return this.image;
    }

    public FirebaseUser getUser(){
        return this.user;
    }
    public String getLoc(){
        return this.loc;
    }
    public String getQuality(){
        return this.quality;
    }
    public String getEnv(){
        return this.env;
    }

    public String getOverall(){
        return this.overall;
    }
    public String getService(){
        return this.service;
    }
}
