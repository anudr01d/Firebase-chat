package com.crazyhitty.chdev.ks.firebasechat.core.registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:36 AM
 * Project: FirebaseChat
 */

public interface RegisterContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser, String username);
        void onRegistrationFailure(String message);

        void onEmailVerificationSuccess(FirebaseUser firebaseUser);
        void onEmailVerificationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String email, String password, String username);
        void sendVerificationEmail();
    }

    interface Interactor {
        void performFirebaseRegistration(Activity activity, String email, String password, String username);
        void sendVerificationEmail();
    }

    interface OnRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser, String username);
        void onFailure(String message);
    }

    interface OnEmailVerificationSent {
        void onEmailSuccess(FirebaseUser firebaseUser);
        void onEmailFailure(String message);
    }
}
