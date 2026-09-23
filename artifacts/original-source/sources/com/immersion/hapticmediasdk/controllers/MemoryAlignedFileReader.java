package com.immersion.hapticmediasdk.controllers;

import com.immersion.content.HapticHeaderUtils;
import com.immersion.content.HeaderUtils;
import com.immersion.hapticmediasdk.models.HapticFileInformation;
import com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException;
import com.immersion.hapticmediasdk.utils.FileManager;
import com.immersion.hapticmediasdk.utils.Log;
import com.immersion.hapticmediasdk.utils.Profiler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import rrrrrr.rcrcrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MemoryAlignedFileReader implements IHapticFileReader {
    private static final String a = "MemoryAlignedFileReader";

    /* renamed from: b0415ЕЕ0415ЕЕ, reason: contains not printable characters */
    public static int f64b04150415 = 10;

    /* renamed from: bЕ041504150415ЕЕ, reason: contains not printable characters */
    public static int f65b041504150415 = 1;

    /* renamed from: bЕ0415Е0415ЕЕ, reason: contains not printable characters */
    public static int f66b04150415 = 0;

    /* renamed from: bЕЕ04150415ЕЕ, reason: contains not printable characters */
    public static int f67b04150415 = 2;
    private static int h = 80;
    private static int i = 0;
    private static final int k = 1024;
    private static final int l = 3072;
    private static final int t = 16;
    private File b;

    /* renamed from: c, reason: collision with root package name */
    private FileChannel f195c;
    private rcrcrr d;
    private rcrcrr e;
    private int f;
    private int g;
    private HapticFileInformation j;
    private String m;
    private FileManager n;
    private HeaderUtils o;
    private byte[] p;
    private final Profiler q;
    private int r;
    private int s;

    public MemoryAlignedFileReader(String str, HeaderUtils headerUtils) throws Exception {
        try {
            this.f = 0;
            this.m = null;
            this.n = null;
            this.p = null;
            try {
                this.q = new Profiler();
                this.m = str;
                int i2 = f64b04150415;
                switch ((i2 * (f65b041504150415 + i2)) % f67b04150415) {
                    case 0:
                        break;
                    default:
                        f64b04150415 = m88b041504150415();
                        f66b04150415 = 92;
                        break;
                }
                this.o = headerUtils;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public MemoryAlignedFileReader(String str, FileManager fileManager, int i2) throws Exception {
        try {
            if (((f64b04150415 + m87b041504150415()) * f64b04150415) % f67b04150415 != f66b04150415) {
                f64b04150415 = m88b041504150415();
                f66b04150415 = m88b041504150415();
            }
            this.f = 0;
            this.m = null;
            this.n = null;
            this.p = null;
            this.q = new Profiler();
            try {
                this.m = str;
                this.n = fileManager;
                this.o = new HapticHeaderUtils();
                this.f = i2;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int a(rcrcrr rcrcrrVar, int i2) throws Exception {
        if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 != f66b04150415) {
            f64b04150415 = 0;
            f66b04150415 = m88b041504150415();
        }
        try {
            try {
                return (i2 - rcrcrrVar.mHapticDataOffset) % rcrcrrVar.mMappedByteBuffer.capacity();
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private boolean a() throws Exception {
        boolean zB = false;
        RandomAccessFile randomAccessFile = null;
        try {
            try {
                try {
                    if (this.j != null) {
                        return true;
                    }
                    if (this.b == null) {
                        if (this.n != null) {
                            this.b = this.n.getHapticStorageFile(this.m);
                        } else {
                            if (this.m == null) {
                                return false;
                            }
                            this.b = new File(this.m);
                        }
                    }
                    if (this.f195c == null) {
                        RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.b, "r");
                        try {
                            this.f195c = randomAccessFile2.getChannel();
                            randomAccessFile = randomAccessFile2;
                        } catch (FileNotFoundException e) {
                            randomAccessFile = randomAccessFile2;
                            try {
                                Log.e(a, "FileNotFoundException");
                                this.n.closeCloseable(randomAccessFile);
                                this.n.closeCloseable(this.f195c);
                                return zB;
                            } catch (Exception e2) {
                                throw e2;
                            }
                        }
                    }
                    if (this.f195c != null) {
                        zB = b();
                        return zB;
                    }
                    if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 == f66b04150415) {
                        return false;
                    }
                    f64b04150415 = m88b041504150415();
                    f66b04150415 = 96;
                    return false;
                } catch (FileNotFoundException e3) {
                }
            } catch (Exception e4) {
                e4.printStackTrace();
                return zB;
            }
        } catch (Exception e5) {
            throw e5;
        }
    }

    private boolean a(int i2) throws Exception {
        if (((f64b04150415 + m87b041504150415()) * f64b04150415) % f67b04150415 != f66b04150415) {
            f64b04150415 = 31;
            f66b04150415 = 17;
        }
        try {
            return this.g >= i2;
        } catch (Exception e) {
            throw e;
        }
    }

    private int b(int i2) {
        boolean z = false;
        if (this.o == null) {
            return 0;
        }
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
        int i3 = f64b04150415;
        switch ((i3 * (f65b041504150415 + i3)) % f67b04150415) {
            case 0:
                break;
            default:
                f64b04150415 = 53;
                f66b04150415 = 85;
                break;
        }
        return this.o.calculateByteOffsetIntoHapticData(i2);
    }

    private boolean b() {
        boolean z = false;
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
            byteBufferAllocate.position(0);
            if (this.f195c.read(byteBufferAllocate, 16L) != 4) {
                return false;
            }
            byteBufferAllocate.flip();
            int i2 = byteBufferAllocate.getInt();
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
            int i3 = i2 + 28;
            ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(i3);
            byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
            if (this.f195c.read(byteBufferAllocate2, 0L) != i3) {
                return false;
            }
            byteBufferAllocate2.position(4);
            this.r = (byteBufferAllocate2.getInt() + 8) - i3;
            this.s = i3;
            byteBufferAllocate2.position(20);
            this.p = new byte[i2];
            byteBufferAllocate2.duplicate().get(this.p, 0, i2);
            this.o.setEncryptedHSI(byteBufferAllocate2, i2);
            int iCalculateBlockSize = this.o.calculateBlockSize();
            if (iCalculateBlockSize <= 0) {
                return false;
            }
            i = iCalculateBlockSize * 2;
            int iCalculateBlockRate = this.o.calculateBlockRate();
            if (iCalculateBlockRate <= 0) {
                return false;
            }
            h = iCalculateBlockRate;
            while (true) {
                try {
                    int[] iArr = new int[-1];
                } catch (Exception e) {
                    f64b04150415 = m88b041504150415();
                    return true;
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private static boolean b(rcrcrr rcrcrrVar, int i2) {
        if (i2 >= rcrcrrVar.mHapticDataOffset) {
            return false;
        }
        if (((f64b04150415 + f65b041504150415) * f64b04150415) % m89b041504150415() == f66b04150415) {
            return true;
        }
        f64b04150415 = 22;
        f66b04150415 = m88b041504150415();
        return true;
    }

    /* renamed from: b04150415Е0415ЕЕ, reason: contains not printable characters */
    public static int m87b041504150415() {
        return 1;
    }

    /* renamed from: b0415Е04150415ЕЕ, reason: contains not printable characters */
    public static int m88b041504150415() {
        return 23;
    }

    /* renamed from: b0415Е0415Е0415Е, reason: contains not printable characters */
    public static int m89b041504150415() {
        return 2;
    }

    /* renamed from: bЕ04150415Е0415Е, reason: contains not printable characters */
    public static int m90b041504150415() {
        return 0;
    }

    private int c(int i2) {
        return this.s + b(i2);
    }

    private void c() throws Exception {
        String str = null;
        try {
            if (this.e != null) {
                int i2 = this.e.mHapticDataOffset + 1024;
                this.d = this.e;
                try {
                    this.e = d(i2 - (i / 2));
                    return;
                } catch (Exception e) {
                    throw e;
                }
            }
            while (true) {
                try {
                    int[] iArr = new int[-1];
                } catch (Exception e2) {
                    f64b04150415 = m88b041504150415();
                    while (true) {
                        try {
                            str.length();
                        } catch (Exception e3) {
                            f64b04150415 = 39;
                            while (true) {
                                try {
                                    int[] iArr2 = new int[-1];
                                } catch (Exception e4) {
                                    f64b04150415 = 45;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e5) {
            throw e5;
        }
    }

    private static boolean c(rcrcrr rcrcrrVar, int i2) throws Exception {
        try {
            if (i2 < rcrcrrVar.mHapticDataOffset + rcrcrrVar.mMappedByteBuffer.capacity()) {
                return false;
            }
            if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 == m90b041504150415()) {
                return true;
            }
            f64b04150415 = m88b041504150415();
            f66b04150415 = m88b041504150415();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    private int d() throws Exception {
        try {
            if (this.o == null) {
                return 0;
            }
            try {
                int numChannels = this.o.getNumChannels();
                if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 == f66b04150415) {
                    return numChannels;
                }
                f64b04150415 = m88b041504150415();
                f66b04150415 = m88b041504150415();
                return numChannels;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private rcrcrr d(int i2) throws Exception {
        int i3;
        try {
            this.q.startTiming();
            if (i2 < this.r) {
                int i4 = this.s + i2;
                try {
                    int iF = f();
                    if (i2 + 1024 + iF <= this.r) {
                        int i5 = iF + 1024;
                        if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 != f66b04150415) {
                            f64b04150415 = 31;
                            f66b04150415 = 69;
                        }
                        i3 = i5;
                    } else {
                        i3 = this.r - i2;
                    }
                    if (i2 + i3 > this.g) {
                        throw new NotEnoughHapticBytesAvailableException("Not enough bytes available yet.");
                    }
                    MappedByteBuffer map = this.f195c.map(FileChannel.MapMode.READ_ONLY, i4, i3);
                    if (map != null) {
                        map.order(ByteOrder.BIG_ENDIAN);
                        rcrcrr rcrcrrVar = new rcrcrr(null);
                        rcrcrrVar.mMappedByteBuffer = map;
                        rcrcrrVar.mHapticDataOffset = i2;
                        return rcrcrrVar;
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            return null;
        } catch (Exception e2) {
            throw e2;
        }
    }

    private static boolean d(rcrcrr rcrcrrVar, int i2) {
        if (b(rcrcrrVar, i2)) {
            return true;
        }
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
        if (!c(rcrcrrVar, i2)) {
            return false;
        }
        int iM88b041504150415 = m88b041504150415();
        switch ((iM88b041504150415 * (f65b041504150415 + iM88b041504150415)) % f67b04150415) {
            case 0:
                break;
            default:
                f64b04150415 = m88b041504150415();
                f66b04150415 = 24;
                break;
        }
        return true;
    }

    private void e() {
        Log.d(a, "%%%%%%%%%%% logBufferState %%%%%%%%%%%");
        if (this.d != null) {
            Log.d(a, "mCurrentMMW capacity = " + this.d.mMappedByteBuffer.capacity());
            StringBuilder sb = new StringBuilder();
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
            Log.d(a, sb.append("mCurrentMMW position = ").append(this.d.mMappedByteBuffer.position()).toString());
            Log.d(a, "mCurrentMMW remaining = " + this.d.mMappedByteBuffer.remaining());
            Log.d(a, "mCurrentMMW mHapticDataOffset = " + this.d.mHapticDataOffset);
            Log.d(a, "mCurrentMMW mHapticDataOffset + position = " + (this.d.mHapticDataOffset + this.d.mMappedByteBuffer.position()));
        } else {
            Log.d(a, "mCurrentMMW is null");
        }
        Log.d(a, "--------------------------------------");
        if (this.e != null) {
            Log.d(a, "mNextMMW capacity = " + this.e.mMappedByteBuffer.capacity());
            Log.d(a, "mNextMMW position = " + this.e.mMappedByteBuffer.position());
            Log.d(a, "mNextMMW remaining = " + this.e.mMappedByteBuffer.remaining());
            Log.d(a, "mNextMMW mHapticDataOffset = " + this.e.mHapticDataOffset);
            StringBuilder sbAppend = new StringBuilder().append("mNextMMW mHapticDataOffset + position = ");
            if (((f64b04150415 + m87b041504150415()) * f64b04150415) % f67b04150415 != f66b04150415) {
                f64b04150415 = m88b041504150415();
                f66b04150415 = m88b041504150415();
            }
            Log.d(a, sbAppend.append(this.e.mHapticDataOffset + this.e.mMappedByteBuffer.position()).toString());
        } else {
            Log.d(a, "mNextMMW is null");
        }
        Log.d(a, "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static boolean e(rcrcrr rcrcrrVar, int i2) throws Exception {
        try {
            int i3 = i;
            int i4 = f64b04150415;
            switch ((i4 * (f65b041504150415 + i4)) % f67b04150415) {
                case 0:
                    break;
                default:
                    f64b04150415 = 4;
                    f66b04150415 = 62;
                    break;
            }
            return c(rcrcrrVar, i3 + i2);
        } catch (Exception e) {
            throw e;
        }
    }

    private int f() {
        int i2 = 0;
        while ((i2 + 1024) % (i / 2) != 0) {
            i2 += 16;
        }
        return i2;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x006d  */
    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean bufferAtPlaybackPosition(int r8) throws java.lang.Exception {
        /*
            r7 = this;
            r1 = 1
            r0 = 0
            boolean r2 = r7.a()
            if (r2 != 0) goto L9
        L8:
            return r0
        L9:
            int r2 = r7.b(r8)
            int r3 = r7.r
            if (r2 >= r3) goto L8
            rrrrrr.rcrcrr r3 = r7.d
            if (r3 == 0) goto L1d
            rrrrrr.rcrcrr r3 = r7.d
            boolean r3 = d(r3, r2)
            if (r3 == 0) goto L7f
        L1d:
            rrrrrr.rcrcrr r3 = r7.e     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 == 0) goto L31
            rrrrrr.rcrcrr r3 = r7.e     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            boolean r3 = d(r3, r2)     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 != 0) goto L31
            rrrrrr.rcrcrr r3 = r7.e     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            boolean r3 = e(r3, r2)     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 == 0) goto L7c
        L31:
            rrrrrr.rcrcrr r3 = r7.d     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 == 0) goto L3b
            rrrrrr.rcrcrr r3 = r7.d     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            int r3 = r3.mHapticDataOffset     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 == r2) goto L41
        L3b:
            rrrrrr.rcrcrr r3 = r7.d(r2)     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            r7.d = r3     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
        L41:
            rrrrrr.rcrcrr r3 = r7.e     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            if (r3 == 0) goto L6d
            rrrrrr.rcrcrr r3 = r7.e     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            int r3 = r3.mHapticDataOffset     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            int r4 = r2 + 1024
            int r5 = com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.i     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            int r5 = r5 / 2
            int r4 = r4 - r5
            int r5 = m88b041504150415()
            int r6 = com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.f65b041504150415
            int r5 = r5 + r6
            int r6 = m88b041504150415()
            int r5 = r5 * r6
            int r6 = com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.f67b04150415
            int r5 = r5 % r6
            int r6 = com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.f66b04150415
            if (r5 == r6) goto L6b
            r5 = 98
            com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.f64b04150415 = r5
            r5 = 73
            com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.f66b04150415 = r5
        L6b:
            if (r3 == r4) goto L7a
        L6d:
            int r2 = r2 + 1024
            int r3 = com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.i     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            int r3 = r3 / 2
            int r2 = r2 - r3
            rrrrrr.rcrcrr r2 = r7.d(r2)     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
            r7.e = r2     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
        L7a:
            r0 = r1
            goto L8
        L7c:
            r7.c()     // Catch: java.io.IOException -> L93 com.immersion.hapticmediasdk.models.NotEnoughHapticBytesAvailableException -> L96
        L7f:
            rrrrrr.rcrcrr r0 = r7.d
            if (r0 == 0) goto L90
            rrrrrr.rcrcrr r0 = r7.d
            java.nio.MappedByteBuffer r0 = r0.mMappedByteBuffer
            rrrrrr.rcrcrr r3 = r7.d
            int r2 = r7.a(r3, r2)
            r0.position(r2)
        L90:
            r0 = r1
            goto L8
        L93:
            r1 = move-exception
            goto L8
        L96:
            r1 = move-exception
            goto L8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader.bufferAtPlaybackPosition(int):boolean");
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void close() throws Exception {
        this.n.closeCloseable(this.f195c);
        this.o.dispose();
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public long getBlockOffset(long j) {
        long j2 = j % h;
        int i2 = f64b04150415;
        switch ((i2 * (f65b041504150415 + i2)) % f67b04150415) {
            case 0:
                break;
            default:
                f64b04150415 = m88b041504150415();
                f66b04150415 = 40;
                break;
        }
        return (j2 * 16) / h;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public int getBlockSizeMS() {
        int i2 = h;
        if (((f64b04150415 + f65b041504150415) * f64b04150415) % f67b04150415 != f66b04150415) {
            f64b04150415 = 57;
            f66b04150415 = 94;
        }
        return i2;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public byte[] getBufferForPlaybackPosition(int i2) throws NotEnoughHapticBytesAvailableException {
        int iB;
        byte[] bArr = null;
        boolean z = false;
        if (this.d == null || (iB = b(i2)) >= this.r - i) {
            return null;
        }
        try {
            byte[] bArr2 = new byte[i];
            if (this.d.mMappedByteBuffer.remaining() < i) {
                c();
            }
            int iPosition = this.d.mMappedByteBuffer.position() + this.d.mHapticDataOffset;
            if (iPosition < iB || iPosition > iB) {
                int iPosition2 = (iB - iPosition) + this.d.mMappedByteBuffer.position();
                if (iPosition2 < 0) {
                    iPosition2 = 0;
                } else if (this.d.mMappedByteBuffer.limit() < iPosition2) {
                    iPosition2 = this.d.mMappedByteBuffer.limit() - 1;
                }
                this.d.mMappedByteBuffer.position(iPosition2);
            }
            int iRemaining = this.d.mMappedByteBuffer.remaining();
            int i3 = f64b04150415;
            switch ((i3 * (f65b041504150415 + i3)) % m89b041504150415()) {
                case 0:
                    break;
                default:
                    f64b04150415 = m88b041504150415();
                    f66b04150415 = m88b041504150415();
                    break;
            }
            MappedByteBuffer mappedByteBuffer = this.d.mMappedByteBuffer;
            if (iRemaining >= i) {
                iRemaining = i;
            }
            mappedByteBuffer.get(bArr2, 0, iRemaining);
            MappedByteBuffer mappedByteBuffer2 = this.d.mMappedByteBuffer;
            int iPosition3 = this.d.mMappedByteBuffer.position();
            while (true) {
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
            mappedByteBuffer2.position(iPosition3 - (i / 2));
            bArr = bArr2;
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return bArr;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public byte[] getEncryptedHapticHeader() {
        return this.p;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public int getHapticBlockIndex(long j) throws Exception {
        try {
            int iB = b((int) j);
            int i2 = this.f;
            if (((f64b04150415 + m87b041504150415()) * f64b04150415) % f67b04150415 != f66b04150415) {
                f64b04150415 = 2;
                f66b04150415 = m88b041504150415();
            }
            if (i2 == 2) {
                return iB / 16;
            }
            if (this.f < 3) {
                return 0;
            }
            try {
                return iB / (d() * 16);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public HapticFileInformation getHapticFileInformation() {
        return this.j;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void setBlockSizeMS(int i2) throws Exception {
        int i3 = f64b04150415;
        switch ((i3 * (m87b041504150415() + i3)) % f67b04150415) {
            case 0:
                break;
            default:
                f64b04150415 = m88b041504150415();
                f66b04150415 = m88b041504150415();
                break;
        }
        try {
            h = i2;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void setBytesAvailable(int i2) throws Exception {
        this.g = i2;
        if (this.g <= 0) {
            this.g = i2;
            a();
        }
    }
}
