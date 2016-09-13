package com.shubham.theselfieapp;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.widget.Button;
import android.widget.Toast;

import com.shubham.theselfieapp.utils.MyApplication;
import com.shubham.theselfieapp.utils.MyCameraSurfaceView;
import com.shubham.theselfieapp.utils.UtilityClass;

import java.io.IOException;

/**
 * Created by Shubham Gupta on 13-09-2016.
 */
public class VideoRecording {
    boolean recording;
    private MediaRecorder mediaRecorder;
    Context context;
    MyCameraSurfaceView myCameraSurfaceView;

    public VideoRecording(Context context,MyCameraSurfaceView myCameraSurfaceView,Camera myCamera){
        this.context = context;
        recording = false;
        this.myCameraSurfaceView = myCameraSurfaceView;
        this.myCamera = myCamera;
    }

    public interface OnRecordingChanged{
        void onRecordingDone(boolean needToStartGallery);
        void saveVideoInDatabase(String fileName);
    }
    OnRecordingChanged onRecordingChanged;
    void initRecordingChanged(){
        onRecordingChanged = (OnRecordingChanged)context;
    }
    void recordVideo(Button videoRecording){
        if(recording){
            // stop recording and release camera
            mediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            //Exit after saved
            onRecordingChanged.onRecordingDone(true);
            onRecordingChanged.saveVideoInDatabase(videoPath);
           // finish();
           //startGallery();
        }else{

            //Release Camera before MediaRecorder start
            releaseCamera();

            if(!prepareMediaRecorder()){
                Toast.makeText(context,
                        "Fail in prepareMediaRecorder()!\n - Ended -",
                        Toast.LENGTH_LONG).show();
                onRecordingChanged.onRecordingDone(false);
              //  finish();
            }

            mediaRecorder.start();
            recording = true;
            videoRecording.setBackgroundResource(R.drawable.stop_icon);

        }
    }

    Camera myCamera;
    String videoPath;
    private boolean prepareMediaRecorder(){
        myCamera  = MyApplication.getCameraInstance();
        myCamera.setDisplayOrientation(90);
        mediaRecorder = new MediaRecorder();

        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        videoPath = UtilityClass.getOutputMediaFile(".mp4").getPath();

        mediaRecorder.setOutputFile(videoPath);
        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M


        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }
     void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

     void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }



}
