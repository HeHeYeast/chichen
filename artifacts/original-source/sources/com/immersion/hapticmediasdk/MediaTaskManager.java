package com.immersion.hapticmediasdk;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.controllers.HapticPlaybackThread;
import com.immersion.hapticmediasdk.controllers.MediaController;
import com.immersion.hapticmediasdk.utils.Log;
import com.immersion.hapticmediasdk.utils.RuntimeInfo;
import rrrrrr.crccrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MediaTaskManager implements Runnable {
    private static final String a = "MediaTaskManager";

    /* renamed from: b04150415ЕЕЕЕ, reason: contains not printable characters */
    public static int f37b04150415 = 2;

    /* renamed from: bЕ04150415ЕЕЕ, reason: contains not printable characters */
    public static int f38b04150415 = 0;

    /* renamed from: bЕ0415ЕЕЕЕ, reason: contains not printable characters */
    public static int f39b0415 = 1;

    /* renamed from: bС04210421042104210421, reason: contains not printable characters */
    public static int f40b04210421042104210421 = 37;
    private final Object b;

    /* renamed from: c, reason: collision with root package name */
    private final Object f191c;
    private long d;
    private long e;
    private Handler f;
    private volatile HapticContentSDK.SDKStatus g;
    private MediaController h;
    private String i;
    private boolean j;
    private Context k;
    private RuntimeInfo l;

    public MediaTaskManager(Handler handler, Context context, RuntimeInfo runtimeInfo) throws Exception {
        if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != m33b0415()) {
            f40b04210421042104210421 = 47;
            f39b0415 = m34b0415();
        }
        try {
            this.b = new Object();
            try {
                this.f191c = new Object();
                this.g = HapticContentSDK.SDKStatus.NOT_INITIALIZED;
                this.f = handler;
                this.k = context;
                this.l = runtimeInfo;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int a() throws Exception {
        try {
            this.f.removeCallbacks(this);
            if (this.h != null && d() != 0) {
                Log.e(a, "Could not dispose haptics, reset anyway.");
            }
            try {
                this.i = null;
                this.d = 0L;
                this.g = HapticContentSDK.SDKStatus.NOT_INITIALIZED;
                int i = f40b04210421042104210421;
                switch ((i * (m32b04150415() + i)) % f37b04150415) {
                    case 0:
                        return 0;
                    default:
                        f40b04210421042104210421 = m34b0415();
                        f39b0415 = 55;
                        return 0;
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int a(HapticContentSDK.SDKStatus sDKStatus) throws Exception {
        int i = f40b04210421042104210421;
        switch ((i * (f39b0415 + i)) % f37b04150415) {
            case 0:
                break;
            default:
                f40b04210421042104210421 = 19;
                f39b0415 = m34b0415();
                break;
        }
        try {
            try {
                this.f.removeCallbacks(this);
                this.g = sDKStatus;
                if (this.i == null) {
                    return -4;
                }
                this.h = new MediaController(this.f.getLooper(), this);
                Handler controlHandler = this.h.getControlHandler();
                this.h.initHapticPlayback(new HapticPlaybackThread(this.k, this.i, controlHandler, this.j, this.l));
                return 0;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int b() {
        this.f.removeCallbacks(this);
        int iOnPrepared = this.h.onPrepared();
        if (iOnPrepared == 0) {
            this.g = HapticContentSDK.SDKStatus.PLAYING;
            Handler handler = this.f;
            int i = f40b04210421042104210421;
            switch ((i * (f39b0415 + i)) % f37b04150415) {
                case 0:
                    break;
                default:
                    f40b04210421042104210421 = m34b0415();
                    f38b04150415 = 68;
                    break;
            }
            handler.postDelayed(this, 1500L);
        }
        return iOnPrepared;
    }

    /* renamed from: b0415Е0415ЕЕЕ, reason: contains not printable characters */
    public static int m32b04150415() {
        return 1;
    }

    /* renamed from: b0415ЕЕЕЕЕ, reason: contains not printable characters */
    public static int m33b0415() {
        return 0;
    }

    /* renamed from: bЕЕ0415ЕЕЕ, reason: contains not printable characters */
    public static int m34b0415() {
        return 54;
    }

    private int c() throws Exception {
        try {
            this.f.removeCallbacks(this);
            this.d = 0L;
            if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != f38b04150415) {
                f40b04210421042104210421 = m34b0415();
                f38b04150415 = m34b0415();
            }
            try {
                int iStopHapticPlayback = this.h.stopHapticPlayback();
                if (iStopHapticPlayback == 0) {
                    this.g = HapticContentSDK.SDKStatus.STOPPED;
                }
                return iStopHapticPlayback;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int d() throws Exception {
        int iC = c();
        if (iC == 0) {
            while (true) {
                boolean z = false;
                switch (z) {
                    case false:
                        break;
                    case true:
                    default:
                        while (true) {
                            switch (1) {
                            }
                        }
                        break;
                }
            }
            this.h.onDestroy(this.f);
            if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != f38b04150415) {
                f40b04210421042104210421 = 80;
                f38b04150415 = 44;
            }
            this.h = null;
        }
        return iC;
    }

    private int e() throws Exception {
        int i = 2;
        try {
            this.f.removeCallbacks(this);
            try {
                int iOnPause = this.h.onPause();
                if (iOnPause == 0) {
                    while (true) {
                        try {
                            i /= 0;
                        } catch (Exception e) {
                            f40b04210421042104210421 = m34b0415();
                            this.g = HapticContentSDK.SDKStatus.PAUSED;
                        }
                    }
                }
                return iOnPause;
            } catch (Exception e2) {
                throw e2;
            }
        } catch (Exception e3) {
            throw e3;
        }
    }

    private int f() {
        boolean z = false;
        this.f.removeCallbacks(this);
        if (!this.f.postDelayed(this, 1500L)) {
            return -1;
        }
        while (true) {
            switch (z) {
                case false:
                    break;
                case true:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        if (((m34b0415() + f39b0415) * m34b0415()) % f37b04150415 == f38b04150415) {
            return 0;
        }
        f40b04210421042104210421 = 70;
        f38b04150415 = 50;
        return 0;
    }

    private int g() throws Exception {
        try {
            int iOnPause = this.h.onPause();
            if (iOnPause == 0) {
                if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != m33b0415()) {
                    f40b04210421042104210421 = 64;
                    f38b04150415 = 32;
                }
                this.g = HapticContentSDK.SDKStatus.PAUSED_DUE_TO_TIMEOUT;
            }
            return iOnPause;
        } catch (Exception e) {
            throw e;
        }
    }

    private int h() throws Exception {
        try {
            int iOnPause = this.h.onPause();
            if (iOnPause == 0) {
                this.g = HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING;
            }
            if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != f38b04150415) {
                f40b04210421042104210421 = 29;
                f38b04150415 = m34b0415();
            }
            return iOnPause;
        } catch (Exception e) {
            throw e;
        }
    }

    private int i() {
        if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != f38b04150415) {
            f40b04210421042104210421 = 78;
            f38b04150415 = 14;
        }
        int iB = b();
        if (iB == 0) {
            iB = f();
        }
        while (true) {
            boolean z = false;
            switch (z) {
                case false:
                    break;
                case true:
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        return iB;
    }

    public int SeekTo(int i) {
        int iM34b0415 = m34b0415();
        switch ((iM34b0415 * (m32b04150415() + iM34b0415)) % f37b04150415) {
            case 0:
                break;
            default:
                f40b04210421042104210421 = 10;
                f39b0415 = 6;
                break;
        }
        setMediaTimestamp(i);
        this.h.seekTo(i);
        if (getSDKStatus() != HapticContentSDK.SDKStatus.PLAYING) {
            return 0;
        }
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        return this.h.prepareHapticPlayback();
    }

    public long getMediaReferenceTime() {
        long j;
        synchronized (this.f191c) {
            try {
                j = this.e;
            } catch (Throwable th) {
                while (true) {
                    boolean z = false;
                    switch (z) {
                        case false:
                            break;
                        case true:
                        default:
                            while (true) {
                                switch (1) {
                                }
                            }
                            break;
                    }
                }
                throw th;
            }
        }
        return j;
    }

    public long getMediaTimestamp() {
        long j;
        synchronized (this.f191c) {
            try {
                j = this.d;
            } catch (Throwable th) {
                while (true) {
                    switch (1) {
                        case 0:
                            break;
                        case 1:
                            break;
                        default:
                            while (true) {
                                boolean z = false;
                                switch (z) {
                                }
                            }
                    }
                }
                throw th;
            }
        }
        return j;
    }

    public HapticContentSDK.SDKStatus getSDKStatus() {
        HapticContentSDK.SDKStatus sDKStatus;
        synchronized (this.b) {
            sDKStatus = this.g;
        }
        return sDKStatus;
    }

    @Override // java.lang.Runnable
    public void run() {
        System.currentTimeMillis();
        if (((f40b04210421042104210421 + f39b0415) * f40b04210421042104210421) % f37b04150415 != f38b04150415) {
            f40b04210421042104210421 = 91;
            f38b04150415 = 30;
        }
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        switch (1) {
                        }
                    }
                    break;
            }
        }
        transitToState(HapticContentSDK.SDKStatus.PAUSED_DUE_TO_TIMEOUT);
    }

    public void setHapticsUrl(String str, boolean z) {
        boolean z2 = false;
        synchronized (this.b) {
            try {
                this.i = str;
                this.j = z;
            } catch (Throwable th) {
                while (true) {
                    switch (z2) {
                        case false:
                            break;
                        case true:
                            break;
                        default:
                            while (true) {
                                switch (z2) {
                                }
                            }
                    }
                }
                throw th;
            }
        }
    }

    public void setMediaReferenceTime() {
        synchronized (this.f191c) {
            HapticContentSDK.SDKStatus sDKStatus = this.g;
            while (true) {
                switch (1) {
                    case 0:
                    case 1:
                        break;
                    default:
                        while (true) {
                            boolean z = false;
                            switch (z) {
                            }
                        }
                        break;
                }
            }
            if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED) {
                this.h.waitHapticStopped();
            }
            this.e = SystemClock.uptimeMillis();
        }
    }

    public void setMediaTimestamp(long j) {
        synchronized (this.f191c) {
            if (this.g == HapticContentSDK.SDKStatus.STOPPED) {
                this.h.waitHapticStopped();
            }
            this.e = SystemClock.uptimeMillis();
            while (true) {
                switch (1) {
                    case 0:
                        break;
                    case 1:
                        break;
                    default:
                        while (true) {
                            switch (1) {
                            }
                        }
                        break;
                }
            }
            this.d = j;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int transitToState(HapticContentSDK.SDKStatus sDKStatus) {
        boolean z = false;
        int i = -1;
        synchronized (this.b) {
            if (sDKStatus != HapticContentSDK.SDKStatus.NOT_INITIALIZED) {
                switch (crccrr.f117b0425042504250425[this.g.ordinal()]) {
                    case 1:
                        if (sDKStatus == HapticContentSDK.SDKStatus.INITIALIZED) {
                            i = a(sDKStatus);
                            break;
                        }
                        break;
                    case 2:
                        if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                            if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED) {
                                if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
                                    i = c();
                                    this.g = HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR;
                                    break;
                                }
                            } else {
                                i = c();
                                break;
                            }
                        } else {
                            i = i();
                            break;
                        }
                        break;
                    case 3:
                        while (true) {
                            switch (1) {
                                case 0:
                                case 1:
                                    break;
                                default:
                                    while (true) {
                                        switch (z) {
                                        }
                                    }
                                    break;
                            }
                        }
                        if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                            if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED) {
                                if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED_DUE_TO_TIMEOUT) {
                                    if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING) {
                                        if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED) {
                                            if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
                                                i = c();
                                                this.g = HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR;
                                                break;
                                            }
                                        } else {
                                            i = c();
                                            break;
                                        }
                                    } else {
                                        i = h();
                                        Log.w(a, "Haptic playback is paused due to slow data buffering...");
                                        break;
                                    }
                                } else {
                                    Log.w(a, "Haptic playback is paused due to update time-out. Call update() to resume playback");
                                    i = g();
                                    break;
                                }
                            } else {
                                i = e();
                                break;
                            }
                        } else {
                            i = f();
                            break;
                        }
                        break;
                    case 4:
                        if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                            if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED) {
                                if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED) {
                                    if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
                                        i = c();
                                        this.g = HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR;
                                        break;
                                    }
                                } else {
                                    i = c();
                                    break;
                                }
                            } else {
                                i = 0;
                                break;
                            }
                        } else {
                            this.h.setRequestBufferPosition((int) this.d);
                            i = i();
                            break;
                        }
                        break;
                    case 5:
                        if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED_DUE_TO_TIMEOUT) {
                            if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                                if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED) {
                                    if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED) {
                                        if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
                                            i = c();
                                            this.g = HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR;
                                            break;
                                        }
                                    } else {
                                        i = c();
                                        break;
                                    }
                                } else {
                                    this.g = HapticContentSDK.SDKStatus.PAUSED;
                                    i = 0;
                                    break;
                                }
                            } else {
                                this.h.setRequestBufferPosition((int) this.d);
                                i = i();
                                break;
                            }
                        } else {
                            i = 0;
                            break;
                        }
                        break;
                    case 6:
                        if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING) {
                            if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                                if (sDKStatus != HapticContentSDK.SDKStatus.PAUSED) {
                                    if (sDKStatus != HapticContentSDK.SDKStatus.STOPPED) {
                                        if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR) {
                                            i = c();
                                            this.g = HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR;
                                            break;
                                        }
                                    } else {
                                        i = c();
                                        break;
                                    }
                                } else {
                                    this.g = HapticContentSDK.SDKStatus.PAUSED;
                                    i = 0;
                                    break;
                                }
                            } else {
                                this.h.setRequestBufferPosition((int) this.d);
                                i = i();
                                break;
                            }
                        } else {
                            i = 0;
                            break;
                        }
                        break;
                    case 7:
                        if (sDKStatus != HapticContentSDK.SDKStatus.PLAYING) {
                            if (sDKStatus == HapticContentSDK.SDKStatus.STOPPED) {
                                i = 0;
                                break;
                            }
                        } else {
                            i = i();
                            break;
                        }
                        break;
                }
            } else {
                i = a();
            }
        }
        return i;
    }
}
