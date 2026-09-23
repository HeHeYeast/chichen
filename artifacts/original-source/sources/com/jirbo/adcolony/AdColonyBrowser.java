package com.jirbo.adcolony;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.lang.reflect.InvocationTargetException;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyBrowser extends Activity {
    public static String url;
    WebView b;

    /* renamed from: c, reason: collision with root package name */
    ADCImage f206c;
    ADCImage d;
    ADCImage e;
    ADCImage f;
    ADCImage g;
    ADCImage h;
    ADCImage i;
    ADCImage j;
    ADCImage k;
    RelativeLayout l;
    RelativeLayout m;
    boolean n = false;
    boolean o = false;
    boolean p = false;
    boolean q = false;
    ProgressBar r;
    DisplayMetrics s;
    a t;
    c u;
    static boolean a = true;
    static boolean v = false;
    static boolean w = false;
    static boolean x = false;
    static boolean y = false;
    static boolean z = true;
    static boolean A = false;
    static boolean B = false;
    static boolean C = false;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = true;
        this.f206c = new ADCImage(com.jirbo.adcolony.a.j("browser_back_image_normal"));
        this.d = new ADCImage(com.jirbo.adcolony.a.j("browser_stop_image_normal"));
        this.e = new ADCImage(com.jirbo.adcolony.a.j("browser_reload_image_normal"));
        this.f = new ADCImage(com.jirbo.adcolony.a.j("browser_forward_image_normal"));
        this.g = new ADCImage(com.jirbo.adcolony.a.j("browser_close_image_normal"));
        this.h = new ADCImage(com.jirbo.adcolony.a.j("browser_glow_button"));
        this.i = new ADCImage(com.jirbo.adcolony.a.j("browser_icon"));
        this.j = new ADCImage(com.jirbo.adcolony.a.j("browser_back_image_normal"), true);
        this.k = new ADCImage(com.jirbo.adcolony.a.j("browser_forward_image_normal"), true);
        this.s = AdColony.activity().getResources().getDisplayMetrics();
        float f = this.s.widthPixels / this.s.xdpi;
        float f2 = this.s.heightPixels / this.s.ydpi;
        double dSqrt = (Math.sqrt((this.s.widthPixels * this.s.widthPixels) + (this.s.heightPixels * this.s.heightPixels)) / Math.sqrt((f * f) + (f2 * f2))) / 220.0d;
        if (dSqrt > 1.8d) {
            dSqrt = 1.8d;
        }
        z = true;
        v = false;
        w = false;
        C = false;
        this.f206c.a(dSqrt);
        this.d.a(dSqrt);
        this.e.a(dSqrt);
        this.f.a(dSqrt);
        this.g.a(dSqrt);
        this.h.a(dSqrt);
        this.j.a(dSqrt);
        this.k.a(dSqrt);
        this.r = new ProgressBar(this);
        this.r.setVisibility(4);
        this.m = new RelativeLayout(this);
        this.l = new RelativeLayout(this);
        this.l.setBackgroundColor(-3355444);
        if (!com.jirbo.adcolony.a.m) {
            this.l.setLayoutParams(new RelativeLayout.LayoutParams(-1, (int) (this.f206c.g * 1.5d)));
        } else {
            this.l.setLayoutParams(new RelativeLayout.LayoutParams(-1, (int) (this.f206c.g * 1.5d)));
        }
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().requestFeature(2);
        setVolumeControlStream(3);
        this.b = new WebView(this);
        this.b.getSettings().setJavaScriptEnabled(true);
        this.b.getSettings().setBuiltInZoomControls(true);
        this.b.getSettings().setUseWideViewPort(true);
        this.b.getSettings().setLoadWithOverviewMode(true);
        this.b.getSettings().setGeolocationEnabled(true);
        if (a) {
            if (!com.jirbo.adcolony.a.m) {
                if (Build.VERSION.SDK_INT >= 10) {
                    setRequestedOrientation(6);
                } else {
                    setRequestedOrientation(0);
                }
            } else {
                setRequestedOrientation(com.jirbo.adcolony.a.w);
            }
        }
        a = true;
        this.b.setWebChromeClient(new WebChromeClient() { // from class: com.jirbo.adcolony.AdColonyBrowser.1
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView view, int progress) {
                AdColonyBrowser.this.setProgress(progress * 1000);
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        this.b.setWebViewClient(new WebViewClient() { // from class: com.jirbo.adcolony.AdColonyBrowser.2
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url2) {
                if (!url2.startsWith("market://") && !url2.startsWith("amzn://")) {
                    return false;
                }
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url2));
                if (com.jirbo.adcolony.a.K != null) {
                    com.jirbo.adcolony.a.K.startActivity(intent);
                }
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView view, String url2, Bitmap favicon) {
                if (!AdColonyBrowser.C) {
                    AdColonyBrowser.x = true;
                    AdColonyBrowser.y = false;
                    AdColonyBrowser.this.r.setVisibility(0);
                }
                AdColonyBrowser.this.t.invalidate();
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView view, int errorCode, String description, String failing_url) {
                l.d.a("Error viewing URL: ").b((Object) description);
                AdColonyBrowser.this.finish();
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url2) {
                if (!AdColonyBrowser.C) {
                    AdColonyBrowser.y = true;
                    AdColonyBrowser.x = false;
                    AdColonyBrowser.this.r.setVisibility(4);
                    AdColonyBrowser.v = AdColonyBrowser.this.b.canGoBack();
                    AdColonyBrowser.w = AdColonyBrowser.this.b.canGoForward();
                }
                AdColonyBrowser.this.t.invalidate();
            }
        });
        this.t = new a(this);
        this.u = new c(this);
        this.m.setBackgroundColor(16777215);
        this.m.addView(this.l);
        this.l.setId(12345);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, this.s.heightPixels - ((int) (this.g.g * 1.5d)));
        layoutParams.addRule(3, this.l.getId());
        this.m.addView(this.b, layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, 20);
        layoutParams2.addRule(3, this.l.getId());
        layoutParams2.setMargins(0, -10, 0, 0);
        this.m.addView(this.u, layoutParams2);
        int i = this.s.widthPixels > this.s.heightPixels ? this.s.widthPixels : this.s.heightPixels;
        this.m.addView(this.t, new RelativeLayout.LayoutParams(i * 2, i * 2));
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, this.s.heightPixels - ((int) (this.g.g * 1.5d)));
        layoutParams3.addRule(3, this.l.getId());
        this.m.addView(new b(this), layoutParams3);
        setContentView(this.m);
        this.b.loadUrl(url);
        l.f226c.a("Viewing ").b((Object) url);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean has_focus) {
        super.onWindowFocusChanged(has_focus);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.t.b();
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        z = true;
        this.t.invalidate();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration new_config) {
        super.onConfigurationChanged(new_config);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, this.s.heightPixels - ((int) (1.5d * this.g.g)));
        layoutParams.addRule(3, this.l.getId());
        this.b.setLayoutParams(layoutParams);
        z = true;
        this.t.invalidate();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        if (!com.jirbo.adcolony.a.u && A) {
            for (int i = 0; i < com.jirbo.adcolony.a.ad.size(); i++) {
                com.jirbo.adcolony.a.ad.get(i).recycle();
            }
            com.jirbo.adcolony.a.ad.clear();
        }
        A = false;
        B = false;
        super.onDestroy();
    }

    class b extends View {
        Rect a;

        public b(Activity activity) {
            super(activity);
            this.a = new Rect();
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            if (!AdColonyBrowser.y) {
                canvas.drawARGB(MotionEventCompat.ACTION_MASK, 0, 0, 0);
                getDrawingRect(this.a);
                AdColonyBrowser.this.i.a(canvas, (this.a.width() - AdColonyBrowser.this.i.f) / 2, (this.a.height() - AdColonyBrowser.this.i.g) / 2);
                invalidate();
            }
        }
    }

    class c extends View {
        Paint a;
        ADCImage b;

        /* renamed from: c, reason: collision with root package name */
        ADCImage f208c;

        public c(Activity activity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            super(activity);
            this.a = new Paint();
            this.b = new ADCImage(com.jirbo.adcolony.a.j("close_image_normal"));
            this.f208c = new ADCImage(com.jirbo.adcolony.a.j("close_image_down"));
            try {
                getClass().getMethod("setLayerType", Integer.TYPE, Paint.class).invoke(this, 1, null);
            } catch (Exception e) {
            }
            this.a.setColor(-3355444);
            this.a.setStrokeWidth(10.0f);
            this.a.setStyle(Paint.Style.STROKE);
            this.a.setShadowLayer(3.0f, BitmapDescriptorFactory.HUE_RED, 1.0f, FluctConstants.FRAME_ALPHA_COLOR);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, AdColonyBrowser.this.l.getWidth(), 10.0f, this.a);
        }
    }

    class a extends View {
        Rect a;
        Paint b;

        public a(Activity activity) {
            super(activity);
            this.a = new Rect();
            this.b = new Paint();
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            getDrawingRect(this.a);
            int height = (AdColonyBrowser.this.l.getHeight() - AdColonyBrowser.this.f206c.g) / 2;
            if (!AdColonyBrowser.v) {
                AdColonyBrowser.this.f206c.a(canvas, AdColonyBrowser.this.f206c.f, height);
            } else {
                AdColonyBrowser.this.j.a(canvas, AdColonyBrowser.this.f206c.f, height);
            }
            if (!AdColonyBrowser.w) {
                AdColonyBrowser.this.f.a(canvas, AdColonyBrowser.this.f206c.c() + (AdColonyBrowser.this.l.getWidth() / 10) + AdColonyBrowser.this.f206c.f, height);
            } else {
                AdColonyBrowser.this.k.a(canvas, AdColonyBrowser.this.f206c.c() + (AdColonyBrowser.this.l.getWidth() / 10) + AdColonyBrowser.this.f206c.f, height);
            }
            if (AdColonyBrowser.x) {
                AdColonyBrowser.this.d.a(canvas, AdColonyBrowser.this.f.c() + AdColonyBrowser.this.f.f + (AdColonyBrowser.this.l.getWidth() / 10), height);
            } else {
                AdColonyBrowser.this.e.a(canvas, AdColonyBrowser.this.f.c() + AdColonyBrowser.this.f.f + (AdColonyBrowser.this.l.getWidth() / 10), height);
            }
            AdColonyBrowser.this.g.a(canvas, AdColonyBrowser.this.l.getWidth() - (AdColonyBrowser.this.g.f * 2), height);
            if (AdColonyBrowser.this.n) {
                AdColonyBrowser.this.h.c((AdColonyBrowser.this.f206c.c() - (AdColonyBrowser.this.h.f / 2)) + (AdColonyBrowser.this.f206c.f / 2), (AdColonyBrowser.this.f206c.d() - (AdColonyBrowser.this.h.g / 2)) + (AdColonyBrowser.this.f206c.g / 2));
                AdColonyBrowser.this.h.a(canvas);
            }
            if (AdColonyBrowser.this.o) {
                AdColonyBrowser.this.h.c((AdColonyBrowser.this.f.c() - (AdColonyBrowser.this.h.f / 2)) + (AdColonyBrowser.this.f.f / 2), (AdColonyBrowser.this.f.d() - (AdColonyBrowser.this.h.g / 2)) + (AdColonyBrowser.this.f.g / 2));
                AdColonyBrowser.this.h.a(canvas);
            }
            if (AdColonyBrowser.this.p) {
                AdColonyBrowser.this.h.c((AdColonyBrowser.this.e.c() - (AdColonyBrowser.this.h.f / 2)) + (AdColonyBrowser.this.e.f / 2), (AdColonyBrowser.this.e.d() - (AdColonyBrowser.this.h.g / 2)) + (AdColonyBrowser.this.e.g / 2));
                AdColonyBrowser.this.h.a(canvas);
            }
            if (AdColonyBrowser.this.q) {
                AdColonyBrowser.this.h.c((AdColonyBrowser.this.g.c() - (AdColonyBrowser.this.h.f / 2)) + (AdColonyBrowser.this.g.f / 2), (AdColonyBrowser.this.g.d() - (AdColonyBrowser.this.h.g / 2)) + (AdColonyBrowser.this.g.g / 2));
                AdColonyBrowser.this.h.a(canvas);
            }
            a();
        }

        public void a() {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AdColonyBrowser.this.r.getWidth(), AdColonyBrowser.this.r.getHeight());
            layoutParams.topMargin = (AdColonyBrowser.this.l.getHeight() - AdColonyBrowser.this.d.g) / 2;
            layoutParams.leftMargin = (AdColonyBrowser.this.l.getWidth() / 10) + AdColonyBrowser.this.d.c() + AdColonyBrowser.this.d.f;
            if (AdColonyBrowser.z && AdColonyBrowser.this.d.c() != 0) {
                AdColonyBrowser.this.m.removeView(AdColonyBrowser.this.r);
                AdColonyBrowser.this.m.addView(AdColonyBrowser.this.r, layoutParams);
                AdColonyBrowser.z = false;
            }
            if (AdColonyBrowser.this.r.getLayoutParams() != null) {
                AdColonyBrowser.this.r.getLayoutParams().height = AdColonyBrowser.this.d.g;
                AdColonyBrowser.this.r.getLayoutParams().width = AdColonyBrowser.this.d.f;
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 0) {
                if (a(AdColonyBrowser.this.f206c, x, y) && AdColonyBrowser.v) {
                    AdColonyBrowser.this.n = true;
                    invalidate();
                    return true;
                }
                if (a(AdColonyBrowser.this.f, x, y) && AdColonyBrowser.w) {
                    AdColonyBrowser.this.o = true;
                    invalidate();
                    return true;
                }
                if (a(AdColonyBrowser.this.e, x, y)) {
                    AdColonyBrowser.this.p = true;
                    invalidate();
                    return true;
                }
                if (a(AdColonyBrowser.this.g, x, y)) {
                    AdColonyBrowser.this.q = true;
                    invalidate();
                    return true;
                }
            }
            if (action == 1) {
                if (a(AdColonyBrowser.this.f206c, x, y) && AdColonyBrowser.v) {
                    AdColonyBrowser.this.b.goBack();
                    b();
                    return true;
                }
                if (a(AdColonyBrowser.this.f, x, y) && AdColonyBrowser.w) {
                    AdColonyBrowser.this.b.goForward();
                    b();
                    return true;
                }
                if (a(AdColonyBrowser.this.e, x, y) && AdColonyBrowser.x) {
                    AdColonyBrowser.this.b.stopLoading();
                    b();
                    return true;
                }
                if (a(AdColonyBrowser.this.e, x, y) && !AdColonyBrowser.x) {
                    AdColonyBrowser.this.b.reload();
                    b();
                    return true;
                }
                if (a(AdColonyBrowser.this.g, x, y)) {
                    AdColonyBrowser.C = true;
                    AdColonyBrowser.this.b.loadData("", "text/html", "utf-8");
                    AdColonyBrowser.w = false;
                    AdColonyBrowser.v = false;
                    AdColonyBrowser.x = false;
                    b();
                    AdColonyBrowser.this.finish();
                    return true;
                }
                b();
            }
            return false;
        }

        public void b() {
            AdColonyBrowser.this.n = false;
            AdColonyBrowser.this.o = false;
            AdColonyBrowser.this.p = false;
            AdColonyBrowser.this.q = false;
            invalidate();
        }

        public boolean a(ADCImage aDCImage, int i, int i2) {
            return i < (aDCImage.c() + aDCImage.f) + 16 && i > aDCImage.c() + (-16) && i2 < (aDCImage.d() + aDCImage.g) + 16 && i2 > aDCImage.d() + (-16);
        }
    }
}
