package com.idtinc.maingame.sublayout0;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Tool_2_SelectListBackViewUnit {
    private float LISTBACKVIEW_HEIGHT;
    private float LISTBACKVIEW_OFFSET_X;
    private float LISTBACKVIEW_OFFSET_Y;
    private float LISTBACKVIEW_WIDTH;
    private float LISTVIEW_HEIGHT;
    private float LISTVIEW_OFFSET_X;
    private float LISTVIEW_OFFSET_Y;
    private float LISTVIEW_WIDTH;
    private float TOOL_BUTTON_SPACE_Y = 49.0f;
    protected AppDelegate appDelegate;
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
    private MyDraw myDraw;
    Tool_2_SelectListUnit tool_2_SelectListUnit;
    private float zoomRate;

    public Tool_2_SelectListBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, Tool_2_SelectListUnit _tool_2_SelectListUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.LISTBACKVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.LISTBACKVIEW_OFFSET_Y = 4.0f;
        this.LISTBACKVIEW_WIDTH = 320.0f;
        this.LISTBACKVIEW_HEIGHT = 138.0f;
        this.listBackViewColor0 = -16;
        this.listBackViewStrokeWidth1 = 2.0f;
        this.listBackViewColor1 = -7576502;
        this.listBackViewStrokeWidth2 = 2.0f;
        this.listBackViewColor2 = -16;
        this.listBackViewStrokeWidth3 = 1.0f;
        this.listBackViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.listBackViewRadius = 20.0f;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + 13.0f;
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + 35.0f + 17.0f;
        this.LISTVIEW_WIDTH = 270.0f;
        this.LISTVIEW_HEIGHT = 240.0f;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.tool_2_SelectListUnit = _tool_2_SelectListUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.LISTBACKVIEW_OFFSET_X = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        if (!this.appDelegate.isRetina4) {
            this.LISTBACKVIEW_OFFSET_Y = 40.0f * this.zoomRate;
        } else {
            this.LISTBACKVIEW_OFFSET_Y = 84.0f * this.zoomRate;
        }
        this.LISTBACKVIEW_WIDTH = 320.0f * this.zoomRate;
        this.LISTBACKVIEW_HEIGHT = 138.0f * this.zoomRate;
        this.listBackViewColor0 = -16;
        this.listBackViewStrokeWidth1 = this.zoomRate * 2.0f;
        this.listBackViewColor1 = -7576502;
        this.listBackViewStrokeWidth2 = this.zoomRate * 2.0f;
        this.listBackViewColor2 = -16;
        this.listBackViewStrokeWidth3 = this.zoomRate * 1.0f;
        this.listBackViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.listBackViewRadius = this.zoomRate * 20.0f;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (8.0f * this.zoomRate);
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (45.0f * this.zoomRate);
        this.LISTVIEW_WIDTH = 290.0f * this.zoomRate;
        this.LISTVIEW_HEIGHT = 210.0f * this.zoomRate;
        this.myDraw = new MyDraw();
    }

    public void resize() {
        this.LISTBACKVIEW_OFFSET_X = this.tool_2_SelectListUnit.LISTBACKVIEW_OFFSET_X;
        this.LISTBACKVIEW_OFFSET_Y = this.tool_2_SelectListUnit.LISTBACKVIEW_OFFSET_Y;
        this.LISTBACKVIEW_WIDTH = this.tool_2_SelectListUnit.LISTBACKVIEW_WIDTH;
        this.LISTBACKVIEW_HEIGHT = this.tool_2_SelectListUnit.LISTBACKVIEW_HEIGHT;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        bitmapPaint.setColor(1711276032);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        MyDraw.drawStrokeRect(canvas, this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.LISTBACKVIEW_WIDTH, this.LISTBACKVIEW_HEIGHT, this.listBackViewColor0, this.listBackViewStrokeWidth1, this.listBackViewColor1, this.listBackViewStrokeWidth2, this.listBackViewColor2, this.listBackViewStrokeWidth3, this.listBackViewColor3, this.listBackViewRadius);
    }

    public void onDestroy() {
        this.myDraw = null;
        this.tool_2_SelectListUnit = null;
        this.appDelegate = null;
    }
}
