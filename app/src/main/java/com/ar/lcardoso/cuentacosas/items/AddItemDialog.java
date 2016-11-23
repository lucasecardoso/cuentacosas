package com.ar.lcardoso.cuentacosas.items;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ar.lcardoso.cuentacosas.R;

/**
 * Created by Lucas on 21/11/2016.
 */

public class AddItemDialog extends DialogFragment {

    private AddItemDialogListener mListener;
    private View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (AddItemDialogListener) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            mListener = (AddItemDialogListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Pass null as the parent view because it's going in the dialog layout
        mView = inflater.inflate(R.layout.additem_dialog, null);
        builder.setView(mView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText et = (EditText) mView.findViewById(R.id.additem_name);
                mListener.onDialogPositiveClick(AddItemDialog.this, et.getText().toString());
            }
        });

        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onDialogNegativeClick(AddItemDialog.this);
            }
        });

        return builder.create();
    }

    public interface AddItemDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String itemName);
        void onDialogNegativeClick(DialogFragment dialog);
    }
}
