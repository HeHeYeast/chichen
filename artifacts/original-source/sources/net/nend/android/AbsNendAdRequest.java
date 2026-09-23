package net.nend.android;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.util.Locale;
import net.nend.android.NendConstants;
import net.nend.android.NendHelper;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class AbsNendAdRequest {
    protected final String mApiKey;
    protected final String mDomain;
    protected final String mPath;
    protected final String mProtocol;
    protected final int mSpotId;

    abstract String buildRequestUrl(String str);

    abstract String getDomain();

    abstract String getPath();

    AbsNendAdRequest(Context context, int spotId, String apiKey) {
        if (context == null) {
            throw new NullPointerException("Context is null.");
        }
        if (spotId <= 0) {
            throw new IllegalArgumentException("Spot id is invalid. spot id : " + spotId);
        }
        if (TextUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("Api key is invalid. api key : " + apiKey);
        }
        this.mSpotId = spotId;
        this.mApiKey = apiKey;
        this.mProtocol = NendHelper.MetaDataHelper.getStringValue(context, NendConstants.MetaData.ADSCHEME.getName(), "http");
        this.mDomain = NendHelper.MetaDataHelper.getStringValue(context, NendConstants.MetaData.ADAUTHORITY.getName(), getDomain());
        this.mPath = NendHelper.MetaDataHelper.getStringValue(context, NendConstants.MetaData.ADPATH.getName(), getPath());
    }

    protected String getOS() {
        return "android";
    }

    protected String getSDKVersion() {
        return "2.3.3";
    }

    protected String getModel() {
        return Build.MODEL;
    }

    protected String getDevice() {
        return Build.DEVICE;
    }

    protected String getLocale() {
        return Locale.getDefault().toString();
    }

    protected String getVersion() {
        return Build.VERSION.RELEASE;
    }

    String getRequestUrl(String uid) {
        if (TextUtils.isEmpty(uid)) {
            throw new IllegalArgumentException("UID is invalid. uid : " + uid);
        }
        return buildRequestUrl(uid);
    }
}
