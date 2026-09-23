package rrrrrr;

import com.immersion.hapticmediasdk.controllers.HapticPlaybackThread;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class crcrrr implements Runnable {

    /* renamed from: b042704270427ЧЧЧ, reason: contains not printable characters */
    public static int f120b042704270427 = 2;

    /* renamed from: b04270427ЧЧЧЧ, reason: contains not printable characters */
    public static int f121b04270427 = 0;

    /* renamed from: bЧ0427ЧЧЧЧ, reason: contains not printable characters */
    public static int f122b0427 = 48;

    /* renamed from: bЧЧ0427ЧЧЧ, reason: contains not printable characters */
    public static int f123b0427 = 1;

    /* renamed from: b04440444ф0444фф, reason: contains not printable characters */
    public final /* synthetic */ HapticPlaybackThread f124b044404440444;

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0020. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x0024. Please report as an issue. */
    public crcrrr(HapticPlaybackThread hapticPlaybackThread) {
        this.f124b044404440444 = hapticPlaybackThread;
        if (((f122b0427 + f123b0427) * f122b0427) % m129b04270427() != f121b04270427) {
            f122b0427 = m130b04270427();
            f121b04270427 = m130b04270427();
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
    }

    /* renamed from: b0427Ч0427ЧЧЧ, reason: contains not printable characters */
    public static int m129b04270427() {
        return 2;
    }

    /* renamed from: bЧ04270427ЧЧЧ, reason: contains not printable characters */
    public static int m130b04270427() {
        return 15;
    }

    @Override // java.lang.Runnable
    public void run() throws Exception {
        try {
            HapticPlaybackThread hapticPlaybackThread = this.f124b044404440444;
            if (((f122b0427 + f123b0427) * f122b0427) % f120b042704270427 != f121b04270427) {
                f122b0427 = m130b04270427();
                f121b04270427 = 40;
            }
            try {
                HapticPlaybackThread.m63b041104110411(hapticPlaybackThread);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }
}
