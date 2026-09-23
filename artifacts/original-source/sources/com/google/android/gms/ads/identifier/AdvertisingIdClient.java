package com.google.android.gms.ads.identifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.a;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.p;
import java.io.IOException;
import java.security.cert.CertificateException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class AdvertisingIdClient {

    public static final class Info {
        private final String dX;
        private final boolean dY;

        Info(String advertisingId, boolean limitAdTrackingEnabled) {
            this.dX = advertisingId;
            this.dY = limitAdTrackingEnabled;
        }

        public String getId() {
            return this.dX;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.dY;
        }
    }

    private static a g(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException, IOException, CertificateException {
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            try {
                GooglePlayServicesUtil.m(context);
                a aVar = new a();
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
                if (context.bindService(intent, aVar, 1)) {
                    return aVar;
                }
                throw new IOException("Connection failure");
            } catch (GooglePlayServicesNotAvailableException e) {
                throw new IOException(e);
            }
        } catch (PackageManager.NameNotFoundException e2) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }

    public static Info getAdvertisingIdInfo(Context context) throws GooglePlayServicesRepairableException, IllegalStateException, GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException, IOException, CertificateException {
        dm.x("Calling this from your main thread can lead to deadlock");
        a aVarG = g(context);
        try {
            try {
                p pVarB = p.a.b(aVarG.aG());
                return new Info(pVarB.getId(), pVarB.a(true));
            } catch (RemoteException e) {
                Log.i("AdvertisingIdClient", "GMS remote exception ", e);
                throw new IOException("Remote exception");
            } catch (InterruptedException e2) {
                throw new IOException("Interrupted exception");
            }
        } finally {
            context.unbindService(aVarG);
        }
    }
}
