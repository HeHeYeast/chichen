package com.idtinc.ckchickandduck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AppMainSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private AppDelegate appDelegate;
    private AppMainActivity appMainActivity;
    private boolean drawFlag;
    private boolean drawingFlag;
    private float finalHeight;
    private float finalWidth;
    private SurfaceHolder surfaceHolder;
    private float zoomRate;

    public AppMainSurfaceView(Context context, float _finalwidth, float _finalheight, float _zoomrate, AppMainActivity _appMainActivity) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.drawFlag = false;
        this.drawingFlag = false;
        this.surfaceHolder = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.appMainActivity = _appMainActivity;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        setOnTouchListener(this);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        setFocusable(true);
        this.drawFlag = false;
        this.drawingFlag = false;
        startDraw();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void startDraw() {
        if (!this.drawFlag) {
            this.drawFlag = true;
            AnimThread animThread = new AnimThread();
            Thread thread = new Thread(animThread);
            thread.start();
        }
    }

    public void stopDraw() {
        this.drawFlag = false;
    }

    class AnimThread implements Runnable {
        public AnimThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (AppMainSurfaceView.this.drawFlag) {
                long startTime = System.currentTimeMillis();
                try {
                    AppMainSurfaceView.this.runDraw();
                } catch (Exception e) {
                }
                long endTime = System.currentTimeMillis();
                if (AppMainSurfaceView.this.appDelegate != null) {
                    float nowDrawSleepSeconds = AppMainSurfaceView.this.appDelegate.drawSleepSeconds;
                    for (int diffTime = (int) (endTime - startTime); diffTime <= nowDrawSleepSeconds; diffTime = (int) (System.currentTimeMillis() - startTime)) {
                    }
                }
                Thread.yield();
            }
        }
    }

    public void runDraw() {
        if (this.appDelegate != null && !this.appDelegate.getOnPauseF() && getVisibility() == 0 && !this.drawingFlag && this.surfaceHolder != null) {
            this.drawingFlag = true;
            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    doDraw(canvas);
                }
            } catch (Exception e) {
            }
            if (canvas != null) {
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
            this.drawingFlag = false;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View _view, MotionEvent _event) {
        if (this.appDelegate != null && this.appMainActivity != null && (this.appMainActivity.onlineGameViewController == null || this.appMainActivity.onlineGameViewController.getVisibility() != 0)) {
            if (this.appDelegate.getNowStatus() == -1) {
                if (this.appMainActivity.mainMenuViewController != null) {
                    this.appMainActivity.mainMenuViewController.gameOnTouch(_event);
                }
            } else if (this.appDelegate.getNowStatus() == 0) {
                if (this.appMainActivity.savesCheckViewController != null) {
                    this.appMainActivity.savesCheckViewController.gameOnTouch(_event);
                }
            } else if (this.appDelegate.getNowStatus() == 1) {
                if (this.appMainActivity.mainGameViewController != null) {
                    this.appMainActivity.mainGameViewController.gameOnTouch(_event);
                }
                Log.i("qqqqqqqqqqqqq", "tttouch");
            }
        }
        return true;
    }

    public void doDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        if (this.appDelegate != null && this.appMainActivity != null) {
            if (this.appDelegate.getNowStatus() == -1) {
                if (this.appMainActivity.mainMenuViewController != null) {
                    this.appMainActivity.mainMenuViewController.gameDraw(canvas);
                }
            } else if (this.appDelegate.getNowStatus() == 0) {
                if (this.appMainActivity.savesCheckViewController != null) {
                    this.appMainActivity.savesCheckViewController.gameDraw(canvas);
                }
            } else if (this.appDelegate.getNowStatus() == 1 && this.appMainActivity.mainGameViewController != null) {
                this.appMainActivity.mainGameViewController.gameDraw(canvas);
            }
        }
    }

    public Bitmap getScreenshot() {
        Bitmap returnBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        doDraw(canvas);
        return returnBitmap;
    }

    public void onDestroy() {
        stopDraw();
        if (this.surfaceHolder != null) {
            this.surfaceHolder.removeCallback(this);
            this.surfaceHolder = null;
        }
        this.appMainActivity = null;
        this.appDelegate = null;
    }
}
