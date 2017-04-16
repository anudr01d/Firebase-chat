package com.crazyhitty.chdev.ks.firebasechat.core.registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:10 AM
 * Project: FirebaseChat
 */

public class RegisterPresenter implements RegisterContract.Presenter, RegisterContract.OnRegistrationListener, RegisterContract.OnEmailVerificationSent {
    private RegisterContract.View mRegisterView;
    private RegisterInteractor mRegisterInteractor;

    public RegisterPresenter(RegisterContract.View registerView) {
        this.mRegisterView = registerView;
        mRegisterInteractor = new RegisterInteractor(this, this);
    }

    @Override
    public void register(Activity activity, String email, String password, String username) {
        mRegisterInteractor.performFirebaseRegistration(activity, email, password, username);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser, String username) {
        mRegisterView.onRegistrationSuccess(firebaseUser, username);
    }


    @Override
    public void sendVerificationEmail(){
        mRegisterInteractor.sendVerificationEmail();
    }

    @Override
    public void onEmailSuccess(FirebaseUser firebaseUser){
        mRegisterView.onEmailVerificationSuccess(firebaseUser);
    }

    @Override
    public void onEmailFailure(String message){
        mRegisterView.onEmailVerificationFailure(message);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);
    }
}
