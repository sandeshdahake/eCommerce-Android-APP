package com.android.ecommerce.ux.fragments;

import android.app.ProgressDialog;
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
        final ProgressDialog pd = ProgressDialog.show(this.getContext(), "Compare Dunia", "Take a deep breath and happy shopping...",true);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }
        });
        mWebView.loadUrl("http://www.yahoo.co.in");
        String url = getArguments().getString("url");
        mWebView.loadUrl(url);
        // Enable Javascript
        return v;
    }

}