package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class ao extends ImobileSdkAdListener {
    final /* synthetic */ an a;
    private final /* synthetic */ ViewGroup b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ RelativeLayout f295c;
    private final /* synthetic */ f d;
    private final /* synthetic */ Activity e;

    ao(an anVar, ViewGroup viewGroup, RelativeLayout relativeLayout, f fVar, Activity activity) {
        this.a = anVar;
        this.b = viewGroup;
        this.f295c = relativeLayout;
        this.d = fVar;
        this.e = activity;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdCloseCompleted() {
        this.d.a(j.DISPLAYED);
        this.a.t.onAdCloseCompleted();
        this.a.u = null;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdReadyCompleted() {
        x.a(null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        if (this.b != null) {
            this.b.addView(this.f295c);
        } else {
            layoutParams.topMargin = this.d.e();
            layoutParams.leftMargin = this.d.f();
            layoutParams.gravity = 48;
            this.e.addContentView(this.f295c, layoutParams);
        }
        this.d.a();
        this.a.t.onAdShowCompleted();
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onDismissAdScreen() {
        this.a.t.onDismissAdScreen();
    }
}
