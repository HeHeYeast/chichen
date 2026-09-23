package com.idtinc.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CustomButtonType0 {
    Typeface buttonTitleLabelTypeface;
    public short delayClickCnt;
    public short delayClickCntNext;
    private short delayToStatus0Cnt;
    private short delayToStatus0CntNext;
    private short delayToStatus2Cnt;
    public CustomButtonType0Delegate delegate;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    public short nowStatus;
    public short tag;
    private float zoomRate;
    private float buttonOffsetX = BitmapDescriptorFactory.HUE_RED;
    private float buttonOffsetY = BitmapDescriptorFactory.HUE_RED;
    private float buttonWidth = BitmapDescriptorFactory.HUE_RED;
    private float buttonHeight = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor2 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonRadius = BitmapDescriptorFactory.HUE_RED;
    public String buttonTitleLabelString = "";
    private float buttonTitleLabelFontSize = BitmapDescriptorFactory.HUE_RED;
    private int buttonTitleLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonTitleLabelStroke1Width = 6.0f;
    private int buttonTitleLabelColor1 = 0;
    private float buttonTitleLabelStroke2Width = 10.0f;
    private int buttonTitleLabelColor2 = 0;
    private float buttonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    private float buttonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    private MyDraw myDraw = new MyDraw();

    public CustomButtonType0(float _finalwidth, float _finalheight, float _zoomrate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.delayClickCntNext = (short) -1;
        this.delayClickCnt = (short) -1;
        this.delayToStatus0CntNext = (short) -1;
        this.delayToStatus0Cnt = (short) -1;
        this.delayToStatus2Cnt = (short) -1;
        this.tag = (short) -9999;
        this.nowStatus = (short) -1;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.delayClickCntNext = (short) -1;
        this.delayClickCnt = (short) -1;
        this.delayToStatus0CntNext = (short) -1;
        this.delayToStatus0Cnt = (short) -1;
        this.delayToStatus2Cnt = (short) -1;
        this.tag = (short) -9999;
        this.nowStatus = (short) -1;
    }

    public void setButton(float _buttonOffsetX, float _buttonOffsetY, float _buttonWidth, float _buttonHeight, int _buttonColor0, float _buttonStrokeWidth1, int _buttonColor1, float _buttonStrokeWidth2, int _buttonColor2, float _buttonStrokeWidth3, int _buttonColor3, float _buttonRadius, String _buttonTitleLabelString, Typeface _buttonTitleLabelTypeface, float _buttonTitleLabelFontSize, int _buttonTitleLabelColor0, float _buttonTitleLabelStroke1Width, int _buttonTitleLabelColor1, float _buttonTitleLabelStroke2Width, int _buttonTitleLabelColor2) {
        this.buttonOffsetX = _buttonOffsetX;
        this.buttonOffsetY = _buttonOffsetY;
        this.buttonWidth = _buttonWidth;
        this.buttonHeight = _buttonHeight;
        this.buttonColor0 = _buttonColor0;
        this.buttonStrokeWidth1 = _buttonStrokeWidth1;
        this.buttonColor1 = _buttonColor1;
        this.buttonStrokeWidth2 = _buttonStrokeWidth2;
        this.buttonColor2 = _buttonColor2;
        this.buttonStrokeWidth3 = _buttonStrokeWidth3;
        this.buttonColor3 = _buttonColor3;
        this.buttonRadius = _buttonRadius;
        this.buttonTitleLabelString = _buttonTitleLabelString;
        this.buttonTitleLabelTypeface = _buttonTitleLabelTypeface;
        this.buttonTitleLabelFontSize = _buttonTitleLabelFontSize;
        this.buttonTitleLabelColor0 = _buttonTitleLabelColor0;
        this.buttonTitleLabelStroke1Width = _buttonTitleLabelStroke1Width;
        this.buttonTitleLabelColor1 = _buttonTitleLabelColor1;
        this.buttonTitleLabelStroke2Width = _buttonTitleLabelStroke2Width;
        this.buttonTitleLabelColor2 = _buttonTitleLabelColor2;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.buttonTitleLabelOffsetX = this.buttonOffsetX + ((this.buttonWidth - newPaint.measureText(this.buttonTitleLabelString)) / 2.0f);
        this.buttonTitleLabelOffsetY = this.buttonOffsetY + ((this.buttonHeight + (this.buttonTitleLabelFontSize * 0.7f)) / 2.0f);
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void startWithDelayToStatus0Cnt(short _delayToStatus0Cnt) {
        changeNowStatus(0);
        if (this.nowStatus == 0) {
            this.delayToStatus0CntNext = _delayToStatus0Cnt;
            this.delayToStatus0Cnt = _delayToStatus0Cnt;
            this.delayToStatus2Cnt = (short) -1;
        }
    }

    public void startWithDelayToStatus2Cnt(short _delayToStatus2Cnt) {
        changeNowStatus(0);
        if (this.nowStatus == 0) {
            this.delayToStatus2Cnt = _delayToStatus2Cnt;
            this.delayToStatus0Cnt = (short) -1;
            this.delayToStatus0CntNext = (short) -1;
        }
    }

    public void stop() {
        changeNowStatus(2);
    }

    public void changeNowStatus(int _newStatus) {
        if (_newStatus == -1) {
            this.nowStatus = (short) -1;
            this.delayClickCnt = (short) -1;
            this.delayToStatus0CntNext = (short) -1;
            this.delayToStatus0Cnt = (short) -1;
            this.delayToStatus2Cnt = (short) -1;
            this.hidden = true;
            return;
        }
        if (_newStatus == 0) {
            if (this.nowStatus == -1 || this.nowStatus == 2) {
                this.delayClickCnt = (short) -1;
                this.delayToStatus0CntNext = (short) -1;
                this.delayToStatus0Cnt = (short) -1;
                this.delayToStatus2Cnt = (short) -1;
                this.nowStatus = (short) 0;
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 1) {
            if (this.nowStatus == 0) {
                this.nowStatus = (short) 1;
                this.delayClickCnt = (short) -1;
                if (this.delayToStatus0Cnt <= 0) {
                    this.delayToStatus0CntNext = (short) -1;
                    this.delayToStatus0Cnt = (short) -1;
                }
                if (this.delayToStatus2Cnt <= 0) {
                    this.delayToStatus2Cnt = (short) -1;
                }
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 2) {
            this.nowStatus = (short) 2;
            this.delayClickCnt = (short) -1;
            this.delayToStatus0CntNext = (short) -1;
            this.delayToStatus0Cnt = (short) -1;
            this.delayToStatus2Cnt = (short) -1;
            this.hidden = false;
        }
    }

    public void doClick() {
        if (this.nowStatus == 0) {
            changeNowStatus(1);
            this.delegate.buttonClick(this.tag);
        }
    }

    public void cancelCkick() {
        this.delayClickCnt = (short) -1;
    }

    public void doLoop() {
        if (this.nowStatus == 0) {
            if (this.delayClickCntNext > 0) {
                if (this.delayClickCnt > 0) {
                    this.delayClickCnt = (short) (this.delayClickCnt - 1);
                    if (this.delayClickCnt <= 0) {
                        this.delayClickCnt = (short) -1;
                        doClick();
                    }
                }
            } else {
                this.delayClickCnt = (short) -1;
            }
        }
        if (this.nowStatus == 1) {
            if (this.delayToStatus0Cnt > 0) {
                this.delayToStatus2Cnt = (short) -1;
                this.delayToStatus0Cnt = (short) (this.delayToStatus0Cnt - 1);
                if (this.delayToStatus0Cnt <= 0) {
                    this.delayToStatus0Cnt = (short) -1;
                    short oldDelayToStatus0CntNext = this.delayToStatus0CntNext;
                    changeNowStatus(-1);
                    startWithDelayToStatus0Cnt(oldDelayToStatus0CntNext);
                }
            }
            if (this.delayToStatus2Cnt > 0) {
                this.delayToStatus0CntNext = (short) -1;
                this.delayToStatus0Cnt = (short) -1;
                this.delayToStatus2Cnt = (short) (this.delayToStatus2Cnt - 1);
                if (this.delayToStatus2Cnt <= 0) {
                    this.delayToStatus2Cnt = (short) -1;
                    changeNowStatus(2);
                }
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.nowStatus != 0) {
            return false;
        }
        if (event.getAction() == 0 && event.getX() > this.buttonOffsetX && event.getX() < this.buttonOffsetX + this.buttonWidth && event.getY() > this.buttonOffsetY && event.getY() < this.buttonOffsetY + this.buttonHeight) {
            Log.d("tag= " + ((int) this.tag), "X=" + event.getX() + ", Y=  " + event.getY());
            if (this.delayClickCntNext > 0) {
                this.delayClickCnt = this.delayClickCntNext;
            } else {
                this.delayClickCnt = (short) 10;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.custom.CustomButtonType0.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CustomButtonType0.this.doClick();
                    }
                }, 100L);
            }
            returnF = true;
        }
        return returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.nowStatus >= 0 && this.nowStatus <= 2) {
            MyDraw.drawStrokeRect(canvas, this.buttonOffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius);
            if (this.buttonTitleLabelString.length() > 0) {
                MyDraw.drawStrokeText(canvas, this.buttonTitleLabelOffsetX, this.buttonTitleLabelOffsetY, this.buttonTitleLabelTypeface, this.buttonTitleLabelString, this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
            }
            if (this.nowStatus == 0 && this.delayClickCnt > 0) {
                MyDraw.drawStrokeRect(canvas, this.buttonOffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
            } else if (this.nowStatus == 1) {
                MyDraw.drawStrokeRect(canvas, this.buttonOffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
            }
        }
    }

    public void onDestroy() {
        this.delegate = null;
    }
}
