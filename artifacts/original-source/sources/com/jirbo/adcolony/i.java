package com.jirbo.adcolony;

import java.io.File;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class i {
    i() {
    }

    public static boolean a(File file) {
        if (file.exists()) {
            File[] fileArrListFiles = file.listFiles();
            for (int i = 0; i < fileArrListFiles.length; i++) {
                if (fileArrListFiles[i].isDirectory()) {
                    a(fileArrListFiles[i]);
                } else {
                    fileArrListFiles[i].delete();
                }
            }
        }
        return file.delete();
    }
}
