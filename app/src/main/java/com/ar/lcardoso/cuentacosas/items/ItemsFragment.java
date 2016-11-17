package com.ar.lcardoso.cuentacosas.items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * View for Items
 *
 * Created by Lucas on 15/11/2016.
 */
public class ItemsFragment extends Fragment implements ItemsContract.View {

    ItemsContract.Presenter mPresenter;

    LinearLayout mItemsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setPresenter(@NotNull ItemsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.item_fragment, container, false);

        ListView listView = (ListView) root.findViewById(R.id.items_list);
        mItemsView = (LinearLayout) root.findViewById(R.id.items_LL);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_item);
        fab.setOnClickListener((v) -> { mPresenter.addNewItem("Test item"); });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void showItems(List<Item> items) {

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
            items = checkNotNull(items);
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

            final Item item = getItem(position);

            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            titleView.setText(item.getTitle());

            return rowView;
        }
    }

    public interface ItemsListener {

        void onItemClick(Item clickedItem);

        void onCompleteItemClick(Item completedClick);

        void onActivateItemClick(Item activatedItem);
    }
}
