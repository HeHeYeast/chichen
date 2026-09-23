package com.jirbo.adcolony;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import com.immersion.hapticmediasdk.HapticContentSDK;
import com.immersion.hapticmediasdk.HapticContentSDKFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public abstract class ADCVideo extends Activity {
    static int a;
    static int b;

    /* renamed from: c, reason: collision with root package name */
    static int f203c;
    static boolean d;
    static boolean e;
    static boolean f;
    static boolean g;
    static Activity h;
    static boolean i;
    static boolean j;
    e E;
    ae F;
    AdColonyAd G;
    HapticContentSDK H;
    String I;
    boolean J;
    VideoView M;
    FrameLayout N;
    FrameLayout O;
    FrameLayout P;
    ADCImage R;
    a S;
    FileInputStream T;
    boolean k;
    boolean l;
    boolean m;
    boolean n;
    boolean o;
    double p;
    double q;
    long r;
    long s;
    int t;
    int u;
    int v;
    int w;
    int x;
    int y;
    int z;
    String A = "";
    String B = "";
    boolean C = true;
    boolean D = true;
    boolean K = true;
    String L = "Your purchase will begin shortly!";
    Rect Q = new Rect();

    static void a() {
        a = 0;
        d = false;
        e = false;
        g = false;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        int i2 = 1;
        com.jirbo.adcolony.a.aa = false;
        super.onCreate(savedInstanceState);
        this.J = com.jirbo.adcolony.a.i("haptics_enabled");
        this.I = com.jirbo.adcolony.a.j("haptics_filepath");
        this.L = com.jirbo.adcolony.a.j("in_progress");
        this.B = com.jirbo.adcolony.a.j("video_filepath");
        if (this.J) {
            try {
                this.H = HapticContentSDKFactory.GetNewSDKInstance(0, this);
                this.H.openHaptics(this.I);
            } catch (Exception e2) {
                e2.printStackTrace();
                this.J = false;
            }
            if (this.H == null) {
                this.J = false;
            }
        }
        h = this;
        com.jirbo.adcolony.a.Q = !com.jirbo.adcolony.a.i("video_enabled");
        com.jirbo.adcolony.a.P = !com.jirbo.adcolony.a.i("end_card_enabled");
        com.jirbo.adcolony.a.R = com.jirbo.adcolony.a.i("load_timeout_enabled");
        com.jirbo.adcolony.a.S = com.jirbo.adcolony.a.h("load_timeout");
        for (int i3 = 0; i3 < com.jirbo.adcolony.a.ag.size(); i3++) {
            if (com.jirbo.adcolony.a.ag.get(i3) != null) {
                AdColonyNativeAdView adColonyNativeAdView = com.jirbo.adcolony.a.ag.get(i3);
                if (adColonyNativeAdView.ad != null) {
                    adColonyNativeAdView.S.setVisibility(4);
                }
                if (adColonyNativeAdView.Q != null) {
                    adColonyNativeAdView.Q.setVisibility(4);
                }
            }
        }
        this.G = com.jirbo.adcolony.a.J;
        if (this.G == null) {
            finish();
            return;
        }
        if (com.jirbo.adcolony.a.i("v4iap_enabled")) {
            this.G.u = AdColonyIAPEngagement.AUTOMATIC;
            this.G.t = true;
            this.G.m = com.jirbo.adcolony.a.j("product_id");
        }
        e = this.G.s;
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (!com.jirbo.adcolony.a.m) {
            if (Build.VERSION.SDK_INT >= 10) {
                setRequestedOrientation(6);
            } else {
                setRequestedOrientation(0);
            }
        } else {
            int i4 = getResources().getConfiguration().orientation;
            com.jirbo.adcolony.a.w = (i4 == 0 || i4 == 6 || i4 == 2) ? 6 : 7;
            if (Build.VERSION.SDK_INT >= 10 && !Build.MODEL.equals("Kindle Fire")) {
                setRequestedOrientation(com.jirbo.adcolony.a.w);
            } else {
                if (Build.MODEL.equals("Kindle Fire")) {
                    getRequestedOrientation();
                    switch (((WindowManager) getSystemService("window")).getDefaultDisplay().getRotation()) {
                        case 0:
                            break;
                        case 1:
                            i2 = 0;
                            break;
                        case 2:
                            i2 = 9;
                            break;
                        default:
                            i2 = 8;
                            break;
                    }
                } else {
                    i2 = i4;
                }
                com.jirbo.adcolony.a.w = i2;
                setRequestedOrientation(i2);
            }
        }
        setVolumeControlStream(3);
        this.E = new e(this);
        this.N = new FrameLayout(this);
        this.F = new ae(this);
        this.P = new FrameLayout(this);
        this.S = new a(this);
        this.R = new ADCImage(com.jirbo.adcolony.a.j("browser_icon"));
        AdColonyBrowser.A = false;
        com.jirbo.adcolony.a.K = this;
        com.jirbo.adcolony.a.L = this;
    }

    @Override // android.app.Activity
    public void onResume() throws Exception {
        i = true;
        super.onResume();
        if (com.jirbo.adcolony.a.a()) {
            finish();
        }
        b();
        if (this.C) {
            this.C = false;
            if (!d) {
                if (this.F.Q) {
                    this.O.addView(this.F.a);
                }
                if (this.F.Q) {
                    this.O.setVisibility(4);
                }
                if (Build.MODEL.equals("Kindle Fire")) {
                    this.F.m = 20;
                }
                if (Build.MODEL.equals("SCH-I800")) {
                    this.F.m = 25;
                }
                this.N.addView(this.E, new FrameLayout.LayoutParams(this.x, this.y, 17));
                if (this.F.Q) {
                    this.N.addView(this.O, new FrameLayout.LayoutParams(this.t, this.u - this.F.m, 17));
                }
                this.N.addView(this.F, new FrameLayout.LayoutParams(this.t, this.u, 17));
            }
        }
        if (g) {
            this.P.removeView(this.S);
            this.P.addView(this.S);
            setContentView(this.P);
        } else {
            setContentView(this.N);
            if (d) {
                this.r = System.currentTimeMillis();
            }
        }
        this.E.a((MediaPlayer.OnCompletionListener) this.F);
        this.E.a((MediaPlayer.OnErrorListener) this.F);
        try {
            this.T = new FileInputStream(this.B);
            this.E.a(this.T.getFD());
            if (!j) {
                onWindowFocusChanged(true);
            }
            if (com.jirbo.adcolony.a.Q) {
                this.F.a();
                this.F.c();
            }
        } catch (IOException e2) {
            com.jirbo.adcolony.a.e("Unable to play video: " + com.jirbo.adcolony.a.j("video_filepath"));
            finish();
        }
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (!com.jirbo.adcolony.a.aa) {
        }
        com.jirbo.adcolony.a.v = true;
        com.jirbo.adcolony.a.K = null;
        com.jirbo.adcolony.a.aa = true;
    }

    boolean b() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.t = displayMetrics.widthPixels;
        this.u = displayMetrics.heightPixels;
        this.t = displayMetrics.widthPixels;
        this.u = displayMetrics.heightPixels;
        this.z = FluctConstants.FRAME_ALPHA_COLOR;
        getWindow().setBackgroundDrawable(new ColorDrawable(this.z));
        int i2 = this.t;
        int i3 = this.u;
        this.x = i2;
        this.y = i3;
        if (!com.jirbo.adcolony.a.m && this.x < this.y) {
            this.t = i3;
            this.u = i2;
            this.x = i3;
            this.y = i2;
        }
        if (!com.jirbo.adcolony.a.B) {
            return false;
        }
        com.jirbo.adcolony.a.B = false;
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean has_focus) throws Exception {
        if (has_focus) {
            j = false;
            if (!d && i) {
                if (this.E != null) {
                    if (f203c != 0) {
                        a = f203c;
                    }
                    f203c = 0;
                    this.E.seekTo(a);
                    if (com.jirbo.adcolony.a.m) {
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() { // from class: com.jirbo.adcolony.ADCVideo.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ADCVideo.this.E.setBackgroundColor(0);
                            }
                        };
                        this.E.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
                        handler.postDelayed(runnable, 900L);
                    } else {
                        this.E.setBackgroundColor(0);
                    }
                    if (!w.H) {
                        this.F.S = false;
                        this.E.start();
                        if (this.J) {
                            if (this.K) {
                                this.H.play();
                            } else {
                                this.H.resume();
                            }
                            this.K = false;
                        }
                    }
                    this.F.requestFocus();
                    this.F.invalidate();
                    return;
                }
                return;
            }
            if (g) {
                if (this.M != null) {
                    this.M.seekTo(b);
                    this.M.start();
                    return;
                } else {
                    if (this.P != null) {
                        this.P.removeAllViews();
                    }
                    setContentView(this.N);
                    return;
                }
            }
            if (d) {
                this.F.invalidate();
                return;
            }
            return;
        }
        if (i) {
            if (this.J) {
                this.H.pause();
            }
            a = this.E.getCurrentPosition();
            this.E.pause();
        }
        j = true;
    }

    @Override // android.app.Activity
    public void onPause() throws Exception {
        i = false;
        if (g) {
            if (this.M != null) {
                b = this.M.getCurrentPosition();
                this.M.stopPlayback();
            }
        } else {
            b = 0;
        }
        if (d) {
            View view = new View(this);
            view.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            setContentView(view);
            this.s = System.currentTimeMillis();
            if (!isFinishing()) {
                this.q += (this.s - this.r) / 1000.0d;
            }
        }
        if (this.E != null) {
            if (this.E.getCurrentPosition() != 0) {
                a = this.E.getCurrentPosition();
            }
            this.E.a();
            this.E.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            if (this.J) {
                this.H.pause();
            }
        } else {
            a = 0;
        }
        this.F.A = true;
        this.F.I = false;
        this.F.H = false;
        this.F.J = false;
        this.F.u = 0;
        this.F.t = 0;
        this.F.invalidate();
        super.onPause();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keycode, KeyEvent event) throws IOException {
        if (w.I != null && w.I.onKeyDown(keycode, event)) {
            return true;
        }
        if (keycode == 4) {
            if (d) {
                if (g) {
                    this.M.stopPlayback();
                    g = false;
                    this.P.removeAllViews();
                    setContentView(this.N);
                } else if (this.F != null && this.F.t == 0) {
                    com.jirbo.adcolony.a.aa = true;
                    this.F.f();
                }
            } else if (this.F != null && w.I != null) {
                Iterator<ADCImage> it = w.I.o.iterator();
                while (it.hasNext()) {
                    it.next().a();
                }
                w.I = null;
                w.H = false;
                this.E.start();
            } else if (this.F != null && this.F.M && this.F.O) {
                com.jirbo.adcolony.a.aa = true;
                this.F.g();
            }
            return true;
        }
        if (keycode == 82) {
            return true;
        }
        return super.onKeyUp(keycode, event);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == 4) {
            return true;
        }
        return super.onKeyDown(keycode, event);
    }

    void a(String str) {
        this.A = str;
        g = true;
        this.M = new VideoView(this);
        this.M.setVideoURI(Uri.parse(str));
        new MediaController(this).setMediaPlayer(this.M);
        this.M.setLayoutParams(new FrameLayout.LayoutParams(this.t, this.u, 17));
        this.P.addView(this.M);
        this.P.addView(this.S);
        setContentView(this.P);
        this.M.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.jirbo.adcolony.ADCVideo.2
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer media_player) {
                ADCVideo.this.setContentView(ADCVideo.this.N);
                ADCVideo.this.P.removeAllViews();
                ADCVideo.g = false;
            }
        });
        this.M.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.jirbo.adcolony.ADCVideo.3
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer media_player) {
                ADCVideo.this.P.removeViewAt(1);
            }
        });
        this.M.start();
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
            ADCVideo.this.R.a(canvas, (this.a.width() - ADCVideo.this.R.f) / 2, (this.a.height() - ADCVideo.this.R.g) / 2);
            invalidate();
        }
    }
}
