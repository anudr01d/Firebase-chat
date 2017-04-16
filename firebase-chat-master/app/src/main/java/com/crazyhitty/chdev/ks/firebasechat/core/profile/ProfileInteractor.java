package com.crazyhitty.chdev.ks.firebasechat.core.profile;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;


public class ProfileInteractor implements ProfileContract.Interactor {
    private ProfileContract.OnSaveProfileListener mOnSaveProfileListener;

    public ProfileInteractor(ProfileContract.OnSaveProfileListener onSaveProfileListener) {
        this.mOnSaveProfileListener = onSaveProfileListener;
    }

    @Override
    public void performFirebaseSaveProfile(final Activity activity) {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        Group group = new Group(groupName, userList,
//                new SharedPrefUtil(activity.getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN));
//        group.setGroupID(UUID.randomUUID().toString());
//        database.child(Constants.ARG_GROUPS)
//                .child(group.getGroupName())
//                .setValue(group)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            mOnSaveProfileListener.onSuccess(activity.getApplicationContext().getString(R.string.group_successfully_added));
//                        } else {
//                            mOnSaveProfileListener.onFailure(activity.getApplicationContext().getString(R.string.group_unable_to_add));
//                        }
//                    }
//                });
    }
}
