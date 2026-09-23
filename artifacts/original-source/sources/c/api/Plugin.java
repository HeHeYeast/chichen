package c.api;

import c.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0105s;
import vpadn.C0108v;
import vpadn.InterfaceC0102p;

@Deprecated
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class Plugin extends C0103q {
    public C0105s ctx;

    public abstract C0108v execute(String str, JSONArray jSONArray, String str2);

    public boolean isSynch(String str) {
        return false;
    }

    @Override // vpadn.C0103q
    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super.initialize(interfaceC0102p, cordovaWebView);
        setContext(interfaceC0102p);
        setView(cordovaWebView);
    }

    public void setContext(InterfaceC0102p interfaceC0102p) {
        this.cordova = interfaceC0102p;
        this.ctx = new C0105s(this.cordova);
    }

    public void setView(CordovaWebView cordovaWebView) {
        this.webView = cordovaWebView;
    }

    @Override // vpadn.C0103q
    public boolean execute(final String str, final JSONArray jSONArray, C0101o c0101o) throws JSONException {
        final String str2 = c0101o.a;
        if (!isSynch(str)) {
            this.cordova.e().execute(new Runnable() { // from class: c.api.Plugin.1
                @Override // java.lang.Runnable
                public final void run() {
                    C0108v c0108v;
                    try {
                        c0108v = Plugin.this.execute(str, jSONArray, str2);
                    } catch (Throwable th) {
                        c0108v = new C0108v(C0108v.a.ERROR, th.getMessage());
                    }
                    Plugin.this.sendPluginResult(c0108v, str2);
                }
            });
        } else {
            C0108v c0108vExecute = execute(str, jSONArray, str2);
            if (c0108vExecute == null) {
                c0108vExecute = new C0108v(C0108v.a.NO_RESULT);
            }
            c0101o.a(c0108vExecute);
        }
        return true;
    }

    public void sendJavascript(String str) {
        this.webView.c(str);
    }

    public void sendPluginResult(C0108v c0108v, String str) {
        this.webView.a(c0108v, str);
    }

    public void success(C0108v c0108v, String str) {
        this.webView.a(c0108v, str);
    }

    public void success(JSONObject jSONObject, String str) {
        this.webView.a(new C0108v(C0108v.a.OK, jSONObject), str);
    }

    public void success(String str, String str2) {
        this.webView.a(new C0108v(C0108v.a.OK, str), str2);
    }

    public void error(C0108v c0108v, String str) {
        this.webView.a(c0108v, str);
    }

    public void error(JSONObject jSONObject, String str) {
        this.webView.a(new C0108v(C0108v.a.ERROR, jSONObject), str);
    }

    public void error(String str, String str2) {
        this.webView.a(new C0108v(C0108v.a.ERROR, str), str2);
    }
}
