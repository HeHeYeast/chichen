package net.nend.android;

import android.text.TextUtils;
import java.util.ArrayList;
import net.nend.android.AdParameter;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdIconResponse {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
    private ArrayList<AdParameter> mAdParameterList;
    private String mImpressionCountUrl;
    private String mMessage;
    private final int mReloadIntervalInSeconds;
    private int mStatusCode;
    private final AdParameter.ViewType mViewType;

    static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType() {
        int[] iArr = $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
        if (iArr == null) {
            iArr = new int[AdParameter.ViewType.valuesCustom().length];
            try {
                iArr[AdParameter.ViewType.ADVIEW.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdParameter.ViewType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AdParameter.ViewType.WEBVIEW.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$net$nend$android$AdParameter$ViewType = iArr;
        }
        return iArr;
    }

    static final class Builder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private ArrayList<AdParameter> mAdParameterList;
        private String mImpressionCountUrl;
        private String mMessage;
        private int mReloadIntervalInSeconds;
        private int mStatusCode;
        private AdParameter.ViewType mViewType = AdParameter.ViewType.NONE;

        static {
            $assertionsDisabled = !NendAdIconResponse.class.desiredAssertionStatus();
        }

        Builder() {
        }

        Builder setViewType(AdParameter.ViewType viewType) {
            if (!$assertionsDisabled && viewType == null) {
                throw new AssertionError();
            }
            this.mViewType = viewType;
            return this;
        }

        Builder setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
            this.mReloadIntervalInSeconds = reloadIntervalInSeconds;
            return this;
        }

        Builder setStatusCode(int statusCode) {
            this.mStatusCode = statusCode;
            return this;
        }

        Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        Builder setImpressionCountUrl(String impressionCountUrl) {
            if (impressionCountUrl != null) {
                this.mImpressionCountUrl = impressionCountUrl.replaceAll(" ", "%20");
            } else {
                this.mImpressionCountUrl = null;
            }
            return this;
        }

        Builder setAdParameterList(ArrayList<AdParameter> adParameterList) {
            this.mAdParameterList = adParameterList;
            return this;
        }

        NendAdIconResponse build() {
            return new NendAdIconResponse(this, null);
        }
    }

    private NendAdIconResponse(Builder builder) {
        switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[builder.mViewType.ordinal()]) {
            case 2:
                if (TextUtils.isEmpty(builder.mImpressionCountUrl)) {
                    throw new IllegalArgumentException("ImpressionCount Url is invalid.");
                }
                this.mViewType = AdParameter.ViewType.ADVIEW;
                this.mReloadIntervalInSeconds = builder.mReloadIntervalInSeconds;
                this.mStatusCode = builder.mStatusCode;
                this.mMessage = builder.mMessage;
                this.mImpressionCountUrl = builder.mImpressionCountUrl;
                this.mAdParameterList = builder.mAdParameterList;
                return;
            default:
                throw new IllegalArgumentException("Unknown view type.");
        }
    }

    /* synthetic */ NendAdIconResponse(Builder builder, NendAdIconResponse nendAdIconResponse) {
        this(builder);
    }

    public AdParameter.ViewType getViewType() {
        return this.mViewType;
    }

    public int getReloadIntervalInSeconds() {
        return this.mReloadIntervalInSeconds;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public String getImpressionCountUrl() {
        return this.mImpressionCountUrl;
    }

    public ArrayList<AdParameter> getAdParameterList() {
        return this.mAdParameterList;
    }
}
