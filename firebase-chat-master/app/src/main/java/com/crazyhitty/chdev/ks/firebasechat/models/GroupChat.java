package com.crazyhitty.chdev.ks.firebasechat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class GroupChat {
    public String sender;
    public String groupid;
    public String message;
    public long timestamp;
    public String groupName;
    public String imageurl;

    public GroupChat() {
    }

    public GroupChat(String groupid, String sender, String message, long timestamp, String groupName, String imageurl) {
        this.sender = sender;
        this.groupid = groupid;
        this.message = message;
        this.timestamp = timestamp;
        this.groupName = groupName;
        this.imageurl = imageurl;
    }
}
