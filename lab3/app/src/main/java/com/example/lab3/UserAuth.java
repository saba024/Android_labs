package com.example.lab3;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserAuth {

    public static void ChangeAvatar(Uri uri){
        UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileChange);
    }

    public static Uri getPhotoUri(){
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
    }

    public static void ChangeUserName(String username){
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdate);
    }

    public static String getUserName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
}
