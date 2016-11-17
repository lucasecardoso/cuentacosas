package com.ar.lcardoso.cuentacosas.items;

import android.support.annotation.NonNull;

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
    }

    @Override
    public void addNewItem(String title) {
        mDataSource.saveItem(new Item(title), new ItemsDataSource.SaveItemCallback() {
            @Override
            public void onItemSaved() {
                System.out.println("Item saved successfully");

            }

            @Override
            public void onSaveFailed() {
                System.out.println("Error occurred while saving item");
            }
        });
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
}
