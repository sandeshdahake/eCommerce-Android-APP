package com.android.ecommerce.ux.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.SettingsMy;
import com.android.ecommerce.api.EndPoints;
import com.android.ecommerce.api.GsonRequest;
import com.android.ecommerce.entities.Metadata;
import com.android.ecommerce.entities.User;
import com.android.ecommerce.entities.product.ImageUrlFromApi;
import com.android.ecommerce.entities.product.ProductMetadata;
import com.android.ecommerce.entities.productList.Product;
import com.android.ecommerce.interfaces.ProductImagesRecyclerInterface;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.utils.JsonUtils;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.RecyclerMarginDecorator;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.MainActivity;
import com.android.ecommerce.ux.adapters.ProductImagesRecyclerAdapter;
import com.android.ecommerce.ux.dialogs.ProductImagesDialogFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.MessageDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mbanje.kurt.fabbutton.FabButton;
import timber.log.Timber;

/**
 * Fragment shows a detail of the product.
 */
public class ProductFragment extends Fragment {

    private ProductMetadata productMetadata ;
    private JsonObject productJsonData ;

    private static final String PRODUCT_ID = "product_id";

    private ProgressBar progressView;

    // Fields referencing complex screen layouts.
    private View layoutEmpty;
    private RelativeLayout productContainer;
    private ScrollView contentScrollLayout;
    ArrayList<String> productImagesUrls;

    // Fields referencing product related views.
    private TextView productNameTv;
    private TextView productPriceDiscountTv;
    private TextView productPriceTv;
    private TextView productInfoTv;
    private TextView productPriceDiscountPercentTv;

    /**
     * Refers to the displayed product.
     */
    private Product product;

    /**
     * Spinner offering all available product colors.
     */
    private ViewPager viewPager;
    private RecyclerView productImagesRecycler;
    private ViewTreeObserver.OnScrollChangedListener scrollViewListener;
    private ProductImagesRecyclerAdapter productImagesAdapter;

    // Indicates running add product to cart request.
    private ImageView addToCartImage;
    private ProgressBar addToCartProgress;

    /**
     * Floating button allowing add/remove product from wishlist.
     */
    private FabButton wishlistButton;
    /**
     * Determine if product is in wishlist.
     */
    private boolean inWishlist = false;
    /**
     * Id of the wishlist item representing product.
     */
    private long wishlistId = CONST.DEFAULT_EMPTY_ID;
    private TabLayout tabLayout;

