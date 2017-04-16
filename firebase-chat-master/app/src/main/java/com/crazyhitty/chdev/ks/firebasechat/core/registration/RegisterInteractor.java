package com.crazyhitty.chdev.ks.firebasechat.core.registration;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.firebasechat.ui.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterInteractor implements RegisterContract.Interactor {
    private static final String TAG = RegisterInteractor.class.getSimpleName();

    private RegisterContract.OnRegistrationListener mOnRegistrationListener;
    private RegisterContract.OnEmailVerificationSent mOnEmailSentListener;

    public RegisterInteractor(RegisterContract.OnRegistrationListener onRegistrationListener, RegisterContract.OnEmailVerificationSent onEmailVerificationSent) {
        this.mOnRegistrationListener = onRegistrationListener;
        this.mOnEmailSentListener = onEmailVerificationSent;
    }

    @Override
    public void performFirebaseRegistration(Activity activity, final String email, String password, final String username) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "performFirebaseRegistration:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            mOnRegistrationListener.onFailure(task.getException().getMessage());
                        } else {
                            mOnRegistrationListener.onSuccess(task.getResult().getUser(), username);
                        }
                    }
                });
    }

    @Override
    public void sendVerificationEmail()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                                FirebaseAuth.getInstance().signOut();
                            }
                            mOnEmailSentListener.onEmailSuccess(user);

                        }
                        else
                        {
                            mOnEmailSentListener.onEmailFailure("Verification email could not be sent!");

                        }
                    }
                });
    }
}
