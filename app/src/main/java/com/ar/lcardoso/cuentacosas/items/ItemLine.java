package com.ar.lcardoso.cuentacosas.items;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ar.lcardoso.cuentacosas.R;

/**
 * Created by Lucas on 1/10/2016.
 */

public class ItemLine extends LinearLayout {

    private int mCount = 0;

    public ItemLine(Context context) {
        super(context);
        initializeViews(context);
    }

    public ItemLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public ItemLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        Log.d("DEBUG", "initializeViews() called");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_line, this);
        postInflateTasks();
    }

    protected void postInflateTasks() {
        super.onFinishInflate();

        Log.d("DEBUG", "postInflateTasks() called");

        final EditText et = (EditText)this.findViewById(R.id.edit_text);
        final TextView tv = (TextView) this.findViewById(R.id.item_title);

        final View plusBtn = this.findViewById(R.id.plus_btn);
        final View minusBtn = this.findViewById(R.id.minus_btn);

        et.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                switchTextViews();
            }

            return true;
        });

        et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                switchTextViews();
                hideKeyboard(v);
            }
        });

        tv.setOnClickListener(v -> switchTextViews());
        et.setOnClickListener(v -> switchTextViews());

        plusBtn.setOnClickListener((v) -> addToCounter());
        minusBtn.setOnClickListener((v) -> removeFromCounter());
    }

    private void switchTextViews() {
        final TextView tv = (TextView) this.findViewById(R.id.item_title);
        final EditText et = (EditText)this.findViewById(R.id.edit_text);

        if (tv.getVisibility() == VISIBLE) {
            tv.setVisibility(GONE);
            et.setVisibility(VISIBLE);
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        } else {
            tv.setText(et.getText());
            et.setVisibility(GONE);
            tv.setVisibility(VISIBLE);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addToCounter() {
        mCount++;
        TextView counter = (TextView) this.findViewById(+ R.id.count_text);
        counter.setText("" + mCount);
    }

    public void removeFromCounter() {
        mCount--;
        TextView counter = (TextView) this.findViewById(R.id.count_text);
        counter.setText("" + mCount);
    }
}
