package com.vpadn.ads;

import com.vpadn.ads.VpadnAdRequest;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface VpadnAdListener {
    void onVpadnDismissScreen(VpadnAd vpadnAd);

    void onVpadnFailedToReceiveAd(VpadnAd vpadnAd, VpadnAdRequest.VpadnErrorCode vpadnErrorCode);

    void onVpadnLeaveApplication(VpadnAd vpadnAd);

    void onVpadnPresentScreen(VpadnAd vpadnAd);

    void onVpadnReceiveAd(VpadnAd vpadnAd);
}
