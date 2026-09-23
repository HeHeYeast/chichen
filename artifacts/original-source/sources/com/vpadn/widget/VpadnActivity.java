package com.vpadn.widget;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import c.NetworkManager;
import com.vpadn.ads.VpadnAdSize;
import com.vpon.webview.VponAdWebView;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0086a;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;
import vpadn.E;
import vpadn.InterfaceC0102p;
import vpadn.M;
import vpadn.N;
import vpadn.P;
import vpadn.ab;
import vpadn.ar;
import vpadn.as;
import vpadn.at;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnActivity extends Activity implements View.OnClickListener, E, M, at, InterfaceC0102p {
    private static final RelativeLayout.LayoutParams J = new RelativeLayout.LayoutParams(-1, -1);
    private long H;
    private long I;
    private as K;
    private int h;
    private int i;
    private String j;
    private boolean k;
    private int l;
    private VponAdWebView a = null;
    private RelativeLayout b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f271c = null;
    private String d = null;
    private boolean e = false;
    private ImageView f = null;
    private C0103q g = null;
    private String m = null;
    private String n = null;
    private String o = null;
    private boolean p = true;
    private boolean q = false;
    private boolean r = false;
    private int s = 16777215;
    private String t = null;
    private ProgressBar u = null;
    private int v = 0;
    private LinearLayout w = null;
    private int x = 0;
    private Button y = null;
    private Button z = null;
    private Button A = null;
    private Button B = null;
    private final ExecutorService C = Executors.newCachedThreadPool();
    private Map<String, Map<Integer, C0101o>> D = Collections.synchronizedMap(new HashMap());
    private Map<String, String> E = Collections.synchronizedMap(new HashMap());
    private boolean F = false;
    private JSONObject G = new JSONObject();
    private int L = 0;
    private boolean M = false;
    private boolean N = true;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) throws Throwable {
        boolean z;
        boolean z2 = true;
        int i = 1;
        ab.a("VponActivity", ">>>>>>>>>Enter onCreate");
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        this.f271c = extras.getString("adType");
        if (C0086a.c(this.f271c)) {
            ab.b("VponActivity", "mAdType is null at VponActivity onCreate method");
            finish();
            return;
        }
        requestWindowFeature(1);
        if (extras.getBoolean("isFullScreen")) {
            getWindow().addFlags(1024);
        }
        getWindow().setFlags(16777216, 16777216);
        C0086a.c(this);
        this.D.clear();
        this.e = extras.getBoolean("isUseCustomClose");
        this.h = extras.getInt("originalRequestedOrientation");
        this.i = extras.getInt("beforeActivityOrientation");
        this.j = extras.getString("forceOrientation");
        if (this.f271c.equals("playVideoWithNativePlayer")) {
            this.k = true;
        } else {
            this.k = extras.getBoolean("isAllowOrientationChange");
        }
        if (!this.j.equals(NetworkManager.TYPE_NONE)) {
            if (this.j.equals("portrait")) {
                setRequestedOrientation(1);
            } else if (this.j.equals("landscape")) {
                setRequestedOrientation(0);
            }
        } else if (!this.k) {
            if (this.i == 2) {
                setRequestedOrientation(0);
            } else if (this.i == 1) {
                setRequestedOrientation(1);
            }
        }
        this.l = extras.getInt("distance");
        this.m = extras.getString("getControllerKey");
        this.n = extras.getString("getCallbackContextKey");
        String string = extras.getString("click_url");
        if (string != null) {
            this.E.put("url_type_click", string);
        }
        this.H = extras.getLong("session_id");
        this.I = extras.getLong("sequence_number");
        this.b = new RelativeLayout(this);
        this.v = extras.getInt("statusBarHeight");
        try {
            if (this.f271c.equals(FluctConstants.XML_NODE_INTERSTITIAL)) {
                this.t = extras.getString("url");
                this.d = extras.getString("html");
                if (!C0086a.c(this.d)) {
                    ab.a("VponActivity", "showInterstitialAd :HTML:" + this.d);
                    this.a = new VponAdWebView("InterstitialAdWebView(new Activity)", this, this);
                    this.a.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                    this.b.setBackgroundColor(0);
                    this.a.setBackgroundColor(extras.containsKey("backgroundColor") ? extras.getInt("backgroundColor") : 0);
                    this.b.addView(this.a, J);
                    new Handler().postDelayed(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.9
                        @Override // java.lang.Runnable
                        public final void run() {
                            if (VpadnActivity.this.e) {
                                return;
                            }
                            VpadnActivity.this.q();
                        }
                    }, 500L);
                    setContentView(this.b);
                    b(false);
                    return;
                }
                ab.b("VponActivity", "show interstitial ad, but mHtml is NULL!!");
                finish();
                return;
            }
            if (this.f271c.equals("sdkOpenWebApp")) {
                ab.a("VponActivity", ">>>>>>>>>Enter onCreate showSdkOpenWebApp!!");
                this.t = extras.getString("url");
                this.d = extras.getString("html");
                this.s = extras.getInt("backgroundColor");
                this.p = extras.getBoolean("isShowProgressBar");
                this.q = extras.getBoolean("isShowNavigationBar");
                this.r = extras.getBoolean("isUseWebViewLoadUrl");
                if (C0086a.c(this.t)) {
                    this.r = false;
                    i = 0;
                }
                if (C0086a.c(this.t) || C0086a.c(this.d)) {
                    i = i;
                } else {
                    this.r = false;
                }
                this.b.setBackgroundColor(0);
                this.a = new VponAdWebView("SdkOpenWebApp", this, this);
                this.a.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                this.a.setBackgroundColor(this.s);
                this.a.setOnTouchListener(new View.OnTouchListener(this) { // from class: com.vpadn.widget.VpadnActivity.1
                    @Override // android.view.View.OnTouchListener
                    public final boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });
                if (this.q) {
                    p();
                }
                o();
                if (this.p) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                    layoutParams.addRule(13);
                    this.u = new ProgressBar(this, null, R.attr.progressBarStyleLarge);
                    this.b.addView(this.u, layoutParams);
                }
                if (!this.e) {
                    q();
                }
                setContentView(this.b);
                if (i != 0 && !this.r) {
                    ab.a("VponActivity", "2 PART CALL asyncLoadHtmlToWebViewByUrl(true)");
                    b(true);
                    return;
                } else if (this.r) {
                    ab.a("VponActivity", "2 PART CALL mShowWebView.loadUrl(mUrl);");
                    this.a.loadUrl(this.t);
                    return;
                } else {
                    if (!C0086a.c(this.d)) {
                        ab.a("VponActivity", "2 PART CALL mShowWebView.loadDataWithBaseURL;");
                        this.a.loadDataWithBaseURL("file:///android_asset/www/vpon", this.d, "text/html", "utf-8", null);
                        return;
                    }
                    return;
                }
            }
            if (this.f271c.equals("playVideoWithNativePlayer")) {
                this.e = true;
                if (this.h == 0 || this.j.equals("landscape")) {
                    z = true;
                    z2 = false;
                } else if (this.h == 1) {
                    setRequestedOrientation(4);
                    z = false;
                } else {
                    z2 = false;
                    z = false;
                }
                this.o = extras.getString("getVideoDataKey");
                P pA = P.a();
                if (this.o == null) {
                    ab.b("VponActivity", "cannot get mGetVideoDataKey");
                    f("PlayVideoEx failed, Cannot get mGetVideoDataKey");
                    finish();
                    return;
                }
                Object objA = pA.a(this.o);
                if (objA == null) {
                    ab.b("VponActivity", "cannot get videoData");
                    finish();
                    return;
                }
                ar arVar = (ar) objA;
                arVar.a(this.H);
                arVar.b(this.I);
                String strD = C0086a.d();
                if (strD != null) {
                    arVar.f(strD);
                }
                pA.b(this.o);
                this.b.setBackgroundColor(arVar.k());
                setContentView(this.b);
                this.K = new as(this, this, arVar);
                this.K.a(z, z2);
                s();
                return;
            }
            ab.b("VponActivity", "illeage AdType:" + this.f271c + " at VponActivity onCreate method");
        } catch (Exception e) {
            ab.a("VponActivity", "VpadnActivity throw Exception", e);
            finish();
        }
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        super.setTheme(R.style.Theme.Translucent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        try {
            if (this.a != null) {
                this.b.removeView(this.a);
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int i = rect.right - rect.left;
                int i2 = (rect.bottom - this.v) - this.x;
                ab.c("VponActivity", "webViewWidth:" + i + " webViewHeight:" + i2 + " mHeightOfNavigationBar:" + this.x);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
                layoutParams.addRule(10);
                layoutParams.addRule(14);
                layoutParams.addRule(13);
                this.b.addView(this.a, layoutParams);
            }
        } catch (Exception e) {
            ab.a("VponActivity", "addWebViewToRelativeLayout throw Exception", e);
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.vpadn.widget.VpadnActivity$6] */
    private void b(final boolean z) {
        try {
            new AsyncTask<Object, Integer, Integer>() { // from class: com.vpadn.widget.VpadnActivity.6
                @Override // android.os.AsyncTask
                protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                    if (z) {
                        if (VpadnActivity.this.d(VpadnActivity.this.t)) {
                            if (VpadnActivity.this.a != null) {
                                VpadnActivity.this.a.loadDataWithBaseURL(VpadnActivity.this.t, VpadnActivity.this.d, "text/html", "utf-8", null);
                            }
                        } else if (VpadnActivity.this.a != null) {
                            VpadnActivity.this.a.loadUrl(VpadnActivity.this.t);
                        }
                    } else if (C0086a.c(VpadnActivity.this.d) || VpadnActivity.this.a == null) {
                        ab.b("VponActivity", "SHOW interstitial ad error (StringUtils.isBlank(mHtml) == true || mShowWebView == null) ");
                    } else {
                        VpadnActivity.this.a.loadDataWithBaseURL(VpadnActivity.this.t, VpadnActivity.this.d, "text/html", "utf-8", null);
                    }
                    return 1;
                }
            }.execute(new Object[0]);
        } catch (Exception e) {
            ab.a("VponActivity", "asyncLoadHtmlToWebViewByUrl throw Exception:", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        int i;
        ab.a("VponActivity", "CALL createNaviationBar");
        boolean z = false;
        if (Resources.getSystem().getConfiguration().orientation == 2) {
            z = true;
        }
        this.w = new LinearLayout(this);
        this.y = new Button(this);
        this.z = new Button(this);
        this.A = new Button(this);
        this.B = new Button(this);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int height = defaultDisplay.getHeight();
        int width = defaultDisplay.getWidth();
        if (z) {
            i = (int) (height * 0.09375d);
        } else {
            i = (int) (height * 0.08333333333333333d);
        }
        this.w.setMinimumHeight(i);
        this.w.setMinimumWidth(width);
        int i2 = width / 4;
        int i3 = (int) (i2 / 2.030075187969925d);
        if (i3 > i) {
            ab.a("VponActivity", "(buttonHeight > navigationBarLayoutHeight) buttonHeight:" + i3 + " navigationBarLayoutHeight" + i);
            i2 = (int) (i * 2.030075187969925d);
            i3 = i;
        }
        ab.a("VponActivity", "screenWidth:" + width + " screenHeight:" + height);
        ab.a("VponActivity", "navigationBarLayoutHeight:" + i + " buttonWidth:" + i2 + " buttonHeight:" + i3);
        this.w.setBackgroundColor(0);
        this.w.setBackgroundDrawable(e("/vpon_bg.png"));
        this.y.setId(991);
        this.y.setBackgroundColor(0);
        this.y.setBackgroundDrawable(e("/vpon_close.png"));
        this.z.setId(992);
        this.z.setBackgroundColor(0);
        this.z.setBackgroundDrawable(e("/vpon_prev.png"));
        this.A.setId(993);
        this.A.setBackgroundColor(0);
        this.A.setBackgroundDrawable(e("/vpon_next.png"));
        this.B.setId(994);
        this.B.setBackgroundColor(0);
        this.B.setBackgroundDrawable(e("/vpon_opennew.png"));
        double d = ((width / 4.0d) - i2) / 2.0d;
        double d2 = (i - i3) / 2.0d;
        ab.a("VponActivity", "buttonMargins:" + d2);
        if (d2 < 0.0d) {
            d2 = 0.0d;
        }
        if (d < 0.0d) {
            d = 0.0d;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i2, i3);
        layoutParams.setMargins((int) d, (int) d2, 0, 0);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i2, i3);
        layoutParams2.setMargins((int) (d * 2.0d), (int) d2, 0, 0);
        this.y.setOnClickListener(this);
        this.z.setOnClickListener(this);
        this.A.setOnClickListener(this);
        this.B.setOnClickListener(this);
        View.OnTouchListener onTouchListener = new View.OnTouchListener() { // from class: com.vpadn.widget.VpadnActivity.8
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (view.getId() == 991) {
                        VpadnActivity.this.y.setBackgroundColor(0);
                        VpadnActivity.this.y.setBackgroundDrawable(VpadnActivity.this.e("/vpon_close_mix.png"));
                    } else if (view.getId() == 992) {
                        VpadnActivity.this.z.setBackgroundColor(0);
                        VpadnActivity.this.z.setBackgroundDrawable(VpadnActivity.this.e("/vpon_prev_mix.png"));
                    } else if (view.getId() == 993) {
                        VpadnActivity.this.A.setBackgroundColor(0);
                        VpadnActivity.this.A.setBackgroundDrawable(VpadnActivity.this.e("/vpon_next_mix.png"));
                    } else if (view.getId() == 994) {
                        VpadnActivity.this.B.setBackgroundColor(0);
                        VpadnActivity.this.B.setBackgroundDrawable(VpadnActivity.this.e("/vpon_opennew_mix.png"));
                    }
                } else if (motionEvent.getAction() == 1) {
                    if (view.getId() == 991) {
                        VpadnActivity.this.y.setBackgroundColor(0);
                        VpadnActivity.this.y.setBackgroundDrawable(VpadnActivity.this.e("/vpon_close.png"));
                    } else if (view.getId() == 992) {
                        VpadnActivity.this.z.setBackgroundColor(0);
                        VpadnActivity.this.z.setBackgroundDrawable(VpadnActivity.this.e("/vpon_prev.png"));
                    } else if (view.getId() == 993) {
                        VpadnActivity.this.A.setBackgroundColor(0);
                        VpadnActivity.this.A.setBackgroundDrawable(VpadnActivity.this.e("/vpon_next.png"));
                    } else if (view.getId() == 994) {
                        VpadnActivity.this.B.setBackgroundColor(0);
                        VpadnActivity.this.B.setBackgroundDrawable(VpadnActivity.this.e("/vpon_opennew.png"));
                    }
                }
                return false;
            }
        };
        this.y.setOnTouchListener(onTouchListener);
        this.z.setOnTouchListener(onTouchListener);
        this.A.setOnTouchListener(onTouchListener);
        this.B.setOnTouchListener(onTouchListener);
        this.w.addView(this.y, layoutParams);
        this.w.addView(this.z, layoutParams2);
        this.w.addView(this.A, layoutParams2);
        this.w.addView(this.B, layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.addRule(12);
        layoutParams3.addRule(14);
        layoutParams3.addRule(13);
        this.b.addView(this.w, layoutParams3);
        this.x = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean d(String str) throws Throwable {
        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            C0086a.a(defaultHttpClient);
            C0086a.a(str, defaultHttpClient);
            Object objA = P.a().a("user-agent");
            if (objA != null) {
                ab.c("VponActivity", "userAgent:" + objA);
                defaultHttpClient.getParams().setParameter("http.useragent", objA);
            }
            HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str));
            C0086a.b(str, defaultHttpClient);
            if (httpResponseExecute.getStatusLine().getStatusCode() != 200) {
                ab.b("VponActivity", ">>>>>getStatusCode() != HttpStatus.SC_OK\n" + httpResponseExecute.getStatusLine().getStatusCode());
                return false;
            }
            this.d = EntityUtils.toString(httpResponseExecute.getEntity(), "UTF-8");
            if (C0086a.c(this.d)) {
                ab.b("VponActivity", "StringUtils.isBlank(mHtml)");
                return false;
            }
            ab.c("VponActivity", "two part mHtml:" + this.d);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Object objA;
        ab.a("VponActivity", "------------------> onDestroy");
        this.M = true;
        super.onDestroy();
        if (this.K != null && this.f271c != null && this.f271c.equals("playVideoWithNativePlayer")) {
            this.K.g();
        }
        if (this.a != null) {
            this.a.setWebViewJsAlertShow(false);
        }
        setRequestedOrientation(this.h);
        P pA = P.a();
        if (C0086a.c(this.f271c)) {
            ab.b("VponActivity", "onDestroy--> StringUtils.isBlank(mAdType)");
        } else if (this.m != null && this.N && (objA = pA.a(this.m)) != null) {
            E e = (E) objA;
            e.j();
            e.h();
            pA.b(this.m);
        }
        if (this.a != null) {
            this.a.stopLoading();
            this.a.removeAllViews();
            this.a.e();
            this.a = null;
        }
        this.b.removeAllViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(boolean z) {
        if (this.b == null) {
            ab.b("VponActivity", "redrawCloseIndicatorButton, but mRelativeLayout == null");
        } else if (z) {
            new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.10
                @Override // java.lang.Runnable
                public final void run() {
                    if (VpadnActivity.this.b.getChildCount() >= 2 && VpadnActivity.this.f != null) {
                        VpadnActivity.this.b.removeView(VpadnActivity.this.f);
                        VpadnActivity.this.f = null;
                    }
                    VpadnActivity.this.q();
                }
            });
        } else if (this.b != null) {
            new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.11
                @Override // java.lang.Runnable
                public final void run() {
                    if (VpadnActivity.this.e || VpadnActivity.this.f != null) {
                        if (VpadnActivity.this.e || VpadnActivity.this.b.getChildCount() < 2 || VpadnActivity.this.f == null) {
                            if (!VpadnActivity.this.e || VpadnActivity.this.b.getChildCount() < 2 || VpadnActivity.this.f == null) {
                                boolean unused = VpadnActivity.this.e;
                                return;
                            } else {
                                VpadnActivity.this.b.removeView(VpadnActivity.this.f);
                                VpadnActivity.this.f = null;
                                return;
                            }
                        }
                        VpadnActivity.this.b.removeView(VpadnActivity.this.f);
                        VpadnActivity.this.f = null;
                        VpadnActivity.this.q();
                        return;
                    }
                    VpadnActivity.this.q();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        ab.a("VponActivity", "call createCloseButton()");
        if (!this.M && this.b != null) {
            this.f = new ImageView(this);
            this.f.setVisibility(0);
            this.f.setBackgroundDrawable(e("/vpon_video2_close.png"));
            this.f.setOnClickListener(new View.OnClickListener() { // from class: com.vpadn.widget.VpadnActivity.12
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VpadnActivity.this.r();
                }
            });
            int i = (int) (C0086a.g(this).density * 50.0f);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i);
            layoutParams.rightMargin = i;
            this.b.addView(this.f, layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        ab.a("VponActivity", "doClose()");
        if (this.a != null) {
            this.a.b(true);
        }
        finish();
    }

    public final void b() {
        new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.13
            @Override // java.lang.Runnable
            public final void run() {
                VpadnActivity.this.r();
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Uri uri;
        ab.a("VponActivity", "v.getId():" + view.getId());
        switch (view.getId()) {
            case 991:
                r();
                break;
            case 992:
                if (this.a.canGoBack()) {
                    ab.b("VponActivity", "CanGoBack()");
                    this.a.goBack();
                    break;
                }
                break;
            case 993:
                this.a.goForward();
                break;
            case 994:
                if (this.t != null) {
                    uri = Uri.parse(this.t);
                } else {
                    uri = Uri.parse(this.a.getUrl());
                }
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                intent.setFlags(268435456);
                startActivity(intent);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable e(String str) {
        return new BitmapDrawable(getClass().getResourceAsStream(str));
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        ab.b("VponActivity", "-------->>>requestCode:" + i + " resultCode:" + i2);
        C0103q c0103q = this.g;
        if (c0103q != null) {
            ab.b("VponActivity", "--------call callback.onActivityResult");
            c0103q.onActivityResult(i, i2, intent);
        } else {
            ab.b("VponActivity", "--------callback == null");
        }
    }

    @Override // vpadn.InterfaceC0102p
    public final void a(C0103q c0103q, Intent intent, int i) {
        ab.b("VponActivity", "-------->>>Call startActivityForResult requestCode:" + i);
        this.g = c0103q;
        startActivityForResult(intent, i);
    }

    @Override // vpadn.InterfaceC0102p
    public final Activity a() {
        return this;
    }

    @Override // vpadn.InterfaceC0102p
    public final Object a(String str, Object obj) {
        if (str.equals("close")) {
            ab.b("VponActivity", "Call onMessage id is close");
            new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.2
                @Override // java.lang.Runnable
                public final void run() {
                    VpadnActivity.this.r();
                }
            });
            return null;
        }
        return null;
    }

    @Override // vpadn.InterfaceC0102p
    public final ExecutorService e() {
        return this.C;
    }

    @Override // vpadn.M
    public final void c() {
        this.F = true;
        a("onshow", (JSONObject) null);
    }

    @Override // vpadn.M
    public final void d() {
        this.F = false;
        a("onhide", (JSONObject) null);
    }

    @Override // vpadn.M
    public final void f() {
        E e;
        E e2;
        if (this.f271c != null && this.f271c.equals("sdkOpenWebApp")) {
            new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.3
                @Override // java.lang.Runnable
                public final void run() {
                    if (VpadnActivity.this.b != null && VpadnActivity.this.u != null) {
                        ab.a("VponActivity", "REMOVE mProgressBar");
                        VpadnActivity.this.b.removeView(VpadnActivity.this.u);
                    }
                }
            });
            if (this.b != null) {
                ab.a("VponActivity", "end onWebViewLoadPageFinish");
                s();
                if (this.m != null && (e2 = (E) P.a().a(this.m)) != null) {
                    e2.i();
                }
            }
        } else if (this.f271c != null && this.f271c.equals(FluctConstants.XML_NODE_INTERSTITIAL) && this.m != null && (e = (E) P.a().a(this.m)) != null) {
            e.i();
        }
        if (this.l > 0) {
            this.a.loadUrl(String.format("javascript:getDistance('%d')", Integer.valueOf(this.l)));
        }
    }

    @Override // vpadn.M
    public final void a(int i, int i2) {
        ab.a("VponActivity", "Call onWebViewSizeChanged w:" + i + " h:" + i2);
        if (this.L != 0) {
            c(false);
            if (this.b != null && this.w != null && this.q) {
                new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.7
                    @Override // java.lang.Runnable
                    public final void run() {
                        VpadnActivity.this.b.removeView(VpadnActivity.this.w);
                        VpadnActivity.this.p();
                    }
                });
            }
        }
        this.L++;
    }

    @Override // vpadn.M
    public final void a(int i, int i2, int i3, int i4) throws JSONException {
        if (this.a != null) {
            this.a.getGlobalVisibleRect(new Rect());
            int iRound = Math.round(VpadnAdSize.convertPixelsToDp(r0.left, this));
            int iRound2 = Math.round(VpadnAdSize.convertPixelsToDp(r0.top, this));
            int iRound3 = Math.round(VpadnAdSize.convertPixelsToDp(r0.right - r0.left, this));
            int iRound4 = Math.round(VpadnAdSize.convertPixelsToDp(r0.bottom - r0.top, this));
            ab.a("VponActivity", "X1:" + iRound + " Y1:" + iRound2 + " wDip:" + iRound3 + " hDip:" + iRound4);
            try {
                this.G.put("x", iRound);
                this.G.put("y", iRound2);
                this.G.put("w", iRound3);
                this.G.put("h", iRound4);
                a("ad_pos_change", this.G);
            } catch (Exception e) {
                e.printStackTrace();
                ab.b("VponActivity", "onWebViewLayoutChanged throw exception");
            }
        }
    }

    @Override // vpadn.M
    public final void a(WebView webView, int i, String str, String str2) throws JSONException {
        if (this.f271c.equals("sdkOpenWebApp") || this.f271c.equals(FluctConstants.XML_NODE_INTERSTITIAL)) {
            new Handler().post(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.4
                @Override // java.lang.Runnable
                public final void run() {
                    if (VpadnActivity.this.b != null) {
                        if (VpadnActivity.this.u != null) {
                            VpadnActivity.this.b.removeView(VpadnActivity.this.u);
                            VpadnActivity.this.u = null;
                        }
                        VpadnActivity.this.b.setBackgroundColor(-1);
                        VpadnActivity.this.c(true);
                    }
                }
            });
        }
        if (this.n != null) {
            P pA = P.a();
            C0101o c0101o = (C0101o) pA.a(this.n);
            if (c0101o != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("e", "call onWebViewReceivedError");
                    c0101o.b(jSONObject);
                    pA.b(this.n);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // vpadn.E
    public final JSONObject g() throws JSONException {
        JSONObject jSONObjectA;
        Exception e;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObjectA = N.a().a(this, jSONObject);
        } catch (Exception e2) {
            jSONObjectA = jSONObject;
            e = e2;
        }
        try {
            jSONObjectA.put("sid", this.H);
            jSONObjectA.put("seq", this.I);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return jSONObjectA;
        }
        return jSONObjectA;
    }

    @Override // vpadn.E
    public final void a(boolean z) {
        this.e = z;
        c(false);
    }

    @Override // vpadn.E
    public final void h() {
    }

    @Override // vpadn.E
    public final void i() {
    }

    @Override // vpadn.E
    public final void a(long j) {
    }

    @Override // vpadn.E
    public final void j() {
    }

    @Override // vpadn.E
    public final void a(String str) {
    }

    @Override // vpadn.E
    public final void b(String str) {
    }

    @Override // vpadn.E
    public final void c(String str) {
    }

    @Override // vpadn.E
    public final String k() {
        return this.E.remove("url_type_click");
    }

    @Override // vpadn.E
    public final void a(String str, int i, C0101o c0101o) {
        try {
            if (this.K != null && str.startsWith("video_")) {
                if (this.K != null) {
                    this.K.a(str, i, c0101o);
                    return;
                }
                return;
            }
            if (!"onhide".equals(str) && !"onshow".equals(str) && !"ad_pos_change".equals(str)) {
                if (str.startsWith("video_")) {
                    ab.b("VponActivity", "VideoEventType add not supported! while mVideoManager is null. video event type: " + str);
                    c0101o.b(new JSONObject().put("e", "VideoEventType add not supported!"));
                    return;
                } else {
                    ab.b("VponActivity", "EventType add not supported! " + str);
                    c0101o.b(new JSONObject().put("e", "EventType add not supported!"));
                    return;
                }
            }
            Map<Integer, C0101o> map = this.D.get(str);
            if (map == null) {
                HashMap map2 = new HashMap();
                map2.put(Integer.valueOf(i), c0101o);
                this.D.put(str, map2);
            } else {
                map.put(Integer.valueOf(i), c0101o);
            }
            if ("ad_pos_change".equals(str)) {
                C0108v c0108v = new C0108v(C0108v.a.OK, this.G);
                ab.a("VponActivity", "Send ad_postion_change:" + this.G);
                c0108v.a(true);
                c0101o.a(c0108v);
                return;
            }
            if ("onshow".equals(str)) {
                if (this.F) {
                    C0108v c0108v2 = new C0108v(C0108v.a.OK);
                    c0108v2.a(true);
                    c0101o.a(c0108v2);
                    ab.a("VponActivity", "VponActivity IS SHOW!!");
                    return;
                }
                return;
            }
            if ("onhide".equals(str) && !this.F) {
                C0108v c0108v3 = new C0108v(C0108v.a.OK);
                c0108v3.a(true);
                c0101o.a(c0108v3);
                ab.a("VponActivity", "VponActivity IS HIDE!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                c0101o.b(new JSONObject().put("e", "addEventListener throw Exception:" + e.getMessage()));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }

    @Override // vpadn.E
    public final void b(String str, int i, C0101o c0101o) {
        try {
            if (this.K != null && str.startsWith("video_")) {
                this.K.b(str, i, c0101o);
                return;
            }
            if (!"onhide".equals(str) && !"onshow".equals(str) && !"ad_pos_change".equals(str)) {
                if (str.startsWith("video_")) {
                    ab.b("VponActivity", "VideoEventType remove not supported! while mVideoManager is null. video event type: " + str);
                    c0101o.b(new JSONObject().put("e", "VideoEventType remove not supported!"));
                    return;
                } else {
                    ab.b("VponActivity", "EventType remove not supported! " + str);
                    c0101o.b(new JSONObject().put("e", "EventType remove not supported!"));
                    return;
                }
            }
            if (this.D.containsKey(str)) {
                Map<Integer, C0101o> map = this.D.get(str);
                map.remove(Integer.valueOf(i));
                if (map.size() == 0) {
                    this.D.remove(str);
                }
                c0101o.b();
            }
            ab.b("VponActivity", "Cannot find event type in event listenerMap to remove! " + str);
            c0101o.b(new JSONObject().put("e", "Cannot find event type in event listenerMap to remove!"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                c0101o.b(new JSONObject().put("e", "removeEventListener throw Exception:" + e.getMessage()));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }

    private void a(String str, JSONObject jSONObject) {
        C0108v c0108v;
        if (jSONObject != null) {
            ab.a("VponActivity", "VponActivity TriggerEvent eventType:" + str + " retObj:" + jSONObject.toString());
        } else {
            ab.a("VponActivity", "VponActivity TriggerEvent eventType:" + str);
        }
        if (this.D.get(str) != null) {
            Iterator<C0101o> it = this.D.get(str).values().iterator();
            if (jSONObject != null) {
                c0108v = new C0108v(C0108v.a.OK, jSONObject);
            } else {
                c0108v = new C0108v(C0108v.a.OK);
            }
            c0108v.a(true);
            while (it.hasNext()) {
                it.next().a(c0108v);
            }
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        View focusedChild = null;
        if (this.a != null) {
            focusedChild = this.a.getFocusedChild();
        }
        return (this.a == null || (!this.a.g() && focusedChild == null) || i != 4) ? super.onKeyUp(i, keyEvent) : this.a.onKeyUp(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        View focusedChild = null;
        if (this.a != null) {
            focusedChild = this.a.getFocusedChild();
        }
        return (focusedChild == null || i != 4) ? super.onKeyDown(i, keyEvent) : this.a.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    protected void onPause() {
        ab.a("VponActivity", "VponActivity onPause()");
        super.onPause();
        if (this.K != null && this.f271c != null && this.f271c.equals("playVideoWithNativePlayer")) {
            this.K.e();
        }
        if (this.a != null) {
            this.a.b(true);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        ab.a("VponActivity", "VponActivity onResume()");
        super.onResume();
        if (this.K != null && this.f271c != null && this.f271c.equals("playVideoWithNativePlayer")) {
            this.K.f();
        }
        if (this.a != null) {
            this.a.a(false, false);
        }
    }

    @Override // vpadn.E
    public final void l() {
        Object objA;
        P pA = P.a();
        if (this.m != null && (objA = pA.a(this.m)) != null && (objA instanceof E)) {
            ((E) objA).l();
        }
    }

    @Override // vpadn.M
    public final void m() {
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, ar arVar) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("adType", "playVideoWithNativePlayer");
            bundle.putInt("originalRequestedOrientation", this.h);
            bundle.putInt("beforeActivityOrientation", this.i);
            P pA = P.a();
            if (arVar.v()) {
                bundle.putString("getControllerKey", this.m);
                this.N = false;
            } else {
                String string = UUID.randomUUID().toString();
                bundle.putString("getControllerKey", string);
                pA.a(string, this);
            }
            String string2 = UUID.randomUUID().toString();
            bundle.putString("getCallbackContextKey", string2);
            pA.a(string2, c0101o);
            String string3 = UUID.randomUUID().toString();
            bundle.putString("getVideoDataKey", string3);
            pA.a(string3, arVar);
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            bundle.putInt("statusBarHeight", rect.top);
            String str = NetworkManager.TYPE_NONE;
            if (arVar.j()) {
                if (arVar.i()) {
                    str = "landscape";
                } else {
                    str = "portrait";
                }
            }
            bundle.putString("forceOrientation", str);
            bundle.putString("click_url", this.E.get("url_type_click"));
            bundle.putLong("session_id", this.H);
            bundle.putLong("sequence_number", this.I);
            Intent intent = new Intent(this, (Class<?>) VpadnActivity.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setFlags(268435456);
            bundle.putBoolean("isFullScreen", (getWindow().getAttributes().flags & 1024) != 0);
            intent.putExtras(bundle);
            startActivity(intent);
            if (arVar.v()) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ab.b("VponActivity", "playVideoOnNativePlayer throw Exception:" + e.getMessage());
        }
    }

    private void s() {
        if (this.n != null) {
            P pA = P.a();
            C0101o c0101o = (C0101o) pA.a(this.n);
            if (c0101o != null) {
                c0101o.b();
                pA.b(this.n);
            }
        }
    }

    private void f(String str) throws JSONException {
        if (this.n != null) {
            P pA = P.a();
            C0101o c0101o = (C0101o) pA.a(this.n);
            if (c0101o != null) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("e", str);
                } catch (JSONException e) {
                }
                c0101o.b(jSONObject);
                pA.b(this.n);
            }
        }
    }

    public final RelativeLayout n() {
        return this.b;
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) throws JSONException, IOException {
        super.onConfigurationChanged(configuration);
        if (this.j.equals(NetworkManager.TYPE_NONE)) {
            if (this.f271c.equals("sdkOpenWebApp")) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.vpadn.widget.VpadnActivity.5
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            VpadnActivity.this.o();
                        } catch (Exception e) {
                        }
                    }
                }, 500L);
            }
            if (configuration.orientation == 2) {
                ab.a("VponActivity", "VponActivity orientation change to ORIENTATION_LANDSCAPE");
                if (this.K != null) {
                    if (this.K.j().a(ar.b.LANDSCAPE) && this.a != null) {
                        this.a.setVisibility(0);
                    }
                    this.K.a(configuration.orientation);
                    return;
                }
                return;
            }
            if (configuration.orientation == 1) {
                ab.a("VponActivity", "VponActivity orientation change to ORIENTATION_PORTRAIT");
                if (this.K != null) {
                    if (this.K != null && this.a != null) {
                        ar arVarJ = this.K.j();
                        if (arVarJ != null && arVarJ.a(ar.b.PORTRAIT)) {
                            if (this.a.getVisibility() == 4) {
                                this.a.setVisibility(0);
                            }
                            if (arVarJ.d().equals(ar.b)) {
                                this.a.setLayoutParams(u());
                            } else if (arVarJ.d().equals(ar.a)) {
                                this.a.setLayoutParams(t());
                            }
                        } else if (this.a.getVisibility() == 0) {
                            this.a.setVisibility(4);
                        }
                    }
                    this.K.a(configuration.orientation);
                }
            }
        }
    }

    public final void a(String str, String str2, boolean z, boolean z2, String str3, int i, boolean z3, boolean z4, boolean z5) {
        try {
            ab.a("VponActivity", "===========>>Enter videoActivityTo2PartActivity");
            Bundle bundle = new Bundle();
            bundle.putString("adType", "sdkOpenWebApp");
            bundle.putString("url", str);
            bundle.putBoolean("isUseCustomClose", z);
            String string = UUID.randomUUID().toString();
            bundle.putString("getControllerKey", string);
            P.a().a(string, this);
            int requestedOrientation = getRequestedOrientation();
            int i2 = Resources.getSystem().getConfiguration().orientation;
            bundle.putInt("originalRequestedOrientation", requestedOrientation);
            bundle.putInt("beforeActivityOrientation", i2);
            bundle.putString("forceOrientation", str3);
            bundle.putBoolean("isAllowOrientationChange", z2);
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            bundle.putInt("statusBarHeight", rect.top);
            if (str2 != null) {
                bundle.putString("html", str2);
            }
            bundle.putInt("backgroundColor", i);
            bundle.putBoolean("isShowProgressBar", z3);
            bundle.putBoolean("isShowNavigationBar", z4);
            bundle.putBoolean("isUseWebViewLoadUrl", z5);
            bundle.putString("click_url", this.E.get("url_type_click"));
            bundle.putLong("session_id", this.H);
            bundle.putLong("sequence_number", this.I);
            Intent intent = new Intent(this, (Class<?>) VpadnActivity.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setFlags(268435456);
            bundle.putBoolean("isFullScreen", (getWindow().getAttributes().flags & 1024) != 0);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {
            ab.a("VponActivity", "videoActivityTo2PartActivity throw Exception:" + e.getMessage(), e);
        }
    }

    @Override // vpadn.at
    public final void a(ar arVar, ar.b bVar) {
        String strU = arVar.u();
        if (!C0086a.c(strU)) {
            if (arVar.a(bVar)) {
                if (bVar.equals(ar.b.PORTRAIT)) {
                    a(arVar.d(), strU);
                } else {
                    h(strU);
                }
            }
            if (this.a == null) {
                if (bVar.equals(ar.b.LANDSCAPE)) {
                    if (arVar.a(ar.b.PORTRAIT)) {
                        a(arVar.d(), strU);
                        this.a.setVisibility(4);
                        return;
                    }
                    return;
                }
                if (arVar.a(ar.b.LANDSCAPE)) {
                    h(strU);
                    this.a.setVisibility(4);
                }
            }
        }
    }

    private void a(String str, String str2) {
        if (str.equals(ar.d)) {
            h(str2);
            return;
        }
        if (str.equals(ar.a)) {
            g(str2);
            this.b.addView(this.a, t());
        } else if (str.equals(ar.b)) {
            g(str2);
            this.b.addView(this.a, u());
        }
    }

    private void g(String str) {
        try {
            this.a = new VponAdWebView("videoWebView", this, this);
            this.a.setVerticalScrollBarEnabled(true);
            this.a.setBackgroundColor(16777215);
            this.a.loadUrl(str);
        } catch (Exception e) {
            ab.a("VponActivity", "createVideoWebViewAndLoadUrl throw Exception", e);
        }
    }

    private void h(String str) {
        g(str);
        this.b.addView(this.a, J);
    }

    private static RelativeLayout.LayoutParams t() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(10);
        layoutParams.addRule(2, 33333);
        return layoutParams;
    }

    private static RelativeLayout.LayoutParams u() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(12);
        layoutParams.addRule(3, 33333);
        return layoutParams;
    }

    @Override // vpadn.at
    public final void a(ar.b bVar) {
        if (bVar.equals(ar.b.LANDSCAPE) && this.a != null) {
            this.a.setLayoutParams(J);
        }
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, String str) {
        try {
            c0101o.b(new JSONObject().put("e", "Cannot call cacheVideoByUrl while interstitial .show mehtod had called"));
        } catch (JSONException e) {
        }
        ab.d("VponActivity", "Cannot call cacheVideoByUrl while interstitial .show mehtod had called");
    }

    @Override // vpadn.E
    public final void a(String str, JSONArray jSONArray, C0101o c0101o) throws IllegalStateException, JSONException {
        if (this.K != null) {
            this.K.a(str, jSONArray, c0101o);
        }
    }
}
