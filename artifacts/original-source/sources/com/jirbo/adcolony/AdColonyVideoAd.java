package com.jirbo.adcolony;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyVideoAd extends AdColonyInterstitialAd {
    public AdColonyVideoAd() {
    }

    public AdColonyVideoAd(String zone_id) {
        super(zone_id);
    }

    @Override // com.jirbo.adcolony.AdColonyInterstitialAd
    public AdColonyVideoAd withListener(AdColonyAdListener listener) {
        this.v = listener;
        return this;
    }
}
