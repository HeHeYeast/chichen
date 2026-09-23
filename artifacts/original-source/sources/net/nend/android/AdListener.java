package net.nend.android;

import net.nend.android.NendAdView;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
interface AdListener {
    void onFailedToReceiveAd(NendAdView.NendError nendError);

    void onReceiveAd();
}
