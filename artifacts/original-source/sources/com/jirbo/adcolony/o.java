package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.ADCDownload;
import com.jirbo.adcolony.ab;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class o implements ADCDownload.Listener {
    d a;
    int f;
    boolean h;
    boolean i;
    double j;
    ArrayList<a> b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    HashMap<String, a> f255c = new HashMap<>();
    int d = 1;
    ab.b e = new ab.b(2.0d);
    ArrayList<String> g = new ArrayList<>();

    o(d dVar) {
        this.a = dVar;
    }

    void a() {
        b();
        this.h = true;
    }

    void b() {
        l.a.b((Object) "Loading media info");
        ADCData.g gVarB = k.b(new f("media_info.txt"));
        if (gVarB == null) {
            gVarB = new ADCData.g();
            l.a.b((Object) "No saved media info exists.");
        } else {
            l.a.b((Object) "Loaded media info");
        }
        this.d = gVarB.g("next_file_number");
        if (this.d <= 0) {
            this.d = 1;
        }
        ADCData.c cVarC = gVarB.c("assets");
        if (cVarC != null) {
            this.b.clear();
            for (int i = 0; i < cVarC.i(); i++) {
                ADCData.g gVarB2 = cVarC.b(i);
                a aVar = new a();
                aVar.a = gVarB2.e("url");
                aVar.b = gVarB2.e("filepath");
                aVar.f256c = gVarB2.e("last_modified");
                aVar.f = gVarB2.g("file_number");
                aVar.g = gVarB2.g("size");
                aVar.e = gVarB2.h("ready");
                aVar.h = gVarB2.f("last_accessed");
                if (aVar.f > this.d) {
                    this.d = aVar.f + 1;
                }
                this.b.add(aVar);
            }
        }
        c();
    }

    void c() {
        HashMap map = new HashMap();
        String str = this.a.f.f202c;
        String[] list = new File(str).list();
        String[] strArr = list == null ? new String[0] : list;
        for (String str2 : strArr) {
            String str3 = str + str2;
            map.put(str3, str3);
        }
        HashMap map2 = new HashMap();
        this.j = 0.0d;
        ArrayList<a> arrayList = new ArrayList<>();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.b.size()) {
                break;
            }
            a aVar = this.b.get(i2);
            if (!aVar.d && aVar.e) {
                String str4 = aVar.b;
                if (map.containsKey(str4) && new File(str4).length() == aVar.g) {
                    this.j += aVar.g;
                    arrayList.add(aVar);
                    map2.put(str4, str4);
                }
            }
            i = i2 + 1;
        }
        this.b = arrayList;
        for (String str5 : strArr) {
            String str6 = str + str5;
            if (!map2.containsKey(str6)) {
                l.b.a("Deleting unused media ").b((Object) str6);
                new File(str6).delete();
            }
        }
        this.f255c.clear();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= this.b.size()) {
                break;
            }
            a aVar2 = this.b.get(i4);
            this.f255c.put(aVar2.a, aVar2);
            i3 = i4 + 1;
        }
        double d = this.a.b.j.g;
        if (d > 0.0d) {
            l.b.a("Media pool at ").a(this.j / 1048576.0d).a("/").a(d / 1048576.0d).b((Object) " MB");
        }
    }

    void d() {
        l.a.b((Object) "Saving media info");
        ADCData.c cVar = new ADCData.c();
        for (int i = 0; i < this.b.size(); i++) {
            a aVar = this.b.get(i);
            if (aVar.e && !aVar.d) {
                ADCData.g gVar = new ADCData.g();
                gVar.b("url", aVar.a);
                gVar.b("filepath", aVar.b);
                gVar.b("last_modified", aVar.f256c);
                gVar.b("file_number", aVar.f);
                gVar.b("size", aVar.g);
                gVar.b("ready", aVar.e);
                gVar.b("last_accessed", aVar.h);
                cVar.a(gVar);
            }
        }
        ADCData.g gVar2 = new ADCData.g();
        gVar2.b("next_file_number", this.d);
        gVar2.a("assets", (ADCData.i) cVar);
        k.a(new f("media_info.txt"), gVar2);
        this.i = false;
    }

    boolean a(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        a aVar = this.f255c.get(str);
        if (aVar == null) {
            this.a.b.j.a();
            return false;
        }
        if (aVar.e) {
            if (aVar.d) {
                return false;
            }
            aVar.h = ab.c();
            return true;
        }
        if (!aVar.d) {
            this.a.b.j.a();
        }
        return false;
    }

    String b(String str) {
        a aVar = this.f255c.get(str);
        if (aVar == null || aVar.b == null) {
            return "(file not found)";
        }
        aVar.h = ab.c();
        this.i = true;
        this.e.a(2.0d);
        return aVar.b;
    }

    void a(String str, String str2) {
        if (str != null && !str.equals("")) {
            if (str2 == null) {
                str2 = "";
            }
            a aVar = this.f255c.get(str);
            if (aVar != null) {
                aVar.h = ab.c();
                if (aVar.f256c.equals(str2) && (aVar.e || aVar.d)) {
                    return;
                }
            } else {
                aVar = new a();
                aVar.a = str;
                this.b.add(aVar);
                aVar.h = ab.c();
                this.f255c.put(str, aVar);
            }
            if (aVar.f == 0) {
                int iH = h();
                String str3 = this.a.f.f202c + a(str, iH);
                aVar.f = iH;
                aVar.b = str3;
            }
            aVar.f256c = str2;
            aVar.d = true;
            aVar.e = false;
            l.a.a("Adding ").a(str).b((Object) " to pending downloads.");
            this.g.add(str);
            this.i = true;
            this.e.a(2.0d);
            com.jirbo.adcolony.a.r = true;
        }
    }

    void e() {
        f();
        if (this.i && this.e.a()) {
            g();
            d();
        }
    }

    void f() {
        if (this.a.b.j.j.equals("wifi") && !q.a()) {
            l.a.b((Object) "Skipping asset download due to CACHE_FILTER_WIFI");
            return;
        }
        if (this.a.b.j.j.equals("cell") && !q.b()) {
            l.a.b((Object) "Skipping asset download due to CACHE_FILTER_CELL.");
            return;
        }
        while (this.f < 3 && this.g.size() > 0) {
            String strRemove = this.g.remove(0);
            a aVar = this.f255c.get(strRemove);
            if (aVar != null && (strRemove == null || strRemove.equals(""))) {
                l.d.b((Object) "[ADC ERROR] - NULL URL");
                new RuntimeException().printStackTrace();
            }
            if (aVar != null && strRemove != null && !strRemove.equals("")) {
                com.jirbo.adcolony.a.r = true;
                this.f++;
                new ADCDownload(this.a, strRemove, this, aVar.b).a(aVar).b();
            }
        }
    }

    @Override // com.jirbo.adcolony.ADCDownload.Listener
    public void on_download_finished(ADCDownload download) {
        a aVar = (a) download.e;
        this.f--;
        this.i = true;
        this.e.a(2.0d);
        aVar.e = download.i;
        aVar.d = false;
        if (download.i) {
            aVar.g = download.m;
            this.j += aVar.g;
            l.a.a("Downloaded ").b((Object) aVar.a);
        }
        com.jirbo.adcolony.a.h();
        f();
    }

    void g() {
        double d = this.a.b.j.g;
        if (d == 0.0d) {
            return;
        }
        while (this.j > this.a.b.j.g) {
            int i = 0;
            a aVar = null;
            while (i < this.b.size()) {
                a aVar2 = this.b.get(i);
                if (!aVar2.e || (aVar != null && aVar2.h >= aVar.h)) {
                    aVar2 = aVar;
                }
                i++;
                aVar = aVar2;
            }
            if (aVar != null && aVar.b != null) {
                l.b.a("Deleting ").b((Object) aVar.b);
                aVar.e = false;
                new File(aVar.b).delete();
                aVar.b = null;
                this.j -= aVar.g;
                l.b.a("Media pool now at ").a(this.j / 1048576.0d).a("/").a(d / 1048576.0d).b((Object) " MB");
                this.i = true;
                this.e.a(2.0d);
            } else {
                return;
            }
        }
    }

    int h() {
        this.i = true;
        this.e.a(2.0d);
        int i = this.d;
        this.d = i + 1;
        return i;
    }

    String a(String str, int i) {
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf == -1) {
            return i + "";
        }
        String strSubstring = str.substring(iLastIndexOf);
        if (strSubstring.contains("/")) {
            strSubstring = ".0";
        }
        return i + strSubstring;
    }

    static class a {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f256c;
        boolean d;
        boolean e;
        int f;
        int g;
        double h;

        a() {
        }
    }
}
