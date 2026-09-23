package vpadn;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import c.CordovaWebView;
import com.google.android.gms.plus.PlusShare;
import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: vpadn.f, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class C0092f extends WebViewClient {
    InterfaceC0102p a;
    CordovaWebView b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f340c = false;
    private Hashtable<String, C0086a> d = new Hashtable<>();

    public C0092f(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        this.a = interfaceC0102p;
        this.b = cordovaWebView;
    }

    public final void a(CordovaWebView cordovaWebView) {
        this.b = cordovaWebView;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) throws NumberFormatException {
        String strSubstring;
        if (this.b.a == null || !this.b.a.b(str)) {
            if (str.startsWith("tel:")) {
                try {
                    Intent intent = new Intent("android.intent.action.DIAL");
                    intent.setData(Uri.parse(str));
                    this.a.a().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    C0104r.e("Cordova", "Error dialing " + str + ": " + e.toString());
                }
            } else if (str.startsWith("geo:")) {
                try {
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setData(Uri.parse(str));
                    this.a.a().startActivity(intent2);
                } catch (ActivityNotFoundException e2) {
                    C0104r.e("Cordova", "Error showing map " + str + ": " + e2.toString());
                }
            } else if (str.startsWith("mailto:")) {
                try {
                    Intent intent3 = new Intent("android.intent.action.VIEW");
                    intent3.setData(Uri.parse(str));
                    this.a.a().startActivity(intent3);
                } catch (ActivityNotFoundException e3) {
                    C0104r.e("Cordova", "Error sending email " + str + ": " + e3.toString());
                }
            } else if (str.startsWith("sms:")) {
                try {
                    Intent intent4 = new Intent("android.intent.action.VIEW");
                    int iIndexOf = str.indexOf(63);
                    if (iIndexOf == -1) {
                        strSubstring = str.substring(4);
                    } else {
                        strSubstring = str.substring(4, iIndexOf);
                        String query = Uri.parse(str).getQuery();
                        if (query != null && query.startsWith("body=")) {
                            intent4.putExtra("sms_body", query.substring(5));
                        }
                    }
                    intent4.setData(Uri.parse("sms:" + strSubstring));
                    intent4.putExtra("address", strSubstring);
                    intent4.setType("vnd.android-dir/mms-sms");
                    this.a.a().startActivity(intent4);
                } catch (ActivityNotFoundException e4) {
                    C0104r.e("Cordova", "Error sending sms " + str + ":" + e4.toString());
                }
            } else if (str.startsWith("file://") || str.startsWith("data:") || str.indexOf(this.b.f160c) == 0 || C0088b.a(str)) {
                if (this.b.d || str.startsWith("data:")) {
                    return false;
                }
                this.b.loadUrl(str);
            } else {
                try {
                    Intent intent5 = new Intent("android.intent.action.VIEW");
                    intent5.setData(Uri.parse(str));
                    this.a.a().startActivity(intent5);
                } catch (ActivityNotFoundException e5) {
                    C0104r.b("Cordova", "Error loading url " + str, e5);
                }
            }
        }
        return true;
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        C0086a c0086a = this.d.get(str.concat(str2));
        if (c0086a == null) {
            c0086a = this.d.get(str);
            if (c0086a == null) {
                c0086a = this.d.get(str2);
            }
            if (c0086a == null) {
                c0086a = this.d.get("");
            }
        }
        if (c0086a != null) {
            httpAuthHandler.proceed(null, null);
        } else {
            super.onReceivedHttpAuthRequest(webView, httpAuthHandler, str, str2);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (!this.b.d) {
            webView.clearHistory();
            this.f340c = true;
        }
        this.b.f.a();
        this.b.a("onPageStarted", (Object) str);
        if (this.b.a != null) {
            this.b.a.c();
        }
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) throws NumberFormatException {
        super.onPageFinished(webView, str);
        C0104r.b("Cordova", "onPageFinished(" + str + ")");
        if (this.f340c) {
            webView.clearHistory();
            this.f340c = false;
        }
        this.b.e++;
        if (!str.equals("about:blank")) {
            C0104r.b("Cordova", "Trying to fire onNativeReady");
            this.b.loadUrl("javascript:try{ cordova.require('cordova/channel').onNativeReady.fire();}catch(e){_nativeReady = true;}");
            this.b.a("onNativeReady", (Object) null);
        }
        this.b.a("onPageFinished", (Object) str);
        if (this.b.getVisibility() == 4) {
            new Thread(new Runnable() { // from class: vpadn.f.1
                @Override // java.lang.Runnable
                public final void run() throws InterruptedException {
                    try {
                        Thread.sleep(2000L);
                        C0092f.this.a.a().runOnUiThread(new Runnable() { // from class: vpadn.f.1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                C0092f.this.b.a("spinner", (Object) "stop");
                            }
                        });
                    } catch (InterruptedException e) {
                    }
                }
            }).start();
        }
        if (str.equals("about:blank")) {
            this.b.a("exit", (Object) null);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int i, String str, String str2) {
        C0104r.b("Cordova", "CordovaWebViewClient.onReceivedError: Error code=%s Description=%s URL=%s", Integer.valueOf(i), str, str2);
        this.b.e++;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("errorCode", i);
            jSONObject.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
            jSONObject.put("url", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.b.a("onReceivedError", jSONObject);
    }

    @Override // android.webkit.WebViewClient
    @TargetApi(8)
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        try {
            if ((this.a.a().getPackageManager().getApplicationInfo(this.a.a().getPackageName(), 128).flags & 2) != 0) {
                sslErrorHandler.proceed();
            } else {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        } catch (PackageManager.NameNotFoundException e) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    @Override // android.webkit.WebViewClient
    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
        if (!this.b.b().equals(str)) {
            this.b.d(str);
        }
    }
}
