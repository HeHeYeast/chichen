package com.jirbo.adcolony;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.p;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class a {
    static boolean D = false;
    static boolean E = false;
    static boolean G = false;
    static boolean H = false;
    static h I = null;
    static AdColonyAd J = null;
    static ADCVideo K = null;
    static ADCVideo L = null;
    static HandlerC0074a M = null;
    static b N = null;
    static boolean O = false;
    static boolean P = false;
    static boolean Q = false;
    static boolean R = false;
    static int S = 0;
    static String T = null;
    static String U = null;
    static String V = null;
    static String W = null;
    static String X = null;
    public static final boolean a = false;
    static boolean aa = false;
    static long ab = 0;
    static HashMap ah = null;
    private static Activity ai = null;
    public static final boolean b = false;

    /* renamed from: c, reason: collision with root package name */
    public static final boolean f212c = false;
    public static final boolean d = false;
    public static final int g = 0;
    public static final int h = 1;
    public static final int i = 2;
    public static final int j = 3;
    static final String k = "AdColony";
    static boolean m;
    static boolean o;
    static boolean p;
    static boolean q;
    static boolean r;
    static boolean s;
    static boolean t;
    public static String e = null;
    public static final String f = null;
    static d l = new d();
    static int n = 2;
    static boolean u = false;
    static boolean v = true;
    static int w = 0;
    static double x = 1.0d;
    static boolean y = false;
    static boolean z = false;
    static boolean A = false;
    static boolean B = false;
    static boolean C = true;
    static boolean F = false;
    static ArrayList<String> Y = new ArrayList<>();
    static ADCData.c Z = new ADCData.c();
    static int ac = 0;
    static ArrayList<Bitmap> ad = new ArrayList<>();
    static ArrayList<AdColonyV4VCListener> ae = new ArrayList<>();
    static ArrayList<AdColonyAdAvailabilityListener> af = new ArrayList<>();
    static ArrayList<AdColonyNativeAdView> ag = new ArrayList<>();

    a() {
    }

    static void a(Activity activity) {
        if (activity != ai && activity != null) {
            ai = activity;
            M = new HandlerC0074a();
            N = new b();
            new p.a();
        }
    }

    static void b(Activity activity) {
        p = false;
        a(activity);
        I = null;
        m = g.i();
        if (G) {
            G = false;
            o = false;
            l = new d();
        }
    }

    static boolean a() {
        return ai == null;
    }

    static Activity b() {
        if (ai == null) {
            throw new AdColonyException("AdColony.configure() must be called before any other AdColony methods. If you have called AdColony.configure(), the Activity reference you passed in via AdColony.configure() OR AdColony.resume() is null.");
        }
        return ai;
    }

    static boolean c() {
        return G || q || !o;
    }

    static boolean d() {
        return (!o || G || q) ? false : true;
    }

    static void a(String str) {
        G = true;
        e(str);
    }

    static void a(RuntimeException runtimeException) {
        G = true;
        e(runtimeException.toString());
        runtimeException.printStackTrace();
    }

    static void e() {
        b();
    }

    static void a(int i2) {
        n = i2;
        l.a.f = i2 <= 0;
        l.b.f = i2 <= 1;
        l.f226c.f = i2 <= 2;
        l.d.f = i2 <= 3;
        if (i2 <= 0) {
            b("DEVELOPER LOGGING ENABLED");
        }
        if (i2 <= 1) {
            c("DEBUG LOGGING ENABLED");
        }
    }

    static boolean b(int i2) {
        return n <= i2;
    }

    static boolean f() {
        return n <= 0;
    }

    static boolean g() {
        return n <= 1;
    }

    static void a(int i2, String str) {
        if (n <= i2) {
            switch (i2) {
                case 0:
                case 1:
                    Log.d(k, str);
                    break;
                case 2:
                    Log.i(k, str);
                    break;
                case 3:
                    Log.e(k, str);
                    break;
            }
        }
    }

    static void b(String str) {
        a(0, str);
    }

    static void c(String str) {
        a(1, str);
    }

    static void d(String str) {
        a(2, str);
    }

    static void e(String str) {
        a(3, str);
    }

    static void f(String str) {
        Toast.makeText(b(), str, 0).show();
    }

    static double g(String str) {
        return l.a(str);
    }

    static int h(String str) {
        return l.b(str);
    }

    static boolean i(String str) {
        return l.c(str);
    }

    static String j(String str) {
        return l.d(str);
    }

    static void k(String str) {
        l.a(str, (String) null);
    }

    static void a(String str, String str2) {
        l.a(str, str2);
    }

    static void a(String str, AdColonyAd adColonyAd) {
        l.a(str, (String) null, adColonyAd);
    }

    static void a(String str, String str2, AdColonyAd adColonyAd) {
        l.a(str, str2, adColonyAd);
    }

    static void h() {
        boolean zA;
        if (l != null && af.size() != 0 && ah != null) {
            for (Map.Entry entry : ah.entrySet()) {
                boolean zBooleanValue = ((Boolean) entry.getValue()).booleanValue();
                if (AdColony.isZoneV4VC((String) entry.getKey())) {
                    zA = l.b((String) entry.getKey(), true, false);
                } else {
                    zA = l.a((String) entry.getKey(), true, false);
                }
                boolean zB = AdColony.isZoneNative((String) entry.getKey()) ? new AdColonyNativeAdView(b(), (String) entry.getKey(), 300, true).b(true) : zA;
                if (zBooleanValue != zB) {
                    ah.put(entry.getKey(), Boolean.valueOf(zB));
                    for (int i2 = 0; i2 < af.size(); i2++) {
                        af.get(i2).onAdColonyAdAvailabilityChange(zB, (String) entry.getKey());
                    }
                }
            }
        }
    }

    static void a(AdColonyNativeAdView adColonyNativeAdView) {
        ag.add(adColonyNativeAdView);
    }

    /* renamed from: com.jirbo.adcolony.a$a, reason: collision with other inner class name */
    static class HandlerC0074a extends Handler {
        AdColonyAd a;

        HandlerC0074a() {
        }

        public void a(AdColonyAd adColonyAd) {
            if (adColonyAd == null) {
                this.a = a.J;
            } else {
                this.a = adColonyAd;
            }
            sendMessage(obtainMessage(1));
        }

        public void b(AdColonyAd adColonyAd) {
            if (adColonyAd == null) {
                this.a = a.J;
            } else {
                this.a = adColonyAd;
            }
            sendMessage(obtainMessage(0));
        }

        @Override // android.os.Handler
        public void handleMessage(Message m) {
            switch (m.what) {
                case 0:
                    a.a("skip", this.a);
                    if (a.J != null) {
                        a.J.f = 1;
                        a.J.a();
                        break;
                    }
                    break;
                case 1:
                    ADCData.g gVar = new ADCData.g();
                    if (a.L.F.Q) {
                        gVar.b("html5_endcard_loading_started", a.L.k);
                    }
                    if (a.L.F.Q) {
                        gVar.b("html5_endcard_loading_finished", a.L.l);
                    }
                    if (a.L.F.Q) {
                        gVar.b("html5_endcard_loading_time", a.L.p);
                    }
                    if (a.L.F.Q) {
                        gVar.b("html5_endcard_loading_timeout", a.L.m);
                    }
                    if (a.L.q < 60000.0d) {
                        gVar.b("endcard_time_spent", a.L.q);
                    }
                    gVar.b("endcard_dissolved", a.L.n);
                    ADCVideo aDCVideo = a.L;
                    gVar.b("replay", ADCVideo.e);
                    gVar.b("reward", a.L.o);
                    a.l.d.a("continue", gVar, this.a);
                    a.l.b.e();
                    if (a.J != null) {
                        a.J.f = 4;
                        a.J.a();
                        break;
                    }
                    break;
            }
        }
    }

    static class b extends Handler {
        b() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message m) {
            String str = (String) m.obj;
            int i = m.what;
            boolean z = str != null;
            if (!z) {
                str = "";
            }
            AdColonyV4VCReward adColonyV4VCReward = new AdColonyV4VCReward(z, str, i);
            for (int i2 = 0; i2 < a.ae.size(); i2++) {
                a.ae.get(i2).onAdColonyV4VCReward(adColonyV4VCReward);
            }
        }

        public void a(boolean z, String str, int i) {
            if (!z) {
                str = null;
            }
            sendMessage(obtainMessage(i, str));
        }
    }

    static void a(j jVar) {
        l.a(jVar);
    }
}
