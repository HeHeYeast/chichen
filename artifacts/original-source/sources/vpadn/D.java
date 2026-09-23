package vpadn;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import c.NetworkManager;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.widget.VpadnActivity;
import com.vpon.webview.VponAdWebView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class D extends C implements K, L, M {
    private Timer A;
    private Timer B;
    private int C;
    private int D;
    private boolean E;
    private String F;
    private boolean G;
    private int H;
    private int I;
    private int J;
    private int K;
    private int L;
    private int M;
    private boolean N;
    private boolean O;
    private Bitmap P;
    private Canvas Q;
    private Rect R;
    private VponAdWebView o;
    private VponAdWebView p;
    private VponAdWebView q;
    private Q r;
    private Q s;
    private RelativeLayout t;
    private ImageView u;
    private boolean v;
    private J w;
    private boolean x;
    private Timer y;
    private int z;

    public D(Activity activity, J j) {
        super(activity);
        this.t = null;
        this.u = null;
        this.v = false;
        this.w = null;
        this.x = false;
        this.y = null;
        this.z = 0;
        this.B = null;
        this.C = -1;
        this.E = true;
        this.F = NetworkManager.TYPE_NONE;
        this.G = false;
        this.H = 0;
        this.I = 0;
        this.J = 0;
        this.K = 0;
        this.L = 0;
        this.M = 0;
        this.N = false;
        this.O = false;
        this.w = j;
        this.C = Resources.getSystem().getConfiguration().orientation;
    }

    public final VponAdWebView s() {
        return this.q;
    }

    @Override // vpadn.C
    protected final void n() {
        new Handler().post(new Runnable() { // from class: vpadn.D.1
            @Override // java.lang.Runnable
            public final void run() {
                ab.a("VponBannerController", "doLoadBanner");
                if (D.this.w != null) {
                    ab.a("VponBannerController", "Call mNotificationListener.onVponAdReceived();");
                    D.this.w.onVponAdReceived();
                }
                D.this.b(D.this.x);
            }
        });
    }

    @Override // vpadn.C
    protected final void a(final Object obj) {
        new Handler().post(new Runnable() { // from class: vpadn.D.9
            @Override // java.lang.Runnable
            public final void run() {
                ab.b("VponBannerController", "doLoadBannerFail");
                if (D.this.w != null) {
                    D.this.y();
                    D.this.O = false;
                    if (obj == null || !(obj instanceof String) || !obj.toString().equals("NO_FILL")) {
                        D.this.w.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                    } else {
                        D.this.w.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.NO_FILL);
                    }
                }
            }
        });
    }

    public final void t() {
        new Handler().post(new Runnable() { // from class: vpadn.D.10
            @Override // java.lang.Runnable
            public final void run() {
                D.f(D.this);
            }
        });
    }

    @Override // vpadn.E
    public final void a(boolean z) {
        this.v = z;
        if (this.G) {
            final boolean z2 = this.v;
            if (!this.G || this.t == null) {
                return;
            }
            new Handler().post(new Runnable() { // from class: vpadn.D.3
                @Override // java.lang.Runnable
                public final void run() {
                    if (D.this.t.getChildCount() >= 2 && z2) {
                        D.this.t.removeView(D.this.u);
                    }
                    if (D.this.t.getChildCount() == 1 && !z2) {
                        D.this.C();
                    }
                }
            });
        }
    }

    public final void a(C0101o c0101o, String str, String str2, boolean z, boolean z2, String str3, int i, boolean z3, boolean z4, boolean z5) {
        Location locationA;
        try {
            ab.c("VponBannerController", "===========>>Enter doOpenWebAppForSDKPlugIn");
            Bundle bundle = new Bundle();
            bundle.putString("adType", "sdkOpenWebApp");
            bundle.putString("url", str);
            bundle.putBoolean("isUseCustomClose", z);
            String string = UUID.randomUUID().toString();
            bundle.putString("getControllerKey", string);
            P pA = P.a();
            pA.a(string, this);
            String string2 = UUID.randomUUID().toString();
            bundle.putString("getCallbackContextKey", string2);
            pA.a(string2, c0101o);
            this.D = this.a.getRequestedOrientation();
            this.C = Resources.getSystem().getConfiguration().orientation;
            bundle.putInt("originalRequestedOrientation", this.D);
            bundle.putInt("beforeActivityOrientation", this.C);
            bundle.putString("forceOrientation", str3);
            bundle.putBoolean("isAllowOrientationChange", z2);
            Rect rect = new Rect();
            this.a.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            bundle.putInt("statusBarHeight", rect.top);
            if (this.s != null && (locationA = ad.a(this.a).a()) != null) {
                Q q = this.s;
                Q q2 = this.s;
                bundle.putInt("distance", ae.a(null, null, locationA.getLatitude(), locationA.getLongitude()));
            }
            if (str2 != null) {
                bundle.putString("html", str2);
            }
            bundle.putInt("backgroundColor", i);
            bundle.putBoolean("isShowProgressBar", z3);
            bundle.putBoolean("isShowNavigationBar", z4);
            bundle.putBoolean("isUseWebViewLoadUrl", z5);
            bundle.putString("click_url", this.g.get("url_type_click"));
            bundle.putLong("session_id", p());
            bundle.putLong("sequence_number", q());
            Intent intent = new Intent(this.a, (Class<?>) VpadnActivity.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setFlags(268435456);
            bundle.putBoolean("isFullScreen", (this.a.getWindow().getAttributes().flags & 1024) != 0);
            intent.putExtras(bundle);
            this.a.startActivity(intent);
            if (this.w != null) {
                this.w.onVponPresent();
            }
            if (this.x) {
                G();
                this.N = true;
            }
        } catch (Exception e) {
            ab.a("VponBannerController", "doOpenWebAppForSDKPlugIn throw Exception:" + e.getMessage(), e);
        }
    }

    @Override // vpadn.C, vpadn.E
    public final void h() {
        super.h();
        if (this.x && this.s != null && this.y == null && this.N) {
            this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.11
                @Override // java.lang.Runnable
                public final void run() {
                    if (!D.this.O) {
                        D.this.b(D.this.s.b);
                    }
                    D.this.N = false;
                }
            });
        }
    }

    public final void a(C0101o c0101o, boolean z, boolean z2, String str, int i) {
        ab.a("VponBannerController", "Call doExpandForSDKPlugin");
        if (this.q != null) {
            try {
                this.D = this.a.getRequestedOrientation();
                this.C = Resources.getSystem().getConfiguration().orientation;
                this.w.onPrepareExpandMode();
                this.F = str;
                this.E = z2;
                this.v = z;
                if (!this.F.equals(NetworkManager.TYPE_NONE)) {
                    if (this.F.equals("portrait")) {
                        this.a.setRequestedOrientation(1);
                    } else if (this.F.equals("landscape")) {
                        this.a.setRequestedOrientation(0);
                    }
                } else if (!this.E) {
                    if (this.C == 2) {
                        this.a.setRequestedOrientation(0);
                    } else if (this.C == 1) {
                        this.a.setRequestedOrientation(1);
                    }
                }
                this.t = new RelativeLayout(this.a);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                this.t.setBackgroundColor(0);
                this.q.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                this.q.setVponWebViewId("bannerWebViewExpanded");
                this.q.setBackgroundColor(i);
                this.q.requestFocus();
                this.t.addView(this.q, layoutParams);
                ViewGroup viewGroup = (ViewGroup) this.a.findViewById(android.R.id.content);
                if (viewGroup.getRootView() != null) {
                    Rect rect = new Rect();
                    this.a.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    this.t.setPadding(0, rect.top, 0, 0);
                    ((ViewGroup) viewGroup.getRootView()).addView(this.t);
                } else {
                    viewGroup.addView(this.t);
                }
                if (!this.v) {
                    C();
                }
                this.G = true;
                c0101o.b();
                if (this.w != null) {
                    this.w.onVponPresent();
                }
                if (this.x) {
                    G();
                    this.N = true;
                }
            } catch (Exception e) {
                ab.a("VponBannerController", "doExpandForSDKPlugin throw Exception:" + e.getMessage(), e);
                try {
                    c0101o.b(new JSONObject().put("e", "doExpandForSDKPlugin throw execption:" + e.getMessage()));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                this.q.setVponWebViewId("bannerWebView");
                this.G = false;
                j();
            }
        }
    }

    public final void a(C0101o c0101o, int i, int i2, int i3, int i4, String str) {
        TextView textView;
        RelativeLayout.LayoutParams layoutParams;
        int i5;
        int i6;
        ab.a("VponBannerController", "w:" + i + " h:" + i2 + " offX:" + i3 + " offY:" + i4);
        if (this.q != null) {
            try {
                this.D = this.a.getRequestedOrientation();
                this.C = Resources.getSystem().getConfiguration().orientation;
                Rect rect = new Rect();
                this.q.getGlobalVisibleRect(rect);
                Rect rect2 = new Rect();
                this.a.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect2);
                int i7 = rect2.top;
                int top = i7 + (this.a.getWindow().findViewById(android.R.id.content).getTop() - i7);
                int iConvertDpToPixel = (int) VpadnAdSize.convertDpToPixel(i, this.a);
                int iConvertDpToPixel2 = (int) VpadnAdSize.convertDpToPixel(i2, this.a);
                int iConvertDpToPixel3 = (int) VpadnAdSize.convertDpToPixel(i3, this.a);
                int iConvertDpToPixel4 = (int) VpadnAdSize.convertDpToPixel(i4, this.a);
                int i8 = rect.left + iConvertDpToPixel3;
                int i9 = (rect.top + iConvertDpToPixel4) - top;
                if (i8 < 0 || i9 < 0) {
                    ab.b("VponBannerController", "leftMarginPix < 0 || topMarginPix < 0 at doResizeForSDKPlugin. leftMarginPix:" + i8 + " topMarginPix:" + i9);
                    c0101o.b(new JSONObject().put("e", "leftMarginPix < 0 || topMarginPix < 0 at doResizeForSDKPlugin"));
                    return;
                }
                int i10 = -1;
                int i11 = -1;
                int iConvertDpToPixel5 = (int) VpadnAdSize.convertDpToPixel(50.0f, this.a);
                if (C0086a.c(str)) {
                    textView = null;
                    layoutParams = null;
                    i5 = -1;
                    i6 = -1;
                } else {
                    if (str.equals("top-right")) {
                        i10 = i8 + (iConvertDpToPixel - iConvertDpToPixel5);
                        i11 = i9;
                    } else if (str.equals("top-left")) {
                        i11 = i9;
                        i10 = i8;
                    } else if (str.equals("bottom-left")) {
                        i11 = (iConvertDpToPixel2 - iConvertDpToPixel5) + i9;
                        i10 = i8;
                    } else if (str.equals("bottom-right")) {
                        i10 = i8 + (iConvertDpToPixel - iConvertDpToPixel5);
                        i11 = (iConvertDpToPixel2 - iConvertDpToPixel5) + i9;
                    } else if (str.equals("top-center")) {
                        i10 = i8 + ((iConvertDpToPixel / 2) - (iConvertDpToPixel5 / 2));
                        i11 = i9;
                    } else if (str.equals("bottom-center")) {
                        i10 = i8 + ((iConvertDpToPixel / 2) - (iConvertDpToPixel5 / 2));
                        i11 = (iConvertDpToPixel2 - iConvertDpToPixel5) + i9;
                    } else if (str.equals("center")) {
                        i10 = i8 + ((iConvertDpToPixel / 2) - (iConvertDpToPixel5 / 2));
                        i11 = ((iConvertDpToPixel2 / 2) - (iConvertDpToPixel5 / 2)) + i9;
                    }
                    TextView textView2 = new TextView(this.a);
                    textView2.setWidth(iConvertDpToPixel5);
                    textView2.setHeight(iConvertDpToPixel5);
                    textView2.setBackgroundColor(0);
                    textView2.setOnClickListener(new View.OnClickListener() { // from class: vpadn.D.12
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            D.f(D.this);
                        }
                    });
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(iConvertDpToPixel5, iConvertDpToPixel5);
                    layoutParams2.leftMargin = i10;
                    layoutParams2.topMargin = i11;
                    textView = textView2;
                    layoutParams = layoutParams2;
                    i5 = i10;
                    i6 = i11;
                }
                ab.a("VponBannerController", " doResizeForSDKPlugin wPix:" + iConvertDpToPixel + " hPix:" + iConvertDpToPixel2 + " leftMarginPix:" + i8 + " topMarginPix:" + i9);
                this.w.onPrepareExpandMode();
                if (this.t != null) {
                    ((ViewGroup) this.a.findViewById(android.R.id.content)).removeView(this.t);
                    this.t.removeAllViews();
                    this.t = null;
                }
                this.t = new RelativeLayout(this.a);
                this.t.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(iConvertDpToPixel, iConvertDpToPixel2);
                layoutParams3.leftMargin = i8;
                layoutParams3.topMargin = i9;
                this.t.setBackgroundColor(0);
                this.q.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                this.q.setVponWebViewId("bannerWebViewResized");
                this.q.setBackgroundColor(0);
                this.q.bringToFront();
                this.q.requestFocus();
                this.t.addView(this.q, layoutParams3);
                if (textView != null && layoutParams != null && i5 >= 0 && i6 >= 0) {
                    this.t.addView(textView, layoutParams);
                }
                ((ViewGroup) this.a.findViewById(android.R.id.content)).addView(this.t);
                this.G = true;
                c0101o.b();
                if (this.w != null) {
                    this.w.onVponPresent();
                }
                if (this.x) {
                    G();
                    this.N = true;
                }
            } catch (Exception e) {
                ab.a("VponBannerController", "doResizeForSDKPlugin throw Exception:" + e.getMessage(), e);
                try {
                    c0101o.b(new JSONObject().put("e", "doResizeForSDKPlugin throw execption:" + e.getMessage()));
                } catch (JSONException e2) {
                    ab.a("VponBannerController", "doResizeForSDKPlugin throw Exception", e2);
                }
                this.q.setVponWebViewId("bannerWebView");
                this.G = false;
                j();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        if (this.t != null) {
            this.u = new ImageView(this.a);
            this.u.setVisibility(0);
            this.u.setBackgroundDrawable(new BitmapDrawable(getClass().getResourceAsStream("/vpon_video2_close.png")));
            this.u.setOnClickListener(new View.OnClickListener() { // from class: vpadn.D.13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    D.f(D.this);
                }
            });
            int i = (int) (C0086a.g(this.a).density * 50.0f);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i);
            layoutParams.rightMargin = i;
            this.t.addView(this.u, layoutParams);
        }
    }

    static /* synthetic */ void f(D d) {
        ab.a("VponBannerController", "ENTER doCloseExpand()");
        if (d.q != null) {
            d.q.b(true);
            d.q.a(false, false);
        } else {
            ab.b("VponBannerController", "mShowWebView is NULL in doCloseExpand");
        }
        if (d.t == null) {
            ab.b("VponBannerController", "mExpandRelativeLayout is null at doCloseExpand()");
            return;
        }
        if (d.G) {
            d.G = false;
            if (d.q != null) {
                d.q.setVponWebViewId("bannerWebView");
                d.q.setBackgroundColor(0);
            }
        }
        if (d.t != null) {
            ((ViewGroup) d.a.findViewById(android.R.id.content)).removeView(d.t);
            d.t.removeAllViews();
            d.t = null;
        }
        d.a.setRequestedOrientation(d.D);
        d.w.onLeaveExpandMode();
        if (d.x && d.s != null && d.y == null && d.N) {
            if (!d.O) {
                d.b(d.s.b);
            }
            d.N = false;
        }
        d.j();
    }

    @Override // vpadn.C
    public final void d(String str) {
        this.b = str;
    }

    public final void b(boolean z) {
        this.x = z;
        if (C0086a.c(this.b)) {
            ab.b("VponBannerController", "Invalid Banner ID!!");
        }
        if (this.a != null) {
            if (!ae.d(this.a)) {
                ab.b("VponBannerController", "permission-checking is failed!!");
                return;
            }
            String str = this.g.get("url_type_banner");
            if (str != null) {
                StringBuilder sb = new StringBuilder("endRequestToServer mRequestCount:");
                int i = this.K + 1;
                this.K = i;
                ab.a("VponBannerController", sb.append(i).append(" mLoadInitHtmlTimeOutCount:").append(this.M).toString());
                ae.a();
                try {
                    new I(str, this, this.e).execute(new Object[0]);
                    return;
                } catch (Exception e) {
                    ab.a("VponBannerController", "sendRequestToServer throw Exception", e);
                    return;
                }
            }
            ab.b("VponBannerController", "mUrlMap.get(VponControllerInterface.URL_TYPE_BANNER) return null");
        }
    }

    public final void a(VpadnAdRequest vpadnAdRequest) throws JSONException {
        try {
            ab.a("VponBannerController", "---->enter loadInitHtmlTemplate");
            final int i = this.L;
            this.f = false;
            this.O = true;
            this.m = vpadnAdRequest;
            this.x = vpadnAdRequest.isAutoRefresh();
            H();
            TimerTask timerTask = new TimerTask() { // from class: vpadn.D.14
                @Override // java.util.TimerTask, java.lang.Runnable
                public final void run() {
                    if (i == D.this.L) {
                        ab.b("VponBannerController", "TIMEOUT FOR LOAD INIT HTML TO BANNER");
                        D.this.M++;
                        D.this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.14.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                D.this.L();
                            }
                        });
                        D.this.b(1L);
                    }
                }
            };
            I();
            this.z = 0;
            this.H = 0;
            if (!r()) {
                ab.b("VponBannerController", "Device is not on-line");
                if (this.w != null) {
                    this.w.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.NETWORK_ERROR);
                }
                if (this.x) {
                    this.B = new Timer();
                    this.B.schedule(timerTask, 60000L);
                    return;
                }
                return;
            }
            if (this.x) {
                this.B = new Timer();
                try {
                    this.B.schedule(timerTask, 60000L);
                } catch (Exception e) {
                    ab.a("VponBannerController", "mCheckLoadInitHtmlToShowBannerTimer.schedule throw Exception:", e);
                }
            }
            if (!((PowerManager) this.a.getSystemService("power")).isScreenOn()) {
                ab.d("VponBannerController", "ScreenOff");
                if (this.x) {
                    b(10L);
                }
                if (this.w != null) {
                    this.w.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                    return;
                }
                return;
            }
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObjectB = N.a().b(this.a, (JSONObject) null);
            jSONObject.put("pf", this.d);
            jSONObjectB.put("pf", this.d);
            o();
            JSONObject jSONObjectA = a(jSONObjectB, vpadnAdRequest, this.j, this.k);
            jSONObjectA.put("build", "41504102");
            String strReplaceAll = "<!doctype html> <html> <head> <meta charset='utf-8'/>\n<script type='text/javascript' charset='utf-8' src='http://m.vpon.com/sdk/vpadn-sdk-core-v1.js'></script>\n<script type='text/javascript' charset='utf-8'>\nVPSDK_LoadSdkConstants( JSON_REPLACE1 );\nVPSDK_BuildAdReqUrl( JSON_REPLACE2 );\n</script><body></body></html>".replaceAll("JSON_REPLACE1", jSONObject.toString(4)).replaceAll("JSON_REPLACE2", jSONObjectA.toString(4));
            ab.a("VponBannerController", strReplaceAll);
            J();
            this.p = new VponAdWebView("init", this.a, this, this);
            this.p.loadDataWithBaseURL("file:///android_asset/www/vpadn", strReplaceAll, "text/html", "utf-8", null);
        } catch (Exception e2) {
            ab.a("VponBannerController", "loadInitHtmlTemplate throw Exception: " + e2.getMessage(), e2);
        }
    }

    private String D() {
        String str;
        try {
            int iIntValue = ((Integer) N.a().a((JSONObject) null, this.a).get("u_w")).intValue();
            double dIntValue = ((Integer) r2.get("u_h")).intValue() * 0.125d;
            boolean z = false;
            if (this.f308c.getMixModeCustomStr().equals(VpadnAdSize.MIX_CUSTOME_HEIGHT)) {
                z = true;
            }
            if (z) {
                this.J = this.f308c.getHeightInPixels(this.a);
                if (iIntValue >= 728 && dIntValue >= 90.0d) {
                    this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                    str = "728x90_mb";
                } else if (iIntValue >= 468 && dIntValue >= 60.0d) {
                    this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                    str = "468x60_mb";
                } else {
                    this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                    str = "320x50_mb";
                }
            } else {
                this.I = this.f308c.getWidthInPixels(this.a);
                if (iIntValue >= 728 && dIntValue >= 90.0d) {
                    this.J = (int) VpadnAdSize.convertDpToPixel(90.0f, this.a);
                    str = "728x90_mb";
                } else if (iIntValue >= 468 && dIntValue >= 60.0d) {
                    this.J = (int) VpadnAdSize.convertDpToPixel(60.0f, this.a);
                    str = "468x60_mb";
                } else {
                    this.J = (int) VpadnAdSize.convertDpToPixel(50.0f, this.a);
                    str = "320x50_mb";
                }
            }
            return str;
        } catch (Exception e) {
            ab.a("VponBannerController", "getMixAdSizeFormatAndSetWebViewSize throw Exception", e);
            return null;
        }
    }

    private String E() {
        String str;
        try {
            JSONObject jSONObjectA = N.a().a((JSONObject) null, this.a);
            int iIntValue = ((Integer) jSONObjectA.get("u_w")).intValue();
            int iIntValue2 = ((Integer) jSONObjectA.get("u_h")).intValue();
            ab.c("VponBannerController", "unitWidthDip:" + iIntValue + " unitHeightDip:" + iIntValue2);
            double d = iIntValue2 * 0.125d;
            if (iIntValue >= 728 && d >= 90.0d) {
                this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                this.J = (int) VpadnAdSize.convertDpToPixel(90.0f, this.a);
                str = "728x90_mb";
            } else if (iIntValue >= 468 && d >= 60.0d) {
                this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                this.J = (int) VpadnAdSize.convertDpToPixel(60.0f, this.a);
                str = "468x60_mb";
            } else if (iIntValue >= 320 && d >= 50.0d) {
                this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                this.J = (int) VpadnAdSize.convertDpToPixel(50.0f, this.a);
                str = "320x50_mb";
            } else {
                this.I = (int) VpadnAdSize.convertDpToPixel(iIntValue, this.a);
                this.J = (int) VpadnAdSize.convertDpToPixel(32.0f, this.a);
                str = "480x32_mb";
            }
            return str;
        } catch (Exception e) {
            ab.a("VponBannerController", "getSmartBannerAdSizeFormatAndSetWebViewSize throws Exception", e);
            return null;
        }
    }

    private JSONObject a(JSONObject jSONObject, VpadnAdRequest vpadnAdRequest, long j, long j2) throws JSONException {
        try {
            jSONObject.put("sid", j);
            jSONObject.put("seq", j2);
            if (this.f308c.equals(VpadnAdSize.SMART_BANNER)) {
                jSONObject.put("format", E());
            } else if (this.f308c.isMix()) {
                jSONObject.put("format", D());
            } else if (this.f308c.isCustomAdSize()) {
                this.I = this.f308c.getWidthInPixels(this.a);
                this.J = this.f308c.getHeightInPixels(this.a);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("w", this.f308c.getWidth());
                jSONObject2.put("h", this.f308c.getHeight());
                jSONObject.put("ad_frame", jSONObject2);
            } else {
                this.I = this.f308c.getWidthInPixels(this.a);
                this.J = this.f308c.getHeightInPixels(this.a);
                jSONObject.put("format", this.f308c.getWidth() + "x" + this.f308c.getHeight() + "_mb");
            }
            jSONObject.put("bid", this.b);
            if (vpadnAdRequest.isTestDevice(this.a)) {
                jSONObject.put("adtest", 1);
            } else {
                jSONObject.put("adtest", 0);
            }
            if (this.i.length() >= 4) {
                if (this.i.has("x")) {
                    jSONObject.put("ad_x", this.i.getInt("x"));
                }
                if (this.i.has("y")) {
                    jSONObject.put("ad_y", this.i.getInt("y"));
                }
                if (this.i.has("w")) {
                    jSONObject.put("ad_w", this.i.getInt("w"));
                }
                if (this.i.has("h")) {
                    jSONObject.put("ad_h", this.i.getInt("h"));
                }
            } else {
                jSONObject.put("ad_x", 0);
                jSONObject.put("ad_y", 0);
                jSONObject.put("ad_w", 0);
                jSONObject.put("ad_h", 0);
            }
            if (this.h) {
                jSONObject.put("ad_v", 1);
            } else {
                jSONObject.put("ad_v", 0);
            }
            Set<String> keywords = vpadnAdRequest.getKeywords();
            JSONArray jSONArray = new JSONArray();
            Iterator<String> it = keywords.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next());
            }
            jSONObject.put("kw", jSONArray);
            JSONObject jSONObjectC = N.a().c(this.a, (JSONObject) null);
            int age = vpadnAdRequest.getAge();
            if (age > 0 && age < 150) {
                jSONObjectC.put("age", age);
            }
            if (!vpadnAdRequest.getGender().equals(VpadnAdRequest.Gender.UNKNOWN)) {
                if (vpadnAdRequest.getGender().equals(VpadnAdRequest.Gender.MALE)) {
                    jSONObjectC.put("gender", 0);
                } else {
                    jSONObjectC.put("gender", 1);
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD");
            Date birthday = vpadnAdRequest.getBirthday();
            if (birthday != null) {
                jSONObjectC.put("bday", simpleDateFormat.format(birthday));
            }
            String string = jSONObjectC.toString();
            ab.a("VponBannerController", "secretJsonObjStr:" + string);
            jSONObject.put("ms", C0086a.a("NH/mLeyCBfokzYKUPNGEEg==", string));
        } catch (Exception e) {
            ab.a("VponBannerController", "collectPushlierParams throw Exception", e);
        }
        return jSONObject;
    }

    public final void a(Location location) {
        if (location == null || this.q == null || this.f || this.s == null) {
            return;
        }
        Q q = this.s;
    }

    public final void u() {
        this.n = true;
        if (this.w != null) {
            this.w.onVponPresent();
        }
    }

    @Override // vpadn.E
    public final void j() {
        if (this.n) {
            this.n = false;
        }
        if (this.w != null) {
            this.w.onVponDismiss();
        }
    }

    public final void v() {
        if (this.w != null) {
            this.w.onVponLeaveApplication();
        }
    }

    @Override // vpadn.M
    public final void c() {
        this.h = true;
        a("onshow", (JSONObject) null);
        if (this.n) {
            this.n = false;
            j();
        }
    }

    @Override // vpadn.M
    public final void d() {
        this.h = false;
        a("onhide", (JSONObject) null);
    }

    @Override // vpadn.M
    public final void f() {
        if (this.p != null && this.p.h().equals("init")) {
            this.p.setVponWebViewId("init-finish");
            ab.c("VponBannerController", "Load init html template finish");
            return;
        }
        ab.a("VponBannerController", "onWebViewLoadPageFinish -> showBanner");
        J();
        ab.a("VponBannerController", "enter showBanner");
        this.O = false;
        this.L++;
        if (!this.G) {
            K();
        }
        if (this.o == null || this.r == null) {
            if (this.o == null) {
                ab.b("VponBannerController", " mPrepareWebView == null at showBanner");
                return;
            } else {
                ab.b("VponBannerController", " mPrepareAd == null at showBanner");
                return;
            }
        }
        if (this.x) {
            b(this.r.b);
        }
        if (!this.G) {
            this.q = this.o;
            this.q.setBackgroundColor(0);
            this.s = this.r;
            this.w.onControllerWebViewReady(this.I, this.J);
            a(ad.a(this.a).a());
            S.a().c();
            F();
        }
    }

    @Override // vpadn.M
    public final void a(int i, int i2) {
        ab.c("VponBannerController", "Call onWebViewSizeChanged");
        if (this.q == null || this.a == null || this.v || !this.G || this.t == null || this.u == null || this.v) {
            return;
        }
        new Handler().post(new Runnable() { // from class: vpadn.D.2
            @Override // java.lang.Runnable
            public final void run() {
                if (D.this.t.getChildCount() >= 2) {
                    D.this.t.removeView(D.this.u);
                    D.this.C();
                }
            }
        });
    }

    @Override // vpadn.M
    public final void a(int i, int i2, int i3, int i4) throws JSONException {
        ab.a("VponBannerController", "onWebViewLayoutChanged Left:" + i + " top:" + i2);
        if (this.q != null) {
            ab.a("VponBannerController", "globalvisible Left:" + i + " top:" + i2);
            int iRound = Math.round(VpadnAdSize.convertPixelsToDp(i, this.a));
            int iRound2 = Math.round(VpadnAdSize.convertPixelsToDp(i2, this.a));
            int iRound3 = Math.round(VpadnAdSize.convertPixelsToDp(i3 - i, this.a));
            int iRound4 = Math.round(VpadnAdSize.convertPixelsToDp(i4 - i2, this.a));
            ab.c("VponBannerController", "onWebViewLayoutChanged: X1:" + iRound + " Y1:" + iRound2 + " wDip:" + iRound3 + " hDip:" + iRound4);
            try {
                this.i.put("x", iRound);
                this.i.put("y", iRound2);
                this.i.put("w", iRound3);
                this.i.put("h", iRound4);
                a("ad_pos_change", this.i);
            } catch (Exception e) {
                ab.a("VponBannerController", "onWebViewLayoutChanged throw exception", e);
            }
        }
    }

    @Override // vpadn.M
    public final void a(WebView webView, int i, String str, String str2) {
        VponAdWebView vponAdWebView;
        String str3 = "onWebViewReceivedError errorCode:" + i + " des:" + str + " failingUrl:" + str2;
        ab.b("VponBannerController", str3);
        S.a().b("[ERROR] " + str3, true);
        if (webView == null) {
            ab.b("VponBannerController", "webView is null in onWebViewReceivedError");
            vponAdWebView = null;
        } else {
            vponAdWebView = (VponAdWebView) webView;
            ab.b("VponBannerController", "vponWebView ID:" + vponAdWebView.h());
            S.a().b("[ERROR] vponWebView ID:" + vponAdWebView.h(), true);
        }
        if (vponAdWebView.h().equals("init")) {
            if (this.w != null) {
                this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.15
                    @Override // java.lang.Runnable
                    public final void run() {
                        D.this.w.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INVALID_REQUEST);
                    }
                });
                return;
            }
            return;
        }
        this.a.runOnUiThread(new Runnable(this) { // from class: vpadn.D.16
            @Override // java.lang.Runnable
            public final void run() {
                S.a().d();
            }
        });
    }

    @Override // vpadn.L
    public final void b(final Object obj) {
        this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.4
            @Override // java.lang.Runnable
            public final void run() {
                Q q = (Q) obj;
                if (D.this.l != -1) {
                    q.b = D.this.l;
                } else {
                    ab.b("VponBannerController", "Cannot get refresh time, set isFresh field to false");
                    D.this.x = false;
                }
                try {
                    D.a(D.this, q);
                    D d = D.this;
                } catch (Exception e) {
                    ab.a("VponBannerController", "prepareBanner throw Exception", e);
                }
            }
        });
    }

    @Override // vpadn.K
    public final void w() {
        ab.a("VponBannerController", "call onVponBannerImpression");
        if (this.s != null) {
            this.s.f315c = true;
        }
    }

    @Override // vpadn.K
    public final void a(G g) {
        ab.b("VponBannerController", "call onVponBannerImpressionFailed VponReturnCode:" + g.a());
        if (this.s != null) {
            this.s.f315c = true;
        }
    }

    static /* synthetic */ void a(D d, Q q) {
        ab.a("VponBannerController", "Enter perpareBanner");
        if (d.a == null || d.f) {
            if (d.a == null) {
                ab.b("VponBannerController", "prepareBanner mContext == null");
            }
            if (d.f) {
                ab.b("VponBannerController", "prepareBanner mIsDestroy == true");
                return;
            }
            return;
        }
        d.o = new VponAdWebView("bannerWebView", d.a, d, d);
        d.r = q;
        String str = q.a;
        ab.a("VponBannerController", "real get BannerHtml:" + str);
        ab.c("VponBannerController", "/////////////Banner OK!!//////////////////");
        d.b();
        String str2 = d.g.get("url_type_banner");
        int iLastIndexOf = str2.lastIndexOf(47);
        ab.a("VponBannerController", "baseUrl:" + (iLastIndexOf > 0 ? str2.substring(0, iLastIndexOf + 1) : String.valueOf(str2) + "/"));
        d.o.loadDataWithBaseURL(str2, str, "text/html", "utf-8", null);
    }

    static /* synthetic */ void n(D d) {
        if (d.a == null || d.s == null || d.s.f315c) {
            return;
        }
        final String strRemove = d.g.remove("url_type_impression");
        if (strRemove == null) {
            ab.b("VponBannerController", "Cannot get impression URL");
            return;
        }
        ab.a("VponBannerController", "Send impression to server impressionUrl:" + strRemove);
        try {
            new H(strRemove, d, d.e).execute(new Object[0]);
        } catch (Exception e) {
            ab.a("VponBannerController", "sendImpressionToServer throw Exception", e);
        }
        d.a.runOnUiThread(new Runnable(d) { // from class: vpadn.D.5
            @Override // java.lang.Runnable
            public final void run() {
                S.a().a(strRemove);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        int iIntValue = 1;
        if (P.a().c("viewable_duration")) {
            iIntValue = (((Integer) P.a().a("viewable_duration")).intValue() / 500) + 1;
        }
        if (!a((View) this.q)) {
            this.H++;
            if (this.H == iIntValue) {
                this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.6
                    @Override // java.lang.Runnable
                    public final void run() {
                        D.n(D.this);
                    }
                });
                this.z = 0;
                this.H = 0;
                I();
                return;
            }
            if (this.f) {
                ab.d("VponBannerController", "mIsDestroy == true NO need to create mCoveredCheckTimer");
                this.z = 0;
                this.H = 0;
                I();
                return;
            }
            if (this.A == null) {
                this.A = new Timer();
            }
            try {
                this.A.schedule(new a(this, (byte) 0), 500L);
                return;
            } catch (Exception e) {
                ab.a("VponBannerController", "mCoveredCheckTimer.schedule throw Exception:", e);
                return;
            }
        }
        ab.a("VponBannerController", "Cover test fail mTimesOfBlockViewCheck:" + this.z);
        if (this.z < 60) {
            this.z++;
            long j = 1000;
            if (this.z > 30) {
                j = 5000;
            }
            if (this.f) {
                ab.d("VponBannerController", "mIsDestroy == true NO need to create mCoveredCheckTimer");
                this.z = 0;
                this.H = 0;
                I();
                return;
            }
            if (this.A == null) {
                this.A = new Timer();
            }
            this.H = 0;
            try {
                this.A.schedule(new a(this, (byte) 0), j);
                return;
            } catch (Exception e2) {
                ab.a("VponBannerController", "mCoveredCheckTimer.schedule throw Exception:", e2);
                return;
            }
        }
        this.z = 0;
        this.H = 0;
    }

    class a extends TimerTask {
        private a() {
        }

        /* synthetic */ a(D d, byte b) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            if (D.this.s == null || !D.this.s.f315c) {
                int i = 1000;
                if (D.this.H > 0) {
                    i = 500;
                }
                long j = i;
                D.this.F();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(long j) {
        G();
        ab.a("VponBannerController", "refreshRequestTimerStart second:" + j);
        this.y = new Timer();
        try {
            this.y.schedule(new b(this, (byte) 0), 1000 * j);
        } catch (Exception e) {
            ab.a("VponBannerController", "refreshRequestTimerStart throw Exception:", e);
        }
    }

    class b extends TimerTask {
        private b() {
        }

        /* synthetic */ b(D d, byte b) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            if (!D.this.G) {
                D.this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.b.1
                    @Override // java.lang.Runnable
                    public final void run() throws JSONException {
                        D.this.a(D.this.m);
                    }
                });
            }
        }
    }

    public final void x() {
        try {
            H();
            G();
            I();
        } catch (Exception e) {
            ab.a("VponBannerController", "cancelTimer() throws Exception!", e);
        }
    }

    private synchronized void G() {
        if (this.y != null) {
            ab.a("VponBannerController", "cancelAutoRefreshTimer()");
            this.y.cancel();
            this.y.purge();
            this.y = null;
        }
    }

    private synchronized void H() {
        if (this.B != null) {
            ab.a("VponBannerController", "mCheckLoadInitHtmlToShowBannerTimer()");
            this.B.cancel();
            this.B.purge();
            this.B = null;
        }
    }

    private synchronized void I() {
        if (this.A != null) {
            ab.a("VponBannerController", "cancelCoveredCheckTimer()");
            this.A.cancel();
            this.A.purge();
            this.A = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x005d, code lost:
    
        r4 = r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean a(android.view.View r13) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: vpadn.D.a(android.view.View):boolean");
    }

    private static int a(View view, ViewGroup viewGroup) {
        int i = 0;
        while (i < viewGroup.getChildCount() && viewGroup.getChildAt(i) != view) {
            i++;
        }
        return i;
    }

    public final void y() {
        ab.c("VponBannerController", "call webViewHandleDestroy");
        this.f = true;
        if (this.q == this.o && this.q != null) {
            K();
        } else {
            L();
            K();
        }
        J();
    }

    private synchronized void J() {
        if (this.p != null) {
            ab.a("VponBannerController", "destroy mInitWebView");
            this.p.stopLoading();
            this.p.removeAllViews();
            this.p.e();
            this.p = null;
        }
    }

    private synchronized void K() {
        if (this.q != null) {
            ab.a("VponBannerController", "destroy mShowWebView");
            this.q.stopLoading();
            this.q.removeAllViews();
            this.q.e();
            this.q = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void L() {
        if (this.o != null) {
            ab.a("VponBannerController", "destroy mPrepareWebView");
            this.o.stopLoading();
            this.o.removeAllViews();
            this.o.e();
            this.o = null;
        }
    }

    public final boolean z() {
        return this.q != null;
    }

    @Override // vpadn.E
    public final void l() {
        v();
    }

    @Override // vpadn.M
    public final void m() {
        new Handler().post(new Runnable() { // from class: vpadn.D.7
            @Override // java.lang.Runnable
            public final void run() {
                D.f(D.this);
            }
        });
    }

    public final void A() {
        if (this.x) {
            G();
        }
    }

    public final void B() {
        this.a.runOnUiThread(new Runnable() { // from class: vpadn.D.8
            @Override // java.lang.Runnable
            public final void run() {
                if (D.this.x && !D.this.O && D.this.s != null && D.this.y == null) {
                    D.this.b(D.this.s.b);
                }
            }
        });
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, ar arVar) {
        Location locationA;
        try {
            ab.a("VponBannerController", "===========>>Enter playVideoOnNativePlayer");
            Bundle bundle = new Bundle();
            bundle.putString("adType", "playVideoWithNativePlayer");
            String string = UUID.randomUUID().toString();
            bundle.putString("getControllerKey", string);
            P pA = P.a();
            pA.a(string, this);
            String string2 = UUID.randomUUID().toString();
            bundle.putString("getCallbackContextKey", string2);
            pA.a(string2, c0101o);
            String string3 = UUID.randomUUID().toString();
            bundle.putString("getVideoDataKey", string3);
            pA.a(string3, arVar);
            this.D = this.a.getRequestedOrientation();
            this.C = Resources.getSystem().getConfiguration().orientation;
            bundle.putInt("originalRequestedOrientation", this.D);
            bundle.putInt("beforeActivityOrientation", this.C);
            Rect rect = new Rect();
            this.a.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            bundle.putInt("statusBarHeight", rect.top);
            if (this.s != null && (locationA = ad.a(this.a).a()) != null) {
                Q q = this.s;
                Q q2 = this.s;
                bundle.putInt("distance", ae.a(null, null, locationA.getLatitude(), locationA.getLongitude()));
            }
            String str = NetworkManager.TYPE_NONE;
            if (arVar.j()) {
                if (arVar.i()) {
                    str = "landscape";
                } else {
                    str = "portrait";
                }
            }
            bundle.putString("forceOrientation", str);
            bundle.putString("click_url", this.g.get("url_type_click"));
            bundle.putLong("session_id", p());
            bundle.putLong("sequence_number", q());
            Intent intent = new Intent(this.a, (Class<?>) VpadnActivity.class);
            intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            intent.setFlags(268435456);
            bundle.putBoolean("isFullScreen", (this.a.getWindow().getAttributes().flags & 1024) != 0);
            intent.putExtras(bundle);
            this.a.startActivity(intent);
            if (this.w != null) {
                this.w.onVponPresent();
            }
            if (this.x) {
                G();
                this.N = true;
            }
        } catch (Exception e) {
            ab.a("VponBannerController", "playVideoOnNativePlayer throw Exception:" + e.getMessage(), e);
        }
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, String str) {
        try {
            c0101o.b(new JSONObject().put("e", "Banner cannot call cacheVideoByUrl. "));
        } catch (JSONException e) {
        }
        ab.b("VponBannerController", "Banner cannot call cacheVideoByUrl. ");
    }

    @Override // vpadn.E
    public final void a(String str, JSONArray jSONArray, C0101o c0101o) {
        try {
            c0101o.b(new JSONObject().put("e", "Banner cannot call controlNativeVideoPlayer. "));
        } catch (JSONException e) {
        }
        ab.b("VponBannerController", "Banner cannot call controlNativeVideoPlayer. ");
    }

    public final void a(String str, String str2, String str3) {
        if (this.q != null) {
            ab.a("VponBannerController", "VponBannerController.testSendJsonToVponCordovaPlugin return string:" + this.q.a(str, str2, str3));
        } else {
            ab.b("VponBannerController", "Cannot call VponBannerController.testSendJsonToVponCordovaPlugin ");
        }
    }
}
