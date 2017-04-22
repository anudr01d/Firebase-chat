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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.ChatPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend.UnsendContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.individual.unsend.UnsendPresenter;
import com.crazyhitty.chdev.ks.firebasechat.events.PushNotificationEvent;
import com.crazyhitty.chdev.ks.firebasechat.models.Chat;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.GroupChatActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.ChatRecyclerAdapter;
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
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener, ItemClickSupport.OnItemLongClickListener,ItemClickSupport.OnItemClickListener, UnsendContract.View {
    RequestManager mRequestManager;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressDialog mProgressDialog;

    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private ChatPresenter mChatPresenter;
    private UnsendPresenter mUnsendPresenter;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
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

        mChatPresenter = new ChatPresenter(this);
        mUnsendPresenter= new UnsendPresenter(this);
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));

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
            String receiver = getArguments().getString(Constants.ARG_RECEIVER);
            String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
            String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
            Chat chat = new Chat(sender,
                    receiver,
                    senderUid,
                    receiverUid,
                    message,
                    System.currentTimeMillis(),"");
            mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                    chat,
                    receiverFirebaseToken);
        } else {
            Toast.makeText(getActivity(), "Please write a valid message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImageMessage(Uri uri) {
            String receiver = getArguments().getString(Constants.ARG_RECEIVER);
            String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
            String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
            Chat chat = new Chat(sender,
                    receiver,
                    senderUid,
                    receiverUid,
                    "",
                    System.currentTimeMillis(),
                    uri.toString());
            mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                    chat,
                    receiverFirebaseToken);
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
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>(), getActivity(), mRequestManager);
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
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(mChatRecyclerAdapter.getChat(position).sender)) {
                //check for elapsed time if past 1 minute delete message from firebase
                long start = System.currentTimeMillis();
                if (start - mChatRecyclerAdapter.getChat(position).timestamp <= 60000) {
                    String roomtype = mChatRecyclerAdapter.getChat(position).senderUid + "_" + mChatRecyclerAdapter.getChat(position).receiverUid;
                    String roomType2 = mChatRecyclerAdapter.getChat(position).receiverUid + "_" + mChatRecyclerAdapter.getChat(position).senderUid;
                    unsendMessageDialog(roomtype, roomType2, mChatRecyclerAdapter.getChat(position).timestamp);
                }
            }
        }
        return true;
    }

    private void unsendMessageDialog(final String roomType, final String roomType2, final long timestamp) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.unsend)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.unsend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mUnsendPresenter.deleteMessage(roomType,roomType2,timestamp);
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
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
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
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
