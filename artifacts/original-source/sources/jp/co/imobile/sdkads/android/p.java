package jp.co.imobile.sdkads.android;

import android.content.Context;
import java.util.Map;
import java.util.TimerTask;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class p extends TimerTask {
    private static /* synthetic */ int[] b;
    final /* synthetic */ ImobileSdkAd a;

    p(ImobileSdkAd imobileSdkAd) {
        this.a = imobileSdkAd;
    }

    private static /* synthetic */ int[] a() {
        int[] iArr = b;
        if (iArr == null) {
            iArr = new int[am.a().length];
            try {
                iArr[am.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[am.LODING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[am.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[am.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[am.START.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[am.STOP.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            b = iArr;
        }
        return iArr;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public final void run() {
        x.a(null);
        for (Map.Entry entry : this.a.b.entrySet()) {
            new StringBuilder("Spot status:").append(((z) entry.getValue()).a()).append(" on spot:").append(((z) entry.getValue()).f306c);
            x.a(null);
            switch (a()[((z) entry.getValue()).a().ordinal()]) {
                case 3:
                    z zVar = (z) entry.getValue();
                    Context unused = this.a.g;
                    zVar.i();
                    break;
                case 6:
                    ((z) entry.getValue()).h();
                    break;
            }
        }
    }
}
