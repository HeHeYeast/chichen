package com.jirbo.adcolony;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADCData {
    static i a = new h();
    static i b = new a();

    /* renamed from: c, reason: collision with root package name */
    static i f199c = new d();

    static class i {
        i() {
        }

        boolean m() {
            return false;
        }

        boolean f() {
            return false;
        }

        boolean k() {
            return false;
        }

        boolean p() {
            return b_() || c();
        }

        boolean b_() {
            return false;
        }

        boolean c() {
            return false;
        }

        boolean a() {
            return false;
        }

        boolean c_() {
            return false;
        }

        boolean g() {
            return true;
        }

        g n() {
            return null;
        }

        c h() {
            return null;
        }

        String b() {
            return q();
        }

        double d() {
            return 0.0d;
        }

        int e() {
            return 0;
        }

        boolean l() {
            return false;
        }

        public String toString() {
            return q();
        }

        String q() {
            z zVar = new z();
            a(zVar);
            return zVar.toString();
        }

        void a(af afVar) {
        }

        void a(af afVar, String str) {
            if (str != null) {
                afVar.b('\"');
                int length = str.length();
                for (int i = 0; i < length; i++) {
                    char cCharAt = str.charAt(i);
                    switch (cCharAt) {
                        case '\b':
                            afVar.a("\\b");
                            break;
                        case '\t':
                            afVar.a("\\t");
                            break;
                        case '\n':
                            afVar.a("\\n");
                            break;
                        case '\f':
                            afVar.a("\\f");
                            break;
                        case '\r':
                            afVar.a("\\r");
                            break;
                        case '\"':
                            afVar.a("\\\"");
                            break;
                        case '/':
                            afVar.a("\\/");
                            break;
                        case '\\':
                            afVar.a("\\\\");
                            break;
                        default:
                            if (cCharAt >= ' ' && cCharAt <= '~') {
                                afVar.b(cCharAt);
                                break;
                            } else {
                                afVar.a("\\u");
                                int i2 = cCharAt;
                                for (int i3 = 0; i3 < 4; i3++) {
                                    int i4 = (i2 >> 12) & 15;
                                    i2 <<= 4;
                                    if (i4 <= 9) {
                                        afVar.a(i4);
                                    } else {
                                        afVar.b((char) ((i4 - 10) + 97));
                                    }
                                }
                                break;
                            }
                            break;
                    }
                }
                afVar.b('\"');
            }
        }
    }

    static class d extends i {
        d() {
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean c_() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        String b() {
            return "null";
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            afVar.a("null");
        }
    }

    static class h extends i {
        h() {
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean a() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        String b() {
            return "true";
        }

        @Override // com.jirbo.adcolony.ADCData.i
        double d() {
            return 1.0d;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        int e() {
            return 1;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean l() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            afVar.a("true");
        }
    }

    static class a extends i {
        a() {
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean a() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        String b() {
            return "false";
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            afVar.a("false");
        }
    }

    static class g extends i implements Serializable {
        HashMap<String, i> a = new HashMap<>();
        ArrayList<String> b = new ArrayList<>();

        g() {
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean m() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean g() {
            return this.a.size() < 0 || (this.a.size() == 1 && this.a.get(this.b.get(0)).g());
        }

        @Override // com.jirbo.adcolony.ADCData.i
        g n() {
            return this;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            boolean z;
            int size = this.b.size();
            if (size == 0) {
                afVar.a("{}");
                return;
            }
            if (size == 1 && this.a.get(this.b.get(0)).g()) {
                afVar.a("{");
                String str = this.b.get(0);
                i iVar = this.a.get(str);
                a(afVar, str);
                afVar.b(':');
                iVar.a(afVar);
                afVar.a("}");
                return;
            }
            afVar.b("{");
            afVar.i += 2;
            int i = 0;
            boolean z2 = true;
            while (i < size) {
                if (z2) {
                    z = false;
                } else {
                    afVar.c(',');
                    z = z2;
                }
                String str2 = this.b.get(i);
                i iVar2 = this.a.get(str2);
                a(afVar, str2);
                afVar.b(':');
                if (!iVar2.g()) {
                    afVar.d();
                }
                iVar2.a(afVar);
                i++;
                z2 = z;
            }
            afVar.d();
            afVar.i -= 2;
            afVar.a("}");
        }

        int o() {
            return this.b.size();
        }

        String a(int i) {
            return this.b.get(i);
        }

        boolean a(String str) {
            return this.a.containsKey(str);
        }

        g a(String str, g gVar) {
            i iVar = this.a.get(str);
            return (iVar == null || !iVar.m()) ? gVar : iVar.n();
        }

        c a(String str, c cVar) {
            i iVar = this.a.get(str);
            return (iVar == null || !iVar.f()) ? cVar : iVar.h();
        }

        ArrayList<String> a(String str, ArrayList<String> arrayList) {
            c cVarC = c(str);
            if (cVarC != null) {
                arrayList = new ArrayList<>();
                for (int i = 0; i < cVarC.i(); i++) {
                    String strD = cVarC.d(i);
                    if (strD != null) {
                        arrayList.add(strD);
                    }
                }
            }
            return arrayList;
        }

        String a(String str, String str2) {
            i iVar = this.a.get(str);
            return (iVar == null || !iVar.k()) ? str2 : iVar.b();
        }

        double a(String str, double d) {
            i iVar = this.a.get(str);
            return (iVar == null || !iVar.p()) ? d : iVar.d();
        }

        int a(String str, int i) {
            i iVar = this.a.get(str);
            return (iVar == null || !iVar.p()) ? i : iVar.e();
        }

        boolean a(String str, boolean z) {
            i iVar = this.a.get(str);
            if (iVar != null) {
                return (iVar.a() || iVar.k()) ? iVar.l() : z;
            }
            return z;
        }

        g b(String str) {
            g gVarA = a(str, (g) null);
            return gVarA != null ? gVarA : new g();
        }

        c c(String str) {
            c cVarA = a(str, (c) null);
            return cVarA != null ? cVarA : new c();
        }

        ArrayList<String> d(String str) {
            ArrayList<String> arrayListA = a(str, (ArrayList<String>) null);
            return arrayListA == null ? new ArrayList<>() : arrayListA;
        }

        String e(String str) {
            return a(str, "");
        }

        double f(String str) {
            return a(str, 0.0d);
        }

        int g(String str) {
            return a(str, 0);
        }

        boolean h(String str) {
            return a(str, false);
        }

        void a(String str, i iVar) {
            if (!this.a.containsKey(str)) {
                this.b.add(str);
            }
            this.a.put(str, iVar);
        }

        void b(String str, String str2) {
            a(str, new f(str2));
        }

        void b(String str, double d) {
            a(str, new e(d));
        }

        void b(String str, int i) {
            a(str, new b(i));
        }

        void b(String str, boolean z) {
            a(str, z ? ADCData.a : ADCData.b);
        }
    }

    static class c extends i {
        ArrayList<i> a = new ArrayList<>();

        c() {
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean f() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean g() {
            return this.a.size() == 0 || (this.a.size() == 1 && this.a.get(0).g());
        }

        @Override // com.jirbo.adcolony.ADCData.i
        c h() {
            return this;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            boolean z;
            int size = this.a.size();
            if (size == 0) {
                afVar.a("[]");
                return;
            }
            if (size == 1 && this.a.get(0).g()) {
                afVar.a("[");
                this.a.get(0).a(afVar);
                afVar.a("]");
                return;
            }
            afVar.b("[");
            afVar.i += 2;
            int i = 0;
            boolean z2 = true;
            while (i < size) {
                if (z2) {
                    z = false;
                } else {
                    afVar.c(',');
                    z = z2;
                }
                this.a.get(i).a(afVar);
                i++;
                z2 = z;
            }
            afVar.d();
            afVar.i -= 2;
            afVar.a("]");
        }

        int i() {
            return this.a.size();
        }

        void j() {
            this.a.clear();
        }

        c a(i iVar) {
            this.a.add(iVar);
            return this;
        }

        c a(String str) {
            a(new f(str));
            return this;
        }

        c a(double d) {
            a(new e(d));
            return this;
        }

        c a(int i) {
            a(new b(i));
            return this;
        }

        c a(boolean z) {
            a(z ? ADCData.a : ADCData.b);
            return this;
        }

        c a(c cVar) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < cVar.i()) {
                    a(cVar.a.get(i2));
                    i = i2 + 1;
                } else {
                    return this;
                }
            }
        }

        g a(int i, g gVar) {
            i iVar = this.a.get(i);
            return (iVar == null || !iVar.m()) ? gVar : iVar.n();
        }

        c a(int i, c cVar) {
            i iVar = this.a.get(i);
            return (iVar == null || !iVar.f()) ? cVar : iVar.h();
        }

        String a(int i, String str) {
            i iVar = this.a.get(i);
            return (iVar == null || !iVar.k()) ? str : iVar.b();
        }

        double a(int i, double d) {
            i iVar = this.a.get(i);
            return (iVar == null || !iVar.p()) ? d : iVar.d();
        }

        int a(int i, int i2) {
            i iVar = this.a.get(i);
            return (iVar == null || !iVar.p()) ? i2 : iVar.e();
        }

        boolean a(int i, boolean z) {
            i iVar = this.a.get(i);
            if (iVar != null) {
                return (iVar.a() || iVar.k()) ? iVar.l() : z;
            }
            return z;
        }

        g b(int i) {
            g gVarA = a(i, (g) null);
            return gVarA != null ? gVarA : new g();
        }

        c c(int i) {
            c cVarA = a(i, (c) null);
            return cVarA != null ? cVarA : new c();
        }

        String d(int i) {
            return a(i, "");
        }

        double e(int i) {
            return a(i, 0.0d);
        }

        int f(int i) {
            return a(i, 0);
        }

        boolean g(int i) {
            return a(i, false);
        }

        i a_() {
            return this.a.remove(this.a.size() - 1);
        }
    }

    static class f extends i implements Serializable {
        String a;

        f(String str) {
            this.a = str;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean k() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        String b() {
            return this.a;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        double d() {
            try {
                return Double.parseDouble(this.a);
            } catch (NumberFormatException e) {
                return 0.0d;
            }
        }

        @Override // com.jirbo.adcolony.ADCData.i
        int e() {
            return (int) d();
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean l() {
            String lowerCase = this.a.toLowerCase();
            return lowerCase.equals("true") || lowerCase.equals("yes");
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            a(afVar, this.a);
        }
    }

    static class e extends i {
        double a;

        e(double d) {
            this.a = d;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean b_() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        double d() {
            return this.a;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        int e() {
            return (int) this.a;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            afVar.a(this.a);
        }
    }

    static class b extends i {
        int a;

        b(int i) {
            this.a = i;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        boolean c() {
            return true;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        double d() {
            return this.a;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        int e() {
            return this.a;
        }

        @Override // com.jirbo.adcolony.ADCData.i
        void a(af afVar) {
            afVar.a(this.a);
        }
    }

    public static void main(String[] args) {
        System.out.println("==== ADCData Test ====");
        g gVar = new g();
        gVar.b("one", 1);
        gVar.b("pi", 3.14d);
        gVar.b("name", "\"Abe Pralle\"");
        gVar.a("list", (i) new c());
        gVar.a("subtable", (i) new g());
        gVar.b("subtable").b("five", 5);
        System.out.println("LIST:" + gVar.c("list"));
        gVar.c("list").a(3);
        System.out.println(gVar);
        System.out.println(gVar.g("one"));
        System.out.println(gVar.f("one"));
        System.out.println(gVar.g("pi"));
        System.out.println(gVar.f("pi"));
        System.out.println(gVar.e("name"));
        System.out.println(gVar.f("name"));
        System.out.println(gVar.g("name"));
        System.out.println(gVar.c("list"));
        System.out.println(gVar.c("list2"));
        System.out.println(gVar.c("subtable"));
        System.out.println(gVar.b("subtable"));
        System.out.println(gVar.b("subtable2"));
        System.out.println(gVar.b("list"));
    }
}
