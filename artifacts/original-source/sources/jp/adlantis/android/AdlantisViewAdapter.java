package jp.adlantis.android;

import android.view.View;
import android.view.ViewParent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisViewAdapter extends AdViewAdapter {
    public AdlantisViewAdapter(View view) {
        super(view);
        ((AdlantisAdViewContainer) this.adView).addRequestListener(new AdRequestListener() { // from class: jp.adlantis.android.AdlantisViewAdapter.1
            @Override // jp.adlantis.android.AdRequestListener
            public void onFailedToReceiveAd(AdRequestNotifier adRequestNotifier) {
                AdlantisViewAdapter.this.listeners.notifyListenersFailedToReceiveAd(AdlantisViewAdapter.this.adlantisView());
            }

            @Override // jp.adlantis.android.AdRequestListener
            public void onReceiveAd(AdRequestNotifier adRequestNotifier) {
                AdlantisViewAdapter.this.listeners.notifyListenersAdReceived(AdlantisViewAdapter.this.adlantisView());
            }

            @Override // jp.adlantis.android.AdRequestListener
            public void onTouchAd(AdRequestNotifier adRequestNotifier) {
                AdlantisViewAdapter.this.listeners.notifyListenersAdTouched(AdlantisViewAdapter.this.adlantisView());
            }
        });
    }

    protected AdlantisView adlantisView() {
        ViewParent parent = this.adView.getParent();
        if (parent instanceof AdlantisView) {
            return (AdlantisView) parent;
        }
        return null;
    }

    @Override // jp.adlantis.android.AdViewAdapter
    public void clearAds() {
        ((AdlantisAdViewContainer) this.adView).clearAds();
    }

    @Override // jp.adlantis.android.AdViewAdapter
    public void connect() {
        ((AdlantisAdViewContainer) this.adView).connect();
    }
}
