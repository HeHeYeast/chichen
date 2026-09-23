package com.idtinc.ckad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import jp.co.imobile.sdkads.android.FailNotificationReason;
import jp.co.imobile.sdkads.android.ImobileSdkAd;
import jp.co.imobile.sdkads.android.ImobileSdkAdListener;
import jp.co.voyagegroup.android.fluct.jar.FluctView;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdControlLayout extends FrameLayout {
    static final String IMOBILE_SDK_ADS_MEDIA_ID_BANNER = "161059";
    static final String IMOBILE_SDK_ADS_PUBLISHER_ID_BANNER = "19804";
    static final String IMOBILE_SDK_ADS_SPOT_ID_BANNER = "426255";
    private final float ADMOB_DISPLAY_SECOND;
    private final float AD_DISPLAY_SECOND_MIN;
    private final String AD_JSON_KEY0;
    private final String AD_JSON_KEY1;
    private final String AD_JSON_KEY2;
    private final String AD_JSON_REQUEST_URL;
    private final String AD_NEND_NID_FULL;
    private final String AD_NEND_NID_ICON;
    private final String AD_NEND_SID_FULL;
    private final String AD_NEND_SID_ICON;
    private final String AD_ZUCKS_MID;
    private final String AD_ZUCKS_MID_BANNER;
    private final String AD_ZUCKS_MID_FULL;
    private final float IMOBILE_DISPLAY_SECOND;
    private final String MY_AD_SECONDS;
    private final String MY_AD_URI;
    private final String MY_AD_URIPK;
    private final String MY_AD_URL0;
    private final String MY_AD_URL1;
    private final float ZUCKS_DISPLAY_SECOND;
    private float adMobDisplaySecond;
    private boolean adMobF;
    private AdView adMobView;
    private AppDelegate appDelegate;
    private int finalHeight;
    private int finalWidth;
    private FluctView fluctAdView;
    private FrameLayout fluctAdViewLayout;
    FrameLayout iMobileAdLayout;
    private float iMobileDisplaySecond;
    private boolean iMobileF;
    MyAdImageView myAdImageView;
    private Context myContext;
    MyFullAdLayout myFullAdLayout;
    private String new_zucksMID_Banner;
    private short nowAdStatus;
    private RequestAsyncTask requestAsyncTask;
    private float zoomRate;
    private float zucksDisplaySecond;
    private boolean zucksF;
    private String zucksMID_Banner;

    public AdControlLayout(Context _context, int _finalwidth, int _finalheight, float _zoomrate, MyFullAdLayout _myFullAdLayout) throws PackageManager.NameNotFoundException {
        super(_context);
        this.AD_JSON_REQUEST_URL = "http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php";
        this.AD_JSON_KEY0 = "ckcdAdMob_android";
        this.AD_JSON_KEY1 = "ckcdZucks_android";
        this.AD_JSON_KEY2 = "ckcdIMobile_android";
        this.AD_NEND_NID_ICON = "ckcdNendNID_icon_android";
        this.AD_NEND_SID_ICON = "ckcdNendSID_icon_android";
        this.AD_NEND_NID_FULL = "ckcdNendNID_full_android";
        this.AD_NEND_SID_FULL = "ckcdNendSID_full_android";
        this.AD_ZUCKS_MID = "ckcdZucksMID";
        this.AD_ZUCKS_MID_BANNER = "ckcdZucksMID_banner";
        this.AD_ZUCKS_MID_FULL = "ckcdZucksMID_full";
        this.MY_AD_URL0 = "ckcdMyAdUrl0_a";
        this.MY_AD_URL1 = "ckcdMyAdUrl1_a";
        this.MY_AD_URI = "ckcdMyAdUri_a";
        this.MY_AD_URIPK = "ckcdMyAdUriPk_a";
        this.MY_AD_SECONDS = "ckcdMyAdSed_a";
        this.ADMOB_DISPLAY_SECOND = 5.0f;
        this.ZUCKS_DISPLAY_SECOND = 5.0f;
        this.IMOBILE_DISPLAY_SECOND = 5.0f;
        this.AD_DISPLAY_SECOND_MIN = 5.0f;
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.zoomRate = 1.0f;
        this.nowAdStatus = (short) 0;
        this.adMobF = false;
        this.zucksF = false;
        this.iMobileF = false;
        this.adMobDisplaySecond = 5.0f;
        this.zucksDisplaySecond = 5.0f;
        this.iMobileDisplaySecond = 5.0f;
        this.requestAsyncTask = null;
        this.adMobView = null;
        this.new_zucksMID_Banner = "jgfmxi5f2f";
        this.zucksMID_Banner = "jgfmxi5f2f";
        this.fluctAdViewLayout = null;
        this.iMobileAdLayout = null;
        this.myAdImageView = null;
        this.myFullAdLayout = null;
        this.myContext = _context;
        this.appDelegate = (AppDelegate) this.myContext.getApplicationContext();
        this.myFullAdLayout = _myFullAdLayout;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.nowAdStatus = (short) 0;
        this.adMobF = false;
        this.zucksF = false;
        this.iMobileF = false;
        this.adMobDisplaySecond = 5.0f;
        this.zucksDisplaySecond = 5.0f;
        this.iMobileDisplaySecond = 5.0f;
        this.adMobView = new AdView((Activity) this.myContext);
        this.adMobView.setAdUnitId("ca-app-pub-8234307584269595/9998173063");
        this.adMobView.setAdSize(AdSize.BANNER);
        this.adMobView.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        addView(this.adMobView, this.finalWidth, this.finalHeight);
        this.adMobView.setVisibility(0);
        this.adMobView.setAdListener(new AdListener() { // from class: com.idtinc.ckad.AdControlLayout.1
            @Override // com.google.android.gms.ads.AdListener
            public void onAdClosed() {
                Log.d("AdControlLayout", "adView onAdClosed");
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLeftApplication() {
                Log.d("AdControlLayout", "adView onAdLeftApplication");
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdOpened() {
                Log.d("AdControlLayout", "adView onAdOpened");
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                Log.d("AdControlLayout", "adView onReceiveAd");
                AdControlLayout.this.changeAdMobF(true);
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int errorCode) {
                Log.d("AdControlLayout", "adView onAdFailedToLoad");
                AdControlLayout.this.changeAdMobF(false);
            }
        });
        this.adMobView.loadAd(new AdRequest.Builder().build());
        this.fluctAdViewLayout = new FrameLayout(getContext());
        this.fluctAdViewLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams fluctAdViewLayoutLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (this.zoomRate * 50.0f));
        fluctAdViewLayoutLayoutParams.leftMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        fluctAdViewLayoutLayoutParams.topMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        fluctAdViewLayoutLayoutParams.gravity = 51;
        this.fluctAdViewLayout.setVisibility(8);
        addView(this.fluctAdViewLayout, fluctAdViewLayoutLayoutParams);
        FrameLayout.LayoutParams fluctAdViewLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (this.zoomRate * 50.0f));
        fluctAdViewLayoutParams.leftMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        fluctAdViewLayoutParams.topMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        fluctAdViewLayoutParams.gravity = 51;
        this.fluctAdView = new FluctView(getContext(), this.zucksMID_Banner);
        this.fluctAdView.setBackgroundColor(0);
        this.fluctAdView.setVisibility(0);
        this.fluctAdViewLayout.addView(this.fluctAdView, fluctAdViewLayoutParams);
        doInitImobileBanner();
        doChangeAdStatus(2.0f);
    }

    public void doInitImobileBanner() throws PackageManager.NameNotFoundException {
        this.iMobileAdLayout = new FrameLayout(getContext());
        this.iMobileAdLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams iMobileAdLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (50.0f * this.zoomRate));
        iMobileAdLayoutParams.leftMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        iMobileAdLayoutParams.topMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        iMobileAdLayoutParams.gravity = 51;
        this.iMobileAdLayout.setVisibility(8);
        addView(this.iMobileAdLayout, iMobileAdLayoutParams);
        ImobileSdkAd.registerSpotInline((Activity) this.myContext, IMOBILE_SDK_ADS_PUBLISHER_ID_BANNER, IMOBILE_SDK_ADS_MEDIA_ID_BANNER, IMOBILE_SDK_ADS_SPOT_ID_BANNER);
        ImobileSdkAd.setImobileSdkAdListener(IMOBILE_SDK_ADS_SPOT_ID_BANNER, new ImobileSdkAdListener() { // from class: com.idtinc.ckad.AdControlLayout.2
            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdReadyCompleted() {
                AdControlLayout.this.iMobileF = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdShowCompleted() {
                AdControlLayout.this.iMobileF = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCliclkCompleted() {
                AdControlLayout.this.iMobileF = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCloseCompleted() {
                AdControlLayout.this.iMobileF = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onFailed(FailNotificationReason reason) {
                AdControlLayout.this.iMobileF = false;
            }
        });
        ImobileSdkAd.start(IMOBILE_SDK_ADS_SPOT_ID_BANNER);
        ImobileSdkAd.showAd((Activity) this.myContext, IMOBILE_SDK_ADS_SPOT_ID_BANNER, this.iMobileAdLayout);
    }

    public Boolean checkFluctBannerViewOkF(FluctView _fluctBannerView) {
        if (_fluctBannerView == null || _fluctBannerView.getChildCount() <= 0) {
            return false;
        }
        return true;
    }

    public void initFluctAdViewWithMid(String _zucksMID_Banner) {
        if (this.fluctAdViewLayout != null && _zucksMID_Banner != null && _zucksMID_Banner.length() > 0 && !this.zucksMID_Banner.equals(_zucksMID_Banner)) {
            this.zucksMID_Banner = _zucksMID_Banner;
            if (this.fluctAdView != null) {
                this.fluctAdView.setVisibility(8);
                this.fluctAdView.destroy();
                this.fluctAdView = null;
                this.fluctAdViewLayout.removeAllViews();
            }
            if (this.fluctAdView == null) {
                FrameLayout.LayoutParams fluctAdViewLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (50.0f * this.zoomRate));
                fluctAdViewLayoutParams.leftMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
                fluctAdViewLayoutParams.topMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
                fluctAdViewLayoutParams.gravity = 51;
                this.fluctAdView = new FluctView(getContext(), this.zucksMID_Banner);
                this.fluctAdView.setBackgroundColor(0);
                this.fluctAdView.setVisibility(0);
                this.fluctAdViewLayout.addView(this.fluctAdView, fluctAdViewLayoutParams);
            }
        }
    }

    public void initMyAdImageView() {
        if (this.myAdImageView == null) {
            this.myAdImageView = new MyAdImageView(getContext(), this.finalWidth, this.finalHeight, this.zoomRate);
            this.myAdImageView.setBackgroundColor(0);
            addView(this.myAdImageView, this.finalWidth, this.finalHeight);
            this.myAdImageView.setVisibility(8);
        }
    }

    public void doChangeAdStatus(float _waitSeconds) {
        if (this.appDelegate != null && this.nowAdStatus >= 0) {
            Log.d("AdControlLayout", "doChangeAdStatus _waitSeconds:" + _waitSeconds);
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.AdControlLayout.3
                @Override // java.lang.Runnable
                public void run() {
                    AdControlLayout.this.changeAdStatus();
                }
            }, (long) (1000.0f * _waitSeconds));
        }
    }

    public void changeAdStatus() {
        if (this.appDelegate != null) {
            if (this.appDelegate.getOnPauseF()) {
                if (this.nowAdStatus == 0) {
                    doChangeAdStatus(this.adMobDisplaySecond);
                    return;
                }
                if (this.nowAdStatus == 1) {
                    doChangeAdStatus(this.zucksDisplaySecond);
                    return;
                } else if (this.nowAdStatus == 2) {
                    doChangeAdStatus(this.iMobileDisplaySecond);
                    return;
                } else {
                    doChangeAdStatus(60.0f);
                    return;
                }
            }
            this.zucksF = checkFluctBannerViewOkF(this.fluctAdView).booleanValue();
            this.adMobView.setVisibility(0);
            this.fluctAdViewLayout.setVisibility(8);
            this.iMobileAdLayout.setVisibility(8);
            if (this.nowAdStatus == 0) {
                if (!this.zucksF) {
                    this.fluctAdViewLayout.setVisibility(8);
                    if (!this.iMobileF) {
                        this.adMobView.setVisibility(0);
                        this.iMobileAdLayout.setVisibility(8);
                        this.nowAdStatus = (short) 0;
                        doChangeAdStatus(this.adMobDisplaySecond);
                        Log.d("adStatus", "00:adMob display");
                        return;
                    }
                    this.adMobView.setVisibility(8);
                    this.iMobileAdLayout.setVisibility(0);
                    this.nowAdStatus = (short) 2;
                    doChangeAdStatus(this.iMobileDisplaySecond);
                    Log.d("adStatus", "02:adVpon display");
                    return;
                }
                if (this.zucksDisplaySecond < 5.0f) {
                    this.fluctAdViewLayout.setVisibility(8);
                    if (!this.iMobileF) {
                        this.adMobView.setVisibility(0);
                        this.iMobileAdLayout.setVisibility(8);
                        this.nowAdStatus = (short) 0;
                        doChangeAdStatus(this.adMobDisplaySecond);
                        Log.d("adStatus", "00:adMob display");
                        return;
                    }
                    this.adMobView.setVisibility(8);
                    this.iMobileAdLayout.setVisibility(0);
                    this.nowAdStatus = (short) 2;
                    doChangeAdStatus(this.iMobileDisplaySecond);
                    Log.d("adStatus", "02:adVpon display");
                    return;
                }
                this.fluctAdViewLayout.setVisibility(0);
                this.adMobView.setVisibility(8);
                this.iMobileAdLayout.setVisibility(8);
                this.nowAdStatus = (short) 1;
                doChangeAdStatus(this.zucksDisplaySecond);
                Log.d("adStatus", "01:zucks display");
                return;
            }
            if (this.nowAdStatus == 1) {
                if (!this.iMobileF) {
                    this.iMobileAdLayout.setVisibility(8);
                    if (!this.adMobF) {
                        this.adMobView.setVisibility(8);
                        this.fluctAdViewLayout.setVisibility(0);
                        this.nowAdStatus = (short) 1;
                        doChangeAdStatus(this.zucksDisplaySecond);
                        Log.d("adStatus", "11:zucks display");
                        return;
                    }
                    this.adMobView.setVisibility(0);
                    this.fluctAdViewLayout.setVisibility(8);
                    this.nowAdStatus = (short) 0;
                    doChangeAdStatus(this.adMobDisplaySecond);
                    Log.d("adStatus", "10:adMob display");
                    return;
                }
                if (this.iMobileDisplaySecond < 5.0f) {
                    this.iMobileAdLayout.setVisibility(8);
                    if (!this.adMobF) {
                        this.adMobView.setVisibility(8);
                        this.fluctAdViewLayout.setVisibility(0);
                        this.nowAdStatus = (short) 1;
                        doChangeAdStatus(this.zucksDisplaySecond);
                        Log.d("adStatus", "11:zucks display");
                        return;
                    }
                    this.adMobView.setVisibility(0);
                    this.fluctAdViewLayout.setVisibility(8);
                    this.nowAdStatus = (short) 0;
                    doChangeAdStatus(this.adMobDisplaySecond);
                    Log.d("adStatus", "10:adMob display");
                    return;
                }
                this.iMobileAdLayout.setVisibility(0);
                this.adMobView.setVisibility(8);
                this.fluctAdViewLayout.setVisibility(8);
                this.nowAdStatus = (short) 2;
                doChangeAdStatus(this.iMobileDisplaySecond);
                Log.d("adStatus", "12:adVpon display");
                return;
            }
            if (this.nowAdStatus == 2) {
                if (!this.adMobF) {
                    this.adMobView.setVisibility(8);
                    if (!this.zucksF) {
                        this.fluctAdViewLayout.setVisibility(8);
                        this.iMobileAdLayout.setVisibility(0);
                        this.nowAdStatus = (short) 2;
                        doChangeAdStatus(this.iMobileDisplaySecond);
                        Log.d("adStatus", "22:adVpon display");
                        return;
                    }
                    this.fluctAdViewLayout.setVisibility(0);
                    this.iMobileAdLayout.setVisibility(8);
                    this.nowAdStatus = (short) 1;
                    doChangeAdStatus(this.zucksDisplaySecond);
                    Log.d("adStatus", "21:zucks display");
                    return;
                }
                if (this.adMobDisplaySecond < 5.0f) {
                    this.adMobView.setVisibility(8);
                    if (!this.zucksF) {
                        this.fluctAdViewLayout.setVisibility(8);
                        this.iMobileAdLayout.setVisibility(0);
                        this.nowAdStatus = (short) 2;
                        doChangeAdStatus(this.iMobileDisplaySecond);
                        Log.d("adStatus", "22:adVpon display");
                        return;
                    }
                    this.fluctAdViewLayout.setVisibility(0);
                    this.iMobileAdLayout.setVisibility(8);
                    this.nowAdStatus = (short) 1;
                    doChangeAdStatus(this.zucksDisplaySecond);
                    Log.d("adStatus", "21:zucks display");
                    return;
                }
                this.adMobView.setVisibility(0);
                this.fluctAdViewLayout.setVisibility(8);
                this.iMobileAdLayout.setVisibility(8);
                this.nowAdStatus = (short) 0;
                doChangeAdStatus(this.adMobDisplaySecond);
                Log.d("adStatus", "20:adMob display");
            }
        }
    }

    void changeAdMobF(boolean _adMobF) {
        this.adMobF = _adMobF;
        Log.d("AdControlLayout", "adMobF = " + this.adMobF);
    }

    void changeZucksF(boolean _zucksF) {
        this.zucksF = _zucksF;
        Log.d("AdControlLayout", "zucksF = " + this.zucksF);
    }

    void changeIMobileF(boolean _iMobileF) {
        this.iMobileF = _iMobileF;
        Log.d("AdControlLayout", "iMobileF = " + this.iMobileF);
    }

    public void startRequest(boolean refreshF) {
        if (this.appDelegate != null) {
            clearRequestAsyncTask();
            if (this.appDelegate.checkInterNet()) {
                this.requestAsyncTask = new RequestAsyncTask(this, null);
                this.requestAsyncTask.execute("http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php");
            }
        }
    }

    public void clearRequestAsyncTask() {
        if (this.requestAsyncTask != null) {
            if (!this.requestAsyncTask.isCancelled()) {
                this.requestAsyncTask.cancel(true);
            }
            this.requestAsyncTask = null;
        }
    }

    private class RequestAsyncTask extends AsyncTask<String, Integer, JSONObject> {
        private RequestAsyncTask() {
        }

        /* synthetic */ RequestAsyncTask(AdControlLayout adControlLayout, RequestAsyncTask requestAsyncTask) {
            this();
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public JSONObject doInBackground(String... params) throws IOException {
            String urlString;
            if (AdControlLayout.this.appDelegate == null) {
                return null;
            }
            JSONObject json = null;
            try {
                String languageString = AdControlLayout.this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    urlString = String.valueOf("http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php") + "?language=ja";
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    urlString = String.valueOf("http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php") + "?language=tw";
                } else if (languageString.equals("zh-CN")) {
                    urlString = String.valueOf("http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php") + "?language=cn";
                } else {
                    urlString = String.valueOf("http://idtinc.home.dyndns.org/AdRotation/json_ck_cd_a.php") + "?language=en";
                }
                URL url = new URL(String.valueOf(urlString) + "&rd=" + AdControlLayout.this.appDelegate.getNowDateString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(HapticContentSDK.f17b04440444044404440444);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                if (jsonString != null) {
                    Log.i("`````@@@@@@@@@@@@@@@@@jsonString", "jsonString:" + jsonString);
                    try {
                        json = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reader.close();
                return json;
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                return json;
            } catch (IOException e3) {
                return json;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate((Object[]) progress);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(JSONObject _json) throws JSONException {
            String new_nendNID_Icon;
            int new_nendSID_Icon;
            String new_nendNID_Full;
            int new_nendSID_Full;
            String tkZucksMID;
            String tkZucksMID_banner;
            String tkZucksMID_full;
            int nend_full_activeInt;
            int zucks_full_activeInt;
            int imobile_full_activeInt;
            int imobile_wall_activeInt;
            int adcolony_interstisial_activeInt;
            int aoto_open_bonus_pageInt;
            int full_ad_next_display_cnt;
            int full_ad_active;
            if (AdControlLayout.this.appDelegate != null) {
                if (_json != null) {
                    float newAdMobDisplaySecond = BitmapDescriptorFactory.HUE_RED;
                    try {
                        int newAdMobDisplaySecondInt = _json.getInt("ckcdAdMob_android");
                        if (newAdMobDisplaySecondInt > 0) {
                            newAdMobDisplaySecond = newAdMobDisplaySecondInt / 10.0f;
                            Log.d("newAdMobDisplaySecond", "newAdMobDisplaySecond:" + newAdMobDisplaySecond);
                        }
                    } catch (JSONException e) {
                    }
                    if (newAdMobDisplaySecond > BitmapDescriptorFactory.HUE_RED) {
                        AdControlLayout.this.adMobDisplaySecond = newAdMobDisplaySecond;
                    }
                    float newZucksDisplaySecond = BitmapDescriptorFactory.HUE_RED;
                    try {
                        int newZucksDisplaySecondInt = _json.getInt("ckcdZucks_android");
                        if (newZucksDisplaySecondInt > 0) {
                            newZucksDisplaySecond = newZucksDisplaySecondInt / 10.0f;
                            Log.d("newZucksDisplaySecond", "newZucksDisplaySecond:" + newZucksDisplaySecond);
                        }
                    } catch (JSONException e2) {
                    }
                    if (newZucksDisplaySecond > BitmapDescriptorFactory.HUE_RED) {
                        AdControlLayout.this.zucksDisplaySecond = newZucksDisplaySecond;
                    }
                    try {
                        new_nendNID_Icon = _json.getString("ckcdNendNID_icon_android");
                        if (new_nendNID_Icon == null) {
                            new_nendNID_Icon = "";
                        }
                    } catch (JSONException e3) {
                        new_nendNID_Icon = "";
                    }
                    try {
                        new_nendSID_Icon = _json.getInt("ckcdNendSID_icon_android");
                    } catch (JSONException e4) {
                        new_nendSID_Icon = 0;
                    }
                    if (new_nendSID_Icon < 0) {
                        new_nendSID_Icon = 0;
                    }
                    if (new_nendNID_Icon.length() > 0 && new_nendSID_Icon > 0 && AdControlLayout.this.myFullAdLayout != null) {
                        AdControlLayout.this.myFullAdLayout.initNadIconView(new_nendNID_Icon, new_nendSID_Icon);
                    }
                    try {
                        new_nendNID_Full = _json.getString("ckcdNendNID_full_android");
                        if (new_nendNID_Full == null) {
                            new_nendNID_Full = "";
                        }
                    } catch (JSONException e5) {
                        new_nendNID_Full = "";
                    }
                    try {
                        new_nendSID_Full = _json.getInt("ckcdNendSID_full_android");
                    } catch (JSONException e6) {
                        new_nendSID_Full = 0;
                    }
                    if (new_nendSID_Full < 0) {
                        new_nendSID_Full = 0;
                    }
                    if (new_nendNID_Full.length() > 0 && new_nendSID_Full > 0 && AdControlLayout.this.myFullAdLayout != null) {
                        AdControlLayout.this.myFullAdLayout.initNadFullView0(new_nendNID_Full, new_nendSID_Full);
                    }
                    try {
                        tkZucksMID = _json.getString("ckcdZucksMID");
                        if (tkZucksMID == null) {
                            tkZucksMID = "";
                        }
                    } catch (JSONException e7) {
                        tkZucksMID = "";
                    }
                    if (tkZucksMID.length() > 0) {
                        AdControlLayout.this.initFluctAdViewWithMid(tkZucksMID);
                    }
                    try {
                        tkZucksMID_banner = _json.getString("ckcdZucksMID_banner");
                        if (tkZucksMID_banner == null) {
                            tkZucksMID_banner = "";
                        }
                    } catch (JSONException e8) {
                        tkZucksMID_banner = "";
                    }
                    if (tkZucksMID_banner.length() > 0 && AdControlLayout.this.myFullAdLayout != null) {
                        AdControlLayout.this.myFullAdLayout.initFluctAdViewWithMid(tkZucksMID_banner);
                    }
                    try {
                        tkZucksMID_full = _json.getString("ckcdZucksMID_full");
                        if (tkZucksMID_full == null) {
                            tkZucksMID_full = "";
                        }
                    } catch (JSONException e9) {
                        tkZucksMID_full = "";
                    }
                    if (tkZucksMID_full.length() > 0 && AdControlLayout.this.myFullAdLayout != null) {
                        AdControlLayout.this.myFullAdLayout.initFluctFullAdViewWithMid(tkZucksMID_full);
                    }
                    float newIMobileDisplaySecond = BitmapDescriptorFactory.HUE_RED;
                    try {
                        int newIMobileDisplaySecondInt = _json.getInt("ckcdIMobile_android");
                        if (newIMobileDisplaySecondInt > 0) {
                            newIMobileDisplaySecond = newIMobileDisplaySecondInt / 10.0f;
                            Log.d("newIMobileDisplaySecond", "newIMobileDisplaySecond:" + newIMobileDisplaySecond);
                        }
                    } catch (JSONException e10) {
                    }
                    if (newIMobileDisplaySecond > BitmapDescriptorFactory.HUE_RED) {
                        AdControlLayout.this.iMobileDisplaySecond = newIMobileDisplaySecond;
                    }
                    if (AdControlLayout.this.appDelegate.defaultSharedPreferences == null) {
                        AdControlLayout.this.appDelegate.defaultSharedPreferences = AdControlLayout.this.appDelegate.getSharedPreferences("default", 0);
                    }
                    if (AdControlLayout.this.appDelegate.defaultSharedPreferences != null) {
                        SharedPreferences.Editor editor = AdControlLayout.this.appDelegate.defaultSharedPreferences.edit();
                        try {
                            nend_full_activeInt = _json.getInt("nend_full_active");
                            if (nend_full_activeInt != 1) {
                                nend_full_activeInt = 0;
                            }
                        } catch (JSONException e11) {
                            nend_full_activeInt = 0;
                        }
                        if (nend_full_activeInt == 1) {
                            editor.putBoolean("nend_full_active", true);
                        } else {
                            editor.putBoolean("nend_full_active", false);
                        }
                        try {
                            zucks_full_activeInt = _json.getInt("zucks_full_active");
                            if (zucks_full_activeInt != 1) {
                                zucks_full_activeInt = 0;
                            }
                        } catch (JSONException e12) {
                            zucks_full_activeInt = 0;
                        }
                        if (zucks_full_activeInt == 1) {
                            editor.putBoolean("zucks_full_active", true);
                        } else {
                            editor.putBoolean("zucks_full_active", false);
                        }
                        try {
                            imobile_full_activeInt = _json.getInt("imobile_full_active");
                            if (imobile_full_activeInt != 1) {
                                imobile_full_activeInt = 0;
                            }
                        } catch (JSONException e13) {
                            imobile_full_activeInt = 0;
                        }
                        if (imobile_full_activeInt == 1) {
                            editor.putBoolean("imobile_full_active", true);
                        } else {
                            editor.putBoolean("imobile_full_active", false);
                        }
                        try {
                            imobile_wall_activeInt = _json.getInt("imobile_wall_active");
                            if (imobile_wall_activeInt != 1) {
                                imobile_wall_activeInt = 0;
                            }
                        } catch (JSONException e14) {
                            imobile_wall_activeInt = 0;
                        }
                        if (imobile_wall_activeInt == 1) {
                            editor.putBoolean("imobile_wall_active", true);
                        } else {
                            editor.putBoolean("imobile_wall_active", false);
                        }
                        try {
                            adcolony_interstisial_activeInt = _json.getInt("adcolony_interstisial_active");
                            if (adcolony_interstisial_activeInt != 1) {
                                adcolony_interstisial_activeInt = 0;
                            }
                        } catch (JSONException e15) {
                            adcolony_interstisial_activeInt = 0;
                        }
                        if (adcolony_interstisial_activeInt == 1) {
                            editor.putBoolean("adcolony_interstisial_active", true);
                        } else {
                            editor.putBoolean("adcolony_interstisial_active", false);
                        }
                        try {
                            aoto_open_bonus_pageInt = _json.getInt("aoto_open_bonus_page");
                            if (aoto_open_bonus_pageInt != 1) {
                                aoto_open_bonus_pageInt = 0;
                            }
                        } catch (JSONException e16) {
                            aoto_open_bonus_pageInt = 0;
                        }
                        if (aoto_open_bonus_pageInt == 1) {
                            editor.putBoolean("aoto_open_bonus_page", true);
                        } else {
                            editor.putBoolean("aoto_open_bonus_page", false);
                        }
                        try {
                            full_ad_next_display_cnt = _json.getInt("full_ad_next_display_cnt");
                            if (full_ad_next_display_cnt <= 0) {
                                full_ad_next_display_cnt = 0;
                            }
                        } catch (JSONException e17) {
                            full_ad_next_display_cnt = 0;
                        }
                        if (full_ad_next_display_cnt > 0) {
                            AdControlLayout.this.myFullAdLayout.setNewNextDisplayCnt(full_ad_next_display_cnt);
                        }
                        try {
                            int full_ad_active2 = _json.getInt("full_ad_active");
                            if (full_ad_active2 == 0) {
                                full_ad_active = 0;
                            } else {
                                full_ad_active = 1;
                            }
                        } catch (JSONException e18) {
                            full_ad_active = 1;
                        }
                        if (full_ad_active == 1) {
                            editor.putBoolean("full_ad_active", true);
                        } else {
                            editor.putBoolean("full_ad_active", false);
                        }
                        editor.commit();
                    }
                    String newMyAdUrl0 = null;
                    String newMyAdUrl1 = null;
                    String newMyAdUri = null;
                    String newMyAdUriPk = null;
                    float newMyAdSec = -1.0f;
                    try {
                        int newMyAdSecInt = _json.getInt("ckcdMyAdSed_a");
                        if (newMyAdSecInt > 0) {
                            newMyAdSec = newMyAdSecInt / 10.0f;
                            Log.d("newMyAdSec", "newMyAdSec:" + newMyAdSec);
                        }
                        newMyAdUrl0 = _json.getString("ckcdMyAdUrl0_a");
                        newMyAdUrl1 = _json.getString("ckcdMyAdUrl1_a");
                        newMyAdUri = _json.getString("ckcdMyAdUri_a");
                        newMyAdUriPk = _json.getString("ckcdMyAdUriPk_a");
                    } catch (JSONException e19) {
                    }
                    if (newMyAdUrl0 != null && newMyAdUrl1 != null && newMyAdUrl0.length() >= 10 && newMyAdUrl1.length() >= 10 && newMyAdSec >= 1.0f) {
                        AdControlLayout.this.initMyAdImageView();
                        if (AdControlLayout.this.myAdImageView != null) {
                            AdControlLayout.this.myAdImageView.requestStart(newMyAdUrl0, newMyAdUrl1, newMyAdUri, newMyAdUriPk, newMyAdSec);
                        }
                    }
                }
                super.onPostExecute((RequestAsyncTask) _json);
            }
        }
    }

    public void onDestroy() {
        this.nowAdStatus = (short) -1;
        clearRequestAsyncTask();
        if (this.myAdImageView != null) {
            this.myAdImageView.onDestroy();
            this.myAdImageView = null;
        }
        ImobileSdkAd.activityDestory();
        if (this.fluctAdView != null) {
            this.fluctAdView.destroy();
            this.fluctAdView = null;
        }
        if (this.fluctAdViewLayout != null) {
            this.fluctAdViewLayout.removeAllViews();
        }
        if (this.adMobView != null) {
            this.adMobView.destroy();
            this.adMobView = null;
        }
        removeAllViews();
        this.myFullAdLayout = null;
        this.myContext = null;
        this.appDelegate = null;
    }
}
