package jp.adlantis.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisView extends RelativeLayout implements AdRequestNotifier {
    protected AdViewAdapter adViewAdapter;

    public AdlantisView(Context context) {
        super(context);
        this.adViewAdapter = null;
        setupView();
    }

    public AdlantisView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.adViewAdapter = null;
        setupView();
    }

    protected AdManager adManager() {
        return AdManager.getInstance();
    }

    protected AdService adService() {
        return isInEditMode() ? new NullAdService() : adManager().getActiveAdService(getContext());
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void addRequestListener(AdRequestListener adRequestListener) {
        this.adViewAdapter.addRequestListener(adRequestListener);
    }

    protected AdlantisAdViewContainer adlantisAdViewContainer() {
        View viewAdView = this.adViewAdapter.adView();
        if (viewAdView == null || !(viewAdView instanceof AdlantisAdViewContainer)) {
            return null;
        }
        return (AdlantisAdViewContainer) viewAdView;
    }

    public void clearAds() {
        this.adViewAdapter.clearAds();
    }

    public void connect() {
        this.adViewAdapter.connect();
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        AdService adService = adService();
        if (adService != null) {
            if (i == 0) {
                adService.resume();
            } else {
                adService.pause();
            }
        }
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void removeRequestListener(AdRequestListener adRequestListener) {
        this.adViewAdapter.removeRequestListener(adRequestListener);
    }

    public void setAdFetchInterval(long j) {
        AdlantisAdViewContainer adlantisAdViewContainer = adlantisAdViewContainer();
        if (adlantisAdViewContainer != null) {
            adlantisAdViewContainer.setAdFetchInterval(j);
        }
    }

    public void setGapPublisherID(String str) {
        AdlantisAdViewContainer adlantisAdViewContainer = adlantisAdViewContainer();
        if (adlantisAdViewContainer != null) {
            adlantisAdViewContainer.setGapPublisherID(str);
        }
    }

    public void setKeywords(String str) {
        AdlantisAdViewContainer adlantisAdViewContainer = adlantisAdViewContainer();
        if (adlantisAdViewContainer != null) {
            adlantisAdViewContainer.setKeywords(str);
        }
    }

    public void setPublisherID(String str) {
        AdlantisAdViewContainer adlantisAdViewContainer = adlantisAdViewContainer();
        if (adlantisAdViewContainer != null) {
            adlantisAdViewContainer.setPublisherID(str);
        }
    }

    protected void setupView() {
        AdService adService = adService();
        if (adService != null) {
            adService.setTargetingParam(adManager().getTargetingParam());
            this.adViewAdapter = adService.adViewAdapter(getContext());
            View viewAdView = this.adViewAdapter.adView();
            if (viewAdView != null) {
                addView(viewAdView);
            }
        }
    }
}
