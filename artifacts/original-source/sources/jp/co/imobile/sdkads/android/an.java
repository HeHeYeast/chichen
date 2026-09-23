package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class an extends z {
    ImobileSdkAdListener u = null;

    an() {
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0024 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // jp.co.imobile.sdkads.android.z
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void a(android.app.Activity r8, jp.co.imobile.sdkads.android.ImobileSdkAdListener r9, android.graphics.Point r10, java.lang.Boolean r11, android.view.ViewGroup r12, jp.co.imobile.sdkads.android.ImobileIconParams r13, java.lang.Boolean r14) {
        /*
            r7 = this;
            r2 = 0
            if (r8 == 0) goto L9
            android.view.Window r0 = r8.getWindow()
            if (r0 != 0) goto La
        L9:
            return
        La:
            r7.q = r9
            android.graphics.Rect r6 = new android.graphics.Rect
            int r0 = r10.x
            int r1 = r10.y
            int r3 = r7.h
            int r4 = r7.i
            r6.<init>(r0, r1, r3, r4)
            int r0 = r7.k
            if (r0 == 0) goto L4c
            jp.co.imobile.sdkads.android.f r1 = r7.j()
            r4 = r1
        L22:
            if (r4 == 0) goto L6d
            r4.a(r13)     // Catch: jp.co.imobile.sdkads.android.y -> L41
            r4.k = r14     // Catch: jp.co.imobile.sdkads.android.y -> L41
            r0 = 0
            jp.co.imobile.sdkads.android.x.a(r0)     // Catch: jp.co.imobile.sdkads.android.y -> L41
            android.widget.RelativeLayout r3 = r4.a(r8)     // Catch: jp.co.imobile.sdkads.android.y -> L41
            jp.co.imobile.sdkads.android.ao r0 = new jp.co.imobile.sdkads.android.ao     // Catch: jp.co.imobile.sdkads.android.y -> L41
            r1 = r7
            r2 = r12
            r5 = r8
            r0.<init>(r1, r2, r3, r4, r5)     // Catch: jp.co.imobile.sdkads.android.y -> L41
            r7.u = r0     // Catch: jp.co.imobile.sdkads.android.y -> L41
            jp.co.imobile.sdkads.android.ImobileSdkAdListener r0 = r7.u     // Catch: jp.co.imobile.sdkads.android.y -> L41
            r4.a(r0, r6)     // Catch: jp.co.imobile.sdkads.android.y -> L41
            goto L9
        L41:
            r0 = move-exception
            jp.co.imobile.sdkads.android.ImobileSdkAdListener r1 = r7.t
            jp.co.imobile.sdkads.android.FailNotificationReason r0 = r0.a()
            r1.onFailed(r0)
            goto L9
        L4c:
            jp.co.imobile.sdkads.android.a r1 = new jp.co.imobile.sdkads.android.a     // Catch: jp.co.imobile.sdkads.android.y -> L60
            android.content.Context r0 = jp.co.imobile.sdkads.android.ImobileSdkAd.a()     // Catch: jp.co.imobile.sdkads.android.y -> L60
            jp.co.imobile.sdkads.android.ImobileSdkAdListener r3 = r7.t     // Catch: jp.co.imobile.sdkads.android.y -> L60
            r4 = 0
            r1.<init>(r7, r0, r3, r4)     // Catch: jp.co.imobile.sdkads.android.y -> L60
            jp.co.imobile.sdkads.android.ImobileSdkAd.a()     // Catch: jp.co.imobile.sdkads.android.y -> L75
            r1.a(r7)     // Catch: jp.co.imobile.sdkads.android.y -> L75
            r4 = r1
            goto L22
        L60:
            r0 = move-exception
            r1 = r2
        L62:
            jp.co.imobile.sdkads.android.ImobileSdkAdListener r2 = r7.t
            jp.co.imobile.sdkads.android.FailNotificationReason r0 = r0.a()
            r2.onFailed(r0)
            r4 = r1
            goto L22
        L6d:
            jp.co.imobile.sdkads.android.ImobileSdkAdListener r0 = r7.t
            jp.co.imobile.sdkads.android.FailNotificationReason r1 = jp.co.imobile.sdkads.android.FailNotificationReason.AD_NOT_READY
            r0.onFailed(r1)
            goto L9
        L75:
            r0 = move-exception
            goto L62
        */
        throw new UnsupportedOperationException("Method not decompiled: jp.co.imobile.sdkads.android.an.a(android.app.Activity, jp.co.imobile.sdkads.android.ImobileSdkAdListener, android.graphics.Point, java.lang.Boolean, android.view.ViewGroup, jp.co.imobile.sdkads.android.ImobileIconParams, java.lang.Boolean):void");
    }

    @Override // jp.co.imobile.sdkads.android.z
    final boolean k() {
        return a() == am.START;
    }

    @Override // jp.co.imobile.sdkads.android.z
    final void l() {
        if (a() == am.START) {
            a(am.PAUSE);
        }
    }

    @Override // jp.co.imobile.sdkads.android.z
    final void m() {
        if (this.u != null) {
            this.u.onAdCloseCompleted();
        }
    }
}
