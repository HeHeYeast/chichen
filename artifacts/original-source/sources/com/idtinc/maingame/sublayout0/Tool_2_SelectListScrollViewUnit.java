package com.idtinc.maingame.sublayout0;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import java.lang.reflect.Array;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Tool_2_SelectListScrollViewUnit {
    private AppDelegate appDelegate;
    private short buttonClickCnt;
    private short[][] buttonsStatusArray;
    private float finalHeight;
    private float finalWidth;
    private MyDraw myDraw;
    private int noIngredientLabelColor0;
    private int noIngredientLabelColor1;
    private int noIngredientLabelColor2;
    private float noIngredientLabelFontSize;
    private float noIngredientLabelOffsetX;
    private float noIngredientLabelOffsetY;
    private String noIngredientLabelString;
    private float noIngredientLabelStroke1Width;
    private float noIngredientLabelStroke2Width;
    Typeface noIngredientLabelTypeface;
    private float offsetX;
    private float offsetY;
    private float originHeight;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    private Tool_2_SelectListUnit tool_2_SelectListUnit;
    private short touchButtonIndex;
    public float useLabelFontSize;
    public float useLabelOffsetX;
    public float useLabelOffsetY;
    public String useLabelString;
    public float useLabelStroke1Width;
    public float useLabelStroke2Width;
    public Typeface useLabelTypeface;
    private float zoomRate;

    public Tool_2_SelectListScrollViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, Tool_2_SelectListUnit _tool_2_SelectListUnit, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.noIngredientLabelString = "";
        this.noIngredientLabelFontSize = 14.0f;
        this.noIngredientLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
        this.noIngredientLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.noIngredientLabelColor1 = 0;
        this.noIngredientLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.noIngredientLabelColor2 = 0;
        this.noIngredientLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.noIngredientLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.useLabelString = "";
        this.useLabelFontSize = 14.0f;
        this.useLabelStroke1Width = 2.0f;
        this.useLabelStroke2Width = 3.0f;
        this.useLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.useLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.buttonsStatusArray = null;
        this.appDelegate = _appDelegate;
        this.tool_2_SelectListUnit = _tool_2_SelectListUnit;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.originHeight = _finalheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        Paint newPaint = new Paint(257);
        this.noIngredientLabelString = this.appDelegate.getResources().getString(R.string.NoIngredients);
        this.noIngredientLabelTypeface = Typeface.DEFAULT_BOLD;
        this.noIngredientLabelFontSize = this.zoomRate * 14.0f;
        this.noIngredientLabelColor0 = 0;
        this.noIngredientLabelStroke1Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.noIngredientLabelColor1 = 0;
        this.noIngredientLabelStroke2Width = this.zoomRate * BitmapDescriptorFactory.HUE_RED;
        this.noIngredientLabelColor2 = 0;
        newPaint.setTypeface(this.noIngredientLabelTypeface);
        newPaint.setTextSize(this.noIngredientLabelFontSize);
        this.noIngredientLabelOffsetX = this.offsetX + ((this.finalWidth - newPaint.measureText(this.noIngredientLabelString)) / 2.0f);
        this.noIngredientLabelOffsetY = this.offsetY + (30.0f * this.zoomRate);
        this.useLabelString = this.appDelegate.getResources().getString(R.string.Use);
        this.useLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.useLabelFontSize = this.zoomRate * 14.0f;
        this.useLabelStroke1Width = this.zoomRate * 2.0f;
        this.useLabelStroke2Width = 3.0f * this.zoomRate;
        newPaint.setTypeface(this.useLabelTypeface);
        newPaint.setTextSize(this.useLabelFontSize);
        this.useLabelOffsetX = (this.tool_2_SelectListUnit.TOOL_BUTTON_WIDTH - newPaint.measureText(this.useLabelString)) / 2.0f;
        this.useLabelOffsetY = 52.0f * this.zoomRate;
        this.buttonsStatusArray = null;
        this.myDraw = new MyDraw();
    }

    public short reload() {
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        short rowCnt = 0;
        int toolDictionarysArrayCountWithTypeId = this.appDelegate.getToolDictionarysArrayCountWithTypeId((short) 2);
        if (this.buttonsStatusArray == null && toolDictionarysArrayCountWithTypeId > 0) {
            this.buttonsStatusArray = (short[][]) Array.newInstance((Class<?>) Short.TYPE, toolDictionarysArrayCountWithTypeId, 2);
            for (int i = 0; i < this.buttonsStatusArray.length; i++) {
                this.buttonsStatusArray[i][0] = -1;
                this.buttonsStatusArray[i][1] = -1;
            }
        }
        short nowButtonIndex = 0;
        if (this.buttonsStatusArray != null) {
            for (short i2 = 0; i2 < toolDictionarysArrayCountWithTypeId && i2 < this.buttonsStatusArray.length; i2 = (short) (i2 + 1)) {
                ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 2, i2);
                if (toolUnitDictionary != null) {
                    short countShort = toolUnitDictionary.getCount();
                    if (countShort > 0) {
                        this.buttonsStatusArray[nowButtonIndex][0] = i2;
                        this.buttonsStatusArray[nowButtonIndex][1] = 0;
                        short selectShort = toolUnitDictionary.getSelect();
                        if (selectShort > 0) {
                            this.buttonsStatusArray[nowButtonIndex][1] = 1;
                        }
                        nowButtonIndex = (short) (nowButtonIndex + 1);
                        if (nowButtonIndex >= this.buttonsStatusArray.length) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (nowButtonIndex < this.buttonsStatusArray.length) {
                for (int i3 = nowButtonIndex; i3 < this.buttonsStatusArray.length; i3++) {
                    this.buttonsStatusArray[i3][0] = -1;
                    this.buttonsStatusArray[i3][1] = -1;
                }
            }
            this.noIngredientLabelColor0 = 0;
            if (nowButtonIndex <= 0) {
                this.noIngredientLabelColor0 = FluctConstants.FRAME_ALPHA_COLOR;
            }
            this.tool_2_SelectListUnit.getClass();
            rowCnt = (short) ((nowButtonIndex - 1) / 5);
            if (rowCnt <= 0) {
                rowCnt = 0;
            }
            this.tool_2_SelectListUnit.SCROLLLAYOUT_HEIGHT = this.tool_2_SelectListUnit.TOOL_BUTTON_SPACE_Y * (rowCnt + 1);
        }
        return rowCnt;
    }

    public short getSelectedCnt() {
        short selectedCnt = 0;
        if (this.buttonsStatusArray != null) {
            for (int i = 0; i < this.buttonsStatusArray.length && this.buttonsStatusArray[i][0] >= 0; i++) {
                if (this.buttonsStatusArray[i][1] > 0) {
                    selectedCnt = (short) (selectedCnt + 1);
                }
            }
        }
        return selectedCnt;
    }

    public void unSelectedAllButtons() {
        if (this.buttonsStatusArray != null) {
            for (int i = 0; i < this.buttonsStatusArray.length && this.buttonsStatusArray[i][0] >= 0; i++) {
                this.buttonsStatusArray[i][1] = 0;
            }
        }
    }

    public void saveSelected() {
        ToolUnitDictionary toolUnitDictionary;
        short toolDictionarysArrayListCount = this.appDelegate.getToolDictionarysArrayCountWithTypeId((short) 2);
        for (short i = 0; i < toolDictionarysArrayListCount; i = (short) (i + 1)) {
            ToolUnitDictionary toolUnitDictionary2 = this.appDelegate.getToolDictionaryWithId((short) 2, i);
            if (toolUnitDictionary2 != null) {
                toolUnitDictionary2.setSelect((short) 0);
            }
        }
        if (this.buttonsStatusArray != null) {
            for (int i2 = 0; i2 < this.buttonsStatusArray.length && this.buttonsStatusArray[i2][0] >= 0; i2++) {
                if (this.buttonsStatusArray[i2][1] > 0 && (toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 2, this.buttonsStatusArray[i2][0])) != null) {
                    toolUnitDictionary.setSelect((short) 1);
                }
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (event.getAction() == 0) {
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
            float touchEventX = event.getX() - this.offsetX;
            float touchEventY = event.getY() - this.offsetY;
            if (touchEventY > BitmapDescriptorFactory.HUE_RED && touchEventY < this.finalHeight && touchEventX > BitmapDescriptorFactory.HUE_RED && touchEventX < this.finalWidth && this.buttonsStatusArray != null) {
                short buttonIndexX = (short) (touchEventX / this.tool_2_SelectListUnit.TOOL_BUTTON_WIDTH);
                if (buttonIndexX < 0) {
                    buttonIndexX = 0;
                } else if (buttonIndexX > 4) {
                    buttonIndexX = 4;
                }
                short buttonIndexY = (short) (touchEventY / this.tool_2_SelectListUnit.TOOL_BUTTON_HEIGHT);
                if (buttonIndexY < 0) {
                    buttonIndexY = 0;
                }
                this.tool_2_SelectListUnit.getClass();
                short newTouchButtonIndex = (short) ((buttonIndexY * 5) + buttonIndexX);
                if (newTouchButtonIndex >= 0 && newTouchButtonIndex < this.buttonsStatusArray.length && this.buttonsStatusArray[newTouchButtonIndex][0] >= 0 && this.buttonsStatusArray[newTouchButtonIndex][1] >= 0) {
                    this.touchButtonIndex = newTouchButtonIndex;
                    this.buttonClickCnt = (short) 2;
                    doClick();
                    return true;
                }
            }
        }
        return false;
    }

    public void doClick() {
        if (this.buttonsStatusArray != null) {
            if (this.touchButtonIndex >= 0 && this.touchButtonIndex < this.buttonsStatusArray.length && this.buttonsStatusArray[this.touchButtonIndex][0] >= 0) {
                if (this.buttonsStatusArray[this.touchButtonIndex][1] == 0) {
                    short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
                    short countMax = tool_0_0_level;
                    if (countMax > 2) {
                        countMax = 2;
                    }
                    if (countMax == 0) {
                        unSelectedAllButtons();
                        this.buttonsStatusArray[this.touchButtonIndex][1] = 1;
                        this.appDelegate.doSoundPoolPlay(1);
                    } else if (countMax > 0) {
                        if (getSelectedCnt() >= countMax + 1) {
                            if (getSelectedCnt() != countMax + 1) {
                                unSelectedAllButtons();
                            }
                            this.appDelegate.doSoundPoolPlay(2);
                        } else {
                            this.buttonsStatusArray[this.touchButtonIndex][1] = 1;
                            this.appDelegate.doSoundPoolPlay(1);
                        }
                    }
                } else if (this.buttonsStatusArray[this.touchButtonIndex][1] == 1) {
                    this.buttonsStatusArray[this.touchButtonIndex][1] = 0;
                    this.appDelegate.doSoundPoolPlay(2);
                }
                this.tool_2_SelectListUnit.checkLabel1String();
            }
            this.touchButtonIndex = (short) -1;
            this.buttonClickCnt = (short) -1;
        }
    }

    public void gameDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint bitmapPaint = new Paint();
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        bitmapPaint.setColor(0);
        if (this.appDelegate != null) {
            MyDraw.drawStrokeText(canvas, this.noIngredientLabelOffsetX, this.noIngredientLabelOffsetY, this.noIngredientLabelTypeface, this.noIngredientLabelString, this.noIngredientLabelFontSize, this.noIngredientLabelColor0, this.noIngredientLabelStroke1Width, this.noIngredientLabelColor1, this.noIngredientLabelStroke2Width, this.noIngredientLabelColor2);
            if (this.buttonsStatusArray != null) {
                for (int i = 0; i < this.buttonsStatusArray.length && this.buttonsStatusArray[i][0] >= 0; i++) {
                    if (this.buttonsStatusArray[i][1] >= 0) {
                        Bitmap toolBitmap = null;
                        if (this.buttonsStatusArray[i][1] == 0) {
                            if (this.appDelegate.tool2Level0Image0ArrayList != null && this.buttonsStatusArray[i][0] < this.appDelegate.tool2Level0Image0ArrayList.size()) {
                                toolBitmap = this.appDelegate.tool2Level0Image0ArrayList.get(this.buttonsStatusArray[i][0]);
                                bitmapPaint.setAlpha(90);
                                if (this.buttonsStatusArray[i][0] == 15) {
                                    bitmapPaint.setAlpha(110);
                                }
                            }
                        } else if (this.buttonsStatusArray[i][1] == 1 && this.appDelegate.tool2Level0Image1ArrayList != null && this.buttonsStatusArray[i][0] < this.appDelegate.tool2Level0Image1ArrayList.size()) {
                            toolBitmap = this.appDelegate.tool2Level0Image1ArrayList.get(this.buttonsStatusArray[i][0]);
                            bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        }
                        if (toolBitmap != null) {
                            float f = this.offsetX;
                            float f2 = this.tool_2_SelectListUnit.TOOL_BUTTON_WIDTH;
                            this.tool_2_SelectListUnit.getClass();
                            int imgOffsetX = (int) (f + (f2 * (i % 5)));
                            float f3 = this.offsetY;
                            float f4 = this.tool_2_SelectListUnit.TOOL_BUTTON_SPACE_Y;
                            this.tool_2_SelectListUnit.getClass();
                            int imgOffsetY = (int) (f3 + (f4 * (i / 5)));
                            canvas.drawBitmap(toolBitmap, new Rect(0, 0, toolBitmap.getWidth(), toolBitmap.getHeight()), new Rect(imgOffsetX, imgOffsetY, (int) (imgOffsetX + this.tool_2_SelectListUnit.TOOL_BUTTON_WIDTH), (int) (imgOffsetY + this.tool_2_SelectListUnit.TOOL_BUTTON_HEIGHT)), bitmapPaint);
                            if (this.buttonsStatusArray[i][1] == 1) {
                                MyDraw.drawStrokeText(canvas, imgOffsetX + this.useLabelOffsetX, imgOffsetY + this.useLabelOffsetY, this.useLabelTypeface, this.useLabelString, this.useLabelFontSize, -6106, this.useLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.useLabelStroke2Width, -1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.buttonsStatusArray = null;
        this.tool_2_SelectListUnit = null;
        this.appDelegate = null;
    }
}
