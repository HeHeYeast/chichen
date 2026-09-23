package com.google.android.gms.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.c;
import com.google.android.gms.maps.internal.q;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.security.cert.CertificateException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class MapsInitializer {
    private MapsInitializer() {
    }

    public static void initialize(Context context) throws GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException, CertificateException {
        dm.e(context);
        c cVarU = q.u(context);
        try {
            CameraUpdateFactory.a(cVarU.cG());
            BitmapDescriptorFactory.a(cVarU.cH());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
