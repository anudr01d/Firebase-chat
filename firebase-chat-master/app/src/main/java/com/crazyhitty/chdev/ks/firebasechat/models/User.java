package com.crazyhitty.chdev.ks.firebasechat.models;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/1/2016 , 8:35 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class User {
    public String uid;
    public String email;
    public String firebaseToken;
    public boolean isSelected;
    public String username;
    public boolean emailverified;
    //public Uri imguri;
    public String imguri;

    public User() {
    }

    public User(String uid, String email, String firebaseToken, String username, boolean emailverified, String imguri) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
        this.username = username;
        this.emailverified = emailverified;
        this.imguri = imguri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
