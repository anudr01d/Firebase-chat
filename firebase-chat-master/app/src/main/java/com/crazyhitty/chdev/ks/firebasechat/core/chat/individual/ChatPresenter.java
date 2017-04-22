package com.crazyhitty.chdev.ks.firebasechat.core.chat.individual;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.crazyhitty.chdev.ks.firebasechat.models.Chat;

/**
 * Author: Kartik Sharma
 * Created on: 9/2/2016 , 10:05 PM
 * Project: FirebaseChat
 */

public class ChatPresenter implements ChatContract.Presenter, ChatContract.OnSendMessageListener,
        ChatContract.OnGetMessagesListener, ChatContract.OnUploadImageSuccess {
    private ChatContract.View mView;
    private ChatInteractor mChatInteractor;

    public ChatPresenter(ChatContract.View view) {
        this.mView = view;
        mChatInteractor = new ChatInteractor(this, this, this);
    }

    @Override
    public void uploadImage(Bitmap bmp) {
        mChatInteractor.uploadImageToFirebase(bmp);
    }

    @Override
    public void sendMessage(Context context, Chat chat, String receiverFirebaseToken) {
        mChatInteractor.sendMessageToFirebaseUser(context, chat, receiverFirebaseToken);
    }

    @Override
    public void getMessage(String senderUid, String receiverUid) {
        mChatInteractor.getMessageFromFirebaseUser(senderUid, receiverUid);
    }

    @Override
    public void onSendMessageSuccess() {
        mView.onSendMessageSuccess();
    }

    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFailure(message);
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        mView.onGetMessagesSuccess(chat);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        mView.onGetMessagesFailure(message);
    }

    @Override
    public void onSendImageSuccess(Uri uri) {
        mView.onUploadImageSuccess(uri);
    }

    @Override
    public void onSendImageFailure(String message) {
        mView.onUploadImageFailure(message);
    }
}
