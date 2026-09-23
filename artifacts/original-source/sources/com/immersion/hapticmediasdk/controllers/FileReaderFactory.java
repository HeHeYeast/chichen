package com.immersion.hapticmediasdk.controllers;

import com.immersion.content.HapticHeaderUtils;
import com.immersion.hapticmediasdk.utils.FileManager;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FileReaderFactory {
    private static final String a = "FileReaderFactory";

    /* renamed from: b044604460446ц0446ц, reason: contains not printable characters */
    public static int f41b0446044604460446 = 2;

    /* renamed from: b0446ц044604460446ц, reason: contains not printable characters */
    public static int f42b0446044604460446 = 0;

    /* renamed from: b0446ц0446ц0446ц, reason: contains not printable characters */
    public static int f43b044604460446 = 72;

    /* renamed from: bц0446ц04460446ц, reason: contains not printable characters */
    public static int f44b044604460446 = 1;

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x001b. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x001f. Please report as an issue. */
    public FileReaderFactory() {
        int i = f43b044604460446;
        switch ((i * (m37b044604460446() + i)) % f41b0446044604460446) {
            case 0:
                break;
            default:
                f43b044604460446 = m38b04460446();
                f41b0446044604460446 = m38b04460446();
                break;
        }
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
    }

    private static int a(String str, FileManager fileManager) throws Exception {
        File file;
        int iA = 0;
        String str2 = null;
        try {
            if (fileManager != null) {
                file = fileManager.getHapticStorageFile(str);
            } else {
                if (str == null) {
                    return 0;
                }
                file = new File(str);
            }
            if (file.length() == 0) {
                return -1;
            }
            FileChannel channel = 0 == 0 ? new RandomAccessFile(file, "r").getChannel() : null;
            if (channel == null) {
                return 0;
            }
            iA = a(channel);
            channel.close();
            while (true) {
                try {
                    str2.length();
                } catch (Exception e) {
                    f43b044604460446 = m38b04460446();
                    return iA;
                }
            }
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
                return iA;
            } catch (Exception e3) {
                throw e3;
            }
        }
    }

    private static int a(FileChannel fileChannel) {
        boolean z = false;
        try {
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
            if (((m38b04460446() + m37b044604460446()) * m38b04460446()) % m36b0446044604460446() != f42b0446044604460446) {
                f43b044604460446 = m38b04460446();
                f42b0446044604460446 = 93;
            }
            byteBufferAllocate.position(0);
            if (fileChannel.read(byteBufferAllocate, 16L) != 4) {
                return 0;
            }
            byteBufferAllocate.flip();
            int i = byteBufferAllocate.getInt();
            int i2 = i + 28;
            ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(i2);
            byteBufferAllocate2.order(ByteOrder.LITTLE_ENDIAN);
            if (fileChannel.read(byteBufferAllocate2, 0L) != i2) {
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
            byteBufferAllocate2.position(4);
            int i3 = byteBufferAllocate2.getInt() + 8;
            byteBufferAllocate2.position(20);
            HapticHeaderUtils hapticHeaderUtils = new HapticHeaderUtils();
            hapticHeaderUtils.setEncryptedHSI(byteBufferAllocate2, i);
            return hapticHeaderUtils.getMajorVersionNumber();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* renamed from: b0446цц04460446ц, reason: contains not printable characters */
    public static int m35b044604460446() {
        return 0;
    }

    /* renamed from: bц0446044604460446ц, reason: contains not printable characters */
    public static int m36b0446044604460446() {
        return 2;
    }

    /* renamed from: bц04460446ц0446ц, reason: contains not printable characters */
    public static int m37b044604460446() {
        return 1;
    }

    /* renamed from: bццц04460446ц, reason: contains not printable characters */
    public static int m38b04460446() {
        return 47;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0013. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.immersion.hapticmediasdk.controllers.IHapticFileReader getHapticFileReaderInstance(java.lang.String r4, com.immersion.hapticmediasdk.utils.FileManager r5) {
        /*
            r3 = 0
            r0 = 0
            int r1 = a(r4, r5)     // Catch: java.lang.Error -> L20
            switch(r1) {
                case -1: goto L18;
                case 0: goto L9;
                case 1: goto L52;
                case 2: goto L42;
                case 3: goto L4a;
                default: goto L9;
            }     // Catch: java.lang.Error -> L20
        L9:
            java.lang.String r1 = "FileReaderFactory"
            java.lang.String r2 = "Unsupported HAPT file version"
            com.immersion.hapticmediasdk.utils.Log.e(r1, r2)     // Catch: java.lang.Error -> L20
        L10:
            switch(r3) {
                case 0: goto L17;
                case 1: goto L10;
                default: goto L13;
            }     // Catch: java.lang.Error -> L20
        L13:
            switch(r3) {
                case 0: goto L17;
                case 1: goto L10;
                default: goto L16;
            }     // Catch: java.lang.Error -> L20
        L16:
            goto L13
        L17:
            return r0
        L18:
            java.lang.String r1 = "FileReaderFactory"
            java.lang.String r2 = "Can't retrieve Major version! Not enough bytes available yet."
            com.immersion.hapticmediasdk.utils.Log.i(r1, r2)     // Catch: java.lang.Error -> L20
            goto L17
        L20:
            r1 = move-exception
            r1.printStackTrace()
            int r1 = com.immersion.hapticmediasdk.controllers.FileReaderFactory.f43b044604460446
            int r2 = com.immersion.hapticmediasdk.controllers.FileReaderFactory.f44b044604460446
            int r1 = r1 + r2
            int r2 = com.immersion.hapticmediasdk.controllers.FileReaderFactory.f43b044604460446
            int r1 = r1 * r2
            int r2 = com.immersion.hapticmediasdk.controllers.FileReaderFactory.f41b0446044604460446
            int r1 = r1 % r2
            int r2 = m35b044604460446()
            if (r1 == r2) goto L17
            int r1 = m38b04460446()
            com.immersion.hapticmediasdk.controllers.FileReaderFactory.f43b044604460446 = r1
            int r1 = m38b04460446()
            com.immersion.hapticmediasdk.controllers.FileReaderFactory.f44b044604460446 = r1
            goto L17
        L42:
            com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader r1 = new com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader     // Catch: java.lang.Error -> L20
            r2 = 2
            r1.<init>(r4, r5, r2)     // Catch: java.lang.Error -> L20
            r0 = r1
            goto L17
        L4a:
            com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader r1 = new com.immersion.hapticmediasdk.controllers.MemoryAlignedFileReader     // Catch: java.lang.Error -> L20
            r2 = 3
            r1.<init>(r4, r5, r2)     // Catch: java.lang.Error -> L20
            r0 = r1
            goto L17
        L52:
            com.immersion.hapticmediasdk.controllers.MemoryMappedFileReader r1 = new com.immersion.hapticmediasdk.controllers.MemoryMappedFileReader     // Catch: java.lang.Error -> L20
            r1.<init>(r4, r5)     // Catch: java.lang.Error -> L20
            r0 = r1
            goto L17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.immersion.hapticmediasdk.controllers.FileReaderFactory.getHapticFileReaderInstance(java.lang.String, com.immersion.hapticmediasdk.utils.FileManager):com.immersion.hapticmediasdk.controllers.IHapticFileReader");
    }
}
