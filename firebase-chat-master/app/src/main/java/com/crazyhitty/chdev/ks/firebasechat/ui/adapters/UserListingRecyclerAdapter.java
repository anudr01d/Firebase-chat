package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:23 PM
 * Project: FirebaseChat
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    private Activity mActivity;

    public UserListingRecyclerAdapter(List<User> users, Activity activity) {
        this.mUsers = users;
        this.mActivity = activity;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = mUsers.get(position);

        //String alphabet = user.email.substring(0, 1);

        holder.txtUsername.setText(user.username);
//        Glide.with(mActivity)
//                .placeholder(R.drawable.ic_icon)
//                .load(Uri.parse(user.imguri))
//                .into(holder.imgProfile);
        if(!user.imguri.equals("")) {
            Glide.with(mActivity).load(Uri.parse(user.imguri)).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.imgProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        //holder.txtUserAlphabet.setText("a");
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername;
        private ImageView imgProfile;

        ViewHolder(View itemView) {
            super(itemView);
            //txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            imgProfile = (ImageView) itemView.findViewById(R.id.imgprofile);
        }
    }
}
