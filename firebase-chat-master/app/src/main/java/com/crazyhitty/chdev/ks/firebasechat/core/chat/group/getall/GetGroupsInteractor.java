package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.getall;

import android.text.TextUtils;

import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GetGroupsInteractor implements GetGroupsContract.Interactor {
    private static final String TAG = "GetGroupsInteractor";

    private GetGroupsContract.OnGetAllGroupsListener mOnGetAllGroupsListener;

    public GetGroupsInteractor(GetGroupsContract.OnGetAllGroupsListener onGetAllUsersListener) {
        this.mOnGetAllGroupsListener = onGetAllUsersListener;
    }


    @Override
    public void getAllGroupsFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_GROUPS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<Group> groups = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    Group group = dataSnapshotChild.getValue(Group.class);
                    groups.add(group);
                }
                mOnGetAllGroupsListener.onGetAllGroupsSuccess(groups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllGroupsListener.onGetAllGroupsFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getChatGroupsFromFirebase() {
        /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef().
                    Chat chat=dataSnapshotChild.getValue(Chat.class);
                    if(chat.)4
                    if(!TextUtils.equals(user.uid,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
