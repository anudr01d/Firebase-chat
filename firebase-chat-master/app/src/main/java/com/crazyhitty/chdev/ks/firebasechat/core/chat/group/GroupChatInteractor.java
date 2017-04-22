package com.crazyhitty.chdev.ks.firebasechat.core.chat.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.fcm.FcmGroupNotificationBuilder;
import com.crazyhitty.chdev.ks.firebasechat.fcm.FcmNotificationBuilder;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:08 PM
 * Project: FirebaseChat
 */

public class GroupChatInteractor implements GroupChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private GroupChatContract.OnSendMessageListener mOnSendMessageListener;
    private GroupChatContract.OnGetMessagesListener mOnGetMessagesListener;

    private ChatContract.OnUploadImageSuccess mOnImageUploadListener;


    public GroupChatInteractor(GroupChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public GroupChatInteractor(GroupChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public GroupChatInteractor(GroupChatContract.OnSendMessageListener onSendMessageListener,
                          GroupChatContract.OnGetMessagesListener onGetMessagesListener, ChatContract.OnUploadImageSuccess mOnImageUploadListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
        this.mOnImageUploadListener = mOnImageUploadListener;
    }

    @Override
    public void uploadImageToFirebase(Bitmap bmp) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference profileImageRef = storageRef.child("images/"+ System.currentTimeMillis() +".png");
        // Get the data from an ImageView as bytes

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                mOnImageUploadListener.onSendImageFailure("Profile could not be updated");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mOnImageUploadListener.onSendImageSuccess(downloadUrl);
                //updateUserProfile(downloadUrl);
            }
        });
    }

    @Override
    public void sendMessageToFirebaseUser(final Context context, final GroupChat chat) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.ARG_GROUP_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.child(Constants.ARG_GROUP_CHAT_ROOMS)
                        .child(chat.groupid)
                        .child(String.valueOf(chat.timestamp))
                        .setValue(chat);
                getMessageFromFirebaseUser(chat.sender, chat.groupid);

            // send push notification to the receiver
            sendPushNotificationToReceiver(chat.groupid,
                                           chat.sender,
                                           chat.message,
                                            chat.groupName);
                mOnSendMessageListener.onSendMessageSuccess();
        }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String to, String sender, String message, String groupName) {
        FcmGroupNotificationBuilder.initialize()
                .to(to)
                .sender(sender)
                .message(message)
                .groupname(groupName)
                .send();
    }

    @Override
    public void getMessageFromFirebaseUser(final String groupid, final String senderUid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.ARG_GROUP_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG, "getMessageFromFirebaseUser: ");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_GROUP_CHAT_ROOMS)
                            .child(groupid)
                            .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            GroupChat chat = dataSnapshot.getValue(GroupChat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
}
