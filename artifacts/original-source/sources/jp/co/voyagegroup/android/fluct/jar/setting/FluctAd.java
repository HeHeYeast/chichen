package jp.co.voyagegroup.android.fluct.jar.setting;

import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctAd implements Serializable {
    private static final long serialVersionUID = -3573165148725762405L;
    private String mAdHtml;
    private String mBackColor;

    public String getBackColor() {
        return this.mBackColor;
    }

    public void setBackColor(String backColor) {
        this.mBackColor = backColor;
    }

    public String getAdHtml() {
        return this.mAdHtml;
    }

    public void setAdHtml(String adHtml) {
        this.mAdHtml = adHtml;
    }

    public String toString() {
        return "FluctAd [adHtml=" + this.mAdHtml + ", backColor=" + this.mBackColor + "]";
    }
}
