package c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BatteryListener extends C0103q {
    private C0101o b = null;
    private BroadcastReceiver a = null;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) {
        if (str.equals("start")) {
            if (this.b != null) {
                c0101o.b("Battery listener already running.");
                return true;
            }
            this.b = c0101o;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            if (this.a == null) {
                this.a = new BroadcastReceiver() { // from class: c.BatteryListener.1
                    @Override // android.content.BroadcastReceiver
                    public final void onReceive(Context context, Intent intent) {
                        BatteryListener.a(BatteryListener.this, intent);
                    }
                };
                this.cordova.a().registerReceiver(this.a, intentFilter);
            }
            C0108v c0108v = new C0108v(C0108v.a.NO_RESULT);
            c0108v.a(true);
            c0101o.a(c0108v);
            return true;
        }
        if (!str.equals("stop")) {
            return false;
        }
        a();
        a(new JSONObject(), false);
        this.b = null;
        c0101o.b();
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        a();
    }

    @Override // vpadn.C0103q
    public void onReset() {
        a();
    }

    private void a() {
        if (this.a != null) {
            try {
                this.cordova.a().unregisterReceiver(this.a);
                this.a = null;
            } catch (Exception e) {
                Log.e("BatteryManager", "Error unregistering battery receiver: " + e.getMessage(), e);
            }
        }
    }

    private static JSONObject a(Intent intent) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("level", intent.getIntExtra("level", 0));
            jSONObject.put("isPlugged", intent.getIntExtra("plugged", -1) > 0);
        } catch (JSONException e) {
            Log.e("BatteryManager", e.getMessage(), e);
        }
        return jSONObject;
    }

    static /* synthetic */ void a(BatteryListener batteryListener, Intent intent) {
        batteryListener.a(a(intent), true);
    }

    private void a(JSONObject jSONObject, boolean z) {
        if (this.b != null) {
            C0108v c0108v = new C0108v(C0108v.a.OK, jSONObject);
            c0108v.a(z);
            this.b.a(c0108v);
        }
    }
}
