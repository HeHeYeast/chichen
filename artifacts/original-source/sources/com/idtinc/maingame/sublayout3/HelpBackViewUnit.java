package com.idtinc.maingame.sublayout3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HelpBackViewUnit {
    private float BIGBACKVIEW_HEIGHT;
    private float BIGBACKVIEW_OFFSET_X;
    private float BIGBACKVIEW_OFFSET_Y;
    private float BIGBACKVIEW_WIDTH;
    private AppDelegate appDelegate;
    public int backViewColor0;
    public int backViewColor1;
    public int backViewColor2;
    public int backViewColor3;
    public float backViewRadius;
    public float backViewStrokeWidth1;
    public float backViewStrokeWidth2;
    public float backViewStrokeWidth3;
    private float finalHeight;
    private float finalWidth;
    private MyDraw myDraw;
    private float zoomRate;

    public HelpBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.BIGBACKVIEW_OFFSET_X = 25.0f;
        this.BIGBACKVIEW_OFFSET_Y = 40.0f;
        this.BIGBACKVIEW_WIDTH = 270.0f;
        this.BIGBACKVIEW_HEIGHT = 415.0f - this.BIGBACKVIEW_OFFSET_Y;
        this.backViewColor0 = -16;
        this.backViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor1 = 0;
        this.backViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor2 = 0;
        this.backViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor3 = 0;
        this.backViewRadius = 10.0f;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.BIGBACKVIEW_OFFSET_X = 21.0f * this.zoomRate;
        this.BIGBACKVIEW_OFFSET_Y = this.zoomRate * 40.0f;
        this.BIGBACKVIEW_WIDTH = 278.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.BIGBACKVIEW_HEIGHT = (this.zoomRate * 415.0f) - this.BIGBACKVIEW_OFFSET_Y;
        } else {
            this.BIGBACKVIEW_HEIGHT = (503.0f * this.zoomRate) - this.BIGBACKVIEW_OFFSET_Y;
        }
        this.backViewColor0 = -16;
        this.backViewStrokeWidth1 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backViewColor1 = 0;
        this.backViewStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backViewColor2 = 0;
        this.backViewStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backViewColor3 = 0;
        this.backViewRadius = this.zoomRate * 10.0f;
        this.myDraw = new MyDraw();
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(-3940609);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.appDelegate != null && this.appDelegate.optionBackGroundBitmap != null) {
            bitmapPaint.setAlpha(77);
            canvas.drawBitmap(this.appDelegate.optionBackGroundBitmap, new Rect(0, 0, this.appDelegate.optionBackGroundBitmap.getWidth(), this.appDelegate.optionBackGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
        }
        MyDraw.drawStrokeRect(canvas, this.BIGBACKVIEW_OFFSET_X, this.BIGBACKVIEW_OFFSET_Y, this.BIGBACKVIEW_WIDTH, this.BIGBACKVIEW_HEIGHT, this.backViewColor0, BitmapDescriptorFactory.HUE_RED, this.backViewColor1, BitmapDescriptorFactory.HUE_RED, this.backViewColor2, BitmapDescriptorFactory.HUE_RED, this.backViewColor3, this.backViewRadius);
    }

    public void onDestroy() {
        this.myDraw = null;
        this.appDelegate = null;
    }
}
