package jp.co.imobile.sdkads.android;

import java.util.concurrent.Callable;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class t implements Callable {
    private final /* synthetic */ String a;

    t(String str) {
        this.a = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ Object call() {
        return v.b(this.a);
    }
}
