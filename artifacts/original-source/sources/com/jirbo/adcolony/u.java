package com.jirbo.adcolony;

import com.immersion.hapticmediasdk.HapticContentSDK;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.ADCDownload;
import com.jirbo.adcolony.n;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class u implements ADCDownload.Listener {
    d a;
    ArrayList<a> b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    ArrayList<a> f262c = new ArrayList<>();
    int d = 0;
    boolean e = false;

    u(d dVar) {
        this.a = dVar;
    }

    void a(String str, AdColonyAd adColonyAd) {
        if (this.a != null && this.a.b != null && this.a.b.j != null && this.a.b.j.n != null && this.a.b.j.n.a(str) != null) {
            l.a.a("Ad request for zone ").b((Object) str);
            n.ab abVarA = this.a.b.j.n.a(str);
            if (abVarA != null && abVarA.h != null && abVarA.h.a != null) {
                ADCData.g gVar = new ADCData.g();
                if (com.jirbo.adcolony.a.ac == 0) {
                    gVar.b("request_denied", false);
                } else {
                    gVar.b("request_denied", true);
                }
                gVar.b("request_denied_reason", com.jirbo.adcolony.a.ac);
                a("request", abVarA.h.a, gVar, adColonyAd);
                l.a.a("Tracking ad request - URL : ").b((Object) abVarA.h.a);
            }
        }
    }

    void a(String str, ADCData.g gVar) {
        n.f fVar = this.a.b.j.k;
        if (fVar != null) {
            a(str, fVar.h.e(str), gVar);
        }
        n.w wVar = this.a.b.j.l;
        if (wVar != null) {
            a(str, wVar.d.get(str));
        }
    }

    void b(String str, AdColonyAd adColonyAd) {
        a(str, (ADCData.g) null, adColonyAd);
    }

    void a(String str, ADCData.g gVar, AdColonyAd adColonyAd) {
        if (str == null) {
            l.d.b((Object) "No such event type:").b((Object) str);
            return;
        }
        if (str.equals("start") || str.equals("native_start")) {
            com.jirbo.adcolony.a.l.e.i++;
        }
        if (gVar == null) {
            gVar = new ADCData.g();
            gVar.b("replay", adColonyAd.s);
        }
        gVar.b("s_imp_count", com.jirbo.adcolony.a.l.e.i);
        a(str, adColonyAd.i.t.D.e(str), gVar, adColonyAd);
        a(str, adColonyAd.i.q.C.get(str));
    }

    void a(String str, String str2, ADCData.g gVar) {
        a(str, str2, gVar, null);
    }

    void a(String str, String str2, ADCData.g gVar, AdColonyAd adColonyAd) {
        if (str2 != null && !str2.equals("")) {
            if (gVar == null) {
                gVar = new ADCData.g();
            }
            String strB = ab.b();
            if (adColonyAd != null) {
                gVar.b("asi", adColonyAd.l);
            }
            gVar.b("sid", this.a.e.j);
            gVar.b("guid", strB);
            gVar.b("guid_key", ab.b(strB + "DUBu6wJ27y6xs7VWmNDw67DD"));
            a aVar = new a();
            aVar.a = str;
            aVar.b = str2;
            l.a.b((Object) "EVENT ---------------------------");
            l.a.a("EVENT - TYPE = ").b((Object) str);
            l.a.a("EVENT - URL  = ").b((Object) str2);
            aVar.f263c = gVar.q();
            if (str.equals("reward_v4vc")) {
                aVar.d = gVar.e("v4vc_name");
                aVar.h = gVar.g("v4vc_amount");
            }
            this.b.add(aVar);
            this.e = true;
            com.jirbo.adcolony.a.r = true;
        }
    }

    void a(String str, ArrayList<String> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < arrayList.size()) {
                    String str2 = arrayList.get(i2);
                    a aVar = new a();
                    aVar.a = str;
                    aVar.b = str2;
                    aVar.k = true;
                    this.b.add(aVar);
                    i = i2 + 1;
                } else {
                    this.e = true;
                    com.jirbo.adcolony.a.r = true;
                    return;
                }
            }
        }
    }

    void a(double d, AdColonyAd adColonyAd) {
        double d2 = adColonyAd.n;
        if (d >= d2) {
            if (d2 < 0.25d && d >= 0.25d) {
                if (AdColony.isZoneV4VC(adColonyAd.g) || !adColonyAd.k.equals("native")) {
                    b("first_quartile", adColonyAd);
                } else {
                    b("native_first_quartile", adColonyAd);
                }
            }
            if (d2 < 0.5d && d >= 0.5d) {
                if (AdColony.isZoneV4VC(adColonyAd.g) || !adColonyAd.k.equals("native")) {
                    b("midpoint", adColonyAd);
                } else {
                    b("native_midpoint", adColonyAd);
                }
            }
            if (d2 < 0.75d && d >= 0.75d) {
                if (AdColony.isZoneV4VC(adColonyAd.g) || !adColonyAd.k.equals("native")) {
                    b("third_quartile", adColonyAd);
                } else {
                    b("native_third_quartile", adColonyAd);
                }
            }
            if (d2 < 1.0d && d >= 1.0d && !adColonyAd.k.equals("native")) {
                l.a.a("Tracking ad event - complete");
                ADCData.g gVar = new ADCData.g();
                if (adColonyAd.r) {
                    gVar.b("ad_slot", adColonyAd.h.k.d);
                } else {
                    gVar.b("ad_slot", adColonyAd.h.k.d);
                }
                gVar.b("replay", adColonyAd.s);
                a("complete", gVar, adColonyAd);
            }
            adColonyAd.n = d;
        }
    }

    void a() {
        b();
        this.d = 0;
    }

    void b() {
        com.jirbo.adcolony.a.r = true;
        ADCData.c cVarC = k.c(new f("tracking_info.txt"));
        if (cVarC != null) {
            this.b.clear();
            for (int i = 0; i < cVarC.i(); i++) {
                ADCData.g gVarB = cVarC.b(i);
                a aVar = new a();
                aVar.a = gVarB.e("type");
                aVar.b = gVarB.e("url");
                aVar.f263c = gVarB.a("payload", (String) null);
                aVar.f = gVarB.g("attempts");
                aVar.k = gVarB.h("third_party");
                if (aVar.a.equals("v4vc_callback") || aVar.a.equals("reward_v4vc")) {
                    aVar.d = gVarB.e("v4vc_name");
                    aVar.h = gVarB.g("v4vc_amount");
                }
                this.b.add(aVar);
            }
        }
        l.a.a("Loaded ").a(this.b.size()).b((Object) " events");
    }

    void c() {
        this.f262c.clear();
        this.f262c.addAll(this.b);
        this.b.clear();
        ADCData.c cVar = new ADCData.c();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.f262c.size()) {
                a aVar = this.f262c.get(i2);
                if (!aVar.i) {
                    this.b.add(aVar);
                    ADCData.g gVar = new ADCData.g();
                    gVar.b("type", aVar.a);
                    gVar.b("url", aVar.b);
                    if (aVar.f263c != null) {
                        gVar.b("payload", aVar.f263c);
                    }
                    gVar.b("attempts", aVar.f);
                    if (aVar.d != null) {
                        gVar.b("v4vc_name", aVar.d);
                        gVar.b("v4vc_amount", aVar.h);
                    }
                    if (aVar.k) {
                        gVar.b("third_party", true);
                    }
                    cVar.a(gVar);
                }
                i = i2 + 1;
            } else {
                this.f262c.clear();
                l.a.a("Saving tracking_info (").a(this.b.size()).b((Object) " events)");
                k.a(new f("tracking_info.txt"), cVar);
                return;
            }
        }
    }

    void d() {
        if (this.e) {
            this.e = false;
            c();
        }
        e();
    }

    void e() {
        if (this.b.size() != 0) {
            while (this.b.size() > 1000) {
                this.b.remove(this.b.size() - 1);
            }
            if (q.c()) {
                double dC = ab.c();
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 < this.b.size()) {
                        a aVar = this.b.get(i2);
                        if (aVar.e < dC && !aVar.j) {
                            if (this.d != 5) {
                                this.d++;
                                aVar.j = true;
                                if (aVar.a.equals("v4vc_callback")) {
                                    com.jirbo.adcolony.a.Y.add(aVar.b);
                                }
                                ADCDownload aDCDownloadA = new ADCDownload(this.a, aVar.b, this).a(aVar);
                                if (aVar.k) {
                                    aDCDownloadA.h = true;
                                }
                                if (aVar.f263c != null) {
                                    aDCDownloadA.a("application/json", aVar.f263c);
                                }
                                l.b.a("Submitting '").a(aVar.a).b((Object) "' event.");
                                aDCDownloadA.b();
                                com.jirbo.adcolony.a.r = true;
                            } else {
                                return;
                            }
                        }
                        i = i2 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    @Override // com.jirbo.adcolony.ADCDownload.Listener
    public void on_download_finished(ADCDownload download) {
        int i = HapticContentSDK.f17b04440444044404440444;
        com.jirbo.adcolony.a.r = true;
        this.e = true;
        this.d--;
        a aVar = (a) download.e;
        l.a.a("on_download_finished - event.type = ").b((Object) aVar.a);
        aVar.j = false;
        boolean zEquals = download.i;
        if (zEquals && aVar.f263c != null) {
            ADCData.g gVarB = k.b(download.n);
            if (gVarB != null) {
                zEquals = gVarB.e("status").equals("success");
                if (zEquals && aVar.a.equals("reward_v4vc")) {
                    if (gVarB.h("v4vc_status")) {
                        String strE = gVarB.e("v4vc_callback");
                        if (strE.length() > 0) {
                            a aVar2 = new a();
                            aVar2.a = "v4vc_callback";
                            aVar2.b = strE;
                            aVar2.d = aVar.d;
                            aVar2.h = aVar.h;
                            this.b.add(aVar2);
                        } else {
                            if (com.jirbo.adcolony.a.K != null) {
                                com.jirbo.adcolony.a.K.o = true;
                            }
                            l.a.b((Object) "Client-side V4VC success");
                        }
                    } else {
                        l.a.b((Object) "Client-side V4VC failure");
                    }
                }
            } else {
                zEquals = false;
            }
        }
        if (zEquals && aVar.a.equals("v4vc_callback")) {
            l.a.b((Object) "v4vc_callback response:").b((Object) download.n);
            if (download.n.indexOf("vc_success") != -1) {
                if (com.jirbo.adcolony.a.K != null) {
                    com.jirbo.adcolony.a.K.o = true;
                }
                l.a.b((Object) "v4vc_callback success");
                this.a.a(true, aVar.d, aVar.h);
            } else if (download.n.indexOf("vc_decline") != -1 || download.n.indexOf("vc_noreward") != -1) {
                l.f226c.a("Server-side V4VC failure: ").b((Object) download.f200c);
                l.a.b((Object) "v4vc_callback declined");
                this.a.a(false, "", 0);
            } else {
                l.f226c.a("Server-side V4VC failure: ").b((Object) download.f200c);
                zEquals = false;
            }
        }
        if (zEquals) {
            l.a.a("Event submission SUCCESS for type: ").b((Object) aVar.a);
            aVar.i = true;
        } else {
            l.a.a("Event submission FAILED for type: ").a(aVar.a).a(" on try ").b(aVar.f + 1);
            aVar.f++;
            if (aVar.f >= 24) {
                l.d.b((Object) "Discarding event after 24 attempts to report.");
                aVar.i = true;
                if (aVar.a.equals("v4vc_callback")) {
                    this.a.a(false, "", 0);
                }
            } else {
                int i2 = aVar.g > 0 ? aVar.g * 3 : 20;
                if (i2 <= 10000) {
                    i = i2;
                }
                l.a.a("Retrying in ").a(i).a(" seconds (attempt ").a(aVar.f).b((Object) ")");
                aVar.g = i;
                aVar.e = ab.c() + i;
            }
        }
        if (!this.a.e.b) {
            c();
        }
    }

    static class a {
        String a;
        String b;

        /* renamed from: c, reason: collision with root package name */
        String f263c;
        String d;
        double e;
        int f;
        int g;
        int h;
        boolean i;
        boolean j;
        boolean k;

        a() {
        }
    }
}
