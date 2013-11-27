package com.hasgeek.funnel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ExploreEventFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView w = new WebView(getActivity());
        w.setWebViewClient(new WebViewClient());
        w.loadUrl("http://droidcon.in/2013/explore-app");
        return w;
    }

}
