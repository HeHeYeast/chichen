package net.nend.android;

import android.content.Context;
import android.util.DisplayMetrics;
import java.io.IOException;
import java.lang.ref.WeakReference;
import net.nend.android.AdParameter;
import net.nend.android.DownloadTask;
import net.nend.android.NendAdView;
import net.nend.android.NendHelper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAd implements Ad, DownloadTask.Downloadable<AdParameter> {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Context mContext;
    private String mIconId;
    private DisplayMetrics mMetrics;
    private final NendAdRequest mRequest;
    private final String mUid;
    private AdParameter.ViewType mViewType = AdParameter.ViewType.NONE;
    private String mImageUrl = null;
    private String mClickUrl = null;
    private String mWebViewUrl = null;
    private String mTitleText = null;
    private int mWidth = 320;
    private int mHeight = 50;
    private int mReloadIntervalInSeconds = 60;
    private WeakReference<AdListener> mListenerReference = null;
    private DownloadTask<AdParameter> mTask = null;

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

    static {
        $assertionsDisabled = !NendAd.class.desiredAssertionStatus();
    }

    NendAd(Context context, int spotId, String apiKey, DisplayMetrics metrics) {
        if (context == null) {
            throw new NullPointerException("Context is null.");
        }
        if (spotId <= 0) {
            throw new IllegalArgumentException("Spot id is invalid. spot id : " + spotId);
        }
        if (apiKey == null || apiKey.length() == 0) {
            throw new IllegalArgumentException("Api key is invalid. api key : " + apiKey);
        }
        this.mContext = context;
        this.mMetrics = metrics;
        this.mRequest = new NendAdRequest(context, spotId, apiKey);
        this.mUid = NendHelper.makeUid(context);
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

    @Override // net.nend.android.Ad
    public String getUid() {
        return this.mUid;
    }

    @Override // net.nend.android.Ad
    public boolean isRequestable() {
        return this.mTask == null || this.mTask.isFinished();
    }

    @Override // net.nend.android.Ad
    public boolean requestAd() {
        if (!isRequestable()) {
            return false;
        }
        this.mTask = new DownloadTask<>(this);
        NendHelper.AsyncTaskHelper.execute(this.mTask, new Void[0]);
        return true;
    }

    @Override // net.nend.android.Ad
    public void cancelRequest() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
    }

    @Override // net.nend.android.Ad
    public void setListener(AdListener listener) {
        this.mListenerReference = new WeakReference<>(listener);
    }

    public AdListener getListener() {
        if (this.mListenerReference != null) {
            return this.mListenerReference.get();
        }
        return null;
    }

    @Override // net.nend.android.Ad
    public void removeListener() {
        this.mListenerReference = null;
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public String getRequestUrl() {
        return this.mRequest.getRequestUrl(this.mUid);
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public AdParameter makeResponse(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        try {
            return new NendAdResponseParser(this.mContext).parseResponse(EntityUtils.toString(entity));
        } catch (IOException e) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            NendLog.d(NendStatus.ERR_HTTP_REQUEST, e);
            return null;
        } catch (ParseException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            NendLog.d(NendStatus.ERR_HTTP_REQUEST, e2);
            return null;
        }
    }

    @Override // net.nend.android.DownloadTask.Downloadable
    public void onDownload(AdParameter response) {
        boolean isEnableDisplay = true;
        AdListener listener = getListener();
        if (response != null) {
            float density = this.mMetrics.density;
            float width = response.getWidth() * density;
            float height = response.getHeight() * density;
            if (width / 2.0f > this.mMetrics.widthPixels || height / 2.0f > this.mMetrics.heightPixels || width > this.mMetrics.widthPixels || height > this.mMetrics.heightPixels) {
                isEnableDisplay = false;
                if (listener != null) {
                    listener.onFailedToReceiveAd(NendAdView.NendError.AD_SIZE_TOO_LARGE);
                }
            }
            switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[response.getViewType().ordinal()]) {
                case 2:
                    setAdViewParam(response);
                    break;
                case 3:
                    setWebViewParam(response);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    if (listener != null) {
                        listener.onFailedToReceiveAd(NendAdView.NendError.INVALID_RESPONSE_TYPE);
                        return;
                    }
                    return;
            }
            if (listener != null && isEnableDisplay) {
                listener.onReceiveAd();
                return;
            }
            return;
        }
        if (listener != null) {
            listener.onFailedToReceiveAd(NendAdView.NendError.FAILED_AD_REQUEST);
        }
    }

    private void setAdViewParam(AdParameter response) {
        if (!$assertionsDisabled && response == null) {
            throw new AssertionError();
        }
        this.mViewType = AdParameter.ViewType.ADVIEW;
        this.mReloadIntervalInSeconds = NendHelper.setReloadIntervalInSeconds(response.getReloadIntervalInSeconds());
        this.mImageUrl = response.getImageUrl();
        this.mClickUrl = response.getClickUrl();
        this.mTitleText = response.getTitleText();
        this.mHeight = response.getHeight();
        this.mWidth = response.getWidth();
        this.mIconId = response.getIconId();
        this.mWebViewUrl = null;
    }

    private void setWebViewParam(AdParameter response) {
        if (!$assertionsDisabled && response == null) {
            throw new AssertionError();
        }
        this.mViewType = AdParameter.ViewType.WEBVIEW;
        this.mWebViewUrl = response.getWebViewUrl();
        this.mImageUrl = null;
        this.mClickUrl = null;
        this.mTitleText = null;
        this.mIconId = null;
        this.mHeight = response.getHeight();
        this.mWidth = response.getWidth();
    }
}
