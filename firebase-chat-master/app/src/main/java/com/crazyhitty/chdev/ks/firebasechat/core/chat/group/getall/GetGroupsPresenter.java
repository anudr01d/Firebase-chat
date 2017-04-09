package com.crazyhitty.chdev.ks.firebasechat.core.chat.group.getall;

import com.crazyhitty.chdev.ks.firebasechat.models.Group;

import java.util.List;

public class GetGroupsPresenter implements GetGroupsContract.Presenter, GetGroupsContract.OnGetAllGroupsListener {
    private GetGroupsContract.View mView;
    private GetGroupsInteractor mGetGroupsInteractor;

    public GetGroupsPresenter(GetGroupsContract.View view) {
        this.mView = view;
        mGetGroupsInteractor = new GetGroupsInteractor(this);
    }

    @Override
    public void getAllGroups() {
        mGetGroupsInteractor.getAllGroupsFromFirebase();
    }

    @Override
    public void getChatGroups() {
        mGetGroupsInteractor.getChatGroupsFromFirebase();
    }

    @Override
    public void onGetAllGroupsSuccess(List<Group> users) {
        mView.onGetAllGroupsSuccess(users);
    }

    @Override
    public void onGetAllGroupsFailure(String message) {
        mView.onGetAllGroupsFailure(message);
    }
}
