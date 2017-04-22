package com.crazyhitty.chdev.ks.firebasechat.core.chat.group;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;


public class GroupChatPresenter implements GroupChatContract.Presenter, GroupChatContract.OnSendMessageListener,
        GroupChatContract.OnGetMessagesListener, ChatContract.OnUploadImageSuccess  {
    private GroupChatContract.View mView;
    private GroupChatInteractor mChatInteractor;

    public GroupChatPresenter(GroupChatContract.View view) {
        this.mView = view;
        mChatInteractor = new GroupChatInteractor(this, this, this);
    }

    @Override
    public void uploadImage(Bitmap bmp) {
        mChatInteractor.uploadImageToFirebase(bmp);
    }

    @Override
    public void sendMessage(Context context, GroupChat chat) {
        mChatInteractor.sendMessageToFirebaseUser(context, chat);
    }

    @Override
    public void getMessage(String groupid, String senderid) {
        mChatInteractor.getMessageFromFirebaseUser(groupid, senderid);
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
    public void onGetMessagesSuccess(GroupChat chat) {
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
