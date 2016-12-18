package com.android.ecommerce.ux.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.entities.Banner;
import com.android.ecommerce.entities.Metadata;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.MainActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import timber.log.Timber;

/**
 * Provides "welcome" screen customizable from web administration. Often contains banners with sales or best products.
 */
public class BannersFragment extends Fragment {

    private ProgressDialog progressDialog;

    // content related fields.
    private RecyclerView bannersRecycler;
    private Metadata bannersMetadata;

    /**
     * Indicates if user data should be loaded from server or from memory.
     */
    private boolean mAlreadyLoaded = false;

    /**
     * Holds reference for empty view. Show to user when no data loaded.
     */
    private View emptyContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Just_arrived));

        View view = inflater.inflate(R.layout.fragment_banners, container, false);

        progressDialog = Utils.generateProgressDialog(getActivity(), false);

        prepareEmptyContent(view);

        return view;
    }

    private void prepareEmptyContent(View view) {
        emptyContent = view.findViewById(R.id.banners_empty);
        TextView emptyContentAction = (TextView) view.findViewById(R.id.banners_empty_action);
        emptyContentAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // Just open drawer menu.
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) activity;
                    if (mainActivity.drawerFragment != null)
                        mainActivity.drawerFragment.toggleDrawerMenu();
                }
            }
        });
    }


    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.BANNER_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (bannersRecycler != null) bannersRecycler.clearOnScrollListeners();
        super.onDestroyView();
    }
}
