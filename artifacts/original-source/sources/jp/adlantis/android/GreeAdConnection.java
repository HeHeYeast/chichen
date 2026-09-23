package jp.adlantis.android;

import android.net.Uri;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GreeAdConnection extends AdNetworkConnection {
    public GreeAdConnection() {
        this._host = "sp.ad.gap.adlantis.jp";
        this._conversionTagHost = "sp.conv.gap.adlantis.jp";
        this._conversionTagTestHost = "sp.gap.developer.gree.co.jp";
    }

    @Override // jp.adlantis.android.AdNetworkConnection
    public Uri.Builder appendParameters(Uri.Builder builder) {
        String userId = GreeApiDelegator.getUserId();
        String sha1DigestInString = userId != null ? GreeApiDelegator.getSha1DigestInString(userId) : null;
        if (sha1DigestInString != null) {
            builder.appendQueryParameter("luid", "gree:" + sha1DigestInString);
        }
        builder.appendQueryParameter("partner", "gap");
        return builder;
    }

    @Override // jp.adlantis.android.AdNetworkConnection
    public String publisherIDMetadataKey() {
        return "GAP_Publisher_ID";
    }
}
