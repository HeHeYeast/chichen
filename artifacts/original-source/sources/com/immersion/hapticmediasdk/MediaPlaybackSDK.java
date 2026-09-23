package com.immersion.hapticmediasdk;

import android.content.Context;
import android.webkit.URLUtil;
import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.utils.Log;
import java.io.File;
import java.net.MalformedURLException;
import rrrrrr.rrccrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MediaPlaybackSDK extends HapticContentSDK {
    private static final String a = "HapticContentSDK";

    /* renamed from: b044A044Aъъ044Aъ, reason: contains not printable characters */
    public static int f33b044A044A044A = 1;

    /* renamed from: b044Aъъъ044Aъ, reason: contains not printable characters */
    public static int f34b044A044A = 13;

    /* renamed from: bъ044Aъъ044Aъ, reason: contains not printable characters */
    public static int f35b044A044A = 0;

    /* renamed from: bъъ044Aъ044Aъ, reason: contains not printable characters */
    public static int f36b044A044A = 2;
    private int b;

    public MediaPlaybackSDK(Context context) throws Exception {
        try {
            super(0, context);
            if (((f34b044A044A + f33b044A044A044A) * f34b044A044A) % f36b044A044A != f35b044A044A) {
                f34b044A044A = m29b044A044A044A();
                f35b044A044A = m29b044A044A044A();
            }
            try {
                this.b = 400;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private boolean a(String str) {
        boolean z = false;
        try {
            rrccrr rrccrrVar = new rrccrr(this, str.replaceFirst("https", "http"));
            new Thread(rrccrrVar, "ping url").start();
            synchronized (rrccrrVar) {
                this.b = -100;
                while (this.b < 0) {
                    try {
                        rrccrrVar.wait(100L);
                    } catch (InterruptedException e) {
                    }
                }
                while (true) {
                    switch (z) {
                        case false:
                            break;
                        case true:
                            break;
                        default:
                            while (true) {
                                switch (z) {
                                }
                            }
                            break;
                    }
                }
                if (200 <= this.b && this.b <= 399) {
                    z = true;
                }
            }
        } catch (MalformedURLException e2) {
            Log.e(a, e2.getMessage());
        }
        return z;
    }

    private int b(String str) {
        if (str == null) {
            Log.e(a, "invalid local hapt file url: null");
            return -4;
        }
        File file = new File(str);
        if (!file.isFile()) {
            Log.e(a, "invalid local hapt file url: directory");
            return -4;
        }
        if (file.canRead()) {
            this.mMediaTaskManager.setHapticsUrl(str, false);
            return this.mMediaTaskManager.transitToState(HapticContentSDK.SDKStatus.INITIALIZED);
        }
        Log.e(a, "could not access local hapt file: permission denied");
        return -3;
    }

    /* renamed from: b044A044A044Aъ044Aъ, reason: contains not printable characters */
    public static int m28b044A044A044A044A() {
        return 2;
    }

    /* renamed from: b044Aъ044Aъ044Aъ, reason: contains not printable characters */
    public static int m29b044A044A044A() {
        return 61;
    }

    /* renamed from: bллл043B043Bл, reason: contains not printable characters */
    public static /* synthetic */ int m30b043B043B(MediaPlaybackSDK mediaPlaybackSDK, int i) throws Exception {
        if (((m29b044A044A044A() + f33b044A044A044A) * m29b044A044A044A()) % f36b044A044A != f35b044A044A) {
            f34b044A044A = 4;
            f35b044A044A = m29b044A044A044A();
        }
        try {
            mediaPlaybackSDK.b = i;
            return i;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bъ044A044Aъ044Aъ, reason: contains not printable characters */
    public static int m31b044A044A044A() {
        return 1;
    }

    @Override // com.immersion.hapticmediasdk.HapticContentSDK
    public int openHaptics(String str) {
        HapticContentSDK.SDKStatus sDKStatus = getSDKStatus();
        if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED && sDKStatus != HapticContentSDK.SDKStatus.NOT_INITIALIZED && sDKStatus != HapticContentSDK.SDKStatus.INITIALIZED && sDKStatus != HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
            return -1;
        }
        int iTransitToState = this.mMediaTaskManager.transitToState(HapticContentSDK.SDKStatus.NOT_INITIALIZED);
        if (iTransitToState != 0) {
            return iTransitToState;
        }
        if (!URLUtil.isValidUrl(str)) {
            return b(str);
        }
        if (!URLUtil.isHttpUrl(str) && !URLUtil.isHttpsUrl(str)) {
            if (URLUtil.isFileUrl(str)) {
                return b(str);
            }
            Log.e(a, "could not access hapt file url: unsupposted protocol");
            return -5;
        }
        if (a(str)) {
            this.mMediaTaskManager.setHapticsUrl(str, true);
            return this.mMediaTaskManager.transitToState(HapticContentSDK.SDKStatus.INITIALIZED);
        }
        Log.e(a, "could not access hapt file url: Inaccessible URL");
        return -2;
    }
}
