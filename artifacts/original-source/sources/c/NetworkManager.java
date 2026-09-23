package c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.json.JSONArray;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;
import vpadn.InterfaceC0102p;
import vpadn.ab;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NetworkManager extends C0103q {
    public static final String CDMA = "cdma";
    public static final String EDGE = "edge";
    public static final String EHRPD = "ehrpd";
    public static final String GPRS = "gprs";
    public static final String GSM = "gsm";
    public static final String HSDPA = "hsdpa";
    public static final String HSPA = "hspa";
    public static final String HSPA_PLUS = "hspa+";
    public static final String HSUPA = "hsupa";
    public static final String LTE = "lte";
    public static final String MOBILE = "mobile";
    public static final String ONEXRTT = "1xrtt";
    public static final String TYPE_2G = "2g";
    public static final String TYPE_3G = "3g";
    public static final String TYPE_4G = "4g";
    public static final String TYPE_ETHERNET = "ethernet";
    public static final String TYPE_NONE = "none";
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_WIFI = "wifi";
    public static final String UMB = "umb";
    public static final String UMTS = "umts";
    public static final String WIFI = "wifi";
    public static final String WIMAX = "wimax";
    ConnectivityManager a;
    private C0101o b;
    public static int NOT_REACHABLE = 0;
    public static int REACHABLE_VIA_CARRIER_DATA_NETWORK = 1;
    public static int REACHABLE_VIA_WIFI_NETWORK = 2;

    /* renamed from: c, reason: collision with root package name */
    private boolean f178c = false;
    private String e = "";
    private BroadcastReceiver d = null;

    @Override // vpadn.C0103q
    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super.initialize(interfaceC0102p, cordovaWebView);
        this.a = (ConnectivityManager) interfaceC0102p.a().getSystemService("connectivity");
        this.b = null;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        if (this.d == null) {
            this.d = new BroadcastReceiver() { // from class: c.NetworkManager.1
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    if (NetworkManager.this.webView != null) {
                        NetworkManager.a(NetworkManager.this, NetworkManager.this.a.getActiveNetworkInfo());
                    }
                }
            };
            try {
                interfaceC0102p.a().registerReceiver(this.d, intentFilter);
                this.f178c = true;
            } catch (Exception e) {
                ab.a("NetworkManager", "NetworkManager.initialize method throw Exception:" + e.getMessage(), e);
                this.f178c = false;
            }
        }
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) {
        if (!str.equals("getConnectionInfo")) {
            return false;
        }
        this.b = c0101o;
        C0108v c0108v = new C0108v(C0108v.a.OK, a(this.a.getActiveNetworkInfo()));
        c0108v.a(true);
        c0101o.a(c0108v);
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        if (this.d != null && this.f178c) {
            try {
                this.cordova.a().unregisterReceiver(this.d);
                this.f178c = false;
            } catch (Exception e) {
                ab.a("NetworkManager", "Error unregistering network receiver: " + e.getMessage(), e);
            }
        }
    }

    static /* synthetic */ void a(NetworkManager networkManager, NetworkInfo networkInfo) {
        String strA = networkManager.a(networkInfo);
        if (strA.equals(networkManager.e)) {
            return;
        }
        if (networkManager.b != null) {
            C0108v c0108v = new C0108v(C0108v.a.OK, strA);
            c0108v.a(true);
            networkManager.b.a(c0108v);
        }
        networkManager.webView.a("networkconnection", (Object) strA);
        networkManager.e = strA;
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String a(android.net.NetworkInfo r5) {
        /*
            r4 = this;
            java.lang.String r0 = "none"
            if (r5 == 0) goto Lc
            boolean r0 = r5.isConnected()
            if (r0 != 0) goto L21
            java.lang.String r0 = "none"
        Lc:
            java.lang.String r1 = "NetworkManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Connection Type: "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            vpadn.ab.a(r1, r2)
            return r0
        L21:
            if (r5 == 0) goto Led
            java.lang.String r0 = r5.getTypeName()
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "wifi"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L36
            java.lang.String r0 = "wifi"
            goto Lc
        L36:
            java.lang.String r0 = r0.toLowerCase()
            java.lang.String r1 = "mobile"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lf1
            java.lang.String r0 = r5.getSubtypeName()
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "gsm"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L6a
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "gprs"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L6a
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "edge"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L6d
        L6a:
            java.lang.String r0 = "2g"
            goto Lc
        L6d:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "cdma"
            boolean r1 = r1.startsWith(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "umts"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "1xrtt"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "ehrpd"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "hsupa"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "hsdpa"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Lc1
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "hspa"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto Lc5
        Lc1:
            java.lang.String r0 = "3g"
            goto Lc
        Lc5:
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "lte"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Le9
            java.lang.String r1 = r0.toLowerCase()
            java.lang.String r2 = "umb"
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto Le9
            java.lang.String r0 = r0.toLowerCase()
            java.lang.String r1 = "hspa+"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lf1
        Le9:
            java.lang.String r0 = "4g"
            goto Lc
        Led:
            java.lang.String r0 = "none"
            goto Lc
        Lf1:
            java.lang.String r0 = "unknown"
            goto Lc
        */
        throw new UnsupportedOperationException("Method not decompiled: c.NetworkManager.a(android.net.NetworkInfo):java.lang.String");
    }
}
