package com.idtinc.maingame.sublayout0;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import java.util.ArrayList;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class EggSelectUnit {
    private AppDelegate appDelegate;
    private float backGroundORectRadius;
    private float backGroundORectffsetX;
    private float backGroundRectHeight;
    private float backGroundRectOffsetY;
    private float backGroundRectWidth;
    private float buttonHeight;
    private float buttonOffsetX;
    private float buttonOffsetY;
    private float buttonSpaceY;
    private float buttonWidth;
    private float finalHeight;
    public float finalWidth;
    private MainGameUnit mainGameUnit;
    private MyDraw myDraw;
    private float touchRectHeight;
    private float touchRectOffsetX;
    private float touchRectOffsetY;
    private float touchRectWidth;
    private float zoomRate;
    public final float BUTTONSVIEW_WIDTH = 64.0f;
    public final float BUTTONSVIEW_HEIGHT = 100.0f;
    private short[] buttonsStatusArray = null;
    private float[] buttonsOffsetYArray = null;

    public EggSelectUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameUnit _mainGameUnit, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchRectOffsetX = 3.0f;
        this.touchRectOffsetY = 145.0f;
        this.touchRectWidth = 38.0f;
        this.touchRectHeight = 76.0f;
        this.backGroundORectffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backGroundRectOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.backGroundRectWidth = BitmapDescriptorFactory.HUE_RED;
        this.backGroundRectHeight = BitmapDescriptorFactory.HUE_RED;
        this.backGroundORectRadius = BitmapDescriptorFactory.HUE_RED;
        this.buttonOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = BitmapDescriptorFactory.HUE_RED;
        this.buttonHeight = BitmapDescriptorFactory.HUE_RED;
        this.buttonSpaceY = this.touchRectWidth;
        this.appDelegate = _appDelegate;
        this.mainGameUnit = _mainGameUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        if (!this.appDelegate.isRetina4) {
            this.touchRectOffsetY = 101.0f * this.zoomRate;
        } else {
            this.touchRectOffsetY = 145.0f * this.zoomRate;
        }
        this.touchRectOffsetX = this.zoomRate * 3.0f;
        this.touchRectWidth = this.zoomRate * 38.0f;
        this.touchRectHeight = this.zoomRate * 76.0f;
        this.backGroundORectffsetX = this.touchRectOffsetX + (this.zoomRate * 2.0f);
        this.backGroundRectOffsetY = this.touchRectOffsetY;
        this.backGroundRectWidth = this.touchRectWidth - (4.0f * this.zoomRate);
        this.backGroundRectHeight = BitmapDescriptorFactory.HUE_RED;
        this.backGroundORectRadius = this.backGroundRectWidth / 2.0f;
        this.buttonOffsetX = this.touchRectOffsetX - (5.0f * this.zoomRate);
        this.buttonOffsetY = (-13.0f) * this.zoomRate;
        this.buttonWidth = this.touchRectWidth + (10.0f * this.zoomRate);
        this.buttonHeight = this.buttonWidth;
        this.buttonSpaceY = this.touchRectWidth;
        this.myDraw = new MyDraw();
    }

    public void refresh() {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        ToolUnitDictionary toolUnitDictionary;
        if (this.appDelegate != null && this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 4 && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(3)) != null && toolDictionarysArrayList.size() > 0) {
            if (this.buttonsStatusArray == null) {
                this.buttonsStatusArray = new short[toolDictionarysArrayList.size()];
                for (int i = 0; i < this.buttonsStatusArray.length; i++) {
                    this.buttonsStatusArray[i] = -1;
                }
            }
            if (this.buttonsOffsetYArray == null) {
                this.buttonsOffsetYArray = new float[toolDictionarysArrayList.size()];
                for (int i2 = 0; i2 < this.buttonsOffsetYArray.length; i2++) {
                    this.buttonsOffsetYArray[i2] = -999.0f;
                }
            }
            if (this.buttonsStatusArray == null) {
                for (int i3 = 0; i3 < this.buttonsStatusArray.length; i3++) {
                    this.buttonsStatusArray[i3] = -1;
                }
            }
            if (this.buttonsOffsetYArray == null) {
                for (int i4 = 0; i4 < this.buttonsOffsetYArray.length; i4++) {
                    this.buttonsOffsetYArray[i4] = -999.0f;
                }
            }
            short egg_id_select_index = this.appDelegate.getShort_egg_id_select_index();
            short totalButtonsCnt = 0;
            for (int i5 = 0; i5 < toolDictionarysArrayList.size(); i5++) {
                if (i5 < this.buttonsStatusArray.length && i5 < this.buttonsOffsetYArray.length && (toolUnitDictionary = toolDictionarysArrayList.get(i5)) != null) {
                    short count = toolUnitDictionary.getCount();
                    if (count == 1) {
                        if (i5 == egg_id_select_index) {
                            this.buttonsStatusArray[i5] = 1;
                            this.buttonsOffsetYArray[i5] = this.touchRectOffsetY + this.buttonOffsetY + (this.buttonSpaceY * i5) + (4.0f * this.zoomRate);
                        } else {
                            this.buttonsStatusArray[i5] = 0;
                            this.buttonsOffsetYArray[i5] = this.touchRectOffsetY + this.buttonOffsetY + (this.buttonSpaceY * i5);
                        }
                        totalButtonsCnt = (short) (totalButtonsCnt + 1);
                    } else {
                        this.buttonsStatusArray[i5] = -1;
                        this.buttonsOffsetYArray[i5] = -999.0f;
                    }
                }
            }
            this.backGroundRectHeight = BitmapDescriptorFactory.HUE_RED;
            if (totalButtonsCnt > 1) {
                this.backGroundRectHeight = (this.touchRectWidth * totalButtonsCnt) - (1.0f * this.zoomRate);
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (event.getAction() != 0 || event.getX() <= this.touchRectOffsetX || event.getX() >= this.touchRectOffsetX + this.touchRectWidth || event.getY() <= this.touchRectOffsetY || event.getY() >= this.touchRectOffsetY + this.touchRectHeight) {
            return false;
        }
        short selectIndex = (short) ((event.getY() - this.touchRectOffsetY) / this.buttonSpaceY);
        if (this.appDelegate == null || this.appDelegate.getTool3CountWithIndex(selectIndex) <= 0 || selectIndex == this.appDelegate.getShort_egg_id_select_index() || this.appDelegate.timeSaveDictionary == null) {
            return true;
        }
        this.appDelegate.timeSaveDictionary.setEggIDSelectIndex(selectIndex);
        refresh();
        this.appDelegate.doSoundPoolPlay(1);
        return true;
    }

    public void gameDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        if (this.appDelegate != null) {
            MyDraw.drawStrokeRect(canvas, this.backGroundORectffsetX, this.backGroundRectOffsetY, this.backGroundRectWidth, this.backGroundRectHeight, 1711276032, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.backGroundORectRadius);
            if (this.buttonsStatusArray != null && this.buttonsOffsetYArray != null) {
                for (int i = 0; i < this.buttonsStatusArray.length; i++) {
                    Bitmap eggBitmap = null;
                    if (this.buttonsStatusArray[i] == 0) {
                        if (i == 0) {
                            if (this.appDelegate.egg0ImageArrayList != null) {
                                eggBitmap = this.appDelegate.egg0ImageArrayList.get(0);
                            }
                        } else if (i == 1 && this.appDelegate.egg1ImageArrayList != null) {
                            eggBitmap = this.appDelegate.egg1ImageArrayList.get(0);
                        }
                    } else if (this.buttonsStatusArray[i] == 1) {
                        if (i == 0) {
                            if (this.appDelegate.egg0ImageArrayList != null) {
                                eggBitmap = this.appDelegate.egg0ImageArrayList.get(3);
                            }
                        } else if (i == 1 && this.appDelegate.egg1ImageArrayList != null) {
                            eggBitmap = this.appDelegate.egg1ImageArrayList.get(3);
                        }
                    }
                    if (eggBitmap != null) {
                        canvas.drawBitmap(eggBitmap, new Rect(0, 0, eggBitmap.getWidth(), eggBitmap.getHeight()), new Rect((int) this.buttonOffsetX, (int) this.buttonsOffsetYArray[i], (int) (this.buttonOffsetX + this.buttonWidth), (int) (this.buttonsOffsetYArray[i] + this.buttonHeight)), bitmapPaint);
                    }
                }
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.mainGameUnit = null;
        this.appDelegate = null;
    }
}
