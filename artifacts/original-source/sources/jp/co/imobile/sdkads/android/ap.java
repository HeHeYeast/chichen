package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.widget.RelativeLayout;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ap extends z {
    private Dialog v = null;
    ImobileSdkAdListener u = null;

    ap() {
    }

    private void a(Activity activity, f fVar, Rect rect) {
        x.a(null);
        this.v = new Dialog(activity);
        this.v.setOwnerActivity(activity);
        this.v.setOnKeyListener(new aq(this));
        this.v.getWindow().setLayout(-1, -1);
        this.v.requestWindowFeature(1);
        this.v.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        int requestedOrientation = this.v.getOwnerActivity().getRequestedOrientation();
        activity.setRequestedOrientation(r.b(activity));
        this.u = new ar(this, fVar, requestedOrientation);
        this.v.setContentView(fVar.a(activity), new RelativeLayout.LayoutParams(-1, -2));
        fVar.a(this.u, rect);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:42|29|(2:46|30)|44|31|32|(2:34|56)(1:55)) */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00fd, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00fe, code lost:
    
        jp.co.imobile.sdkads.android.x.b("showAdDialog", "");
        r7.t.onFailed(r0.a());
     */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    @Override // jp.co.imobile.sdkads.android.z
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void a(android.app.Activity r8, jp.co.imobile.sdkads.android.ImobileSdkAdListener r9, android.graphics.Point r10, java.lang.Boolean r11, android.view.ViewGroup r12, jp.co.imobile.sdkads.android.ImobileIconParams r13, java.lang.Boolean r14) {
        /*
            Method dump skipped, instructions count: 273
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: jp.co.imobile.sdkads.android.ap.a(android.app.Activity, jp.co.imobile.sdkads.android.ImobileSdkAdListener, android.graphics.Point, java.lang.Boolean, android.view.ViewGroup, jp.co.imobile.sdkads.android.ImobileIconParams, java.lang.Boolean):void");
    }

    @Override // jp.co.imobile.sdkads.android.z
    final boolean k() {
        return this.k != 0 ? j() != null : a() == am.START;
    }

    @Override // jp.co.imobile.sdkads.android.z
    final void l() {
        if (a() == am.START) {
            a(am.PAUSE);
        }
    }

    @Override // jp.co.imobile.sdkads.android.z
    final void m() {
        if (this.v == null || !this.v.isShowing() || this.u == null) {
            return;
        }
        this.u.onAdCloseCompleted();
    }
}
