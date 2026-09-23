package vpadn;

import c.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;
import vpadn.C0108v;

/* renamed from: vpadn.o, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0101o {
    public String a;
    boolean b;

    /* renamed from: c, reason: collision with root package name */
    private CordovaWebView f347c;

    public C0101o(String str, CordovaWebView cordovaWebView) {
        this.a = str;
        this.f347c = cordovaWebView;
    }

    public final String a() {
        return this.a;
    }

    public final void a(C0108v c0108v) {
        synchronized (this) {
            if (this.b) {
                ab.d("CordovaPlugin", "Attempted to send a second callback for ID: " + this.a + "\nResult was: " + c0108v.c());
            } else {
                this.b = !c0108v.e();
                this.f347c.a(c0108v, this.a);
            }
        }
    }

    public final void a(JSONObject jSONObject) {
        a(new C0108v(C0108v.a.OK, jSONObject));
    }

    public final void a(String str) {
        a(new C0108v(C0108v.a.OK, str));
    }

    public final void a(JSONArray jSONArray) {
        a(new C0108v(C0108v.a.OK, jSONArray));
    }

    public final void b() {
        a(new C0108v(C0108v.a.OK));
    }

    public final void b(JSONObject jSONObject) {
        a(new C0108v(C0108v.a.ERROR, jSONObject));
    }

    public final void b(String str) {
        a(new C0108v(C0108v.a.ERROR, str));
    }

    public final void a(int i) {
        a(new C0108v(C0108v.a.ERROR, i));
    }
}
