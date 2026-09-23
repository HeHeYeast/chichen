package vpadn;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ad implements LocationListener {

    /* renamed from: c, reason: collision with root package name */
    private static ad f320c;
    private LocationManager d;
    private boolean g;
    private boolean h;
    private Timer i;
    private Activity k;
    private static int a = -1;
    private static int b = -1;
    private static Location f = null;
    private List<LocationListener> e = new LinkedList();
    private boolean j = false;

    class a extends TimerTask {
        a() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            ab.a("VponLocation", "[location] enter LocationRegisterTask");
            if (ad.this.k != null) {
                ad.this.k.runOnUiThread(new Runnable() { // from class: vpadn.ad.a.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            if (!ad.this.j) {
                                if (ad.this.g) {
                                    ad.this.d.requestLocationUpdates("gps", 2000L, 10.0f, ad.this);
                                    ad.this.j = true;
                                }
                                if (ad.this.h) {
                                    ad.this.d.requestLocationUpdates("network", 2000L, 10.0f, ad.this);
                                    ad.this.j = true;
                                }
                                if (!ad.this.j) {
                                    ab.b("VponLocation", "isNetworkProvideEnable is false and isGpsProviderEnable is false at LocationRegisterTask");
                                    return;
                                }
                                return;
                            }
                            ab.b("VponLocation", "[location]isRegisterLocationListener IS TRUE");
                        } catch (Exception e) {
                            ab.a("VponLocation", "[location]requestLocationUpdates throw Exception.", e);
                        }
                    }
                });
            } else {
                ab.b("VponLocation", "[location]activity IS NULL at LocationRegisterTask");
            }
        }
    }

    private ad(Context context) {
        this.d = null;
        this.g = false;
        this.h = false;
        ab.a("VponLocation", "[location] enter VponLoaction ctor");
        try {
            if (context instanceof Activity) {
                this.k = (Activity) context;
            } else {
                ab.b("VponLocation", "[location]appContext instanceof Activity is false at VponLocation ctor");
            }
            this.d = (LocationManager) context.getSystemService("location");
            if (ae.b(context)) {
                this.g = this.d.isProviderEnabled("gps");
            }
            this.h = this.d.isProviderEnabled("network");
            ab.a("VponLocation", "[location]isGpsProviderEnable:" + this.g + " isNetworkProvideEnable:" + this.h);
            if (this.g || this.h) {
                String bestProvider = this.d.getBestProvider(new Criteria(), true);
                ab.a("VponLocation", "[location]Best Location Provide:" + bestProvider);
                f = this.d.getLastKnownLocation(bestProvider);
                ab.a("VponLocation", "[location]locationMgr.getLastKnownLocation(locationPrivider); currentlocation:" + f);
                c(1);
                return;
            }
            ab.b("VponLocation", "[location]new VponLocation failed. isGpsProviderEnable and isNetworkProvideEnable are false.");
        } catch (Exception e) {
            ab.a("VponLocation", "[location]VponLocation ctor throws Exception:" + e.getMessage(), e);
        }
    }

    private void c(int i) {
        this.i = new Timer();
        this.i.schedule(new a(), i);
    }

    public static ad a(Context context) {
        if (f320c == null) {
            synchronized (ad.class) {
                if (f320c == null) {
                    f320c = new ad(context);
                }
            }
        }
        return f320c;
    }

    public final synchronized Location a() {
        Location location = null;
        synchronized (this) {
            ab.a("VponLocation", "[location] call getLocation()");
            if (f == null) {
                ab.d("VponLocation", "[location] currentlocation == null at getLocation()");
                ab.d("VponLocation", "[location]isGpsProviderEnable:" + this.g + " isNetworkProvideEnable:" + this.h);
            } else if (b != -1 && System.currentTimeMillis() - f.getTime() > b * 1000) {
                ab.d("VponLocation", "[location]locCacheTime != -1 && System.currentTimeMillis() - currentlocation.getTime() > (locCacheTime * 2)");
                if (this.i == null && !this.j) {
                    ab.d("VponLocation", "[location] startLocationRegisterTimer(1), because currentlocation is very old");
                    c(1);
                }
            } else {
                location = f;
            }
        }
        return location;
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        ab.a("VponLocation", "[location] onLocationChanged");
        f = location;
        if (location == null) {
            ab.b("VponLocation", "[location] currentlocation == null at onLocationChanged");
            return;
        }
        ab.a("VponLocation", "[location] onLocationChanged ->" + f.getAccuracy());
        int accuracy = (int) f.getAccuracy();
        if (a == -1) {
            a = 300;
        }
        if (this.j && accuracy <= a) {
            ab.a("VponLocation", "[location] Call locationMgr removeUpdates");
            this.d.removeUpdates(this);
            this.j = false;
            if (b == -1) {
                b = 300;
            }
            if (this.i != null) {
                this.i.cancel();
                this.i.purge();
                this.i = null;
            }
            c(b * 1000);
        } else {
            ab.d("VponLocation", "[location] currentAccuracy:" + accuracy);
        }
        Iterator<LocationListener> it = this.e.iterator();
        while (it.hasNext()) {
            it.next().onLocationChanged(location);
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        Iterator<LocationListener> it = this.e.iterator();
        while (it.hasNext()) {
            it.next().onProviderDisabled(str);
        }
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        Iterator<LocationListener> it = this.e.iterator();
        while (it.hasNext()) {
            it.next().onProviderEnabled(str);
        }
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
        Iterator<LocationListener> it = this.e.iterator();
        while (it.hasNext()) {
            it.next().onStatusChanged(str, i, bundle);
        }
    }

    public static synchronized void a(int i) {
        a = i;
    }

    public static synchronized void b(int i) {
        b = i;
    }

    public final synchronized void b() {
        ab.a("VponLocation", "[location] call cancelCheckLocationTimerAndUnregisterListener");
        if (this.i != null) {
            this.i.cancel();
            this.i.purge();
            this.i = null;
        }
        if (this.j) {
            this.d.removeUpdates(this);
            this.j = false;
        }
    }
}
