package com.mobdeve.s17.aquino.melanie.restorate;

public class UserData {
    //String name;
    String email;
    int profile;
    public UserData(/*String name, */String email, int profile){
        //this.name=name;
        this.email =email;
        this.profile = profile;
    }

    public int getProfile(){
        return this.profile;
    }
    public String getEmail(){
        return this.email;
    }
}
