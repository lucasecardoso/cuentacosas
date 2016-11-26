package com.ar.lcardoso.cuentacosas.items;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ar.lcardoso.cuentacosas.R;
import com.ar.lcardoso.cuentacosas.data.Item;

/**
 * Created by Lucas on 26/11/2016.
 */

public class EditItemDialog extends DialogFragment {

    private EditDialogListener mListener;
    private View mView;
    private Item mItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (EditDialogListener) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            mListener = (EditDialogListener) activity;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = getArguments();

        String itemId = savedInstanceState.getString("itemId");
        String itemName = savedInstanceState.getString("itemName");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mView = inflater.inflate(R.layout.additem_dialog, null);
        builder.setView(mView);

        EditText et = (EditText) mView.findViewById(R.id.additem_name);
        et.setText(itemName);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (et.getText().toString().isEmpty())
                Toast.makeText(getActivity(), R.string.additem_no_item_name_error, Toast.LENGTH_LONG).show();
            else
                mListener.onEditDialogPositiveClick(itemId, et.getText().toString());
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> mListener.onEditDialogNegativeClick());

        return builder.create();
    }

    public void setItem(Item item) {
        this.mItem = item;
    }

    public interface EditDialogListener {
        void onEditDialogPositiveClick(String itemId, String text);
        void onEditDialogNegativeClick();
    }

}
