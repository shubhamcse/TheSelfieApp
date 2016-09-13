package com.shubham.theselfieapp;

import android.content.Intent;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;
import com.shubham.theselfieapp.utils.MyApplication;

import java.io.IOException;

/**
 * Created by Shubham Gupta on 13-09-2016.
 */
public class UploadService extends android.app.IntentService {
    public static final String REQUEST_EXTRA_OBJECT = "object";
    private  String TAG = this.getClass().getName();
    DatabaseHelper databaseHelper;

    public UploadService(){
        super(UploadService.class.getName());

    }
    @Override
    protected void onHandleIntent(Intent intent) {
           Log.i("Service","service");
        databaseHelper = new DatabaseHelper(getApplicationContext());
        Image image = (Image) intent.getSerializableExtra(REQUEST_EXTRA_OBJECT);
        if(image.getSyncStatus().equals(Image.SYNC_STATUS.NOT_SYNCED))
        uploadFile(image);
    }

    void uploadFile(final Image image) {
        String filePath = image.getFileName();
        java.io.File file = new java.io.File(filePath);
        Client mKinveyClient = MyApplication.initKinvey();
        if (!mKinveyClient.user().isUserLoggedIn())
            mKinveyClient.user().login(new KinveyUserCallback() {
                @Override
                public void onFailure(Throwable error) {
                    Log.e(TAG, "Login Failure", error);
                }

                @Override
                public void onSuccess(User result) {
                    Log.i(TAG, "Logged in a new implicit user with id: " + result.getId());

                }
            });
        else
            mKinveyClient.user().retrieve(new KinveyUserCallback() {
                @Override
                public void onFailure(Throwable e) { Log.e(TAG, "Login Failure", e);}
                @Override
                public void onSuccess(User user) {  Log.i(TAG, "Logged in a new implicit user with id: " + user.getId()); }
            });


        mKinveyClient.file().upload(file, new UploaderProgressListener() {

            @Override
            public void onSuccess(FileMetaData fileMetaData) {
                Log.i(TAG, "successfully upload file" + fileMetaData.getFileName());
                databaseHelper.updateSyncStatus(image);
                sendBroadcast();
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, "failed to upload file.", error);
            }

            @Override
            public void progressChanged(MediaHttpUploader uploader) throws IOException {
                Log.i(TAG, "upload progress: " + uploader.getUploadState());
                // all updates to UI widgets need to be done on the UI thread
            }
        });
    }

   void sendBroadcast(){
       Intent intent = new Intent();
       intent.setAction(GalleryActivity.BROADCAST_ACTION);
       //intent.setPackage(getPackageName());
       Log.i(TAG, "sending broadcast ");
       sendBroadcast(intent);
    }
}
