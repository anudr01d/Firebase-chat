package com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 11:06 AM
 * Project: FirebaseChat
 */

public interface UnsendContract {
    interface View {
        void onDeleteMessageSuccess(String message);
        void onDeleteMessageFailure(String message);
    }

    interface Presenter {
        void deleteMessage(String roomType, long timestamp);
    }

    interface Interactor {
        void deleteMessageFromFirebase(String roomType, long timestamp);
    }

    interface OnDeleteMessageListener {
        void onDeleteMessageSuccess(String message);
        void onDeleteMessageFailure(String message);
    }
}
