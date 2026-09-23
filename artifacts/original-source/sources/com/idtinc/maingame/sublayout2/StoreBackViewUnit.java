package com.idtinc.maingame.sublayout2;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StoreBackViewUnit {
    public float GETBONUS_BUTTON_ADD_MAX_X;
    public float GETBONUS_BUTTON_ADD_MAX_Y;
    public float GETBONUS_BUTTON_HEIGHT;
    public float GETBONUS_BUTTON_OFFSET_X;
    public float GETBONUS_BUTTON_OFFSET_Y;
    public float GETBONUS_BUTTON_SPEED_X;
    public float GETBONUS_BUTTON_SPEED_Y;
    public float GETBONUS_BUTTON_WIDTH;
    private AppDelegate appDelegate;
    private short buttonClickCnt;
    private float finalHeight;
    private float finalWidth;
    public float getBonusAddX;
    public float getBonusAddY;
    public float getBonusButtonHeight;
    public float getBonusButtonOffsetX;
    public float getBonusButtonOffsetY;
    public float getBonusButtonSpeedX;
    public float getBonusButtonSpeedY;
    private short getBonusButtonStatus;
    public float getBonusButtonWidth;
    public float giftButtonBackRectHeight;
    public float giftButtonBackRectOffsetX;
    public float giftButtonBackRectOffsetY;
    public float giftButtonBackRectRadius;
    public float giftButtonBackRectWidth;
    public float giftButtonHeight;
    public float giftButtonOffsetX;
    public float giftButtonOffsetY;
    private short giftButtonStatus;
    public float giftButtonWidth;
    public EditText giftInputEditText;
    private MyDraw myDraw;
    private StoreUnit storeUnit;
    private short touchButtonIndex;
    private float zoomRate;
    private final String JUST_GOOD_GIFT_0_REQUEST_URL = "http://www.idtfun.com/ck/gift/gift_tool_2_35.html";
    private Bitmap getBonusButtonBitmap0 = null;
    private Bitmap getBonusButtonBitmap1 = null;
    private Bitmap giftButtonBitmap = null;
    private Bitmap giftButtonOnBitmap = null;
    private Bitmap giftButtonOffBitmap = null;
    private Bitmap backGroundBitmap = null;

    public StoreBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, StoreUnit _storeUnit, AppDelegate _appDelegate) throws IOException {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.giftButtonStatus = (short) -1;
        this.getBonusButtonStatus = (short) -1;
        this.giftButtonBackRectWidth = 46.0f;
        this.giftButtonBackRectHeight = 46.0f;
        this.giftButtonBackRectOffsetX = 8.0f;
        this.giftButtonBackRectOffsetY = 36.0f;
        this.giftButtonBackRectRadius = 36.0f;
        this.giftButtonWidth = 46.0f;
        this.giftButtonHeight = 46.0f;
        this.giftButtonOffsetX = 8.0f;
        this.giftButtonOffsetY = 36.0f;
        this.GETBONUS_BUTTON_OFFSET_X = 267.0f;
        this.GETBONUS_BUTTON_OFFSET_Y = 37.0f;
        this.GETBONUS_BUTTON_WIDTH = 44.0f;
        this.GETBONUS_BUTTON_HEIGHT = 44.0f;
        this.GETBONUS_BUTTON_SPEED_X = 2.0f;
        this.GETBONUS_BUTTON_SPEED_Y = 2.0f;
        this.GETBONUS_BUTTON_ADD_MAX_X = 4.0f;
        this.GETBONUS_BUTTON_ADD_MAX_Y = 4.0f;
        this.getBonusAddX = BitmapDescriptorFactory.HUE_RED;
        this.getBonusAddY = BitmapDescriptorFactory.HUE_RED;
        this.getBonusButtonSpeedX = this.GETBONUS_BUTTON_SPEED_X;
        this.getBonusButtonSpeedY = this.GETBONUS_BUTTON_SPEED_Y;
        this.getBonusButtonWidth = 46.0f;
        this.getBonusButtonHeight = 46.0f;
        this.getBonusButtonOffsetX = 266.0f;
        this.getBonusButtonOffsetY = 36.0f;
        this.appDelegate = null;
        this.storeUnit = null;
        this.appDelegate = _appDelegate;
        this.storeUnit = _storeUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.giftButtonStatus = (short) -1;
        this.getBonusButtonStatus = (short) 0;
        this.giftInputEditText = new EditText(this.appDelegate);
        this.giftInputEditText.setTextSize(12.0f);
        this.giftInputEditText.setVisibility(8);
        this.giftInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        this.giftInputEditText.setSingleLine(true);
        this.giftInputEditText.setImeOptions(6);
        this.giftInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.idtinc.maingame.sublayout2.StoreBackViewUnit.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView _textView, int actionId, KeyEvent event) {
                if (actionId == 0 || actionId == 6) {
                    StoreBackViewUnit.this.hiddenSoftInput(_textView);
                    if (StoreBackViewUnit.this.giftInputEditText == null) {
                        return true;
                    }
                    if (StoreBackViewUnit.this.giftInputEditText.getText().toString().length() > 0) {
                        StoreBackViewUnit.this.changGiftButtonStatus((short) 10);
                        if (StoreBackViewUnit.this.storeUnit.hidden) {
                            return true;
                        }
                        StoreBackViewUnit.this.appDelegate.doSoundPoolPlay(1);
                        return true;
                    }
                    StoreBackViewUnit.this.changGiftButtonStatus((short) -1);
                    if (StoreBackViewUnit.this.storeUnit.hidden) {
                        return true;
                    }
                    StoreBackViewUnit.this.appDelegate.doSoundPoolPlay(2);
                    return true;
                }
                return false;
            }
        });
        this.giftButtonBackRectWidth = 236.0f * this.zoomRate;
        this.giftButtonBackRectHeight = 36.0f * this.zoomRate;
        this.giftButtonBackRectOffsetX = 13.0f * this.zoomRate;
        this.giftButtonBackRectOffsetY = 41.0f * this.zoomRate;
        this.giftButtonBackRectRadius = this.giftButtonBackRectHeight / 2.0f;
        this.giftButtonWidth = this.zoomRate * 46.0f;
        this.giftButtonHeight = this.zoomRate * 46.0f;
        this.giftButtonOffsetX = 8.0f * this.zoomRate;
        this.giftButtonOffsetY = 36.0f * this.zoomRate;
        this.GETBONUS_BUTTON_OFFSET_X = 267.0f * this.zoomRate;
        this.GETBONUS_BUTTON_OFFSET_Y = 37.0f * this.zoomRate;
        this.GETBONUS_BUTTON_WIDTH = 44.0f * this.zoomRate;
        this.GETBONUS_BUTTON_HEIGHT = 44.0f * this.zoomRate;
        this.GETBONUS_BUTTON_SPEED_X = this.zoomRate * 2.0f;
        this.GETBONUS_BUTTON_SPEED_Y = this.zoomRate * 2.0f;
        this.GETBONUS_BUTTON_ADD_MAX_X = 4.0f * this.zoomRate;
        this.GETBONUS_BUTTON_ADD_MAX_Y = 4.0f * this.zoomRate;
        this.getBonusAddX = BitmapDescriptorFactory.HUE_RED;
        this.getBonusAddY = BitmapDescriptorFactory.HUE_RED;
        this.getBonusButtonSpeedX = this.GETBONUS_BUTTON_SPEED_X;
        this.getBonusButtonSpeedY = this.GETBONUS_BUTTON_SPEED_Y;
        this.getBonusButtonOffsetX = this.GETBONUS_BUTTON_OFFSET_X - this.getBonusAddX;
        this.getBonusButtonOffsetY = this.GETBONUS_BUTTON_OFFSET_Y - this.getBonusAddY;
        this.getBonusButtonWidth = this.GETBONUS_BUTTON_WIDTH + (this.getBonusAddX * 2.0f);
        this.getBonusButtonHeight = this.GETBONUS_BUTTON_HEIGHT + (this.getBonusAddY * 2.0f);
        clearBitmap();
        refreshSubBitmap();
        this.myDraw = new MyDraw();
        changGiftButtonStatus((short) -1);
    }

    public void clickedGiftButton() {
        if (this.giftButtonStatus == 0) {
            changGiftButtonStatus((short) -1);
            if (!this.storeUnit.hidden) {
                this.appDelegate.doSoundPoolPlay(2);
                return;
            }
            return;
        }
        if (this.giftButtonStatus == -1) {
            changGiftButtonStatus((short) 0);
            if (!this.storeUnit.hidden) {
                this.appDelegate.doSoundPoolPlay(4);
            }
        }
    }

    public void changGiftButtonStatus(short _newStatus) {
        hiddenSoftInputGiftInputEditText();
        this.giftInputEditText.setVisibility(8);
        this.giftButtonStatus = (short) -999;
    }

    public void checkInput(String _inputString) {
        if (checkJustGoodWithString(_inputString) && this.storeUnit != null) {
            this.storeUnit.goToGiftPage("gift_tool_2_35", "http://www.idtfun.com/ck/gift/gift_tool_2_35.html");
        }
        changGiftButtonStatus((short) -1);
    }

    public boolean checkJustGoodWithString(String _inputString) {
        if (!_inputString.equals("justgood")) {
            return false;
        }
        return true;
    }

    public void hiddenSoftInputGiftInputEditText() {
        if (this.giftInputEditText != null) {
            hiddenSoftInput(this.giftInputEditText);
        }
    }

    public void hiddenSoftInput(TextView _textView) {
        if (_textView != null) {
            InputMethodManager imm = (InputMethodManager) _textView.getContext().getSystemService("input_method");
            imm.hideSoftInputFromWindow(_textView.getWindowToken(), 0);
        }
    }

    public void clearBitmap() {
        if (this.backGroundBitmap != null) {
            if (!this.backGroundBitmap.isRecycled()) {
                this.backGroundBitmap.recycle();
            }
            this.backGroundBitmap = null;
        }
    }

    public void clearSubBitmap() {
        if (this.getBonusButtonBitmap0 != null) {
            if (!this.getBonusButtonBitmap0.isRecycled()) {
                this.getBonusButtonBitmap0.recycle();
            }
            this.getBonusButtonBitmap0 = null;
        }
        if (this.getBonusButtonBitmap1 != null) {
            if (!this.getBonusButtonBitmap1.isRecycled()) {
                this.getBonusButtonBitmap1.recycle();
            }
            this.getBonusButtonBitmap1 = null;
        }
        if (this.giftButtonBitmap != null) {
            if (!this.giftButtonBitmap.isRecycled()) {
                this.giftButtonBitmap.recycle();
            }
            this.giftButtonBitmap = null;
        }
        if (this.giftButtonOnBitmap != null) {
            if (!this.giftButtonOnBitmap.isRecycled()) {
                this.giftButtonOnBitmap.recycle();
            }
            this.giftButtonOnBitmap = null;
        }
        if (this.giftButtonOffBitmap != null) {
            if (!this.giftButtonOffBitmap.isRecycled()) {
                this.giftButtonOffBitmap.recycle();
            }
            this.giftButtonOffBitmap = null;
        }
    }

    public void refreshBitmap() throws IOException {
        clearBitmap();
        if (this.appDelegate != null) {
            AssetManager asm = this.appDelegate.getAssets();
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inJustDecodeBounds = false;
            opt2.inPurgeable = true;
            opt2.inInputShareable = true;
            try {
                InputStream inputStream = asm.open("png/MainGame/store_bg.jpg");
                BufferedInputStream buf = new BufferedInputStream(inputStream);
                BitmapFactory.decodeStream(buf, null, opt);
                int scale = this.appDelegate.getBitmapScale(opt.outWidth, this.finalWidth);
                opt2.inSampleSize = scale;
                this.backGroundBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
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
                InputStream inputStream = asm.open("png/MainGame/coin.png");
                this.getBonusButtonBitmap0 = BitmapFactory.decodeStream(inputStream, null, opt2);
                inputStream.close();
            } catch (IOException e) {
            }
            try {
                InputStream inputStream2 = asm.open("png/MainGame/coin_1.png");
                this.getBonusButtonBitmap1 = BitmapFactory.decodeStream(inputStream2, null, opt2);
                inputStream2.close();
            } catch (IOException e2) {
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("StoreBackViewUnit", "onTouchEvent");
        if (event.getAction() != 0 || this.getBonusButtonStatus != 0 || event.getY() <= this.getBonusButtonOffsetY || event.getY() >= this.getBonusButtonOffsetY + this.getBonusButtonHeight || event.getX() <= this.getBonusButtonOffsetX || event.getX() >= this.getBonusButtonOffsetX + this.getBonusButtonWidth) {
            return false;
        }
        this.touchButtonIndex = (short) 1;
        this.buttonClickCnt = (short) 3;
        Log.d("getBonusButton", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("getBonusButton", "touchButtonIndex:" + ((int) this.touchButtonIndex));
        new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreBackViewUnit.2
            @Override // java.lang.Runnable
            public void run() {
                StoreBackViewUnit.this.doClick();
            }
        }, 200L);
        return true;
    }

    public void doClick() {
        if (this.touchButtonIndex >= 0 && this.touchButtonIndex <= 1) {
            if (this.touchButtonIndex == 0) {
                clickedGiftButton();
            } else if (this.touchButtonIndex == 1) {
                changGiftButtonStatus((short) -1);
                this.storeUnit.openBonusLayout();
                this.appDelegate.doSoundPoolPlay(1);
            }
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        if (this.backGroundBitmap != null) {
            canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.finalHeight), bitmapPaint);
            if (!this.appDelegate.isRetina4) {
                canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
            } else {
                canvas.drawBitmap(this.backGroundBitmap, new Rect(0, 0, this.backGroundBitmap.getWidth(), this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
            }
        }
        if (this.getBonusButtonStatus == 0) {
            this.getBonusAddX += this.getBonusButtonSpeedX;
            this.getBonusAddY += this.getBonusButtonSpeedY;
            if (this.getBonusAddX >= this.GETBONUS_BUTTON_ADD_MAX_X) {
                this.getBonusAddX = this.GETBONUS_BUTTON_ADD_MAX_X;
                this.getBonusAddY = this.GETBONUS_BUTTON_ADD_MAX_Y;
                this.getBonusButtonSpeedX *= -1.0f;
                this.getBonusButtonSpeedY *= -1.0f;
            } else if (this.getBonusAddX <= BitmapDescriptorFactory.HUE_RED) {
                this.getBonusAddX = BitmapDescriptorFactory.HUE_RED;
                this.getBonusAddY = BitmapDescriptorFactory.HUE_RED;
                this.getBonusButtonSpeedX *= -1.0f;
                this.getBonusButtonSpeedY *= -1.0f;
            }
            this.getBonusButtonOffsetX = this.GETBONUS_BUTTON_OFFSET_X - this.getBonusAddX;
            this.getBonusButtonOffsetY = this.GETBONUS_BUTTON_OFFSET_Y - this.getBonusAddY;
            this.getBonusButtonWidth = this.GETBONUS_BUTTON_WIDTH + (this.getBonusAddX * 2.0f);
            this.getBonusButtonHeight = this.GETBONUS_BUTTON_HEIGHT + (this.getBonusAddY * 2.0f);
            if (this.getBonusButtonBitmap0 != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                canvas.drawBitmap(this.getBonusButtonBitmap0, new Rect(0, 0, this.getBonusButtonBitmap0.getWidth(), this.getBonusButtonBitmap0.getHeight()), new Rect((int) this.getBonusButtonOffsetX, (int) this.getBonusButtonOffsetY, (int) (this.getBonusButtonOffsetX + this.getBonusButtonWidth), (int) (this.getBonusButtonOffsetY + this.getBonusButtonHeight)), bitmapPaint);
            }
            if (this.touchButtonIndex == 1 && this.getBonusButtonBitmap1 != null) {
                bitmapPaint.setAlpha(128);
                canvas.drawBitmap(this.getBonusButtonBitmap1, new Rect(0, 0, this.getBonusButtonBitmap1.getWidth(), this.getBonusButtonBitmap1.getHeight()), new Rect((int) this.getBonusButtonOffsetX, (int) this.getBonusButtonOffsetY, (int) (this.getBonusButtonOffsetX + this.getBonusButtonWidth), (int) (this.getBonusButtonOffsetY + this.getBonusButtonHeight)), bitmapPaint);
            }
        }
    }

    public void onDestroy() {
        clearSubBitmap();
        clearBitmap();
        this.myDraw = null;
        if (this.giftInputEditText != null) {
            hiddenSoftInputGiftInputEditText();
            this.giftInputEditText.setVisibility(8);
            this.giftInputEditText = null;
        }
        this.storeUnit = null;
        this.appDelegate = null;
    }
}
