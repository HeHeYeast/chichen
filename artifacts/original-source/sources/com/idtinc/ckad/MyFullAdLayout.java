package com.idtinc.ckad;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;
import com.idtinc.ckchickandduck.R;
import jp.co.imobile.sdkads.android.FailNotificationReason;
import jp.co.imobile.sdkads.android.ImobileIconParams;
import jp.co.imobile.sdkads.android.ImobileSdkAd;
import jp.co.imobile.sdkads.android.ImobileSdkAdListener;
import jp.co.voyagegroup.android.fluct.jar.FluctView;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import net.nend.android.NendAdIconLoader;
import net.nend.android.NendAdIconView;
import net.nend.android.NendAdListener;
import net.nend.android.NendAdView;
import net.nend.android.NendIconError;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MyFullAdLayout extends FrameLayout implements NendAdListener {
    static final String IMOBILE_SDK_ADS_MEDIA_ID_FULL = "161059";
    static final String IMOBILE_SDK_ADS_MEDIA_ID_ICON = "161059";
    static final String IMOBILE_SDK_ADS_PUBLISHER_ID_FULL = "19804";
    static final String IMOBILE_SDK_ADS_PUBLISHER_ID_ICON = "19804";
    static final String IMOBILE_SDK_ADS_SPOT_ID_FULL = "426256";
    static final String IMOBILE_SDK_ADS_SPOT_ID_ICON = "426257";
    private final int CLOSE_BUTTON_DELAY_SECONDS;
    private final int LOOP_INTERVAL_SECONDS;
    int NEXT_DISPLAY_CNT;
    AdView adMobFullView0;
    FrameLayout adMobFullView0RectView;
    short adMobFullViewStatus;
    AdView adMobViewBottom;
    Boolean adToastF;
    private AppDelegate appDelegate;
    private AppMainActivity appMainActivity;
    FrameLayout bottomIconsView;
    FrameLayout bottomImobileIconsView;
    ImageButton closeButton;
    private int finalHeight;
    private int finalWidth;
    private FluctView fluctAdView;
    private FrameLayout fluctAdViewLayout;
    private FluctView fluctFullAdView;
    private FrameLayout fluctFullAdViewLayout;
    FrameLayout iMobileFulAdLayout;
    IconAdView iconAdView0;
    IconAdView iconAdView1;
    IconAdView iconFullAdView0;
    Boolean imobileAdsFullStatus;
    private Context myContext;
    FrameLayout nadFullLayout0;
    NendAdView nadFullView0;
    float nadFullView0Height;
    float nadFullView0Width;
    short nadFullViewStatus;
    NendAdIconLoader nadIconLoader;
    NendAdIconView nadIconView0;
    NendAdIconView nadIconView1;
    NendAdIconView nadIconView2;
    NendAdIconView nadIconView3;
    NendAdIconView nadIconView4;
    NendAdIconView nadIconView5;
    private String nendNID_Full;
    private String nendNID_Icon;
    private int nendSID_Full;
    private int nendSID_Icon;
    int nextDisplayCnt;
    short nowStatus;
    private float zoomRate;
    private String zucksMID_Banner;
    private String zucksMID_Full;

    public MyFullAdLayout(Context _context, int _finalwidth, int _finalheight, float _zoomrate, AppMainActivity _appMainActivity) throws PackageManager.NameNotFoundException {
        String iconFullAdView0UrlString;
        String iconAdView0UrlString;
        String iconAdView1UrlString;
        super(_context);
        this.LOOP_INTERVAL_SECONDS = 60000;
        this.CLOSE_BUTTON_DELAY_SECONDS = GamesClient.STATUS_ACHIEVEMENT_UNLOCK_FAILURE;
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.zoomRate = 1.0f;
        this.adToastF = false;
        this.nadFullView0Width = 300.0f;
        this.nadFullView0Height = 250.0f;
        this.nadFullViewStatus = (short) -1;
        this.adMobFullViewStatus = (short) -1;
        this.NEXT_DISPLAY_CNT = 8;
        this.nextDisplayCnt = this.NEXT_DISPLAY_CNT;
        this.nowStatus = (short) -2;
        this.closeButton = null;
        this.bottomImobileIconsView = null;
        this.bottomIconsView = null;
        this.nadIconLoader = null;
        this.nadFullLayout0 = null;
        this.nadFullView0 = null;
        this.adMobViewBottom = null;
        this.adMobFullView0 = null;
        this.iMobileFulAdLayout = null;
        this.imobileAdsFullStatus = true;
        this.zucksMID_Full = "e4ufmnhcjz";
        this.fluctFullAdViewLayout = null;
        this.fluctFullAdView = null;
        this.zucksMID_Banner = "z26j8z46rm";
        this.fluctAdViewLayout = null;
        this.iconFullAdView0 = null;
        this.iconAdView0 = null;
        this.iconAdView1 = null;
        this.nendNID_Icon = "fd10615e68fc1dc63ece1e7faa1a4e036e032d08";
        this.nendSID_Icon = 133767;
        this.nendNID_Full = "325709aa54ce94997bb2ccfdc1bb8e709dcb3906";
        this.nendSID_Full = 133771;
        this.myContext = _context;
        this.appDelegate = (AppDelegate) this.myContext.getApplicationContext();
        this.appMainActivity = _appMainActivity;
        float density = this.appDelegate != null ? this.appDelegate.density : 1.0f;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        setVisibility(8);
        this.nadFullViewStatus = (short) -1;
        this.adMobFullViewStatus = (short) -1;
        this.nextDisplayCnt = this.NEXT_DISPLAY_CNT;
        this.nowStatus = (short) -2;
        this.closeButton = new ImageButton(_context);
        FrameLayout.LayoutParams closeButtonParams = new FrameLayout.LayoutParams((int) (28.0d * this.zoomRate), (int) (28.0d * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            closeButtonParams.leftMargin = (int) (0.0d * this.zoomRate);
            closeButtonParams.topMargin = (int) (0.0d * this.zoomRate);
        } else {
            closeButtonParams.leftMargin = (int) (10.0d * this.zoomRate);
            closeButtonParams.topMargin = (int) (20.0d * this.zoomRate);
        }
        this.closeButton.setBackgroundResource(R.drawable.white_batsu_0);
        this.closeButton.setVisibility(8);
        closeButtonParams.gravity = 51;
        this.closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.idtinc.ckad.MyFullAdLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                MyFullAdLayout.this.close();
            }
        });
        addView(this.closeButton, closeButtonParams);
        this.bottomImobileIconsView = new FrameLayout(getContext());
        this.bottomImobileIconsView.setBackgroundColor(0);
        FrameLayout.LayoutParams bottomImobileIconsViewLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (70.0f * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            bottomImobileIconsViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            bottomImobileIconsViewLayoutParams.topMargin = (int) (362.0f * this.zoomRate);
        } else {
            bottomImobileIconsViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            bottomImobileIconsViewLayoutParams.topMargin = (int) (436.0f * this.zoomRate);
        }
        bottomImobileIconsViewLayoutParams.gravity = 51;
        this.bottomImobileIconsView.setVisibility(8);
        addView(this.bottomImobileIconsView, bottomImobileIconsViewLayoutParams);
        ImobileSdkAd.registerSpotInline((Activity) this.myContext, "19804", "161059", IMOBILE_SDK_ADS_SPOT_ID_ICON);
        ImobileSdkAd.start(IMOBILE_SDK_ADS_SPOT_ID_ICON);
        ImobileIconParams imobileIconParams = new ImobileIconParams();
        imobileIconParams.setIconSize(57);
        imobileIconParams.setIconNumber(4);
        imobileIconParams.setIconTitleEnable(false);
        imobileIconParams.setIconTitleShadowEnable(false);
        imobileIconParams.setIconTitleFontColor("#000000");
        ImobileSdkAd.showAd((Activity) this.myContext, IMOBILE_SDK_ADS_SPOT_ID_ICON, this.bottomImobileIconsView, imobileIconParams);
        this.bottomIconsView = new FrameLayout(getContext());
        this.bottomIconsView.setBackgroundColor(0);
        FrameLayout.LayoutParams bottomIconsViewLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (75.0f * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            bottomIconsViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            bottomIconsViewLayoutParams.topMargin = (int) (359.0f * this.zoomRate);
        } else {
            bottomIconsViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            bottomIconsViewLayoutParams.topMargin = (int) (433.0f * this.zoomRate);
        }
        bottomIconsViewLayoutParams.gravity = 51;
        this.bottomIconsView.setVisibility(8);
        addView(this.bottomIconsView, bottomIconsViewLayoutParams);
        this.nadIconView0 = new NendAdIconView(_context);
        this.nadIconView0.setBackgroundColor(0);
        this.nadIconView0.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView0LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            nadIconView0LayoutParams.leftMargin = (int) (1.0d * this.zoomRate);
            nadIconView0LayoutParams.topMargin = (int) (33.0d * this.zoomRate);
        } else {
            nadIconView0LayoutParams.leftMargin = (int) (1.0d * this.zoomRate);
            nadIconView0LayoutParams.topMargin = (int) (77.0d * this.zoomRate);
        }
        nadIconView0LayoutParams.gravity = 51;
        this.nadIconView0.setId(0);
        addView(this.nadIconView0, nadIconView0LayoutParams);
        this.nadIconView1 = new NendAdIconView(_context);
        this.nadIconView1.setBackgroundColor(0);
        this.nadIconView1.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView1LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            nadIconView1LayoutParams.leftMargin = (int) (244.0d * this.zoomRate);
            nadIconView1LayoutParams.topMargin = (int) (33.0d * this.zoomRate);
        } else {
            nadIconView1LayoutParams.leftMargin = (int) (244.0d * this.zoomRate);
            nadIconView1LayoutParams.topMargin = (int) (77.0d * this.zoomRate);
        }
        nadIconView1LayoutParams.gravity = 51;
        this.nadIconView1.setId(1);
        addView(this.nadIconView1, nadIconView1LayoutParams);
        this.nadIconView2 = new NendAdIconView(_context);
        this.nadIconView2.setBackgroundColor(0);
        this.nadIconView2.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView2LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        nadIconView2LayoutParams.leftMargin = (int) (1.0f * this.zoomRate);
        nadIconView2LayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        nadIconView2LayoutParams.gravity = 51;
        this.nadIconView2.setId(2);
        this.nadIconView2.setVisibility(8);
        this.bottomIconsView.addView(this.nadIconView2, nadIconView2LayoutParams);
        this.nadIconView3 = new NendAdIconView(_context);
        this.nadIconView3.setBackgroundColor(0);
        this.nadIconView3.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView3LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        nadIconView3LayoutParams.leftMargin = (int) (82.0f * this.zoomRate);
        nadIconView3LayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        nadIconView3LayoutParams.gravity = 51;
        this.nadIconView3.setId(3);
        this.nadIconView3.setVisibility(8);
        this.bottomIconsView.addView(this.nadIconView3, nadIconView3LayoutParams);
        this.nadIconView4 = new NendAdIconView(_context);
        this.nadIconView4.setBackgroundColor(0);
        this.nadIconView4.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView4LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        nadIconView4LayoutParams.leftMargin = (int) (163.0f * this.zoomRate);
        nadIconView4LayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        nadIconView4LayoutParams.gravity = 51;
        this.nadIconView4.setId(4);
        this.nadIconView4.setVisibility(8);
        this.bottomIconsView.addView(this.nadIconView4, nadIconView4LayoutParams);
        this.nadIconView5 = new NendAdIconView(_context);
        this.nadIconView5.setBackgroundColor(0);
        this.nadIconView5.setTitleVisible(false);
        FrameLayout.LayoutParams nadIconView5LayoutParams = new FrameLayout.LayoutParams((int) (75.0d * this.zoomRate), (int) (75.0d * this.zoomRate));
        nadIconView5LayoutParams.leftMargin = (int) (244.0f * this.zoomRate);
        nadIconView5LayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        nadIconView5LayoutParams.gravity = 51;
        this.nadIconView5.setId(5);
        this.nadIconView5.setVisibility(8);
        this.bottomIconsView.addView(this.nadIconView5, nadIconView5LayoutParams);
        this.nadIconLoader = new NendAdIconLoader(_context, this.nendSID_Icon, this.nendNID_Icon);
        this.nadIconLoader.addIconView(this.nadIconView0);
        this.nadIconLoader.addIconView(this.nadIconView1);
        this.nadIconLoader.addIconView(this.nadIconView2);
        this.nadIconLoader.addIconView(this.nadIconView3);
        this.nadIconLoader.addIconView(this.nadIconView4);
        this.nadIconLoader.addIconView(this.nadIconView5);
        this.nadIconLoader.setOnReceiveLisner(new NendAdIconLoader.OnReceiveListner() { // from class: com.idtinc.ckad.MyFullAdLayout.2
            @Override // net.nend.android.NendAdIconLoader.OnReceiveListner
            public void onReceiveAd(NendAdIconView iconView) {
                if (MyFullAdLayout.this.adToastF.booleanValue()) {
                    Toast.makeText(MyFullAdLayout.this.appDelegate, "收到nand icon " + iconView.getId(), 0).show();
                }
                iconView.setVisibility(0);
                MyFullAdLayout.this.checkIconOrBannerBottom();
            }
        });
        this.nadIconLoader.setOnClickListner(new NendAdIconLoader.OnClickListner() { // from class: com.idtinc.ckad.MyFullAdLayout.3
            @Override // net.nend.android.NendAdIconLoader.OnClickListner
            public void onClick(NendAdIconView iconView) {
                MyFullAdLayout.this.close();
            }
        });
        this.nadIconLoader.setOnFailedListner(new NendAdIconLoader.OnFailedListner() { // from class: com.idtinc.ckad.MyFullAdLayout.4
            @Override // net.nend.android.NendAdIconLoader.OnFailedListner
            public void onFailedToReceiveAd(NendIconError iconError) {
                MyFullAdLayout.this.checkIconOrBannerBottom();
            }
        });
        this.nadFullView0Width = 300.0f * this.zoomRate;
        if (this.nadFullView0Width > 600.0f) {
            this.nadFullView0Width = 600.0f;
        }
        this.nadFullView0Height = 250.0f * this.zoomRate;
        if (this.nadFullView0Height > 500.0f) {
            this.nadFullView0Height = 500.0f;
        }
        this.adMobFullView0RectView = new FrameLayout(getContext());
        this.adMobFullView0RectView.setBackgroundColor(0);
        FrameLayout.LayoutParams adMobFullView0RectViewLayoutParams = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
        if (!this.appDelegate.isRetina4) {
            adMobFullView0RectViewLayoutParams.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            adMobFullView0RectViewLayoutParams.topMargin = (int) ((((480.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f) - (15.0f * this.zoomRate));
        } else {
            adMobFullView0RectViewLayoutParams.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            adMobFullView0RectViewLayoutParams.topMargin = (int) (((568.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f);
        }
        adMobFullView0RectViewLayoutParams.gravity = 51;
        addView(this.adMobFullView0RectView, adMobFullView0RectViewLayoutParams);
        this.nadFullLayout0 = new FrameLayout(getContext());
        this.nadFullLayout0.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams nadFullLayout0Params = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
        if (!this.appDelegate.isRetina4) {
            nadFullLayout0Params.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            nadFullLayout0Params.topMargin = (int) ((((480.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f) - (15.0f * this.zoomRate));
        } else {
            nadFullLayout0Params.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            nadFullLayout0Params.topMargin = (int) (((568.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f);
        }
        nadFullLayout0Params.gravity = 51;
        this.nadFullLayout0.setVisibility(8);
        addView(this.nadFullLayout0, nadFullLayout0Params);
        this.nadFullView0 = new NendAdView(getContext(), this.nendSID_Full, this.nendNID_Full);
        this.nadFullView0.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams nadFullView0LayoutParams = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
        nadFullView0LayoutParams.leftMargin = 0;
        nadFullView0LayoutParams.topMargin = 0;
        nadFullView0LayoutParams.gravity = 51;
        this.nadFullView0.setId(10);
        this.nadFullView0.setListener(this);
        this.nadFullLayout0.addView(this.nadFullView0, nadFullView0LayoutParams);
        this.iMobileFulAdLayout = new FrameLayout(getContext());
        this.iMobileFulAdLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams iMobileFulAdLayoutParams = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
        if (!this.appDelegate.isRetina4) {
            iMobileFulAdLayoutParams.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            iMobileFulAdLayoutParams.topMargin = (int) ((((480.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f) - (15.0f * this.zoomRate));
        } else {
            iMobileFulAdLayoutParams.leftMargin = (int) (((320.0f * this.zoomRate) - this.nadFullView0Width) / 2.0f);
            iMobileFulAdLayoutParams.topMargin = (int) (((568.0f * this.zoomRate) - this.nadFullView0Height) / 2.0f);
        }
        iMobileFulAdLayoutParams.gravity = 51;
        this.iMobileFulAdLayout.setVisibility(8);
        addView(this.iMobileFulAdLayout, iMobileFulAdLayoutParams);
        ImobileSdkAd.registerSpotInline((Activity) this.myContext, "19804", "161059", IMOBILE_SDK_ADS_SPOT_ID_FULL);
        ImobileSdkAd.setImobileSdkAdListener(IMOBILE_SDK_ADS_SPOT_ID_FULL, new ImobileSdkAdListener() { // from class: com.idtinc.ckad.MyFullAdLayout.5
            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdReadyCompleted() {
                MyFullAdLayout.this.imobileAdsFullStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdShowCompleted() {
                MyFullAdLayout.this.imobileAdsFullStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCliclkCompleted() {
                MyFullAdLayout.this.imobileAdsFullStatus = true;
                MyFullAdLayout.this.close();
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCloseCompleted() {
                MyFullAdLayout.this.imobileAdsFullStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onFailed(FailNotificationReason reason) {
                MyFullAdLayout.this.imobileAdsFullStatus = false;
            }
        });
        ImobileSdkAd.start(IMOBILE_SDK_ADS_SPOT_ID_FULL);
        ImobileSdkAd.showAd((Activity) this.myContext, IMOBILE_SDK_ADS_SPOT_ID_FULL, this.iMobileFulAdLayout);
        this.fluctFullAdViewLayout = new FrameLayout(getContext());
        this.fluctFullAdViewLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams fluctFullAdViewLayoutLayoutParams = new FrameLayout.LayoutParams((int) (300.0f * this.zoomRate), (int) (250.0f * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            fluctFullAdViewLayoutLayoutParams.leftMargin = (int) (10.0f * this.zoomRate);
            fluctFullAdViewLayoutLayoutParams.topMargin = (int) (100.0f * this.zoomRate);
        } else {
            fluctFullAdViewLayoutLayoutParams.leftMargin = (int) (10.0f * this.zoomRate);
            fluctFullAdViewLayoutLayoutParams.topMargin = (int) (159.0f * this.zoomRate);
        }
        fluctFullAdViewLayoutLayoutParams.gravity = 51;
        this.fluctFullAdViewLayout.setVisibility(8);
        addView(this.fluctFullAdViewLayout, fluctFullAdViewLayoutLayoutParams);
        FrameLayout.LayoutParams fluctFullAdViewLayoutParams = new FrameLayout.LayoutParams((int) (300.0f * this.zoomRate), (int) (250.0f * this.zoomRate));
        fluctFullAdViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        fluctFullAdViewLayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        fluctFullAdViewLayoutParams.gravity = 51;
        this.fluctFullAdView = new FluctView(getContext(), this.zucksMID_Full);
        this.fluctFullAdView.setBackgroundColor(0);
        this.fluctFullAdView.setVisibility(0);
        this.fluctFullAdViewLayout.addView(this.fluctFullAdView, fluctFullAdViewLayoutParams);
        this.fluctAdViewLayout = new FrameLayout(getContext());
        this.fluctAdViewLayout.setBackgroundColor(0);
        FrameLayout.LayoutParams fluctAdViewLayoutLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (50.0f * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            fluctAdViewLayoutLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            fluctAdViewLayoutLayoutParams.topMargin = (int) (362.0f * this.zoomRate);
        } else {
            fluctAdViewLayoutLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
            fluctAdViewLayoutLayoutParams.topMargin = (int) (436.0f * this.zoomRate);
        }
        fluctAdViewLayoutLayoutParams.gravity = 51;
        this.fluctAdViewLayout.setVisibility(8);
        addView(this.fluctAdViewLayout, fluctAdViewLayoutLayoutParams);
        FrameLayout.LayoutParams fluctAdViewLayoutParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (50.0f * this.zoomRate));
        fluctAdViewLayoutParams.leftMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        fluctAdViewLayoutParams.topMargin = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        fluctAdViewLayoutParams.gravity = 51;
        this.fluctAdView = new FluctView(getContext(), this.zucksMID_Banner);
        this.fluctAdView.setBackgroundColor(0);
        this.fluctAdView.setVisibility(0);
        this.fluctAdViewLayout.addView(this.fluctAdView, fluctAdViewLayoutParams);
        String iconFullAdView0UrlString2 = "http://idtinc.home.dyndns.org/IconAd/CkCdA/adb0.html?width=" + ((int) ((300.0f * this.zoomRate) / density)) + "&height=" + ((int) ((250.0f * this.zoomRate) / density));
        String iconAdView0UrlString2 = "http://idtinc.home.dyndns.org/IconAd/CkCdA/ad0.html?width=" + ((int) ((57.0f * this.zoomRate) / density)) + "&height=" + ((int) ((57.0f * this.zoomRate) / density));
        String iconAdView1UrlString2 = "http://idtinc.home.dyndns.org/IconAd/CkCdA/ad1.html?width=" + ((int) ((57.0f * this.zoomRate) / density)) + "&height=" + ((int) ((57.0f * this.zoomRate) / density));
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            iconFullAdView0UrlString = String.valueOf(iconFullAdView0UrlString2) + "&language=ja";
            iconAdView0UrlString = String.valueOf(iconAdView0UrlString2) + "&language=ja";
            iconAdView1UrlString = String.valueOf(iconAdView1UrlString2) + "&language=ja";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            iconFullAdView0UrlString = String.valueOf(iconFullAdView0UrlString2) + "&language=tw";
            iconAdView0UrlString = String.valueOf(iconAdView0UrlString2) + "&language=tw";
            iconAdView1UrlString = String.valueOf(iconAdView1UrlString2) + "&language=tw";
        } else if (languageString.equals("zh-CN")) {
            iconFullAdView0UrlString = String.valueOf(iconFullAdView0UrlString2) + "&language=cn";
            iconAdView0UrlString = String.valueOf(iconAdView0UrlString2) + "&language=cn";
            iconAdView1UrlString = String.valueOf(iconAdView1UrlString2) + "&language=cn";
        } else {
            iconFullAdView0UrlString = String.valueOf(iconFullAdView0UrlString2) + "&language=en";
            iconAdView0UrlString = String.valueOf(iconAdView0UrlString2) + "&language=en";
            iconAdView1UrlString = String.valueOf(iconAdView1UrlString2) + "&language=en";
        }
        String iconFullAdView0UrlString3 = String.valueOf(iconFullAdView0UrlString) + "&rd=" + this.appDelegate.getNowDateString();
        String iconAdView0UrlString3 = String.valueOf(iconAdView0UrlString) + "&rd=" + this.appDelegate.getNowDateString();
        String iconAdView1UrlString3 = String.valueOf(iconAdView1UrlString) + "&rd=" + this.appDelegate.getNowDateString();
        this.iconFullAdView0 = new IconAdView(getContext(), (int) (300.0f * this.zoomRate), (int) (250.0f * this.zoomRate), this.zoomRate, this);
        this.iconFullAdView0.setBackgroundColor(0);
        FrameLayout.LayoutParams iconFullAdView0LayoutParams = new FrameLayout.LayoutParams((int) (300.0f * this.zoomRate), (int) (250.0f * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            iconFullAdView0LayoutParams.leftMargin = (int) (10.0f * this.zoomRate);
            iconFullAdView0LayoutParams.topMargin = (int) (100.0f * this.zoomRate);
        } else {
            iconFullAdView0LayoutParams.leftMargin = (int) (10.0f * this.zoomRate);
            iconFullAdView0LayoutParams.topMargin = (int) (159.0f * this.zoomRate);
        }
        iconFullAdView0LayoutParams.gravity = 51;
        this.iconFullAdView0.setNewAdUrl(iconFullAdView0UrlString3);
        this.iconFullAdView0.tag = (short) 999;
        addView(this.iconFullAdView0, iconFullAdView0LayoutParams);
        this.iconAdView0 = new IconAdView(getContext(), (int) (57.0d * this.zoomRate), (int) (57.0d * this.zoomRate), this.zoomRate, this);
        this.iconAdView0.setBackgroundColor(0);
        FrameLayout.LayoutParams iconAdView0LayoutParams = new FrameLayout.LayoutParams((int) (57.0d * this.zoomRate), (int) (57.0d * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            iconAdView0LayoutParams.leftMargin = (int) (91.0d * this.zoomRate);
            iconAdView0LayoutParams.topMargin = (int) (33.0d * this.zoomRate);
        } else {
            iconAdView0LayoutParams.leftMargin = (int) (91.0d * this.zoomRate);
            iconAdView0LayoutParams.topMargin = (int) (77.0d * this.zoomRate);
        }
        iconAdView0LayoutParams.gravity = 51;
        this.iconAdView0.setNewAdUrl(iconAdView0UrlString3);
        this.iconAdView0.tag = (short) 0;
        addView(this.iconAdView0, iconAdView0LayoutParams);
        this.iconAdView1 = new IconAdView(getContext(), (int) (57.0d * this.zoomRate), (int) (57.0d * this.zoomRate), this.zoomRate, this);
        this.iconAdView1.setBackgroundColor(0);
        FrameLayout.LayoutParams iconAdView1LayoutParams = new FrameLayout.LayoutParams((int) (57.0d * this.zoomRate), (int) (57.0d * this.zoomRate));
        if (!this.appDelegate.isRetina4) {
            iconAdView1LayoutParams.leftMargin = (int) (172.0d * this.zoomRate);
            iconAdView1LayoutParams.topMargin = (int) (33.0d * this.zoomRate);
        } else {
            iconAdView1LayoutParams.leftMargin = (int) (172.0d * this.zoomRate);
            iconAdView1LayoutParams.topMargin = (int) (77.0d * this.zoomRate);
        }
        iconAdView1LayoutParams.gravity = 51;
        this.iconAdView1.setNewAdUrl(iconAdView1UrlString3);
        this.iconAdView1.tag = (short) 1;
        addView(this.iconAdView1, iconAdView1LayoutParams);
    }

    public void checkIconOrBannerBottom() {
        short iconOkCnt = 0;
        if (this.nadIconView2.getVisibility() == 0) {
            iconOkCnt = (short) 1;
        }
        if (this.nadIconView3.getVisibility() == 0) {
            iconOkCnt = (short) (iconOkCnt + 1);
        }
        if (this.nadIconView4.getVisibility() == 0) {
            iconOkCnt = (short) (iconOkCnt + 1);
        }
        if (this.nadIconView5.getVisibility() == 0) {
            iconOkCnt = (short) (iconOkCnt + 1);
        }
        if (iconOkCnt >= 3) {
            this.bottomImobileIconsView.setVisibility(8);
            this.fluctAdViewLayout.setVisibility(8);
            this.bottomIconsView.setVisibility(0);
        } else {
            this.bottomIconsView.setVisibility(8);
            if (checkFluctBannerViewOkF(this.fluctAdView).booleanValue()) {
                this.bottomImobileIconsView.setVisibility(8);
                this.fluctAdViewLayout.setVisibility(0);
            } else {
                this.fluctAdViewLayout.setVisibility(8);
                this.bottomImobileIconsView.setVisibility(0);
            }
        }
        if (this.adToastF.booleanValue()) {
            Toast.makeText(this.appDelegate, "Total Icon:" + ((int) iconOkCnt), 0).show();
        }
    }

    public void releaseAdMobViewBottom() {
        if (this.adMobViewBottom != null) {
            removeView(this.adMobViewBottom);
            this.adMobViewBottom.setAdListener(null);
            this.adMobViewBottom.destroy();
            this.adMobViewBottom = null;
        }
    }

    public void initAdMobViewBottom() {
        releaseAdMobViewBottom();
        if (this.adMobViewBottom == null) {
            this.adMobViewBottom = new AdView((Activity) this.myContext);
            this.adMobViewBottom.setAdUnitId("ca-app-pub-8234307584269595/9998173063");
            this.adMobViewBottom.setAdSize(AdSize.BANNER);
            this.adMobViewBottom.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            FrameLayout.LayoutParams adMobViewBottomLayoutParams = new FrameLayout.LayoutParams((int) (320.0d * this.zoomRate), (int) (50.0d * this.zoomRate));
            adMobViewBottomLayoutParams.gravity = 81;
            addView(this.adMobViewBottom, adMobViewBottomLayoutParams);
            this.adMobViewBottom.setAdListener(new AdListener() { // from class: com.idtinc.ckad.MyFullAdLayout.6
                @Override // com.google.android.gms.ads.AdListener
                public void onAdClosed() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLeftApplication() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdOpened() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLoaded() {
                    Log.d("AdControlLayout", "adView onReceiveAd");
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdFailedToLoad(int errorCode) {
                    Log.d("AdControlLayout", "adView onAdFailedToLoad");
                }
            });
            this.adMobViewBottom.loadAd(new AdRequest.Builder().build());
        }
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

    public void initFluctFullAdViewWithMid(String _zucksMID_Full) {
        if (this.fluctFullAdViewLayout != null && _zucksMID_Full != null && _zucksMID_Full.length() > 0 && !this.zucksMID_Full.equals(_zucksMID_Full)) {
            this.zucksMID_Full = _zucksMID_Full;
            if (this.fluctFullAdView != null) {
                this.fluctFullAdView.setVisibility(8);
                this.fluctFullAdView.destroy();
                this.fluctFullAdView = null;
                this.fluctFullAdViewLayout.removeAllViews();
            }
            if (this.fluctFullAdView == null) {
                FrameLayout.LayoutParams fluctFullAdViewLayoutParams = new FrameLayout.LayoutParams((int) (300.0f * this.zoomRate), (int) (250.0f * this.zoomRate));
                fluctFullAdViewLayoutParams.leftMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
                fluctFullAdViewLayoutParams.topMargin = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
                fluctFullAdViewLayoutParams.gravity = 51;
                this.fluctFullAdView = new FluctView(getContext(), this.zucksMID_Full);
                this.fluctFullAdView.setBackgroundColor(0);
                this.fluctFullAdView.setVisibility(0);
                this.fluctFullAdViewLayout.addView(this.fluctFullAdView, fluctFullAdViewLayoutParams);
            }
        }
    }

    public void initNadIconView(String _nendNID_Icon, int _nendSID_Icon) {
        if (!this.nendNID_Icon.equals(_nendNID_Icon) && this.nendSID_Icon != _nendSID_Icon) {
            this.nendNID_Icon = _nendNID_Icon;
            this.nendSID_Icon = _nendSID_Icon;
            if (this.nadIconLoader != null) {
                this.nadIconLoader.pause();
                this.nadIconLoader.setOnReceiveLisner(null);
                if (this.nadIconView5 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView5);
                }
                if (this.nadIconView4 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView4);
                }
                if (this.nadIconView3 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView3);
                }
                if (this.nadIconView2 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView2);
                }
                if (this.nadIconView1 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView1);
                }
                if (this.nadIconView0 != null) {
                    this.nadIconLoader.removeIconView(this.nadIconView0);
                }
                this.nadIconLoader = null;
            }
            this.nadIconLoader = new NendAdIconLoader(getContext(), this.nendSID_Icon, this.nendNID_Icon);
            if (this.nadIconView5 != null) {
                this.nadIconLoader.addIconView(this.nadIconView5);
            }
            if (this.nadIconView4 != null) {
                this.nadIconLoader.addIconView(this.nadIconView4);
            }
            if (this.nadIconView3 != null) {
                this.nadIconLoader.addIconView(this.nadIconView3);
            }
            if (this.nadIconView2 != null) {
                this.nadIconLoader.addIconView(this.nadIconView2);
            }
            if (this.nadIconView1 != null) {
                this.nadIconLoader.addIconView(this.nadIconView1);
            }
            if (this.nadIconView0 != null) {
                this.nadIconLoader.addIconView(this.nadIconView0);
            }
            this.nadIconLoader.loadAd();
            this.nadIconLoader.setOnReceiveLisner(new NendAdIconLoader.OnReceiveListner() { // from class: com.idtinc.ckad.MyFullAdLayout.7
                @Override // net.nend.android.NendAdIconLoader.OnReceiveListner
                public void onReceiveAd(NendAdIconView iconView) {
                    if (MyFullAdLayout.this.adToastF.booleanValue()) {
                        Toast.makeText(MyFullAdLayout.this.appDelegate, "收到nand icon " + iconView.getId(), 0).show();
                    }
                    iconView.setVisibility(0);
                    MyFullAdLayout.this.checkIconOrBannerBottom();
                }
            });
            this.nadIconLoader.setOnClickListner(new NendAdIconLoader.OnClickListner() { // from class: com.idtinc.ckad.MyFullAdLayout.8
                @Override // net.nend.android.NendAdIconLoader.OnClickListner
                public void onClick(NendAdIconView iconView) {
                    MyFullAdLayout.this.close();
                }
            });
            this.nadIconLoader.setOnFailedListner(new NendAdIconLoader.OnFailedListner() { // from class: com.idtinc.ckad.MyFullAdLayout.9
                @Override // net.nend.android.NendAdIconLoader.OnFailedListner
                public void onFailedToReceiveAd(NendIconError iconError) {
                    MyFullAdLayout.this.checkIconOrBannerBottom();
                }
            });
        }
    }

    public void initNadFullView0(String _nendNID_Full, int _nendSID_Full) {
        if (!this.nendNID_Full.equals(_nendNID_Full) && this.nendSID_Full != _nendSID_Full) {
            this.nendNID_Full = _nendNID_Full;
            this.nendSID_Full = _nendSID_Full;
            if (this.nadFullView0 != null) {
                this.nadFullView0.pause();
                this.nadFullView0.removeListener();
                this.nadFullLayout0.removeView(this.nadFullView0);
                this.nadFullView0 = null;
            }
            this.nadFullView0 = new NendAdView(getContext(), this.nendSID_Full, this.nendNID_Full);
            this.nadFullView0.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            FrameLayout.LayoutParams nadFullView0LayoutParams = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
            nadFullView0LayoutParams.leftMargin = 0;
            nadFullView0LayoutParams.topMargin = 0;
            nadFullView0LayoutParams.gravity = 51;
            this.nadFullView0.setId(10);
            this.nadFullView0.setListener(this);
            this.nadFullLayout0.addView(this.nadFullView0, nadFullView0LayoutParams);
        }
    }

    public void releaseAdMobFullView0() {
        this.adMobFullViewStatus = (short) -1;
        if (this.adMobFullView0 != null) {
            this.adMobFullView0RectView.removeView(this.adMobFullView0);
            this.adMobFullView0.setAdListener(null);
            this.adMobFullView0.destroy();
            this.adMobFullView0 = null;
        }
    }

    public void initAdMobFullView0() {
        releaseAdMobFullView0();
        if (this.adMobFullView0 == null) {
            this.adMobFullView0 = new AdView((Activity) this.myContext);
            this.adMobFullView0.setAdUnitId("ca-app-pub-8234307584269595/6142625861");
            this.adMobFullView0.setAdSize(AdSize.MEDIUM_RECTANGLE);
            this.adMobFullView0.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            FrameLayout.LayoutParams adMobFullView0LayoutParams = new FrameLayout.LayoutParams((int) this.nadFullView0Width, (int) this.nadFullView0Height);
            adMobFullView0LayoutParams.gravity = 51;
            this.adMobFullViewStatus = (short) 0;
            this.adMobFullView0RectView.addView(this.adMobFullView0, adMobFullView0LayoutParams);
            this.adMobFullView0.setAdListener(new AdListener() { // from class: com.idtinc.ckad.MyFullAdLayout.10
                @Override // com.google.android.gms.ads.AdListener
                public void onAdClosed() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLeftApplication() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdOpened() {
                    MyFullAdLayout.this.close();
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLoaded() {
                    Log.d("AdControlLayout", "adView onReceiveAd");
                    if (MyFullAdLayout.this.adToastF.booleanValue()) {
                        Toast.makeText(MyFullAdLayout.this.appDelegate, "收到admob 320 250", 0).show();
                    }
                    MyFullAdLayout.this.adMobFullViewStatus = (short) 1;
                    if (MyFullAdLayout.this.nowStatus == -1) {
                        MyFullAdLayout.this.close();
                    } else if (MyFullAdLayout.this.nowStatus == 0) {
                        MyFullAdLayout.this.open();
                    }
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdFailedToLoad(int errorCode) {
                    if (MyFullAdLayout.this.adToastF.booleanValue()) {
                        Toast.makeText(MyFullAdLayout.this.appDelegate, "沒收到admob 320 250", 0).show();
                    }
                    MyFullAdLayout.this.adMobFullViewStatus = (short) -1;
                    if (MyFullAdLayout.this.nowStatus == 0 && MyFullAdLayout.this.nadFullViewStatus == -1) {
                        MyFullAdLayout.this.loadAdMobFullScreenAd();
                        MyFullAdLayout.this.close();
                    }
                }
            });
            this.adMobFullView0.loadAd(new AdRequest.Builder().build());
        }
    }

    public void checkFluctFullAdViewDisplay() {
        if (this.fluctFullAdView != null && this.fluctFullAdViewLayout != null) {
            Boolean showZucksFullF = false;
            if (this.appDelegate != null) {
                if (this.appDelegate.defaultSharedPreferences == null) {
                    this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("zucks_full_active", false)) {
                    showZucksFullF = true;
                }
            }
            if (showZucksFullF.booleanValue() && checkFluctBannerViewOkF(this.fluctFullAdView).booleanValue()) {
                this.fluctFullAdViewLayout.setVisibility(0);
            } else {
                this.fluctFullAdViewLayout.setVisibility(8);
            }
        }
    }

    public Boolean checkFluctBannerViewOkF(FluctView _fluctBannerView) {
        if (_fluctBannerView == null || _fluctBannerView.getChildCount() <= 0) {
            return false;
        }
        return true;
    }

    public void startAdLoopWithCnt(short _newDisplayCnt) {
        if (this.nowStatus >= -2) {
            if (_newDisplayCnt >= 0) {
                this.nextDisplayCnt = _newDisplayCnt;
            }
            if (this.nextDisplayCnt == 0) {
                doLoop();
            }
        }
    }

    public void setNewNextDisplayCnt(int _newNextDisplayCnt) {
        if (_newNextDisplayCnt > 0) {
            this.NEXT_DISPLAY_CNT = _newNextDisplayCnt;
            if (this.nextDisplayCnt > this.NEXT_DISPLAY_CNT) {
                this.nextDisplayCnt = this.NEXT_DISPLAY_CNT;
            }
        }
    }

    public void doAdLoop() {
        if (this.nowStatus >= -2) {
            doLoop();
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.MyFullAdLayout.11
                @Override // java.lang.Runnable
                public void run() {
                    MyFullAdLayout.this.doAdLoop();
                }
            }, 60000L);
        }
    }

    public void doLoop() {
        if (this.nowStatus >= -2) {
            this.nextDisplayCnt--;
            if (this.nextDisplayCnt <= 0) {
                this.nextDisplayCnt = this.NEXT_DISPLAY_CNT;
                if (this.appMainActivity != null) {
                    this.appMainActivity.doDisplayFullAdView();
                }
            }
        }
    }

    public void loadToOpen() {
        setVisibility(8);
        initAdMobViewBottom();
        initAdMobFullView0();
        if (this.iconAdView0 != null) {
            this.iconAdView0.start();
        }
        if (this.iconAdView1 != null) {
            this.iconAdView1.start();
        }
        if (this.iconFullAdView0 != null) {
            this.iconFullAdView0.start();
        }
        if (this.nadFullViewStatus == 1) {
            open();
        } else {
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckad.MyFullAdLayout.12
                @Override // java.lang.Runnable
                public void run() {
                    MyFullAdLayout.this.doReloadNad(MyFullAdLayout.this.nowStatus);
                }
            }, 1000L);
        }
        this.nowStatus = (short) 0;
    }

    public void doReloadNad(short _nowStatus) {
        if (_nowStatus == -2) {
            if (this.nadFullView0 != null) {
                this.nadFullViewStatus = (short) 0;
                this.nadFullLayout0.setVisibility(8);
                this.nadFullView0.loadAd();
            }
            if (this.nadIconLoader != null) {
                this.nadIconLoader.loadAd();
                return;
            }
            return;
        }
        if (this.nadFullView0 != null) {
            this.nadFullView0.pause();
            this.nadFullView0.resume();
            this.nadFullView0.loadAd();
        }
        if (this.nadIconLoader != null) {
            this.nadIconLoader.pause();
            this.nadIconLoader.resume();
            this.nadIconLoader.loadAd();
        }
    }

    public void open() {
        this.nowStatus = (short) 1;
        if (this.closeButton != null) {
            this.closeButton.setVisibility(0);
            Animation alphaAnimation = new AlphaAnimation(BitmapDescriptorFactory.HUE_RED, 1.0f);
            alphaAnimation.setDuration(3000L);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.ckad.MyFullAdLayout.13
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                }
            });
            this.closeButton.startAnimation(alphaAnimation);
        }
        Boolean showImobileFullF = false;
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("imobile_full_active", false) && this.imobileAdsFullStatus.booleanValue()) {
                showImobileFullF = true;
            }
        }
        if (showImobileFullF.booleanValue()) {
            this.iMobileFulAdLayout.setVisibility(0);
        } else {
            this.iMobileFulAdLayout.setVisibility(8);
        }
        checkFluctFullAdViewDisplay();
        setVisibility(0);
        Animation alphaAnimation2 = new AlphaAnimation(BitmapDescriptorFactory.HUE_RED, 1.0f);
        alphaAnimation2.setDuration(250L);
        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.ckad.MyFullAdLayout.14
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }
        });
        startAnimation(alphaAnimation2);
    }

    public void close() {
        this.nowStatus = (short) -1;
        setVisibility(8);
        if (this.nadFullView0 != null) {
            this.nadFullView0.pause();
        }
        if (this.nadIconLoader != null) {
            this.nadIconLoader.pause();
        }
        releaseAdMobViewBottom();
        releaseAdMobFullView0();
        if (this.iconAdView0 != null) {
            this.iconAdView0.stop();
        }
        if (this.iconAdView1 != null) {
            this.iconAdView1.stop();
        }
        if (this.iconFullAdView0 != null) {
            this.iconFullAdView0.stop();
        }
        Animation alphaAnimation = new AlphaAnimation(1.0f, BitmapDescriptorFactory.HUE_RED);
        alphaAnimation.setDuration(100L);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.ckad.MyFullAdLayout.15
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }
        });
        startAnimation(alphaAnimation);
    }

    @Override // net.nend.android.NendAdListener
    public void onClick(NendAdView nendAdView) {
        close();
    }

    @Override // net.nend.android.NendAdListener
    public void onReceiveAd(NendAdView nendAdView) {
        int nendAdViewId = nendAdView.getId();
        if (nendAdViewId == 10) {
            if (this.adToastF.booleanValue()) {
                Toast.makeText(this.appDelegate, "收到nand 320 250", 0).show();
            }
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("nend_full_active", false)) {
                this.nadFullLayout0.setVisibility(0);
            }
            this.nadFullViewStatus = (short) 1;
            if (this.nowStatus == -1) {
                close();
            } else if (this.nowStatus == 0) {
                open();
            }
        }
    }

    @Override // net.nend.android.NendAdListener
    public void onFailedToReceiveAd(NendAdView nendAdView) {
        int nendAdViewId = nendAdView.getId();
        if (nendAdViewId == 10) {
            if (this.adToastF.booleanValue()) {
                Toast.makeText(this.appDelegate, "沒收到nand 320 250", 0).show();
            }
            this.nadFullLayout0.setVisibility(8);
            this.nadFullViewStatus = (short) -1;
            if (this.nowStatus == 0 && this.adMobFullViewStatus == -1) {
                Boolean.valueOf(false);
                if (this.iconFullAdView0 != null && this.iconFullAdView0.redirectUrl != null && this.iconFullAdView0.redirectUrl.length() > 5) {
                    open();
                } else {
                    loadAdMobFullScreenAd();
                    close();
                }
            }
        }
    }

    @Override // net.nend.android.NendAdListener
    public void onDismissScreen(NendAdView arg0) {
    }

    public void onClickIconAdView(IconAdView _iconAdView) {
        close();
    }

    public void loadAdMobFullScreenAd() {
        if (this.appDelegate != null) {
            this.appDelegate.loadAdMobFullScreenAd();
        }
    }

    public void onDestroy() {
        this.nowStatus = (short) -3;
        if (this.fluctFullAdView != null) {
            this.fluctFullAdView.destroy();
            this.fluctFullAdView = null;
        }
        if (this.fluctFullAdViewLayout != null) {
            this.fluctFullAdViewLayout.removeAllViews();
        }
        if (this.fluctAdView != null) {
            this.fluctAdView.destroy();
            this.fluctAdView = null;
        }
        if (this.fluctAdViewLayout != null) {
            this.fluctAdViewLayout.removeAllViews();
        }
        if (this.iconAdView1 != null) {
            this.iconAdView1.stop();
            this.iconAdView1.onDestroy();
            this.iconAdView1 = null;
        }
        if (this.iconAdView0 != null) {
            this.iconAdView0.stop();
            this.iconAdView0.onDestroy();
            this.iconAdView0 = null;
        }
        if (this.iconFullAdView0 != null) {
            this.iconFullAdView0.stop();
            this.iconFullAdView0.onDestroy();
            this.iconFullAdView0 = null;
        }
        ImobileSdkAd.activityDestory();
        this.nadFullViewStatus = (short) -1;
        if (this.nadFullView0 != null) {
            this.nadFullView0.pause();
            this.nadFullView0 = null;
        }
        if (this.adMobViewBottom != null) {
            removeView(this.adMobViewBottom);
            this.adMobViewBottom.setAdListener(null);
            this.adMobViewBottom.destroy();
            this.adMobViewBottom = null;
        }
        this.adMobFullViewStatus = (short) -1;
        if (this.adMobFullView0 != null) {
            this.adMobFullView0RectView.removeView(this.adMobFullView0);
            this.adMobFullView0.setAdListener(null);
            this.adMobFullView0.destroy();
            this.adMobFullView0 = null;
        }
        if (this.adMobFullView0RectView != null) {
            this.adMobFullView0RectView.setVisibility(8);
            removeView(this.adMobFullView0RectView);
            this.adMobFullView0RectView = null;
        }
        if (this.nadIconLoader != null) {
            this.nadIconLoader.pause();
            this.nadIconLoader.setOnReceiveLisner(null);
            if (this.nadIconView5 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView5);
                this.nadIconView5 = null;
            }
            if (this.nadIconView4 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView4);
                this.nadIconView4 = null;
            }
            if (this.nadIconView3 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView3);
                this.nadIconView3 = null;
            }
            if (this.nadIconView2 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView2);
                this.nadIconView2 = null;
            }
            if (this.nadIconView1 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView1);
                this.nadIconView1 = null;
            }
            if (this.nadIconView0 != null) {
                this.nadIconLoader.removeIconView(this.nadIconView0);
                this.nadIconView0 = null;
            }
            this.nadIconLoader = null;
        }
        if (this.closeButton != null) {
            this.closeButton.setOnClickListener(null);
            this.closeButton = null;
        }
        removeAllViews();
        this.appMainActivity = null;
        this.myContext = null;
        this.appDelegate = null;
    }
}
