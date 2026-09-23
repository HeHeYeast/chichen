package jp.co.voyagegroup.android.fluct.jar.task;

import android.content.Context;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctConfig;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctInterstitialManager;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctPreferences;
import jp.co.voyagegroup.android.fluct.jar.setting.FluctSetting;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctAdapterThread extends Thread {
    private static final String TAG = "FluctAdapterThread";
    private Context mContext;
    private boolean mHalt;
    private FluctInterstitialManager mInterstitial;
    private boolean mInterstitialCallback;
    private String mMediaId;
    private FluctAdapterThreadListener mThreadListener;
    private int mType;

    public interface FluctAdapterThreadListener {
        void initWebView();

        void startTimer();
    }

    public FluctAdapterThread(Context context, String mediaId, int type, FluctAdapterThreadListener listener) {
        Log.d(TAG, "FluctAdapterThread : ");
        this.mContext = context;
        this.mMediaId = mediaId;
        this.mType = type;
        this.mThreadListener = listener;
        this.mHalt = false;
    }

    public FluctAdapterThread(Context context, String mediaId, int type, FluctInterstitialManager interstitial, boolean callback) {
        Log.d(TAG, "FluctAdapterThread : ");
        this.mContext = context;
        this.mMediaId = mediaId;
        this.mType = type;
        this.mInterstitial = interstitial;
        this.mInterstitialCallback = callback;
        this.mHalt = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Log.d(TAG, "FluctAdapterThread run : start run in : " + this.mType);
        String errorMsg = null;
        try {
            try {
                if (!this.mHalt) {
                    FluctConfig config = FluctConfig.getInstance();
                    FluctPreferences perferences = FluctPreferences.getInstance();
                    perferences.makeFluctPreferences(this.mContext);
                    switch (this.mType) {
                        case 1:
                            Log.v(TAG, "FluctAdapterThread run : prepare get config start");
                            config.getFromNet(this.mContext, this.mMediaId);
                            break;
                        case 2:
                            Log.v(TAG, "FluctAdapterThread run : normal get config start");
                            boolean isNeedUpdate = getConfigFromDBorNet();
                            startListener();
                            if (isNeedUpdate) {
                                Log.v(TAG, "FluctAdapterThread run : need update config");
                                Thread.currentThread().setPriority(Thread.currentThread().getPriority() - 2);
                                config.getFromNet(this.mContext, this.mMediaId);
                                break;
                            }
                            break;
                        case 3:
                            Log.v(TAG, "FluctAdapterThread run : interstitial get config start");
                            errorMsg = config.getFromNetErrorMsg(this.mContext, this.mMediaId);
                            break;
                    }
                }
                this.mContext = null;
                this.mMediaId = null;
                this.mThreadListener = null;
                if (this.mInterstitial != null && this.mInterstitialCallback) {
                    this.mInterstitial.startShowing(errorMsg);
                }
                this.mInterstitial = null;
                this.mInterstitialCallback = false;
            } catch (Exception e) {
                Log.e(TAG, "FluctAdapterThread run : Exception is " + e.getLocalizedMessage());
                this.mContext = null;
                this.mMediaId = null;
                this.mThreadListener = null;
                if (this.mInterstitial != null && this.mInterstitialCallback) {
                    this.mInterstitial.startShowing(null);
                }
                this.mInterstitial = null;
                this.mInterstitialCallback = false;
            }
        } catch (Throwable th) {
            this.mContext = null;
            this.mMediaId = null;
            this.mThreadListener = null;
            if (this.mInterstitial != null && this.mInterstitialCallback) {
                this.mInterstitial.startShowing(null);
            }
            this.mInterstitial = null;
            this.mInterstitialCallback = false;
            throw th;
        }
    }

    private void startListener() {
        Log.d(TAG, "startListener : ");
        FluctSetting setting = FluctConfig.getInstance().getConfigFromMap(this.mMediaId);
        if (setting != null) {
            Log.v(TAG, "startListener : start the timer of FluctViewHelper");
            this.mThreadListener.initWebView();
            this.mThreadListener.startTimer();
        }
    }

    private boolean getConfigFromDBorNet() {
        Log.d(TAG, "getConfigFromDBorNet : ");
        FluctConfig config = FluctConfig.getInstance();
        FluctSetting setting = config.getFromDB(this.mContext, this.mMediaId);
        if (setting != null) {
            return true;
        }
        Log.v(TAG, "getConfigFromDBorNet : DB has no config");
        FluctSetting setting2 = config.getFromNet(this.mContext, this.mMediaId);
        if (setting2 == null) {
            Log.v(TAG, "getConfigFromDBorNet : got writelock failed");
            FluctSetting setting3 = config.getFromDBWithResultLock(this.mContext, this.mMediaId);
            if (setting3 == null) {
                return true;
            }
            Log.v(TAG, "getConfigFromDBorNet : got config by waiting for DB");
            return false;
        }
        Log.v(TAG, "getConfigFromDBorNet : got config from network");
        return false;
    }

    public void halt() {
        Log.d(TAG, "halt : ");
        this.mHalt = true;
        interrupt();
    }
}
