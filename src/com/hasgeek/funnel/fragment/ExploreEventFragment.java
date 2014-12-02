package com.hasgeek.funnel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hasgeek.funnel.R;

import java.net.URLEncoder;


public class ExploreEventFragment extends Fragment {

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore_event, container, false);
        WebView webView = (WebView) v.findViewById(R.id.wv_explore_event);
        webView.setWebViewClient(mWebViewClient);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("https://scrollback.io/hasgeek?"+URLEncoder.encode("webview={}"));
        mProgressBar = (ProgressBar) v.findViewById(R.id.pb_explore_event);
        return v;
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    };

}
