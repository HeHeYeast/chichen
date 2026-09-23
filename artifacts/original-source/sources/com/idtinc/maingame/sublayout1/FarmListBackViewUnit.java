package com.idtinc.maingame.sublayout1;

import android.graphics.Canvas;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmListBackViewUnit {
    private float LISTBACKVIEW_HEIGHT;
    private float LISTBACKVIEW_OFFSET_X;
    private float LISTBACKVIEW_OFFSET_Y;
    private float LISTBACKVIEW_WIDTH;
    private float LISTVIEW_HEIGHT;
    private float LISTVIEW_OFFSET_X;
    private float LISTVIEW_OFFSET_Y;
    private float LISTVIEW_WIDTH;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public int listBackViewColor0;
    public int listBackViewColor1;
    public int listBackViewColor2;
    public int listBackViewColor3;
    public float listBackViewRadius;
    public float listBackViewStrokeWidth1;
    public float listBackViewStrokeWidth2;
    public float listBackViewStrokeWidth3;
    private MyDraw myDraw = new MyDraw();
    private float offsetX;
    private float offsetY;
    private float zoomRate;

    public FarmListBackViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.LISTBACKVIEW_OFFSET_X = 12.0f;
        this.LISTBACKVIEW_OFFSET_Y = 60.0f;
        this.LISTBACKVIEW_WIDTH = 296.0f;
        this.LISTBACKVIEW_HEIGHT = 304.0f;
        this.listBackViewColor0 = -200082;
        this.listBackViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor1 = 0;
        this.listBackViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor2 = 0;
        this.listBackViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor3 = 0;
        this.listBackViewRadius = 8.0f;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + 13.0f;
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + 35.0f + 17.0f;
        this.LISTVIEW_WIDTH = 270.0f;
        this.LISTVIEW_HEIGHT = 240.0f;
        this.appDelegate = _appDelegate;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.LISTBACKVIEW_OFFSET_X = this.offsetX + (this.zoomRate * 5.0f);
        this.LISTBACKVIEW_OFFSET_Y = this.offsetY + (this.zoomRate * 5.0f);
        this.LISTBACKVIEW_WIDTH = 306.0f * this.zoomRate;
        this.LISTBACKVIEW_HEIGHT = 364.0f * this.zoomRate;
        this.listBackViewColor0 = -200082;
        this.listBackViewStrokeWidth1 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor1 = 0;
        this.listBackViewStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor2 = 0;
        this.listBackViewStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor3 = 0;
        this.listBackViewRadius = this.zoomRate * 8.0f;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (this.zoomRate * 8.0f);
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (45.0f * this.zoomRate);
        this.LISTVIEW_WIDTH = 290.0f * this.zoomRate;
        this.LISTVIEW_HEIGHT = 280.0f * this.zoomRate;
    }

    public void gameDraw(Canvas canvas) {
        MyDraw.drawShadowRect(canvas, this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.LISTBACKVIEW_WIDTH, this.LISTBACKVIEW_HEIGHT, Integer.MIN_VALUE, 2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.listBackViewRadius);
        MyDraw.drawStrokeRect(canvas, this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.LISTBACKVIEW_WIDTH, this.LISTBACKVIEW_HEIGHT, this.listBackViewColor0, this.listBackViewStrokeWidth1, this.listBackViewColor1, this.listBackViewStrokeWidth2, this.listBackViewColor2, this.listBackViewStrokeWidth3, this.listBackViewColor3, this.listBackViewRadius);
        MyDraw.drawStrokeRect(canvas, this.LISTVIEW_OFFSET_X, this.LISTVIEW_OFFSET_Y, this.LISTVIEW_WIDTH, this.LISTVIEW_HEIGHT, -16, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.listBackViewRadius);
    }

    public void onDestroy() {
        this.myDraw = null;
        this.appDelegate = null;
    }
}
