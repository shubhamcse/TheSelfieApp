package com.shubham.theselfieapp.utils;

import android.app.Application;
import android.content.Context;
import android.hardware.Camera;

import com.kinvey.android.Client;
import com.shubham.theselfieapp.R;

/**
 * Created by Shubham Gupta on 04-10-2015.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    public MyApplication(){
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Iconify.with(new MaterialModule());
    }
    public final static Client initKinvey(){
        final Client mKinveyClient = new Client.Builder(getContext().getResources().getString(R.string.app_key_kinvey),getContext().getResources().getString(R.string.app_secret_kinvey)
                , getContext()).build();
        return mKinveyClient;

    }

    public static Camera getCameraInstance(){
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    public static Context getContext() {
        return instance;
    }
}
