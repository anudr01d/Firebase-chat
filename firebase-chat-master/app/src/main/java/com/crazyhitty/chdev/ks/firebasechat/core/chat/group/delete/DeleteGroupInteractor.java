package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.delete;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class DeleteGroupInteractor implements DeleteGroupContract.Interactor {
    private static final String TAG = "GetGroupsInteractor";

    private DeleteGroupContract.OnDeleteGroupListener mOnGetAllGroupsListener;

    public DeleteGroupInteractor(DeleteGroupContract.OnDeleteGroupListener onGetAllUsersListener) {
        this.mOnGetAllGroupsListener = onGetAllUsersListener;
    }


    @Override
    public void deleteGroupFromFirebase(String groupId, String key, final String userEmail) {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.ARG_GROUPS)
                .child(groupId)
                .child("userList")
                .child(key).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mOnGetAllGroupsListener.onDeleteGroupSuccess("Deletion successful");
                        } else {
                            mOnGetAllGroupsListener.onDeleteGroupFailure("Deletion failure");
                        }
                    }
                });
//
//                addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
//                while (dataSnapshots.hasNext()) {
//                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
//                    User user = dataSnapshotChild.getValue(User.class);
//                    if(user.email.equals(userEmail)) {
//                        dataSnapshots.remove();
//                    }
//                }
//                mOnGetAllGroupsListener.onDeleteGroupSuccess("");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                mOnGetAllGroupsListener.onDeleteGroupFailure(databaseError.getMessage());
//            }
//        });
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        database.child(Constants.ARG_GROUPS)
//                .child(groupName)
//                .child("userList").
//        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
//        hopperUpdates.put("nickname", "Amazing Grace");
//
//        hopperRef.updateChildren(hopperUpdates);
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
}
