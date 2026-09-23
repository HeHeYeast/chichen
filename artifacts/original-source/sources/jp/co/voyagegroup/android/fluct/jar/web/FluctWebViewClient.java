package jp.co.voyagegroup.android.fluct.jar.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctWebViewClient extends WebViewClient {
    private static final String TAG = "FluctWebViewClient";
    private FluctInterstitialActivity mInterstitialActivity;

    public FluctWebViewClient() {
        Log.d(TAG, "FluctWebViewClient : ");
    }

    public FluctWebViewClient(FluctInterstitialActivity activity) {
        Log.d(TAG, "FluctWebViewClient : activity ");
        this.mInterstitialActivity = activity;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Log.d(TAG, "shouldOverrideUrlLoading : url is " + url);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(FluctUtils.replaceParams(webView.getContext(), url)));
        intent.addFlags(268435456);
        webView.getContext().startActivity(intent);
        if (this.mInterstitialActivity != null) {
            this.mInterstitialActivity.callback(1);
        }
        return true;
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted : url is " + url + " webview is " + webView);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String url) {
        Log.d(TAG, "onPageFinished : url is " + url + " webview is " + webView);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int errorCode, String description, String url) {
        Log.e(TAG, "onReceivedError : view is " + webView);
        Log.e(TAG, "onReceivedError : errorCode is " + errorCode);
        Log.e(TAG, "onReceivedError : description is " + description);
        Log.e(TAG, "onReceivedError : failingUrl is " + url);
        if (this.mInterstitialActivity != null) {
            this.mInterstitialActivity.callback(100);
        }
    }
}
