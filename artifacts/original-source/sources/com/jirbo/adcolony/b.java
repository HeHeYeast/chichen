package com.jirbo.adcolony;

import android.os.Handler;
import android.os.Looper;
import c.NetworkManager;
import com.google.android.gcm.GCMConstants;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.ADCDownload;
import com.jirbo.adcolony.n;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class b implements ADCDownload.Listener {
    d a;
    boolean b;

    /* renamed from: c, reason: collision with root package name */
    boolean f217c;
    boolean d;
    double g;
    Handler h;
    Runnable i;
    boolean e = true;
    boolean f = false;
    n.e j = new n.e();

    b(d dVar) {
        this.a = dVar;
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        this.h = new Handler();
        this.i = new Runnable() { // from class: com.jirbo.adcolony.b.1
            @Override // java.lang.Runnable
            public void run() {
                b.this.e = true;
                if (b.this.f) {
                    b.this.e();
                }
            }
        };
    }

    void a() {
    }

    void b() {
        l.b.b((Object) "Attempting to load backup manifest from file.");
        f fVar = new f("manifest.txt");
        ADCData.g gVarB = k.b(fVar);
        if (gVarB != null) {
            if (a(gVarB)) {
                this.b = true;
                this.j.a();
            } else {
                l.b.b((Object) "Invalid manifest loaded.");
                fVar.c();
                this.b = false;
            }
        }
    }

    String c() {
        if (!this.b) {
            return null;
        }
        String str = null;
        for (int i = 0; i < this.j.n.a(); i++) {
            n.ab abVarA = this.j.n.a(i);
            if (abVarA.e()) {
                str = abVarA.a;
                if (abVarA.a()) {
                    return abVarA.a;
                }
            }
        }
        return str;
    }

    String d() {
        if (!this.b) {
            return null;
        }
        String str = null;
        for (int i = 0; i < this.j.n.a(); i++) {
            n.ab abVarA = this.j.n.a(i);
            if (abVarA.g()) {
                str = abVarA.a;
                if (abVarA.a()) {
                    return abVarA.a;
                }
            }
        }
        return str;
    }

    boolean a(String str) {
        return a(str, true);
    }

    boolean a(String str, boolean z) {
        for (int i = 0; i < this.j.n.a(); i++) {
            n.ab abVarA = this.j.n.a(i);
            if (abVarA.c(z) && abVarA.a.equals(str)) {
                return true;
            }
        }
        return false;
    }

    boolean b(String str) {
        return b(str, false);
    }

    boolean b(String str, boolean z) {
        return z ? c(str, z) : !this.b ? l.f226c.b("Ads are not ready to be played, as they are still downloading.") : z ? this.j.a(str, true, false) : this.j.a(str, false, true);
    }

    boolean c(String str, boolean z) {
        if (this.b) {
            return z ? this.j.a(str, true, false) : this.j.a(str, false, true);
        }
        return false;
    }

    void e() {
        if (this.e || a.z) {
            this.e = false;
            this.f217c = true;
            this.f = false;
            this.h.postDelayed(this.i, 60000L);
            return;
        }
        this.f = true;
    }

    void f() {
        if (ab.c() >= this.g) {
            this.f217c = true;
        }
        if (this.f217c) {
            this.f217c = false;
            if (g.c() >= 32) {
                this.g = ab.c() + 600.0d;
                g();
            }
        }
        if (!q.c()) {
            if (a.C) {
                a.h();
            }
            a.C = false;
        } else {
            if (!a.C) {
                a.h();
            }
            a.C = true;
        }
    }

    void g() {
        boolean z = true;
        a.r = true;
        l.b.b((Object) "Refreshing manifest");
        if (!q.c()) {
            l.b.b((Object) "Not connected to network.");
            l.a.a("attempted_load:").a(this.d).a(" is_configured:").b(this.b);
            if (!this.d) {
                this.d = true;
                if (!this.b) {
                    b();
                    return;
                }
                return;
            }
            return;
        }
        z zVar = new z();
        c cVar = this.a.a;
        zVar.a(c.f218c);
        zVar.a("?app_id=");
        zVar.a(this.a.a.j);
        zVar.a("&zones=");
        if (this.a.a.k != null) {
            for (String str : this.a.a.k) {
                if (z) {
                    z = false;
                } else {
                    zVar.a(",");
                }
                zVar.a(str);
            }
        }
        String str2 = a.l.e.j == null ? "" : a.l.e.j;
        String str3 = "" + a.l.e.i;
        zVar.a(this.a.a.h);
        zVar.a("&carrier=");
        zVar.a(q.a(this.a.a.w));
        zVar.a("&network_type=");
        if (q.a()) {
            zVar.a("wifi");
        } else if (q.b()) {
            zVar.a("cell");
        } else {
            zVar.a(NetworkManager.TYPE_NONE);
        }
        zVar.a("&custom_id=");
        zVar.a(q.a(this.a.a.x));
        zVar.a("&sid=");
        zVar.a(str2);
        zVar.a("&s_imp_count=");
        zVar.a(str3);
        l.b.b((Object) "Downloading ad manifest from");
        l.b.b(zVar);
        new ADCDownload(this.a, zVar.toString(), this).b();
    }

    @Override // com.jirbo.adcolony.ADCDownload.Listener
    public void on_download_finished(ADCDownload download) {
        a.r = true;
        if (download.i) {
            l.f226c.b((Object) "Finished downloading:");
            l.f226c.b((Object) download.f200c);
            ADCData.g gVarB = k.b(download.n);
            if (gVarB == null) {
                l.a.b((Object) "Invalid JSON in manifest.  Raw data:");
                l.a.b((Object) download.n);
                return;
            }
            if (a(gVarB)) {
                l.b.b((Object) "Ad manifest updated.");
                new f("manifest.txt").a(download.n);
                this.b = true;
                this.j.a();
                if (this.j.i == null || this.j.i.equals("")) {
                    this.j.i = "all";
                }
                if (this.j.j == null || this.j.j.equals("")) {
                    this.j.j = "all";
                }
                a.h();
                return;
            }
            l.b.b((Object) "Invalid manifest.");
            return;
        }
        l.f226c.b((Object) "Error downloading:");
        l.f226c.b((Object) download.f200c);
    }

    boolean a(ADCData.g gVar) {
        if (gVar == null || !gVar.e("status").equals("success") || !this.j.a(gVar.b(GCMConstants.EXTRA_APPLICATION_PENDING_INTENT))) {
            return false;
        }
        l.a.b((Object) "Finished parsing manifest");
        if (!this.j.h.equalsIgnoreCase(NetworkManager.TYPE_NONE)) {
            l.f226c.b((Object) "Enabling debug logging.");
            a.a(1);
        } else {
            a.a(2);
        }
        return true;
    }
}
