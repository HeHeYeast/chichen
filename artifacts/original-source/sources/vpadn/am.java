package vpadn;

import android.content.Intent;
import android.net.Uri;
import c.NetworkManager;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.ar;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class am extends af {
    private JSONObject a;
    private String b;

    /* renamed from: c, reason: collision with root package name */
    private String f323c;
    private as d;
    private ar.a.EnumC0081a e;

    am(as asVar, JSONObject jSONObject, ar.a.EnumC0081a enumC0081a, String str, String str2) {
        super(asVar, asVar.d(), str2);
        this.a = jSONObject;
        this.d = asVar;
        this.e = enumC0081a;
        this.b = str;
        if (this.e == null) {
            this.e = ar.a.EnumC0081a.OUTAPP;
        }
        if (this.a != null && this.a.has("u")) {
            try {
                this.f323c = this.a.getString("u");
            } catch (JSONException e) {
                ab.b("OpenWebCommand", "Cannot get URL");
            }
        }
    }

    @Override // vpadn.af
    public final void b() throws JSONException {
        if (this.b != null) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.b));
                intent.addFlags(268435456);
                this.d.d().startActivity(intent);
                return;
            } catch (Exception e) {
                ab.a("OpenWebCommand", "start activity with appUrl is failed", e);
                if (this.e == ar.a.EnumC0081a.OUTAPP) {
                    d();
                    return;
                } else {
                    if (this.e == ar.a.EnumC0081a.INAPP) {
                        e();
                        return;
                    }
                    return;
                }
            }
        }
        if (this.e == ar.a.EnumC0081a.OUTAPP) {
            d();
        } else if (this.e == ar.a.EnumC0081a.INAPP) {
            e();
        }
    }

    private void d() {
        if (this.f323c == null) {
            ab.b("OpenWebCommand", "OpenWebCommand.execute(doOpenAppOrBrowser): URL IS null");
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.f323c));
            intent.addFlags(268435456);
            this.d.d().startActivity(intent);
        } catch (Exception e) {
            ab.a("OpenWebCommand", "OpenWebCommand.execute(doOpenAppOrBrowser) throws Exception", e);
        }
    }

    private void e() throws JSONException {
        final boolean z;
        final boolean z2;
        final boolean z3;
        final boolean z4;
        final boolean z5;
        try {
            if (this.a == null) {
                ab.b("OpenWebCommand", "actionButtonDataJsonObj == null");
                return;
            }
            if (this.f323c == null) {
                ab.b("OpenWebCommand", "OpenWebCommand.execute(doOpenWebAppStep1): URL IS null");
                return;
            }
            final String string = null;
            if (this.a.has("html")) {
                string = this.a.getString("html");
            }
            if (this.a.has("custom_close")) {
                z = this.a.getInt("custom_close") > 0;
            } else {
                z = false;
            }
            if (this.a.has("allow_orientation_change")) {
                z2 = this.a.getInt("allow_orientation_change") > 0;
            } else {
                z2 = true;
            }
            final String string2 = NetworkManager.TYPE_NONE;
            if (this.a.has("force_orientation")) {
                string2 = this.a.getString("force_orientation");
            }
            final int i = 16777215;
            if (this.a.has("bk_c")) {
                i = this.a.getInt("bk_c");
            }
            if (this.a.has("show_prog_bar")) {
                z3 = this.a.getInt("show_prog_bar") > 0;
            } else {
                z3 = false;
            }
            if (this.a.has("show_nav_bar")) {
                z4 = this.a.getInt("show_nav_bar") > 0;
            } else {
                z4 = false;
            }
            if (this.a.has("use_webview_load_url")) {
                z5 = this.a.getInt("use_webview_load_url") > 0;
            } else {
                z5 = false;
            }
            if (this.f323c == null && string == null) {
                ab.b("OpenWebCommand", "u is null and html is null");
            } else {
                final String str = this.f323c;
                this.d.d().runOnUiThread(new Runnable() { // from class: vpadn.am.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        am.this.d.d().a(str, string, z, z2, string2, i, z3, z4, z5);
                    }
                });
            }
        } catch (Exception e) {
            ab.a("OpenWebCommand", "throws exception in doOpenWebAppStep1", e);
        }
    }
}
