package jp.adlantis.android;

import android.app.Activity;
import android.widget.RelativeLayout;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisCustomEventBanner implements CustomEventBanner, AdRequestListener {
    private AdlantisView adView;
    private CustomEventBannerListener bannerListener;

    @Override // com.google.ads.mediation.customevent.CustomEvent
    public void destroy() {
    }

    @Override // jp.adlantis.android.AdRequestListener
    public void onFailedToReceiveAd(AdRequestNotifier adRequestNotifier) {
        this.bannerListener.onFailedToReceiveAd();
    }

    @Override // jp.adlantis.android.AdRequestListener
    public void onReceiveAd(AdRequestNotifier adRequestNotifier) {
        this.bannerListener.onReceivedAd(this.adView);
    }

    @Override // jp.adlantis.android.AdRequestListener
    public void onTouchAd(AdRequestNotifier adRequestNotifier) {
        this.bannerListener.onClick();
    }

    @Override // com.google.ads.mediation.customevent.CustomEventBanner
    public void requestBannerAd(CustomEventBannerListener customEventBannerListener, Activity activity, String str, String str2, AdSize adSize, MediationAdRequest mediationAdRequest, Object obj) {
        this.bannerListener = customEventBannerListener;
        this.adView = new AdlantisView(activity);
        this.adView.setAdFetchInterval(0L);
        this.adView.addRequestListener(this);
        this.adView.setLayoutParams(new RelativeLayout.LayoutParams(-2, adSize.getHeightInPixels(activity)));
        this.adView.setPublisherID(str2);
    }
}
