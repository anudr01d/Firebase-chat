package com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend;

import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;


public class UnsendInteractor implements UnsendContract.Interactor {
    private static final String TAG = "UnsendInteractor";

    private UnsendContract.OnDeleteMessageListener mOnDeleteMessageListener;

    public UnsendInteractor(UnsendContract.OnDeleteMessageListener onGetAllUsersListener) {
        this.mOnDeleteMessageListener = onGetAllUsersListener;
    }

    @Override
    public void deleteMessageFromFirebase(String roomType, final String roomType2, final long timestamp) {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.ARG_CHAT_ROOMS)
                .child(roomType)
                .child(String.valueOf(timestamp))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            deleteSecondRoom(roomType2, timestamp);
                        } else {
                        }
                    }
                });
    }


    public void deleteSecondRoom(String roomType2, long timestamp){
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.ARG_CHAT_ROOMS)
                .child(roomType2)
                .child(String.valueOf(timestamp))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mOnDeleteMessageListener.onDeleteMessageSuccess("Deletion successful");
                        } else {
                            mOnDeleteMessageListener.onDeleteMessageFailure("Deletion failure");
                        }
                    }
                });
    }

    @Override
    public void deleteGroupMessageFromFirebase(String roomType, long timestamp) {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.ARG_GROUP_CHAT_ROOMS)
                .child(roomType)
                .child(String.valueOf(timestamp))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mOnDeleteMessageListener.onDeleteMessageSuccess("Deletion successful");
                        } else {
                            mOnDeleteMessageListener.onDeleteMessageFailure("Deletion failure");
                        }
                    }
                });
    }
}
