package jp.co.imobile.sdkads.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class e extends WebView {
    private Boolean a;
    private Boolean b;

    /* renamed from: c, reason: collision with root package name */
    private Boolean f297c;
    private Boolean d;
    private ImobileSdkAdListener e;

    e(Context context, Boolean bool) {
        super(context);
        this.a = true;
        this.b = false;
        this.f297c = false;
        this.d = false;
        this.e = null;
        this.b = bool;
        x.a(null);
    }

    private static Boolean a(ViewGroup viewGroup) {
        while (true) {
            try {
                ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
                if (viewGroup2 == null) {
                    return false;
                }
                if (viewGroup2.getVisibility() != 0) {
                    return true;
                }
                viewGroup = viewGroup2;
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    final Boolean a() {
        if (a(this).booleanValue()) {
            x.a(null);
            return false;
        }
        new StringBuilder("AdWebView getHasFocus : ").append(this.a);
        x.a(null);
        return this.a;
    }

    public final void a(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
    }

    final void a(String str) {
        super.loadUrl(str);
    }

    final void a(String str, String str2, String str3, String str4) {
        super.loadDataWithBaseURL(str, str2, str3, str4, null);
    }

    final void a(ImobileSdkAdListener imobileSdkAdListener) {
        x.a(null);
        this.d = true;
        this.e = imobileSdkAdListener;
    }

    @Override // android.webkit.WebView
    public final boolean canGoBack() {
        return false;
    }

    @Override // android.webkit.WebView
    public final boolean canGoBackOrForward(int i) {
        return false;
    }

    @Override // android.webkit.WebView
    public final boolean canGoForward() {
        return false;
    }

    @Override // android.webkit.WebView
    public final void clearView() {
    }

    @Override // android.webkit.WebView
    public final Bitmap getFavicon() {
        return null;
    }

    @Override // android.webkit.WebView
    public final String getTitle() {
        return null;
    }

    @Override // android.webkit.WebView
    public final String getUrl() {
        return null;
    }

    @Override // android.webkit.WebView
    public final void goBack() {
    }

    @Override // android.webkit.WebView
    public final void goBackOrForward(int i) {
    }

    @Override // android.webkit.WebView
    public final void goForward() {
    }

    @Override // android.webkit.WebView
    public final void loadData(String str, String str2, String str3) {
    }

    @Override // android.webkit.WebView
    public final void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
    }

    @Override // android.webkit.WebView
    public final void loadUrl(String str) {
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        if (!this.b.booleanValue() && getParent() != null && !this.f297c.booleanValue()) {
            this.f297c = true;
            ((ViewGroup) getParent()).removeView(this);
            removeAllViews();
            destroy();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.webkit.WebView, android.view.View
    public final void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.a = Boolean.valueOf(hasFocus);
        new StringBuilder("AdWebView Status change onWindowFocusChanged:").append(this.a);
        x.a(null);
        if (this.d.booleanValue() && hasFocus && this.e != null) {
            x.a(null);
            this.e.onDismissAdScreen();
        }
    }

    @Override // android.webkit.WebView
    public final boolean pageDown(boolean z) {
        return false;
    }

    @Override // android.webkit.WebView
    public final boolean pageUp(boolean z) {
        return false;
    }

    @Override // android.webkit.WebView
    public final void reload() {
    }

    @Override // android.webkit.WebView
    public final void setDownloadListener(DownloadListener downloadListener) {
    }

    @Override // android.webkit.WebView
    public final void setWebChromeClient(WebChromeClient webChromeClient) {
    }

    @Override // android.webkit.WebView
    public final void setWebViewClient(WebViewClient webViewClient) {
    }

    @Override // android.webkit.WebView
    public final void stopLoading() {
    }
}
