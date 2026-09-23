package vpadn;

import android.content.Intent;
import c.CordovaWebView;
import c.Device;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import vpadn.C0108v;

/* renamed from: vpadn.u, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0107u {
    private static String a = "PluginManager";

    /* renamed from: c, reason: collision with root package name */
    private final InterfaceC0102p f349c;
    private final CordovaWebView d;
    private final ConcurrentHashMap<String, C0106t> b = new ConcurrentHashMap<>();
    private HashMap<String, String> f = new HashMap<>();
    private HashMap<String, String> g = new HashMap<>();
    private boolean e = true;

    public C0107u(CordovaWebView cordovaWebView, InterfaceC0102p interfaceC0102p) {
        this.f349c = interfaceC0102p;
        this.d = cordovaWebView;
        this.g.put("App", "c.App");
        this.g.put("Geolocation", "c.GeoBroker");
        this.g.put(Device.TAG, "c.Device");
        this.g.put("Accelerometer", "c.AccelListener");
        this.g.put("Compass", "c.CompassListener");
        this.g.put("Media", "c.AudioHandler");
        this.g.put("Camera", "c.CameraLauncher");
        this.g.put("File", "c.FileUtils");
        this.g.put("NetworkStatus", "c.NetworkManager");
        this.g.put("Notification", "c.Notification");
        this.g.put("Storage", "c.Storage");
        this.g.put("FileTransfer", "c.FileTransfer");
        this.g.put("Capture", "c.Capture");
        this.g.put("Battery", "c.BatteryListener");
        this.g.put("Echo", "c.Echo");
        this.g.put("Globalization", "c.Globalization");
        this.g.put("InAppBrowser", "c.InAppBrowser");
        this.g.put("VponSdk", "com.vpon.cordova.VponSDKPlugIn");
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x01e8 A[PHI: r0 r1
  0x01e8: PHI (r0v23 java.lang.String) = 
  (r0v20 java.lang.String)
  (r0v20 java.lang.String)
  (r0v20 java.lang.String)
  (r0v20 java.lang.String)
  (r0v32 java.lang.String)
  (r0v32 java.lang.String)
 binds: [B:50:0x018d, B:52:0x0199, B:36:0x013d, B:37:0x013f, B:42:0x0159, B:44:0x015f] A[DONT_GENERATE, DONT_INLINE]
  0x01e8: PHI (r1v12 java.lang.String) = 
  (r1v9 java.lang.String)
  (r1v9 java.lang.String)
  (r1v9 java.lang.String)
  (r1v9 java.lang.String)
  (r1v19 java.lang.String)
  (r1v19 java.lang.String)
 binds: [B:50:0x018d, B:52:0x0199, B:36:0x013d, B:37:0x013f, B:42:0x0159, B:44:0x015f] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a() {
        /*
            Method dump skipped, instructions count: 493
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: vpadn.C0107u.a():void");
    }

    public final boolean a(String str, String str2, String str3, String str4) {
        boolean z = true;
        C0103q c0103qA = a(str);
        if (c0103qA == null) {
            this.d.a(new C0108v(C0108v.a.CLASS_NOT_FOUND_EXCEPTION), str3);
        } else {
            try {
                C0101o c0101o = new C0101o(str3, this.d);
                if (!c0103qA.execute(str2, str4, c0101o)) {
                    this.d.a(new C0108v(C0108v.a.INVALID_ACTION), str3);
                } else {
                    z = c0101o.b;
                }
            } catch (JSONException e) {
                this.d.a(new C0108v(C0108v.a.JSON_EXCEPTION), str3);
            }
        }
        return z;
    }

    public final C0103q a(String str) {
        C0106t c0106t = this.b.get(str);
        if (c0106t == null) {
            return null;
        }
        C0103q c0103q = c0106t.b;
        return c0103q == null ? c0106t.a(this.d, this.f349c) : c0103q;
    }

    private void a(C0106t c0106t) {
        this.b.put(c0106t.a, c0106t);
    }

    public final void a(boolean z) {
        for (C0106t c0106t : this.b.values()) {
            if (c0106t.b != null) {
                c0106t.b.onPause(z);
            }
        }
    }

    public final void b(boolean z) {
        for (C0106t c0106t : this.b.values()) {
            if (c0106t.b != null) {
                c0106t.b.onResume(z);
            }
        }
    }

    public final void b() {
        for (C0106t c0106t : this.b.values()) {
            if (c0106t.b != null) {
                c0106t.b.onDestroy();
            }
        }
    }

    public final Object a(String str, Object obj) {
        Object objOnMessage;
        Object objA = this.f349c.a(str, obj);
        if (objA == null) {
            for (C0106t c0106t : this.b.values()) {
                if (c0106t.b != null && (objOnMessage = c0106t.b.onMessage(str, obj)) != null) {
                    return objOnMessage;
                }
            }
            return null;
        }
        return objA;
    }

    public final void a(Intent intent) {
        for (C0106t c0106t : this.b.values()) {
            if (c0106t.b != null) {
                c0106t.b.onNewIntent(intent);
            }
        }
    }

    public final boolean b(String str) {
        for (Map.Entry<String, String> entry : this.f.entrySet()) {
            if (str.startsWith(entry.getKey())) {
                return a(entry.getValue()).onOverrideUrlLoading(str);
            }
        }
        return false;
    }

    public final void c() {
        Iterator<C0106t> it = this.b.values().iterator();
        while (it.hasNext()) {
            C0103q c0103q = it.next().b;
            if (c0103q != null) {
                c0103q.onReset();
            }
        }
    }
}
