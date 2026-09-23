package com.idtinc.ckchickandduck;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class SavesCheckViewUnit {
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private int loadingCnt;
    private float loadingLabelOffsetX;
    private float loadingLabelOffsetY;
    private String loadingLabelString;
    private Typeface loadingLabelTypeface;
    private MyDraw myDraw;
    private float zoomRate;
    private float loadingLabelFontSize = 32.0f;
    private float loadingLabelStroke1Width = 6.0f;
    private float loadingLabelStroke2Width = 10.0f;

    public SavesCheckViewUnit(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.loadingCnt = 0;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.loadingLabelString = "";
        this.loadingLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.loadingCnt = 0;
        this.loadingLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.loadingLabelFontSize *= this.zoomRate;
        this.loadingLabelStroke1Width *= this.zoomRate;
        this.loadingLabelStroke2Width *= this.zoomRate;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.loadingLabelTypeface);
        newPaint.setTextSize(this.loadingLabelFontSize);
        this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + ".";
        this.loadingLabelOffsetX = (this.finalWidth - newPaint.measureText(String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "..")) / 2.0f;
        if (!this.appDelegate.isRetina4) {
            this.loadingLabelOffsetY = (210.0f * this.zoomRate) + this.loadingLabelFontSize;
        } else {
            this.loadingLabelOffsetY = (254.0f * this.zoomRate) + this.loadingLabelFontSize;
        }
        this.myDraw = new MyDraw();
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.appDelegate != null) {
            this.loadingCnt++;
            this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + ".";
            if (this.loadingCnt < 20) {
                this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + ".";
            } else if (this.loadingCnt < 40) {
                this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "..";
            } else if (this.loadingCnt < 60) {
                this.loadingLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Loading)) + "...";
            } else {
                this.loadingCnt = 0;
            }
            MyDraw.drawStrokeText(canvas, this.loadingLabelOffsetX, this.loadingLabelOffsetY, this.loadingLabelTypeface, this.loadingLabelString, this.loadingLabelFontSize, -6106, this.loadingLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.loadingLabelStroke2Width, -16);
        }
    }

    public void onDestroy() {
        this.appDelegate = null;
    }
}
