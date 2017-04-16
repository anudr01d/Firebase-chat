package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.delete;

import com.crazyhitty.chdev.ks.firebasechat.models.Group;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface DeleteGroupContract {
    interface View {
        void onDeleteGroupSuccess(String message);
        void onDeleteGroupFailure(String message);
    }

    interface Presenter {
        void deleteGroup(String groupId, String key,  String userEmail);
    }

    interface Interactor {
        void deleteGroupFromFirebase(String groupId, String key,  String userEmail);
    }

    interface OnDeleteGroupListener {
        void onDeleteGroupSuccess(String message);
        void onDeleteGroupFailure(String message);
    }
}
