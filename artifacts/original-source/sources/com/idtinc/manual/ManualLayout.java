package com.idtinc.manual;

import android.content.Context;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.maingame.MainGameViewController;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ManualLayout extends RelativeLayout {
    private float BIGBACKVIEW_HEIGHT;
    private float BIGBACKVIEW_OFFSET_X;
    private float BIGBACKVIEW_OFFSET_Y;
    private float BIGBACKVIEW_WIDTH;
    private float SCROLLVIEW_HEIGHT;
    private float SCROLLVIEW_OFFSET_X;
    private float SCROLLVIEW_OFFSET_Y;
    private float SCROLLVIEW_WIDTH;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    private MainGameViewController mainGameControllerLayout;
    private ManualBackView manualBackView;
    private ManualFrontView manualFrontView;
    private ManualScrollViewUnit manualScrollViewUnit;
    private float zoomRate;

    public ManualLayout(Context context, float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameControllerLayout) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.BIGBACKVIEW_OFFSET_X = 25.0f;
        this.BIGBACKVIEW_OFFSET_Y = 47.0f;
        this.BIGBACKVIEW_WIDTH = 270.0f;
        this.BIGBACKVIEW_HEIGHT = 342.0f;
        this.SCROLLVIEW_OFFSET_X = this.BIGBACKVIEW_OFFSET_X + 4.0f;
        this.SCROLLVIEW_OFFSET_Y = this.BIGBACKVIEW_OFFSET_Y + 4.0f;
        this.SCROLLVIEW_WIDTH = this.BIGBACKVIEW_WIDTH - 8.0f;
        this.SCROLLVIEW_HEIGHT = this.BIGBACKVIEW_HEIGHT - 8.0f;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.mainGameControllerLayout = _mainGameControllerLayout;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.BIGBACKVIEW_OFFSET_X = 23.0f * this.zoomRate;
        this.BIGBACKVIEW_OFFSET_Y = this.zoomRate * 47.0f;
        this.BIGBACKVIEW_WIDTH = 274.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.BIGBACKVIEW_HEIGHT = 342.0f * this.zoomRate;
        } else {
            this.BIGBACKVIEW_HEIGHT = 430.0f * this.zoomRate;
        }
        this.SCROLLVIEW_OFFSET_X = this.BIGBACKVIEW_OFFSET_X + (7.0f * this.zoomRate);
        this.SCROLLVIEW_OFFSET_Y = this.BIGBACKVIEW_OFFSET_Y + (this.zoomRate * 8.0f);
        this.SCROLLVIEW_WIDTH = this.BIGBACKVIEW_WIDTH - (14.0f * this.zoomRate);
        this.SCROLLVIEW_HEIGHT = this.BIGBACKVIEW_HEIGHT - (16.0f * this.zoomRate);
        this.manualBackView = new ManualBackView(context, this.finalWidth, this.finalHeight, this.zoomRate);
        addView(this.manualBackView, (int) this.finalWidth, (int) this.finalHeight);
        this.manualScrollViewUnit = new ManualScrollViewUnit(context, this.SCROLLVIEW_WIDTH, this.SCROLLVIEW_HEIGHT, this.zoomRate);
        this.manualScrollViewUnit.setBackgroundColor(0);
        RelativeLayout.LayoutParams manualScrollViewUnitLayoutParams = new RelativeLayout.LayoutParams((int) this.SCROLLVIEW_WIDTH, (int) this.SCROLLVIEW_HEIGHT);
        manualScrollViewUnitLayoutParams.setMargins((int) this.SCROLLVIEW_OFFSET_X, (int) this.SCROLLVIEW_OFFSET_Y, 0, 0);
        addView(this.manualScrollViewUnit, manualScrollViewUnitLayoutParams);
        this.manualFrontView = new ManualFrontView(context, this.finalWidth, this.finalHeight, this.zoomRate, this);
        addView(this.manualFrontView, (int) this.finalWidth, (int) this.finalHeight);
    }

    public void changeManualIndex(short _manualIndex) {
        this.manualScrollViewUnit.changManualIndex(_manualIndex);
        this.manualFrontView.changManualIndex(_manualIndex);
    }

    public void doLoop() {
        if (getVisibility() == 0) {
            this.manualFrontView.doLoop();
        }
    }

    public void doClose() {
        if (getVisibility() == 0) {
            this.appDelegate.doSoundPoolPlay(2);
            this.mainGameControllerLayout.doManualLayoutHidden(true);
        }
    }

    public void doHidden() {
        clearBitmap();
    }

    public void finishHidden() {
    }

    public void clearBitmap() {
        this.manualScrollViewUnit.clearBitmap();
    }

    public void onDestroy() {
        clearBitmap();
        if (this.manualFrontView != null) {
            this.manualFrontView.onDestroy();
            this.manualFrontView = null;
        }
        if (this.manualScrollViewUnit != null) {
            this.manualScrollViewUnit.onDestroy();
            this.manualScrollViewUnit = null;
        }
        if (this.manualBackView != null) {
            this.manualBackView.onDestroy();
            this.manualBackView = null;
        }
        this.mainGameControllerLayout = null;
        this.appDelegate = null;
    }
}
