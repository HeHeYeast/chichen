package com.jirbo.adcolony;

import android.content.Intent;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.ab;
import com.jirbo.adcolony.n;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class d {
    volatile boolean k;
    boolean l;
    boolean m;
    c a = new c(this);
    b b = new b(this);

    /* renamed from: c, reason: collision with root package name */
    o f219c = new o(this);
    u d = new u(this);
    v e = new v(this);
    ADCStorage f = new ADCStorage(this);
    ah g = new ah(this);
    t h = new t(this);
    ArrayList<j> i = new ArrayList<>();
    ArrayList<j> j = new ArrayList<>();
    ab.a n = new ab.a();

    d() {
    }

    void a(j jVar) {
        synchronized (this.i) {
            if (a.d()) {
                this.i.add(jVar);
                if (!this.k) {
                    g();
                }
            }
        }
    }

    void a() {
        if (!this.l && a.d()) {
            while (true) {
                try {
                    if (this.l && (this.k || this.i.size() <= 0)) {
                        break;
                    }
                    this.l = true;
                    this.j.addAll(this.i);
                    this.i.clear();
                    for (int i = 0; i < this.j.size(); i++) {
                        if (this.j.get(i) != null) {
                            this.j.get(i).a();
                        }
                    }
                    this.j.clear();
                } catch (RuntimeException e) {
                    this.l = false;
                    this.j.clear();
                    this.i.clear();
                    a.a(e);
                    return;
                }
            }
            this.l = false;
        }
    }

    void b() {
        this.k = true;
        new j(this) { // from class: com.jirbo.adcolony.d.1
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.e.c();
            }
        };
    }

    void c() {
        this.k = false;
        new j(this) { // from class: com.jirbo.adcolony.d.2
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.e.d();
            }
        };
    }

    void d() {
        new j(this) { // from class: com.jirbo.adcolony.d.3
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.e.e();
            }
        };
    }

    synchronized void a(final AdColonyAd adColonyAd) {
        this.a.n = 0.0d;
        l.a.b((Object) "Tracking ad event - start");
        adColonyAd.h.k.d++;
        if (!adColonyAd.b()) {
            adColonyAd.h.k();
            this.h.a(adColonyAd.g, adColonyAd.i.d);
        }
        new j(this) { // from class: com.jirbo.adcolony.d.4
            @Override // com.jirbo.adcolony.j
            void a() {
                if (AdColony.isZoneV4VC(adColonyAd.g) || !adColonyAd.k.equals("native")) {
                    d.this.a("start", "{\"ad_slot\":" + adColonyAd.h.k.d + ", \"replay\":" + adColonyAd.s + "}", adColonyAd);
                }
            }
        };
    }

    void a(final double d, final AdColonyAd adColonyAd) {
        new j(this) { // from class: com.jirbo.adcolony.d.5
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.d.a(d, adColonyAd);
            }
        };
    }

    synchronized void a(boolean z, String str, int i) {
        a.N.a(z, str, i);
    }

    synchronized void a(boolean z, final AdColonyAd adColonyAd) {
        int i = 0;
        boolean z2 = true;
        synchronized (this) {
            if (adColonyAd != null) {
                a(1.0d, adColonyAd);
                if (!z && adColonyAd.b()) {
                    adColonyAd.h.k();
                    this.h.a(adColonyAd.g, adColonyAd.i.d);
                    AdColonyV4VCAd adColonyV4VCAd = (AdColonyV4VCAd) a.J;
                    final String rewardName = adColonyV4VCAd.getRewardName();
                    final int rewardAmount = adColonyV4VCAd.getRewardAmount();
                    int viewsPerReward = adColonyV4VCAd.getViewsPerReward();
                    if (viewsPerReward > 1) {
                        int iC = this.h.c(adColonyV4VCAd.getRewardName()) + 1;
                        if (iC < viewsPerReward) {
                            z2 = false;
                            i = iC;
                        }
                        this.h.b(adColonyV4VCAd.getRewardName(), i);
                    }
                    if (z2) {
                        if (adColonyV4VCAd.h.j.e) {
                            a(true, rewardName, rewardAmount);
                        }
                        new j(this) { // from class: com.jirbo.adcolony.d.6
                            @Override // com.jirbo.adcolony.j
                            void a() {
                                ADCData.g gVar = new ADCData.g();
                                gVar.b("v4vc_name", rewardName);
                                gVar.b("v4vc_amount", rewardAmount);
                                this.o.d.a("reward_v4vc", gVar, adColonyAd);
                            }
                        };
                    }
                }
            }
        }
    }

    void a(final String str, final String str2) {
        new j(this) { // from class: com.jirbo.adcolony.d.7
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.d.a(str, k.b(str2));
            }
        };
    }

    void a(final String str, final String str2, final AdColonyAd adColonyAd) {
        new j(this) { // from class: com.jirbo.adcolony.d.8
            @Override // com.jirbo.adcolony.j
            void a() {
                this.o.d.a(str, k.b(str2), adColonyAd);
            }
        };
    }

    synchronized double a(String str) {
        double dF;
        try {
            dF = this.a.i.f(str);
        } catch (RuntimeException e) {
            a.a(e);
            dF = 0.0d;
        }
        return dF;
    }

    synchronized int b(String str) {
        int iG;
        try {
            iG = this.a.i.g(str);
        } catch (RuntimeException e) {
            a.a(e);
            iG = 0;
        }
        return iG;
    }

    synchronized boolean c(String str) {
        boolean zH;
        try {
            zH = this.a.i.h(str);
        } catch (RuntimeException e) {
            a.a(e);
            zH = false;
        }
        return zH;
    }

    synchronized String d(String str) {
        String strE;
        try {
            strE = this.a.i.e(str);
        } catch (RuntimeException e) {
            a.a(e);
            strE = null;
        }
        return strE;
    }

    synchronized String e() {
        return this.b.c();
    }

    synchronized String f() {
        return this.b.d();
    }

    synchronized int e(String str) {
        return this.h.c(str);
    }

    synchronized void a(String str, int i) {
        this.h.b(str, i);
    }

    synchronized boolean f(String str) {
        return a(str, false, true);
    }

    synchronized boolean a(String str, boolean z, boolean z2) {
        boolean zB = false;
        synchronized (this) {
            try {
                if (a.d() && this.b.b(str, z)) {
                    zB = this.b.j.n.a(str).b(z2);
                }
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
        return zB;
    }

    synchronized boolean g(String str) {
        return b(str, false, true);
    }

    synchronized boolean b(String str, boolean z, boolean z2) {
        boolean zC = false;
        synchronized (this) {
            try {
                if (a.d() && this.b.b(str, z)) {
                    zC = this.b.j.n.a(str).c(z2);
                }
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
        return zC;
    }

    synchronized void a(AdColonyVideoAd adColonyVideoAd) {
        this.a.b(adColonyVideoAd.g);
    }

    synchronized void a(AdColonyInterstitialAd adColonyInterstitialAd) {
        this.a.b(adColonyInterstitialAd.g);
    }

    synchronized boolean b(AdColonyVideoAd adColonyVideoAd) {
        boolean zB = false;
        synchronized (this) {
            try {
                a.J = adColonyVideoAd;
                String str = adColonyVideoAd.g;
                if (f(str)) {
                    l.a.a("Showing ad for zone ").b((Object) str);
                    a(adColonyVideoAd);
                    zB = b((AdColonyAd) adColonyVideoAd);
                }
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
        return zB;
    }

    synchronized boolean b(AdColonyInterstitialAd adColonyInterstitialAd) {
        boolean zB = false;
        synchronized (this) {
            try {
                a.J = adColonyInterstitialAd;
                String str = adColonyInterstitialAd.g;
                if (f(str)) {
                    l.a.a("Showing ad for zone ").b((Object) str);
                    a(adColonyInterstitialAd);
                    zB = b((AdColonyAd) adColonyInterstitialAd);
                }
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
        return zB;
    }

    synchronized n.ab h(String str) {
        return this.b.j.n.a(str);
    }

    synchronized void a(AdColonyV4VCAd adColonyV4VCAd) {
        this.a.c(adColonyV4VCAd.g);
    }

    synchronized boolean b(AdColonyV4VCAd adColonyV4VCAd) {
        boolean zB = false;
        synchronized (this) {
            try {
                a.J = adColonyV4VCAd;
                String str = adColonyV4VCAd.g;
                if (g(str)) {
                    l.a.a("Showing v4vc for zone ").b((Object) str);
                    a(adColonyV4VCAd);
                    zB = b((AdColonyAd) adColonyV4VCAd);
                }
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
        return zB;
    }

    synchronized boolean b(AdColonyAd adColonyAd) {
        boolean z;
        if (this.a.l.b()) {
            a.J.f = 3;
            z = false;
        } else {
            a(adColonyAd);
            ADCVideo.a();
            if (a.m) {
                l.a.b((Object) "Launching AdColonyOverlay");
                a.b().startActivity(new Intent(a.b(), (Class<?>) AdColonyOverlay.class));
            } else {
                l.a.b((Object) "Launching AdColonyFullscreen");
                a.b().startActivity(new Intent(a.b(), (Class<?>) AdColonyFullscreen.class));
            }
            z = true;
        }
        return z;
    }

    synchronized void a(String str, String str2, String[] strArr) {
        try {
            a(a.n);
            l.f226c.a("==== Configuring AdColony ").a(this.a.b).b((Object) " ====");
            l.a.a("package name: ").b((Object) ab.f());
            this.a.j = str2;
            this.a.k = strArr;
            this.a.a(str);
            this.n.a();
        } catch (RuntimeException e) {
            a.a(e);
        }
    }

    synchronized void g() {
        if (!a.c()) {
            try {
                a();
                if (!a.p) {
                    if (g.n() != null || this.n.b() > 5.0d) {
                        this.a.a();
                        a.p = true;
                    }
                    a.r = true;
                }
                this.b.f();
                this.f219c.e();
                this.e.b();
                this.d.d();
                this.h.d();
                this.g.d();
            } catch (RuntimeException e) {
                a.a(e);
            }
        }
    }

    void a(int i) {
        a.a(i);
    }
}
