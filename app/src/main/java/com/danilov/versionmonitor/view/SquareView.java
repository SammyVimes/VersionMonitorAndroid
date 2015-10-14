package com.danilov.versionmonitor.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Semyon on 14.10.2015.
 */
public class SquareView extends LinearLayout {

    public SquareView(final Context context) {
        super(context);
    }

    public SquareView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
        final View mChildren = getChildAt(0);
        mChildren.measure(size, size);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(final boolean changed, final int l, final int u, final int r,
                            final int d) {
        getChildAt(0).layout(0, 0, r - l, d - u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestLayout() {
        super.requestLayout();
        forceLayout();
    }

}
