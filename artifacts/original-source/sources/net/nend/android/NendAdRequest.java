package net.nend.android;

import android.content.Context;
import android.net.Uri;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendAdRequest extends AbsNendAdRequest {
    public NendAdRequest(Context context, int spotId, String apiKey) {
        super(context, spotId, apiKey);
    }

    @Override // net.nend.android.AbsNendAdRequest
    String getDomain() {
        return "ad1.nend.net";
    }

    @Override // net.nend.android.AbsNendAdRequest
    String getPath() {
        return "na.php";
    }

    @Override // net.nend.android.AbsNendAdRequest
    String buildRequestUrl(String uid) {
        return new Uri.Builder().scheme(this.mProtocol).authority(this.mDomain).path(this.mPath).appendQueryParameter("apikey", this.mApiKey).appendQueryParameter("spot", String.valueOf(this.mSpotId)).appendQueryParameter("uid", uid).appendQueryParameter("os", getOS()).appendQueryParameter("version", getVersion()).appendQueryParameter("model", getModel()).appendQueryParameter("device", getDevice()).appendQueryParameter("localize", getLocale()).appendQueryParameter("sdkver", getSDKVersion()).toString();
    }
}
