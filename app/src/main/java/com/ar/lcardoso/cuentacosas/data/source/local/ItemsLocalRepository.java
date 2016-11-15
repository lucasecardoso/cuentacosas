package com.ar.lcardoso.cuentacosas.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.ar.lcardoso.cuentacosas.data.Item;
import com.ar.lcardoso.cuentacosas.data.source.ItemsDataSource;
import com.ar.lcardoso.cuentacosas.data.source.local.ItemsPersistenceContract.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Lucas on 14/11/2016.
 */

public class ItemsLocalRepository implements ItemsDataSource {

    private static ItemsLocalRepository INSTANCE;
    private final ItemsDbHelper mDbHelper;

    private ItemsLocalRepository(Context context) {
        checkNotNull(context);
        mDbHelper = new ItemsDbHelper(context);
    }

    public static ItemsLocalRepository getInstance(@NonNull Context context) {

        if (INSTANCE == null)
            return new ItemsLocalRepository(context);

        return INSTANCE;
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

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

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
}
