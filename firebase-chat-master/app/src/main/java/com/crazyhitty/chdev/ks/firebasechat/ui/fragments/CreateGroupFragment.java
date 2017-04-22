package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupPayload;
import com.crazyhitty.chdev.ks.firebasechat.models.PayloadData;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.GroupListingActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.SelectableUserListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateGroupFragment extends Fragment implements View.OnClickListener, CreateGroupContract.View, GetUsersContract.View {
    private static final String TAG = CreateGroupFragment.class.getSimpleName();
    private CreateGroupPresenter mCreateGroupPresenter;
    private EditText mETxtGrpName;
    private Button mBtnCreate;
    private RecyclerView mRecyclerViewAllUserListing;
    private SelectableUserListingRecyclerAdapter mUserListingRecyclerAdapter;
    private GetUsersPresenter mGetUsersPresenter;
    private boolean getAll = true;

    public static CreateGroupFragment newInstance() {
        Bundle args = new Bundle();
        CreateGroupFragment fragment = new CreateGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_create_group, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mETxtGrpName = (EditText) view.findViewById(R.id.edTxtGrpName);
        mBtnCreate = (Button) view.findViewById(R.id.btnCreateGroup);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mCreateGroupPresenter = new CreateGroupPresenter(this);
        mGetUsersPresenter = new GetUsersPresenter(this);
        mBtnCreate.setOnClickListener(this);
        getUsers(getAll);

//        //TODO : revisit this
//        FirebaseMessaging.getInstance().subscribeToTopic("64f3a563-a491-4243-9014-7c43a2ce21b8");
//        makeJsonObjReq();
    }

    private void getUsers(boolean getAll) {
        mGetUsersPresenter.getAllUsers(getAll);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.btnCreateGroup:
                List<User> userList = mUserListingRecyclerAdapter.getmUsers();
                if(!mETxtGrpName.getText().toString().equals("")) {
                    mCreateGroupPresenter.createGroup(getActivity(), mETxtGrpName.getText().toString(), getSelectedUsers(userList));
                } else {
                    Toast.makeText(getActivity(), "Please enter a valid group name", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void moveToGroupListing() {
        GroupListingActivity.startActivity(getActivity(), Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private List<User> getSelectedUsers(List<User> userList) {
        List<User> selectedList = new ArrayList<User>();
        for (User user : userList) {
            if (user.isSelected()) {
                selectedList.add(user);
            }
        }
        return selectedList;
    }

    @Override
    public void onCreateGroupSuccess(String message) {
        Log.e(TAG, "onCreateGroupSuccess: " + message);
        moveToGroupListing();
        getActivity().finish();
    }

    @Override
    public void onCreateGroupFailure(String message) {
        Log.e(TAG, "onCreateGroupFailure: " + message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        users = filterVerifiedUsers(users);
        mUserListingRecyclerAdapter = new SelectableUserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    private List<User> filterVerifiedUsers(List<User> users) {
        List<User> tempusers = new ArrayList<>();
        for(User usr : users){
            if(usr.emailverified){
                tempusers.add(usr);
            }
        }
        return tempusers;
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetChatUsersSuccess(List<User> users) {

    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }

    }