package com.jirbo.adcolony;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class h extends View {
    static double p;
    static String q = "";
    static String r = "";
    static boolean s = true;
    static Paint t = new Paint(1);
    static float[] u = new float[80];
    int A;
    int B;
    int C;
    int D;
    int E;
    String F;
    AdColonyV4VCAd G;
    ADCImage a;
    ADCImage b;

    /* renamed from: c, reason: collision with root package name */
    ADCImage f225c;
    ADCImage d;
    ADCImage e;
    ADCImage f;
    ADCImage g;
    ADCImage h;
    double i;
    double j;
    double k;
    double l;
    double m;
    boolean n;
    ArrayList<ADCImage> o;
    AdColonyInterstitialAd v;
    long w;
    int x;
    int y;
    int z;

    h() {
        super(a.b());
        this.i = 2.8d;
        this.j = 2.05d;
        this.k = 1.3d;
        this.l = 2.5d;
        this.m = 1.5d;
        this.o = new ArrayList<>();
        this.w = System.currentTimeMillis();
    }

    public boolean a() {
        double d = 0.8d;
        if (this.a == null) {
            this.a = new ADCImage(a.j("pre_popup_bg"));
            this.b = new ADCImage(a.j("v4vc_logo"));
            this.f225c = new ADCImage(a.j("yes_button_normal"));
            this.d = new ADCImage(a.j("yes_button_down"));
            this.e = new ADCImage(a.j("no_button_normal"));
            this.f = new ADCImage(a.j("no_button_down"));
            this.h = new ADCImage(a.j("done_button_normal"));
            this.g = new ADCImage(a.j("done_button_down"));
            this.o.add(this.a);
            this.o.add(this.b);
            this.o.add(this.f225c);
            this.o.add(this.d);
            this.o.add(this.e);
            this.o.add(this.f);
            this.o.add(this.h);
            this.o.add(this.g);
            Display defaultDisplay = a.b().getWindowManager().getDefaultDisplay();
            int width = defaultDisplay.getWidth();
            double d2 = defaultDisplay.getHeight() > width ? (r4 - width) / 360.0d : (width - r4) / 360.0d;
            if (d2 < 0.8d && !a.m) {
                this.n = true;
            }
            double d3 = d2 <= 2.5d ? d2 : 2.5d;
            if (d3 >= 0.8d) {
                d = d3;
            } else if (!a.m) {
                d = 1.7d;
            }
            p = d;
            if (this.n) {
                this.i = 2.6d;
                this.j = 1.8d;
                this.k = 1.0d;
                this.l = 2.2d;
                this.m = 1.2d;
            }
            this.a.a(d / 1.8d);
            this.b.a(d / 1.8d);
            this.d.a(d / 1.8d);
            this.f.a(d / 1.8d);
            this.f225c.a(d / 1.8d);
            this.e.a(d / 1.8d);
            this.g.a(d / 1.8d);
            this.h.a(d / 1.8d);
            t.setTextSize((float) (18.0d * d));
            if (this.n) {
                t.setTextSize((float) (d * 9.0d));
            }
            t.setFakeBoldText(true);
        }
        return true;
    }

    public h(String str, int i, AdColonyInterstitialAd adColonyInterstitialAd) {
        super(AdColony.activity());
        this.i = 2.8d;
        this.j = 2.05d;
        this.k = 1.3d;
        this.l = 2.5d;
        this.m = 1.5d;
        this.o = new ArrayList<>();
        this.w = System.currentTimeMillis();
        this.F = str;
        this.E = i;
        this.v = adColonyInterstitialAd;
        if (a()) {
            AdColony.activity().addContentView(this, new FrameLayout.LayoutParams(-1, -1, 17));
        }
    }

    int a(String str) {
        t.getTextWidths(str, u);
        float f = BitmapDescriptorFactory.HUE_RED;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            f += u[i];
        }
        return (int) f;
    }

    int b() {
        return (int) t.getTextSize();
    }

    void a(String str, int i, int i2, Canvas canvas) {
        int iA = i - (a(str) / 2);
        t.setColor(-986896);
        canvas.drawText(str, iA + 1, i2 + 1, t);
        t.setColor(-8355712);
        canvas.drawText(str, iA, i2, t);
    }

    void b(String str, int i, int i2, Canvas canvas) {
        int iA = i - (a(str) / 2);
        t.setColor(-8355712);
        canvas.drawText(str, iA + 2, i2 + 2, t);
        t.setColor(-1);
        canvas.drawText(str, iA, i2, t);
    }

    void c(String str, int i, int i2, Canvas canvas) {
        b(str, (this.f225c.f / 2) + i, (this.f225c.g / 2) + i2 + ((b() * 4) / 10), canvas);
    }

    boolean a(int i, int i2, int i3, int i4) {
        return i >= i3 && i2 >= i4 && i < this.f225c.f + i3 && i2 < this.f225c.g + i4;
    }

    void a(String str, String str2) {
        int i;
        int iA = a(str);
        q = "";
        r = "";
        if (iA > (this.a.f - a("WW")) - a(str2)) {
            s = false;
            int i2 = 0;
            String str3 = "";
            int iA2 = 0;
            while (iA2 < (this.a.f - a("WW")) - a(str2)) {
                str3 = str3 + str.charAt(i2);
                i2++;
                iA2 = a(str3);
            }
            int i3 = 0;
            int i4 = 0;
            while (i3 < i2) {
                if (str3.charAt(i3) == ' ' && i3 >= 5) {
                    q = str.substring(0, i3);
                    i = i3;
                } else {
                    q = i4 < 5 ? str.substring(0, i2) : q;
                    i = i4;
                }
                i3++;
                i4 = i;
            }
            r = i4 < 5 ? str.substring(i2) : str.substring(i4);
            return;
        }
        s = true;
        q = str;
        r = "";
    }

    void c() {
        double d = this.n ? 12.0d : 16.0d;
        Display defaultDisplay = a.b().getWindowManager().getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        this.x = (width - this.a.f) / 2;
        this.y = ((height - this.a.g) / 2) - 80;
        this.z = this.x + (this.a.f / 2);
        this.A = this.y + (this.a.g / 2);
        this.D = this.y + ((int) (this.a.g - (this.f225c.g + (p * d))));
        this.B = this.x + ((int) (p * d));
        this.C = ((int) (this.a.f - ((d * p) + this.f225c.f))) + this.x;
    }
}
