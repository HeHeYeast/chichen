package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class g implements Runnable {
    final /* synthetic */ f a;
    private final /* synthetic */ z b;

    g(f fVar, z zVar) {
        this.a = fVar;
        this.b = zVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.f298c.a(this.b.f(), this.b.g(), "text/html", "utf-8");
    }
}
