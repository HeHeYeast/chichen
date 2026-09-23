package com.idtinc.ckad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.idtinc.ckchickandduck.AppDelegate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class IconAdView extends WebView {
    private String adUrl;
    private AppDelegate appDelegate;
    private MyFullAdLayout delegate;
    private int finalHeight;
    private int finalWidth;
    public String redirectUri;
    public String redirectUriPk;
    public String redirectUrl;
    public short tag;
    private float zoomRate;

    /* JADX WARN: Multi-variable type inference failed */
    public IconAdView(Context context, int i, int i2, float f, MyFullAdLayout myFullAdLayout) {
        super(context);
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.zoomRate = 1.0f;
        this.tag = (short) -1;
        this.adUrl = "";
        this.redirectUrl = "";
        this.redirectUri = "";
        this.redirectUriPk = "";
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.delegate = myFullAdLayout;
        this.tag = (short) -1;
        setVisibility(8);
        setWebViewClient(new WebViewClient() { // from class: com.idtinc.ckad.IconAdView.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                IconAdView.this.redirectUrl = "";
                IconAdView.this.redirectUri = "";
                IconAdView.this.redirectUriPk = "";
                view.loadUrl("javascript:window.ExtObj.readRedirectUrl(document.getElementsByName('redirecturl')[0].href);");
                if (IconAdView.this.tag < 100) {
                    IconAdView.this.doDisplay();
                } else if (IconAdView.this.tag >= 999) {
                    if (IconAdView.this.redirectUrl == null) {
                        new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.IconAdView.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IconAdView.this.checkRedirectUrlAndDoDisplay();
                            }
                        }, 1500L);
                    } else if (IconAdView.this.redirectUrl.length() < 5) {
                        new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.IconAdView.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                IconAdView.this.checkRedirectUrlAndDoDisplay();
                            }
                        }, 1500L);
                    } else {
                        IconAdView.this.doDisplay();
                    }
                }
                view.loadUrl("javascript:window.ExtObjUri.readRedirectUri(document.getElementsByName('redirecturi')[0].id);");
                view.loadUrl("javascript:window.ExtObjUriPk.readRedirectUriPk(document.getElementsByName('redirecturipk')[0].id);");
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                IconAdView.this.stop();
            }
        });
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        WebSettings settings = getSettings();
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(2);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        addJavascriptInterface(new FindRedirectUrl(this, null), "ExtObj");
        addJavascriptInterface(new FindRedirectUri(this, 0 == true ? 1 : 0), "ExtObjUri");
        addJavascriptInterface(new FindRedirectUriPk(this, 0 == true ? 1 : 0), "ExtObjUriPk");
        this.adUrl = "";
        this.redirectUrl = "";
        this.redirectUri = "";
        this.redirectUriPk = "";
    }

    public void setNewAdUrl(String _adUrl) {
        this.adUrl = "";
        if (_adUrl != null && _adUrl.length() >= 5) {
            this.adUrl = _adUrl;
        }
    }

    public void start() {
        stop();
        if (this.adUrl != null && this.adUrl.length() >= 5 && this.appDelegate.checkInterNet()) {
            loadUrl(this.adUrl);
        }
    }

    public void stop() {
        setVisibility(8);
        stopLoading();
        this.redirectUrl = "";
        this.redirectUri = "";
        this.redirectUriPk = "";
    }

    public void doHidden() {
        setVisibility(8);
    }

    public void doDisplay() {
        setVisibility(0);
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (getVisibility() != 0) {
            return false;
        }
        if (event.getAction() == 0) {
            if (this.appDelegate != null && this.appDelegate.checkInterNet()) {
                Boolean uriOkF = false;
                if (this.redirectUri != null && this.redirectUriPk != null && this.redirectUri.length() >= 5 && this.redirectUriPk.length() >= 5 && this.appDelegate.checkPackageInstalled(this.redirectUriPk)) {
                    uriOkF = true;
                }
                if (uriOkF.booleanValue()) {
                    Uri uri = Uri.parse(this.redirectUri);
                    Intent intent = new Intent("android.intent.action.VIEW", uri);
                    getContext().startActivity(intent);
                } else if (this.redirectUrl.length() > 5) {
                    Uri uri2 = Uri.parse(this.redirectUrl);
                    Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                    getContext().startActivity(intent2);
                }
            }
            if (this.delegate != null) {
                this.delegate.onClickIconAdView(this);
            }
        } else {
            event.getAction();
        }
        return true;
    }

    private class FindRedirectUrl {
        private FindRedirectUrl() {
        }

        /* synthetic */ FindRedirectUrl(IconAdView iconAdView, FindRedirectUrl findRedirectUrl) {
            this();
        }

        public void readRedirectUrl(String _redirectUrl) {
            IconAdView.this.redirectUrl = _redirectUrl;
        }
    }

    private class FindRedirectUri {
        private FindRedirectUri() {
        }

        /* synthetic */ FindRedirectUri(IconAdView iconAdView, FindRedirectUri findRedirectUri) {
            this();
        }

        public void readRedirectUri(String _redirectUri) {
            IconAdView.this.redirectUri = _redirectUri;
        }
    }

    private class FindRedirectUriPk {
        private FindRedirectUriPk() {
        }

        /* synthetic */ FindRedirectUriPk(IconAdView iconAdView, FindRedirectUriPk findRedirectUriPk) {
            this();
        }

        public void readRedirectUriPk(String _redirectUriPk) {
            IconAdView.this.redirectUriPk = _redirectUriPk;
        }
    }

    public void checkRedirectUrlAndDoDisplay() {
        if (this.tag >= 999 && this.redirectUrl != null && this.redirectUrl.length() >= 5) {
            doDisplay();
        }
    }

    public void onDestroy() {
        stopLoading();
        this.delegate = null;
        this.appDelegate = null;
    }
}
