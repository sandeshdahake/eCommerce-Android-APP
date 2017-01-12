package com.android.ecommerce.ux.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ecommerce.R;


/**
 * Created by sandeshdahake@gmail.com on 19-09-2016.
 */
public class ProductDetailAffiliate extends Fragment {

    public ProductDetailAffiliate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_affiliate, container, false);
    }

}