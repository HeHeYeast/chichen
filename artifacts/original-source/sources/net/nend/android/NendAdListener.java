package net.nend.android;

import java.util.EventListener;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface NendAdListener extends EventListener {
    void onClick(NendAdView nendAdView);

    void onDismissScreen(NendAdView nendAdView);

    void onFailedToReceiveAd(NendAdView nendAdView);

    void onReceiveAd(NendAdView nendAdView);
}
