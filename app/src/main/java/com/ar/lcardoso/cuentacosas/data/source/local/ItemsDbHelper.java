package com.ar.lcardoso.cuentacosas.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lucas on 14/11/2016.
 */

public class ItemsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "LucasItems.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemsPersistenceContract.ItemEntry.TABLE_NAME + " (" +
                    ItemsPersistenceContract.ItemEntry._ID + " TEXT PRIMARY KEY, " +
                    ItemsPersistenceContract.ItemEntry.COLUMN_NAME_ENTRY_ID + " TEXT, " +
                    ItemsPersistenceContract.ItemEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    ItemsPersistenceContract.ItemEntry.COLUMN_NAME_COUNT + " INTEGER " +
            " )";


    public ItemsDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_ENTRIES); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
