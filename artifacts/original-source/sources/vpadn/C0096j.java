package vpadn;

import android.location.LocationManager;
import c.GeoBroker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* renamed from: vpadn.j, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0096j extends C0091e {
    public C0096j(LocationManager locationManager, GeoBroker geoBroker) {
        super(locationManager, geoBroker, "[Cordova GPSListener]");
    }

    @Override // vpadn.C0091e
    protected final void b() {
        if (!this.f339c) {
            if (this.b.getProvider("gps") != null) {
                this.f339c = true;
                this.b.requestLocationUpdates("gps", 60000L, BitmapDescriptorFactory.HUE_RED, this);
            } else {
                a(C0091e.a, "GPS provider is not available.");
            }
        }
    }
}
