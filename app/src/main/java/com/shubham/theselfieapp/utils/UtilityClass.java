package com.shubham.theselfieapp.utils;

import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shubham Gupta on 01-09-2016.
 */
public class UtilityClass {
    public static File getOutputMediaFile(String imageOrVideo) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        String fileNameStart;
        if(imageOrVideo.equals(".mp4"))
            fileNameStart = ".mp4";
        else
            fileNameStart = ".jpg";

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + fileNameStart + timeStamp + imageOrVideo);

        return mediaFile;
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {

            camera = Camera.open(0);
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }
}
