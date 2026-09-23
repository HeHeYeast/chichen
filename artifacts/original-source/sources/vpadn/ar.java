package vpadn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ar {
    public static String a = "top";
    public static String b = "bottom";

    /* renamed from: c, reason: collision with root package name */
    public static String f328c = "middle";
    public static String d = "back";
    public static String e = "start";
    public static String f = "firstQuartile";
    public static String g = "midpoint";
    public static String h = "thirdQuartile";
    public static String i = "complete";
    private long C;
    private long D;
    private String F;
    private String G;
    private String j;
    private String k;
    private String s;
    private String u;
    private String l = "v";
    private String m = "v";
    private boolean n = false;
    private boolean o = false;
    private int p = 0;
    private String q = a;
    private String r = b;
    private int t = 0;
    private List<a> v = new ArrayList();
    private Map<String, List<String>> w = new HashMap();
    private b x = b.NONE;
    private int y = 0;
    private boolean z = false;
    private boolean A = false;
    private boolean B = true;
    private String E = "";
    private boolean H = false;
    private boolean I = true;

    public enum b {
        NONE,
        PORTRAIT,
        LANDSCAPE;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static b[] valuesCustom() {
            b[] bVarArrValuesCustom = values();
            int length = bVarArrValuesCustom.length;
            b[] bVarArr = new b[length];
            System.arraycopy(bVarArrValuesCustom, 0, bVarArr, 0, length);
            return bVarArr;
        }
    }

    public static class a {
        public String a;
        public EnumC0081a b;

        /* renamed from: c, reason: collision with root package name */
        public String f329c;
        public String d;
        public String e;
        public JSONObject f;

        /* renamed from: vpadn.ar$a$a, reason: collision with other inner class name */
        public enum EnumC0081a {
            OUTAPP,
            INAPP;

            /* renamed from: values, reason: to resolve conflict with enum method */
            public static EnumC0081a[] valuesCustom() {
                EnumC0081a[] enumC0081aArrValuesCustom = values();
                int length = enumC0081aArrValuesCustom.length;
                EnumC0081a[] enumC0081aArr = new EnumC0081a[length];
                System.arraycopy(enumC0081aArrValuesCustom, 0, enumC0081aArr, 0, length);
                return enumC0081aArr;
            }
        }

        public final String toString() {
            String property = System.getProperty("line.separator");
            StringBuilder sb = new StringBuilder();
            sb.append("      " + getClass().getName() + " Object {" + property);
            sb.append("        actionType:" + this.a + property);
            sb.append("        launchType:" + this.b + property);
            if (this.f329c != null) {
                sb.append("        text:" + this.f329c + property);
            }
            if (this.d != null) {
                sb.append("        appUrl:" + this.d + property);
            }
            if (this.e != null) {
                sb.append("        buttonTrackingUrl:" + this.e + property);
            }
            try {
                if (this.f != null) {
                    sb.append("        data:" + this.f.toString(8) + property);
                }
            } catch (JSONException e) {
            }
            sb.append("      }");
            return sb.toString();
        }
    }

    public final String toString() {
        String property = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(getClass().getName()) + " Object {" + property);
        if (this.o) {
            sb.append("    portraitType:" + this.l + property);
        }
        if (this.n) {
            sb.append("    landscapeType:" + this.m + property);
        }
        sb.append("    videoUrl:" + this.j + property);
        if (this.k != null) {
            sb.append("    webUrl:" + this.k + property);
        }
        sb.append("    countDownInSec:" + this.p + property);
        sb.append("    portraitVideoPosition:" + this.q + property);
        sb.append("    portraitWebPosition:" + this.r + property);
        if (this.s != null) {
            sb.append("    trackingUrl:" + this.s + property);
        }
        if (this.t != 0) {
            sb.append("    trackingInterval:" + this.t + property);
        }
        sb.append("    forceFirstMode:" + this.x + property);
        Iterator<a> it = this.v.iterator();
        while (it.hasNext()) {
            sb.append(String.valueOf(it.next().toString()) + property);
        }
        sb.append("}");
        return sb.toString();
    }

    public ar(String str) {
        this.j = str;
    }

    public final boolean a(b bVar) {
        if (bVar.equals(b.PORTRAIT)) {
            if (this.l.equals("vw") && this.k != null) {
                return true;
            }
        } else if (bVar.equals(b.LANDSCAPE) && this.m.equals("vw") && this.k != null) {
            return true;
        }
        return false;
    }

    public final String a() {
        return this.j;
    }

    public final void a(String str) {
        this.j = str;
    }

    public final int b() {
        return this.p;
    }

    public final void a(int i2) {
        this.p = i2;
    }

    public final String c() {
        return this.q;
    }

    public final void b(String str) {
        this.q = str;
        if (str == null || this.q.equals(a)) {
            this.r = b;
        } else if (this.q.equals(b)) {
            this.r = a;
        } else if (this.q.equals(f328c)) {
            this.r = d;
        }
    }

    public final String d() {
        return this.r;
    }

    public final String e() {
        return this.s;
    }

    public final void c(String str) {
        this.s = str;
    }

    public final int f() {
        return this.t;
    }

    public final void b(int i2) {
        this.t = i2;
    }

    public final void d(String str) {
        this.l = str;
    }

    public final void e(String str) {
        this.m = str;
    }

    public final List<a> g() {
        return this.v;
    }

    public final void a(a aVar) {
        this.v.add(aVar);
    }

    public final void h() {
        this.v.clear();
    }

    public final boolean i() {
        return this.n;
    }

    public final void a(boolean z) {
        this.n = true;
    }

    public final void b(boolean z) {
        this.o = true;
    }

    public final boolean j() {
        return (this.n && !this.o) || (!this.n && this.o);
    }

    public final void b(b bVar) {
        this.x = bVar;
    }

    public final int k() {
        return this.y;
    }

    public final void c(int i2) {
        this.y = i2;
    }

    public final boolean l() {
        return this.z;
    }

    public final void c(boolean z) {
        this.z = z;
    }

    public final boolean m() {
        return this.A;
    }

    public final void d(boolean z) {
        this.A = z;
    }

    public final long n() {
        return this.C;
    }

    public final void a(long j) {
        this.C = j;
    }

    public final long o() {
        return this.D;
    }

    public final void b(long j) {
        this.D = j;
    }

    public final String p() {
        return this.E;
    }

    public final void f(String str) {
        this.E = str;
    }

    public final boolean q() {
        return this.B;
    }

    public final void e(boolean z) {
        this.B = false;
    }

    public final String r() {
        return this.u;
    }

    public final void g(String str) {
        this.u = str;
    }

    public final String s() {
        return this.F;
    }

    public final void h(String str) {
        this.F = str;
    }

    public final String t() {
        return this.G;
    }

    public final void i(String str) {
        this.G = str;
    }

    public final String u() {
        return this.k;
    }

    public final void j(String str) {
        this.k = str;
    }

    public final boolean v() {
        return this.H;
    }

    public final void f(boolean z) {
        this.H = true;
    }

    public final boolean w() {
        return this.I;
    }

    public final void g(boolean z) {
        this.I = false;
    }

    public final void a(JSONObject jSONObject) {
        try {
            if (jSONObject.has(e)) {
                a(e, jSONObject.getJSONArray(e));
            }
        } catch (JSONException e2) {
        }
        try {
            if (jSONObject.has(f)) {
                a(f, jSONObject.getJSONArray(f));
            }
        } catch (JSONException e3) {
        }
        try {
            if (jSONObject.has(g)) {
                a(g, jSONObject.getJSONArray(g));
            }
        } catch (JSONException e4) {
        }
        try {
            if (jSONObject.has(h)) {
                a(h, jSONObject.getJSONArray(h));
            }
        } catch (JSONException e5) {
        }
        try {
            if (jSONObject.has(i)) {
                a(i, jSONObject.getJSONArray(i));
            }
        } catch (JSONException e6) {
        }
    }

    private void a(String str, JSONArray jSONArray) {
        int length;
        if (jSONArray != null && (length = jSONArray.length()) > 0) {
            List<String> arrayList = this.w.get(str);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.w.put(str, arrayList);
            }
            for (int i2 = 0; i2 < length; i2++) {
                try {
                    arrayList.add(jSONArray.getString(i2));
                } catch (Exception e2) {
                    ab.b("VideoData", "urlList.add(trackingUrlJsonArray.getString(i)) throw Exception");
                }
            }
        }
    }

    public final List<String> k(String str) {
        return this.w.get(str);
    }

    public final void a(Map<String, List<String>> map) {
        this.w = map;
    }

    public final Map<String, List<String>> x() {
        return this.w;
    }
}
