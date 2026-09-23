package c;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;
import org.json.JSONException;
import vpadn.C0088b;
import vpadn.C0090d;
import vpadn.C0092f;
import vpadn.C0098l;
import vpadn.C0099m;
import vpadn.C0104r;
import vpadn.C0107u;
import vpadn.C0108v;
import vpadn.InterfaceC0102p;
import vpadn.ab;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CordovaWebView extends WebView {
    private static FrameLayout.LayoutParams s = new FrameLayout.LayoutParams(-1, -1, 17);
    public C0107u a;
    C0092f b;

    /* renamed from: c, reason: collision with root package name */
    public String f160c;
    public boolean d;
    public int e;
    public C0099m f;
    public ExposedJsApi g;
    private ArrayList<Integer> h;
    private ArrayList<Integer> i;
    private InterfaceC0102p j;
    private C0090d k;
    private String l;
    private Stack<String> m;
    private boolean n;
    private long o;
    private String p;
    private View q;
    private WebChromeClient.CustomViewCallback r;

    /* JADX WARN: Multi-variable type inference failed */
    public CordovaWebView(Context context) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super(((Activity) context).getApplicationContext());
        this.h = new ArrayList<>();
        this.i = new ArrayList<>();
        this.m = new Stack<>();
        this.d = true;
        this.e = 0;
        this.o = 0L;
        if (InterfaceC0102p.class.isInstance(context)) {
            this.j = (InterfaceC0102p) context;
        } else {
            ab.a("CordovaWebView", "Your activity must implement CordovaInterface to work");
        }
        i();
        h();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CordovaWebView(Context context, InterfaceC0102p interfaceC0102p) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super(((Activity) context).getApplicationContext());
        this.h = new ArrayList<>();
        this.i = new ArrayList<>();
        this.m = new Stack<>();
        this.d = true;
        this.e = 0;
        this.o = 0L;
        if (InterfaceC0102p.class.isInstance(context)) {
            this.j = (InterfaceC0102p) context;
        } else {
            this.j = interfaceC0102p;
            ab.a("CordovaWebView", "Your activity must implement CordovaInterface to work");
        }
        i();
        h();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CordovaWebView(Context context, AttributeSet attributeSet) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super(((Activity) context).getApplicationContext(), attributeSet);
        this.h = new ArrayList<>();
        this.i = new ArrayList<>();
        this.m = new Stack<>();
        this.d = true;
        this.e = 0;
        this.o = 0L;
        if (InterfaceC0102p.class.isInstance(context)) {
            this.j = (InterfaceC0102p) context;
        } else {
            ab.a("CordovaWebView", "Your activity must implement CordovaInterface to work");
        }
        setWebChromeClient(new C0090d(this.j, this));
        InterfaceC0102p interfaceC0102p = this.j;
        if (Build.VERSION.SDK_INT < 11) {
            setWebViewClient(new C0092f(this.j, this));
        } else {
            setWebViewClient((C0092f) new C0098l(this.j, this));
        }
        i();
        h();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CordovaWebView(Context context, AttributeSet attributeSet, int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super(((Activity) context).getApplicationContext(), attributeSet, i);
        this.h = new ArrayList<>();
        this.i = new ArrayList<>();
        this.m = new Stack<>();
        this.d = true;
        this.e = 0;
        this.o = 0L;
        if (InterfaceC0102p.class.isInstance(context)) {
            this.j = (InterfaceC0102p) context;
        } else {
            ab.a("CordovaWebView", "Your activity must implement CordovaInterface to work");
        }
        setWebChromeClient(new C0090d(this.j, this));
        i();
        h();
    }

    @SuppressLint({"NewApi"})
    private void h() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setInitialScale(0);
        setVerticalScrollBarEnabled(false);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        try {
            Method method = WebSettings.class.getMethod("setNavDump", Boolean.TYPE);
            if (Build.VERSION.SDK_INT < 11) {
                method.invoke(settings, true);
            }
        } catch (IllegalAccessException e) {
            ab.a("CordovaWebView", "This should never happen: IllegalAccessException means this isn't Android anymore");
        } catch (IllegalArgumentException e2) {
            ab.a("CordovaWebView", "Doing the NavDump failed with bad arguments");
        } catch (NoSuchMethodException e3) {
            ab.a("CordovaWebView", "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
        } catch (InvocationTargetException e4) {
            ab.a("CordovaWebView", "This should never happen: InvocationTargetException means this isn't Android anymore.");
        }
        if (Build.VERSION.SDK_INT > 15) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setDatabaseEnabled(true);
        String path = this.j.a().getApplicationContext().getDir("database", 0).getPath();
        settings.setDatabasePath(path);
        settings.setGeolocationDatabasePath(path);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(5242880L);
        settings.setAppCachePath(this.j.a().getApplicationContext().getDir("database", 0).getPath());
        settings.setAppCacheEnabled(true);
        this.a = new C0107u(this, this.j);
        this.f = new C0099m(this, this.j);
        this.g = new ExposedJsApi(this.a, this.f);
        int i = Build.VERSION.SDK_INT;
        if ((i >= 11 && i <= 13) || i < 9) {
            ab.c("CordovaWebView", "Disabled addJavascriptInterface() bridge since Android version is old.");
        } else if (i >= 11 || !Build.MANUFACTURER.equals(NetworkManager.TYPE_UNKNOWN)) {
            addJavascriptInterface(this.g, "_cordovaNative");
        } else {
            ab.c("CordovaWebView", "Disabled addJavascriptInterface() bridge callback due to a bug on the 2.3 emulator");
        }
    }

    public void setWebViewClient(C0092f c0092f) {
        this.b = c0092f;
        super.setWebViewClient((WebViewClient) c0092f);
    }

    public void setWebChromeClient(C0090d c0090d) {
        this.k = c0090d;
        super.setWebChromeClient((WebChromeClient) c0090d);
    }

    public final C0090d a() {
        return this.k;
    }

    @Override // android.webkit.WebView
    public void loadUrl(String str) throws NumberFormatException {
        if (str.equals("about:blank") || str.startsWith("javascript:")) {
            b(str);
            return;
        }
        String strA = a("url", (String) null);
        if (strA == null || this.m.size() > 0) {
            f(str);
        } else {
            f(strA);
        }
    }

    private void f(final String str) throws NumberFormatException {
        C0104r.b("CordovaWebView", ">>> loadUrl(" + str + ")");
        this.l = str;
        if (this.f160c == null) {
            int iLastIndexOf = str.lastIndexOf(47);
            if (iLastIndexOf > 0) {
                this.f160c = str.substring(0, iLastIndexOf + 1);
            } else {
                this.f160c = String.valueOf(this.l) + "/";
            }
            if (this.f160c.startsWith("http://tw.adon.vpon.com/xpon/activity")) {
                this.f160c = "http://tw.adon.vpon.com/xpon/";
            }
            this.a.a();
            if (!this.d) {
                this.m.push(str);
            }
        }
        final int i = this.e;
        final int i2 = Integer.parseInt(a("loadUrlTimeoutValue", "20000"));
        final Runnable runnable = new Runnable() { // from class: c.CordovaWebView.1
            @Override // java.lang.Runnable
            public final void run() {
                C0104r.e("CordovaWebView", "CordovaWebView: TIMEOUT ERROR!");
                this.stopLoading();
                if (CordovaWebView.this.b != null) {
                    CordovaWebView.this.b.onReceivedError(this, -6, "The connection to the server was unsuccessful.", str);
                }
            }
        };
        final Runnable runnable2 = new Runnable(this) { // from class: c.CordovaWebView.2
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    synchronized (this) {
                        wait(i2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.e == i) {
                    this.j.a().runOnUiThread(runnable);
                }
            }
        };
        this.j.a().runOnUiThread(new Runnable(this) { // from class: c.CordovaWebView.3
            @Override // java.lang.Runnable
            public final void run() {
                new Thread(runnable2).start();
                this.b(str);
            }
        });
    }

    @Override // android.webkit.WebView
    public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) throws NumberFormatException {
        this.l = null;
        this.f160c = str;
        this.p = str2;
        this.a.a();
        final int i = this.e;
        final int i2 = Integer.parseInt(a("loadUrlTimeoutValue", "20000"));
        final Runnable runnable = new Runnable() { // from class: c.CordovaWebView.4
            @Override // java.lang.Runnable
            public final void run() {
                C0104r.e("CordovaWebView", "CordovaWebView: TIMEOUT ERROR!--loadDataWithBaseURL");
                if (this != null) {
                    try {
                        this.stopLoading();
                    } catch (Exception e) {
                    }
                }
                if (CordovaWebView.this.b != null) {
                    CordovaWebView.this.b.onReceivedError(this, -6, "The connection to the server was unsuccessful.", CordovaWebView.this.l);
                }
            }
        };
        final Runnable runnable2 = new Runnable(this) { // from class: c.CordovaWebView.5
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    synchronized (this) {
                        wait(i2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.e == i) {
                    this.j.a().runOnUiThread(runnable);
                }
            }
        };
        this.j.a().runOnUiThread(new Runnable() { // from class: c.CordovaWebView.6
            @Override // java.lang.Runnable
            public final void run() {
                new Thread(runnable2).start();
                this.a(CordovaWebView.this.p);
            }
        });
    }

    final void a(String str) {
        try {
            WebSettings settings = getSettings();
            if (settings == null) {
                ab.b("CordovaWebView", "this.getSettings() == null ??");
            } else {
                settings.setDefaultTextEncodingName("utf-8");
                super.loadDataWithBaseURL(this.f160c, str, "text/html", "utf-8", null);
            }
        } catch (Exception e) {
            ab.a("CordovaWebView", "loadDataWithBaseURLNow throw Exception ", e);
        }
    }

    public final void b(String str) {
        if (C0104r.a(3) && !str.startsWith("javascript:")) {
            C0104r.b("CordovaWebView", ">>> loadUrlNow()");
        }
        if (str != null && this.f160c != null) {
            if (str.startsWith("file://") || str.indexOf(this.f160c) == 0 || str.startsWith("javascript:") || C0088b.a(str)) {
                super.loadUrl(str);
            }
        }
    }

    public final void c(String str) {
        this.f.a(str);
    }

    public final void a(C0108v c0108v, String str) {
        this.f.a(c0108v, str);
    }

    public final void a(String str, Object obj) {
        if (this.a != null) {
            this.a.a(str, obj);
        }
    }

    public final String b() {
        return this.m.size() > 0 ? this.m.peek() : "";
    }

    public final void d(String str) {
        this.m.push(str);
    }

    public final boolean c() throws NumberFormatException {
        if (super.canGoBack() && this.d) {
            WebBackForwardList webBackForwardListCopyBackForwardList = copyBackForwardList();
            int size = webBackForwardListCopyBackForwardList.getSize();
            for (int i = 0; i < size; i++) {
                C0104r.b("CordovaWebView", "The URL at index: " + Integer.toString(i) + "is " + webBackForwardListCopyBackForwardList.getItemAtIndex(i).getUrl());
            }
            super.goBack();
            return true;
        }
        if (this.m.size() <= 1 || this.d) {
            return false;
        }
        this.m.pop();
        loadUrl(this.m.pop());
        return true;
    }

    @Override // android.webkit.WebView
    public boolean canGoBack() {
        return (super.canGoBack() && this.d) || this.m.size() > 1;
    }

    public final void a(String str, boolean z, boolean z2) throws NumberFormatException {
        C0104r.b("CordovaWebView", "showWebPage(%s, %b, %b, HashMap", str, Boolean.valueOf(z), Boolean.valueOf(z2));
        if (z2) {
            clearHistory();
        }
        if (!z) {
            if (str.startsWith("file://") || str.indexOf(this.f160c) == 0 || C0088b.a(str)) {
                if (z2) {
                    this.m.clear();
                }
                loadUrl(str);
                return;
            }
            C0104r.d("CordovaWebView", "showWebPage: Cannot load URL into webview since it is not in white list.  Loading into browser instead. (URL=" + str + ")");
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                this.j.a().startActivity(intent);
                return;
            } catch (ActivityNotFoundException e) {
                C0104r.b("CordovaWebView", "Error loading url " + str, e);
                return;
            }
        }
        try {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(Uri.parse(str));
            this.j.a().startActivity(intent2);
        } catch (ActivityNotFoundException e2) {
            C0104r.b("CordovaWebView", "Error loading url " + str, e2);
        }
    }

    private void i() {
        if ("false".equals(a("useBrowserHistory", "true"))) {
            this.d = false;
            ab.d("CordovaWebView", "useBrowserHistory=false is deprecated as of Cordova 2.2.0 and will be removed six months after the 2.2.0 release.  Please use the browser history and use history.back().");
        }
        if ("true".equals(a("fullscreen", "false"))) {
            this.j.a().getWindow().clearFlags(2048);
            this.j.a().getWindow().setFlags(1024, 1024);
        }
    }

    private String a(String str, String str2) {
        Object obj;
        Bundle extras = this.j.a().getIntent().getExtras();
        return (extras == null || (obj = extras.get(str)) == null) ? str2 : obj.toString();
    }

    @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) throws NumberFormatException {
        boolean zEquals;
        if (this.h.contains(Integer.valueOf(i))) {
            if (i == 25) {
                C0104r.b("CordovaWebView", "Down Key Hit");
                loadUrl("javascript:cordova.fireDocumentEvent('volumedownbutton');");
                return true;
            }
            if (i == 24) {
                C0104r.b("CordovaWebView", "Up Key Hit");
                loadUrl("javascript:cordova.fireDocumentEvent('volumeupbutton');");
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }
        if (i == 4) {
            if (!this.d) {
                return this.m.size() > 1 || this.n;
            }
            WebHistoryItem itemAtIndex = copyBackForwardList().getItemAtIndex(0);
            if (itemAtIndex != null) {
                String url = itemAtIndex.getUrl();
                String url2 = getUrl();
                C0104r.b("CordovaWebView", "The current URL is: " + url2);
                C0104r.b("CordovaWebView", "The URL at item 0 is:" + url);
                zEquals = url2.equals(url);
            } else {
                zEquals = false;
            }
            return !zEquals || this.n;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) throws NumberFormatException {
        if (i == 4) {
            if (this.q != null) {
                f();
            } else {
                if (!this.n) {
                    return c();
                }
                loadUrl("javascript:cordova.fireDocumentEvent('backbutton');");
                return true;
            }
        } else {
            if (i == 82) {
                if (this.o < keyEvent.getEventTime()) {
                    loadUrl("javascript:cordova.fireDocumentEvent('menubutton');");
                }
                this.o = keyEvent.getEventTime();
                return super.onKeyUp(i, keyEvent);
            }
            if (i == 84) {
                loadUrl("javascript:cordova.fireDocumentEvent('searchbutton');");
                return true;
            }
            if (this.i.contains(Integer.valueOf(i))) {
                return super.onKeyUp(i, keyEvent);
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    public final void a(boolean z) {
        this.n = z;
    }

    public final void e(String str) {
        if (str.compareTo("volumeup") == 0) {
            this.h.add(24);
        } else if (str.compareTo("volumedown") == 0) {
            this.h.add(25);
        }
    }

    public final boolean d() {
        return this.n;
    }

    public void b(boolean z) throws NumberFormatException {
        C0104r.b("CordovaWebView", "Handle the pause");
        loadUrl("javascript:try{cordova.fireDocumentEvent('pause');}catch(e){console.log('exception firing pause event from native');};");
        if (this.a != null) {
            this.a.a(z);
        }
        if (!z) {
            pauseTimers();
        }
    }

    public void a(boolean z, boolean z2) throws NumberFormatException {
        loadUrl("javascript:try{cordova.fireDocumentEvent('resume');}catch(e){console.log('exception firing resume event from native');};");
        if (this.a != null) {
            this.a.b(z);
        }
        resumeTimers();
    }

    public final void e() throws NumberFormatException {
        loadUrl("javascript:try{cordova.require('cordova/channel').onDestroy.fire();}catch(e){console.log('exception firing destroy event from native');};");
        loadUrl("about:blank");
        if (this.a != null) {
            this.a.b();
        }
    }

    public final void a(Intent intent) {
        if (this.a != null) {
            this.a.a(intent);
        }
    }

    public void a(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        ab.a("CordovaWebView", "showing Custom View");
        if (this.q != null) {
            customViewCallback.onCustomViewHidden();
            return;
        }
        this.q = view;
        this.r = customViewCallback;
        ViewGroup viewGroup = (ViewGroup) getParent();
        viewGroup.addView(view, s);
        setVisibility(8);
        viewGroup.setVisibility(0);
        viewGroup.bringToFront();
    }

    public void f() {
        ab.a("CordovaWebView", "Hidding Custom View");
        if (this.q != null) {
            this.q.setVisibility(8);
            ((ViewGroup) getParent()).removeView(this.q);
            this.q = null;
            this.r.onCustomViewHidden();
            setVisibility(0);
        }
    }

    public final boolean g() {
        return this.q != null;
    }

    @Override // android.webkit.WebView
    public WebBackForwardList restoreState(Bundle bundle) {
        WebBackForwardList webBackForwardListRestoreState = super.restoreState(bundle);
        ab.a("CordovaWebView", "WebView restoration crew now restoring!");
        this.a.a();
        return webBackForwardListRestoreState;
    }

    public final String a(String str, String str2, String str3) {
        try {
            return this.g.exec("VponSdk", str, str2, str3);
        } catch (JSONException e) {
            ab.a("VPON", "testVponCordovaPlugin throws Exception", e);
            return null;
        }
    }
}
