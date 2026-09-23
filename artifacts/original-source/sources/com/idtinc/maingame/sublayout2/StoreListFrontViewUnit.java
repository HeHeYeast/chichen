package com.idtinc.maingame.sublayout2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StoreListFrontViewUnit {
    private float LISTBACKVIEW_HEIGHT;
    private float LISTBACKVIEW_OFFSET_X;
    private float LISTBACKVIEW_OFFSET_Y;
    private float LISTBACKVIEW_WIDTH;
    private float LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_X;
    private float LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y;
    private float LISTVIEW_FASTSCROLLVIEW_HEIGHT;
    private float LISTVIEW_FASTSCROLLVIEW_OFFSET_X;
    private float LISTVIEW_FASTSCROLLVIEW_OFFSET_Y;
    private float LISTVIEW_FASTSCROLLVIEW_RADIUS;
    private float LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH;
    private float LISTVIEW_FASTSCROLLVIEW_WIDTH;
    private float LISTVIEW_HEIGHT;
    private float LISTVIEW_OFFSET_X;
    private float LISTVIEW_OFFSET_Y;
    private float LISTVIEW_WIDTH;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public int listBackViewColor0;
    public float listBlockViewRadius;
    public float listFronViewStrokeWidth3;
    public float listFrontViewRadius;
    public float listFrontViewRadius1;
    public float listViewFastScrollDragViewHeight;
    public float listViewFastScrollDragViewOffsetX;
    public float listViewFastScrollDragViewOffsetY;
    public float listViewFastScrollDragViewWidth;
    private MyDraw myDraw;
    private StoreUnit storeUnit;
    private float zoomRate;

    public StoreListFrontViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, StoreUnit _storeUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.LISTBACKVIEW_OFFSET_X = 12.0f;
        this.LISTBACKVIEW_OFFSET_Y = 60.0f;
        this.LISTBACKVIEW_WIDTH = 296.0f;
        this.LISTBACKVIEW_HEIGHT = 304.0f;
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X = 290.0f;
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y = 47.0f;
        this.LISTVIEW_FASTSCROLLVIEW_WIDTH = 8.0f;
        this.LISTVIEW_FASTSCROLLVIEW_HEIGHT = 206.0f;
        this.LISTVIEW_FASTSCROLLVIEW_RADIUS = 4.0f;
        this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH = 2.0f;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.listBackViewColor0 = -3487;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + 13.0f;
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + 35.0f + 17.0f;
        this.LISTVIEW_WIDTH = 270.0f;
        this.LISTVIEW_HEIGHT = 240.0f;
        this.listFrontViewRadius = 8.0f;
        this.listFrontViewRadius1 = this.listFrontViewRadius / 2.6f;
        this.listFronViewStrokeWidth3 = 2.0f;
        this.listBlockViewRadius = 4.0f;
        this.listViewFastScrollDragViewWidth = 26.0f;
        this.listViewFastScrollDragViewHeight = 26.0f;
        this.listViewFastScrollDragViewOffsetX = 290.0f;
        this.listViewFastScrollDragViewOffsetY = 47.0f;
        this.appDelegate = _appDelegate;
        this.storeUnit = _storeUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.LISTBACKVIEW_OFFSET_X = _offsetX;
        this.LISTBACKVIEW_OFFSET_Y = _offsetY;
        this.LISTBACKVIEW_WIDTH = 296.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTBACKVIEW_HEIGHT = 304.0f * this.zoomRate;
        } else {
            this.LISTBACKVIEW_HEIGHT = 364.0f * this.zoomRate;
        }
        this.listBackViewColor0 = -3487;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (13.0f * this.zoomRate);
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (52.0f * this.zoomRate);
        this.LISTVIEW_WIDTH = 270.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTVIEW_HEIGHT = 240.0f * this.zoomRate;
        } else {
            this.LISTVIEW_HEIGHT = 300.0f * this.zoomRate;
        }
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (275.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (54.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLVIEW_WIDTH = this.zoomRate * 8.0f;
        if (!this.appDelegate.isRetina4) {
            this.LISTVIEW_FASTSCROLLVIEW_HEIGHT = 236.0f * this.zoomRate;
        } else {
            this.LISTVIEW_FASTSCROLLVIEW_HEIGHT = 296.0f * this.zoomRate;
        }
        this.LISTVIEW_FASTSCROLLVIEW_RADIUS = this.zoomRate * 4.0f;
        this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH = this.zoomRate * 2.0f;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_X = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X + (3.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y - (13.0f * this.zoomRate);
        this.listViewFastScrollDragViewWidth = this.zoomRate * 26.0f;
        this.listViewFastScrollDragViewHeight = this.zoomRate * 26.0f;
        this.listViewFastScrollDragViewOffsetX = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X - (10.0f * this.zoomRate);
        this.listViewFastScrollDragViewOffsetY = this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y;
        this.listFrontViewRadius = this.zoomRate * 8.0f;
        this.listFrontViewRadius1 = this.listFrontViewRadius / 2.6f;
        this.listFronViewStrokeWidth3 = this.zoomRate * 2.0f;
        this.listBlockViewRadius = this.listFrontViewRadius / 2.0f;
        this.myDraw = new MyDraw();
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("FarmListFrontkView", "onTouchEvent");
        if ((event.getAction() != 0 && event.getAction() != 2) || event.getX() < this.listViewFastScrollDragViewOffsetX || event.getY() < this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y - (20.0f * this.zoomRate) || event.getY() >= this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y + this.LISTVIEW_FASTSCROLLVIEW_HEIGHT + (30.0f * this.zoomRate)) {
            return false;
        }
        float nowHight = this.LISTVIEW_FASTSCROLLVIEW_HEIGHT;
        if (nowHight < 1.0f) {
            nowHight = 1.0f;
        }
        float nowOffsetY = event.getY() - this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y;
        if (nowOffsetY < BitmapDescriptorFactory.HUE_RED) {
            nowOffsetY = BitmapDescriptorFactory.HUE_RED;
        }
        if (nowOffsetY > nowHight) {
            nowOffsetY = nowHight;
        }
        if (this.storeUnit != null) {
            this.storeUnit.doStoreListScrollViewUnitScroll(nowOffsetY / nowHight);
        }
        return true;
    }

    public void changeListViewFastScrollDragViewOffset(float _offsetY, float _contentY) {
        if (_contentY >= 1.0f) {
            this.listViewFastScrollDragViewOffsetY = this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y + ((this.LISTVIEW_FASTSCROLLVIEW_HEIGHT * _offsetY) / _contentY);
            if (this.listViewFastScrollDragViewOffsetY < this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y) {
                this.listViewFastScrollDragViewOffsetY = this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y;
            } else if (this.listViewFastScrollDragViewOffsetY > this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y + this.LISTVIEW_FASTSCROLLVIEW_HEIGHT) {
                this.listViewFastScrollDragViewOffsetY = this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y + this.LISTVIEW_FASTSCROLLVIEW_HEIGHT;
            }
        }
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_OFFSET_X, this.LISTVIEW_OFFSET_Y, this.LISTVIEW_WIDTH, this.LISTVIEW_HEIGHT, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.listFronViewStrokeWidth3, -800609, this.listFrontViewRadius1);
        MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_OFFSET_X, this.LISTVIEW_OFFSET_Y, this.LISTVIEW_WIDTH, this.LISTVIEW_HEIGHT, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.listFronViewStrokeWidth3, -2447756, this.listFrontViewRadius);
        if (this.storeUnit.storeListSelectUnit.nowListIndex != 2) {
            MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X, this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y, this.LISTVIEW_FASTSCROLLVIEW_WIDTH, this.LISTVIEW_FASTSCROLLVIEW_HEIGHT, -2500135, this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH, -2447756, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.LISTVIEW_FASTSCROLLVIEW_RADIUS);
            if (this.appDelegate.listview_fastscrolldragview_Bitmap != null) {
                canvas.drawBitmap(this.appDelegate.listview_fastscrolldragview_Bitmap, new Rect(0, 0, this.appDelegate.listview_fastscrolldragview_Bitmap.getWidth(), this.appDelegate.listview_fastscrolldragview_Bitmap.getHeight()), new Rect((int) this.listViewFastScrollDragViewOffsetX, (int) this.listViewFastScrollDragViewOffsetY, (int) (this.listViewFastScrollDragViewOffsetX + this.listViewFastScrollDragViewWidth), (int) (this.listViewFastScrollDragViewOffsetY + this.listViewFastScrollDragViewHeight)), bitmapPaint);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.storeUnit = null;
        this.appDelegate = null;
    }
}
