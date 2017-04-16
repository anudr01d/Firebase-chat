package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.delete;

public class DeleteGroupPresenter implements DeleteGroupContract.Presenter, DeleteGroupContract.OnDeleteGroupListener {
    private DeleteGroupContract.View mView;
    private DeleteGroupInteractor mDeleteGroupInteractor;

    public DeleteGroupPresenter(DeleteGroupContract.View view) {
        this.mView = view;
        mDeleteGroupInteractor = new DeleteGroupInteractor(this);
    }

    @Override
    public void deleteGroup(String groupid, String key, String email) {
        mDeleteGroupInteractor.deleteGroupFromFirebase(groupid,key,email);
    }

    @Override
    public void onDeleteGroupSuccess(String message) {
        mView.onDeleteGroupSuccess(message);
    }

    @Override
    public void onDeleteGroupFailure(String message) {
        mView.onDeleteGroupFailure(message);
    }
}
