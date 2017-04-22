package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.GroupChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.GroupChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend.UnsendContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend.UnsendPresenter;
import com.crazyhitty.chdev.ks.firebasechat.events.GroupPushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.events.PushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupChat;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.ChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.GroupChatRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;
import com.crazyhitty.chdev.ks.firebasechat.utils.ItemClickSupport;
import com.crazyhitty.chdev.ks.firebasechat.utils.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 10:36 AM
 * Project: FirebaseChat
 */

public class GroupChatFragment extends Fragment implements GroupChatContract.View, TextView.OnEditorActionListener, ItemClickSupport.OnItemLongClickListener,ItemClickSupport.OnItemClickListener, UnsendContract.View  {
    RequestManager mRequestManager;
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressDialog mProgressDialog;

    private GroupChatRecyclerAdapter mChatRecyclerAdapter;

    private GroupChatPresenter mChatPresenter;
    private UnsendPresenter mUnsendPresenter;

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
        //FirebaseMessaging.getInstance().unsubscribeFromTopic(getArguments().getString(Constants.ARG_GROUPID));
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
        mRequestManager= Glide.with(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new GroupChatPresenter(this);
        mChatPresenter.getMessage(getArguments().getString(Constants.ARG_GROUPID),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUnsendPresenter= new UnsendPresenter(this);

        mETxtMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() <= mETxtMessage.getCompoundDrawables()[0].getBounds().width()+30)
                    {
                        selectImage();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //mImgProfile.setImageBitmap(bm);
        uploadImage(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mImgProfile.setImageBitmap(thumbnail);
        uploadImage(thumbnail);
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
        if(!mETxtMessage.getText().toString().trim().equals("")) {
        String message = mETxtMessage.getText().toString();
        String to = getArguments().getString(Constants.ARG_GROUPID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        GroupChat chat = new GroupChat(to, sender, message, System.currentTimeMillis(), getArguments().getString(Constants.ARG_GROUPNAME), "");
        mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat);
        } else {
            Toast.makeText(getActivity(), "Please write a valid message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImageMessage(Uri uri) {
            String to = getArguments().getString(Constants.ARG_GROUPID);
            String sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            GroupChat chat = new GroupChat(to, sender, "", System.currentTimeMillis(), getArguments().getString(Constants.ARG_GROUPNAME), uri.toString());
            mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat);
    }

    private void uploadImage(Bitmap bm){
        mChatPresenter.uploadImage(bm);
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
            mChatRecyclerAdapter = new GroupChatRecyclerAdapter(new ArrayList<GroupChat>(), getActivity(), mRequestManager);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
            ItemClickSupport.addTo(mRecyclerViewChat)
                    .setOnItemClickListener(this)
                    .setOnItemLongClickListener(this);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
    }

    @Override
    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
        if(position==recyclerView.getAdapter().getItemCount()-1){
            //check if sender is current user
            if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(mChatRecyclerAdapter.getChat(position).sender)) {
                //check for elapsed time if past 1 minute delete message from firebase
                long start = System.currentTimeMillis();
                if (start - mChatRecyclerAdapter.getChat(position).timestamp <= 60000) {
                    String roomtype = mChatRecyclerAdapter.getChat(position).groupid;
                    unsendMessageDialog(roomtype, mChatRecyclerAdapter.getChat(position).timestamp);
                }
            }
        }
        return true;
    }

    private void unsendMessageDialog(final String roomType, final long timestamp) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.unsend)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.unsend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mUnsendPresenter.deleteGroupMessage(roomType,timestamp);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadImageSuccess(Uri uri) {
        sendImageMessage(uri);
    }

    @Override
    public void onUploadImageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteMessageSuccess(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            mChatRecyclerAdapter.remove(mChatRecyclerAdapter.getItemCount()-1);
            mChatRecyclerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteMessageFailure(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onGroupPushNotificationEvent(GroupPushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(pushNotificationEvent.getGroupid(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }
}
