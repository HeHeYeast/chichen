package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class af implements Runnable {
    final /* synthetic */ aa a;

    af(aa aaVar) {
        this.a = aaVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.q.onAdCloseCompleted();
    }
}
