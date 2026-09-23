package com.vpadn.ads;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface VpadnAd {
    boolean isReady();

    void loadAd(VpadnAdRequest vpadnAdRequest);

    void setAdListener(VpadnAdListener vpadnAdListener);

    void stopLoading();
}
