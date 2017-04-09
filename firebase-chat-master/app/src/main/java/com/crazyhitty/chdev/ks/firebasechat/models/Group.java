package com.crazyhitty.chdev.ks.firebasechat.models;

import java.util.List;

/**
 * Created by anudeep on 08/04/17.
 */

public class Group {
    public String groupName;
    public String groupID;
    public List<User> userList;
    public String firebaseToken;

    public Group() {
    }

    public Group(String groupName, List<User> userList, String firebaseToken) {
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
