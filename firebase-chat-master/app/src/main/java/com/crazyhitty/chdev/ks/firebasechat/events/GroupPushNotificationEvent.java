package com.crazyhitty.chdev.ks.firebasechat.events;


public class GroupPushNotificationEvent {
    private String groupid;
    private String sender;

    public GroupPushNotificationEvent() {
    }

    public GroupPushNotificationEvent(String groupid, String sender) {
        this.groupid = groupid;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
