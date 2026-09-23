package com.idtinc.custom;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import java.io.IOException;
import java.io.InputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ContentPopUnit implements CustomButtonType0Delegate {
    public AppDelegate appDelegate;
    public int backStrokeViewColor0;
    public int backStrokeViewColor1;
    public int backStrokeViewColor2;
    public int backStrokeViewColor3;
    public float backStrokeViewHeight;
    public float backStrokeViewOffsetX;
    public float backStrokeViewOffsetY;
    public float backStrokeViewRadius;
    public float backStrokeViewStrokeWidth1;
    public float backStrokeViewStrokeWidth2;
    public float backStrokeViewStrokeWidth3;
    public float backStrokeViewWidth;
    public int blockImageCutHeight;
    public int blockImageCutOffsetX;
    public int blockImageCutOffsetY;
    public int blockImageCutWidth;
    public int blockImageHeight;
    public int blockImageOffsetX;
    public int blockImageOffsetY;
    public int blockImageWidth;
    private CustomButtonType0 button0;
    private int button0SeIndex;
    private CustomButtonType0 button1;
    private int button1SeIndex;
    public short buttonClickCnt;
    private short buttonIndex;
    Typeface buttonTitleLabelTypeface;
    public Typeface contentLabelTypeface;
    private ContentPopUnitBackView contentPopUnitBackView;
    public ContentPopUnitDelegate delegate;
    public float facebookButtonHeight;
    public float facebookButtonOffsetX;
    public float facebookButtonOffsetY;
    public float facebookButtonRadius;
    public short facebookButtonStatus;
    public float facebookButtonWidth;
    private boolean fadeOutAnimeF;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    public int imageView0BackViewColor0;
    public int imageView0BackViewColor1;
    public int imageView0BackViewColor2;
    public int imageView0BackViewColor3;
    public float imageView0BackViewHeight;
    public float imageView0BackViewOffsetX;
    public float imageView0BackViewOffsetY;
    public float imageView0BackViewRadius;
    public float imageView0BackViewStrokeWidth1;
    public float imageView0BackViewStrokeWidth2;
    public float imageView0BackViewStrokeWidth3;
    public float imageView0BackViewWidth;
    public float lineButtonHeight;
    public float lineButtonOffsetX;
    public float lineButtonOffsetY;
    public float lineButtonRadius;
    public short lineButtonStatus;
    public float lineButtonWidth;
    public float mailButtonHeight;
    public float mailButtonOffsetX;
    public float mailButtonOffsetY;
    public float mailButtonRadius;
    public short mailButtonStatus;
    public float mailButtonWidth;
    public short nowStatus;
    public short selectedButtonIndex;
    public float smallImage0Height;
    public float smallImage0OffsetX;
    public float smallImage0OffsetY;
    public float smallImage0Width;
    public short subTag;
    public short tag;
    public int titleBackViewColor0;
    public int titleBackViewColor1;
    public int titleBackViewColor2;
    public int titleBackViewColor3;
    public float titleBackViewHeight;
    public float titleBackViewOffsetX;
    public float titleBackViewOffsetY;
    public float titleBackViewRadius;
    public float titleBackViewStrokeWidth1;
    public float titleBackViewStrokeWidth2;
    public float titleBackViewStrokeWidth3;
    public float titleBackViewWidth;
    public Typeface titleLabelTypeface;
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
    public Bitmap facebookButtonBitmap = null;
    public Bitmap lineButtonBitmap = null;
    public Bitmap mailButtonBitmap = null;
    protected Bitmap imageBitmap0 = null;

    public ContentPopUnit(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) throws IOException {
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
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.facebookButtonStatus = (short) 0;
        this.lineButtonStatus = (short) -1;
        this.mailButtonStatus = (short) 0;
        this.smallImage0OffsetX = BitmapDescriptorFactory.HUE_RED;
        this.smallImage0OffsetY = BitmapDescriptorFactory.HUE_RED;
        this.smallImage0Width = 60.0f;
        this.smallImage0Height = 60.0f;
        this.blockImageCutOffsetX = 0;
        this.blockImageCutOffsetY = 270;
        this.blockImageCutWidth = 320;
        this.blockImageCutHeight = 50;
        this.blockImageOffsetX = 0;
        this.blockImageOffsetY = 430;
        this.blockImageWidth = 320;
        this.blockImageHeight = 50;
        this.backStrokeViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewHeight = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backStrokeViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backStrokeViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backStrokeViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backStrokeViewRadius = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewHeight = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackViewRadius = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewHeight = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.imageView0BackViewStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.imageView0BackViewStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.imageView0BackViewStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewColor3 = FluctConstants.FRAME_ALPHA_COLOR;
        this.imageView0BackViewRadius = BitmapDescriptorFactory.HUE_RED;
        this.facebookButtonWidth = 40.0f;
        this.facebookButtonHeight = 40.0f;
        this.facebookButtonOffsetX = 105.0f;
        this.facebookButtonOffsetY = 160.0f;
        this.facebookButtonRadius = 6.0f;
        this.lineButtonWidth = 40.0f;
        this.lineButtonHeight = 40.0f;
        this.lineButtonOffsetX = 105.0f;
        this.lineButtonOffsetY = 160.0f;
        this.lineButtonRadius = 6.0f;
        this.mailButtonWidth = 40.0f;
        this.mailButtonHeight = 40.0f;
        this.mailButtonOffsetX = 105.0f;
        this.mailButtonOffsetY = 160.0f;
        this.mailButtonRadius = 6.0f;
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
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.facebookButtonStatus = (short) 0;
        this.lineButtonStatus = (short) -1;
        this.mailButtonStatus = (short) 0;
        this.backStrokeViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewHeight = 200.0f * this.zoomRate;
        this.backStrokeViewColor0 = -16;
        this.backStrokeViewStrokeWidth1 = 2.0f * this.zoomRate;
        this.backStrokeViewColor1 = -7576502;
        this.backStrokeViewStrokeWidth2 = 2.0f * this.zoomRate;
        this.backStrokeViewColor2 = 872415231;
        this.backStrokeViewStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backStrokeViewColor3 = 0;
        this.backStrokeViewRadius = 20.0f * this.zoomRate;
        this.blockImageCutOffsetX = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        this.blockImageCutOffsetY = (int) (270.0f * this.zoomRate);
        this.blockImageCutWidth = (int) (this.blockImageCutOffsetX + (320.0f * this.zoomRate));
        this.blockImageCutHeight = (int) (this.blockImageCutOffsetY + (50.0f * this.zoomRate));
        if (this.appDelegate != null && this.appDelegate.optionBackGroundBitmap != null) {
            float imageScale = this.finalWidth > BitmapDescriptorFactory.HUE_RED ? (this.zoomRate * this.appDelegate.optionBackGroundBitmap.getWidth()) / this.finalWidth : 1.0f;
            this.blockImageCutOffsetX = (int) (BitmapDescriptorFactory.HUE_RED * imageScale);
            this.blockImageCutOffsetY = (int) (270.0f * imageScale);
            this.blockImageCutWidth = this.blockImageCutOffsetX + this.appDelegate.optionBackGroundBitmap.getWidth();
            this.blockImageCutHeight = (int) (this.blockImageCutOffsetY + (50.0f * imageScale));
        }
        this.blockImageOffsetX = (int) (this.zoomRate * BitmapDescriptorFactory.HUE_RED);
        if (!this.appDelegate.isRetina4) {
            this.blockImageOffsetY = (int) ((-45.0f) * this.zoomRate);
        } else {
            this.blockImageOffsetY = (int) ((-1.0f) * this.zoomRate);
        }
        this.blockImageWidth = (int) (this.blockImageOffsetX + (320.0f * this.zoomRate));
        this.blockImageHeight = (int) (this.blockImageOffsetY + this.appDelegate.isRetina4Height);
        this.titleBackViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewWidth = BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewHeight = 34.0f * this.zoomRate;
        this.titleBackViewColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleBackViewStrokeWidth1 = 2.0f * this.zoomRate;
        this.titleBackViewColor1 = -200082;
        this.titleBackViewStrokeWidth2 = 2.0f * this.zoomRate;
        this.titleBackViewColor2 = -7576502;
        this.titleBackViewStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleBackViewColor3 = 0;
        this.titleBackViewRadius = 17.0f * this.zoomRate;
        this.imageView0BackViewOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.imageView0BackViewWidth = 72.0f * this.zoomRate;
        this.imageView0BackViewHeight = 72.0f * this.zoomRate;
        this.imageView0BackViewColor0 = -16;
        this.imageView0BackViewStrokeWidth1 = 2.0f * this.zoomRate;
        this.imageView0BackViewColor1 = -99;
        this.imageView0BackViewStrokeWidth2 = 2.0f * this.zoomRate;
        this.imageView0BackViewColor2 = -7576502;
        this.imageView0BackViewStrokeWidth3 = 2.0f * this.zoomRate;
        this.imageView0BackViewColor3 = 872415231;
        this.imageView0BackViewRadius = 6.0f * this.zoomRate;
        this.smallImage0OffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage0OffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.smallImage0Width = 60.0f * this.zoomRate;
        this.smallImage0Height = 60.0f * this.zoomRate;
        this.contentPopUnitBackView = new ContentPopUnitBackView(this.finalWidth, this.finalHeight, this.zoomRate, this);
        this.button0 = new CustomButtonType0(this.finalWidth, this.finalHeight, this.zoomRate);
        this.button0.delegate = this;
        this.button0.tag = (short) 0;
        this.button1 = new CustomButtonType0(this.finalWidth, this.finalHeight, this.zoomRate);
        this.button1.delegate = this;
        this.button1.tag = (short) 1;
        this.facebookButtonWidth = this.zoomRate * 40.0f;
        this.facebookButtonHeight = this.zoomRate * 40.0f;
        this.facebookButtonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.facebookButtonOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.facebookButtonRadius = 6.0f * this.zoomRate;
        this.lineButtonWidth = this.zoomRate * 40.0f;
        this.lineButtonHeight = this.zoomRate * 40.0f;
        this.lineButtonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.lineButtonOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.lineButtonRadius = 6.0f * this.zoomRate;
        this.mailButtonWidth = this.zoomRate * 40.0f;
        this.mailButtonHeight = this.zoomRate * 40.0f;
        this.mailButtonOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.mailButtonOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.mailButtonRadius = 6.0f * this.zoomRate;
        refreshSubBitmap();
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
        this.blockImageCutOffsetX = (int) this.backViewOffsetX;
        this.blockImageCutOffsetY = (int) this.backViewOffsetY;
        this.blockImageCutWidth = (int) (this.backViewOffsetX + this.backViewWidth);
        this.blockImageCutHeight = (int) (this.backViewOffsetY + this.backViewHeight);
        this.blockImageOffsetX = (int) (BitmapDescriptorFactory.HUE_RED * this.zoomRate);
        if (!this.appDelegate.isRetina4) {
            this.blockImageOffsetY = (int) ((-45.0f) * this.zoomRate);
        } else {
            this.blockImageOffsetY = (int) ((-1.0f) * this.zoomRate);
        }
        this.blockImageWidth = (int) (this.blockImageOffsetX + (320.0f * this.zoomRate));
        this.blockImageHeight = (int) (this.blockImageOffsetY + this.appDelegate.isRetina4Height);
        this.backStrokeViewOffsetX = this.backViewOffsetX + (15.0f * this.zoomRate);
        this.backStrokeViewOffsetY = this.backViewOffsetY + (41.0f * this.zoomRate);
        this.backStrokeViewWidth = this.backViewWidth - (30.0f * this.zoomRate);
        this.titleBackViewOffsetX = this.backViewOffsetX + (30.0f * this.zoomRate);
        this.titleBackViewOffsetY = this.backViewOffsetY + (25.0f * this.zoomRate);
        this.titleBackViewWidth = this.backViewWidth - (60.0f * this.zoomRate);
        this.imageView0BackViewOffsetX = this.backStrokeViewOffsetX + (34.0f * this.zoomRate);
        this.imageView0BackViewOffsetY = this.backStrokeViewOffsetY + (48.0f * this.zoomRate);
        this.smallImage0OffsetX = this.backStrokeViewOffsetX + (40.0f * this.zoomRate);
        this.smallImage0OffsetY = this.backStrokeViewOffsetY + (50.0f * this.zoomRate);
        this.facebookButtonOffsetX = (this.backStrokeViewOffsetX + (this.backStrokeViewWidth / 2.0f)) - (90.0f * this.zoomRate);
        this.facebookButtonOffsetY = this.backStrokeViewOffsetY + (140.0f * this.zoomRate);
        this.lineButtonOffsetX = (this.backStrokeViewOffsetX + (this.backStrokeViewWidth / 2.0f)) - (20.0f * this.zoomRate);
        this.lineButtonOffsetY = this.backStrokeViewOffsetY + (140.0f * this.zoomRate);
        this.mailButtonOffsetX = this.backStrokeViewOffsetX + (this.backStrokeViewWidth / 2.0f) + (50.0f * this.zoomRate);
        this.mailButtonOffsetY = this.backStrokeViewOffsetY + (140.0f * this.zoomRate);
    }

    public void setTitleLabelParams(String _titleLabelString, Typeface _titleLabelTypeface, float _offsetX, float _offsetY, float _fontSize, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2) {
        this.titleLabelString = _titleLabelString;
        short fontIndex = 0;
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            fontIndex = 1;
        }
        if (fontIndex == 0) {
            this.titleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.titleLabelFontSize = (5.0f + _fontSize) * this.zoomRate;
            this.titleLabelOffsetY = this.titleBackViewOffsetY + (1.2f * this.titleLabelFontSize);
        } else {
            this.titleLabelTypeface = Typeface.DEFAULT_BOLD;
            this.titleLabelFontSize = (1.0f + _fontSize) * this.zoomRate;
            this.titleLabelOffsetY = this.titleBackViewOffsetY + (1.4f * this.titleLabelFontSize);
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = this.titleBackViewOffsetX + ((this.titleBackViewWidth - newPaint.measureText(this.titleLabelString)) / 2.0f);
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
        short fontIndex = 0;
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            fontIndex = 1;
        }
        if (fontIndex == 0) {
            this.contentLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.contentLabelFontSize = (4.0f + _fontSize) * this.zoomRate;
        } else {
            this.contentLabelTypeface = Typeface.DEFAULT_BOLD;
            this.contentLabelFontSize = (BitmapDescriptorFactory.HUE_RED + _fontSize) * this.zoomRate;
        }
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.contentLabelTypeface);
        newPaint.setTextSize(this.contentLabelFontSize);
        this.contentLabelOffsetX0 = this.backStrokeViewOffsetX + (118.0f * this.zoomRate);
        this.contentLabelOffsetX1 = this.backStrokeViewOffsetX + (118.0f * this.zoomRate);
        this.contentLabelOffsetX2 = this.backStrokeViewOffsetX + (118.0f * this.zoomRate);
        this.contentLabelOffsetX3 = this.backStrokeViewOffsetX + (118.0f * this.zoomRate);
        this.contentLabelOffsetX4 = this.backStrokeViewOffsetX + (118.0f * this.zoomRate);
        this.contentLabelOffsetY = this.backStrokeViewOffsetY + (this.zoomRate * _offsetY) + this.contentLabelFontSize;
        this.contentLabelSpaceY = this.zoomRate * _spaceY;
        this.contentLabelColor0 = _color0;
        this.contentLabelStroke1Width = this.zoomRate * _strokeWidth1;
        this.contentLabelColor1 = _color1;
        this.contentLabelStroke2Width = this.zoomRate * _strokeWidth2;
        this.contentLabelColor2 = _color2;
    }

    public void setType(short _type, String _button0TitleLabelString, String _button1TitleLabelString, Typeface _buttonTitleLabelTypeface, float _buttonTitleLabelFontSize, int _buttonTitleLabelColor0, float _buttonTitleLabelStroke1Width, int _buttonTitleLabelColor1, float _buttonTitleLabelStroke2Width, int _buttonTitleLabelColor2, int _color0, float _strokeWidth1, int _color1, float _strokeWidth2, int _color2, float _strokeWidth3, int _color3, float _radius, Integer _button0SeIndex, Integer _button1SeIndex, Boolean _fadeOutAnimeF) {
        this.type = _type;
        this.buttonWidth = 110.0f * this.zoomRate;
        this.buttonHeight = 32.0f * this.zoomRate;
        this.buttonOffsetY = ((this.backViewOffsetY + this.backViewHeight) - this.buttonHeight) - (12.0f * this.zoomRate);
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
    }

    public void clearSubBitmap() {
        if (this.facebookButtonBitmap != null) {
            if (!this.facebookButtonBitmap.isRecycled()) {
                this.facebookButtonBitmap.recycle();
            }
            this.facebookButtonBitmap = null;
        }
        if (this.lineButtonBitmap != null) {
            if (!this.lineButtonBitmap.isRecycled()) {
                this.lineButtonBitmap.recycle();
            }
            this.lineButtonBitmap = null;
        }
        if (this.mailButtonBitmap != null) {
            if (!this.mailButtonBitmap.isRecycled()) {
                this.mailButtonBitmap.recycle();
            }
            this.mailButtonBitmap = null;
        }
    }

    public void refreshSubBitmap() throws IOException {
        clearSubBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = 1;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                InputStream inputStream = asm.open("png/Share/facebook_icon.png");
                this.facebookButtonBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                InputStream inputStream2 = asm.open("png/Share/line_icon.png");
                this.lineButtonBitmap = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
            try {
                InputStream inputStream3 = asm.open("png/Share/mail_icon.png");
                this.mailButtonBitmap = BitmapFactory.decodeStream(inputStream3, null, opt2);
                inputStream3.close();
            } catch (IOException e3) {
            }
        }
    }

    public void clearDrawableBitmap0() {
        if (this.imageBitmap0 != null) {
            this.imageBitmap0 = null;
        }
    }

    public void changeDrawableBitmap0(Bitmap _imageBitmap0, int _alpha) {
        clearDrawableBitmap0();
        this.imageBitmap0 = _imageBitmap0;
    }

    public boolean checkLineButtonStatuse() {
        if (this.appDelegate == null) {
            return false;
        }
        if (this.appDelegate.checkPackageInstalled("jp.naver.line.android")) {
            this.lineButtonStatus = (short) 0;
            return true;
        }
        this.lineButtonStatus = (short) -1;
        return false;
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void pop() {
        checkLineButtonStatuse();
        changeNowStatus(0);
    }

    public void changeNowStatus(int _newStatus) {
        Log.d("AlertType0Layout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            this.nowStatus = (short) -1;
            unclickAllButton();
            this.titleLabelString = "";
            this.contentLabelString0 = "";
            this.contentLabelString1 = "";
            this.contentLabelString2 = "";
            this.contentLabelString3 = "";
            this.contentLabelString4 = "";
            this.button0TitleLabelString = "";
            this.button1TitleLabelString = "";
            clearDrawableBitmap0();
            this.buttonIndex = (short) -1;
            this.button0.changeNowStatus(-1);
            this.button1.changeNowStatus(-1);
            this.hidden = true;
            return;
        }
        if (_newStatus == 0) {
            if (this.nowStatus == -1) {
                this.nowStatus = (short) 0;
                unclickAllButton();
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
                this.button0.changeNowStatus(-1);
                this.button1.changeNowStatus(-1);
                this.buttonIndex = (short) -1;
                this.hidden = true;
            }
        }
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
                returnF = this.button1.gameOnTouch(event);
            }
            if (!returnF && event.getX() > this.facebookButtonOffsetX && event.getX() < this.facebookButtonOffsetX + this.facebookButtonWidth && event.getY() >= this.facebookButtonOffsetY && event.getY() <= this.facebookButtonOffsetY + this.facebookButtonHeight) {
                this.selectedButtonIndex = (short) 10;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.custom.ContentPopUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ContentPopUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (!returnF && this.lineButtonStatus == 0 && event.getX() > this.lineButtonOffsetX && event.getX() < this.lineButtonOffsetX + this.lineButtonWidth && event.getY() >= this.lineButtonOffsetY && event.getY() <= this.lineButtonOffsetY + this.lineButtonHeight) {
                this.selectedButtonIndex = (short) 11;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.custom.ContentPopUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ContentPopUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (!returnF && event.getX() > this.mailButtonOffsetX && event.getX() < this.mailButtonOffsetX + this.mailButtonWidth && event.getY() >= this.mailButtonOffsetY && event.getY() <= this.mailButtonOffsetY + this.mailButtonHeight) {
                this.selectedButtonIndex = (short) 12;
                this.buttonClickCnt = (short) 0;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.custom.ContentPopUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                        ContentPopUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            returnF = true;
        } else if (this.nowStatus == 0 || this.nowStatus == 2) {
            returnF = true;
        }
        return returnF;
    }

    public void doClick() {
        if (this.selectedButtonIndex >= 0) {
            if (this.selectedButtonIndex == 10) {
                this.delegate.contentPopUnitButtonClick(this.selectedButtonIndex);
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
            } else if (this.selectedButtonIndex == 11) {
                if (checkLineButtonStatuse()) {
                    this.delegate.contentPopUnitButtonClick(this.selectedButtonIndex);
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(1);
                    }
                }
            } else if (this.selectedButtonIndex == 12) {
                this.delegate.contentPopUnitButtonClick(this.selectedButtonIndex);
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
            }
        }
        unclickAllButton();
    }

    public void unclickAllButton() {
        this.selectedButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        if (this.appDelegate != null) {
            if (this.contentPopUnitBackView != null) {
                this.contentPopUnitBackView.gameDraw(canvas);
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
        clearSubBitmap();
        clearDrawableBitmap0();
        if (this.button0 != null) {
            this.button0.onDestroy();
            this.button0 = null;
        }
        if (this.button1 != null) {
            this.button1.onDestroy();
            this.button1 = null;
        }
        if (this.contentPopUnitBackView != null) {
            this.contentPopUnitBackView.onDestroy();
            this.contentPopUnitBackView = null;
        }
        this.appDelegate = null;
    }
}
