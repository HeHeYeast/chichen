package jp.adlantis.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.HashMap;
import java.util.Map;
import jp.adlantis.android.AdManager;
import jp.adlantis.android.AdlantisAdsModel;
import jp.adlantis.android.utils.ADLDebugUtils;
import jp.adlantis.android.utils.AdlantisUtils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdlantisAdViewContainer extends RelativeLayout implements AdRequestNotifier {
    static int _animationDuration = 500;
    static final int touchHighlightAnimationDuration = 150;
    private AdlantisAdView[] _adViews;
    AdlantisAdsModel _adsModel;
    private boolean _buttonPressed;
    private int _currentAdIndex;
    private boolean _detachingFromWindow;
    private Handler _handler;
    private boolean _handlingUserEvent;
    private long _idNotSpecifiedWarningInterval;
    private boolean _inOnWindowVisibilityChanged;
    private boolean _layoutComplete;
    private int _onWindowVisibilityChangedVisibility;
    private int _previousAdCount;
    private ProgressBar _processIndicator;
    private ViewFlipper _rootViewFlipper;
    private View _touchHighlight;
    protected String lastUsedPublisherID;
    protected AdRequestListeners listeners;
    private long mAdFetchInterval;
    protected AdRequest mCurrentAdRequest;
    private Runnable mRotateAdTask;
    private Runnable mUpdateAdsTask;

    public enum AnimationType {
        NONE,
        FADE,
        SLIDE_FROM_RIGHT,
        SLIDE_FROM_LEFT
    }

    public AdlantisAdViewContainer(Context context) throws PackageManager.NameNotFoundException {
        super(context);
        this._adsModel = new AdlantisAdsModel();
        this._currentAdIndex = 0;
        this._handler = new Handler(Looper.getMainLooper());
        this._idNotSpecifiedWarningInterval = 5000L;
        this._previousAdCount = 0;
        this.listeners = new AdRequestListeners();
        this.mRotateAdTask = new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.1
            @Override // java.lang.Runnable
            public void run() {
                AdlantisAdViewContainer.this.showNextAd();
                AdlantisAdViewContainer.this._handler.postAtTime(this, SystemClock.uptimeMillis() + AdlantisAdViewContainer.this.adDisplayInterval());
            }
        };
        this.mUpdateAdsTask = new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.2
            @Override // java.lang.Runnable
            public void run() {
                AdlantisAdViewContainer.this.connect();
            }
        };
        this.mAdFetchInterval = adManager().adFetchInterval();
        commonInit();
    }

    public AdlantisAdViewContainer(Context context, AttributeSet attributeSet) throws PackageManager.NameNotFoundException {
        super(context, attributeSet);
        this._adsModel = new AdlantisAdsModel();
        this._currentAdIndex = 0;
        this._handler = new Handler(Looper.getMainLooper());
        this._idNotSpecifiedWarningInterval = 5000L;
        this._previousAdCount = 0;
        this.listeners = new AdRequestListeners();
        this.mRotateAdTask = new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.1
            @Override // java.lang.Runnable
            public void run() {
                AdlantisAdViewContainer.this.showNextAd();
                AdlantisAdViewContainer.this._handler.postAtTime(this, SystemClock.uptimeMillis() + AdlantisAdViewContainer.this.adDisplayInterval());
            }
        };
        this.mUpdateAdsTask = new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.2
            @Override // java.lang.Runnable
            public void run() {
                AdlantisAdViewContainer.this.connect();
            }
        };
        this.mAdFetchInterval = adManager().adFetchInterval();
        String attributeValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/jp.adlantis.android", "publisherID");
        if (attributeValue != null) {
            setPublisherID(attributeValue);
        }
        commonInit();
    }

    private void adCountChanged() {
        int iAdCountForCurrentOrientation = adCountForCurrentOrientation();
        if (this._rootViewFlipper == null) {
            Log.w(getClass().getSimpleName(), "adCountChanged called when _rootViewFlipper is not available");
        } else if (iAdCountForCurrentOrientation > 0 && this._previousAdCount == 0) {
            this._rootViewFlipper.setVisibility(0);
            this._rootViewFlipper.startAnimation(fadeInAnimation());
        } else if (iAdCountForCurrentOrientation == 0 && this._previousAdCount > 0) {
            this._rootViewFlipper.startAnimation(fadeOutAnimation());
            this._rootViewFlipper.setVisibility(4);
        }
        this._previousAdCount = iAdCountForCurrentOrientation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long adDisplayInterval() {
        return adManager().adDisplayInterval();
    }

    private long adFetchInterval() {
        return this.mAdFetchInterval;
    }

    public static int animationDuration() {
        return _animationDuration;
    }

    private void commonInit() throws PackageManager.NameNotFoundException {
        setupLayout();
        setupModelListener();
        if (getPublisherID() == null) {
            String strPackagePublisherID = packagePublisherID();
            if (strPackagePublisherID != null) {
                if (AdManager.isGreeSdk()) {
                    setGapPublisherID(strPackagePublisherID);
                } else {
                    setPublisherID(strPackagePublisherID);
                }
            }
        } else if (hasAdsForCurrentOrientation()) {
            startTimers();
        } else {
            connect();
        }
        showToastIfPublisherIdNotSpecified();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectIfPublisherIDChanged() {
        if (publisherIdChanged()) {
            connect();
        }
    }

    private AdlantisAd currentAd() {
        AdlantisAd[] adlantisAdArrAdsForCurrentOrientation = adsForCurrentOrientation();
        if (adlantisAdArrAdsForCurrentOrientation == null || adlantisAdArrAdsForCurrentOrientation.length <= 0 || this._currentAdIndex >= adlantisAdArrAdsForCurrentOrientation.length) {
            return null;
        }
        return adlantisAdArrAdsForCurrentOrientation[this._currentAdIndex];
    }

    static int defaultBackgroundColor() {
        return ViewSettings.defaultBackgroundColor();
    }

    static Animation fadeInAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(BitmapDescriptorFactory.HUE_RED, 1.0f);
        alphaAnimation.setDuration(animationDuration());
        return alphaAnimation;
    }

    static Animation fadeOutAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, BitmapDescriptorFactory.HUE_RED);
        alphaAnimation.setDuration(animationDuration());
        return alphaAnimation;
    }

    private void handleUserTouchUp() {
        AdlantisAd adlantisAdCurrentAd = currentAd();
        if (adlantisAdCurrentAd == null || this._handlingUserEvent) {
            return;
        }
        this._handlingUserEvent = true;
        this._processIndicator.setVisibility(0);
        adlantisAdCurrentAd.sendImpressionCount();
        if (adlantisAdCurrentAd.tapUrlString() != null) {
            handleClickRequest(adlantisAdCurrentAd);
        } else {
            this._handlingUserEvent = false;
        }
    }

    static Animation inFromLeftAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, -1.0f, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED);
        translateAnimation.setDuration(animationDuration());
        return translateAnimation;
    }

    static Animation inFromRightAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, 1.0f, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED);
        translateAnimation.setDuration(animationDuration());
        return translateAnimation;
    }

    private boolean inView(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        boolean z = x >= BitmapDescriptorFactory.HUE_RED && x <= ((float) getWidth()) && y >= BitmapDescriptorFactory.HUE_RED && y <= ((float) getHeight());
        return z ? motionEvent.getEdgeFlags() == 0 : z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean openUri(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.addFlags(268435456);
        try {
            getContext().startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e(getClass().getSimpleName(), "activity not found for url=" + uri + " exception=" + e);
            return false;
        }
    }

    static Animation outToLeftAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, BitmapDescriptorFactory.HUE_RED, 2, -1.0f, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED);
        translateAnimation.setDuration(animationDuration());
        return translateAnimation;
    }

    static Animation outToRightAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(2, BitmapDescriptorFactory.HUE_RED, 2, 1.0f, 2, BitmapDescriptorFactory.HUE_RED, 2, BitmapDescriptorFactory.HUE_RED);
        translateAnimation.setDuration(animationDuration());
        return translateAnimation;
    }

    private String packagePublisherID() throws PackageManager.NameNotFoundException {
        try {
            ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                if (adManager().hasTestAdRequestUrls()) {
                    String str = (String) applicationInfo.metaData.get("Adlantis_adRequestUrl");
                    String[] strArr = {str};
                    if (str != null) {
                        adManager().setTestAdRequestUrls(strArr);
                    }
                }
                String str2 = (String) applicationInfo.metaData.get("Adlantis_keywords");
                if (str2 != null) {
                    adManager().setKeywords(str2);
                }
                String str3 = (String) applicationInfo.metaData.get("Adlantis_host");
                if (str3 != null) {
                    adManager().setHost(str3);
                }
                return (String) applicationInfo.metaData.get(AdManager.getInstance().publisherIDMetadataKey());
            }
        } catch (Exception e) {
            logD("packagePublisherID" + e);
        }
        return null;
    }

    private void setAdByIndex(int i) {
        logD("setAdByIndex=" + i + " this=" + this);
        if (this._rootViewFlipper == null) {
            logD("setAdByIndex _rootViewFlipper == null");
            return;
        }
        int iAdCountForCurrentOrientation = adCountForCurrentOrientation();
        if (iAdCountForCurrentOrientation != 0) {
            if (i >= iAdCountForCurrentOrientation) {
                i = 0;
            }
            (this._rootViewFlipper.getCurrentView() == this._adViews[0] ? this._adViews[1] : this._adViews[0]).setAdByIndex(i);
            logD("Animation: " + getClass().getSimpleName() + ".adCountChanged _rootViewFlipper.showNext view = " + this);
            this._rootViewFlipper.showNext();
        }
    }

    public static void setAnimationDuration(int i) {
        _animationDuration = i;
    }

    private void setButtonState(boolean z) {
        if (z != this._buttonPressed) {
            if (z) {
                this._touchHighlight.setVisibility(0);
                this._touchHighlight.startAnimation(touchFadeIn());
                setPressed(true);
            } else {
                this._touchHighlight.startAnimation(touchFadeOut());
                setPressed(false);
            }
            this._buttonPressed = z;
        }
    }

    private void setupLayout() {
        setClickable(true);
        this._rootViewFlipper = createRootViewFlipper();
        addView(this._rootViewFlipper, rootViewFlipperLayoutParams());
        View viewCreateTouchHighlight = createTouchHighlight();
        this._touchHighlight = viewCreateTouchHighlight;
        addView(viewCreateTouchHighlight);
        this._processIndicator = createProgressBar();
        addView(this._processIndicator);
        this._layoutComplete = true;
        logD("setupLayout setting _layoutComplete = true");
        setAnimationType(AnimationType.FADE);
        if (hasAdsForCurrentOrientation()) {
            logD("setupLayout calling setAdByIndex");
            this._currentAdIndex = 0;
            setAdByIndex(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTimers() {
        if (hasAdsForCurrentOrientation()) {
            this._handler.removeCallbacks(this.mRotateAdTask);
            this._handler.postDelayed(this.mRotateAdTask, adDisplayInterval());
        }
        this._handler.removeCallbacks(this.mUpdateAdsTask);
        if (this.mAdFetchInterval > 0) {
            this._handler.postDelayed(this.mUpdateAdsTask, adFetchInterval());
        }
    }

    private void stopTimers() {
        if (this._handler != null) {
            this._handler.removeCallbacks(this.mRotateAdTask);
            this._handler.removeCallbacks(this.mUpdateAdsTask);
        }
    }

    private Animation touchFadeIn() {
        Animation animationFadeInAnimation = fadeInAnimation();
        animationFadeInAnimation.setDuration(150L);
        animationFadeInAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: jp.adlantis.android.AdlantisAdViewContainer.8
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        return animationFadeInAnimation;
    }

    private Animation touchFadeOut() {
        Animation animationFadeOutAnimation = fadeOutAnimation();
        animationFadeOutAnimation.setDuration(150L);
        animationFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: jp.adlantis.android.AdlantisAdViewContainer.9
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                AdlantisAdViewContainer.this._touchHighlight.setVisibility(4);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        return animationFadeOutAnimation;
    }

    private void viewVisibilityChanged() {
        if (shouldRunAdTimers()) {
            startTimers();
        } else {
            stopTimers();
        }
    }

    protected int adCountForCurrentOrientation() {
        return getModel().adCountForOrientation(orientation());
    }

    protected AdManager adManager() {
        return AdManager.getInstance();
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void addRequestListener(AdRequestListener adRequestListener) {
        this.listeners.addRequestListener(adRequestListener);
    }

    public Map<String, String> additionalParametersForAdRequest() {
        HashMap map = new HashMap();
        map.put("orientation", orientationString());
        return map;
    }

    protected AdlantisAd[] adsForCurrentOrientation() {
        return getModel().adsForOrientation(orientation());
    }

    protected void adsWereLoaded() {
        adCountChanged();
        if (hasAdsForCurrentOrientation()) {
            this._currentAdIndex = 0;
            if (this._layoutComplete) {
                setAdByIndex(this._currentAdIndex);
            } else {
                logD("adsWereLoaded() layout not complete!!!");
            }
        }
        startTimers();
    }

    public void clearAds() {
        this._handler.removeCallbacks(this.mRotateAdTask);
        getModel().clearAds();
        adCountChanged();
    }

    public void connect() {
        if (getPublisherID() == null) {
            Log.e(getClass().getSimpleName(), getClass().getSimpleName() + ": can't connect because publisherID hasn't been set.");
            return;
        }
        logD("connect view =" + this);
        this.lastUsedPublisherID = getPublisherID();
        createAdRequest().connect(getContext(), additionalParametersForAdRequest(), null);
    }

    public AdRequest createAdRequest() {
        this.mCurrentAdRequest = new AdRequest(getModel());
        logD("createAdRequest adRequest = " + this.mCurrentAdRequest);
        this.mCurrentAdRequest.addRequestListener(new AdRequestListener() { // from class: jp.adlantis.android.AdlantisAdViewContainer.6
            @Override // jp.adlantis.android.AdRequestListener
            public void onFailedToReceiveAd(AdRequestNotifier adRequestNotifier) {
                AdlantisAdViewContainer.this.logD("onFailedToReceiveAd adRequest = " + AdlantisAdViewContainer.this.mCurrentAdRequest);
                AdlantisAdViewContainer.this.listeners.notifyListenersFailedToReceiveAd(adRequestNotifier);
                AdlantisAdViewContainer.this.mCurrentAdRequest = null;
            }

            @Override // jp.adlantis.android.AdRequestListener
            public void onReceiveAd(AdRequestNotifier adRequestNotifier) {
                AdlantisAdViewContainer.this.logD("onReceiveAd adRequest = " + AdlantisAdViewContainer.this.mCurrentAdRequest);
                AdlantisAdViewContainer.this.listeners.notifyListenersAdReceived(adRequestNotifier);
                AdlantisAdViewContainer.this.mCurrentAdRequest = null;
            }

            @Override // jp.adlantis.android.AdRequestListener
            public void onTouchAd(AdRequestNotifier adRequestNotifier) {
            }
        });
        return this.mCurrentAdRequest;
    }

    protected AdlantisAdView createAdlantisAdView() {
        AdlantisAdView adlantisAdView = new AdlantisAdView(getContext());
        adlantisAdView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        adlantisAdView.setAdsModel(getModel());
        return adlantisAdView;
    }

    protected ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(13);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(4);
        return progressBar;
    }

    protected ViewFlipper createRootViewFlipper() {
        AdlantisViewFlipper adlantisViewFlipper = new AdlantisViewFlipper(getContext());
        adlantisViewFlipper.setBackgroundColor(defaultBackgroundColor());
        if (!hasAdsForCurrentOrientation()) {
            adlantisViewFlipper.setVisibility(4);
        }
        this._adViews = new AdlantisAdView[2];
        for (int i = 0; i < this._adViews.length; i++) {
            this._adViews[i] = createAdlantisAdView();
            adlantisViewFlipper.addView(this._adViews[i]);
        }
        return adlantisViewFlipper;
    }

    protected View createTouchHighlight() {
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        view.setBackgroundColor(1728053247);
        view.setVisibility(4);
        return view;
    }

    protected void dumpLayoutGeometry() {
        ADLDebugUtils.dumpSubviewLayout(this, 0);
    }

    protected AdlantisAdsModel getModel() {
        return this._adsModel;
    }

    public String getPublisherID() {
        return adManager().getPublisherID();
    }

    protected int getWindowCurrentVisibility() {
        return this._inOnWindowVisibilityChanged ? this._onWindowVisibilityChangedVisibility : getWindowVisibility();
    }

    protected void handleClickRequest(AdlantisAd adlantisAd) {
        adManager().handleClickRequest(adlantisAd, new AdManager.AdManagerRedirectUrlProcessedCallback() { // from class: jp.adlantis.android.AdlantisAdViewContainer.7
            @Override // jp.adlantis.android.AdManager.AdManagerRedirectUrlProcessedCallback
            public void redirectProcessed(Uri uri) {
                boolean zOpenUri = AdlantisAdViewContainer.this.openUri(uri);
                AdlantisAdViewContainer.this._processIndicator.setVisibility(4);
                AdlantisAdViewContainer.this._handlingUserEvent = false;
                if (zOpenUri) {
                    return;
                }
                AdlantisAdViewContainer.this.startTimers();
            }
        });
        logD("onTouchAd AdRequestNotifier = " + this);
        this.listeners.notifyListenersAdTouched(this);
    }

    protected boolean hasAdsForCurrentOrientation() {
        return adCountForCurrentOrientation() > 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return (this._detachingFromWindow || getWindowToken() == null || !getWindowToken().pingBinder()) ? false : true;
    }

    boolean isDoingAdRequest() {
        return this.mCurrentAdRequest != null;
    }

    public boolean isNetworkAvailable() {
        return AdManager.isNetworkAvailable(getContext());
    }

    protected void logD(String str) {
        Log.d(getClass().getSimpleName(), str);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewVisibilityChanged();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this._detachingFromWindow = true;
        super.onDetachedFromWindow();
        viewVisibilityChanged();
        this._detachingFromWindow = false;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this._rootViewFlipper.setLayoutParams(rootViewFlipperLayoutParams());
        postRequestLayout();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (currentAd() == null) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                stopTimers();
                setButtonState(true);
                break;
            case 1:
                setButtonState(false);
                if (!inView(motionEvent)) {
                    startTimers();
                    break;
                } else {
                    handleUserTouchUp();
                    break;
                }
            case 2:
                setButtonState(inView(motionEvent));
                break;
            case 3:
            case 4:
                setButtonState(false);
                startTimers();
                break;
        }
        return true;
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        this._inOnWindowVisibilityChanged = true;
        this._onWindowVisibilityChangedVisibility = i;
        super.onWindowVisibilityChanged(i);
        viewVisibilityChanged();
        this._inOnWindowVisibilityChanged = false;
    }

    protected int orientation() {
        return AdlantisUtils.orientation(this);
    }

    protected String orientationString() {
        return AdlantisUtils.orientationToString(orientation());
    }

    protected void postRequestLayout() {
        post(new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.3
            @Override // java.lang.Runnable
            public void run() {
                AdlantisAdViewContainer.this.requestLayout();
            }
        });
    }

    public boolean publisherIdChanged() {
        String publisherID = getPublisherID();
        return (publisherID == this.lastUsedPublisherID && (publisherID == null || publisherID.equals(this.lastUsedPublisherID))) ? false : true;
    }

    @Override // jp.adlantis.android.AdRequestNotifier
    public void removeRequestListener(AdRequestListener adRequestListener) {
        this.listeners.removeRequestListener(adRequestListener);
    }

    protected RelativeLayout.LayoutParams rootViewFlipperLayoutParams() {
        Rect rectAdSizeForOrientation = AdlantisUtils.adSizeForOrientation(orientation());
        float fDisplayDensity = AdlantisUtils.displayDensity(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (rectAdSizeForOrientation.width() * fDisplayDensity), (int) (rectAdSizeForOrientation.height() * fDisplayDensity));
        layoutParams.addRule(13);
        return layoutParams;
    }

    protected void setAdFetchInterval(long j) {
        this.mAdFetchInterval = j;
        startTimers();
    }

    public void setAnimationType(AnimationType animationType) {
        if (this._rootViewFlipper == null) {
        }
        switch (animationType) {
            case NONE:
                this._rootViewFlipper.setInAnimation(null);
                this._rootViewFlipper.setOutAnimation(null);
                break;
            case FADE:
                this._rootViewFlipper.setInAnimation(fadeInAnimation());
                this._rootViewFlipper.setOutAnimation(fadeOutAnimation());
                break;
            case SLIDE_FROM_RIGHT:
                this._rootViewFlipper.setInAnimation(inFromRightAnimation());
                this._rootViewFlipper.setOutAnimation(outToLeftAnimation());
                break;
            case SLIDE_FROM_LEFT:
                this._rootViewFlipper.setInAnimation(inFromLeftAnimation());
                this._rootViewFlipper.setOutAnimation(outToRightAnimation());
                break;
        }
    }

    public void setGapPublisherID(String str) {
        adManager().setGapPublisherID(str);
        connectIfPublisherIDChanged();
    }

    public void setKeywords(String str) {
        adManager().setKeywords(str);
    }

    public void setPublisherID(String str) {
        adManager().setPublisherID(str);
        connectIfPublisherIDChanged();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        viewVisibilityChanged();
    }

    public void setupModelListener() {
        getModel().addListener(new AdlantisAdsModel.AdlantisAdsModelListener() { // from class: jp.adlantis.android.AdlantisAdViewContainer.4
            @Override // jp.adlantis.android.model.OnChangeListener
            public void onChange(AdlantisAdsModel adlantisAdsModel) {
                AdlantisAdViewContainer.this._handler.post(new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AdlantisAdViewContainer.this.adsWereLoaded();
                    }
                });
            }
        });
    }

    protected boolean shouldRunAdTimers() {
        return getParent() != null && getVisibility() == 0 && isAttachedToWindow() && getWindowCurrentVisibility() == 0;
    }

    public void showNextAd() {
        int iAdCountForCurrentOrientation = adCountForCurrentOrientation();
        if (iAdCountForCurrentOrientation > 1) {
            this._currentAdIndex = (this._currentAdIndex + 1) % iAdCountForCurrentOrientation;
            setAdByIndex(this._currentAdIndex);
        }
    }

    public void showToastIfPublisherIdNotSpecified() {
        if (getPublisherID() == null) {
            this._handler.postDelayed(new Runnable() { // from class: jp.adlantis.android.AdlantisAdViewContainer.5
                @Override // java.lang.Runnable
                public void run() {
                    if (AdlantisAdViewContainer.this.getPublisherID() == null) {
                        Toast.makeText(AdlantisAdViewContainer.this.getContext(), "AdlantisView publisher id not set", 1).show();
                        Log.e(getClass().getSimpleName(), getClass().getSimpleName() + ": can't display ads because publisherID hasn't been set.");
                    } else {
                        if (AdlantisAdViewContainer.this.isDoingAdRequest()) {
                            return;
                        }
                        AdlantisAdViewContainer.this.connectIfPublisherIDChanged();
                    }
                }
            }, this._idNotSpecifiedWarningInterval);
        }
    }
}
