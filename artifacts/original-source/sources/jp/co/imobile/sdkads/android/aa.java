package jp.co.imobile.sdkads.android;

import java.util.Iterator;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class aa extends ImobileSdkAdListener {
    final /* synthetic */ z a;

    aa(z zVar) {
        this.a = zVar;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdCliclkCompleted() {
        this.a.s.set(false);
        if (this.a.p != null) {
            this.a.v.post(new ad(this));
        }
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdCloseCompleted() {
        this.a.s.set(false);
        if (this.a.p != null) {
            this.a.v.post(new ae(this));
        }
        if (this.a.q != null) {
            this.a.v.post(new af(this));
        }
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdReadyCompleted() {
        if (this.a.p != null) {
            this.a.v.post(new ab(this));
        }
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdShowCompleted() {
        if (this.a.p != null) {
            this.a.v.post(new ac(this));
        }
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onDismissAdScreen() {
        if (this.a.p != null) {
            this.a.v.post(new ai(this));
        }
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onFailed(FailNotificationReason reason) {
        this.a.s.set(false);
        if (reason == FailNotificationReason.AUTHORITY) {
            this.a.a(am.ERROR);
        }
        Iterator it = this.a.o.iterator();
        boolean z = true;
        while (it.hasNext()) {
            f fVar = (f) it.next();
            if (fVar.d() == j.DISPLAYABLE || fVar.d() == j.LODING) {
                z = false;
            }
        }
        if (z && this.a.p != null) {
            this.a.v.post(new ag(this, reason));
        }
        if (this.a.q == null || reason != FailNotificationReason.AD_NOT_READY) {
            return;
        }
        this.a.v.post(new ah(this));
    }
}
