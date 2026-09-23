package com.idtinc.ck_bonus;

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
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import java.io.InputStream;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class BonusFrontViewUnit {
    private AppDelegate appDelegate;
    public int backButtonColor0;
    public int backButtonColor1;
    public int backButtonColor2;
    public int backButtonColor3;
    public float backButtonHeight;
    public float backButtonOffsetX;
    public float backButtonOffsetY;
    public float backButtonRadius;
    private short backButtonStatus;
    public float backButtonStrokeWidth1;
    public float backButtonStrokeWidth2;
    public float backButtonStrokeWidth3;
    public int backButtonTitleLabelColor0;
    public int backButtonTitleLabelColor1;
    public int backButtonTitleLabelColor2;
    public float backButtonTitleLabelFontSize;
    public float backButtonTitleLabelOffsetX;
    public float backButtonTitleLabelOffsetY;
    public String backButtonTitleLabelString;
    public float backButtonTitleLabelStroke1Width;
    public float backButtonTitleLabelStroke2Width;
    Typeface backButtonTitleLabelTypeface;
    public float backButtonWidth;
    private BonusUnitView bonusUnitView;
    public int bottomViewColor0;
    public int bottomViewColor1;
    private float bottomViewHeight;
    private float bottomViewOffsetX;
    private float bottomViewOffsetY;
    public float bottomViewStrokeWidth1;
    private float bottomViewWidth;
    private short buttonClickCnt;
    private float finalHeight;
    private float finalWidth;
    private Bitmap reloadButtonBitmap = null;
    float reloadButtonBitmapHeight;
    float reloadButtonBitmapOffsetX;
    float reloadButtonBitmapOffsetY;
    float reloadButtonBitmapWidth;
    public int reloadButtonColor0;
    public int reloadButtonColor1;
    public int reloadButtonColor2;
    public int reloadButtonColor3;
    public float reloadButtonHeight;
    public float reloadButtonOffsetX;
    public float reloadButtonOffsetY;
    public float reloadButtonRadius;
    public short reloadButtonStatus;
    public float reloadButtonStrokeWidth1;
    public float reloadButtonStrokeWidth2;
    public float reloadButtonStrokeWidth3;
    public float reloadButtonWidth;
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
    public int topViewColor0;
    public int topViewColor1;
    private float topViewHeight;
    private float topViewOffsetX;
    private float topViewOffsetY;
    public float topViewStrokeWidth1;
    private float topViewWidth;
    private short touchButtonIndex;
    private float zoomRate;

    public BonusFrontViewUnit(float _finalwidth, float _finalheight, float _zoomrate, BonusUnitView _bonusUnitView, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) -1;
        this.reloadButtonStatus = (short) -1;
        this.topViewOffsetX = -1.0f;
        this.topViewOffsetY = -1.0f;
        this.topViewWidth = 322.0f;
        this.topViewHeight = 41.0f;
        this.topViewColor0 = -2248316;
        this.topViewStrokeWidth1 = 1.0f;
        this.topViewColor1 = -872415232;
        this.titleLabelString = "";
        this.titleLabelFontSize = 22.0f;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 2.0f;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        this.titleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.titleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backButtonWidth = 50.0f;
        this.backButtonHeight = 30.0f;
        this.backButtonOffsetX = 5.0f;
        this.backButtonOffsetY = 4.0f;
        this.backButtonColor0 = -7576502;
        this.backButtonStrokeWidth1 = 1.0f;
        this.backButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.backButtonColor2 = 0;
        this.backButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.backButtonColor3 = 0;
        this.backButtonRadius = 6.0f;
        this.backButtonTitleLabelString = "";
        this.backButtonTitleLabelFontSize = 14.0f;
        this.backButtonTitleLabelColor0 = -1;
        this.backButtonTitleLabelStroke1Width = 0.5f;
        this.backButtonTitleLabelColor1 = -1;
        this.backButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelColor2 = 0;
        this.backButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.reloadButtonWidth = 50.0f;
        this.reloadButtonHeight = 40.0f;
        this.reloadButtonOffsetX = 275.0f;
        this.reloadButtonOffsetY = 4.0f;
        this.reloadButtonColor0 = -7576502;
        this.reloadButtonStrokeWidth1 = 1.0f;
        this.reloadButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.reloadButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.reloadButtonColor2 = 0;
        this.reloadButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.reloadButtonColor3 = 0;
        this.reloadButtonRadius = 6.0f;
        this.bottomViewOffsetX = -1.0f;
        this.bottomViewOffsetY = 429.0f;
        this.bottomViewWidth = 322.0f;
        this.bottomViewHeight = 51.0f;
        this.bottomViewColor0 = -2248316;
        this.bottomViewStrokeWidth1 = 1.0f;
        this.bottomViewColor1 = -872415232;
        this.reloadButtonBitmapOffsetX = 11.0f;
        this.reloadButtonBitmapOffsetY = 6.0f;
        this.reloadButtonBitmapWidth = 18.0f;
        this.reloadButtonBitmapHeight = 18.0f;
        this.appDelegate = _appDelegate;
        this.bonusUnitView = _bonusUnitView;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.backButtonStatus = (short) 0;
        this.reloadButtonStatus = (short) 0;
        this.topViewOffsetX = this.zoomRate * (-1.0f);
        this.topViewOffsetY = this.zoomRate * (-1.0f);
        this.topViewWidth = 322.0f * this.zoomRate;
        this.topViewHeight = 41.0f * this.zoomRate;
        this.topViewColor0 = -2248316;
        this.topViewStrokeWidth1 = this.zoomRate * 1.0f;
        this.topViewColor1 = -872415232;
        Paint newPaint = new Paint(257);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            this.titleLabelString = "cp 獲得";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            this.titleLabelString = "獲得 cp";
        } else if (languageString.equals("zh-CN")) {
            this.titleLabelString = "获得 cp";
        } else {
            this.titleLabelString = "Get cp";
        }
        this.titleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.titleLabelFontSize = 22.0f * this.zoomRate;
        this.titleLabelColor0 = -1;
        this.titleLabelStroke1Width = 2.0f * this.zoomRate;
        this.titleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.titleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.titleLabelColor2 = 0;
        newPaint.setTypeface(this.titleLabelTypeface);
        newPaint.setTextSize(this.titleLabelFontSize);
        this.titleLabelOffsetX = ((320.0f * this.zoomRate) - newPaint.measureText(this.titleLabelString)) / 2.0f;
        this.titleLabelOffsetY = 26.0f * this.zoomRate;
        this.backButtonWidth = 50.0f * this.zoomRate;
        this.backButtonHeight = 30.0f * this.zoomRate;
        this.backButtonOffsetX = 5.0f * this.zoomRate;
        this.backButtonOffsetY = 4.0f * this.zoomRate;
        this.backButtonColor0 = -7576502;
        this.backButtonStrokeWidth1 = this.zoomRate * 1.0f;
        this.backButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.backButtonStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backButtonColor2 = 0;
        this.backButtonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backButtonColor3 = 0;
        this.backButtonRadius = 6.0f * this.zoomRate;
        this.backButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Back);
        this.backButtonTitleLabelTypeface = Typeface.DEFAULT_BOLD;
        this.backButtonTitleLabelFontSize = 14.0f * this.zoomRate;
        this.backButtonTitleLabelColor0 = -1;
        this.backButtonTitleLabelStroke1Width = 0.5f * this.zoomRate;
        this.backButtonTitleLabelColor1 = -1;
        this.backButtonTitleLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.backButtonTitleLabelColor2 = 0;
        newPaint.setTypeface(this.backButtonTitleLabelTypeface);
        newPaint.setTextSize(this.backButtonTitleLabelFontSize);
        this.backButtonTitleLabelOffsetX = this.backButtonOffsetX + ((this.backButtonWidth - newPaint.measureText(this.backButtonTitleLabelString)) / 2.0f);
        this.backButtonTitleLabelOffsetY = this.backButtonOffsetY + (20.0f * this.zoomRate);
        this.reloadButtonWidth = 40.0f * this.zoomRate;
        this.reloadButtonHeight = 30.0f * this.zoomRate;
        this.reloadButtonOffsetX = 275.0f * this.zoomRate;
        this.reloadButtonOffsetY = 4.0f * this.zoomRate;
        this.reloadButtonColor0 = -7576502;
        this.reloadButtonStrokeWidth1 = this.zoomRate * 1.0f;
        this.reloadButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.reloadButtonStrokeWidth2 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.reloadButtonColor2 = 0;
        this.reloadButtonStrokeWidth3 = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.reloadButtonColor3 = 0;
        this.reloadButtonRadius = 6.0f * this.zoomRate;
        this.reloadButtonBitmapOffsetX = this.reloadButtonOffsetX + (11.0f * this.zoomRate);
        this.reloadButtonBitmapOffsetY = this.reloadButtonOffsetY + (6.0f * this.zoomRate);
        this.reloadButtonBitmapWidth = 18.0f * this.zoomRate;
        this.reloadButtonBitmapHeight = 18.0f * this.zoomRate;
        this.bottomViewOffsetX = this.zoomRate * (-1.0f);
        if (!this.appDelegate.isRetina4) {
            this.bottomViewOffsetY = 429.0f * this.zoomRate;
        } else {
            this.bottomViewOffsetY = 517.0f * this.zoomRate;
        }
        this.bottomViewWidth = 322.0f * this.zoomRate;
        this.bottomViewHeight = 51.0f * this.zoomRate;
        this.bottomViewColor0 = -2248316;
        this.bottomViewStrokeWidth1 = this.zoomRate * 1.0f;
        this.bottomViewColor1 = -872415232;
        clearBitmap();
    }

    public void clearBitmap() {
        if (this.reloadButtonBitmap != null) {
            if (!this.reloadButtonBitmap.isRecycled()) {
                this.reloadButtonBitmap.recycle();
            }
            this.reloadButtonBitmap = null;
        }
    }

    public void refreshBitmap() throws IOException {
        clearBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inSampleSize = 1;
            opt2.inPreferredConfig = Bitmap.Config.RGB_565;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                InputStream inputStream = asm.open("png/Button/kousinbotan.png");
                this.reloadButtonBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.bonusUnitView.getVisibility() != 0) {
            return false;
        }
        Log.d("BackButton", "onTouchEvent");
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            if (this.backButtonStatus == 0 && event.getX() > this.backButtonOffsetX && event.getX() < this.backButtonOffsetX + this.backButtonWidth && event.getY() > this.backButtonOffsetY && event.getY() < this.backButtonOffsetY + this.backButtonHeight) {
                this.touchButtonIndex = (short) 0;
                this.buttonClickCnt = (short) 3;
                Log.d("backButton", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("backButton", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ck_bonus.BonusFrontViewUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BonusFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
            if (this.reloadButtonStatus == 0 && event.getX() > this.reloadButtonOffsetX && event.getX() < this.reloadButtonOffsetX + this.reloadButtonWidth && event.getY() > this.reloadButtonOffsetY && event.getY() < this.reloadButtonOffsetY + this.reloadButtonHeight) {
                this.touchButtonIndex = (short) 1;
                this.buttonClickCnt = (short) 3;
                Log.d("reloadButton", "X=" + event.getX() + ", Y=  " + event.getY());
                Log.d("reloadButton", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ck_bonus.BonusFrontViewUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BonusFrontViewUnit.this.doClick();
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }

    public void doClick() {
        if (this.bonusUnitView.getVisibility() == 0) {
            if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 1) {
                if (this.touchButtonIndex == 0) {
                    this.appDelegate.doSoundPoolPlay(2);
                    this.bonusUnitView.doBonusLayoutHidden();
                } else if (this.touchButtonIndex == 1) {
                    this.bonusUnitView.requestStart();
                    this.appDelegate.doSoundPoolPlay(1);
                }
            }
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
        }
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(this.topViewColor0);
        canvas.drawRect(this.topViewOffsetX, this.topViewOffsetY, this.topViewWidth, this.topViewHeight, bitmapPaint);
        bitmapPaint.setColor(this.topViewColor1);
        canvas.drawRect(this.topViewOffsetX, this.topViewHeight, this.topViewWidth, this.topViewStrokeWidth1 + this.topViewHeight, bitmapPaint);
        MyDraw.drawStrokeText(canvas, this.titleLabelOffsetX, this.titleLabelOffsetY, this.titleLabelTypeface, this.titleLabelString, this.titleLabelFontSize, this.titleLabelColor0, this.titleLabelStroke1Width, this.titleLabelColor1, this.titleLabelStroke2Width, this.titleLabelColor2);
        if (this.backButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, this.backButtonColor0, this.backButtonStrokeWidth1, this.backButtonColor1, this.backButtonStrokeWidth2, this.backButtonColor2, this.backButtonStrokeWidth3, this.backButtonColor3, this.backButtonRadius);
            MyDraw.drawStrokeText(canvas, this.backButtonTitleLabelOffsetX, this.backButtonTitleLabelOffsetY, this.backButtonTitleLabelTypeface, this.backButtonTitleLabelString, this.backButtonTitleLabelFontSize, this.backButtonTitleLabelColor0, this.backButtonTitleLabelStroke1Width, this.backButtonTitleLabelColor1, this.backButtonTitleLabelStroke2Width, this.backButtonTitleLabelColor2);
            if (this.touchButtonIndex == 0) {
                MyDraw.drawStrokeRect(canvas, this.backButtonOffsetX, this.backButtonOffsetY, this.backButtonWidth, this.backButtonHeight, 1426063360, this.backButtonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.backButtonRadius);
            }
        }
        if (this.reloadButtonStatus == 0) {
            MyDraw.drawStrokeRect(canvas, this.reloadButtonOffsetX, this.reloadButtonOffsetY, this.reloadButtonWidth, this.reloadButtonHeight, this.reloadButtonColor0, this.reloadButtonStrokeWidth1, this.reloadButtonColor1, this.reloadButtonStrokeWidth2, this.reloadButtonColor2, this.reloadButtonStrokeWidth3, this.reloadButtonColor3, this.reloadButtonRadius);
            if (this.reloadButtonBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.reloadButtonBitmap, new Rect(0, 0, this.reloadButtonBitmap.getWidth(), this.reloadButtonBitmap.getHeight()), new Rect((int) this.reloadButtonBitmapOffsetX, (int) this.reloadButtonBitmapOffsetY, (int) (this.reloadButtonBitmapOffsetX + this.reloadButtonBitmapWidth), (int) (this.reloadButtonBitmapOffsetY + this.reloadButtonBitmapHeight)), bitmapPaint);
            }
            if (this.touchButtonIndex == 1) {
                MyDraw.drawStrokeRect(canvas, this.reloadButtonOffsetX, this.reloadButtonOffsetY, this.reloadButtonWidth, this.reloadButtonHeight, 1426063360, this.reloadButtonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.reloadButtonRadius);
            }
        }
        MyDraw.drawStrokeRect(canvas, this.bottomViewOffsetX, this.bottomViewOffsetY, this.bottomViewWidth, this.bottomViewHeight, this.bottomViewColor0, (int) this.bottomViewStrokeWidth1, this.bottomViewColor1, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED);
    }

    public void onDestroy() {
        clearBitmap();
        this.bonusUnitView = null;
        this.appDelegate = null;
    }
}
