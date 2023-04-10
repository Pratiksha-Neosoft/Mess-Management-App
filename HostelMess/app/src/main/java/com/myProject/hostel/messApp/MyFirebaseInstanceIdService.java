package com.myProject.hostel.messApp;

import android.util.Log;

/**
 * Created by prajjwal-ubuntu on 9/1/19.
 */

public class MyFirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendToken(com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken());

    }
    void sendToken(String token){
        Log.d("token",String.valueOf(token));
    }
}
