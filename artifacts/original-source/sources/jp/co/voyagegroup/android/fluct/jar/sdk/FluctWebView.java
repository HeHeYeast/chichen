package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Picture;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctAd;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;
import jp.co.voyagegroup.android.fluct.jar.web.FluctWebViewClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctWebView extends WebView implements WebView.PictureListener {
    private static final String TAG = "FluctWebView";
    private boolean mSettingFlag;

    public FluctWebView(Context context, FluctSetting fluctSetting) {
        super(context);
        this.mSettingFlag = false;
        Log.d(TAG, "new FluctWebView : ");
        initFluctWebView(context, fluctSetting);
        setWebViewClient(new FluctWebViewClient());
    }

    public FluctWebView(Context context, FluctSetting fluctSetting, FluctInterstitialActivity activity) {
        super(context);
        this.mSettingFlag = false;
        Log.d(TAG, "new FluctWebView : Interstitial");
        initFluctWebView(context, fluctSetting);
        setWebViewClient(new FluctWebViewClient(activity));
    }

    private void initFluctWebView(Context context, FluctSetting fluctSetting) {
        getSettings().setJavaScriptEnabled(true);
        setVerticalScrollbarOverlay(true);
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion == 7) {
            setPictureListener(this);
        }
        setDatabase(context, getSettings());
        setHorizontalScrollBarEnabled(false);
        setBackgroundColor(0);
        setVisibility(8);
        setWebViewClient(new FluctWebViewClient());
        String addUserAgent = fluctSetting.getUserAgent();
        if (addUserAgent != null) {
            String userAgent = getSettings().getUserAgentString();
            getSettings().setUserAgentString(String.valueOf(userAgent) + addUserAgent);
        }
    }

    @Override // android.webkit.WebView
    public void destroy() {
        Log.d(TAG, "destroy : ");
        loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);
        stopLoading();
        setWebChromeClient(null);
        setWebViewClient(null);
        clearFocus();
        setVisibility(8);
        super.destroy();
    }

    public void setAdHtml(FluctAd fluctAd) {
        Log.d(TAG, "setAdHtml : ");
        String adHtml = processAdHtml(fluctAd.getAdHtml(), fluctAd.getBackColor());
        Log.v(TAG, "setAdHtml : adHtml is " + adHtml);
        loadDataWithBaseURL("http://fluct.jp", adHtml, "text/html", "UTF-8", null);
    }

    public void setAdHtml(String adHtml) {
        Log.d(TAG, "setAdHtml : String ");
        String replaceAdHtml = FluctUtils.replaceParams(getContext(), adHtml);
        Log.v(TAG, "setAdHtml : adHtml is " + adHtml);
        loadDataWithBaseURL("http://fluct.jp", replaceAdHtml, "text/html", "UTF-8", null);
    }

    private void setDatabase(Context context, WebSettings setting) {
        Log.d(TAG, "setDatabase : ");
        String dataPath = "data/data/" + context.getApplicationContext().getPackageName() + "/database";
        setting.setDatabasePath(dataPath);
        setting.setDomStorageEnabled(true);
        setting.setGeolocationEnabled(true);
    }

    private String processAdHtml(String adHtml, String color) {
        Log.d(TAG, "processAdHtml : color is " + color);
        String result = adHtml;
        if (color != null && !"".equals(color)) {
            String addColor = "<style type=\"text/css\">body{background-color:#" + color + ";}</style>";
            String[] adHtmls = result.split("</head>");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(adHtmls[0]);
            stringBuffer.append(addColor);
            stringBuffer.append("</head>");
            stringBuffer.append(adHtmls[1]);
            result = stringBuffer.toString();
            try {
                setBackgroundColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "processAdHtml : IllegalArgumentException is " + e.getLocalizedMessage());
            }
        }
        String result2 = FluctUtils.replaceParams(getContext(), result);
        Log.v(TAG, "processAdHtml : adHtml is " + result2);
        return result2;
    }

    @Override // android.webkit.WebView.PictureListener
    public void onNewPicture(WebView webview, Picture picture) {
        Log.d(TAG, "onNewPicture : ");
        if (!this.mSettingFlag) {
            Log.v(TAG, "onNewPicture : picture  height is " + picture.getHeight() + ":" + webview.getLayoutParams().height);
            if (picture.getHeight() != 0) {
                ViewGroup.LayoutParams layoutParam = webview.getLayoutParams();
                layoutParam.height = (int) (picture.getHeight() * webview.getScale());
                webview.setLayoutParams(layoutParam);
                this.mSettingFlag = true;
            }
        }
    }
}
