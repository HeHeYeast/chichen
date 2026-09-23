package com.jirbo.adcolony;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.plus.PlusShare;
import com.jirbo.adcolony.ADCData;
import com.jirbo.adcolony.n;
import java.io.FileInputStream;
import java.io.IOException;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AdColonyNativeAdView extends FrameLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
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
    AdColonyInterstitialAd K;
    AdColonyNativeAdListener L;
    AdColonyNativeAdMutedListener M;
    ADCImage N;
    ADCImage O;
    ADCImage P;
    ImageView Q;
    b R;
    View S;
    Bitmap T;
    ADCImage U;
    ImageView V;
    boolean W;
    Button Z;
    TextView a;
    float aA;
    FileInputStream aB;
    String aa;
    String ab;
    String ac;
    MediaPlayer ad;
    Surface ae;
    String af;
    String ag;
    String ah;
    String ai;
    String aj;
    String ak;
    String al;
    String am;
    AdColonyIAPEngagement an;
    int ao;
    int ap;
    int aq;
    int ar;
    int as;
    int at;
    int au;
    int av;
    n.ab aw;
    n.a ax;
    float ay;
    float az;
    TextView b;

    /* renamed from: c, reason: collision with root package name */
    TextView f209c;
    Activity d;
    String e;
    String f;
    ViewGroup g;
    SurfaceTexture h;
    int i;
    int j;
    int k;
    int l;
    boolean m;
    boolean n;
    boolean o;
    boolean p;
    boolean q;
    boolean r;
    boolean s;
    boolean t;
    boolean u;
    boolean v;
    boolean w;
    boolean x;
    boolean y;
    boolean z;

    public AdColonyNativeAdView(Activity context, String zone_id, int width) {
        super(context);
        this.B = true;
        this.D = true;
        this.W = false;
        this.aa = "";
        this.ab = "";
        this.ac = "";
        this.am = "";
        this.an = AdColonyIAPEngagement.NONE;
        this.ar = -1;
        this.at = -3355444;
        this.au = FluctConstants.FRAME_ALPHA_COLOR;
        this.ay = 0.25f;
        this.az = 0.25f;
        a(context, zone_id, width);
        a();
    }

    public AdColonyNativeAdView(Activity context, String zone_id, int width, int height) {
        super(context);
        this.B = true;
        this.D = true;
        this.W = false;
        this.aa = "";
        this.ab = "";
        this.ac = "";
        this.am = "";
        this.an = AdColonyIAPEngagement.NONE;
        this.ar = -1;
        this.at = -3355444;
        this.au = FluctConstants.FRAME_ALPHA_COLOR;
        this.ay = 0.25f;
        this.az = 0.25f;
        a(context, zone_id, width, height);
        a(false);
    }

    AdColonyNativeAdView(Activity context, String zone_id, int width, boolean is_private) {
        super(context);
        this.B = true;
        this.D = true;
        this.W = false;
        this.aa = "";
        this.ab = "";
        this.ac = "";
        this.am = "";
        this.an = AdColonyIAPEngagement.NONE;
        this.ar = -1;
        this.at = -3355444;
        this.au = FluctConstants.FRAME_ALPHA_COLOR;
        this.ay = 0.25f;
        this.az = 0.25f;
        this.G = is_private;
        a(context, zone_id, width);
        a();
    }

    void a(Activity activity, String str, int i) {
        a(activity, str, i, 0);
    }

    void a(Activity activity, String str, int i, int i2) {
        int width;
        int height;
        com.jirbo.adcolony.a.e();
        if (!this.G) {
            com.jirbo.adcolony.a.ag.add(this);
        }
        com.jirbo.adcolony.a.ac = 0;
        this.d = activity;
        this.e = str;
        this.aq = i;
        this.k = i;
        if (i2 != 0) {
            this.l = i2;
            this.ar = i2;
            this.o = true;
        }
        this.r = true;
        this.aA = com.jirbo.adcolony.a.b().getResources().getDisplayMetrics().density;
        Display defaultDisplay = com.jirbo.adcolony.a.b().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 14) {
            Point point = new Point();
            defaultDisplay.getSize(point);
            width = point.x;
            height = point.y;
        } else {
            width = defaultDisplay.getWidth();
            height = defaultDisplay.getHeight();
        }
        if (width >= height) {
            width = height;
        }
        this.av = width;
        this.K = new AdColonyInterstitialAd(str);
        this.K.j = "native";
        this.K.k = "native";
        com.jirbo.adcolony.a.l.d.a(str, this.K);
        setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
    }

    void a() {
        a(true);
    }

    void a(boolean z) {
        this.z = false;
        this.q = false;
        setWillNotDraw(false);
        this.K.x = this;
        if (this.B) {
            if (com.jirbo.adcolony.a.l == null || com.jirbo.adcolony.a.l.a == null || this.K == null || this.K.g == null || !com.jirbo.adcolony.a.l.a(this.K.g, true, false)) {
                this.u = true;
            } else {
                com.jirbo.adcolony.a.l.a.b(this.e);
            }
            this.K.b(true);
            this.aw = this.K.h;
            this.f = com.jirbo.adcolony.a.j("video_filepath");
            this.af = com.jirbo.adcolony.a.j("advertiser_name");
            this.ag = com.jirbo.adcolony.a.j(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
            this.ah = com.jirbo.adcolony.a.j(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
            this.ai = com.jirbo.adcolony.a.j("poster_image");
            this.aj = com.jirbo.adcolony.a.j("unmute");
            this.ak = com.jirbo.adcolony.a.j("mute");
            this.al = com.jirbo.adcolony.a.j("thumb_image");
            this.W = com.jirbo.adcolony.a.i("native_engagement_enabled");
            this.aa = com.jirbo.adcolony.a.j("native_engagement_label");
            this.ab = com.jirbo.adcolony.a.j("native_engagement_command");
            this.ac = com.jirbo.adcolony.a.j("native_engagement_type");
            this.J = com.jirbo.adcolony.a.i("v4iap_enabled");
            if (this.J) {
                this.an = AdColonyIAPEngagement.AUTOMATIC;
            }
            this.am = com.jirbo.adcolony.a.j("product_id");
            if (this.K.i == null || this.K.i.w == null) {
                this.y = true;
            } else {
                this.y = this.K.i.w.b;
            }
            if (this.aw != null) {
                this.aw.k();
            }
            if (this.K.i == null || this.K.i.w == null || !this.K.i.w.a || this.K.h == null) {
                com.jirbo.adcolony.a.ac = 13;
                return;
            }
            this.s = true;
            if (!this.G) {
                this.B = false;
            } else {
                return;
            }
        } else if (Build.VERSION.SDK_INT < 14) {
            return;
        }
        this.ao = this.K.i.v.b;
        this.ap = this.K.i.v.f229c;
        if (this.ar == -1) {
            this.ar = (int) (this.ap * (this.aq / this.ao));
            this.l = this.ar;
        }
        float f = this.ao / this.ap;
        if (this.aq / this.ao > this.ar / this.ap) {
            this.aq = (int) (this.ar * f);
        } else {
            this.ar = (int) (this.aq / f);
        }
        if (this.W) {
            this.Z = new Button(com.jirbo.adcolony.a.b());
            this.Z.setText(this.aa);
            this.Z.setGravity(17);
            this.Z.setTextSize((int) (18.0d * (this.aq / this.av)));
            this.Z.setPadding(0, 0, 0, 0);
            this.Z.setBackgroundColor(this.at);
            this.Z.setTextColor(this.au);
            this.Z.setOnTouchListener(new View.OnTouchListener() { // from class: com.jirbo.adcolony.AdColonyNativeAdView.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == 0) {
                        float[] fArr = new float[3];
                        Color.colorToHSV(AdColonyNativeAdView.this.at, fArr);
                        fArr[2] = fArr[2] * 0.8f;
                        AdColonyNativeAdView.this.Z.setBackgroundColor(Color.HSVToColor(fArr));
                    } else if (action == 3) {
                        AdColonyNativeAdView.this.Z.setBackgroundColor(AdColonyNativeAdView.this.at);
                    } else if (action == 1) {
                        if (AdColonyNativeAdView.this.J) {
                            AdColonyNativeAdView.this.an = AdColonyIAPEngagement.OVERLAY;
                            AdColonyNativeAdView.this.u = true;
                        } else {
                            if (AdColonyNativeAdView.this.ac.equals("install") || AdColonyNativeAdView.this.ac.equals("url")) {
                                com.jirbo.adcolony.a.l.d.b("native_overlay_click", AdColonyNativeAdView.this.K);
                                try {
                                    com.jirbo.adcolony.a.b().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdColonyNativeAdView.this.ab)));
                                } catch (Exception e) {
                                    Toast.makeText(com.jirbo.adcolony.a.b(), "Unable to open store.", 0).show();
                                }
                            }
                            AdColonyNativeAdView.this.Z.setBackgroundColor(AdColonyNativeAdView.this.at);
                        }
                    }
                    return true;
                }
            });
        }
        this.N = new ADCImage(this.ai, true, false);
        this.N.a(1.0f / (((float) this.N.f) / ((float) this.k)) > 1.0f / (((float) this.N.g) / ((float) this.l)) ? 1.0f / (this.N.g / this.l) : 1.0f / (this.N.f / this.k), true);
        this.P = new ADCImage(this.aj, true, false);
        this.O = new ADCImage(this.ak, true, false);
        this.U = new ADCImage(this.al, true, false);
        this.U.a(1.0f / ((float) ((this.U.f / this.aq) / ((this.aq / 5.5d) / this.aq))), true);
        this.O.a(this.aA / 2.0f, true);
        this.P.a(this.aA / 2.0f, true);
        this.R = new b(com.jirbo.adcolony.a.b());
        this.V = new ImageView(com.jirbo.adcolony.a.b());
        this.Q = new ImageView(com.jirbo.adcolony.a.b());
        this.V.setImageBitmap(this.U.a);
        if (this.r) {
            this.Q.setImageBitmap(this.O.a);
        } else {
            this.Q.setImageBitmap(this.P.a);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.O.f, this.O.g, 48);
        layoutParams.setMargins(this.k - this.O.f, 0, 0, 0);
        this.Q.setOnClickListener(new View.OnClickListener() { // from class: com.jirbo.adcolony.AdColonyNativeAdView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (AdColonyNativeAdView.this.r) {
                    if (AdColonyNativeAdView.this.M != null) {
                        AdColonyNativeAdView.this.M.onAdColonyNativeAdMuted(AdColonyNativeAdView.this, true);
                    }
                    AdColonyNativeAdView.this.a(true, true);
                    AdColonyNativeAdView.this.x = true;
                    return;
                }
                if (AdColonyNativeAdView.this.T == AdColonyNativeAdView.this.P.a) {
                    if (AdColonyNativeAdView.this.M != null) {
                        AdColonyNativeAdView.this.M.onAdColonyNativeAdMuted(AdColonyNativeAdView.this, false);
                    }
                    AdColonyNativeAdView.this.x = false;
                    AdColonyNativeAdView.this.a(false, true);
                }
            }
        });
        this.T = this.O.a;
        if (this.u) {
            this.Q.setVisibility(8);
        }
        if (this.v) {
            this.Q.setVisibility(4);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            this.S = new a(com.jirbo.adcolony.a.b(), this.u);
        }
        int i = this.W ? 48 : 17;
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(this.aq, this.ar, i);
        if (i == 48) {
            layoutParams2.setMargins((this.k - this.aq) / 2, (this.l - this.ar) / 2, 0, 0);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            addView(this.S, layoutParams2);
        }
        if (Build.VERSION.SDK_INT < 14) {
            this.u = true;
        }
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(this.k, this.l, i);
        layoutParams3.setMargins((this.k - this.aq) / 2, (this.l - this.ar) / 2, 0, 0);
        addView(this.R, layoutParams3);
        if (this.y && Build.VERSION.SDK_INT >= 14 && this.D) {
            addView(this.Q, layoutParams);
        }
        if (this.W) {
            addView(this.Z, z ? new FrameLayout.LayoutParams(this.k, this.l / 5, 80) : new FrameLayout.LayoutParams(this.k, this.ar / 5, 80));
        }
    }

    public boolean isReady() {
        return this.K.a(true) && this.s && !this.F;
    }

    boolean b(boolean z) {
        return this.K.a(true) && AdColony.isZoneNative(this.e);
    }

    public int getNativeAdWidth() {
        return this.k;
    }

    public int getNativeAdHeight() {
        return this.W ? this.l + (this.l / 5) : this.l;
    }

    public void setOverlayButtonColor(int color) {
        if (this.W) {
            this.Z.setBackgroundColor(color);
        }
        this.at = color;
    }

    public void setOverlayButtonTextColor(int color) {
        if (this.W) {
            this.Z.setTextColor(color);
        }
        this.au = color;
    }

    public void setOverlayButtonTypeface(Typeface tf, int style) {
        if (this.W) {
            this.Z.setTypeface(tf, style);
        }
    }

    void a(boolean z, boolean z2) {
        if (z) {
            this.Q.setImageBitmap(this.P.a);
            this.r = false;
            a(BitmapDescriptorFactory.HUE_RED, z2);
            this.T = this.P.a;
            return;
        }
        if (!this.x && this.T == this.P.a) {
            this.Q.setImageBitmap(this.O.a);
            this.r = true;
            if (this.ad != null) {
                if (this.az != 0.0d) {
                    a(this.az, z2);
                } else {
                    a(0.25f, z2);
                }
            }
            this.T = this.O.a;
        }
    }

    public void setMuted(boolean mute) {
        a(mute, false);
    }

    public void destroy() {
        l.f226c.b((Object) "[ADC] Native Ad Destroy called.");
        if (this.ae != null) {
            this.ae.release();
        }
        if (this.ad != null) {
            this.ad.release();
        }
        this.ad = null;
        com.jirbo.adcolony.a.ag.remove(this);
    }

    public ImageView getAdvertiserImage() {
        if (this.U == null) {
            this.U = new ADCImage(this.al, true, false);
            this.U.a(this.aA / 2.0f, true);
        }
        if (this.V == null) {
            this.V = new ImageView(com.jirbo.adcolony.a.b());
            this.V.setImageBitmap(this.U.a);
        }
        return this.V;
    }

    public String getTitle() {
        return this.ah;
    }

    public String getAdvertiserName() {
        return this.af;
    }

    public String getDescription() {
        return this.ag;
    }

    public boolean canceled() {
        return this.I;
    }

    public boolean iapEnabled() {
        return this.J;
    }

    public String iapProductID() {
        return this.am;
    }

    public AdColonyIAPEngagement iapEngagementType() {
        return (this.K == null || this.K.u != AdColonyIAPEngagement.END_CARD) ? this.an : AdColonyIAPEngagement.END_CARD;
    }

    public AdColonyNativeAdView withListener(AdColonyNativeAdListener listener) {
        this.L = listener;
        this.K.w = listener;
        return this;
    }

    public AdColonyNativeAdView withMutedListener(AdColonyNativeAdMutedListener mute_listener) {
        this.M = mute_listener;
        return this;
    }

    public void pause() throws IllegalStateException {
        l.f226c.b((Object) "[ADC] Native Ad Pause called.");
        if (this.ad != null && !this.u && this.ad.isPlaying() && Build.VERSION.SDK_INT >= 14) {
            com.jirbo.adcolony.a.l.d.b("video_paused", this.K);
            this.v = true;
            this.ad.pause();
            this.R.setVisibility(0);
            this.Q.setVisibility(4);
        }
    }

    public void resume() throws IllegalStateException {
        l.f226c.b((Object) "[ADC] Native Ad Resume called.");
        if (this.ad != null && this.v && !this.u && Build.VERSION.SDK_INT >= 14) {
            com.jirbo.adcolony.a.l.d.b("video_resumed", this.K);
            this.v = false;
            this.ad.seekTo(this.K.p);
            this.ad.start();
            this.R.setVisibility(4);
            this.Q.setVisibility(0);
        }
    }

    void c(boolean z) {
        if (this.ad != null && this.Q != null) {
            if (z) {
                this.ad.setVolume(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                this.Q.setImageBitmap(this.P.a);
                this.T = this.P.a;
            } else {
                this.ad.setVolume(this.az, this.az);
                this.Q.setImageBitmap(this.O.a);
                this.T = this.O.a;
            }
        }
    }

    void a(float f, boolean z) {
        if (Build.VERSION.SDK_INT >= 14) {
            this.az = f;
            if (this.ad == null || f < 0.0d || f > 1.0d) {
                if (f >= 0.0d && f <= 1.0d) {
                    this.ay = f;
                    return;
                }
                return;
            }
            if (!this.x) {
                this.ad.setVolume(f, f);
            }
            if (this.z) {
                if (this.T == this.P.a && f > 0.0d && !this.x) {
                    ADCData.g gVar = new ADCData.g();
                    gVar.b("user_action", z);
                    this.Q.setImageBitmap(this.O.a);
                    this.T = this.O.a;
                    com.jirbo.adcolony.a.l.d.a("sound_unmute", gVar, this.K);
                    this.r = true;
                    return;
                }
                if (this.T == this.O.a && f == 0.0d) {
                    ADCData.g gVar2 = new ADCData.g();
                    gVar2.b("user_action", z);
                    this.Q.setImageBitmap(this.P.a);
                    this.T = this.P.a;
                    com.jirbo.adcolony.a.l.d.a("sound_mute", gVar2, this.K);
                    this.r = false;
                }
            }
        }
    }

    public void setVolume(float v) {
        a(v, false);
    }

    synchronized void b() {
        if ((this.u || this.ad == null || !this.ad.isPlaying()) && this.ad != null) {
            setVolume(this.az);
            this.ad.start();
            com.jirbo.adcolony.a.l.a((AdColonyAd) this.K);
            this.K.q = true;
            if (this.L != null) {
                this.L.onAdColonyNativeAdStarted(false, this);
            }
        }
    }

    void c() throws IllegalStateException {
        if (!this.u && this.ad != null && this.ad.isPlaying() && !this.v) {
            com.jirbo.adcolony.a.l.d.b("video_paused", this.K);
            this.ad.pause();
        }
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer player) {
        l.f226c.b((Object) "[ADC] Native Ad onPrepared called.");
        this.z = true;
        if (this.T == null || this.O.a == null) {
            this.R.setVisibility(0);
            this.Q.setVisibility(8);
            this.u = true;
            this.ad = null;
            this.K.p = 0;
            return;
        }
        if (!this.r && this.T.equals(this.O.a)) {
            c(true);
        } else {
            setVolume(this.az);
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer player) throws IOException {
        try {
            this.aB.close();
        } catch (Exception e) {
        }
        this.R.setVisibility(0);
        this.Q.setVisibility(8);
        this.K.j = "native";
        this.K.k = "native";
        this.K.q = true;
        this.u = true;
        if (this.ad != null) {
            this.ad.release();
        }
        this.ad = null;
        this.K.p = 0;
        ADCData.g gVar = new ADCData.g();
        gVar.b("ad_slot", this.K.h.k.d);
        gVar.b("replay", false);
        com.jirbo.adcolony.a.l.d.a("native_complete", gVar, this.K);
        if (this.L != null) {
            this.L.onAdColonyNativeAdFinished(false, this);
        }
        this.C = true;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer player, int what, int extra) {
        this.R.setVisibility(0);
        this.Q.setVisibility(8);
        this.u = true;
        this.z = true;
        this.ad = null;
        this.K.p = 0;
        return true;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) throws IllegalStateException {
        if (this.g != null) {
            Rect rect = new Rect();
            if (!this.g.hasFocus()) {
                this.g.requestFocus();
            }
            if (!this.u && this.ad != null) {
                this.as = this.ad.getCurrentPosition();
            }
            if (this.as != 0) {
                this.K.p = this.as;
            }
            getLocalVisibleRect(rect);
            boolean z = rect.bottom - rect.top > getNativeAdHeight() / 2;
            if ((!z && !this.n) || (this.n && (!z || (rect.bottom - rect.top < getNativeAdHeight() && rect.top != 0)))) {
                if (!this.u && this.ad != null && this.ad.isPlaying() && !this.v) {
                    l.f226c.b((Object) "[ADC] Scroll Pause");
                    com.jirbo.adcolony.a.l.d.b("video_paused", this.K);
                    this.ad.pause();
                    this.R.setVisibility(0);
                }
            } else if (!this.u && this.ad != null && this.ad.isPlaying()) {
                if (!this.z) {
                    canvas.drawARGB(MotionEventCompat.ACTION_MASK, 0, 0, 0);
                } else {
                    this.K.j = "native";
                    this.K.k = "native";
                    com.jirbo.adcolony.a.l.a(this.ad.getCurrentPosition() / this.ad.getDuration(), this.K);
                    if (!this.H && this.ad.getCurrentPosition() > 2000) {
                        this.H = true;
                        com.jirbo.adcolony.a.l.a("native_start", "{\"ad_slot\":" + this.K.h.k.d + ", \"replay\":false}", this.K);
                    }
                }
            } else if (!this.R.a) {
                canvas.drawARGB(MotionEventCompat.ACTION_MASK, 0, 0, 0);
            }
            if (this.A || this.u) {
                return;
            }
            invalidate();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 14) {
            return false;
        }
        if (event.getAction() == 1 && com.jirbo.adcolony.a.v && q.c()) {
            com.jirbo.adcolony.a.J = this.K;
            com.jirbo.adcolony.a.l.a.a(this.e, this.K.i);
            ADCVideo.a();
            this.K.s = this.C;
            this.K.r = true;
            this.K.j = "native";
            this.K.k = "fullscreen";
            com.jirbo.adcolony.a.v = false;
            com.jirbo.adcolony.a.l.d.b("video_expanded", this.K);
            if (this.L != null) {
                this.L.onAdColonyNativeAdStarted(true, this);
            }
            if (com.jirbo.adcolony.a.m) {
                l.a.b((Object) "Launching AdColonyOverlay");
                com.jirbo.adcolony.a.b().startActivity(new Intent(com.jirbo.adcolony.a.b(), (Class<?>) AdColonyOverlay.class));
            } else {
                l.a.b((Object) "Launching AdColonyFullscreen");
                com.jirbo.adcolony.a.b().startActivity(new Intent(com.jirbo.adcolony.a.b(), (Class<?>) AdColonyFullscreen.class));
            }
            if (this.u) {
                this.K.f = -1;
                this.K.h.k.d++;
                com.jirbo.adcolony.a.l.a("start", "{\"ad_slot\":" + this.K.h.k.d + ", \"replay\":" + this.K.s + "}", this.K);
                com.jirbo.adcolony.a.l.h.a(this.K.g, this.K.i.d);
            }
            this.u = true;
            this.C = true;
        }
        return true;
    }

    public void notifyAddedToListView() throws IllegalStateException, IOException, IllegalArgumentException {
        if (!this.m) {
            this.m = true;
        } else {
            ((a) this.S).onSurfaceTextureAvailable(this.h, this.i, this.j);
        }
    }

    public void prepareForListView() {
        this.n = true;
    }

    class a extends TextureView implements TextureView.SurfaceTextureListener {
        boolean a;
        boolean b;

        a(AdColonyNativeAdView adColonyNativeAdView, Context context) {
            this(context, false);
        }

        a(Context context, boolean z) {
            super(context);
            this.a = false;
            this.b = false;
            setSurfaceTextureListener(this);
            setWillNotDraw(false);
            this.a = z;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int w, int h) throws IllegalStateException, IOException, IllegalArgumentException {
            if (texture == null) {
                AdColonyNativeAdView.this.u = true;
                AdColonyNativeAdView.this.Q.setVisibility(8);
                return;
            }
            AdColonyNativeAdView.this.R.setVisibility(0);
            AdColonyNativeAdView.this.h = texture;
            if (!AdColonyNativeAdView.this.u && !this.a) {
                AdColonyNativeAdView.this.ae = new Surface(texture);
                if (AdColonyNativeAdView.this.ad != null) {
                    AdColonyNativeAdView.this.ad.release();
                }
                AdColonyNativeAdView.this.i = w;
                AdColonyNativeAdView.this.j = h;
                AdColonyNativeAdView.this.ad = new MediaPlayer();
                try {
                    AdColonyNativeAdView.this.aB = new FileInputStream(AdColonyNativeAdView.this.f);
                    AdColonyNativeAdView.this.ad.setDataSource(AdColonyNativeAdView.this.aB.getFD());
                    AdColonyNativeAdView.this.ad.setSurface(AdColonyNativeAdView.this.ae);
                    AdColonyNativeAdView.this.ad.setOnCompletionListener(AdColonyNativeAdView.this);
                    AdColonyNativeAdView.this.ad.setOnPreparedListener(AdColonyNativeAdView.this);
                    AdColonyNativeAdView.this.ad.setOnErrorListener(AdColonyNativeAdView.this);
                    AdColonyNativeAdView.this.ad.prepareAsync();
                    l.f226c.b((Object) "[ADC] Native Ad Prepare called.");
                    this.b = true;
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() { // from class: com.jirbo.adcolony.AdColonyNativeAdView.a.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!AdColonyNativeAdView.this.z && !AdColonyNativeAdView.this.A) {
                                a.this.b = false;
                                AdColonyNativeAdView.this.u = true;
                                AdColonyNativeAdView.this.Q.setVisibility(8);
                            }
                        }
                    };
                    if (!this.b) {
                        handler.postDelayed(runnable, 1800L);
                    }
                } catch (Exception e) {
                    AdColonyNativeAdView.this.u = true;
                    AdColonyNativeAdView.this.Q.setVisibility(8);
                }
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int w, int h) {
            l.f226c.b((Object) "[ADC] onSurfaceTextureSizeChanged");
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            l.f226c.b((Object) "[ADC] Native surface destroyed");
            AdColonyNativeAdView.this.z = false;
            AdColonyNativeAdView.this.Q.setVisibility(4);
            AdColonyNativeAdView.this.R.setVisibility(0);
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent event) throws IllegalStateException {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            if (action == 1 && com.jirbo.adcolony.a.v && q.c() && (x <= (AdColonyNativeAdView.this.aq - AdColonyNativeAdView.this.O.f) + 8 || y >= AdColonyNativeAdView.this.O.g + 8 || AdColonyNativeAdView.this.u || AdColonyNativeAdView.this.ad == null || !AdColonyNativeAdView.this.ad.isPlaying())) {
                com.jirbo.adcolony.a.J = AdColonyNativeAdView.this.K;
                com.jirbo.adcolony.a.l.a.a(AdColonyNativeAdView.this.e, AdColonyNativeAdView.this.K.i);
                ADCVideo.a();
                AdColonyNativeAdView.this.K.j = "native";
                AdColonyNativeAdView.this.K.k = "fullscreen";
                AdColonyNativeAdView.this.K.r = true;
                AdColonyNativeAdView.this.K.s = AdColonyNativeAdView.this.C;
                if ((AdColonyNativeAdView.this.z || AdColonyNativeAdView.this.u) && q.c()) {
                    if (AdColonyNativeAdView.this.L != null) {
                        AdColonyNativeAdView.this.L.onAdColonyNativeAdStarted(true, AdColonyNativeAdView.this);
                    }
                    if (AdColonyNativeAdView.this.ad != null && AdColonyNativeAdView.this.ad.isPlaying()) {
                        ADCVideo.f203c = AdColonyNativeAdView.this.ad.getCurrentPosition();
                        AdColonyNativeAdView.this.K.o = AdColonyNativeAdView.this.K.n;
                        AdColonyNativeAdView.this.ad.pause();
                        AdColonyNativeAdView.this.u = true;
                    } else {
                        AdColonyNativeAdView.this.K.o = 0.0d;
                        ADCVideo.f203c = 0;
                    }
                    com.jirbo.adcolony.a.v = false;
                    com.jirbo.adcolony.a.l.d.b("video_expanded", AdColonyNativeAdView.this.K);
                    if (com.jirbo.adcolony.a.m) {
                        l.a.b((Object) "Launching AdColonyOverlay");
                        com.jirbo.adcolony.a.b().startActivity(new Intent(com.jirbo.adcolony.a.b(), (Class<?>) AdColonyOverlay.class));
                    } else {
                        l.a.b((Object) "Launching AdColonyFullscreen");
                        com.jirbo.adcolony.a.b().startActivity(new Intent(com.jirbo.adcolony.a.b(), (Class<?>) AdColonyFullscreen.class));
                    }
                    if (AdColonyNativeAdView.this.u) {
                        AdColonyNativeAdView.this.K.h.k.d++;
                        com.jirbo.adcolony.a.l.a("start", "{\"ad_slot\":" + AdColonyNativeAdView.this.K.h.k.d + ", \"replay\":" + AdColonyNativeAdView.this.K.s + "}", AdColonyNativeAdView.this.K);
                        com.jirbo.adcolony.a.l.h.a(AdColonyNativeAdView.this.K.g, AdColonyNativeAdView.this.K.i.d);
                    }
                    AdColonyNativeAdView.this.C = true;
                }
            }
            return true;
        }
    }

    class b extends View {
        boolean a;

        public b(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) throws IllegalStateException {
            AdColonyNativeAdView.this.g = (ViewGroup) getParent().getParent();
            Rect rect = new Rect();
            if (AdColonyNativeAdView.this.ad != null && !AdColonyNativeAdView.this.ad.isPlaying() && AdColonyNativeAdView.this.n) {
                this.a = false;
            }
            if (getLocalVisibleRect(rect) && Build.VERSION.SDK_INT >= 14 && AdColonyNativeAdView.this.z) {
                if ((!AdColonyNativeAdView.this.n || (AdColonyNativeAdView.this.n && (rect.top == 0 || rect.bottom - rect.top > AdColonyNativeAdView.this.getNativeAdHeight()))) && rect.bottom - rect.top > AdColonyNativeAdView.this.getNativeAdHeight() / 2) {
                    if (this.a || AdColonyNativeAdView.this.u || AdColonyNativeAdView.this.ad == null || AdColonyNativeAdView.this.ad.isPlaying() || AdColonyNativeAdView.this.A || AdColonyNativeAdView.this.K.a(true) || !AdColonyNativeAdView.this.t) {
                    }
                    if (!AdColonyNativeAdView.this.t) {
                        l.f226c.b((Object) "[ADC] Native Ad Starting");
                        AdColonyNativeAdView.this.b();
                        AdColonyNativeAdView.this.t = true;
                        AdColonyNativeAdView.this.K.j = "native";
                        AdColonyNativeAdView.this.K.k = "native";
                    } else if (!AdColonyNativeAdView.this.v && AdColonyNativeAdView.this.ad != null && q.c() && !AdColonyNativeAdView.this.ad.isPlaying() && !com.jirbo.adcolony.a.t) {
                        l.f226c.b((Object) "[ADC] Native Ad Resuming");
                        com.jirbo.adcolony.a.l.d.b("video_resumed", AdColonyNativeAdView.this.K);
                        if (!AdColonyNativeAdView.this.r) {
                            AdColonyNativeAdView.this.c(true);
                        }
                        AdColonyNativeAdView.this.setVolume(AdColonyNativeAdView.this.az);
                        AdColonyNativeAdView.this.ad.seekTo(AdColonyNativeAdView.this.K.p);
                        AdColonyNativeAdView.this.ad.start();
                    } else if (!AdColonyNativeAdView.this.u && !AdColonyNativeAdView.this.t && !com.jirbo.adcolony.a.l.a(AdColonyNativeAdView.this.K.g, true, false)) {
                        AdColonyNativeAdView.this.u = true;
                        setVisibility(0);
                        AdColonyNativeAdView.this.Q.setVisibility(8);
                    }
                }
                this.a = true;
            } else {
                this.a = false;
            }
            if (!AdColonyNativeAdView.this.u && !q.c() && AdColonyNativeAdView.this.ad != null && !AdColonyNativeAdView.this.ad.isPlaying()) {
                setVisibility(0);
                AdColonyNativeAdView.this.Q.setVisibility(8);
                AdColonyNativeAdView.this.u = true;
            }
            if (!AdColonyNativeAdView.this.u && AdColonyNativeAdView.this.ad != null && AdColonyNativeAdView.this.ad.isPlaying()) {
                setVisibility(8);
                AdColonyNativeAdView.this.Q.setVisibility(0);
            } else if (AdColonyNativeAdView.this.u || AdColonyNativeAdView.this.v) {
                canvas.drawARGB(MotionEventCompat.ACTION_MASK, 0, 0, 0);
                AdColonyNativeAdView.this.Q.setVisibility(8);
                AdColonyNativeAdView.this.N.a(canvas, (AdColonyNativeAdView.this.aq - AdColonyNativeAdView.this.N.f) / 2, (AdColonyNativeAdView.this.ar - AdColonyNativeAdView.this.N.g) / 2);
            }
            if (AdColonyNativeAdView.this.A || AdColonyNativeAdView.this.u) {
                return;
            }
            invalidate();
        }
    }
}
