package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class al implements Runnable {
    final /* synthetic */ ak a;
    private final /* synthetic */ z b;

    al(ak akVar, z zVar) {
        this.a = akVar;
        this.b = zVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.a.a.k > 0) {
            for (int i = 0; this.a.a.k > i; i++) {
                try {
                    new StringBuilder("spot id : ").append(this.a.a.f306c);
                    x.a(null);
                    a aVar = new a(this.b, ImobileSdkAd.a(), this.a.a.t, true);
                    z zVar = this.b;
                    ImobileSdkAd.a();
                    aVar.a(zVar);
                    this.a.a.o.add(aVar);
                } catch (y e) {
                    this.a.a.t.onFailed(e.a());
                }
            }
        }
        synchronized (this) {
            this.a.a.a(am.START);
        }
    }
}
