package c;

import android.location.Location;
import android.location.LocationManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0091e;
import vpadn.C0096j;
import vpadn.C0100n;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GeoBroker extends C0103q {
    private C0096j a;
    private C0100n b;

    /* renamed from: c, reason: collision with root package name */
    private LocationManager f174c;

    @Override // vpadn.C0103q
    public boolean execute(String str, JSONArray jSONArray, C0101o c0101o) throws JSONException {
        if (this.f174c == null) {
            this.f174c = (LocationManager) this.cordova.a().getSystemService("location");
            this.b = new C0100n(this.f174c, this);
            this.a = new C0096j(this.f174c, this);
        }
        if (this.f174c.isProviderEnabled("gps") || this.f174c.isProviderEnabled("network")) {
            if (str.equals("getLocation")) {
                boolean z = jSONArray.getBoolean(0);
                int i = jSONArray.getInt(1);
                Location lastKnownLocation = this.f174c.getLastKnownLocation(z ? "gps" : "network");
                if (lastKnownLocation != null && System.currentTimeMillis() - lastKnownLocation.getTime() <= i) {
                    c0101o.a(new C0108v(C0108v.a.OK, returnLocationJSON(lastKnownLocation)));
                } else if (z) {
                    this.a.a(c0101o);
                } else {
                    this.b.a(c0101o);
                }
            } else if (str.equals("addWatch")) {
                String string = jSONArray.getString(0);
                if (jSONArray.getBoolean(1)) {
                    this.a.a(string, c0101o);
                } else {
                    this.b.a(string, c0101o);
                }
            } else {
                if (!str.equals("clearWatch")) {
                    return false;
                }
                String string2 = jSONArray.getString(0);
                this.a.a(string2);
                this.b.a(string2);
            }
        } else {
            c0101o.a(new C0108v(C0108v.a.NO_RESULT, "Location API is not available for this device."));
        }
        return true;
    }

    @Override // vpadn.C0103q
    public void onDestroy() {
        if (this.b != null) {
            this.b.a();
            this.b = null;
        }
        if (this.a != null) {
            this.a.a();
            this.a = null;
        }
    }

    @Override // vpadn.C0103q
    public void onReset() {
        onDestroy();
    }

    public JSONObject returnLocationJSON(Location location) throws JSONException {
        Float fValueOf = null;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("latitude", location.getLatitude());
            jSONObject.put("longitude", location.getLongitude());
            jSONObject.put("altitude", location.hasAltitude() ? Double.valueOf(location.getAltitude()) : null);
            jSONObject.put("accuracy", location.getAccuracy());
            if (location.hasBearing() && location.hasSpeed()) {
                fValueOf = Float.valueOf(location.getBearing());
            }
            jSONObject.put("heading", fValueOf);
            jSONObject.put("speed", location.getSpeed());
            jSONObject.put("timestamp", location.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void win(Location location, C0101o c0101o) {
        c0101o.a(new C0108v(C0108v.a.OK, returnLocationJSON(location)));
    }

    public void fail(int i, String str, C0101o c0101o) throws JSONException {
        String str2;
        JSONObject jSONObject;
        C0108v c0108v;
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("code", i);
            jSONObject2.put("message", str);
            jSONObject = jSONObject2;
            str2 = null;
        } catch (JSONException e) {
            str2 = "{'code':" + i + ",'message':'" + str.replaceAll("'", "'") + "'}";
            jSONObject = null;
        }
        if (jSONObject != null) {
            c0108v = new C0108v(C0108v.a.ERROR, jSONObject);
        } else {
            c0108v = new C0108v(C0108v.a.ERROR, str2);
        }
        c0101o.a(c0108v);
    }

    public boolean isGlobalListener(C0091e c0091e) {
        if (this.a == null || this.b == null) {
            return false;
        }
        return this.a.equals(c0091e) || this.b.equals(c0091e);
    }
}
