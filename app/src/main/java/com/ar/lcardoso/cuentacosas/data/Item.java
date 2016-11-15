package com.ar.lcardoso.cuentacosas.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by Lucas on 14/11/2016.
 */

public class Item {

    @NonNull
    private final String id;

    @Nullable
    private String title;

    private int count;

    public Item(@Nullable String title) {
        this(UUID.randomUUID().toString(), title, 0);
    }

    public Item(@NonNull String id, @Nullable String title, int count) {
        this.id = id;
        this.title = title;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public String getId() { return id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
