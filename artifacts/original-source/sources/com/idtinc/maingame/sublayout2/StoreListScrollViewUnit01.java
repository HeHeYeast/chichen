package com.idtinc.maingame.sublayout2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Debug;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.ToolDataDictionary;
import com.idtinc.ckunit.ToolLevelDictionary;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.MyDraw;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StoreListScrollViewUnit01 {
    private AppDelegate appDelegate;
    private short buyButtonClickCnt;
    public int buyButtonColor0;
    public int buyButtonColor1;
    public int buyButtonColor2;
    public int buyButtonColor3;
    public float buyButtonHeight;
    public float buyButtonOffsetX;
    public float buyButtonOffsetY;
    public float buyButtonRadius;
    public float buyButtonStrokeWidth1;
    public float buyButtonStrokeWidth2;
    public float buyButtonStrokeWidth3;
    public int buyButtonTitleLabelColor0;
    public int buyButtonTitleLabelColor1;
    public int buyButtonTitleLabelColor2;
    public float buyButtonTitleLabelFontSize;
    public float buyButtonTitleLabelOffsetX;
    public float buyButtonTitleLabelOffsetY;
    public String buyButtonTitleLabelString;
    public float buyButtonTitleLabelStroke1Width;
    public float buyButtonTitleLabelStroke2Width;
    Typeface buyButtonTitleLabelTypeface;
    public float buyButtonWidth;
    short[] buyButtonsStatusArray;
    public float cellSpaceLineHeight;
    public float cellSpaceY;
    private float clipRectHeight;
    private float clipRectWidth;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MyDraw myDraw;
    public float nameLabelFontSize;
    public float nameLabelOffsetX;
    public float nameLabelOffsetY;
    public float nameLabelStroke1Width;
    public float nameLabelStroke2Width;
    public Typeface nameLabelTypeface;
    public float offsetScrollY;
    public float offsetScrollYMax;
    private float offsetX;
    private float offsetY;
    private float originHeight;
    public float priceLabelFontSize;
    public float priceLabelOffsetX;
    public float priceLabelOffsetY;
    public float priceLabelStroke1Width;
    public float priceLabelStroke2Width;
    public float priceTitleLabelFontSize;
    public float priceTitleLabelOffsetX;
    public float priceTitleLabelOffsetY;
    public String priceTitleLabelString;
    public float priceTitleLabelStroke1Width;
    public Typeface priceTitleLabelTypeface;
    public float purchasedLabelFontSize;
    public float purchasedLabelOffsetX;
    public float purchasedLabelOffsetY;
    public String purchasedLabelString;
    public float purchasedLabelStroke1Width;
    public float purchasedLabelStroke2Width;
    public Typeface purchasedLabelTypeface;
    private short selectedBuyButtonIndex;
    public float smallImageHeight;
    public float smallImageOffsetX;
    public float smallImageOffsetY;
    public float smallImageWidth;
    private StoreUnit storeUnit;
    private float zoomRate;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    public String nameLabelString = "";

    public StoreListScrollViewUnit01(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, StoreUnit _storeUnit, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.clipRectWidth = BitmapDescriptorFactory.HUE_RED;
        this.clipRectHeight = BitmapDescriptorFactory.HUE_RED;
        this.hidden = false;
        this.selectedBuyButtonIndex = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        this.smallImageOffsetX = 5.0f;
        this.smallImageOffsetY = -1.0f;
        this.smallImageWidth = 60.0f;
        this.smallImageHeight = 60.0f;
        this.nameLabelFontSize = 16.0f;
        this.nameLabelStroke1Width = 0.2f;
        this.nameLabelStroke2Width = 3.0f;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.nameLabelOffsetX = 68.0f;
        this.nameLabelOffsetY = 6.0f;
        this.priceTitleLabelString = "";
        this.priceTitleLabelFontSize = 16.0f;
        this.priceTitleLabelStroke1Width = 0.2f;
        this.priceTitleLabelOffsetX = 68.0f;
        this.priceTitleLabelOffsetY = 33.0f;
        this.priceLabelFontSize = 16.0f;
        this.priceLabelStroke1Width = 0.2f;
        this.priceLabelStroke2Width = 3.0f;
        this.priceLabelOffsetX = 120.0f;
        this.priceLabelOffsetY = 34.0f;
        this.purchasedLabelString = "";
        this.purchasedLabelFontSize = 13.0f;
        this.purchasedLabelStroke1Width = 2.0f;
        this.purchasedLabelStroke2Width = 3.0f;
        this.purchasedLabelOffsetX = 35.0f;
        this.purchasedLabelOffsetY = 30.0f;
        this.cellSpaceY = 60.0f;
        this.cellSpaceLineHeight = 1.0f;
        this.buyButtonWidth = 65.0f;
        this.buyButtonHeight = 26.0f;
        this.buyButtonOffsetX = 183.0f;
        this.buyButtonOffsetY = 30.0f;
        this.buyButtonColor0 = -3171731;
        this.buyButtonStrokeWidth1 = 2.0f;
        this.buyButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buyButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.buyButtonColor2 = 0;
        this.buyButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.buyButtonColor3 = 0;
        this.buyButtonRadius = 10.0f;
        this.buyButtonTitleLabelString = "";
        this.buyButtonTitleLabelFontSize = 18.0f;
        this.buyButtonTitleLabelColor0 = -1;
        this.buyButtonTitleLabelStroke1Width = 2.0f;
        this.buyButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buyButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.buyButtonTitleLabelColor2 = 0;
        this.buyButtonTitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buyButtonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate = _appDelegate;
        this.storeUnit = _storeUnit;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.originHeight = _finalheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.clipRectWidth = this.offsetX + this.finalWidth;
        this.clipRectHeight = this.offsetY + this.originHeight;
        this.hidden = false;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.selectedBuyButtonIndex = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        this.smallImageOffsetX = this.offsetX + (5.0f * this.zoomRate);
        this.smallImageOffsetY = this.offsetY - (1.0f * this.zoomRate);
        this.smallImageWidth = 60.0f * this.zoomRate;
        this.smallImageHeight = 60.0f * this.zoomRate;
        Paint newPaint = new Paint(257);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            this.nameLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.nameLabelFontSize = 16.0f * this.zoomRate;
            this.priceTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.priceTitleLabelFontSize = 16.0f * this.zoomRate;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            this.nameLabelTypeface = Typeface.DEFAULT_BOLD;
            this.nameLabelFontSize = 15.0f * this.zoomRate;
            this.priceTitleLabelTypeface = Typeface.DEFAULT_BOLD;
            this.priceTitleLabelFontSize = 15.0f * this.zoomRate;
        } else {
            this.nameLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.nameLabelFontSize = 16.0f * this.zoomRate;
            this.priceTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.priceTitleLabelFontSize = 16.0f * this.zoomRate;
        }
        this.nameLabelStroke1Width = 0.2f * this.zoomRate;
        this.nameLabelStroke2Width = 3.0f * this.zoomRate;
        this.nameLabelOffsetX = this.offsetX + (68.0f * this.zoomRate);
        this.nameLabelOffsetY = this.offsetY + (6.0f * this.zoomRate) + this.nameLabelFontSize;
        this.priceTitleLabelStroke1Width = 0.2f * this.zoomRate;
        newPaint.setTypeface(this.priceTitleLabelTypeface);
        newPaint.setTextSize(this.priceTitleLabelFontSize);
        this.priceTitleLabelString = " " + this.appDelegate.getResources().getString(R.string.Price) + ":";
        this.priceTitleLabelOffsetX = this.offsetX + (68.0f * this.zoomRate);
        this.priceTitleLabelOffsetY = this.offsetY + (33.0f * this.zoomRate) + this.priceTitleLabelFontSize;
        this.priceLabelFontSize = 16.0f * this.zoomRate;
        this.priceLabelStroke1Width = 0.2f * this.zoomRate;
        this.priceLabelStroke2Width = 3.0f * this.zoomRate;
        this.priceLabelOffsetX = this.offsetX + (120.0f * this.zoomRate);
        this.priceLabelOffsetY = this.offsetY + (34.0f * this.zoomRate) + this.priceTitleLabelFontSize;
        this.purchasedLabelString = this.appDelegate.getResources().getString(R.string.Purchased);
        this.purchasedLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.purchasedLabelFontSize = 13.0f * this.zoomRate;
        newPaint.setTypeface(this.purchasedLabelTypeface);
        newPaint.setTextSize(this.purchasedLabelFontSize);
        this.purchasedLabelStroke1Width = 2.0f * this.zoomRate;
        this.purchasedLabelStroke2Width = 3.0f * this.zoomRate;
        this.purchasedLabelOffsetX = this.offsetX + ((35.0f * this.zoomRate) - (newPaint.measureText(this.purchasedLabelString) / 2.0f));
        this.purchasedLabelOffsetY = this.offsetY + (30.0f * this.zoomRate) + this.purchasedLabelFontSize;
        this.cellSpaceY = 60.0f * this.zoomRate;
        this.cellSpaceLineHeight = 1.0f * this.zoomRate;
        this.buyButtonWidth = 65.0f * this.zoomRate;
        this.buyButtonHeight = 26.0f * this.zoomRate;
        this.buyButtonOffsetX = this.offsetX + (183.0f * this.zoomRate);
        this.buyButtonOffsetY = this.offsetY + (30.0f * this.zoomRate);
        this.buyButtonColor0 = -3171731;
        this.buyButtonStrokeWidth1 = 2.0f * this.zoomRate;
        this.buyButtonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buyButtonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buyButtonColor2 = 0;
        this.buyButtonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buyButtonColor3 = 0;
        this.buyButtonRadius = 10.0f * this.zoomRate;
        this.buyButtonTitleLabelString = this.appDelegate.getResources().getString(R.string.Buy);
        this.buyButtonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.buyButtonTitleLabelFontSize = 18.0f * this.zoomRate;
        newPaint.setTypeface(this.buyButtonTitleLabelTypeface);
        newPaint.setTextSize(this.buyButtonTitleLabelFontSize);
        this.buyButtonTitleLabelColor0 = -1;
        this.buyButtonTitleLabelStroke1Width = 2.0f * this.zoomRate;
        this.buyButtonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buyButtonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buyButtonTitleLabelColor2 = 0;
        this.buyButtonTitleLabelOffsetX = this.buyButtonOffsetX + ((this.buyButtonWidth - newPaint.measureText(this.buyButtonTitleLabelString)) / 2.0f);
        this.buyButtonTitleLabelOffsetY = this.buyButtonOffsetY + ((this.buyButtonHeight + (this.buyButtonTitleLabelFontSize * 0.7f)) / 2.0f);
        this.myDraw = new MyDraw();
    }

    public void reload() {
        Log.e("StoreListScrollLayout01", "pepepepewpvpxxcpvcxa = " + Debug.getNativeHeapSize());
        Log.d("StoreListScrollLayout01", "reload");
        int toolDictionarysArrayCountWithTypeId = this.appDelegate.getToolDictionarysArrayCountWithTypeId((short) 1);
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.selectedBuyButtonIndex = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        if (this.buyButtonsStatusArray == null && toolDictionarysArrayCountWithTypeId > 0) {
            this.buyButtonsStatusArray = new short[toolDictionarysArrayCountWithTypeId];
            for (int i = 0; i < toolDictionarysArrayCountWithTypeId; i++) {
                Log.d("buyButtonsStstusArray", "appMainActivity.getToolDictionarysArrayCountWithTypeId((short)1)" + i);
                this.buyButtonsStatusArray[i] = -1;
            }
        }
        if (this.buyButtonsStatusArray != null) {
            for (int i2 = 0; i2 < toolDictionarysArrayCountWithTypeId && i2 < this.buyButtonsStatusArray.length; i2++) {
                ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, (short) i2);
                if (toolUnitDictionary != null) {
                    short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
                    short levelShort = toolUnitDictionary.getLevel();
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, (short) i2);
                    short lvBuyCpIndex = -1;
                    if (levelShort == -1) {
                        lvBuyCpIndex = 0;
                    } else if (levelShort == 0) {
                        if (tool_0_0_level > 0) {
                            lvBuyCpIndex = 1;
                        } else {
                            lvBuyCpIndex = -1;
                        }
                    } else if (levelShort == 1) {
                        if (tool_0_0_level > 1) {
                            lvBuyCpIndex = 2;
                        } else {
                            lvBuyCpIndex = -1;
                        }
                    } else if (levelShort == 1) {
                        lvBuyCpIndex = -1;
                    }
                    int buyCp = -1;
                    if (toolDataDictionary != null && lvBuyCpIndex >= 0 && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size()) {
                        ToolLevelDictionary newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex);
                        buyCp = newToolLevelDictionary.getLvBuyCp();
                    }
                    if (buyCp >= 0) {
                        this.buyButtonsStatusArray[i2] = 1;
                        if (this.appDelegate.timeSaveDictionary != null) {
                            int nowCp = (int) this.appDelegate.timeSaveDictionary.getPoint();
                            if (nowCp >= buyCp) {
                                this.buyButtonsStatusArray[i2] = 0;
                            }
                        }
                    } else {
                        this.buyButtonsStatusArray[i2] = -1;
                    }
                }
            }
        }
        this.finalHeight = this.originHeight;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        if (toolDictionarysArrayCountWithTypeId > 4) {
            this.finalHeight = this.cellSpaceY * toolDictionarysArrayCountWithTypeId;
            this.offsetScrollYMax = this.finalHeight - this.originHeight;
            Log.d("StoreListScrollLayout01", "finalHeight:" + this.finalHeight);
        }
        doScrollViewAutoOffset(this.offsetScrollY);
    }

    public void unclickAllButton() {
        this.selectedBuyButtonIndex = (short) -1;
        this.buyButtonClickCnt = (short) -1;
    }

    public void setPreScrollPoint(float _preScrollX, float _preScrollY) {
        this.preScrollX = _preScrollX;
        this.preScrollY = _preScrollY;
    }

    public float getPreScrollX() {
        return this.preScrollX;
    }

    public float getPreScrollY() {
        return this.preScrollY;
    }

    public boolean gameOnTouch(MotionEvent event) throws Resources.NotFoundException {
        if (event.getX() < this.offsetX || event.getX() > this.clipRectWidth || event.getY() < this.offsetY || event.getY() > this.clipRectHeight) {
            unclickAllButton();
            return false;
        }
        Log.d("StoreListScrollLayout01", "onTouchEvent");
        if (event.getAction() == 1) {
            doClick();
            Log.d("StoreListScrollLayout01", "ACTION_UP   X=" + event.getX() + ", Y=  " + event.getY());
        } else if (event.getAction() == 0) {
            this.selectedBuyButtonIndex = (short) -1;
            this.buyButtonClickCnt = (short) -1;
            this.preScrollY = event.getY();
            if (event.getX() > this.buyButtonOffsetX && event.getX() < this.buyButtonOffsetX + this.buyButtonWidth && event.getY() > this.offsetY && event.getY() < this.clipRectHeight) {
                short buttonIndex = (short) (((this.offsetScrollY + event.getY()) - this.offsetY) / this.cellSpaceY);
                float checkOffsetY = ((this.offsetScrollY + event.getY()) - this.offsetY) - (this.cellSpaceY * buttonIndex);
                if (checkOffsetY > this.buyButtonOffsetY - this.offsetY && checkOffsetY < (this.buyButtonOffsetY + this.buyButtonHeight) - this.offsetY && buttonIndex >= 0 && buttonIndex < this.buyButtonsStatusArray.length && this.buyButtonsStatusArray[buttonIndex] == 0) {
                    this.selectedBuyButtonIndex = buttonIndex;
                    this.buyButtonClickCnt = (short) 2;
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreListScrollViewUnit01.1
                        @Override // java.lang.Runnable
                        public void run() throws Resources.NotFoundException {
                            StoreListScrollViewUnit01.this.doClick();
                        }
                    }, 200L);
                    return true;
                }
            }
        } else if (event.getAction() == 2) {
            float addOffScrollY = this.preScrollY - event.getY();
            this.offsetScrollY += addOffScrollY;
            if (this.offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
                this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
            } else if (this.offsetScrollY > this.offsetScrollYMax) {
                this.offsetScrollY = this.offsetScrollYMax;
            }
            doScrollViewAutoOffset(this.offsetScrollY);
            this.preScrollY = event.getY();
            Log.d("StoreListScrollLayout01", "ACTION_MOVE   X=" + event.getX() + ", Y=  " + event.getY());
            Log.d("StoreListScrollLayout01", "preScrollY =  " + this.preScrollY);
            Log.d("StoreListScrollLayout01", "offsetScrollY =  " + this.offsetScrollY);
        }
        return true;
    }

    public void doClick() throws Resources.NotFoundException {
        if (this.selectedBuyButtonIndex >= 0 && this.selectedBuyButtonIndex < this.buyButtonsStatusArray.length) {
            this.storeUnit.readyBuyWithId((short) 1, this.selectedBuyButtonIndex);
        }
        unclickAllButton();
    }

    public void gameDraw(Canvas canvas) {
        String nameString;
        short drowType;
        if (this.appDelegate != null) {
            canvas.save();
            canvas.clipRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight);
            Paint bitmapPaint = new Paint();
            bitmapPaint.setColor(-16);
            canvas.drawRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight, bitmapPaint);
            float drawOffsetScrollY = this.offsetScrollY;
            short toolDictionarysArrayListCount = this.appDelegate.getToolDictionarysArrayCountWithTypeId((short) 1);
            short displayCellsCnt = 6;
            if (!this.appDelegate.isRetina4) {
                displayCellsCnt = 5;
            }
            int indexMin = (int) (drawOffsetScrollY / this.cellSpaceY);
            if (indexMin < 0) {
                indexMin = 0;
            } else if (indexMin > toolDictionarysArrayListCount - displayCellsCnt) {
                indexMin = toolDictionarysArrayListCount - displayCellsCnt;
            }
            int indexMax = indexMin + displayCellsCnt;
            if (indexMax > toolDictionarysArrayListCount) {
                indexMax = toolDictionarysArrayListCount;
            }
            for (int i = indexMin; i < indexMax; i++) {
                ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, (short) i);
                if (toolUnitDictionary != null) {
                    Bitmap toolBitmap = null;
                    short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
                    short levelShort = toolUnitDictionary.getLevel();
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, (short) i);
                    String languageString = this.appDelegate.getLocaleLanguage();
                    if (levelShort <= -2) {
                        nameString = "???";
                    } else if (languageString.equals("ja-JP")) {
                        nameString = toolDataDictionary.getTitleJa();
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        nameString = toolDataDictionary.getTitleZhTW();
                    } else if (languageString.equals("zh-CN")) {
                        nameString = toolDataDictionary.getTitleZhCN();
                    } else {
                        nameString = toolDataDictionary.getTitleEn();
                    }
                    String priceString = " -";
                    short lvBuyCpIndex = -1;
                    if (levelShort == -1) {
                        lvBuyCpIndex = 0;
                    } else if (levelShort == 0) {
                        if (tool_0_0_level > 0) {
                            lvBuyCpIndex = 1;
                        } else {
                            lvBuyCpIndex = 0;
                        }
                    } else if (levelShort == 1) {
                        if (tool_0_0_level > 1) {
                            lvBuyCpIndex = 2;
                        } else {
                            lvBuyCpIndex = 1;
                        }
                    } else if (levelShort == 2) {
                        lvBuyCpIndex = 2;
                    }
                    int buyCp = -1;
                    if (toolDataDictionary != null && lvBuyCpIndex >= 0 && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size()) {
                        ToolLevelDictionary newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex);
                        buyCp = newToolLevelDictionary.getLvBuyCp();
                    }
                    if (buyCp >= 0) {
                        priceString = new StringBuilder().append(buyCp).toString();
                    }
                    if (levelShort <= -2) {
                        if (this.appDelegate.tool1Level0Image0ArrayList != null && this.appDelegate.tool1Level0Image0ArrayList.size() > i) {
                            toolBitmap = this.appDelegate.tool1Level0Image0ArrayList.get(i);
                        }
                        drowType = -1;
                    } else if (levelShort == -1) {
                        nameString = String.valueOf(nameString) + "  Lv.1";
                        if (this.appDelegate.tool1Level0Image0ArrayList != null && this.appDelegate.tool1Level0Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level0Image0ArrayList.get(i)) != null) {
                            bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        }
                        drowType = 0;
                    } else if (levelShort <= 0) {
                        if (tool_0_0_level > 0) {
                            nameString = String.valueOf(nameString) + "  Lv.2";
                            if (this.appDelegate.tool1Level1Image0ArrayList != null && this.appDelegate.tool1Level1Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level1Image0ArrayList.get(i)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                            }
                            drowType = 0;
                        } else {
                            nameString = String.valueOf(nameString) + "  Lv.1";
                            if (this.appDelegate.tool1Level0Image0ArrayList != null && this.appDelegate.tool1Level0Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level0Image0ArrayList.get(i)) != null) {
                                bitmapPaint.setAlpha(178);
                            }
                            drowType = 1;
                        }
                    } else if (levelShort <= 1) {
                        if (tool_0_0_level > 1) {
                            nameString = String.valueOf(nameString) + "  Lv.3";
                            if (this.appDelegate.tool1Level2Image0ArrayList != null && this.appDelegate.tool1Level2Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level2Image0ArrayList.get(i)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                            }
                            drowType = 0;
                        } else {
                            nameString = String.valueOf(nameString) + "  Lv.2";
                            if (this.appDelegate.tool1Level1Image0ArrayList != null && this.appDelegate.tool1Level1Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level1Image0ArrayList.get(i)) != null) {
                                bitmapPaint.setAlpha(178);
                            }
                            drowType = 1;
                        }
                    } else {
                        nameString = String.valueOf(nameString) + "  Lv.3";
                        if (this.appDelegate.tool1Level2Image0ArrayList != null && this.appDelegate.tool1Level2Image0ArrayList.size() > i && (toolBitmap = this.appDelegate.tool1Level2Image0ArrayList.get(i)) != null) {
                            bitmapPaint.setAlpha(178);
                        }
                        drowType = 1;
                    }
                    float cellOffsetY = (this.cellSpaceY * i) - drawOffsetScrollY;
                    if (toolBitmap != null) {
                        int imageOffsetY = (int) (this.smallImageOffsetY + cellOffsetY);
                        if (drowType == -1) {
                            Paint paintMask = new Paint();
                            ColorMatrix cm = new ColorMatrix(new float[]{BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 1.0f, BitmapDescriptorFactory.HUE_RED, 1.0f, 1.0f, 1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED});
                            paintMask.setColorFilter(new ColorMatrixColorFilter(cm));
                            paintMask.setAlpha(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                            canvas.drawBitmap(toolBitmap, new Rect(0, 0, toolBitmap.getWidth(), toolBitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), paintMask);
                            if (this.appDelegate.tool_locked_mark_Bitmap != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.drawBitmap(this.appDelegate.tool_locked_mark_Bitmap, new Rect(0, 0, this.appDelegate.tool_locked_mark_Bitmap.getWidth(), this.appDelegate.tool_locked_mark_Bitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), bitmapPaint);
                            }
                        } else {
                            canvas.drawBitmap(toolBitmap, new Rect(0, 0, toolBitmap.getWidth(), toolBitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), bitmapPaint);
                        }
                    }
                    if (drowType == 1) {
                        MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY + cellOffsetY, this.nameLabelTypeface, " " + (i + 1) + ". " + nameString, this.nameLabelFontSize, -1, this.nameLabelStroke1Width, -1, this.nameLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                        MyDraw.drawStrokeText(canvas, this.purchasedLabelOffsetX, this.purchasedLabelOffsetY + cellOffsetY, this.purchasedLabelTypeface, this.purchasedLabelString, this.purchasedLabelFontSize, -436207872, this.purchasedLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.purchasedLabelStroke2Width, -1);
                        MyDraw.drawStrokeText(canvas, this.priceTitleLabelOffsetX, this.priceTitleLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, this.priceTitleLabelString, this.priceTitleLabelFontSize, FluctConstants.FRAME_ALPHA_COLOR, this.priceTitleLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                        MyDraw.drawStrokeText(canvas, this.priceLabelOffsetX, this.priceLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, priceString, this.priceLabelFontSize, -1, this.priceLabelStroke1Width, -1, this.priceLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                    } else if (drowType == 0) {
                        MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY + cellOffsetY, this.nameLabelTypeface, " " + (i + 1) + ". " + nameString, this.nameLabelFontSize, -1, this.nameLabelStroke1Width, -1, this.nameLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                        MyDraw.drawStrokeText(canvas, this.priceTitleLabelOffsetX, this.priceTitleLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, this.priceTitleLabelString, this.priceTitleLabelFontSize, FluctConstants.FRAME_ALPHA_COLOR, this.priceTitleLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                        MyDraw.drawStrokeText(canvas, this.priceLabelOffsetX, this.priceLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, priceString, this.priceLabelFontSize, -1, this.priceLabelStroke1Width, -1, this.priceLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                    } else if (drowType == -1) {
                        MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY + cellOffsetY, this.nameLabelTypeface, " " + (i + 1) + ". " + nameString, this.nameLabelFontSize, -6710887, this.nameLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                        MyDraw.drawStrokeText(canvas, this.priceTitleLabelOffsetX, this.priceTitleLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, this.priceTitleLabelString, this.priceTitleLabelFontSize, -6710887, this.priceTitleLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                        MyDraw.drawStrokeText(canvas, this.priceLabelOffsetX, this.priceLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, " -", this.priceLabelFontSize, -6710887, this.priceLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                    }
                    if (this.buyButtonsStatusArray != null && i < this.buyButtonsStatusArray.length) {
                        if (this.buyButtonsStatusArray[i] == 0) {
                            MyDraw.drawStrokeRect(canvas, this.buyButtonOffsetX, this.buyButtonOffsetY + cellOffsetY, this.buyButtonWidth, this.buyButtonHeight, this.buyButtonColor0, this.buyButtonStrokeWidth1, this.buyButtonColor1, this.buyButtonStrokeWidth2, this.buyButtonColor2, this.buyButtonStrokeWidth3, this.buyButtonColor3, this.buyButtonRadius);
                            MyDraw.drawStrokeText(canvas, this.buyButtonTitleLabelOffsetX, this.buyButtonTitleLabelOffsetY + cellOffsetY, this.buyButtonTitleLabelTypeface, this.buyButtonTitleLabelString, this.buyButtonTitleLabelFontSize, this.buyButtonTitleLabelColor0, this.buyButtonTitleLabelStroke1Width, this.buyButtonTitleLabelColor1, this.buyButtonTitleLabelStroke2Width, this.buyButtonTitleLabelColor2);
                            if (i == this.selectedBuyButtonIndex) {
                                MyDraw.drawStrokeRect(canvas, this.buyButtonOffsetX, this.buyButtonOffsetY + cellOffsetY, this.buyButtonWidth, this.buyButtonHeight, 1426063360, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buyButtonRadius);
                            }
                        } else if (this.buyButtonsStatusArray[i] == 1) {
                            MyDraw.drawStrokeRect(canvas, this.buyButtonOffsetX, this.buyButtonOffsetY + cellOffsetY, this.buyButtonWidth, this.buyButtonHeight, -1714447763, this.buyButtonStrokeWidth1, FluctConstants.FRAME_ALPHA_COLOR, this.buyButtonStrokeWidth2, 0, this.buyButtonStrokeWidth3, 0, this.buyButtonRadius);
                            MyDraw.drawStrokeText(canvas, this.buyButtonTitleLabelOffsetX, this.buyButtonTitleLabelOffsetY + cellOffsetY, this.buyButtonTitleLabelTypeface, this.buyButtonTitleLabelString, this.buyButtonTitleLabelFontSize, -1711276033, this.buyButtonTitleLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, this.buyButtonTitleLabelStroke2Width, 0);
                            MyDraw.drawStrokeRect(canvas, this.buyButtonOffsetX, this.buyButtonOffsetY + cellOffsetY, this.buyButtonWidth, this.buyButtonHeight, 1627389951, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buyButtonRadius);
                        }
                    }
                }
                MyDraw.drawOneRect(canvas, this.offsetX, ((this.offsetY + (this.cellSpaceY * (i + 1))) - this.cellSpaceLineHeight) - drawOffsetScrollY, this.finalWidth, this.cellSpaceLineHeight, 570425344, BitmapDescriptorFactory.HUE_RED);
            }
            canvas.restore();
        }
    }

    public void doScrollViewAutoOffset(float _offsetScrollY) {
        this.offsetScrollY = _offsetScrollY;
        if (this.offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
            this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        } else if (this.offsetScrollY > this.offsetScrollYMax) {
            this.offsetScrollY = this.offsetScrollYMax;
        }
        if (this.storeUnit != null) {
            float contentY = this.offsetScrollYMax;
            if (contentY < 1.0f) {
                contentY = 1.0f;
            }
            float offsetY = this.offsetScrollY;
            if (offsetY < 0.0d) {
                offsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (offsetY > contentY) {
                offsetY = contentY;
            }
            if (this.storeUnit != null) {
                this.storeUnit.changeListViewFastScrollDragViewOffset(offsetY, contentY, (short) 1);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.storeUnit = null;
        this.appDelegate = null;
    }
}
