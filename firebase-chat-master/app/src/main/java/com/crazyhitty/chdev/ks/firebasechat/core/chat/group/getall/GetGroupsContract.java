package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.getall;

import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface GetGroupsContract {
    interface View {
        void onGetAllGroupsSuccess(List<Group> Groups);

        void onGetAllGroupsFailure(String message);

        void onGetChatGroupsSuccess(List<Group> Groups);

        void onGetChatGroupsFailure(String message);
    }

    interface Presenter {
        void getAllGroups();

        void getChatGroups();
    }

    interface Interactor {
        void getAllGroupsFromFirebase();

        void getChatGroupsFromFirebase();
    }

    interface OnGetAllGroupsListener {
        void onGetAllGroupsSuccess(List<Group> Groups);

        void onGetAllGroupsFailure(String message);
    }

    interface OnGetChatGroupsListener {
        void onGetChatGroupsSuccess(List<Group> Groups);

        void onGetChatGroupsFailure(String message);
    }
}
