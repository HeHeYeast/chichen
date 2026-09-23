package com.immersion.content;

import java.nio.ByteBuffer;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HapticHeaderUtils extends HeaderUtils {
    private static final String b = "HapticHeaderUtils";

    /* renamed from: b042104210421С04210421, reason: contains not printable characters */
    public static int f6b04210421042104210421 = 33;

    /* renamed from: b04210421С042104210421, reason: contains not printable characters */
    public static int f7b04210421042104210421 = 0;

    /* renamed from: b0421СС042104210421, reason: contains not printable characters */
    public static int f8b0421042104210421 = 2;

    /* renamed from: bССС042104210421, reason: contains not printable characters */
    public static int f9b042104210421 = 1;
    long a;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f189c;
    private int d;

    public HapticHeaderUtils() throws Exception {
        int i = f6b04210421042104210421;
        switch ((i * (f9b042104210421 + i)) % f8b0421042104210421) {
            case 0:
                break;
            default:
                f6b04210421042104210421 = 43;
                f9b042104210421 = m16b0421042104210421();
                break;
        }
        try {
        } catch (Exception e) {
            throw e;
        }
    }

    /* renamed from: b0421С0421042104210421, reason: contains not printable characters */
    public static int m15b04210421042104210421() {
        return 1;
    }

    /* renamed from: bС0421С042104210421, reason: contains not printable characters */
    public static int m16b0421042104210421() {
        return 80;
    }

    /* renamed from: bСС0421042104210421, reason: contains not printable characters */
    public static int m17b0421042104210421() {
        return 0;
    }

    private native int calculateBlockRateNative(long j);

    private native int calculateBlockSizeNative(long j);

    private native int calculateByteOffsetIntoHapticDataNative(long j, int i);

    private native void disposeNative(long j);

    private native String getContentIdNative(long j);

    private native int getEncryptionNative(long j);

    private native int getMajorVersionNumberNative(long j);

    private native int getMinorVersionNumberNative(long j);

    private native int getNumChannelsNative(long j);

    private native long init(byte[] bArr, int i);

    @Override // com.immersion.content.HeaderUtils
    public int calculateBlockRate() {
        if (((f6b04210421042104210421 + m15b04210421042104210421()) * f6b04210421042104210421) % f8b0421042104210421 != m17b0421042104210421()) {
            f6b04210421042104210421 = m16b0421042104210421();
            f7b04210421042104210421 = 12;
        }
        return calculateBlockRateNative(this.a);
    }

    @Override // com.immersion.content.HeaderUtils
    public int calculateBlockSize() {
        if (((f6b04210421042104210421 + f9b042104210421) * f6b04210421042104210421) % f8b0421042104210421 != f7b04210421042104210421) {
            f6b04210421042104210421 = m16b0421042104210421();
            f7b04210421042104210421 = 84;
        }
        int iCalculateBlockSizeNative = calculateBlockSizeNative(this.a);
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
                    break;
            }
        }
        return iCalculateBlockSizeNative;
    }

    @Override // com.immersion.content.HeaderUtils
    public int calculateByteOffsetIntoHapticData(int i) {
        long j = this.a;
        int iM16b0421042104210421 = m16b0421042104210421();
        switch ((iM16b0421042104210421 * (f9b042104210421 + iM16b0421042104210421)) % f8b0421042104210421) {
            case 0:
                break;
            default:
                f6b04210421042104210421 = 89;
                f7b04210421042104210421 = m16b0421042104210421();
                break;
        }
        return calculateByteOffsetIntoHapticDataNative(j, i);
    }

    @Override // com.immersion.content.HeaderUtils
    public void dispose() {
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
        if (((f6b04210421042104210421 + m15b04210421042104210421()) * f6b04210421042104210421) % f8b0421042104210421 != f7b04210421042104210421) {
            f6b04210421042104210421 = m16b0421042104210421();
            f7b04210421042104210421 = 92;
        }
        disposeNative(this.a);
    }

    @Override // com.immersion.content.HeaderUtils
    public String getContentUUID() throws Exception {
        try {
            long j = this.a;
            if (((f6b04210421042104210421 + m15b04210421042104210421()) * f6b04210421042104210421) % f8b0421042104210421 != m17b0421042104210421()) {
                f6b04210421042104210421 = 46;
                f7b04210421042104210421 = 43;
            }
            return getContentIdNative(j);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.immersion.content.HeaderUtils
    public int getEncryption() {
        long j = this.a;
        if (((f6b04210421042104210421 + f9b042104210421) * f6b04210421042104210421) % f8b0421042104210421 != f7b04210421042104210421) {
            f6b04210421042104210421 = m16b0421042104210421();
            f7b04210421042104210421 = m16b0421042104210421();
        }
        return getEncryptionNative(j);
    }

    @Override // com.immersion.content.HeaderUtils
    public int getMajorVersionNumber() {
        return getMajorVersionNumberNative(this.a);
    }

    @Override // com.immersion.content.HeaderUtils
    public int getMinorVersionNumber() throws Exception {
        if (((f6b04210421042104210421 + m15b04210421042104210421()) * f6b04210421042104210421) % f8b0421042104210421 != f7b04210421042104210421) {
            f6b04210421042104210421 = m16b0421042104210421();
            f7b04210421042104210421 = m16b0421042104210421();
        }
        try {
            try {
                return getMinorVersionNumberNative(this.a);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    @Override // com.immersion.content.HeaderUtils
    public int getNumChannels() throws Exception {
        if (((f6b04210421042104210421 + m15b04210421042104210421()) * f6b04210421042104210421) % f8b0421042104210421 != f7b04210421042104210421) {
            f6b04210421042104210421 = 92;
            f7b04210421042104210421 = m16b0421042104210421();
        }
        try {
            return getNumChannelsNative(this.a);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.immersion.content.HeaderUtils
    public void setEncryptedHSI(ByteBuffer byteBuffer, int i) {
        this.d = i;
        this.f189c = new byte[this.d];
        byteBuffer.get(this.f189c, 0, this.d);
        this.a = init(this.f189c, this.d);
    }
}
