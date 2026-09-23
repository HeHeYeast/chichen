package vpadn;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebView;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class C implements E, InterfaceC0102p {
    protected Activity a;
    protected String b;
    protected String d;
    protected String e;
    protected long j;
    protected long k;

    /* renamed from: c, reason: collision with root package name */
    protected VpadnAdSize f308c = null;
    protected boolean f = false;
    private final ExecutorService o = Executors.newCachedThreadPool();
    private Map<String, Map<Integer, C0101o>> p = Collections.synchronizedMap(new HashMap());
    protected Map<String, String> g = Collections.synchronizedMap(new HashMap());
    protected boolean h = false;
    protected JSONObject i = new JSONObject();
    protected long l = -1;
    protected VpadnAdRequest m = null;
    protected boolean n = false;

    protected abstract void a(Object obj);

    protected abstract void n();

    public C(Activity activity) {
        this.e = null;
        this.a = activity;
        if (P.a().a("user-agent") == null) {
            this.e = String.valueOf(new WebView(this.a).getSettings().getUserAgentString()) + " (Mobile; vpadn-sdk-a-v4.2.5)";
            P.a().a("user-agent", new String(this.e));
        } else {
            this.e = (String) P.a().a("user-agent");
        }
        C0086a.c(this.a);
    }

    public final void b() {
        this.p.clear();
    }

    public void d(String str) {
        this.b = str;
    }

    public final void a(VpadnAdSize vpadnAdSize) {
        this.f308c = vpadnAdSize;
    }

    public final void e(String str) {
        this.d = str;
    }

    @Override // vpadn.E
    public final void a(long j) {
        this.l = j;
    }

    @Override // vpadn.E
    public final void a(String str) {
        this.g.put("url_type_banner", str);
    }

    @Override // vpadn.E
    public final void b(String str) {
        this.g.put("url_type_click", str);
    }

    @Override // vpadn.E
    public final void c(String str) {
        this.g.put("url_type_impression", str);
    }

    @Override // vpadn.E
    public final String k() {
        return this.g.remove("url_type_click");
    }

    @Override // vpadn.E
    public final void a(String str, int i, C0101o c0101o) {
        try {
            if (str.startsWith("video_")) {
                ab.b("AbstractVponController", "video event cannot add to AbstractVponController.addEventListener" + str);
                c0101o.b(new JSONObject().put("e", "add video EventType not supported!"));
                return;
            }
            if (!"onhide".equals(str) && !"onshow".equals(str) && !"ad_pos_change".equals(str)) {
                ab.b("AbstractVponController", "EventType not supported! " + str);
                c0101o.b(new JSONObject().put("e", "add EventType not supported!"));
                return;
            }
            Map<Integer, C0101o> map = this.p.get(str);
            if (map == null) {
                HashMap map2 = new HashMap();
                map2.put(Integer.valueOf(i), c0101o);
                this.p.put(str, map2);
            } else {
                map.put(Integer.valueOf(i), c0101o);
            }
            if ("ad_pos_change".equals(str)) {
                C0108v c0108v = new C0108v(C0108v.a.OK, this.i);
                c0108v.a(true);
                c0101o.a(c0108v);
                return;
            }
            if ("onshow".equals(str)) {
                if (this.h) {
                    C0108v c0108v2 = new C0108v(C0108v.a.OK);
                    c0108v2.a(true);
                    c0101o.a(c0108v2);
                    ab.a("AbstractVponController", "IS SHOW!!");
                    return;
                }
                return;
            }
            if ("onhide".equals(str) && !this.h) {
                C0108v c0108v3 = new C0108v(C0108v.a.OK);
                c0108v3.a(true);
                c0101o.a(c0108v3);
                ab.a("AbstractVponController", "IS HIDE!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                c0101o.b(new JSONObject().put("e", "addEventListener throw Exception:" + e.getMessage()));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }

    @Override // vpadn.E
    public final void b(String str, int i, C0101o c0101o) {
        try {
            if (str.startsWith("video_")) {
                ab.b("AbstractVponController", "video event cannot remove to AbstractVponController.removeEventListener" + str);
                c0101o.b(new JSONObject().put("e", "remove video EventType not supported!"));
                return;
            }
            if (!"onhide".equals(str) && !"onshow".equals(str) && !"ad_pos_change".equals(str)) {
                ab.b("AbstractVponController", "remove EventType not supported! " + str);
                c0101o.b(new JSONObject().put("e", "remove EventType not supported!"));
                return;
            }
            if (this.p.containsKey(str)) {
                Map<Integer, C0101o> map = this.p.get(str);
                map.remove(Integer.valueOf(i));
                if (map.size() == 0) {
                    this.p.remove(str);
                }
                c0101o.b();
            }
            ab.b("AbstractVponController", "Cannot find event type in event listenerMap to remove! " + str);
            c0101o.b(new JSONObject().put("e", "Cannot find event type in event listenerMap to remove!"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                c0101o.b(new JSONObject().put("e", "removeEventListener throw Exception:" + e.getMessage()));
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }

    @Override // vpadn.E
    public void h() {
        this.h = true;
        a("onshow", (JSONObject) null);
    }

    @Override // vpadn.E
    public final void i() {
        this.h = false;
        a("onhide", (JSONObject) null);
    }

    @Override // vpadn.E
    public final JSONObject g() throws JSONException {
        JSONObject jSONObjectA;
        Exception e;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObjectA = N.a().a(this.a, jSONObject);
        } catch (Exception e2) {
            jSONObjectA = jSONObject;
            e = e2;
        }
        try {
            jSONObjectA.put("sid", this.j);
            jSONObjectA.put("seq", this.k);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return jSONObjectA;
        }
        return jSONObjectA;
    }

    protected final void a(String str, JSONObject jSONObject) {
        C0108v c0108v;
        if (jSONObject != null) {
            ab.c("AbstractVponController", "Banner TriggerEvent eventType:" + str + " retObj:" + jSONObject.toString());
        } else {
            ab.c("AbstractVponController", "Banner TriggerEvent eventType:" + str);
        }
        if (this.p.get(str) != null) {
            Iterator<C0101o> it = this.p.get(str).values().iterator();
            if (jSONObject != null) {
                c0108v = new C0108v(C0108v.a.OK, jSONObject);
            } else {
                c0108v = new C0108v(C0108v.a.OK);
            }
            c0108v.a(true);
            while (it.hasNext()) {
                it.next().a(c0108v);
            }
        }
    }

    protected final void o() {
        O oA = O.a();
        this.j = oA.b();
        this.k = oA.c();
    }

    protected final long p() {
        return this.j;
    }

    protected final long q() {
        return this.k;
    }

    @Override // vpadn.InterfaceC0102p
    public final void a(C0103q c0103q, Intent intent, int i) {
        this.a.startActivity(intent);
    }

    @Override // vpadn.InterfaceC0102p
    public final Activity a() {
        return this.a;
    }

    @Override // vpadn.InterfaceC0102p
    public final ExecutorService e() {
        return this.o;
    }

    @Override // vpadn.InterfaceC0102p
    public final Object a(String str, Object obj) {
        if (str.equals("load_banner")) {
            n();
            return null;
        }
        if (str.equals("load_banner_fail")) {
            a(obj);
            return null;
        }
        return null;
    }

    protected final boolean r() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.a.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
