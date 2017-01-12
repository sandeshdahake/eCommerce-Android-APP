package com.android.ecommerce.ux.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import android.support.v4.app.Fragment;


/**
 * Created by sandeshdahake@gmail.com on 13-01-2017.
 */

public class WebViewFragment extends Fragment {

    public WebView mWebView;

    public static Fragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_webview, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        String url = getArguments().getString("url");
        mWebView.loadUrl(url);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

}