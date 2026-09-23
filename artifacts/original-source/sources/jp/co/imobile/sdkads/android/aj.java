package jp.co.imobile.sdkads.android;

import java.util.concurrent.Callable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class aj implements Callable {
    final /* synthetic */ z a;
    private final /* synthetic */ z b;

    aj(z zVar, z zVar2) {
        this.a = zVar;
        this.b = zVar2;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Object call() {
        return v.a(this.b);
    }
}
