package com.ar.lcardoso.cuentacosas.items;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ar.lcardoso.cuentacosas.R;
import com.ar.lcardoso.cuentacosas.data.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * View for Items
 *
 * Created by Lucas on 15/11/2016.
 */
public class ItemsFragment extends Fragment implements ItemsContract.View {

    private ItemsContract.Presenter mPresenter;

    private LinearLayout mItemsView;

    private View mRoot;

    private ItemsAdapter mAdapter;

    private ItemsListener mItemListener = new ItemsListener() {

        @Override
        public void onAddCountClicked(Item item) {
            Log.d("DEBUG", "onAddCountClicked()");

            mPresenter.addCount(item);
        }

        @Override
        public void onMinusCountClicked(Item item) {
            Log.d("DEBUG", "onMinusCountClicked()");

            mPresenter.substractCount(item);
        }

        @Override
        public void onItemNameHold() {
            Log.d("DEBUG", "onItemNameHold()");
        }

        @Override
        public void onItemNameClicked(View rowView) {
            Log.d("DEBUG", "onItemNameClicked()");

            View toolbar = rowView.findViewById(R.id.item_line_toolbar);

            ItemToolbarAnimation animation = new ItemToolbarAnimation(toolbar, 500);

            toolbar.startAnimation(animation);
        }
    };

    public ItemsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ItemsAdapter(new ArrayList<>(0), mItemListener);
    }

    @Override
    public void setPresenter(@NotNull ItemsContract.Presenter presenter) {
        Log.d("DEBUG", "Setting presenter");
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.item_fragment, container, false);

        ListView listView = (ListView) mRoot.findViewById(R.id.items_list);
        listView.setAdapter(mAdapter);
        mItemsView = (LinearLayout) mRoot.findViewById(R.id.items_LL);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_item);
        fab.setOnClickListener((v) -> {
            DialogFragment f = new AddItemDialog();
            f.show(getActivity().getFragmentManager(), "additem");
        });

        setHasOptionsMenu(true);

        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showItems(List<Item> items) {
        mAdapter.setList(items);
    }

    @Override
    public void showNoItems() {

    }

    @Override
    public void showLoading() {

    }

    private static class ItemsAdapter extends BaseAdapter {

        private List<Item> items;
        private ItemsListener listener;

        public ItemsAdapter(List<Item> items, ItemsListener listener) {
            setList(items);
            this.listener = listener;
        }

        private void setList(List<Item> items) {
            this.items = checkNotNull(items);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View rowView, ViewGroup parent) {
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.item_line, parent, false);
            }

            final View rv = rowView;
            final Item item = getItem(position);

            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            titleView.setText(item.getTitle());

            TextView countView = (TextView) rowView.findViewById(R.id.count_text);
            countView.setText("" + item.getCount());

            View plusBtn = rowView.findViewById(R.id.plus_btn);
            View minusBtn = rowView.findViewById(R.id.minus_btn);

            plusBtn.setOnClickListener(view -> {
                listener.onAddCountClicked(item);
            });

            minusBtn.setOnClickListener(view -> {
                listener.onMinusCountClicked(item);
            });

            titleView.setOnClickListener(view -> listener.onItemNameClicked(rv));

            return rowView;
        }
    }

    public interface ItemsListener {
        void onAddCountClicked(Item item);

        void onMinusCountClicked(Item item);

        void onItemNameHold();

        void onItemNameClicked(View view);
    }
}
