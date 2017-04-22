package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.getall.GetGroupsContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.getall.GetGroupsPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.ChatActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.GroupListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.UserListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class UsersFragment extends Fragment implements GetUsersContract.View, ItemClickSupport.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, GetGroupsContract.View {
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    private boolean getAll = false;
    private List<Group> grps;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;

    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUsersPresenter mGetUsersPresenter;
    private GetGroupsPresenter mGetGroupsPresenter;

    public static UsersFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_users, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGetUsersPresenter = new GetUsersPresenter(this);
        mGetGroupsPresenter = new GetGroupsPresenter(this);
        getUsers();
        mGetGroupsPresenter.getAllGroups();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
        getUsers();
    }

    private void getUsers() {
        if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {

        } else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
            mGetUsersPresenter.getAllUsers(getAll);
        }
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ChatActivity.startActivity(getActivity(),
                mUserListingRecyclerAdapter.getUser(position).email,
                mUserListingRecyclerAdapter.getUser(position).uid,
                mUserListingRecyclerAdapter.getUser(position).firebaseToken);
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        List<User> verifiedUsers = filterVerifiedUsers(users);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(verifiedUsers, getActivity());
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
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        try {
            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetChatUsersSuccess(List<User> users) {

    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }


    @Override
    public void onGetAllGroupsSuccess(List<Group> users) {
        //filter groups based on the current user's presence
        grps = new ArrayList<>();
        for(Group g : users) {
            Iterator it = g.getUserList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                User usr = (User)pair.getValue();
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase(usr.email.toString())) {
                    grps.add(g);
                }
                //it.remove(); // avoids a ConcurrentModificationException
            }
        }
        subscribeToAllGroups(grps);
    }

    private void subscribeToAllGroups(List<Group> groups){
        for(Group grp : groups) {
            if(grp!=null) {
                FirebaseMessaging.getInstance().subscribeToTopic(grp.getGroupID());
            }
        }
    }
    private void unSubscribeAllGroups(List<Group> groups){
        for(Group grp : groups) {
            if(grp!=null) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(grp.getGroupID());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(grps!=null) {
//            unSubscribeAllGroups(grps);
//        }
    }

    @Override
    public void onGetAllGroupsFailure(String message) {
    }

    @Override
    public void onGetChatGroupsSuccess(List<Group> users) {

    }

    @Override
    public void onGetChatGroupsFailure(String message) {

    }

}
