package com.ar.lcardoso.cuentacosas.items;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ar.lcardoso.cuentacosas.data.Item;
import com.ar.lcardoso.cuentacosas.data.source.ItemsDataSource;

import java.util.List;

/**
 * Created by Lucas on 14/11/2016.
 */

public class ItemsPresenter implements ItemsContract.Presenter {

    @NonNull
    private final ItemsDataSource mDataSource;

    @NonNull
    private final ItemsContract.View mItemsView;

    public ItemsPresenter(@NonNull ItemsDataSource mDataSource, @NonNull ItemsContract.View mItemsFragment) {
        this.mDataSource = mDataSource;
        this.mItemsView = mItemsFragment;
        this.mItemsView.setPresenter(this);
    }

    @Override
    public void addNewItem(String title) {
        mDataSource.saveItem(new Item(title), new ItemsDataSource.SaveItemCallback() {
            @Override
            public void onItemSaved() {
                System.out.println("Item saved successfully");
                showItems();
            }

            @Override
            public void onSaveFailed() {
                Log.d("DEBUG", "Error occurred while saving item");
            }
        });

        showItems();
    }

    @Override
    public void deleteItems() {
        mDataSource.deleteAllItems();
        mItemsView.showNoItems();
    }

    @Override
    public void deleteItem(Item item) {
        mDataSource.deleteItem(item, new ItemsDataSource.DeleteItemCallback() {
            @Override
            public void onItemDeleted() { showItems(); }

            @Override
            public void onDeleteFailed(Item item) { Log.d("DEBUG", "Error occurred when deleting item"); }
        });
    }

    @Override
    public void editItem(Item item) {
        mDataSource.editItem(item, new ItemsDataSource.EditItemCallback() {
            @Override
            public void onItemEdited() { showItems(); }

            @Override
            public void onEditFailed(Item item) { Log.d("DEBUG", "Error occurred when updating item"); }
        });
    }

    @Override
    public void editItem(String itemId, String newTitle) {
        mDataSource.getItem(itemId, new ItemsDataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(Item item) {
                item.setTitle(newTitle);
                editItem(item);
            }

            @Override
            public void onDataNotAvailable() { Log.d("DEBUG", "Error occurred when updating item"); }
        });
    }

    @Override
    public void addCount(Item item) {
        mDataSource.addCount(item, updateItemCallback);
    }

    @Override
    public void subtractCount(Item item) {
        mDataSource.subtractCount(item, updateItemCallback);
    }

    public void showItems() {
        mItemsView.showLoading();

        mDataSource.getItems(new ItemsDataSource.LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> items) {
                mItemsView.showItems(items);
            }

            @Override
            public void onDataNotAvailable() {
                mItemsView.showNoItems();
            }
        });
    }

    @Override
    public void start() {
        showItems();
    }

    private ItemsDataSource.UpdateItemCallback updateItemCallback = new ItemsDataSource.UpdateItemCallback() {

        @Override
        public void onItemUpdated() {
            showItems();
        }

        @Override
        public void onUpdateFailed(Item item) {
            Log.d("DEBUG", "Item " + item.getId() + " " + item.getTitle() + " update failed");
        }

    };
}
