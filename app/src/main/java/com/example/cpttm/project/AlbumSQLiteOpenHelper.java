package com.example.cpttm.project;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLite", "onUpgrade");
        db.execSQL("DROP TABLE booklet");
        db.execSQL("DROP TABLE list");
        onCreate(db);
    }
}