package jp.adlantis.android;

import android.content.Context;
import android.view.View;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADLAdService extends AdService {
    private String publisherId;

    public ADLAdService(String str) {
        this.publisherId = str;
    }

    @Override // jp.adlantis.android.AdService
    public AdViewAdapter adViewAdapter(Context context) {
        return new AdlantisViewAdapter(createAdView(context));
    }

    @Override // jp.adlantis.android.AdService
    public View createAdView(Context context) {
        return new AdlantisAdViewContainer(context);
    }

    public String getPublisherId() {
        return this.publisherId;
    }

    @Override // jp.adlantis.android.AdService
    public void pause() {
    }

    @Override // jp.adlantis.android.AdService
    public void resume() {
    }

    public void setPublisherId(String str) {
        this.publisherId = str;
    }
}
