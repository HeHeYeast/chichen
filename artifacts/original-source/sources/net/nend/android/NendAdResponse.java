package net.nend.android;

import android.text.TextUtils;
import net.nend.android.AdParameter;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdResponse implements AdParameter {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
    private final String mClickUrl;
    private final int mHeight;
    private final String mIconId;
    private final String mImageUrl;
    private final int mReloadIntervalInSeconds;
    private final String mTitleText;
    private final AdParameter.ViewType mViewType;
    private final String mWebViewUrl;
    private final int mWidth;

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
        private String mClickUrl;
        private int mHeight;
        private String mIconId;
        private String mImageUrl;
        private int mReloadIntervalInSeconds;
        private String mTitleText;
        private AdParameter.ViewType mViewType = AdParameter.ViewType.NONE;
        private String mWebViewUrl;
        private int mWidth;

        static {
            $assertionsDisabled = !NendAdResponse.class.desiredAssertionStatus();
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

        Builder setImageUrl(String imageUrl) {
            if (imageUrl != null) {
                this.mImageUrl = imageUrl.replaceAll(" ", "%20");
            } else {
                this.mImageUrl = null;
            }
            return this;
        }

        Builder setClickUrl(String clickUrl) {
            if (clickUrl != null) {
                this.mClickUrl = clickUrl.replaceAll(" ", "%20");
            } else {
                this.mClickUrl = null;
            }
            return this;
        }

        Builder setWebViewUrl(String webViewUrl) {
            if (webViewUrl != null) {
                this.mWebViewUrl = webViewUrl.replaceAll(" ", "%20");
            } else {
                this.mWebViewUrl = null;
            }
            return this;
        }

        Builder setTitleText(String titleText) {
            this.mTitleText = titleText;
            return this;
        }

        Builder setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
            this.mReloadIntervalInSeconds = reloadIntervalInSeconds;
            return this;
        }

        Builder setHeight(int height) {
            this.mHeight = height;
            return this;
        }

        Builder setWidth(int width) {
            this.mWidth = width;
            return this;
        }

        Builder setIconId(String iconId) {
            this.mIconId = iconId;
            return this;
        }

        NendAdResponse build() {
            return new NendAdResponse(this, null);
        }
    }

    private NendAdResponse(Builder builder) {
        switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[builder.mViewType.ordinal()]) {
            case 2:
                if (!TextUtils.isEmpty(builder.mImageUrl)) {
                    if (TextUtils.isEmpty(builder.mClickUrl)) {
                        throw new IllegalArgumentException("Click url is invalid");
                    }
                    this.mViewType = AdParameter.ViewType.ADVIEW;
                    this.mImageUrl = builder.mImageUrl;
                    this.mClickUrl = builder.mClickUrl;
                    this.mWebViewUrl = null;
                    this.mTitleText = builder.mTitleText;
                    this.mReloadIntervalInSeconds = builder.mReloadIntervalInSeconds;
                    this.mHeight = builder.mHeight;
                    this.mWidth = builder.mWidth;
                    this.mIconId = builder.mIconId;
                    return;
                }
                throw new IllegalArgumentException("Image url is invalid.");
            case 3:
                if (TextUtils.isEmpty(builder.mWebViewUrl)) {
                    throw new IllegalArgumentException("Web view url is invalid");
                }
                this.mViewType = AdParameter.ViewType.WEBVIEW;
                this.mImageUrl = null;
                this.mClickUrl = null;
                this.mWebViewUrl = builder.mWebViewUrl;
                this.mTitleText = null;
                this.mReloadIntervalInSeconds = 0;
                this.mHeight = builder.mHeight;
                this.mWidth = builder.mWidth;
                this.mIconId = null;
                return;
            default:
                throw new IllegalArgumentException("Uknown view type.");
        }
    }

    /* synthetic */ NendAdResponse(Builder builder, NendAdResponse nendAdResponse) {
        this(builder);
    }

    @Override // net.nend.android.AdParameter
    public AdParameter.ViewType getViewType() {
        return this.mViewType;
    }

    @Override // net.nend.android.AdParameter
    public String getImageUrl() {
        return this.mImageUrl;
    }

    @Override // net.nend.android.AdParameter
    public String getClickUrl() {
        return this.mClickUrl;
    }

    @Override // net.nend.android.AdParameter
    public String getWebViewUrl() {
        return this.mWebViewUrl;
    }

    @Override // net.nend.android.AdParameter
    public String getTitleText() {
        return this.mTitleText;
    }

    @Override // net.nend.android.AdParameter
    public int getWidth() {
        return this.mWidth;
    }

    @Override // net.nend.android.AdParameter
    public int getHeight() {
        return this.mHeight;
    }

    @Override // net.nend.android.AdParameter
    public int getReloadIntervalInSeconds() {
        return this.mReloadIntervalInSeconds;
    }

    @Override // net.nend.android.AdParameter
    public String getIconId() {
        return this.mIconId;
    }
}
