package com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend;

public class UnsendPresenter implements UnsendContract.Presenter, UnsendContract.OnDeleteMessageListener {
    private UnsendContract.View mView;
    private UnsendInteractor mDeleteGroupInteractor;

    public UnsendPresenter(UnsendContract.View view) {
        this.mView = view;
        mDeleteGroupInteractor = new UnsendInteractor(this);
    }

    @Override
    public void deleteMessage(String roomType, String roomType2, long timestamp) {
        mDeleteGroupInteractor.deleteMessageFromFirebase(roomType, roomType2,timestamp);
    }

    @Override
    public void deleteGroupMessage(String roomType, long timestamp){
        mDeleteGroupInteractor.deleteGroupMessageFromFirebase(roomType, timestamp);
    }

    @Override
    public void onDeleteMessageSuccess(String message) {
        mView.onDeleteMessageSuccess(message);
    }

    @Override
    public void onDeleteMessageFailure(String message) {
        mView.onDeleteMessageFailure(message);
    }
}
