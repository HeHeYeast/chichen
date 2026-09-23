package jp.adlantis.android.utils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Base64Coder {
    private static byte[] map2;
    private static final String systemLineSeparator = System.getProperty("line.separator");
    private static char[] map1 = new char[64];

    static {
        char c2 = 'A';
        int i = 0;
        while (c2 <= 'Z') {
            map1[i] = c2;
            c2 = (char) (c2 + 1);
            i++;
        }
        char c3 = 'a';
        while (c3 <= 'z') {
            map1[i] = c3;
            c3 = (char) (c3 + 1);
            i++;
        }
        char c4 = '0';
        while (c4 <= '9') {
            map1[i] = c4;
            c4 = (char) (c4 + 1);
            i++;
        }
        int i2 = i + 1;
        map1[i] = '+';
        int i3 = i2 + 1;
        map1[i2] = '/';
        map2 = new byte[128];
        for (int i4 = 0; i4 < map2.length; i4++) {
            map2[i4] = -1;
        }
        for (int i5 = 0; i5 < 64; i5++) {
            map2[map1[i5]] = (byte) i5;
        }
    }

    private Base64Coder() {
    }

    public static byte[] decode(String str) {
        return decode(str.toCharArray());
    }

    public static byte[] decode(char[] cArr) {
        return decode(cArr, 0, cArr.length);
    }

    public static byte[] decode(char[] cArr, int i, int i2) {
        char c2;
        int i3;
        char c3;
        int i4;
        int i5;
        if (i2 % 4 != 0) {
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        }
        while (i2 > 0 && cArr[(i + i2) - 1] == '=') {
            i2--;
        }
        int i6 = (i2 * 3) / 4;
        byte[] bArr = new byte[i6];
        int i7 = i + i2;
        int i8 = 0;
        while (i < i7) {
            int i9 = i + 1;
            char c4 = cArr[i];
            int i10 = i9 + 1;
            char c5 = cArr[i9];
            if (i10 < i7) {
                c2 = cArr[i10];
                i10++;
            } else {
                c2 = 'A';
            }
            if (i10 < i7) {
                int i11 = i10 + 1;
                c3 = cArr[i10];
                i3 = i11;
            } else {
                i3 = i10;
                c3 = 'A';
            }
            if (c4 > 127 || c5 > 127 || c2 > 127 || c3 > 127) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            byte b = map2[c4];
            byte b2 = map2[c5];
            byte b3 = map2[c2];
            byte b4 = map2[c3];
            if (b < 0 || b2 < 0 || b3 < 0 || b4 < 0) {
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            int i12 = (b << 2) | (b2 >>> 4);
            int i13 = ((b2 & 15) << 4) | (b3 >>> 2);
            int i14 = ((b3 & 3) << 6) | b4;
            int i15 = i8 + 1;
            bArr[i8] = (byte) i12;
            if (i15 < i6) {
                i4 = i15 + 1;
                bArr[i15] = (byte) i13;
            } else {
                i4 = i15;
            }
            if (i4 < i6) {
                i5 = i4 + 1;
                bArr[i4] = (byte) i14;
            } else {
                i5 = i4;
            }
            i8 = i5;
            i = i3;
        }
        return bArr;
    }

    public static byte[] decodeLines(String str) {
        char[] cArr = new char[str.length()];
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt != ' ' && cCharAt != '\r' && cCharAt != '\n' && cCharAt != '\t') {
                cArr[i] = cCharAt;
                i++;
            }
        }
        return decode(cArr, 0, i);
    }

    public static String decodeString(String str) {
        return new String(decode(str));
    }

    public static char[] encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    public static char[] encode(byte[] bArr, int i) {
        return encode(bArr, 0, i);
    }

    public static char[] encode(byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6 = ((i2 * 4) + 2) / 3;
        char[] cArr = new char[((i2 + 2) / 3) * 4];
        int i7 = i + i2;
        int i8 = 0;
        while (i < i7) {
            int i9 = i + 1;
            int i10 = bArr[i] & 255;
            if (i9 < i7) {
                i3 = bArr[i9] & 255;
                i9++;
            } else {
                i3 = 0;
            }
            if (i9 < i7) {
                i4 = i9 + 1;
                i5 = bArr[i9] & 255;
            } else {
                i4 = i9;
                i5 = 0;
            }
            int i11 = i10 >>> 2;
            int i12 = ((i10 & 3) << 4) | (i3 >>> 4);
            int i13 = ((i3 & 15) << 2) | (i5 >>> 6);
            int i14 = i5 & 63;
            int i15 = i8 + 1;
            cArr[i8] = map1[i11];
            int i16 = i15 + 1;
            cArr[i15] = map1[i12];
            cArr[i16] = i16 < i6 ? map1[i13] : '=';
            int i17 = i16 + 1;
            cArr[i17] = i17 < i6 ? map1[i14] : '=';
            i8 = i17 + 1;
            i = i4;
        }
        return cArr;
    }

    public static String encodeLines(byte[] bArr) {
        return encodeLines(bArr, 0, bArr.length, 76, systemLineSeparator);
    }

    public static String encodeLines(byte[] bArr, int i, int i2, int i3, String str) {
        int i4 = (i3 * 3) / 4;
        if (i4 <= 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder(((((i2 + i4) - 1) / i4) * str.length()) + (((i2 + 2) / 3) * 4));
        int i5 = 0;
        while (i5 < i2) {
            int iMin = Math.min(i2 - i5, i4);
            sb.append(encode(bArr, i + i5, iMin));
            sb.append(str);
            i5 += iMin;
        }
        return sb.toString();
    }

    public static String encodeString(String str) {
        return new String(encode(str.getBytes()));
    }
}
