package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ac implements Runnable {
    final /* synthetic */ aa a;

    ac(aa aaVar) {
        this.a = aaVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.p.onAdShowCompleted();
    }
}
