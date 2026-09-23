package com.idtinc.maingame.sublayout0;

import android.graphics.Canvas;
import android.graphics.Paint;
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
public class Tool_2_SelectListFrontViewUnit {
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
    private int countLabel0Color0;
    private int countLabel0Color1;
    private int countLabel0Color2;
    private float countLabel0FontSize;
    private float countLabel0OffsetX;
    private float countLabel0OffsetY;
    private String countLabel0String;
    private float countLabel0Stroke1Width;
    private float countLabel0Stroke2Width;
    Typeface countLabel0Typeface;
    private int countLabel1Color0;
    private int countLabel1Color1;
    private int countLabel1Color2;
    private float countLabel1FontSize;
    private float countLabel1OffsetX;
    private float countLabel1OffsetY;
    private String countLabel1String;
    private float countLabel1Stroke1Width;
    private float countLabel1Stroke2Width;
    Typeface countLabel1Typeface;
    private float finalHeight;
    private float finalWidth;
    private MyDraw myDraw;
    public int okButtonColor0;
    public int okButtonColor1;
    public int okButtonColor2;
    public int okButtonColor3;
    public float okButtonHeight;
    public float okButtonOffsetX;
    public float okButtonOffsetY;
    public float okButtonRadius;
    public short okButtonStatus;
    public float okButtonStrokeWidth1;
    public float okButtonStrokeWidth2;
    public float okButtonStrokeWidth3;
    public int okButtonTitleLabelColor0;
    public int okButtonTitleLabelColor1;
    public int okButtonTitleLabelColor2;
    public float okButtonTitleLabelFontSize;
    public float okButtonTitleLabelOffsetX;
    public float okButtonTitleLabelOffsetY;
    public String okButtonTitleLabelString;
    public float okButtonTitleLabelStroke1Width;
    public float okButtonTitleLabelStroke2Width;
    Typeface okButtonTitleLabelTypeface;
    public float okButtonWidth;
    private int titleLabelColor0;
    private int titleLabelColor1;
    private int titleLabelColor2;
    private float titleLabelFontSize;
    private float titleLabelOffsetX;
    private float titleLabelOffsetY;
    private String titleLabelString;
    private float titleLabelStroke1Width;
    private float titleLabelStroke2Width;
    Typeface titleLabelTypeface;
    private Tool_2_SelectListUnit tool_2_SelectListUnit;
    private short touchButtonIndex;
    private float zoomRate;

