package c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0104r;
import vpadn.InterfaceC0102p;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Device extends C0103q {
    public static final String TAG = "Device";
    public static String cordovaVersion = "2.5.0";
    public static String platform = "Android";
    public static String uuid;
    private BroadcastReceiver a = null;

    @Override // vpadn.C0103q
    public void initialize(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super.initialize(interfaceC0102p, cordovaWebView);
        uuid = getUuid();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.a = new BroadcastReceiver() { // from class: c.Device.1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction().equals("android.intent.action.PHONE_STATE") && intent.hasExtra("state")) {
                    String stringExtra = intent.getStringExtra("state");
                    if (stringExtra.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        C0104r.c(Device.TAG, "Telephone RINGING");
                        Device.this.webView.a("telephone", (Object) "ringing");
                    } else if (stringExtra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        C0104r.c(Device.TAG, "Telephone OFFHOOK");
                        Device.this.webView.a("telephone", (Object) "offhook");
                    } else if (stringExtra.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        C0104r.c(Device.TAG, "Telephone IDLE");
                        Device.this.webView.a("telephone", (Object) "idle");
                    }
                }
            }
        };
        try {
            this.cordova.a().registerReceiver(this.a, intentFilter);
        } catch (Exception e) {
            C0104r.b("VPON", "initTelephonyReceiver throw Exception:" + e.getMessage(), e);
        }
    }

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        if (str.equals("getDeviceInfo")) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("uuid", uuid);
            jSONObject.put("version", getOSVersion());
            jSONObject.put("platform", platform);
            jSONObject.put("name", getProductName());
            jSONObject.put("cordova", cordovaVersion);
            jSONObject.put("model", getModel());
            c0101o.a(jSONObject);
            return true;
        }
        return false;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        try {
            this.cordova.a().unregisterReceiver(this.a);
        } catch (Exception e) {
            C0104r.a(TAG, "Device onDestroy throw Exception:" + e.getMessage(), e);
        }
    }

    public String getPlatform() {
        return platform;
    }

    public String getUuid() {
        return Settings.Secure.getString(this.cordova.a().getContentResolver(), "android_id");
    }

    public String getCordovaVersion() {
        return cordovaVersion;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getProductName() {
        return Build.PRODUCT;
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    public String getTimeZoneID() {
        return TimeZone.getDefault().getID();
    }
}
