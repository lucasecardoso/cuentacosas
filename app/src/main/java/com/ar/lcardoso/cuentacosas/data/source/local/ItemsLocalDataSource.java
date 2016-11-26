package com.ar.lcardoso.cuentacosas.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ar.lcardoso.cuentacosas.data.Item;
import com.ar.lcardoso.cuentacosas.data.source.ItemsDataSource;
import com.ar.lcardoso.cuentacosas.data.source.local.ItemsPersistenceContract.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lucas on 14/11/2016.
 */

public class ItemsLocalDataSource implements ItemsDataSource {

    private static ItemsLocalDataSource INSTANCE;
    private final ItemsDbHelper mDbHelper;

    private ItemsLocalDataSource(Context context) {
        checkNotNull(context);
        mDbHelper = new ItemsDbHelper(context);
    }

    public static ItemsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            return new ItemsLocalDataSource(context);

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getItems(@NonNull final LoadItemsCallback callback) {
        checkNotNull(callback);

        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = {
                ItemEntry.COLUMN_NAME_ENTRY_ID,
                ItemEntry.COLUMN_NAME_TITLE,
                ItemEntry.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(ItemEntry.TABLE_NAME, columns, null, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String entryId = c.getString(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_TITLE));
                int count = c.getInt(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_COUNT));

                Item item = new Item(entryId, title, count);
                items.add(item);
            }
        }

        if (c != null)
            c.close();

        db.close();

        if (items.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onItemsLoaded(items);

    }

    @Override
    public void getItem(@NonNull String id, @NonNull GetItemCallback callback) {
        checkNotNull(callback);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = {
                ItemEntry.COLUMN_NAME_ENTRY_ID,
                ItemEntry.COLUMN_NAME_TITLE,
                ItemEntry.COLUMN_NAME_COUNT
        };

        String selection = ItemEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { id };

        Cursor c = db.query(ItemEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Item item = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String entryId = c.getString(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_TITLE));
            int count = c.getInt(c.getColumnIndexOrThrow(ItemEntry.COLUMN_NAME_COUNT));

            item = new Item(entryId, title, count);
        }

        if (c != null)
            c.close();

        db.close();

        if (item == null)
            callback.onDataNotAvailable();
        else
            callback.onItemLoaded(item);
    }

    @Override
    public void saveItem(@NonNull Item item, @NonNull SaveItemCallback callback) {
        checkNotNull(item);
        checkNotNull(callback);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_NAME_ENTRY_ID, item.getId());
        values.put(ItemEntry.COLUMN_NAME_TITLE, item.getTitle());
        values.put(ItemEntry.COLUMN_NAME_COUNT, item.getCount());

        if (db.insert(ItemEntry.TABLE_NAME, null, values) == -1) {
            db.close();
            callback.onSaveFailed();
        } else {
            db.close();
            callback.onItemSaved();
        }
    }

    @Override
    public void editItem(@NonNull Item item, @NonNull EditItemCallback callback) {
        checkNotNull(item);
        checkNotNull(callback);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_NAME_TITLE, item.getTitle());

        int update = db.update(ItemEntry.TABLE_NAME, values, ItemEntry.COLUMN_NAME_ENTRY_ID + " = ?", new String[]{item.getId()});

        if (update == -1) {
            db.close();
            callback.onEditFailed(item);
        }
        else {
            db.close();
            callback.onItemEdited();
        }

    }

    @Override
    public void deleteItem(@NonNull Item item, @NonNull DeleteItemCallback callback) {
        checkNotNull(item);
        checkNotNull(callback);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int delete = db.delete(ItemEntry.TABLE_NAME, ItemEntry.COLUMN_NAME_ENTRY_ID + " = ? ", new String[]{item.getId()});

        db.close();

        if (delete == -1)
            callback.onDeleteFailed(item);
        else
            callback.onItemDeleted();
    }

    @Override
    public void deleteAllItems() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(ItemEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void addCount(@NonNull Item item, UpdateItemCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        item.setCount(item.getCount() + 1);

        int update = updateItemCount(item, db);

        db.close();

        if (update == -1)
            callback.onUpdateFailed(item);
        else
            callback.onItemUpdated();

    }


    @Override
    public void subtractCount(@NonNull Item item, @NonNull UpdateItemCallback updateItemCallback) {
        if (item.getCount() <= 0)
            return;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        item.setCount(item.getCount() - 1);

        int update = updateItemCount(item, db);

        db.close();

        if (update == -1)
            updateItemCallback.onUpdateFailed(item);
        else
            updateItemCallback.onItemUpdated();

    }


    private int updateItemCount(@NonNull Item item, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_NAME_COUNT, item.getCount());

        Log.d("DEBUG", "Updating item " + item.getId() + " with count = " + item.getCount());

        return db.update(ItemEntry.TABLE_NAME, values, ItemEntry.COLUMN_NAME_ENTRY_ID + " = ? ", new String[]{item.getId()});
    }
}
