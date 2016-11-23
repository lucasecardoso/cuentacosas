package com.ar.lcardoso.cuentacosas.items;

import com.ar.lcardoso.cuentacosas.BasePresenter;
import com.ar.lcardoso.cuentacosas.BaseView;
import com.ar.lcardoso.cuentacosas.data.Item;

import java.util.List;

/**
 * Created by Lucas on 14/11/2016.
 */

public interface ItemsContract {

    interface View extends BaseView<Presenter> {
        void showItems(List<Item> items);
        void showNoItems();
        void showLoading();
    }

    interface Presenter extends BasePresenter {
        void addNewItem(String title);
        void deleteItems();

        void addCount(Item item);
        void substractCount(Item item);
    }
}
