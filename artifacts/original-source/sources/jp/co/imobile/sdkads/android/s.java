package jp.co.imobile.sdkads.android;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;
import java.security.cert.CertificateException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class s implements Runnable {
    final /* synthetic */ r a;
    private final /* synthetic */ Context b;

    s(r rVar, Context context) {
        this.a = rVar;
        this.b = context;
    }

    @Override // java.lang.Runnable
    public final void run() throws PackageManager.NameNotFoundException, CertificateException {
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.b);
            if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                x.a(null);
            } else {
                this.a.m = advertisingIdInfo.getId();
                new StringBuilder("AdvertisingId get success. (advertisementId: ").append(this.a.m).append(")");
                x.a(null);
            }
        } catch (GooglePlayServicesNotAvailableException e) {
            x.a(e);
        } catch (GooglePlayServicesRepairableException e2) {
            x.a(e2);
        } catch (IOException e3) {
            x.a(e3);
        } catch (IllegalStateException e4) {
            x.a(e4);
            throw e4;
        }
    }
}
