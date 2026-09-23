package jp.adlantis.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GreeAdManager extends AdManager {
    @Override // jp.adlantis.android.AdManager
    public String byline() {
        return "Ads by GREE";
    }

    @Override // jp.adlantis.android.AdManager
    AdNetworkConnection createConnection() {
        return new GreeAdConnection();
    }

    @Override // jp.adlantis.android.AdManager
    public String sdkDescription() {
        return "GREE Ad SDK";
    }
}
