package com.idtinc.maingame.sublayout0;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BottomBlockLayout {
    private AppDelegate appDelegate;
    private int blockImageCutHeight;
    private int blockImageCutOffsetX;
    private int blockImageCutOffsetY;
    private int blockImageCutWidth;
    private int blockImageHeight;
    private int blockImageOffsetX;
    private int blockImageOffsetY;
    private int blockImageWidth;
    private float blockViewHeight;
    private float blockViewOffsetX;
    private float blockViewOffsetY;
    private float blockViewWidth;
    private int finalHeight;
    private int finalWidth;
    private float frontRectStrokeWidth1;
    private float zoomRate;

    public BottomBlockLayout(int _finalwidth, int _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = 0;
        this.finalHeight = 0;
        this.zoomRate = 1.0f;
        this.blockViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.blockViewOffsetY = 430.0f;
        this.blockViewWidth = 320.0f;
        this.blockViewHeight = 50.0f;
        this.blockImageCutOffsetX = 0;
        this.blockImageCutOffsetY = 270;
        this.blockImageCutWidth = 320;
        this.blockImageCutHeight = 50;
        this.blockImageOffsetX = 0;
        this.blockImageOffsetY = 430;
        this.blockImageWidth = 320;
        this.blockImageHeight = 50;
        this.frontRectStrokeWidth1 = 2.0f;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.blockViewOffsetX = 1.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.blockViewOffsetY = this.zoomRate * 430.0f;
        } else {
            this.blockViewOffsetY = 518.0f * this.zoomRate;
        }
        this.blockViewWidth = this.blockViewOffsetX + (318.0f * this.zoomRate);
        this.blockViewHeight = this.blockViewOffsetY + (49.0f * this.zoomRate);
        this.blockImageCutOffsetX = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        this.blockImageCutOffsetY = (int) (270.0f * this.zoomRate);
        this.blockImageCutWidth = (int) (this.blockImageCutOffsetX + (this.zoomRate * 320.0f));
        this.blockImageCutHeight = (int) (this.blockImageCutOffsetY + (this.zoomRate * 50.0f));
        if (this.appDelegate != null && this.appDelegate.optionBackGroundBitmap != null) {
            float imageScale = this.finalWidth > 0 ? (this.zoomRate * this.appDelegate.optionBackGroundBitmap.getWidth()) / this.finalWidth : 1.0f;
            this.blockImageCutOffsetX = (int) (BitmapDescriptorFactory.HUE_RED * imageScale);
            this.blockImageCutOffsetY = (int) (270.0f * imageScale);
            this.blockImageCutWidth = this.blockImageCutOffsetX + this.appDelegate.optionBackGroundBitmap.getWidth();
            this.blockImageCutHeight = (int) (this.blockImageCutOffsetY + (50.0f * imageScale));
        }
        this.blockImageOffsetX = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        if (!this.appDelegate.isRetina4) {
            this.blockImageOffsetY = (int) (this.zoomRate * 430.0f);
        } else {
            this.blockImageOffsetY = (int) (518.0f * this.zoomRate);
        }
        this.blockImageWidth = (int) (this.blockImageOffsetX + (this.zoomRate * 320.0f));
        this.blockImageHeight = (int) (this.blockImageOffsetY + (this.zoomRate * 50.0f));
        this.frontRectStrokeWidth1 = 2.0f * this.zoomRate;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(-2248316);
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        canvas.drawRect(this.blockViewOffsetX, this.blockViewOffsetY, this.blockViewWidth, this.blockViewHeight, bitmapPaint);
        if (this.appDelegate != null && this.appDelegate.optionBackGroundBitmap != null) {
            bitmapPaint.setAlpha(128);
            canvas.drawBitmap(this.appDelegate.optionBackGroundBitmap, new Rect(this.blockImageCutOffsetX, this.blockImageCutOffsetY, this.blockImageCutWidth, this.blockImageCutHeight), new Rect(this.blockImageOffsetX, this.blockImageOffsetY, this.blockImageWidth, this.blockImageHeight), bitmapPaint);
        }
        bitmapPaint.setColor(-7576502);
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        bitmapPaint.setStrokeWidth(this.frontRectStrokeWidth1);
        bitmapPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(new RectF(this.blockViewOffsetX, this.blockViewOffsetY, this.blockViewWidth, this.blockViewHeight), BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, bitmapPaint);
    }

    public void onDestroy() {
        this.appDelegate = null;
    }
}
