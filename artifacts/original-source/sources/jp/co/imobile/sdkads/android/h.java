package jp.co.imobile.sdkads.android;

import java.util.concurrent.Callable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class h implements Callable {
    final /* synthetic */ f a;
    private final /* synthetic */ String b;

    h(f fVar, String str) {
        this.a = fVar;
        this.b = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Object call() {
        return v.a(this.b);
    }
}
