package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;
import java.io.Serializable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ag implements Serializable {
    String a;
    int b;

    /* renamed from: c, reason: collision with root package name */
    int f215c;
    int d;

    ag() {
        this.a = "";
    }

    ag(String str) {
        this.a = "";
        this.a = str;
    }

    boolean a(ADCData.g gVar) {
        if (gVar == null) {
            return false;
        }
        this.a = gVar.a("uuid", "error");
        this.b = gVar.g("skipped_plays");
        this.f215c = gVar.g("play_order_index");
        return true;
    }

    ADCData.g a() {
        ADCData.g gVar = new ADCData.g();
        gVar.b("uuid", this.a);
        gVar.b("skipped_plays", this.b);
        gVar.b("play_order_index", this.f215c);
        return gVar;
    }
}
