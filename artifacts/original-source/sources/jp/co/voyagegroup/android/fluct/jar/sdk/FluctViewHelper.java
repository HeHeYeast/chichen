package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.voyagegroup.android.fluct.jar.FluctView;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctAd;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.task.FluctAdapterThread;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctViewHelper implements FluctAdapterThread.FluctAdapterThreadListener {
    private static final String TAG = "FluctViewHelper";
    private TranslateAnimation mAnimationIn;
    private int mAnimationIndex;
    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() { // from class: jp.co.voyagegroup.android.fluct.jar.sdk.FluctViewHelper.1
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            FluctViewHelper.this.flipWebView();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            FluctViewHelper.this.mWebViewToLoad.setVisibility(8);
        }
    };
    private TranslateAnimation mAnimationOut;
    private Context mContext;
    private FluctView mFluctView;
    private FluctWebView mFluctWebViewSecond;
    private FluctWebView mFluctWebviewFirst;
    private boolean mHasPreLoad;
    private Timer mLoadTimer;
    private Handler mMainThreadHandler;
    private String mMediaId;
    private Timer mRefreshTimer;
    private FluctSetting mSetting;
    private FluctWebView mWebViewToLoad;

    public FluctViewHelper(Context context, FluctView fluctView, String mediaId) {
        Log.d(TAG, "FluctViewHelper : ");
        this.mContext = context;
        this.mFluctView = fluctView;
        this.mMediaId = mediaId;
        this.mMainThreadHandler = new FluctMainThreadHandler(Looper.getMainLooper(), null);
        this.mAnimationIndex = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFluctViewHelper() {
        Log.d(TAG, "initFluctViewHelper : ");
        this.mFluctWebviewFirst = new FluctWebView(this.mContext, this.mSetting);
        this.mFluctWebViewSecond = new FluctWebView(this.mContext, this.mSetting);
        this.mFluctView.addView(this.mFluctWebViewSecond);
        this.mFluctView.addView(this.mFluctWebviewFirst);
        this.mWebViewToLoad = this.mFluctWebviewFirst;
        FluctAd fluctAd = this.mSetting.getFluctAd();
        if (fluctAd != null && FluctUtils.isNetWorkAvailable(this.mContext)) {
            this.mFluctWebviewFirst.setAdHtml(fluctAd);
            showWebViewLoaded();
        }
    }

    public void destroyFluctWebView(FluctView fluctView) {
        Log.d(TAG, "destroyFluctWebView : ");
        stopAnimation();
        if (this.mFluctWebviewFirst != null) {
            fluctView.removeView(this.mFluctWebviewFirst);
            this.mFluctWebviewFirst.destroy();
            this.mFluctWebviewFirst = null;
        }
        if (this.mFluctWebViewSecond != null) {
            fluctView.removeView(this.mFluctWebViewSecond);
            this.mFluctWebViewSecond.destroy();
            this.mFluctWebViewSecond = null;
        }
        this.mWebViewToLoad = null;
        this.mContext = null;
    }

    public void stopAnimation() {
        Log.d(TAG, "stopAnimation : ");
        if (this.mAnimationIn != null) {
            this.mAnimationIn.setAnimationListener(null);
            if (Build.VERSION.SDK_INT > 7) {
                this.mAnimationIn.cancel();
            }
            this.mAnimationIn.reset();
            this.mAnimationIn = null;
        }
        if (this.mAnimationOut != null) {
            this.mAnimationOut.setAnimationListener(null);
            if (Build.VERSION.SDK_INT > 7) {
                this.mAnimationOut.cancel();
            }
            this.mAnimationOut.reset();
            this.mAnimationOut = null;
        }
        if (this.mFluctWebviewFirst != null) {
            this.mFluctWebviewFirst.clearAnimation();
        }
        if (this.mFluctWebViewSecond != null) {
            this.mFluctWebViewSecond.clearAnimation();
        }
    }

    public void loadFluctWebView() {
        Log.d(TAG, "loadFluctWebView : ");
        if (this.mWebViewToLoad != null) {
            FluctAd fluctAd = this.mSetting.getFluctAd();
            if (fluctAd != null && FluctUtils.isNetWorkAvailable(this.mContext)) {
                this.mWebViewToLoad.setAdHtml(fluctAd);
                this.mWebViewToLoad.setVisibility(4);
            } else {
                this.mWebViewToLoad.setVisibility(8);
            }
        }
    }

    public void refreshFluctWebView() {
        Log.d(TAG, "refreshFluctWebView : ");
        if (this.mWebViewToLoad != null) {
            showWebViewLoaded();
        }
    }

    private void showWebViewLoaded() {
        Log.d(TAG, "showWebViewLoaded : ");
        if (FluctUtils.isNetWorkAvailable(this.mContext)) {
            this.mWebViewToLoad.setVisibility(0);
            if (this.mSetting.getAnimations() != null && this.mSetting.getAnimations().size() > 0) {
                this.mAnimationIn = null;
                this.mAnimationOut = null;
                prepareAnimation();
                if (this.mAnimationIn == null && this.mAnimationOut == null) {
                    flipWebView();
                    this.mWebViewToLoad.setVisibility(8);
                } else {
                    this.mWebViewToLoad.bringToFront();
                    this.mWebViewToLoad.setAnimation(this.mAnimationIn);
                    if (this.mWebViewToLoad == this.mFluctWebviewFirst) {
                        this.mFluctWebViewSecond.setAnimation(this.mAnimationOut);
                    } else {
                        this.mFluctWebviewFirst.setAnimation(this.mAnimationOut);
                    }
                    this.mAnimationOut.start();
                    this.mAnimationIn.start();
                }
                this.mAnimationIndex++;
                if (this.mAnimationIndex >= this.mSetting.getAnimations().size()) {
                    this.mAnimationIndex = 0;
                    return;
                }
                return;
            }
            flipWebView();
            this.mWebViewToLoad.setVisibility(8);
            return;
        }
        this.mFluctWebviewFirst.setVisibility(8);
        this.mFluctWebViewSecond.setVisibility(8);
    }

    private void prepareAnimation() {
        Log.d(TAG, "prepareAnimation : ");
        FluctSetting.Animation animation = this.mSetting.getAnimations().get(this.mAnimationIndex);
        int toX = 0;
        int toY = 0;
        int fromX = 0;
        int fromY = 0;
        switch (animation.getType()) {
            case 1:
                toY = this.mWebViewToLoad.getHeight();
                fromY = 0 - this.mWebViewToLoad.getHeight();
                break;
            case 2:
                toY = 0 - this.mWebViewToLoad.getHeight();
                fromY = this.mWebViewToLoad.getHeight();
                break;
            case 3:
                toX = this.mWebViewToLoad.getWidth();
                fromX = 0 - this.mWebViewToLoad.getWidth();
                break;
            case 4:
                toX = 0 - this.mWebViewToLoad.getWidth();
                fromX = this.mWebViewToLoad.getWidth();
                break;
        }
        if (toX != 0 || toY != 0 || fromX != 0 || fromY != 0) {
            this.mAnimationOut = new TranslateAnimation(BitmapDescriptorFactory.HUE_RED, toX, BitmapDescriptorFactory.HUE_RED, toY);
            this.mAnimationOut.setDuration(animation.getDuration());
            this.mAnimationIn = new TranslateAnimation(fromX, BitmapDescriptorFactory.HUE_RED, fromY, BitmapDescriptorFactory.HUE_RED);
            this.mAnimationIn.setDuration(animation.getDuration());
            this.mAnimationIn.setAnimationListener(this.mAnimationListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void flipWebView() {
        Log.d(TAG, "flipWebView : ");
        synchronized (this.mWebViewToLoad) {
            if (this.mWebViewToLoad.equals(this.mFluctWebviewFirst)) {
                this.mWebViewToLoad = this.mFluctWebViewSecond;
                Log.v(TAG, "flipWebView : SwitchWebView First -> Second");
            } else {
                this.mWebViewToLoad = this.mFluctWebviewFirst;
                Log.v(TAG, "flipWebView : SwitchWebView Second -> First");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextRefresh() {
        Log.d(TAG, "scheduleNextRefresh : ");
        long refreshTime = this.mSetting.getRefreshTime() * 1000;
        if (refreshTime > 0) {
            long loadDelayTime = getLoadDelayTime();
            if (loadDelayTime != -1) {
                this.mHasPreLoad = true;
                scheduleLoadTimer(loadDelayTime);
            } else {
                this.mHasPreLoad = false;
            }
            scheduleRefreshTimer();
        }
    }

    private void scheduleRefreshTimer() {
        Log.d(TAG, "scheduleRefreshTimer : ");
        if (this.mRefreshTimer != null) {
            this.mRefreshTimer.schedule(new TimerTask() { // from class: jp.co.voyagegroup.android.fluct.jar.sdk.FluctViewHelper.2
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (!FluctViewHelper.this.mHasPreLoad) {
                        FluctViewHelper.this.mSetting = FluctConfig.getInstance().getConfigFromMap(FluctViewHelper.this.mMediaId);
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = FluctViewHelper.this;
                        FluctViewHelper.this.mMainThreadHandler.sendMessage(msg);
                    }
                    Message msg2 = new Message();
                    msg2.what = 1;
                    msg2.obj = FluctViewHelper.this;
                    FluctViewHelper.this.mMainThreadHandler.sendMessage(msg2);
                    FluctViewHelper.this.scheduleNextRefresh();
                }
            }, this.mSetting.getRefreshTime() * 1000);
        }
    }

    private void scheduleLoadTimer(long loadDelayTime) {
        Log.d(TAG, "scheduleLoadTimer : loadDelayTime is " + loadDelayTime);
        if (this.mLoadTimer != null) {
            this.mLoadTimer.schedule(new TimerTask() { // from class: jp.co.voyagegroup.android.fluct.jar.sdk.FluctViewHelper.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    FluctViewHelper.this.mSetting = FluctConfig.getInstance().getConfigFromMap(FluctViewHelper.this.mMediaId);
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = FluctViewHelper.this;
                    FluctViewHelper.this.mMainThreadHandler.sendMessage(msg);
                }
            }, loadDelayTime);
        }
    }

    public synchronized void stopTimer() {
        Log.d(TAG, "stopTimer : ");
        if (this.mLoadTimer != null) {
            this.mLoadTimer.cancel();
            this.mLoadTimer.purge();
            this.mLoadTimer = null;
        }
        if (this.mRefreshTimer != null) {
            this.mRefreshTimer.cancel();
            this.mRefreshTimer.purge();
            this.mRefreshTimer = null;
        }
        if (this.mFluctWebviewFirst != null && this.mFluctWebViewSecond != null && (this.mFluctWebviewFirst.getVisibility() == 0 || this.mFluctWebViewSecond.getVisibility() == 0)) {
            if (this.mFluctWebviewFirst.getVisibility() == 0) {
                this.mFluctWebviewFirst.setVisibility(4);
            } else {
                this.mFluctWebviewFirst.setVisibility(8);
            }
            if (this.mFluctWebViewSecond.getVisibility() == 0) {
                this.mFluctWebViewSecond.setVisibility(4);
            } else {
                this.mFluctWebViewSecond.setVisibility(8);
            }
        }
    }

    public synchronized void stopTimerAndDelWebView() {
        Log.d(TAG, "stopTimerAndDelWebView : ");
        stopTimer();
        destroyFluctWebView(this.mFluctView);
        Log.d(TAG, "dispose FluctPreferences : ");
        FluctPreferences.getInstance().dispose();
    }

    private long getLoadDelayTime() {
        Log.d(TAG, "getLoadDelayTime : ");
        long loadTime = this.mSetting.getLoadTime();
        long refreshTime = this.mSetting.getRefreshTime() * 1000;
        Log.v(TAG, "getLoadDelayTime : LoadTime is " + loadTime + " RefreshTime is " + refreshTime);
        if (refreshTime <= loadTime || loadTime <= 0) {
            return -1L;
        }
        long loadDelayTime = refreshTime - loadTime;
        return loadDelayTime;
    }

    @Override // jp.co.voyagegroup.android.fluct.jar.task.FluctAdapterThread.FluctAdapterThreadListener
    public void initWebView() {
        Log.d(TAG, "initWebView : ");
        this.mSetting = FluctConfig.getInstance().getConfigFromMap(this.mMediaId);
        Message msg = new Message();
        msg.what = 3;
        msg.obj = this;
        this.mMainThreadHandler.sendMessage(msg);
    }

    @Override // jp.co.voyagegroup.android.fluct.jar.task.FluctAdapterThread.FluctAdapterThreadListener
    public void startTimer() {
        Log.d(TAG, "startTimer : ");
        if (this.mRefreshTimer == null) {
            this.mRefreshTimer = new Timer();
        }
        if (this.mLoadTimer == null) {
            this.mLoadTimer = new Timer();
        }
        scheduleNextRefresh();
    }

    private static final class FluctMainThreadHandler extends Handler {
        private static final String TAG = "FluctMainThreadHandler";

        /* synthetic */ FluctMainThreadHandler(Looper looper, FluctMainThreadHandler fluctMainThreadHandler) {
            this(looper);
        }

        private FluctMainThreadHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage : msg is " + msg);
            if (msg.obj != null) {
                FluctViewHelper helper = (FluctViewHelper) msg.obj;
                try {
                    switch (msg.what) {
                        case 1:
                            helper.refreshFluctWebView();
                            break;
                        case 2:
                            helper.loadFluctWebView();
                            break;
                        case 3:
                            helper.initFluctViewHelper();
                            break;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage : Exception is " + e.getLocalizedMessage());
                }
            }
        }
    }
}
