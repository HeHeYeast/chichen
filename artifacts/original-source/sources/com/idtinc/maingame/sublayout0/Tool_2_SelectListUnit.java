package com.idtinc.maingame.sublayout0;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Tool_2_SelectListUnit {
    public float LISTBACKVIEW_HEIGHT;
    public float LISTBACKVIEW_OFFSET_X;
    public float LISTBACKVIEW_OFFSET_Y;
    public float LISTBACKVIEW_WIDTH;
    public float SCROLLLAYOUT_HEIGHT;
    public float SCROLLLAYOUT_OFFSET_X;
    public float SCROLLLAYOUT_OFFSET_Y;
    public float SCROLLLAYOUT_WIDTH;
    public float TOOL_BUTTON_HEIGHT;
    public float TOOL_BUTTON_SPACE_Y;
    public float TOOL_BUTTON_WIDTH;
    public AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    public MainGameUnit mainGameUnit;
    private Tool_2_SelectListBackViewUnit tool_2_SelectListBackViewUnit;
    private Tool_2_SelectListFrontViewUnit tool_2_SelectListFrontViewUnit;
    private Tool_2_SelectListScrollViewUnit tool_2_SelectListScrollViewUnit;
    private float zoomRate;
    public boolean hidden = false;
    public short nowStatus = -1;
    public final short BUTTON_ROW_CNT = 5;

    public Tool_2_SelectListUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameUnit _mainGameUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.LISTBACKVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.LISTBACKVIEW_OFFSET_Y = 4.0f;
        this.LISTBACKVIEW_WIDTH = 320.0f;
        this.LISTBACKVIEW_HEIGHT = 138.0f;
        this.SCROLLLAYOUT_OFFSET_X = 35.0f;
        this.SCROLLLAYOUT_OFFSET_Y = 39.0f;
        this.SCROLLLAYOUT_WIDTH = 250.0f;
        this.SCROLLLAYOUT_HEIGHT = 49.0f;
        this.TOOL_BUTTON_WIDTH = 50.0f;
        this.TOOL_BUTTON_HEIGHT = 50.0f;
        this.TOOL_BUTTON_SPACE_Y = 49.0f;
        this.appDelegate = _appDelegate;
        this.mainGameUnit = _mainGameUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.LISTBACKVIEW_OFFSET_X = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.LISTBACKVIEW_OFFSET_Y = 40.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTBACKVIEW_OFFSET_Y = 40.0f * this.zoomRate;
        } else {
            this.LISTBACKVIEW_OFFSET_Y = 84.0f * this.zoomRate;
        }
        this.LISTBACKVIEW_WIDTH = 320.0f * this.zoomRate;
        this.LISTBACKVIEW_HEIGHT = 138.0f * this.zoomRate;
        this.TOOL_BUTTON_WIDTH = this.zoomRate * 50.0f;
        this.TOOL_BUTTON_HEIGHT = this.zoomRate * 50.0f;
        this.TOOL_BUTTON_SPACE_Y = this.zoomRate * 49.0f;
        this.SCROLLLAYOUT_OFFSET_X = this.zoomRate * 35.0f;
        this.SCROLLLAYOUT_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (this.zoomRate * 39.0f);
        this.SCROLLLAYOUT_WIDTH = 250.0f * this.zoomRate;
        this.SCROLLLAYOUT_HEIGHT = this.TOOL_BUTTON_SPACE_Y * 10.0f;
        this.tool_2_SelectListBackViewUnit = new Tool_2_SelectListBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.tool_2_SelectListScrollViewUnit = new Tool_2_SelectListScrollViewUnit(this.SCROLLLAYOUT_OFFSET_X, this.SCROLLLAYOUT_OFFSET_Y, this.SCROLLLAYOUT_WIDTH, this.SCROLLLAYOUT_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.tool_2_SelectListFrontViewUnit = new Tool_2_SelectListFrontViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
    }

    public void open() {
        reload();
    }

    public void reload() {
        short rowCnt = this.tool_2_SelectListScrollViewUnit.reload();
        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
        short countMax = tool_0_0_level;
        if (countMax > 2) {
            countMax = 2;
        }
        this.LISTBACKVIEW_HEIGHT = (138.0f * this.zoomRate) + (this.TOOL_BUTTON_SPACE_Y * rowCnt);
        this.tool_2_SelectListBackViewUnit.resize();
        this.tool_2_SelectListFrontViewUnit.reset();
        this.tool_2_SelectListFrontViewUnit.changeCountLabel0String((short) (countMax + 1));
        checkLabel1String();
        this.tool_2_SelectListFrontViewUnit.resize();
    }

    public void checkLabel1String() {
        short selectedCnt = this.tool_2_SelectListScrollViewUnit.getSelectedCnt();
        this.tool_2_SelectListFrontViewUnit.changeCountLabel1String(selectedCnt);
    }

    public void close() {
        this.tool_2_SelectListFrontViewUnit.reset();
        if (!this.hidden) {
            this.mainGameUnit.closeTool_2_SelectListLayout(true);
        }
    }

    public void done() {
        this.tool_2_SelectListScrollViewUnit.saveSelected();
        this.mainGameUnit.tool_2_SelectListViewSelected();
        if (!this.hidden) {
            this.mainGameUnit.closeTool_2_SelectListLayout(true);
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        return (this.tool_2_SelectListFrontViewUnit == null || !(returnF = this.tool_2_SelectListFrontViewUnit.gameOnTouch(event))) ? (this.tool_2_SelectListScrollViewUnit == null || !(returnF = this.tool_2_SelectListScrollViewUnit.gameOnTouch(event))) ? returnF : returnF : returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.tool_2_SelectListBackViewUnit != null) {
            this.tool_2_SelectListBackViewUnit.gameDraw(canvas);
        }
        if (this.tool_2_SelectListScrollViewUnit != null) {
            this.tool_2_SelectListScrollViewUnit.gameDraw(canvas);
        }
        if (this.tool_2_SelectListFrontViewUnit != null) {
            this.tool_2_SelectListFrontViewUnit.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.tool_2_SelectListFrontViewUnit != null) {
            this.tool_2_SelectListFrontViewUnit.onDestroy();
            this.tool_2_SelectListFrontViewUnit = null;
        }
        if (this.tool_2_SelectListScrollViewUnit != null) {
            this.tool_2_SelectListScrollViewUnit.onDestroy();
            this.tool_2_SelectListScrollViewUnit = null;
        }
        if (this.tool_2_SelectListBackViewUnit != null) {
            this.tool_2_SelectListBackViewUnit.onDestroy();
            this.tool_2_SelectListBackViewUnit = null;
        }
        this.mainGameUnit = null;
        this.appDelegate = null;
    }
}
