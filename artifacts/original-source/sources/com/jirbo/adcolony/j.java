package com.jirbo.adcolony;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class j {
    d o;

    abstract void a();

    j(d dVar) {
        this(dVar, true);
    }

    j(d dVar, boolean z) {
        this.o = dVar;
        if (z) {
            dVar.a(this);
        }
    }
}
