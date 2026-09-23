package jp.co.voyagegroup.android.fluct.jar.db;

import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctInterstitialTable implements Serializable {
    private static final long serialVersionUID = 2562785579224548567L;
    private String mAdHtml;
    private int mDisplayRate;
    private int mHeight;
    private String mMediaId;
    private int mUpdateTime;
    private int mWidth;

    public void setMediaId(String mediaId) {
        this.mMediaId = mediaId;
    }

    public void setRate(int rate) {
        this.mDisplayRate = rate;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setAdHtml(String html) {
        this.mAdHtml = html;
    }

    public void setUpdateTime(int time) {
        this.mUpdateTime = time;
    }

    public String getMediaId() {
        return this.mMediaId;
    }

    public int getRate() {
        return this.mDisplayRate;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public String getAdHtml() {
        return this.mAdHtml;
    }

    public int getUpdateTime() {
        return this.mUpdateTime;
    }
}
