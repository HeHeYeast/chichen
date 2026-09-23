package com.idtinc.maingame.sublayout3;

import android.content.Context;
import android.graphics.Canvas;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HelpFrontViewUnit {
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

    public HelpFrontViewUnit(Context context, float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.BIGBACKVIEW_OFFSET_X = 25.0f;
        this.BIGBACKVIEW_OFFSET_Y = 40.0f;
        this.BIGBACKVIEW_WIDTH = 270.0f;
        this.BIGBACKVIEW_HEIGHT = 415.0f - this.BIGBACKVIEW_OFFSET_Y;
        this.backViewColor0 = -7576502;
        this.backViewStrokeWidth1 = 2.0f;
        this.backViewColor1 = -7576502;
        this.backViewStrokeWidth2 = 2.0f;
        this.backViewColor2 = -16;
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
        this.backViewColor0 = -1;
        this.backViewStrokeWidth1 = this.zoomRate * 2.0f;
        this.backViewColor1 = -407644;
        this.backViewStrokeWidth2 = 4.0f * this.zoomRate;
        this.backViewColor2 = -16;
        this.backViewStrokeWidth3 = 3.0f * this.zoomRate;
        this.backViewColor3 = -407644;
        this.backViewRadius = this.zoomRate * 10.0f;
        this.myDraw = new MyDraw();
    }

    public void gameDraw(Canvas canvas) {
        MyDraw.drawOnlyStrokeRect(canvas, this.BIGBACKVIEW_OFFSET_X, this.BIGBACKVIEW_OFFSET_Y, this.BIGBACKVIEW_WIDTH, this.BIGBACKVIEW_HEIGHT, 0, this.backViewStrokeWidth1, this.backViewColor1, this.backViewStrokeWidth2, this.backViewColor2, this.backViewStrokeWidth3, this.backViewColor3, this.backViewRadius);
    }

    public void onDestroy() {
        this.myDraw = null;
        this.appDelegate = null;
    }
}
