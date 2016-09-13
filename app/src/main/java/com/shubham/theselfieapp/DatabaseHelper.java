package com.shubham.theselfieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Shubham Gupta on 13-09-2016.
 */

    public class DatabaseHelper extends SQLiteOpenHelper {

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "gallery";

        // Contacts table name
        private static final String TABLE_LIST = "list";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_PATH = "path";
        private static final String KEY_STATUS = "syncStatus";



        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LIST + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PATH + " TEXT,"
                    + KEY_STATUS + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

            // Create tables again
            onCreate(db);

        }

    public void addFile(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PATH, image.getFileName());
        values.put(KEY_STATUS,String.valueOf(image.getSyncStatus()));

        // Inserting Row
        Log.i("DB",""+db.insert(TABLE_LIST, null, values));
        db.close();
    }

    public ArrayList<Image> getAllList() {
        ArrayList<Image> list = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.set_id(Integer.parseInt(cursor.getString(0)));
                image.setFileName(cursor.getString(1));
                image.setSyncStatus(cursor.getString(2));
                list.add(image);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<Image> getUnSyncedList() {
        ArrayList<Image> list = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIST + " WHERE " + KEY_STATUS + "='" + Image.SYNC_STATUS.NOT_SYNCED + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.set_id(Integer.parseInt(cursor.getString(0)));
                image.setFileName(cursor.getString(1));
                image.setSyncStatus(cursor.getString(2));
                list.add(image);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public int updateSyncStatus(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
      //  values.put(KEY_PATH, image.getFileName());
        values.put(KEY_STATUS,(String.valueOf(Image.SYNC_STATUS.SYNCED)));


        return db.update(TABLE_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(image.get_id()) });
    }
}
