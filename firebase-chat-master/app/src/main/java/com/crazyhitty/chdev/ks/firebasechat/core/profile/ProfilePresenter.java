package com.crazyhitty.chdev.ks.firebasechat.core.profile;

import android.app.Activity;

import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:10 AM
 * Project: FirebaseChat
 */

public class ProfilePresenter implements ProfileContract.Presenter, ProfileContract.OnSaveProfileListener {
    private ProfileContract.View mCreateGroupView;
    private ProfileInteractor mProfileInteractor;

    public ProfilePresenter(ProfileContract.View loginView) {
        this.mCreateGroupView = loginView;
        mProfileInteractor = new ProfileInteractor(this);
    }

    @Override
    public void saveProfile(Activity activity) {
        mProfileInteractor.performFirebaseSaveProfile(activity);
    }

    @Override
    public void onSuccess(String message) {
        mCreateGroupView.onSaveProfileSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mCreateGroupView.onSaveProfileFailure(message);
    }
}
