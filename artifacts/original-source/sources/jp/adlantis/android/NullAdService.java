package jp.adlantis.android;

import android.content.Context;
import android.view.View;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NullAdService extends AdService {
    @Override // jp.adlantis.android.AdService
    public AdViewAdapter adViewAdapter(Context context) {
        return new NullAdViewAdapter(createAdView(context));
    }

    @Override // jp.adlantis.android.AdService
    public View createAdView(Context context) {
        return new NullAdView(context);
    }

    @Override // jp.adlantis.android.AdService
    public void pause() {
    }

    @Override // jp.adlantis.android.AdService
    public void resume() {
    }
}
