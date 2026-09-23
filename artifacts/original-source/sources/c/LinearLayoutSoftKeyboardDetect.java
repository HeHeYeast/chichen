package c;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import vpadn.C0104r;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class LinearLayoutSoftKeyboardDetect extends LinearLayout {
    private int a;
    private int b;

    /* renamed from: c, reason: collision with root package name */
    private int f177c;
    private int d;
    private DroidGap e;

    public LinearLayoutSoftKeyboardDetect(Context context, int i, int i2) {
        super(context);
        this.a = 0;
        this.b = 0;
        this.f177c = 0;
        this.d = 0;
        this.e = null;
        this.f177c = i;
        this.d = i2;
        this.e = (DroidGap) context;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        C0104r.a("SoftKeyboardDetect", "We are in our onMeasure method");
        int size = View.MeasureSpec.getSize(i2);
        int size2 = View.MeasureSpec.getSize(i);
        C0104r.a("SoftKeyboardDetect", "Old Height = %d", Integer.valueOf(this.a));
        C0104r.a("SoftKeyboardDetect", "Height = %d", Integer.valueOf(size));
        C0104r.a("SoftKeyboardDetect", "Old Width = %d", Integer.valueOf(this.b));
        C0104r.a("SoftKeyboardDetect", "Width = %d", Integer.valueOf(size2));
        if (this.a == 0 || this.a == size) {
            C0104r.b("SoftKeyboardDetect", "Ignore this event");
        } else if (this.d == size2) {
            int i3 = this.d;
            this.d = this.f177c;
            this.f177c = i3;
            C0104r.a("SoftKeyboardDetect", "Orientation Change");
        } else if (size > this.a) {
            if (this.e != null) {
                this.e.a.c("cordova.fireDocumentEvent('hidekeyboard');");
            }
        } else if (size < this.a && this.e != null) {
            this.e.a.c("cordova.fireDocumentEvent('showkeyboard');");
        }
        this.a = size;
        this.b = size2;
    }
}
