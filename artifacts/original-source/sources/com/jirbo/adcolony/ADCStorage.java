package com.jirbo.adcolony;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADCStorage {
    d a;
    String b;

    /* renamed from: c, reason: collision with root package name */
    String f202c;
    String d;
    File e;
    File f;

    ADCStorage(d controller) {
        this.a = controller;
    }

    void a() {
        l.a.b((Object) "Configuring storage");
        boolean z = true;
        if (d() != null && a(d()) > a(c()) + 1048576.0d && a(c()) < 3.145728E7d) {
            z = false;
        }
        if (z) {
            l.b.b((Object) "Using internal storage:");
            this.b = c() + "/adc/";
        } else {
            this.b = d() + "/.adc2/" + ab.f() + "/";
            l.b.b((Object) "Using external storage:");
        }
        this.f202c = this.b + "media/";
        l.a.b((Object) this.f202c);
        this.e = new File(this.f202c);
        if (!this.e.isDirectory()) {
            this.e.delete();
            this.e.mkdirs();
        }
        if (!this.e.isDirectory()) {
            a.a("Cannot create media folder.");
            return;
        }
        if (a(this.f202c) < 2.097152E7d) {
            a.a("Not enough space to store temporary files (" + a(this.f202c) + " bytes available).");
            return;
        }
        this.d = c() + "/adc/data/";
        if (a.n == 0) {
            this.d = this.b + "data/";
        }
        l.a.a("Internal data path: ").b((Object) this.d);
        this.f = new File(this.d);
        if (!this.f.isDirectory()) {
            this.f.delete();
        }
        this.f.mkdirs();
        f fVar = new f("iap_cache.txt");
        fVar.c();
        k.a(fVar, a.Z);
    }

    void b() {
        if (this.e != null && this.f != null) {
            if (!this.e.isDirectory()) {
                this.e.delete();
            }
            if (!this.f.isDirectory()) {
                this.f.delete();
            }
            this.e.mkdirs();
            this.f.mkdirs();
        }
    }

    String c() {
        return AdColony.activity().getFilesDir().getAbsolutePath();
    }

    String d() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    double a(String str) {
        try {
            StatFs statFs = new StatFs(str);
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (RuntimeException e) {
            return 0.0d;
        }
    }
}
