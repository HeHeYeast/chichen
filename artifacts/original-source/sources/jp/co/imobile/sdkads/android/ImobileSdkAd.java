package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ImobileSdkAd {
    private static ImobileSdkAd a = new ImobileSdkAd();
    private static /* synthetic */ int[] m;
    private static /* synthetic */ int[] n;
    private r h;
    private ConcurrentHashMap b = new ConcurrentHashMap();

    /* renamed from: c, reason: collision with root package name */
    private Boolean f292c = false;
    private Boolean d = false;
    private Boolean e = true;
    private AdOrientation f = AdOrientation.AUTO;
    private Context g = null;
    private Timer i = null;
    private TimerTask j = null;
    private Boolean k = false;
    private final BroadcastReceiver l = new o(this);

    public enum AdShowType {
        DIALOG,
        INLINE;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static AdShowType[] valuesCustom() {
            AdShowType[] adShowTypeArrValuesCustom = values();
            int length = adShowTypeArrValuesCustom.length;
            AdShowType[] adShowTypeArr = new AdShowType[length];
            System.arraycopy(adShowTypeArrValuesCustom, 0, adShowTypeArr, 0, length);
            return adShowTypeArr;
        }
    }

    public enum AdType {
        NORMAL_AD,
        HOUSE_AD;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static AdType[] valuesCustom() {
            AdType[] adTypeArrValuesCustom = values();
            int length = adTypeArrValuesCustom.length;
            AdType[] adTypeArr = new AdType[length];
            System.arraycopy(adTypeArrValuesCustom, 0, adTypeArr, 0, length);
            return adTypeArr;
        }
    }

    private ImobileSdkAd() {
    }

    static Context a() {
        return a.g;
    }

    private void a(Activity activity, String str, String str2, String str3, AdShowType adShowType) throws PackageManager.NameNotFoundException {
        if (this.g == null) {
            this.g = activity.getApplicationContext();
            try {
                ApplicationInfo applicationInfo = activity.getApplicationContext().getPackageManager().getApplicationInfo(activity.getApplicationContext().getPackageName(), 128);
                if (applicationInfo.metaData != null) {
                    this.f292c = Boolean.valueOf(applicationInfo.metaData.getBoolean("i-mobile_Testing", w.b.booleanValue()));
                    this.d = Boolean.valueOf(applicationInfo.metaData.getBoolean("i-mobile_DebugLogging", w.a.booleanValue()));
                    this.e = Boolean.valueOf(applicationInfo.metaData.getBoolean("i-mobile_SendID", w.f305c.booleanValue()));
                    if (this.f == AdOrientation.AUTO) {
                        String string = applicationInfo.metaData.getString("i-mobile_AdOrientation");
                        if (string != null) {
                            try {
                                this.f = AdOrientation.valueOf(string.toUpperCase(Locale.getDefault()));
                            } catch (RuntimeException e) {
                                x.a("ImobileSdkAd parameter error.", "i-mobile_ShowLayout value Illegal, use value default(AUTO).");
                                this.f = w.d;
                            }
                        } else {
                            this.f = w.d;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e2) {
            }
            this.h = r.a();
            this.h.a(activity);
        }
        z anVar = (z) this.b.get(str3);
        if (anVar == null) {
            switch (e()[adShowType.ordinal()]) {
                case 1:
                    anVar = new ap();
                    break;
                case 2:
                    anVar = new an();
                    break;
                default:
                    x.a("ImobileSdkAd spot create error.", "adShowType not found.");
                    break;
            }
            if (anVar != null) {
                anVar.a(adShowType);
                activity.getApplicationContext();
                anVar.a(str, str2, str3);
                this.b.put(str3, anVar);
            }
        }
    }

    private void a(Activity activity, String str, ImobileSdkAdListener imobileSdkAdListener, Point point, Boolean bool, ViewGroup viewGroup, ImobileIconParams imobileIconParams, Boolean bool2) {
        z zVar = (z) this.b.get(str);
        if (zVar == null) {
            x.a("ImobileSdkAd start error.", "Spot is not registered.");
            if (imobileSdkAdListener != null) {
                imobileSdkAdListener.onAdCloseCompleted();
            }
        }
        Point point2 = point == null ? new Point(0, 0) : point;
        synchronized (this) {
            switch (f()[zVar.a().ordinal()]) {
                case 2:
                case 4:
                case 6:
                    switch (e()[zVar.b().ordinal()]) {
                        case 1:
                            if (zVar.t != null) {
                                zVar.t.onAdCloseCompleted();
                            }
                            if (imobileSdkAdListener != null) {
                                imobileSdkAdListener.onAdCloseCompleted();
                            }
                            x.a("ImobileSdkAd start error.", "Spot is not registered.");
                            break;
                        case 2:
                            zVar.b(new q(this, zVar, activity, imobileSdkAdListener, point2, bool, viewGroup, imobileIconParams, bool2));
                            break;
                    }
                case 3:
                    zVar.a(activity, imobileSdkAdListener, point2, bool, viewGroup, imobileIconParams, bool2);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Boolean bool) {
        Iterator it = this.b.entrySet().iterator();
        while (it.hasNext()) {
            ((z) ((Map.Entry) it.next()).getValue()).l();
        }
        if (this.i != null) {
            this.i.cancel();
        }
        this.j = null;
        x.a(null);
        if (bool.booleanValue() && this.k.booleanValue()) {
            this.g.unregisterReceiver(this.l);
            this.k = false;
        }
        r.a().d();
    }

    private void a(String str) {
        z zVar = (z) this.b.get(str);
        if (zVar == null) {
            x.a("ImobileSdkAd start error.", "Spot is not registered.");
            return;
        }
        new StringBuilder("spot id : ").append(str);
        x.a(null);
        zVar.h();
        r.a();
        if (!r.b().equals("") && this.j == null) {
            this.i = new Timer(true);
            this.j = new p(this);
            this.i.schedule(this.j, 0L, 5000L);
            x.a(null);
        }
        if (this.k.booleanValue()) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.g.registerReceiver(this.l, intentFilter);
        this.k = true;
    }

    public static void activityDestory() {
        Iterator it = a.b.entrySet().iterator();
        while (it.hasNext()) {
            ((z) ((Map.Entry) it.next()).getValue()).m();
        }
    }

    static Boolean b() {
        return a.f292c;
    }

    static Boolean c() {
        return a.d;
    }

    static AdOrientation d() {
        return a.f;
    }

    private static /* synthetic */ int[] e() {
        int[] iArr = m;
        if (iArr == null) {
            iArr = new int[AdShowType.valuesCustom().length];
            try {
                iArr[AdShowType.DIALOG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdShowType.INLINE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            m = iArr;
        }
        return iArr;
    }

    private static /* synthetic */ int[] f() {
        int[] iArr = n;
        if (iArr == null) {
            iArr = new int[am.a().length];
            try {
                iArr[am.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[am.LODING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[am.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[am.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[am.START.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[am.STOP.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            n = iArr;
        }
        return iArr;
    }

    public static boolean isShowAd(String spotId) {
        z zVar = (z) a.b.get(spotId);
        if (zVar != null) {
            return zVar.k();
        }
        return false;
    }

    public static void registerSpot(Activity activity, String publisherId, String mediaId, String spotId) throws PackageManager.NameNotFoundException {
        a.a(activity, publisherId, mediaId, spotId, AdShowType.DIALOG);
    }

    public static void registerSpotFullScreen(Activity activity, String publisherId, String mediaId, String spotId) throws PackageManager.NameNotFoundException {
        a.a(activity, publisherId, mediaId, spotId, AdShowType.DIALOG);
    }

    public static void registerSpotInline(Activity activity, String publisherId, String mediaId, String spotId) throws PackageManager.NameNotFoundException {
        a.a(activity, publisherId, mediaId, spotId, AdShowType.INLINE);
    }

    public static void setAdOrientation(AdOrientation adOrientation) {
        a.f = adOrientation;
    }

    public static void setImobileSdkAdListener(String spotId, ImobileSdkAdListener listener) {
        z zVar = (z) a.b.get(spotId);
        if (zVar != null) {
            zVar.a(listener);
        } else {
            x.a("ImobileSdkAd start error.", "Spot is not registered.");
        }
    }

    public static void setShowAdIntervalTime(String str, int i) {
    }

    public static void setShowAdSkipCount(String str, int i) {
    }

    public static void showAd(Activity activity, String spotId) {
        a.a(activity, spotId, null, null, false, null, null, false);
    }

    public static void showAd(Activity activity, String spotId, int left, int top, boolean isDpiConvert) {
        if (isDpiConvert) {
            r.a();
            left = r.a(left);
            r.a();
            top = r.a(top);
        }
        a.a(activity, spotId, null, new Point(left, top), false, null, new ImobileIconParams(), false);
    }

    public static void showAd(Activity activity, String spotId, int left, int top, boolean isDpiConvert, ImobileIconParams iconParams) {
        if (isDpiConvert) {
            r.a();
            left = r.a(left);
            r.a();
            top = r.a(top);
        }
        a.a(activity, spotId, null, new Point(left, top), false, null, iconParams, false);
    }

    public static void showAd(Activity activity, String spotId, ViewGroup targetViewGroup) {
        a.a(activity, spotId, null, null, false, targetViewGroup, new ImobileIconParams(), false);
    }

    public static void showAd(Activity activity, String spotId, ViewGroup targetViewGroup, ImobileIconParams iconParams) {
        a.a(activity, spotId, null, null, false, targetViewGroup, iconParams, false);
    }

    public static void showAd(Activity activity, String spotId, ImobileSdkAdListener listener) {
        a.a(activity, spotId, listener, null, false, null, null, false);
    }

    public static void showAdForAdMobMediation(Activity activity, String spotId, ViewGroup targetViewGroup) {
        a.a(activity, spotId, null, null, false, targetViewGroup, new ImobileIconParams(), true);
    }

    public static void showAdForce(Activity activity, String spotId, ImobileSdkAdListener listener) {
        a.a(activity, spotId, listener, null, true, null, null, false);
    }

    public static void showAdforce(Activity activity, String spotId) {
        a.a(activity, spotId, null, null, true, null, null, false);
    }

    public static void start(String spotId) {
        a.a(spotId);
    }

    public static void startAll() {
        ImobileSdkAd imobileSdkAd = a;
        for (Map.Entry entry : imobileSdkAd.b.entrySet()) {
            if (((z) entry.getValue()).a() == am.PAUSE) {
                ((z) entry.getValue()).a(am.START);
            }
            imobileSdkAd.a(((z) entry.getValue()).f306c);
        }
        if (imobileSdkAd.g != null) {
            r.a().c();
        }
    }

    public static void stop(String spotId) {
        ((z) a.b.get(spotId)).l();
    }

    public static void stopAll() {
        a.a((Boolean) true);
    }
}
