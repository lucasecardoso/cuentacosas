package com.ar.lcardoso.cuentacosas.items;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by Lucas on 23/11/2016.
 */

public class ItemToolbarAnimation extends Animation {

    private final View mAnimatedView;
    private final LinearLayout.LayoutParams mLayoutParams;
    private int mMarginStart, mMarginEnd;

    private boolean isVisibleAfter = false;
    private boolean wasEndedAlready = false;

    public ItemToolbarAnimation(View view, int duration) {
        this.mAnimatedView = view;
        this.mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

        mMarginStart = mLayoutParams.bottomMargin;
        mMarginEnd = (mMarginStart == 0 ? (0 - view.getHeight()) : 0);
        isVisibleAfter = (view.getVisibility() == View.VISIBLE);
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        Log.d("DEBUG", "interpolated time: " + interpolatedTime);

        if (interpolatedTime < 1.0f) {

            mLayoutParams.bottomMargin = mMarginStart + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

            Log.d("DEBUG", "new bottom margin = " + mLayoutParams.bottomMargin);

            mAnimatedView.requestLayout();

        } else if (!wasEndedAlready) {
            mLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();

            if (!isVisibleAfter)
                mAnimatedView.setVisibility(View.GONE);

            wasEndedAlready = true;

        }
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
