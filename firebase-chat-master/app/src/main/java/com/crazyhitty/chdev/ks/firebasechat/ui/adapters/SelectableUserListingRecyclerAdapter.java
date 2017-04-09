package com.crazyhitty.chdev.ks.firebasechat.ui.adapters;

import android.hardware.usb.UsbRequest;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.models.User;

import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:23 PM
 * Project: FirebaseChat
 */

public class SelectableUserListingRecyclerAdapter extends RecyclerView.Adapter<SelectableUserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;

    public SelectableUserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectable_item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        User user = mUsers.get(position);

        holder.txtUsername.setText(user.email);
        holder.chkSelected.setChecked(mUsers.get(position).isSelected());

        holder.chkSelected.setTag(mUsers.get(position));


        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                User user = (User) cb.getTag();
                user.setSelected(cb.isChecked());
                mUsers.get(position).setSelected(cb.isChecked());
            }
        });
    }

    public List<User> getmUsers() {
        return mUsers;
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
        public CheckBox chkSelected;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkuser);
        }
    }
}
