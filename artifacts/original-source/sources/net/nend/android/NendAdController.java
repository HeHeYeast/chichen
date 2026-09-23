package net.nend.android;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendAdController {
    private static final int MESSAGE_CODE = 718;
    private final Ad mAd;
    private final Handler mHandler;
    private boolean mHasWindowFocus = false;
    private boolean mReloadable = true;

    private static class ControllerHandler extends Handler {
        private WeakReference<Ad> weakReference;

        ControllerHandler(Looper looper, Ad ad) {
            super(looper);
            this.weakReference = new WeakReference<>(ad);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Ad ad = this.weakReference.get();
            if (ad != null) {
                ad.requestAd();
            }
        }
    }

    NendAdController(Ad ad) {
        if (ad == null) {
            throw new NullPointerException("Ad object is null.");
        }
        this.mAd = ad;
        this.mHandler = new ControllerHandler(Looper.getMainLooper(), ad);
    }

    void setReloadable(boolean reloadable) {
        this.mReloadable = reloadable;
        if (reloadable) {
            reloadAd();
        } else {
            cancelRequest();
        }
    }

    void requestAd() {
        cancelRequest();
        this.mHandler.sendEmptyMessage(MESSAGE_CODE);
    }

    boolean reloadAd() {
        if (!this.mReloadable || !this.mHasWindowFocus || this.mHandler.hasMessages(MESSAGE_CODE)) {
            return false;
        }
        this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE, this.mAd.getReloadIntervalInSeconds() * 1000);
        return true;
    }

    void cancelRequest() {
        this.mHandler.removeMessages(MESSAGE_CODE);
        this.mAd.cancelRequest();
    }

    void onWindowFocusChanged(boolean hasWindowFocus) {
        this.mHasWindowFocus = hasWindowFocus;
        if (hasWindowFocus && this.mAd.isRequestable()) {
            reloadAd();
        }
    }
}
