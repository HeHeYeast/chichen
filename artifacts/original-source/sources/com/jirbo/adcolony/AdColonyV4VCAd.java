package com.jirbo.adcolony;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class AdColonyV4VCAd extends AdColonyAd {
    AdColonyAdListener v;
    boolean w = false;
    boolean x = false;
    boolean y;

    public AdColonyV4VCAd() {
        a.u = false;
        a.e();
        this.j = "v4vc";
        this.y = false;
        this.k = "fullscreen";
        this.l = ab.b();
    }

    public AdColonyV4VCAd(String zone_id) {
        a.e();
        this.g = zone_id;
        this.j = "v4vc";
        this.y = false;
        this.k = "fullscreen";
        this.l = ab.b();
    }

    public AdColonyV4VCAd withListener(AdColonyAdListener listener) {
        this.v = listener;
        return this;
    }

    public AdColonyV4VCAd withConfirmationDialog(boolean setting) {
        this.w = setting;
        return this;
    }

    public AdColonyV4VCAd withResultsDialog(boolean setting) {
        this.x = setting;
        a.u = this.x;
        return this;
    }

    public AdColonyV4VCAd withConfirmationDialog() {
        return withConfirmationDialog(true);
    }

    public AdColonyV4VCAd withResultsDialog() {
        return withResultsDialog(true);
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    boolean b() {
        return true;
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    boolean a(boolean z) {
        return false;
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    public boolean isReady() {
        if (this.g == null) {
            this.g = a.l.f();
            if (this.g == null) {
                return false;
            }
        }
        return a.l.g(this.g);
    }

    public String getRewardName() {
        return !c() ? "" : this.h.j.d;
    }

    public int getRewardAmount() {
        if (c()) {
            return this.h.j.f231c;
        }
        return 0;
    }

    public int getViewsPerReward() {
        if (c()) {
            return this.h.j.f;
        }
        return 0;
    }

    public int getRemainingViewsUntilReward() {
        if (c()) {
            return this.h.j.f - a.l.e(this.h.j.d);
        }
        return 0;
    }

    public void show() {
        a.ac = 0;
        if (this.y) {
            l.d.b((Object) "Show attempt on out of date ad object. Please instantiate a new ad object for each ad attempt.");
            return;
        }
        this.y = true;
        if (!isReady()) {
            new j(a.l) { // from class: com.jirbo.adcolony.AdColonyV4VCAd.1
                @Override // com.jirbo.adcolony.j
                void a() {
                    if (AdColonyV4VCAd.this.g != null) {
                        this.o.d.a(AdColonyV4VCAd.this.g, AdColonyV4VCAd.this);
                    }
                }
            };
            this.f = 2;
            if (this.v != null) {
                this.v.onAdColonyAdAttemptFinished(this);
                return;
            }
            return;
        }
        if (a.v) {
            new j(a.l) { // from class: com.jirbo.adcolony.AdColonyV4VCAd.2
                @Override // com.jirbo.adcolony.j
                void a() {
                    this.o.d.a(AdColonyV4VCAd.this.g, AdColonyV4VCAd.this);
                }
            };
            a.v = false;
            c();
            a.J = this;
            a.l.a(this);
            if (!this.w) {
                c(true);
            } else {
                a("Confirmation");
            }
        }
    }

    void c(boolean z) {
        if (z) {
            if (a.l.b(this)) {
                if (this.v != null) {
                    this.v.onAdColonyAdStarted(this);
                }
                this.f = 4;
            } else {
                this.f = 3;
            }
        } else {
            this.f = 1;
        }
        if (this.f == 4 || this.v == null) {
            return;
        }
        this.v.onAdColonyAdAttemptFinished(this);
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    void a() {
        if (this.f == 4 && this.x) {
            a("Result");
        }
        if (this.v != null) {
            this.v.onAdColonyAdAttemptFinished(this);
        }
        a.h();
        if (!a.u && !AdColonyBrowser.B) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= a.ad.size()) {
                    break;
                }
                a.ad.get(i2).recycle();
                i = i2 + 1;
            }
            a.ad.clear();
        }
        a.K = null;
        if (!this.x) {
            a.v = true;
        }
        System.gc();
    }

    void a(String str) {
        String str2 = ("" + getRewardAmount()) + " " + getRewardName();
        if (!str.equals("Confirmation")) {
            a.I = new ad(str2, this);
        } else {
            a.I = new ac(str2, this);
        }
    }
}
