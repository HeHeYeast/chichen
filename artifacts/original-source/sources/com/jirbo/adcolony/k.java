package com.jirbo.adcolony;

import com.jirbo.adcolony.ADCData;
import java.io.IOException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class k {
    static z a = new z();

    k() {
    }

    static void a(f fVar, ADCData.i iVar) throws IOException {
        y yVarA = fVar.a();
        if (iVar == null) {
            yVarA.b("null");
        } else {
            iVar.a(yVarA);
            yVarA.d();
        }
        yVarA.b();
    }

    static void a(f fVar, ADCData.g gVar) {
        y yVarA = fVar.a();
        if (gVar != null) {
            gVar.a(yVarA);
            yVarA.d();
        } else {
            l.b.b((Object) "Saving empty property table.");
            yVarA.b("{}");
        }
        yVarA.b();
    }

    static void a(f fVar, ADCData.c cVar) {
        y yVarA = fVar.a();
        if (cVar != null) {
            cVar.a(yVarA);
            yVarA.d();
        } else {
            l.b.b((Object) "Saving empty property list.");
            yVarA.b("[]");
        }
        yVarA.b();
    }

    static ADCData.i a(f fVar) {
        s sVarB = fVar.b();
        if (sVarB == null) {
            return null;
        }
        return a(sVarB);
    }

    static ADCData.g b(f fVar) {
        ADCData.i iVarA = a(fVar);
        if (iVarA == null || !iVarA.m()) {
            return null;
        }
        return iVarA.n();
    }

    static ADCData.c c(f fVar) {
        ADCData.i iVarA = a(fVar);
        if (iVarA == null || !iVarA.f()) {
            return null;
        }
        return iVarA.h();
    }

    static ADCData.i a(String str) {
        if (str == null) {
            return null;
        }
        return a(new s(str));
    }

    static ADCData.g b(String str) {
        ADCData.i iVarA = a(str);
        if (iVarA == null || !iVarA.m()) {
            return null;
        }
        return iVarA.n();
    }

    static ADCData.c c(String str) {
        ADCData.i iVarA = a(str);
        if (iVarA == null || !iVarA.f()) {
            return null;
        }
        return iVarA.h();
    }

    static ADCData.i a(s sVar) {
        ADCData.i fVar;
        try {
            b(sVar);
            char cB = sVar.b();
            if (cB == '{') {
                fVar = c(sVar);
            } else if (cB == '[') {
                fVar = d(sVar);
            } else if (cB == '-') {
                fVar = h(sVar);
            } else if (cB >= '0' && cB <= '9') {
                fVar = h(sVar);
            } else if (cB == '\"' || cB == '\'') {
                String strE = e(sVar);
                if (strE.length() == 0) {
                    fVar = new ADCData.f("");
                } else {
                    char cCharAt = strE.charAt(0);
                    if (cCharAt == 't' && strE.equals("true")) {
                        fVar = ADCData.a;
                    } else if (cCharAt == 'f' && strE.equals("false")) {
                        fVar = ADCData.b;
                    } else {
                        fVar = (cCharAt == 'n' && strE.equals("null")) ? ADCData.f199c : new ADCData.f(strE);
                    }
                }
            } else if ((cB < 'a' || cB > 'z') && !((cB >= 'A' && cB <= 'Z') || cB == '_' || cB == '$')) {
                fVar = null;
            } else {
                String strG = g(sVar);
                if (strG.length() == 0) {
                    fVar = new ADCData.f("");
                } else {
                    char cCharAt2 = strG.charAt(0);
                    if (cCharAt2 == 't' && strG.equals("true")) {
                        fVar = ADCData.a;
                    } else if (cCharAt2 == 'f' && strG.equals("false")) {
                        fVar = ADCData.b;
                    } else {
                        fVar = (cCharAt2 == 'n' && strG.equals("null")) ? ADCData.f199c : new ADCData.f(strG);
                    }
                }
            }
            return fVar;
        } catch (RuntimeException e) {
            return null;
        }
    }

    static void b(s sVar) {
        char cB = sVar.b();
        while (sVar.a()) {
            if (cB <= ' ' || cB > '~') {
                sVar.c();
                cB = sVar.b();
            } else {
                return;
            }
        }
    }

    static ADCData.g c(s sVar) {
        b(sVar);
        if (!sVar.a('{')) {
            return null;
        }
        b(sVar);
        ADCData.g gVar = new ADCData.g();
        if (sVar.a('}')) {
            return gVar;
        }
        boolean z = true;
        while (true) {
            if (!z && !sVar.a(',')) {
                break;
            }
            z = false;
            String strG = g(sVar);
            b(sVar);
            if (!sVar.a(':')) {
                gVar.b(strG, true);
            } else {
                b(sVar);
                gVar.a(strG, a(sVar));
            }
            b(sVar);
        }
        if (sVar.a('}')) {
            return gVar;
        }
        return null;
    }

    static ADCData.c d(s sVar) {
        b(sVar);
        if (!sVar.a('[')) {
            return null;
        }
        b(sVar);
        ADCData.c cVar = new ADCData.c();
        if (sVar.a(']')) {
            return cVar;
        }
        boolean z = true;
        while (true) {
            if (!z && !sVar.a(',')) {
                break;
            }
            z = false;
            cVar.a(a(sVar));
            b(sVar);
        }
        if (sVar.a(']')) {
            return cVar;
        }
        return null;
    }

    static String e(s sVar) {
        char c2 = '\"';
        b(sVar);
        if (!sVar.a('\"') && sVar.a('\'')) {
            c2 = '\'';
        }
        if (!sVar.a()) {
            return "";
        }
        a.a();
        char c3 = sVar.c();
        while (sVar.a() && c3 != c2) {
            if (c3 == '\\') {
                char c4 = sVar.c();
                if (c4 == 'b') {
                    a.b('\b');
                } else if (c4 == 'f') {
                    a.b('\f');
                } else if (c4 == 'n') {
                    a.b('\n');
                } else if (c4 == 'r') {
                    a.b('\r');
                } else if (c4 == 't') {
                    a.b('\t');
                } else if (c4 == 'u') {
                    a.b(f(sVar));
                } else {
                    a.b(c4);
                }
            } else {
                a.b(c3);
            }
            c3 = sVar.c();
        }
        return a.toString();
    }

    static int a(int i) {
        if (i >= 48 && i <= 57) {
            return i - 48;
        }
        if (i >= 97 && i <= 102) {
            return (i - 97) + 10;
        }
        if (i < 65 || i > 70) {
            return 0;
        }
        return (i - 65) + 10;
    }

    static char f(s sVar) {
        int iA = 0;
        for (int i = 0; i < 4; i++) {
            if (sVar.a()) {
                iA = (iA << 4) | a(sVar.c());
            }
        }
        return (char) iA;
    }

    static String g(s sVar) {
        b(sVar);
        char cB = sVar.b();
        if (cB == '\"' || cB == '\'') {
            return e(sVar);
        }
        a.a();
        boolean z = false;
        while (!z && sVar.a()) {
            if ((cB >= 'a' && cB <= 'z') || ((cB >= 'A' && cB <= 'Z') || cB == '_' || cB == '$')) {
                sVar.c();
                a.b(cB);
                cB = sVar.b();
            } else {
                z = true;
            }
        }
        return a.toString();
    }

    static ADCData.i h(s sVar) {
        b(sVar);
        double d = 1.0d;
        if (sVar.a('-')) {
            d = -1.0d;
            b(sVar);
        }
        double dPow = 0.0d;
        char cB = sVar.b();
        while (sVar.a() && cB >= '0' && cB <= '9') {
            sVar.c();
            dPow = (dPow * 10.0d) + (cB - '0');
            cB = sVar.b();
        }
        boolean z = false;
        if (sVar.a('.')) {
            double d2 = 0.0d;
            double d3 = 0.0d;
            char cB2 = sVar.b();
            while (sVar.a() && cB2 >= '0' && cB2 <= '9') {
                sVar.c();
                d2 = (d2 * 10.0d) + (cB2 - '0');
                d3 += 1.0d;
                cB2 = sVar.b();
            }
            dPow += d2 / Math.pow(10.0d, d3);
            z = true;
        }
        if (sVar.a('e') || sVar.a('E')) {
            boolean z2 = false;
            if (!sVar.a('+') && sVar.a('-')) {
                z2 = true;
            }
            double d4 = 0.0d;
            char cB3 = sVar.b();
            while (sVar.a() && cB3 >= '0' && cB3 <= '9') {
                sVar.c();
                d4 = (d4 * 10.0d) + (cB3 - '0');
                cB3 = sVar.b();
            }
            dPow = z2 ? dPow / Math.pow(10.0d, d4) : dPow * Math.pow(10.0d, d4);
        }
        double d5 = dPow * d;
        return (z || d5 != ((double) ((int) d5))) ? new ADCData.e(d5) : new ADCData.b((int) d5);
    }

    public static void a(String[] strArr) throws IOException {
        System.out.println("==== ADCJSON Test ====");
        b(new f("test.txt"));
        a(new f("test2.txt"), a(new f("test.txt")));
        a(new f("test3.txt"), a(new f("test2.txt")));
    }
}
