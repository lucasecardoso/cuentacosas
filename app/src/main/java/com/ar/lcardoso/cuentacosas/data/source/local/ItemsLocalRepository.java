package com.ar.lcardoso.cuentacosas.data.source.local;

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

        String[] projection = {
                ItemEntry.COLUMN_NAME_ENTRY_ID,
                ItemEntry.COLUMN_NAME_TITLE,
                ItemEntry.COLUMN_NAME_COUNT
        };

        Cursor c = db.query(ItemEntry.TABLE_NAME, projection, null, null, null, null, null, null);

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

    }
}
