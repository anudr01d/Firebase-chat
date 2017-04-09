package com.crazyhitty.chdev.ks.firebasechat.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONObject;

/**
 * Author: Kartik Sharma
 * Created on: 9/4/2016 , 12:43 PM
 * Project: FirebaseChat
 */

@IgnoreExtraProperties
public class PayloadData {
    public String senderid;
    public String message;

    public PayloadData() {
    }

    public PayloadData(String senderid, String message) {
        this.senderid = senderid;
        this.message = message;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("senderid", senderid);
            jo.put("message", message);
        } catch (Exception e) {
        }
        return  jo;
    }
}
