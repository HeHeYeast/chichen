package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class t {
    d a;
    ArrayList<a> b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    HashMap<Integer, Integer> f260c = new HashMap<>();
    HashMap<String, Integer> d = new HashMap<>();
    boolean e = false;
    boolean f = false;

    t(d dVar) {
        this.a = dVar;
    }

    void a() {
        b();
        this.e = false;
    }

    void a(String str, int i) {
        l.a.b((Object) "Adding play event to play history");
        this.e = true;
        this.b.add(new a(this.a.e.j, ab.c(), str, i));
        Integer num = this.f260c.get(Integer.valueOf(i));
        l.a.a("Got play count of ").a(num).b((Object) " for this ad");
        if (num == null) {
            this.f260c.put(Integer.valueOf(i), 1);
        } else {
            this.f260c.put(Integer.valueOf(i), Integer.valueOf(num.intValue() + 1));
        }
    }

    synchronized int a(String str) {
        int i;
        int i2 = 0;
        synchronized (this) {
            String str2 = this.a.e.j;
            int size = this.b.size() - 1;
            boolean z = false;
            while (size >= 0) {
                if (this.b.get(size) == null || this.b.get(size).f261c == null) {
                    break;
                }
                if (this.b.get(size).f261c.equals(str2)) {
                    z = true;
                    if (this.b.get(size).a.equals(str)) {
                        i = i2 + 1;
                    }
                    size--;
                    z = z;
                    i2 = i;
                } else if (z) {
                    break;
                }
                i = i2;
                size--;
                z = z;
                i2 = i;
            }
        }
        return i2;
    }

    synchronized int a(String str, double d) {
        int i;
        double dC = ab.c() - d;
        i = 0;
        int size = this.b.size() - 1;
        while (size >= 0) {
            if (this.b.get(size).d < dC) {
                break;
            }
            int i2 = str.equals(this.b.get(size).a) ? i + 1 : i;
            size--;
            i = i2;
        }
        return i;
    }

    int b(String str) {
        return a(str, 86400.0d);
    }

    synchronized int a(int i, double d) {
        int i2;
        double dC = ab.c() - d;
        i2 = 0;
        int size = this.b.size() - 1;
        while (size >= 0) {
            if (this.b.get(size).d < dC) {
                break;
            }
            int i3 = i == this.b.get(size).b ? i2 + 1 : i2;
            size--;
            i2 = i3;
        }
        return i2;
    }

    int c(String str) {
        Integer num = this.d.get(str);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    void b(String str, int i) {
        this.d.put(str, Integer.valueOf(i));
        this.e = true;
    }

    int a(int i) {
        return a(i, 86400.0d);
    }

    int b(int i) {
        return a(i, 604800.0d);
    }

    int c(int i) {
        return a(i, 2628000.0d);
    }

    int d(int i) {
        return a(i, 1.5768E7d);
    }

    void b() throws NumberFormatException {
        ADCData.g gVarB;
        com.jirbo.adcolony.a.r = true;
        if ((!this.f || this.b.get(this.b.size() - 1).f261c != this.a.e.j) && (gVarB = k.b(new f("play_history_info.txt"))) != null) {
            this.b.clear();
            this.d = new HashMap<>();
            ADCData.g gVarB2 = gVarB.b("reward_credit");
            for (int i = 0; i < gVarB2.o(); i++) {
                String strA = gVarB2.a(i);
                this.d.put(strA, Integer.valueOf(gVarB2.g(strA)));
            }
            ADCData.c cVarC = gVarB.c("play_events");
            for (int i2 = 0; i2 < cVarC.i(); i2++) {
                ADCData.g gVarB3 = cVarC.b(i2);
                double dF = gVarB3.f("timestamp");
                String strE = gVarB3.e("zone_id");
                int iG = gVarB3.g("ad_id");
                if (dF != 0.0d && strE != null && iG != 0) {
                    this.b.add(new a(null, dF, strE, iG));
                }
            }
            this.f260c = new HashMap<>();
            ADCData.g gVarB4 = gVarB.b("play_counts");
            for (int i3 = 0; i3 < gVarB4.o(); i3++) {
                int i4 = Integer.parseInt(gVarB4.a(i3));
                this.f260c.put(Integer.valueOf(i4), Integer.valueOf(gVarB4.g("" + i4)));
            }
            this.f = true;
        }
    }

    void c() {
        ADCData.c cVar = new ADCData.c();
        ADCData.g gVar = new ADCData.g();
        ADCData.g gVar2 = new ADCData.g();
        double dC = ab.c() - 2678400.0d;
        for (int size = this.b.size() - 1; size >= 0; size--) {
            a aVar = this.b.get(size);
            if (aVar.d < dC) {
                break;
            }
            ADCData.g gVar3 = new ADCData.g();
            gVar3.b("zone_id", aVar.a);
            gVar3.b("ad_id", aVar.b);
            gVar3.b("timestamp", aVar.d);
            cVar.a(gVar3);
        }
        gVar.a("play_events", (ADCData.i) cVar);
        Iterator<Integer> it = this.f260c.keySet().iterator();
        while (it.hasNext()) {
            int iIntValue = it.next().intValue();
            gVar2.b("" + iIntValue, this.f260c.get(Integer.valueOf(iIntValue)).intValue());
        }
        gVar.a("play_counts", (ADCData.i) gVar2);
        ADCData.g gVar4 = new ADCData.g();
        if (this.d.size() > 0) {
            for (String str : this.d.keySet()) {
                gVar4.b(str, this.d.get(str).intValue());
            }
        }
        gVar.a("reward_credit", (ADCData.i) gVar4);
        l.a.a("Saving play history");
        k.a(new f("play_history_info.txt"), gVar);
    }

    void d() {
        if (this.e) {
            this.e = false;
            c();
        }
    }

    static class a {
        String a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        String f261c;
        double d;

        a(String str, double d, String str2, int i) {
            this.f261c = str;
            this.d = d;
            this.a = str2;
            this.b = i;
        }
    }
}
