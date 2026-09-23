package jp.co.voyagegroup.android.fluct.jar.setting;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctConversionEntity implements Serializable {
    private static final long serialVersionUID = 3538379580278642662L;
    private String mBrowserOpenUrl;
    private ArrayList<String> mConvUrl;

    public String getBrowserOpenUrl() {
        return this.mBrowserOpenUrl;
    }

    public void setBrowserOpenUrl(String browserOpenUrl) {
        this.mBrowserOpenUrl = browserOpenUrl;
    }

    public ArrayList<String> getConvUrl() {
        return this.mConvUrl;
    }

    public void setConvUrl(ArrayList<String> convUrl) {
        this.mConvUrl = convUrl;
    }

    public String toString() {
        return "FluctConversionEntity [convUrl=" + this.mConvUrl + ", browserOpenUrl=" + this.mBrowserOpenUrl + "]";
    }
}
