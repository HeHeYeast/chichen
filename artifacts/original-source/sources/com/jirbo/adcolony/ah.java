package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ah {
    d a;
    boolean b = false;

    /* renamed from: c, reason: collision with root package name */
    ArrayList<ag> f216c = new ArrayList<>();

    ah(d dVar) {
        this.a = dVar;
    }

    void a() {
        ADCData.c cVarC = k.c(new f("zone_state.txt"));
        if (cVarC != null) {
            this.f216c.clear();
            for (int i = 0; i < cVarC.i(); i++) {
                ADCData.g gVarB = cVarC.b(i);
                ag agVar = new ag();
                if (agVar.a(gVarB)) {
                    this.f216c.add(agVar);
                }
            }
        }
        for (String str : this.a.a.k) {
            a(str);
        }
    }

    void b() {
        l.a.b((Object) "Saving zone state...");
        this.b = false;
        ADCData.c cVar = new ADCData.c();
        for (String str : this.a.a.k) {
            cVar.a(a(str).a());
        }
        k.a(new f("zone_state.txt"), cVar);
        l.a.b((Object) "Saved zone state");
    }

    int c() {
        return this.f216c.size();
    }

    ag a(int i) {
        return this.f216c.get(i);
    }

    ag a(String str) {
        int size = this.f216c.size();
        for (int i = 0; i < size; i++) {
            ag agVar = this.f216c.get(i);
            if (agVar.a.equals(str)) {
                return agVar;
            }
        }
        this.b = true;
        ag agVar2 = new ag(str);
        this.f216c.add(agVar2);
        return agVar2;
    }

    void d() {
        if (this.b) {
            b();
        }
    }
}
