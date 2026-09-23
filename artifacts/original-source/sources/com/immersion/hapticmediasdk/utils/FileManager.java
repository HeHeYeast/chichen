package com.immersion.hapticmediasdk.utils;

import android.content.Context;
import android.os.Process;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FileManager {
    public static final String HAPTIC_STORAGE_FILENAME = "dat.hapt";
    public static final String TAG = "FileManager";

    /* renamed from: b0427042704270427Ч0427, reason: contains not printable characters */
    public static int f92b04270427042704270427 = 75;

    /* renamed from: b0427Ч04270427Ч0427, reason: contains not printable characters */
    public static int f93b0427042704270427 = 1;

    /* renamed from: bЧ042704270427Ч0427, reason: contains not printable characters */
    public static int f94b0427042704270427 = 2;

    /* renamed from: bЧЧЧЧ04270427, reason: contains not printable characters */
    public static int f95b04270427;
    Context a;
    private int b;

    public FileManager(Context context) {
        boolean z = false;
        int i = 3;
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
        while (true) {
            try {
                i /= 0;
            } catch (Exception e) {
                this.b = 0;
                this.a = context;
                this.b = Process.myTid();
                return;
            }
        }
    }

    /* renamed from: b0427ЧЧЧ04270427, reason: contains not printable characters */
    public static int m118b042704270427() {
        return 0;
    }

    /* renamed from: bЧ0427ЧЧ04270427, reason: contains not printable characters */
    public static int m119b042704270427() {
        return 2;
    }

    /* renamed from: bЧЧ04270427Ч0427, reason: contains not printable characters */
    public static int m120b042704270427() {
        return 28;
    }

    public void closeCloseable(Closeable closeable) throws Exception {
        if (((m120b042704270427() + f93b0427042704270427) * m120b042704270427()) % m119b042704270427() != f95b04270427) {
            f92b04270427042704270427 = m120b042704270427();
            f95b04270427 = m120b042704270427();
        }
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    throw e2;
                }
            } catch (Exception e3) {
                throw e3;
            }
        }
    }

    public void deleteHapticStorage() throws Exception {
        try {
            File[] fileArrListFiles = new File(getInternalHapticPath()).listFiles();
            if (fileArrListFiles != null) {
                int iM120b042704270427 = m120b042704270427();
                switch ((iM120b042704270427 * (f93b0427042704270427 + iM120b042704270427)) % f94b0427042704270427) {
                    case 0:
                        break;
                    default:
                        f93b0427042704270427 = 63;
                        break;
                }
                for (File file : fileArrListFiles) {
                    try {
                        if (file.getName().endsWith(this.b + HAPTIC_STORAGE_FILENAME)) {
                            file.delete();
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public File getHapticStorageFile(String str) throws Exception {
        try {
            try {
                String path = this.a.getFilesDir().getPath();
                StringBuilder sbAppend = new StringBuilder().append(getUniqueFileName(str));
                if (((f92b04270427042704270427 + f93b0427042704270427) * f92b04270427042704270427) % f94b0427042704270427 != m118b042704270427()) {
                    f92b04270427042704270427 = m120b042704270427();
                    f95b04270427 = m120b042704270427();
                }
                return new File(path, sbAppend.append(HAPTIC_STORAGE_FILENAME).toString());
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e2) {
            throw e2;
        }
    }

    public String getInternalHapticPath() {
        File filesDir = this.a.getFilesDir();
        int iM120b042704270427 = m120b042704270427();
        switch ((iM120b042704270427 * (f93b0427042704270427 + iM120b042704270427)) % f94b0427042704270427) {
            case 0:
                break;
            default:
                f93b0427042704270427 = 65;
                break;
        }
        return filesDir.getAbsolutePath();
    }

    public String getUniqueFileName(String str) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(), 0, str.length());
            Object[] objArr = new Object[2];
            objArr[0] = new BigInteger(1, messageDigest.digest());
            if (((f92b04270427042704270427 + f93b0427042704270427) * f92b04270427042704270427) % f94b0427042704270427 != f95b04270427) {
                f92b04270427042704270427 = m120b042704270427();
                f95b04270427 = 59;
            }
            objArr[1] = Integer.valueOf(this.b);
            return String.format("%032x_%d", objArr);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                return null;
            } catch (Exception e2) {
                throw e2;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x005c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.io.BufferedOutputStream makeOutputStream(java.lang.String r9) throws java.io.IOException {
        /*
            r8 = this;
            r0 = 0
            r7 = 0
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r1]
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L6a
            r4.<init>(r9)     // Catch: java.lang.Exception -> L6a
            java.lang.String r1 = new java.lang.String     // Catch: java.lang.Exception -> L6a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L6a
            r2.<init>()     // Catch: java.lang.Exception -> L6a
            int r5 = com.immersion.hapticmediasdk.utils.FileManager.f92b04270427042704270427
            int r6 = com.immersion.hapticmediasdk.utils.FileManager.f93b0427042704270427
            int r6 = r6 + r5
            int r5 = r5 * r6
            int r6 = com.immersion.hapticmediasdk.utils.FileManager.f94b0427042704270427
            int r5 = r5 % r6
            switch(r5) {
                case 0: goto L28;
                default: goto L1e;
            }
        L1e:
            r5 = 73
            com.immersion.hapticmediasdk.utils.FileManager.f92b04270427042704270427 = r5
            int r5 = m120b042704270427()
            com.immersion.hapticmediasdk.utils.FileManager.f95b04270427 = r5
        L28:
            java.lang.String r5 = r8.getUniqueFileName(r9)     // Catch: java.lang.Exception -> L6a
            java.lang.StringBuilder r2 = r2.append(r5)     // Catch: java.lang.Exception -> L6a
            java.lang.String r5 = "dat.hapt"
            java.lang.StringBuilder r2 = r2.append(r5)     // Catch: java.lang.Exception -> L6a
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L6a
            r1.<init>(r2)     // Catch: java.lang.Exception -> L6a
            android.content.Context r2 = r8.a     // Catch: java.lang.Exception -> L6a
            r5 = 0
            java.io.FileOutputStream r2 = r2.openFileOutput(r1, r5)     // Catch: java.lang.Exception -> L6a
            int r1 = r4.available()     // Catch: java.lang.Exception -> L70
        L48:
            if (r1 <= 0) goto L57
            int r1 = r4.read(r3)     // Catch: java.lang.Exception -> L70
            r5 = 0
            r2.write(r3, r5, r1)     // Catch: java.lang.Exception -> L70
            int r1 = r4.available()     // Catch: java.lang.Exception -> L70
            goto L48
        L57:
            r4.close()     // Catch: java.lang.Exception -> L70
        L5a:
            if (r2 == 0) goto L69
            java.io.BufferedOutputStream r0 = new java.io.BufferedOutputStream
        L5e:
            r1 = 1
            switch(r1) {
                case 0: goto L5e;
                case 1: goto L66;
                default: goto L62;
            }
        L62:
            switch(r7) {
                case 0: goto L66;
                case 1: goto L5e;
                default: goto L65;
            }
        L65:
            goto L62
        L66:
            r0.<init>(r2)
        L69:
            return r0
        L6a:
            r1 = move-exception
            r2 = r0
        L6c:
            r1.printStackTrace()
            goto L5a
        L70:
            r1 = move-exception
            goto L6c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.immersion.hapticmediasdk.utils.FileManager.makeOutputStream(java.lang.String):java.io.BufferedOutputStream");
    }

    public BufferedOutputStream makeOutputStreamForStreaming(String str) throws Exception {
        try {
            try {
                FileOutputStream fileOutputStreamOpenFileOutput = this.a.openFileOutput(getUniqueFileName(str) + HAPTIC_STORAGE_FILENAME, 0);
                if (((f92b04270427042704270427 + f93b0427042704270427) * f92b04270427042704270427) % f94b0427042704270427 != f95b04270427) {
                    f92b04270427042704270427 = 23;
                    f95b04270427 = m120b042704270427();
                }
                return new BufferedOutputStream(fileOutputStreamOpenFileOutput);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e2) {
                throw e2;
            }
        } catch (Exception e3) {
            throw e3;
        }
    }
}
