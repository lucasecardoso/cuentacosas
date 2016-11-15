package com.ar.lcardoso.cuentacosas.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Lucas on 14/11/2016.
 */

public final class ItemsPersistenceContract {

    private ItemsPersistenceContract() {}

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COUNT = "count";
    }
}
