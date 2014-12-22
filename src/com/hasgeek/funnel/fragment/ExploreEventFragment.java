package com.hasgeek.funnel.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
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
        webView.getSettings().setDomStorageEnabled(true);
//        String nick;
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
//
//        if (!sp.getString("sb_nick", "1234").equalsIgnoreCase("1234")) {
//            nick=sp.getString("sb_nick", "1234");
//        }
//        else
//        {
//            final EditText input = new EditText(getActivity());
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Update Status")
//                    .setMessage("")
//                    .setView(input)
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        final String in=input.getText().toString();
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            PreferenceManager.getDefaultSharedPreferences(getActivity())
//                            .edit()
//                            .putString("sb_nick",(in.equalsIgnoreCase("") ? "hasgeeknick" : in))
//                            .apply();
//                        }
//                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    // Do nothing.
//                }
//            }).show();
//            nick=sp.getString("sb_nick", "1234");
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
        webView.loadUrl("https://scrollback.io/hasgeek?webview={}");
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
