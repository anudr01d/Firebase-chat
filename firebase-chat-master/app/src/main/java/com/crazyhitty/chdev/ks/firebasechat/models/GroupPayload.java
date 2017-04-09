package com.crazyhitty.chdev.ks.firebasechat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.eventbus.util.ExceptionToResourceMapping;
import org.json.JSONObject;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class GroupPayload {
    public String to;
    public PayloadData data;

    public GroupPayload() {
    }

    public GroupPayload(String to, PayloadData data) {
        this.to = to;
        this.data = data;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("to", to);
            jo.put("data", data.toJSON());
        } catch(Exception e) {
        }

        return jo;
    }
}
