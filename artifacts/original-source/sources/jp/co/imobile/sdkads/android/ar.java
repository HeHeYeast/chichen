package jp.co.imobile.sdkads.android;

import android.R;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ar extends ImobileSdkAdListener {
    final /* synthetic */ ap a;
    private final /* synthetic */ f b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ int f296c;

    ar(ap apVar, f fVar, int i) {
        this.a = apVar;
        this.b = fVar;
        this.f296c = i;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdCloseCompleted() {
        this.b.a(j.DISPLAYED);
        this.a.v.getOwnerActivity().setRequestedOrientation(this.f296c);
        this.a.v.dismiss();
        this.a.t.onAdCloseCompleted();
        View viewFindViewById = this.a.v.findViewById(985478646);
        if (viewFindViewById != null) {
            ((ViewGroup) this.a.v.findViewById(R.id.content)).removeView(viewFindViewById);
            x.a(null);
        } else {
            x.b("Ad Dialog close failed.", "");
        }
        this.a.v = null;
        this.a.u = null;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdReadyCompleted() {
        x.a(null);
        this.a.v.show();
        this.b.a();
        this.a.t.onAdShowCompleted();
    }
}
