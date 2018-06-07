package com.example.cpttm.project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.util.Objects;

import butterknife.internal.Utils;

public class AlbumSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "album.db";
    private static final int DB_VERSION = 100;

    public AlbumSQLiteOpenHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public AlbumSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLite", "onCreate");
        db.execSQL("CREATE TABLE booklet (id INTEGER NOT NULL , img BLOB NOT NULL )");
        db.execSQL("CREATE TABLE list (id INTEGER NOT NULL UNIQUE, title TEXT NOT NULL, artist TEXT NOT NULL, cover BLOB NOT NULL, url TEXT NOT NULL, PRIMARY KEY(`id`) )");
        initializeListData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLite", "onUpgrade");
        db.execSQL("DROP TABLE booklet");
        db.execSQL("DROP TABLE list");
        onCreate(db);
    }

    private void initializeListData(SQLiteDatabase db) {
        addList(db, 1, "The Juiceman Cometh ft. Saqi", "The Polish Ambassador",
                getBlobFromBitmap(Objects.requireNonNull(getBitmapFromURL("https://placeimg.com/100/100/nature"))),
                "http://ioio-xr.16mb.com/song/1.mp3");
        for (int i = 0; i < 3; i++) {
            addBooklet(db, 1, getBlobFromBitmap(Objects.requireNonNull(getBitmapFromURL("https://placeimg.com/200/200/nature"))));
        }

        addList(db, 2, "Sun on Ice", "YAKAREE",
                getBlobFromBitmap(Objects.requireNonNull(getBitmapFromURL("https://placeimg.com/100/100/nature"))),
                "http://ioio-xr.16mb.com/song/2.mp3");
        for (int i = 0; i < 2; i++) {
            addBooklet(db, 2, getBlobFromBitmap(Objects.requireNonNull(getBitmapFromURL("https://placeimg.com/200/200/nature"))));
        }
    }

    //Insert values to the table contacts
    private void addList(SQLiteDatabase db, int id, String title, String artist, byte[] cover, String url) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("artist", artist);
        values.put("cover", cover);
        values.put("url", url);

        db.insert("list", null, values);
        //db.close();
    }

    private void addBooklet(SQLiteDatabase db, int id, byte[] img) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("img", img);

        db.insert("booklet", null, values);
        //db.close();
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private static byte[] getBlobFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }
}