    public Tool_2_SelectListFrontViewUnit(float _finalwidth, float _finalheight, float _zoomrate, Tool_2_SelectListUnit _tool_2_SelectListUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) -1;
        this.okButtonStatus = (short) -1;
        this.countLabel0String = "";
        this.countLabel0FontSize = 28.0f;
        this.countLabel0Color0 = -1;
        this.countLabel0Stroke1Width = 3.0f;
        this.countLabel0Color1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.countLabel0Stroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.countLabel0Color2 = 0;
        this.countLabel0OffsetX = BitmapDescriptorFactory.HUE_RED;
        this.countLabel0OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.countLabel1String = "";
        this.countLabel1FontSize = 28.0f;
        this.countLabel1Color0 = -5850;
        this.countLabel1Stroke1Width = 3.0f;
        this.countLabel1Color1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.countLabel1Stroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.countLabel1Color2 = 0;
        this.countLabel1OffsetX = BitmapDescriptorFactory.HUE_RED;
        this.countLabel1OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelString = "";
        this.titleLabelFontSize = 28.0f;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonWidth = 30.0f;
        this.closeButtonHeight = 30.0f;
        this.closeButtonOffsetX = 9.0f;
        this.closeButtonOffsetY = 49.0f;
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
        this.okButtonWidth = 110.0f;
        this.okButtonHeight = 32.0f;
        this.okButtonOffsetX = 160.0f;
        this.okButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.okButtonColor0 = -227838;
        this.okButtonStrokeWidth1 = 2.0f;
        this.okButtonColor1 = -16;
        this.okButtonStrokeWidth2 = 2.0f;
        this.okButtonColor2 = -7576502;
        this.okButtonStrokeWidth3 = 1.0f;
        this.okButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.okButtonRadius = 10.0f;
        this.okButtonTitleLabelString = "";
        this.okButtonTitleLabelFontSize = 22.0f;
        this.okButtonTitleLabelColor0 = -1;
        this.okButtonTitleLabelStroke1Width = 2.0f;
        this.okButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.okButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.okButtonTitleLabelColor2 = 0;
        this.okButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.okButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = _appDelegate;
        this.tool_2_SelectListUnit = _tool_2_SelectListUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
        this.okButtonStatus = (short) 0;
        Paint newPaint = new Paint(257);
        this.countLabel0String = "";
        this.countLabel0Typeface = this.appDelegate.typeface_FONTNAME_00;
        this.countLabel0FontSize = 28.0f * this.zoomRate;
        this.countLabel0Color0 = -1;
        this.countLabel0Stroke1Width = 3.0f * this.zoomRate;
        this.countLabel0Color1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.countLabel0Stroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.countLabel0Color2 = 0;
        newPaint.setTypeface(this.countLabel0Typeface);
        newPaint.setTextSize(this.countLabel0FontSize);
        this.countLabel0OffsetX = 270.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.countLabel0OffsetY = (80.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        } else {
            this.countLabel0OffsetY = (124.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        }
        this.countLabel1String = "";
        this.countLabel1Typeface = this.appDelegate.typeface_FONTNAME_00;
        this.countLabel1FontSize = 28.0f * this.zoomRate;
        this.countLabel1Color0 = -5850;
        this.countLabel1Stroke1Width = 3.0f * this.zoomRate;
        this.countLabel1Color1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.countLabel1Stroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.countLabel1Color2 = 0;
        newPaint.setTypeface(this.countLabel1Typeface);
        newPaint.setTextSize(this.countLabel1FontSize);
        this.countLabel1OffsetX = (270.0f * this.zoomRate) - newPaint.measureText(this.countLabel1String);
        if (!this.appDelegate.isRetina4) {
            this.countLabel1OffsetY = (80.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        } else {
            this.countLabel1OffsetY = (124.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        }
        this.titleLabelString = this.appDelegate.getResources().getString(R.string.Ingredient);
        this.titleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.titleLabelFontSize = 28.0f * this.zoomRate;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f * this.zoomRate;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = ((320.0f * this.zoomRate) - newPaint.measureText(this.titleLabelString)) / 2.0f;
        if (!this.appDelegate.isRetina4) {
            this.titleLabelOffsetY = (80.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        } else {
            this.titleLabelOffsetY = (124.0f * this.zoomRate) - (this.countLabel0FontSize * 0.2f);
        }
        this.closeButtonWidth = 30.0f * this.zoomRate;
        this.closeButtonHeight = 30.0f * this.zoomRate;
        this.closeButtonOffsetX = 9.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.closeButtonOffsetY = 49.0f * this.zoomRate;
        } else {
            this.closeButtonOffsetY = 93.0f * this.zoomRate;
        }
        this.closeButtonColor0 = -2248316;
        this.closeButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.closeButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.closeButtonStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor2 = 0;
        this.closeButtonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor3 = 0;
        this.closeButtonRadius = 15.0f * this.zoomRate;
        this.closeButtonTitleLabelString = "X";
        this.closeButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.closeButtonTitleLabelFontSize = 22.0f * this.zoomRate;
        this.closeButtonTitleLabelColor0 = -16;
        this.closeButtonTitleLabelStroke1Width = this.zoomRate * 2.0f;
        this.closeButtonTitleLabelColor1 = -16;
        this.closeButtonTitleLabelStroke2Width = 0.5f * this.zoomRate;
        this.closeButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.closeButtonTitleLabelTypeface);
        newPaint.setTextSize(this.closeButtonTitleLabelFontSize);
        this.closeButtonTitleLabelOffsetX = this.closeButtonOffsetX + (1.0f * this.zoomRate) + ((this.closeButtonWidth - newPaint.measureText(this.closeButtonTitleLabelString)) / 2.0f);
        this.closeButtonTitleLabelOffsetY = (this.closeButtonOffsetY + this.closeButtonHeight) - (this.closeButtonTitleLabelFontSize * 0.4f);
        this.okButtonWidth = 110.0f * this.zoomRate;
        this.okButtonHeight = 32.0f * this.zoomRate;
        this.okButtonOffsetX = 105.0f * this.zoomRate;
        this.okButtonOffsetY = (this.tool_2_SelectListUnit.LISTBACKVIEW_OFFSET_Y + this.tool_2_SelectListUnit.LISTBACKVIEW_HEIGHT) - (42.0f * this.zoomRate);
        this.okButtonColor0 = -227838;
        this.okButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.okButtonColor1 = -16;
        this.okButtonStrokeWidth2 = this.zoomRate * 2.0f;
        this.okButtonColor2 = -7576502;
        this.okButtonStrokeWidth3 = 1.0f * this.zoomRate;
        this.okButtonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.okButtonRadius = 10.0f * this.zoomRate;
        this.okButtonTitleLabelString = "OK";
        this.okButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.okButtonTitleLabelFontSize = 22.0f * this.zoomRate;
        this.okButtonTitleLabelColor0 = -1;
        this.okButtonTitleLabelStroke1Width = this.zoomRate * 2.0f;
        this.okButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.okButtonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.okButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.okButtonTitleLabelTypeface);
        newPaint.setTextSize(this.okButtonTitleLabelFontSize);
        this.okButtonTitleLabelOffsetX = ((320.0f * this.zoomRate) - newPaint.measureText(this.okButtonTitleLabelString)) / 2.0f;
        this.okButtonTitleLabelOffsetY = (this.okButtonOffsetY + this.okButtonHeight) - (this.okButtonTitleLabelFontSize * 0.4f);
        this.myDraw = new MyDraw();
    }

    public void resize() {
        this.okButtonOffsetY = (this.tool_2_SelectListUnit.LISTBACKVIEW_OFFSET_Y + this.tool_2_SelectListUnit.LISTBACKVIEW_HEIGHT) - (42.0f * this.zoomRate);
        this.okButtonTitleLabelOffsetY = (this.okButtonOffsetY + this.okButtonHeight) - (this.okButtonTitleLabelFontSize * 0.4f);
    }

