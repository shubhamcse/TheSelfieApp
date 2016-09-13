package com.shubham.theselfieapp;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Shubham Gupta on 13-09-2016.
 */
public class Image implements Serializable{
    String fileName;
    int  _id;
    public enum SYNC_STATUS { SYNCED,NOT_SYNCED }
    String syncStatus;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SYNC_STATUS getSyncStatus() {
        Log.i("Image","status:"+syncStatus);
        return SYNC_STATUS.valueOf(syncStatus);
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }




}
