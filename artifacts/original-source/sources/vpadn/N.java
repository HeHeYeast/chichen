package vpadn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import com.vpadn.ads.VpadnAdSize;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class N {
    private static N a = new N();
    private String b = "00000000-0000-0000-0000-000000000000";

    /* renamed from: c, reason: collision with root package name */
    private boolean f312c = false;

    private N() {
    }

    public static N a() {
        return a;
    }

    public final JSONObject a(Context context, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        return o(j(p(f(q(r(t(a(s(jSONObject, context), context), context), context), context)), context), context), context);
    }

    public final JSONObject b(Context context, JSONObject jSONObject) {
        return c(j(d(k(l(e(n(m(o(p(f(q(r(t(a(s(new JSONObject(), context), context), context), context), context)), context), context), context), context)), context), context)), context));
    }

    public final JSONObject c(Context context, JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        a(context);
        return b(a(b(c(d(e(f(g(h(i(jSONObject2, context), context), context), context), context), context), context), context)));
    }

    public final JSONObject d(Context context, JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        a(context);
        return b(a(e(f(g(h(i(jSONObject2, context), context), context), context), context)));
    }

    private synchronized String b() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(String str) {
        ab.a("DeviceDataCollector", "set advertisingId:" + str);
        this.b = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void c() {
        this.f312c = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void d() {
        this.f312c = false;
    }

    private synchronized boolean e() {
        return this.f312c;
    }

    private void a(final Context context) {
        new Thread(new Runnable() { // from class: vpadn.N.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    ab.a("DeviceDataCollector", "createThreadToGetAdvertisingId");
                    T tB = C0086a.b(context);
                    N.this.a(tB.a);
                    if (tB.b) {
                        N.this.c();
                    } else {
                        N.this.d();
                    }
                } catch (Exception e) {
                    ab.a("DeviceDataCollector", "createThreadToGetAdvertisingId return Exception", e);
                }
            }
        }).start();
    }

    private JSONObject a(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                ab.a("DeviceDataCollector", "addAdvertisingId throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("advertising_id", b());
        } catch (Exception e3) {
            e = e3;
            ab.a("DeviceDataCollector", "addAdvertisingId throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private JSONObject b(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                ab.a("DeviceDataCollector", "addLimitAdTracking throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("limit_ad_tracking", e());
        } catch (Exception e3) {
            e = e3;
            ab.a("DeviceDataCollector", "addLimitAdTracking throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject b(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addLac throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("lac", C0086a.o(context));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addLac throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject c(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addAcceptAdFormat throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("output", "html");
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addAcceptAdFormat throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject c(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addCellId throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("cell_id", C0086a.n(context));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addCellId throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject d(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addLatLonAccuracy throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            Location locationA = ad.a(context).a();
            if (locationA != null) {
                jSONObject2.put("lat", locationA.getLatitude());
                jSONObject2.put("lon", locationA.getLongitude());
                jSONObject2.put("loc_acc", locationA.getAccuracy());
            } else {
                ab.d("DeviceDataCollector", "VponLocation.instance(context).getLocation() return null");
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addLatLonAccuracy throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject e(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addBSSId throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            String strF = C0086a.f(context);
            if (strF != null) {
                jSONObject2.put("bssid", strF.replaceAll(":", "").toUpperCase());
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addBSSId throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject f(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addAndroidId throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("and_id", C0086a.h(context));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addAndroidId throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject g(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addOpenUdid throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("open_udid", C0086a.i(context));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addOpenUdid throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject h(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addMacAddress throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            String strE = C0086a.e(context);
            if (strE != null) {
                jSONObject2.put("mac", strE.replaceAll(":", "").toUpperCase());
            } else {
                ab.d("DeviceDataCollector", " VponDevice.getMac(context) is null");
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addMacAddress throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject i(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addIMEI throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("imei", C0086a.d(context));
            ab.a("DeviceDataCollector", "IMEI:" + jSONObject2.toString(4));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addIMEI throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ba A[Catch: Exception -> 0x00de, TryCatch #0 {Exception -> 0x00de, blocks: (B:5:0x000b, B:9:0x0021, B:10:0x0026, B:12:0x002c, B:13:0x0031, B:15:0x0037, B:16:0x003c, B:20:0x004b, B:21:0x0050, B:22:0x005c, B:25:0x0062, B:26:0x006c, B:28:0x007b, B:32:0x0084, B:33:0x0089, B:35:0x008f, B:36:0x0094, B:38:0x009d, B:40:0x00a3, B:42:0x00b1, B:44:0x00ba, B:45:0x00bf, B:60:0x012f, B:49:0x00d6, B:54:0x00fe, B:55:0x0107, B:56:0x0110, B:57:0x0119, B:58:0x0123), top: B:65:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static org.json.JSONObject j(org.json.JSONObject r8, android.content.Context r9) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: vpadn.N.j(org.json.JSONObject, android.content.Context):org.json.JSONObject");
    }

    private static JSONObject d(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addIsRunInSimulator throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            ab.c("DeviceDataCollector", "android.os.Build.PRODUCT:" + Build.PRODUCT);
            if (Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk")) {
                jSONObject2.put("simulator", 1);
            } else {
                jSONObject2.put("simulator", 0);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addIsRunInSimulator throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject k(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addAppName throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("app_name", String.valueOf(C0086a.b()) + ".android." + ((Activity) context).getPackageName());
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addAppName throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject l(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addPackageName throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("msid", ((Activity) context).getPackageName());
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addPackageName throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject e(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addOutputFomat throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("format", "html");
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addOutputFomat throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject m(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addMncMCC throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            String strL = C0086a.l(context);
            String strJ = C0086a.j(context);
            jSONObject2.put("n_mnc", strL);
            jSONObject2.put("n_mcc", strJ);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addMncMCC throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject n(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addSimMncMCC throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            String strM = C0086a.m(context);
            String strK = C0086a.k(context);
            jSONObject2.put("mnc", strM);
            jSONObject2.put("mcc", strK);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addSimMncMCC throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject o(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addOsVersion throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("os_v", C0086a.b());
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addOsVersion throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject p(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addDeviceOrientation throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            int i = context.getResources().getConfiguration().orientation;
            if (i == 3) {
                i = 0;
            }
            if (2 == i) {
                jSONObject2.put("u_o", 2);
            } else {
                jSONObject2.put("u_o", 1);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addDeviceOrientation throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject f(JSONObject jSONObject) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addSDKVersion throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("sdk", "vpadn-sdk-a-v4.2.5");
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addSDKVersion throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject q(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addNetworkType throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            jSONObject2.put("ni", C0086a.p(context));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addNetworkType throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    private static JSONObject r(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addDeviceLanguage throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            Configuration configuration = context.getResources().getConfiguration();
            if (configuration.locale.equals(Locale.TAIWAN) || configuration.locale.equals(Locale.TRADITIONAL_CHINESE)) {
                jSONObject2.put("lang", "zh_TW");
            } else if (configuration.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                jSONObject2.put("lang", "zh_CN");
            } else {
                jSONObject2.put("lang", "en_US");
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addDeviceLanguage throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    @SuppressLint({"NewApi"})
    private static JSONObject s(JSONObject jSONObject, Context context) throws JSONException {
        Exception exc;
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        int i;
        int i2;
        if (jSONObject == null) {
            try {
                jSONObject3 = new JSONObject();
            } catch (Exception e) {
                exc = e;
                jSONObject2 = jSONObject;
                exc.printStackTrace();
                ab.a("DeviceDataCollector", "addScreenWidthAndHeight throw Exception:" + exc.getMessage(), exc);
                return jSONObject2;
            }
        } else {
            jSONObject3 = jSONObject;
        }
        try {
            int i3 = Build.VERSION.SDK_INT;
            ab.c("DeviceDataCollector", "currentApiVersion:" + i3);
            if (i3 >= 17) {
                Point point = new Point();
                ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealSize(point);
                i = point.x;
                i2 = point.y;
            } else {
                i = C0086a.g(context).widthPixels;
                i2 = C0086a.g(context).heightPixels;
            }
            int iRound = Math.round(VpadnAdSize.convertPixelsToDp(i, context));
            int iRound2 = Math.round(VpadnAdSize.convertPixelsToDp(i2, context));
            jSONObject3.put("s_w", iRound);
            jSONObject3.put("s_h", iRound2);
            return jSONObject3;
        } catch (Exception e2) {
            jSONObject2 = jSONObject3;
            exc = e2;
            exc.printStackTrace();
            ab.a("DeviceDataCollector", "addScreenWidthAndHeight throw Exception:" + exc.getMessage(), exc);
            return jSONObject2;
        }
    }

    private JSONObject t(JSONObject jSONObject, Context context) throws JSONException {
        Exception e;
        JSONObject jSONObject2;
        if (jSONObject == null) {
            try {
                jSONObject2 = new JSONObject();
            } catch (Exception e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                ab.a("DeviceDataCollector", "addDensity throw Exception:" + e.getMessage(), e);
                return jSONObject2;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            StringBuilder sb = new StringBuilder("Device:");
            String str = Build.MANUFACTURER;
            String str2 = Build.MODEL;
            ab.c("DeviceDataCollector", sb.append((str2 == null || str == null || !str2.startsWith(str)) ? String.valueOf(b(str)) + " " + str2 : b(str2)).append(" metrics.densityDpi:").append(displayMetrics.densityDpi).append(" metrics.density:").append(displayMetrics.density).toString());
            jSONObject2.put("u_sd", displayMetrics.density);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ab.a("DeviceDataCollector", "addDensity throw Exception:" + e.getMessage(), e);
            return jSONObject2;
        }
        return jSONObject2;
    }

    public final JSONObject a(JSONObject jSONObject, Context context) throws JSONException {
        Exception exc;
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        if (jSONObject == null) {
            try {
                jSONObject3 = new JSONObject();
            } catch (Exception e) {
                exc = e;
                jSONObject2 = jSONObject;
                exc.printStackTrace();
                ab.a("DeviceDataCollector", "addUnitWidthAndHeight throw Exception:" + exc.getMessage(), exc);
                return jSONObject2;
            }
        } else {
            jSONObject3 = jSONObject;
        }
        try {
            int i = C0086a.g(context).widthPixels;
            int i2 = C0086a.g(context).heightPixels;
            int iB = b(context);
            int top = ((Activity) context).getWindow().findViewById(android.R.id.content).getTop() - b(context);
            int iRound = Math.round(VpadnAdSize.convertPixelsToDp(i, context));
            int iRound2 = Math.round(VpadnAdSize.convertPixelsToDp((i2 - iB) - top, context));
            jSONObject3.put("u_w", iRound);
            jSONObject3.put("u_h", iRound2);
            return jSONObject3;
        } catch (Exception e2) {
            jSONObject2 = jSONObject3;
            exc = e2;
            exc.printStackTrace();
            ab.a("DeviceDataCollector", "addUnitWidthAndHeight throw Exception:" + exc.getMessage(), exc);
            return jSONObject2;
        }
    }

    private static int b(Context context) {
        Rect rect = new Rect();
        Window window = ((Activity) context).getWindow();
        if (window != null && window.getDecorView() != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            return rect.top;
        }
        ab.b("DeviceDataCollector", "getStatusBarHeight ERROR (w != null && w.getDecorView() != null) is false");
        return 0;
    }

    private static String b(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char cCharAt = str.charAt(0);
        return !Character.isUpperCase(cCharAt) ? String.valueOf(Character.toUpperCase(cCharAt)) + str.substring(1) : str;
    }
}
