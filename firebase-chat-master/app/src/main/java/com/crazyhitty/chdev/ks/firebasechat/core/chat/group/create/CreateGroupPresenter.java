package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create;

import android.app.Activity;

import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:10 AM
 * Project: FirebaseChat
 */

public class CreateGroupPresenter implements CreateGroupContract.Presenter, CreateGroupContract.OnCreateGroupListener {
    private CreateGroupContract.View mCreateGroupView;
    private CreateGroupInteractor mCreaterGroupInteractor;

    public CreateGroupPresenter(CreateGroupContract.View loginView) {
        this.mCreateGroupView = loginView;
        mCreaterGroupInteractor = new CreateGroupInteractor(this);
    }

    @Override
    public void createGroup(Activity activity, String groupName, List<User> groupList) {
        mCreaterGroupInteractor.performFirebaseCreateGroup(activity, groupName, groupList);
    }

    @Override
    public void onSuccess(String message) {
        mCreateGroupView.onCreateGroupSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mCreateGroupView.onCreateGroupFailure(message);
    }
}
