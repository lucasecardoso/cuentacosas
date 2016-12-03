package com.ar.lcardoso.cuentacosas.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ar.lcardoso.cuentacosas.data.source.ItemsDataSource;
import com.ar.lcardoso.cuentacosas.data.source.local.ItemsLocalDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



/**
 * Created by Lucas on 15/11/2016.
 */

@RunWith(AndroidJUnit4.class)
public class ItemsLocalDataSourceTest {

    private final static String ITEM_TITLE = "title";

    private final static String ITEM_TITLE2 = "title2";

    private final static String ITEM_TITLE3 = "title3";

    private ItemsLocalDataSource mLocalDataSource;


    @Before
    public void setup() {
        mLocalDataSource = ItemsLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanup() {
        mLocalDataSource.deleteAllItems();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveItem() {
        //Given a new item
        final Item newItem = new Item(ITEM_TITLE, 1);

        //When saved into the persistent storage
        mLocalDataSource.saveItem(newItem, new ItemsDataSource.SaveItemCallback() {
            @Override
            public void onItemSaved() {}

            @Override
            public void onSaveFailed() { fail("Callback error"); }
        });

        //Check if it can be retrieved
        mLocalDataSource.getItem(newItem.getId(), new ItemsDataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(Item item) { assertThat(newItem, is(item)); }

            @Override
            public void onDataNotAvailable() { fail("Callback error"); }
        });
    }

    @Test
    public void retrieveItem() {
        //Create new item
        final Item item1 = new Item(ITEM_TITLE, 1);

        //Save them to storage
        mLocalDataSource.saveItem(item1, saveItemCallback);

        //Check if the item we saved is correct
        mLocalDataSource.getItem(item1.getId(), new ItemsDataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(Item item) { assertThat(item1, is(item)); }

            @Override
            public void onDataNotAvailable() { fail("Callback error"); }
        });
    }

    @Test
    public void retrieveAllItems() {
        //Create several items
        final Item item1 = new Item(ITEM_TITLE, 1);
        final Item item2 = new Item(ITEM_TITLE2, 1);
        final Item item3 = new Item(ITEM_TITLE3, 1);

        mLocalDataSource.saveItem(item1, saveItemCallback);
        mLocalDataSource.saveItem(item2, saveItemCallback);
        mLocalDataSource.saveItem(item3, saveItemCallback);

        mLocalDataSource.getItems(new ItemsDataSource.LoadItemsCallback() {
            @Override
            public void onItemsLoaded(List<Item> items) {
                assertNotNull(items);
                assertTrue(items.size() >= 3);

                boolean item1found = false;
                boolean item2found = false;
                boolean item3found = false;

                for (Item item : items) {
                    if (item.equals(item1))
                        item1found = true;
                    if (item.equals(item2))
                        item2found = true;
                    if (item.equals(item3))
                        item3found = true;
                }

                assertTrue(item1found);
                assertTrue(item2found);
                assertTrue(item3found);
            }

            @Override
            public void onDataNotAvailable() { fail("Callback error"); }
        });

    }

    //Generic callback for non-save related tests
    private ItemsDataSource.SaveItemCallback saveItemCallback = new ItemsDataSource.SaveItemCallback() {
        @Override
        public void onItemSaved() {}

        @Override
        public void onSaveFailed() { fail("Save item error");}
    };

}
