package com.shubham.theselfieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shubham.theselfieapp.adapters.GalleryThumbnailAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shubham Gupta on 01-09-2016.
 */
@EActivity(R.layout.gallery_layout)
public class GalleryActivity extends AppCompatActivity{
    private String TAG = "GalleryActivity";
    public static final String BROADCAST_ACTION = "com.shubham.theselfieapp.UpdateService.BROADCAST";

    File[] mediaFiles;
    File imageDir;
    ArrayList<Image> fileName = new ArrayList<Image>();
    @ViewById(R.id.recycler_view)
    RecyclerView recyclerView;
    GalleryThumbnailAdapter galleryThumbnailAdapter;
    DatabaseHelper databaseHelper;

    @AfterViews
    void init() {

        databaseHelper = new DatabaseHelper(this);
        GridLayoutManager glm = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(glm);

        galleryThumbnailAdapter = new GalleryThumbnailAdapter();


        imageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
     /*   mediaFiles = imageDir.listFiles();
        for (File file : mediaFiles) {
            fileName.add(file.getPath());
            Log.e(TAG, "path:" + file.getPath());
        }
        uploadFile(fileName.get(5));*/
        getFiles();
        uploadImages();
        recyclerView.setAdapter(galleryThumbnailAdapter);

    }

    void getFiles(){
        fileName = databaseHelper.getAllList();
        galleryThumbnailAdapter.filePath = fileName;
    }

    public String readFileName(File file) {
        String name = file.getName();
        return name;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Reciever","successful");
           // UpdateUI updateUI = (UpdateUI)context;
            updateUI();
        }
    };




    void uploadImages(){
    ArrayList<Image> images = databaseHelper.getUnSyncedList();
    Intent imageSyncService = null;
    Log.i("Service","images:"+images.size());
    for(Image i:images)
    {
        imageSyncService = new Intent(getApplicationContext(), UploadService.class);
        imageSyncService.putExtra(UploadService.REQUEST_EXTRA_OBJECT, i);
        startService(imageSyncService);
    }
}

    public void updateUI() {
        getFiles();
        galleryThumbnailAdapter.notifyDataSetChanged();
    }
}
