package vpadn;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.vpadn.widget.VpadnActivity;
import com.vpon.video.ActionButton;
import com.vpon.video.ChangeSoundActionButton;
import com.vpon.video.FuncButton;
import com.vpon.video.PlayPauseVideoActionButton;
import com.vpon.video.VponVideoView;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import vpadn.C0108v;
import vpadn.ar;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class as {
    private static HashSet<String> ae;
    private RelativeLayout A;
    private RelativeLayout B;
    private TextView C;
    private ActionButton D;
    private ActionButton E;
    private ActionButton F;
    private ActionButton G;
    private ActionButton H;
    private ActionButton I;
    private int J;
    private ProgressBar K;
    private int L;
    private at P;
    private Timer T;
    private Timer U;
    private Timer V;
    private Timer W;
    private Timer X;
    private au af;
    int b;

    /* renamed from: c, reason: collision with root package name */
    int f332c;
    private VpadnActivity d;
    private VponVideoView e;
    private ar f;
    private j n;
    private RelativeLayout o;
    private RelativeLayout p;
    private RelativeLayout q;
    private LinearLayout r;
    private ImageView s;
    private Drawable t;
    private TextView u;
    private View v;
    private RelativeLayout w;
    private FuncButton x;
    private TextView y;
    private FuncButton z;
    private MediaPlayer g = null;
    private boolean h = true;
    private boolean i = true;
    private boolean j = false;
    private boolean k = false;
    private boolean l = false;
    private boolean m = false;
    boolean a = false;
    private boolean M = true;
    private int N = 16;
    private int O = 9;
    private Map<String, Map<Integer, C0101o>> Q = Collections.synchronizedMap(new HashMap());
    private boolean R = false;
    private Timer S = new Timer();
    private JSONObject Y = new JSONObject();
    private JSONObject Z = new JSONObject();
    private JSONObject aa = new JSONObject();
    private JSONObject ab = new JSONObject();
    private JSONObject ac = new JSONObject();
    private boolean ad = false;

    static {
        HashSet<String> hashSet = new HashSet<>();
        ae = hashSet;
        hashSet.add("fb");
        ae.add("map");
        ae.add("ren");
        ae.add("wei");
        ae.add("twi");
        ae.add("open_url");
        ae.add("open_store");
        ae.add("other");
        ae.add("place_call");
        ae.add("send_sms");
        ae.add("lin");
        ae.add("cre_cal_event");
    }

    public static boolean a(String str) {
        return ae.contains(str);
    }

    class a extends TimerTask {
        a() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            try {
                if (as.this.e != null && !as.this.k) {
                    int currentPosition = as.this.e.getCurrentPosition();
                    if (currentPosition <= 0 || as.this.L != 0) {
                        if (as.this.L > 0 && currentPosition > as.this.L) {
                            as.d(as.this);
                            as.this.S.cancel();
                            as.this.S.purge();
                            as.this.S = null;
                            as.this.d.runOnUiThread(as.this.new c());
                            as.a(as.this, false);
                        }
                    } else {
                        as.d(as.this);
                        as.this.S.cancel();
                        as.this.S.purge();
                        as.this.S = null;
                        as.this.d.runOnUiThread(as.this.new c());
                        as.a(as.this, true);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    static /* synthetic */ void d(as asVar) {
        try {
            if (asVar.g == null || !asVar.g.isPlaying()) {
                return;
            }
            asVar.R = false;
            asVar.a("video_play", (JSONObject) null);
        } catch (Exception e2) {
        }
    }

    class e extends TimerTask {
        e() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            if (!as.this.k) {
                as.this.d.runOnUiThread(as.this.new d());
            }
        }
    }

    class b extends TimerTask {
        b() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            if (as.this.J > 0) {
                as.this.d.runOnUiThread(as.this.new i());
                as.this.m();
            }
        }
    }

    class h extends TimerTask {
        h() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            as.i(as.this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        if (this.T != null) {
            this.T.cancel();
            this.T.purge();
            this.T = null;
        }
        if (this.J > 0) {
            this.T = new Timer();
            try {
                this.T.schedule(new b(), 1000L);
            } catch (Exception e2) {
                ab.a("VideoManager", "mCountDownTimer.schedule throw Exception:", e2);
            }
        }
    }

    class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                if (as.this.g != null && as.this.g.isPlaying() && !as.this.k) {
                    if ((as.this.A.getVisibility() != 0 && as.this.F.getVisibility() != 0) || !as.this.g.isPlaying()) {
                        return;
                    }
                    as.this.b(true);
                    as.this.c(true);
                    as.this.e(true);
                    as.this.f(true);
                    if (as.this.m) {
                        return;
                    }
                    as.this.d(true);
                }
            } catch (Exception e) {
                ab.a("VideoManager", "HideTopBottomPlayPauseRunnable throw Exception:e", e);
            }
        }
    }

    class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (as.this.J > 0) {
                as.n(as.this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() throws IOException {
        m();
        this.p.removeView(this.A);
        this.A.setBackgroundColor(0);
        this.A.setBackgroundDrawable(b("/vpon_video2_bg_top.png"));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.n.b, M() ? (int) ((this.n.a / 8.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d));
        layoutParams.addRule(10);
        this.p.addView(this.A, layoutParams);
        s();
        this.p.removeView(this.B);
        this.B.setBackgroundColor(0);
        this.B.setBackgroundDrawable(b("/vpon_video2_bg_bottom.png"));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.n.b, M() ? (int) ((this.n.a / 8.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d));
        layoutParams2.addRule(12);
        this.p.addView(this.B, layoutParams2);
        t();
        if (this.v != null) {
            this.p.removeView(this.v);
        }
        this.v = new View(this.d);
        this.v.setId(555);
        this.v.setBackgroundColor(0);
        int i2 = M() ? (int) ((this.n.a / 8.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(i2, i2);
        layoutParams3.addRule(5);
        layoutParams3.addRule(10);
        this.p.addView(this.v, layoutParams3);
        if (this.f.s() != null || this.f.t() != null) {
            if (this.r.indexOfChild(this.u) != -1) {
                this.r.removeView(this.u);
            }
            if (this.p.indexOfChild(this.r) != -1) {
                this.p.removeView(this.r);
            }
            boolean zM = M();
            if (this.f.s() != null) {
                if (this.t == null) {
                    this.t = c(this.f.s());
                }
                if (this.t != null) {
                    if (this.s != null) {
                        this.r.removeView(this.s);
                    }
                    this.s = new ImageView(this.d);
                    this.s.setBackground(this.t);
                    int i3 = zM ? (int) ((this.n.a / 7.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d);
                    this.r.addView(this.s, new LinearLayout.LayoutParams(i3, i3));
                } else {
                    ab.b("VideoManager", "getIconFromUrl return null");
                }
            }
            if (this.f.t() != null && zM) {
                this.u = new TextView(this.d);
                this.u.setText("   " + this.f.t());
                this.u.setTextSize(25.0f);
                this.u.setShadowLayer(1.5f, 2.0f, -2.0f, -1442840576);
                this.r.addView(this.u, new LinearLayout.LayoutParams(-2, -2));
            }
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-2, -2);
            if (this.v != null) {
                layoutParams4.addRule(10);
                layoutParams4.addRule(5);
                layoutParams4.addRule(1, this.v.getId());
                this.p.addView(this.r, layoutParams4);
            }
        }
        A();
        if (!this.f.j() && !this.a) {
            if (this.E != null) {
                this.B.removeView(this.E);
                this.E = null;
            }
            ActionButton.a aVar = new ActionButton.a() { // from class: vpadn.as.13
                @Override // com.vpon.video.ActionButton.a
                public final void a(ActionButton actionButton) {
                    actionButton.setCommand(as.this.I());
                    actionButton.setButtonIcon(as.this.H());
                }
            };
            if (!M() || this.a || this.M) {
                this.E = new ActionButton(this, b(H()), I());
                this.E.setId(333);
                this.E.setAfterPressButtonListener(aVar);
                int i4 = M() ? (int) ((this.n.a / 8.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d);
                RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i4, i4);
                layoutParams5.addRule(12);
                layoutParams5.addRule(11);
                this.B.addView(this.E, layoutParams5);
            }
        }
        if (this.D != null) {
            this.B.removeView(this.D);
        }
        this.D = new ChangeSoundActionButton(this, b(this.i ? "/vpon_video2_s-off.png" : "/vpon_video2_s-on.png"), new ah(this));
        int i5 = M() ? (int) ((this.n.a / 8.0d) * 1.0d) : (int) ((this.n.a / 6.0d) * 1.0d);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(i5, i5);
        if (this.E != null) {
            layoutParams6.addRule(12);
            layoutParams6.addRule(7);
            layoutParams6.addRule(0, this.E.getId());
        } else {
            layoutParams6.addRule(12);
            layoutParams6.addRule(11);
        }
        this.B.addView(this.D, layoutParams6);
        if (!this.m) {
            new Timer().schedule(new TimerTask() { // from class: vpadn.as.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public final void run() {
                    if (as.this.d != null) {
                        as.this.d.runOnUiThread(new Runnable() { // from class: vpadn.as.1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                as.u(as.this);
                            }
                        });
                    }
                }
            }, 100L);
        }
        if (!this.f.m()) {
            if (this.J <= 0) {
                L();
            }
        } else if (this.l) {
            L();
        }
        J();
        if (this.m) {
            p();
        }
    }

    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public final void run() throws IOException {
            as.this.G();
            as.this.F();
            as.this.n();
            as.this.D();
        }
    }

    public as(VpadnActivity vpadnActivity, at atVar, ar arVar) {
        this.J = 0;
        this.P = null;
        this.Q.clear();
        this.d = vpadnActivity;
        this.P = atVar;
        this.f = arVar;
        this.o = this.d.n();
        this.p = new RelativeLayout(this.d);
        this.p.setId(33333);
        this.q = new RelativeLayout(this.d);
        this.r = new LinearLayout(this.d);
        this.r.setOrientation(0);
        this.w = new RelativeLayout(this.d);
        this.y = new TextView(this.d);
        this.y.setId(666);
        this.y.setText("  ");
        this.y.setTextSize(1.0f);
        this.A = new RelativeLayout(this.d);
        this.B = new RelativeLayout(this.d);
        this.J = arVar.b();
        this.e = new VponVideoView(this.d);
        this.e.setOnPreparedListener(new AnonymousClass7());
        this.e.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: vpadn.as.8
            @Override // android.media.MediaPlayer.OnErrorListener
            public final boolean onError(MediaPlayer mediaPlayer, int i2, int i3) {
                ab.b("VideoManager", "Video OnErrorListener what:" + i2 + " extra:" + i3);
                as.this.d.finish();
                return true;
            }
        });
        try {
            this.e.setOnInfoListener(new MediaPlayer.OnInfoListener(this) { // from class: vpadn.as.9
                @Override // android.media.MediaPlayer.OnInfoListener
                public final boolean onInfo(MediaPlayer mediaPlayer, int i2, int i3) {
                    return false;
                }
            });
        } catch (NoSuchMethodError e2) {
            ab.b("VideoManager", "setOnInfoListener throws NoSuchMethodError", e2);
        }
        this.e.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: vpadn.as.10
            @Override // android.media.MediaPlayer.OnCompletionListener
            public final void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
                ab.a("VideoManager", "Call OnCompletionListener");
                as.this.l = true;
                as.this.m = true;
                as.this.a("video_ended", (JSONObject) null);
                if (as.this.af != null) {
                    au auVar = as.this.af;
                    if (auVar.k) {
                        auVar.n = 0;
                    } else {
                        Iterator<String> it = auVar.g.iterator();
                        while (it.hasNext()) {
                            auVar.b(auVar.a(it.next()));
                        }
                        auVar.g.clear();
                    }
                }
                if (as.this.f.l()) {
                    as.this.a();
                    return;
                }
                as.this.L();
                if (as.this.f.m()) {
                    as.this.f.d(false);
                    as.this.J = -1;
                }
                as.this.p();
                as.this.J();
            }
        });
        this.p.setOnClickListener(new View.OnClickListener() { // from class: vpadn.as.11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                try {
                    as.B(as.this);
                } catch (Exception e3) {
                    ab.b("VideoManager", "showOrHideTopBottomPlayPauseAndScheduleHideTimer throw Exception", e3);
                }
            }
        });
    }

    /* renamed from: vpadn.as$7, reason: invalid class name */
    final class AnonymousClass7 implements MediaPlayer.OnPreparedListener {
        AnonymousClass7() {
        }

        @Override // android.media.MediaPlayer.OnPreparedListener
        public final void onPrepared(MediaPlayer mediaPlayer) throws JSONException {
            ab.a("VideoManager", "onPrepared");
            as.this.g = mediaPlayer;
            as.this.b = as.this.g.getVideoWidth();
            as.this.f332c = as.this.g.getVideoHeight();
            as.this.m = false;
            if (as.this.f.q()) {
                as.a(as.this, BitmapDescriptorFactory.HUE_RED);
                as.this.a(true);
            } else {
                as.a(as.this, 0.6f);
                as.this.a(false);
            }
            if (as.this.S == null) {
                as.this.S = new Timer();
            }
            try {
                as.this.S.schedule(as.this.new a(), 100L, 100L);
            } catch (Exception e) {
                ab.a("VideoManager", "mCheckHasPlayedTimer.schedule throw Exception:", e);
            }
            try {
                as.this.g.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() { // from class: vpadn.as.7.1
                    @Override // android.media.MediaPlayer.OnSeekCompleteListener
                    public final void onSeekComplete(MediaPlayer mediaPlayer2) {
                        as.this.d.runOnUiThread(new Runnable() { // from class: vpadn.as.7.1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                as.w(as.this);
                            }
                        });
                    }
                });
            } catch (Exception e2) {
            }
        }
    }

    static /* synthetic */ void w(as asVar) {
        ab.a("VideoManager", "Seek Complete!!");
        if (asVar.ad) {
            asVar.ad = false;
            asVar.F();
            asVar.G();
            if (asVar.g.isPlaying()) {
                return;
            }
            if (!asVar.R()) {
                asVar.S();
            }
            asVar.a("video_play", (JSONObject) null);
        }
    }

    private static AnimationSet o() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, BitmapDescriptorFactory.HUE_RED);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setStartOffset(10L);
        alphaAnimation.setDuration(300L);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        if (this.H != null) {
            this.p.removeView(this.H);
            this.H = null;
        }
        this.H = new ActionButton(this, b("/vpon_video2_replay.png"), new ap(this, this.d, this.f.r()));
        int i2 = (int) ((this.n.b / 8.0d) * 1.0d);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i2, i2);
        layoutParams.addRule(13);
        this.p.addView(this.H, layoutParams);
        this.F.setVisibility(4);
        s();
        t();
        u();
        v();
        this.H.setAfterPressButtonListener(new ActionButton.a() { // from class: vpadn.as.12
            @Override // com.vpon.video.ActionButton.a
            public final void a(ActionButton actionButton) {
                as.this.m = false;
                if (as.this.H != null) {
                    as.this.p.removeView(as.this.H);
                    as.this.H = null;
                }
                as.this.s();
                as.this.t();
                as.this.u();
                as.this.v();
                if (as.this.F != null) {
                    as.u(as.this);
                    as.this.J();
                }
                as.this.D();
                as.this.L = 0;
                new Handler().postDelayed(new Runnable() { // from class: vpadn.as.12.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        if (as.this.af != null) {
                            ab.a("VideoManager", "Call trackingManager.init()");
                            au auVar = as.this.af;
                            auVar.a(auVar.b, auVar.a);
                        }
                    }
                }, 500L);
            }
        });
    }

    final void a() throws IllegalStateException {
        if (M() && this.f.a(ar.b.LANDSCAPE) && this.p.getVisibility() == 0) {
            q();
        } else {
            r();
        }
    }

    private void q() throws IllegalStateException {
        try {
            this.g.pause();
            a("video_pause", (JSONObject) null);
        } catch (Exception e2) {
        }
        if (this.p != null) {
            this.p.setVisibility(4);
        }
        at atVar = this.P;
        ar arVar = this.f;
        atVar.a(ar.b.LANDSCAPE);
        L();
        try {
            K();
            this.I = new ActionButton(this, b("/vpon_video2_a-left.png"), new ag() { // from class: vpadn.as.2
                @Override // vpadn.ag
                public final void a() throws IllegalStateException {
                    if (as.this.p.getVisibility() != 0) {
                        as.this.w();
                        if (as.this.F != null) {
                            as.this.F.setButtonIcon(as.this.b("/vpon_video2_pause.png"));
                        }
                        try {
                            as.this.g.start();
                            as.this.a("video_play", (JSONObject) null);
                        } catch (Exception e3) {
                        }
                        if (as.this.G != null && as.this.G.getVisibility() == 0) {
                            as.this.L();
                        }
                        as.this.b(false);
                        as.this.c(false);
                        as.this.d(false);
                        as.this.e(false);
                        as.this.f(false);
                        as.this.K();
                    }
                }
            });
            this.I.setAfterPressButtonListener(new ActionButton.a() { // from class: vpadn.as.3
                @Override // com.vpon.video.ActionButton.a
                public final void a(ActionButton actionButton) {
                    if (as.this.H != null && as.this.H.getVisibility() == 0) {
                        as.this.m = false;
                        as.this.p.removeView(as.this.H);
                        as.this.H = null;
                        as.this.s();
                        as.this.t();
                        as.this.u();
                        as.this.v();
                        if (as.this.F != null) {
                            as.u(as.this);
                            as.this.J();
                        }
                        as.this.D();
                        as.this.L = 0;
                    }
                }
            });
            if (M()) {
                int i2 = (int) ((this.n.a / 8.0d) * 1.0d);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i2, i2);
                layoutParams.addRule(11);
                layoutParams.addRule(10);
                this.o.addView(this.I, layoutParams);
            } else {
                ab.b("VideoManager", "showVideoViewLayout button only exist at landscape mode");
            }
        } catch (Exception e3) {
            ab.a("VideoManager", "putShowVideoViewLayoutButton throw Exception", e3);
        }
    }

    private void r() {
        this.k = true;
        this.d.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        if (this.A != null) {
            this.A.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t() {
        if (this.B != null) {
            this.B.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u() {
        if (this.w != null) {
            this.w.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v() {
        if (this.r != null) {
            this.r.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w() {
        if (this.p != null) {
            this.p.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        if (this.A != null) {
            if (z) {
                this.A.setAnimation(o());
            }
            this.A.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(boolean z) {
        if (this.B != null) {
            if (z) {
                this.B.setAnimation(o());
            }
            this.B.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(boolean z) {
        if (this.F != null) {
            if (z) {
                this.F.setAnimation(o());
            }
            this.F.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(boolean z) {
        if (this.w != null) {
            if (z) {
                this.w.setAnimation(o());
            }
            this.w.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(boolean z) {
        if (this.r != null) {
            if (z) {
                this.r.setAnimation(o());
            }
            this.r.setVisibility(4);
        }
    }

    public final void a(boolean z, boolean z2) {
        this.a = z;
        this.M = z2;
        if (this.P != null) {
            if (M()) {
                this.P.a(this.f, ar.b.LANDSCAPE);
            } else {
                this.P.a(this.f, ar.b.PORTRAIT);
            }
        }
        x();
    }

    private void x() {
        az azVar;
        C();
        this.p.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.p.addView(this.e, layoutParams);
        E();
        if (this.n == null) {
            ab.b("VideoManager", "mVideoSize == null in putVideoViewLayoutToActivityRootLayout()");
        } else {
            boolean zM = M();
            RelativeLayout.LayoutParams layoutParams2 = zM ? new RelativeLayout.LayoutParams(-1, -1) : new RelativeLayout.LayoutParams(this.n.b, this.n.a);
            if (!zM) {
                if (this.f.c().equals(ar.b)) {
                    layoutParams2.addRule(12);
                } else if (this.f.c().equals(ar.f328c)) {
                    layoutParams2.addRule(13);
                } else if (this.f.c().equals(ar.a)) {
                    layoutParams2.addRule(10);
                }
            }
            this.o.addView(this.p, layoutParams2);
        }
        y();
        if (this.L != 0 && this.l) {
            this.L = 0;
        }
        try {
            azVar = new az(this.d, "vpadn_video_cache", 100000000);
        } catch (Exception e2) {
            ab.b("VideoManager", "Unable to create VpadnDiskLruCache for video cache in playVideo.");
            azVar = null;
        }
        if (azVar != null && this.f.w()) {
            Uri uriA = azVar.a(this.f.a());
            if (uriA != null) {
                ab.a("VideoManager", "use disk cache to play video");
                this.e.setVideoURI(uriA);
            } else {
                ab.a("VideoManager", "Cannot find video in disk cache");
                this.e.setVideoURI(Uri.parse(this.f.a()));
            }
        } else {
            this.e.setVideoURI(Uri.parse(this.f.a()));
        }
        if (this.L != 0) {
            this.e.seekTo(this.L);
        }
        this.e.start();
        this.e.setKeepScreenOn(true);
        ab.a("VideoManager", "mIsVideoLoadingState = true;");
        this.R = true;
        a("video_loading", (JSONObject) null);
    }

    private void y() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(13);
        this.K = new ProgressBar(this.d, null, android.R.attr.progressBarStyleSmallInverse);
        this.p.addView(this.K, layoutParams);
    }

    private String z() {
        String strValueOf;
        String strValueOf2;
        if (this.g == null) {
            ab.b("VideoManager", "mMediaPlayer == null at getRemainingTimeString()");
            return "";
        }
        try {
            if (this.g.getDuration() == -1) {
                ab.b("VideoManager", "mMediaPlayer.getDuration() == -1");
                return "";
            }
            if (this.m) {
                return " 00:00";
            }
            try {
                int duration = this.g.getDuration() - this.g.getCurrentPosition();
                int i2 = duration > 1000 ? duration / 1000 : 0;
                int i3 = i2 / 60;
                int i4 = i2 % 60;
                if (i3 < 10) {
                    strValueOf = "0" + i3;
                } else {
                    strValueOf = String.valueOf(i3);
                }
                if (i4 < 10) {
                    strValueOf2 = "0" + i4;
                } else {
                    strValueOf2 = String.valueOf(i4);
                }
                return String.format(" %s:%s", strValueOf, strValueOf2);
            } catch (Exception e2) {
                return " 00:00";
            }
        } catch (Exception e3) {
            ab.b("VideoManager", "getRemainingTimeString throw Exception :", e3);
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A() {
        if (this.C != null) {
            this.p.removeView(this.C);
        }
        this.C = new TextView(this.d);
        this.C.setBackgroundColor(0);
        this.C.setText(z());
        this.C.setTextColor(-1);
        this.C.setTextSize(1, 16.0f);
        this.C.setShadowLayer(1.5f, -3.0f, 3.0f, FluctConstants.FRAME_ALPHA_COLOR);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(9);
        layoutParams.addRule(12);
        this.p.addView(this.C, layoutParams);
        if (this.W != null) {
            this.W.cancel();
            this.W.purge();
            this.W = null;
        }
        this.W = new Timer();
        try {
            this.W.schedule(new g(), 1000L);
        } catch (Exception e2) {
            ab.a("VideoManager", "mPutVideoRemainingTimeTimer.schedule throws Exception", e2);
        }
    }

    private void B() {
        if (this.W != null) {
            this.W.cancel();
            this.W.purge();
            this.W = null;
        }
    }

    class g extends TimerTask {
        g() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() {
            as.this.d.runOnUiThread(as.this.new f());
        }
    }

    class f implements Runnable {
        f() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            as.this.A();
        }
    }

    class j {
        int a;
        int b;

        j(as asVar, int i, int i2) {
            this.b = i;
            this.a = i2;
        }
    }

    private j C() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.d.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (M()) {
            int i2 = displayMetrics.heightPixels;
            this.n = new j(this, (int) ((i2 / this.O) * this.N), i2);
        } else {
            int i3 = displayMetrics.widthPixels;
            this.n = new j(this, i3, (int) ((i3 / this.N) * this.O));
        }
        return this.n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D() {
        if (this.U == null) {
            this.U = new Timer();
        } else {
            this.U.cancel();
            this.U.purge();
            this.U = new Timer();
        }
        try {
            this.U.schedule(new e(), 1500L);
        } catch (Exception e2) {
            ab.b("VideoManager", "mHideTopBottomPlayPauseTimer.schedule throw Exception:", e2);
        }
    }

    static /* synthetic */ void B(as asVar) {
        if (asVar.k) {
            return;
        }
        if ((asVar.A != null && asVar.A.getVisibility() == 0) || (asVar.F != null && asVar.F.getVisibility() == 0)) {
            asVar.b(false);
            asVar.c(false);
            asVar.d(false);
            asVar.e(false);
            asVar.f(false);
            return;
        }
        asVar.s();
        asVar.t();
        asVar.u();
        asVar.v();
        if (!asVar.m && asVar.F != null) {
            asVar.F.setVisibility(0);
        }
        asVar.D();
    }

    static /* synthetic */ void n(as asVar) {
        boolean zIsPlaying = true;
        try {
            if (asVar.g != null) {
                zIsPlaying = asVar.g.isPlaying();
            }
        } catch (Exception e2) {
        }
        if (asVar.g != null && zIsPlaying) {
            asVar.J--;
        }
        if (asVar.J != 0 || asVar.f.m()) {
            return;
        }
        asVar.L();
    }

    private void E() {
        this.p.removeView(this.q);
        this.q.setBackgroundColor(-872415232);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.p.addView(this.q, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        this.p.removeView(this.q);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G() {
        if (this.K != null) {
            this.p.removeView(this.K);
            this.K = null;
        }
    }

    static /* synthetic */ void a(as asVar, float f2) {
        if (asVar.g != null) {
            asVar.g.setVolume(f2, f2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable b(String str) {
        BitmapDrawable bitmapDrawable;
        if (str == null) {
            return null;
        }
        try {
            bitmapDrawable = new BitmapDrawable(getClass().getResourceAsStream(str));
        } catch (Exception e2) {
            ab.a("VideoManager", "getDrawableByFileName throw Exception", e2);
            bitmapDrawable = null;
        }
        return bitmapDrawable;
    }

    private static Drawable c(String str) throws IOException {
        try {
            return Drawable.createFromStream((InputStream) new URL(str).getContent(), null);
        } catch (MalformedURLException e2) {
            ab.a("VideoManager", "getButtonImageFromUrl throws Exception", e2);
            return null;
        } catch (IOException e3) {
            ab.a("VideoManager", "getButtonImageFromUrl throws Exception", e3);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String H() {
        if (M()) {
            return "/vpon_video2_reply.png";
        }
        return "/vpon_video2_full.png";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ag I() {
        return M() ? new al(this) : new ak(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void J() {
        this.p.removeView(this.w);
        this.w.removeAllViews();
        int i2 = 0;
        Iterator<ar.a> it = this.f.g().iterator();
        while (true) {
            int i3 = i2;
            if (!it.hasNext()) {
                break;
            }
            ar.a next = it.next();
            if (i3 == 0) {
                this.x = a(next);
                if (this.x != null) {
                    this.x.setId(999);
                } else {
                    return;
                }
            } else if (i3 == 1) {
                this.z = a(next);
                if (this.z == null) {
                    return;
                }
            } else if (i3 >= 2) {
                ab.d("VideoManager", "funcButtonCount >= MAX_OF_FUNC_BUTTONS");
                break;
            }
            i2 = i3 + 1;
            if (this.x != null || this.z != null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                if (i2 == 2) {
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                    layoutParams2.addRule(11);
                    layoutParams2.addRule(3, this.x.getId());
                    this.w.addView(this.y, layoutParams2);
                    layoutParams.addRule(11);
                    layoutParams.addRule(3, this.y.getId());
                    if (this.m) {
                        this.z.setBackgroundColor(1711276032);
                    }
                    this.w.addView(this.z, layoutParams);
                } else {
                    layoutParams.addRule(10);
                    layoutParams.addRule(11);
                    this.w.addView(this.x, layoutParams);
                }
            } else {
                ab.b("VideoManager", "putFuncButtonList(data); return null");
            }
        }
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.addRule(11);
        layoutParams3.addRule(10);
        this.p.addView(this.w, layoutParams3);
    }

    private FuncButton a(ar.a aVar) {
        String str = aVar.a;
        String str2 = aVar.f329c;
        if (str.equals("place_call")) {
            return new FuncButton(this, str2, new an(this, aVar.f, aVar.e));
        }
        if (str.equals("send_sms")) {
            return new FuncButton(this, str2, new aq(this, aVar.f, aVar.e));
        }
        if (str.equals("cre_cal_event")) {
            return new FuncButton(this, str2, new aj(this, aVar.f, aVar.e));
        }
        return new FuncButton(this, str2, new am(this, aVar.f, aVar.b, aVar.d, aVar.e));
    }

    static /* synthetic */ void u(as asVar) {
        if (asVar.F != null) {
            asVar.p.removeView(asVar.F);
        }
        try {
            asVar.F = new PlayPauseVideoActionButton(asVar, asVar.b((asVar.g == null || !asVar.g.isPlaying()) ? "/vpon_video2_play.png" : "/vpon_video2_pause.png"), new ap(asVar, null, null)) { // from class: vpadn.as.14
                @Override // com.vpon.video.PlayPauseVideoActionButton, com.vpon.video.ActionButton
                public final void a() {
                    super.a();
                    if (as.this.g.isPlaying()) {
                        as.this.D();
                    }
                }
            };
            int i2 = (int) ((asVar.n.b / 8.0d) * 1.0d);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i2, i2);
            layoutParams.addRule(13);
            asVar.p.addView(asVar.F, layoutParams);
        } catch (Exception e2) {
            ab.b("VideoManager", "putPlayPauseButton throw Exception", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        try {
            if (this.I != null) {
                this.o.removeView(this.I);
                this.I = null;
            }
        } catch (Exception e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void L() {
        int i2;
        boolean z = true;
        try {
            if (this.G != null) {
                this.p.removeView(this.G);
                this.o.removeView(this.G);
            }
            boolean zM = M();
            if (zM && this.f.a(ar.b.LANDSCAPE) && this.p.getVisibility() == 0) {
                this.G = new ActionButton(this, b("/vpon_video2_a-right.png"), new ai(this));
            } else {
                this.G = new ActionButton(this, b("/vpon_video2_close.png"), new ai(this));
            }
            if (zM) {
                i2 = (int) ((this.n.a / 8.0d) * 1.0d);
            } else {
                i2 = (int) ((this.n.a / 6.0d) * 1.0d);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i2, i2);
            layoutParams.addRule(5);
            layoutParams.addRule(10);
            if (zM) {
                if (this.p.getVisibility() == 0) {
                    this.p.addView(this.G, layoutParams);
                    return;
                } else {
                    this.o.addView(this.G, layoutParams);
                    return;
                }
            }
            if (!M() && (!this.f.a(ar.b.PORTRAIT) || (this.f.c().equals(ar.a) && this.p.getVisibility() == 0))) {
                z = false;
            }
            if (z) {
                this.o.addView(this.G, layoutParams);
            } else {
                this.p.addView(this.G, layoutParams);
            }
        } catch (Exception e2) {
            ab.a("VideoManager", "putCloseButton throw Exception", e2);
        }
    }

    private boolean M() {
        int iN = N();
        return iN == 0 || iN == 8;
    }

    public final MediaPlayer b() {
        return this.g;
    }

    public final boolean c() {
        return this.i;
    }

    final void a(boolean z) throws JSONException {
        this.i = z;
        Q();
    }

    public final VpadnActivity d() {
        return this.d;
    }

    public final void e() {
        ab.a("VideoManager", "Call notifyVponActivityOnPause");
        this.j = true;
        if (this.e != null) {
            this.L = this.e.getCurrentPosition();
            this.q.removeAllViews();
            this.p.removeAllViews();
            this.o.removeView(this.p);
            this.h = false;
            O();
            B();
        }
    }

    public final void f() {
        ab.a("VideoManager", "Call notifyVponActivityOnResume");
        this.j = false;
        if (this.e != null && !this.h) {
            x();
        }
    }

    public final void g() {
        ab.a("VideoManager", "Call notifyVponActivityOnDestroy");
        this.k = true;
        O();
        B();
        if (this.X != null) {
            this.X.cancel();
            this.X.purge();
            this.X = null;
        }
    }

    private int N() {
        int rotation = this.d.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.d.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.widthPixels;
        int i3 = displayMetrics.heightPixels;
        if (((rotation == 0 || rotation == 2) && i3 > i2) || ((rotation == 1 || rotation == 3) && i2 > i3)) {
            switch (rotation) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    ab.b("VideoManager", "Unknown screen orientation. Defaulting to portrait.");
                    break;
            }
            return 1;
        }
        switch (rotation) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                ab.b("VideoManager", "Unknown screen orientation. Defaulting to landscape.");
                break;
        }
        return 1;
    }

    public final void a(int i2) throws JSONException, IOException {
        ab.a("VideoManager", "Call redrawAllOfView() orientation:" + i2);
        if ((this.p.getVisibility() != 0) && 1 == i2) {
            K();
            w();
        } else if (this.I != null) {
            this.o.removeView(this.I);
            this.I = null;
        }
        C();
        if (this.e != null && this.p != null) {
            this.p.getLayoutParams().height = this.n.a;
            this.p.getLayoutParams().width = this.n.b;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.p.getLayoutParams();
            if (2 == i2) {
                layoutParams.addRule(13);
            } else if (this.f.c().equals(ar.b)) {
                layoutParams.addRule(12);
            } else if (this.f.c().equals(ar.f328c)) {
                layoutParams.addRule(13);
            } else if (this.f.c().equals(ar.a)) {
                layoutParams.addRule(10);
            }
            this.p.requestLayout();
            n();
            D();
        }
        P();
        try {
            final boolean zIsPlaying = this.g.isPlaying();
            new Timer().schedule(new TimerTask() { // from class: vpadn.as.4
                @Override // java.util.TimerTask, java.lang.Runnable
                public final void run() {
                    if (as.this.d != null) {
                        VpadnActivity vpadnActivity = as.this.d;
                        final boolean z = zIsPlaying;
                        vpadnActivity.runOnUiThread(new Runnable() { // from class: vpadn.as.4.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                try {
                                    boolean zIsPlaying2 = as.this.g.isPlaying();
                                    if (as.this.g.isPlaying() != z) {
                                        if (zIsPlaying2) {
                                            as.this.a("video_play", (JSONObject) null);
                                        } else {
                                            as.this.a("video_pause", (JSONObject) null);
                                        }
                                    }
                                } catch (Exception e2) {
                                }
                            }
                        });
                    }
                }
            }, 100L);
        } catch (Exception e2) {
        }
    }

    public final void h() {
        int iN = N();
        if (!this.f.j() && iN != 0 && iN != 8) {
            this.d.setRequestedOrientation(0);
            new Timer().schedule(new TimerTask() { // from class: vpadn.as.5
                @Override // java.util.TimerTask, java.lang.Runnable
                public final void run() {
                    if (!as.this.k && !as.this.a) {
                        as.this.d.setRequestedOrientation(4);
                    }
                }
            }, 5000L);
        }
    }

    public final void i() {
        int iN = N();
        if (!this.f.j() && iN != 1 && iN != 9) {
            this.d.setRequestedOrientation(1);
            new Timer().schedule(new TimerTask() { // from class: vpadn.as.6
                @Override // java.util.TimerTask, java.lang.Runnable
                public final void run() {
                    if (!as.this.k && !as.this.a) {
                        as.this.d.setRequestedOrientation(4);
                    }
                }
            }, 5000L);
        }
    }

    static /* synthetic */ void a(as asVar, boolean z) {
        if (asVar.f == null || (asVar.f.x().isEmpty() && asVar.f.e() == null)) {
            ab.d("VideoManager", "Cannot start tracking timer (mVideoData == null || mVideoData.getTrackingDataMap().isEmpty())");
            return;
        }
        if (asVar.af == null) {
            asVar.af = new au(asVar.d);
        }
        if (z) {
            asVar.af.a(asVar.f, asVar.g);
        }
        if (asVar.V != null) {
            asVar.V.cancel();
            asVar.V.purge();
            asVar.V = null;
        }
        asVar.V = new Timer();
        try {
            asVar.V.schedule(asVar.new h(), 100L, 1000L);
        } catch (Exception e2) {
            ab.a("VideoManager", "mTrackingTimer.schedule throw Exception:", e2);
        }
    }

    public final ar j() {
        return this.f;
    }

    public final int k() {
        try {
            if (this.g != null) {
                return this.g.getCurrentPosition();
            }
            return 0;
        } catch (Exception e2) {
            return 0;
        }
    }

    public final int l() {
        try {
            if (this.g != null) {
                return this.g.getDuration();
            }
            return 0;
        } catch (Exception e2) {
            ab.a("VideoManager", "getVideoDuration throw Exception e:", e2);
            return 0;
        }
    }

    private void O() {
        if (this.V != null) {
            this.V.cancel();
            this.V.purge();
            this.V = null;
        }
    }

    static /* synthetic */ void i(as asVar) {
        try {
            if (asVar.k || asVar.j || asVar.g == null || !asVar.g.isPlaying() || asVar.af == null) {
                return;
            }
            au auVar = asVar.af;
            int currentPosition = asVar.g.getCurrentPosition();
            if (auVar.k) {
                auVar.n++;
                if (auVar.n == auVar.m) {
                    auVar.b(auVar.a(auVar.l));
                    auVar.n = 0;
                    return;
                }
                return;
            }
            if (currentPosition > 0 && currentPosition < auVar.h) {
                Iterator<String> it = auVar.f334c.iterator();
                while (it.hasNext()) {
                    auVar.b(auVar.a(it.next()));
                }
                auVar.f334c.clear();
            }
            if (currentPosition >= auVar.h && currentPosition < auVar.i) {
                Iterator<String> it2 = auVar.d.iterator();
                while (it2.hasNext()) {
                    auVar.b(auVar.a(it2.next()));
                }
                auVar.d.clear();
            }
            if (currentPosition >= auVar.i && currentPosition < auVar.j) {
                Iterator<String> it3 = auVar.e.iterator();
                while (it3.hasNext()) {
                    auVar.b(auVar.a(it3.next()));
                }
                auVar.e.clear();
            }
            if (currentPosition >= auVar.j) {
                Iterator<String> it4 = auVar.f.iterator();
                while (it4.hasNext()) {
                    auVar.b(auVar.a(it4.next()));
                }
                auVar.f.clear();
            }
        } catch (Exception e2) {
            ab.a("VideoManager", "sendTrackingUrl() throws Exception", e2);
        }
    }

    public final void a(String str, int i2, C0101o c0101o) throws JSONException {
        ab.a("VideoManager", "addVideoEventListener evnetType:" + str);
        if (str.equals("video_loading")) {
            c("video_loading", i2, c0101o);
            if (this.R) {
                C0108v c0108v = new C0108v(C0108v.a.OK);
                c0108v.a(true);
                c0101o.a(c0108v);
                ab.a("VideoManager", "send video loading event!! at doHandleAddLoadingEvent");
                return;
            }
            return;
        }
        if (str.equals("video_play")) {
            c("video_play", i2, c0101o);
            try {
                if (this.g == null || !this.g.isPlaying()) {
                    return;
                }
                C0108v c0108v2 = new C0108v(C0108v.a.OK);
                c0108v2.a(true);
                c0101o.a(c0108v2);
                ab.a("VideoManager", "send video play event!! at doHandleAddPlayEvent");
                return;
            } catch (Exception e2) {
                return;
            }
        }
        if (str.equals("video_pause")) {
            c("video_pause", i2, c0101o);
            try {
                if (this.g == null || this.g.isPlaying() || this.m) {
                    return;
                }
                C0108v c0108v3 = new C0108v(C0108v.a.OK);
                c0108v3.a(true);
                c0101o.a(c0108v3);
                ab.a("VideoManager", "send video pause event!! at doHandleAddPauseEvent");
                return;
            } catch (Exception e3) {
                return;
            }
        }
        if (str.equals("video_ended")) {
            c("video_ended", i2, c0101o);
            try {
                if (this.m) {
                    C0108v c0108v4 = new C0108v(C0108v.a.OK);
                    c0108v4.a(true);
                    c0101o.a(c0108v4);
                    ab.a("VideoManager", "send video ended event!! at doHandleAddEndedEvent");
                    return;
                }
                return;
            } catch (Exception e4) {
                return;
            }
        }
        if (str.equals("video_timeupdate")) {
            c("video_timeupdate", i2, c0101o);
            if (this.X == null) {
                this.X = new Timer();
                try {
                    this.X.schedule(new k(), 0L, 1000L);
                    return;
                } catch (Exception e5) {
                    ab.a("VideoManager", "mVideoTimeUpdateEventTimer.schedule throw Exception", e5);
                    return;
                }
            }
            return;
        }
        if (str.equals("video_volumechange")) {
            c("video_volumechange", i2, c0101o);
            Q();
        } else if (str.equals("video_orientation")) {
            c("video_orientation", i2, c0101o);
            P();
        }
    }

    private void P() throws JSONException {
        try {
            if (M()) {
                this.aa.put("orientation", "landscape");
            } else {
                this.aa.put("orientation", "portrait");
            }
        } catch (JSONException e2) {
            ab.a("VideoManager", "triggerOrientationChangeEvent throw JSONException", e2);
        }
        a("video_orientation", this.aa);
    }

    private void Q() throws JSONException {
        try {
            if (this.g != null) {
                if (this.i) {
                    this.Z.put("volume", 0);
                } else {
                    this.Z.put("volume", 1);
                }
                a("video_volumechange", this.Z);
            }
        } catch (Exception e2) {
        }
    }

    class k extends TimerTask {
        private int a = -1;

        k() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public final void run() throws JSONException {
            int iRound;
            try {
                if (as.this.g != null && as.this.g.isPlaying() && this.a != (iRound = (int) Math.round(as.this.k() / 1000.0d))) {
                    this.a = iRound;
                    as.this.Y.put("current_time", iRound);
                    as.this.a("video_timeupdate", as.this.Y);
                }
            } catch (Exception e) {
            }
        }
    }

    private void c(String str, int i2, C0101o c0101o) {
        Map<Integer, C0101o> map = this.Q.get(str);
        if (map == null) {
            HashMap map2 = new HashMap();
            map2.put(Integer.valueOf(i2), c0101o);
            this.Q.put(str, map2);
            return;
        }
        map.put(Integer.valueOf(i2), c0101o);
    }

    public final void b(String str, int i2, C0101o c0101o) throws JSONException {
        try {
            if (this.Q.containsKey(str)) {
                Map<Integer, C0101o> map = this.Q.get(str);
                map.remove(Integer.valueOf(i2));
                if (map.size() == 0) {
                    this.Q.remove(str);
                }
                c0101o.b();
                return;
            }
            a(c0101o, "Cannot find event type in mVideoEventListenerMap to remove! " + str);
        } catch (Exception e2) {
            a(c0101o, "removeVideoEventListener throw Exception e:", e2);
        }
    }

    final void a(String str, JSONObject jSONObject) {
        C0108v c0108v;
        if (jSONObject != null) {
            ab.a("VideoManager", "triggerVideoEvent videoEventType:" + str + " retObj:" + jSONObject.toString());
        } else {
            ab.a("VideoManager", "triggerVideoEvent videoEventType:" + str);
        }
        if (this.Q.get(str) != null) {
            Iterator<C0101o> it = this.Q.get(str).values().iterator();
            if (jSONObject != null) {
                c0108v = new C0108v(C0108v.a.OK, jSONObject);
            } else {
                c0108v = new C0108v(C0108v.a.OK);
            }
            c0108v.a(true);
            while (it.hasNext()) {
                it.next().a(c0108v);
            }
            return;
        }
        ab.d("VideoManager", "Cannot find video event:" + str + " registered! ");
    }

    public final void a(String str, JSONArray jSONArray, C0101o c0101o) throws IllegalStateException, JSONException {
        int i2;
        if (str.equals("play_video")) {
            try {
                if (this.p == null || this.p.getVisibility() == 4) {
                    a(c0101o, "videoViewLayout is invisible, Cannot call play video from .js side!");
                } else if (R()) {
                    c0101o.b();
                } else if (this.g == null || this.g.isPlaying()) {
                    ab.b("VideoManager", "vidoe always playing");
                    c0101o.b();
                } else {
                    S();
                    c0101o.b();
                }
                return;
            } catch (Exception e2) {
                a(c0101o, "playVideoByCordova throw Exception", e2);
                return;
            }
        }
        if (str.equals("pause_video")) {
            try {
                if (this.g == null) {
                    a(c0101o, "mMediaPlayer == null at pauseVideoByCordova");
                } else if (this.g.isPlaying()) {
                    S();
                    c0101o.b();
                } else {
                    ab.b("VideoManager", "vidoe always paused");
                    c0101o.b();
                }
                return;
            } catch (Exception e3) {
                a(c0101o, "pauseVideoByCordova throw Exception", e3);
                return;
            }
        }
        if (str.equals("mute_video")) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(0);
                if (jSONObject.has("mute")) {
                    boolean z = jSONObject.getInt("mute") != 0;
                    if (z && !this.i) {
                        this.D.performClick();
                    } else if (z || !this.i) {
                        ab.d("VideoManager", "NO NEED DO ANYTHING! FOR MUTE_VIDEO");
                    } else {
                        this.D.performClick();
                    }
                    try {
                        if (this.i) {
                            this.ab.put("mute", 1);
                        } else {
                            this.ab.put("mute", 0);
                        }
                        c0101o.a(this.ab);
                        return;
                    } catch (JSONException e4) {
                        a(c0101o, "muteVideoByCordova2 throw exception", e4);
                        return;
                    }
                }
                a(c0101o, "muteVideoByCordova cannot find mute field");
                return;
            } catch (Exception e5) {
                a(c0101o, "muteVideoByCordova1 throw exception", e5);
                return;
            }
        }
        if (str.equals("change_video_orientation")) {
            try {
                if (this.f.j() || this.a) {
                    a(c0101o, "cannot change orientation (mVideoData.isFixOrientation() || mIsFixedOrientationToLandscape)");
                    return;
                }
                boolean zM = M();
                JSONObject jSONObject2 = jSONArray.getJSONObject(0);
                if (!jSONObject2.has("orientation")) {
                    a(c0101o, "cannot find change orientation field");
                    return;
                }
                String string = jSONObject2.getString("orientation");
                if (string.equals("landscape") || string.equals("portrait")) {
                    if ((string.equals("landscape") && !zM) || (string.equals("portrait") && zM)) {
                        if (this.E != null) {
                            this.E.performClick();
                        } else if (zM) {
                            i();
                        }
                    }
                    c0101o.b();
                    return;
                }
                a(c0101o, "change orientation name of field is typo error");
                return;
            } catch (Exception e6) {
                ab.a("VideoManager", "chagneVideoOrientationByCordova throw exception", e6);
                return;
            }
        }
        if (str.equals("hide_video")) {
            if (!M() || !this.f.a(ar.b.LANDSCAPE) || this.p.getVisibility() != 0) {
                a(c0101o, "cannot execute show hide action");
                return;
            } else {
                q();
                c0101o.b();
                return;
            }
        }
        if (str.equals("show_video")) {
            if (this.I == null || this.I.getVisibility() != 0) {
                a(c0101o, "cannot execute hide video action");
                return;
            } else {
                this.I.performClick();
                c0101o.b();
                return;
            }
        }
        if (str.equals("get_video_total_time")) {
            if (this.g != null) {
                try {
                    try {
                        this.ac.put("total_time", (int) (this.g.getDuration() / 1000.0d));
                        c0101o.a(this.ac);
                        return;
                    } catch (Exception e7) {
                        return;
                    }
                } catch (Exception e8) {
                    try {
                        this.ac.put("total_time", 0);
                        c0101o.a(this.ac);
                        return;
                    } catch (Exception e9) {
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        this.ac.put("total_time", 0);
                        c0101o.a(this.ac);
                    } catch (Exception e10) {
                    }
                    throw th;
                }
            }
            a(c0101o, "mMediaPlayer == null in getVideoTotalTimeByCordova");
            return;
        }
        if (str.equals("seek_video")) {
            try {
                if (this.g == null) {
                    a(c0101o, "mMediaPlayer == null at seekVideoByCordova");
                } else {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(0);
                    if (jSONObject3.has("seek_to")) {
                        int i3 = jSONObject3.getInt("seek_to");
                        ab.b("VideoManager", "seekToSec:" + i3);
                        int i4 = i3 * 1000;
                        if (i4 > this.g.getDuration() || i3 < 0) {
                            a(c0101o, "seekToMillSec > mMediaPlayer.getDuration() || seekToSec < 0");
                        } else {
                            this.ad = true;
                            E();
                            y();
                            this.g.seekTo(i4);
                            c0101o.b();
                        }
                    } else {
                        a(c0101o, "Cannot find seek_to field for seek video action");
                    }
                }
                return;
            } catch (Exception e11) {
                a(c0101o, "seekVideoByCordova throw exception", e11);
                return;
            }
        }
        if (str.equals("play_next_video")) {
            try {
                if (this.g == null) {
                    a(c0101o, "mMediaPlayer == null at playNextVideoByCordova");
                    return;
                }
                JSONObject jSONObject4 = jSONArray.getJSONObject(0);
                if (!jSONObject4.has("v_u")) {
                    a(c0101o, "Cannot find video url for play next video");
                    return;
                }
                String string2 = jSONObject4.getString("v_u");
                if (C0086a.c(string2) || string2.equals(this.f.a())) {
                    a(c0101o, "!StringUtils.isBlank(nextVideoUrl) && !nextVideoUrl.equals(mVideoData.getVideoUrl()) is false");
                    return;
                }
                ao aoVar = new ao(string2);
                if (jSONObject4.has("auto_close") && jSONObject4.getInt("auto_close") == 1) {
                    aoVar.d = true;
                }
                if (jSONObject4.has("tracking_u")) {
                    String string3 = jSONObject4.getString("tracking_u");
                    if (!C0086a.c(string3)) {
                        aoVar.b = string3;
                    }
                }
                if (jSONObject4.has("v_tracking")) {
                    aoVar.a(jSONObject4.getJSONObject("v_tracking"));
                }
                if (jSONObject4.has("tracking_interval") && (i2 = jSONObject4.getInt("tracking_interval")) > 0) {
                    aoVar.f326c = i2;
                }
                if (jSONObject4.has("replay_tracking_u")) {
                    String string4 = jSONObject4.getString("replay_tracking_u");
                    if (!C0086a.c(string4)) {
                        aoVar.e = string4;
                    }
                }
                a(aoVar, jSONObject4);
                a(aoVar);
                c0101o.b();
            } catch (Exception e12) {
                a(c0101o, "playNextVideoByCordova throw exception", e12);
            }
        }
    }

    private boolean R() {
        if (!this.m || this.H == null || this.g.isPlaying()) {
            return false;
        }
        this.H.performClick();
        return true;
    }

    private void S() {
        if (this.F != null) {
            if (this.F.getVisibility() == 4) {
                this.p.performClick();
            }
            this.F.performClick();
        }
    }

    private static void a(ao aoVar, JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2;
        ar.a.EnumC0081a enumC0081a;
        try {
            if (jSONObject.has("btns")) {
                JSONArray jSONArray = jSONObject.getJSONArray("btns");
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(i2);
                    ar.a aVar = new ar.a();
                    if (jSONObject3.has("action")) {
                        if (ae.contains(jSONObject3.getString("action"))) {
                            aVar.a = jSONObject3.getString("action");
                            if (jSONObject3.has("btn_text")) {
                                String string = jSONObject3.getString("btn_text");
                                if (!C0086a.c(string)) {
                                    aVar.f329c = string;
                                    if (jSONObject3.has("app_u")) {
                                        aVar.d = jSONObject3.getString("app_u");
                                    }
                                    if (jSONObject3.has("btn_tracking_u")) {
                                        aVar.e = jSONObject3.getString("btn_tracking_u");
                                    }
                                    if (jSONObject3.has("launch_type")) {
                                        String string2 = jSONObject3.getString("launch_type");
                                        if (string2.equals("inapp")) {
                                            enumC0081a = ar.a.EnumC0081a.INAPP;
                                        } else if (string2.equals("outapp")) {
                                            enumC0081a = ar.a.EnumC0081a.OUTAPP;
                                        } else {
                                            ab.b("VideoManager", "button launch type format error");
                                        }
                                        aVar.b = enumC0081a;
                                        if (jSONObject3.has("data") && (jSONObject2 = jSONObject3.getJSONObject("data")) != null) {
                                            aVar.f = new JSONObject(jSONObject2.toString());
                                        }
                                        aoVar.f.add(aVar);
                                    } else {
                                        if (jSONObject3.has("data")) {
                                            aVar.f = new JSONObject(jSONObject2.toString());
                                        }
                                        aoVar.f.add(aVar);
                                    }
                                } else {
                                    ab.b("VideoManager", "btn_text is blank");
                                }
                            }
                        } else {
                            ab.b("VideoManager", "Unsupport function button Type");
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
    }

    private void a(ao aoVar) {
        ab.a("VideoManager", "Call handlePlayNextVideo");
        this.q.removeAllViews();
        this.p.removeAllViews();
        this.o.removeView(this.p);
        O();
        B();
        this.L = 0;
        this.f.a(aoVar.a);
        if (this.f.l() != aoVar.d) {
            this.f.c(aoVar.d);
        }
        this.f.c(aoVar.b);
        this.f.b(aoVar.f326c);
        this.f.a(aoVar.g);
        this.f.g(aoVar.e);
        this.f.h();
        if (aoVar.f.size() > 0) {
            Iterator<ar.a> it = aoVar.f.iterator();
            while (it.hasNext()) {
                this.f.a(it.next());
            }
        }
        w();
        x();
    }

    private void a(C0101o c0101o, String str, Exception exc) throws JSONException {
        ab.a("VideoManager", "Video Action throw Exception:", exc);
        a(c0101o, str);
    }

    private static void a(C0101o c0101o, String str) throws JSONException {
        ab.b("VideoManager", "Video Action Error:" + str);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("e", str);
            c0101o.b(jSONObject);
        } catch (Exception e2) {
        }
    }
}
