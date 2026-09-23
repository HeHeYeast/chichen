package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.graphics.Point;
import android.view.ViewGroup;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class q extends ImobileSdkAdListener {
    final /* synthetic */ ImobileSdkAd a;
    private final /* synthetic */ z b;

    /* renamed from: c, reason: collision with root package name */
    private final /* synthetic */ Activity f302c;
    private final /* synthetic */ ImobileSdkAdListener d;
    private final /* synthetic */ Point e;
    private final /* synthetic */ Boolean f;
    private final /* synthetic */ ViewGroup g;
    private final /* synthetic */ ImobileIconParams h;
    private final /* synthetic */ Boolean i;

    q(ImobileSdkAd imobileSdkAd, z zVar, Activity activity, ImobileSdkAdListener imobileSdkAdListener, Point point, Boolean bool, ViewGroup viewGroup, ImobileIconParams imobileIconParams, Boolean bool2) {
        this.a = imobileSdkAd;
        this.b = zVar;
        this.f302c = activity;
        this.d = imobileSdkAdListener;
        this.e = point;
        this.f = bool;
        this.g = viewGroup;
        this.h = imobileIconParams;
        this.i = bool2;
    }

    @Override // jp.co.imobile.sdkads.android.ImobileSdkAdListener
    public final void onAdReadyCompleted() {
        this.b.a(this.f302c, this.d, this.e, this.f, this.g, this.h, this.i);
    }
}
