package com.shubham.theselfieapp;

/**
 * Created by Shubham Gupta on 02-09-2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shubham.theselfieapp.utils.MyApplication;
import com.shubham.theselfieapp.utils.MyCameraSurfaceView;
import com.shubham.theselfieapp.utils.UtilityClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidVideoCapture extends Activity implements VideoRecording.OnRecordingChanged{

    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;


    Button myButton;
    Button videoRecording;
    VideoRecording videoRecordingClass;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_camera_activity_layout);

        databaseHelper = new DatabaseHelper(this);

        Log.i("TAG",""+String.valueOf(Image.SYNC_STATUS.NOT_SYNCED));
        //Get Camera for preview
        myCamera = MyApplication.getCameraInstance();
        if(myCamera == null){
            Toast.makeText(AndroidVideoCapture.this,
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        videoRecordingClass = new VideoRecording(this,myCameraSurfaceView,myCamera);
        videoRecordingClass.initRecordingChanged();
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.camera_preview);
        myCameraPreview.addView(myCameraSurfaceView);

        myButton = (Button)findViewById(R.id.button_capture);
        myButton.setOnClickListener(myButtonOnClickListener);

        videoRecording = (Button)findViewById(R.id.button_video);
        videoRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoRecordingClass.recordVideo(videoRecording);
            }
        });
    }

    Button.OnClickListener myButtonOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            myCamera.takePicture(null, null, mPicture);


        }};


    @Override
    protected void onPause() {
        super.onPause();
        videoRecordingClass.releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        videoRecordingClass.releaseCamera();              // release the camera immediately on pause event
    }



    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = UtilityClass.getOutputMediaFile(".jpg");
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                saveImageInDatabase(pictureFile.getPath());
                finish();
                startGallery();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    void startGallery(){
        Intent intent = new Intent(this,GalleryActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onRecordingDone(boolean needToStartGallery) {
        if(needToStartGallery){
            finish();
            startGallery();
        }
        else
            finish();
    }

    @Override
    public void saveVideoInDatabase(String fileName) {
        Image image = new Image();
        image.setFileName(fileName);
        image.setSyncStatus(String.valueOf(Image.SYNC_STATUS.NOT_SYNCED));
        databaseHelper.addFile(image);
    }
    public void saveImageInDatabase(String fileName) {
        Image image = new Image();
        image.setFileName(fileName);
        image.setSyncStatus(String.valueOf(Image.SYNC_STATUS.NOT_SYNCED));
        databaseHelper.addFile(image);
    }
}
