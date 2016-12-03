package com.ar.lcardoso.cuentacosas.items;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ar.lcardoso.cuentacosas.R;
import com.ar.lcardoso.cuentacosas.data.source.local.ItemsLocalDataSource;

/**
 * Created by Lucas on 14/11/2016.
 */

public class ItemsActivity extends AppCompatActivity
        implements AddItemDialog.AddItemDialogListener, EditItemDialog.EditDialogListener {

    private ItemsContract.Presenter mItemsPresenter;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.items_activity);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(getApplication().getApplicationContext(), R.color.colorPrimaryDark));

        ItemsFragment fragment =
                (ItemsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (fragment == null) {
            //Create fragment
            fragment = new ItemsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, fragment);
            transaction.commit();
        }

        mItemsPresenter =
                new ItemsPresenter(ItemsLocalDataSource.getInstance(getApplicationContext()), fragment);

        //mItemsPresenter.deleteItems();

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String itemName, int step) {
        if (itemName.isEmpty()) {
            Toast.makeText(this, R.string.additem_no_item_name_error, Toast.LENGTH_LONG);
            return;
        }

        mItemsPresenter.addNewItem(itemName, step);
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d("DEBUG", "Nope");
        dialog.dismiss();
    }


    @Override
    public void onEditDialogPositiveClick(String itemId, String text, int step) {
        mItemsPresenter.editItem(itemId, text, step);
    }

    @Override
    public void onEditDialogNegativeClick() {

    }
}
