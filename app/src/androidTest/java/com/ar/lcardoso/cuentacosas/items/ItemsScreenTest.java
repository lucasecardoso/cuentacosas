package com.ar.lcardoso.cuentacosas.items;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.ar.lcardoso.cuentacosas.R;
import com.ar.lcardoso.cuentacosas.data.source.ItemsDataSource;
import com.ar.lcardoso.cuentacosas.data.source.local.ItemsLocalDataSource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;




/**
 * Tests for the Items screen (ItemsActivity + ItemsFragment)
 * Created by Lucas on 21/11/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemsScreenTest {

    private static final String TITLE = "Test";

    @Rule
    public ActivityTestRule<ItemsActivity> mItemsActivityTestRule
            = new ActivityTestRule<ItemsActivity>(ItemsActivity.class) {

        /**
         * To avoid a long list of items, before every test we call
         * {@link ItemsDataSource#deleteAllItems()}.
         */
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            ItemsLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext()).deleteAllItems();
        }
    };

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "item cannot be null or empty");

        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return allOf(isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }
        };
    }


    @Test
    public void clickAddItem_displayAlertDialog() {
        onView(withId(R.id.fab_add_item)).perform(click());
        onView(withText(R.string.additem_headline)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAddItem_addItem() {
        createItem(TITLE);
        onView(withItemText(TITLE)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAddItem_dismissDialog() {
        onView(withId(R.id.fab_add_item)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText(R.string.additem_headline)).check(doesNotExist());
        onView(withItemText(TITLE)).check(doesNotExist());
    }

    @Test
    public void clickAddCounterButton_addsOne() {
        createItem(TITLE);
        onView(withId(R.id.plus_btn)).perform(click());
        onView(withId(R.id.count_text)).check(matches(withText("1")));
    }

    @Test
    public void clickSubtractCounterButton_staysZero() {
        createItem(TITLE);
        onView(withId(R.id.minus_btn)).perform(click());
        onView(withId(R.id.count_text)).check(matches(withText("0")));
    }

    @Test
    public void clickSubtractCounterButton_subtractsOne() {
        createItem(TITLE);
        onView(withId(R.id.plus_btn)).perform(click());
        onView(withId(R.id.plus_btn)).perform(click());
        onView(withId(R.id.minus_btn)).perform(click());
        onView(withId(R.id.count_text)).check(matches(withText("1")));
    }


    private void createItem(String title) {
        onView(withId(R.id.fab_add_item)).perform(click());
        onView(withId(R.id.additem_name)).perform(typeText(title));
        onView(withId(android.R.id.button1)).perform(click());
    }

}
