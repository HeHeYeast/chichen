package jp.adlantis.android;

import android.view.View;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdViewAdapter implements AdRequestNotifier {
    protected View adView;
    protected AdRequestListeners listeners = new AdRequestListeners();

    AdViewAdapter(View view) {
        this.adView = view;
    }

    public View adView() {
        return this.adView;
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void addRequestListener(AdRequestListener adRequestListener) {
        this.listeners.addRequestListener(adRequestListener);
    }

    public void clearAds() {
    }

    public void connect() {
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void removeRequestListener(AdRequestListener adRequestListener) {
        this.listeners.removeRequestListener(adRequestListener);
    }
}
