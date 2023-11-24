package com.mobdeve.s17.aquino.melanie.restorate;

public class ReviewsData {
    //String name;
    UserData user;
    String loc;
    String quality;
    String overall;
    String env;
    String service;
    int image;


    public ReviewsData(UserData user, String loc, String quality,String service,String environment, String overall, int image){
        this.loc=loc;
        this.quality=quality;
        this.overall=overall;
        this.image = image;
        this.env=environment;
        this.user =user;
        this.service =service;
    }

    public int getImage(){
        return this.image;
    }

    public UserData getUser(){
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
