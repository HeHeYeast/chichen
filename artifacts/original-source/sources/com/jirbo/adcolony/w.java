package com.jirbo.adcolony;

import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class w extends h {
    static boolean H;
    static w I;
    boolean J;
    boolean K;
    ADCVideo L;

    public w(ADCVideo aDCVideo, AdColonyV4VCAd adColonyV4VCAd) {
        this.L = aDCVideo;
        this.G = adColonyV4VCAd;
        aDCVideo.E.pause();
        I = this;
        if (!a()) {
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.L.E != null) {
            H = true;
            c();
            int iCurrentTimeMillis = (((int) (System.currentTimeMillis() - this.w)) * MotionEventCompat.ACTION_MASK) / 1000;
            canvas.drawARGB(iCurrentTimeMillis <= 128 ? iCurrentTimeMillis : 128, 0, 0, 0);
            this.a.a(canvas, this.x, this.y);
            int iB = (b() * 3) / 2;
            a("Completion is required to receive", this.z, (int) (this.A - (iB * 2.75d)), canvas);
            a("your reward.", this.z, this.A - (iB * 2), canvas);
            a("Are you sure you want to skip?", this.z, (int) (this.A - (iB * 1.25d)), canvas);
            this.b.a(canvas, this.z - (this.b.f / 2), this.A - (this.b.g / 2));
            if (!this.J) {
                this.f225c.a(canvas, this.B, this.D);
            } else {
                this.d.a(canvas, this.B, this.D);
            }
            if (!this.K) {
                this.e.a(canvas, this.C, this.D);
            } else {
                this.f.a(canvas, this.C, this.D);
            }
            c("Yes", this.B, this.D, canvas);
            c("No", this.C, this.D, canvas);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        ADCVideo aDCVideo = this.L;
        if (ADCVideo.d) {
            I = null;
            return this.L.F.onTouchEvent(event);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == 1) {
            if (a(x, y, this.B, this.D) && this.J) {
                I = null;
                H = false;
                a.u = false;
                a.aa = true;
                a.M.b(this.G);
                AdColonyBrowser.A = true;
                this.L.finish();
            } else if (a(x, y, this.C, this.D) && this.K) {
                I = null;
                H = false;
                this.L.E.start();
            }
            this.J = false;
            this.K = false;
            invalidate();
        }
        if (event.getAction() != 0) {
            return true;
        }
        if (a(x, y, this.B, this.D)) {
            this.J = true;
            invalidate();
            return true;
        }
        if (!a(x, y, this.C, this.D)) {
            return true;
        }
        this.K = true;
        invalidate();
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (this.L.E != null && keycode == 4) {
            return super.onKeyDown(keycode, event);
        }
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keycode, KeyEvent event) {
        if (keycode != 4) {
            return false;
        }
        I = null;
        this.L.E.start();
        return true;
    }

    @Override // com.jirbo.adcolony.h
    void c() {
        int i = this.L.t;
        int i2 = this.L.u;
        this.x = (i - this.a.f) / 2;
        this.y = (i2 - this.a.g) / 2;
        this.z = this.x + (this.a.f / 2);
        this.A = this.y + (this.a.g / 2);
        this.D = this.y + ((int) (this.a.g - (this.f225c.g + (p * 16.0d))));
        this.B = this.x + ((int) (p * 16.0d));
        this.C = this.x + ((int) (this.a.f - (this.f225c.f + (p * 16.0d))));
    }
}
