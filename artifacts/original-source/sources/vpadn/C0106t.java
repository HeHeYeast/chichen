package vpadn;

import c.CordovaWebView;

/* renamed from: vpadn.t, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0106t {
    public String a;
    public C0103q b = null;

    /* renamed from: c, reason: collision with root package name */
    public boolean f348c;
    private String d;

    public C0106t(String str, String str2, boolean z) {
        this.a = "";
        this.d = "";
        this.f348c = false;
        this.a = str;
        this.d = str2;
        this.f348c = z;
    }

    public final C0103q a(CordovaWebView cordovaWebView, InterfaceC0102p interfaceC0102p) {
        if (this.b != null) {
            return this.b;
        }
        try {
            String str = this.d;
            Class<?> cls = str != null ? Class.forName(str) : null;
            if (cls != null ? C0103q.class.isAssignableFrom(cls) : false) {
                this.b = (C0103q) cls.newInstance();
                this.b.initialize(interfaceC0102p, cordovaWebView);
                return this.b;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding plugin " + this.d + ".");
        }
        return null;
    }
}
