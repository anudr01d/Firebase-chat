package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.profile.ProfileContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.GroupListingActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.SelectableUserListingRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener, ProfileContract.View {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ImageView mImgProfile;
    private TextView mTxtUsername,mTxtUserEmail;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mImgProfile = (ImageView) view.findViewById(R.id.dp);
        mTxtUsername = (TextView) view.findViewById(R.id.txtUsername);
        mTxtUserEmail = (TextView) view.findViewById(R.id.txtEmail);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mTxtUserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mTxtUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

//        switch (viewId) {
//            case R.id.btnSave:
//                break;
//        }
    }


    @Override
    public void onSaveProfileSuccess(String message) {
        Log.e(TAG, "onSaveProfileSuccess: " + message);
        //moveToGroupListing();
        getActivity().finish();
    }

    @Override
    public void onSaveProfileFailure(String message) {
        Log.e(TAG, "onCreateGrouFailure: " + message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    }