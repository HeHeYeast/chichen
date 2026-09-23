package com.jirbo.adcolony;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.immersion.hapticmediasdk.HapticContentSDKFactory;
import com.jirbo.adcolony.ab;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ae extends View implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    static float[] ay = new float[80];
    boolean A;
    boolean B;
    boolean C;
    boolean D;
    boolean E;
    boolean F;
    boolean G;
    boolean H;
    boolean I;
    boolean J;
    boolean K;
    boolean L;
    boolean M;
    boolean N;
    boolean O;
    boolean P;
    boolean Q;
    boolean R;
    boolean S;
    boolean T;
    boolean U;
    Canvas V;
    String W;
    String Z;
    WebView a;
    float aA;
    float aB;
    float aC;
    float aD;
    float aE;
    float aF;
    Paint aG;
    RectF aH;
    b aI;
    Handler aJ;
    String aa;
    String ab;
    String ac;
    ab.b ad;
    Paint ae;
    Paint af;
    Paint ag;
    Paint ah;
    Rect ai;
    ADCImage aj;
    ADCImage ak;
    ADCImage al;
    ADCImage am;
    ADCImage an;
    ADCImage ao;
    ADCImage ap;
    ADCImage aq;
    ADCImage ar;
    ADCImage as;
    ADCImage at;
    ADCImage[] au;
    ADCImage[] av;
    m aw;
    String[] ax;
    float az;
    WebView b;

    /* renamed from: c, reason: collision with root package name */
    View f214c;
    ADCVideo d;
    double e;
    double f;
    int g;
    int h;
    int i;
    int j;
    int k;
    int l;
    int m;
    int n;
    int o;
    int p;
    int q;
    int r;
    int s;
    int t;
    int u;
    long v;
    long w;
    float x;
    boolean y;
    boolean z;

    ae(ADCVideo aDCVideo) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super(aDCVideo);
        this.e = 1.0d;
        this.f = 1.0d;
        this.g = 99;
        this.h = 0;
        this.y = true;
        this.z = true;
        this.A = true;
        this.B = true;
        this.C = true;
        this.D = true;
        this.W = com.jirbo.adcolony.a.l.a.b;
        this.ae = new Paint();
        this.af = new Paint(1);
        this.ag = new Paint(1);
        this.ah = new Paint(1);
        this.ai = new Rect();
        this.au = new ADCImage[4];
        this.av = new ADCImage[4];
        this.ax = new String[4];
        this.aG = new Paint(1);
        this.aH = new RectF();
        this.aI = new b();
        this.aJ = new Handler() { // from class: com.jirbo.adcolony.ae.2
            @Override // android.os.Handler
            public void handleMessage(Message m) throws Exception {
                if (!ae.this.d.isFinishing() && ae.this.d.E != null) {
                    ae.this.a(m.what);
                }
            }
        };
        this.d = aDCVideo;
        this.M = com.jirbo.adcolony.a.l.a.s;
        if (com.jirbo.adcolony.a.J != null) {
            this.M |= com.jirbo.adcolony.a.J.i.v.l.a;
            com.jirbo.adcolony.a.J.n = com.jirbo.adcolony.a.J.o;
        }
        this.x = aDCVideo.getResources().getDisplayMetrics().density;
        this.Q = com.jirbo.adcolony.a.O;
        if (com.jirbo.adcolony.a.e != null) {
            com.jirbo.adcolony.a.T = com.jirbo.adcolony.a.e;
        }
        if (com.jirbo.adcolony.a.J != null && com.jirbo.adcolony.a.J.i.u.d) {
            this.N = !this.Q;
        }
        if (this.N) {
            this.aj = new ADCImage(com.jirbo.adcolony.a.j("end_card_filepath"));
            this.n = this.aj.f;
            this.o = this.aj.g;
            if (this.n == 0) {
                this.n = 480;
            }
            if (this.o == 0) {
                this.o = 320;
            }
            this.au[0] = new ADCImage(com.jirbo.adcolony.a.j("info_image_normal"));
            this.au[1] = new ADCImage(com.jirbo.adcolony.a.j("download_image_normal"));
            this.au[2] = new ADCImage(com.jirbo.adcolony.a.j("replay_image_normal"));
            this.au[3] = new ADCImage(com.jirbo.adcolony.a.j("continue_image_normal"));
            this.av[0] = new ADCImage(com.jirbo.adcolony.a.j("info_image_down"), true);
            this.av[1] = new ADCImage(com.jirbo.adcolony.a.j("download_image_down"), true);
            this.av[2] = new ADCImage(com.jirbo.adcolony.a.j("replay_image_down"), true);
            this.av[3] = new ADCImage(com.jirbo.adcolony.a.j("continue_image_down"), true);
            this.ax[0] = "Info";
            this.ax[1] = "Download";
            this.ax[2] = "Replay";
            this.ax[3] = "Continue";
        } else if (this.Q) {
            this.ao = new ADCImage(com.jirbo.adcolony.a.j("reload_image_normal"));
            this.am = new ADCImage(com.jirbo.adcolony.a.j("close_image_normal"));
            this.an = new ADCImage(com.jirbo.adcolony.a.j("close_image_down"));
            this.ap = new ADCImage(com.jirbo.adcolony.a.j("reload_image_down"));
            this.as = new ADCImage(com.jirbo.adcolony.a.j("browser_icon"));
            this.f214c = new a(aDCVideo);
            b();
        }
        if (this.M) {
            this.ak = new ADCImage(com.jirbo.adcolony.a.j("skip_video_image_normal"));
            this.al = new ADCImage(com.jirbo.adcolony.a.j("skip_video_image_down"));
            this.p = com.jirbo.adcolony.a.h("skip_delay") * 1000;
        }
        this.aG.setStyle(Paint.Style.STROKE);
        float f = 2.0f * aDCVideo.getResources().getDisplayMetrics().density;
        if ((f > 6.0f ? 6.0f : f) < 4.0f) {
        }
        this.aG.setStrokeWidth(2.0f * aDCVideo.getResources().getDisplayMetrics().density);
        this.aG.setColor(-3355444);
        this.S = false;
        this.L = false;
        this.T = false;
        if (com.jirbo.adcolony.a.J != null) {
            this.L = com.jirbo.adcolony.a.J.i.v.m.a;
            this.T = com.jirbo.adcolony.a.i("image_overlay_enabled");
        }
        if (this.L) {
            this.aq = new ADCImage(com.jirbo.adcolony.a.j("engagement_image_normal"));
            this.ar = new ADCImage(com.jirbo.adcolony.a.j("engagement_image_down"));
            this.ab = com.jirbo.adcolony.a.J.i.v.m.j;
            this.Z = com.jirbo.adcolony.a.J.i.v.m.l;
            this.aa = com.jirbo.adcolony.a.J.i.v.m.o;
            this.r = com.jirbo.adcolony.a.J.i.v.m.f236c;
            this.q = com.jirbo.adcolony.a.h("engagement_delay") * 1000;
            if (this.Z.equals("")) {
                this.Z = "Learn More";
            }
            if (!this.aa.equals("")) {
                this.G = true;
            }
            if (this.G) {
                this.b = new WebView(aDCVideo);
                this.b.setBackgroundColor(0);
            }
            if (this.aq == null || this.ar == null) {
                this.L = false;
            }
        }
        if (this.T) {
            this.at = new ADCImage(com.jirbo.adcolony.a.j("image_overlay_filepath"));
            this.at.a(AdColony.isTablet() ? (this.r * (this.x / 1.0d)) / this.at.g : (this.r * (this.x / 0.75d)) / this.at.g);
        }
        if (ADCVideo.d) {
            e();
        }
        this.ae.setColor(-1);
        this.ag.setTextSize(24.0f);
        this.ag.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.af.setColor(-3355444);
        this.af.setTextSize(20.0f);
        this.af.setTextAlign(Paint.Align.CENTER);
        this.ah.setTextSize(20.0f);
        this.ah.setColor(-1);
        try {
            getClass().getMethod("setLayerType", Integer.TYPE, Paint.class).invoke(this, 1, null);
        } catch (Exception e) {
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (!this.F) {
            a();
            this.V = canvas;
            if (!this.O && this.M) {
                this.O = this.d.E.getCurrentPosition() > this.p;
            }
            if (!this.P && this.L) {
                this.P = this.d.E.getCurrentPosition() > this.q;
            }
            ADCVideo aDCVideo = this.d;
            if (ADCVideo.d && this.N) {
                canvas.drawARGB((this.d.z >> 24) & MotionEventCompat.ACTION_MASK, 0, 0, 0);
                this.aj.a(canvas, (this.d.t - this.aj.f) / 2, (this.d.u - this.aj.g) / 2);
                int iC = this.aj.c() + ((int) (186.0d * this.e));
                int iD = this.aj.d() + ((int) (470.0d * this.e));
                for (int i = 0; i < this.au.length; i++) {
                    if (this.t == i + 1 || (this.u == i + 1 && !this.A && this.u != 0)) {
                        this.av[i].a(this.e);
                        this.av[i].a(canvas, iC, iD);
                        iC = (int) (iC + (157.0f * this.e));
                    } else if (this.A || i + 1 != this.u) {
                        this.au[i].a(this.e);
                        this.au[i].a(canvas, iC, iD);
                        iC = (int) (iC + (157.0f * this.e));
                    }
                    this.af.setColor(-1);
                    this.af.clearShadowLayer();
                    canvas.drawText(this.ax[i], this.au[i].c() + (this.au[i].f / 2), this.au[i].d() + this.au[i].g, this.af);
                }
                return;
            }
            ADCVideo aDCVideo2 = this.d;
            if (ADCVideo.d && this.Q) {
                this.am.a(this.f);
                this.an.a(this.f);
                this.ao.a(this.f);
                this.ap.a(this.f);
                this.i = (com.jirbo.adcolony.a.m || this.i == 0) ? this.d.t - this.am.f : this.i;
                this.j = 0;
                this.k = 0;
                this.l = 0;
                if (this.H) {
                    this.an.a(canvas, this.i, this.j);
                } else {
                    this.am.a(canvas, this.i, this.j);
                }
                if (this.I) {
                    this.ap.a(canvas, this.k, this.l);
                } else {
                    this.ao.a(canvas, this.k, this.l);
                }
                i();
                return;
            }
            if (this.d.E != null) {
                com.jirbo.adcolony.a.l.a(this.d.E.getCurrentPosition() / this.d.E.getDuration(), this.d.G);
                if (this.d.J) {
                    this.d.H.update(this.d.E.getCurrentPosition());
                }
                int currentPosition = this.d.E.getCurrentPosition();
                int i2 = ((this.s - currentPosition) + 999) / 1000;
                int i3 = (this.S && i2 == 1) ? 0 : i2;
                if (i3 == 0) {
                    this.S = true;
                }
                if (currentPosition >= 500) {
                    if (this.B) {
                        this.aA = (float) (360.0d / (this.s / 1000.0d));
                        this.B = false;
                        this.af.getTextBounds("0123456789", 0, 9, new Rect());
                        this.aD = r0.height();
                    }
                    this.aB = getWidth();
                    this.aC = getHeight();
                    this.aE = this.aD;
                    this.aF = (this.d.u - this.aD) - this.m;
                    this.aH.set(this.aE - (this.aD / 2.0f), this.aF - (2.0f * this.aD), this.aE + (2.0f * this.aD), this.aF + (this.aD / 2.0f));
                    this.aG.setShadowLayer((int) (4.0d * this.e), BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR);
                    this.az = (float) (((this.s / 1000.0d) - (currentPosition / 1000.0d)) * this.aA);
                    canvas.drawArc(this.aH, 270.0f, this.az, false, this.aG);
                    ADCVideo aDCVideo3 = this.d;
                    if (!ADCVideo.d) {
                        this.af.setColor(-3355444);
                        this.af.setShadowLayer((int) (2.0d * this.e), BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR);
                        this.af.setTextAlign(Paint.Align.CENTER);
                        this.af.setLinearText(true);
                        canvas.drawText("" + i3, this.aH.centerX(), (float) (this.aH.centerY() + (this.af.getFontMetrics().bottom * 1.35d)), this.af);
                    }
                    if (this.M) {
                        ADCVideo aDCVideo4 = this.d;
                        if (!ADCVideo.d && this.O) {
                            if (this.t == 10) {
                                this.al.a(canvas, this.d.t - this.al.f, (int) (this.e * 4.0d));
                            } else {
                                this.ak.a(canvas, this.d.t - this.ak.f, (int) (this.e * 4.0d));
                            }
                        }
                    }
                    if (this.L && this.P) {
                        if (!this.G && !this.T) {
                            if (this.J) {
                                this.ar.c((int) ((this.d.t - this.ar.f) - (this.aD / 2.0f)), ((this.d.u - this.ar.g) - this.m) - ((int) (this.aD / 2.0f)));
                                this.ar.a(canvas);
                            } else {
                                this.aq.c((int) ((this.d.t - this.aq.f) - (this.aD / 2.0f)), ((this.d.u - this.aq.g) - this.m) - ((int) (this.aD / 2.0f)));
                                this.aq.a(canvas);
                            }
                            this.ag.setTextAlign(Paint.Align.CENTER);
                            canvas.drawText(this.Z, this.aq.e.centerX(), (float) (this.aq.e.centerY() + (this.ag.getFontMetrics().bottom * 1.35d)), this.ag);
                        } else if (!this.G && this.T) {
                            this.at.c((int) ((this.d.t - this.at.f) - (this.aD / 2.0f)), ((this.d.u - this.at.g) - this.m) - ((int) (this.aD / 2.0f)));
                            this.at.a(canvas);
                        }
                    }
                }
                if (w.I != null) {
                    w.I.onDraw(canvas);
                }
            }
            ADCVideo aDCVideo5 = this.d;
            if (ADCVideo.i) {
                invalidate();
            }
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.m = this.d.u - h;
        if (Build.MODEL.equals("Kindle Fire")) {
            this.m = 20;
        }
        if (Build.MODEL.equals("SCH-I800")) {
            this.m = 25;
        }
        if (Build.MODEL.equals("SHW-M380K") || Build.MODEL.equals("SHW-M380S") || Build.MODEL.equals("SHW-M380W")) {
            this.m = 40;
        }
    }

    void a(int i) throws Exception {
        try {
            if (this.C || i == 10) {
                this.C = false;
                switch (i) {
                    case 1:
                        this.t = 0;
                        com.jirbo.adcolony.a.a("info", "{\"ad_slot\":" + com.jirbo.adcolony.a.J.h.k.f215c + "}", this.d.G);
                        String strJ = com.jirbo.adcolony.a.j("info_url");
                        l.b.a("INFO ").b((Object) strJ);
                        if (strJ.startsWith("market:") || strJ.startsWith("amzn:")) {
                            this.d.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strJ)));
                            break;
                        } else {
                            AdColonyBrowser.url = strJ;
                            this.d.startActivity(new Intent(this.d, (Class<?>) AdColonyBrowser.class));
                            break;
                        }
                        break;
                    case 2:
                        this.t = 0;
                        com.jirbo.adcolony.a.a("download", "{\"ad_slot\":" + com.jirbo.adcolony.a.J.h.k.f215c + "}", this.d.G);
                        String strJ2 = com.jirbo.adcolony.a.j("download_url");
                        l.b.a("DOWNLOAD ").b((Object) strJ2);
                        if (strJ2.startsWith("market:") || strJ2.startsWith("amzn:")) {
                            this.d.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strJ2)));
                            break;
                        } else {
                            AdColonyBrowser.url = strJ2;
                            this.d.startActivity(new Intent(this.d, (Class<?>) AdColonyBrowser.class));
                            break;
                        }
                        break;
                    case 3:
                        this.t = 0;
                        h();
                        invalidate();
                        break;
                    case 4:
                        this.t = 0;
                        this.d.E.a();
                        f();
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        this.t = 0;
                        break;
                    case 10:
                        this.t = 0;
                        g();
                        break;
                }
                new Handler().postDelayed(new Runnable() { // from class: com.jirbo.adcolony.ae.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ae.this.C = true;
                    }
                }, 1500L);
            }
        } catch (RuntimeException e) {
            this.C = true;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int iA;
        int action = event.getAction();
        if (w.I != null) {
            w.I.onTouchEvent(event);
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (action == 0) {
            ADCVideo aDCVideo = this.d;
            if (ADCVideo.d && this.Q) {
                if (a(this.am, x, y)) {
                    this.H = true;
                    invalidate();
                    return true;
                }
                if (!a(this.ao, x, y)) {
                    return false;
                }
                this.I = true;
                invalidate();
                return true;
            }
            ADCVideo aDCVideo2 = this.d;
            if (ADCVideo.d && this.N) {
                x = (int) ((event.getX() - this.aj.c()) / (this.e * 2.0d));
                y = (int) ((event.getY() - this.aj.d()) / (this.e * 2.0d));
                if (this.t == 0 && y >= 235 && y < 305) {
                    int iA2 = a(x, y);
                    this.t = iA2;
                    this.u = iA2;
                    this.A = false;
                    invalidate();
                }
            }
            if (this.M && this.O && this.d.E != null && a(this.ak, x, y)) {
                this.t = 10;
                this.u = this.t;
                this.A = false;
                invalidate();
                return true;
            }
            if (this.L && this.P && (a(this.aq, x, y) || a(this.at, x, y))) {
                this.J = true;
                invalidate();
                return true;
            }
        } else {
            if (action == 1) {
                ADCVideo aDCVideo3 = this.d;
                if (ADCVideo.d && this.Q) {
                    if (a(this.am, x, y) && this.H) {
                        this.t = 4;
                        if (this.a != null) {
                            this.a.clearCache(true);
                        }
                        this.aJ.sendMessageDelayed(this.aJ.obtainMessage(this.t), 250L);
                        return true;
                    }
                    if (a(this.ao, x, y) && this.I) {
                        this.t = 3;
                        if (this.a != null) {
                            this.a.clearCache(true);
                        }
                        this.aJ.sendMessageDelayed(this.aJ.obtainMessage(this.t), 250L);
                        return true;
                    }
                }
                ADCVideo aDCVideo4 = this.d;
                if (ADCVideo.d && this.N) {
                    x = (int) ((event.getX() - this.aj.c()) / (this.e * 2.0d));
                    y = (int) ((event.getY() - this.aj.d()) / (this.e * 2.0d));
                    if (!this.A && y >= 235 && y < 305 && (iA = a(x, y)) > 0 && iA == this.u) {
                        this.aJ.sendMessageDelayed(this.aJ.obtainMessage(iA), 250L);
                    }
                }
                if (this.M && this.O && this.d.E != null && a(this.ak, x, y)) {
                    this.t = 10;
                    this.A = true;
                    this.u = this.t;
                    this.aJ.sendMessageDelayed(this.aJ.obtainMessage(this.t), 250L);
                    return true;
                }
                if (this.L && this.P && (a(this.aq, x, y) || a(this.at, x, y))) {
                    this.J = false;
                    if (this.ab.startsWith("market:") || this.ab.startsWith("amzn:")) {
                        this.d.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.ab)));
                    } else if (this.ab.startsWith("v4iap:")) {
                        this.ac = this.d.G.m;
                        this.d.G.u = AdColonyIAPEngagement.OVERLAY;
                        this.L = false;
                        this.U = true;
                        this.T = false;
                        g();
                    } else {
                        AdColonyBrowser.url = this.ab;
                        this.d.startActivity(new Intent(this.d, (Class<?>) AdColonyBrowser.class));
                    }
                    com.jirbo.adcolony.a.a("in_video_engagement", "{\"ad_slot\":" + com.jirbo.adcolony.a.J.h.k.f215c + "}", this.d.G);
                    return true;
                }
                this.H = false;
                this.I = false;
                this.J = false;
                this.A = true;
                this.t = 0;
                invalidate();
                return true;
            }
            if (action == 3) {
                this.H = false;
                this.I = false;
                this.J = false;
                this.A = true;
                this.t = 0;
                invalidate();
                return true;
            }
        }
        return true;
    }

    int a(int i, int i2) {
        if (i >= this.g && i < this.g + 62) {
            return 1;
        }
        if (i >= this.g + 78 && i < this.g + 78 + 62) {
            return 2;
        }
        if (i >= this.g + 78 + 78 && i < this.g + 78 + 78 + 62) {
            return 3;
        }
        if (i < this.g + 78 + 78 + 78 || i >= this.g + 78 + 78 + 78 + 62) {
            return (this.d.E == null || !this.M || i < this.d.E.getWidth() - this.ak.f || i2 > this.ak.g) ? 0 : 10;
        }
        return 4;
    }

    public boolean a(ADCImage aDCImage, int i, int i2) {
        return aDCImage != null && i < (aDCImage.c() + aDCImage.f) + 8 && i > aDCImage.c() + (-8) && i2 < (aDCImage.d() + aDCImage.g) + 8 && i2 > aDCImage.d() + (-8);
    }

    public void a() {
        double d;
        boolean zB = this.d.b();
        this.y |= zB;
        if (this.d.E != null) {
            if (this.s <= 0) {
                this.s = this.d.E.getDuration();
            }
            if (zB) {
                setLayoutParams(new FrameLayout.LayoutParams(this.d.t, this.d.u, 17));
                this.d.E.setLayoutParams(new FrameLayout.LayoutParams(this.d.x, this.d.y, 17));
                this.y = true;
            }
        }
        if (this.y) {
            this.y = false;
            if (this.z) {
                DisplayMetrics displayMetrics = AdColony.activity().getResources().getDisplayMetrics();
                float f = displayMetrics.widthPixels / displayMetrics.xdpi;
                float f2 = displayMetrics.heightPixels / displayMetrics.ydpi;
                double dSqrt = Math.sqrt((displayMetrics.heightPixels * displayMetrics.heightPixels) + (displayMetrics.widthPixels * displayMetrics.widthPixels)) / Math.sqrt((f * f) + (f2 * f2));
                this.f = dSqrt / 280.0d < 0.7d ? 0.7d : dSqrt / 280.0d;
                if (!AdColony.isTablet() && this.f == 0.7d) {
                    this.f = 1.0d;
                }
                float f3 = this.f * 20.0d < 18.0d ? 18.0f : (float) (this.f * 20.0d);
                float f4 = this.f * 20.0d < 18.0d ? 18.0f : (float) (this.f * 20.0d);
                this.af.setTextSize(f3);
                this.ah.setTextSize(f3);
                this.ag.setTextSize(f4);
                if (this.L && this.aq != null && this.ar != null) {
                    this.aq.a(b(this.Z + (this.aq.f * 2)), this.aq.g);
                    this.ar.a(b(this.Z + (this.ar.f * 2)), this.ar.g);
                }
                if (this.d.t > this.d.u) {
                    int i = this.d.u;
                } else {
                    int i2 = this.d.t;
                }
                this.z = false;
            }
            if (this.Q) {
                if (zB && this.a != null) {
                    this.a.setLayoutParams(new FrameLayout.LayoutParams(this.d.t, this.d.u - this.m, 17));
                }
                this.e = ((double) this.d.y) / 640.0d < 0.9d ? 0.9d : this.d.y / 640.0d;
                if (!AdColony.isTablet() && this.e == 0.9d) {
                    this.e = 1.2d;
                }
            }
            if (this.N) {
                double d2 = this.n / this.o;
                if (this.d.t / d2 > this.d.u / 1.0d) {
                    d = this.d.u / 1.0d;
                } else {
                    d = this.d.t / d2;
                }
                this.d.x = (int) (d2 * d);
                this.d.y = (int) (d * 1.0d);
                this.e = this.d.t > this.d.u ? this.d.y / 640.0d : this.d.y / 960.0d;
                this.aj.a(((double) this.d.t) / ((double) this.n) > ((double) this.d.u) / ((double) this.o) ? this.d.u / this.o : this.d.t / this.n);
                this.aj.d(this.d.t, this.d.u);
            }
            if (this.L && this.aq != null && this.ar != null) {
                if (this.aq != null && this.ar != null && this.aq.b != null && this.ar.b != null) {
                    int height = (int) (this.aq.b.getHeight() * this.f);
                    int height2 = (int) (this.ar.b.getHeight() * this.f);
                    this.aq.b(this.aq.f, height);
                    this.ar.b(this.ar.f, height2);
                } else {
                    this.L = false;
                }
            }
            if (this.M) {
                this.ak.a(this.f);
                this.al.a(this.f);
            }
        }
    }

    void b() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.a = new WebView(this.d);
        this.a.setFocusable(true);
        this.a.setHorizontalScrollBarEnabled(false);
        this.a.setVerticalScrollBarEnabled(false);
        WebSettings settings = this.a.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setBuiltInZoomControls(true);
        settings.setGeolocationEnabled(true);
        this.a.setWebChromeClient(new WebChromeClient() { // from class: com.jirbo.adcolony.ae.3
            @Override // android.webkit.WebChromeClient
            public boolean onConsoleMessage(ConsoleMessage cm) {
                String strSourceId = cm.sourceId();
                if (strSourceId == null) {
                    strSourceId = "Internal";
                } else {
                    int iLastIndexOf = strSourceId.lastIndexOf(47);
                    if (iLastIndexOf != -1) {
                        strSourceId = strSourceId.substring(iLastIndexOf + 1);
                    }
                }
                l.b.a(cm.message()).a(" [").a(strSourceId).a(" line ").a(cm.lineNumber()).b((Object) "]");
                return true;
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        this.d.O = new FrameLayout(this.d);
        if (com.jirbo.adcolony.a.i("hardware_acceleration_disabled")) {
            try {
                this.d.O.getClass().getMethod("setLayerType", Integer.TYPE, Paint.class).invoke(this.a, 1, null);
            } catch (Exception e) {
            }
        }
        this.aw = new m(this.d, this.a, this.d);
        this.a.setWebViewClient(new WebViewClient() { // from class: com.jirbo.adcolony.ae.4
            String a = com.jirbo.adcolony.a.T;

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                l.a.a("DEC request: ").b((Object) url);
                if (url.contains("mraid:")) {
                    ae.this.aw.a(url);
                    return true;
                }
                if (!url.contains("youtube")) {
                    return url.contains("mraid.js");
                }
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + url));
                intent.putExtra("VIDEO_ID", url);
                ae.this.d.startActivity(intent);
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onLoadResource(WebView view, String url) {
                l.a.a("DEC onLoad: ").b((Object) url);
                if (url.equals(this.a)) {
                    l.a.b((Object) "DEC disabling mouse events");
                    ae.this.a("if (typeof(CN) != 'undefined' && CN.div) {\n  if (typeof(cn_dispatch_on_touch_begin) != 'undefined') CN.div.removeEventListener('mousedown',  cn_dispatch_on_touch_begin, true);\n  if (typeof(cn_dispatch_on_touch_end) != 'undefined')   CN.div.removeEventListener('mouseup',  cn_dispatch_on_touch_end, true);\n  if (typeof(cn_dispatch_on_touch_move) != 'undefined')  CN.div.removeEventListener('mousemove',  cn_dispatch_on_touch_move, true);\n}\n");
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.equals(this.a)) {
                    ae.this.d.k = true;
                    ae.this.v = System.currentTimeMillis();
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                if (url.equals(this.a) || com.jirbo.adcolony.a.T.startsWith("<")) {
                    ae.this.D = false;
                    ae.this.d.l = true;
                    ae.this.w = System.currentTimeMillis();
                    ae.this.d.p = (ae.this.w - ae.this.v) / 1000.0d;
                }
                ae.this.d.N.removeView(ae.this.f214c);
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            if (com.jirbo.adcolony.a.T.startsWith("<")) {
                this.a.loadData(com.jirbo.adcolony.a.T, "text/html; charset=UTF-8", null);
            } else {
                this.a.loadUrl(com.jirbo.adcolony.a.T);
            }
        }
        String strA = ab.a(com.jirbo.adcolony.a.U, "");
        l.a.b((Object) "Injecting mraid");
        a(strA);
        a("var is_tablet=" + (com.jirbo.adcolony.a.m ? "true" : "false") + ";");
        String str = com.jirbo.adcolony.a.m ? "tablet" : "phone";
        a("adc_bridge.adc_version='" + com.jirbo.adcolony.a.W + "'");
        a("adc_bridge.os_version='" + com.jirbo.adcolony.a.V + "'");
        a("adc_bridge.os_name='android'");
        a("adc_bridge.device_type='" + str + "'");
        a("adc_bridge.fireChangeEvent({state:'default'});");
        a("adc_bridge.fireReadyEvent()");
        if (Build.VERSION.SDK_INT < 19) {
            if (!com.jirbo.adcolony.a.T.startsWith("<")) {
                this.a.loadUrl(com.jirbo.adcolony.a.T);
            } else {
                this.a.loadData(com.jirbo.adcolony.a.T, "text/html; charset=UTF-8", null);
            }
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer player) throws IOException {
        c();
    }

    public void c() throws IOException {
        d dVar = com.jirbo.adcolony.a.l;
        ADCVideo aDCVideo = this.d;
        dVar.a(ADCVideo.e, this.d.G);
        if (this.Q && this.D && com.jirbo.adcolony.a.R) {
            this.d.N.addView(this.f214c);
            new Handler().postDelayed(new Runnable() { // from class: com.jirbo.adcolony.ae.5
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    if (ae.this.D && ae.this.d != null && ae.this.Q && ae.this.a != null) {
                        ae.this.d.m = true;
                        ae.this.f();
                    }
                }
            }, com.jirbo.adcolony.a.S * 1000);
        }
        if (com.jirbo.adcolony.a.P) {
            f();
        }
        com.jirbo.adcolony.a.a("card_shown", this.d.G);
        synchronized (this.aI) {
            this.ad = null;
            if (com.jirbo.adcolony.a.J.i.u.e) {
                this.ad = new ab.b(com.jirbo.adcolony.a.J.i.u.g);
            }
        }
        if (this.Q) {
            Handler handler = new Handler();
            final View view = new View(this.d);
            Runnable runnable = new Runnable() { // from class: com.jirbo.adcolony.ae.6
                @Override // java.lang.Runnable
                public void run() {
                    ae.this.d.N.removeView(view);
                    ae.this.a(true);
                    ae.this.d.r = System.currentTimeMillis();
                }
            };
            view.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            this.d.N.addView(view);
            handler.postDelayed(runnable, 500L);
            this.d.O.setVisibility(0);
        }
        this.d.r = System.currentTimeMillis();
        e();
    }

    void d() {
        this.a.loadUrl(com.jirbo.adcolony.a.T);
        l.a.a("Loading - end card url = ").b((Object) com.jirbo.adcolony.a.T);
    }

    void a(String str) {
        if (!this.N && this.a != null) {
            if (Build.VERSION.SDK_INT < 19) {
                this.a.loadUrl("javascript:" + str);
            } else {
                this.a.evaluateJavascript(str, null);
            }
        }
    }

    void a(boolean z) {
        if (!this.N) {
            if (z) {
                a("adc_bridge.fireChangeEvent({viewable:true});");
            } else {
                a("adc_bridge.fireChangeEvent({viewable:false});");
            }
        }
    }

    void b(boolean z) {
        if (this.N) {
        }
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mp, int what, int extra) {
        c(true);
        return true;
    }

    void e() {
        new Handler().postDelayed(new Runnable() { // from class: com.jirbo.adcolony.ae.7
            @Override // java.lang.Runnable
            public void run() {
                if (ae.this.d.E != null) {
                    ae.this.d.E.setVisibility(8);
                }
            }
        }, 300L);
        if (this.d.J) {
            this.d.H.stop();
        }
        ADCVideo aDCVideo = this.d;
        ADCVideo.d = true;
        if (this.d.E != null) {
            this.d.E.a();
        }
        w.I = null;
        invalidate();
        this.I = false;
        invalidate();
    }

    void f() throws IOException {
        if (this.d != null) {
            if (!this.Q || (this.a != null && this.d.O != null && this.d.N != null)) {
                com.jirbo.adcolony.a.D = true;
                this.d.s = System.currentTimeMillis();
                this.d.q += (this.d.s - this.d.r) / 1000.0d;
                com.jirbo.adcolony.a.aa = true;
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 < com.jirbo.adcolony.a.ag.size()) {
                        if (com.jirbo.adcolony.a.ag.get(i2) != null) {
                            com.jirbo.adcolony.a.ag.get(i2).a();
                        }
                        i = i2 + 1;
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                        }
                    }
                }
                this.d.T.close();
                this.d.finish();
                this.ad = null;
                if (this.Q) {
                    this.d.N.removeView(this.d.O);
                    this.a.destroy();
                    this.a = null;
                }
                com.jirbo.adcolony.a.M.a(this.d.G);
                AdColonyBrowser.A = true;
            }
        }
    }

    void g() {
        c(false);
    }

    void c(boolean z) {
        com.jirbo.adcolony.a.D = true;
        if (com.jirbo.adcolony.a.J.b() && !z) {
            ADCVideo aDCVideo = this.d;
            ADCVideo.a = this.d.E.getCurrentPosition();
            w.I = new w(this.d, (AdColonyV4VCAd) com.jirbo.adcolony.a.J);
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < com.jirbo.adcolony.a.ag.size()) {
                if (com.jirbo.adcolony.a.ag.get(i2) != null) {
                    com.jirbo.adcolony.a.ag.get(i2).a();
                }
                i = i2 + 1;
            } else {
                this.d.finish();
                com.jirbo.adcolony.a.M.b(this.d.G);
                com.jirbo.adcolony.a.aa = true;
                AdColonyBrowser.A = true;
                return;
            }
        }
    }

    void h() throws Exception {
        com.jirbo.adcolony.a.a("replay", this.d.G);
        ADCVideo aDCVideo = this.d;
        ADCVideo.e = true;
        ADCVideo aDCVideo2 = this.d;
        ADCVideo.d = false;
        ADCVideo aDCVideo3 = this.d;
        ADCVideo.a = 0;
        this.S = false;
        final View view = new View(this.d);
        view.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.d.N.addView(view, new FrameLayout.LayoutParams(this.d.t, this.d.u, 17));
        new Handler().postDelayed(new Runnable() { // from class: com.jirbo.adcolony.ae.8
            @Override // java.lang.Runnable
            public void run() {
                if (ae.this.Q) {
                    ae.this.d.O.setVisibility(4);
                }
                ae.this.d.N.removeView(view);
            }
        }, 900L);
        this.d.E.start();
        if (this.d.J) {
            try {
                this.d.H = HapticContentSDKFactory.GetNewSDKInstance(0, this.d);
                this.d.H.openHaptics(this.d.I);
            } catch (Exception e) {
                this.d.J = false;
            }
            if (this.d.H == null) {
                this.d.J = false;
            }
            if (this.d.J) {
                this.d.H.play();
            }
        }
        com.jirbo.adcolony.a.l.a(this.d.G);
        this.d.E.requestFocus();
        this.d.E.setBackgroundColor(0);
        this.d.E.setVisibility(0);
        a(false);
    }

    int b(String str) {
        this.ag.getTextWidths(str, ay);
        float f = BitmapDescriptorFactory.HUE_RED;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            f += ay[i];
        }
        return (int) f;
    }

    void i() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.jirbo.adcolony.ae.9
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                Rect rect = new Rect();
                this.getWindowVisibleDisplayFrame(rect);
                if (ae.this.a != null) {
                    ae.this.b((this.getRootView().getHeight() - (rect.bottom - rect.top)) - ((ae.this.d.u - ae.this.a.getHeight()) / 2));
                }
                ae.this.j();
            }
        });
    }

    void j() {
        if (this.h >= 70 && !this.E) {
            this.E = true;
            b(true);
        } else if (this.E && this.h == 0) {
            this.E = false;
            b(false);
        }
    }

    void b(int i) {
        this.h = i;
        if (i < 0) {
            this.h = 0;
        }
    }

    class a extends View {
        Rect a;

        public a(Activity activity) {
            super(activity);
            this.a = new Rect();
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            canvas.drawARGB(MotionEventCompat.ACTION_MASK, 0, 0, 0);
            getDrawingRect(this.a);
            ae.this.as.a(canvas, (this.a.width() - ae.this.as.f) / 2, (this.a.height() - ae.this.as.g) / 2);
            invalidate();
        }
    }

    class b extends Handler {
        b() {
            a();
        }

        void a() {
            sendMessageDelayed(obtainMessage(), 500L);
        }

        @Override // android.os.Handler
        public void handleMessage(Message m) {
            a();
            if (!ae.this.d.isFinishing() && ae.this.d.E != null) {
                synchronized (this) {
                    if (ae.this.ad != null && ae.this.ad.a() && !ae.this.d.E.isPlaying()) {
                        ae.this.ad = null;
                        ae.this.t = 0;
                        if (ae.this.d.E != null) {
                            ae.this.d.E.a();
                        }
                        ae.this.d.n = true;
                        ae.this.f();
                    }
                }
            }
        }
    }
}
