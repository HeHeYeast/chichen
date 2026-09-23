package rrrrrr;

import com.immersion.hapticmediasdk.HapticContentSDK;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public /* synthetic */ class crccrr {

    /* renamed from: b041704170417ЗЗЗ, reason: contains not printable characters */
    public static int f116b041704170417 = 1;

    /* renamed from: b042504250425ХХ0425, reason: contains not printable characters */
    public static final /* synthetic */ int[] f117b0425042504250425 = new int[HapticContentSDK.SDKStatus.values().length];

    /* renamed from: bЗ04170417ЗЗЗ, reason: contains not printable characters */
    public static int f118b04170417 = 88;

    /* renamed from: bЗЗЗ0417ЗЗ, reason: contains not printable characters */
    public static int f119b0417 = 2;

    static {
        try {
            f117b0425042504250425[HapticContentSDK.SDKStatus.NOT_INITIALIZED.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            f117b0425042504250425[HapticContentSDK.SDKStatus.INITIALIZED.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        while (true) {
            switch (1) {
                case 0:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
                case 1:
                    try {
                        f117b0425042504250425[HapticContentSDK.SDKStatus.PLAYING.ordinal()] = 3;
                    } catch (NoSuchFieldError e3) {
                    }
                    try {
                        f117b0425042504250425[HapticContentSDK.SDKStatus.PAUSED.ordinal()] = 4;
                        int i = f118b04170417;
                        switch ((i * (f116b041704170417 + i)) % f119b0417) {
                            case 0:
                                break;
                            default:
                                f118b04170417 = 57;
                                f116b041704170417 = 26;
                                break;
                        }
                    } catch (NoSuchFieldError e4) {
                    }
                    try {
                        f117b0425042504250425[HapticContentSDK.SDKStatus.PAUSED_DUE_TO_TIMEOUT.ordinal()] = 5;
                    } catch (NoSuchFieldError e5) {
                    }
                    try {
                        f117b0425042504250425[HapticContentSDK.SDKStatus.PAUSED_DUE_TO_BUFFERING.ordinal()] = 6;
                    } catch (NoSuchFieldError e6) {
                    }
                    try {
                        f117b0425042504250425[HapticContentSDK.SDKStatus.STOPPED.ordinal()] = 7;
                        return;
                    } catch (NoSuchFieldError e7) {
                        return;
                    }
            }
        }
    }
}
