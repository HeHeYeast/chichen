package com.jirbo.adcolony;

import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyInterstitialAd extends AdColonyAd {
    AdColonyAdListener v;
    AdColonyNativeAdListener w;
    AdColonyNativeAdView x;
    boolean y;

    public AdColonyInterstitialAd() {
        a.u = false;
        a.e();
        this.j = FluctConstants.XML_NODE_INTERSTITIAL;
        this.k = "fullscreen";
        this.y = false;
        this.l = ab.b();
    }

    public AdColonyInterstitialAd(String zone_id) {
        this.j = FluctConstants.XML_NODE_INTERSTITIAL;
        this.k = "fullscreen";
        a.e();
        this.g = zone_id;
        this.y = false;
        this.l = ab.b();
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    boolean b() {
        return false;
    }

    public AdColonyInterstitialAd withListener(AdColonyAdListener listener) {
        this.v = listener;
        return this;
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    boolean a(boolean z) {
        if (this.g == null) {
            this.g = a.l.e();
            if (this.g == null) {
                return false;
            }
        }
        return a.l.f(this.g);
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    public boolean isReady() {
        if (this.g == null) {
            this.g = a.l.e();
            if (this.g == null) {
                return false;
            }
        }
        if (AdColony.isZoneNative(this.g)) {
            a.ac = 12;
            return false;
        }
        return a.l.f(this.g);
    }

    public void show() {
        a.ac = 0;
        if (this.y) {
            l.d.b((Object) "Show attempt on out of date ad object. Please instantiate a new ad object for each ad attempt.");
            return;
        }
        this.y = true;
        this.j = FluctConstants.XML_NODE_INTERSTITIAL;
        this.k = "fullscreen";
        if (!isReady()) {
            new j(a.l) { // from class: com.jirbo.adcolony.AdColonyInterstitialAd.1
                @Override // com.jirbo.adcolony.j
                void a() {
                    if (AdColonyInterstitialAd.this.g != null) {
                        this.o.d.a(AdColonyInterstitialAd.this.g, AdColonyInterstitialAd.this);
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
            new j(a.l) { // from class: com.jirbo.adcolony.AdColonyInterstitialAd.2
                @Override // com.jirbo.adcolony.j
                void a() {
                    this.o.d.a(AdColonyInterstitialAd.this.g, AdColonyInterstitialAd.this);
                }
            };
            a.v = false;
            c();
            a.J = this;
            if (!a.l.b(this)) {
                if (this.v != null) {
                    this.v.onAdColonyAdAttemptFinished(this);
                }
                a.v = true;
                return;
            } else if (this.v != null) {
                this.v.onAdColonyAdStarted(this);
            }
        }
        this.f = 4;
    }

    @Override // com.jirbo.adcolony.AdColonyAd
    void a() {
        int i = 0;
        this.j = FluctConstants.XML_NODE_INTERSTITIAL;
        this.k = "fullscreen";
        if (this.v != null) {
            this.v.onAdColonyAdAttemptFinished(this);
        } else if (this.w != null) {
            if (canceled()) {
                this.x.I = true;
            } else {
                this.x.I = false;
            }
            this.w.onAdColonyNativeAdFinished(true, this.x);
        }
        a.h();
        System.gc();
        if (!a.u && !AdColonyBrowser.B) {
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
        a.v = true;
    }
}
