package vpadn;

import android.content.Intent;
import c.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/* renamed from: vpadn.q, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class C0103q {
    private static /* synthetic */ boolean a;
    public InterfaceC0102p cordova;
    public String id;
    public CordovaWebView webView;

    static {
        a = !C0103q.class.desiredAssertionStatus();
    }

    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        if (!a && this.cordova != null) {
            throw new AssertionError();
        }
        this.cordova = interfaceC0102p;
        this.webView = cordovaWebView;
    }

    public boolean execute(String str, String str2, C0101o c0101o) throws JSONException {
        return execute(str, new JSONArray(str2), c0101o);
    }

    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        return execute(str, new C0089c(jSONArray), c0101o);
    }

    public boolean execute(String str, C0089c c0089c, C0101o c0101o) throws JSONException {
        return false;
    }

    public void onPause(boolean z) {
    }

    public void onResume(boolean z) {
    }

    public void onNewIntent(Intent intent) {
    }

    public void onDestroy() {
    }

    public Object onMessage(String str, Object obj) {
        return null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public boolean onOverrideUrlLoading(String str) {
        return false;
    }

    public void onReset() {
    }
}
