package vpadn;

import java.io.UnsupportedEncodingException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class X {
    private static /* synthetic */ boolean a;

    static {
        a = !X.class.desiredAssertionStatus();
    }

    static abstract class a {
        public byte[] a;
        public int b;

        a() {
        }
    }

    public static byte[] a(String str, int i) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        b bVar = new b(0, new byte[(length * 3) / 4]);
        if (!bVar.a(bytes, 0, length, true)) {
            throw new IllegalArgumentException("bad base-64");
        }
        if (bVar.b == bVar.a.length) {
            return bVar.a;
        }
        byte[] bArr = new byte[bVar.b];
        System.arraycopy(bVar.a, 0, bArr, 0, bVar.b);
        return bArr;
    }

    static class b extends a {

        /* renamed from: c, reason: collision with root package name */
        private static final int[] f316c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int e;
        private int f;
        private final int[] g;

        public b(int i, byte[] bArr) {
            this.a = bArr;
            this.g = (i & 8) == 0 ? f316c : d;
            this.e = 0;
            this.f = 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:56:0x0112  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0118  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x0122  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0132  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean a(byte[] r10, int r11, int r12, boolean r13) {
            /*
                Method dump skipped, instructions count: 342
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: vpadn.X.b.a(byte[], int, int, boolean):boolean");
        }
    }

    public static String a(byte[] bArr, int i) {
        int i2;
        try {
            int length = bArr.length;
            c cVar = new c(2, null);
            int i3 = (length / 3) * 4;
            if (!cVar.f317c) {
                switch (length % 3) {
                    case 1:
                        i3 += 2;
                        break;
                    case 2:
                        i3 += 3;
                        break;
                }
            } else if (length % 3 > 0) {
                i3 += 4;
            }
            if (!cVar.d || length <= 0) {
                i2 = i3;
            } else {
                i2 = ((cVar.e ? 2 : 1) * (((length - 1) / 57) + 1)) + i3;
            }
            cVar.a = new byte[i2];
            cVar.a(bArr, 0, length, true);
            if (a || cVar.b == i2) {
                return new String(cVar.a, "US-ASCII");
            }
            throw new AssertionError();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    static class c extends a {
        private static final byte[] f;
        private static final byte[] g;
        private static /* synthetic */ boolean l;

        /* renamed from: c, reason: collision with root package name */
        public final boolean f317c;
        public final boolean d;
        public final boolean e;
        private final byte[] h;
        private int i;
        private int j;
        private final byte[] k;

        static {
            l = !X.class.desiredAssertionStatus();
            f = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
            g = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        }

        public c(int i, byte[] bArr) {
            this.a = null;
            this.f317c = (i & 1) == 0;
            this.d = (i & 2) == 0;
            this.e = (i & 4) != 0;
            this.k = (i & 8) == 0 ? f : g;
            this.h = new byte[2];
            this.i = 0;
            this.j = this.d ? 19 : -1;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:4:0x000f  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x0207 A[PHI: r0 r1
  0x0207: PHI (r0v47 int) = (r0v39 int), (r0v64 int) binds: [B:61:0x01b5, B:24:0x0092] A[DONT_GENERATE, DONT_INLINE]
  0x0207: PHI (r1v22 int) = (r1v21 int), (r1v25 int) binds: [B:61:0x01b5, B:24:0x0092] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean a(byte[] r11, int r12, int r13, boolean r14) {
            /*
                Method dump skipped, instructions count: 550
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: vpadn.X.c.a(byte[], int, int, boolean):boolean");
        }
    }

    private X() {
    }
}
