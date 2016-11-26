package com.ar.lcardoso.cuentacosas.data.source;

import android.support.annotation.NonNull;

import com.ar.lcardoso.cuentacosas.data.Item;

import java.util.List;

/**
 * Created by Lucas on 14/11/2016.
 */

public interface ItemsDataSource {


    interface LoadItemsCallback {

        void onItemsLoaded(List<Item> items);

        void onDataNotAvailable();
    }

    interface GetItemCallback {

        void onItemLoaded(Item item);

        void onDataNotAvailable();
    }

    interface SaveItemCallback {

        void onItemSaved();

        void onSaveFailed();
    }

    interface UpdateItemCallback {

        void onItemUpdated();

        void onUpdateFailed(Item item);
    }

    interface DeleteItemCallback {

        void onItemDeleted();

        void onDeleteFailed(Item item);
    }

    interface EditItemCallback {

        void onItemEdited();

        void onEditFailed(Item item);
    }

    void getItems(@NonNull LoadItemsCallback callback);

    void getItem(@NonNull String id, @NonNull GetItemCallback callback);

    void saveItem(@NonNull Item item, @NonNull SaveItemCallback callback);

    void editItem(@NonNull Item item, @NonNull EditItemCallback callback);

    void deleteItem(@NonNull Item item, @NonNull DeleteItemCallback callback);

    void deleteAllItems();

    void addCount(@NonNull Item item, @NonNull UpdateItemCallback callback);

    void subtractCount(@NonNull Item item, @NonNull UpdateItemCallback updateItemCallback);


}
