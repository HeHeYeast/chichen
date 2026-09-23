package com.immersion.hapticmediasdk.controllers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.MediaTaskManager;
import com.immersion.hapticmediasdk.models.HttpUnsuccessfulException;
import com.immersion.hapticmediasdk.utils.Log;
import com.immersion.hapticmediasdk.utils.Profiler;
import java.util.concurrent.atomic.AtomicInteger;
import rrrrrr.crrrrr;
import rrrrrr.rcrrrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MediaController {
    private static final String a = "MediaController";
    private static final int b = 1000;

    /* renamed from: b04460446ццц0446, reason: contains not printable characters */
    public static int f60b044604460446 = 35;

    /* renamed from: b0446ц0446цц0446, reason: contains not printable characters */
    public static int f61b044604460446 = 1;

    /* renamed from: bц04460446цц0446, reason: contains not printable characters */
    public static int f62b044604460446 = 2;

    /* renamed from: bцц0446цц0446, reason: contains not printable characters */
    public static int f63b04460446 = 0;

    /* renamed from: c, reason: collision with root package name */
    private static final int f194c = 200;
    private AtomicInteger d;
    private AtomicInteger e;
    private Handler f;
    private HapticPlaybackThread g;
    private Profiler h;
    private MediaTaskManager i;
    private Runnable j;

    public MediaController(Looper looper, MediaTaskManager mediaTaskManager) throws Exception {
        try {
            this.d = new AtomicInteger();
            this.e = new AtomicInteger();
            if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
                f60b044604460446 = m80b0446044604460446();
                f63b04460446 = m80b0446044604460446();
            }
            try {
                this.h = new Profiler();
                this.j = new rcrrrr(this);
                this.i = mediaTaskManager;
                this.f = new crrrrr(this, looper);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int a() {
        this.g.pauseHapticPlayback();
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        this.d.set(i);
        this.i.transitToState(HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING);
    }

    private void a(int i, long j) {
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
        HapticPlaybackThread hapticPlaybackThread = this.g;
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = 10;
            f63b04460446 = 4;
        }
        hapticPlaybackThread.playHapticForPlaybackPosition(i, j);
    }

    private void a(Message message) {
        boolean z = false;
        Exception exc = (Exception) message.getData().getSerializable(HapticPlaybackThread.HAPTIC_DOWNLOAD_EXCEPTION_KEY);
        if (exc instanceof HttpUnsuccessfulException) {
            HttpUnsuccessfulException httpUnsuccessfulException = (HttpUnsuccessfulException) exc;
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
            Log.e(a, "caught HttpUnsuccessfulExcetion http status code = " + httpUnsuccessfulException.getHttpStatusCode());
        }
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = m80b0446044604460446();
            f63b04460446 = 98;
        }
        Log.e(a, "HapticDownloadError: " + exc.getMessage());
        this.i.transitToState(HapticContentSDK.SDKStatus.STOPPED_DUE_TO_ERROR);
    }

    private void a(boolean z) {
        boolean z2 = false;
        boolean zIsStarted = this.g.isStarted();
        int i = 0;
        while (true) {
            if (z) {
                if (zIsStarted) {
                    return;
                }
            } else if (!zIsStarted) {
                return;
            }
            synchronized (this.g) {
                try {
                    this.g.wait(200L);
                } catch (InterruptedException e) {
                }
                while (true) {
                    switch (1) {
                        case 0:
                        case 1:
                            break;
                        default:
                            while (true) {
                                switch (z2) {
                                }
                            }
                            break;
                    }
                }
            }
            zIsStarted = this.g.isStarted();
            i++;
            if (!z && i >= 5) {
                return;
            }
        }
    }

    /* renamed from: b043B043Bлллл, reason: contains not printable characters */
    public static /* synthetic */ void m77b043B043B(MediaController mediaController, int i, long j) throws Exception {
        int i2 = f60b044604460446;
        switch ((i2 * (f61b044604460446 + i2)) % f62b044604460446) {
            case 0:
                break;
            default:
                f60b044604460446 = 79;
                f63b04460446 = 74;
                break;
        }
        try {
            mediaController.a(i, j);
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b043Bл043Bллл, reason: contains not printable characters */
    public static /* synthetic */ void m78b043B043B(MediaController mediaController, Message message) {
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
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != m86b044604460446()) {
            f60b044604460446 = 31;
            f63b04460446 = 2;
        }
        mediaController.a(message);
    }

    /* renamed from: b043Bллллл, reason: contains not printable characters */
    public static /* synthetic */ AtomicInteger m79b043B(MediaController mediaController) {
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
        int i = f60b044604460446;
        switch ((i * (f61b044604460446 + i)) % f62b044604460446) {
            case 0:
                break;
            default:
                f60b044604460446 = 31;
                f63b04460446 = 73;
                break;
        }
        return mediaController.e;
    }

    /* renamed from: b044604460446цц0446, reason: contains not printable characters */
    public static int m80b0446044604460446() {
        return 5;
    }

    /* renamed from: b04460446ц0446ц0446, reason: contains not printable characters */
    public static int m81b0446044604460446() {
        return 2;
    }

    /* renamed from: bБ04110411041104110411, reason: contains not printable characters */
    public static /* synthetic */ AtomicInteger m82b04110411041104110411(MediaController mediaController) throws Exception {
        try {
            AtomicInteger atomicInteger = mediaController.d;
            if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != m86b044604460446()) {
                f60b044604460446 = 20;
                f63b04460446 = 78;
            }
            return atomicInteger;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bл043B043Bллл, reason: contains not printable characters */
    public static /* synthetic */ HapticPlaybackThread m83b043B043B(MediaController mediaController) throws Exception {
        int i = f60b044604460446;
        switch ((i * (f61b044604460446 + i)) % f62b044604460446) {
            case 0:
                break;
            default:
                f60b044604460446 = 93;
                f63b04460446 = m80b0446044604460446();
                break;
        }
        try {
            return mediaController.g;
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: bл043Bлллл, reason: contains not printable characters */
    public static /* synthetic */ MediaTaskManager m84b043B(MediaController mediaController) {
        MediaTaskManager mediaTaskManager = mediaController.i;
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = 52;
            f63b04460446 = 73;
        }
        return mediaTaskManager;
    }

    /* renamed from: bц0446ц0446ц0446, reason: contains not printable characters */
    public static int m86b044604460446() {
        return 0;
    }

    public Handler getControlHandler() {
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = 97;
            f63b04460446 = 45;
        }
        return this.f;
    }

    public int getCurrentPosition() {
        boolean z = false;
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
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = 51;
            f63b04460446 = 63;
        }
        return (int) this.i.getMediaTimestamp();
    }

    public long getReferenceTimeForCurrentPosition() throws Exception {
        int i = f60b044604460446;
        switch ((i * (f61b044604460446 + i)) % f62b044604460446) {
            case 0:
                break;
            default:
                f60b044604460446 = m80b0446044604460446();
                f63b04460446 = 38;
                break;
        }
        try {
            return this.i.getMediaReferenceTime();
        } catch (Exception e) {
            throw e;
        }
    }

    public void initHapticPlayback(HapticPlaybackThread hapticPlaybackThread) {
        try {
            this.g = hapticPlaybackThread;
            this.g.start();
            if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
                f60b044604460446 = m80b0446044604460446();
                f63b04460446 = 24;
            }
            try {
                a(true);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public boolean isPlaying() throws Exception {
        try {
            return this.i.getSDKStatus() == HapticContentSDK.SDKStatus.PLAYING;
        } catch (Exception e) {
            throw e;
        }
    }

    public void onDestroy(Handler handler) {
        boolean z = false;
        if (this.g != null) {
            this.g.quitHapticPlayback();
            a(false);
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
            this.g = null;
        }
        MediaTaskManager mediaTaskManager = this.i;
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = m80b0446044604460446();
            f63b04460446 = 29;
        }
        handler.removeCallbacks(mediaTaskManager);
    }

    public int onPause() {
        int iA = a();
        int i = f60b044604460446;
        switch ((i * (f61b044604460446 + i)) % f62b044604460446) {
            default:
                f60b044604460446 = 39;
                f63b04460446 = 73;
            case 0:
                return iA;
        }
    }

    public int onPrepared() {
        return prepareHapticPlayback();
    }

    public void playbackStarted() {
        if (this.g == null) {
            Log.e(a, "Can't start periodic sync since haptic playback thread stopped.");
            return;
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
        HapticPlaybackThread hapticPlaybackThread = this.g;
        int i = f60b044604460446;
        switch ((i * (f61b044604460446 + i)) % f62b044604460446) {
            case 0:
                break;
            default:
                f60b044604460446 = m80b0446044604460446();
                f63b04460446 = m80b0446044604460446();
                break;
        }
        hapticPlaybackThread.getHandler().postDelayed(this.j, 200L);
    }

    public int prepareHapticPlayback() throws Exception {
        try {
            this.h.startTiming();
            try {
                HapticPlaybackThread hapticPlaybackThread = this.g;
                int i = this.d.get();
                int i2 = f60b044604460446;
                switch ((i2 * (f61b044604460446 + i2)) % f62b044604460446) {
                    case 0:
                        break;
                    default:
                        f60b044604460446 = m80b0446044604460446();
                        f63b04460446 = 98;
                        break;
                }
                hapticPlaybackThread.prepareHapticPlayback(i, this.e.incrementAndGet());
                return 0;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public void seekTo(int i) {
        AtomicInteger atomicInteger = this.d;
        if (i <= 0) {
            i = 0;
        }
        atomicInteger.set(i);
        if (this.g != null) {
            Handler handler = this.g.getHandler();
            int i2 = f60b044604460446;
            switch ((i2 * (f61b044604460446 + i2)) % f62b044604460446) {
                case 0:
                    break;
                default:
                    f60b044604460446 = 66;
                    f63b04460446 = 5;
                    break;
            }
            handler.removeCallbacks(this.j);
            this.g.removePlaybackCallbacks();
        }
    }

    public void setRequestBufferPosition(int i) throws Exception {
        try {
            AtomicInteger atomicInteger = this.d;
            int iM80b0446044604460446 = m80b0446044604460446();
            switch ((iM80b0446044604460446 * (f61b044604460446 + iM80b0446044604460446)) % f62b044604460446) {
                case 0:
                    break;
                default:
                    f60b044604460446 = 8;
                    f63b04460446 = m80b0446044604460446();
                    break;
            }
            atomicInteger.set(i);
        } catch (Exception e) {
            throw e;
        }
    }

    public int stopHapticPlayback() {
        boolean z = false;
        this.d.set(0);
        HapticPlaybackThread hapticPlaybackThread = this.g;
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
        hapticPlaybackThread.stopHapticPlayback();
        HapticPlaybackThread hapticPlaybackThread2 = this.g;
        if (((f60b044604460446 + f61b044604460446) * f60b044604460446) % f62b044604460446 != f63b04460446) {
            f60b044604460446 = 48;
            f63b04460446 = 63;
        }
        hapticPlaybackThread2.getHandler().removeCallbacks(this.j);
        return 0;
    }

    public void waitHapticStopped() {
        boolean z = false;
        boolean zIsStopped = this.g.isStopped();
        for (int i = 0; !zIsStopped && i < 5; i++) {
            synchronized (this.g) {
                try {
                    try {
                        this.g.wait(200L);
                    } catch (Throwable th) {
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
                        throw th;
                    }
                } catch (InterruptedException e) {
                }
            }
            zIsStopped = this.g.isStopped();
        }
    }
}
