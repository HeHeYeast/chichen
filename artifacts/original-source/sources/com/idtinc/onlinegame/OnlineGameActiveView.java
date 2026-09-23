package com.idtinc.onlinegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class OnlineGameActiveView extends RelativeLayout {
    private AppDelegate appDelegate;
    public int backViewColor0;
    public int backViewColor1;
    public int backViewColor2;
    public int backViewColor3;
    public float backViewHeight;
    public float backViewOffsetX;
    public float backViewOffsetY;
    public float backViewRadius;
    public float backViewStrokeWidth1;
    public float backViewStrokeWidth2;
    public float backViewStrokeWidth3;
    public float backViewWidth;
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
    private float finalHeight;
    private float finalWidth;
    private int loadingBackRectColor0;
    private int loadingBackRectColor1;
    private int loadingBackRectColor2;
    private int loadingBackRectColor3;
    private float loadingBackRectHeight;
    private float loadingBackRectOffsetX;
    private float loadingBackRectOffsetY;
    private float loadingBackRectRadius;
    private float loadingBackRectStrokeWidth1;
    private float loadingBackRectStrokeWidth2;
    private float loadingBackRectStrokeWidth3;
    private float loadingBackRectWidth;
    private short loadingCnt;
    private int loadingLabelColor0;
    private int loadingLabelColor1;
    private int loadingLabelColor2;
    public String loadingLabelDrawString;
    private float loadingLabelFontSize;
    private float loadingLabelOffsetX;
    private float loadingLabelOffsetY;
    public String loadingLabelString;
    private float loadingLabelStroke1Width;
    private float loadingLabelStroke2Width;
    Typeface loadingLabelTypeface;
    private MyDraw myDraw;
    private OnlineGameViewController onlineGameViewController;
    private short touchButtonIndex;
    private float zoomRate;

    public OnlineGameActiveView(Context context, float _finalwidth, float _finalheight, float _zoomrate, OnlineGameViewController _onlineGameViewController, AppDelegate _appDelegate) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.loadingCnt = (short) 0;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) -1;
        this.backViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.backViewHeight = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backViewRadius = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectOffsetX = 95.0f;
        this.loadingBackRectOffsetY = 174.0f;
        this.loadingBackRectWidth = 130.0f;
        this.loadingBackRectHeight = 130.0f;
        this.loadingBackRectColor0 = 1426063360;
        this.loadingBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor1 = 0;
        this.loadingBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor2 = 0;
        this.loadingBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor3 = 0;
        this.loadingBackRectRadius = 10.0f;
        this.loadingLabelString = "";
        this.loadingLabelDrawString = "";
        this.loadingLabelFontSize = 16.0f;
        this.loadingLabelColor0 = -1;
        this.loadingLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor1 = -1;
        this.loadingLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor2 = 0;
        this.loadingLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonWidth = 30.0f;
        this.closeButtonHeight = 30.0f;
        this.closeButtonOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor0 = -2248316;
        this.closeButtonStrokeWidth1 = 3.0f;
        this.closeButtonColor1 = -7576502;
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
        this.appDelegate = null;
        this.onlineGameViewController = null;
        this.appDelegate = _appDelegate;
        this.onlineGameViewController = _onlineGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.loadingCnt = (short) 0;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
        this.backViewOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backViewOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backViewWidth = this.finalWidth;
        this.backViewHeight = this.finalHeight - (50.0f * this.zoomRate);
        this.backViewColor0 = -16;
        this.backViewStrokeWidth1 = 3.0f * this.zoomRate;
        this.backViewColor1 = -7576502;
        this.backViewStrokeWidth2 = 3.0f * this.zoomRate;
        this.backViewColor2 = -16;
        this.backViewStrokeWidth3 = 3.0f * this.zoomRate;
        this.backViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backViewRadius = 10.0f * this.zoomRate;
        Paint newPaint = new Paint(257);
        this.loadingBackRectOffsetX = 95.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.loadingBackRectOffsetY = 156.0f * this.zoomRate;
        } else {
            this.loadingBackRectOffsetY = 200.0f * this.zoomRate;
        }
        this.loadingBackRectWidth = 130.0f * this.zoomRate;
        this.loadingBackRectHeight = 130.0f * this.zoomRate;
        this.loadingBackRectColor0 = 1426063360;
        this.loadingBackRectStrokeWidth1 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor1 = 0;
        this.loadingBackRectStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor2 = 0;
        this.loadingBackRectStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loadingBackRectColor3 = 0;
        this.loadingBackRectRadius = 10.0f * this.zoomRate;
        this.loadingLabelString = "";
        this.loadingLabelDrawString = "";
        this.loadingLabelTypeface = Typeface.DEFAULT_BOLD;
        this.loadingLabelFontSize = 16.0f * this.zoomRate;
        this.loadingLabelColor0 = -1;
        this.loadingLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor1 = -1;
        this.loadingLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.loadingLabelColor2 = 0;
        newPaint.setTypeface(this.loadingLabelTypeface);
        newPaint.setTextSize(this.loadingLabelFontSize);
        this.loadingLabelOffsetX = this.loadingBackRectOffsetX + ((this.loadingBackRectWidth - newPaint.measureText(this.loadingLabelString)) / 2.0f);
        this.loadingLabelOffsetY = this.loadingBackRectOffsetY + (this.loadingBackRectHeight / 2.0f) + (4.0f * this.zoomRate);
        this.closeButtonWidth = 30.0f * this.zoomRate;
        this.closeButtonHeight = 30.0f * this.zoomRate;
        this.closeButtonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.closeButtonOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.closeButtonColor0 = -2248316;
        this.closeButtonStrokeWidth1 = 3.0f * this.zoomRate;
        this.closeButtonColor1 = -7576502;
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
        this.myDraw = new MyDraw();
    }

    public void reset() {
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.closeButtonStatus = (short) 0;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        boolean returnF;
        if (this.onlineGameViewController == null || this.onlineGameViewController.getVisibility() != 0) {
            return false;
        }
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.closeButtonStatus == 0 && event.getX() >= this.closeButtonOffsetX && event.getX() < this.closeButtonOffsetX + this.closeButtonWidth && event.getY() >= this.closeButtonOffsetY && event.getY() < this.closeButtonOffsetY + this.closeButtonHeight) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) -1;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.onlinegame.OnlineGameActiveView.1
                    @Override // java.lang.Runnable
                    public void run() {
                        OnlineGameActiveView.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        if (this.loadingLabelString != null && this.loadingLabelString.length() > 0) {
            return true;
        }
        if (this.onlineGameViewController.accountLoginUnit != null && this.onlineGameViewController.accountLoginUnit.accountRegisterUnit != null && this.onlineGameViewController.uploadSaveFileUnit != null && this.onlineGameViewController.downloadSaveFileUnit != null) {
            if (!this.onlineGameViewController.downloadSaveFileUnit.hidden.booleanValue()) {
                boolean returnF2 = this.onlineGameViewController.downloadSaveFileUnit.gameOnTouch(event);
                if (returnF2) {
                    return returnF2;
                }
            } else if (!this.onlineGameViewController.uploadSaveFileUnit.hidden.booleanValue()) {
                boolean returnF3 = this.onlineGameViewController.uploadSaveFileUnit.gameOnTouch(event);
                if (returnF3) {
                    return returnF3;
                }
            } else if (!this.onlineGameViewController.accountLoginUnit.accountRegisterUnit.hidden.booleanValue()) {
                boolean returnF4 = this.onlineGameViewController.accountLoginUnit.accountRegisterUnit.gameOnTouch(event);
                if (returnF4) {
                    return returnF4;
                }
            } else if (!this.onlineGameViewController.accountLoginUnit.hidden.booleanValue() && (returnF = this.onlineGameViewController.accountLoginUnit.gameOnTouch(event))) {
                return returnF;
            }
        }
        return true;
    }

    public void doClick() {
        if (this.touchButtonIndex == 0 && this.onlineGameViewController.getVisibility() == 0) {
            this.appDelegate.doSoundPoolPlay(2);
            this.onlineGameViewController.clicked(this.touchButtonIndex);
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void doLoop() {
        if (getVisibility() == 0) {
            if (this.loadingLabelString.length() > 0) {
                this.loadingCnt = (short) (this.loadingCnt - 1);
                if (this.loadingCnt <= 0) {
                    this.loadingCnt = (short) 15;
                    this.loadingLabelDrawString = String.valueOf(this.loadingLabelString) + ".";
                } else if (this.loadingCnt == 10) {
                    this.loadingLabelDrawString = String.valueOf(this.loadingLabelString) + "..";
                } else if (this.loadingCnt == 5) {
                    this.loadingLabelDrawString = String.valueOf(this.loadingLabelString) + "...";
                }
            }
            invalidate();
        }
    }

    public void startLoading(String _loadingLabelString) {
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.loadingLabelTypeface);
        newPaint.setTextSize(this.loadingLabelFontSize);
        this.loadingLabelString = _loadingLabelString;
        this.loadingLabelOffsetX = this.loadingBackRectOffsetX + ((this.loadingBackRectWidth - newPaint.measureText(String.valueOf(this.loadingLabelString) + "...")) / 2.0f);
    }

    public void stopLoading() {
        this.loadingLabelString = "";
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(0);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.appDelegate != null && this.onlineGameViewController != null) {
            MyDraw.drawStrokeRect(canvas, this.backViewOffsetX, this.backViewOffsetY, this.backViewWidth, this.backViewHeight, this.backViewColor0, this.backViewStrokeWidth1, this.backViewColor1, this.backViewStrokeWidth2, this.backViewColor2, this.backViewStrokeWidth3, this.backViewColor3, this.backViewRadius);
            if (this.onlineGameViewController.accountLoginUnit != null && this.onlineGameViewController.accountLoginUnit.accountRegisterUnit != null && this.onlineGameViewController.uploadSaveFileUnit != null && this.onlineGameViewController.downloadSaveFileUnit != null) {
                if (!this.onlineGameViewController.downloadSaveFileUnit.hidden.booleanValue()) {
                    if (!this.onlineGameViewController.accountLoginUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountLoginUnitEditText();
                    }
                    if (!this.onlineGameViewController.accountRegisterUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountRegisterUnitUnitEditText();
                    }
                    this.onlineGameViewController.downloadSaveFileUnit.gameDraw(canvas);
                } else if (!this.onlineGameViewController.uploadSaveFileUnit.hidden.booleanValue()) {
                    if (!this.onlineGameViewController.accountLoginUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountLoginUnitEditText();
                    }
                    if (!this.onlineGameViewController.accountRegisterUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountRegisterUnitUnitEditText();
                    }
                    this.onlineGameViewController.uploadSaveFileUnit.gameDraw(canvas);
                } else if (!this.onlineGameViewController.accountLoginUnit.accountRegisterUnit.hidden.booleanValue()) {
                    if (!this.onlineGameViewController.accountLoginUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountLoginUnitEditText();
                    }
                    if (this.onlineGameViewController.accountRegisterUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.displayAccountRegisterUnitUnitEditText();
                    }
                    this.onlineGameViewController.accountLoginUnit.accountRegisterUnit.gameDraw(canvas);
                } else if (!this.onlineGameViewController.accountLoginUnit.hidden.booleanValue()) {
                    if (this.onlineGameViewController.accountLoginUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.displayAccountLoginUnitEditText();
                    }
                    if (!this.onlineGameViewController.accountRegisterUnitEditTextHiddenF.booleanValue()) {
                        this.onlineGameViewController.hiddenAccountRegisterUnitUnitEditText();
                    }
                    this.onlineGameViewController.accountLoginUnit.gameDraw(canvas);
                }
            }
            if (this.loadingLabelString != null && this.loadingLabelString.length() > 0) {
                bitmapPaint.setColor(-2130706433);
                canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
                MyDraw.drawStrokeRect(canvas, this.loadingBackRectOffsetX, this.loadingBackRectOffsetY, this.loadingBackRectWidth, this.loadingBackRectHeight, this.loadingBackRectColor0, this.loadingBackRectStrokeWidth1, this.loadingBackRectColor1, this.loadingBackRectStrokeWidth2, this.loadingBackRectColor2, this.loadingBackRectStrokeWidth3, this.loadingBackRectColor3, this.loadingBackRectRadius);
                MyDraw.drawStrokeRect(canvas, this.loadingBackRectOffsetX, this.loadingBackRectOffsetY, this.loadingBackRectWidth, this.loadingBackRectHeight, this.loadingBackRectColor0, this.loadingBackRectStrokeWidth1, this.loadingBackRectColor1, this.loadingBackRectStrokeWidth2, this.loadingBackRectColor2, this.loadingBackRectStrokeWidth3, this.loadingBackRectColor3, this.loadingBackRectRadius);
                MyDraw.drawStrokeText(canvas, this.loadingLabelOffsetX, this.loadingLabelOffsetY, this.loadingLabelTypeface, this.loadingLabelDrawString, this.loadingLabelFontSize, this.loadingLabelColor0, this.loadingLabelStroke1Width, this.loadingLabelColor1, this.loadingLabelStroke2Width, 0);
            }
            if (this.closeButtonStatus == 0) {
                MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, this.closeButtonColor0, this.closeButtonStrokeWidth1, this.closeButtonColor1, this.closeButtonStrokeWidth2, this.closeButtonColor2, this.closeButtonStrokeWidth3, this.closeButtonColor3, this.closeButtonRadius);
                MyDraw.drawStrokeText(canvas, this.closeButtonTitleLabelOffsetX, this.closeButtonTitleLabelOffsetY, this.closeButtonTitleLabelTypeface, this.closeButtonTitleLabelString, this.closeButtonTitleLabelFontSize, this.closeButtonTitleLabelColor0, this.closeButtonTitleLabelStroke1Width, this.closeButtonTitleLabelColor1, this.closeButtonTitleLabelStroke2Width, this.closeButtonTitleLabelColor2);
                if (this.touchButtonIndex == 0) {
                    MyDraw.drawStrokeRect(canvas, this.closeButtonOffsetX, this.closeButtonOffsetY, this.closeButtonWidth, this.closeButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.closeButtonRadius);
                }
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.onlineGameViewController = null;
        this.appDelegate = null;
    }
}
