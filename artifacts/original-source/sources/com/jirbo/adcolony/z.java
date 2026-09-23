package com.jirbo.adcolony;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class z extends af {
    StringBuilder a = new StringBuilder();

    z() {
    }

    void a() {
        this.a.setLength(0);
        this.i = 0;
    }

    @Override // com.jirbo.adcolony.af
    void a(char c2) {
        this.a.append(c2);
    }

    public String toString() {
        return this.a.toString();
    }

    public static void a(String[] strArr) {
        z zVar = new z();
        zVar.b("A king who was mad at the time");
        zVar.b("Declared limerick writing a crime");
        zVar.i += 2;
        zVar.b("So late in the night");
        zVar.b("All the poets would write");
        zVar.i -= 2;
        zVar.b("Verses without any rhyme or meter");
        zVar.d();
        zVar.i += 4;
        zVar.b("David\nGerrold");
        zVar.i += 2;
        zVar.b(4.0d);
        zVar.i += 2;
        zVar.b(0.0d);
        zVar.i += 2;
        zVar.b(-100023.0d);
        zVar.i += 2;
        zVar.c(-6L);
        zVar.i += 2;
        zVar.c(0L);
        zVar.i += 2;
        zVar.c(234L);
        zVar.i += 2;
        zVar.c(Long.MIN_VALUE);
        zVar.i += 2;
        zVar.b(true);
        zVar.i += 2;
        zVar.b(false);
        zVar.i += 2;
        System.out.println(zVar);
    }
}
