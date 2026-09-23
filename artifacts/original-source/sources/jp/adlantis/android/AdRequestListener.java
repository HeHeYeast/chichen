package jp.adlantis.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface AdRequestListener {
    void onFailedToReceiveAd(AdRequestNotifier adRequestNotifier);

    void onReceiveAd(AdRequestNotifier adRequestNotifier);

    void onTouchAd(AdRequestNotifier adRequestNotifier);
}
