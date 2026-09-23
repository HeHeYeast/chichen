package com.idtinc.maingame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class TopButtonsUnit {
    private float SELECTED_Y;
    public final float TOPBUTTONSVIEW_HEIGHT = 52.0f;
    private float UNSELECTED_Y;
    private AppDelegate appDelegate;
    private float button0OffsetY;
    private float button0TitleLabelOffsetX;
    public String button0TitleLabelString;
    private float button1OffsetY;
    private float button1TitleLabelOffsetX;
    public String button1TitleLabelString;
    private float button2OffsetY;
    private float button2TitleLabelOffsetX;
    public String button2TitleLabelString;
    private float button3OffsetY;
    private float button3TitleLabelOffsetX;
    public String button3TitleLabelString;
    private float buttonHeight;
    private float buttonOffsetX;
    private float buttonRadius;
    private float buttonSpaceX;
    private float buttonStrokeWidth1;
    private float buttonStrokeWidth2;
    private float buttonStrokeWidth3;
    private float buttonTitleLabelFontSize;
    private float buttonTitleLabelOffsetY;
    private float buttonTitleLabelStroke1Width;
    private float buttonTitleLabelStroke2Width;
    Typeface buttonTitleLabelTypeface;
    private float buttonWidth;
    public boolean controlF;
    private int fadeFrontViewAlpha;
    private Paint fadeFrontViewPaint;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MainGameViewController mainGameViewController;
    private MyDraw myDraw;
    public short nextButtonIndex;
    public short nowButtonIndex;
    public short nowStatus;
    public short oldButtonIndex;
    private float zoomRate;

    public TopButtonsUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameViewController, AppDelegate _appDelegate) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.controlF = false;
        this.nowStatus = (short) -1;
        this.nextButtonIndex = (short) -1;
        this.oldButtonIndex = (short) -1;
        this.nowButtonIndex = (short) -1;
        this.fadeFrontViewAlpha = 0;
        this.buttonOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button0OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.button1OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.button2OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.button3OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.buttonSpaceX = BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = BitmapDescriptorFactory.HUE_RED;
        this.buttonHeight = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.buttonRadius = BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelString = "";
        this.button1TitleLabelString = "";
        this.button2TitleLabelString = "";
        this.button3TitleLabelString = "";
        this.buttonTitleLabelFontSize = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button2TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button3TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.SELECTED_Y = -20.0f;
        this.UNSELECTED_Y = -26.0f;
        this.appDelegate = _appDelegate;
        this.mainGameViewController = _mainGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.controlF = false;
        this.nowStatus = (short) -1;
        this.nextButtonIndex = (short) -1;
        this.oldButtonIndex = (short) -1;
        this.nowButtonIndex = (short) -1;
        this.fadeFrontViewAlpha = 0;
        this.SELECTED_Y = (-20.0f) * this.zoomRate;
        this.UNSELECTED_Y = (-26.0f) * this.zoomRate;
        this.buttonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.button0OffsetY = this.UNSELECTED_Y;
        this.button1OffsetY = this.UNSELECTED_Y;
        this.button2OffsetY = this.UNSELECTED_Y;
        this.button3OffsetY = this.UNSELECTED_Y;
        this.buttonSpaceX = 80.0f * this.zoomRate;
        this.buttonWidth = 80.0f * this.zoomRate;
        this.buttonHeight = 52.0f * this.zoomRate;
        this.buttonStrokeWidth1 = this.zoomRate * 2.0f;
        this.buttonStrokeWidth2 = this.zoomRate * 2.0f;
        this.buttonStrokeWidth3 = this.zoomRate * 1.0f;
        this.buttonRadius = 10.0f * this.zoomRate;
        this.button0TitleLabelString = this.appDelegate.getResources().getString(R.string.Kitchen);
        this.button1TitleLabelString = this.appDelegate.getResources().getString(R.string.Farm);
        this.button2TitleLabelString = this.appDelegate.getResources().getString(R.string.Store);
        this.button3TitleLabelString = this.appDelegate.getResources().getString(R.string.Other);
        this.buttonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.buttonTitleLabelFontSize = 20.0f * this.zoomRate;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.buttonTitleLabelStroke1Width = 3.0f * this.zoomRate;
        this.buttonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.button0TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * BitmapDescriptorFactory.HUE_RED) + ((this.buttonWidth - newPaint.measureText(this.button0TitleLabelString)) / 2.0f);
        this.button1TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 1.0f) + ((this.buttonWidth - newPaint.measureText(this.button1TitleLabelString)) / 2.0f);
        this.button2TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 2.0f) + ((this.buttonWidth - newPaint.measureText(this.button2TitleLabelString)) / 2.0f);
        this.button3TitleLabelOffsetX = this.buttonOffsetX + (this.buttonSpaceX * 3.0f) + ((this.buttonWidth - newPaint.measureText(this.button3TitleLabelString)) / 2.0f);
        this.buttonTitleLabelOffsetY = 44.0f * this.zoomRate;
        this.fadeFrontViewPaint = new Paint(257);
        this.fadeFrontViewPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.fadeFrontViewPaint.setStyle(Paint.Style.FILL);
        this.myDraw = new MyDraw();
        changeNowStatus(-1);
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void start() {
        changeNowStatus(0);
    }

    public void selectButtonWithIndex(short _buttonIndex, short _nextStatus) {
        this.nextButtonIndex = (short) -1;
        this.mainGameViewController.hiddenSubViews();
        if (_nextStatus == 1) {
            this.oldButtonIndex = this.nowButtonIndex;
            this.nowButtonIndex = _buttonIndex;
            changeNowStatus(1);
        } else {
            this.nextButtonIndex = _buttonIndex;
            changeNowStatus(2);
        }
    }

    public void changeNowStatus(int _newStatus) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        Log.d("TopButtonsLayout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            this.hidden = true;
            this.nowStatus = (short) -1;
            this.oldButtonIndex = (short) -1;
            this.nowButtonIndex = (short) -1;
            this.fadeFrontViewAlpha = 0;
            return;
        }
        if (_newStatus == 0) {
            this.fadeFrontViewAlpha = 0;
            this.nowStatus = (short) 0;
            this.hidden = false;
            refreshControlF();
            return;
        }
        if (_newStatus == 1) {
            if (this.nowStatus == 0 || this.nowStatus == 2) {
                this.fadeFrontViewAlpha = 400;
                this.mainGameViewController.doViewChange();
                this.nowStatus = (short) 1;
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 2 && this.nowStatus == 0) {
            this.fadeFrontViewAlpha = 0;
            this.nowStatus = (short) 2;
            this.hidden = false;
        }
    }

    public void doLoop() {
        if (this.nowStatus == 1) {
            this.fadeFrontViewAlpha -= 80;
            if (this.fadeFrontViewAlpha <= 0) {
                changeNowStatus(0);
                return;
            }
            return;
        }
        if (this.nowStatus == 2) {
            this.fadeFrontViewAlpha += 80;
            if (this.fadeFrontViewAlpha >= 400) {
                selectButtonWithIndex(this.nextButtonIndex, (short) 1);
            }
        }
    }

    public void refreshControlF() {
        this.controlF = true;
        if (this.mainGameViewController.manualLayout != null && this.mainGameViewController.manualLayout.getVisibility() == 0) {
            this.controlF = false;
        }
        if (this.mainGameViewController.bonusUnitView != null && this.mainGameViewController.bonusUnitView.getVisibility() == 0) {
            this.controlF = false;
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.nowStatus != 0) {
            return false;
        }
        if (event.getAction() == 0 && event.getX() > BitmapDescriptorFactory.HUE_RED && event.getX() < this.finalWidth && event.getY() > BitmapDescriptorFactory.HUE_RED && event.getY() < this.buttonHeight + this.SELECTED_Y) {
            Log.d("onTouchEvent", "X=" + event.getX() + ", Y=  " + event.getY());
            short s = this.nowButtonIndex;
            short newButtonIndex = (short) (event.getX() / this.buttonWidth);
            if (newButtonIndex < 0) {
                newButtonIndex = 0;
            } else if (newButtonIndex > this.appDelegate.MAIN_PAGES_CNT - 1) {
                newButtonIndex = (short) (this.appDelegate.MAIN_PAGES_CNT - 1);
            }
            Log.d("onTouchEvent", "newButtonIndex:" + ((int) newButtonIndex));
            if (newButtonIndex != this.nowButtonIndex) {
                this.appDelegate.doSoundPoolPlay(3);
                selectButtonWithIndex(newButtonIndex, (short) 2);
            }
            returnF = true;
        }
        return returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.nowStatus >= 0 && this.nowStatus <= 2) {
            for (int i = 0; i < this.appDelegate.MAIN_PAGES_CNT; i++) {
                float buttonOffsetY = this.UNSELECTED_Y;
                int buttonColor0 = -16;
                int buttonColor1 = -3109815;
                int buttonColor2 = -2248316;
                int buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
                int titleLabelColor = -1710619;
                int titleLabelStroke1Color = -11776948;
                int titleLabelStroke2Color = 0;
                if (this.nowButtonIndex == i) {
                    buttonOffsetY = this.SELECTED_Y;
                    buttonColor0 = -200082;
                    buttonColor1 = -227838;
                    buttonColor2 = -12988;
                    buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
                    titleLabelColor = -1;
                    titleLabelStroke1Color = FluctConstants.FRAME_ALPHA_COLOR;
                    titleLabelStroke2Color = 0;
                }
                String titleLabelString = "";
                float titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
                float titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
                if (i == 0) {
                    titleLabelString = this.button0TitleLabelString;
                    titleLabelOffsetX = this.button0TitleLabelOffsetX;
                    titleLabelOffsetY = buttonOffsetY + this.buttonTitleLabelOffsetY;
                } else if (i == 1) {
                    titleLabelString = this.button1TitleLabelString;
                    titleLabelOffsetX = this.button1TitleLabelOffsetX;
                    titleLabelOffsetY = buttonOffsetY + this.buttonTitleLabelOffsetY;
                } else if (i == 2) {
                    titleLabelString = this.button2TitleLabelString;
                    titleLabelOffsetX = this.button2TitleLabelOffsetX;
                    titleLabelOffsetY = buttonOffsetY + this.buttonTitleLabelOffsetY;
                } else if (i == 3) {
                    titleLabelString = this.button3TitleLabelString;
                    titleLabelOffsetX = this.button3TitleLabelOffsetX;
                    titleLabelOffsetY = buttonOffsetY + this.buttonTitleLabelOffsetY;
                }
                MyDraw.drawStrokeRect(canvas, (this.buttonSpaceX * i) + this.buttonOffsetX, buttonOffsetY, this.buttonWidth, this.buttonHeight, buttonColor0, this.buttonStrokeWidth1, buttonColor1, this.buttonStrokeWidth2, buttonColor2, this.buttonStrokeWidth3, buttonColor3, this.buttonRadius);
                if (titleLabelString.length() > 0) {
                    MyDraw.drawStrokeText(canvas, titleLabelOffsetX, titleLabelOffsetY, this.buttonTitleLabelTypeface, titleLabelString, this.buttonTitleLabelFontSize, titleLabelColor, this.buttonTitleLabelStroke1Width, titleLabelStroke1Color, this.buttonTitleLabelStroke2Width, titleLabelStroke2Color);
                }
            }
            if (this.fadeFrontViewAlpha > 0) {
                if (this.fadeFrontViewAlpha > 255) {
                    this.fadeFrontViewPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                } else {
                    this.fadeFrontViewPaint.setAlpha(this.fadeFrontViewAlpha);
                }
                canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, this.fadeFrontViewPaint);
            }
        }
    }

    public void onDestroy() {
        this.fadeFrontViewPaint = null;
        this.myDraw = null;
        this.mainGameViewController = null;
        this.appDelegate = null;
    }
}
