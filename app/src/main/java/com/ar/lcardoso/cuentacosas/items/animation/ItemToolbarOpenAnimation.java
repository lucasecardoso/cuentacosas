package com.ar.lcardoso.cuentacosas.items.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by Lucas on 24/11/2016.
 */

public class ItemToolbarOpenAnimation extends Animation {

    private final View mView;
    private final LinearLayout.LayoutParams mLayoutParams;
    private final int mMarginFinal = 40;
    private final int mMarginStart;

    public ItemToolbarOpenAnimation(View view, int duration) {
        this.mView = view;
        this.mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        this.mMarginStart = mLayoutParams.bottomMargin;

        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            mLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginFinal - mMarginStart) * interpolatedTime);

            Log.d("DEBUG", "new bottom margin = " + mLayoutParams.bottomMargin);

            mView.requestLayout();
        }
    }
}
