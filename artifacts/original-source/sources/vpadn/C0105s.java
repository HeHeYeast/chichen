package vpadn;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import java.util.concurrent.ExecutorService;

@Deprecated
/* renamed from: vpadn.s, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0105s implements InterfaceC0102p {
    private InterfaceC0102p a;

    public C0105s(InterfaceC0102p interfaceC0102p) {
        this.a = interfaceC0102p;
    }

    @Override // vpadn.InterfaceC0102p
    @Deprecated
    public final Activity a() {
        Log.i("Deprecation Notice", "Replace ctx.getActivity() with cordova.getActivity()");
        return this.a.a();
    }

    @Override // vpadn.InterfaceC0102p
    @Deprecated
    public final Object a(String str, Object obj) {
        Log.i("Deprecation Notice", "Replace ctx.onMessage() with cordova.onMessage()");
        return this.a.a(str, obj);
    }

    @Override // vpadn.InterfaceC0102p
    @Deprecated
    public final void a(C0103q c0103q, Intent intent, int i) {
        Log.i("Deprecation Notice", "Replace ctx.startActivityForResult() with cordova.startActivityForResult()");
        this.a.a(c0103q, intent, i);
    }

    @Override // vpadn.InterfaceC0102p
    public final ExecutorService e() {
        Log.i("Deprecation Notice", "Replace ctx.getThreadPool() with cordova.getThreadPool()");
        return this.a.e();
    }
}
