package org.secuso.privacyfriendlynotes.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.secuso.privacyfriendlynotes.PlatformFragment;
import org.secuso.privacyfriendlynotes.code_old.HelpActivity;
import org.secuso.privacyfriendlynotes.R;

/**
 * Created by Robin on 11.09.2016.
 */
public class WelcomeDialog extends DialogFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       // LayoutInflater i = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       //builder.setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome_dialog, null));
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome_dialog, null);
        WebView webView = (WebView) view.findViewById(R.id.web123);
        webView.setWebViewClient(new WebViewClient());

        //webView.loadData("<html>这是一段HTML的代码</html>","text/html", "utf-8");
        webView.loadUrl("http://140.131.114.157:80/Carbon-Forum-5.9.0");//加载url

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//不使用缓存，只从网络获取数据.



        builder.setView(view);

        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.dialog_welcome));
        builder.setPositiveButton(getActivity().getString(R.string.dialog_ok), null);
        builder.setNegativeButton(getActivity().getString(R.string.dialog_help), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), PlatformFragment.class));
            }
        });

        return builder.create();
    }
}