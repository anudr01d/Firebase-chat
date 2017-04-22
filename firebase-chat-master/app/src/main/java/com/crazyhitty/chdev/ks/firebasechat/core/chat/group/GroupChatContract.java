package com.crazyhitty.chdev.ks.firebasechat.core.chat.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface GroupChatContract {
    interface View {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);

        void onGetMessagesSuccess(GroupChat chat);

        void onGetMessagesFailure(String message);

        void onUploadImageSuccess(Uri uri);
        void onUploadImageFailure(String message);
    }

    interface Presenter {
        void uploadImage(Bitmap bmp);
        void sendMessage(Context context, GroupChat chat);

        void getMessage(String senderUid, String receiverUid);
    }

    interface Interactor {
        void uploadImageToFirebase(Bitmap bmp);
        void sendMessageToFirebaseUser(Context context, GroupChat chat);

        void getMessageFromFirebaseUser(String senderUid, String receiverUid);
    }

    interface OnSendMessageListener {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);
    }

    interface OnUploadImageSuccess {
        void onSendImageSuccess(Uri uri);
        void onSendImageFailure(String message);
    }

    interface OnGetMessagesListener {
        void onGetMessagesSuccess(GroupChat chat);

        void onGetMessagesFailure(String message);
    }
}
