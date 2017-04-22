package com.crazyhitty.chdev.ks.firebasechat.core.profile;

import android.app.Activity;
import android.graphics.Bitmap;

import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Anudeep
 * Created on: 4/8/2017 , 9:11 PM
 * Project: FirebaseChat
 */

public interface ProfileContract {
    interface View {
        void onSaveProfileSuccess(String message);
        void onSaveProfileFailure(String message);
    }

    interface Presenter {
        void saveProfile(Bitmap bmp);
    }

    interface Interactor {
        void performFirebaseSaveProfile(Bitmap bmp);
    }

    interface OnSaveProfileListener {
        void onSuccess(String message);
        void onFailure(String message);
    }
}
