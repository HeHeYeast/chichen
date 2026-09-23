package com.jirbo.adcolony;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class e extends SurfaceView implements MediaController.MediaPlayerControl {
    static final int e = -1;
    static final int f = 0;
    static final int g = 1;
    static final int h = 2;
    static final int i = 3;
    static final int j = 4;
    static final int k = 5;
    static final int l = 6;
    static final int m = 7;
    static final int n = 8;
    MediaPlayer.OnErrorListener A;
    int B;
    boolean C;
    boolean D;
    boolean E;
    boolean F;
    int G;
    MediaPlayer.OnVideoSizeChangedListener H;
    MediaPlayer.OnPreparedListener I;
    SurfaceHolder.Callback J;
    private MediaPlayer.OnCompletionListener K;
    private MediaPlayer.OnErrorListener L;
    private MediaPlayer.OnBufferingUpdateListener M;
    String a;
    Uri b;

    /* renamed from: c, reason: collision with root package name */
    FileDescriptor f224c;
    int d;
    int o;
    int p;
    SurfaceHolder q;
    MediaPlayer r;
    int s;
    int t;
    int u;
    int v;
    MediaController w;
    MediaPlayer.OnCompletionListener x;
    MediaPlayer.OnPreparedListener y;
    int z;

    e(Context context) {
        super(context);
        this.a = "ADCCustomVideoView";
        this.o = 0;
        this.p = 0;
        this.q = null;
        this.r = null;
        this.H = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.jirbo.adcolony.e.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                }
            }
        };
        this.I = new MediaPlayer.OnPreparedListener() { // from class: com.jirbo.adcolony.e.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) {
                e.this.o = 2;
                e eVar = e.this;
                e eVar2 = e.this;
                e.this.E = true;
                eVar2.D = true;
                eVar.C = true;
                if (e.this.y != null) {
                    e.this.y.onPrepared(e.this.r);
                }
                if (e.this.w != null) {
                    e.this.w.setEnabled(true);
                }
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                int i2 = e.this.B;
                if (i2 != 0) {
                    e.this.seekTo(i2);
                }
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                    if (e.this.u == e.this.s && e.this.v == e.this.t) {
                        if (e.this.p == 3) {
                            e.this.start();
                            if (e.this.w != null) {
                                e.this.w.show();
                                return;
                            }
                            return;
                        }
                        if (!e.this.isPlaying()) {
                            if ((i2 != 0 || e.this.getCurrentPosition() > 0) && e.this.w != null) {
                                e.this.w.show(0);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (e.this.p == 3) {
                    e.this.start();
                }
            }
        };
        this.K = new MediaPlayer.OnCompletionListener() { // from class: com.jirbo.adcolony.e.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                e.this.o = 5;
                e.this.p = 5;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.x != null) {
                    e.this.x.onCompletion(e.this.r);
                }
            }
        };
        this.L = new MediaPlayer.OnErrorListener() { // from class: com.jirbo.adcolony.e.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                String str;
                Log.d(e.this.a, "Error: " + framework_err + "," + impl_err);
                e.this.o = -1;
                e.this.p = -1;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if ((e.this.A == null || !e.this.A.onError(e.this.r, framework_err, impl_err)) && e.this.getWindowToken() != null) {
                    e.this.b().getResources();
                    if (framework_err == 200) {
                        str = "Invalid progressive playback";
                    } else {
                        str = "Unknown error";
                    }
                    new AlertDialog.Builder(e.this.b()).setTitle("ERROR").setMessage(str).setPositiveButton("OKAY", new DialogInterface.OnClickListener() { // from class: com.jirbo.adcolony.e.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (e.this.x != null) {
                                e.this.x.onCompletion(e.this.r);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.M = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.jirbo.adcolony.e.5
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                e.this.z = percent;
            }
        };
        this.J = new SurfaceHolder.Callback() { // from class: com.jirbo.adcolony.e.6
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h2) {
                e.this.u = w;
                e.this.v = h2;
                boolean z = e.this.p == 3;
                boolean z2 = e.this.s == w && e.this.t == h2;
                if (e.this.r != null && z && z2) {
                    if (e.this.B != 0) {
                        e.this.seekTo(e.this.B);
                    }
                    e.this.start();
                    if (e.this.w != null) {
                        e.this.w.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
                e.this.q = holder;
                if (e.this.r == null || e.this.o != 6 || e.this.p != 7) {
                    e.this.f();
                } else {
                    e.this.r.setDisplay(e.this.q);
                    e.this.d();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                e.this.q = null;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.o != 6) {
                    e.this.a(true);
                }
            }
        };
        e();
    }

    e(Context context, boolean z) {
        super(context);
        this.a = "ADCCustomVideoView";
        this.o = 0;
        this.p = 0;
        this.q = null;
        this.r = null;
        this.H = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.jirbo.adcolony.e.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                }
            }
        };
        this.I = new MediaPlayer.OnPreparedListener() { // from class: com.jirbo.adcolony.e.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) {
                e.this.o = 2;
                e eVar = e.this;
                e eVar2 = e.this;
                e.this.E = true;
                eVar2.D = true;
                eVar.C = true;
                if (e.this.y != null) {
                    e.this.y.onPrepared(e.this.r);
                }
                if (e.this.w != null) {
                    e.this.w.setEnabled(true);
                }
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                int i2 = e.this.B;
                if (i2 != 0) {
                    e.this.seekTo(i2);
                }
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                    if (e.this.u == e.this.s && e.this.v == e.this.t) {
                        if (e.this.p == 3) {
                            e.this.start();
                            if (e.this.w != null) {
                                e.this.w.show();
                                return;
                            }
                            return;
                        }
                        if (!e.this.isPlaying()) {
                            if ((i2 != 0 || e.this.getCurrentPosition() > 0) && e.this.w != null) {
                                e.this.w.show(0);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (e.this.p == 3) {
                    e.this.start();
                }
            }
        };
        this.K = new MediaPlayer.OnCompletionListener() { // from class: com.jirbo.adcolony.e.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                e.this.o = 5;
                e.this.p = 5;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.x != null) {
                    e.this.x.onCompletion(e.this.r);
                }
            }
        };
        this.L = new MediaPlayer.OnErrorListener() { // from class: com.jirbo.adcolony.e.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                String str;
                Log.d(e.this.a, "Error: " + framework_err + "," + impl_err);
                e.this.o = -1;
                e.this.p = -1;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if ((e.this.A == null || !e.this.A.onError(e.this.r, framework_err, impl_err)) && e.this.getWindowToken() != null) {
                    e.this.b().getResources();
                    if (framework_err == 200) {
                        str = "Invalid progressive playback";
                    } else {
                        str = "Unknown error";
                    }
                    new AlertDialog.Builder(e.this.b()).setTitle("ERROR").setMessage(str).setPositiveButton("OKAY", new DialogInterface.OnClickListener() { // from class: com.jirbo.adcolony.e.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (e.this.x != null) {
                                e.this.x.onCompletion(e.this.r);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.M = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.jirbo.adcolony.e.5
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                e.this.z = percent;
            }
        };
        this.J = new SurfaceHolder.Callback() { // from class: com.jirbo.adcolony.e.6
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h2) {
                e.this.u = w;
                e.this.v = h2;
                boolean z2 = e.this.p == 3;
                boolean z22 = e.this.s == w && e.this.t == h2;
                if (e.this.r != null && z2 && z22) {
                    if (e.this.B != 0) {
                        e.this.seekTo(e.this.B);
                    }
                    e.this.start();
                    if (e.this.w != null) {
                        e.this.w.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
                e.this.q = holder;
                if (e.this.r == null || e.this.o != 6 || e.this.p != 7) {
                    e.this.f();
                } else {
                    e.this.r.setDisplay(e.this.q);
                    e.this.d();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                e.this.q = null;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.o != 6) {
                    e.this.a(true);
                }
            }
        };
        this.F = z;
        e();
    }

    public e(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        e();
    }

    public e(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.a = "ADCCustomVideoView";
        this.o = 0;
        this.p = 0;
        this.q = null;
        this.r = null;
        this.H = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.jirbo.adcolony.e.1
            @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                }
            }
        };
        this.I = new MediaPlayer.OnPreparedListener() { // from class: com.jirbo.adcolony.e.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) {
                e.this.o = 2;
                e eVar = e.this;
                e eVar2 = e.this;
                e.this.E = true;
                eVar2.D = true;
                eVar.C = true;
                if (e.this.y != null) {
                    e.this.y.onPrepared(e.this.r);
                }
                if (e.this.w != null) {
                    e.this.w.setEnabled(true);
                }
                e.this.s = mp.getVideoWidth();
                e.this.t = mp.getVideoHeight();
                int i22 = e.this.B;
                if (i22 != 0) {
                    e.this.seekTo(i22);
                }
                if (e.this.s != 0 && e.this.t != 0) {
                    e.this.getHolder().setFixedSize(e.this.s, e.this.t);
                    if (e.this.u == e.this.s && e.this.v == e.this.t) {
                        if (e.this.p == 3) {
                            e.this.start();
                            if (e.this.w != null) {
                                e.this.w.show();
                                return;
                            }
                            return;
                        }
                        if (!e.this.isPlaying()) {
                            if ((i22 != 0 || e.this.getCurrentPosition() > 0) && e.this.w != null) {
                                e.this.w.show(0);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (e.this.p == 3) {
                    e.this.start();
                }
            }
        };
        this.K = new MediaPlayer.OnCompletionListener() { // from class: com.jirbo.adcolony.e.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                e.this.o = 5;
                e.this.p = 5;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.x != null) {
                    e.this.x.onCompletion(e.this.r);
                }
            }
        };
        this.L = new MediaPlayer.OnErrorListener() { // from class: com.jirbo.adcolony.e.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                String str;
                Log.d(e.this.a, "Error: " + framework_err + "," + impl_err);
                e.this.o = -1;
                e.this.p = -1;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if ((e.this.A == null || !e.this.A.onError(e.this.r, framework_err, impl_err)) && e.this.getWindowToken() != null) {
                    e.this.b().getResources();
                    if (framework_err == 200) {
                        str = "Invalid progressive playback";
                    } else {
                        str = "Unknown error";
                    }
                    new AlertDialog.Builder(e.this.b()).setTitle("ERROR").setMessage(str).setPositiveButton("OKAY", new DialogInterface.OnClickListener() { // from class: com.jirbo.adcolony.e.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (e.this.x != null) {
                                e.this.x.onCompletion(e.this.r);
                            }
                        }
                    }).setCancelable(false).show();
                }
                return true;
            }
        };
        this.M = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.jirbo.adcolony.e.5
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                e.this.z = percent;
            }
        };
        this.J = new SurfaceHolder.Callback() { // from class: com.jirbo.adcolony.e.6
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h2) {
                e.this.u = w;
                e.this.v = h2;
                boolean z2 = e.this.p == 3;
                boolean z22 = e.this.s == w && e.this.t == h2;
                if (e.this.r != null && z2 && z22) {
                    if (e.this.B != 0) {
                        e.this.seekTo(e.this.B);
                    }
                    e.this.start();
                    if (e.this.w != null) {
                        e.this.w.show();
                    }
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
                e.this.q = holder;
                if (e.this.r == null || e.this.o != 6 || e.this.p != 7) {
                    e.this.f();
                } else {
                    e.this.r.setDisplay(e.this.q);
                    e.this.d();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                e.this.q = null;
                if (e.this.w != null) {
                    e.this.w.hide();
                }
                if (e.this.o != 6) {
                    e.this.a(true);
                }
            }
        };
        e();
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = getDefaultSize(this.s, widthMeasureSpec);
        int defaultSize2 = getDefaultSize(this.t, heightMeasureSpec);
        if (this.s > 0 && this.t > 0) {
            if (this.s * defaultSize2 > this.t * defaultSize) {
                defaultSize2 = (this.t * defaultSize) / this.s;
            } else if (this.s * defaultSize2 < this.t * defaultSize) {
                defaultSize = (this.s * defaultSize2) / this.t;
            }
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }

    public int a(int i2, int i3) {
        int mode = View.MeasureSpec.getMode(i3);
        int size = View.MeasureSpec.getSize(i3);
        switch (mode) {
            case Integer.MIN_VALUE:
                return Math.min(i2, size);
            case 0:
            default:
                return i2;
            case 1073741824:
                return size;
        }
    }

    private void e() {
        this.s = 0;
        this.t = 0;
        getHolder().addCallback(this.J);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        if (this.F) {
            requestFocus();
        }
        this.o = 0;
        this.p = 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getAudioSessionId() {
        return 0;
    }

    public void a(String str) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        a(Uri.parse(str));
    }

    public void a(FileDescriptor fileDescriptor) {
        this.f224c = fileDescriptor;
        this.B = 0;
        f();
        requestLayout();
        invalidate();
    }

    public void a(Uri uri) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        this.b = uri;
        this.B = 0;
        f();
        requestLayout();
        invalidate();
    }

    public void a() {
        if (this.r != null) {
            this.r.stop();
            this.r.release();
            this.r = null;
            this.o = 0;
            this.p = 0;
        }
    }

    Activity b() {
        return AdColony.activity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if ((this.b != null || this.f224c != null) && this.q != null) {
            Intent intent = new Intent("com.android.music.musicservicecommand");
            intent.putExtra("command", "pause");
            b().sendBroadcast(intent);
            a(false);
            try {
                this.r = new MediaPlayer();
                this.r.setOnPreparedListener(this.I);
                this.r.setOnVideoSizeChangedListener(this.H);
                this.d = -1;
                this.r.setOnCompletionListener(this.K);
                this.r.setOnErrorListener(this.L);
                this.r.setOnBufferingUpdateListener(this.M);
                this.z = 0;
                if (this.b != null) {
                    this.r.setDataSource(b(), this.b);
                } else {
                    this.r.setDataSource(this.f224c);
                }
                this.r.setDisplay(this.q);
                this.r.setAudioStreamType(3);
                this.r.setScreenOnWhilePlaying(true);
                this.r.prepare();
                this.o = 1;
                g();
            } catch (IOException e2) {
                if (this.b != null) {
                    Log.w(this.a, "Unable to open content: " + this.b, e2);
                } else {
                    Log.w(this.a, "Unable to open content");
                }
                this.o = -1;
                this.p = -1;
                this.L.onError(this.r, 1, 0);
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                if (this.b != null) {
                    Log.w(this.a, "Unable to open content: " + this.b, e3);
                } else {
                    Log.w(this.a, "Unable to open content");
                }
                this.o = -1;
                this.p = -1;
                this.L.onError(this.r, 1, 0);
                e3.printStackTrace();
            }
        }
    }

    public void a(MediaController mediaController) {
        if (this.w != null) {
            this.w.hide();
        }
        this.w = mediaController;
        g();
    }

    private void g() {
        if (this.r != null && this.w != null) {
            this.w.setMediaPlayer(this);
            this.w.setAnchorView(getParent() instanceof View ? (View) getParent() : this);
            this.w.setEnabled(i());
        }
    }

    public void a(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.y = onPreparedListener;
    }

    public void a(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.x = onCompletionListener;
    }

    public void a(MediaPlayer.OnErrorListener onErrorListener) {
        this.A = onErrorListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z) {
        if (this.r != null) {
            this.r.reset();
            this.r.release();
            this.r = null;
            this.o = 0;
            if (z) {
                this.p = 0;
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (i() && this.w != null) {
            h();
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        if (i() && this.w != null) {
            h();
            return false;
        }
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (i() && z && this.w != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.r.isPlaying()) {
                    pause();
                    this.w.show();
                    return true;
                }
                start();
                this.w.hide();
                return true;
            }
            if (keyCode == 86 && this.r.isPlaying()) {
                pause();
                this.w.show();
            } else {
                h();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void h() {
        if (this.w.isShowing()) {
            this.w.hide();
        } else {
            this.w.show();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() {
        if (i()) {
            this.r.start();
            this.o = 3;
        }
        this.p = 3;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() {
        if (i() && this.r.isPlaying()) {
            this.r.pause();
            this.o = 4;
        }
        this.p = 4;
    }

    public void c() throws IllegalStateException {
        if (i()) {
            this.r.stop();
            this.G = this.o;
            this.o = 6;
            this.p = 6;
        }
    }

    public void d() throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (this.q == null && this.o == 6) {
            this.p = 7;
            return;
        }
        if (this.r != null && this.o == 6) {
            this.r.start();
            this.o = this.G;
            this.p = this.G;
        } else if (this.o == 8) {
            f();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        if (i()) {
            if (this.d > 0) {
                return this.d;
            }
            this.d = this.r.getDuration();
            return this.d;
        }
        this.d = -1;
        return this.d;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (i()) {
            return this.r.getCurrentPosition();
        }
        return 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int msec) {
        if (i()) {
            this.r.seekTo(msec);
            this.B = 0;
        } else {
            this.B = msec;
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        return i() && this.r.isPlaying();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.r != null) {
            return this.z;
        }
        return 0;
    }

    private boolean i() {
        return (this.r == null || this.o == -1 || this.o == 0 || this.o == 1) ? false : true;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        return this.C;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return this.D;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return this.E;
    }
}
