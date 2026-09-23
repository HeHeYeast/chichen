package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import c.NetworkManager;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class r implements LocationListener {

    /* renamed from: c, reason: collision with root package name */
    private static r f303c = new r();
    private static /* synthetic */ int[] u;
    List b;
    private String d = "android";
    private String e = "";
    private String f = "";
    private String g = "";
    private String h = "";
    private String i = "wifi";
    private String j = "";
    private String k = "";
    private String l = "";
    private String m = "";
    private Boolean n = true;
    private float o = BitmapDescriptorFactory.HUE_RED;
    private int p = 0;
    private int q = 0;
    LocationManager a = null;
    private double r = 0.0d;
    private double s = 0.0d;
    private boolean t = false;

    private r() {
    }

    static int a(int i) {
        return (int) ((i * f303c.o) + 0.5f);
    }

    static int a(JSONObject jSONObject, String str) throws y {
        try {
            return jSONObject.getInt(str);
        } catch (JSONException e) {
            new StringBuilder("getJsonIntValueFromKey JSON parce error. value:").append(str);
            x.b("SDK API Message", "PARSE INT");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    static String a(Activity activity, AdOrientation adOrientation, Rect rect) throws JSONException, y {
        String strA;
        JSONObject jSONObjectE = e();
        try {
            JSONObject jSONObject = jSONObjectE.getJSONObject("result");
            switch (g()[adOrientation.ordinal()]) {
                case 2:
                    strA = "p";
                    break;
                case 3:
                    strA = "l";
                    break;
                default:
                    strA = a((Context) activity);
                    break;
            }
            jSONObject.put("rotation", strA);
            jSONObject.put("top", rect.top);
            jSONObject.put("left", rect.left);
            jSONObject.put(FluctConstants.XML_NODE_WIDTH, rect.right);
            jSONObject.put(FluctConstants.XML_NODE_HEIGHT, rect.bottom);
            jSONObjectE.put("status", "succeed");
            return jSONObjectE.toString();
        } catch (JSONException e) {
            new StringBuilder("getDisplayOrientationInfoToJSonString:").append(e.getMessage());
            x.b("SDK API Message", "DM");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    private static String a(Context context) {
        return context.getResources().getConfiguration().orientation == 1 ? "p" : context.getResources().getConfiguration().orientation == 2 ? "l" : "";
    }

    static r a() {
        return f303c;
    }

    static final void a(Activity activity, String str) {
        if (a(str)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            if (activity != null) {
                activity.startActivity(intent);
            } else {
                intent.setFlags(268435456);
                ImobileSdkAd.a().startActivity(intent);
            }
        }
    }

    static final void a(String str, String str2, e eVar) {
        Executors.newCachedThreadPool().submit(new u(Executors.newCachedThreadPool().submit(new t(str)), str2, eVar));
    }

    static final boolean a(String str) {
        return ImobileSdkAd.a().getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(new StringBuilder(String.valueOf(str)).append("://").toString())), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED).size() > 0;
    }

    static final boolean a(JSONObject jSONObject) {
        try {
            return k.a(jSONObject, null).a().booleanValue();
        } catch (y e) {
            x.a(e);
            return false;
        }
    }

    static final int b(Activity activity) {
        boolean z;
        int i;
        if (a((Context) activity).equals("p")) {
            z = true;
            i = 1;
        } else {
            if (!a((Context) activity).equals("l")) {
                return 0;
            }
            z = false;
            i = 0;
        }
        int rotation = ((WindowManager) ImobileSdkAd.a().getSystemService("window")).getDefaultDisplay().getRotation();
        if (Build.VERSION.SDK_INT <= 8) {
            return i;
        }
        switch (rotation) {
            case 0:
                break;
            case 1:
                if (z) {
                    break;
                }
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return 0;
    }

    static String b() {
        ConnectivityManager connectivityManager;
        if (ImobileSdkAd.a().checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1 || (connectivityManager = (ConnectivityManager) ImobileSdkAd.a().getSystemService("connectivity")) == null) {
            return NetworkManager.MOBILE;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) ? "" : activeNetworkInfo.getTypeName().toLowerCase(Locale.getDefault());
    }

    static String b(JSONObject jSONObject, String str) throws y {
        try {
            return jSONObject.getString(str);
        } catch (JSONException e) {
            new StringBuilder("getJsonStringValueFromKey JSON parce error. value:").append(str);
            x.b("SDK API Message", "PARSE STRING");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    static final void b(Activity activity, String str) {
        try {
            activity.startActivity(ImobileSdkAd.a().getPackageManager().getLaunchIntentForPackage(str));
        } catch (Exception e) {
            new StringBuilder("Applicaiotn Start faild. applicaiotnID:").append(str).append(" message:").append(e.getMessage());
            x.b("SDK API Message", "Application not installed.");
        }
    }

    static JSONObject c(String str) throws y {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            new StringBuilder("getJsonObjectFromJsonString JSON parce error. value:").append(str);
            x.b("SDK API Message", "PARSE");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    static JSONObject e() throws JSONException, y {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("status", "faild");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("code", "");
            jSONObject2.put("message", "");
            jSONObject.put("error", jSONObject2);
            jSONObject.put("result", new JSONObject());
            return jSONObject;
        } catch (JSONException e) {
            new StringBuilder("getJsonBaseForHtml JSON parce error. value:").append(e.getMessage());
            x.b("SDK API Message", "BASE PARSE");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    private static /* synthetic */ int[] g() {
        int[] iArr = u;
        if (iArr == null) {
            iArr = new int[AdOrientation.valuesCustom().length];
            try {
                iArr[AdOrientation.AUTO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdOrientation.LANDSCAPE.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AdOrientation.PORTRAIT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            u = iArr;
        }
        return iArr;
    }

    final void a(Activity activity) {
        if (this.t) {
            return;
        }
        Context contextA = ImobileSdkAd.a();
        this.e = contextA.getPackageName();
        this.f = "V1.3";
        this.g = Locale.getDefault().getLanguage();
        this.h = Build.VERSION.RELEASE;
        this.i = b();
        this.j = Build.BRAND;
        this.k = Build.DEVICE;
        new Thread(new s(this, contextA)).start();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.o = displayMetrics.density;
        Point point = new Point(0, 0);
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        ((WindowManager) activity.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics2);
        if (a((Context) activity).equals("l")) {
            point.x = displayMetrics2.heightPixels;
            point.y = displayMetrics2.widthPixels;
        } else {
            point.x = displayMetrics2.widthPixels;
            point.y = displayMetrics2.heightPixels;
        }
        this.p = point.x;
        this.q = point.y;
        this.n = Boolean.valueOf((activity.getWindow().getAttributes().flags & 1024) == 0);
        c();
        this.t = true;
        x.b("SDK API Message", "Sdk api init complete.");
    }

    final void a(Uri.Builder builder) {
        builder.appendQueryParameter("spt", this.d);
        builder.appendQueryParameter("appid", this.e);
        builder.appendQueryParameter("sdkv", this.f);
        builder.appendQueryParameter("lang", this.g);
        builder.appendQueryParameter("os", this.h);
        builder.appendQueryParameter("nk", this.i);
        builder.appendQueryParameter("dvbrand", this.j);
        builder.appendQueryParameter("dvname", this.k);
        if (this.m != null && !this.m.equals("")) {
            builder.appendQueryParameter("gaid", this.m);
        }
        builder.appendQueryParameter("dpr", Float.toString(this.o));
        builder.appendQueryParameter("dpw", Integer.toString(this.p));
        builder.appendQueryParameter("dph", Integer.toString(this.q));
        if (this.r <= 0.0d || this.s <= 0.0d) {
            return;
        }
        builder.appendQueryParameter("lat", Double.toString(this.r));
        builder.appendQueryParameter("lng", Double.toString(this.s));
    }

    final boolean b(String str) {
        if (this.b == null) {
            this.b = new ArrayList();
            Iterator<ApplicationInfo> it = ImobileSdkAd.a().getPackageManager().getInstalledApplications(0).iterator();
            while (it.hasNext()) {
                this.b.add(it.next().packageName);
            }
        }
        return this.b.indexOf(str) >= 0;
    }

    final void c() {
        String str;
        String str2;
        if (this.a != null) {
            return;
        }
        this.a = (LocationManager) ImobileSdkAd.a().getSystemService("location");
        if (ImobileSdkAd.a().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == -1) {
            x.a(null);
            str = "";
        } else {
            str = this.a.isProviderEnabled("gps") ? "gps" : "";
        }
        if (ImobileSdkAd.a().checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == -1) {
            x.a(null);
            str2 = str;
        } else {
            str2 = (this.a.isProviderEnabled("network") && str.equals("")) ? "network" : str;
        }
        if (str2.equals("")) {
            x.a(null);
        } else {
            this.a.requestLocationUpdates(str2, 0L, BitmapDescriptorFactory.HUE_RED, this);
        }
    }

    final void d() {
        if (this.a != null) {
            this.a.removeUpdates(this);
            this.r = 0.0d;
            this.s = 0.0d;
            this.a = null;
        }
    }

    final String f() throws JSONException, y {
        String strA;
        JSONObject jSONObjectE = e();
        try {
            JSONObject jSONObject = jSONObjectE.getJSONObject("result");
            jSONObject.put("spt", this.d);
            jSONObject.put("appid", this.e);
            jSONObject.put("sdkv", this.f);
            jSONObject.put("lang", this.g);
            jSONObject.put("os", this.h);
            jSONObject.put("nk", this.i);
            jSONObject.put("dvbrand", this.j);
            jSONObject.put("dvname", this.k);
            switch (g()[ImobileSdkAd.d().ordinal()]) {
                case 2:
                    strA = "p";
                    break;
                case 3:
                    strA = "l";
                    break;
                default:
                    strA = a(ImobileSdkAd.a());
                    break;
            }
            jSONObject.put("rotation", strA);
            jSONObject.put("deviceid", this.l);
            jSONObject.put("advertisingid", this.m);
            jSONObject.put("statusbar", this.n);
            jSONObject.put("dpr", Float.toString(this.o));
            jSONObject.put("dpw", Integer.toString(this.p));
            jSONObject.put("dph", Integer.toString(this.q));
            jSONObject.put("lat", Double.toString(this.r));
            jSONObject.put("lng", Double.toString(this.s));
            jSONObjectE.put("status", "succeed");
            return jSONObjectE.toString();
        } catch (JSONException e) {
            new StringBuilder("getDeviceInfoJsonForHtml JSON parce error. value:").append(e.getMessage());
            x.b("SDK API Message", "DEVICE PARSE");
            throw new y(FailNotificationReason.RESPONSE);
        }
    }

    @Override // android.location.LocationListener
    public final void onLocationChanged(Location location) {
        this.r = location.getLatitude();
        this.s = location.getLongitude();
        new StringBuilder("Location get lat:").append(location.getLatitude()).append(" lng:").append(location.getLongitude());
        x.a(null);
    }

    @Override // android.location.LocationListener
    public final void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
