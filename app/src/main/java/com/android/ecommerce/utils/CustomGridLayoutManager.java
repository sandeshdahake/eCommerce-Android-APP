package com.android.ecommerce.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by sandeshdahake@gmail.com on 04-01-2017.
 */

public class CustomGridLayoutManager extends GridLayoutManager {
    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //override this method and implement code as below
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
