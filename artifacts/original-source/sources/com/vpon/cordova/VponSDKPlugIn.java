package com.vpon.cordova;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.webkit.URLUtil;
import c.NetworkManager;
import com.google.android.gms.plus.PlusShare;
import com.vpadn.widget.VpadnActivity;
import com.vpon.webview.VponAdWebView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.B;
import vpadn.C0086a;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.D;
import vpadn.E;
import vpadn.N;
import vpadn.P;
import vpadn.S;
import vpadn.ab;
import vpadn.ad;
import vpadn.ar;
import vpadn.as;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VponSDKPlugIn extends C0103q {
    String a;

    /* JADX INFO: Access modifiers changed from: private */
    public static void a(C0101o c0101o, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("e", str);
        c0101o.b(jSONObject);
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, final C0101o c0101o) throws JSONException, IOException, ParseException {
        boolean z;
        String string;
        Uri uri;
        Intent intent;
        boolean z2;
        try {
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throws exception at execute", e);
            try {
                a(c0101o, "throw Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            return false;
        }
        if (str == null || jSONArray == null || c0101o == null) {
            ab.b("VponSDKPlugIn", "action == null || args == null || callbackContext ==null");
            return false;
        }
        ab.a("VponSDKPlugIn", "-->>execute action:" + str + " callbackId:" + c0101o.a() + " args:" + jSONArray.toString(4));
        if ("load_sdk_constants".equals(str)) {
            boolean z3 = false;
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(0);
                P pA = P.a();
                String str2 = (String) jSONObject.opt("mraid2_banner");
                ab.a("VponSDKPlugIn", "Banner JS:" + str2);
                if (C0086a.c(str2)) {
                    z3 = true;
                } else {
                    pA.a("mraid2_banner", str2);
                }
                String str3 = (String) jSONObject.opt("mraid2_expanded");
                ab.a("VponSDKPlugIn", "Banner expanded JS:" + str3);
                if (C0086a.c(str3)) {
                    z3 = true;
                } else {
                    pA.a("mraid2_expanded", str3);
                }
                String str4 = (String) jSONObject.opt("mraid2_interstitial");
                ab.a("VponSDKPlugIn", "Interstitial JS:" + str4);
                if (C0086a.c(str4)) {
                    z2 = true;
                } else {
                    pA.a("mraid2_intersitial", str4);
                    z2 = z3;
                }
                double d = jSONObject.getDouble("viewable_rate");
                ab.a("VponSDKPlugIn", "cover rate:" + d);
                pA.a("viewable_rate", Double.valueOf(d));
                int i = jSONObject.getInt("viewable_duration");
                ab.a("VponSDKPlugIn", "cover duration:" + i);
                pA.a("viewable_duration", Integer.valueOf(i));
                if (jSONObject.has("loc_accuracy")) {
                    int i2 = jSONObject.getInt("loc_accuracy");
                    ab.a("VponSDKPlugIn", "LOC accAccuracy:" + i2);
                    ad.a(i2);
                } else {
                    ab.b("VponSDKPlugIn", "Cannot find loc_accuracy");
                }
                if (jSONObject.has("loc_cachetime")) {
                    int i3 = jSONObject.getInt("loc_cachetime");
                    ab.a("VponSDKPlugIn", "LOC cacheTime:" + i3);
                    ad.b(i3);
                } else {
                    ab.b("VponSDKPlugIn", "Cannot find loc_cachetime");
                }
                if (z2) {
                    ab.b("VponSDKPlugIn", "Cannot get all of js files");
                    a(c0101o, "Cannot get all of js files");
                } else {
                    c0101o.b();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                ab.a("VponSDKPlugIn", "doLoadSdkConstants throw exception:" + e3.getMessage(), e3);
                try {
                    a(c0101o, "doLoadSdkConstants throws Exception:" + e3.getMessage());
                } catch (JSONException e4) {
                    e4.printStackTrace();
                }
            }
            return true;
        }
        if ("close".equals(str)) {
            try {
                if (this.webView instanceof VponAdWebView) {
                    VponAdWebView vponAdWebView = (VponAdWebView) this.webView;
                    if (vponAdWebView.h().equals("bannerWebView")) {
                        ab.b("VponSDKPlugIn", "banner of status of non-expanded cannot call close");
                        a(c0101o, "banner of status of non-expanded cannot call close");
                    } else {
                        String strH = vponAdWebView.h();
                        if (strH.equals("bannerWebViewExpanded") || strH.equals("bannerWebViewResized")) {
                            final D d2 = (D) this.cordova;
                            this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    d2.t();
                                    c0101o.b();
                                }
                            });
                        } else if (strH.equals("SdkOpenWebApp") || strH.equals("InterstitialAdWebView(new Activity)") || strH.equals("videoWebView")) {
                            final VpadnActivity vpadnActivity = (VpadnActivity) this.cordova;
                            this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.7
                                @Override // java.lang.Runnable
                                public final void run() {
                                    vpadnActivity.b();
                                    c0101o.b();
                                }
                            });
                        } else {
                            ab.b("VponSDKPlugIn", "dont support to close unknown VponWebView ID");
                            a(c0101o, "dont support to close unknown VponWebView ID");
                        }
                    }
                } else {
                    ab.b("VponSDKPlugIn", "something wrong webView instanceof VponAdWebView is falsed");
                    a(c0101o, "something wrong webView instanceof VponAdWebView is false");
                }
            } catch (Exception e5) {
                e5.printStackTrace();
                ab.a("VponSDKPlugIn", "doClose throw exception:" + e5.getMessage(), e5);
                try {
                    a(c0101o, "doClose throw Exception:" + e5.getMessage());
                } catch (JSONException e6) {
                    e6.printStackTrace();
                }
            }
            return true;
        }
        if ("open_webapp".equals(str)) {
            try {
                if (this.webView instanceof VponAdWebView) {
                    VponAdWebView vponAdWebView2 = (VponAdWebView) this.webView;
                    if (vponAdWebView2.h().equals("bannerWebView")) {
                        String strH2 = vponAdWebView2.h();
                        JSONObject jSONObject2 = jSONArray.getJSONObject(0);
                        ab.a("VponSDKPlugIn", "jsonObj:" + jSONObject2.toString());
                        String string2 = jSONObject2.has("u") ? jSONObject2.getString("u") : null;
                        String string3 = jSONObject2.has("html") ? jSONObject2.getString("html") : null;
                        boolean z4 = false;
                        if (jSONObject2.has("custom_close")) {
                            z4 = jSONObject2.getInt("custom_close") > 0;
                        }
                        boolean z5 = true;
                        if (jSONObject2.has("allow_orientation_change")) {
                            z5 = jSONObject2.getInt("allow_orientation_change") > 0;
                        }
                        String string4 = NetworkManager.TYPE_NONE;
                        if (jSONObject2.has("force_orientation")) {
                            string4 = jSONObject2.getString("force_orientation");
                        }
                        int i4 = jSONObject2.has("bk_c") ? jSONObject2.getInt("bk_c") : 16777215;
                        boolean z6 = false;
                        if (jSONObject2.has("show_prog_bar")) {
                            z6 = jSONObject2.getInt("show_prog_bar") > 0;
                        }
                        boolean z7 = false;
                        if (jSONObject2.has("show_nav_bar")) {
                            z7 = jSONObject2.getInt("show_nav_bar") > 0;
                        }
                        boolean z8 = false;
                        if (jSONObject2.has("use_webview_load_url")) {
                            z8 = jSONObject2.getInt("use_webview_load_url") > 0;
                        }
                        if (C0086a.c(string3) && C0086a.c(string2)) {
                            ab.b("VponSDKPlugIn", "StringUtils.isBlank(html) && StringUtils.isBlank(url)");
                            a(c0101o, "StringUtils.isBlank(html) && StringUtils.isBlank(url) at doOpenWebAppStep1");
                        } else {
                            doOpenWebAppStep2(strH2, c0101o, string2, string3, z4, z5, string4, i4, z6, z7, z8);
                        }
                    } else {
                        ab.b("VponSDKPlugIn", "only banner webview allow to call open_webapp");
                        a(c0101o, "only banner webview allow to call open_webapp");
                    }
                } else {
                    ab.b("VponSDKPlugIn", "webView instanceof VponAdWebView is false at doOpenWebAppStep1");
                    a(c0101o, "webView instanceof VponAdWebView is false at doOpenWebAppStep1");
                }
            } catch (Exception e7) {
                e7.printStackTrace();
                ab.a("VponSDKPlugIn", "throws exception in doOpenWebAppStep1", e7);
                try {
                    a(c0101o, "throws exception in doOpenWebAppStep1 exception:" + e7.getMessage());
                } catch (JSONException e8) {
                    e8.printStackTrace();
                }
            }
            return true;
        }
        if ("open_browser".equals(str)) {
            try {
                JSONObject jSONObject3 = jSONArray.getJSONObject(0);
                String string5 = jSONObject3.has("u") ? jSONObject3.getString("u") : null;
                if (C0086a.c(string5)) {
                    ab.b("VponSDKPlugIn", "cannot get url!");
                    a(c0101o, "Cannot get ur at doOpenBrowserl!");
                } else {
                    Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(string5));
                    intent2.addFlags(268435456);
                    this.cordova.a().startActivity(intent2);
                    if (this.webView instanceof VponAdWebView) {
                        VponAdWebView vponAdWebView3 = (VponAdWebView) this.webView;
                        if (vponAdWebView3.h().equals("bannerWebView")) {
                            if (this.cordova instanceof D) {
                                D d3 = (D) this.cordova;
                                d3.u();
                                d3.v();
                            }
                        } else if ((vponAdWebView3.h().equals("SdkOpenWebApp") || vponAdWebView3.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView3.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                            ((E) this.cordova).l();
                        }
                    } else {
                        a(c0101o, "something error [webView instanceof VponAdWebView is false]");
                    }
                    c0101o.b();
                }
            } catch (Exception e9) {
                ab.a("VponSDKPlugIn", "doOpenBrowser throw exception:" + e9.getMessage(), e9);
                try {
                    a(c0101o, "something error e.getLocalizedMessage:" + e9.getMessage());
                } catch (JSONException e10) {
                    e10.printStackTrace();
                }
            }
            return true;
        }
        if ("expand".equals(str)) {
            try {
                if (!(this.webView instanceof VponAdWebView)) {
                    ab.b("VponSDKPlugIn", "something wrong webView instanceof VponAdWebView is falsed");
                    a(c0101o, "only banner webview allow to call expand");
                } else if (((VponAdWebView) this.webView).h().equals("bannerWebView")) {
                    JSONObject jSONObject4 = jSONArray.getJSONObject(0);
                    final boolean z9 = false;
                    if (jSONObject4.has("custom_close")) {
                        z9 = jSONObject4.getInt("custom_close") > 0;
                    }
                    final boolean z10 = true;
                    if (jSONObject4.has("allow_orientation_change")) {
                        z10 = jSONObject4.getInt("allow_orientation_change") > 0;
                    }
                    final String string6 = NetworkManager.TYPE_NONE;
                    if (jSONObject4.has("force_orientation")) {
                        string6 = jSONObject4.getString("force_orientation");
                    }
                    final int i5 = jSONObject4.has("bk_c") ? jSONObject4.getInt("bk_c") : 0;
                    if (this.cordova instanceof D) {
                        final D d4 = (D) this.cordova;
                        this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.8
                            @Override // java.lang.Runnable
                            public final void run() {
                                d4.a(c0101o, z9, z10, string6, i5);
                            }
                        });
                    } else {
                        ab.b("VponSDKPlugIn", "only banner webview allow to call expand");
                        a(c0101o, "something error [cordova instanceof VponBannerController is false]");
                    }
                } else {
                    ab.b("VponSDKPlugIn", "only banner webview allow to call expand");
                    a(c0101o, "only banner webview allow to call expand");
                }
            } catch (Exception e11) {
                e11.printStackTrace();
                ab.a("VponSDKPlugIn", "doExpand throw exception:" + e11.getMessage(), e11);
                try {
                    a(c0101o, "something wrong, error exception:" + e11.getMessage());
                } catch (JSONException e12) {
                    e12.printStackTrace();
                }
            }
            return true;
        }
        if ("resize".equals(str)) {
            try {
                if (this.webView instanceof VponAdWebView) {
                    VponAdWebView vponAdWebView4 = (VponAdWebView) this.webView;
                    if (vponAdWebView4.h().equals("bannerWebView") || vponAdWebView4.h().equals("bannerWebViewResized")) {
                        JSONObject jSONObject5 = jSONArray.getJSONObject(0);
                        final int i6 = -1;
                        final int i7 = -1;
                        if (jSONObject5.has("w")) {
                            i6 = jSONObject5.getInt("w");
                        } else {
                            a(c0101o, "doResize cannot find out width attribute");
                        }
                        if (jSONObject5.has("h")) {
                            i7 = jSONObject5.getInt("h");
                        } else {
                            a(c0101o, "doResize cannot find out height attribute");
                        }
                        if (i6 <= 0 || i7 <= 0) {
                            a(c0101o, "doResize  height <=0 or width <= 0");
                        }
                        final int i8 = 0;
                        final int i9 = 0;
                        if (jSONObject5.has("off_x")) {
                            i8 = jSONObject5.getInt("off_x");
                        } else {
                            a(c0101o, "doResize cannot find out off_x attribute");
                        }
                        if (jSONObject5.has("off_y")) {
                            i9 = jSONObject5.getInt("off_y");
                        } else {
                            a(c0101o, "doResize cannot find out off_y attribute");
                        }
                        final String string7 = jSONObject5.has("cust_close_pos") ? jSONObject5.getString("cust_close_pos") : null;
                        final boolean z11 = false;
                        if (jSONObject5.has("allow_off_scr")) {
                            z11 = jSONObject5.getInt("allow_off_scr") > 0;
                        }
                        if (this.cordova instanceof D) {
                            final D d5 = (D) this.cordova;
                            this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.9
                                @Override // java.lang.Runnable
                                public final void run() {
                                    D d6 = d5;
                                    C0101o c0101o2 = c0101o;
                                    int i10 = i6;
                                    int i11 = i7;
                                    int i12 = i8;
                                    int i13 = i9;
                                    String str5 = string7;
                                    boolean z12 = z11;
                                    d6.a(c0101o2, i10, i11, i12, i13, str5);
                                }
                            });
                        } else {
                            ab.b("VponSDKPlugIn", "only banner webview allow to call resize");
                            a(c0101o, "something error [cordova instanceof VponBannerController is false]");
                        }
                    } else {
                        ab.b("VponSDKPlugIn", "only banner or resized webview allow to call resize");
                        a(c0101o, "only banner or resized webview allow to call resize");
                    }
                } else {
                    ab.b("VponSDKPlugIn", "something wrong webView instanceof VponAdWebView is falsed");
                    a(c0101o, "only banner webview allow to call expand");
                }
            } catch (Exception e13) {
                e13.printStackTrace();
                ab.a("VponSDKPlugIn", "doResize throw exception:" + e13.getMessage(), e13);
                try {
                    a(c0101o, "something wrong, error exception:" + e13.getMessage());
                } catch (JSONException e14) {
                    e14.printStackTrace();
                }
            }
            return true;
        }
        if ("click".equals(str)) {
            try {
                if (this.cordova instanceof E) {
                    final String strK = ((E) this.cordova).k();
                    if (strK == null) {
                        a(c0101o, "cannot get click-url, maybe banner webview has sent click to server");
                    } else {
                        ab.a("VponSDKPlugIn", "====> clickUrl is" + strK);
                        this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.3
                            /* JADX WARN: Type inference failed for: r0v2, types: [com.vpon.cordova.VponSDKPlugIn$3$1] */
                            @Override // java.lang.Runnable
                            public final void run() {
                                S.a().b(strK);
                                try {
                                    final String str5 = strK;
                                    final C0101o c0101o2 = c0101o;
                                    new AsyncTask<Object, Integer, Integer>(this) { // from class: com.vpon.cordova.VponSDKPlugIn.3.1
                                        private JSONObject a = new JSONObject();
                                        private int b = -1;

                                        @Override // android.os.AsyncTask
                                        protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                                            return a();
                                        }

                                        private Integer a() throws Throwable {
                                            try {
                                                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                                                C0086a.a(defaultHttpClient);
                                                C0086a.a(str5, defaultHttpClient);
                                                Object objA = P.a().a("user-agent");
                                                if (objA != null) {
                                                    ab.c("VponSDKPlugIn", "userAgent:" + objA);
                                                    defaultHttpClient.getParams().setParameter("http.useragent", objA);
                                                } else {
                                                    ab.b("VponSDKPlugIn", "Cannot get user agent from StaticStorage.instance().get(StaticStorage.USER_AGENT)");
                                                }
                                                HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str5));
                                                C0086a.b(str5, defaultHttpClient);
                                                this.b = httpResponseExecute.getStatusLine().getStatusCode();
                                                if (this.b >= 100 && this.b < 300) {
                                                    ab.a("VponSDKPlugIn", "====> clickUrl is OK, status code:" + this.b);
                                                    this.a.put("status", this.b);
                                                    c0101o2.a(this.a);
                                                } else {
                                                    ab.b("VponSDKPlugIn", "doClick fail. status code:" + this.b);
                                                    this.a.put("status", this.b);
                                                    this.a.put("e", "http status is not in (1xx~2xx) ");
                                                    c0101o2.b(this.a);
                                                }
                                                return 1;
                                            } catch (Exception e15) {
                                                e15.printStackTrace();
                                                try {
                                                    ab.b("VponSDKPlugIn", "http_get throw Exception");
                                                    this.a.put("e", "do_click throw Exception:" + e15.getMessage());
                                                    c0101o2.b(this.a);
                                                } catch (Exception e16) {
                                                    e16.printStackTrace();
                                                }
                                                return 1;
                                            }
                                        }
                                    }.execute(new Object[0]);
                                } catch (Exception e15) {
                                    ab.a("VponSDKPlugIn", "doClick throw Exception", e15);
                                }
                            }
                        });
                        c0101o.b();
                    }
                } else {
                    a(c0101o, "cordova instanceof VponControllerInterface is false [doClick]");
                }
            } catch (Exception e15) {
                e15.printStackTrace();
                ab.a("VponSDKPlugIn", "throw exception at doClick Exception:" + e15.getMessage(), e15);
                try {
                    a(c0101o, "throw exception at doClick Exception:" + e15.getMessage());
                } catch (JSONException e16) {
                    e16.printStackTrace();
                }
            }
            return true;
        }
        if ("place_call".equals(str)) {
            try {
                JSONObject jSONObject6 = jSONArray.getJSONObject(0);
                String string8 = jSONObject6.has("tel") ? jSONObject6.getString("tel") : null;
                if (C0086a.c(string8)) {
                    ab.b("VponSDKPlugIn", "TEL number format is wrong");
                    a(c0101o, "TEL number format is wrong");
                } else {
                    Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(string8.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "")));
                    intent3.addFlags(268435456);
                    this.cordova.a().startActivity(intent3);
                    if (this.webView instanceof VponAdWebView) {
                        VponAdWebView vponAdWebView5 = (VponAdWebView) this.webView;
                        if (vponAdWebView5.h().equals("bannerWebView")) {
                            if (this.cordova instanceof D) {
                                D d6 = (D) this.cordova;
                                d6.u();
                                d6.v();
                            }
                        } else if ((vponAdWebView5.h().equals("SdkOpenWebApp") || vponAdWebView5.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView5.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                            ((E) this.cordova).l();
                        }
                    } else {
                        a(c0101o, "something error [webView instanceof VponAdWebView is false]");
                    }
                    c0101o.b();
                }
            } catch (Exception e17) {
                e17.printStackTrace();
                ab.a("VponSDKPlugIn", "doPlaceCall throw Exception:" + e17.getMessage(), e17);
                try {
                    a(c0101o, "doPlaceCall throw Exception:" + e17.getMessage());
                } catch (JSONException e18) {
                    e18.printStackTrace();
                }
            }
            return true;
        }
        if ("send_sms".equals(str)) {
            try {
                JSONObject jSONObject7 = jSONArray.getJSONObject(0);
                String string9 = jSONObject7.has("tel") ? jSONObject7.getString("tel") : null;
                if (C0086a.c(string9)) {
                    ab.b("VponSDKPlugIn", "TEL number format is wrong");
                    a(c0101o, "TEL number format is wrong");
                } else {
                    String strReplaceAll = string9.replaceAll("\\s+", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");
                    String string10 = jSONObject7.has("b") ? jSONObject7.getString("b") : null;
                    if (C0086a.c(string10)) {
                        ab.b("VponSDKPlugIn", "SMS body is empty");
                        a(c0101o, "SMS body is empty");
                    } else {
                        Intent intent4 = new Intent("android.intent.action.VIEW");
                        intent4.putExtra("sms_body", string10);
                        intent4.setData(Uri.parse("sms:" + strReplaceAll));
                        intent4.putExtra("address", strReplaceAll);
                        intent4.setType("vnd.android-dir/mms-sms");
                        intent4.addFlags(268435456);
                        this.cordova.a().startActivity(intent4);
                        if (this.webView instanceof VponAdWebView) {
                            VponAdWebView vponAdWebView6 = (VponAdWebView) this.webView;
                            if (vponAdWebView6.h().equals("bannerWebView")) {
                                if (this.cordova instanceof D) {
                                    D d7 = (D) this.cordova;
                                    d7.u();
                                    d7.v();
                                }
                            } else if ((vponAdWebView6.h().equals("SdkOpenWebApp") || vponAdWebView6.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView6.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                                ((E) this.cordova).l();
                            }
                        } else {
                            a(c0101o, "something error [webView instanceof VponAdWebView is false]");
                        }
                        c0101o.b();
                    }
                }
            } catch (Exception e19) {
                e19.printStackTrace();
                ab.a("VponSDKPlugIn", "doSendSMS throw Exception:" + e19.getMessage(), e19);
                try {
                    a(c0101o, "doSendSMS throw Exception:" + e19.getMessage());
                } catch (JSONException e20) {
                    e20.printStackTrace();
                }
            }
            return true;
        }
        if ("add_event".equals(str)) {
            try {
                JSONObject jSONObject8 = jSONArray.getJSONObject(0);
                ((E) this.cordova).a((String) jSONObject8.opt("et"), ((Integer) jSONObject8.opt("eid")).intValue(), c0101o);
            } catch (Exception e21) {
                e21.printStackTrace();
                ab.a("VponSDKPlugIn", "doAddEvent throw exception:" + e21.getMessage(), e21);
                try {
                    a(c0101o, " doAddEvent throw exception:" + e21.getMessage());
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
            return true;
        }
        if ("remove_event".equals(str)) {
            try {
                JSONObject jSONObject9 = jSONArray.getJSONObject(0);
                ((E) this.cordova).b((String) jSONObject9.opt("et"), ((Integer) jSONObject9.opt("eid")).intValue(), c0101o);
            } catch (Exception e23) {
                e23.printStackTrace();
                ab.a("VponSDKPlugIn", "doRemoveEvent throw exception:" + e23.getMessage(), e23);
                try {
                    a(c0101o, " doRemoveEvent throw exception:" + e23.getMessage());
                } catch (Exception e24) {
                    e24.printStackTrace();
                }
            }
            return true;
        }
        if ("open_store".equals(str)) {
            try {
                JSONObject jSONObject10 = jSONArray.getJSONObject(0);
                ab.a("VponSDKPlugIn", "jsonObj:" + jSONObject10.toString());
                String string11 = jSONObject10.has("u") ? jSONObject10.getString("u") : null;
                if (string11.startsWith("market:") || string11.startsWith("http:") || string11.startsWith("https:")) {
                    Intent intent5 = new Intent("android.intent.action.VIEW");
                    intent5.setData(Uri.parse(string11));
                    intent5.addFlags(268435456);
                    this.cordova.a().startActivity(intent5);
                    if (this.webView instanceof VponAdWebView) {
                        VponAdWebView vponAdWebView7 = (VponAdWebView) this.webView;
                        if (vponAdWebView7.h().equals("bannerWebView")) {
                            if (this.cordova instanceof D) {
                                D d8 = (D) this.cordova;
                                d8.u();
                                d8.v();
                            }
                        } else if ((vponAdWebView7.h().equals("SdkOpenWebApp") || vponAdWebView7.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView7.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                            ((E) this.cordova).l();
                        }
                    } else {
                        a(c0101o, "something error [webView instanceof VponAdWebView is false]");
                    }
                    c0101o.b();
                } else {
                    ab.b("VponSDKPlugIn", "url format error");
                    a(c0101o, "url format error");
                }
            } catch (Exception e25) {
                e25.printStackTrace();
                ab.a("VponSDKPlugIn", "throw exception at doOpenStore Exception:" + e25.getMessage(), e25);
                try {
                    a(c0101o, "throw exception at doOpenStore Exception:" + e25.getMessage());
                } catch (JSONException e26) {
                    e26.printStackTrace();
                }
            }
            return true;
        }
        if ("open_video".equals(str)) {
            try {
                JSONObject jSONObject11 = jSONArray.getJSONObject(0);
                ab.a("VponSDKPlugIn", "jsonObj:" + jSONObject11.toString());
                string = jSONObject11.has("u") ? jSONObject11.getString("u") : null;
                if (string.contains("bit.ly/") || string.contains("goo.gl/") || string.contains("tinyurl.com/") || string.contains("youtu.be/")) {
                    URLConnection uRLConnectionOpenConnection = new URL(string).openConnection();
                    uRLConnectionOpenConnection.connect();
                    InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
                    string = uRLConnectionOpenConnection.getURL().toString();
                    inputStream.close();
                }
                uri = Uri.parse(string);
            } catch (Exception e27) {
                e27.printStackTrace();
                ab.a("VponSDKPlugIn", "doOpenVideo throws exception", e27);
                try {
                    a(c0101o, "doOpenVideo throws exception:" + e27.getMessage());
                } catch (JSONException e28) {
                    e28.printStackTrace();
                }
            }
            if (string.contains("youtube.com")) {
                Uri uri2 = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
                if (!b()) {
                    ab.b("VponSDKPlugIn", "URL is youtube format but not install youtube client");
                    a(c0101o, "URL is youtube format but not install youtube client");
                    return true;
                }
                intent = new Intent("android.intent.action.VIEW", uri2);
            } else {
                intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(uri, "video/*");
            }
            this.cordova.a().startActivity(intent);
            if (this.webView instanceof VponAdWebView) {
                VponAdWebView vponAdWebView8 = (VponAdWebView) this.webView;
                if (vponAdWebView8.h().equals("bannerWebView")) {
                    if (this.cordova instanceof D) {
                        D d9 = (D) this.cordova;
                        d9.u();
                        d9.v();
                    }
                } else if ((vponAdWebView8.h().equals("SdkOpenWebApp") || vponAdWebView8.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView8.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                    ((E) this.cordova).l();
                }
                c0101o.b();
            } else {
                a(c0101o, "something error [webView instanceof VponAdWebView is false]");
            }
            return true;
        }
        if ("open_videoEx".equals(str)) {
            try {
                if (this.webView instanceof VponAdWebView) {
                    VponAdWebView vponAdWebView9 = (VponAdWebView) this.webView;
                    if (vponAdWebView9.h().equals("init") || vponAdWebView9.h().equals("init-finish")) {
                        String str5 = "error webview call doOpenVideoEx vponWebView.getVponWebViewId():" + vponAdWebView9.h();
                        ab.b("VponSDKPlugIn", str5);
                        a(c0101o, str5);
                    } else {
                        JSONObject jSONObject12 = jSONArray.getJSONObject(0);
                        ab.a("VponSDKPlugIn", "doOpenVideoEx jsonObj:" + jSONObject12.toString(4));
                        final ar arVarB = b(jSONObject12);
                        if (arVarB == null) {
                            a(c0101o, "videoData is NULL in doOpenVideoEx");
                        } else {
                            this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.10
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ((E) VponSDKPlugIn.this.cordova).a(c0101o, arVarB);
                                }
                            });
                        }
                    }
                } else {
                    ab.b("VponSDKPlugIn", "webView instanceof vponAdWebView is false at doOpenVideoEx");
                    a(c0101o, "webView instanceof vponAdWebView is false at doOpenVideoEx");
                }
            } catch (Exception e29) {
                ab.a("VponSDKPlugIn", "throws exception in doOpenVideoEx", e29);
                try {
                    a(c0101o, "throws exception in doOpenVideoEx exception:" + e29.getMessage());
                } catch (JSONException e30) {
                    ab.a("VponSDKPlugIn", "doOpenVideoEx throws Exception", e30);
                }
            }
            return true;
        }
        if ("prepare_open_videoEx".equals(str)) {
            ab.a("VponSDKPlugIn", "call doPrepareOpenVideoEx for video cache");
            try {
                if (this.webView instanceof VponAdWebView) {
                    VponAdWebView vponAdWebView10 = (VponAdWebView) this.webView;
                    if (vponAdWebView10.h().equals("InterstitialAdWebView(new Activity)")) {
                        JSONObject jSONObject13 = jSONArray.getJSONObject(0);
                        ab.a("VponSDKPlugIn", "doPrepareOpenVideoEx jsonObj:" + jSONObject13.toString(4));
                        final String strA = a(jSONObject13);
                        if (strA == null) {
                            ab.b("VponSDKPlugIn", "videoUrl is NULL in doPrepareOpenVideoEx");
                            a(c0101o, "videoUrl is NULL in doPrepareOpenVideoEx");
                        } else {
                            this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.11
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ((E) VponSDKPlugIn.this.cordova).a(c0101o, strA);
                                }
                            });
                        }
                    } else {
                        String str6 = "error webview call doPrepareOpenVideoEx vponWebView.getVponWebViewId():" + vponAdWebView10.h();
                        ab.b("VponSDKPlugIn", str6);
                        a(c0101o, str6);
                    }
                } else {
                    ab.b("VponSDKPlugIn", "webView instanceof vponAdWebView is false at doPrepareOpenVideoEx");
                    a(c0101o, "webView instanceof vponAdWebView is false at doPrepareOpenVideoEx");
                }
            } catch (Exception e31) {
                ab.a("VponSDKPlugIn", "throws exception in doPrepareOpenVideoEx", e31);
                try {
                    a(c0101o, "throws exception in doPrepareOpenVideoEx exception:" + e31.getMessage());
                } catch (JSONException e32) {
                    ab.a("VponSDKPlugIn", "doPrepareOpenVideoEx throws Exception", e32);
                }
            }
            return true;
        }
        if ("open_intent".equals(str)) {
            a(jSONArray, c0101o);
            return true;
        }
        if ("cre_cal_event".equals(str)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this, "yyyy-MM-dd'T'HH:mm:ssZ") { // from class: com.vpon.cordova.VponSDKPlugIn.13
                    @Override // java.text.SimpleDateFormat, java.text.DateFormat
                    public final Date parse(String str7, ParsePosition parsePosition) {
                        return super.parse(str7.replaceFirst(":(?=[0-9]{2}$)", ""), parsePosition);
                    }
                };
                JSONObject jSONObject14 = jSONArray.getJSONObject(0).getJSONObject("e");
                String string12 = jSONObject14.has(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION) ? jSONObject14.getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION) : null;
                String string13 = jSONObject14.has("location") ? jSONObject14.getString("location") : null;
                String string14 = jSONObject14.has("start") ? jSONObject14.getString("start") : null;
                String string15 = jSONObject14.has("end") ? jSONObject14.getString("end") : null;
                String string16 = jSONObject14.has("summary") ? jSONObject14.getString("summary") : null;
                if (string14 == null || string15 == null) {
                    a(c0101o, "Cannot get start or end at doCreateCalendarEvent");
                } else if (string12 == null) {
                    a(c0101o, "Cannot get title (description) at doCreateCalendarEvent");
                } else {
                    Date date = simpleDateFormat.parse(string14);
                    Date date2 = simpleDateFormat.parse(string15);
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    Intent intent6 = new Intent("android.intent.action.EDIT");
                    intent6.setType("vnd.android.cursor.item/event");
                    gregorianCalendar.setTime(date);
                    intent6.putExtra("beginTime", gregorianCalendar.getTimeInMillis());
                    gregorianCalendar.setTime(date2);
                    intent6.putExtra("endTime", gregorianCalendar.getTimeInMillis());
                    if (string16 != null) {
                        intent6.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, string16);
                    }
                    if (string13 != null) {
                        intent6.putExtra("eventLocation", string13);
                    }
                    if (string12 != null) {
                        intent6.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, string12);
                    }
                    intent6.addFlags(268435456);
                    try {
                        this.cordova.a().startActivity(intent6);
                        if (this.webView instanceof VponAdWebView) {
                            VponAdWebView vponAdWebView11 = (VponAdWebView) this.webView;
                            if (vponAdWebView11.h().equals("bannerWebView")) {
                                if (this.cordova instanceof D) {
                                    D d10 = (D) this.cordova;
                                    d10.u();
                                    d10.v();
                                }
                            } else if ((vponAdWebView11.h().equals("SdkOpenWebApp") || vponAdWebView11.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView11.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                                ((E) this.cordova).l();
                            }
                        } else {
                            a(c0101o, "something error [webView instanceof VponAdWebView is false]");
                        }
                        c0101o.b();
                    } catch (Exception e33) {
                        e33.printStackTrace();
                        ab.b("VponSDKPlugIn", "doCreateCalendarEvent throw Exception[startActivity]");
                        a(c0101o, "doCreateCalendarEvent throw Exception:" + e33.getMessage());
                    }
                }
            } catch (Exception e34) {
                e34.printStackTrace();
                ab.a("VponSDKPlugIn", "doCreateCalendarEvent throw Exception" + e34.getMessage(), e34);
                try {
                    a(c0101o, "doCreateCalendarEvent throw Exception:" + e34.getMessage());
                } catch (JSONException e35) {
                    e35.printStackTrace();
                }
            }
            return true;
        }
        if ("store_pic".equals(str)) {
            Activity activityA = this.cordova.a();
            if (activityA == null) {
                ab.b("VponSDKPlugIn", "context is null in doStroePicture");
                try {
                    a(c0101o, "context is null at doStroePicture");
                } catch (JSONException e36) {
                    e36.printStackTrace();
                }
            } else {
                this.cordova.a().runOnUiThread(new AnonymousClass14(jSONArray, c0101o, activityA));
            }
            return true;
        }
        if ("set_orientation".equals(str)) {
            try {
                if (!(this.webView instanceof VponAdWebView)) {
                    ab.b("VponSDKPlugIn", "something wrong [webView instanceof VponAdWebView is falsed]");
                    a(c0101o, "something wrong [webView instanceof VponAdWebView is false]");
                } else if (((VponAdWebView) this.webView).h().equals("bannerWebView")) {
                    ab.b("VponSDKPlugIn", "banner webview do not allow to call set_orientation");
                    a(c0101o, "banner webview do not allow to call set_orientation");
                } else {
                    JSONObject jSONObject15 = jSONArray.getJSONObject(0);
                    ab.a("VponSDKPlugIn", "jsonObj:" + jSONObject15.toString());
                    if (jSONObject15.has("allow_orientation_change")) {
                        z = jSONObject15.getInt("allow_orientation_change") > 0;
                    } else {
                        z = true;
                    }
                    String string17 = NetworkManager.TYPE_NONE;
                    if (jSONObject15.has("force_orientation")) {
                        string17 = jSONObject15.getString("force_orientation");
                    }
                    int i10 = Resources.getSystem().getConfiguration().orientation;
                    if (string17.equals(NetworkManager.TYPE_NONE)) {
                        if (!z) {
                            if (i10 == 2) {
                                this.cordova.a().setRequestedOrientation(0);
                            } else if (i10 == 1) {
                                this.cordova.a().setRequestedOrientation(1);
                            }
                        }
                    } else if (string17.equals("portrait")) {
                        this.cordova.a().setRequestedOrientation(1);
                    } else if (string17.equals("landscape")) {
                        this.cordova.a().setRequestedOrientation(0);
                    }
                    c0101o.b();
                }
            } catch (Exception e37) {
                e37.printStackTrace();
                ab.a("VponSDKPlugIn", "throw exception at doSetOrientation Exception:" + e37.getMessage(), e37);
                try {
                    a(c0101o, "throw exception at doSetOrientation Exception:" + e37.getMessage());
                } catch (JSONException e38) {
                    e38.printStackTrace();
                }
            }
            return true;
        }
        if ("http_get".equals(str)) {
            try {
                JSONObject jSONObject16 = jSONArray.getJSONObject(0);
                final String string18 = jSONObject16.has("u") ? jSONObject16.getString("u") : null;
                if (C0086a.c(string18)) {
                    a(c0101o, "Cannot get url!");
                } else if (string18.toLowerCase().startsWith("http://") || string18.toLowerCase().startsWith("https://")) {
                    final int i11 = jSONObject16.has("timeout") ? jSONObject16.getInt("timeout") : -1;
                    this.cordova.a().runOnUiThread(new Runnable(this) { // from class: com.vpon.cordova.VponSDKPlugIn.4
                        /* JADX WARN: Type inference failed for: r0v1, types: [com.vpon.cordova.VponSDKPlugIn$4$1] */
                        @Override // java.lang.Runnable
                        public final void run() {
                            try {
                                final String str7 = string18;
                                final int i12 = i11;
                                final C0101o c0101o2 = c0101o;
                                new AsyncTask<Object, Integer, Integer>(this) { // from class: com.vpon.cordova.VponSDKPlugIn.4.1
                                    private JSONObject a = new JSONObject();
                                    private int b = -1;

                                    @Override // android.os.AsyncTask
                                    protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                                        return a();
                                    }

                                    private Integer a() throws Throwable {
                                        try {
                                            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                                            C0086a.a(defaultHttpClient);
                                            C0086a.a(str7, defaultHttpClient);
                                            if (i12 != -1) {
                                                ab.c("VponSDKPlugIn", "timeout ms:" + (i12 * 1000));
                                                defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(i12 * 1000));
                                            }
                                            Object objA = P.a().a("user-agent");
                                            if (objA != null) {
                                                ab.c("VponSDKPlugIn", "userAgent:" + objA);
                                                defaultHttpClient.getParams().setParameter("http.useragent", objA);
                                            } else {
                                                ab.b("VponSDKPlugIn", "Cannot get user agent from StaticStorage.instance().get(StaticStorage.USER_AGENT)");
                                            }
                                            HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str7));
                                            C0086a.b(str7, defaultHttpClient);
                                            this.b = httpResponseExecute.getStatusLine().getStatusCode();
                                            if (this.b >= 100 && this.b < 300) {
                                                this.a.put("status", this.b);
                                                c0101o2.a(this.a);
                                            } else {
                                                ab.b("VponSDKPlugIn", "!(statusCode >= 100 && statusCode < 300) at doHttpGet");
                                                this.a.put("status", this.b);
                                                this.a.put("error", "http status is not in (1xx~2xx) ");
                                                c0101o2.b(this.a);
                                            }
                                            return 1;
                                        } catch (Exception e39) {
                                            try {
                                                ab.a("VponSDKPlugIn", "http_get throw Exception:" + e39.getMessage(), e39);
                                                this.a.put("error", "http_get throw Exception:" + e39.getMessage());
                                                this.a.put("status", this.b);
                                                c0101o2.b(this.a);
                                            } catch (Exception e40) {
                                            }
                                            return 1;
                                        }
                                    }
                                }.execute(new Object[0]);
                            } catch (Exception e39) {
                                ab.a("VponSDKPlugIn", "doHttpGet throw Exception:", e39);
                            }
                        }
                    });
                    c0101o.b();
                } else {
                    a(c0101o, "url format error!");
                }
            } catch (Exception e39) {
                e39.printStackTrace();
                ab.a("VponSDKPlugIn", "throw exception at doHttpGet Exception:" + e39.getMessage(), e39);
                try {
                    a(c0101o, "throw exception at doHttpGet" + e39.getMessage());
                } catch (JSONException e40) {
                    e40.printStackTrace();
                }
            }
            return true;
        }
        if ("ad_req".equals(str)) {
            b(jSONArray, c0101o);
            return true;
        }
        if ("get_sdk_params".equals(str)) {
            try {
                JSONObject jSONObjectG = ((E) this.cordova).g();
                ab.a("VponSDKPlugIn", "doGetSdkParams:" + jSONObjectG.toString(4));
                c0101o.a(jSONObjectG);
            } catch (Exception e41) {
                ab.a("VponSDKPlugIn", "throw exception at doGetSdkParams", e41);
                try {
                    a(c0101o, "doGetSdkParams return Exception:" + e41.getMessage());
                } catch (JSONException e42) {
                    ab.a("VponSDKPlugIn", "throw exception at doGetSdkParams e1:", e42);
                }
            }
            return true;
        }
        if ("use_custom_close".equals(str)) {
            g(jSONArray, c0101o);
            return true;
        }
        if ("put_data".equals(str)) {
            d(jSONArray, c0101o);
            return true;
        }
        if ("get_data".equals(str)) {
            e(jSONArray, c0101o);
            return true;
        }
        if ("remove_data".equals(str)) {
            f(jSONArray, c0101o);
            return true;
        }
        if ("can_open_urls".equals(str)) {
            c(jSONArray, c0101o);
            return true;
        }
        if ("get_id_payload".equals(str)) {
            a(c0101o);
            return true;
        }
        if (b(str)) {
            a(str, jSONArray, c0101o);
            return true;
        }
        if ("test".equals(str)) {
            a(c0101o, "Cannot support test now!!");
            return true;
        }
        ab.b("VponSDKPlugIn", "SDK: no action matched!");
        ab.b("VponSDKPlugIn", "-->>execute illegal action:" + str + " callbackId:" + c0101o.a() + " args:" + jSONArray.toString(4));
        JSONObject jSONObject17 = new JSONObject();
        jSONObject17.put("action", str);
        jSONObject17.put("e", "SDK: no action matched!");
        c0101o.b(jSONObject17);
        return true;
        e.printStackTrace();
        ab.a("VponSDKPlugIn", "throws exception at execute", e);
        a(c0101o, "throw Exception:" + e.getMessage());
        return false;
    }

    @Override // vpadn.C0103q
    public void onActivityResult(int i, int i2, Intent intent) {
        ab.b("VponSDKPlugIn", "Call onActivityResult requestCode:" + i + " resultCode:" + i2);
        if (600001 == i) {
            ab.b("VponSDKPlugIn", "return from 'openBrowser'");
        }
    }

    private static String a(JSONObject jSONObject) throws JSONException {
        if (!jSONObject.has("v_u")) {
            return null;
        }
        return jSONObject.getString("v_u");
    }

    private ar b(JSONObject jSONObject) throws JSONException {
        String string;
        String string2;
        String str;
        String str2;
        JSONObject jSONObject2;
        ar.a.EnumC0081a enumC0081a;
        int i;
        int i2;
        String strA = a(jSONObject);
        if (strA == null) {
            ab.b("VponSDKPlugIn", "videoUrl == null");
            return null;
        }
        if (jSONObject.has("p_t")) {
            string = jSONObject.getString("p_t");
            if (!string.equals("v") && !string.equals("vw")) {
                ab.b("VponSDKPlugIn", "protraitType format error");
                string = null;
            }
        } else {
            string = null;
        }
        if (jSONObject.has("l_t")) {
            string2 = jSONObject.getString("l_t");
            if (!string2.equals("v") && !string2.equals("vw")) {
                ab.b("VponSDKPlugIn", "landscapeType format error");
                string2 = null;
            }
        } else {
            string2 = null;
        }
        if (string == null || string2 == null || !string.equals("v") || !string2.equals("vw")) {
            str = string2;
            str2 = string;
        } else {
            str = "v";
            str2 = "v";
        }
        if (str2 == null && str == null) {
            ab.b("VponSDKPlugIn", "protraitType == null && landscapeType == null");
            return null;
        }
        ar arVar = new ar(strA);
        if (str2 != null && str != null && jSONObject.has("force_first_mode")) {
            String string3 = jSONObject.getString("force_first_mode");
            if (string3.equals("l") || string3.equals("p")) {
                if (string3.equals("l")) {
                    arVar.b(ar.b.LANDSCAPE);
                } else {
                    arVar.b(ar.b.PORTRAIT);
                }
            } else {
                ab.b("VponSDKPlugIn", "ForceFirstMode is error");
            }
        }
        if (str2 != null) {
            arVar.b(true);
            arVar.d(str2);
            String str3 = "top";
            if (jSONObject.has("p_v_pos")) {
                String string4 = jSONObject.getString("p_v_pos");
                if (string4.equals("top") || string4.equals("bottom") || string4.equals("middle")) {
                    str3 = string4;
                } else {
                    ab.b("VponSDKPlugIn", "portraitVideoPosition is not equals top bottom or middle");
                }
            }
            arVar.b(str3);
        }
        if (str != null) {
            arVar.a(true);
            arVar.e(str);
        }
        if ((str2 != null && str2.equals("vw")) || (str != null && str.equals("vw"))) {
            String string5 = jSONObject.has("w_u") ? jSONObject.getString("w_u") : null;
            if (string5 != null) {
                if (!string5.startsWith("http:") && !string5.startsWith("https:")) {
                    ab.b("VponSDKPlugIn", "webUrl format error");
                } else {
                    arVar.j(string5);
                }
            }
        }
        if (jSONObject.has("bk_c")) {
            arVar.c(jSONObject.getInt("bk_c"));
        }
        if (jSONObject.has("cd") && (i2 = jSONObject.getInt("cd")) > 0) {
            arVar.a(i2);
        }
        if (jSONObject.has("tracking_u")) {
            String string6 = jSONObject.getString("tracking_u");
            if (!string6.startsWith("http:") && !string6.startsWith("https:")) {
                ab.b("VponSDKPlugIn", "trackingUrl format error");
            } else {
                arVar.c(string6);
            }
        }
        if (jSONObject.has("tracking_interval") && (i = jSONObject.getInt("tracking_interval")) > 0) {
            arVar.b(i);
        }
        if (jSONObject.has("v_tracking")) {
            arVar.a(jSONObject.getJSONObject("v_tracking"));
        }
        if (jSONObject.has("replay_tracking_u")) {
            String string7 = jSONObject.getString("replay_tracking_u");
            if (!string7.startsWith("http:") && !string7.startsWith("https:")) {
                ab.b("VponSDKPlugIn", "replayTrackingUrl format error");
            } else {
                arVar.g(string7);
            }
        }
        if (jSONObject.has("brand_icon_u")) {
            String string8 = jSONObject.getString("brand_icon_u");
            if (!string8.startsWith("http:") && !string8.startsWith("https:")) {
                ab.b("VponSDKPlugIn", "brandIconUrl format error");
            } else {
                arVar.h(string8);
            }
        }
        if (jSONObject.has("brand_text")) {
            String string9 = jSONObject.getString("brand_text");
            if (!C0086a.c(string9)) {
                arVar.i(string9);
            } else {
                ab.b("VponSDKPlugIn", "brandText is blank");
            }
        }
        if (jSONObject.has("auto_close") && jSONObject.getInt("auto_close") == 1) {
            arVar.c(true);
        }
        if (jSONObject.has("ended_close_btn") && jSONObject.getInt("ended_close_btn") == 1) {
            arVar.d(true);
        }
        if (jSONObject.has("mute") && jSONObject.getInt("mute") == 0) {
            arVar.e(false);
        }
        if (jSONObject.has("use_cache") && jSONObject.getInt("use_cache") == 0) {
            arVar.g(false);
        }
        if (jSONObject.has("close_parent") && jSONObject.getInt("close_parent") == 1) {
            arVar.f(true);
        }
        if (jSONObject.has("btns")) {
            JSONArray jSONArray = jSONObject.getJSONArray("btns");
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                JSONObject jSONObject3 = jSONArray.getJSONObject(i3);
                ar.a aVar = new ar.a();
                if (jSONObject3.has("action")) {
                    String string10 = jSONObject3.getString("action");
                    if (as.a(string10)) {
                        if (!string10.equals("lin") || a()) {
                            aVar.a = jSONObject3.getString("action");
                            if (jSONObject3.has("btn_text")) {
                                String string11 = jSONObject3.getString("btn_text");
                                if (!C0086a.c(string11)) {
                                    aVar.f329c = string11;
                                    if (jSONObject3.has("app_u")) {
                                        aVar.d = jSONObject3.getString("app_u");
                                    }
                                    if (jSONObject3.has("btn_tracking_u")) {
                                        aVar.e = jSONObject3.getString("btn_tracking_u");
                                    }
                                    if (jSONObject3.has("launch_type")) {
                                        String string12 = jSONObject3.getString("launch_type");
                                        if (string12.equals("inapp")) {
                                            enumC0081a = ar.a.EnumC0081a.INAPP;
                                        } else if (string12.equals("outapp")) {
                                            enumC0081a = ar.a.EnumC0081a.OUTAPP;
                                        } else {
                                            ab.b("VponSDKPlugIn", "button launch type format error");
                                        }
                                        aVar.b = enumC0081a;
                                        if (jSONObject3.has("data") && (jSONObject2 = jSONObject3.getJSONObject("data")) != null) {
                                            aVar.f = new JSONObject(jSONObject2.toString());
                                        }
                                        arVar.a(aVar);
                                    } else {
                                        if (jSONObject3.has("data")) {
                                            aVar.f = new JSONObject(jSONObject2.toString());
                                        }
                                        arVar.a(aVar);
                                    }
                                } else {
                                    ab.b("VponSDKPlugIn", "btn_text is blank");
                                }
                            }
                        }
                    } else {
                        ab.b("VponSDKPlugIn", "Unsupport Action Type");
                    }
                }
            }
        }
        return arVar;
    }

    private boolean a() {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put("jp.naver.line.android");
        try {
            return a(jSONArray).getInt("jp.naver.line.android") == 1;
        } catch (JSONException e) {
            return false;
        }
    }

    public void doOpenWebAppStep2(final String str, final C0101o c0101o, final String str2, final String str3, final boolean z, final boolean z2, final String str4, final int i, final boolean z3, final boolean z4, final boolean z5) {
        this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.12
            @Override // java.lang.Runnable
            public final void run() {
                if (str.equals("bannerWebView")) {
                    if (!(VponSDKPlugIn.this.cordova instanceof D)) {
                        ab.b("VponSDKPlugIn", "cannot cast to VponBannerController");
                        try {
                            VponSDKPlugIn vponSDKPlugIn = VponSDKPlugIn.this;
                            VponSDKPlugIn.a(c0101o, "cannot cast to VponBannerController");
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    ((D) VponSDKPlugIn.this.cordova).a(c0101o, str2, str3, z, z2, str4, i, z3, z4, z5);
                    return;
                }
                if (!str.equals("InterstitialAdWebView(new Activity)")) {
                    try {
                        ab.b("VponSDKPlugIn", "webId is " + str + " at doOpenWebAppStep2");
                        VponSDKPlugIn vponSDKPlugIn2 = VponSDKPlugIn.this;
                        VponSDKPlugIn.a(c0101o, "webId is " + str + " at doOpenWebAppStep2");
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    /* renamed from: com.vpon.cordova.VponSDKPlugIn$14, reason: invalid class name */
    final class AnonymousClass14 implements Runnable {
        private final /* synthetic */ JSONArray b;

        /* renamed from: c, reason: collision with root package name */
        private final /* synthetic */ C0101o f275c;
        private final /* synthetic */ Context d;

        AnonymousClass14(JSONArray jSONArray, C0101o c0101o, Context context) {
            this.b = jSONArray;
            this.f275c = c0101o;
            this.d = context;
        }

        @Override // java.lang.Runnable
        public final void run() throws JSONException {
            try {
                JSONObject jSONObject = this.b.getJSONObject(0);
                if (!jSONObject.has("u")) {
                    ab.b("VponSDKPlugIn", "jsonObj.has(JSONParamConstant.URL) is false");
                    VponSDKPlugIn vponSDKPlugIn = VponSDKPlugIn.this;
                    VponSDKPlugIn.a(this.f275c, "jsonObj.has(JSONParamConstant.URL) is false");
                } else if (!URLUtil.isNetworkUrl(jSONObject.getString("u"))) {
                    ab.b("VponSDKPlugIn", "!URLUtil.isNetworkUrl(url)");
                    VponSDKPlugIn vponSDKPlugIn2 = VponSDKPlugIn.this;
                    VponSDKPlugIn.a(this.f275c, "jsonObj.has(JSONParamConstant.URL) is false");
                } else {
                    Configuration configuration = this.d.getResources().getConfiguration();
                    String str = "Store Picture";
                    String str2 = "Are You sure to add picture to photo album?";
                    String str3 = "Yes";
                    String str4 = "No";
                    if (configuration.locale.equals(Locale.TAIWAN) || configuration.locale.equals(Locale.TRADITIONAL_CHINESE)) {
                        str = "儲存圖片";
                        str2 = "確定將儲存圖片到手機的相簿？";
                        str3 = "同意";
                        str4 = "不同意";
                    } else if (configuration.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                        str = "储存图片";
                        str2 = "确定将储存图片到手机的相簿？";
                        str3 = "同意";
                        str4 = "不同意";
                    }
                    AlertDialog.Builder message = new AlertDialog.Builder(this.d).setTitle(str).setMessage(str2);
                    final JSONArray jSONArray = this.b;
                    final C0101o c0101o = this.f275c;
                    AlertDialog.Builder positiveButton = message.setPositiveButton(str3, new DialogInterface.OnClickListener() { // from class: com.vpon.cordova.VponSDKPlugIn.14.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            ExecutorService executorServiceE = VponSDKPlugIn.this.cordova.e();
                            final JSONArray jSONArray2 = jSONArray;
                            final C0101o c0101o2 = c0101o;
                            executorServiceE.execute(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.14.1.1
                                @Override // java.lang.Runnable
                                public final void run() throws JSONException {
                                    try {
                                        if (!VponSDKPlugIn.this.a(jSONArray2.getJSONObject(0).getString("u"))) {
                                            ab.b("VponSDKPlugIn", "storePicture return false");
                                            VponSDKPlugIn vponSDKPlugIn3 = VponSDKPlugIn.this;
                                            VponSDKPlugIn.a(c0101o2, "storePicture return false");
                                        } else {
                                            c0101o2.b();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        ab.b("VponSDKPlugIn", "throw exception at doStorePicture postion 2 exception:" + e.getMessage());
                                        try {
                                            VponSDKPlugIn vponSDKPlugIn4 = VponSDKPlugIn.this;
                                            VponSDKPlugIn.a(c0101o2, "throw exception at doStorePicture postion 2 exception:" + e.getMessage());
                                        } catch (JSONException e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });
                    final C0101o c0101o2 = this.f275c;
                    positiveButton.setNegativeButton(str4, new DialogInterface.OnClickListener() { // from class: com.vpon.cordova.VponSDKPlugIn.14.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            ab.a("VponSDKPlugIn", "pic cannot save!");
                            try {
                                VponSDKPlugIn vponSDKPlugIn3 = VponSDKPlugIn.this;
                                VponSDKPlugIn.a(c0101o2, "pic cannot save!");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ab.a("VponSDKPlugIn", "throw exception at doStorePicture Exception:" + e.getMessage(), e);
                try {
                    VponSDKPlugIn vponSDKPlugIn3 = VponSDKPlugIn.this;
                    VponSDKPlugIn.a(this.f275c, "throw exception at doStorePicture exception:" + e.getMessage());
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str) throws IOException {
        ab.b("VponSDKPlugIn", "call storePicture(urlStr)");
        try {
            if (!URLUtil.isNetworkUrl(str)) {
                ab.b("VponSDKPlugIn", "ERROR URL");
                return false;
            }
            URLConnection uRLConnectionOpenConnection = new URL(str).openConnection();
            uRLConnectionOpenConnection.connect();
            InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
            if (inputStream == null) {
                ab.b("VponSDKPlugIn", "inputStream == null");
                return false;
            }
            File randomFile = getRandomFile(uRLConnectionOpenConnection.getContentType());
            if (randomFile == null) {
                return false;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(randomFile);
            byte[] bArr = new byte[256];
            while (true) {
                int i = inputStream.read(bArr);
                if (i > 0) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    try {
                        inputStream.close();
                        fileOutputStream.close();
                        MediaStore.Images.Media.insertImage(this.cordova.a().getContentResolver(), randomFile.getAbsolutePath(), "VPON_AD_PIC", "VPON_AD_PIC_DES");
                        randomFile.delete();
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            ab.a("VponSDKPlugIn", "storePicture throws Exception:" + e2.getMessage(), e2);
            return false;
        }
    }

    public File getRandomFile(String str) {
        String str2 = ".jpg";
        if (!str.startsWith("image")) {
            return null;
        }
        if (str.contains("png")) {
            str2 = ".png";
        } else if (str.contains("gif")) {
            str2 = ".gif";
        }
        String string = UUID.randomUUID().toString();
        String path = Environment.getExternalStorageDirectory().getPath();
        ab.b("VponSDKPlugIn", path);
        File file = new File(path, "/vpon");
        file.mkdirs();
        return new File(file, "VPON-justin" + string + str2);
    }

    private void a(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        try {
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            String string = jSONObject.has("a") ? jSONObject.getString("a") : null;
            String string2 = jSONObject.has("type") ? jSONObject.getString("type") : null;
            Uri uri = jSONObject.has("u") ? Uri.parse(jSONObject.getString("u")) : null;
            JSONObject jSONObject2 = jSONObject.has("extras") ? jSONObject.getJSONObject("extras") : null;
            HashMap map = new HashMap();
            if (jSONObject2 != null) {
                JSONArray jSONArrayNames = jSONObject2.names();
                for (int i = 0; i < jSONArrayNames.length(); i++) {
                    String string3 = jSONArrayNames.getString(i);
                    map.put(string3, jSONObject2.getString(string3));
                }
            }
            JSONArray jSONArray2 = jSONObject.has("cats") ? jSONObject.getJSONArray("cats") : null;
            ArrayList arrayList = new ArrayList();
            if (jSONArray2 != null) {
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    arrayList.add(jSONArray2.getString(i2));
                }
            }
            JSONArray jSONArray3 = jSONObject.has("flags") ? jSONObject.getJSONArray("flags") : null;
            ArrayList arrayList2 = new ArrayList();
            if (jSONArray3 != null) {
                for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                    arrayList2.add(Integer.valueOf(jSONArray3.getInt(i3)));
                }
            }
            Intent intent = uri != null ? new Intent(string, uri) : new Intent(string);
            if (string2 != null && uri != null) {
                intent.setDataAndType(uri, string2);
            } else if (string2 != null) {
                intent.setType(string2);
            }
            for (String str : map.keySet()) {
                String str2 = (String) map.get(str);
                if (str.equals("android.intent.extra.TEXT") && string2.equals("text/html")) {
                    intent.putExtra(str, Html.fromHtml(str2));
                } else if (str.equals("android.intent.extra.STREAM")) {
                    intent.putExtra(str, Uri.parse(str2));
                } else if (str.equals("android.intent.extra.EMAIL")) {
                    intent.putExtra("android.intent.extra.EMAIL", new String[]{str2});
                } else {
                    intent.putExtra(str, str2);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                intent.addCategory((String) it.next());
            }
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                intent.addFlags(((Integer) it2.next()).intValue());
            }
            this.cordova.a().startActivity(intent);
            if (this.webView instanceof VponAdWebView) {
                VponAdWebView vponAdWebView = (VponAdWebView) this.webView;
                if (vponAdWebView.h().equals("bannerWebView")) {
                    if (this.cordova instanceof D) {
                        D d = (D) this.cordova;
                        d.u();
                        d.v();
                    }
                } else if ((vponAdWebView.h().equals("SdkOpenWebApp") || vponAdWebView.h().equals("InterstitialAdWebView(new Activity)") || vponAdWebView.h().equals("videoWebView")) && (this.cordova instanceof E)) {
                    ((E) this.cordova).l();
                }
            } else {
                a(c0101o, "something error [webView instanceof VponAdWebView is false]");
            }
            c0101o.b();
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at openIntent Exception:" + e.getMessage(), e);
            try {
                a(c0101o, "throw exception at openIntent Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void b(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        String string;
        try {
            S.a().b();
            if (this.webView instanceof VponAdWebView) {
                VponAdWebView vponAdWebView = (VponAdWebView) this.webView;
                if (!vponAdWebView.h().equals("init") && !vponAdWebView.h().equals("init-finish")) {
                    ab.b("VponSDKPlugIn", "CALL doAdReq from VponWebView ID:" + vponAdWebView.h());
                    a(c0101o, "CALL doAdReq from VponWebView ID:" + vponAdWebView.h());
                    return;
                }
            }
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            if (!jSONObject.has("u")) {
                string = null;
            } else {
                string = jSONObject.getString("u");
            }
            if (C0086a.c(string)) {
                a(c0101o, "Cannot get url!");
                return;
            }
            if (!string.toLowerCase().startsWith("http://") && !string.toLowerCase().startsWith("https://")) {
                a(c0101o, "url format error!");
                return;
            }
            ab.a("VponSDKPlugIn", "doAdReq http url:" + string);
            if (this.cordova instanceof E) {
                this.cordova.a().runOnUiThread(new AnonymousClass2(string, c0101o, (E) this.cordova));
                c0101o.b();
                return;
            }
            a(c0101o, "cordova instanceof VponControllerInterface is false [doAdReq]");
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at doAdReq Exception:" + e.getMessage(), e);
            try {
                a(c0101o, "throw exception at doAdReq Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    /* renamed from: com.vpon.cordova.VponSDKPlugIn$2, reason: invalid class name */
    final class AnonymousClass2 implements Runnable {
        private final /* synthetic */ String b;

        /* renamed from: c, reason: collision with root package name */
        private final /* synthetic */ C0101o f278c;
        private final /* synthetic */ E d;

        AnonymousClass2(String str, C0101o c0101o, E e) {
            this.b = str;
            this.f278c = c0101o;
            this.d = e;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [com.vpon.cordova.VponSDKPlugIn$2$1] */
        @Override // java.lang.Runnable
        public final void run() {
            try {
                final String str = this.b;
                final C0101o c0101o = this.f278c;
                final E e = this.d;
                new AsyncTask<Object, Integer, Integer>() { // from class: com.vpon.cordova.VponSDKPlugIn.2.1
                    private JSONObject a = new JSONObject();
                    private int b = -1;

                    @Override // android.os.AsyncTask
                    protected final /* synthetic */ Integer doInBackground(Object... objArr) {
                        return a();
                    }

                    private Integer a() throws Throwable {
                        try {
                            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                            C0086a.a(defaultHttpClient);
                            C0086a.a(str, defaultHttpClient);
                            HttpClientParams.setRedirecting(defaultHttpClient.getParams(), false);
                            Object objA = P.a().a("user-agent");
                            if (objA != null) {
                                ab.c("VponSDKPlugIn", "userAgent:" + objA);
                                defaultHttpClient.getParams().setParameter("http.useragent", objA);
                            } else {
                                ab.b("VponSDKPlugIn", "Cannot get user agent from StaticStorage.instance().get(StaticStorage.USER_AGENT)");
                            }
                            HttpResponse httpResponseExecute = defaultHttpClient.execute(new HttpGet(str));
                            C0086a.b(str, defaultHttpClient);
                            this.b = httpResponseExecute.getStatusLine().getStatusCode();
                            ab.c("VponSDKPlugIn", "HTTP-STATUS-CODE:" + this.b);
                            if (this.b != 302 && this.b != 301 && this.b != 303) {
                                this.a.put("status", this.b);
                                this.a.put("e", "http status is not 301~303 ");
                                ab.b("VponSDKPlugIn", "do doAdReq return status code:" + this.b);
                                if (httpResponseExecute.containsHeader("Vpadn-Status-Code")) {
                                    ab.b("VponSDKPlugIn", "doAdReq return error status code:" + httpResponseExecute.getFirstHeader("Vpadn-Status-Code").getValue());
                                }
                                if (httpResponseExecute.containsHeader("Vpadn-Status")) {
                                    VponSDKPlugIn.this.a = httpResponseExecute.getFirstHeader("Vpadn-Status").getValue();
                                    ab.b("VponSDKPlugIn", "doAdReq return error status:" + VponSDKPlugIn.this.a);
                                } else {
                                    VponSDKPlugIn.this.a = null;
                                }
                                if (httpResponseExecute.containsHeader("Vpadn-Status-Desc")) {
                                    ab.b("VponSDKPlugIn", "doAdReq return error status description:" + httpResponseExecute.getFirstHeader("Vpadn-Status-Desc").getValue());
                                }
                                S.a().b(httpResponseExecute, false);
                                VponSDKPlugIn.this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.2.1.1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        if (VponSDKPlugIn.this.webView != null) {
                                            VponSDKPlugIn.this.webView.a("load_banner_fail", (Object) VponSDKPlugIn.this.a);
                                        }
                                    }
                                });
                                c0101o.b(this.a);
                                return 1;
                            }
                            S.a().a(httpResponseExecute, false);
                            if (httpResponseExecute.containsHeader("Vpadn-Refresh-Time")) {
                                String value = httpResponseExecute.getFirstHeader("Vpadn-Refresh-Time").getValue();
                                ab.c("VponSDKPlugIn", "refreshTime:" + value);
                                if (value != null) {
                                    try {
                                        e.a(Integer.valueOf(value).intValue());
                                    } catch (NumberFormatException e2) {
                                        e2.printStackTrace();
                                    }
                                } else {
                                    ab.b("VponSDKPlugIn", "Cannot get banner refreshSecond");
                                }
                            } else {
                                ab.b("VponSDKPlugIn", "Cannot get banner refreshSecond (cannot find field in header)");
                            }
                            if (httpResponseExecute.containsHeader("Location")) {
                                String value2 = httpResponseExecute.getFirstHeader("Location").getValue();
                                ab.c("VponSDKPlugIn", "bannerUrl:" + value2);
                                if (value2 != null) {
                                    e.a(value2);
                                } else {
                                    ab.b("VponSDKPlugIn", "Cannot get banner URL");
                                }
                            } else {
                                ab.b("VponSDKPlugIn", "Cannot get banner URL (cannot find field in header)");
                            }
                            if (httpResponseExecute.containsHeader("Vpadn-Imp")) {
                                String value3 = httpResponseExecute.getFirstHeader("Vpadn-Imp").getValue();
                                ab.c("VponSDKPlugIn", "impressionUrl:" + value3);
                                if (value3 != null) {
                                    e.c(value3);
                                } else {
                                    ab.b("VponSDKPlugIn", "Cannot get impression URL");
                                }
                            } else {
                                ab.b("VponSDKPlugIn", "Cannot get impression URL (cannot find field in header)");
                            }
                            if (httpResponseExecute.containsHeader("Vpadn-Clk")) {
                                String value4 = httpResponseExecute.getFirstHeader("Vpadn-Clk").getValue();
                                ab.c("VponSDKPlugIn", "clickUrl:" + value4);
                                if (value4 != null) {
                                    e.b(value4);
                                } else {
                                    ab.b("VponSDKPlugIn", "Cannot get click URL");
                                }
                            } else {
                                ab.b("VponSDKPlugIn", "Cannot get click URL (cannot find field in header)");
                            }
                            this.a.put("status", this.b);
                            c0101o.a(this.a);
                            VponSDKPlugIn.this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.2.1.2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    VponSDKPlugIn.this.webView.a("load_banner", (Object) null);
                                }
                            });
                            return 1;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            try {
                                ab.a("VponSDKPlugIn", "doAdReq throw Exception:" + e3.getMessage(), e3);
                                this.a.put("e", "doAdReq throw Exception:" + e3.getMessage());
                                c0101o.b(this.a);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                            return 1;
                        }
                    }
                }.execute(new Object[0]);
            } catch (Exception e2) {
                ab.a("VponSDKPlugIn", "doAdReq throw Exception:", e2);
            }
        }
    }

    private void a(C0101o c0101o) throws JSONException {
        try {
            String string = N.a().d(this.cordova.a(), (JSONObject) null).toString();
            ab.a("VponSDKPlugIn", "secretJsonObjStrWithoutLocation:" + string);
            String strA = C0086a.a("NH/mLeyCBfokzYKUPNGEEg==", string);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", strA);
            ab.a("VponSDKPlugIn", "doGetIdPayload ret:" + jSONObject.toString(4));
            c0101o.a(jSONObject);
        } catch (Exception e) {
            ab.a("VponSDKPlugIn", "throw exception at doGetIdPayload", e);
            try {
                a(c0101o, "doGetIdPayload return Exception:" + e.getMessage());
            } catch (JSONException e2) {
                ab.a("VponSDKPlugIn", "throw exception at doGetIdPayload e1:", e2);
            }
        }
    }

    private void c(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        Integer num;
        try {
            ab.a("VponSDKPlugIn", "Enter doCanOpenUrl");
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            if (jSONObject != null && jSONObject.has("urls")) {
                JSONObject jSONObjectA = a(jSONObject.getJSONArray("urls"));
                JSONArray jSONArray2 = new JSONArray();
                Iterator<String> itKeys = jSONObjectA.keys();
                while (itKeys.hasNext()) {
                    JSONObject jSONObject2 = new JSONObject();
                    String next = itKeys.next();
                    try {
                        num = (Integer) jSONObjectA.get(next);
                    } catch (JSONException e) {
                        num = 0;
                    }
                    jSONObject2.put("url", next);
                    jSONObject2.put("result", num);
                    jSONArray2.put(jSONObject2);
                }
                c0101o.a(jSONArray2);
                return;
            }
            try {
                a(c0101o, "doCanOpenUrls format error");
            } catch (JSONException e2) {
                ab.a("VponSDKPlugIn", "throw exception at doCanOpenUrls 1", e2);
            }
        } catch (Exception e3) {
            ab.a("VponSDKPlugIn", "throw exception at doCanOpenUrls 2", e3);
            try {
                a(c0101o, "doCanOpenUrls return Exception:" + e3.getMessage());
            } catch (JSONException e4) {
                ab.a("VponSDKPlugIn", "throw exception at doCanOpenUrls 3", e4);
            }
        }
    }

    private JSONObject a(JSONArray jSONArray) throws JSONException, PackageManager.NameNotFoundException {
        JSONObject jSONObject = new JSONObject();
        for (int i = 0; i < jSONArray.length(); i++) {
            String string = null;
            try {
                string = jSONArray.getString(i);
            } catch (JSONException e) {
            }
            if (string != null) {
                try {
                    this.cordova.a().getPackageManager().getPackageInfo(string, 1);
                    jSONObject.put(string, 1);
                } catch (PackageManager.NameNotFoundException e2) {
                    try {
                        jSONObject.put(string, 0);
                    } catch (JSONException e3) {
                    }
                } catch (JSONException e4) {
                }
            }
        }
        return jSONObject;
    }

    private void d(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        try {
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            String string = null;
            if (jSONObject.has("k")) {
                string = jSONObject.getString("k");
            }
            if (C0086a.c(string)) {
                a(c0101o, "Cannot get key at JSON of put_data!");
            } else {
                P.a().a(string, new JSONObject(jSONObject.toString()));
                c0101o.b();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at doPutData", e);
            try {
                a(c0101o, "doPutData return Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void e(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        try {
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            String string = null;
            if (jSONObject.has("k")) {
                string = jSONObject.getString("k");
            }
            if (C0086a.c(string)) {
                a(c0101o, "Cannot get key at JSON of get_data!");
                return;
            }
            JSONObject jSONObjectD = P.a().d(string);
            if (jSONObjectD != null) {
                c0101o.a(jSONObjectD);
            } else {
                a(c0101o, "Cannot get value by the key:" + string);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at doGetData", e);
            try {
                a(c0101o, "doGetData return Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void f(JSONArray jSONArray, C0101o c0101o) throws JSONException {
        try {
            JSONObject jSONObject = jSONArray.getJSONObject(0);
            String string = null;
            if (jSONObject.has("k")) {
                string = jSONObject.getString("k");
            }
            if (C0086a.c(string)) {
                a(c0101o, "Cannot get key at JSON of remove_data!");
            } else {
                P.a().e(string);
                c0101o.b();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at doRemoveData", e);
            try {
                a(c0101o, "doRemoveData return Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void g(JSONArray jSONArray, final C0101o c0101o) {
        try {
            final boolean z = jSONArray.getJSONObject(0).getInt("custom_close") != 0;
            this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.5
                @Override // java.lang.Runnable
                public final void run() {
                    ((E) VponSDKPlugIn.this.cordova).a(z);
                    c0101o.b();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ab.a("VponSDKPlugIn", "throw exception at doSetCustomClose", e);
            try {
                a(c0101o, "doSetCustomClose return Exception:" + e.getMessage());
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private boolean b() throws PackageManager.NameNotFoundException {
        try {
            this.cordova.a().getPackageManager().getPackageInfo("com.google.android.youtube", 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static boolean b(String str) {
        for (int i = 0; i < B.a.length; i++) {
            if (B.a[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void a(final String str, final JSONArray jSONArray, final C0101o c0101o) {
        try {
            if ((this.webView instanceof VponAdWebView) && !((VponAdWebView) this.webView).h().equals("videoWebView")) {
                ab.b("VponSDKPlugIn", "only VponWebViewConstant.VIDEO_WEBVIEW webview allow to call native video player action");
                a(c0101o, "only VponWebViewConstant.VIDEO_WEBVIEW webview allow to call native video player action");
            } else {
                this.cordova.a().runOnUiThread(new Runnable() { // from class: com.vpon.cordova.VponSDKPlugIn.6
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((E) VponSDKPlugIn.this.cordova).a(str, jSONArray, c0101o);
                    }
                });
            }
        } catch (Exception e) {
            ab.a("VponSDKPlugIn", "throws exception in doControlNativeVideoPlayerAction", e);
            try {
                a(c0101o, "throws exception in doControlNativeVideoPlayerAction exception:" + e.getMessage());
            } catch (JSONException e2) {
                ab.a("VponSDKPlugIn", "doControlNativeVideoPlayerAction throws Exception", e2);
            }
        }
    }
}
