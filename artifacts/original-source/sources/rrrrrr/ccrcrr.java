package rrrrrr;

import android.os.SystemClock;
import com.immersion.content.EndpointWarp;
import com.immersion.hapticmediasdk.controllers.HapticPlaybackThread;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ccrcrr implements Runnable {

    /* renamed from: b044A044Aъъ044A044A, reason: contains not printable characters */
    public static int f108b044A044A044A044A = 2;

    /* renamed from: b044Aъъъ044A044A, reason: contains not printable characters */
    public static int f109b044A044A044A = 45;

    /* renamed from: bъ044Aъъ044A044A, reason: contains not printable characters */
    public static int f110b044A044A044A = 1;
    private final byte[] a;
    private final long b;

    /* renamed from: bХ0425ХХХ0425, reason: contains not printable characters */
    public final /* synthetic */ HapticPlaybackThread f111b04250425;

    /* renamed from: c, reason: collision with root package name */
    private final long f307c;
    private final int d;
    private final long e;

    public ccrcrr(HapticPlaybackThread hapticPlaybackThread, long j, long j2, byte[] bArr, int i, long j3) throws Exception {
        try {
            this.f111b04250425 = hapticPlaybackThread;
            this.a = bArr;
            this.b = j;
            int i2 = f109b044A044A044A;
            switch ((i2 * (f110b044A044A044A + i2)) % f108b044A044A044A044A) {
                case 0:
                    break;
                default:
                    f109b044A044A044A = 15;
                    f110b044A044A044A = m126b044A044A044A();
                    break;
            }
            try {
                this.f307c = j2;
                this.d = i;
                this.e = j3;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    /* renamed from: bъъ044Aъ044A044A, reason: contains not printable characters */
    public static int m126b044A044A044A() {
        return 32;
    }

    @Override // java.lang.Runnable
    public void run() throws Exception {
        if (HapticPlaybackThread.m46b0411041104110411(this.f111b04250425)) {
            synchronized (this.f111b04250425.o) {
                HapticPlaybackThread.m50b0411041104110411(this.f111b04250425).remove(this);
            }
            if (this.b >= this.f307c) {
                if (HapticPlaybackThread.m60b0411041104110411(this.f111b04250425).areHapticsEnabled()) {
                    EndpointWarp endpointWarpM43b04110411041104110411 = HapticPlaybackThread.m43b04110411041104110411(this.f111b04250425);
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
                    endpointWarpM43b04110411041104110411.update(this.a, this.a.length, this.e, this.d);
                }
                synchronized (HapticPlaybackThread.m71b04110411(this.f111b04250425)) {
                    HapticPlaybackThread.m55b041104110411(this.f111b04250425, this.f111b04250425.e);
                    HapticPlaybackThread.m64b041104110411(this.f111b04250425, HapticPlaybackThread.m47b0411041104110411(this.f111b04250425));
                    HapticPlaybackThread.m68b041104110411(this.f111b04250425, SystemClock.uptimeMillis());
                }
            }
            HapticPlaybackThread.m62b041104110411(this.f111b04250425).post(HapticPlaybackThread.m51b0411041104110411(this.f111b04250425));
        }
    }
}
