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
import com.android.ecommerce.api.EndPoints;
import com.android.ecommerce.api.GsonRequest;
import com.android.ecommerce.entities.Banner;
import com.android.ecommerce.entities.BannerProducts;
import com.android.ecommerce.entities.BannersResponse;
import com.android.ecommerce.entities.Metadata;
import com.android.ecommerce.interfaces.BannersRecyclerInterface;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.utils.EndlessRecyclerScrollListener;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.MainActivity;
import com.android.ecommerce.ux.adapters.BannersRecyclerAdapter;
import com.android.ecommerce.ux.adapters.HorizontalBannerListAdapter;
import com.android.ecommerce.ux.adapters.HorizontalProductListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.net.URL;

import timber.log.Timber;

/**
 * Provides "welcome" screen customizable from web administration. Often contains banners with sales or best products.
 */
public class BannersFragment extends Fragment {

    private ProgressDialog progressDialog;

    // content related fields.

    private RecyclerView horizontal_recycler_view_banner;
    private HorizontalBannerListAdapter horizontalAdapterBanner;

    private RecyclerView horizontal_recycler_view_popular;
    private HorizontalProductListAdapter horizontalAdapterPopular;

    private RecyclerView horizontal_recycler_view_featured;
    private HorizontalProductListAdapter horizontalAdapterFeatured;

    /**
     * Indicates if user data should be loaded from server or from memory.
     */
    private boolean mAlreadyLoadedBanner = false;
    private boolean mAlreadyLoadedNew = false;
    private boolean mAlreadyLoadedPopuler = false;

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
        // Don't reload data when return from backStack. Reload if a new instance was created or data was empty.
       //// TODO: 29-12-2016  handel empty adapter for each list
            prepareContentViews(view, true);
            loadBanners(null);
            loadPopuler(null);
            loadFeatured(null);

