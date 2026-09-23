package com.idtinc.maingame.sublayout2;

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
public class StoreListSelectUnit {
    private AppDelegate appDelegate;
    public float button0OffsetX;
    public float button0TitleLabelOffsetX;
    public float button1OffsetX;
    public float button1TitleLabelOffsetX;
    private short buttonClickCnt;
    public int buttonColor0;
    public int buttonColor1;
    public int buttonColor2;
    public int buttonColor3;
    public float buttonHeight;
    public float buttonOffsetY;
    public float buttonRadius;
    public float buttonStrokeWidth1;
    public float buttonStrokeWidth2;
    public float buttonStrokeWidth3;
    public int buttonTitleLabelColor0;
    public int buttonTitleLabelColor1;
    public int buttonTitleLabelColor2;
    public float buttonTitleLabelFontSize;
    public float buttonTitleLabelOffsetY;
    public float buttonTitleLabelStroke1Width;
    public float buttonTitleLabelStroke2Width;
    Typeface buttonTitleLabelTypeface;
    public float buttonWidth;
    private float finalHeight;
    private float finalWidth;
    private short leftButtonStatus;
    private MyDraw myDraw;
    public short nowListIndex;
    private short rightButtonStatus;
    private StoreUnit storeUnit;
    private int titleBackRectColor0;
    private int titleBackRectColor1;
    private int titleBackRectColor2;
    private int titleBackRectColor3;
    private float titleBackRectHeight;
    private float titleBackRectOffsetX;
    private float titleBackRectOffsetY;
    private float titleBackRectRadius;
    private float titleBackRectStrokeWidth1;
    private float titleBackRectStrokeWidth2;
    private float titleBackRectStrokeWidth3;
    private float titleBackRectWidth;
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
    private short touchButtonIndex;
    private float zoomRate;

