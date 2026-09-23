package net.nend.android;

import android.content.Context;
import android.net.Uri;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NendAdIconRequest extends AbsNendAdRequest {
    private int mRequestCount;

    NendAdIconRequest(Context context, int spotId, String apiKey) {
        super(context, spotId, apiKey);
    }

    @Override // net.nend.android.AbsNendAdRequest
    String getDomain() {
        return "ad3.nend.net";
    }

    @Override // net.nend.android.AbsNendAdRequest
    String getPath() {
        return "nia.php";
    }

    @Override // net.nend.android.AbsNendAdRequest
    String buildRequestUrl(String uid) {
        return new Uri.Builder().scheme(this.mProtocol).authority(this.mDomain).path(this.mPath).appendQueryParameter("apikey", this.mApiKey).appendQueryParameter("spot", String.valueOf(this.mSpotId)).appendQueryParameter("uid", uid).appendQueryParameter("os", getOS()).appendQueryParameter("version", getVersion()).appendQueryParameter("model", getModel()).appendQueryParameter("device", getDevice()).appendQueryParameter("localize", getLocale()).appendQueryParameter("sdkver", getSDKVersion()).appendQueryParameter("ad_num", String.valueOf(getRequestCount())).toString();
    }

    void setRequestCount(int count) {
        this.mRequestCount = count;
    }

    int getRequestCount() {
        return this.mRequestCount;
    }
}
