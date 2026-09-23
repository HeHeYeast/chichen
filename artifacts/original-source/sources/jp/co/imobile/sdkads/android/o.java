package jp.co.imobile.sdkads.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class o extends BroadcastReceiver {
    final /* synthetic */ ImobileSdkAd a;

    o(ImobileSdkAd imobileSdkAd) {
        this.a = imobileSdkAd;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        r.a();
        if (r.b().equals("")) {
            x.a(null);
            if (this.a.j != null) {
                x.a(null);
                this.a.a((Boolean) false);
                return;
            }
            return;
        }
        x.a(null);
        if (this.a.j == null) {
            x.a(null);
            ImobileSdkAd.startAll();
        }
    }
}
