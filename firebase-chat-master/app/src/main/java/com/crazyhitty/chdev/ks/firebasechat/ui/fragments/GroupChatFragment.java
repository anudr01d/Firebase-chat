package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.GroupChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.GroupChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.events.GroupPushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.events.PushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.ChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.GroupChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class GroupChatFragment extends Fragment implements GroupChatContract.View, TextView.OnEditorActionListener {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressDialog mProgressDialog;

    private GroupChatRecyclerAdapter mChatRecyclerAdapter;

    private GroupChatPresenter mChatPresenter;

    public static GroupChatFragment newInstance(String to,
                                                String groupname) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_GROUPID, to);
        args.putString(Constants.ARG_GROUPNAME, groupname);
        GroupChatFragment fragment = new GroupChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getArguments().getString(Constants.ARG_GROUPID));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new GroupChatPresenter(this);
        mChatPresenter.getMessage(getArguments().getString(Constants.ARG_GROUPID),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseMessaging.getInstance().subscribeToTopic(getArguments().getString(Constants.ARG_GROUPID));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }

    private void sendMessage() {
        String message = mETxtMessage.getText().toString();
        String to = getArguments().getString(Constants.ARG_GROUPID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GroupChat chat = new GroupChat(to, sender, message, System.currentTimeMillis());
        mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat);
    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(GroupChat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new GroupChatRecyclerAdapter(new ArrayList<GroupChat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onGroupPushNotificationEvent(GroupPushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(pushNotificationEvent.getGroupid(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }
}
