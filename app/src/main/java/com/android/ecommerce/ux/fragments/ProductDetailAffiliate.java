package com.android.ecommerce.ux.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.ecommerce.R;
import com.android.ecommerce.entities.drawerMenu.DrawerItemSubCategory;
import com.android.ecommerce.entities.product.WebStoreProductDetail;
import com.android.ecommerce.entities.productList.Product;
import com.android.ecommerce.interfaces.CategoryRecyclerInterface;
import com.android.ecommerce.interfaces.WebStoreRecyclerInterface;
import com.android.ecommerce.utils.RecyclerMarginDecorator;
import com.android.ecommerce.ux.MainActivity;
import com.android.ecommerce.ux.adapters.ProductsRecyclerAdapter;
import com.android.ecommerce.ux.adapters.WebStoreRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


/**
 * Created by sandeshdahake@gmail.com on 19-09-2016.
 */
public class ProductDetailAffiliate extends Fragment {

   private List<WebStoreProductDetail> listOfWebStore;
    private TextView emptyContentView;
    private RecyclerView storeRecycle;
    private LinearLayoutManager shopRecyclerLayoutManager;
    private WebStoreRecyclerAdapter adapter;

    public static ProductDetailAffiliate newInstance(List<WebStoreProductDetail> listOfWebStore) {
        Bundle args = new Bundle();
        args.putSerializable("storeList", (Serializable) listOfWebStore);
        ProductDetailAffiliate fragment = new ProductDetailAffiliate();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductDetailAffiliate newInstance() {
        ProductDetailAffiliate fragment = new ProductDetailAffiliate();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.fragment_affiliate, container, false);
        this.emptyContentView = (TextView) view.findViewById(R.id.shop_empty);
        this.storeRecycle = (RecyclerView) view.findViewById(R.id.shop_recycler);
        if(adapter == null ||adapter.getItemCount() == 0){
            prepareAdapter();
            prepareShopRecycle(view);
            Bundle startBundle = getArguments();
            if (startBundle != null) {
             List<WebStoreProductDetail> list = (List<WebStoreProductDetail>) startBundle.getSerializable("storeList");
                adapter.addShop(list);
            }

        }else{
            prepareShopRecycle(view);

        }
        checkEmptyContent();

        return view;
    }

    private void prepareAdapter() {
        adapter = new WebStoreRecyclerAdapter(getContext(), new WebStoreRecyclerInterface() {

            @Override
            public void onWebStoreSelected(View view, String url) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                }
                ((MainActivity) getActivity()).onShopSelected(url);

            }
        });

    }

    private void checkEmptyContent() {
        if (adapter != null && adapter.getItemCount() > 0) {
/*
            emptyContentView.setVisibility(View.INVISIBLE);
            storeRecycle.setVisibility(View.VISIBLE);
*/
            emptyContentView.setVisibility(View.VISIBLE);
            storeRecycle.setVisibility(View.INVISIBLE);

        } else {
            emptyContentView.setVisibility(View.VISIBLE);
            storeRecycle.setVisibility(View.INVISIBLE);
        }
    }


    private void prepareShopRecycle(View view) {

        storeRecycle.addItemDecoration(new RecyclerMarginDecorator(getActivity(), RecyclerMarginDecorator.ORIENTATION.BOTH));
        storeRecycle.setItemAnimator(new DefaultItemAnimator());
        storeRecycle.setHasFixedSize(true);
        shopRecyclerLayoutManager = new LinearLayoutManager(getContext());
        storeRecycle.setLayoutManager(shopRecyclerLayoutManager);

    }

}