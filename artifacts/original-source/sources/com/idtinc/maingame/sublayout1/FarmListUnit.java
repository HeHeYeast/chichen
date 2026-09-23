package com.idtinc.maingame.sublayout1;

import android.graphics.Canvas;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmListUnit {
    private AppDelegate appDelegate;
    private FarmListBackViewUnit farmListBackViewUnit;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    public short nowStatus = -1;
    private float offsetX;
    private float offsetY;
    private float zoomRate;

    public FarmListUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.appDelegate = _appDelegate;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.farmListBackViewUnit = new FarmListBackViewUnit(this.offsetX, this.offsetY, this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
    }

    public void gameDraw(Canvas canvas) {
        if (this.farmListBackViewUnit != null) {
            this.farmListBackViewUnit.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.farmListBackViewUnit != null) {
            this.farmListBackViewUnit.onDestroy();
            this.farmListBackViewUnit = null;
        }
        this.appDelegate = null;
    }
}
