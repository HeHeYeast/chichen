package vpadn;

import android.media.ExifInterface;
import java.io.IOException;

/* renamed from: vpadn.g, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0093g {
    private String b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f341c = null;
    private String d = null;
    private String e = null;
    private String f = null;
    private String g = null;
    private String h = null;
    private String i = null;
    private String j = null;
    private String k = null;
    private String l = null;
    private String m = null;
    private String n = null;
    private String o = null;
    private String p = null;
    private String q = null;
    private String r = null;
    public String a = null;
    private String s = null;
    private ExifInterface t = null;
    private ExifInterface u = null;

    public final void a(String str) throws IOException {
        this.t = new ExifInterface(str);
    }

    public final void b(String str) throws IOException {
        this.u = new ExifInterface(str);
    }

    public final void a() {
        this.b = this.t.getAttribute("FNumber");
        this.f341c = this.t.getAttribute("DateTime");
        this.d = this.t.getAttribute("ExposureTime");
        this.e = this.t.getAttribute("Flash");
        this.f = this.t.getAttribute("FocalLength");
        this.g = this.t.getAttribute("GPSAltitude");
        this.h = this.t.getAttribute("GPSAltitudeRef");
        this.i = this.t.getAttribute("GPSDateStamp");
        this.j = this.t.getAttribute("GPSLatitude");
        this.k = this.t.getAttribute("GPSLatitudeRef");
        this.l = this.t.getAttribute("GPSLongitude");
        this.m = this.t.getAttribute("GPSLongitudeRef");
        this.n = this.t.getAttribute("GPSProcessingMethod");
        this.o = this.t.getAttribute("GPSTimeStamp");
        this.p = this.t.getAttribute("ISOSpeedRatings");
        this.q = this.t.getAttribute("Make");
        this.r = this.t.getAttribute("Model");
        this.a = this.t.getAttribute("Orientation");
        this.s = this.t.getAttribute("WhiteBalance");
    }

    public final void b() throws IOException {
        if (this.u != null) {
            if (this.b != null) {
                this.u.setAttribute("FNumber", this.b);
            }
            if (this.f341c != null) {
                this.u.setAttribute("DateTime", this.f341c);
            }
            if (this.d != null) {
                this.u.setAttribute("ExposureTime", this.d);
            }
            if (this.e != null) {
                this.u.setAttribute("Flash", this.e);
            }
            if (this.f != null) {
                this.u.setAttribute("FocalLength", this.f);
            }
            if (this.g != null) {
                this.u.setAttribute("GPSAltitude", this.g);
            }
            if (this.h != null) {
                this.u.setAttribute("GPSAltitudeRef", this.h);
            }
            if (this.i != null) {
                this.u.setAttribute("GPSDateStamp", this.i);
            }
            if (this.j != null) {
                this.u.setAttribute("GPSLatitude", this.j);
            }
            if (this.k != null) {
                this.u.setAttribute("GPSLatitudeRef", this.k);
            }
            if (this.l != null) {
                this.u.setAttribute("GPSLongitude", this.l);
            }
            if (this.m != null) {
                this.u.setAttribute("GPSLongitudeRef", this.m);
            }
            if (this.n != null) {
                this.u.setAttribute("GPSProcessingMethod", this.n);
            }
            if (this.o != null) {
                this.u.setAttribute("GPSTimeStamp", this.o);
            }
            if (this.p != null) {
                this.u.setAttribute("ISOSpeedRatings", this.p);
            }
            if (this.q != null) {
                this.u.setAttribute("Make", this.q);
            }
            if (this.r != null) {
                this.u.setAttribute("Model", this.r);
            }
            if (this.a != null) {
                this.u.setAttribute("Orientation", this.a);
            }
            if (this.s != null) {
                this.u.setAttribute("WhiteBalance", this.s);
            }
            this.u.saveAttributes();
        }
    }

    public final int c() throws NumberFormatException {
        int i = Integer.parseInt(this.a);
        if (i == 1) {
            return 0;
        }
        if (i == 6) {
            return 90;
        }
        if (i == 3) {
            return 180;
        }
        return i == 8 ? 270 : 0;
    }
}