    /**
     * Create a new fragment instance for product detail.
     *
     * @param productId id of the product to show.
     * @return new fragment instance.
     */
    public static ProductFragment newInstance(String productId) {
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Product));

        View view = inflater.inflate(R.layout.fragment_product, container, false);

        progressView = (ProgressBar) view.findViewById(R.id.product_progress);

        productContainer = (RelativeLayout) view.findViewById(R.id.product_container);
        layoutEmpty = view.findViewById(R.id.product_empty_layout);
        contentScrollLayout = (ScrollView) view.findViewById(R.id.product_scroll_layout);


        productNameTv = (TextView) view.findViewById(R.id.product_name);
        productPriceTv = (TextView) view.findViewById(R.id.product_price);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        // prepareButtons(view);
        prepareProductImagesLayout(view);
        prepareScrollViewAndWishlist(view);

        String productId = getArguments().getString(PRODUCT_ID);
        getProduct(productId);
        return view;
    }

    /**
     * Prepare buttons views and listeners.
     *
     * @param view fragment base view.
     */
    private void prepareButtons(View view) {


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProductDetailAffiliate(), "Compare Prices");
        adapter.addFragment(new ProductDetailSpecs(), "SPECS");
        adapter.addFragment(new ProductDetailReview(), "Reviews");
        viewPager.setAdapter(adapter);
    }


    /**
     * Prepare product images and related products views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    private void prepareProductImagesLayout(View view) {
        productImagesRecycler = (RecyclerView) view.findViewById(R.id.product_images_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productImagesRecycler.setLayoutManager(linearLayoutManager);
        productImagesAdapter = new ProductImagesRecyclerAdapter(getActivity(), new ProductImagesRecyclerInterface() {
            @Override
            public void onImageSelected(View v, int position) {
                ProductImagesDialogFragment imagesDialog = ProductImagesDialogFragment.newInstance(productImagesUrls, position);
                if (imagesDialog != null)
                    imagesDialog.show(getFragmentManager(), ProductImagesDialogFragment.class.getSimpleName());
                else {
                    Timber.e("%s Called with empty image list", ProductImagesDialogFragment.class.getSimpleName());
                }
            }
        });
        productImagesRecycler.setAdapter(productImagesAdapter);

        ViewGroup.LayoutParams params = productImagesRecycler.getLayoutParams();
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        // For small screen even smaller images.
        if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            params.height = (int) (dm.heightPixels * 0.4);
        } else {
            params.height = (int) (dm.heightPixels * 0.48);
        }

/*
        // Prepare related products
        RecyclerView relatedProductsRecycler = (RecyclerView) view.findViewById(R.id.product_recommended_images_recycler);
        relatedProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        relatedProductsRecycler.addItemDecoration(new RecyclerMarginDecorator(getContext(), RecyclerMarginDecorator.ORIENTATION.HORIZONTAL));
        relatedProductsAdapter = new RelatedProductsRecyclerAdapter(getActivity(), new RelatedProductsRecyclerInterface() {
            @Override
            public void onRelatedProductSelected(View v, int position, Product product) {
                if (product != null && getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).onProductSelected(product.getId());
                }
            }

        });
        relatedProductsRecycler.setAdapter(relatedProductsAdapter);
*/
    }

    /**
     * Prepare scroll view related animations and floating wishlist button.
     *
     * @param view fragment base view.
     */
    private void prepareScrollViewAndWishlist(View view) {
        final View productBackground = view.findViewById(R.id.product_background);
        wishlistButton = (FabButton) view.findViewById(R.id.product_add_to_wish_list);

        scrollViewListener = new ViewTreeObserver.OnScrollChangedListener() {
            private boolean alphaFull = false;

            @Override
            public void onScrollChanged() {
                int scrollY = contentScrollLayout.getScrollY();
                if (productImagesRecycler != null) {

                    if (wishlistButton.getWidth() * 2 > scrollY) {
                        wishlistButton.setTranslationX(scrollY / 4);
                    } else {
                        wishlistButton.setTranslationX(wishlistButton.getWidth() / 2);
                    }

                    float alphaRatio;
                    if (productImagesRecycler.getHeight() > scrollY) {
                        productImagesRecycler.setTranslationY(scrollY / 2);
                        alphaRatio = (float) scrollY / productImagesRecycler.getHeight();
                    } else {
                        alphaRatio = 1;
                    }
//                    Timber.e("scrollY:" + scrollY + ". Alpha:" + alphaRatio);

                    if (alphaFull) {
                        if (alphaRatio <= 0.99) alphaFull = false;
                    } else {
                        if (alphaRatio >= 0.9) alphaFull = true;
                        productBackground.setAlpha(alphaRatio);
                    }
                } else {
                    Timber.e("Null productImagesScroll");
                }
            }
        };
    }

    /**
     * Load product data.
     *
     * @param productId id of product.
     */
    private void getProduct(final String productId) {
        // Load product info
        String url = String.format(EndPoints.PRODUCT);
        url = url + productId;
        setContentVisible(CONST.VISIBLE.PROGRESS);

        GsonRequest<JsonObject> getProductRequest = new GsonRequest<>(Request.Method.GET, url, null, JsonObject.class,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull JsonObject response) {
                          String subCatID = response.get("Properties").getAsJsonObject().get("SubcategoryId").toString();
                          ProductMetadata metadata = SettingsMy.getSubCategory(subCatID) ;
                          if(metadata == null){
                                 PopulateProductUsingMetadata(response, subCatID, null);
                          }else{
                                 PopulateProductUsingMetadata(response, subCatID, metadata);
                          }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setContentVisible(CONST.VISIBLE.EMPTY);
                MsgUtils.logAndShowErrorMessage(getActivity(), error);
            }
        });
        getProductRequest.setRetryPolicy(MyApplication.getDefaultRetryPolice());
        getProductRequest.setShouldCache(false);
        MyApplication.getInstance().addToRequestQueue(getProductRequest, CONST.PRODUCT_REQUESTS_TAG);
    }

    private void PopulateProductUsingMetadata(final JsonObject productData, final String subCatID, ProductMetadata metadata) {
        if(metadata == null){
            String url = String.format(EndPoints.PRODUCTS_METADATA);
            url = url + subCatID;
            GsonRequest<ProductMetadata> getProductMetadataRequest = new GsonRequest<>(Request.Method.GET, url, null, ProductMetadata.class,
                    new Response.Listener<ProductMetadata>() {
                        @Override
                        public void onResponse(@NonNull ProductMetadata response) {
                            productMetadata = response;
                            SettingsMy.setSubCategory(subCatID, productMetadata);
                            productJsonData = productData;
                            refreshScreenData(response, productData);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    setContentVisible(CONST.VISIBLE.EMPTY);
                    MsgUtils.logAndShowErrorMessage(getActivity(), error);
                }
            },null,null,CONST.PRODUCT_METADATA_TAG);
            getProductMetadataRequest.setRetryPolicy(MyApplication.getDefaultRetryPolice());
            getProductMetadataRequest.setShouldCache(false);
            MyApplication.getInstance().addToRequestQueue(getProductMetadataRequest, CONST.PRODUCT_METADATA_TAG);

        }else{
            productMetadata = metadata;
            productJsonData = productData;
            refreshScreenData(metadata, productData);

        }
    }

    private void refreshScreenData(ProductMetadata metadata, JsonObject productData) {
        setContentVisible(CONST.VISIBLE.CONTENT);

        if (productData != null && metadata != null) {
            JsonObject properties = productData.get("Properties").getAsJsonObject();

            MainActivity.setActionBarTitle(properties.get("Name").toString());
            productNameTv.setText(properties.get("Name").toString());

            JsonArray images = properties.getAsJsonArray("images");
            Type listType = new TypeToken<List<ImageUrlFromApi>>() {}.getType();
            List<ImageUrlFromApi> list = new Gson().fromJson(images.getAsJsonArray(), listType);
            productImagesUrls = new ArrayList<String>();
            if (productImagesAdapter != null) {
                productImagesAdapter.clearAll();
                for (ImageUrlFromApi element : list){
                    productImagesAdapter.addLast(element.getUrl());
                    productImagesUrls.add(element.getUrl());
                }
            }
        } else {
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, getString(R.string.Internal_error), MsgUtils.ToastLength.LONG);
            Timber.e(new RuntimeException(), "Refresh product screen with null product");
        }
    }


    /**
     * Display content layout, progress bar or empty layout.
     *
     * @param visible enum value defining visible layout.
     */
    private void setContentVisible(CONST.VISIBLE visible) {
        if (layoutEmpty != null && contentScrollLayout != null && progressView != null) {
            switch (visible) {
                case EMPTY:
                    layoutEmpty.setVisibility(View.VISIBLE);
                    contentScrollLayout.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.GONE);
                    break;
                case PROGRESS:
                    layoutEmpty.setVisibility(View.GONE);
                    contentScrollLayout.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.VISIBLE);
                    break;
                default: // Content
                    layoutEmpty.setVisibility(View.GONE);
                    contentScrollLayout.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
            }
        } else {
            Timber.e(new RuntimeException(), "Setting content visibility with null views.");
        }
    }


    @Override
    public void onResume() {
        if (contentScrollLayout != null) contentScrollLayout.getViewTreeObserver().addOnScrollChangedListener(scrollViewListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (contentScrollLayout != null) contentScrollLayout.getViewTreeObserver().removeOnScrollChangedListener(scrollViewListener);
        super.onPause();
    }

    @Override
    public void onStop() {
        MyApplication.getInstance().cancelPendingRequests(CONST.PRODUCT_REQUESTS_TAG);
        MyApplication.getInstance().cancelPendingRequests(CONST.PRODUCT_METADATA_TAG);
        super.onStop();
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
