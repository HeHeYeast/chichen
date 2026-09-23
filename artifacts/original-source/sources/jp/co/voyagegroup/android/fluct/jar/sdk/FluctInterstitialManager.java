package jp.co.voyagegroup.android.fluct.jar.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import jp.co.voyagegroup.android.fluct.jar.FluctInterstitial;
import jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity;
import jp.co.voyagegroup.android.fluct.jar.db.FluctDbAccess;
import jp.co.voyagegroup.android.fluct.jar.db.FluctInterstitialTable;
import jp.co.voyagegroup.android.fluct.jar.task.FluctAdapterThread;
import jp.co.voyagegroup.android.fluct.jar.util.FluctUtils;
import jp.co.voyagegroup.android.fluct.jar.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctInterstitialManager {
    private static final String TAG = "FluctInterstitialManager";
    private static FluctAdapterThread mAdapterThread;
    private static Handler mCallbackHandler;
    private Context mContext;
    private int mFrameColor;
    private String mMediaId;
    private static FluctInterstitial.FluctInterstitialCallback mCallback = null;
    private static boolean mAdStatus = false;

    public FluctInterstitialManager(Context context, String mediaId) {
        Log.d(TAG, "FluctInterstitialManager : MediaID is " + mediaId);
        this.mContext = context;
        this.mMediaId = mediaId;
        mCallbackHandler = new CallbackHandler(Looper.getMainLooper(), null);
    }

    public void showIntersitialAd(int frameColor) {
        Log.d(TAG, "showIntersitialAd : frameColor is " + frameColor);
        if (!mAdStatus) {
            mAdStatus = true;
            show(frameColor);
        } else {
            callback(100);
        }
    }

    public void setFluctInterstitialCallback(FluctInterstitial.FluctInterstitialCallback cb) {
        Log.d(TAG, "setFluctInterstitialCallback : ");
        mCallback = cb;
    }

    public void startShowing(String message) {
        Log.d(TAG, "startShowing : message is " + message);
        FluctInterstitialTable data = FluctDbAccess.getInterstitial(this.mContext, this.mMediaId);
        if (data != null) {
            showActivity(data);
            return;
        }
        int status = 6;
        if (message != null) {
            Log.d(TAG, "startShowing : error " + message);
            status = 8;
        }
        callback(status);
    }

    public static void callback(int status) {
        Log.d(TAG, "callback : status " + status);
        Message msg = new Message();
        msg.what = status;
        msg.obj = mCallback;
        mCallbackHandler.sendMessage(msg);
        mAdapterThread = null;
    }

    private void show(int color) {
        Log.d(TAG, "show : color is " + color);
        if (this.mMediaId.equals("")) {
            callback(5);
            return;
        }
        if (!FluctUtils.isNetWorkAvailable(this.mContext)) {
            callback(4);
            return;
        }
        this.mFrameColor = color;
        FluctInterstitialTable data = FluctDbAccess.getInterstitial(this.mContext, this.mMediaId);
        if (data == null) {
            Log.d(TAG, "show : mAdapterThread is " + mAdapterThread);
            if (mAdapterThread == null) {
                mAdapterThread = new FluctAdapterThread(this.mContext, this.mMediaId, 3, this, true);
                mAdapterThread.start();
                return;
            }
            return;
        }
        showActivity(data);
        if (mAdapterThread == null) {
            mAdapterThread = new FluctAdapterThread(this.mContext, this.mMediaId, 3, this, false);
            mAdapterThread.start();
        }
    }

    private void showActivity(FluctInterstitialTable data) {
        Log.d(TAG, "showActivity : ");
        int status = 0;
        if (-1 == data.getAdHtml().indexOf("<html>")) {
            status = 6;
        } else {
            double rate = Math.random() * 100.0d;
            if (rate < data.getRate()) {
                Intent intent = new Intent(this.mContext, (Class<?>) FluctInterstitialActivity.class);
                intent.setFlags(268435456);
                intent.putExtra("media_id", this.mMediaId);
                intent.putExtra("frame_color", this.mFrameColor);
                this.mContext.startActivity(intent);
            } else {
                status = 3;
            }
        }
        if (status != 0) {
            callback(status);
        }
    }

    private static final class CallbackHandler extends Handler {
        private static final String TAG = "CallbackHandler";

        /* synthetic */ CallbackHandler(Looper looper, CallbackHandler callbackHandler) {
            this(looper);
        }

        private CallbackHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage : msg is " + msg);
            if (msg.obj != null) {
                FluctInterstitial.FluctInterstitialCallback interstitialCallback = (FluctInterstitial.FluctInterstitialCallback) msg.obj;
                switch (msg.what) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 100:
                        FluctInterstitialManager.mAdStatus = false;
                        break;
                }
                if (interstitialCallback != null) {
                    interstitialCallback.onReceiveAdInfo(msg.what);
                }
            }
        }
    }
}
