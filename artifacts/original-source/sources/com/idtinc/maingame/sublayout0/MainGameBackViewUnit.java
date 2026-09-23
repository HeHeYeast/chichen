package com.idtinc.maingame.sublayout0;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckunit.CharacterUnitDictionary;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import com.immersion.hapticmediasdk.HapticContentSDK;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainGameBackViewUnit {
    public float alarmButtonHeight;
    public float alarmButtonOffsetX;
    public float alarmButtonOffsetY;
    public float alarmButtonTouchRangeXMax;
    public float alarmButtonTouchRangeXMin;
    public float alarmButtonTouchRangeYMax;
    public float alarmButtonTouchRangeYMin;
    public float alarmButtonWidth;
    private AppDelegate appDelegate;
    public short backGroundIndex;
    private short buttonClickCnt;
    private float finalHeight;
    private float finalWidth;
    public float fixKitchenButtonHeight;
    public float fixKitchenButtonOffsetX;
    public float fixKitchenButtonOffsetY;
    private short fixKitchenButtonStatus;
    public float fixKitchenButtonTouchRangeXMax;
    public float fixKitchenButtonTouchRangeXMin;
    public float fixKitchenButtonTouchRangeYMax;
    public float fixKitchenButtonTouchRangeYMin;
    public float fixKitchenButtonWidth;
    private float gaugeRate;
    public short hp;
    private float levelLabelFontSize;
    private float levelLabelOffsetX;
    private float levelLabelOffsetY;
    private float levelLabelStroke1Width;
    private float levelLabelStroke2Width;
    Typeface levelLabelTypeface;
    public float levelUpButtonHeight;
    public float levelUpButtonOffsetX;
    public float levelUpButtonOffsetY;
    private short levelUpButtonStatus;
    Typeface levelUpButtonTitleLabelTypeface;
    public float levelUpButtonTouchRangeXMax;
    public float levelUpButtonTouchRangeXMin;
    public float levelUpButtonTouchRangeYMax;
    public float levelUpButtonTouchRangeYMin;
    public float levelUpButtonWidth;
    private float levelUpLabelFontSize;
    private float levelUpLabelOffsetX;
    private float levelUpLabelStroke1Width;
    private float levelUpLabelStroke2Width;
    Typeface levelUpLabelTypeface;
    private MainGameUnit mainGameUnit;
    private MyDraw myDraw;
    private float starHeight;
    private float starOffsetX;
    private float starOffsetY;
    private float starWidth;
    private int timeGaugeBackRectColor0;
    private int timeGaugeBackRectColor1;
    private int timeGaugeBackRectColor2;
    private int timeGaugeBackRectColor3;
    private float timeGaugeBackRectHeight;
    private float timeGaugeBackRectOffsetX;
    private float timeGaugeBackRectOffsetY;
    private float timeGaugeBackRectRadius;
    private float timeGaugeBackRectStrokeWidth1;
    private float timeGaugeBackRectStrokeWidth2;
    private float timeGaugeBackRectStrokeWidth3;
    private float timeGaugeBackRectWidth;
    private int timeGaugeBatsuLabelColor0;
    private int timeGaugeBatsuLabelColor1;
    private int timeGaugeBatsuLabelColor2;
    private float timeGaugeBatsuLabelFontSize;
    private float timeGaugeBatsuLabelOffsetX;
    private float timeGaugeBatsuLabelOffsetY;
    private float timeGaugeBatsuLabelStroke1Width;
    private float timeGaugeBatsuLabelStroke2Width;
    Typeface timeGaugeBatsuLabelTypeface;
    private int timeGaugeRedLineColor0;
    private int timeGaugeRedLineColor1;
    private int timeGaugeRedLineColor2;
    private int timeGaugeRedLineColor3;
    private float timeGaugeRedLineHeight;
    private float timeGaugeRedLineOffsetX;
    private float timeGaugeRedLineOffsetY;
    private float timeGaugeRedLineRadius;
    private float timeGaugeRedLineStrokeWidth1;
    private float timeGaugeRedLineStrokeWidth2;
    private float timeGaugeRedLineStrokeWidth3;
    private float timeGaugeRedLineWidth;
    private float tool_1_ImageHeight;
    private float tool_1_ImageOffsetX;
    private float tool_1_ImageOffsetY;
    private float tool_1_ImageWidth;
    private float tool_2_ImageHeight;
    private float tool_2_ImageOffsetX;
    private float tool_2_ImageOffsetY;
    private float tool_2_ImageWidth;
    public float tool_2_SelectListOpenBitmapButtonHeight;
    public float tool_2_SelectListOpenBitmapButtonOffsetX;
    public float tool_2_SelectListOpenBitmapButtonOffsetY;
    public float tool_2_SelectListOpenBitmapButtonWidth;
    public int tool_2_SelectListOpenButtonColor0;
    public int tool_2_SelectListOpenButtonColor1;
    public int tool_2_SelectListOpenButtonColor2;
    public int tool_2_SelectListOpenButtonColor3;
    public float tool_2_SelectListOpenButtonHeight;
    public float tool_2_SelectListOpenButtonOffsetX;
    public float tool_2_SelectListOpenButtonOffsetY;
    public float tool_2_SelectListOpenButtonRadius;
    public int tool_2_SelectListOpenButtonShadowColor;
    public float tool_2_SelectListOpenButtonShadowOffsetX;
    public float tool_2_SelectListOpenButtonShadowOffsetY;
    public float tool_2_SelectListOpenButtonShadowOpacity;
    private short tool_2_SelectListOpenButtonStatus;
    public float tool_2_SelectListOpenButtonStrokeWidth1;
    public float tool_2_SelectListOpenButtonStrokeWidth2;
    public float tool_2_SelectListOpenButtonStrokeWidth3;
    public float tool_2_SelectListOpenButtonTouchRangeXMax;
    public float tool_2_SelectListOpenButtonTouchRangeXMin;
    public float tool_2_SelectListOpenButtonTouchRangeYMax;
    public float tool_2_SelectListOpenButtonTouchRangeYMin;
    public float tool_2_SelectListOpenButtonWidth;
    private float tool_2_SelectdImageHeight;
    private float tool_2_SelectdImageOffsetX;
    private float tool_2_SelectdImageOffsetY;
    private float tool_2_SelectdImageWidth;
    private short touchButtonIndex;
    private float zoomRate;
    private float levelUpLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    public String levelUplevelUpButtonTitleLabelString = "";
    public float levelUpButtonTitleLabelFontSize = 20.0f;
    public int levelUpButtonTitleLabelColor0 = -1;
    public float levelUpButtonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
    public int levelUpButtonTitleLabelColor1 = 0;
    public float levelUpButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
    public int levelUpButtonTitleLabelColor2 = 0;
    public float levelUpButton0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    public float levelUpButton1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    public float levelUpButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    private Bitmap backGroundBitmap = null;
    private Bitmap backDirtyBitmap = null;
    private Bitmap backGroundFrontBitmap = null;
    private Bitmap alarmButtonBitmap0 = null;
    private Bitmap alarmButtonBitmap1 = null;
    private Bitmap levelUpButtonBitmap0 = null;
    private Bitmap levelUpButtonBitmap1 = null;
    private Bitmap fixKitchenButtonBitmap0 = null;
    private Bitmap fixKitchenButtonBitmap1 = null;
    private Bitmap tool_2_SelectListOpenButtonBitmap = null;
    private SurfaceHolder surfaceHolder = null;

    public MainGameBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameUnit _mainGameUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.backGroundIndex = (short) -1;
        this.hp = (short) 100;
        this.gaugeRate = -1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.tool_2_SelectListOpenButtonStatus = (short) -1;
        this.fixKitchenButtonStatus = (short) -1;
        this.levelUpButtonStatus = (short) -1;
        this.timeGaugeBackRectOffsetX = 90.0f;
        this.timeGaugeBackRectOffsetY = 273.0f;
        this.timeGaugeBackRectWidth = 140.0f;
        this.timeGaugeBackRectHeight = 24.0f;
        this.timeGaugeBackRectColor0 = 0;
        this.timeGaugeBackRectStrokeWidth1 = 2.0f;
        this.timeGaugeBackRectColor1 = -6106;
        this.timeGaugeBackRectStrokeWidth2 = 2.5f;
        this.timeGaugeBackRectColor2 = -1308622848;
        this.timeGaugeBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeBackRectColor3 = 0;
        this.timeGaugeBackRectRadius = 8.0f;
        this.timeGaugeRedLineOffsetX = this.timeGaugeBackRectOffsetX + 6.0f;
        this.timeGaugeRedLineOffsetY = this.timeGaugeBackRectOffsetY + 6.0f;
        this.timeGaugeRedLineWidth = 128.0f;
        this.timeGaugeRedLineHeight = 12.0f;
        this.timeGaugeRedLineColor0 = -31230;
        this.timeGaugeRedLineStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor1 = 0;
        this.timeGaugeRedLineStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor2 = 0;
        this.timeGaugeRedLineStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor3 = 0;
        this.timeGaugeRedLineRadius = 2.0f;
        this.timeGaugeBatsuLabelFontSize = 30.0f;
        this.timeGaugeBatsuLabelColor0 = -31230;
        this.timeGaugeBatsuLabelStroke1Width = 3.0f;
        this.timeGaugeBatsuLabelColor1 = -1;
        this.timeGaugeBatsuLabelStroke2Width = 6.0f;
        this.timeGaugeBatsuLabelColor2 = -1308622848;
        this.timeGaugeBatsuLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeBatsuLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.tool_1_ImageOffsetX = 145.0f;
        this.tool_1_ImageOffsetY = -8.0f;
        this.tool_1_ImageWidth = 30.0f;
        this.tool_1_ImageHeight = 30.0f;
        this.tool_2_ImageOffsetX = 145.0f;
        this.tool_2_ImageOffsetY = -30.0f;
        this.tool_2_ImageWidth = 30.0f;
        this.tool_2_ImageHeight = 30.0f;
        this.tool_2_SelectdImageOffsetX = 34.0f;
        this.tool_2_SelectdImageOffsetY = 38.0f;
        this.tool_2_SelectdImageWidth = 48.0f;
        this.tool_2_SelectdImageHeight = 48.0f;
        this.alarmButtonWidth = 38.0f;
        this.alarmButtonHeight = 38.0f;
        this.alarmButtonOffsetX = 84.0f;
        this.alarmButtonOffsetY = 266.0f;
        this.alarmButtonTouchRangeXMin = BitmapDescriptorFactory.HUE_RED;
        this.alarmButtonTouchRangeXMax = BitmapDescriptorFactory.HUE_RED;
        this.alarmButtonTouchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.alarmButtonTouchRangeYMax = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonWidth = 24.0f;
        this.tool_2_SelectListOpenButtonHeight = 24.0f;
        this.tool_2_SelectListOpenButtonOffsetX = 9.0f;
        this.tool_2_SelectListOpenButtonOffsetY = 54.0f;
        this.tool_2_SelectListOpenButtonColor0 = -16;
        this.tool_2_SelectListOpenButtonStrokeWidth1 = 2.0f;
        this.tool_2_SelectListOpenButtonColor1 = -7576502;
        this.tool_2_SelectListOpenButtonStrokeWidth2 = 1.0f;
        this.tool_2_SelectListOpenButtonColor2 = -16;
        this.tool_2_SelectListOpenButtonStrokeWidth3 = 0.5f;
        this.tool_2_SelectListOpenButtonColor3 = 855638016;
        this.tool_2_SelectListOpenButtonRadius = 12.0f;
        this.tool_2_SelectListOpenButtonShadowOpacity = 2.0f;
        this.tool_2_SelectListOpenButtonShadowOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonShadowOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonShadowColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.tool_2_SelectListOpenBitmapButtonWidth = 14.0f;
        this.tool_2_SelectListOpenBitmapButtonHeight = 14.0f;
        this.tool_2_SelectListOpenBitmapButtonOffsetX = 14.0f;
        this.tool_2_SelectListOpenBitmapButtonOffsetY = 59.0f;
        this.tool_2_SelectListOpenButtonTouchRangeXMin = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonTouchRangeXMax = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonTouchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonTouchRangeYMax = BitmapDescriptorFactory.HUE_RED;
        this.fixKitchenButtonOffsetX = 282.0f;
        this.fixKitchenButtonOffsetY = 74.0f;
        this.fixKitchenButtonWidth = 40.0f;
        this.fixKitchenButtonHeight = 40.0f;
        this.fixKitchenButtonTouchRangeXMin = BitmapDescriptorFactory.HUE_RED;
        this.fixKitchenButtonTouchRangeXMax = BitmapDescriptorFactory.HUE_RED;
        this.fixKitchenButtonTouchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.fixKitchenButtonTouchRangeYMax = BitmapDescriptorFactory.HUE_RED;
        this.starOffsetX = 284.0f;
        this.starOffsetY = 34.0f;
        this.starWidth = 36.0f;
        this.starHeight = 36.0f;
        this.levelLabelFontSize = 20.0f;
        this.levelLabelStroke1Width = 2.5f;
        this.levelLabelStroke2Width = 3.5f;
        this.levelLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.levelLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.levelUpButtonOffsetX = 284.0f;
        this.levelUpButtonOffsetY = 34.0f;
        this.levelUpButtonWidth = 36.0f;
        this.levelUpButtonHeight = 36.0f;
        this.levelUpLabelFontSize = 20.0f;
        this.levelUpLabelStroke1Width = 2.5f;
        this.levelUpLabelStroke2Width = 3.5f;
        this.levelUpLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.levelUpButtonTouchRangeXMin = BitmapDescriptorFactory.HUE_RED;
        this.levelUpButtonTouchRangeXMax = BitmapDescriptorFactory.HUE_RED;
        this.levelUpButtonTouchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.levelUpButtonTouchRangeYMax = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = _appDelegate;
        this.mainGameUnit = _mainGameUnit;
        this.backGroundIndex = (short) -1;
        this.hp = (short) 100;
        this.gaugeRate = -1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.tool_2_SelectListOpenButtonStatus = (short) 0;
        this.fixKitchenButtonStatus = (short) -1;
        this.levelUpButtonStatus = (short) -1;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.timeGaugeBackRectOffsetX = 90.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.timeGaugeBackRectOffsetY = 273.0f * this.zoomRate;
        } else {
            this.timeGaugeBackRectOffsetY = 317.0f * this.zoomRate;
        }
        this.timeGaugeBackRectWidth = 140.0f * this.zoomRate;
        this.timeGaugeBackRectHeight = 24.0f * this.zoomRate;
        this.timeGaugeBackRectColor0 = 0;
        this.timeGaugeBackRectStrokeWidth1 = 2.0f * this.zoomRate;
        this.timeGaugeBackRectColor1 = -6106;
        this.timeGaugeBackRectStrokeWidth2 = 3.0f * this.zoomRate;
        this.timeGaugeBackRectColor2 = -1308622848;
        this.timeGaugeBackRectStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeBackRectColor3 = 0;
        this.timeGaugeBackRectRadius = 8.0f * this.zoomRate;
        this.timeGaugeRedLineOffsetX = this.timeGaugeBackRectOffsetX + (6.0f * this.zoomRate);
        this.timeGaugeRedLineOffsetY = this.timeGaugeBackRectOffsetY + (6.0f * this.zoomRate);
        this.timeGaugeRedLineWidth = 128.0f * this.zoomRate;
        this.timeGaugeRedLineHeight = 12.0f * this.zoomRate;
        this.timeGaugeRedLineColor0 = -31230;
        this.timeGaugeRedLineStrokeWidth1 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor1 = 0;
        this.timeGaugeRedLineStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor2 = 0;
        this.timeGaugeRedLineStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.timeGaugeRedLineColor3 = 0;
        this.timeGaugeRedLineRadius = 2.0f * this.zoomRate;
        this.timeGaugeBatsuLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.timeGaugeBatsuLabelFontSize = this.zoomRate * 30.0f;
        this.timeGaugeBatsuLabelColor0 = -31230;
        this.timeGaugeBatsuLabelStroke1Width = 3.0f * this.zoomRate;
        this.timeGaugeBatsuLabelColor1 = -1;
        this.timeGaugeBatsuLabelStroke2Width = 6.0f * this.zoomRate;
        this.timeGaugeBatsuLabelColor2 = -1308622848;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.timeGaugeBatsuLabelTypeface);
        newPaint.setTextSize(this.timeGaugeBatsuLabelFontSize);
        this.timeGaugeBatsuLabelOffsetX = (160.0f * this.zoomRate) - (newPaint.measureText("X") / 2.0f);
        this.timeGaugeBatsuLabelOffsetY = this.timeGaugeBackRectOffsetY + (this.timeGaugeBatsuLabelFontSize * 0.69f);
        this.tool_1_ImageOffsetX = 145.0f * this.zoomRate;
        this.tool_1_ImageOffsetY = this.timeGaugeBackRectOffsetY + ((-8.0f) * this.zoomRate);
        this.tool_1_ImageWidth = this.zoomRate * 30.0f;
        this.tool_1_ImageHeight = this.zoomRate * 30.0f;
        this.tool_2_ImageOffsetX = 115.0f * this.zoomRate;
        this.tool_2_ImageOffsetY = this.timeGaugeBackRectOffsetY + ((-30.0f) * this.zoomRate);
        this.tool_2_ImageWidth = this.zoomRate * 30.0f;
        this.tool_2_ImageHeight = this.zoomRate * 30.0f;
        this.tool_2_SelectdImageOffsetX = 34.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.tool_2_SelectdImageOffsetY = 38.0f * this.zoomRate;
        } else {
            this.tool_2_SelectdImageOffsetY = 82.0f * this.zoomRate;
        }
        this.tool_2_SelectdImageWidth = 48.0f * this.zoomRate;
        this.tool_2_SelectdImageHeight = 48.0f * this.zoomRate;
        this.alarmButtonWidth = 38.0f * this.zoomRate;
        this.alarmButtonHeight = 38.0f * this.zoomRate;
        this.alarmButtonOffsetX = 54.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.alarmButtonOffsetY = 265.0f * this.zoomRate;
        } else {
            this.alarmButtonOffsetY = 309.0f * this.zoomRate;
        }
        this.alarmButtonTouchRangeXMin = this.alarmButtonOffsetX;
        this.alarmButtonTouchRangeXMax = this.alarmButtonOffsetX + this.alarmButtonWidth;
        this.alarmButtonTouchRangeYMin = this.alarmButtonOffsetY;
        this.alarmButtonTouchRangeYMax = this.alarmButtonOffsetY + this.alarmButtonHeight;
        this.tool_2_SelectListOpenButtonWidth = 24.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonHeight = 24.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonOffsetX = 9.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.tool_2_SelectListOpenButtonOffsetY = 54.0f * this.zoomRate;
        } else {
            this.tool_2_SelectListOpenButtonOffsetY = 98.0f * this.zoomRate;
        }
        this.tool_2_SelectListOpenButtonColor0 = -3889;
        this.tool_2_SelectListOpenButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonColor1 = -3109815;
        this.tool_2_SelectListOpenButtonStrokeWidth2 = 1.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonColor2 = -16;
        this.tool_2_SelectListOpenButtonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonColor3 = 0;
        this.tool_2_SelectListOpenButtonRadius = 12.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonShadowOpacity = 3.0f * this.zoomRate;
        this.tool_2_SelectListOpenButtonShadowOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonShadowOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.tool_2_SelectListOpenButtonShadowColor = -1728053248;
        this.tool_2_SelectListOpenBitmapButtonWidth = 14.0f * this.zoomRate;
        this.tool_2_SelectListOpenBitmapButtonHeight = 14.0f * this.zoomRate;
        this.tool_2_SelectListOpenBitmapButtonOffsetX = this.tool_2_SelectListOpenButtonOffsetX + (5.0f * this.zoomRate);
        this.tool_2_SelectListOpenBitmapButtonOffsetY = this.tool_2_SelectListOpenButtonOffsetY + (5.0f * this.zoomRate);
        this.tool_2_SelectListOpenButtonTouchRangeXMin = this.tool_2_SelectListOpenButtonOffsetX - (12.0f * this.zoomRate);
        this.tool_2_SelectListOpenButtonTouchRangeXMax = this.tool_2_SelectListOpenButtonOffsetX + this.tool_2_SelectListOpenButtonWidth + (12.0f * this.zoomRate);
        this.tool_2_SelectListOpenButtonTouchRangeYMin = this.tool_2_SelectListOpenButtonOffsetY - (12.0f * this.zoomRate);
        this.tool_2_SelectListOpenButtonTouchRangeYMax = this.tool_2_SelectListOpenButtonOffsetY + this.tool_2_SelectListOpenButtonHeight + (12.0f * this.zoomRate);
        this.fixKitchenButtonOffsetX = 282.0f * this.zoomRate;
        this.fixKitchenButtonOffsetY = 74.0f * this.zoomRate;
        this.fixKitchenButtonWidth = 40.0f * this.zoomRate;
        this.fixKitchenButtonHeight = 40.0f * this.zoomRate;
        this.fixKitchenButtonTouchRangeXMin = this.fixKitchenButtonOffsetX - (4.0f * this.zoomRate);
        this.fixKitchenButtonTouchRangeXMax = this.fixKitchenButtonOffsetX + this.fixKitchenButtonWidth + (4.0f * this.zoomRate);
        this.fixKitchenButtonTouchRangeYMin = this.fixKitchenButtonOffsetY - (4.0f * this.zoomRate);
        this.fixKitchenButtonTouchRangeYMax = this.fixKitchenButtonOffsetY + this.fixKitchenButtonHeight + (4.0f * this.zoomRate);
        this.starOffsetX = 284.0f * this.zoomRate;
        this.starOffsetY = 34.0f * this.zoomRate;
        this.starWidth = 36.0f * this.zoomRate;
        this.starHeight = 36.0f * this.zoomRate;
        this.levelLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.levelLabelFontSize = 20.0f * this.zoomRate;
        this.levelLabelStroke1Width = 2.5f * this.zoomRate;
        this.levelLabelStroke2Width = 3.5f * this.zoomRate;
        newPaint.setTypeface(this.levelLabelTypeface);
        newPaint.setTextSize(this.levelLabelFontSize);
        this.levelLabelOffsetX = 302.0f * this.zoomRate;
        this.levelLabelOffsetY = 60.5f * this.zoomRate;
        this.levelUpButtonOffsetX = 284.0f * this.zoomRate;
        this.levelUpButtonOffsetY = 34.0f * this.zoomRate;
        this.levelUpButtonWidth = 36.0f * this.zoomRate;
        this.levelUpButtonHeight = 36.0f * this.zoomRate;
        this.levelUpButtonTouchRangeXMin = this.levelUpButtonOffsetX - (4.0f * this.zoomRate);
        this.levelUpButtonTouchRangeXMax = this.levelUpButtonOffsetX + this.levelUpButtonWidth + (4.0f * this.zoomRate);
        this.levelUpButtonTouchRangeYMin = this.levelUpButtonOffsetY - (4.0f * this.zoomRate);
        this.levelUpButtonTouchRangeYMax = this.levelUpButtonOffsetY + this.levelUpButtonHeight + (4.0f * this.zoomRate);
        this.levelUpLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.levelUpLabelFontSize = 20.0f * this.zoomRate;
        this.levelUpLabelStroke1Width = 2.5f * this.zoomRate;
        this.levelUpLabelStroke2Width = 3.5f * this.zoomRate;
        newPaint.setTypeface(this.levelLabelTypeface);
        newPaint.setTextSize(this.levelLabelFontSize);
        this.levelUpLabelOffsetX = 302.0f * this.zoomRate;
        this.levelUpLabelOffsetX = 60.5f * this.zoomRate;
        clearBitmap();
        this.myDraw = new MyDraw();
    }

    public void clearBitmap() {
        if (this.backGroundBitmap != null) {
            if (!this.backGroundBitmap.isRecycled()) {
                this.backGroundBitmap.recycle();
            }
            this.backGroundBitmap = null;
        }
        if (this.backDirtyBitmap != null) {
            if (!this.backDirtyBitmap.isRecycled()) {
                this.backDirtyBitmap.recycle();
            }
            this.backDirtyBitmap = null;
        }
        if (this.backGroundFrontBitmap != null) {
            if (!this.backGroundFrontBitmap.isRecycled()) {
                this.backGroundFrontBitmap.recycle();
            }
            this.backGroundFrontBitmap = null;
        }
        if (this.alarmButtonBitmap0 != null) {
            if (!this.alarmButtonBitmap0.isRecycled()) {
                this.alarmButtonBitmap0.recycle();
            }
            this.alarmButtonBitmap0 = null;
        }
        if (this.alarmButtonBitmap1 != null) {
            if (!this.alarmButtonBitmap1.isRecycled()) {
                this.alarmButtonBitmap1.recycle();
            }
            this.alarmButtonBitmap1 = null;
        }
        if (this.levelUpButtonBitmap0 != null) {
            if (!this.levelUpButtonBitmap0.isRecycled()) {
                this.levelUpButtonBitmap0.recycle();
            }
            this.levelUpButtonBitmap0 = null;
        }
        if (this.levelUpButtonBitmap1 != null) {
            if (!this.levelUpButtonBitmap1.isRecycled()) {
                this.levelUpButtonBitmap1.recycle();
            }
            this.levelUpButtonBitmap1 = null;
        }
        if (this.fixKitchenButtonBitmap0 != null) {
            if (!this.fixKitchenButtonBitmap0.isRecycled()) {
                this.fixKitchenButtonBitmap0.recycle();
            }
            this.fixKitchenButtonBitmap0 = null;
        }
        if (this.fixKitchenButtonBitmap1 != null) {
            if (!this.fixKitchenButtonBitmap1.isRecycled()) {
                this.fixKitchenButtonBitmap1.recycle();
            }
            this.fixKitchenButtonBitmap1 = null;
        }
        if (this.tool_2_SelectListOpenButtonBitmap != null) {
            if (!this.tool_2_SelectListOpenButtonBitmap.isRecycled()) {
                this.tool_2_SelectListOpenButtonBitmap.recycle();
            }
            this.tool_2_SelectListOpenButtonBitmap = null;
        }
    }

    public void refreshBitmap() {
        InputStream inputStream;
        InputStream inputStream2;
        InputStream inputStream3;
        clearBitmap();
        if (this.appDelegate != null) {
            short nowLevel = this.appDelegate.getTool0LevelWithIndex((short) 0);
            AssetManager asm = this.appDelegate.getAssets();
            int scale = 1;
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                if (nowLevel >= 1 && nowLevel <= 3) {
                    inputStream3 = asm.open("png/Tool/Tool0/tool_0_0_" + ((int) nowLevel) + "_0.jpg");
                } else {
                    inputStream3 = asm.open("png/Tool/Tool0/tool_0_0_0_0.jpg");
                }
                BufferedInputStream buf = new BufferedInputStream(inputStream3);
                BitmapFactory.decodeStream(buf, null, opt);
                scale = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
                opt2.inSampleSize = scale;
                this.backGroundBitmap = BitmapFactory.decodeStream(inputStream3, null, opt2);
                inputStream3.close();
            } catch (IOException e) {
            }
            try {
                if (nowLevel >= 1 && nowLevel <= 3) {
                    inputStream2 = asm.open("png/Tool/Tool0/tool_0_0_" + ((int) nowLevel) + "_dirty.png");
                } else {
                    inputStream2 = asm.open("png/Tool/Tool0/tool_0_0_0_dirty.png");
                }
                opt2.inSampleSize = scale;
                this.backDirtyBitmap = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
            if (nowLevel >= 1 && nowLevel <= 3) {
                inputStream = null;
            } else {
                try {
                    inputStream = asm.open("png/Tool/Tool0/tool_0_0_0_front.png");
                } catch (IOException e3) {
                }
            }
            if (inputStream != null) {
                opt2.inSampleSize = scale;
                this.backGroundFrontBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            }
            try {
                InputStream inputStream4 = asm.open("png/MainGame/alarm_on.png");
                opt2.inSampleSize = scale;
                this.alarmButtonBitmap0 = BitmapFactory.decodeStream(inputStream4, null, opt2);
                inputStream4.close();
            } catch (IOException e4) {
            }
            try {
                InputStream inputStream5 = asm.open("png/MainGame/alarm_off.png");
                opt2.inSampleSize = scale;
                this.alarmButtonBitmap1 = BitmapFactory.decodeStream(inputStream5, null, opt2);
                inputStream5.close();
            } catch (IOException e5) {
            }
            try {
                InputStream inputStream6 = asm.open("png/MainGame/level_up_0.png");
                opt2.inSampleSize = scale;
                this.levelUpButtonBitmap0 = BitmapFactory.decodeStream(inputStream6, null, opt2);
                inputStream6.close();
            } catch (IOException e6) {
            }
            try {
                InputStream inputStream7 = asm.open("png/MainGame/level_up_1.png");
                opt2.inSampleSize = scale;
                this.levelUpButtonBitmap1 = BitmapFactory.decodeStream(inputStream7, null, opt2);
                inputStream7.close();
            } catch (IOException e7) {
            }
            try {
                InputStream inputStream8 = asm.open("png/MainGame/kitchen_fix_0_0.png");
                opt2.inSampleSize = scale;
                this.fixKitchenButtonBitmap0 = BitmapFactory.decodeStream(inputStream8, null, opt2);
                inputStream8.close();
            } catch (IOException e8) {
            }
            try {
                InputStream inputStream9 = asm.open("png/MainGame/kitchen_fix_0_1.png");
                opt2.inSampleSize = scale;
                this.fixKitchenButtonBitmap1 = BitmapFactory.decodeStream(inputStream9, null, opt2);
                inputStream9.close();
            } catch (IOException e9) {
            }
            try {
                InputStream inputStream10 = asm.open("png/MainGame/change_icon.png");
                opt2.inSampleSize = scale;
                this.tool_2_SelectListOpenButtonBitmap = BitmapFactory.decodeStream(inputStream10, null, opt2);
                inputStream10.close();
            } catch (IOException e10) {
            }
        }
    }

    public void doLoop() {
        if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 2) {
            if (this.buttonClickCnt > 0) {
                this.buttonClickCnt = (short) (this.buttonClickCnt - 1);
                if (this.buttonClickCnt == 2) {
                    if (this.touchButtonIndex == 0) {
                        this.mainGameUnit.openTool_2_SelectListLayout();
                        this.appDelegate.doSoundPoolPlay(4);
                        return;
                    }
                    if (this.touchButtonIndex == 1) {
                        short levelShort = this.appDelegate.getTool0LevelWithIndex((short) 0);
                        if (levelShort == 0) {
                            MainGameUnit mainGameUnit = this.mainGameUnit;
                            this.appDelegate.getClass();
                            mainGameUnit.levelUpKitchenWithCP(HapticContentSDK.f17b04440444044404440444);
                            return;
                        } else if (levelShort == 1) {
                            MainGameUnit mainGameUnit2 = this.mainGameUnit;
                            this.appDelegate.getClass();
                            mainGameUnit2.levelUpKitchenWithCP(20000);
                            return;
                        } else {
                            if (levelShort == 2) {
                                MainGameUnit mainGameUnit3 = this.mainGameUnit;
                                this.appDelegate.getClass();
                                mainGameUnit3.levelUpKitchenWithCP(30000);
                                return;
                            }
                            return;
                        }
                    }
                    if (this.touchButtonIndex == 2) {
                        short levelShort2 = this.appDelegate.getTool0LevelWithIndex((short) 0);
                        if (levelShort2 == 0) {
                            MainGameUnit mainGameUnit4 = this.mainGameUnit;
                            this.appDelegate.getClass();
                            mainGameUnit4.fixKitchenWithCP(100);
                            return;
                        }
                        if (levelShort2 == 1) {
                            MainGameUnit mainGameUnit5 = this.mainGameUnit;
                            this.appDelegate.getClass();
                            mainGameUnit5.fixKitchenWithCP(150);
                            return;
                        } else if (levelShort2 == 2) {
                            MainGameUnit mainGameUnit6 = this.mainGameUnit;
                            this.appDelegate.getClass();
                            mainGameUnit6.fixKitchenWithCP(200);
                            return;
                        } else {
                            if (levelShort2 == 3) {
                                MainGameUnit mainGameUnit7 = this.mainGameUnit;
                                this.appDelegate.getClass();
                                mainGameUnit7.fixKitchenWithCP(250);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                }
                if (this.buttonClickCnt <= 0) {
                    this.touchButtonIndex = (short) -1;
                    this.buttonClickCnt = (short) -1;
                    return;
                }
                return;
            }
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            return;
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void refresh() {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        if (this.appDelegate.timeSaveDictionary != null) {
            ToolUnitDictionary toolUnitDictionary = null;
            if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolDictionarysArrayList.size() > 0) {
                toolUnitDictionary = toolDictionarysArrayList.get(0);
            }
            this.fixKitchenButtonStatus = (short) -1;
            this.hp = (short) -1;
            this.levelUpButtonStatus = (short) -1;
            if (toolUnitDictionary != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date nowDate = new Date();
                short levelShort = this.appDelegate.getTool0LevelWithIndex((short) 0);
                short newHp = 0;
                String fixedDateString = null;
                if (levelShort >= 0) {
                    fixedDateString = toolUnitDictionary.getFixedDate();
                }
                if (fixedDateString != null && fixedDateString.length() > 0) {
                    Date fixedDate = null;
                    try {
                        fixedDate = sdf.parse(fixedDateString);
                    } catch (ParseException e) {
                    }
                    if (fixedDate != null) {
                        newHp = fixedDate.before(new Date(nowDate.getTime() - (((this.appDelegate.KITCHEN_DIRTY_HOURS * 60) * 60) * 1000))) ? (short) 0 : (short) 100;
                    }
                }
                if (newHp >= 60) {
                    this.hp = (short) 100;
                    this.fixKitchenButtonStatus = (short) -1;
                    toolUnitDictionary.setFixedDate(sdf.format(nowDate));
                } else {
                    this.hp = (short) 0;
                    this.fixKitchenButtonStatus = (short) 0;
                }
                toolUnitDictionary.setHp(newHp);
                if (levelShort >= 0 && levelShort <= 2) {
                    short allTool1LowestLevelShort = this.appDelegate.getAllTool1LowestLevel((short) 6);
                    if (allTool1LowestLevelShort == levelShort) {
                        this.levelUplevelUpButtonTitleLabelString = new StringBuilder().append(levelShort + 2).toString();
                        this.levelUpButtonStatus = (short) 0;
                    }
                }
            } else {
                this.hp = (short) -1;
                this.fixKitchenButtonStatus = (short) -1;
                this.levelUpButtonStatus = (short) -1;
            }
            refreshFixButton();
        }
    }

    public boolean fixKitchenWithCP(int _fixcp) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        this.appDelegate.timeSaveDictionary.getPoint();
        boolean saveF = false;
        this.hp = (short) -1;
        this.fixKitchenButtonStatus = (short) -1;
        ToolUnitDictionary toolUnitDictionary = null;
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolDictionarysArrayList.size() > 0) {
            toolUnitDictionary = toolDictionarysArrayList.get(0);
        }
        if (toolUnitDictionary != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            short levelShort = this.appDelegate.getTool0LevelWithIndex((short) 0);
            short hpShort = toolUnitDictionary.getHp();
            this.appDelegate.getClass();
            float kitchenFixCp = 100.0f;
            if (levelShort == 1) {
                this.appDelegate.getClass();
                kitchenFixCp = 150.0f;
            } else if (levelShort == 2) {
                this.appDelegate.getClass();
                kitchenFixCp = 200.0f;
            }
            short newHp = 0;
            if (hpShort < 60) {
                float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                if (nowPoint >= kitchenFixCp) {
                    this.appDelegate.timeSaveDictionary.setPoint(nowPoint - kitchenFixCp);
                    saveF = true;
                    newHp = 100;
                }
            }
            if (newHp >= 60) {
                this.hp = (short) 100;
                this.fixKitchenButtonStatus = (short) -1;
            } else {
                this.hp = (short) 0;
                this.fixKitchenButtonStatus = (short) 0;
            }
            toolUnitDictionary.setHp(this.hp);
            toolUnitDictionary.setFixedDate(sdf.format(nowDate));
            toolUnitDictionary.setCheckedDate(sdf.format(nowDate));
        } else {
            this.hp = (short) -1;
            this.fixKitchenButtonStatus = (short) -1;
        }
        refreshFixButton();
        return saveF;
    }

    public boolean levelUpKitchenWithCP(int _levelupcp) {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        this.appDelegate.timeSaveDictionary.getPoint();
        boolean saveF = false;
        this.levelUpButtonStatus = (short) -1;
        ToolUnitDictionary toolUnitDictionary = null;
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 0 && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && toolDictionarysArrayList.size() > 0) {
            toolUnitDictionary = toolDictionarysArrayList.get(0);
        }
        if (toolUnitDictionary != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            short levelShort = this.appDelegate.getTool0LevelWithIndex((short) 0);
            if (levelShort >= 0) {
                this.appDelegate.getClass();
                if (_levelupcp == 10000 && levelShort == 0) {
                    float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                    this.appDelegate.getClass();
                    if (nowPoint >= 10000.0f) {
                        this.appDelegate.getClass();
                        this.appDelegate.timeSaveDictionary.setPoint(nowPoint - 10000.0f);
                        toolUnitDictionary.setLevel((short) (levelShort + 1));
                        toolUnitDictionary.setHp((short) 100);
                        toolUnitDictionary.setFixedDate(sdf.format(nowDate));
                        toolUnitDictionary.setCheckedDate(sdf.format(nowDate));
                        saveF = true;
                    }
                } else {
                    this.appDelegate.getClass();
                    if (_levelupcp == 20000 && levelShort == 1) {
                        float nowPoint2 = this.appDelegate.timeSaveDictionary.getPoint();
                        this.appDelegate.getClass();
                        if (nowPoint2 >= 20000.0f) {
                            this.appDelegate.getClass();
                            this.appDelegate.timeSaveDictionary.setPoint(nowPoint2 - 20000.0f);
                            toolUnitDictionary.setLevel((short) (levelShort + 1));
                            toolUnitDictionary.setHp((short) 100);
                            toolUnitDictionary.setFixedDate(sdf.format(nowDate));
                            toolUnitDictionary.setCheckedDate(sdf.format(nowDate));
                            saveF = true;
                        }
                    } else {
                        this.appDelegate.getClass();
                        if (_levelupcp == 30000 && levelShort == 2) {
                            float nowPoint3 = this.appDelegate.timeSaveDictionary.getPoint();
                            this.appDelegate.getClass();
                            if (nowPoint3 >= 30000.0f) {
                                this.appDelegate.getClass();
                                this.appDelegate.timeSaveDictionary.setPoint(nowPoint3 - 30000.0f);
                                toolUnitDictionary.setLevel((short) (levelShort + 1));
                                toolUnitDictionary.setHp((short) 100);
                                toolUnitDictionary.setFixedDate(sdf.format(nowDate));
                                toolUnitDictionary.setCheckedDate(sdf.format(nowDate));
                                saveF = true;
                            }
                        }
                    }
                }
            }
        }
        refreshBitmap();
        return saveF;
    }

    public void refreshFixButton() {
        if (this.hp >= 60) {
            this.fixKitchenButtonStatus = (short) -1;
        } else {
            this.fixKitchenButtonStatus = (short) 0;
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("MainGameBackurfaceView", "onTouchEvent");
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.tool_2_SelectListOpenButtonStatus == 0 && event.getY() > this.tool_2_SelectListOpenButtonTouchRangeYMin && event.getY() < this.tool_2_SelectListOpenButtonTouchRangeYMax && event.getX() > this.tool_2_SelectListOpenButtonTouchRangeXMin && event.getX() < this.tool_2_SelectListOpenButtonTouchRangeXMax) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 3;
                Log.d("MainGameBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("MainGameBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
            }
            if (this.levelUpButtonStatus == 0 && event.getY() > this.levelUpButtonTouchRangeYMin && event.getY() < this.levelUpButtonTouchRangeYMax && event.getX() > this.levelUpButtonTouchRangeXMin && event.getX() < this.levelUpButtonTouchRangeXMax) {
                this.touchButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 3;
                Log.d("MainGameBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("MainGameBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
            }
            if (this.fixKitchenButtonStatus == 0 && event.getY() > this.fixKitchenButtonTouchRangeYMin && event.getY() < this.fixKitchenButtonTouchRangeYMax && event.getX() > this.fixKitchenButtonTouchRangeXMin && event.getX() < this.fixKitchenButtonTouchRangeXMax) {
                this.touchButtonIndex = (short) 2;
                this.buttonClickCnt = (short) 3;
                Log.d("MainGameBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("MainGameBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
            }
            if (this.gaugeRate > BitmapDescriptorFactory.HUE_RED && event.getY() > this.alarmButtonTouchRangeYMin && event.getY() < this.alarmButtonTouchRangeYMax && event.getX() > this.alarmButtonTouchRangeXMin && event.getX() < this.alarmButtonTouchRangeXMax && this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                if (this.appDelegate.defaultSharedPreferences.getBoolean("cook_alarm", false)) {
                    editor.putBoolean("cook_alarm", false);
                    this.appDelegate.doSoundPoolPlay(2);
                } else {
                    editor.putBoolean("cook_alarm", true);
                    this.appDelegate.doSoundPoolPlay(1);
                }
                editor.commit();
                this.mainGameUnit.tool1SelectViewCheckCookAlarm();
            }
        }
        event.getAction();
        if ((event.getAction() == 0 || event.getAction() == 2) && event.getY() > this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y && event.getY() < this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y + this.mainGameUnit.GAMEZONEVIEW_HEIGHT) {
            Log.d("MainGameLayout", "X=" + event.getX() + ", Y=  " + (event.getY() - this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y));
            this.mainGameUnit.getCharacterCheck(event.getX(), event.getY() - this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y);
        }
        return true;
    }

    public void gameDraw(Canvas canvas) {
        short tool1SelectViewNowButtonIndex;
        ToolUnitDictionary toolUnitDictionary;
        short nowTool1Level;
        Bitmap toolBitmap;
        Bitmap toolBitmap2;
        Bitmap toolBitmap3;
        Bitmap toolBitmap4;
        Bitmap toolBitmap5;
        Bitmap toolBitmap6;
        short characterID;
        short characterID2;
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        if (this.appDelegate != null) {
            if (this.backGroundBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                if (!this.appDelegate.isRetina4) {
                    canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (BitmapDescriptorFactory.HUE_RED - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                } else {
                    canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                }
            }
            if (this.hp == 0 && this.backDirtyBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                if (!this.appDelegate.isRetina4) {
                    canvas.drawBitmap(this.backDirtyBitmap, new Rect(0, 0, this.backDirtyBitmap.getWidth(), this.backDirtyBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                } else {
                    canvas.drawBitmap(this.backDirtyBitmap, new Rect(0, 0, this.backDirtyBitmap.getWidth(), this.backDirtyBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                }
            }
            if (this.mainGameUnit.nowCharacterUnitViewsArrayList != null) {
                for (int i = 0; i < this.mainGameUnit.nowCharacterUnitViewsArrayList.size(); i++) {
                    CharacterUnit characterUnit = this.mainGameUnit.nowCharacterUnitViewsArrayList.get(i);
                    if (characterUnit != null && !characterUnit.hidden) {
                        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
                        if (!characterUnit.imageView0Hidden) {
                            Bitmap characterBitmap = null;
                            short characterBitmapDrawType = 0;
                            if (nowCharacterUnitDictionary != null) {
                                short eggId = nowCharacterUnitDictionary.getEggId();
                                if (eggId == 0) {
                                    if (this.appDelegate.character0Image0ArrayList != null && (characterID2 = nowCharacterUnitDictionary.getCharacterId()) >= 0 && characterID2 < this.appDelegate.character0Image0ArrayList.size()) {
                                        if (characterUnit.imageView0TransformX <= BitmapDescriptorFactory.HUE_RED) {
                                            if (characterID2 == 84) {
                                                short characterImageIndex = (short) (characterID2 - 84);
                                                if (characterImageIndex >= 0 && characterImageIndex < this.appDelegate.character0RevImage0ArrayList.size()) {
                                                    characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex);
                                                }
                                            } else if (characterID2 == 84 || characterID2 == 89 || characterID2 == 90 || characterID2 == 91 || characterID2 == 92 || characterID2 == 93 || characterID2 == 94 || characterID2 == 95 || characterID2 == 96 || characterID2 == 97 || characterID2 == 98 || characterID2 == 99 || characterID2 == 100 || characterID2 == 101 || characterID2 == 102 || characterID2 == 103) {
                                                short characterImageIndex2 = (short) ((characterID2 - 89) + 1);
                                                if (characterImageIndex2 >= 0 && characterImageIndex2 < this.appDelegate.character0RevImage0ArrayList.size()) {
                                                    characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex2);
                                                }
                                            } else {
                                                characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID2);
                                                characterBitmapDrawType = 1;
                                            }
                                        } else {
                                            characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID2);
                                        }
                                    }
                                } else if (eggId == 1 && this.appDelegate.character1Image0ArrayList != null && (characterID = nowCharacterUnitDictionary.getCharacterId()) >= 0 && characterID < this.appDelegate.character1Image0ArrayList.size()) {
                                    characterBitmap = this.appDelegate.character1Image0ArrayList.get(characterID);
                                    if (characterUnit.imageView0TransformX <= BitmapDescriptorFactory.HUE_RED) {
                                        characterBitmapDrawType = 1;
                                    }
                                }
                            }
                            if (characterBitmap != null) {
                                int imgOffsetX = (int) (this.mainGameUnit.GAMEZONEVIEW_OFFSET_X + characterUnit.frameOriginX + characterUnit.imageView0OriginX);
                                int imgOffsetY = (int) (this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y + characterUnit.frameOriginY + characterUnit.imageView0OriginY);
                                if (characterUnit.imageView0Alpha >= 1.0f) {
                                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                } else {
                                    bitmapPaint.setAlpha((int) (255.0f * characterUnit.imageView0Alpha));
                                }
                                canvas.save();
                                if (characterBitmapDrawType == 1) {
                                    canvas.scale(-1.0f, 1.0f, imgOffsetX + (characterUnit.imageView0SizeWidth / 2.0f), BitmapDescriptorFactory.HUE_RED);
                                }
                                canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect(imgOffsetX, imgOffsetY, (int) (imgOffsetX + characterUnit.imageView0SizeWidth), (int) (imgOffsetY + characterUnit.imageView0SizeHeight)), bitmapPaint);
                                canvas.restore();
                            }
                        }
                        if (!characterUnit.imageView1Hidden) {
                            Bitmap eggBitmap = null;
                            if (nowCharacterUnitDictionary != null) {
                                short eggId2 = nowCharacterUnitDictionary.getEggId();
                                if (eggId2 == 0) {
                                    if (this.appDelegate.egg0ImageArrayList != null && characterUnit.imageView1Image >= 0 && characterUnit.imageView1Image < this.appDelegate.egg0ImageArrayList.size()) {
                                        eggBitmap = this.appDelegate.egg0ImageArrayList.get(characterUnit.imageView1Image);
                                    }
                                } else if (eggId2 == 1 && this.appDelegate.egg1ImageArrayList != null && characterUnit.imageView1Image >= 0 && characterUnit.imageView1Image < this.appDelegate.egg1ImageArrayList.size()) {
                                    eggBitmap = this.appDelegate.egg1ImageArrayList.get(characterUnit.imageView1Image);
                                }
                            }
                            if (eggBitmap != null) {
                                int imgOffsetX2 = (int) (this.mainGameUnit.GAMEZONEVIEW_OFFSET_X + characterUnit.frameOriginX + characterUnit.imageView1OriginX);
                                int imgOffsetY2 = (int) (this.mainGameUnit.GAMEZONEVIEW_OFFSET_Y + characterUnit.frameOriginY + characterUnit.imageView1OriginY);
                                if (characterUnit.imageView1Alpha >= 1.0f) {
                                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                } else {
                                    bitmapPaint.setAlpha((int) (255.0f * characterUnit.imageView1Alpha));
                                }
                                canvas.save();
                                if (characterUnit.imageView1TransformX < BitmapDescriptorFactory.HUE_RED) {
                                    canvas.scale(-1.0f, 1.0f, imgOffsetX2 + (characterUnit.imageView1SizeWidth / 2.0f), BitmapDescriptorFactory.HUE_RED);
                                }
                                canvas.drawBitmap(eggBitmap, new Rect(0, 0, eggBitmap.getWidth(), eggBitmap.getHeight()), new Rect(imgOffsetX2, imgOffsetY2, (int) (imgOffsetX2 + characterUnit.imageView1SizeWidth), (int) (imgOffsetY2 + characterUnit.imageView1SizeHeight)), bitmapPaint);
                                canvas.restore();
                            }
                        }
                    }
                }
            }
            if (this.backGroundFrontBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.backGroundFrontBitmap, new Rect(0, 0, this.backGroundFrontBitmap.getWidth(), this.backGroundFrontBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.finalHeight), bitmapPaint);
            }
            if (this.mainGameUnit.tool_2_SelectView != null) {
                float now_tool_2_SelectdImageOffsetX = this.tool_2_SelectdImageOffsetX;
                if (this.mainGameUnit.tool_2_SelectView.tool_2_0 >= 0 && this.appDelegate.tool2Level0Image1ArrayList != null && this.mainGameUnit.tool_2_SelectView.tool_2_0 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap6 = this.appDelegate.tool2Level0Image1ArrayList.get(this.mainGameUnit.tool_2_SelectView.tool_2_0)) != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(toolBitmap6, new Rect(0, 0, toolBitmap6.getWidth(), toolBitmap6.getHeight()), new Rect((int) now_tool_2_SelectdImageOffsetX, (int) this.tool_2_SelectdImageOffsetY, (int) (this.tool_2_SelectdImageWidth + now_tool_2_SelectdImageOffsetX), (int) (this.tool_2_SelectdImageOffsetY + this.tool_2_SelectdImageHeight)), bitmapPaint);
                    now_tool_2_SelectdImageOffsetX += this.tool_2_SelectdImageWidth;
                }
                if (this.mainGameUnit.tool_2_SelectView.tool_2_1 >= 0 && this.appDelegate.tool2Level0Image1ArrayList != null && this.mainGameUnit.tool_2_SelectView.tool_2_1 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap5 = this.appDelegate.tool2Level0Image1ArrayList.get(this.mainGameUnit.tool_2_SelectView.tool_2_1)) != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(toolBitmap5, new Rect(0, 0, toolBitmap5.getWidth(), toolBitmap5.getHeight()), new Rect((int) now_tool_2_SelectdImageOffsetX, (int) this.tool_2_SelectdImageOffsetY, (int) (this.tool_2_SelectdImageWidth + now_tool_2_SelectdImageOffsetX), (int) (this.tool_2_SelectdImageOffsetY + this.tool_2_SelectdImageHeight)), bitmapPaint);
                    now_tool_2_SelectdImageOffsetX += this.tool_2_SelectdImageWidth;
                }
                if (this.mainGameUnit.tool_2_SelectView.tool_2_2 >= 0 && this.appDelegate.tool2Level0Image1ArrayList != null && this.mainGameUnit.tool_2_SelectView.tool_2_2 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap4 = this.appDelegate.tool2Level0Image1ArrayList.get(this.mainGameUnit.tool_2_SelectView.tool_2_2)) != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(toolBitmap4, new Rect(0, 0, toolBitmap4.getWidth(), toolBitmap4.getHeight()), new Rect((int) now_tool_2_SelectdImageOffsetX, (int) this.tool_2_SelectdImageOffsetY, (int) (this.tool_2_SelectdImageWidth + now_tool_2_SelectdImageOffsetX), (int) (this.tool_2_SelectdImageOffsetY + this.tool_2_SelectdImageHeight)), bitmapPaint);
                    float f = now_tool_2_SelectdImageOffsetX + this.tool_2_SelectdImageWidth;
                }
            }
            if (this.appDelegate.timeSaveDictionary != null && (tool1SelectViewNowButtonIndex = this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex()) >= 0 && (toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, tool1SelectViewNowButtonIndex)) != null && (nowTool1Level = toolUnitDictionary.getLevel()) >= 0) {
                Bitmap toolBitmap7 = null;
                if (nowTool1Level == 0) {
                    if (this.appDelegate.tool1Level0Image1ArrayList != null && tool1SelectViewNowButtonIndex >= 0 && tool1SelectViewNowButtonIndex < this.appDelegate.tool1Level0Image1ArrayList.size()) {
                        toolBitmap7 = this.appDelegate.tool1Level0Image1ArrayList.get(tool1SelectViewNowButtonIndex);
                    }
                } else if (nowTool1Level == 1) {
                    if (this.appDelegate.tool1Level1Image1ArrayList != null && tool1SelectViewNowButtonIndex >= 0 && tool1SelectViewNowButtonIndex < this.appDelegate.tool1Level1Image1ArrayList.size()) {
                        toolBitmap7 = this.appDelegate.tool1Level1Image1ArrayList.get(tool1SelectViewNowButtonIndex);
                    }
                } else if (nowTool1Level == 2 && this.appDelegate.tool1Level2Image1ArrayList != null && tool1SelectViewNowButtonIndex >= 0 && tool1SelectViewNowButtonIndex < this.appDelegate.tool1Level2Image1ArrayList.size()) {
                    toolBitmap7 = this.appDelegate.tool1Level2Image1ArrayList.get(tool1SelectViewNowButtonIndex);
                }
                if (toolBitmap7 != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date nowDate = new Date();
                    String startDateString = this.appDelegate.timeSaveDictionary.getTool1SelectViewStartDate();
                    float endSeconds = this.appDelegate.timeSaveDictionary.getTool1SelectViewEndSeconds();
                    this.gaugeRate = -1.0f;
                    if (startDateString != null && endSeconds > 1.0d && startDateString.length() > 0) {
                        this.appDelegate.getClass();
                        float seido = endSeconds / 20.0f;
                        Date startDate = null;
                        try {
                            startDate = sdf.parse(startDateString);
                        } catch (ParseException e) {
                        }
                        if (startDate != null) {
                            int i2 = 0;
                            while (true) {
                                float f2 = i2;
                                this.appDelegate.getClass();
                                if (f2 >= 20.0f + 1.0f) {
                                    break;
                                }
                                long time = nowDate.getTime();
                                this.appDelegate.getClass();
                                if (!startDate.before(new Date(time - (((long) (((20.0f + BitmapDescriptorFactory.HUE_RED) - i2) * seido)) * 1000)))) {
                                    i2++;
                                } else {
                                    this.appDelegate.getClass();
                                    this.gaugeRate = i2 / 20.0f;
                                    break;
                                }
                            }
                        }
                    }
                    MyDraw.drawOnly2StrokeRect(canvas, this.timeGaugeBackRectOffsetX, this.timeGaugeBackRectOffsetY, this.timeGaugeBackRectWidth, this.timeGaugeBackRectHeight, this.timeGaugeBackRectColor0, this.timeGaugeBackRectStrokeWidth1, this.timeGaugeBackRectColor1, this.timeGaugeBackRectStrokeWidth2, this.timeGaugeBackRectColor2, this.timeGaugeBackRectStrokeWidth3, this.timeGaugeBackRectColor3, this.timeGaugeBackRectRadius);
                    float drawTimeGaugeRedLineWidth = BitmapDescriptorFactory.HUE_RED;
                    if (this.gaugeRate > BitmapDescriptorFactory.HUE_RED) {
                        drawTimeGaugeRedLineWidth = this.timeGaugeRedLineWidth * this.gaugeRate;
                    }
                    MyDraw.drawStrokeRect(canvas, this.timeGaugeRedLineOffsetX, this.timeGaugeRedLineOffsetY, drawTimeGaugeRedLineWidth, this.timeGaugeRedLineHeight, this.timeGaugeRedLineColor0, this.timeGaugeRedLineStrokeWidth1, this.timeGaugeRedLineColor1, this.timeGaugeRedLineStrokeWidth2, this.timeGaugeRedLineColor2, this.timeGaugeRedLineStrokeWidth3, this.timeGaugeRedLineColor3, this.timeGaugeRedLineRadius);
                    if (this.gaugeRate > BitmapDescriptorFactory.HUE_RED) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        canvas.drawBitmap(toolBitmap7, new Rect(0, 0, toolBitmap7.getWidth(), toolBitmap7.getHeight()), new Rect((int) this.tool_1_ImageOffsetX, (int) this.tool_1_ImageOffsetY, (int) (this.tool_1_ImageOffsetX + this.tool_1_ImageWidth), (int) (this.tool_1_ImageOffsetY + this.tool_1_ImageHeight)), bitmapPaint);
                        short tool_2_0 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_0Index();
                        short tool_2_1 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_1Index();
                        short tool_2_2 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_2Index();
                        float now_tool_2_ImageOffsetX = this.tool_2_ImageOffsetX;
                        if (tool_2_2 < 0) {
                            if (tool_2_1 >= 0) {
                                now_tool_2_ImageOffsetX = this.tool_2_ImageOffsetX + ((this.tool_2_ImageWidth / 2.0f) * 1.0f);
                            } else if (tool_2_0 >= 0) {
                                now_tool_2_ImageOffsetX = this.tool_2_ImageOffsetX + ((this.tool_2_ImageWidth / 2.0f) * 2.0f);
                            }
                        }
                        if (tool_2_0 >= 0) {
                            if (this.appDelegate.tool2Level0Image1ArrayList != null && tool_2_0 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap3 = this.appDelegate.tool2Level0Image1ArrayList.get(tool_2_0)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.drawBitmap(toolBitmap3, new Rect(0, 0, toolBitmap3.getWidth(), toolBitmap3.getHeight()), new Rect((int) now_tool_2_ImageOffsetX, (int) this.tool_2_ImageOffsetY, (int) (this.tool_2_ImageWidth + now_tool_2_ImageOffsetX), (int) (this.tool_2_ImageOffsetY + this.tool_2_ImageHeight)), bitmapPaint);
                                now_tool_2_ImageOffsetX += this.tool_2_ImageWidth;
                            }
                        }
                        if (tool_2_1 >= 0) {
                            if (this.appDelegate.tool2Level0Image1ArrayList != null && tool_2_1 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap2 = this.appDelegate.tool2Level0Image1ArrayList.get(tool_2_1)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.drawBitmap(toolBitmap2, new Rect(0, 0, toolBitmap2.getWidth(), toolBitmap2.getHeight()), new Rect((int) now_tool_2_ImageOffsetX, (int) this.tool_2_ImageOffsetY, (int) (this.tool_2_ImageWidth + now_tool_2_ImageOffsetX), (int) (this.tool_2_ImageOffsetY + this.tool_2_ImageHeight)), bitmapPaint);
                                now_tool_2_ImageOffsetX += this.tool_2_ImageWidth;
                            }
                        }
                        if (tool_2_2 >= 0) {
                            if (this.appDelegate.tool2Level0Image1ArrayList != null && tool_2_2 < this.appDelegate.tool2Level0Image1ArrayList.size() && (toolBitmap = this.appDelegate.tool2Level0Image1ArrayList.get(tool_2_2)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.drawBitmap(toolBitmap, new Rect(0, 0, toolBitmap.getWidth(), toolBitmap.getHeight()), new Rect((int) now_tool_2_ImageOffsetX, (int) this.tool_2_ImageOffsetY, (int) (this.tool_2_ImageWidth + now_tool_2_ImageOffsetX), (int) (this.tool_2_ImageOffsetY + this.tool_2_ImageHeight)), bitmapPaint);
                                float f3 = now_tool_2_ImageOffsetX + this.tool_2_ImageWidth;
                            }
                        }
                        if (this.appDelegate.defaultSharedPreferences != null) {
                            if (this.appDelegate.defaultSharedPreferences.getBoolean("cook_alarm", false)) {
                                if (this.alarmButtonBitmap0 != null) {
                                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                    canvas.drawBitmap(this.alarmButtonBitmap0, new Rect(0, 0, this.alarmButtonBitmap0.getWidth(), this.alarmButtonBitmap0.getHeight()), new Rect((int) this.alarmButtonOffsetX, (int) this.alarmButtonOffsetY, (int) (this.alarmButtonOffsetX + this.alarmButtonWidth), (int) (this.alarmButtonOffsetY + this.alarmButtonHeight)), bitmapPaint);
                                }
                            } else if (this.alarmButtonBitmap1 != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.drawBitmap(this.alarmButtonBitmap1, new Rect(0, 0, this.alarmButtonBitmap1.getWidth(), this.alarmButtonBitmap1.getHeight()), new Rect((int) this.alarmButtonOffsetX, (int) this.alarmButtonOffsetY, (int) (this.alarmButtonOffsetX + this.alarmButtonWidth), (int) (this.alarmButtonOffsetY + this.alarmButtonHeight)), bitmapPaint);
                            }
                        }
                    } else {
                        MyDraw.drawStrokeText(canvas, this.timeGaugeBatsuLabelOffsetX, this.timeGaugeBatsuLabelOffsetY, this.timeGaugeBatsuLabelTypeface, "X", this.timeGaugeBatsuLabelFontSize, this.timeGaugeBatsuLabelColor0, this.timeGaugeBatsuLabelStroke1Width, this.timeGaugeBatsuLabelColor1, this.timeGaugeBatsuLabelStroke2Width, this.timeGaugeBatsuLabelColor2);
                    }
                }
            }
            if (this.tool_2_SelectListOpenButtonStatus == 0) {
                MyDraw.drawStrokeRectWithShadow(canvas, this.tool_2_SelectListOpenButtonOffsetX, this.tool_2_SelectListOpenButtonOffsetY, this.tool_2_SelectListOpenButtonWidth, this.tool_2_SelectListOpenButtonHeight, this.tool_2_SelectListOpenButtonColor0, this.tool_2_SelectListOpenButtonStrokeWidth1, this.tool_2_SelectListOpenButtonColor1, this.tool_2_SelectListOpenButtonStrokeWidth2, this.tool_2_SelectListOpenButtonColor2, this.tool_2_SelectListOpenButtonStrokeWidth3, this.tool_2_SelectListOpenButtonColor3, this.tool_2_SelectListOpenButtonRadius, this.tool_2_SelectListOpenButtonShadowOpacity, this.tool_2_SelectListOpenButtonShadowOffsetX, this.tool_2_SelectListOpenButtonShadowOffsetY, this.tool_2_SelectListOpenButtonShadowColor);
                if (this.tool_2_SelectListOpenButtonBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.tool_2_SelectListOpenButtonBitmap, new Rect(0, 0, this.tool_2_SelectListOpenButtonBitmap.getWidth(), this.tool_2_SelectListOpenButtonBitmap.getHeight()), new Rect((int) this.tool_2_SelectListOpenBitmapButtonOffsetX, (int) this.tool_2_SelectListOpenBitmapButtonOffsetY, (int) (this.tool_2_SelectListOpenBitmapButtonOffsetX + this.tool_2_SelectListOpenBitmapButtonWidth), (int) (this.tool_2_SelectListOpenBitmapButtonOffsetY + this.tool_2_SelectListOpenBitmapButtonHeight)), bitmapPaint);
                }
                if (this.touchButtonIndex == 0) {
                    MyDraw.drawStrokeRect(canvas, this.tool_2_SelectListOpenButtonOffsetX, this.tool_2_SelectListOpenButtonOffsetY, this.tool_2_SelectListOpenButtonWidth, this.tool_2_SelectListOpenButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.tool_2_SelectListOpenButtonRadius);
                }
            }
            if (this.fixKitchenButtonStatus == 0) {
                if (this.touchButtonIndex == 2) {
                    if (this.fixKitchenButtonBitmap1 != null) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        canvas.drawBitmap(this.fixKitchenButtonBitmap1, new Rect(0, 0, this.fixKitchenButtonBitmap1.getWidth(), this.fixKitchenButtonBitmap1.getHeight()), new Rect((int) this.fixKitchenButtonOffsetX, (int) this.fixKitchenButtonOffsetY, (int) (this.fixKitchenButtonOffsetX + this.fixKitchenButtonWidth), (int) (this.fixKitchenButtonOffsetY + this.fixKitchenButtonHeight)), bitmapPaint);
                    }
                } else if (this.fixKitchenButtonBitmap0 != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.fixKitchenButtonBitmap0, new Rect(0, 0, this.fixKitchenButtonBitmap0.getWidth(), this.fixKitchenButtonBitmap0.getHeight()), new Rect((int) this.fixKitchenButtonOffsetX, (int) this.fixKitchenButtonOffsetY, (int) (this.fixKitchenButtonOffsetX + this.fixKitchenButtonWidth), (int) (this.fixKitchenButtonOffsetY + this.fixKitchenButtonHeight)), bitmapPaint);
                }
            }
            short nowLevel = this.appDelegate.getTool0LevelWithIndex((short) 0);
            if (nowLevel >= 0) {
                String levelLabelString = new StringBuilder().append(nowLevel + 1).toString();
                Paint newPaint = new Paint(257);
                newPaint.setTypeface(this.levelLabelTypeface);
                newPaint.setTextSize(this.levelLabelFontSize);
                float nowLevelLabelOffsetY = this.levelLabelOffsetX - (newPaint.measureText(levelLabelString) / 2.0f);
                if (this.appDelegate.starOnBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.appDelegate.starOnBitmap, new Rect(0, 0, this.appDelegate.starOnBitmap.getWidth(), this.appDelegate.starOnBitmap.getHeight()), new Rect((int) this.starOffsetX, (int) this.starOffsetY, (int) (this.starOffsetX + this.starWidth), (int) (this.starOffsetY + this.starHeight)), bitmapPaint);
                }
                if (levelLabelString.length() > 0) {
                    MyDraw.drawStrokeText(canvas, nowLevelLabelOffsetY, this.levelLabelOffsetY, this.levelLabelTypeface, levelLabelString, this.levelLabelFontSize, -1, this.levelLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.levelLabelStroke2Width, 214958079);
                }
            }
            if (this.levelUpButtonStatus == 0) {
                if (this.levelUpButtonBitmap0 != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    canvas.drawBitmap(this.levelUpButtonBitmap0, new Rect(0, 0, this.levelUpButtonBitmap0.getWidth(), this.levelUpButtonBitmap0.getHeight()), new Rect((int) this.levelUpButtonOffsetX, (int) this.levelUpButtonOffsetY, (int) (this.levelUpButtonOffsetX + this.levelUpButtonWidth), (int) (this.levelUpButtonOffsetY + this.levelUpButtonHeight)), bitmapPaint);
                    String levelUpLabelString = new StringBuilder().append(nowLevel + 2).toString();
                    Paint newPaint2 = new Paint(257);
                    newPaint2.setTypeface(this.levelUpLabelTypeface);
                    newPaint2.setTextSize(this.levelUpLabelFontSize);
                    float nowLevelUpLabelOffsetY = this.levelLabelOffsetX - (newPaint2.measureText(levelUpLabelString) / 2.0f);
                    if (levelUpLabelString.length() > 0) {
                        MyDraw.drawStrokeText(canvas, nowLevelUpLabelOffsetY, this.levelUpLabelOffsetX, this.levelUpLabelTypeface, levelUpLabelString, this.levelUpLabelFontSize, -1, this.levelUpLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.levelUpLabelStroke2Width, -855638017);
                    }
                }
                if (this.touchButtonIndex == 1 && this.levelUpButtonBitmap1 != null) {
                    bitmapPaint.setAlpha(128);
                    canvas.drawBitmap(this.levelUpButtonBitmap1, new Rect(0, 0, this.levelUpButtonBitmap1.getWidth(), this.levelUpButtonBitmap1.getHeight()), new Rect((int) this.levelUpButtonOffsetX, (int) this.levelUpButtonOffsetY, (int) (this.levelUpButtonOffsetX + this.levelUpButtonWidth), (int) (this.levelUpButtonOffsetY + this.levelUpButtonHeight)), bitmapPaint);
                }
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        clearBitmap();
        this.mainGameUnit = null;
        this.appDelegate = null;
    }
}
