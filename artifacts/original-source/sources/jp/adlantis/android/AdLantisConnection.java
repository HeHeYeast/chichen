package jp.adlantis.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdLantisConnection extends AdNetworkConnection {
    public AdLantisConnection() {
        this._host = "sp.ad.adlantis.jp";
        this._conversionTagHost = "sp.conv.adlantis.jp";
        this._conversionTagTestHost = "sp.www.adlantis.jp";
    }

    @Override // jp.adlantis.android.AdNetworkConnection
    public String publisherIDMetadataKey() {
        return "Adlantis_Publisher_ID";
    }
}
