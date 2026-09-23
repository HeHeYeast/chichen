package com.jirbo.adcolony;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class aj {
    private static String a = null;
    private static final String b = "INSTALLATION";

    aj() {
    }

    public static synchronized String a(Context context) {
        if (a == null) {
            File file = new File(context.getFilesDir(), b);
            try {
                if (!file.exists()) {
                    b(file);
                }
                a = a(file);
            } catch (Exception e) {
                throw new AdColonyException(e.toString());
            }
        }
        return a;
    }

    private static String a(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] bArr = new byte[(int) randomAccessFile.length()];
        randomAccessFile.readFully(bArr);
        randomAccessFile.close();
        return new String(bArr);
    }

    private static void b(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(UUID.randomUUID().toString().getBytes());
        fileOutputStream.close();
    }
}
