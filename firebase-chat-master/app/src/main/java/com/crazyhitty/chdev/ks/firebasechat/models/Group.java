package com.crazyhitty.chdev.ks.firebasechat.models;

import java.util.List;
import java.util.Map;

/**
 * Created by anudeep on 08/04/17.
 */

public class Group {
    public String groupName;
    public String groupID;
    public Map<String, User> userList;
    public String firebaseToken;

    public Group() {
    }

    public Group(String groupName, Map<String, User> userList, String firebaseToken) {
        this.groupName = groupName;
        this.userList = userList;
        this.firebaseToken = firebaseToken;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Map<String, User> getUserList() {
        return userList;
    }

    public void setUserList(Map<String, User> userList) {
        this.userList = userList;
    }
}
