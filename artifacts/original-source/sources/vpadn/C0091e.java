package vpadn;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import c.GeoBroker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;

/* renamed from: vpadn.e, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class C0091e implements LocationListener {
    public static int a = 2;
    protected LocationManager b;
    private GeoBroker d;
    private String g;

    /* renamed from: c, reason: collision with root package name */
    protected boolean f339c = false;
    private HashMap<String, C0101o> e = new HashMap<>();
    private List<C0101o> f = new ArrayList();

    public C0091e(LocationManager locationManager, GeoBroker geoBroker, String str) {
        this.g = "[Cordova Location Listener]";
        this.b = locationManager;
        this.d = geoBroker;
        this.g = str;
    }

    protected final void a(int i, String str) throws JSONException {
        Iterator<C0101o> it = this.f.iterator();
        while (it.hasNext()) {
            this.d.fail(i, str, it.next());
        }
        if (this.d.isGlobalListener(this)) {
            Log.d(this.g, "Stopping global listener");
            d();
        }
        this.f.clear();
        Iterator<C0101o> it2 = this.e.values().iterator();
        while (it2.hasNext()) {
            this.d.fail(i, str, it2.next());
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) throws JSONException {
        Log.d(this.g, "Location provider '" + str + "' disabled.");
        a(a, "GPS provider disabled.");
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        Log.d(this.g, "Location provider " + str + " has been enabled");
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) throws JSONException {
        Log.d(this.g, "The status of the provider " + str + " has changed");
        if (i == 0) {
            Log.d(this.g, String.valueOf(str) + " is OUT OF SERVICE");
            a(a, "Provider " + str + " is out of service.");
        } else if (i == 1) {
            Log.d(this.g, String.valueOf(str) + " is TEMPORARILY_UNAVAILABLE");
        } else {
            Log.d(this.g, String.valueOf(str) + " is AVAILABLE");
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        Log.d(this.g, "The location has been updated!");
        Iterator<C0101o> it = this.f.iterator();
        while (it.hasNext()) {
            this.d.win(location, it.next());
        }
        if (this.d.isGlobalListener(this)) {
            Log.d(this.g, "Stopping global listener");
            d();
        }
        this.f.clear();
        Iterator<C0101o> it2 = this.e.values().iterator();
        while (it2.hasNext()) {
            this.d.win(location, it2.next());
        }
    }

    private int c() {
        return this.e.size() + this.f.size();
    }

    public final void a(String str, C0101o c0101o) throws JSONException {
        this.e.put(str, c0101o);
        if (c() == 1) {
            b();
        }
    }

    public final void a(C0101o c0101o) throws JSONException {
        this.f.add(c0101o);
        if (c() == 1) {
            b();
        }
    }

    public final void a(String str) {
        if (this.e.containsKey(str)) {
            this.e.remove(str);
        }
        if (c() == 0) {
            d();
        }
    }

    public final void a() {
        d();
    }

    protected void b() throws JSONException {
        if (!this.f339c) {
            if (this.b.getProvider("network") != null) {
                this.f339c = true;
                this.b.requestLocationUpdates("network", 60000L, 10.0f, this);
            } else {
                a(a, "Network provider is not available.");
            }
        }
    }

    private void d() {
        if (this.f339c) {
            this.b.removeUpdates(this);
            this.f339c = false;
        }
    }
}
