package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;


public class CreateGroupInteractor implements CreateGroupContract.Interactor {
    private CreateGroupContract.OnCreateGroupListener mOnCreateGroupListener;

    public CreateGroupInteractor(CreateGroupContract.OnCreateGroupListener onLoginListener) {
        this.mOnCreateGroupListener = onLoginListener;
    }

    @Override
    public void performFirebaseCreateGroup(final Activity activity, final String groupName, final List<User> userList) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final String groupid = UUID.randomUUID().toString();
        final Group group = new Group(groupName, null,
                new SharedPrefUtil(activity.getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN));
        group.setGroupID(groupid);
        database.child(Constants.ARG_GROUPS)
                .child(groupid)//group id
                .setValue(group)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addUserList(activity,userList, groupid);
                            //mOnCreateGroupListener.onSuccess(activity.getApplicationContext().getString(R.string.group_successfully_added));
                        } else {
                            //mOnCreateGroupListener.onFailure(activity.getApplicationContext().getString(R.string.group_unable_to_add));
                        }
                    }
                });
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
//                            mOnCreateGroupListener.onSuccess(activity.getApplicationContext().getString(R.string.group_successfully_added));
//                        } else {
//                            mOnCreateGroupListener.onFailure(activity.getApplicationContext().getString(R.string.group_unable_to_add));
//                        }
//                    }
//                });
    }

    private void addUserList(final Activity activity, List<User> list, String groupid) {
        for(User usr : list) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(Constants.ARG_GROUPS)
                    .child(groupid)
                    .child("userList").push().setValue(usr)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }
        mOnCreateGroupListener.onSuccess(activity.getApplicationContext().getString(R.string.group_successfully_added));
    }
}
