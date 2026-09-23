package com.idtinc.maingame.sublayout1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmListFrontViewUnit {
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
    private short buttonClickCnt;
    public int closeButtonColor0;
    public int closeButtonColor1;
    public int closeButtonColor2;
    public int closeButtonColor3;
    public float closeButtonHeight;
    public float closeButtonOffsetX;
    public float closeButtonOffsetY;
    public float closeButtonRadius;
    private short closeButtonStatus;
    public float closeButtonStrokeWidth1;
    public float closeButtonStrokeWidth2;
    public float closeButtonStrokeWidth3;
    public int closeButtonTitleLabelColor0;
    public int closeButtonTitleLabelColor1;
    public int closeButtonTitleLabelColor2;
    public float closeButtonTitleLabelFontSize;
    public float closeButtonTitleLabelOffsetX;
    public float closeButtonTitleLabelOffsetY;
    public String closeButtonTitleLabelString;
    public float closeButtonTitleLabelStroke1Width;
    public float closeButtonTitleLabelStroke2Width;
    Typeface closeButtonTitleLabelTypeface;
    public float closeButtonWidth;
    private FarmUnit farmUnit;
    private float finalHeight;
    private float finalWidth;
    public int listBackViewColor0;
    public float listBlockViewRadius;
    public float listFronViewStrokeWidth2;
    public float listFronViewStrokeWidth3;
    public float listFrontViewRadius;
    public float listViewFastScrollDragViewHeight;
    public float listViewFastScrollDragViewOffsetX;
    public float listViewFastScrollDragViewOffsetY;
    public float listViewFastScrollDragViewWidth;
    private MyDraw myDraw;
    private float offsetX;
    private float offsetY;
    public int selectAllButtonColor0;
    public int selectAllButtonColor1;
    public int selectAllButtonColor2;
    public int selectAllButtonColor3;
    public float selectAllButtonHeight;
    public float selectAllButtonOffsetX;
    public float selectAllButtonOffsetY;
    public float selectAllButtonRadius;
    public short selectAllButtonStatus;
    public float selectAllButtonStrokeWidth1;
    public float selectAllButtonStrokeWidth2;
    public float selectAllButtonStrokeWidth3;
    public int selectAllButtonTitleLabelColor0;
    public int selectAllButtonTitleLabelColor1;
    public int selectAllButtonTitleLabelColor2;
    public float selectAllButtonTitleLabelFontSize;
    public float selectAllButtonTitleLabelOffsetX;
    public float selectAllButtonTitleLabelOffsetY;
    public String selectAllButtonTitleLabelString;
    public float selectAllButtonTitleLabelStroke1Width;
    public float selectAllButtonTitleLabelStroke2Width;
    Typeface selectAllButtonTitleLabelTypeface;
    public float selectAllButtonWidth;
    public int selectClearButtonColor0;
    public int selectClearButtonColor1;
    public int selectClearButtonColor2;
    public int selectClearButtonColor3;
    public float selectClearButtonHeight;
    public float selectClearButtonOffsetX;
    public float selectClearButtonOffsetY;
    public float selectClearButtonRadius;
    public short selectClearButtonStatus;
    public float selectClearButtonStrokeWidth1;
    public float selectClearButtonStrokeWidth2;
    public float selectClearButtonStrokeWidth3;
    public int selectClearButtonTitleLabelColor0;
    public int selectClearButtonTitleLabelColor1;
    public int selectClearButtonTitleLabelColor2;
    public float selectClearButtonTitleLabelFontSize;
    public float selectClearButtonTitleLabelOffsetX;
    public float selectClearButtonTitleLabelOffsetY;
    public String selectClearButtonTitleLabelString;
    public float selectClearButtonTitleLabelStroke1Width;
    public float selectClearButtonTitleLabelStroke2Width;
    Typeface selectClearButtonTitleLabelTypeface;
    public float selectClearButtonWidth;
    public int selectOneButtonColor0;
    public int selectOneButtonColor1;
    public int selectOneButtonColor2;
    public int selectOneButtonColor3;
    public float selectOneButtonHeight;
    public float selectOneButtonOffsetX;
    public float selectOneButtonOffsetY;
    public float selectOneButtonRadius;
    public short selectOneButtonStatus;
    public float selectOneButtonStrokeWidth1;
    public float selectOneButtonStrokeWidth2;
    public float selectOneButtonStrokeWidth3;
    public int selectOneButtonTitleLabelColor0;
    public int selectOneButtonTitleLabelColor1;
    public int selectOneButtonTitleLabelColor2;
    public float selectOneButtonTitleLabelFontSize;
    public float selectOneButtonTitleLabelOffsetX;
    public float selectOneButtonTitleLabelOffsetY;
    public String selectOneButtonTitleLabelString;
    public float selectOneButtonTitleLabelStroke1Width;
    public float selectOneButtonTitleLabelStroke2Width;
    Typeface selectOneButtonTitleLabelTypeface;
    public float selectOneButtonWidth;
    public int sellButtonColor0;
    public int sellButtonColor1;
    public int sellButtonColor2;
    public int sellButtonColor3;
    public float sellButtonHeight;
    public float sellButtonOffsetX;
    public float sellButtonOffsetY;
    public float sellButtonRadius;
    public short sellButtonStatus;
    public float sellButtonStrokeWidth1;
    public float sellButtonStrokeWidth2;
    public float sellButtonStrokeWidth3;
    public int sellButtonTitleLabelColor0;
    public int sellButtonTitleLabelColor1;
    public int sellButtonTitleLabelColor2;
    public float sellButtonTitleLabelFontSize;
    public float sellButtonTitleLabelOffsetX;
    public float sellButtonTitleLabelOffsetY;
    public String sellButtonTitleLabelString;
    public float sellButtonTitleLabelStroke1Width;
    public float sellButtonTitleLabelStroke2Width;
    Typeface sellButtonTitleLabelTypeface;
    public float sellButtonWidth;
    private int totalLabelColor0;
    private int totalLabelColor1;
    private int totalLabelColor2;
    private float totalLabelFontSize;
    private float totalLabelOffsetX;
    private float totalLabelOffsetY;
    private String totalLabelString;
    private float totalLabelStroke1Width;
    private float totalLabelStroke2Width;
    Typeface totalLabelTypeface;
    private short touchButtonIndex;
    private float zoomRate;

    public FarmListFrontViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, FarmUnit _farmUnit, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) -1;
        this.sellButtonStatus = (short) -1;
        this.selectClearButtonStatus = (short) -1;
        this.selectOneButtonStatus = (short) -1;
        this.selectAllButtonStatus = (short) -1;
        this.LISTBACKVIEW_OFFSET_X = 5.0f;
        this.LISTBACKVIEW_OFFSET_Y = 5.0f;
        this.LISTBACKVIEW_WIDTH = 306.0f;
        this.LISTBACKVIEW_HEIGHT = 294.0f;
        this.listBackViewColor0 = -3487;
        this.LISTVIEW_OFFSET_X = ((this.LISTBACKVIEW_OFFSET_X + 3.0f) + 5.0f) - 2.0f;
        this.LISTVIEW_OFFSET_Y = (((this.LISTBACKVIEW_OFFSET_Y + 35.0f) + 14.0f) - 4.0f) - 2.0f;
        this.LISTVIEW_WIDTH = 294.0f;
        this.LISTVIEW_HEIGHT = 214.0f;
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X = 290.0f;
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y = 47.0f;
        this.LISTVIEW_FASTSCROLLVIEW_WIDTH = 8.0f;
        this.LISTVIEW_FASTSCROLLVIEW_HEIGHT = 206.0f;
        this.LISTVIEW_FASTSCROLLVIEW_RADIUS = 4.0f;
        this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH = 2.0f;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.listFrontViewRadius = 8.0f;
        this.listFronViewStrokeWidth2 = 2.0f;
        this.listFronViewStrokeWidth3 = 2.0f;
        this.listBlockViewRadius = 4.0f;
        this.listViewFastScrollDragViewWidth = 26.0f;
        this.listViewFastScrollDragViewHeight = 26.0f;
        this.listViewFastScrollDragViewOffsetX = 290.0f;
        this.listViewFastScrollDragViewOffsetY = 47.0f;
        this.closeButtonWidth = 28.0f;
        this.closeButtonHeight = 28.0f;
        this.closeButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + 5.0f;
        this.closeButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + 7.0f;
        this.closeButtonColor0 = -2248316;
        this.closeButtonStrokeWidth1 = 2.0f;
        this.closeButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.closeButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor2 = 0;
        this.closeButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor3 = 0;
        this.closeButtonRadius = 15.0f;
        this.closeButtonTitleLabelString = "";
        this.closeButtonTitleLabelFontSize = 22.0f;
        this.closeButtonTitleLabelColor0 = -16;
        this.closeButtonTitleLabelStroke1Width = 2.0f;
        this.closeButtonTitleLabelColor1 = -16;
        this.closeButtonTitleLabelStroke2Width = 0.5f;
        this.closeButtonTitleLabelColor2 = 0;
        this.closeButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.selectClearButtonWidth = 26.0f;
        this.selectClearButtonHeight = 26.0f;
        this.selectClearButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + 226.0f;
        this.selectClearButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + 7.0f;
        this.selectClearButtonColor0 = -227838;
        this.selectClearButtonStrokeWidth1 = 2.0f;
        this.selectClearButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectClearButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.selectClearButtonColor2 = 0;
        this.selectClearButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.selectClearButtonColor3 = 0;
        this.selectClearButtonRadius = 6.0f;
        this.selectClearButtonTitleLabelString = "";
        this.selectClearButtonTitleLabelFontSize = 28.0f;
        this.selectClearButtonTitleLabelColor0 = -16;
        this.selectClearButtonTitleLabelStroke1Width = 1.0f;
        this.selectClearButtonTitleLabelColor1 = -16;
        this.selectClearButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.selectClearButtonTitleLabelColor2 = 0;
        this.selectClearButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.selectClearButtonTitleLabelOffsetY = 1.0f;
        this.selectOneButtonWidth = 26.0f;
        this.selectOneButtonHeight = 26.0f;
        this.selectOneButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + 250.0f;
        this.selectOneButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + 7.0f;
        this.selectOneButtonColor0 = -227838;
        this.selectOneButtonStrokeWidth1 = 2.0f;
        this.selectOneButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectOneButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.selectOneButtonColor2 = 0;
        this.selectOneButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.selectOneButtonColor3 = 0;
        this.selectOneButtonRadius = 6.0f;
        this.selectOneButtonTitleLabelString = "";
        this.selectOneButtonTitleLabelFontSize = 28.0f;
        this.selectOneButtonTitleLabelColor0 = -16;
        this.selectOneButtonTitleLabelStroke1Width = 1.0f;
        this.selectOneButtonTitleLabelColor1 = -16;
        this.selectOneButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.selectOneButtonTitleLabelColor2 = 0;
        this.selectOneButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.selectOneButtonTitleLabelOffsetY = 1.0f;
        this.selectAllButtonWidth = 26.0f;
        this.selectAllButtonHeight = 26.0f;
        this.selectAllButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + 274.0f;
        this.selectAllButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + 7.0f;
        this.selectAllButtonColor0 = -227838;
        this.selectAllButtonStrokeWidth1 = 2.0f;
        this.selectAllButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectAllButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.selectAllButtonColor2 = 0;
        this.selectAllButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.selectAllButtonColor3 = 0;
        this.selectAllButtonRadius = 6.0f;
        this.selectAllButtonTitleLabelString = "";
        this.selectAllButtonTitleLabelFontSize = 28.0f;
        this.selectAllButtonTitleLabelColor0 = -16;
        this.selectAllButtonTitleLabelStroke1Width = 1.0f;
        this.selectAllButtonTitleLabelColor1 = -16;
        this.selectAllButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.selectAllButtonTitleLabelColor2 = 0;
        this.selectAllButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.selectAllButtonTitleLabelOffsetY = 1.0f;
        this.sellButtonWidth = 80.0f;
        this.sellButtonHeight = 30.0f;
        this.sellButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + 10.0f;
        this.sellButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + 248.0f + 13.0f;
        this.sellButtonColor0 = -227838;
        this.sellButtonStrokeWidth1 = 2.0f;
        this.sellButtonColor1 = -16;
        this.sellButtonStrokeWidth2 = 2.0f;
        this.sellButtonColor2 = -7576502;
        this.sellButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.sellButtonColor3 = 0;
        this.sellButtonRadius = 10.0f;
        this.sellButtonTitleLabelString = "";
        this.sellButtonTitleLabelFontSize = 22.0f;
        this.sellButtonTitleLabelColor0 = -1;
        this.sellButtonTitleLabelStroke1Width = 2.0f;
        this.sellButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.sellButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.sellButtonTitleLabelColor2 = 0;
        this.sellButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.sellButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.totalLabelString = "";
        this.totalLabelFontSize = 22.0f;
        this.totalLabelColor0 = -1;
        this.totalLabelStroke1Width = 3.0f;
        this.totalLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.totalLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.totalLabelColor2 = 0;
        this.totalLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.totalLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = _appDelegate;
        this.farmUnit = _farmUnit;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
        this.sellButtonStatus = (short) -1;
        this.selectClearButtonStatus = (short) 0;
        this.selectOneButtonStatus = (short) 0;
        this.selectAllButtonStatus = (short) 0;
        this.LISTBACKVIEW_OFFSET_X = this.offsetX + (5.0f * this.zoomRate);
        this.LISTBACKVIEW_OFFSET_Y = this.offsetY + (5.0f * this.zoomRate);
        this.LISTBACKVIEW_WIDTH = 306.0f * this.zoomRate;
        this.LISTBACKVIEW_HEIGHT = 294.0f * this.zoomRate;
        this.listBackViewColor0 = -3487;
        this.LISTVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (6.0f * this.zoomRate);
        this.LISTVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (43.0f * this.zoomRate);
        this.LISTVIEW_WIDTH = 294.0f * this.zoomRate;
        this.LISTVIEW_HEIGHT = 284.0f * this.zoomRate;
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (290.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (47.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLVIEW_WIDTH = 8.0f * this.zoomRate;
        this.LISTVIEW_FASTSCROLLVIEW_HEIGHT = 276.0f * this.zoomRate;
        this.LISTVIEW_FASTSCROLLVIEW_RADIUS = 4.0f * this.zoomRate;
        this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH = 2.0f * this.zoomRate;
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_X = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X - (10.0f * this.zoomRate);
        this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y - (13.0f * this.zoomRate);
        this.listViewFastScrollDragViewWidth = 26.0f * this.zoomRate;
        this.listViewFastScrollDragViewHeight = 26.0f * this.zoomRate;
        this.listViewFastScrollDragViewOffsetX = this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X - (10.0f * this.zoomRate);
        this.listViewFastScrollDragViewOffsetY = this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y;
        this.listFrontViewRadius = 8.0f * this.zoomRate;
        this.listFronViewStrokeWidth2 = 2.0f * this.zoomRate;
        this.listFronViewStrokeWidth3 = 2.0f * this.zoomRate;
        this.listBlockViewRadius = this.listFrontViewRadius / 2.0f;
        this.closeButtonWidth = 28.0f * this.zoomRate;
        this.closeButtonHeight = 28.0f * this.zoomRate;
        this.closeButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + (5.0f * this.zoomRate);
        this.closeButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + (7.0f * this.zoomRate);
        this.closeButtonColor0 = -2248316;
        this.closeButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.closeButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.closeButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.closeButtonColor2 = 0;
        this.closeButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.closeButtonColor3 = 0;
        this.closeButtonRadius = 15.0f * this.zoomRate;
        this.closeButtonTitleLabelString = "X";
        this.closeButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.closeButtonTitleLabelFontSize = 22.0f * this.zoomRate;
        this.closeButtonTitleLabelColor0 = -16;
        this.closeButtonTitleLabelStroke1Width = 2.0f * this.zoomRate;
        this.closeButtonTitleLabelColor1 = -16;
        this.closeButtonTitleLabelStroke2Width = 0.5f * this.zoomRate;
        this.closeButtonTitleLabelColor2 = 0;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.closeButtonTitleLabelTypeface);
        newPaint.setTextSize(this.closeButtonTitleLabelFontSize);
        this.closeButtonTitleLabelOffsetX = this.closeButtonOffsetX + (1.0f * this.zoomRate) + ((this.closeButtonWidth - newPaint.measureText(this.closeButtonTitleLabelString)) / 2.0f);
        this.closeButtonTitleLabelOffsetY = (this.closeButtonOffsetY + this.closeButtonHeight) - (this.closeButtonTitleLabelFontSize * 0.3f);
        this.selectClearButtonWidth = 26.0f * this.zoomRate;
        this.selectClearButtonHeight = 26.0f * this.zoomRate;
        this.selectClearButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + (226.0f * this.zoomRate);
        this.selectClearButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + (7.0f * this.zoomRate);
        this.selectClearButtonColor0 = -227838;
        this.selectClearButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.selectClearButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectClearButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectClearButtonColor2 = 0;
        this.selectClearButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectClearButtonColor3 = 0;
        this.selectClearButtonRadius = 6.0f * this.zoomRate;
        this.selectClearButtonTitleLabelString = "○";
        this.selectClearButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.selectClearButtonTitleLabelFontSize = 28.0f * this.zoomRate;
        this.selectClearButtonTitleLabelColor0 = -16;
        this.selectClearButtonTitleLabelStroke1Width = 1.0f * this.zoomRate;
        this.selectClearButtonTitleLabelColor1 = -16;
        this.selectClearButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectClearButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.selectClearButtonTitleLabelTypeface);
        newPaint.setTextSize(this.selectClearButtonTitleLabelFontSize);
        this.selectClearButtonTitleLabelOffsetX = this.selectClearButtonOffsetX + (BitmapDescriptorFactory.HUE_RED * this.zoomRate) + ((this.selectClearButtonWidth - newPaint.measureText(this.selectClearButtonTitleLabelString)) / 2.0f);
        this.selectClearButtonTitleLabelOffsetY = (this.selectClearButtonOffsetY + this.selectClearButtonHeight) - (this.selectClearButtonTitleLabelFontSize * 0.07f);
        this.selectOneButtonWidth = 26.0f * this.zoomRate;
        this.selectOneButtonHeight = 26.0f * this.zoomRate;
        this.selectOneButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + (250.0f * this.zoomRate);
        this.selectOneButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + (7.0f * this.zoomRate);
        this.selectOneButtonColor0 = -227838;
        this.selectOneButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.selectOneButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectOneButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectOneButtonColor2 = 0;
        this.selectOneButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectOneButtonColor3 = 0;
        this.selectOneButtonRadius = 6.0f * this.zoomRate;
        this.selectOneButtonTitleLabelString = "◎";
        this.selectOneButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.selectOneButtonTitleLabelFontSize = 28.0f * this.zoomRate;
        this.selectOneButtonTitleLabelColor0 = -16;
        this.selectOneButtonTitleLabelStroke1Width = 1.0f * this.zoomRate;
        this.selectOneButtonTitleLabelColor1 = -16;
        this.selectOneButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectOneButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.selectOneButtonTitleLabelTypeface);
        newPaint.setTextSize(this.selectOneButtonTitleLabelFontSize);
        this.selectOneButtonTitleLabelOffsetX = this.selectOneButtonOffsetX + (BitmapDescriptorFactory.HUE_RED * this.zoomRate) + ((this.selectOneButtonWidth - newPaint.measureText(this.selectOneButtonTitleLabelString)) / 2.0f);
        this.selectOneButtonTitleLabelOffsetY = (this.selectOneButtonOffsetY + this.selectOneButtonHeight) - (this.selectOneButtonTitleLabelFontSize * 0.07f);
        this.selectAllButtonWidth = 26.0f * this.zoomRate;
        this.selectAllButtonHeight = 26.0f * this.zoomRate;
        this.selectAllButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + (274.0f * this.zoomRate);
        this.selectAllButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + (7.0f * this.zoomRate);
        this.selectAllButtonColor0 = -227838;
        this.selectAllButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.selectAllButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectAllButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectAllButtonColor2 = 0;
        this.selectAllButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectAllButtonColor3 = 0;
        this.selectAllButtonRadius = 6.0f * this.zoomRate;
        this.selectAllButtonTitleLabelString = "●";
        this.selectAllButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.selectAllButtonTitleLabelFontSize = 28.0f * this.zoomRate;
        this.selectAllButtonTitleLabelColor0 = -16;
        this.selectAllButtonTitleLabelStroke1Width = 1.0f * this.zoomRate;
        this.selectAllButtonTitleLabelColor1 = -16;
        this.selectAllButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectAllButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.selectAllButtonTitleLabelTypeface);
        newPaint.setTextSize(this.selectAllButtonTitleLabelFontSize);
        this.selectAllButtonTitleLabelOffsetX = this.selectAllButtonOffsetX + (BitmapDescriptorFactory.HUE_RED * this.zoomRate) + ((this.selectAllButtonWidth - newPaint.measureText(this.selectAllButtonTitleLabelString)) / 2.0f);
        this.selectAllButtonTitleLabelOffsetY = (this.selectAllButtonOffsetY + this.selectAllButtonHeight) - (this.selectAllButtonTitleLabelFontSize * 0.07f);
        this.sellButtonWidth = 80.0f * this.zoomRate;
        this.sellButtonHeight = 30.0f * this.zoomRate;
        this.sellButtonOffsetX = this.LISTBACKVIEW_OFFSET_X + (10.0f * this.zoomRate);
        this.sellButtonOffsetY = this.LISTBACKVIEW_OFFSET_Y + (331.0f * this.zoomRate);
        this.sellButtonColor0 = -227838;
        this.sellButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.sellButtonColor1 = -16;
        this.sellButtonStrokeWidth2 = 2.0f * this.zoomRate;
        this.sellButtonColor2 = -7576502;
        this.sellButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.sellButtonColor3 = 0;
        this.sellButtonRadius = 10.0f * this.zoomRate;
        this.sellButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Sell);
        this.sellButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.sellButtonTitleLabelFontSize = 22.0f * this.zoomRate;
        this.sellButtonTitleLabelColor0 = -1;
        this.sellButtonTitleLabelStroke1Width = 2.0f * this.zoomRate;
        this.sellButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.sellButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.sellButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.sellButtonTitleLabelTypeface);
        newPaint.setTextSize(this.sellButtonTitleLabelFontSize);
        this.sellButtonTitleLabelOffsetX = this.sellButtonOffsetX + ((this.sellButtonWidth - newPaint.measureText(this.sellButtonTitleLabelString)) / 2.0f);
        this.sellButtonTitleLabelOffsetY = (this.sellButtonOffsetY + this.sellButtonHeight) - (this.sellButtonTitleLabelFontSize * 0.3f);
        this.totalLabelString = "";
        this.totalLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.totalLabelFontSize = 22.0f * this.zoomRate;
        this.totalLabelColor0 = -1;
        this.totalLabelStroke1Width = 3.0f * this.zoomRate;
        this.totalLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.totalLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.totalLabelColor2 = 0;
        newPaint.setTypeface(this.totalLabelTypeface);
        newPaint.setTextSize(this.totalLabelFontSize);
        this.totalLabelOffsetX = (this.offsetX + (299.0f * this.zoomRate)) - newPaint.measureText(this.totalLabelString);
        this.totalLabelOffsetY = (this.offsetY + (359.0f * this.zoomRate)) - (this.totalLabelFontSize * BitmapDescriptorFactory.HUE_RED);
        this.myDraw = new MyDraw();
    }

    public void reset() {
        changeTotal(0);
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
        this.sellButtonStatus = (short) -1;
    }

    public void changeTotal(int _total) {
        if (_total > 0) {
            this.totalLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ":  " + _total + " cp";
            this.sellButtonStatus = (short) 0;
        } else {
            this.totalLabelString = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ":  - cp";
            this.sellButtonStatus = (short) -1;
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.totalLabelTypeface);
        newPaint.setTextSize(this.totalLabelFontSize);
        this.totalLabelOffsetX = (this.offsetX + (299.0f * this.zoomRate)) - newPaint.measureText(this.totalLabelString);
        this.totalLabelOffsetY = (this.offsetY + (359.0f * this.zoomRate)) - (this.totalLabelFontSize * BitmapDescriptorFactory.HUE_RED);
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("FarmListFrontkView", "onTouchEvent");
        if ((event.getAction() == 0 || event.getAction() == 2) && event.getX() >= this.listViewFastScrollDragViewOffsetX && event.getY() >= this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y - (5.0f * this.zoomRate) && event.getY() < this.LISTVIEW_FASTSCROLLDRAGVIEW_OFFSET_Y + this.LISTVIEW_FASTSCROLLVIEW_HEIGHT + (30.0f * this.zoomRate)) {
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
            if (this.farmUnit != null) {
                this.farmUnit.doFarmListScrollViewUnitScroll(nowOffsetY / nowHight);
            }
            return true;
        }
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.closeButtonStatus == 0 && event.getY() > this.closeButtonOffsetY && event.getY() < this.closeButtonOffsetY + this.closeButtonHeight && event.getX() > this.closeButtonOffsetX && event.getX() < this.closeButtonOffsetX + this.closeButtonWidth) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 4;
                Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmListFrontkView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmListFrontViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.selectClearButtonStatus == 0 && event.getY() > this.selectClearButtonOffsetY && event.getY() < this.selectClearButtonOffsetY + this.selectClearButtonHeight && event.getX() > this.selectClearButtonOffsetX && event.getX() < this.selectClearButtonOffsetX + this.selectClearButtonWidth) {
                this.touchButtonIndex = (short) 10;
                this.buttonClickCnt = (short) 4;
                Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmListFrontkView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmListFrontViewUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.selectOneButtonStatus == 0 && event.getY() > this.selectOneButtonOffsetY && event.getY() < this.selectOneButtonOffsetY + this.selectOneButtonHeight && event.getX() > this.selectOneButtonOffsetX && event.getX() < this.selectOneButtonOffsetX + this.selectOneButtonWidth) {
                this.touchButtonIndex = (short) 11;
                this.buttonClickCnt = (short) 4;
                Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmListFrontkView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmListFrontViewUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.selectAllButtonStatus == 0 && event.getY() > this.selectAllButtonOffsetY && event.getY() < this.selectAllButtonOffsetY + this.selectAllButtonHeight && event.getX() > this.selectAllButtonOffsetX && event.getX() < this.selectAllButtonOffsetX + this.selectAllButtonWidth) {
                this.touchButtonIndex = (short) 12;
                this.buttonClickCnt = (short) 4;
                Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmListFrontkView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmListFrontViewUnit.4
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.sellButtonStatus == 0 && event.getY() > this.sellButtonOffsetY && event.getY() < this.sellButtonOffsetY + this.sellButtonHeight && event.getX() > this.sellButtonOffsetX && event.getX() < this.sellButtonOffsetX + this.sellButtonWidth) {
                this.touchButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 4;
                Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("FarmListFrontkView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmListFrontViewUnit.5
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
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

    public void doClick() {
        if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 12) {
            if (this.touchButtonIndex == 0) {
                if (!this.farmUnit.hidden) {
                    this.appDelegate.doSoundPoolPlay(2);
                    this.farmUnit.closeListLayout();
                }
            } else if (this.touchButtonIndex == 1) {
                this.farmUnit.readySell();
            } else if (this.touchButtonIndex == 10) {
                if (!this.farmUnit.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                    this.farmUnit.doListViewSelectWithType((short) 0);
                }
            } else if (this.touchButtonIndex == 11) {
                if (!this.farmUnit.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                    this.farmUnit.doListViewSelectWithType((short) 1);
                }
            } else if (this.touchButtonIndex == 12 && !this.farmUnit.hidden) {
                this.appDelegate.doSoundPoolPlay(1);
                this.farmUnit.doListViewSelectWithType((short) 2);
            }
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_OFFSET_X, this.LISTVIEW_OFFSET_Y, this.LISTVIEW_WIDTH, this.LISTVIEW_HEIGHT, 0, BitmapDescriptorFactory.HUE_RED, 0, this.listFronViewStrokeWidth2, -200082, this.listFronViewStrokeWidth3, -200082, this.listFrontViewRadius);
        MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_OFFSET_X, this.LISTVIEW_OFFSET_Y, this.LISTVIEW_WIDTH, this.LISTVIEW_HEIGHT, 0, BitmapDescriptorFactory.HUE_RED, 0, this.listFronViewStrokeWidth2, -10751, this.listFronViewStrokeWidth3, -200082, this.listFrontViewRadius);
        MyDraw.drawOnlyStrokeRect(canvas, this.LISTVIEW_FASTSCROLLVIEW_OFFSET_X, this.LISTVIEW_FASTSCROLLVIEW_OFFSET_Y, this.LISTVIEW_FASTSCROLLVIEW_WIDTH, this.LISTVIEW_FASTSCROLLVIEW_HEIGHT, -2500135, this.LISTVIEW_FASTSCROLLVIEW_STROKEWIDTH, -10751, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.LISTVIEW_FASTSCROLLVIEW_RADIUS);
        if (this.appDelegate.listview_fastscrolldragview_Bitmap != null) {
            canvas.drawBitmap(this.appDelegate.listview_fastscrolldragview_Bitmap, new Rect(0, 0, this.appDelegate.listview_fastscrolldragview_Bitmap.getWidth(), this.appDelegate.listview_fastscrolldragview_Bitmap.getHeight()), new Rect((int) this.listViewFastScrollDragViewOffsetX, (int) this.listViewFastScrollDragViewOffsetY, (int) (this.listViewFastScrollDragViewOffsetX + this.listViewFastScrollDragViewWidth), (int) (this.listViewFastScrollDragViewOffsetY + this.listViewFastScrollDragViewHeight)), bitmapPaint);
        }
        if (this.closeButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, this.closeButtonColor0, this.closeButtonStrokeWidth1, this.closeButtonColor1, this.closeButtonStrokeWidth2, this.closeButtonColor2, this.closeButtonStrokeWidth3, this.closeButtonColor3, this.closeButtonRadius);
            MyDraw.drawStrokeText(canvas, this.closeButtonTitleLabelOffsetX, this.closeButtonTitleLabelOffsetY, this.closeButtonTitleLabelTypeface, this.closeButtonTitleLabelString, this.closeButtonTitleLabelFontSize, this.closeButtonTitleLabelColor0, this.closeButtonTitleLabelStroke1Width, this.closeButtonTitleLabelColor1, this.closeButtonTitleLabelStroke2Width, this.closeButtonTitleLabelColor2);
            if (this.touchButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.closeButtonRadius);
            }
        }
        if (this.selectClearButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.selectClearButtonOffsetX, this.selectClearButtonOffsetY, this.selectClearButtonWidth, this.selectClearButtonHeight, this.selectClearButtonColor0, this.selectClearButtonStrokeWidth1, this.selectClearButtonColor1, this.selectClearButtonStrokeWidth2, this.selectClearButtonColor2, this.selectClearButtonStrokeWidth3, this.selectClearButtonColor3, this.selectClearButtonRadius);
            MyDraw.drawStrokeText(canvas, this.selectClearButtonTitleLabelOffsetX, this.selectClearButtonTitleLabelOffsetY, this.selectClearButtonTitleLabelTypeface, this.selectClearButtonTitleLabelString, this.selectClearButtonTitleLabelFontSize, this.selectClearButtonTitleLabelColor0, this.selectClearButtonTitleLabelStroke1Width, this.selectClearButtonTitleLabelColor1, this.selectClearButtonTitleLabelStroke2Width, this.selectClearButtonTitleLabelColor2);
            if (this.touchButtonIndex == 10) {
                MyDraw.drawStrokeRect(canvas, this.selectClearButtonOffsetX, this.selectClearButtonOffsetY, this.selectClearButtonWidth, this.selectClearButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.selectClearButtonRadius);
            }
        }
        if (this.selectOneButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.selectOneButtonOffsetX, this.selectOneButtonOffsetY, this.selectOneButtonWidth, this.selectOneButtonHeight, this.selectOneButtonColor0, this.selectOneButtonStrokeWidth1, this.selectOneButtonColor1, this.selectOneButtonStrokeWidth2, this.selectOneButtonColor2, this.selectOneButtonStrokeWidth3, this.selectOneButtonColor3, this.selectOneButtonRadius);
            MyDraw.drawStrokeText(canvas, this.selectOneButtonTitleLabelOffsetX, this.selectOneButtonTitleLabelOffsetY, this.selectOneButtonTitleLabelTypeface, this.selectOneButtonTitleLabelString, this.selectOneButtonTitleLabelFontSize, this.selectOneButtonTitleLabelColor0, this.selectOneButtonTitleLabelStroke1Width, this.selectOneButtonTitleLabelColor1, this.selectOneButtonTitleLabelStroke2Width, this.selectOneButtonTitleLabelColor2);
            if (this.touchButtonIndex == 11) {
                MyDraw.drawStrokeRect(canvas, this.selectOneButtonOffsetX, this.selectOneButtonOffsetY, this.selectOneButtonWidth, this.selectOneButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.selectOneButtonRadius);
            }
        }
        if (this.selectAllButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.selectAllButtonOffsetX, this.selectAllButtonOffsetY, this.selectAllButtonWidth, this.selectAllButtonHeight, this.selectAllButtonColor0, this.selectAllButtonStrokeWidth1, this.selectAllButtonColor1, this.selectAllButtonStrokeWidth2, this.selectAllButtonColor2, this.selectAllButtonStrokeWidth3, this.selectAllButtonColor3, this.selectAllButtonRadius);
            MyDraw.drawStrokeText(canvas, this.selectAllButtonTitleLabelOffsetX, this.selectAllButtonTitleLabelOffsetY, this.selectAllButtonTitleLabelTypeface, this.selectAllButtonTitleLabelString, this.selectAllButtonTitleLabelFontSize, this.selectAllButtonTitleLabelColor0, this.selectAllButtonTitleLabelStroke1Width, this.selectAllButtonTitleLabelColor1, this.selectAllButtonTitleLabelStroke2Width, this.selectAllButtonTitleLabelColor2);
            if (this.touchButtonIndex == 12) {
                MyDraw.drawStrokeRect(canvas, this.selectAllButtonOffsetX, this.selectAllButtonOffsetY, this.selectAllButtonWidth, this.selectAllButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.selectAllButtonRadius);
            }
        }
        if (this.sellButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.sellButtonOffsetX, this.sellButtonOffsetY, this.sellButtonWidth, this.sellButtonHeight, this.sellButtonColor0, this.sellButtonStrokeWidth1, this.sellButtonColor1, this.sellButtonStrokeWidth2, this.sellButtonColor2, this.sellButtonStrokeWidth3, this.sellButtonColor3, this.sellButtonRadius);
            MyDraw.drawStrokeText(canvas, this.sellButtonTitleLabelOffsetX, this.sellButtonTitleLabelOffsetY, this.sellButtonTitleLabelTypeface, this.sellButtonTitleLabelString, this.sellButtonTitleLabelFontSize, this.sellButtonTitleLabelColor0, this.sellButtonTitleLabelStroke1Width, this.sellButtonTitleLabelColor1, this.sellButtonTitleLabelStroke2Width, this.sellButtonTitleLabelColor2);
            if (this.touchButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.sellButtonOffsetX, this.sellButtonOffsetY, this.sellButtonWidth, this.sellButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.sellButtonRadius);
            }
        }
        MyDraw.drawStrokeText(canvas, this.totalLabelOffsetX, this.totalLabelOffsetY, this.totalLabelTypeface, this.totalLabelString, this.totalLabelFontSize, this.totalLabelColor0, this.totalLabelStroke1Width, this.totalLabelColor1, this.totalLabelStroke2Width, this.totalLabelColor2);
    }

    public void onDestroy() {
        this.myDraw = null;
        this.farmUnit = null;
        this.appDelegate = null;
    }
}
