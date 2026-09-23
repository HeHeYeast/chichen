package com.vpadn.ads;

import android.app.Activity;
import org.json.JSONException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class VpadnVideoInterstitialAd extends VpadnInterstitialAd {
    public VpadnVideoInterstitialAd(Activity activity, String str, String str2) {
        super(activity, str, str2);
    }

    @Override // com.vpadn.ads.VpadnInterstitialAd, com.vpadn.ads.VpadnAd
    public void loadAd(VpadnAdRequest vpadnAdRequest) throws JSONException {
        this.a.s();
        super.loadAd(vpadnAdRequest);
    }
}