        return view;
    }

    private void loadPopuler(String url) {
        progressDialog.show();
        if (url == null) {
            horizontalAdapterPopular.clear();
            url = String.format(EndPoints.BANNERS_POPULAR);
        }

        GsonRequest<BannersResponse> getBannersRequest = new GsonRequest<>(Request.Method.GET, url, null, BannersResponse.class,
                new Response.Listener<BannersResponse>() {
                    @Override
                    public void onResponse(@NonNull BannersResponse response) {
                        Timber.d("response for loadPopuler: %s", response.toString());
                        horizontalAdapterPopular.addBannerProducts(response.getPopularRecords());

                        if (horizontalAdapterPopular.getItemCount() > 0) {
                            emptyContent.setVisibility(View.INVISIBLE);
                            horizontal_recycler_view_popular.setVisibility(View.VISIBLE);
                        } else {
                            emptyContent.setVisibility(View.VISIBLE);
                            horizontal_recycler_view_popular.setVisibility(View.INVISIBLE);
                        }

                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        },null,null,CONST.BANNER_REQUESTS_POPULAR_TAG);
        getBannersRequest.setRetryPolicy(MyApplication.getDefaultRetryPolice());
        getBannersRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getBannersRequest, CONST.BANNER_REQUESTS_POPULAR_TAG);
    }

    private void loadFeatured(String url) {
        progressDialog.show();
        if (url == null) {
            horizontalAdapterFeatured.clear();
            url = String.format(EndPoints.BANNERS_FEATURED);
        }

        GsonRequest<BannersResponse> getBannersRequest = new GsonRequest<>(Request.Method.GET, url, null, BannersResponse.class,
                new Response.Listener<BannersResponse>() {
                    @Override
                    public void onResponse(@NonNull BannersResponse response) {
                        Timber.d("response for loadFeatured: %s", response.toString());
                        horizontalAdapterFeatured.addBannerProducts(response.getFeaturedRecords());

                        if (horizontalAdapterFeatured.getItemCount() > 0) {
                            emptyContent.setVisibility(View.INVISIBLE);
                            horizontal_recycler_view_featured.setVisibility(View.VISIBLE);
                        } else {
                            emptyContent.setVisibility(View.VISIBLE);
                            horizontal_recycler_view_featured.setVisibility(View.INVISIBLE);
                        }

                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        },null,null,CONST.BANNER_REQUESTS_FEATURED_TAG);
        getBannersRequest.setRetryPolicy(MyApplication.getDefaultRetryPolice());
        getBannersRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getBannersRequest, CONST.BANNER_REQUESTS_FEATURED_TAG);
    }

    /**
     * Prepares views and listeners associated with content.
     *
     * @param view       fragment root view.
     * @param freshStart indicates when everything should be recreated.
     */
    private void prepareContentViews(View view, boolean freshStart) {
        horizontal_recycler_view_banner = (RecyclerView) view.findViewById(R.id.horizontal_banner);
        horizontal_recycler_view_featured = (RecyclerView) view.findViewById(R.id.horizontal_recycler_featured);
        horizontal_recycler_view_popular = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view_popular);


            horizontalAdapterBanner = new HorizontalBannerListAdapter(getActivity(), new BannersRecyclerInterface() {
                @Override
                public void onBannerSelected(Banner banner) {
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).onBannerSelected(banner);
                    }
                }

                @Override
                public void onBannerProductSelected(BannerProducts product) {

                }
            });

            horizontalAdapterFeatured = new HorizontalProductListAdapter(getActivity(), new BannersRecyclerInterface() {
                @Override
                public void onBannerSelected(Banner banner) {

                }

                @Override
                public void onBannerProductSelected(BannerProducts product) {
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).onBannerProductSelected(product);
                    }
                }
            });

            horizontalAdapterPopular = new HorizontalProductListAdapter(getActivity(), new BannersRecyclerInterface() {
                @Override
                public void onBannerSelected(Banner banner) {

                }

                @Override
                public void onBannerProductSelected(BannerProducts product) {
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).onBannerProductSelected(product);
                    }
                }
            });


        LinearLayoutManager layoutManagerBanner = new LinearLayoutManager(horizontal_recycler_view_banner.getContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view_banner.setLayoutManager(layoutManagerBanner);
        horizontal_recycler_view_banner.setItemAnimator(new DefaultItemAnimator());
        horizontal_recycler_view_banner.setHasFixedSize(true);
        horizontal_recycler_view_banner.addItemDecoration(new VerticalDividerItemDecoration.Builder(this.getContext()).build());
        horizontal_recycler_view_banner.setAdapter(horizontalAdapterBanner);

        LinearLayoutManager layoutManagerFeatured = new LinearLayoutManager(horizontal_recycler_view_featured.getContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view_featured.setLayoutManager(layoutManagerFeatured);
        horizontal_recycler_view_featured.setItemAnimator(new DefaultItemAnimator());
        horizontal_recycler_view_featured.setHasFixedSize(true);
        horizontal_recycler_view_featured.addItemDecoration(new VerticalDividerItemDecoration.Builder(this.getContext()).build());
        horizontal_recycler_view_featured.setAdapter(horizontalAdapterFeatured);


        LinearLayoutManager layoutManagerPopular = new LinearLayoutManager(horizontal_recycler_view_popular.getContext(),LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view_popular.setLayoutManager(layoutManagerPopular);
        horizontal_recycler_view_popular.setItemAnimator(new DefaultItemAnimator());
        horizontal_recycler_view_popular.setHasFixedSize(true);
        horizontal_recycler_view_popular.addItemDecoration(new VerticalDividerItemDecoration.Builder(this.getContext()).build());
        horizontal_recycler_view_popular.setAdapter(horizontalAdapterPopular);

    }

    /**
     * Prepares views and listeners associated with empty content. Visible only when no content loads.
     *
     * @param view fragment root view.
     */
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

    /**
     * Endless content loader. Should be used after views inflated.
     *
     * @param url null for fresh load. Otherwise use URLs from response metadata.
     */
    private void loadBanners(String url) {
        progressDialog.show();
        if (url == null) {
            horizontalAdapterBanner.clear();
            url = String.format(EndPoints.BANNERS);
        }

            GsonRequest<BannersResponse> getBannersRequest = new GsonRequest<>(Request.Method.GET, url, null, BannersResponse.class,
                new Response.Listener<BannersResponse>() {
                    @Override
                    public void onResponse(@NonNull BannersResponse response) {
                        Timber.d("response: %s", response.toString());
                        horizontalAdapterBanner.addBanners(response.getRecords());

                        if (horizontalAdapterBanner.getItemCount() > 0) {
                            emptyContent.setVisibility(View.INVISIBLE);
                            horizontal_recycler_view_banner.setVisibility(View.VISIBLE);
                        } else {
                            emptyContent.setVisibility(View.VISIBLE);
                            horizontal_recycler_view_banner.setVisibility(View.INVISIBLE);
                        }

                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) progressDialog.cancel();
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        },null,null,CONST.BANNER_REQUESTS_TAG);
        getBannersRequest.setRetryPolicy(MyApplication.getDefaultRetryPolice());
        getBannersRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getBannersRequest, CONST.BANNER_REQUESTS_TAG);
    }

    @Override
    public void onStop() {
        if (progressDialog != null) {
            // Hide progress dialog if exist.
            progressDialog.cancel();
        }
        MyApplication.getInstance().cancelPendingRequests(CONST.BANNER_REQUESTS_TAG);
        MyApplication.getInstance().cancelPendingRequests(CONST.BANNER_REQUESTS_FEATURED_TAG);
        MyApplication.getInstance().cancelPendingRequests(CONST.BANNER_REQUESTS_POPULAR_TAG);

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (horizontal_recycler_view_banner != null) horizontal_recycler_view_banner.clearOnScrollListeners();
        if (horizontal_recycler_view_popular != null) horizontal_recycler_view_popular.clearOnScrollListeners();
        if (horizontal_recycler_view_featured != null) horizontal_recycler_view_featured.clearOnScrollListeners();

        super.onDestroyView();
    }
}
