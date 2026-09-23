package vpadn;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import c.NetworkManager;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.widget.VpadnActivity;
import com.vpon.webview.VponAdWebView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.ax;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class F extends C implements K, L, M, ax.a {
    private boolean A;
    private boolean B;
    private az C;
    private ax D;
    private boolean E;
    private VponAdWebView o;
    private VponAdWebView p;
    private Q q;
    private RelativeLayout r;
    private ImageView s;
    private boolean t;
    private J u;
    private int v;
    private int w;
    private boolean x;
    private String y;
    private String z;

    public F(Activity activity, J j) {
        super(activity);
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = null;
        this.v = -1;
        this.x = true;
        this.y = NetworkManager.TYPE_NONE;
        this.z = null;
        this.A = false;
        this.B = false;
        this.E = false;
        this.u = j;
        this.v = Resources.getSystem().getConfiguration().orientation;
    }

    @Override // vpadn.E
    public final void a(boolean z) {
    }

    public final void s() {
        this.E = true;
    }

    @Override // vpadn.K
    public final void w() {
        ab.a("VponInterstitialAdController", "call onVponBannerImpression SUCCESS");
        this.q.f315c = true;
    }

    @Override // vpadn.K
    public final void a(G g) {
        ab.b("VponInterstitialAdController", "call onVponBannerImpression Failed");
        this.q.f315c = true;
    }

    @Override // vpadn.L
    public final void b(final Object obj) {
        this.a.runOnUiThread(new Runnable() { // from class: vpadn.F.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    S.a().c();
                    F.a(F.this, (Q) obj);
                } catch (Exception e) {
                    ab.a("VponInterstitialAdController", "interstitial onVponBannerRequestReceived throw Exception", e);
                }
            }
        });
    }

    @Override // vpadn.M
    public final void c() {
        if (!this.h) {
            this.h = true;
            a("onshow", (JSONObject) null);
        }
    }

    @Override // vpadn.M
    public final void d() {
        if (this.h) {
            this.h = false;
            a("onhide", (JSONObject) null);
        }
    }

    @Override // vpadn.M
    public final void f() {
        if (this.o != null && this.o.h().equals("init")) {
            this.o.setVponWebViewId("init-finish");
            ab.c("VponInterstitialAdController", "Load init html template finish");
        } else if (this.o != null) {
            this.o.stopLoading();
            this.o.e();
            this.o.destroy();
            this.o = null;
        }
    }

    @Override // vpadn.M
    public final void a(int i, int i2) {
        if (this.p == null || this.a != null) {
        }
    }

    @Override // vpadn.M
    public final void a(int i, int i2, int i3, int i4) throws JSONException {
        if (this.p != null) {
            this.p.getGlobalVisibleRect(new Rect());
            int iRound = Math.round(VpadnAdSize.convertPixelsToDp(r0.left, this.a));
            int iRound2 = Math.round(VpadnAdSize.convertPixelsToDp(r0.top, this.a));
            int iRound3 = Math.round(VpadnAdSize.convertPixelsToDp(r0.right - r0.left, this.a));
            int iRound4 = Math.round(VpadnAdSize.convertPixelsToDp(r0.bottom - r0.top, this.a));
            ab.b("VponInterstitialAdController", "X1:" + iRound + " Y1:" + iRound2 + " wDip:" + iRound3 + " hDip:" + iRound4);
            try {
                this.i.put("x", iRound);
                this.i.put("y", iRound2);
                this.i.put("w", iRound3);
                this.i.put("h", iRound4);
                a("ad_pos_change", this.i);
            } catch (Exception e) {
                e.printStackTrace();
                ab.b("VponInterstitialAdController", "onWebViewLayoutChanged throw exception");
            }
        }
    }

    @Override // vpadn.M
    public final void a(WebView webView, int i, String str, String str2) {
        this.B = false;
    }

    public final boolean t() {
        return this.B;
    }

    public final void a(VpadnAdRequest vpadnAdRequest) throws JSONException {
        try {
            if (!r()) {
                ab.b("VponInterstitialAdController", "[Interstitial] Device is not on-line");
                if (this.u != null) {
                    this.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.NETWORK_ERROR);
                }
            } else {
                this.m = vpadnAdRequest;
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObjectB = N.a().b(this.a, (JSONObject) null);
                jSONObject.put("pf", this.d);
                jSONObjectB.put("pf", this.d);
                o();
                JSONObject jSONObjectA = a(jSONObjectB, vpadnAdRequest, this.j, this.k);
                jSONObjectA.put("build", "41504102");
                String strReplaceAll = "<!doctype html> <html> <head> <meta charset='utf-8'/>\n<script type='text/javascript' charset='utf-8' src='http://m.vpon.com/sdk/vpadn-sdk-core-v1.js'></script>\n<script type='text/javascript' charset='utf-8'>\nVPSDK_LoadSdkConstants( JSON_REPLACE1 );\nVPSDK_BuildAdReqUrl( JSON_REPLACE2 );\n</script><body></body></html>".replaceAll("JSON_REPLACE1", jSONObject.toString(4)).replaceAll("JSON_REPLACE2", jSONObjectA.toString(4));
                ab.a("VponInterstitialAdController", strReplaceAll);
                this.o = new VponAdWebView("init", this.a, this, this);
                this.o.loadDataWithBaseURL("file:///android_asset/www/vpon", strReplaceAll, "text/html", "utf-8", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ab.b("VponInterstitialAdController", "[Interstitial]loadInitHtmlTemplate throw Exception: " + e.getMessage());
        }
    }

    private JSONObject a(JSONObject jSONObject, VpadnAdRequest vpadnAdRequest, long j, long j2) throws JSONException {
        try {
            jSONObject.put("sid", j);
            jSONObject.put("seq", j2);
            Object obj = "mi";
            if (this.E) {
                obj = "mi_vid";
            }
            jSONObject.put("format", obj);
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
            jSONObject.put("ms", C0086a.a("NH/mLeyCBfokzYKUPNGEEg==", jSONObjectC.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public final void u() {
        Location locationA;
        try {
            ab.a("VponInterstitialAdController", "------> showInterstitialAd()");
            if (this.B) {
                this.B = false;
                if (this.a != null) {
                    if (this.a != null && this.q != null && !this.q.f315c) {
                        String strRemove = this.g.remove("url_type_impression");
                        if (strRemove != null) {
                            ab.a("VponInterstitialAdController", "----------->>>[interstitial] Send impression to server impressionUrl:" + strRemove);
                            try {
                                new H(strRemove, this, this.e).execute(new Object[0]);
                            } catch (Exception e) {
                                ab.a("VponInterstitialAdController", "sendImpressionToServer throw Exception", e);
                            }
                        } else {
                            ab.b("VponInterstitialAdController", "Cannot get interstitial impression URL");
                        }
                    }
                    if (this.u != null) {
                        this.u.onVponPresent();
                    }
                    Intent intent = new Intent(this.a, (Class<?>) VpadnActivity.class);
                    intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    intent.setFlags(268435456);
                    Bundle bundle = new Bundle();
                    bundle.putString("adType", FluctConstants.XML_NODE_INTERSTITIAL);
                    bundle.putString("html", this.z);
                    bundle.putBoolean("isMraidAd", false);
                    bundle.putBoolean("isUseCustomClose", false);
                    String string = UUID.randomUUID().toString();
                    bundle.putString("getControllerKey", string);
                    P.a().a(string, this);
                    this.w = this.a.getRequestedOrientation();
                    this.v = Resources.getSystem().getConfiguration().orientation;
                    bundle.putInt("originalRequestedOrientation", this.w);
                    bundle.putInt("beforeActivityOrientation", this.v);
                    bundle.putString("forceOrientation", this.y);
                    bundle.putBoolean("isAllowOrientationChange", this.x);
                    bundle.putString("click_url", this.g.get("url_type_click"));
                    bundle.putString("url", this.g.get("url_type_banner"));
                    if (this.q != null && (locationA = ad.a(this.a).a()) != null) {
                        Q q = this.q;
                        Q q2 = this.q;
                        bundle.putInt("distance", ae.a(null, null, locationA.getLatitude(), locationA.getLongitude()));
                    }
                    bundle.putLong("session_id", p());
                    bundle.putLong("sequence_number", q());
                    bundle.putBoolean("isFullScreen", (this.a.getWindow().getAttributes().flags & 1024) != 0);
                    intent.putExtras(bundle);
                    if (this.p != null) {
                        this.p.stopLoading();
                        this.p.e();
                        this.p.destroy();
                        this.p = null;
                    }
                    this.a.startActivity(intent);
                    return;
                }
                return;
            }
            ab.b("VponInterstitialAdController", "Interstitial Banner is not ready, cannot call showInterstitial");
        } catch (Exception e2) {
            e2.printStackTrace();
            ab.b("VponInterstitialAdController", "showInterstitialAd throw Exception:" + e2.getMessage());
        }
    }

    static /* synthetic */ void a(F f, Q q) {
        ab.a("VponInterstitialAdController", "------> prepareInterstitialAd()");
        if (f.a != null) {
            f.q = q;
            String str = f.q.a;
            f.z = str;
            ab.a("VponInterstitialAdController", "real get Interstitial Html:" + str);
            if (f.C == null) {
                try {
                    f.C = new az(f.a, "vpadn_video_cache", 100000000);
                } catch (Exception e) {
                    ab.a("VponInterstitialAdController", "Unable to create VpadnDiskLruCache.");
                }
            }
            if (f.p != null) {
                f.p.stopLoading();
                f.p.removeAllViews();
                f.p.e();
                f.p = null;
            }
            f.p = new VponAdWebView("InterstitialAdWebView(new Activity)", f.a, f, f);
            new Handler().postDelayed(new Runnable() { // from class: vpadn.F.3
                @Override // java.lang.Runnable
                public final void run() {
                    F.b(F.this);
                }
            }, 1000L);
            if (f.E) {
                return;
            }
            f.B = true;
            if (f.u != null) {
                f.u.onVponAdReceived();
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [vpadn.F$4] */
    static /* synthetic */ void b(F f) {
        if (f.E || f.B) {
            ab.a("VponInterstitialAdController", "---> CALL asyncLoadHtmlToWebViewByUrl()");
            try {
                new AsyncTask<Object, Integer, Integer>() { // from class: vpadn.F.4
                    @Override // android.os.AsyncTask
                    protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                        String str = F.this.g.get("url_type_banner");
                        if (C0086a.c(F.this.z) || F.this.p == null) {
                            if (F.this.p == null) {
                                ab.b("VponInterstitialAdController", "load interstitial ad to webview error-->mShowWebView == null");
                            }
                            if (C0086a.c(F.this.z)) {
                                ab.b("VponInterstitialAdController", "load interstitial ad to webview error-->StringUtils.isBlank(mHtml) == false mHtml:" + F.this.z);
                            }
                        } else {
                            F.this.p.loadDataWithBaseURL(str, F.this.z, "text/html", "utf-8", null);
                        }
                        return 1;
                    }
                }.execute(new Object[0]);
            } catch (Exception e) {
                ab.a("VponInterstitialAdController", "asyncLoadHtmlToWebViewByUrl throw Exception", e);
            }
        }
    }

    static /* synthetic */ void e(F f) {
        if (C0086a.c(f.b)) {
            ab.b("VponInterstitialAdController", "[Interstitial] bannerId is blank");
            if (f.u != null) {
                f.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
            }
        }
        if (f.a != null) {
            if (!ae.d(f.a)) {
                ab.b("VponInterstitialAdController", "[Interstitial] permission-checking is failed!!");
                if (f.u != null) {
                    f.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                    return;
                }
                return;
            }
            String str = f.g.get("url_type_banner");
            if (str != null) {
                try {
                    new I(str, f, f.e).execute(new Object[0]);
                    return;
                } catch (Exception e) {
                    ab.a("VponInterstitialAdController", "sendRequestToServer : VponRequestAsyncTask throw Exception", e);
                    return;
                }
            }
            ab.b("VponInterstitialAdController", "[Interstitial] mUrlMap.get(VponControllerInterface.URL_TYPE_BANNER) return null");
            if (f.u != null) {
                f.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
            }
        }
    }

    @Override // vpadn.E
    public final void j() {
        if (this.u != null) {
            this.u.onVponDismiss();
        }
    }

    public final void v() {
        ab.a("VponInterstitialAdController", "Call webViewHandleDestroy()");
        if (this.o != null) {
            this.o.stopLoading();
            this.o.removeAllViews();
            this.o.e();
            this.o = null;
        }
        if (this.p != null) {
            this.p.stopLoading();
            this.p.removeAllViews();
            this.p.e();
            this.p = null;
        }
        this.f = true;
    }

    @Override // vpadn.C
    protected final void n() {
        new Handler().post(new Runnable() { // from class: vpadn.F.5
            @Override // java.lang.Runnable
            public final void run() {
                F.e(F.this);
            }
        });
    }

    @Override // vpadn.C
    protected final void a(final Object obj) {
        new Handler().post(new Runnable() { // from class: vpadn.F.6
            @Override // java.lang.Runnable
            public final void run() {
                ab.b("VponInterstitialAdController", "[interstitial] doLoadBannerFail");
                F.this.v();
                if (obj == null || !(obj instanceof String) || !obj.toString().equals("NO_FILL")) {
                    F.this.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                } else {
                    F.this.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.NO_FILL);
                }
                S.a().d();
            }
        });
    }

    @Override // vpadn.E
    public final void l() {
        if (this.u != null) {
            this.u.onVponLeaveApplication();
        }
    }

    @Override // vpadn.M
    public final void m() {
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, ar arVar) {
        ab.b("VponInterstitialAdController", "VponInterstitialAdController.playVideoOnNativePlayer is not support");
    }

    @Override // vpadn.E
    public final void a(C0101o c0101o, String str) {
        c0101o.b();
        if (this.C.a(str) != null) {
            ab.a("VponInterstitialAdController", "cached Video already video url:" + str);
            if (this.E) {
                x();
                return;
            }
            return;
        }
        if (this.E) {
            this.D = ay.a(this, this.C);
        } else {
            this.D = ay.a(null, this.C);
        }
        try {
            this.D.execute(str);
        } catch (Exception e) {
            ab.a("VponInterstitialAdController", "cacheVideoByUrl : mVideoDownloadTask.execute throw Exception:", e);
        }
    }

    @Override // vpadn.E
    public final void a(String str, JSONArray jSONArray, C0101o c0101o) {
        try {
            c0101o.b(new JSONObject().put("e", "intersitial controller cannot call controlNativeVideoPlayer "));
        } catch (JSONException e) {
        }
        ab.b("VponInterstitialAdController", "intersitial controller cannot call controlNativeVideoPlayer ");
    }

    @Override // vpadn.ax.a
    public final void x() {
        this.a.runOnUiThread(new Runnable() { // from class: vpadn.F.7
            @Override // java.lang.Runnable
            public final void run() {
                ab.a("VponInterstitialAdController", "CALL onDownloadSuccess() for cache Done");
                F.this.B = true;
                if (F.this.u != null) {
                    F.this.u.onVponAdReceived();
                }
            }
        });
    }

    @Override // vpadn.ax.a
    public final void y() {
        this.a.runOnUiThread(new Runnable() { // from class: vpadn.F.2
            @Override // java.lang.Runnable
            public final void run() {
                ab.b("VponInterstitialAdController", "CALL onDownloadFailed():cant get video cache");
                if (F.this.u != null) {
                    F.this.u.onVponAdFailed(VpadnAdRequest.VpadnErrorCode.INTERNAL_ERROR);
                }
            }
        });
    }

    public final void a(String str, String str2, String str3) {
        if (this.p != null) {
            ab.a("VponInterstitialAdController", "VponInterstitialController.testSendJsonToVponCordovaPlugin return string:" + this.p.a(str, str2, str3));
        } else {
            ab.b("VponInterstitialAdController", "Cannot call VponBannerController.testSendJsonToVponCordovaPlugin ");
        }
    }
}
