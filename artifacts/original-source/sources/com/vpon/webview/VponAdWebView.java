package com.vpon.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.FrameLayout;
import c.CordovaWebView;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import vpadn.C0090d;
import vpadn.C0092f;
import vpadn.InterfaceC0102p;
import vpadn.M;
import vpadn.ab;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VponAdWebView extends CordovaWebView {
    private String h;
    private M i;
    private int j;
    private int k;
    private boolean l;
    private Activity m;
    private int n;

    public VponAdWebView(String str, Activity activity, M m, InterfaceC0102p interfaceC0102p) {
        super(activity, interfaceC0102p);
        this.h = null;
        this.l = true;
        this.m = null;
        this.h = str;
        this.i = m;
        this.m = activity;
        i();
        setWebViewClient((C0092f) new c(interfaceC0102p, this));
        setWebChromeClient((C0090d) new b(interfaceC0102p, this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public VponAdWebView(String str, Activity activity, M m) {
        super(activity);
        this.h = null;
        this.l = true;
        this.m = null;
        this.h = str;
        this.i = m;
        this.m = activity;
        i();
        setWebViewClient((C0092f) new c((InterfaceC0102p) activity, this));
        setWebChromeClient((C0090d) new b((InterfaceC0102p) activity, this));
    }

    @SuppressLint({"NewApi"})
    private void i() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 17) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(8388608L);
        if (this.m != null && this.m.getApplicationContext() != null && this.m.getApplicationContext().getCacheDir() != null) {
            settings.setAppCachePath(this.m.getApplicationContext().getCacheDir().getAbsolutePath());
        } else {
            ab.b("VponAdWebView", "mActivity.getApplicationContext().getCacheDir().getAbsolutePath() has NPE");
        }
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);
        settings.setUserAgentString(String.valueOf(settings.getUserAgentString()) + " (Mobile; vpadn-sdk-a-v4.2.5)");
    }

    public final String h() {
        return this.h;
    }

    public void setVponWebViewId(String str) {
        this.h = str;
    }

    public void setWebViewJsAlertShow(boolean z) {
        this.l = z;
    }

    public void setAdRect(int i, int i2) {
        this.j = i;
        this.k = i2;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x00aa  */
    @Override // c.CordovaWebView, android.webkit.WebView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void loadDataWithBaseURL(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11) {
        /*
            r6 = this;
            java.lang.String r0 = r6.h
            java.lang.String r1 = "init"
            r0.equals(r1)
            java.lang.String r0 = "mraid.js"
            boolean r0 = r8.contains(r0)
            if (r0 == 0) goto Laa
            java.lang.String r0 = r6.h
            java.lang.String r1 = "bannerWebView"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L48
            java.lang.String r0 = "<head>"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "<head><script type='text/javascript' charset='utf-8' src='"
            r1.<init>(r2)
            vpadn.P r2 = vpadn.P.a()
            java.lang.String r3 = "mraid2_banner"
            java.lang.Object r2 = r2.a(r3)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "'></script>"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r8 = r8.replace(r0, r1)
            r2 = r8
        L3f:
            r0 = r6
            r1 = r7
            r3 = r9
            r4 = r10
            r5 = r11
            super.loadDataWithBaseURL(r1, r2, r3, r4, r5)
            return
        L48:
            java.lang.String r0 = r6.h
            java.lang.String r1 = "SdkOpenWebApp"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            java.lang.String r0 = "<head>"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "<head><script type='text/javascript' charset='utf-8' src='"
            r1.<init>(r2)
            vpadn.P r2 = vpadn.P.a()
            java.lang.String r3 = "mraid2_expanded"
            java.lang.Object r2 = r2.a(r3)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "'></script>"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r8 = r8.replace(r0, r1)
            r2 = r8
            goto L3f
        L79:
            java.lang.String r0 = r6.h
            java.lang.String r1 = "InterstitialAdWebView(new Activity)"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Laa
            java.lang.String r0 = "<head>"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "<head><script type='text/javascript' charset='utf-8' src='"
            r1.<init>(r2)
            vpadn.P r2 = vpadn.P.a()
            java.lang.String r3 = "mraid2_intersitial"
            java.lang.Object r2 = r2.a(r3)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = "'></script>"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r8 = r8.replace(r0, r1)
            r2 = r8
            goto L3f
        Laa:
            r2 = r8
            goto L3f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vpon.webview.VponAdWebView.loadDataWithBaseURL(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    class b extends C0090d {
        public b(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
            super(interfaceC0102p, cordovaWebView);
        }

        @Override // android.webkit.WebChromeClient
        public final void onProgressChanged(WebView webView, int i) {
            if (i == 100 && VponAdWebView.this.i != null) {
                VponAdWebView.this.i.f();
            }
        }

        public final void onReachedMaxAppCacheSize(long j, long j2, WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(2 * j);
        }

        @Override // vpadn.C0090d, android.webkit.WebChromeClient
        public final boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            ab.b("VponAdWebView", "MESSAGE:" + str2);
            if (VponAdWebView.this.l) {
                return super.onJsAlert(webView, str, str2, jsResult);
            }
            return true;
        }
    }

    class c extends C0092f {
        public c(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
            super(interfaceC0102p, cordovaWebView);
        }

        @Override // vpadn.C0092f, android.webkit.WebViewClient
        public final void onReceivedError(WebView webView, int i, String str, String str2) {
            ab.b("VponAdWebView", "VponAdWebView onReceivedError errorCode:" + i + " des:" + str + " failingUrl:" + str2);
            VponAdWebView.this.i.a(webView, i, str, str2);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onWindowVisibilityChanged(int i) {
        ab.c("VponAdWebView", "-------->visibility:" + i);
        if (this.i != null) {
            if (i == 0) {
                this.i.c();
            } else {
                this.i.d();
            }
        }
        super.onWindowVisibilityChanged(i);
    }

    @Override // android.widget.AbsoluteLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Rect rect = new Rect();
        this.m.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i5 = rect.top;
        int[] iArr = {0, 0};
        getLocationOnScreen(iArr);
        int i6 = iArr[0];
        int i7 = iArr[1] - i5;
        int i8 = (i3 - i) + i6;
        int i9 = (i4 - i2) + i7;
        if (this.i != null && z) {
            this.i.a(i6, i7, i8, i9);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.i != null) {
            this.i.a(i, i2);
        }
    }

    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        int i3 = this.j;
        int i4 = this.k;
        int i5 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
        int i6 = (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) ? size2 : Integer.MAX_VALUE;
        if (i3 > i5 || i4 > i6) {
            ab.b("vpon ad display", "Not enough space for ad");
            setVisibility(8);
            setMeasuredDimension(size, size2);
        } else {
            setMeasuredDimension(i3, i4);
        }
        super.onMeasure(i, i2);
    }

    class a extends FrameLayout {
        public a(Context context) {
            super(context);
            setFocusableInTouchMode(true);
        }

        @Override // android.view.View, android.view.KeyEvent.Callback
        public final boolean onKeyUp(int i, KeyEvent keyEvent) {
            View focusedChild = getFocusedChild();
            if (!VponAdWebView.this.g() && (focusedChild == null || i != 4 || (!VponAdWebView.this.h.equals("bannerWebViewExpanded") && !VponAdWebView.this.h.equals("bannerWebViewResized")))) {
                return super.onKeyUp(i, keyEvent);
            }
            VponAdWebView.this.f();
            return true;
        }

        @Override // android.view.View, android.view.KeyEvent.Callback
        public final boolean onKeyDown(int i, KeyEvent keyEvent) {
            return super.onKeyDown(i, keyEvent);
        }
    }

    @Override // c.CordovaWebView
    @SuppressLint({"NewApi"})
    public final void a(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        view.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        if (this.h != null && (this.h.equals("bannerWebViewExpanded") || this.h.equals("bannerWebViewResized"))) {
            a aVar = new a(this.m);
            aVar.addView(view);
            super.a(aVar, customViewCallback);
            aVar.requestFocus();
        } else {
            super.a(view, customViewCallback);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            this.n = viewGroup.getSystemUiVisibility();
            viewGroup.setSystemUiVisibility(256);
        }
    }

    @Override // c.CordovaWebView
    @SuppressLint({"NewApi"})
    public final void f() {
        ViewGroup viewGroup;
        ab.a("VponAdWebView", "enter hideCustomView");
        if (Build.VERSION.SDK_INT >= 11 && (viewGroup = (ViewGroup) getParent()) != null) {
            viewGroup.setSystemUiVisibility(this.n);
        }
        super.f();
        if (this.h != null) {
            if (this.h.equals("bannerWebViewExpanded") || this.h.equals("bannerWebViewResized")) {
                requestFocus();
            }
        }
    }

    @Override // c.CordovaWebView
    @SuppressLint({"NewApi"})
    public final void b(boolean z) {
        super.b(z);
        if (Build.VERSION.SDK_INT >= 11) {
            onPause();
        }
    }

    @Override // c.CordovaWebView
    @SuppressLint({"NewApi"})
    public final void a(boolean z, boolean z2) {
        super.a(z, z2);
        if (Build.VERSION.SDK_INT >= 11) {
            onResume();
        }
    }

    @Override // c.CordovaWebView, android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        ab.a("VponAdWebView", "------->onKeyUp");
        if (this.h == null || !((this.h.equals("bannerWebViewExpanded") || this.h.equals("bannerWebViewResized")) && i == 4)) {
            return super.onKeyUp(i, keyEvent);
        }
        if (this.i != null) {
            this.i.m();
        }
        return true;
    }
}
