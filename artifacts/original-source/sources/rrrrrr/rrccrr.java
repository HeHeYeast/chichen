package rrrrrr;

import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.MediaPlaybackSDK;
import com.immersion.hapticmediasdk.utils.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class rrccrr implements Runnable {
    private static final String a = "ValidateURL";

    /* renamed from: b044A044Aъ044A044A044A, reason: contains not printable characters */
    public static int f143b044A044A044A044A044A = 2;

    /* renamed from: b044Aъъ044A044A044A, reason: contains not printable characters */
    public static int f144b044A044A044A044A = 24;

    /* renamed from: bъ044Aъ044A044A044A, reason: contains not printable characters */
    public static int f145b044A044A044A044A = 1;
    private URL b;

    /* renamed from: b0425Х0425ХХ0425, reason: contains not printable characters */
    public final /* synthetic */ MediaPlaybackSDK f146b042504250425;

    public rrccrr(MediaPlaybackSDK mediaPlaybackSDK, String str) throws MalformedURLException {
        String str2 = null;
        this.f146b042504250425 = mediaPlaybackSDK;
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                while (true) {
                    try {
                        str2.length();
                    } catch (Exception e2) {
                        while (true) {
                            try {
                                str2.length();
                            } catch (Exception e3) {
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
                                this.b = new URL(str);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void a(int i) {
        synchronized (this) {
            MediaPlaybackSDK.m30b043B043B(this.f146b042504250425, i);
            notifyAll();
            while (true) {
                boolean z = false;
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
        }
    }

    /* renamed from: bъъ044A044A044A044A, reason: contains not printable characters */
    public static int m138b044A044A044A044A() {
        return 6;
    }

    @Override // java.lang.Runnable
    public void run() throws Exception {
        try {
            try {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) this.b.openConnection();
                    httpURLConnection.setConnectTimeout(HapticContentSDK.f17b04440444044404440444);
                    httpURLConnection.setReadTimeout(HapticContentSDK.f17b04440444044404440444);
                    httpURLConnection.setUseCaches(false);
                    int i = f144b044A044A044A044A;
                    switch ((i * (f145b044A044A044A044A + i)) % f143b044A044A044A044A044A) {
                        case 0:
                            break;
                        default:
                            f144b044A044A044A044A = 0;
                            f145b044A044A044A044A = m138b044A044A044A044A();
                            break;
                    }
                    httpURLConnection.setRequestMethod("HEAD");
                    try {
                        a(httpURLConnection.getResponseCode());
                    } catch (Exception e) {
                        throw e;
                    }
                } catch (IOException e2) {
                    Log.e(a, e2.getMessage());
                }
            } finally {
                a(500);
            }
        } catch (Exception e3) {
            throw e3;
        }
    }
}
