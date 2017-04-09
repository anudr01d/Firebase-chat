package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create;

import android.app.Activity;

import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Anudeep
 * Created on: 4/8/2017 , 9:11 PM
 * Project: FirebaseChat
 */

public interface CreateGroupContract {
    interface View {
        void onCreateGroupSuccess(String message);
        void onCreateGroupFailure(String message);
    }

    interface Presenter {
        void createGroup(Activity activity, String groupName, List<User> userList);
    }

    interface Interactor {
        void performFirebaseCreateGroup(Activity activity, String groupName, List<User> userList);
    }

    interface OnCreateGroupListener {
        void onSuccess(String message);
        void onFailure(String message);
    }
}
