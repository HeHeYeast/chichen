package vpadn;

import java.util.Arrays;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class W {
    private static final char[] a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final int[] b;

    static {
        int[] iArr = new int[256];
        b = iArr;
        Arrays.fill(iArr, -1);
        int length = a.length;
        for (int i = 0; i < length; i++) {
            b[a[i]] = i;
        }
        b[61] = 0;
    }

    public static final byte[] a(byte[] bArr, boolean z) {
        int length = bArr != null ? bArr.length : 0;
        if (length == 0) {
            return new byte[0];
        }
        int i = (length / 3) * 3;
        int i2 = (((length - 1) / 3) + 1) << 2;
        int i3 = i2 + (((i2 - 1) / 76) << 1);
        byte[] bArr2 = new byte[i3];
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i6 < i) {
            int i7 = i6 + 1;
            int i8 = i7 + 1;
            int i9 = ((bArr[i7] & 255) << 8) | ((bArr[i6] & 255) << 16);
            i6 = i8 + 1;
            int i10 = i9 | (bArr[i8] & 255);
            int i11 = i5 + 1;
            bArr2[i5] = (byte) a[(i10 >>> 18) & 63];
            int i12 = i11 + 1;
            bArr2[i11] = (byte) a[(i10 >>> 12) & 63];
            int i13 = i12 + 1;
            bArr2[i12] = (byte) a[(i10 >>> 6) & 63];
            i5 = i13 + 1;
            bArr2[i13] = (byte) a[i10 & 63];
            i4++;
            if (i4 == 19 && i5 < i3 - 2) {
                int i14 = i5 + 1;
                bArr2[i5] = 13;
                bArr2[i14] = 10;
                i5 = i14 + 1;
                i4 = 0;
            }
        }
        int i15 = length - i;
        if (i15 > 0) {
            int i16 = (i15 == 2 ? (bArr[length - 1] & 255) << 2 : 0) | ((bArr[i] & 255) << 10);
            bArr2[i3 - 4] = (byte) a[i16 >> 12];
            bArr2[i3 - 3] = (byte) a[(i16 >>> 6) & 63];
            bArr2[i3 - 2] = i15 == 2 ? (byte) a[i16 & 63] : (byte) 61;
            bArr2[i3 - 1] = 61;
        }
        return bArr2;
    }
}
