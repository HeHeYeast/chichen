package com.jirbo.adcolony;

import com.google.android.gms.plus.PlusShare;
import com.jirbo.adcolony.ADCData;
import java.util.ArrayList;
import java.util.HashMap;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class n {
    n() {
    }

    static class e {
        boolean a;
        boolean b;

        /* renamed from: c, reason: collision with root package name */
        String f234c;
        String d;
        boolean e = false;
        boolean f;
        double g;
        String h;
        String i;
        String j;
        f k;
        w l;
        ArrayList<String> m;
        ae n;
        i o;

        e() {
        }

        boolean a(String str) {
            return a(str, false, true);
        }

        boolean a(String str, boolean z, boolean z2) {
            ab abVarA;
            if (this.a && (abVarA = this.n.a(str)) != null) {
                return abVarA.a(z, z2);
            }
            return false;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            this.b = gVar.h("log_screen_overlay");
            this.f234c = gVar.e("last_country");
            this.d = gVar.e("last_ip");
            this.f = gVar.h("collect_iap_enabled");
            this.g = gVar.f("media_pool_size");
            this.h = gVar.e("log_level");
            this.i = gVar.e("view_network_pass_filter");
            this.j = gVar.e("cache_network_pass_filter");
            this.e = gVar.h("hardware_acceleration_disabled");
            if (this.i == null || this.i.equals("")) {
                this.i = "all";
            }
            if (this.j == null || this.j.equals("")) {
                this.j = "all";
            }
            this.k = new f();
            if (!this.k.a(gVar.b("tracking"))) {
                return false;
            }
            this.l = new w();
            if (!this.l.a(gVar.b("third_party_tracking"))) {
                return false;
            }
            this.m = gVar.d("console_messages");
            com.jirbo.adcolony.l.a.b((Object) "Parsing zones");
            this.n = new ae();
            if (!this.n.a(gVar.c("zones"))) {
                return false;
            }
            this.o = new i();
            if (!this.o.a(gVar.b("device"))) {
                return false;
            }
            com.jirbo.adcolony.l.a.b((Object) "Finished parsing app info");
            return true;
        }

        void a() {
            com.jirbo.adcolony.l.a.b((Object) "Caching media");
            if (this.a) {
                for (int i = 0; i < this.n.a(); i++) {
                    this.n.a(i).l();
                }
            }
        }
    }

    static class i {
        String a;

        i() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.a("type", (String) null);
            com.jirbo.adcolony.a.X = this.a;
            return true;
        }
    }

    static class f {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f235c;
        String d;
        String e;
        String f;
        String g;
        ADCData.g h;

        f() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar != null) {
                this.a = gVar.a("update", (String) null);
                this.b = gVar.a("install", (String) null);
                this.f235c = gVar.a("dynamic_interests", (String) null);
                this.d = gVar.a("user_meta_data", (String) null);
                this.e = gVar.a("in_app_purchase", (String) null);
                this.g = gVar.a("session_end", (String) null);
                this.f = gVar.a("session_start", (String) null);
                this.h = new ADCData.g();
                this.h.b("update", this.a);
                this.h.b("install", this.b);
                this.h.b("dynamic_interests", this.f235c);
                this.h.b("user_meta_data", this.d);
                this.h.b("in_app_purchase", this.e);
                this.h.b("session_end", this.g);
                this.h.b("session_start", this.f);
                com.jirbo.adcolony.f fVar = new com.jirbo.adcolony.f("iap_cache.txt");
                ADCData.c cVarC = com.jirbo.adcolony.k.c(fVar);
                if (cVarC != null) {
                    for (int i = 0; i < cVarC.i(); i++) {
                        com.jirbo.adcolony.a.l.d.a("in_app_purchase", cVarC.a(i, new ADCData.g()));
                    }
                    fVar.c();
                    com.jirbo.adcolony.a.Z.j();
                }
                com.jirbo.adcolony.a.F = true;
            }
            return true;
        }
    }

    static class b {
        String A;
        String B;
        String C;
        ADCData.g D = new ADCData.g();
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f232c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        String l;
        String m;
        String n;
        String o;
        String p;
        String q;
        String r;
        String s;
        String t;
        String u;
        String v;
        String w;
        String x;
        String y;
        String z;

        b() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar != null) {
                this.a = gVar.a("replay", (String) null);
                this.b = gVar.a("card_shown", (String) null);
                this.f232c = gVar.a("html5_interaction", (String) null);
                this.d = gVar.a("cancel", (String) null);
                this.e = gVar.a("download", (String) null);
                this.f = gVar.a("skip", (String) null);
                this.g = gVar.a("info", (String) null);
                this.h = gVar.a("custom_event", (String) null);
                this.i = gVar.a("midpoint", (String) null);
                this.j = gVar.a("card_dissolved", (String) null);
                this.k = gVar.a("start", (String) null);
                this.l = gVar.a("third_quartile", (String) null);
                this.m = gVar.a("complete", (String) null);
                this.n = gVar.a("continue", (String) null);
                this.o = gVar.a("in_video_engagement", (String) null);
                this.p = gVar.a("reward_v4vc", (String) null);
                this.r = gVar.a("first_quartile", (String) null);
                this.q = gVar.a("v4iap", (String) null);
                this.s = gVar.a("video_expanded", (String) null);
                this.t = gVar.a("sound_mute", (String) null);
                this.u = gVar.a("sound_unmute", (String) null);
                this.v = gVar.a("video_paused", (String) null);
                this.w = gVar.a("video_resumed", (String) null);
                this.x = gVar.a("native_start", (String) null);
                this.y = gVar.a("native_first_quartile", (String) null);
                this.z = gVar.a("native_midpoint", (String) null);
                this.A = gVar.a("native_third_quartile", (String) null);
                this.B = gVar.a("native_complete", (String) null);
                this.C = gVar.a("native_overlay_click", (String) null);
                this.D.b("replay", this.a);
                this.D.b("card_shown", this.b);
                this.D.b("html5_interaction", this.f232c);
                this.D.b("cancel", this.d);
                this.D.b("download", this.e);
                this.D.b("skip", this.f);
                this.D.b("info", this.g);
                this.D.b("custom_event", this.h);
                this.D.b("midpoint", this.i);
                this.D.b("card_dissolved", this.j);
                this.D.b("start", this.k);
                this.D.b("third_quartile", this.l);
                this.D.b("complete", this.m);
                this.D.b("continue", this.n);
                this.D.b("in_video_engagement", this.o);
                this.D.b("reward_v4vc", this.p);
                this.D.b("first_quartile", this.r);
                this.D.b("v4iap", this.q);
                this.D.b("video_expanded", this.s);
                this.D.b("sound_mute", this.t);
                this.D.b("sound_unmute", this.u);
                this.D.b("video_paused", this.v);
                this.D.b("video_resumed", this.w);
                this.D.b("native_start", this.x);
                this.D.b("native_first_quartile", this.y);
                this.D.b("native_midpoint", this.z);
                this.D.b("native_third_quartile", this.A);
                this.D.b("native_complete", this.B);
                this.D.b("native_overlay_click", this.C);
            }
            return true;
        }
    }

    static class w {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();

        /* renamed from: c, reason: collision with root package name */
        ArrayList<String> f251c = new ArrayList<>();
        HashMap<String, ArrayList<String>> d = new HashMap<>();

        w() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            ArrayList<String> arrayListD = gVar.d("update");
            this.a = arrayListD;
            if (arrayListD == null) {
                return false;
            }
            ArrayList<String> arrayListD2 = gVar.d("install");
            this.b = arrayListD2;
            if (arrayListD2 == null) {
                return false;
            }
            ArrayList<String> arrayListD3 = gVar.d("session_start");
            this.f251c = arrayListD3;
            if (arrayListD3 == null) {
                return false;
            }
            this.d.put("update", this.a);
            this.d.put("install", this.b);
            this.d.put("session_start", this.f251c);
            return true;
        }
    }

    static class x {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();

        /* renamed from: c, reason: collision with root package name */
        ArrayList<String> f252c = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();
        ArrayList<String> e = new ArrayList<>();
        ArrayList<String> f = new ArrayList<>();
        ArrayList<String> g = new ArrayList<>();
        ArrayList<String> h = new ArrayList<>();
        ArrayList<String> i = new ArrayList<>();
        ArrayList<String> j = new ArrayList<>();
        ArrayList<String> k = new ArrayList<>();
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> m = new ArrayList<>();
        ArrayList<String> n = new ArrayList<>();
        ArrayList<String> o = new ArrayList<>();
        ArrayList<String> p = new ArrayList<>();
        ArrayList<String> q = new ArrayList<>();
        ArrayList<String> r = new ArrayList<>();
        ArrayList<String> s = new ArrayList<>();
        ArrayList<String> t = new ArrayList<>();
        ArrayList<String> u = new ArrayList<>();
        ArrayList<String> v = new ArrayList<>();
        ArrayList<String> w = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        ArrayList<String> y = new ArrayList<>();
        ArrayList<String> z = new ArrayList<>();
        ArrayList<String> A = new ArrayList<>();
        ArrayList<String> B = new ArrayList<>();
        HashMap<String, ArrayList<String>> C = new HashMap<>();

        x() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.d("replay");
            this.b = gVar.d("card_shown");
            this.f252c = gVar.d("html5_interaction");
            this.d = gVar.d("cancel");
            this.e = gVar.d("download");
            this.f = gVar.d("skip");
            this.g = gVar.d("info");
            this.h = gVar.d("midpoint");
            this.i = gVar.d("card_dissolved");
            this.j = gVar.d("start");
            this.k = gVar.d("third_quartile");
            this.l = gVar.d("complete");
            this.m = gVar.d("continue");
            this.n = gVar.d("in_video_engagement");
            this.o = gVar.d("reward_v4vc");
            this.p = gVar.d("first_quartile");
            this.q = gVar.d("v4iap");
            this.r = gVar.d("video_expanded");
            this.s = gVar.d("sound_mute");
            this.t = gVar.d("sound_unmute");
            this.u = gVar.d("video_paused");
            this.v = gVar.d("video_resumed");
            this.w = gVar.d("native_start");
            this.x = gVar.d("native_first_quartile");
            this.y = gVar.d("native_midpoint");
            this.z = gVar.d("native_third_quartile");
            this.A = gVar.d("native_complete");
            this.B = gVar.d("native_overlay_click");
            this.C.put("replay", this.a);
            this.C.put("card_shown", this.b);
            this.C.put("html5_interaction", this.f252c);
            this.C.put("cancel", this.d);
            this.C.put("download", this.e);
            this.C.put("skip", this.f);
            this.C.put("info", this.g);
            this.C.put("midpoint", this.h);
            this.C.put("card_dissolved", this.i);
            this.C.put("start", this.j);
            this.C.put("third_quartile", this.k);
            this.C.put("complete", this.l);
            this.C.put("continue", this.m);
            this.C.put("in_video_engagement", this.n);
            this.C.put("reward_v4vc", this.o);
            this.C.put("first_quartile", this.p);
            this.C.put("v4iap", this.q);
            this.C.put("video_expanded", this.r);
            this.C.put("sound_mute", this.s);
            this.C.put("sound_unmute", this.t);
            this.C.put("video_paused", this.u);
            this.C.put("video_resumed", this.v);
            this.C.put("native_start", this.w);
            this.C.put("native_first_quartile", this.x);
            this.C.put("native_midpoint", this.y);
            this.C.put("native_third_quartile", this.z);
            this.C.put("native_complete", this.A);
            this.C.put("native_overlay_click", this.B);
            return true;
        }
    }

    static class ae {
        ArrayList<ab> a;

        ae() {
        }

        boolean a(ADCData.c cVar) {
            this.a = new ArrayList<>();
            if (cVar == null) {
                return false;
            }
            for (int i = 0; i < cVar.i(); i++) {
                ab abVar = new ab();
                if (!abVar.a(cVar.b(i))) {
                    return false;
                }
                this.a.add(abVar);
            }
            return true;
        }

        int a() {
            return this.a.size();
        }

        ab a(int i) {
            return this.a.get(i);
        }

        ab b() {
            return this.a.get(0);
        }

        ab a(String str) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.a.size()) {
                    ab abVar = this.a.get(i2);
                    if (!abVar.a.equals(str)) {
                        i = i2 + 1;
                    } else {
                        return abVar;
                    }
                } else {
                    com.jirbo.adcolony.l.a.a("No such zone: ").b((Object) str);
                    return null;
                }
            }
        }
    }

    static class ab {
        String a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f230c;
        int d;
        boolean e;
        boolean f;
        ArrayList<String> g;
        ac h;
        d i;
        ad j;
        ag k;

        ab() {
        }

        boolean a() {
            return a(false, true);
        }

        boolean a(boolean z, boolean z2) {
            a aVarI;
            if (!z2) {
                return a(z);
            }
            if (!this.e || !this.f) {
                com.jirbo.adcolony.a.ac = 1;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready to be played, as zone " + this.a + " is disabled or inactive.");
            }
            if (this.i.a() == 0 || this.g.size() == 0) {
                com.jirbo.adcolony.a.ac = 5;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready to be played, as AdColony currently has no videos available to be played in zone " + this.a + ".");
            }
            int size = this.g.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    aVarI = null;
                    break;
                }
                aVarI = i();
                if (aVarI == null) {
                    return com.jirbo.adcolony.l.f226c.b("Ad is not ready to be played due to an unknown error.");
                }
                if (aVarI.a()) {
                    break;
                }
                k();
                i++;
            }
            if (aVarI != null) {
                return a(aVarI) != 0;
            }
            com.jirbo.adcolony.a.ac = 6;
            return com.jirbo.adcolony.l.f226c.b("Ad is not ready to be played as required assets are still downloading or otherwise missing.");
        }

        boolean a(boolean z) {
            a aVarI;
            if (!z) {
                com.jirbo.adcolony.a.h();
            }
            if (!this.e || !this.f || this.i.a() == 0 || this.g.size() == 0) {
                return false;
            }
            int size = this.g.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    aVarI = null;
                    break;
                }
                aVarI = i();
                if (aVarI == null) {
                    return false;
                }
                if (aVarI.a()) {
                    break;
                }
                k();
                i++;
            }
            return (aVarI == null || a(aVarI) == 0) ? false : true;
        }

        boolean b() {
            if (this.b <= 1) {
                return false;
            }
            com.jirbo.adcolony.a.l.g.b = true;
            ag agVar = this.k;
            int i = agVar.b;
            agVar.b = i + 1;
            if (i == 0) {
                return false;
            }
            if (this.k.b >= this.b) {
                this.k.b = 0;
            }
            return true;
        }

        int a(int i, int i2) {
            if (i2 <= 0) {
                return 0;
            }
            if (i != -1) {
                if (i >= i2) {
                    i = i2;
                }
                return i;
            }
            return i2;
        }

        void c() {
            com.jirbo.adcolony.a.l.b.e();
        }

        synchronized int d() {
            return a(i());
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x011d A[Catch: all -> 0x01ce, PHI: r0
  0x011d: PHI (r0v7 int) = (r0v6 int), (r0v30 int) binds: [B:29:0x00d0, B:38:0x010e] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0012, B:7:0x001a, B:9:0x0029, B:12:0x0039, B:14:0x003f, B:17:0x005e, B:18:0x006d, B:20:0x0071, B:22:0x0078, B:23:0x0097, B:25:0x009b, B:27:0x00ac, B:28:0x00cc, B:30:0x00d2, B:32:0x00da, B:34:0x00e2, B:35:0x00ef, B:37:0x00fd, B:39:0x0110, B:40:0x011d, B:42:0x0125, B:44:0x0134, B:45:0x0144, B:47:0x014a, B:49:0x0159, B:50:0x0169, B:52:0x016f, B:54:0x017e, B:55:0x018e, B:57:0x0194, B:59:0x01a3, B:60:0x01b3, B:62:0x01bd), top: B:67:0x0001 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        synchronized int a(com.jirbo.adcolony.n.a r11) {
            /*
                Method dump skipped, instructions count: 465
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.jirbo.adcolony.n.ab.a(com.jirbo.adcolony.n$a):int");
        }

        boolean e() {
            return b(true);
        }

        boolean b(boolean z) {
            if (!z) {
                return f();
            }
            if (!this.e || !this.f) {
                com.jirbo.adcolony.a.ac = 1;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as zone " + this.a + " is disabled or inactive.");
            }
            if (this.i.a() == 0) {
                com.jirbo.adcolony.a.ac = 5;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as there are currently no ads to play in zone " + this.a + ".");
            }
            if (!this.i.b().s.a) {
                return true;
            }
            com.jirbo.adcolony.a.ac = 14;
            return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as zone " + this.a + " is V4VC enabled and must be played using an AdColonyV4VCAd object.");
        }

        boolean f() {
            return this.e && this.f && this.i.a() != 0 && !this.i.b().s.a;
        }

        boolean g() {
            return c(true);
        }

        boolean c(boolean z) {
            if (!z) {
                return h();
            }
            if (!this.e || !this.f) {
                com.jirbo.adcolony.a.ac = 1;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as zone " + this.a + " is disabled or inactive.");
            }
            if (this.i.a() == 0) {
                com.jirbo.adcolony.a.ac = 5;
                return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as there are currently no ads to play in zone " + this.a + ".");
            }
            if (this.i.b().s.a) {
                return true;
            }
            com.jirbo.adcolony.a.ac = 15;
            return com.jirbo.adcolony.l.f226c.b("Ad is not ready, as zone " + this.a + " is not V4VC enabled and must be played using an AdColonyVideoAd object.");
        }

        boolean h() {
            return this.e && this.f && this.i.a() != 0 && this.i.b().s.a;
        }

        a i() {
            if (this.g.size() > 0) {
                return this.i.a(this.g.get(this.k.f215c % this.g.size()));
            }
            return null;
        }

        a j() {
            if (this.g.size() > 0) {
                return this.i.b(this.k.f215c % this.g.size());
            }
            return null;
        }

        void k() {
            if (this.g.size() > 0) {
                this.k.f215c = (this.k.f215c + 1) % this.g.size();
            }
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.e("uuid");
            this.e = gVar.h("enabled");
            this.f = gVar.h("active");
            if (!this.e || !this.f) {
                return true;
            }
            this.b = gVar.g("play_interval");
            this.f230c = gVar.g("daily_play_cap");
            this.d = gVar.g("session_play_cap");
            this.g = new ArrayList<>();
            ArrayList<String> arrayListD = gVar.d("play_order");
            this.g = arrayListD;
            if (arrayListD == null) {
                return false;
            }
            this.h = new ac();
            if (!this.h.a(gVar.b("tracking"))) {
                return false;
            }
            this.i = new d();
            if (!this.i.a(gVar.c("ads"))) {
                return false;
            }
            this.j = new ad();
            if (!this.j.a(gVar.b("v4vc"))) {
                return false;
            }
            this.k = com.jirbo.adcolony.a.l.g.a(this.a);
            return true;
        }

        void l() {
            if (this.e && this.f) {
                for (int i = 0; i < this.i.a(); i++) {
                    this.i.a(i).b();
                }
            }
        }
    }

    static class ac {
        String a;

        ac() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar != null) {
                this.a = gVar.a("request", (String) null);
            }
            return true;
        }
    }

    static class d {
        ArrayList<a> a = new ArrayList<>();

        d() {
        }

        boolean a(ADCData.c cVar) {
            if (cVar == null) {
                return false;
            }
            for (int i = 0; i < cVar.i(); i++) {
                a aVar = new a();
                if (!aVar.a(cVar.b(i))) {
                    return false;
                }
                this.a.add(aVar);
            }
            return true;
        }

        void a(a aVar) {
            this.a.add(aVar);
        }

        int a() {
            return this.a.size();
        }

        a a(int i) {
            return this.a.get(i);
        }

        a b() {
            return this.a.get(0);
        }

        a a(String str) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.a.size()) {
                    a aVar = this.a.get(i2);
                    if (!aVar.a.equals(str)) {
                        i = i2 + 1;
                    } else {
                        return aVar;
                    }
                } else {
                    return null;
                }
            }
        }

        a b(int i) {
            while (i < this.a.size()) {
                a aVar = this.a.get(i);
                if (!aVar.w.a) {
                    i++;
                } else {
                    return aVar;
                }
            }
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.a.size()) {
                    a aVar2 = this.a.get(i3);
                    if (!aVar2.w.a) {
                        i2 = i3 + 1;
                    } else {
                        return aVar2;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    static class y {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        boolean f253c;

        y() {
        }

        boolean a() {
            return true;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.f253c = gVar.h("enabled");
            if (!this.f253c) {
                return true;
            }
            this.a = gVar.d("product_ids").get(0);
            this.b = gVar.e("in_progress");
            return true;
        }
    }

    static class a {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        int f228c;
        int d;
        int e;
        int f;
        int g;
        int h;
        boolean i;
        boolean j;
        boolean k;
        boolean l;
        boolean m;
        boolean n;
        boolean o;
        C0075n p;
        x q;
        m r;
        c s;
        b t;
        h u;
        aa v;
        p w;
        y x;

        a() {
        }

        boolean a() {
            if (!this.r.a()) {
                return false;
            }
            if (this.s.a && !this.s.a()) {
                return false;
            }
            if (!this.w.a || this.w.a()) {
                return (!this.u.d || this.u.a()) && this.v.a() && this.x.a();
            }
            return false;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.e("uuid");
            this.b = gVar.e(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
            this.f228c = gVar.g("ad_campaign_id");
            this.d = gVar.g("ad_id");
            this.e = gVar.g("ad_group_id");
            this.f = gVar.g("cpcv_bid");
            this.g = gVar.g("net_earnings");
            this.h = gVar.g("expires");
            this.i = gVar.h("enable_in_app_store");
            this.j = gVar.h("video_events_on_replays");
            this.k = gVar.h("test_ad");
            this.l = gVar.h("fullscreen");
            this.m = gVar.h("house_ad");
            this.n = gVar.h("contracted");
            this.p = new C0075n();
            if (!this.p.a(gVar.b("limits"))) {
                return false;
            }
            this.q = new x();
            if (!this.q.a(gVar.b("third_party_tracking"))) {
                return false;
            }
            this.r = new m();
            if (!this.r.a(gVar.b("in_app_browser"))) {
                return false;
            }
            this.w = new p();
            if (!this.w.a(gVar.b("native"))) {
                return false;
            }
            this.s = new c();
            if (!this.s.a(gVar.b("v4vc"))) {
                return false;
            }
            this.t = new b();
            if (!this.t.a(gVar.b("ad_tracking"))) {
                return false;
            }
            this.u = new h();
            if (!this.u.a(gVar.b("companion_ad"))) {
                return false;
            }
            this.v = new aa();
            if (!this.v.a(gVar.b("video"))) {
                return false;
            }
            this.x = new y();
            return this.x.a(gVar.b("v4iap"));
        }

        void b() {
            this.s.b();
            this.r.b();
            this.w.b();
            this.u.b();
            this.v.c();
        }
    }

    static class q {
        boolean a;
        boolean b;

        /* renamed from: c, reason: collision with root package name */
        String f245c;
        String d;
        String e;

        q() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = gVar.h("in_app");
            this.f245c = gVar.e("click_action_type");
            this.e = gVar.e("click_action");
            this.d = gVar.e(PlusShare.KEY_CALL_TO_ACTION_LABEL);
            return true;
        }
    }

    static class p {
        boolean a;
        boolean b;

        /* renamed from: c, reason: collision with root package name */
        String f244c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        q j;
        l k;
        l l;

        p() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            this.f244c = gVar.e("poster_image");
            this.d = gVar.e("advertiser_name");
            this.e = gVar.e(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
            this.f = gVar.e(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
            this.g = gVar.e("thumb_image");
            this.h = gVar.e("poster_image_last_modified");
            this.i = gVar.e("thumb_image_last_modified");
            this.k = new l();
            if (!this.k.a(gVar.b("mute"))) {
                return false;
            }
            this.b = this.k.f;
            this.l = new l();
            if (!this.l.a(gVar.b("unmute"))) {
                return false;
            }
            this.j = new q();
            return this.j.a(gVar.b("overlay"));
        }

        boolean a() {
            return this.a && com.jirbo.adcolony.a.l.f219c.a(this.f244c) && com.jirbo.adcolony.a.l.f219c.a(this.g) && this.k.a() && this.l.a();
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.f244c, this.h);
            com.jirbo.adcolony.a.l.f219c.a(this.g, this.i);
            this.k.b();
            this.l.b();
        }
    }

    /* renamed from: com.jirbo.adcolony.n$n, reason: collision with other inner class name */
    static class C0075n {
        int a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f242c;
        int d;
        int e;
        int f;
        int g;
        int h;

        C0075n() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.g("daily_play_cap");
            this.b = gVar.g("custom_play_cap");
            this.f242c = gVar.g("custom_play_cap_period");
            this.d = gVar.g("total_play_cap");
            this.e = gVar.g("monthly_play_cap");
            this.f = gVar.g("weekly_play_cap");
            this.g = gVar.g("volatile_expiration");
            this.h = gVar.g("volatile_play_cap");
            return true;
        }
    }

    static class m {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f241c;
        String d;
        String e;
        String f;
        String g;
        l h;
        g i;
        g j;
        g k;
        g l;
        g m;

        m() {
        }

        boolean a() {
            return com.jirbo.adcolony.a.l.f219c.a(this.a) && com.jirbo.adcolony.a.l.f219c.a(this.f241c) && com.jirbo.adcolony.a.l.f219c.a(this.e) && this.h.a() && this.i.a() && this.j.a() && this.k.a() && this.l.a() && this.m.a();
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.e("tiny_glow_image");
            this.b = gVar.e("tiny_glow_image_last_modified;");
            this.f241c = gVar.e("background_bar_image");
            this.d = gVar.e("background_bar_image_last_modified");
            this.e = gVar.e("background_tile_image");
            this.f = gVar.e("background_tile_image_last_modified");
            this.g = gVar.e("background_color");
            this.h = new l();
            if (!this.h.a(gVar.b("logo"))) {
                return false;
            }
            this.h = new l();
            if (!this.h.a(gVar.b("logo"))) {
                return false;
            }
            this.i = new g();
            if (!this.i.a(gVar.b("stop"))) {
                return false;
            }
            this.j = new g();
            if (!this.j.a(gVar.b("back"))) {
                return false;
            }
            this.k = new g();
            if (!this.k.a(gVar.b("close"))) {
                return false;
            }
            this.l = new g();
            if (!this.l.a(gVar.b("forward"))) {
                return false;
            }
            this.m = new g();
            return this.m.a(gVar.b("reload"));
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.a, this.b);
            com.jirbo.adcolony.a.l.f219c.a(this.f241c, this.d);
            com.jirbo.adcolony.a.l.f219c.a(this.e, this.f);
            this.h.b();
            this.i.b();
            this.j.b();
            this.k.b();
            this.l.b();
            this.m.b();
        }
    }

    static class ad {
        boolean a;
        z b;

        /* renamed from: c, reason: collision with root package name */
        int f231c;
        String d;
        boolean e;
        int f;

        ad() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = new z();
            if (!this.b.a(gVar.b("limits"))) {
                return false;
            }
            this.f231c = gVar.g("reward_amount");
            this.d = gVar.e("reward_name");
            this.e = gVar.h("client_side");
            this.f = gVar.g("videos_per_reward");
            return true;
        }
    }

    static class z {
        int a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f254c;

        z() {
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.g("daily_play_cap");
            this.b = gVar.g("custom_play_cap");
            this.f254c = gVar.g("custom_play_cap_period");
            return true;
        }
    }

    static class c {
        boolean a;
        u b;

        /* renamed from: c, reason: collision with root package name */
        s f233c;

        c() {
        }

        boolean a() {
            return this.b.a() && this.f233c.a();
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = new u();
            if (!this.b.a(gVar.b("pre_popup"))) {
                return false;
            }
            this.f233c = new s();
            return this.f233c.a(gVar.b("post_popup"));
        }

        void b() {
            if (this.a) {
                this.b.b();
                this.f233c.b();
            }
        }
    }

    static class u {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        l f249c;
        t d;

        u() {
        }

        boolean a() {
            return com.jirbo.adcolony.a.l.f219c.a(this.a) && this.f249c.a() && this.d.a();
        }

        boolean a(ADCData.g gVar) {
            this.a = gVar.e("background_image");
            this.b = gVar.e("background_image_last_modified");
            this.f249c = new l();
            if (!this.f249c.a(gVar.b("background_logo"))) {
                return false;
            }
            this.d = new t();
            return this.d.a(gVar.b("dialog"));
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.a, this.b);
            this.f249c.b();
            this.d.b();
        }
    }

    static class t {
        int a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        int f248c;
        int d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        l l;
        g m;
        g n;

        t() {
        }

        boolean a() {
            return com.jirbo.adcolony.a.l.f219c.a(this.e) && this.l.a() && this.m.a();
        }

        boolean a(ADCData.g gVar) {
            this.a = gVar.g("scale");
            this.b = gVar.e("label_reward");
            this.f248c = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.d = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.e = gVar.e("image");
            this.f = gVar.e("image_last_modified");
            this.g = gVar.e(PlusShare.KEY_CALL_TO_ACTION_LABEL);
            this.h = gVar.e("label_rgba");
            this.i = gVar.e("label_shadow_rgba");
            this.j = gVar.e("label_fraction");
            this.k = gVar.e("label_html");
            this.l = new l();
            if (!this.l.a(gVar.b("logo"))) {
                return false;
            }
            this.m = new g();
            if (!this.m.a(gVar.b("option_yes"))) {
                return false;
            }
            this.n = new g();
            return this.n.a(gVar.b("option_no"));
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.e, this.f);
            this.l.b();
            this.m.b();
            this.n.b();
        }
    }

    static class s {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        l f247c;
        r d;

        s() {
        }

        boolean a() {
            return com.jirbo.adcolony.a.l.f219c.a(this.a) && this.f247c.a() && this.d.a();
        }

        boolean a(ADCData.g gVar) {
            this.a = gVar.e("background_image");
            this.b = gVar.e("background_image_last_modified");
            this.f247c = new l();
            if (!this.f247c.a(gVar.b("background_logo"))) {
                return false;
            }
            this.d = new r();
            return this.d.a(gVar.b("dialog"));
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.a, this.b);
            this.d.b();
        }
    }

    static class r {
        int a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        int f246c;
        int d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        l l;
        g m;

        r() {
        }

        boolean a() {
            return com.jirbo.adcolony.a.l.f219c.a(this.e) && this.l.a() && this.m.a();
        }

        boolean a(ADCData.g gVar) {
            this.a = gVar.g("scale");
            this.b = gVar.e("label_reward");
            this.f246c = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.d = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.e = gVar.e("image");
            this.f = gVar.e("image_last_modified");
            this.g = gVar.e(PlusShare.KEY_CALL_TO_ACTION_LABEL);
            this.h = gVar.e("label_rgba");
            this.i = gVar.e("label_shadow_rgba");
            this.j = gVar.e("label_fraction");
            this.k = gVar.e("label_html");
            this.l = new l();
            if (!this.l.a(gVar.b("logo"))) {
                return false;
            }
            this.m = new g();
            return this.m.a(gVar.b("option_done"));
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.e, this.f);
            this.l.b();
            this.m.b();
        }
    }

    static class l {
        int a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f240c;
        String d;
        String e;
        boolean f;

        l() {
        }

        boolean a() {
            if (this.f) {
                return com.jirbo.adcolony.a.l.f219c.a(this.d);
            }
            return true;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.f = gVar.a("enabled", true);
            this.a = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.b = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.f240c = gVar.g("scale");
            this.d = gVar.e("image");
            this.e = gVar.e("image_last_modified");
            if (!this.e.equals("")) {
                return true;
            }
            this.e = gVar.e("last_modified");
            return true;
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.d, this.e);
        }
    }

    static class k {
        boolean a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f239c;
        String d;

        k() {
        }

        boolean a() {
            return !this.a || com.jirbo.adcolony.a.l.f219c.a(this.f239c);
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.a("enabled", false);
            this.f239c = gVar.e("file_url");
            this.d = gVar.e("last_modified");
            return true;
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.f239c, this.d);
        }
    }

    static class g {
        boolean a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f236c;
        int d;
        int e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        String l;
        String m;
        String n;
        String o;
        String p;

        g() {
        }

        boolean a() {
            if (this.a) {
                return com.jirbo.adcolony.a.l.f219c.a(this.f) && com.jirbo.adcolony.a.l.f219c.a(this.h);
            }
            return true;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.a("enabled", true);
            this.e = gVar.g("delay");
            this.b = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.f236c = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.d = gVar.g("scale");
            this.f = gVar.e("image_normal");
            this.g = gVar.e("image_normal_last_modified");
            this.h = gVar.e("image_down");
            this.i = gVar.e("image_down_last_modified");
            this.j = gVar.e("click_action");
            this.k = gVar.e("click_action_type");
            this.l = gVar.e(PlusShare.KEY_CALL_TO_ACTION_LABEL);
            this.m = gVar.e("label_rgba");
            this.n = gVar.e("label_shadow_rgba");
            this.o = gVar.e("label_html");
            this.p = gVar.e("event");
            return true;
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.f, this.g);
            com.jirbo.adcolony.a.l.f219c.a(this.h, this.i);
        }
    }

    static class h {
        String a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f237c;
        boolean d;
        boolean e;
        boolean f;
        double g;
        v h;
        j i;

        h() {
        }

        boolean a() {
            if (this.i.a && !this.i.a()) {
                return false;
            }
            if (this.d) {
                return this.h.a() || this.i.a();
            }
            return true;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.d = gVar.h("enabled");
            if (!this.d) {
                return true;
            }
            this.a = gVar.e("uuid");
            this.b = gVar.g("ad_id");
            this.f237c = gVar.g("ad_campaign_id");
            this.e = gVar.h("dissolve");
            this.f = gVar.h("enable_in_app_store");
            this.g = gVar.f("dissolve_delay");
            this.h = new v();
            if (!this.h.a(gVar.b("static"))) {
                return false;
            }
            this.i = new j();
            return this.i.a(gVar.b("html5"));
        }

        void b() {
            if (this.d) {
                this.h.b();
                this.i.b();
            }
        }
    }

    static class v {
        boolean a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f250c;
        String d;
        String e;
        g f;
        g g;
        g h;
        g i;

        v() {
        }

        boolean a() {
            if (this.a) {
                return com.jirbo.adcolony.a.l.f219c.a(this.d) && this.h.a() && this.i.a() && this.g.a() && this.f.a();
            }
            return true;
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.f250c = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.d = gVar.e("background_image");
            this.e = gVar.e("background_image_last_modified");
            if (com.jirbo.adcolony.a.f != null) {
                this.d = com.jirbo.adcolony.a.f;
            }
            this.h = new g();
            if (!this.h.a(gVar.b("replay"))) {
                return false;
            }
            this.i = new g();
            if (!this.i.a(gVar.b("continue"))) {
                return false;
            }
            this.g = new g();
            if (!this.g.a(gVar.b("download"))) {
                return false;
            }
            this.f = new g();
            return this.f.a(gVar.b("info"));
        }

        void b() {
            if (this.a) {
                com.jirbo.adcolony.a.l.f219c.a(this.d, this.e);
                this.h.b();
                this.i.b();
                this.g.b();
                this.f.b();
            }
        }
    }

    static class j {
        boolean a;
        double b;

        /* renamed from: c, reason: collision with root package name */
        boolean f238c;
        boolean d;
        String e;
        o f;
        String g;
        l h;
        g i;
        g j;

        j() {
        }

        boolean a() {
            if (com.jirbo.adcolony.q.c()) {
                return this.a && this.f.a() && this.h.a() && this.i.a() && this.j.a();
            }
            com.jirbo.adcolony.a.ac = 8;
            return com.jirbo.adcolony.l.f226c.b("Ad not ready due to no network connection.");
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            this.b = gVar.f("load_timeout");
            this.f238c = gVar.h("load_timeout_enabled");
            this.d = gVar.h("load_spinner_enabled");
            this.e = gVar.e("background_color");
            this.g = gVar.e("html5_tag");
            this.f = new o();
            if (!this.f.a(gVar.b("mraid_js"))) {
                return false;
            }
            this.h = new l();
            if (!this.h.a(gVar.b("background_logo"))) {
                return false;
            }
            this.i = new g();
            if (!this.i.a(gVar.b("replay"))) {
                return false;
            }
            this.j = new g();
            return this.j.a(gVar.b("close"));
        }

        void b() {
            if (this.a) {
                if (this.f != null) {
                    this.f.b();
                }
                if (this.h != null) {
                    this.h.b();
                }
                if (this.i != null) {
                    this.i.b();
                }
                if (this.j != null) {
                    this.j.b();
                }
            }
        }
    }

    static class o {
        boolean a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f243c;

        o() {
        }

        boolean a() {
            return !this.a || com.jirbo.adcolony.a.l.f219c.a(this.b);
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = gVar.e("url");
            this.f243c = gVar.e("last_modified");
            return true;
        }

        void b() {
            com.jirbo.adcolony.a.l.f219c.a(this.b, this.f243c);
        }
    }

    static class aa {
        boolean a;
        int b;

        /* renamed from: c, reason: collision with root package name */
        int f229c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        double k;
        g l;
        g m;
        g n;
        k o;

        aa() {
        }

        boolean a() {
            if (!this.a) {
                return true;
            }
            if (com.jirbo.adcolony.a.l.f219c.a(this.d) && this.l.a() && this.m.a() && this.o.a() && this.n.a()) {
                if (com.jirbo.adcolony.a.l.b.j.i.equals("online") && !com.jirbo.adcolony.q.c()) {
                    com.jirbo.adcolony.a.ac = 9;
                    return com.jirbo.adcolony.l.f226c.b("Video not ready due to VIEW_FILTER_ONLINE");
                }
                if (com.jirbo.adcolony.a.l.b.j.i.equals("wifi") && !com.jirbo.adcolony.q.a()) {
                    com.jirbo.adcolony.a.ac = 9;
                    return com.jirbo.adcolony.l.f226c.b("Video not ready due to VIEW_FILTER_WIFI");
                }
                if (com.jirbo.adcolony.a.l.b.j.i.equals("cell") && !com.jirbo.adcolony.q.b()) {
                    com.jirbo.adcolony.a.ac = 9;
                    return com.jirbo.adcolony.l.f226c.b("Video not ready due to VIEW_FILTER_CELL");
                }
                if (!com.jirbo.adcolony.a.l.b.j.i.equals("offline") || !com.jirbo.adcolony.q.c()) {
                    return true;
                }
                com.jirbo.adcolony.a.ac = 9;
                return com.jirbo.adcolony.l.f226c.b("Video not ready due to VIEW_FILTER_OFFLINE");
            }
            return false;
        }

        String b() {
            return com.jirbo.adcolony.a.l.f219c.b(this.d);
        }

        boolean a(ADCData.g gVar) {
            if (gVar == null) {
                return false;
            }
            this.a = gVar.h("enabled");
            if (!this.a) {
                return true;
            }
            this.b = gVar.g(FluctConstants.XML_NODE_WIDTH);
            this.f229c = gVar.g(FluctConstants.XML_NODE_HEIGHT);
            this.d = gVar.e("url");
            this.e = gVar.e("last_modified");
            this.f = gVar.e("video_frame_rate");
            this.g = gVar.e("audio_channels");
            this.h = gVar.e("audio_codec");
            this.i = gVar.e("audio_sample_rate");
            this.j = gVar.e("video_codec");
            this.k = gVar.f("duration");
            this.l = new g();
            if (!this.l.a(gVar.b("skip_video"))) {
                return false;
            }
            this.m = new g();
            if (!this.m.a(gVar.b("in_video_engagement"))) {
                return false;
            }
            this.o = new k();
            if (!this.o.a(gVar.b("haptic"))) {
                return false;
            }
            this.n = new g();
            return this.n.a(gVar.b("in_video_engagement").b("image_overlay"));
        }

        void c() {
            com.jirbo.adcolony.a.l.f219c.a(this.d, this.e);
            this.m.b();
            this.l.b();
            this.o.b();
            this.n.b();
        }
    }
}
