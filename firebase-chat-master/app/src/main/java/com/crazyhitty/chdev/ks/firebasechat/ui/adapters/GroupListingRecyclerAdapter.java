package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.Group;
import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:23 PM
 * Project: FirebaseChat
 */

public class GroupListingRecyclerAdapter extends RecyclerView.Adapter<GroupListingRecyclerAdapter.ViewHolder> {
    private List<Group> mGroups;

    public GroupListingRecyclerAdapter(List<Group> groups) {
        this.mGroups = groups;
    }

    public void add(Group group) {
        mGroups.add(group);
        notifyItemInserted(mGroups.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_group_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = mGroups.get(position);

        //String alphabet = user.email.substring(0, 1);

        holder.txtUsername.setText(group.getGroupName());
        holder.txtUserAlphabet.setText(""+group.getUserList().size());
    }

    @Override
    public int getItemCount() {
        if (mGroups != null) {
            return mGroups.size();
        }
        return 0;
    }

    public Group getGroup(int position) {
        return mGroups.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}
