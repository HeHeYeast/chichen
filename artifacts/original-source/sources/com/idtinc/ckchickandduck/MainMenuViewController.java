package com.idtinc.ckchickandduck;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainMenuViewController {
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MainMenuViewUnit mainMenuViewUnit;
    public short nowStatus;
    private float zoomRate;

    public MainMenuViewController(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.nowStatus = (short) 0;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.nowStatus = (short) 0;
        this.mainMenuViewUnit = new MainMenuViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
    }

    public void restart() {
        if (this.nowStatus == 2) {
            this.mainMenuViewUnit.refreshBackGround();
            this.mainMenuViewUnit.changeMainMenuLayoutNowStatus(3);
        }
    }

    public void goToSavesCheck() {
        if (this.nowStatus == 2) {
            this.mainMenuViewUnit.clearDrawable();
            this.appDelegate.goToSavesCheck();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.nowStatus == 0 && this.mainMenuViewUnit != null) {
            return this.mainMenuViewUnit.gameOnTouch(event);
        }
        return false;
    }

    public void gameDraw(Canvas canvas) {
        if (this.appDelegate != null && this.mainMenuViewUnit != null) {
            this.mainMenuViewUnit.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.mainMenuViewUnit != null) {
            this.mainMenuViewUnit.onDestroy();
            this.mainMenuViewUnit = null;
        }
        this.appDelegate = null;
    }
}
