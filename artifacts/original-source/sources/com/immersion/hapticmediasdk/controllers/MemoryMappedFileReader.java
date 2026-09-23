package com.immersion.hapticmediasdk.controllers;

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
import rrrrrr.ccrrrr;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MemoryMappedFileReader implements IHapticFileReader {
    private static final String a = "MemoryMappedFileReader";

    /* renamed from: b044A044Aъъъъ, reason: contains not printable characters */
    public static int f68b044A044A = 1;

    /* renamed from: bъ044A044Aъъъ, reason: contains not printable characters */
    public static int f69b044A044A = 93;

    /* renamed from: bъ044Aъъъъ, reason: contains not printable characters */
    public static int f70b044A = 0;

    /* renamed from: bъъ044Aъъъ, reason: contains not printable characters */
    public static int f71b044A = 2;
    private static int g = 0;
    private static int h = 0;
    private static final int j = 4096;
    private static final int k = 12288;
    private File b;

    /* renamed from: c, reason: collision with root package name */
    private FileChannel f196c;
    private ccrrrr d;
    private ccrrrr e;
    private int f;
    private HapticFileInformation i;
    private String l;
    private final Profiler m;
    private FileManager n;

    static {
        boolean z = false;
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        g = 40;
        if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
            f69b044A044A = 55;
            f70b044A = 34;
        }
        h = 0;
    }

    public MemoryMappedFileReader(String str, FileManager fileManager) throws Exception {
        try {
            if (((m93b044A() + f68b044A044A) * m93b044A()) % f71b044A != f70b044A) {
                f70b044A = m93b044A();
            }
            this.m = new Profiler();
            try {
                this.l = str;
                this.n = fileManager;
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private int a(ccrrrr ccrrrrVar, int i) {
        int i2 = ccrrrrVar.mHapticDataOffset;
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
        int i3 = i - i2;
        int i4 = f69b044A044A;
        switch ((i4 * (m92b044A044A() + i4)) % f71b044A) {
            case 0:
                break;
            default:
                f69b044A044A = m93b044A();
                f70b044A = m93b044A();
                break;
        }
        return i3 % ccrrrrVar.mMappedByteBuffer.capacity();
    }

    private boolean a() throws Exception {
        boolean zB = false;
        RandomAccessFile randomAccessFile = null;
        try {
            try {
                if (this.i != null) {
                    return true;
                }
                if (a(k)) {
                    return false;
                }
                if (this.b == null) {
                    this.b = this.n.getHapticStorageFile(this.l);
                }
                if (this.f196c == null) {
                    RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.b, "r");
                    try {
                        this.f196c = randomAccessFile2.getChannel();
                        randomAccessFile = randomAccessFile2;
                    } catch (FileNotFoundException e) {
                        e = e;
                        randomAccessFile = randomAccessFile2;
                        Log.e(a, e.getMessage());
                        this.n.closeCloseable(randomAccessFile);
                        this.n.closeCloseable(this.f196c);
                        return zB;
                    }
                }
                if (this.f196c == null) {
                    return false;
                }
                zB = b();
                return zB;
            } catch (FileNotFoundException e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return zB;
        }
    }

    private boolean a(int i) {
        if (this.f < i) {
            return false;
        }
        if (((m93b044A() + f68b044A044A) * m93b044A()) % f71b044A == f70b044A) {
            return true;
        }
        f69b044A044A = 58;
        f70b044A = 75;
        return true;
    }

    private int b(int i) throws Exception {
        int sampleHertz = i / (1000 / this.i.getSampleHertz());
        if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
            f69b044A044A = 77;
            f70b044A = 64;
        }
        float f = (r0 * r2) / 8.0f;
        float bitsPerSample = (this.i.getBitsPerSample() * this.i.getNumberOfChannels()) / 8;
        int i2 = (int) bitsPerSample;
        if (f > bitsPerSample) {
            i2++;
        }
        return i2 * sampleHertz;
    }

    private boolean b() throws Exception {
        boolean z = false;
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
            byteBufferAllocate.position(0);
            if (this.f196c.read(byteBufferAllocate, 16L) != 4) {
                return false;
            }
            byteBufferAllocate.flip();
            int i = byteBufferAllocate.getInt() + 28;
            ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(i);
            byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
            int i2 = this.f196c.read(byteBufferAllocate2, 0L);
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
            if (i2 != i) {
                return false;
            }
            byteBufferAllocate2.flip();
            HapticFileInformation.Builder builder = new HapticFileInformation.Builder();
            builder.setFilePath(this.b.getAbsolutePath());
            byteBufferAllocate2.position(4);
            builder.setTotalFileLength(byteBufferAllocate2.getInt() + 8);
            byteBufferAllocate2.position(20);
            builder.setMajorVersion(byteBufferAllocate2.get());
            builder.setMinorVersion(byteBufferAllocate2.get());
            builder.setEncoding(byteBufferAllocate2.get());
            byteBufferAllocate2.position(24);
            builder.setSampleHertz(byteBufferAllocate2.getInt());
            builder.setBitsPerSample(byteBufferAllocate2.get() | (byteBufferAllocate2.get() << 8));
            int i3 = byteBufferAllocate2.get();
            builder.setNumberOfChannels(i3);
            int[] iArr = new int[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                iArr[i4] = byteBufferAllocate2.get();
            }
            builder.setActuatorArray(iArr);
            builder.setCompressionScheme(byteBufferAllocate2.get());
            byteBufferAllocate2.position(byteBufferAllocate2.position() + 4);
            builder.setHapticDataLength(byteBufferAllocate2.getInt());
            builder.setHapticDataStartByteOffset(byteBufferAllocate2.position());
            this.i = builder.build();
            int sampleHertz = (g * this.i.getSampleHertz()) / 1000;
            int i5 = f69b044A044A;
            switch ((i5 * (m92b044A044A() + i5)) % f71b044A) {
                case 0:
                    break;
                default:
                    f69b044A044A = m93b044A();
                    f70b044A = 51;
                    break;
            }
            h = ((sampleHertz * this.i.getBitsPerSample()) * this.i.getNumberOfChannels()) / 8;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean b(ccrrrr ccrrrrVar, int i) {
        boolean z = false;
        if (i >= ccrrrrVar.mHapticDataOffset) {
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
            return false;
        }
        int i2 = f69b044A044A;
        switch ((i2 * (f68b044A044A + i2)) % f71b044A) {
            case 0:
                break;
            default:
                f69b044A044A = m93b044A();
                f70b044A = m93b044A();
                break;
        }
        return true;
    }

    /* renamed from: b044Aъ044A044Aъъ, reason: contains not printable characters */
    public static int m91b044A044A044A() {
        return 0;
    }

    /* renamed from: b044Aъ044Aъъъ, reason: contains not printable characters */
    public static int m92b044A044A() {
        return 1;
    }

    /* renamed from: b044Aъъъъъ, reason: contains not printable characters */
    public static int m93b044A() {
        return 73;
    }

    /* renamed from: bъ044A044A044Aъъ, reason: contains not printable characters */
    public static int m94b044A044A044A() {
        return 2;
    }

    private int c(int i) throws Exception {
        try {
            HapticFileInformation hapticFileInformation = this.i;
            if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
                f69b044A044A = 98;
                f70b044A = 21;
            }
            try {
                return hapticFileInformation.getHapticDataStartByteOffset() + b(i);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private void c() throws Exception {
        try {
            if (this.e == null) {
                return;
            }
            int i = this.e.mHapticDataOffset + 4096;
            try {
                this.d = this.e;
                if (((m93b044A() + f68b044A044A) * m93b044A()) % m94b044A044A044A() != m91b044A044A044A()) {
                    f69b044A044A = 80;
                    f70b044A = 68;
                }
                this.e = d(i);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    private static boolean c(ccrrrr ccrrrrVar, int i) {
        if (i < ccrrrrVar.mHapticDataOffset + ccrrrrVar.mMappedByteBuffer.capacity()) {
            return false;
        }
        if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A == f70b044A) {
            return true;
        }
        f69b044A044A = m93b044A();
        f70b044A = m93b044A();
        return true;
    }

    private ccrrrr d(int i) throws Exception {
        this.m.startTiming();
        if (i < this.i.getHapticDataLength()) {
            int hapticDataStartByteOffset = this.i.getHapticDataStartByteOffset() + i;
            int hapticDataLength = i + 4096 <= this.i.getHapticDataLength() ? 4096 : this.i.getHapticDataLength() - i;
            if (i + hapticDataLength > this.f) {
                throw new NotEnoughHapticBytesAvailableException("Not enough bytes available yet.");
            }
            MappedByteBuffer map = this.f196c.map(FileChannel.MapMode.READ_ONLY, hapticDataStartByteOffset, hapticDataLength);
            if (map != null) {
                map.order(ByteOrder.LITTLE_ENDIAN);
                ccrrrr ccrrrrVar = new ccrrrr(null);
                ccrrrrVar.mMappedByteBuffer = map;
                ccrrrrVar.mHapticDataOffset = i;
                return ccrrrrVar;
            }
        }
        return null;
    }

    private void d() {
        Log.d(a, "%%%%%%%%%%% logBufferState %%%%%%%%%%%");
        if (this.d != null) {
            Log.d(a, "mCurrentMMW capacity = " + this.d.mMappedByteBuffer.capacity());
            Log.d(a, "mCurrentMMW position = " + this.d.mMappedByteBuffer.position());
            Log.d(a, "mCurrentMMW remaining = " + this.d.mMappedByteBuffer.remaining());
            Log.d(a, "mCurrentMMW mHapticDataOffset = " + this.d.mHapticDataOffset);
            StringBuilder sbAppend = new StringBuilder().append("mCurrentMMW mHapticDataOffset + position = ");
            ccrrrr ccrrrrVar = this.d;
            if (((m93b044A() + f68b044A044A) * m93b044A()) % m94b044A044A044A() != m91b044A044A044A()) {
                f69b044A044A = m93b044A();
                f70b044A = m93b044A();
            }
            Log.d(a, sbAppend.append(ccrrrrVar.mHapticDataOffset + this.d.mMappedByteBuffer.position()).toString());
        } else {
            Log.d(a, "mCurrentMMW is null");
        }
        Log.d(a, "--------------------------------------");
        if (this.e != null) {
            Log.d(a, "mNextMMW capacity = " + this.e.mMappedByteBuffer.capacity());
            Log.d(a, "mNextMMW position = " + this.e.mMappedByteBuffer.position());
            Log.d(a, "mNextMMW remaining = " + this.e.mMappedByteBuffer.remaining());
            Log.d(a, "mNextMMW mHapticDataOffset = " + this.e.mHapticDataOffset);
            Log.d(a, "mNextMMW mHapticDataOffset + position = " + (this.e.mHapticDataOffset + this.e.mMappedByteBuffer.position()));
        } else {
            Log.d(a, "mNextMMW is null");
        }
        Log.d(a, "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static boolean d(ccrrrr ccrrrrVar, int i) throws Exception {
        try {
            if (!b(ccrrrrVar, i)) {
                boolean zC = c(ccrrrrVar, i);
                if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
                    f69b044A044A = 52;
                    f70b044A = 7;
                }
                if (!zC) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    private static boolean e(ccrrrr ccrrrrVar, int i) throws Exception {
        int i2 = f69b044A044A;
        switch ((i2 * (m92b044A044A() + i2)) % f71b044A) {
            case 0:
                break;
            default:
                f69b044A044A = 57;
                f70b044A = 27;
                break;
        }
        try {
            try {
                return c(ccrrrrVar, h + i);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public boolean bufferAtPlaybackPosition(int i) throws Exception {
        try {
            if (!a()) {
                return false;
            }
            int iB = b(i);
            try {
                if (this.d == null || d(this.d, iB)) {
                    try {
                        if (this.e == null || d(this.e, iB) || e(this.e, iB)) {
                            if (this.d == null || this.d.mHapticDataOffset != iB) {
                                this.d = d(iB);
                            }
                            if (this.e == null || this.e.mHapticDataOffset != iB + 4096) {
                                this.e = d(iB + 4096);
                            }
                            return true;
                        }
                        c();
                        while (true) {
                            try {
                                int[] iArr = new int[-1];
                            } catch (Exception e) {
                                f69b044A044A = 5;
                            }
                        }
                    } catch (NotEnoughHapticBytesAvailableException e2) {
                        Log.w(a, e2.getMessage());
                        return false;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return false;
                    }
                }
                if (this.d != null) {
                    this.d.mMappedByteBuffer.position(a(this.d, iB));
                }
                return true;
            } catch (Exception e4) {
                throw e4;
            }
        } catch (Exception e5) {
            throw e5;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void close() throws Exception {
        int i = f69b044A044A;
        switch ((i * (m92b044A044A() + i)) % f71b044A) {
            case 0:
                break;
            default:
                f69b044A044A = m93b044A();
                f70b044A = 35;
                break;
        }
        try {
            this.n.closeCloseable(this.f196c);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public long getBlockOffset(long j2) {
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
        int i = f69b044A044A;
        switch ((i * (f68b044A044A + i)) % f71b044A) {
            case 0:
                break;
            default:
                f69b044A044A = 89;
                f70b044A = 47;
                break;
        }
        return 0L;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public int getBlockSizeMS() throws Exception {
        try {
            int i = g;
            if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
                f69b044A044A = m93b044A();
                f70b044A = m93b044A();
            }
            return i;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public byte[] getBufferForPlaybackPosition(int i) throws NotEnoughHapticBytesAvailableException {
        boolean z = false;
        if (this.d == null) {
            return null;
        }
        ccrrrr ccrrrrVar = this.d;
        if (((f69b044A044A + f68b044A044A) * f69b044A044A) % f71b044A != f70b044A) {
            f69b044A044A = m93b044A();
            f70b044A = 47;
        }
        int i2 = ccrrrrVar.mHapticDataOffset;
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        if (i2 + this.d.mMappedByteBuffer.position() >= this.i.getHapticDataLength()) {
            return null;
        }
        try {
            byte[] bArr = new byte[h];
            if (h >= this.d.mMappedByteBuffer.remaining()) {
                int iRemaining = this.d.mMappedByteBuffer.remaining();
                int iRemaining2 = h - iRemaining;
                this.d.mMappedByteBuffer.get(bArr, 0, iRemaining);
                if (iRemaining2 > 0 && this.e != null) {
                    if (this.e.mMappedByteBuffer.remaining() < iRemaining2) {
                        iRemaining2 = this.e.mMappedByteBuffer.remaining();
                    }
                    this.e.mMappedByteBuffer.get(bArr, iRemaining, iRemaining2);
                }
                c();
            } else {
                this.d.mMappedByteBuffer.get(bArr, 0, h);
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public byte[] getEncryptedHapticHeader() {
        return new byte[0];
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public int getHapticBlockIndex(long j2) {
        return 0;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public HapticFileInformation getHapticFileInformation() {
        return this.i;
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void setBlockSizeMS(int i) {
        g = i;
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
        int iM93b044A = m93b044A();
        switch ((iM93b044A * (m92b044A044A() + iM93b044A)) % f71b044A) {
            case 0:
                break;
            default:
                f70b044A = 15;
                break;
        }
    }

    @Override // com.immersion.hapticmediasdk.controllers.IHapticFileReader
    public void setBytesAvailable(int i) throws Exception {
        this.f = i;
        if (((f69b044A044A + m92b044A044A()) * f69b044A044A) % f71b044A != m91b044A044A044A()) {
            f69b044A044A = 62;
            f70b044A = 4;
        }
        a();
    }
}