    public void changeCountLabel0String(short _count) {
        this.countLabel0String = "";
        if (_count > 0) {
            this.countLabel0String = "/" + ((int) _count);
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.countLabel0Typeface);
        newPaint.setTextSize(this.countLabel0FontSize);
        this.countLabel0OffsetX = 270.0f * this.zoomRate;
    }

    public void changeCountLabel1String(short _count) {
        this.countLabel1String = "";
        if (_count >= 0) {
            this.countLabel1String = new StringBuilder().append((int) _count).toString();
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.countLabel1Typeface);
        newPaint.setTextSize(this.countLabel1FontSize);
        this.countLabel1OffsetX = (270.0f * this.zoomRate) - newPaint.measureText(this.countLabel1String);
    }

    public void reset() {
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
        this.okButtonStatus = (short) 0;
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmListFrontkView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("Tool_2_SelectListFrontView", "onTouchEvent");
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.closeButtonStatus == 0 && event.getY() > this.closeButtonOffsetY && event.getY() < this.closeButtonOffsetY + this.closeButtonHeight && event.getX() > this.closeButtonOffsetX && event.getX() < this.closeButtonOffsetX + this.closeButtonWidth) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 3;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.Tool_2_SelectListFrontViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Tool_2_SelectListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.okButtonStatus == 0 && event.getY() > this.okButtonOffsetY && event.getY() < this.okButtonOffsetY + this.okButtonHeight && event.getX() > this.okButtonOffsetX && event.getX() < this.okButtonOffsetX + this.okButtonWidth) {
                this.touchButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 3;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.Tool_2_SelectListFrontViewUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Tool_2_SelectListFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        } else {
            event.getAction();
        }
        return false;
    }

    public void doClick() {
        if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 1) {
            if (this.touchButtonIndex == 0) {
                this.appDelegate.doSoundPoolPlay(2);
                this.tool_2_SelectListUnit.close();
            } else if (this.touchButtonIndex == 1) {
                this.appDelegate.doSoundPoolPlay(1);
                this.tool_2_SelectListUnit.done();
            }
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        MyDraw.drawStrokeText(canvas, this.titleLabelOffsetX, this.titleLabelOffsetY, this.titleLabelTypeface, this.titleLabelString, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        MyDraw.drawStrokeText(canvas, this.countLabel0OffsetX, this.countLabel0OffsetY, this.countLabel0Typeface, this.countLabel0String, this.countLabel0FontSize, this.countLabel0Color0, this.countLabel0Stroke1Width, this.countLabel0Color1, this.countLabel0Stroke2Width, this.countLabel0Color2);
        MyDraw.drawStrokeText(canvas, this.countLabel1OffsetX, this.countLabel1OffsetY, this.countLabel1Typeface, this.countLabel1String, this.countLabel1FontSize, this.countLabel1Color0, this.countLabel1Stroke1Width, this.countLabel1Color1, this.countLabel1Stroke2Width, this.countLabel1Color2);
        if (this.closeButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, this.closeButtonColor0, this.closeButtonStrokeWidth1, this.closeButtonColor1, this.closeButtonStrokeWidth2, this.closeButtonColor2, this.closeButtonStrokeWidth3, this.closeButtonColor3, this.closeButtonRadius);
            MyDraw.drawStrokeText(canvas, this.closeButtonTitleLabelOffsetX, this.closeButtonTitleLabelOffsetY, this.closeButtonTitleLabelTypeface, this.closeButtonTitleLabelString, this.closeButtonTitleLabelFontSize, this.closeButtonTitleLabelColor0, this.closeButtonTitleLabelStroke1Width, this.closeButtonTitleLabelColor1, this.closeButtonTitleLabelStroke2Width, this.closeButtonTitleLabelColor2);
            if (this.touchButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.closeButtonRadius);
            }
        }
        if (this.okButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.okButtonOffsetX, this.okButtonOffsetY, this.okButtonWidth, this.okButtonHeight, this.okButtonColor0, this.okButtonStrokeWidth1, this.okButtonColor1, this.okButtonStrokeWidth2, this.okButtonColor2, this.okButtonStrokeWidth3, this.okButtonColor3, this.okButtonRadius);
            MyDraw.drawStrokeText(canvas, this.okButtonTitleLabelOffsetX, this.okButtonTitleLabelOffsetY, this.okButtonTitleLabelTypeface, this.okButtonTitleLabelString, this.okButtonTitleLabelFontSize, this.okButtonTitleLabelColor0, this.okButtonTitleLabelStroke1Width, this.okButtonTitleLabelColor1, this.okButtonTitleLabelStroke2Width, this.okButtonTitleLabelColor2);
            if (this.touchButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.okButtonOffsetX, this.okButtonOffsetY, this.okButtonWidth, this.okButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.okButtonRadius);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.tool_2_SelectListUnit = null;
        this.appDelegate = null;
    }
}
