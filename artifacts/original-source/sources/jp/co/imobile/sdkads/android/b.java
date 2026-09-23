package jp.co.imobile.sdkads.android;

import android.graphics.Rect;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class b extends ImobileSdkAdListener {
    final /* synthetic */ a a;
    private final /* synthetic */ Rect b;

    b(a aVar, Rect rect) {
        this.a = aVar;
        this.b = rect;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdReadyCompleted() {
        try {
            StringBuilder sb = new StringBuilder("javascript:ShowAdBefore('");
            r.a();
            sb.append(r.a(this.a.o, ImobileSdkAd.d(), this.b)).append("');");
            x.a(null);
            e eVar = this.a.f298c;
            StringBuilder sb2 = new StringBuilder("javascript:ShowAdBefore('");
            r.a();
            eVar.a(sb2.append(r.a(this.a.o, ImobileSdkAd.d(), this.b)).append("');").toString());
            this.a.e = null;
        } catch (y e) {
            this.a.m.onFailed(FailNotificationReason.UNKNOWN);
        }
    }
}
