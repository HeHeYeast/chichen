package com.idtinc.maingame.sublayout2;

import android.graphics.Canvas;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StoreListUnit {
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public short nowStatus = -1;
    private StoreListBackViewUnit storeListBackViewUnit;
    private float zoomRate;

    public StoreListUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.storeListBackViewUnit = new StoreListBackViewUnit(_offsetX, _offsetY, this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
    }

    public void gameDraw(Canvas canvas) {
        if (this.storeListBackViewUnit != null) {
            this.storeListBackViewUnit.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.storeListBackViewUnit != null) {
            this.storeListBackViewUnit.onDestroy();
            this.storeListBackViewUnit = null;
        }
        this.appDelegate = null;
    }
}
