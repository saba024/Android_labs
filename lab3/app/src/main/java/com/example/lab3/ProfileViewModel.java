package com.example.lab3;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {
    private MutableLiveData<String> usernameLiveData = null;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getUserNameLiveData() {
        if (usernameLiveData == null) {
            usernameLiveData = new MutableLiveData<>();
        }
        return usernameLiveData;
    }

    public void setUserName(String username) {
        UserAuth.ChangeUserName(username);
        usernameLiveData.setValue(UserAuth.getUserName());
    }

    public Uri getImageUri() {
        return UserAuth.getPhotoUri();
    }

    public void setImageUri(Uri uri) {
        UserAuth.ChangeAvatar(uri);
    }
}
