package com.idtinc.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AlertUnitType0 implements CustomButtonType0Delegate {
    private AlertUnitType0BackView alertUnitType0BackView;
    public AppDelegate appDelegate;
    private CustomButtonType0 button0;
    private int button0SeIndex;
    private CustomButtonType0 button1;
    private int button1SeIndex;
    private short buttonIndex;
    Typeface buttonTitleLabelTypeface;
    private Character_Images_View character_Images_View;
    public Typeface contentLabelTypeface;
    public AlertUnitType0Delegate delegate;
    private boolean fadeOutAnimeF;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    public short nowStatus;
    public float smallImage0Height;
    public float smallImage0OffsetX;
    public float smallImage0OffsetY;
    public float smallImage0Width;
    public int smallImage1FadeOutAlpha;
    public float smallImage1Height;
    public float smallImage1OffsetX;
    public float smallImage1OffsetY;
    public float smallImage1Width;
    public short subTag;
    public short tag;
    public Typeface titleLabelTypeface;
    private Tool_1_Images_View tool_1_Images_View;
    private short type;
    private float zoomRate;
    public float backViewOffsetX = BitmapDescriptorFactory.HUE_RED;
    public float backViewOffsetY = BitmapDescriptorFactory.HUE_RED;
    public float backViewWidth = BitmapDescriptorFactory.HUE_RED;
    public float backViewHeight = BitmapDescriptorFactory.HUE_RED;
    public int backViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    public float backViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
    public int backViewColor1 = FluctConstants.FRAME_ALPHA_COLOR;
    public float backViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
    public int backViewColor2 = FluctConstants.FRAME_ALPHA_COLOR;
    public float backViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
    public int backViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
    public float backViewRadius = BitmapDescriptorFactory.HUE_RED;
    public String titleLabelString = "";
    public float titleLabelFontSize = 32.0f;
    public int titleLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    public float titleLabelStroke1Width = 6.0f;
    public int titleLabelColor1 = 0;
    public float titleLabelStroke2Width = 10.0f;
    public int titleLabelColor2 = 0;
    public float titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    public float titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    public String contentLabelString0 = "";
    public String contentLabelString1 = "";
    public String contentLabelString2 = "";
    public String contentLabelString3 = "";
    public String contentLabelString4 = "";
    public float contentLabelFontSize = 12.0f;
    public int contentLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    public float contentLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
    public int contentLabelColor1 = 0;
    public float contentLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
    public int contentLabelColor2 = 0;
    public float contentLabelOffsetX0 = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelOffsetX1 = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelOffsetX2 = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelOffsetX3 = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelOffsetX4 = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    public float contentLabelSpaceY = BitmapDescriptorFactory.HUE_RED;
    private float buttonWidth = 110.0f;
    private float buttonHeight = 32.0f;
    private float button0OffsetX = BitmapDescriptorFactory.HUE_RED;
    private float button1OffsetX = BitmapDescriptorFactory.HUE_RED;
    private float buttonOffsetY = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor2 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
    private int buttonColor3 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonRadius = BitmapDescriptorFactory.HUE_RED;
    public String button0TitleLabelString = "";
    public String button1TitleLabelString = "";
    private float buttonTitleLabelFontSize = 32.0f;
    private int buttonTitleLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
    private float buttonTitleLabelStroke1Width = 6.0f;
    private int buttonTitleLabelColor1 = 0;
    private float buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
    private int buttonTitleLabelColor2 = 0;
    private float button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    private float button0TitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    private float button1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
    private float button1TitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
    protected Bitmap imageBitmap0 = null;
    protected Bitmap imageBitmap1 = null;

    public AlertUnitType0(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = true;
        this.type = (short) 0;
        this.tag = (short) -9999;
        this.subTag = (short) -9999;
        this.buttonIndex = (short) -1;
        this.button0SeIndex = -1;
        this.button1SeIndex = -1;
        this.fadeOutAnimeF = false;
        this.nowStatus = (short) -1;
        this.smallImage0OffsetX = 50.0f;
        this.smallImage0OffsetY = 50.0f;
        this.smallImage0Width = 60.0f;
        this.smallImage0Height = 60.0f;
        this.smallImage1FadeOutAlpha = 0;
        this.smallImage1OffsetX = 50.0f;
        this.smallImage1OffsetY = 50.0f;
        this.smallImage1Width = 60.0f;
        this.smallImage1Height = 60.0f;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = true;
        this.type = (short) 0;
        this.tag = (short) -9999;
        this.subTag = (short) -9999;
        this.buttonIndex = (short) -1;
        this.button0SeIndex = -1;
        this.button1SeIndex = -1;
        this.fadeOutAnimeF = false;
        this.nowStatus = (short) -1;
        this.smallImage0OffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage0OffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage0Width = this.zoomRate * 60.0f;
        this.smallImage0Height = this.zoomRate * 60.0f;
        this.smallImage1FadeOutAlpha = 0;
        this.smallImage1OffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage1OffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage1Width = this.zoomRate * 60.0f;
        this.smallImage1Height = this.zoomRate * 60.0f;
        this.alertUnitType0BackView = new AlertUnitType0BackView(this.finalWidth, this.finalHeight, this.zoomRate, this);
        this.tool_1_Images_View = new Tool_1_Images_View(20.0f, 35.0f, 280.0f, 74.0f, this.zoomRate, (short) 10, this.appDelegate, this);
        this.character_Images_View = new Character_Images_View(20.0f, 35.0f, 280.0f, 74.0f, this.zoomRate, (short) 10, this.appDelegate, this);
        this.button0 = new CustomButtonType0(this.finalWidth, this.finalHeight, this.zoomRate);
        this.button0.delegate = this;
        this.button0.tag = (short) 0;
        this.button1 = new CustomButtonType0(this.finalWidth, this.finalHeight, this.zoomRate);
        this.button1.delegate = this;
        this.button1.tag = (short) 1;
    }

    public void setBackViewParams(float _offsetX, float _offsetY, float _width, float _height, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius) {
        this.backViewOffsetX = this.zoomRate * _offsetX;
        this.backViewOffsetY = this.zoomRate * _offsetY;
        this.backViewWidth = this.zoomRate * _width;
        this.backViewHeight = this.zoomRate * _height;
        this.backViewColor0 = _color0;
        this.backViewStrokeWidth1 = this.zoomRate * _strokeWidth1;
        this.backViewColor1 = _color1;
        this.backViewStrokeWidth2 = this.zoomRate * _strokeWidth2;
        this.backViewColor2 = _color2;
        this.backViewStrokeWidth3 = this.zoomRate * _strokeWidth3;
        this.backViewColor3 = _color3;
        this.backViewRadius = this.zoomRate * _radius;
        this.smallImage0OffsetX = this.backViewOffsetX + (this.zoomRate * 50.0f);
        this.smallImage0OffsetY = this.backViewOffsetY + (this.zoomRate * 50.0f);
        this.smallImage1OffsetX = this.backViewOffsetX + (this.zoomRate * 50.0f);
        this.smallImage1OffsetY = this.backViewOffsetY + (this.zoomRate * 50.0f);
    }

    public void setTitleLabelParams(String _titleLabelString, Typeface _titleLabelTypeface, float _offsetX, float _offsetY, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2) {
        this.titleLabelString = _titleLabelString;
        this.titleLabelTypeface = _titleLabelTypeface;
        this.titleLabelFontSize = this.zoomRate * _fontSize;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.titleLabelString)) / 2.0f);
        this.titleLabelOffsetY = this.backViewOffsetY + (this.zoomRate * _offsetY) + this.titleLabelFontSize;
        this.titleLabelColor0 = _color0;
        this.titleLabelStroke1Width = this.zoomRate * _strokeWidth1;
        this.titleLabelColor1 = _color1;
        this.titleLabelStroke2Width = this.zoomRate * _strokeWidth2;
        this.titleLabelColor2 = _color2;
    }

    public void setContentLabelParams(String _contentLabelString0, String _contentLabelString1, String _contentLabelString2, String _contentLabelString3, String _contentLabelString4, Typeface _contentLabelTypeface, float _offsetX, float _offsetY, float _spaceY, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2) {
        this.contentLabelString0 = _contentLabelString0;
        this.contentLabelString1 = _contentLabelString1;
        this.contentLabelString2 = _contentLabelString2;
        this.contentLabelString3 = _contentLabelString3;
        this.contentLabelString4 = _contentLabelString4;
        this.contentLabelTypeface = _contentLabelTypeface;
        this.contentLabelFontSize = this.zoomRate * _fontSize;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.contentLabelTypeface);
        newPaint.setTextSize(this.contentLabelFontSize);
        this.contentLabelOffsetX0 = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.contentLabelString0)) / 2.0f);
        this.contentLabelOffsetX1 = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.contentLabelString1)) / 2.0f);
        this.contentLabelOffsetX2 = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.contentLabelString2)) / 2.0f);
        this.contentLabelOffsetX3 = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.contentLabelString3)) / 2.0f);
        this.contentLabelOffsetX4 = this.backViewOffsetX + ((this.backViewWidth - newPaint.measureText(this.contentLabelString4)) / 2.0f);
        this.contentLabelOffsetY = this.backViewOffsetY + (this.zoomRate * _offsetY) + this.contentLabelFontSize;
        this.contentLabelSpaceY = this.zoomRate * _spaceY;
        this.contentLabelColor0 = _color0;
        this.contentLabelStroke1Width = this.zoomRate * _strokeWidth1;
        this.contentLabelColor1 = _color1;
        this.contentLabelStroke2Width = this.zoomRate * _strokeWidth2;
        this.contentLabelColor2 = _color2;
        this.smallImage0OffsetX = this.backViewOffsetX + (50.0f * this.zoomRate);
        this.smallImage0OffsetY = this.backViewOffsetY + (50.0f * this.zoomRate);
        this.smallImage1OffsetX = this.backViewOffsetX + (50.0f * this.zoomRate);
        this.smallImage1OffsetY = this.backViewOffsetY + (50.0f * this.zoomRate);
    }

    public void setType(short _type, String _button0TitleLabelString, String _button1TitleLabelString, Typeface _buttonTitleLabelTypeface, float _buttonTitleLabelFontSize, int _buttonTitleLabelColor0, float _buttonTitleLabelStroke1Width, int _buttonTitleLabelColor1, float _buttonTitleLabelStroke2Width, int _buttonTitleLabelColor2, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius, Integer _button0SeIndex, Integer _button1SeIndex, Boolean _fadeOutAnimeF) {
        this.type = _type;
        this.buttonWidth = 110.0f * this.zoomRate;
        this.buttonHeight = 32.0f * this.zoomRate;
        this.buttonOffsetY = ((this.backViewOffsetY + this.backViewHeight) - this.buttonHeight) - (15.0f * this.zoomRate);
        this.button0OffsetX = (float) (this.backViewOffsetX + (((this.backViewWidth / 2.0f) - this.buttonWidth) / 2.0f) + (10.0d * this.zoomRate));
        if (this.type >= 1) {
            this.button1OffsetX = this.backViewOffsetX + ((this.backViewWidth - this.buttonWidth) / 2.0f);
        } else {
            this.button1OffsetX = (float) (((this.backViewOffsetX + (this.backViewWidth / 2.0f)) + (((this.backViewWidth / 2.0f) - this.buttonWidth) / 2.0f)) - (10.0d * this.zoomRate));
        }
        this.buttonColor0 = _color0;
        this.buttonStrokeWidth1 = this.zoomRate * _strokeWidth1;
        this.buttonColor1 = _color1;
        this.buttonStrokeWidth2 = this.zoomRate * _strokeWidth2;
        this.buttonColor2 = _color2;
        this.buttonStrokeWidth3 = this.zoomRate * _strokeWidth3;
        this.buttonColor3 = _color3;
        this.buttonRadius = this.zoomRate * _radius;
        this.button0TitleLabelString = _button0TitleLabelString;
        this.button1TitleLabelString = _button1TitleLabelString;
        this.buttonTitleLabelTypeface = _buttonTitleLabelTypeface;
        this.buttonTitleLabelFontSize = this.zoomRate * _buttonTitleLabelFontSize;
        this.buttonTitleLabelColor0 = _buttonTitleLabelColor0;
        this.buttonTitleLabelStroke1Width = this.zoomRate * _buttonTitleLabelStroke1Width;
        this.buttonTitleLabelColor1 = _buttonTitleLabelColor1;
        this.buttonTitleLabelStroke2Width *= this.zoomRate;
        this.buttonTitleLabelColor2 = _buttonTitleLabelColor2;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.button0TitleLabelOffsetX = this.backViewOffsetX + this.button0OffsetX + ((this.buttonWidth - newPaint.measureText(this.button0TitleLabelString)) / 2.0f);
        this.button0TitleLabelOffsetY = this.buttonOffsetY + ((this.buttonHeight + (this.buttonTitleLabelFontSize * 0.7f)) / 2.0f);
        this.button1TitleLabelOffsetX = this.backViewOffsetX + this.button1OffsetX + ((this.buttonWidth - newPaint.measureText(this.button1TitleLabelString)) / 2.0f);
        this.button1TitleLabelOffsetY = this.buttonOffsetY + ((this.buttonHeight + (this.buttonTitleLabelFontSize * 0.7f)) / 2.0f);
        this.button1.setButton(this.button1OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius, this.button1TitleLabelString, this.buttonTitleLabelTypeface, this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
        if (this.type < 1) {
            this.button0.setButton(this.button0OffsetX, this.buttonOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius, this.button0TitleLabelString, this.buttonTitleLabelTypeface, this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
        }
        this.button0SeIndex = _button0SeIndex.intValue();
        this.button1SeIndex = _button1SeIndex.intValue();
        this.fadeOutAnimeF = _fadeOutAnimeF.booleanValue();
        clearDrawableBitmap0();
        clearDrawableBitmap1();
    }

    public void clearDrawableBitmap0() {
        if (this.imageBitmap0 != null) {
            this.imageBitmap0 = null;
        }
    }

    public void clearDrawableBitmap1() {
        if (this.imageBitmap1 != null) {
            this.imageBitmap1 = null;
        }
    }

    public void changeDrawableBitmap0(Bitmap _imageBitmap0, int _alpha) {
        clearDrawableBitmap0();
        this.imageBitmap0 = _imageBitmap0;
    }

    public void changeDrawableBitmap1(Bitmap _imageBitmap1, int _alpha) {
        clearDrawableBitmap1();
        this.imageBitmap1 = _imageBitmap1;
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void pop() {
        changeNowStatus(0);
    }

    public void changeNowStatus(int _newStatus) {
        Log.d("AlertType0Layout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            this.nowStatus = (short) -1;
            this.titleLabelString = "";
            this.contentLabelString0 = "";
            this.contentLabelString1 = "";
            this.contentLabelString2 = "";
            this.contentLabelString3 = "";
            this.contentLabelString4 = "";
            this.button0TitleLabelString = "";
            this.button1TitleLabelString = "";
            clearDrawableBitmap0();
            clearDrawableBitmap1();
            this.buttonIndex = (short) -1;
            this.button0.changeNowStatus(-1);
            this.button1.changeNowStatus(-1);
            this.smallImage1FadeOutAlpha = 0;
            if (this.character_Images_View != null) {
                this.character_Images_View.hidden = true;
            }
            if (this.tool_1_Images_View != null) {
                this.tool_1_Images_View.hidden = true;
            }
            this.hidden = true;
            return;
        }
        if (_newStatus == 0) {
            if (this.nowStatus == -1) {
                this.nowStatus = (short) 0;
                this.buttonIndex = (short) -1;
                this.button1.tag = (short) 1;
                this.button1.changeNowStatus(2);
                if (this.type >= 1) {
                    this.button0.tag = (short) -9999;
                    this.button0.changeNowStatus(-1);
                } else {
                    this.button0.tag = (short) 0;
                    this.button0.changeNowStatus(2);
                }
                this.hidden = false;
                changeNowStatus(1);
                return;
            }
            return;
        }
        if (_newStatus == 1) {
            if (this.nowStatus == 0) {
                this.nowStatus = (short) 1;
                this.buttonIndex = (short) -1;
                this.button1.tag = (short) 1;
                this.button1.changeNowStatus(0);
                if (this.type >= 1) {
                    this.button0.tag = (short) -9999;
                    this.button0.changeNowStatus(-1);
                } else {
                    this.button0.tag = (short) 0;
                    this.button0.changeNowStatus(0);
                }
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 2) {
            if (this.nowStatus == 1) {
                this.nowStatus = (short) 2;
                changeNowStatus(3);
                return;
            }
            return;
        }
        if (_newStatus == 3) {
            if (this.nowStatus == 1 || this.nowStatus == 2) {
                this.nowStatus = (short) 3;
                this.titleLabelString = "";
                this.contentLabelString0 = "";
                this.contentLabelString1 = "";
                this.contentLabelString2 = "";
                this.contentLabelString3 = "";
                this.contentLabelString4 = "";
                this.button0TitleLabelString = "";
                this.button1TitleLabelString = "";
                clearDrawableBitmap0();
                clearDrawableBitmap1();
                this.button0.changeNowStatus(-1);
                this.button1.changeNowStatus(-1);
                this.delegate.buttonClick(this.tag, this.subTag, this.buttonIndex);
                this.buttonIndex = (short) -1;
                this.hidden = true;
            }
        }
    }

    public short set_Character_Images_View_Infos(String _infosString) {
        short returnCount = 0;
        if (_infosString != null && _infosString.length() > 0) {
            if (this.character_Images_View != null) {
                returnCount = this.character_Images_View.setInfos(_infosString);
            }
            return returnCount;
        }
        return (short) 0;
    }

    public short set_Tool_1_Images_View_Infos(String _infosString) {
        short returnCount = 0;
        if (_infosString != null && _infosString.length() > 0) {
            if (this.tool_1_Images_View != null) {
                returnCount = this.tool_1_Images_View.setInfos(_infosString);
            }
            return returnCount;
        }
        return (short) 0;
    }

    @Override // com.idtinc.custom.CustomButtonType0Delegate
    public void buttonClick(short _tag) {
        if (this.nowStatus == 1) {
            this.buttonIndex = _tag;
            if (this.appDelegate.defaultSharedPreferences.getBoolean("sound_switch", false)) {
                if (this.buttonIndex == 0) {
                    if (this.button0SeIndex >= 0) {
                        this.appDelegate.doSoundPoolPlay(this.button0SeIndex);
                    }
                } else if (this.buttonIndex == 1 && this.button1SeIndex >= 0) {
                    this.appDelegate.doSoundPoolPlay(this.button1SeIndex);
                }
            }
            if (this.fadeOutAnimeF) {
                changeNowStatus(2);
            } else {
                changeNowStatus(3);
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.nowStatus == 1) {
            if (0 == 0 && this.button0 != null && !this.button0.hidden) {
                returnF = this.button0.gameOnTouch(event);
            }
            if (!returnF && this.button1 != null && !this.button1.hidden) {
                this.button1.gameOnTouch(event);
            }
            return true;
        }
        if (this.nowStatus != 0 && this.nowStatus != 2) {
            return false;
        }
        return true;
    }

    public void gameDraw(Canvas canvas) {
        if (this.appDelegate != null) {
            if (this.alertUnitType0BackView != null) {
                this.alertUnitType0BackView.gameDraw(canvas);
            }
            if (this.tool_1_Images_View != null && !this.tool_1_Images_View.hidden) {
                this.tool_1_Images_View.gameDraw(canvas);
            }
            if (this.character_Images_View != null && !this.character_Images_View.hidden) {
                this.character_Images_View.gameDraw(canvas);
            }
            if (this.button0 != null && !this.button0.hidden) {
                this.button0.gameDraw(canvas);
            }
            if (this.button1 != null && !this.button1.hidden) {
                this.button1.gameDraw(canvas);
            }
        }
    }

    public void onDestroy() {
        clearDrawableBitmap0();
        clearDrawableBitmap1();
        if (this.button0 != null) {
            this.button0.onDestroy();
            this.button0 = null;
        }
        if (this.button1 != null) {
            this.button1.onDestroy();
            this.button1 = null;
        }
        if (this.character_Images_View != null) {
            this.character_Images_View.onDestroy();
            this.character_Images_View = null;
        }
        if (this.tool_1_Images_View != null) {
            this.tool_1_Images_View.onDestroy();
            this.tool_1_Images_View = null;
        }
        if (this.alertUnitType0BackView != null) {
            this.alertUnitType0BackView.onDestroy();
            this.alertUnitType0BackView = null;
        }
        this.appDelegate = null;
    }
}
