package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class v {
    d a;
    boolean b;

    /* renamed from: c, reason: collision with root package name */
    boolean f264c;
    double f;
    double g;
    double h;
    int i;
    boolean d = false;
    boolean e = false;
    String j = "uuid";

    v(d dVar) {
        this.a = dVar;
    }

    void a() {
    }

    void b() {
        if (this.a.b.b) {
            if (this.d) {
                this.d = false;
                this.a.d.a("install", (ADCData.g) null);
            }
            if (this.e) {
                this.e = false;
                this.a.d.a("session_start", (ADCData.g) null);
            }
        }
    }

    void c() {
        l.b.b((Object) "AdColony resuming");
        a.r = true;
        if (this.b) {
            l.d.b((Object) "AdColony.onResume() called multiple times in succession.");
        }
        this.b = true;
        g();
        double dC = ab.c();
        if (this.f264c) {
            if (dC - this.g > this.a.a.d) {
                a(this.h);
                this.f = dC;
                h();
            }
            this.f264c = false;
            f();
        } else {
            this.f = dC;
            h();
        }
        a.h();
    }

    void d() {
        l.b.b((Object) "AdColony suspending");
        a.r = true;
        if (!this.b) {
            l.d.b((Object) "AdColony.onPause() called without initial call to onResume().");
        }
        this.b = false;
        this.f264c = true;
        this.g = ab.c();
        f();
    }

    void e() {
        l.b.b((Object) "AdColony terminating");
        a.r = true;
        a(this.h);
        this.f264c = false;
        f();
    }

    void f() {
        ADCData.g gVar = new ADCData.g();
        gVar.b("allow_resume", this.f264c);
        gVar.b("start_time", this.f);
        gVar.b("finish_time", this.g);
        gVar.b("session_time", this.h);
        k.a(new f("session_info.txt"), gVar);
    }

    void g() {
        ADCData.g gVarB = k.b(new f("session_info.txt"));
        if (gVarB != null) {
            this.f264c = gVarB.h("allow_resume");
            this.f = gVarB.f("start_time");
            this.g = gVarB.f("finish_time");
            this.h = gVarB.f("session_time");
            return;
        }
        this.d = true;
    }

    void h() {
        this.e = true;
        this.j = ab.b();
        this.h = 0.0d;
        this.i = 0;
        if (a.l != null && a.l.b != null && a.l.b.j != null && a.l.b.j.n != null) {
            for (int i = 0; i < a.l.b.j.n.a(); i++) {
                if (a.l.b.j.n.a(i).k != null) {
                    a.l.b.j.n.a(i).k.d = 0;
                }
            }
        }
    }

    void a(double d) {
        l.a.a("Submitting session duration ").b(d);
        ADCData.g gVar = new ADCData.g();
        gVar.b("session_length", (int) d);
        this.a.d.a("session_end", gVar);
    }
}
