package jp.co.imobile.sdkads.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.widget.RelativeLayout;
import java.util.Date;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class a extends f {
    private RelativeLayout n;
    private Activity o;

    a(z zVar, Context context, ImobileSdkAdListener imobileSdkAdListener, boolean z) {
        super(imobileSdkAdListener);
        this.o = null;
        Boolean boolValueOf = Boolean.valueOf(z);
        this.n = new RelativeLayout(context);
        this.n.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        this.n.setId(985478646);
        this.n.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.n.setClickable(true);
        this.n.setOnClickListener(new c(this));
        this.f298c = new e(context, boolValueOf);
        this.f298c.setBackgroundColor(0);
        this.f298c.setVerticalScrollbarOverlay(true);
        this.f298c.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT > 10 && this.f298c.isHardwareAccelerated()) {
            this.f298c.setLayerType(1, null);
        }
        this.f298c.getSettings().setJavaScriptEnabled(true);
        this.f298c.a(new d(this, zVar));
        this.n.addView(this.f298c, -1, -1);
    }

    static /* synthetic */ void a(a aVar, int i, int i2, int i3, int i4) {
        aVar.i = i;
        aVar.h = i2;
        aVar.n.getLayoutParams().width = i3;
        aVar.n.getLayoutParams().height = i4;
        aVar.f298c.getLayoutParams().width = i3;
        aVar.f298c.getLayoutParams().height = i4;
        aVar.n.requestLayout();
    }

    static /* synthetic */ void d(a aVar) {
        if (aVar.l != null) {
            aVar.l.onAdCloseCompleted();
            aVar.l = null;
        }
    }

    @Override // jp.co.imobile.sdkads.android.f
    final RelativeLayout a(Activity activity) {
        x.a(null);
        if (activity != null) {
            this.o = activity;
        }
        return this.n;
    }

    @Override // jp.co.imobile.sdkads.android.f
    final void a() {
        x.a(null);
        this.f298c.a("javascript:ShowComplete();");
        a(j.DISPLAING);
    }

    @Override // jp.co.imobile.sdkads.android.f
    final void a(ImobileSdkAdListener imobileSdkAdListener, Rect rect) throws y {
        x.a(null);
        if (this.o == null) {
            x.a(null);
            throw new y(FailNotificationReason.UNKNOWN);
        }
        this.l = imobileSdkAdListener;
        if (this.d.booleanValue()) {
            x.a(null);
            StringBuilder sb = new StringBuilder("javascript:ShowAdBefore('");
            r.a();
            sb.append(r.a(this.o, ImobileSdkAd.d(), rect)).append("');");
            x.a(null);
            e eVar = this.f298c;
            StringBuilder sb2 = new StringBuilder("javascript:ShowAdBefore('");
            r.a();
            eVar.a(sb2.append(r.a(this.o, ImobileSdkAd.d(), rect)).append("');").toString());
        } else {
            x.a(null);
            this.e = new b(this, rect);
        }
        x.a(null);
    }

    @Override // jp.co.imobile.sdkads.android.f
    final Date b() {
        return this.a;
    }

    @Override // jp.co.imobile.sdkads.android.f
    final Date c() {
        return this.f;
    }
}
