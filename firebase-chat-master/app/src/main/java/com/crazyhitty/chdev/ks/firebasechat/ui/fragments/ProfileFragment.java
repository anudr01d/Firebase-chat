package com.crazyhitty.chdev.ks.firebasechat.ui.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crazyhitty.chdev.ks.firebasechat.R;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupContract;
import com.crazyhitty.chdev.ks.firebasechat.core.chat.group.create.CreateGroupPresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.profile.ProfileContract;
import com.crazyhitty.chdev.ks.firebasechat.core.profile.ProfilePresenter;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersContract;
import com.crazyhitty.chdev.ks.firebasechat.core.users.get.all.GetUsersPresenter;
import com.crazyhitty.chdev.ks.firebasechat.models.User;
import com.crazyhitty.chdev.ks.firebasechat.ui.activities.GroupListingActivity;
import com.crazyhitty.chdev.ks.firebasechat.ui.adapters.SelectableUserListingRecyclerAdapter;
import com.crazyhitty.chdev.ks.firebasechat.utils.Utility;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener, ProfileContract.View {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ImageView mImgProfile;
    private TextView mTxtUsername,mTxtUserEmail;
    private String userChoosenTask;
    private ProfilePresenter mPresenter;

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
        final ImageView v = (ImageView) fragmentView.findViewById(R.id.dp);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        selectImage();
                        break;
                    }
                }
                return true;
            }
        });
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
        mPresenter = new ProfilePresenter(this);
        mTxtUserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        mTxtUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
            Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(mImgProfile);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.dp:
                selectImage();
                break;
        }
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
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
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
        mImgProfile.setImageBitmap(bm);
        mPresenter.saveProfile(bm);
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
        mImgProfile.setImageBitmap(thumbnail);
        mPresenter.saveProfile(thumbnail);
    }



    @Override
    public void onSaveProfileSuccess(String message) {
        Log.e(TAG, "onSaveProfileSuccess: " + message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveProfileFailure(String message) {
        Log.e(TAG, "onCreateGrouFailure: " + message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    }