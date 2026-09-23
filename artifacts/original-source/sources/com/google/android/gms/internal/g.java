package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;
import java.security.cert.CertificateException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class g extends f {

    class a {
        private String dt;
        private boolean du;

        public a(String str, boolean z) {
            this.dt = str;
            this.du = z;
        }

        public String getId() {
            return this.dt;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.du;
        }
    }

    private g(Context context, j jVar, k kVar) {
        super(context, jVar, kVar);
    }

    public static g a(String str, Context context) {
        com.google.android.gms.internal.a aVar = new com.google.android.gms.internal.a();
        a(str, context, aVar);
        return new g(context, aVar, new m(239));
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0029 -> B:19:0x001f). Please report as a decompilation issue!!! */
    @Override // com.google.android.gms.internal.f, com.google.android.gms.internal.e
    protected void b(Context context) throws IllegalStateException, PackageManager.NameNotFoundException, CertificateException {
        super.b(context);
        try {
            try {
                a aVarF = f(context);
                a(28, aVarF.isLimitAdTrackingEnabled() ? 1L : 0L);
                String id = aVarF.getId();
                if (id != null) {
                    a(30, id);
                }
            } catch (GooglePlayServicesNotAvailableException e) {
            } catch (IOException e2) {
                a(28, 1L);
            }
        } catch (IOException e3) {
        }
    }

    a f(Context context) throws IllegalStateException, GooglePlayServicesNotAvailableException, PackageManager.NameNotFoundException, IOException, CertificateException {
        String strA;
        int i = 0;
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            String id = advertisingIdInfo.getId();
            if (id == null || !id.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
                strA = id;
            } else {
                byte[] bArr = new byte[16];
                int i2 = 0;
                while (i < id.length()) {
                    if (i == 8 || i == 13 || i == 18 || i == 23) {
                        i++;
                    }
                    bArr[i2] = (byte) ((Character.digit(id.charAt(i), 16) << 4) + Character.digit(id.charAt(i + 1), 16));
                    i2++;
                    i += 2;
                }
                strA = this.di.a(bArr, true);
            }
            return new a(strA, advertisingIdInfo.isLimitAdTrackingEnabled());
        } catch (GooglePlayServicesRepairableException e) {
            throw new IOException(e);
        }
    }
}
