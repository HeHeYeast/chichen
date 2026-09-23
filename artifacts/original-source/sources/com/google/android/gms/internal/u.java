package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.ac;
import com.google.android.gms.internal.ad;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class u extends com.google.android.gms.dynamic.e<ad> {
    private static final u er = new u();

    private u() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    public static ac a(Context context, x xVar, String str, av avVar) {
        ac acVarB;
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0 && (acVarB = er.b(context, xVar, str, avVar)) != null) {
            return acVarB;
        }
        cn.m("Using AdManager from the client jar.");
        return new r(context, xVar, str, avVar, new co(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, true));
    }

    private ac b(Context context, x xVar, String str, av avVar) {
        try {
            return ac.a.f(t(context).a(com.google.android.gms.dynamic.c.g(context), xVar, str, avVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE));
        } catch (RemoteException e) {
            cn.b("Could not create remote AdManager.", e);
            return null;
        } catch (e.a e2) {
            cn.b("Could not create remote AdManager.", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.e
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public ad d(IBinder iBinder) {
        return ad.a.g(iBinder);
    }
}
