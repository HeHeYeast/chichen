package com.jirbo.adcolony;

import com.jirbo.adcolony.n;
import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class AdColonyAd implements Serializable {
    static final int a = 0;
    static final int b = 1;

    /* renamed from: c, reason: collision with root package name */
    static final int f205c = 2;
    static final int d = 3;
    static final int e = 4;
    String g;
    n.ab h;
    n.a i;
    int p;
    boolean q;
    boolean r;
    boolean s;
    boolean t;
    int f = 0;
    String j = "";
    String k = "";
    String l = "";
    String m = "";
    double n = 0.0d;
    double o = 0.0d;
    AdColonyIAPEngagement u = AdColonyIAPEngagement.NONE;

    abstract void a();

    abstract boolean a(boolean z);

    abstract boolean b();

    abstract boolean isReady();

    public boolean shown() {
        return this.f == 4;
    }

    public boolean notShown() {
        return this.f != 4;
    }

    public boolean canceled() {
        return this.f == 1;
    }

    public boolean noFill() {
        return this.f == 2;
    }

    public boolean skipped() {
        return this.f == 3;
    }

    public boolean iapEnabled() {
        return this.t;
    }

    public AdColonyIAPEngagement iapEngagementType() {
        return this.u;
    }

    public String iapProductID() {
        return this.m;
    }

    public int getAvailableViews() {
        if (isReady() && c()) {
            return this.h.d();
        }
        return 0;
    }

    boolean c() {
        return b(false);
    }

    boolean b(boolean z) {
        if (this.f == 4) {
            return true;
        }
        if (!isReady() && !z) {
            return false;
        }
        if (!a(true) && z) {
            return false;
        }
        this.h = a.l.h(this.g);
        this.i = z ? this.h.j() : this.h.i();
        return this.i != null;
    }
}
