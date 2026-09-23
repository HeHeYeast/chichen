package com.idtinc.ckchickandduck;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.custom.MyDraw;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainMenuViewUnit {
    private static final int startLabelTextAddAlphaUnit = 10;
    private AppDelegate appDelegate;
    private short buttonClickCnt;
    private int copyrightLabelColor0;
    private int copyrightLabelColor1;
    private int copyrightLabelColor2;
    private float copyrightLabelFontSize;
    private float copyrightLabelOffsetX;
    private float copyrightLabelOffsetY;
    int copyrightLabelShadowColor;
    float copyrightLabelShadowOffsetX;
    float copyrightLabelShadowOffsetY;
    float copyrightLabelShadowOpacity;
    public String copyrightLabelString;
    private float copyrightLabelStroke1Width;
    private float copyrightLabelStroke2Width;
    Typeface copyrightLabelTypeface;
    public float downloadSaveFileBitmapButtonHeight;
    public float downloadSaveFileBitmapButtonOffsetX;
    public float downloadSaveFileBitmapButtonOffsetY;
    public float downloadSaveFileBitmapButtonWidth;
    public int downloadSaveFileButtonColor0;
    public int downloadSaveFileButtonColor1;
    public int downloadSaveFileButtonColor2;
    public int downloadSaveFileButtonColor3;
    public float downloadSaveFileButtonHeight;
    public float downloadSaveFileButtonOffsetX;
    public float downloadSaveFileButtonOffsetY;
    public float downloadSaveFileButtonRadius;
    public int downloadSaveFileButtonShadowColor;
    public float downloadSaveFileButtonShadowOffsetX;
    public float downloadSaveFileButtonShadowOffsetY;
    public float downloadSaveFileButtonShadowOpacity;
    private short downloadSaveFileButtonStatus;
    public float downloadSaveFileButtonStrokeWidth1;
    public float downloadSaveFileButtonStrokeWidth2;
    public float downloadSaveFileButtonStrokeWidth3;
    public float downloadSaveFileButtonTouchRangeXMax;
    public float downloadSaveFileButtonTouchRangeXMin;
    public float downloadSaveFileButtonTouchRangeYMax;
    public float downloadSaveFileButtonTouchRangeYMin;
    public float downloadSaveFileButtonWidth;
    private Paint fadeFrontViewPaint;
    public float finalHeight;
    public float finalWidth;
    private MainMenuViewController mainMenuViewController;
    private MyDraw myDraw;
    private float startLabelOffsetX;
    private float startLabelOffsetY;
    private int startLabelTextAddAlpha;
    private int startLabelTextAlpha;
    private Typeface startLabelTypeface;
    private short touchButtonIndex;
    public float zoomRate;
    private float startLabelFontSize = 32.0f;
    private float startLabelStroke1Width = 6.0f;
    private float startLabelStroke2Width = 10.0f;
    private int fadeFrontViewAlpha = 0;
    private Bitmap backGroundBitmap = null;
    private Bitmap logoBitmap = null;
    private Bitmap downloadSaveFileButtonBitmap = null;

    public MainMenuViewUnit(float _finalwidth, float _finalheight, float _zoomrate, MainMenuViewController _mainMenuViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.downloadSaveFileButtonStatus = (short) -1;
        this.startLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.startLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.startLabelTextAlpha = 0;
        this.startLabelTextAddAlpha = 0;
        this.copyrightLabelString = "";
        this.copyrightLabelFontSize = 12.0f;
        this.copyrightLabelColor0 = 0;
        this.copyrightLabelStroke1Width = 2.0f;
        this.copyrightLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.copyrightLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelColor2 = 0;
        this.copyrightLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelShadowColor = -8947849;
        this.copyrightLabelShadowOpacity = 1.0f;
        this.copyrightLabelShadowOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelShadowOffsetY = 1.0f;
        this.downloadSaveFileButtonWidth = 44.0f;
        this.downloadSaveFileButtonHeight = 44.0f;
        this.downloadSaveFileButtonOffsetX = 312.0f - this.downloadSaveFileButtonWidth;
        this.downloadSaveFileButtonOffsetY = 8.0f;
        this.downloadSaveFileButtonColor0 = -16;
        this.downloadSaveFileButtonStrokeWidth1 = 2.0f;
        this.downloadSaveFileButtonColor1 = -7576502;
        this.downloadSaveFileButtonStrokeWidth2 = 1.0f;
        this.downloadSaveFileButtonColor2 = -16;
        this.downloadSaveFileButtonStrokeWidth3 = 0.5f;
        this.downloadSaveFileButtonColor3 = 855638016;
        this.downloadSaveFileButtonRadius = 22.0f;
        this.downloadSaveFileButtonShadowOpacity = 2.0f;
        this.downloadSaveFileButtonShadowOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonShadowOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonShadowColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.downloadSaveFileBitmapButtonWidth = 14.0f;
        this.downloadSaveFileBitmapButtonHeight = 14.0f;
        this.downloadSaveFileBitmapButtonOffsetX = 14.0f;
        this.downloadSaveFileBitmapButtonOffsetY = 59.0f;
        this.downloadSaveFileButtonTouchRangeXMin = BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonTouchRangeXMax = BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonTouchRangeYMin = BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonTouchRangeYMax = BitmapDescriptorFactory.HUE_RED;
        this.myDraw = null;
        this.appDelegate = _appDelegate;
        this.mainMenuViewController = _mainMenuViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.downloadSaveFileButtonStatus = (short) 0;
        this.startLabelFontSize *= this.zoomRate;
        this.startLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.startLabelStroke1Width *= this.zoomRate;
        this.startLabelStroke2Width *= this.zoomRate;
        Paint newPaint = new Paint(257);
        newPaint.setTypeface(this.startLabelTypeface);
        newPaint.setTextSize(this.startLabelFontSize);
        this.startLabelOffsetX = (this.finalWidth - newPaint.measureText("Touch to Start")) / 2.0f;
        if (!this.appDelegate.isRetina4) {
            this.startLabelOffsetY = (210.0f * this.zoomRate) + this.startLabelFontSize;
        } else {
            this.startLabelOffsetY = (254.0f * this.zoomRate) + this.startLabelFontSize;
        }
        this.startLabelTextAlpha = 0;
        this.startLabelTextAddAlpha = 10;
        StringBuilder sb = new StringBuilder("Version ");
        this.appDelegate.getClass();
        this.copyrightLabelString = sb.append("2.3.0").append("   ").append("©2013 iDT Digital Inc.").toString();
        this.copyrightLabelTypeface = Typeface.DEFAULT_BOLD;
        this.copyrightLabelFontSize = this.zoomRate * 12.0f;
        this.copyrightLabelColor0 = -1;
        this.copyrightLabelStroke1Width = this.zoomRate * 2.0f;
        this.copyrightLabelColor1 = FluctConstants.FRAME_BASE_COLOR;
        this.copyrightLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        newPaint.setTypeface(this.copyrightLabelTypeface);
        newPaint.setTextSize(this.copyrightLabelFontSize);
        this.copyrightLabelOffsetX = (this.finalWidth - newPaint.measureText(this.copyrightLabelString)) / 2.0f;
        this.copyrightLabelOffsetY = this.finalHeight - (14.0f * this.zoomRate);
        this.copyrightLabelShadowColor = FluctConstants.FRAME_ALPHA_COLOR;
        this.copyrightLabelShadowOpacity = this.zoomRate * 2.0f;
        this.copyrightLabelShadowOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.copyrightLabelShadowOffsetY = this.zoomRate * 1.0f;
        this.downloadSaveFileButtonWidth = 44.0f * this.zoomRate;
        this.downloadSaveFileButtonHeight = 44.0f * this.zoomRate;
        this.downloadSaveFileButtonOffsetX = (312.0f * this.zoomRate) - this.downloadSaveFileButtonWidth;
        this.downloadSaveFileButtonOffsetY = 8.0f * this.zoomRate;
        this.downloadSaveFileButtonColor0 = -3889;
        this.downloadSaveFileButtonStrokeWidth1 = this.zoomRate * 2.0f;
        this.downloadSaveFileButtonColor1 = -3109815;
        this.downloadSaveFileButtonStrokeWidth2 = this.zoomRate * 1.0f;
        this.downloadSaveFileButtonColor2 = -16;
        this.downloadSaveFileButtonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonColor3 = 0;
        this.downloadSaveFileButtonRadius = 22.0f * this.zoomRate;
        this.downloadSaveFileButtonShadowOpacity = 3.0f * this.zoomRate;
        this.downloadSaveFileButtonShadowOffsetX = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonShadowOffsetY = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.downloadSaveFileButtonShadowColor = -1728053248;
        this.downloadSaveFileBitmapButtonWidth = 36.0f * this.zoomRate;
        this.downloadSaveFileBitmapButtonHeight = 36.0f * this.zoomRate;
        this.downloadSaveFileBitmapButtonOffsetX = this.downloadSaveFileButtonOffsetX + (4.0f * this.zoomRate);
        this.downloadSaveFileBitmapButtonOffsetY = this.downloadSaveFileButtonOffsetY + (3.0f * this.zoomRate);
        this.downloadSaveFileButtonTouchRangeXMin = this.downloadSaveFileButtonOffsetX - (this.zoomRate * 12.0f);
        this.downloadSaveFileButtonTouchRangeXMax = this.downloadSaveFileButtonOffsetX + this.downloadSaveFileButtonWidth + (this.zoomRate * 12.0f);
        this.downloadSaveFileButtonTouchRangeYMin = this.downloadSaveFileButtonOffsetY - (this.zoomRate * 12.0f);
        this.downloadSaveFileButtonTouchRangeYMax = this.downloadSaveFileButtonOffsetY + this.downloadSaveFileButtonHeight + (this.zoomRate * 12.0f);
        refreshBackGround();
        this.fadeFrontViewPaint = new Paint(257);
        this.fadeFrontViewPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        this.fadeFrontViewPaint.setStyle(Paint.Style.FILL);
        this.myDraw = new MyDraw();
    }

    public void clearDrawable() {
        if (this.backGroundBitmap != null) {
            if (!this.backGroundBitmap.isRecycled()) {
                this.backGroundBitmap.recycle();
            }
            this.backGroundBitmap = null;
        }
        if (this.logoBitmap != null) {
            if (!this.logoBitmap.isRecycled()) {
                this.logoBitmap.recycle();
            }
            this.logoBitmap = null;
        }
        if (this.downloadSaveFileButtonBitmap != null) {
            if (!this.downloadSaveFileButtonBitmap.isRecycled()) {
                this.downloadSaveFileButtonBitmap.recycle();
            }
            this.downloadSaveFileButtonBitmap = null;
        }
    }

    public void refreshBackGround() {
        InputStream inputStream;
        clearDrawable();
        AssetManager asm = this.appDelegate.getAssets();
        try {
            InputStream inputStream2 = asm.open("png/MainMenu/main_bg.jpg");
            BufferedInputStream buf = new BufferedInputStream(inputStream2);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(buf, null, opt);
            int scale = 1;
            if (this.appDelegate != null) {
                scale = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
            }
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = scale;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            this.backGroundBitmap = BitmapFactory.decodeStream(inputStream2, null, opt2);
            inputStream2.close();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                inputStream = asm.open("png/MainMenu/main_logo_ja.png");
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                inputStream = asm.open("png/MainMenu/main_logo_tw.png");
            } else if (languageString.equals("zh-CN")) {
                inputStream = asm.open("png/MainMenu/main_logo_cn.png");
            } else {
                inputStream = asm.open("png/MainMenu/main_logo_en.png");
            }
            this.logoBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
            inputStream.close();
            InputStream inputStream3 = asm.open("png/MainGame/download.png");
            this.downloadSaveFileButtonBitmap = BitmapFactory.decodeStream(inputStream3, null, opt2);
            inputStream3.close();
        } catch (IOException e) {
        }
    }

    public void changeMainMenuLayoutNowStatus(int _newStatus) {
        Log.d("changeMainMenuLayoutNowStatus", "changeMainMenuLayoutNowStatus " + _newStatus);
        if (_newStatus == 0) {
            this.mainMenuViewController.nowStatus = (short) 0;
            this.startLabelTextAlpha = 0;
            this.startLabelTextAddAlpha = 10;
            this.fadeFrontViewAlpha = 0;
            return;
        }
        if (_newStatus == 1) {
            if (this.mainMenuViewController.nowStatus == 0) {
                this.mainMenuViewController.nowStatus = (short) 1;
                this.fadeFrontViewAlpha = 0;
                doFadeOut();
                return;
            }
            return;
        }
        if (_newStatus == 2) {
            if (this.mainMenuViewController.nowStatus == 1) {
                this.mainMenuViewController.nowStatus = (short) 2;
                this.fadeFrontViewAlpha = MotionEventCompat.ACTION_MASK;
                this.mainMenuViewController.goToSavesCheck();
                return;
            }
            return;
        }
        if (_newStatus == 3 && this.mainMenuViewController.nowStatus == 2) {
            this.mainMenuViewController.nowStatus = (short) 3;
            this.fadeFrontViewAlpha = MotionEventCompat.ACTION_MASK;
            doFadeIn();
        }
    }

    public void doFadeOut() {
        if (this.mainMenuViewController.nowStatus == 1) {
            this.fadeFrontViewAlpha += 25;
            if (this.fadeFrontViewAlpha >= 275) {
                this.fadeFrontViewAlpha = 275;
                changeMainMenuLayoutNowStatus(2);
            } else {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.MainMenuViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MainMenuViewUnit.this.doFadeOut();
                    }
                }, 50L);
            }
        }
    }

    public void doFadeIn() {
        if (this.mainMenuViewController.nowStatus == 3) {
            this.fadeFrontViewAlpha -= 25;
            if (this.fadeFrontViewAlpha <= 0) {
                this.fadeFrontViewAlpha = 0;
                changeMainMenuLayoutNowStatus(0);
            } else {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.MainMenuViewUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        MainMenuViewUnit.this.doFadeIn();
                    }
                }, 50L);
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.mainMenuViewController.nowStatus != 0 || event.getAction() != 0) {
            return false;
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        if (this.downloadSaveFileButtonStatus == 0 && event.getY() > this.downloadSaveFileButtonTouchRangeYMin && event.getY() < this.downloadSaveFileButtonTouchRangeYMax && event.getX() > this.downloadSaveFileButtonTouchRangeXMin && event.getX() < this.downloadSaveFileButtonTouchRangeXMax) {
            this.touchButtonIndex = (short) 0;
            this.buttonClickCnt = (short) 0;
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.MainMenuViewUnit.3
                @Override // java.lang.Runnable
                public void run() {
                    MainMenuViewUnit.this.doClick();
                }
            }, 200L);
            return true;
        }
        changeMainMenuLayoutNowStatus(1);
        this.appDelegate.doSoundPoolPlay(0);
        return true;
    }

    public void doClick() {
        if (this.touchButtonIndex == 0 && this.mainMenuViewController.nowStatus == 0 && this.appDelegate != null) {
            this.appDelegate.doSoundPoolPlay(1);
            this.appDelegate.openOnlineGameViewControllerWithAutoLogIn(true, (short) 200);
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        if (this.appDelegate != null) {
            if (this.mainMenuViewController.nowStatus == 0) {
                Paint bitmapPaint = new Paint();
                if (this.backGroundBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                    } else {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                    }
                }
                this.startLabelTextAlpha += this.startLabelTextAddAlpha;
                if (this.startLabelTextAlpha >= 255) {
                    this.startLabelTextAlpha = MotionEventCompat.ACTION_MASK;
                    this.startLabelTextAddAlpha = -10;
                } else if (this.startLabelTextAlpha <= 0) {
                    this.startLabelTextAlpha = 0;
                    this.startLabelTextAddAlpha = 10;
                }
                MyDraw.drawStrokeText(canvas, this.startLabelOffsetX, this.startLabelOffsetY, this.startLabelTypeface, "Touch to Start", this.startLabelFontSize, -6106, this.startLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.startLabelStroke2Width, -16);
                if (this.backGroundBitmap != null) {
                    bitmapPaint.setAlpha(this.startLabelTextAlpha);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                    } else {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                    }
                }
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                MyDraw.drawShadowStrokeText(canvas, this.copyrightLabelOffsetX, this.copyrightLabelOffsetY, this.copyrightLabelTypeface, this.copyrightLabelString, this.copyrightLabelFontSize, this.copyrightLabelColor0, this.copyrightLabelStroke1Width, this.copyrightLabelColor1, this.copyrightLabelStroke2Width, this.copyrightLabelColor2, this.copyrightLabelShadowColor, (int) this.copyrightLabelShadowOpacity, this.copyrightLabelShadowOffsetX, this.copyrightLabelShadowOffsetY);
                if (this.logoBitmap != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                    } else {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                    }
                }
                if (this.downloadSaveFileButtonStatus == 0) {
                    MyDraw.drawStrokeRectWithShadow(canvas, this.downloadSaveFileButtonOffsetX, this.downloadSaveFileButtonOffsetY, this.downloadSaveFileButtonWidth, this.downloadSaveFileButtonHeight, this.downloadSaveFileButtonColor0, this.downloadSaveFileButtonStrokeWidth1, this.downloadSaveFileButtonColor1, this.downloadSaveFileButtonStrokeWidth2, this.downloadSaveFileButtonColor2, this.downloadSaveFileButtonStrokeWidth3, this.downloadSaveFileButtonColor3, this.downloadSaveFileButtonRadius, this.downloadSaveFileButtonShadowOpacity, this.downloadSaveFileButtonShadowOffsetX, this.downloadSaveFileButtonShadowOffsetY, this.downloadSaveFileButtonShadowColor);
                    if (this.downloadSaveFileButtonBitmap != null) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        canvas.drawBitmap(this.downloadSaveFileButtonBitmap, new Rect(0, 0, this.downloadSaveFileButtonBitmap.getWidth(), this.downloadSaveFileButtonBitmap.getHeight()), new Rect((int) this.downloadSaveFileBitmapButtonOffsetX, (int) this.downloadSaveFileBitmapButtonOffsetY, (int) (this.downloadSaveFileBitmapButtonOffsetX + this.downloadSaveFileBitmapButtonWidth), (int) (this.downloadSaveFileBitmapButtonOffsetY + this.downloadSaveFileBitmapButtonHeight)), bitmapPaint);
                    }
                    if (this.touchButtonIndex == 0) {
                        MyDraw.drawStrokeRect(canvas, this.downloadSaveFileButtonOffsetX, this.downloadSaveFileButtonOffsetY, this.downloadSaveFileButtonWidth, this.downloadSaveFileButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.downloadSaveFileButtonRadius);
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.mainMenuViewController.nowStatus == 1) {
                this.fadeFrontViewPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
                Paint bitmapPaint2 = new Paint();
                if (this.backGroundBitmap != null) {
                    bitmapPaint2.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint2);
                    } else {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint2);
                    }
                }
                MyDraw.drawShadowStrokeText(canvas, this.copyrightLabelOffsetX, this.copyrightLabelOffsetY, this.copyrightLabelTypeface, this.copyrightLabelString, this.copyrightLabelFontSize, this.copyrightLabelColor0, this.copyrightLabelStroke1Width, this.copyrightLabelColor1, this.copyrightLabelStroke2Width, this.copyrightLabelColor2, this.copyrightLabelShadowColor, (int) this.copyrightLabelShadowOpacity, this.copyrightLabelShadowOffsetX, this.copyrightLabelShadowOffsetY);
                if (this.logoBitmap != null) {
                    bitmapPaint2.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint2);
                    } else {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint2);
                    }
                }
                if (this.fadeFrontViewAlpha < 0) {
                    this.fadeFrontViewPaint.setAlpha(0);
                } else if (this.fadeFrontViewAlpha > 255) {
                    this.fadeFrontViewPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                } else {
                    this.fadeFrontViewPaint.setAlpha(this.fadeFrontViewAlpha);
                }
                canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, this.fadeFrontViewPaint);
                return;
            }
            if (this.mainMenuViewController.nowStatus != 2 && this.mainMenuViewController.nowStatus == 3) {
                this.fadeFrontViewPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
                Paint bitmapPaint3 = new Paint();
                if (this.backGroundBitmap != null) {
                    bitmapPaint3.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint3);
                    } else {
                        canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint3);
                    }
                }
                MyDraw.drawShadowStrokeText(canvas, this.copyrightLabelOffsetX, this.copyrightLabelOffsetY, this.copyrightLabelTypeface, this.copyrightLabelString, this.copyrightLabelFontSize, this.copyrightLabelColor0, this.copyrightLabelStroke1Width, this.copyrightLabelColor1, this.copyrightLabelStroke2Width, this.copyrightLabelColor2, this.copyrightLabelShadowColor, (int) this.copyrightLabelShadowOpacity, this.copyrightLabelShadowOffsetX, this.copyrightLabelShadowOffsetY);
                if (this.logoBitmap != null) {
                    bitmapPaint3.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint3);
                    } else {
                        canvas.drawBitmap(this.logoBitmap, new Rect(0, 0, this.logoBitmap.getWidth(), this.logoBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint3);
                    }
                }
                if (this.fadeFrontViewAlpha < 0) {
                    this.fadeFrontViewPaint.setAlpha(0);
                } else if (this.fadeFrontViewAlpha > 255) {
                    this.fadeFrontViewPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                } else {
                    this.fadeFrontViewPaint.setAlpha(this.fadeFrontViewAlpha);
                }
                canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, this.fadeFrontViewPaint);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.fadeFrontViewPaint = null;
        clearDrawable();
        this.mainMenuViewController = null;
        this.appDelegate = null;
    }
}
