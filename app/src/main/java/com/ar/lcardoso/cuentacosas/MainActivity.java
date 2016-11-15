package com.ar.lcardoso.cuentacosas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ar.lcardoso.cuentacosas.items.ItemLine;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void addItemLine(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.inner_layout);

        //NEED TO SET PROGRAMMATICALLY THE WIDTH AND HEIGHT OF THE VIEW
        layout.addView(new ItemLine(this));
    }
}