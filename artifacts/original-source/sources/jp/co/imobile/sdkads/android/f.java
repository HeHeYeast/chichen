package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.widget.RelativeLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
abstract class f {
    protected Date a;

    /* renamed from: c, reason: collision with root package name */
    protected e f298c;
    protected ImobileSdkAdListener e;
    protected Date f;
    protected ImobileSdkAdListener l;
    protected ImobileSdkAdListener m;
    private j n = j.NONE;
    protected Map b = new HashMap();
    protected Boolean d = false;
    protected Rect g = null;
    protected int h = 0;
    protected int i = 0;
    protected ImobileIconParams j = null;
    protected Boolean k = false;
    private final Handler o = new Handler();

    f(ImobileSdkAdListener imobileSdkAdListener) {
        this.m = imobileSdkAdListener;
    }

    abstract RelativeLayout a(Activity activity);

    abstract void a();

    final void a(ImobileIconParams imobileIconParams) {
        this.j = imobileIconParams;
    }

    abstract void a(ImobileSdkAdListener imobileSdkAdListener, Rect rect);

    final void a(j jVar) {
        this.n = jVar;
    }

    final void a(z zVar) {
        this.n = j.LODING;
        this.a = null;
        this.d = false;
        this.f = new Date();
        this.o.post(new g(this, zVar));
    }

    abstract Date b();

    abstract Date c();

    final j d() {
        return this.n;
    }

    final int e() {
        return this.h;
    }

    final int f() {
        return this.i;
    }
}
