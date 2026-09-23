package com.immersion.content;

import android.content.Context;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class EndpointWarp {
    private static final String b = "EndpointWarp";

    /* renamed from: b041504150415Е0415Е, reason: contains not printable characters */
    public static int f2b0415041504150415 = 39;

    /* renamed from: b0415Е041504150415Е, reason: contains not printable characters */
    public static int f3b0415041504150415 = 1;

    /* renamed from: bЕ0415Е04150415Е, reason: contains not printable characters */
    public static int f4b041504150415 = 2;

    /* renamed from: bЕЕЕ04150415Е, reason: contains not printable characters */
    public static int f5b04150415;
    long a;

    public EndpointWarp(Context context, byte b2, byte b3, byte b4, byte b5, int i, short s, byte b6, byte[] bArr, byte b7) {
        if (((f2b0415041504150415 + m13b041504150415()) * f2b0415041504150415) % f4b041504150415 != f5b04150415) {
            f2b0415041504150415 = 10;
            f5b04150415 = m12b0415041504150415();
        }
        this.a = create(context, b2, b3, b4, b5, i, s, b6, bArr, b7);
    }

    public EndpointWarp(Context context, byte[] bArr, int i) {
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
        if (((f2b0415041504150415 + f3b0415041504150415) * f2b0415041504150415) % f4b041504150415 != m14b041504150415()) {
            f2b0415041504150415 = m12b0415041504150415();
            f5b04150415 = m12b0415041504150415();
        }
        this.a = createWarp(context, bArr, i);
    }

    /* renamed from: b04150415Е04150415Е, reason: contains not printable characters */
    public static int m12b0415041504150415() {
        return 29;
    }

    /* renamed from: b0415ЕЕ04150415Е, reason: contains not printable characters */
    public static int m13b041504150415() {
        return 1;
    }

    /* renamed from: bЕЕ041504150415Е, reason: contains not printable characters */
    public static int m14b041504150415() {
        return 0;
    }

    private native long create(Context context, byte b2, byte b3, byte b4, byte b5, int i, short s, byte b6, byte[] bArr, byte b7);

    private native long createWarp(Context context, byte[] bArr, int i);

    private native void disposeWarp(long j);

    private native void flushWarp(long j);

    private native long getWarpCurrentPosition(long j);

    public static boolean loadSharedLibrary() {
        try {
            System.loadLibrary("ImmEndpointWarpJ");
            return true;
        } catch (UnsatisfiedLinkError e) {
            if (System.getProperty("java.vm.name").contains("Java HotSpot")) {
                return true;
            }
            android.util.Log.e(b, "Unable to load libImmEndpointWarpJ.so.Please make sure this file is in the libs/armeabi folder.");
            if (((m12b0415041504150415() + f3b0415041504150415) * m12b0415041504150415()) % f4b041504150415 != f5b04150415) {
                f2b0415041504150415 = m12b0415041504150415();
                f5b04150415 = m12b0415041504150415();
            }
            e.printStackTrace();
            return false;
        }
    }

    private native void startWarp(long j);

    private native void stopWarp(long j);

    private native void updateWarp(long j, byte[] bArr, int i, long j2, long j3);

    public void dispose() throws Exception {
        while (true) {
            try {
                int[] iArr = new int[-1];
            } catch (Exception e) {
                f2b0415041504150415 = 82;
                try {
                    disposeWarp(this.a);
                    return;
                } catch (Exception e2) {
                    throw e2;
                }
            }
        }
    }

    public void flush() {
        flushWarp(this.a);
    }

    public long getCurrentPosition() {
        if (((f2b0415041504150415 + f3b0415041504150415) * f2b0415041504150415) % f4b041504150415 != f5b04150415) {
            f2b0415041504150415 = m12b0415041504150415();
            f5b04150415 = m12b0415041504150415();
        }
        return getWarpCurrentPosition(this.a);
    }

    public void start() throws Exception {
        try {
            long j = this.a;
            int i = f2b0415041504150415;
            switch ((i * (f3b0415041504150415 + i)) % f4b041504150415) {
                case 0:
                    break;
                default:
                    f2b0415041504150415 = 27;
                    f5b04150415 = m12b0415041504150415();
                    break;
            }
            try {
                startWarp(j);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public void stop() {
        long j = this.a;
        if (((m12b0415041504150415() + f3b0415041504150415) * m12b0415041504150415()) % f4b041504150415 != f5b04150415) {
            f2b0415041504150415 = 12;
            f5b04150415 = m12b0415041504150415();
        }
        stopWarp(j);
    }

    public void update(byte[] bArr, int i, long j, long j2) throws Exception {
        try {
            updateWarp(this.a, bArr, i, j, j2);
            if (((f2b0415041504150415 + f3b0415041504150415) * f2b0415041504150415) % f4b041504150415 != f5b04150415) {
                f2b0415041504150415 = m12b0415041504150415();
                f5b04150415 = m12b0415041504150415();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
