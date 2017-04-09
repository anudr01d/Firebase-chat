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
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.GroupChatActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.GroupListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.UserListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment implements GetGroupsContract.View, ItemClickSupport.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllGroupListing;

    private GroupListingRecyclerAdapter mGroupListingRecyclerAdapter;

    private GetGroupsPresenter mGetGroupsPresenter;

    public static GroupsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        GroupsFragment fragment = new GroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_groups, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllGroupListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_group_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGetGroupsPresenter = new GetGroupsPresenter(this);
        getGroups();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        ItemClickSupport.addTo(mRecyclerViewAllGroupListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);


    }

    @Override
    public void onRefresh() {
        getGroups();
    }

    private void getGroups() {
        if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {

        } else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
            mGetGroupsPresenter.getAllGroups();
        }
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        GroupChatActivity.startActivity(getActivity(),
                mGroupListingRecyclerAdapter.getGroup(position).getGroupID(),
                mGroupListingRecyclerAdapter.getGroup(position).getGroupName());
    }

    @Override
    public void onGetAllGroupsSuccess(List<Group> users) {
        //filter groups based on the current user's presence
        List<Group> grps = new ArrayList<>();
        for(Group g : users) {
            for(User us : g.getUserList()) {
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equalsIgnoreCase(us.email)) {
                    grps.add(g);
                }
            }
        }
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mGroupListingRecyclerAdapter = new GroupListingRecyclerAdapter(grps);
        mRecyclerViewAllGroupListing.setAdapter(mGroupListingRecyclerAdapter);
        mGroupListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllGroupsFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetChatGroupsSuccess(List<Group> users) {

    }

    @Override
    public void onGetChatGroupsFailure(String message) {

    }
}
