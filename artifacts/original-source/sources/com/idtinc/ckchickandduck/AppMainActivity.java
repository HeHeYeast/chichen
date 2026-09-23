package com.idtinc.ckchickandduck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckad.AdControlLayout;
import com.idtinc.ckad.MyFullAdLayout;
import com.idtinc.ckchickandduck_alarm.CallAlarm;
import com.idtinc.maingame.MainGameViewController;
import com.idtinc.onlinegame.OnlineGameViewController;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import jp.co.imobile.sdkads.android.FailNotificationReason;
import jp.co.imobile.sdkads.android.ImobileSdkAd;
import jp.co.imobile.sdkads.android.ImobileSdkAdListener;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

@SuppressLint({"SimpleDateFormat", "HandlerLeak", "UseSparseArrays"})
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AppMainActivity extends Activity implements AppDelegateInterface, AdColonyAdAvailabilityListener, AdColonyAdListener {
    static final String ADCOLONY_APP_ID = "app967824d6929a49eb83";
    static final String ADCOLONY_ZONE_ID_INTERSTISIAL = "vz3ed51c351b20454081";
    static final String ADCOLONY_ZONE_ID_V4VC = "vze95623e007184a6d9d";
    static final String IMOBILE_SDK_ADS_MEDIA_ID_WALL = "161059";
    static final String IMOBILE_SDK_ADS_PUBLISHER_ID_WALL = "19804";
    static final String IMOBILE_SDK_ADS_SPOT_ID_WALL = "426258";
    private static final List<String> PERMISSIONS_0 = Arrays.asList("publish_actions");
    AdControlLayout adControlLayout;
    FrameLayout allLayout;
    private InterstitialAd interstitial;
    LogoImageLayout logoImageLayout;
    FrameLayout mainLayout;
    MyFullAdLayout myFullAdLayout;
    float zoomRate;
    final String SENDER_ID = "235863556449";
    Boolean imobileAdsWallStatus = true;
    int dispWidth = 0;
    int dispHeight = 0;
    float finalWidth = BitmapDescriptorFactory.HUE_RED;
    float finalHeight = BitmapDescriptorFactory.HUE_RED;
    String gcmAlertString = "";
    String gcmMessageString = "";
    String gcmUrlString = "";
    private AppDelegate appDelegate = null;
    private AppMainSurfaceView appMainSurfaceView = null;
    FrameLayout appMainSurfaceViewLayout = null;
    MainGameViewController mainGameViewController = null;
    SavesCheckViewController savesCheckViewController = null;
    MainMenuViewController mainMenuViewController = null;
    OnlineGameViewController onlineGameViewController = null;
    Boolean initDoDisplayFullAdView = false;
    AdView adMobView = null;
    private Session.StatusCallback statusCallback = new SessionStatusCallback(this, null);
    private short fbNextActionStstus = -1;
    private Bitmap FBShareBitmap = null;

    private class SessionStatusCallback implements Session.StatusCallback {
        private SessionStatusCallback() {
        }

        /* synthetic */ SessionStatusCallback(AppMainActivity appMainActivity, SessionStatusCallback sessionStatusCallback) {
            this();
        }

        @Override // com.facebook.Session.StatusCallback
        public void call(Session session, SessionState state, Exception exception) throws Resources.NotFoundException {
            Log.d("Session", "SessionStatusCallback");
            AppMainActivity.this.onSessionStateChange(session, state, exception);
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent;
        super.onCreate(savedInstanceState);
        setVolumeControlStream(3);
        getWindow().setFlags(128, 128);
        setContentView(R.layout.activity_app_main);
        Display defDisp = getWindowManager().getDefaultDisplay();
        DisplayMetrics dispMet = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dispMet);
        this.appDelegate = (AppDelegate) getApplicationContext();
        this.appDelegate.setCurrentActivity(this);
        this.appDelegate.initFirst(defDisp, dispMet);
        if (this.appDelegate != null && (intent = getIntent()) != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && processExtraData(bundle)) {
                intent.putExtras(new Bundle());
            }
            setIntent(intent);
        }
        if (this.appDelegate != null) {
            this.appDelegate.gcmCheckF = true;
        }
        try {
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);
            String registrationId = GCMRegistrar.getRegistrationId(this);
            if (registrationId.equals("")) {
                GCMRegistrar.register(this, "235863556449");
            } else if (this.appDelegate != null) {
                this.appDelegate.set_device_token(registrationId);
                this.appDelegate.post_device_token();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fbNextActionStstus = (short) -1;
        Session session = new Session(this);
        Session.setActiveSession(session);
        this.dispWidth = defDisp.getWidth();
        this.dispHeight = defDisp.getHeight();
        Log.d("MyApp", "Width=" + this.dispWidth);
        Log.d("MyApp", "Height=" + this.dispHeight);
        float floatWidth = this.dispWidth;
        float floatHeight = this.dispHeight;
        float checkFloatHeight = (float) (floatWidth * 1.5d);
        Log.d("MyApp", "checkFloatHeight=" + checkFloatHeight);
        if (checkFloatHeight <= floatHeight) {
            this.finalWidth = this.dispWidth;
            this.finalHeight = checkFloatHeight;
        } else {
            this.finalHeight = floatHeight;
            this.finalWidth = (this.finalHeight * 2.0f) / 3.0f;
        }
        this.zoomRate = this.finalWidth / 320.0f;
        Log.d("MyApp", "finalWidth=" + this.finalWidth);
        Log.d("MyApp", "finalHeight=" + this.finalHeight);
        Log.d("MyApp", "zoomRate=" + this.zoomRate);
        this.finalWidth = this.appDelegate.finalWidth;
        this.finalHeight = this.appDelegate.finalHeight;
        this.zoomRate = this.appDelegate.zoomRate;
        this.mainLayout = (FrameLayout) findViewById(R.id.appMainLayout);
        this.mainLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        ViewGroup.LayoutParams mainLayoutParams = this.mainLayout.getLayoutParams();
        mainLayoutParams.width = this.dispWidth;
        mainLayoutParams.height = this.dispHeight;
        this.mainLayout.setLayoutParams(mainLayoutParams);
        this.logoImageLayout = new LogoImageLayout(this, this.dispWidth, this.dispHeight, (int) this.finalWidth, (int) this.appDelegate.notRetina4Height, this.zoomRate);
        this.logoImageLayout.setBackgroundColor(0);
        FrameLayout.LayoutParams logoImageLayoutParams = new FrameLayout.LayoutParams((int) this.finalWidth, this.dispHeight);
        logoImageLayoutParams.gravity = 17;
        this.mainLayout.addView(this.logoImageLayout, logoImageLayoutParams);
        new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.AppMainActivity.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                AppMainActivity.this.doInitAll();
            }
        }, 300L);
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void readyDoShareImage(short _type) throws Resources.NotFoundException, IOException {
        if (_type >= 0 || _type <= 1) {
            this.fbNextActionStstus = (short) -1;
            Session session = getNowActiveSession();
            if (session != null) {
                this.fbNextActionStstus = _type;
                if (this.FBShareBitmap != null) {
                    if (!this.FBShareBitmap.isRecycled()) {
                        this.FBShareBitmap.recycle();
                    }
                    this.FBShareBitmap = null;
                }
                if (this.fbNextActionStstus == 0) {
                    AssetManager asm = getAssets();
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.Options opt2 = new BitmapFactory.Options();
                    opt2.inJustDecodeBounds = false;
                    opt2.inPurgeable = true;
                    opt2.inInputShareable = true;
                    try {
                        InputStream inputStream = asm.open("png/icon.png");
                        BufferedInputStream buf = new BufferedInputStream(inputStream);
                        BitmapFactory.decodeStream(buf, null, opt);
                        this.FBShareBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                        inputStream.close();
                    } catch (IOException e) {
                    }
                } else if (this.fbNextActionStstus == 1) {
                    if (!this.appDelegate.isRetina4) {
                        this.FBShareBitmap = this.appDelegate.takeScreenShot(this.zoomRate * 10.0f, 55.0f * this.zoomRate, this.zoomRate * 300.0f, this.zoomRate * 235.0f);
                    } else {
                        this.FBShareBitmap = this.appDelegate.takeScreenShot(this.zoomRate * 10.0f, 99.0f * this.zoomRate, this.zoomRate * 300.0f, this.zoomRate * 235.0f);
                    }
                }
                if (!session.isOpened()) {
                    doFacebookLogIn();
                    return;
                }
                String alertDialogMessage = this.appDelegate.getResources().getString(R.string.FBShareIconMessage);
                if (this.fbNextActionStstus == 1) {
                    alertDialogMessage = this.appDelegate.getResources().getString(R.string.FBShareCharacterMessage);
                }
                new AlertDialog.Builder(this).setTitle(R.string.facebook).setMessage(alertDialogMessage).setPositiveButton(R.string.No, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(2);
                        }
                    }
                }).setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                        }
                        AppMainActivity.this.preparePublish();
                    }
                }).show();
            }
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void readyDoFbLogin(short _type) {
        if (_type == 100 || _type == 200) {
            this.fbNextActionStstus = (short) -1;
            Session session = getNowActiveSession();
            if (session != null) {
                this.fbNextActionStstus = _type;
                if (!session.isOpened()) {
                    doFacebookLogIn();
                } else {
                    doFacebookLogIn();
                }
            }
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void readyDoFacebookLogout() {
        new AlertDialog.Builder(this).setTitle(R.string.facebook).setMessage(R.string.FBLogoutMessage).setPositiveButton(R.string.No, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (AppMainActivity.this.appDelegate != null) {
                    AppMainActivity.this.appDelegate.doSoundPoolPlay(2);
                }
            }
        }).setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) throws Resources.NotFoundException {
                if (AppMainActivity.this.appDelegate != null) {
                    AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                }
                AppMainActivity.this.doFacebookLogout();
            }
        }).show();
    }

    public Session getNowActiveSession() {
        Session session = Session.getActiveSession();
        if (session == null) {
            Session session2 = new Session(this);
            Session.setActiveSession(session2);
        }
        return Session.getActiveSession();
    }

    public void doFacebookLogIn() {
        Session session = getNowActiveSession();
        if (session != null) {
            if (!session.isOpened()) {
                if (session.isClosed()) {
                    session = new Session(this);
                    Session.setActiveSession(session);
                }
                session.openForRead(new Session.OpenRequest(this));
                return;
            }
            Session.openActiveSession((Activity) this, true, this.statusCallback);
        }
    }

    public void doFacebookLogout() throws Resources.NotFoundException {
        String title = this.appDelegate.getResources().getString(R.string.facebook);
        Session session = getNowActiveSession();
        if (session != null && !session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
        this.fbNextActionStstus = (short) -1;
        this.appDelegate.set_fb_account_id("");
        this.appDelegate.set_fb_account_user_name("");
        this.appDelegate.set_fb_account_first_name("");
        this.appDelegate.set_fb_account_last_name("");
        this.appDelegate.set_fb_account_name("");
        this.appDelegate.set_fb_account_email("");
        this.appDelegate.set_fb_account_gender("");
        this.appDelegate.set_fb_account_birthday("");
        this.appDelegate.do_fb_account_logout();
        String message = this.appDelegate.getResources().getString(R.string.LoggedOut);
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public boolean getLogoutF() {
        boolean logoutF = false;
        Session session = getNowActiveSession();
        if (session != null) {
            if (session.isClosed()) {
                logoutF = true;
            }
            if (session.getAccessToken() == null || session.getAccessToken().length() <= 0) {
                return true;
            }
            return logoutF;
        }
        return true;
    }

    public void checkPublishPermission() {
        Session session;
        if (hasPublishPermission() && (session = Session.getActiveSession()) != null) {
            session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS_0));
        }
    }

    public boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    public void preparePublish() {
        if (this.fbNextActionStstus >= 0 && this.fbNextActionStstus <= 1) {
            Session session = Session.getActiveSession();
            if (session != null) {
                checkPublishPermission();
                doPost(this.fbNextActionStstus);
            }
            this.fbNextActionStstus = (short) -1;
        }
    }

    public void doPost(short _fbNextActionStstus) {
        if (_fbNextActionStstus >= 0 && _fbNextActionStstus <= 1) {
            String message = String.valueOf(this.appDelegate.getResources().getString(R.string.ChickKitchen2)) + " (Free App)\n\niOS: http://www.idtfun.com/apps/chickkitchen_cd_ap0.html\n\nAndroid: http://www.idtfun.com/apps/chickkitchen_cd_gp0.html\n";
            if (this.FBShareBitmap != null) {
                Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), this.FBShareBitmap, new Request.Callback() { // from class: com.idtinc.ckchickandduck.AppMainActivity.6
                    @Override // com.facebook.Request.Callback
                    public void onCompleted(Response response) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
                        AppMainActivity.this.showPublishResult("", response.getGraphObject(), response.getError());
                    }
                });
                Bundle params = request.getParameters();
                params.putString("message", message);
                request.executeAsync();
            }
        }
    }

    public void showPublishResult(String message, GraphObject result, FacebookRequestError error) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        String alertMessage;
        String title = this.appDelegate.getResources().getString(R.string.facebook);
        if (error == null) {
            alertMessage = this.appDelegate.getResources().getString(R.string.UploadSucceeded);
            checkFacebookBonus();
        } else {
            alertMessage = this.appDelegate.getResources().getString(R.string.UploadFailed);
        }
        new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
        if (this.FBShareBitmap != null) {
            if (!this.FBShareBitmap.isRecycled()) {
                this.FBShareBitmap.recycle();
            }
            this.FBShareBitmap = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionStateChange(Session session, SessionState state, Exception exception) throws Resources.NotFoundException {
        if ((exception instanceof FacebookOperationCanceledException) || (exception instanceof FacebookAuthorizationException)) {
            this.fbNextActionStstus = (short) -1;
            return;
        }
        if (state != SessionState.OPENED_TOKEN_UPDATED && state == SessionState.OPENED) {
            checkPublishPermission();
            if (this.fbNextActionStstus >= 0 && this.fbNextActionStstus <= 1) {
                String alertDialogMessage = this.appDelegate.getResources().getString(R.string.FBShareIconMessage);
                if (this.fbNextActionStstus == 1) {
                    alertDialogMessage = this.appDelegate.getResources().getString(R.string.FBShareCharacterMessage);
                }
                new AlertDialog.Builder(this).setTitle(R.string.facebook).setMessage(alertDialogMessage).setPositiveButton(R.string.No, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(2);
                        }
                    }
                }).setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                        }
                        AppMainActivity.this.preparePublish();
                    }
                }).show();
                return;
            }
            if (this.fbNextActionStstus == 100 || this.fbNextActionStstus == 200) {
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() { // from class: com.idtinc.ckchickandduck.AppMainActivity.9
                    @Override // com.facebook.Request.GraphUserCallback
                    public void onCompleted(GraphUser user, Response response) {
                        String fb_account_id;
                        Date date_origin;
                        Boolean.valueOf(false);
                        if (user != null && (fb_account_id = user.getId()) != null && fb_account_id.length() > 0 && AppMainActivity.this.appDelegate != null && AppMainActivity.this.appDelegate.set_fb_account_id(fb_account_id).booleanValue()) {
                            String fb_account_user_name = user.getUsername();
                            if (fb_account_user_name != null) {
                                AppMainActivity.this.appDelegate.set_fb_account_user_name(fb_account_user_name);
                            }
                            String fb_account_first_name = user.getFirstName();
                            if (fb_account_first_name != null) {
                                AppMainActivity.this.appDelegate.set_fb_account_first_name(fb_account_first_name);
                            }
                            String fb_account_last_name = user.getLastName();
                            if (fb_account_last_name != null) {
                                AppMainActivity.this.appDelegate.set_fb_account_last_name(fb_account_last_name);
                            }
                            String fb_account_name = user.getName();
                            if (fb_account_name != null) {
                                AppMainActivity.this.appDelegate.set_fb_account_name(fb_account_name);
                            }
                            String fb_account_email = "";
                            if (user.getProperty("email") != null) {
                                fb_account_email = user.getProperty("email").toString();
                            }
                            if (fb_account_email != null) {
                                AppMainActivity.this.appDelegate.set_fb_account_email(fb_account_email);
                            }
                            String fb_account_gender = "";
                            if (user.getProperty("gender") != null) {
                                fb_account_gender = user.getProperty("gender").toString();
                            }
                            if (fb_account_gender != null) {
                                if (fb_account_gender.equals("male")) {
                                    AppMainActivity.this.appDelegate.set_fb_account_gender("1");
                                } else if (fb_account_gender.equals("female")) {
                                    AppMainActivity.this.appDelegate.set_fb_account_gender("2");
                                } else {
                                    AppMainActivity.this.appDelegate.set_fb_account_gender("0");
                                }
                            }
                            String fb_account_birthday = user.getBirthday();
                            if (fb_account_birthday != null) {
                                SimpleDateFormat sdf_origin = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    date_origin = sdf_origin.parse(fb_account_birthday);
                                } catch (ParseException e) {
                                    date_origin = null;
                                }
                                if (date_origin != null) {
                                    SimpleDateFormat sdf_new = new SimpleDateFormat("yyyy/MM/dd");
                                    String fb_account_birthday2 = sdf_new.format(date_origin);
                                    if (fb_account_birthday2 != null) {
                                        AppMainActivity.this.appDelegate.set_fb_account_birthday(fb_account_birthday2);
                                    }
                                }
                            }
                            AppMainActivity.this.appDelegate.do_idt_account_logout();
                            AppMainActivity.this.appDelegate.set_fb_account_logged_in(true);
                            AppMainActivity.this.openOnlineGameViewControllerWithAutoLogIn(true, AppMainActivity.this.fbNextActionStstus);
                            Boolean.valueOf(true);
                        }
                        AppMainActivity.this.fbNextActionStstus = (short) -1;
                    }
                });
            }
        }
    }

    class FacebookGraphUserCallback implements Request.GraphUserCallback {
        private ProgressDialog mProgress;

        public FacebookGraphUserCallback(String message) {
            this.mProgress = null;
            this.mProgress = new ProgressDialog(AppMainActivity.this);
            this.mProgress.setMessage(message);
            this.mProgress.setProgressStyle(0);
            this.mProgress.show();
        }

        @Override // com.facebook.Request.GraphUserCallback
        public void onCompleted(GraphUser user, Response response) {
            this.mProgress.dismiss();
        }
    }

    public void displayToast(String _string) {
    }

    public void checkFacebookBonus() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                int gotBonus = this.appDelegate.defaultSharedPreferences.getInt("get_bonus_bonus", -1);
                if (gotBonus > 0) {
                    SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                    editor.putBoolean("get_bonus_active", true);
                    editor.commit();
                    if (this.mainGameViewController != null) {
                        this.mainGameViewController.checkGetBonus();
                    }
                }
            }
        }
    }

    public void doInitAll() throws IOException {
        if (this.appDelegate != null) {
            this.appDelegate.readyDoInitSecond();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public boolean isInstallSoftware(String packageName) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName, 0);
            return pInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void readyDoInitViews() {
        initViews();
        if (this.appDelegate != null) {
            this.appDelegate.changeNowStatus(-1);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.AppMainActivity.10
            @Override // java.lang.Runnable
            public void run() throws PackageManager.NameNotFoundException {
                AppMainActivity.this.doInitImobileWall();
                AppMainActivity.this.doInitMyFullAdLayout();
                AppMainActivity.this.doInitAdControlLayout();
            }
        }, 50L);
    }

    public void initViews() {
        if (this.logoImageLayout != null) {
            this.logoImageLayout.onDestroy();
            this.logoImageLayout.setVisibility(8);
            this.mainLayout.removeView(this.logoImageLayout);
            this.logoImageLayout = null;
        }
        this.allLayout = new FrameLayout(this);
        this.allLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        FrameLayout.LayoutParams allLayoutParams = new FrameLayout.LayoutParams((int) this.finalWidth, (int) this.finalHeight);
        allLayoutParams.gravity = 81;
        this.mainLayout.addView(this.allLayout, allLayoutParams);
        this.appMainSurfaceViewLayout = new FrameLayout(this);
        this.appMainSurfaceViewLayout.setBackgroundColor(267386880);
        this.allLayout.addView(this.appMainSurfaceViewLayout, (int) this.finalWidth, (int) this.finalHeight);
        this.appMainSurfaceView = new AppMainSurfaceView(this, this.finalWidth, this.finalHeight, this.zoomRate, this);
        this.appMainSurfaceView.setBackgroundColor(0);
        FrameLayout.LayoutParams appMainSurfaceViewLayoutParams = new FrameLayout.LayoutParams((int) this.finalWidth, (int) this.finalHeight);
        appMainSurfaceViewLayoutParams.gravity = 81;
        this.appMainSurfaceViewLayout.addView(this.appMainSurfaceView, appMainSurfaceViewLayoutParams);
        this.mainMenuViewController = new MainMenuViewController(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        initOnLineGameViewController();
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void showNoInternetAlertDialog() {
        new AlertDialog.Builder(this).setTitle("").setMessage(this.appDelegate.getResources().getString(R.string.CantConnectToTheInternet)).setPositiveButton(this.appDelegate.getResources().getString(R.string.OK), (DialogInterface.OnClickListener) null).show();
    }

    public void doInitAdControlLayout() {
        this.adControlLayout = new AdControlLayout(this, (int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 50.0f), this.zoomRate, this.myFullAdLayout);
        this.adControlLayout.setBackgroundColor(1711276032);
        FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams((int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 50.0f));
        adViewLayoutParams.gravity = 81;
        this.mainLayout.addView(this.adControlLayout, adViewLayoutParams);
        this.adControlLayout.startRequest(false);
        this.interstitial = new InterstitialAd(this);
        this.interstitial.setAdUnitId("ca-app-pub-8234307584269595/8521439865");
        this.interstitial.setAdListener(new AdListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.11
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
                Log.d("AdControlLayout", "adView onAdLoaded");
                if (AppMainActivity.this.interstitial.isLoaded()) {
                    AppMainActivity.this.interstitial.show();
                }
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int errorCode) {
                Log.d("AdControlLayout", "adView onAdFailedToLoad");
            }
        });
    }

    public void doInitImobileWall() throws PackageManager.NameNotFoundException {
        ImobileSdkAd.registerSpotFullScreen(this, IMOBILE_SDK_ADS_PUBLISHER_ID_WALL, IMOBILE_SDK_ADS_MEDIA_ID_WALL, IMOBILE_SDK_ADS_SPOT_ID_WALL);
        ImobileSdkAd.setImobileSdkAdListener(IMOBILE_SDK_ADS_SPOT_ID_WALL, new ImobileSdkAdListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.12
            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdReadyCompleted() {
                AppMainActivity.this.imobileAdsWallStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdShowCompleted() {
                AppMainActivity.this.imobileAdsWallStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCliclkCompleted() {
                AppMainActivity.this.imobileAdsWallStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onAdCloseCompleted() {
                AppMainActivity.this.imobileAdsWallStatus = true;
            }

            @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
            public void onFailed(FailNotificationReason reason) {
                AppMainActivity.this.imobileAdsWallStatus = false;
            }
        });
        ImobileSdkAd.start(IMOBILE_SDK_ADS_SPOT_ID_WALL);
    }

    public void doInitMyFullAdLayout() {
        AdColony.configure(this, "version:2.3.0,store:google", ADCOLONY_APP_ID, ADCOLONY_ZONE_ID_V4VC);
        AdColonyV4VCListener listener = new AdColonyV4VCListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.13
            @Override // com.jirbo.adcolony.AdColonyV4VCListener
            public void onAdColonyV4VCReward(AdColonyV4VCReward reward) {
                if (reward.success() && AppMainActivity.this.appDelegate.defaultSharedPreferences != null) {
                    AppMainActivity.this.appDelegate.defaultSharedPreferences.getString("get_coins_title", "");
                    AppMainActivity.this.appDelegate.defaultSharedPreferences.getString("get_coins_content", "");
                    int gotCoins = AppMainActivity.this.appDelegate.defaultSharedPreferences.getInt("get_coins_coins", -1);
                    if (gotCoins >= 0) {
                        SharedPreferences.Editor editor = AppMainActivity.this.appDelegate.defaultSharedPreferences.edit();
                        editor.putBoolean("get_coins_active", true);
                        editor.commit();
                    }
                }
            }
        };
        AdColony.addV4VCListener(listener);
        if (!this.appDelegate.isRetina4) {
            this.myFullAdLayout = new MyFullAdLayout(this, (int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 480.0f), this.zoomRate, this);
            this.myFullAdLayout.setBackgroundColor(-301989888);
            FrameLayout.LayoutParams myFullAdLayoutParams = new FrameLayout.LayoutParams((int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 480.0f));
            myFullAdLayoutParams.gravity = 81;
            this.mainLayout.addView(this.myFullAdLayout, myFullAdLayoutParams);
        } else {
            this.myFullAdLayout = new MyFullAdLayout(this, (int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 568.0f), this.zoomRate, this);
            this.myFullAdLayout.setBackgroundColor(-301989888);
            FrameLayout.LayoutParams myFullAdLayoutParams2 = new FrameLayout.LayoutParams((int) (this.zoomRate * 320.0f), (int) (this.zoomRate * 568.0f));
            myFullAdLayoutParams2.gravity = 81;
            this.mainLayout.addView(this.myFullAdLayout, myFullAdLayoutParams2);
        }
        if (this.myFullAdLayout != null) {
            this.myFullAdLayout.setOnTouchListener(new View.OnTouchListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.14
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            this.myFullAdLayout.startAdLoopWithCnt((short) 2);
            this.myFullAdLayout.doAdLoop();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void hiddenAdControlLayout(boolean _hiddenF) {
        if (this.adControlLayout != null) {
            if (_hiddenF) {
                this.adControlLayout.setVisibility(8);
            } else {
                this.adControlLayout.setVisibility(0);
            }
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void hiddenBonusUnitViewAd(boolean _hiddenF) {
        if (this.adMobView != null) {
            if (this.mainLayout != null) {
                this.mainLayout.removeView(this.adMobView);
            }
            this.adMobView.setAdListener(null);
            this.adMobView.destroy();
            this.adMobView = null;
        }
        if (!_hiddenF && this.adMobView == null) {
            this.adMobView = new AdView(this);
            this.adMobView.setAdUnitId("ca-app-pub-8234307584269595/9998173063");
            this.adMobView.setAdSize(AdSize.BANNER);
            this.adMobView.setBackgroundColor(1711276032);
            FrameLayout.LayoutParams adMobViewParams = new FrameLayout.LayoutParams((int) (320.0f * this.zoomRate), (int) (50.0f * this.zoomRate));
            adMobViewParams.gravity = 81;
            this.mainLayout.addView(this.adMobView, adMobViewParams);
            this.adMobView.setVisibility(0);
            this.adMobView.setAdListener(new AdListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.15
                @Override // com.google.android.gms.ads.AdListener
                public void onAdClosed() {
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLeftApplication() {
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdOpened() {
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdLoaded() {
                }

                @Override // com.google.android.gms.ads.AdListener
                public void onAdFailedToLoad(int errorCode) {
                }
            });
            this.adMobView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public Bitmap takeScreenShot(float _takeOffsetX, float _takeOffsetY, float _takeWidth, float _takeHeight) {
        if (this.appMainSurfaceView == null) {
            return null;
        }
        Bitmap originBitmap = this.appMainSurfaceView.getScreenshot();
        Bitmap returnBitmap = Bitmap.createBitmap(originBitmap, (int) _takeOffsetX, (int) _takeOffsetY, (int) _takeWidth, (int) _takeHeight);
        return returnBitmap;
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public boolean savePic(Bitmap b, String strFilePath, String strFileName) throws IOException {
        try {
            FileOutputStream fos = openFileOutput(strFileName, 1);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public boolean checkAdColonyV4VCF() {
        if (!AdColony.statusForZone(ADCOLONY_ZONE_ID_V4VC).equals("active")) {
            return false;
        }
        return true;
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public boolean showAdColonyV4VCF() {
        AdColonyV4VCAd adColonyV4VCAd;
        if (!checkAdColonyV4VCF() || (adColonyV4VCAd = new AdColonyV4VCAd(ADCOLONY_ZONE_ID_V4VC).withListener(this)) == null || adColonyV4VCAd.noFill()) {
            return false;
        }
        adColonyV4VCAd.show();
        return true;
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void loadAdMobFullScreenAd() {
        if (this.interstitial != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            this.interstitial.loadAd(adRequest);
        }
    }

    public void doDisplayFullAdView() {
        Boolean showImobileWallF = false;
        Boolean adcolony_interstisial_active = false;
        Boolean full_ad_active = true;
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                if (this.appDelegate.defaultSharedPreferences.getBoolean("imobile_wall_active", false) && this.imobileAdsWallStatus.booleanValue()) {
                    showImobileWallF = true;
                }
                if (this.appDelegate.defaultSharedPreferences.getBoolean("adcolony_interstisial_active", false)) {
                    adcolony_interstisial_active = true;
                }
                if (!this.appDelegate.defaultSharedPreferences.getBoolean("full_ad_active", false)) {
                    full_ad_active = false;
                }
            }
        }
        if (full_ad_active.booleanValue()) {
            boolean doShowAdColonyV4VCF = false;
            if (adcolony_interstisial_active.booleanValue()) {
                doShowAdColonyV4VCF = showAdColonyV4VCF();
            }
            if (!doShowAdColonyV4VCF) {
                if (showImobileWallF.booleanValue()) {
                    ImobileSdkAd.showAdforce(this, IMOBILE_SDK_ADS_SPOT_ID_WALL);
                } else if (this.myFullAdLayout != null) {
                    this.myFullAdLayout.loadToOpen();
                }
            }
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void displayFullAdView() {
        if (this.myFullAdLayout != null) {
            this.myFullAdLayout.startAdLoopWithCnt((short) 0);
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void delayDisplayFullAdView(int _delaySeconds) {
        displayFullAdView();
    }

    public void initMainGameViewController() {
        if (this.mainGameViewController == null) {
            this.mainGameViewController = new MainGameViewController(this, this.finalWidth, this.finalHeight, this.zoomRate, this);
            this.mainGameViewController.setBackgroundColor(0);
            this.mainGameViewController.setVisibility(8);
            this.allLayout.addView(this.mainGameViewController, (int) this.finalWidth, (int) this.finalHeight);
        }
    }

    public void initOnLineGameViewController() {
        if (this.onlineGameViewController == null) {
            this.onlineGameViewController = new OnlineGameViewController(this, this.finalWidth, this.finalHeight, this.zoomRate, this);
            this.onlineGameViewController.setBackgroundColor(-1728053248);
            this.onlineGameViewController.setVisibility(8);
            hiddenOnlineGameViewController();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void openOnlineGameViewControllerWithAutoLogIn(Boolean _autoLogInF, short _nextActiveStstus) {
        if (this.onlineGameViewController != null) {
            this.allLayout.addView(this.onlineGameViewController, (int) this.finalWidth, (int) this.finalHeight);
            this.onlineGameViewController.openWithAutoLogIn(_autoLogInF, _nextActiveStstus);
            this.onlineGameViewController.setVisibility(0);
        }
    }

    public void hiddenOnlineGameViewController() {
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.setVisibility(8);
            this.allLayout.removeView(this.onlineGameViewController);
        }
    }

    public void initSavesCheckViewController() {
        if (this.savesCheckViewController == null) {
            this.savesCheckViewController = new SavesCheckViewController(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
            this.savesCheckViewController.hidden = true;
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void shareMailWithUriImage(String _intentTitle, String _subject, String _text, Uri _uriToImage) {
        Intent sendEmailIntent = new Intent("android.intent.action.SEND");
        sendEmailIntent.setType("image/png");
        if (_uriToImage != null) {
            sendEmailIntent.putExtra("android.intent.extra.STREAM", _uriToImage);
        }
        sendEmailIntent.putExtra("android.intent.extra.SUBJECT", _subject);
        sendEmailIntent.putExtra("android.intent.extra.TEXT", _text);
        startActivity(Intent.createChooser(sendEmailIntent, _intentTitle));
    }

    public void shareMail() {
        Uri uriToImage = Uri.parse("android.resource://" + this.appDelegate.getPackageName() + "/" + R.drawable.icon);
        Intent sendEmailIntent = new Intent("android.intent.action.SEND");
        sendEmailIntent.setType("image/png");
        sendEmailIntent.putExtra("android.intent.extra.STREAM", uriToImage);
        sendEmailIntent.putExtra("android.intent.extra.SUBJECT", this.appDelegate.getResources().getString(R.string.ChickKitchen2));
        sendEmailIntent.putExtra("android.intent.extra.TEXT", String.valueOf(this.appDelegate.getResources().getString(R.string.ChickKitchen2)) + " (Free App)\n\niOS: http://www.idtfun.com/apps/chickkitchen_cd_ap0.html\n\nAndroid: http://www.idtfun.com/apps/chickkitchen_cd_gp0.html\n");
        startActivity(Intent.createChooser(sendEmailIntent, this.appDelegate.getResources().getString(R.string.TellFriendsCK2)));
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void emailUs() {
        Log.d("HelpLayout", "emailUs");
        Intent sendEmailIntent = new Intent("android.intent.action.SEND");
        sendEmailIntent.setType("text/plain");
        sendEmailIntent.putExtra("android.intent.extra.EMAIL", new String[]{"idtincservice@gmail.com"});
        sendEmailIntent.putExtra("android.intent.extra.SUBJECT", this.appDelegate.getResources().getString(R.string.ChickKitchen2));
        startActivity(Intent.createChooser(sendEmailIntent, this.appDelegate.getResources().getString(R.string.ContactUs)));
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void goToSlosPage() {
        if (this.appDelegate != null) {
            if (this.appDelegate.checkInterNet()) {
                Uri uri = Uri.parse("http://sentive.net/");
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                startActivity(intent);
                return;
            }
            showNoInternetAlertDialog();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void goToCKKB() {
        if (this.appDelegate != null) {
            if (isInstallSoftware("com.idtinc.ckkujibiki")) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.idtinc.ckkujibiki", "com.idtinc.ckkujibiki.AppMainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("CallComIdtIncCkKb:", "CallComIdtncCkChickAndDuck_0");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    if (this.appDelegate != null) {
                        this.appDelegate.set_gift_tool_2((short) 68, true);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    if (this.appDelegate.checkInterNet()) {
                        Uri uri = Uri.parse("market://details?id=com.idtinc.ckkujibiki");
                        startActivity(new Intent("android.intent.action.VIEW", uri));
                        return;
                    } else {
                        showNoInternetAlertDialog();
                        return;
                    }
                }
            }
            if (this.appDelegate.checkInterNet()) {
                Uri uri2 = Uri.parse("market://details?id=com.idtinc.ckkujibiki");
                startActivity(new Intent("android.intent.action.VIEW", uri2));
                return;
            }
            showNoInternetAlertDialog();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void goToCKMV() {
        if (this.appDelegate != null) {
            if (isInstallSoftware("com.idtinc.ckmonstervillage")) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.idtinc.ckmonstervillage", "com.idtinc.ckmonstervillage.AppMainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("CallComIdtIncCkMv:", "CallComIdtncCkChickAndDuck_0");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    return;
                } catch (Exception e) {
                    if (this.appDelegate.checkInterNet()) {
                        Uri uri = Uri.parse("market://details?id=com.idtinc.ckmonstervillage");
                        startActivity(new Intent("android.intent.action.VIEW", uri));
                        return;
                    } else {
                        showNoInternetAlertDialog();
                        return;
                    }
                }
            }
            if (this.appDelegate.checkInterNet()) {
                Uri uri2 = Uri.parse("market://details?id=com.idtinc.ckmonstervillage");
                startActivity(new Intent("android.intent.action.VIEW", uri2));
                return;
            }
            showNoInternetAlertDialog();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void returnToMainGame() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.appDelegate.getNowStatus() == 0) {
            changeNowStatus(1);
        }
        if (this.appDelegate.getNowStatus() == 1) {
            this.mainGameViewController.doWillEnterForeground();
            Log.d("backToMainGame", "backToMainGame");
            this.mainGameViewController.setVisibility(0);
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void goToMainGame() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.appDelegate.getNowStatus() == 0) {
            initMainGameViewController();
            changeNowStatus(1);
        }
        if (this.appDelegate.getNowStatus() == 1) {
            this.mainGameViewController.doInit();
            Log.d("goToMainGame", "goToMainGame");
            this.mainGameViewController.setVisibility(0);
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void backToSavesCheck() {
        if (this.appDelegate.getNowStatus() == 1) {
            changeNowStatus(0);
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void goToSavesCheck() {
        if (this.appDelegate.getNowStatus() == -1) {
            initSavesCheckViewController();
            changeNowStatus(0);
        }
        if (this.appDelegate.getNowStatus() == 0) {
            this.savesCheckViewController.restart();
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void backToMainMenu() {
        if (this.appDelegate.getNowStatus() == 0) {
            changeNowStatus(-1);
        }
    }

    public void changeNowStatus(int _newStatus) {
        if (_newStatus == -1) {
            if (this.appDelegate.getNowStatus() == 0) {
                this.appDelegate.changeNowStatus(-1);
                if (this.mainGameViewController != null) {
                    this.mainGameViewController.setVisibility(8);
                }
                if (this.savesCheckViewController != null) {
                    this.savesCheckViewController.hidden = true;
                    this.savesCheckViewController.reset();
                }
                if (this.mainMenuViewController != null) {
                    this.mainMenuViewController.restart();
                    this.mainMenuViewController.hidden = false;
                    return;
                }
                return;
            }
            return;
        }
        if (_newStatus == 0) {
            if (this.appDelegate.getNowStatus() == -1 || this.appDelegate.getNowStatus() == 1) {
                this.appDelegate.changeNowStatus(0);
                if (this.mainGameViewController != null) {
                    this.mainGameViewController.setVisibility(8);
                }
                if (this.mainMenuViewController != null) {
                    this.mainMenuViewController.hidden = true;
                }
                if (this.savesCheckViewController != null) {
                    this.savesCheckViewController.hidden = false;
                    this.savesCheckViewController.reset();
                    return;
                }
                return;
            }
            return;
        }
        if (_newStatus == 1 && this.appDelegate.getNowStatus() == 0) {
            this.appDelegate.changeNowStatus(1);
            if (this.mainGameViewController != null) {
                this.mainGameViewController.setVisibility(0);
            }
            if (this.mainMenuViewController != null) {
                this.mainMenuViewController.hidden = true;
            }
            if (this.savesCheckViewController != null) {
                this.savesCheckViewController.hidden = true;
            }
        }
    }

    @Override // com.idtinc.ckchickandduck.AppDelegateInterface
    public void doMainLoop() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.appDelegate != null) {
            if (this.appDelegate.soundPlayCnt >= 0) {
                this.appDelegate.soundPlayCnt = (short) (r0.soundPlayCnt - 1);
            }
            if (this.appDelegate.getNowStatus() != 0 && this.appDelegate.getNowStatus() == 1 && this.mainGameViewController != null) {
                this.mainGameViewController.doLoop();
            }
            if (this.onlineGameViewController != null && this.onlineGameViewController.getVisibility() == 0) {
                this.onlineGameViewController.doLoop();
            }
            if (this.appDelegate.gcmCheckF) {
                checkGcmAlert();
            }
        }
    }

    public void setCookAlarmWithDateString(String _dateString) {
        removeAllAlarmNotification();
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    public void setCookAlarmOnWithFireDateString(String _dateString, String _bodyString) {
        removeAllAlarmNotification();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date fireDate = null;
        try {
            fireDate = sdf.parse(_dateString);
        } catch (ParseException e) {
        }
        if (fireDate != null) {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(fireDate.getTime());
            Bundle bundle = new Bundle();
            bundle.putString("body_string", _bodyString);
            Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
            intent.putExtras(bundle);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
            AlarmManager am = (AlarmManager) getSystemService("alarm");
            am.set(0, c2.getTimeInMillis(), sender);
        }
    }

    public void removeAllAlarmNotification() {
        Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService("alarm");
        am.cancel(sender);
    }

    public void removeAlarmNotificationWithNotificationID(String _notificationIDString) {
        Intent intent = new Intent(this, (Class<?>) CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService("alarm");
        am.cancel(sender);
    }

    public String getLocaleLanguage() {
        Locale local = Locale.getDefault();
        return String.format("%s-%s", local.getLanguage(), local.getCountry());
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(this.statusCallback);
        Log.i("appMessage", "onStart");
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.appDelegate == null) {
            this.appDelegate = (AppDelegate) getApplicationContext();
        }
        if (this.appDelegate != null) {
            this.appDelegate.setCurrentActivity(this);
            this.appDelegate.changeOnPauseF(false);
            this.appDelegate.doWillEnterForeground();
            if (this.appDelegate.getNowStatus() == 0 && this.savesCheckViewController != null) {
                this.savesCheckViewController.reset();
                this.savesCheckViewController.doWillEnterForeground();
            }
        }
        if (this.adControlLayout != null) {
            this.adControlLayout.startRequest(false);
        }
        super.onResume();
        AdColony.resume(this);
        Log.i("appMessage", "onResume");
    }

    @Override // android.app.Activity
    public void onPause() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.doWillTerminate();
        }
        if (this.appDelegate != null) {
            if (this.appDelegate.getNowStatus() == 0) {
                if (this.savesCheckViewController != null) {
                    this.savesCheckViewController.doWillTerminate();
                }
            } else if (this.appDelegate.getNowStatus() == 1 && this.mainGameViewController != null) {
                this.mainGameViewController.doWillTerminate();
            }
            this.appDelegate.changeOnPauseF(true);
        }
        super.onPause();
        AdColony.pause();
        super.onPause();
        Log.i("appMessage", "onPause");
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(this.statusCallback);
        Log.i("appMessage", "onStop");
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) throws IOException {
        super.onSaveInstanceState(outState);
        outState.putString("test", "Test");
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
        Log.i("appMessage", "onSaveInstanceState");
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("appMessage", "onRestoreInstanceState");
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && processExtraData(bundle)) {
                intent.putExtras(new Bundle());
            }
            setIntent(intent);
        }
        if (this.appDelegate != null) {
            this.appDelegate.gcmCheckF = true;
        }
    }

    private boolean processExtraData(Bundle _bundle) throws NumberFormatException {
        if (_bundle == null || this.appDelegate == null) {
            return false;
        }
        if (this.appDelegate.defaultSharedPreferences == null) {
            this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
        }
        int getOmikujiShort = -2;
        boolean gift_tool_2_68_unlockF = false;
        String getOmikujiString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtIncCkKb_Character_");
        if (getOmikujiString != null) {
            if (getOmikujiString.length() > 0) {
                getOmikujiShort = Integer.parseInt(getOmikujiString);
                gift_tool_2_68_unlockF = true;
            }
            this.appDelegate.set_gift_tool_2((short) 68, gift_tool_2_68_unlockF);
            if (getOmikujiShort >= 0 && getOmikujiShort <= 14 && this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                editor.putInt("gift_tool_2_68_egg_id", 0);
                editor.putInt("gift_tool_2_68_character_id", getOmikujiShort + 89);
                editor.commit();
            }
            displayFullAdView();
            return true;
        }
        String getStageToolString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtIncCkMv_Tool_");
        if (getStageToolString != null && getStageToolString.length() > 0) {
            int getStageToolShort = Integer.parseInt(getStageToolString);
            if (getStageToolShort == 69) {
                this.appDelegate.set_gift_tool_2((short) 69, true);
            } else if (getStageToolShort == 70) {
                this.appDelegate.set_gift_tool_2((short) 70, true);
            }
            displayFullAdView();
            return true;
        }
        String getChicksString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtncCk_Send_Chicks=");
        if (getChicksString != null) {
            if (this.appDelegate.defaultSharedPreferences != null && getChicksString.length() > 0) {
                String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                if (ck_send_chick_string.length() > 0) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.AppMainActivity.16
                        @Override // java.lang.Runnable
                        public void run() {
                            AppMainActivity.this.openCkWithSendChickResultError0();
                        }
                    }, 1000L);
                } else {
                    SharedPreferences.Editor editor2 = this.appDelegate.defaultSharedPreferences.edit();
                    editor2.putString("ck_send_chick_string", getChicksString);
                    editor2.commit();
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.AppMainActivity.17
                        @Override // java.lang.Runnable
                        public void run() {
                            AppMainActivity.this.openCkWithSendChickResultSuccess1();
                        }
                    }, 1000L);
                }
            }
            return true;
        }
        String getKitchenString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtncCk_Send_Kitchen=");
        if (getKitchenString != null) {
            if (this.appDelegate.defaultSharedPreferences != null && getKitchenString.length() > 0) {
                SharedPreferences.Editor editor3 = this.appDelegate.defaultSharedPreferences.edit();
                editor3.putString("ck_send_kitchen_string", getKitchenString);
                editor3.commit();
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.AppMainActivity.18
                    @Override // java.lang.Runnable
                    public void run() {
                        AppMainActivity.this.openCkWithSendKitchenResultSuccess1();
                    }
                }, 1000L);
            }
            return true;
        }
        String getMonsterVillageString = _bundle.getString("CallComIdtncCkChickAndDuck:CallComIdtIncCkMv");
        if (getMonsterVillageString != null) {
            displayFullAdView();
            return true;
        }
        return false;
    }

    public void openCkWithSendChickResultError0() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                if (ck_send_chick_string.length() > 0 && isInstallSoftware("com.idtinc.ck")) {
                    Intent intent = new Intent();
                    intent.setClassName("com.idtinc.ck", "com.idtinc.ck.AppMainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("CallComIdtncCk:CallComIdtncCkChickAndDuck_Send_Chicks_Result=", String.valueOf(ck_send_chick_string) + "=0");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    }

    public void openCkWithSendChickResultSuccess1() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                if (ck_send_chick_string.length() > 0 && isInstallSoftware("com.idtinc.ck")) {
                    Intent intent = new Intent();
                    intent.setClassName("com.idtinc.ck", "com.idtinc.ck.AppMainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("CallComIdtncCk:CallComIdtncCkChickAndDuck_Send_Chicks_Result=", String.valueOf(ck_send_chick_string) + "=1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    }

    public void openCkWithSendKitchenResultSuccess1() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String ck_send_kitchen_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_kitchen_string", "");
                if (ck_send_kitchen_string.length() > 0 && isInstallSoftware("com.idtinc.ck")) {
                    Intent intent = new Intent();
                    intent.setClassName("com.idtinc.ck", "com.idtinc.ck.AppMainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("CallComIdtncCk:CallComIdtncCkChickAndDuck_Send_Kitchen_Result=", String.valueOf(ck_send_kitchen_string) + "=1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    }

    public void checkGcmAlert() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                this.gcmAlertString = this.appDelegate.defaultSharedPreferences.getString("gcm_alert", "");
                if (this.gcmAlertString == null) {
                    this.gcmAlertString = "";
                }
                if (this.gcmAlertString.length() > 0) {
                    this.gcmMessageString = this.appDelegate.defaultSharedPreferences.getString("gcm_message", "");
                    if (this.gcmMessageString == null) {
                        this.gcmMessageString = "";
                    }
                    this.gcmUrlString = this.appDelegate.defaultSharedPreferences.getString("gcm_url", "");
                    if (this.gcmUrlString == null) {
                        this.gcmUrlString = "";
                    }
                    if (this.gcmUrlString.length() <= 0) {
                        new AlertDialog.Builder(this).setTitle(this.gcmAlertString).setMessage(this.gcmMessageString).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.19
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppMainActivity.this.appDelegate != null) {
                                    AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                                }
                            }
                        }).show();
                    } else {
                        new AlertDialog.Builder(this).setTitle(this.gcmAlertString).setMessage(this.gcmMessageString).setPositiveButton(R.string.No, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.20
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppMainActivity.this.appDelegate != null) {
                                    AppMainActivity.this.appDelegate.doSoundPoolPlay(2);
                                }
                            }
                        }).setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.21
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppMainActivity.this.appDelegate != null) {
                                    AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                                }
                                AppMainActivity.this.goToGcmUrl();
                            }
                        }).show();
                    }
                    if (this.appDelegate != null) {
                        this.appDelegate.doSoundPoolPlay(4);
                    }
                    SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                    editor.putString("gcm_alert", "");
                    editor.putString("gcm_message", "");
                    editor.putString("gcm_url", "");
                    editor.commit();
                }
                this.appDelegate.gcmCheckF = false;
            }
        }
    }

    public void goToGcmUrl() {
        if (this.gcmUrlString != null && this.gcmUrlString.length() > 0) {
            if (this.appDelegate.checkInterNet()) {
                Uri uri = Uri.parse(this.gcmUrlString);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                startActivity(intent);
                return;
            }
            showNoInternetAlertDialog();
        }
    }

    public void doSoundPoolPlay(int _index) {
    }

    @Override // android.app.Activity
    public void onDestroy() throws IllegalStateException {
        Log.i("appMessage", "onDestroy");
        GCMRegistrar.onDestroy(getApplicationContext());
        if (this.myFullAdLayout != null) {
            this.myFullAdLayout.onDestroy();
            this.myFullAdLayout = null;
        }
        if (this.adMobView != null) {
            this.adMobView.destroy();
            this.adMobView = null;
        }
        if (this.adControlLayout != null) {
            this.adControlLayout.onDestroy();
            this.adControlLayout = null;
        }
        if (this.onlineGameViewController != null) {
            this.onlineGameViewController.onDestroy();
            this.onlineGameViewController = null;
        }
        if (this.mainGameViewController != null) {
            this.mainGameViewController.onDestroy();
            this.mainGameViewController = null;
        }
        if (this.savesCheckViewController != null) {
            this.savesCheckViewController.onDestroy();
            this.savesCheckViewController = null;
        }
        if (this.mainMenuViewController != null) {
            this.mainMenuViewController.onDestroy();
            this.mainMenuViewController = null;
        }
        if (this.appMainSurfaceView != null) {
            this.appMainSurfaceView.onDestroy();
            this.appMainSurfaceView = null;
        }
        if (this.logoImageLayout != null) {
            this.logoImageLayout.onDestroy();
            this.logoImageLayout = null;
        }
        if (this.appDelegate != null) {
            this.appDelegate.changeNowStatus(-999);
            this.appDelegate.onDestroy();
            clearReferences();
            this.appDelegate = null;
        }
        if (this.FBShareBitmap != null) {
            if (!this.FBShareBitmap.isRecycled()) {
                this.FBShareBitmap.recycle();
            }
            this.FBShareBitmap = null;
        }
        System.gc();
        super.onDestroy();
    }

    private void clearReferences() {
        AppDelegateInterface currActivity = this.appDelegate.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            this.appDelegate.setCurrentActivity(null);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        showBackAlert(1);
        event.startTracking();
        return true;
    }

    public void showBackAlert(int id) {
        switch (id) {
            case 1:
                new AlertDialog.Builder(this).setTitle(R.string.QuitGame).setMessage(R.string.QuitGameMessage).setPositiveButton(R.string.No, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.22
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(2);
                        }
                    }
                }).setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck.AppMainActivity.23
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppMainActivity.this.appDelegate != null) {
                            AppMainActivity.this.appDelegate.doSoundPoolPlay(1);
                        }
                        AppMainActivity.this.finish();
                    }
                }).show();
                if (this.appDelegate != null) {
                    this.appDelegate.doSoundPoolPlay(4);
                    break;
                }
                break;
        }
    }

    @Override // com.jirbo.adcolony.AdColonyAdAvailabilityListener
    public void onAdColonyAdAvailabilityChange(boolean arg0, String arg1) {
    }

    @Override // com.jirbo.adcolony.AdColonyAdListener
    public void onAdColonyAdAttemptFinished(AdColonyAd ad) {
    }

    @Override // com.jirbo.adcolony.AdColonyAdListener
    public void onAdColonyAdStarted(AdColonyAd arg0) {
    }
}