    public StoreListSelectUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, StoreUnit _storeUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowListIndex = (short) -1;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.leftButtonStatus = (short) -1;
        this.rightButtonStatus = (short) -1;
        this.titleLabelString = "";
        this.titleBackRectOffsetX = 58.0f;
        this.titleBackRectOffsetY = 13.0f;
        this.titleBackRectWidth = 180.0f;
        this.titleBackRectHeight = 26.0f;
        this.titleBackRectColor0 = 855638016;
        this.titleBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor1 = 0;
        this.titleBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor2 = 0;
        this.titleBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor3 = 0;
        this.titleBackRectRadius = 6.0f;
        this.titleLabelFontSize = 22.0f;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = 26.0f;
        this.buttonHeight = 26.0f;
        this.button0OffsetX = 30.0f;
        this.button1OffsetX = 240.0f;
        this.buttonOffsetY = 13.0f;
        this.buttonColor0 = -3171731;
        this.buttonStrokeWidth1 = 2.0f;
        this.buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.buttonColor2 = 0;
        this.buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.buttonColor3 = 0;
        this.buttonRadius = 6.0f;
        this.buttonTitleLabelFontSize = 20.0f;
        this.buttonTitleLabelColor0 = -1;
        this.buttonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor1 = 0;
        this.buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor2 = 0;
        this.button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = null;
        this.storeUnit = null;
        this.appDelegate = _appDelegate;
        this.storeUnit = _storeUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.nowListIndex = (short) -1;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.leftButtonStatus = (short) -1;
        this.rightButtonStatus = (short) -1;
        this.titleLabelString = "";
        this.titleBackRectOffsetX = 70.0f * this.zoomRate;
        this.titleBackRectOffsetY = (13.0f * this.zoomRate) + _offsetY;
        this.titleBackRectWidth = 180.0f * this.zoomRate;
        this.titleBackRectHeight = this.zoomRate * 26.0f;
        this.titleBackRectColor0 = 855638016;
        this.titleBackRectStrokeWidth1 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor1 = 0;
        this.titleBackRectStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor2 = 0;
        this.titleBackRectStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackRectColor3 = 0;
        this.titleBackRectRadius = 6.0f * this.zoomRate;
        this.titleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.titleLabelFontSize = 22.0f * this.zoomRate;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 3.0f * this.zoomRate;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = this.zoomRate * 26.0f;
        this.buttonHeight = this.zoomRate * 26.0f;
        this.button0OffsetX = 42.0f * this.zoomRate;
        this.button1OffsetX = 252.0f * this.zoomRate;
        this.buttonOffsetY = (13.0f * this.zoomRate) + _offsetY;
        this.buttonColor0 = -3171731;
        this.buttonStrokeWidth1 = this.zoomRate * 2.0f;
        this.buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonColor2 = 0;
        this.buttonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonColor3 = 0;
        this.buttonRadius = 6.0f * this.zoomRate;
        this.buttonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.buttonTitleLabelFontSize = 20.0f * this.zoomRate;
        this.buttonTitleLabelColor0 = -1;
        this.buttonTitleLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor1 = 0;
        this.buttonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor2 = 0;
        this.button0TitleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.button1TitleLabelOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.button0TitleLabelOffsetX = this.button0OffsetX + ((this.buttonWidth - newPaint.measureText("<")) / 2.0f);
        this.button1TitleLabelOffsetX = this.button1OffsetX + ((this.buttonWidth - newPaint.measureText(">")) / 2.0f);
        this.buttonTitleLabelOffsetY = (this.buttonOffsetY + this.buttonHeight) - (this.buttonTitleLabelFontSize * 0.3f);
        this.myDraw = new MyDraw();
    }

    public void changeListIndex(short _listIndex) {
        if (_listIndex == -1) {
            this.nowListIndex = (short) -1;
        } else if (_listIndex == 0) {
            this.nowListIndex = (short) 0;
        } else if (_listIndex == 1) {
            this.nowListIndex = (short) 1;
        } else if (_listIndex == 2) {
            this.nowListIndex = (short) 2;
        } else {
            this.nowListIndex = (short) 0;
        }
        this.titleLabelString = "";
        this.leftButtonStatus = (short) -1;
        this.rightButtonStatus = (short) -1;
        if (this.nowListIndex == 0) {
            this.titleLabelString = this.appDelegate.getResources().getString(R.string.Ingredient);
            this.rightButtonStatus = (short) 0;
        } else if (this.nowListIndex == 1) {
            this.titleLabelString = this.appDelegate.getResources().getString(R.string.Kitchenware);
            this.leftButtonStatus = (short) 0;
            this.rightButtonStatus = (short) 0;
        } else if (this.nowListIndex == 2) {
            this.titleLabelString = this.appDelegate.getResources().getString(R.string.Egg);
            this.leftButtonStatus = (short) 0;
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = this.titleBackRectOffsetX + ((this.titleBackRectWidth - newPaint.measureText(this.titleLabelString)) / 2.0f);
        this.titleLabelOffsetY = (this.titleBackRectOffsetY + this.titleBackRectHeight) - (this.titleLabelFontSize * 0.2f);
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("StoreListSelectLayout", "onTouchEvent");
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.leftButtonStatus == 0 && event.getY() > this.buttonOffsetY && event.getY() < this.buttonOffsetY + this.buttonHeight && event.getX() > this.button0OffsetX && event.getX() < this.button0OffsetX + this.buttonWidth) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 3;
                Log.d("StoreListSelectLayout", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("StoreListSelectLayout", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreListSelectUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StoreListSelectUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.rightButtonStatus == 0 && event.getY() > this.buttonOffsetY && event.getY() < this.buttonOffsetY + this.buttonHeight && event.getX() > this.button1OffsetX && event.getX() < this.button1OffsetX + this.buttonWidth) {
                this.touchButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 3;
                Log.d("StoreListSelectLayout", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("StoreListSelectLayout", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreListSelectUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        StoreListSelectUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }

    public void doClick() {
        if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 1) {
            if (this.touchButtonIndex == 0) {
                if (this.nowListIndex == 1) {
                    this.storeUnit.changeListTo((short) 2, false);
                    this.appDelegate.doSoundPoolPlay(2);
                } else if (this.nowListIndex == 2) {
                    this.storeUnit.changeListTo((short) 1, false);
                    this.appDelegate.doSoundPoolPlay(2);
                }
            } else if (this.touchButtonIndex == 1) {
                if (this.nowListIndex == 0) {
                    this.storeUnit.changeListTo((short) 1, true);
                    this.appDelegate.doSoundPoolPlay(2);
                } else if (this.nowListIndex == 1) {
                    this.storeUnit.changeListTo((short) 3, false);
                    this.appDelegate.doSoundPoolPlay(2);
                }
            }
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        MyDraw.drawStrokeRect(canvas, this.titleBackRectOffsetX, this.titleBackRectOffsetY, this.titleBackRectWidth, this.titleBackRectHeight, this.titleBackRectColor0, this.titleBackRectStrokeWidth1, this.titleBackRectColor1, this.titleBackRectStrokeWidth2, this.titleBackRectColor2, this.titleBackRectStrokeWidth3, this.titleBackRectColor3, this.titleBackRectRadius);
        MyDraw.drawStrokeText(canvas, this.titleLabelOffsetX, this.titleLabelOffsetY, this.titleLabelTypeface, this.titleLabelString, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        if (this.leftButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.button0OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius);
            MyDraw.drawStrokeText(canvas, this.button0TitleLabelOffsetX, this.buttonTitleLabelOffsetY, this.buttonTitleLabelTypeface, "<", this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
            if (this.touchButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.button0OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, -1711276033, this.buttonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
            }
        }
        if (this.rightButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.button1OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius);
            MyDraw.drawStrokeText(canvas, this.button1TitleLabelOffsetX, this.buttonTitleLabelOffsetY, this.buttonTitleLabelTypeface, ">", this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
            if (this.touchButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.button1OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, -1711276033, this.buttonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.storeUnit = null;
        this.appDelegate = null;
    }
}
