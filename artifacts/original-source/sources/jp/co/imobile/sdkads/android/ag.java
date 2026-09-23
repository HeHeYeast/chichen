package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ag implements Runnable {
    final /* synthetic */ aa a;
    private final /* synthetic */ FailNotificationReason b;

    ag(aa aaVar, FailNotificationReason failNotificationReason) {
        this.a = aaVar;
        this.b = failNotificationReason;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.p.onFailed(this.b);
    }
}
