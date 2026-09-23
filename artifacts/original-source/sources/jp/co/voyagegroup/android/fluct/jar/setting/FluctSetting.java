package jp.co.voyagegroup.android.fluct.jar.setting;

import java.io.Serializable;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.db.FluctInterstitialTable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctSetting implements Serializable {
    private static final long serialVersionUID = -3651259190832877939L;
    private ArrayList<Animation> mAnimations;
    private int mBrowser;
    private String mErrorMessages;
    private FluctAd mFluctAd;
    private FluctConversionEntity mFluctConversion;
    private FluctInterstitialTable mFluctInterstitial;
    private long mLoadTime;
    private String mMediaId;
    private String mMode;
    private long mRefreshTime;
    private String mUserAgent;

    public ArrayList<Animation> getAnimations() {
        return this.mAnimations;
    }

    public void setAnimations(ArrayList<Animation> animations) {
        this.mAnimations = animations;
    }

    public String getMediaId() {
        return this.mMediaId;
    }

    public void setMediaId(String mediaId) {
        this.mMediaId = mediaId;
    }

    public FluctAd getFluctAd() {
        return this.mFluctAd;
    }

    public void setFluctAd(FluctAd fluctAd) {
        this.mFluctAd = fluctAd;
    }

    public FluctConversionEntity getFluctConversion() {
        return this.mFluctConversion;
    }

    public void setFluctConversion(FluctConversionEntity fluctConversion) {
        this.mFluctConversion = fluctConversion;
    }

    public String getMode() {
        return this.mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public long getRefreshTime() {
        return this.mRefreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.mRefreshTime = refreshTime;
    }

    public int getBrowser() {
        return this.mBrowser;
    }

    public void setBrowser(int browser) {
        this.mBrowser = browser;
    }

    public String getErrorMessages() {
        return this.mErrorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.mErrorMessages = errorMessages;
    }

    public long getLoadTime() {
        return this.mLoadTime;
    }

    public void setLoadTime(long loadTime) {
        this.mLoadTime = loadTime;
    }

    public String getUserAgent() {
        return this.mUserAgent;
    }

    public void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent.replace("\n", "").replace("\r", "");
    }

    public FluctInterstitialTable getFluctInterstitial() {
        return this.mFluctInterstitial;
    }

    public void setFluctInterstitial(FluctInterstitialTable interstitial) {
        this.mFluctInterstitial = interstitial;
    }

    public String toString() {
        return "FluctSetting [mMediaId=" + this.mMediaId + ", mFluctAd=" + this.mFluctAd + ", mFluctConversion=" + this.mFluctConversion + ", mMode=" + this.mMode + ", mRefreshTime=" + this.mRefreshTime + ", mBrowser=" + this.mBrowser + ", mErrorMessages=" + this.mErrorMessages + ", mLoadTime=" + this.mLoadTime + "]";
    }

    public static class Animation implements Serializable {
        private static final long serialVersionUID = 0;
        private int mDuration;
        private int mType;

        public Animation(int type, int duration) {
            this.mType = type;
            this.mDuration = duration;
        }

        public int getType() {
            return this.mType;
        }

        public void setType(int type) {
            this.mType = type;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public void setDuration(int duration) {
            this.mDuration = duration;
        }
    }
}
