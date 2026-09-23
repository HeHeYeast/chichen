package rrrrrr;

import com.immersion.hapticmediasdk.controllers.HapticPlaybackThread;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class rrcrrr implements Runnable {

    /* renamed from: b0415Е0415Е04150415, reason: contains not printable characters */
    public static int f147b0415041504150415 = 1;

    /* renamed from: bЕ04150415Е04150415, reason: contains not printable characters */
    public static int f148b0415041504150415 = 2;

    /* renamed from: bЕЕ0415Е04150415, reason: contains not printable characters */
    public static int f149b041504150415 = 2;

    /* renamed from: bЕЕЕ041504150415, reason: contains not printable characters */
    public static int f150b041504150415;

    /* renamed from: bЗ04170417041704170417, reason: contains not printable characters */
    public final /* synthetic */ HapticPlaybackThread f151b04170417041704170417;

    public rrcrrr(HapticPlaybackThread hapticPlaybackThread) {
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
        int i = f149b041504150415;
        switch ((i * (f147b0415041504150415 + i)) % f148b0415041504150415) {
            case 0:
                break;
            default:
                f149b041504150415 = m139b04150415041504150415();
                f147b0415041504150415 = m139b04150415041504150415();
                break;
        }
        this.f151b04170417041704170417 = hapticPlaybackThread;
    }

    /* renamed from: b041504150415Е04150415, reason: contains not printable characters */
    public static int m139b04150415041504150415() {
        return 54;
    }

    @Override // java.lang.Runnable
    public void run() {
        HapticPlaybackThread.m59b0411041104110411(this.f151b04170417041704170417);
    }
}
