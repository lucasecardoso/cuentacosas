package com.ar.lcardoso.cuentacosas.items;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ar.lcardoso.cuentacosas.R;
import com.ar.lcardoso.cuentacosas.data.Item;
import com.ar.lcardoso.cuentacosas.items.animation.ItemToolbarCloseAnimation;
import com.ar.lcardoso.cuentacosas.items.animation.ItemToolbarOpenAnimation;

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

            mPresenter.subtractCount(item);
        }

        @Override
        public void onItemNameClicked(View rowView) {
            Log.d("DEBUG", "onItemNameClicked()");

            View toolbar = rowView.findViewById(R.id.item_line_toolbar);
            if (toolbar.getVisibility() == View.GONE) {
                toolbar.setVisibility(View.VISIBLE);
                ItemToolbarOpenAnimation anim = new ItemToolbarOpenAnimation(toolbar, 500);
                toolbar.startAnimation(anim);
            } else {
                ItemToolbarCloseAnimation anim = new ItemToolbarCloseAnimation(toolbar, 500);

                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toolbar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                toolbar.startAnimation(anim);
            }

        }

        @Override
        public void onDeleteItemClicked(Item item) {
            mPresenter.deleteItem(item);
        }

        @Override
        public void onEditItemClicked(Item item) {

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment f = new AddItemDialog();
                f.show(ItemsFragment.this.getActivity().getFragmentManager(), "additem");
            }
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
    public void showNoItems() { showItems(new ArrayList<>()); }

    @Override
    public void showLoading() {

    }

    private class ItemsAdapter extends BaseAdapter {

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
            final TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            titleView.setText(item.getTitle());
            TextView countView = (TextView) rowView.findViewById(R.id.count_text);
            countView.setText("" + item.getCount());

            View plusBtn = rowView.findViewById(R.id.plus_btn);
            View minusBtn = rowView.findViewById(R.id.minus_btn);
            View editBtn = rowView.findViewById(R.id.edit_item);
            View deleteBtn = rowView.findViewById(R.id.delete_item);


            plusBtn.setOnClickListener(view -> listener.onAddCountClicked(item));
            minusBtn.setOnClickListener(view -> listener.onMinusCountClicked(item));
            deleteBtn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.deleteitem_confirm)
                        .setPositiveButton("Yes", (dialogInterface, i) -> listener.onDeleteItemClicked(item))
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();

            });
            editBtn.setOnClickListener(view -> {
                DialogFragment dialog = new EditItemDialog();
                Bundle bundle = new Bundle();
                bundle.putString("itemName", item.getTitle());
                bundle.putString("itemId", item.getId());
                bundle.putInt("itemStep", item.getStep());
                dialog.setArguments(bundle);
                dialog.show(getActivity().getFragmentManager(), "editItem");
            });
            titleView.setOnClickListener(view -> listener.onItemNameClicked(rv));

            return rowView;
        }
    }

    public interface ItemsListener {
        void onAddCountClicked(Item item);

        void onMinusCountClicked(Item item);

        void onItemNameClicked(View view);

        void onDeleteItemClicked(Item item);

        void onEditItemClicked(Item item);
    }
}
