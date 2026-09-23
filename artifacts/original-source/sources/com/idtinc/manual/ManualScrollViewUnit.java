package com.idtinc.manual;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ManualScrollViewUnit extends View {
    private AppDelegate appDelegate;
    private Bitmap backGroundBitmap0;
    private Bitmap backGroundBitmap1;
    private float finalHeight;
    private float finalWidth;
    private String manualImagePathString;
    private short manualIndex;
    private MyDraw myDraw;
    public float offsetScrollY;
    private float offsetScrollYMax;
    private float originHeight;
    private float preScrollX;
    private float preScrollY;
    public float scrollBarHeight;
    public float scrollBarOffsetX;
    public float scrollBarOffsetY;
    public float scrollBarRadius;
    public float scrollBarWidth;
    private float zoomOriginHeight;
    private float zoomRate;

    public ManualScrollViewUnit(Context context, float _finalwidth, float _finalheight, float _zoomrate) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomOriginHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.preScrollX = -9999.0f;
        this.preScrollY = -9999.0f;
        this.scrollBarOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarWidth = 6.0f;
        this.scrollBarHeight = 60.0f;
        this.scrollBarRadius = 2.0f;
        this.manualIndex = (short) 0;
        this.manualImagePathString = "";
        this.appDelegate = null;
        this.backGroundBitmap0 = null;
        this.backGroundBitmap1 = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.finalWidth = _finalwidth;
        this.originHeight = _finalheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.zoomOriginHeight = (this.originHeight * 2.0f) / this.zoomRate;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.scrollBarWidth = this.zoomRate * 6.0f;
        this.scrollBarHeight = this.finalHeight;
        this.scrollBarOffsetX = this.finalWidth - this.scrollBarWidth;
        this.scrollBarOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarRadius = this.scrollBarWidth / 2.0f;
        this.manualIndex = (short) 0;
        this.manualImagePathString = "";
        clearBitmap();
        this.myDraw = new MyDraw();
    }

    public void changManualIndex(short _manualIndex) throws IOException {
        this.manualIndex = _manualIndex;
        this.manualImagePathString = "png/Manual/manual000_";
        this.finalHeight = 2410.0f * this.zoomRate;
        if (_manualIndex == 1) {
            this.manualImagePathString = "png/Manual/manual001_";
            this.finalHeight = 1150.0f * this.zoomRate;
        } else if (_manualIndex == 2) {
            this.manualImagePathString = "png/Manual/manual002_";
            this.finalHeight = 820.0f * this.zoomRate;
        }
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            this.manualImagePathString = String.valueOf(this.manualImagePathString) + "ja";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            this.manualImagePathString = String.valueOf(this.manualImagePathString) + "tw";
        } else if (languageString.equals("zh-CN")) {
            this.manualImagePathString = String.valueOf(this.manualImagePathString) + "cn";
        } else {
            this.manualImagePathString = String.valueOf(this.manualImagePathString) + "en";
        }
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.scrollBarHeight = this.originHeight;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        if (this.finalHeight > this.originHeight) {
            this.scrollBarHeight = this.originHeight * (this.originHeight / this.finalHeight);
            this.offsetScrollYMax = this.finalHeight - this.originHeight;
            Log.d("ManualScrollSurfaceView", "finalHeight:" + this.finalHeight);
        }
        this.scrollBarOffsetY = BitmapDescriptorFactory.HUE_RED;
        getImage();
        invalidate();
    }

    public void clearBitmap() {
        if (this.backGroundBitmap0 != null) {
            if (!this.backGroundBitmap0.isRecycled()) {
                this.backGroundBitmap0.recycle();
            }
            this.backGroundBitmap0 = null;
        }
        if (this.backGroundBitmap1 != null) {
            if (!this.backGroundBitmap1.isRecycled()) {
                this.backGroundBitmap1.recycle();
            }
            this.backGroundBitmap1 = null;
        }
    }

    public void getImage() throws IOException {
        InputStream inputStream;
        clearBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                String inputStreamString = this.manualImagePathString;
                if (this.manualIndex == 0) {
                    inputStream = asm.open(String.valueOf(inputStreamString) + "0.jpg");
                } else {
                    inputStream = asm.open(String.valueOf(inputStreamString) + ".jpg");
                }
                BufferedInputStream buf = new BufferedInputStream(inputStream);
                BitmapFactory.decodeStream(buf, null, opt);
                int scale = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
                opt2.inSampleSize = scale;
                this.backGroundBitmap0 = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            if (this.manualIndex == 0) {
                try {
                    InputStream inputStream2 = asm.open(String.valueOf(this.manualImagePathString) + "1.jpg");
                    BufferedInputStream buf2 = new BufferedInputStream(inputStream2);
                    BitmapFactory.decodeStream(buf2, null, opt);
                    int scale2 = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
                    opt2.inSampleSize = scale2;
                    this.backGroundBitmap1 = BitmapFactory.decodeStream(inputStream2, null, opt2);
                    inputStream2.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (getVisibility() == 0) {
            Log.d("ManualScrollSurfaceView", "onTouchEvent");
            if (event.getAction() == 1) {
                Log.d("StoreListScrollLayout02", "ACTION_UP   X=" + event.getX() + ", Y=  " + event.getY());
            } else if (event.getAction() == 0) {
                this.preScrollY = event.getY();
            } else if (event.getAction() == 2) {
                float addOffScrollY = this.preScrollY - event.getY();
                this.offsetScrollY += addOffScrollY;
                if (this.offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
                    this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
                } else if (this.offsetScrollY > this.offsetScrollYMax) {
                    this.offsetScrollY = this.offsetScrollYMax;
                }
                this.preScrollY = event.getY();
                invalidate();
                Log.d("StoreListScrollLayout02", "ACTION_MOVE   X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("StoreListScrollLayout02", "preScrollY =  " + this.preScrollY);
                Log.d("StoreListScrollLayout02", "offsetScrollY =  " + this.offsetScrollY);
            }
        }
        return true;
    }

    public void setPreScrollPoint(float _preScrollX, float _preScrollY) {
        this.preScrollX = _preScrollX;
        this.preScrollY = _preScrollY;
    }

    public float getPreScrollX() {
        return this.preScrollX;
    }

    public float getPreScrollY() {
        return this.preScrollY;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        bitmapPaint.setColor(-1);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.backGroundBitmap0 != null) {
            float zoomOffsetScrollY = (2.0f * this.offsetScrollY) / this.zoomRate;
            Log.d("ManualScrollSurfaceView:" + zoomOffsetScrollY, "draw");
            if (this.manualIndex != 0) {
                canvas.drawBitmap(this.backGroundBitmap0, new Rect(0, (int) zoomOffsetScrollY, this.backGroundBitmap0.getWidth(), (int) (this.zoomOriginHeight + zoomOffsetScrollY)), new Rect(0, 0, (int) this.finalWidth, (int) this.originHeight), bitmapPaint);
            } else if (zoomOffsetScrollY > 4000.0f) {
                if (this.backGroundBitmap1 != null) {
                    canvas.drawBitmap(this.backGroundBitmap1, new Rect(0, (int) zoomOffsetScrollY, this.backGroundBitmap1.getWidth(), (int) (this.zoomOriginHeight + zoomOffsetScrollY)), new Rect(0, 0, (int) this.finalWidth, (int) this.originHeight), bitmapPaint);
                }
            } else if (this.zoomOriginHeight + zoomOffsetScrollY > 4000.0f) {
                float bitmap0Height = 4000.0f - zoomOffsetScrollY;
                float drawBitmap0Height = (this.zoomRate * bitmap0Height) / 2.0f;
                canvas.drawBitmap(this.backGroundBitmap0, new Rect(0, (int) zoomOffsetScrollY, this.backGroundBitmap0.getWidth(), (int) (zoomOffsetScrollY + bitmap0Height)), new Rect(0, 0, (int) this.finalWidth, (int) drawBitmap0Height), bitmapPaint);
                if (this.backGroundBitmap1 != null) {
                    float bitmap1Height = this.zoomOriginHeight - bitmap0Height;
                    canvas.drawBitmap(this.backGroundBitmap1, new Rect(0, 0, this.backGroundBitmap1.getWidth(), (int) bitmap1Height), new Rect(0, (int) drawBitmap0Height, (int) this.finalWidth, (int) (((this.zoomRate * bitmap1Height) / 2.0f) + drawBitmap0Height)), bitmapPaint);
                }
            } else {
                canvas.drawBitmap(this.backGroundBitmap0, new Rect(0, (int) zoomOffsetScrollY, this.backGroundBitmap0.getWidth(), (int) (this.zoomOriginHeight + zoomOffsetScrollY)), new Rect(0, 0, (int) this.finalWidth, (int) this.originHeight), bitmapPaint);
            }
        }
        this.scrollBarOffsetY = (this.offsetScrollY * this.scrollBarHeight) / this.originHeight;
        MyDraw.drawStrokeRect(canvas, this.scrollBarOffsetX, this.scrollBarOffsetY, this.scrollBarWidth, this.scrollBarHeight, 1711276032, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.scrollBarRadius);
    }

    public void onDestroy() {
        this.myDraw = null;
        clearBitmap();
        this.appDelegate = null;
    }
}
