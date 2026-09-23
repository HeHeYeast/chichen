package com.immersion.hapticmediasdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.immersion.hapticmediasdk.utils.Log;
import com.immersion.hapticmediasdk.utils.RuntimeInfo;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class HapticContentSDK {
    public static final int INACCESSIBLE_URL = -2;
    public static final int INVALID = -1;
    public static final int MALFORMED_URL = -4;
    public static final int PERMISSION_DENIED = -3;
    public static final int SDKMODE_MEDIAPLAYBACK = 0;
    public static final int SUCCESS = 0;
    public static final int UNSUPPORTED_PROTOCOL = -5;
    private static final String a = "HapticContentSDK";

    /* renamed from: b044404440444ф04440444, reason: contains not printable characters */
    public static final int f17b04440444044404440444 = 10000;

    /* renamed from: b044604460446ццц, reason: contains not printable characters */
    public static int f18b044604460446 = 25;

    /* renamed from: b0446цц0446цц, reason: contains not printable characters */
    public static int f19b04460446 = 1;

    /* renamed from: bф04440444ф04440444, reason: contains not printable characters */
    public static final int f20b0444044404440444 = 1500;

    /* renamed from: bц0446ц0446цц, reason: contains not printable characters */
    public static int f21b04460446 = 2;

    /* renamed from: bццц0446цц, reason: contains not printable characters */
    public static int f22b0446;
    private HandlerThread b;

    /* renamed from: c, reason: collision with root package name */
    private Handler f190c;
    private Context d;
    private RuntimeInfo e;
    public MediaTaskManager mMediaTaskManager;
    public SDKStatus mSDKStatus = SDKStatus.NOT_INITIALIZED;
    public boolean mDisposed = false;

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    public static final class SDKStatus {
        public static final SDKStatus DISPOSED;
        public static final SDKStatus PAUSED_DUE_TO_BUFFERING;
        private static final /* synthetic */ SDKStatus[] a;

        /* renamed from: b04170417ЗЗЗЗ, reason: contains not printable characters */
        public static int f23b04170417 = 0;

        /* renamed from: b0417ЗЗЗЗЗ, reason: contains not printable characters */
        public static int f24b0417 = 2;

        /* renamed from: b044Aъ044A044A044A044A, reason: contains not printable characters */
        public static int f25b044A044A044A044A044A = 6;

        /* renamed from: bъ044A044A044A044A044A, reason: contains not printable characters */
        public static int f26b044A044A044A044A044A = 1;
        public static final SDKStatus NOT_INITIALIZED = new SDKStatus("NOT_INITIALIZED", 0);
        public static final SDKStatus INITIALIZED = new SDKStatus("INITIALIZED", 1);
        public static final SDKStatus PLAYING = new SDKStatus("PLAYING", 2);
        public static final SDKStatus STOPPED = new SDKStatus("STOPPED", 3);
        public static final SDKStatus STOPPED_DUE_TO_ERROR = new SDKStatus("STOPPED_DUE_TO_ERROR", 4);
        public static final SDKStatus PAUSED = new SDKStatus("PAUSED", 5);
        public static final SDKStatus PAUSED_DUE_TO_TIMEOUT = new SDKStatus("PAUSED_DUE_TO_TIMEOUT", 6);

        static {
            boolean z = false;
            SDKStatus sDKStatus = new SDKStatus("PAUSED_DUE_TO_BUFFERING", 7);
            while (true) {
                switch (1) {
                    case 0:
                        break;
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
            PAUSED_DUE_TO_BUFFERING = sDKStatus;
            if (((m24b0417() + f26b044A044A044A044A044A) * m24b0417()) % f24b0417 != f23b04170417) {
                f25b044A044A044A044A044A = m24b0417();
                f23b04170417 = m24b0417();
            }
            DISPOSED = new SDKStatus("DISPOSED", 8);
            a = new SDKStatus[]{NOT_INITIALIZED, INITIALIZED, PLAYING, STOPPED, STOPPED_DUE_TO_ERROR, PAUSED, PAUSED_DUE_TO_TIMEOUT, PAUSED_DUE_TO_BUFFERING, DISPOSED};
        }

        private SDKStatus(String str, int i) {
        }

        /* renamed from: b0417З0417ЗЗЗ, reason: contains not printable characters */
        public static int m23b04170417() {
            return 2;
        }

        /* renamed from: bЗ0417ЗЗЗЗ, reason: contains not printable characters */
        public static int m24b0417() {
            return 37;
        }

        /* renamed from: bЗЗ0417ЗЗЗ, reason: contains not printable characters */
        public static int m25b0417() {
            return 1;
        }

        public static SDKStatus valueOf(String str) throws Exception {
            try {
                Enum enumValueOf = Enum.valueOf(SDKStatus.class, str);
                if (((f25b044A044A044A044A044A + m25b0417()) * f25b044A044A044A044A044A) % f24b0417 != f23b04170417) {
                    f25b044A044A044A044A044A = 22;
                    f23b04170417 = 7;
                }
                try {
                    return (SDKStatus) enumValueOf;
                } catch (Exception e) {
                    throw e;
                }
            } catch (Exception e2) {
                throw e2;
            }
        }

        public static SDKStatus valueOfCaseInsensitive(String str) throws Exception {
            try {
                for (SDKStatus sDKStatus : values()) {
                    if (((f25b044A044A044A044A044A + f26b044A044A044A044A044A) * f25b044A044A044A044A044A) % m23b04170417() != f23b04170417) {
                        f25b044A044A044A044A044A = 55;
                        f23b04170417 = m24b0417();
                    }
                    if (str.equalsIgnoreCase(sDKStatus.name())) {
                        return sDKStatus;
                    }
                }
                return null;
            } catch (Exception e) {
                throw e;
            }
        }

        public static SDKStatus[] values() throws Exception {
            int i = f25b044A044A044A044A044A;
            switch ((i * (f26b044A044A044A044A044A + i)) % f24b0417) {
                case 0:
                    break;
                default:
                    f25b044A044A044A044A044A = m24b0417();
                    f26b044A044A044A044A044A = 72;
                    break;
            }
            try {
                try {
                    return (SDKStatus[]) a.clone();
                } catch (Exception e) {
                    throw e;
                }
            } catch (Exception e2) {
                throw e2;
            }
        }
    }

    public HapticContentSDK(int i, Context context) {
        this.d = context;
        if (((f18b044604460446 + f19b04460446) * f18b044604460446) % f21b04460446 != f22b0446) {
            f18b044604460446 = 24;
            f22b0446 = m20b044604460446();
        }
        this.e = new RuntimeInfo();
    }

    /* renamed from: b04460446ц0446цц, reason: contains not printable characters */
    public static int m20b044604460446() {
        return 96;
    }

    /* renamed from: bцц04460446цц, reason: contains not printable characters */
    public static int m21b04460446() {
        return 2;
    }

    /* renamed from: bБ04110411Б04110411, reason: contains not printable characters */
    public int m22b0411041104110411() throws Exception {
        int i;
        try {
            try {
                if (this.d.getPackageManager().checkPermission("android.permission.VIBRATE", this.d.getPackageName()) == 0) {
                    this.b = new HandlerThread("SDK Monitor");
                    this.b.start();
                    this.f190c = new Handler(this.b.getLooper());
                    Handler handler = this.f190c;
                    int i2 = f18b044604460446;
                    switch ((i2 * (f19b04460446 + i2)) % f21b04460446) {
                        case 0:
                            break;
                        default:
                            f18b044604460446 = m20b044604460446();
                            f22b0446 = 93;
                            break;
                    }
                    this.mMediaTaskManager = new MediaTaskManager(handler, this.d, this.e);
                    i = 0;
                } else {
                    Log.e(a, "Failed to create a Haptic Content SDK instance.Vibrate permission denied.");
                    i = -3;
                }
                return i;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public final void dispose() {
        if (getSDKStatus() != SDKStatus.DISPOSED) {
            this.mMediaTaskManager.transitToState(SDKStatus.NOT_INITIALIZED);
            this.b.quit();
            this.b = null;
            this.mMediaTaskManager = null;
            this.mDisposed = true;
        }
    }

    public void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    public final SDKStatus getSDKStatus() {
        return this.mDisposed ? SDKStatus.DISPOSED : this.mMediaTaskManager.getSDKStatus();
    }

    public final String getVersion() {
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                f18b044604460446 = 88;
                return HapticMediaSDKVersion.Version;
            }
        }
    }

    public final int mute() throws Exception {
        int i = f18b044604460446;
        switch ((i * (f19b04460446 + i)) % f21b04460446) {
            case 0:
                break;
            default:
                f18b044604460446 = 16;
                f22b0446 = m20b044604460446();
                break;
        }
        try {
            if (getSDKStatus() == SDKStatus.DISPOSED) {
                return -1;
            }
            try {
                this.e.mute();
                return 0;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public abstract int openHaptics(String str);

    public final int pause() throws Exception {
        String str = null;
        try {
            SDKStatus sDKStatus = getSDKStatus();
            if (sDKStatus != SDKStatus.DISPOSED) {
                if (sDKStatus != SDKStatus.STOPPED_DUE_TO_ERROR) {
                    try {
                        return this.mMediaTaskManager.transitToState(SDKStatus.PAUSED);
                    } catch (Exception e) {
                        throw e;
                    }
                }
                while (true) {
                    try {
                        str.length();
                    } catch (Exception e2) {
                        f18b044604460446 = 21;
                    }
                }
            }
            return -1;
        } catch (Exception e3) {
            throw e3;
        }
    }

    public final int play() throws Exception {
        try {
            SDKStatus sDKStatus = getSDKStatus();
            if (sDKStatus != SDKStatus.INITIALIZED && sDKStatus != SDKStatus.STOPPED) {
                return -1;
            }
            this.mMediaTaskManager.setMediaTimestamp(0L);
            MediaTaskManager mediaTaskManager = this.mMediaTaskManager;
            SDKStatus sDKStatus2 = SDKStatus.PLAYING;
            if (((m20b044604460446() + f19b04460446) * m20b044604460446()) % m21b04460446() != f22b0446) {
                f18b044604460446 = m20b044604460446();
                f22b0446 = m20b044604460446();
            }
            try {
                return mediaTaskManager.transitToState(sDKStatus2);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public final int resume() {
        SDKStatus sDKStatus = getSDKStatus();
        if (sDKStatus != SDKStatus.PAUSED && sDKStatus != SDKStatus.PLAYING) {
            if (sDKStatus != SDKStatus.STOPPED) {
                return -1;
            }
            while (true) {
                try {
                    int[] iArr = new int[-1];
                } catch (Exception e) {
                    f18b044604460446 = 99;
                }
            }
        }
        this.mMediaTaskManager.setMediaReferenceTime();
        return this.mMediaTaskManager.transitToState(SDKStatus.PLAYING);
    }

    public final int seek(int i) {
        SDKStatus sDKStatus = getSDKStatus();
        int i2 = f18b044604460446;
        switch ((i2 * (f19b04460446 + i2)) % f21b04460446) {
            case 0:
                break;
            default:
                f18b044604460446 = 56;
                f22b0446 = m20b044604460446();
                break;
        }
        SDKStatus sDKStatus2 = SDKStatus.DISPOSED;
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
        if (sDKStatus == sDKStatus2 || sDKStatus == SDKStatus.NOT_INITIALIZED || sDKStatus == SDKStatus.STOPPED_DUE_TO_ERROR) {
            return -1;
        }
        return this.mMediaTaskManager.SeekTo(i);
    }

    public final int stop() {
        SDKStatus sDKStatus = getSDKStatus();
        if (sDKStatus != SDKStatus.DISPOSED && sDKStatus != SDKStatus.NOT_INITIALIZED) {
            int iTransitToState = this.mMediaTaskManager.transitToState(SDKStatus.STOPPED);
            while (true) {
                switch (1) {
                    case 0:
                        break;
                    case 1:
                        return iTransitToState;
                    default:
                        while (true) {
                            switch (1) {
                                case 1:
                                    return iTransitToState;
                            }
                        }
                        break;
                }
            }
        } else {
            return -1;
        }
    }

    public final int unmute() throws Exception {
        try {
            if (getSDKStatus() == SDKStatus.DISPOSED) {
                return -1;
            }
            this.e.unmute();
            return 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public final int update(long j) {
        SDKStatus sDKStatus = getSDKStatus();
        int i = f18b044604460446;
        switch ((i * (f19b04460446 + i)) % f21b04460446) {
            case 0:
                break;
            default:
                f18b044604460446 = m20b044604460446();
                f22b0446 = 98;
                break;
        }
        if (sDKStatus == SDKStatus.PLAYING || sDKStatus == SDKStatus.PAUSED_DUE_TO_TIMEOUT) {
            this.mMediaTaskManager.setMediaTimestamp(j);
            return this.mMediaTaskManager.transitToState(SDKStatus.PLAYING);
        }
        if (sDKStatus != SDKStatus.PAUSED && sDKStatus != SDKStatus.PAUSED_DUE_TO_BUFFERING) {
            return -1;
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
        this.mMediaTaskManager.setMediaTimestamp(j);
        return 0;
    }
}
