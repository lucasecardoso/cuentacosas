package com.ar.lcardoso.cuentacosas.items;

import com.ar.lcardoso.cuentacosas.BasePresenter;
import com.ar.lcardoso.cuentacosas.BaseView;

/**
 * Created by Lucas on 14/11/2016.
 */

public interface ItemsContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void addNewItem();
    }
}
