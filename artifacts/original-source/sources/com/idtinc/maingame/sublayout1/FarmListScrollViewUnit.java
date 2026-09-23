package com.idtinc.maingame.sublayout1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.CharacterDataDictionary;
import com.idtinc.ckunit.FarmUnitDictionary;
import com.idtinc.custom.MyDraw;
import java.lang.reflect.Array;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmListScrollViewUnit {
    private AppDelegate appDelegate;
    public float button0OffsetX;
    public float button0TitleLabelOffsetX;
    public String button0TitleLabelString;
    public float button1OffsetX;
    public float button1TitleLabelOffsetX;
    public String button1TitleLabelString;
    public int buttonColor0;
    public int buttonColor1;
    public int buttonColor2;
    public int buttonColor3;
    public float buttonHeight;
    public float buttonOffsetY;
    public float buttonRadius;
    public float buttonStrokeWidth1;
    public float buttonStrokeWidth2;
    public float buttonStrokeWidth3;
    public int buttonTitleLabelColor0;
    public int buttonTitleLabelColor1;
    public int buttonTitleLabelColor2;
    public float buttonTitleLabelFontSize;
    public float buttonTitleLabelOffsetY;
    public float buttonTitleLabelStroke1Width;
    public float buttonTitleLabelStroke2Width;
    Typeface buttonTitleLabelTypeface;
    public float buttonWidth;
    private short[][] buttonsStatusArray;
    private short buyButtonClickCnt;
    public float cellSpaceLineHeight;
    public float cellSpaceY;
    private float clipRectHeight;
    private float clipRectWidth;
    public float countLabelOffsetX;
    public float countLabelOffsetY;
    public float countLabelStroke1Width;
    public float countLabelStroke2Width;
    public float countTitleLabelFontSize;
    public float countTitleLabelOffsetX;
    public float countTitleLabelOffsetY;
    public String countTitleLabelString;
    public float countTitleLabelStroke1Width;
    public Typeface countTitleLabelTypeface;
    public int[][] countsArray;
    private FarmUnit farmUnit;
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
    private short selectedButtonIndex0;
    private short selectedButtonIndex1;
    private int selectedCountBackRectColor0;
    private int selectedCountBackRectColor1;
    private int selectedCountBackRectColor2;
    private int selectedCountBackRectColor3;
    private float selectedCountBackRectHeight;
    private float selectedCountBackRectOffsetX;
    private float selectedCountBackRectOffsetY;
    private float selectedCountBackRectRadius;
    private float selectedCountBackRectStrokeWidth1;
    private float selectedCountBackRectStrokeWidth2;
    private float selectedCountBackRectStrokeWidth3;
    private float selectedCountBackRectWidth;
    private int selectedCountLabelColor0;
    private int selectedCountLabelColor1;
    private int selectedCountLabelColor2;
    private float selectedCountLabelFontSize;
    private float selectedCountLabelOffsetX;
    private float selectedCountLabelOffsetY;
    private float selectedCountLabelStroke1Width;
    private float selectedCountLabelStroke2Width;
    Typeface selectedCountLabelTypeface;
    public float smallImageHeight;
    public float smallImageOffsetX;
    public float smallImageOffsetY;
    public float smallImageTouchHeight;
    public float smallImageTouchOffsetX;
    public float smallImageTouchOffsetY;
    public float smallImageTouchWidth;
    public float smallImageWidth;
    public short tag;
    private float zoomRate;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    public String nameLabelString = "";

    public FarmListScrollViewUnit(float _offsetX, float _offsetY, float _finalwidth, float _finalheight, float _zoomrate, FarmUnit _farmUnit, AppDelegate _appDelegate) {
        this.offsetX = BitmapDescriptorFactory.HUE_RED;
        this.offsetY = BitmapDescriptorFactory.HUE_RED;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.originHeight = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.clipRectWidth = BitmapDescriptorFactory.HUE_RED;
        this.clipRectHeight = BitmapDescriptorFactory.HUE_RED;
        this.hidden = false;
        this.tag = (short) -1;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        this.selectedButtonIndex0 = (short) -1;
        this.selectedButtonIndex1 = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        this.smallImageOffsetX = 5.0f;
        this.smallImageOffsetY = -1.0f;
        this.smallImageWidth = 60.0f;
        this.smallImageHeight = 60.0f;
        this.smallImageTouchOffsetX = 10.0f;
        this.smallImageTouchOffsetY = 4.0f;
        this.smallImageTouchWidth = 50.0f;
        this.smallImageTouchHeight = 50.0f;
        this.nameLabelFontSize = 16.0f;
        this.nameLabelStroke1Width = 0.2f;
        this.nameLabelStroke2Width = 3.0f;
        this.nameLabelOffsetX = 60.0f;
        this.nameLabelOffsetY = 2.0f;
        this.priceTitleLabelString = "";
        this.priceTitleLabelFontSize = 16.0f;
        this.priceTitleLabelStroke1Width = 0.2f;
        this.priceTitleLabelOffsetX = 60.0f;
        this.priceTitleLabelOffsetY = 20.0f;
        this.priceLabelStroke1Width = 0.2f;
        this.priceLabelStroke2Width = 3.0f;
        this.priceLabelOffsetX = 105.0f;
        this.priceLabelOffsetY = 20.0f;
        this.countTitleLabelString = "";
        this.countTitleLabelFontSize = 16.0f;
        this.countTitleLabelStroke1Width = 0.2f;
        this.countTitleLabelOffsetX = 60.0f;
        this.countTitleLabelOffsetY = 40.0f;
        this.countLabelStroke1Width = 0.2f;
        this.countLabelStroke2Width = 3.0f;
        this.countLabelOffsetX = 105.0f;
        this.countLabelOffsetY = 40.0f;
        this.selectedCountBackRectOffsetX = 183.0f;
        this.selectedCountBackRectOffsetY = 39.0f;
        this.selectedCountBackRectWidth = 55.0f;
        this.selectedCountBackRectHeight = 28.0f;
        this.selectedCountBackRectColor0 = -3355444;
        this.selectedCountBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED;
        this.selectedCountBackRectColor1 = 0;
        this.selectedCountBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.selectedCountBackRectColor2 = 0;
        this.selectedCountBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.selectedCountBackRectColor3 = 0;
        this.selectedCountBackRectRadius = 6.0f;
        this.selectedCountLabelFontSize = 16.0f;
        this.selectedCountLabelColor0 = -1;
        this.selectedCountLabelStroke1Width = 0.2f;
        this.selectedCountLabelColor1 = -1;
        this.selectedCountLabelStroke2Width = 3.0f;
        this.selectedCountLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        this.selectedCountLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.selectedCountLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.buttonWidth = 28.0f;
        this.buttonHeight = 28.0f;
        this.button0OffsetX = 154.0f;
        this.button1OffsetX = 239.0f;
        this.buttonOffsetY = 39.0f;
        this.buttonColor0 = -31230;
        this.buttonStrokeWidth1 = 2.0f;
        this.buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED;
        this.buttonColor2 = 0;
        this.buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED;
        this.buttonColor3 = 0;
        this.buttonRadius = 6.0f;
        this.button0TitleLabelString = "";
        this.button1TitleLabelString = "";
        this.buttonTitleLabelFontSize = 30.0f;
        this.buttonTitleLabelColor0 = -1;
        this.buttonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelColor2 = 0;
        this.button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.button1TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.buttonTitleLabelOffsetY = BitmapDescriptorFactory.HUE_RED;
        this.cellSpaceY = 70.0f;
        this.cellSpaceLineHeight = 1.0f;
        this.appDelegate = _appDelegate;
        this.farmUnit = _farmUnit;
        this.offsetX = _offsetX;
        this.offsetY = _offsetY;
        this.finalWidth = _finalwidth;
        this.originHeight = _finalheight;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.clipRectWidth = this.offsetX + this.finalWidth;
        this.clipRectHeight = this.offsetY + this.originHeight;
        this.hidden = false;
        this.tag = (short) -1;
        this.offsetScrollYMax = this.originHeight;
        this.offsetScrollY = BitmapDescriptorFactory.HUE_RED;
        setPreScrollPoint(-9999.0f, -9999.0f);
        this.selectedButtonIndex0 = (short) -1;
        this.selectedButtonIndex1 = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        this.smallImageOffsetX = this.offsetX + (5.0f * this.zoomRate);
        this.smallImageOffsetY = this.offsetY - (1.0f * this.zoomRate);
        this.smallImageWidth = 60.0f * this.zoomRate;
        this.smallImageHeight = 60.0f * this.zoomRate;
        this.smallImageTouchOffsetX = this.offsetX + (10.0f * this.zoomRate);
        this.smallImageTouchOffsetY = this.offsetY + (4.0f * this.zoomRate);
        this.smallImageTouchWidth = 50.0f * this.zoomRate;
        this.smallImageTouchHeight = 50.0f * this.zoomRate;
        Paint newPaint = new Paint(257);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            this.nameLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.nameLabelFontSize = 16.0f * this.zoomRate;
            this.priceTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.priceTitleLabelFontSize = 16.0f * this.zoomRate;
            this.countTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.countTitleLabelFontSize = 16.0f * this.zoomRate;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
            this.nameLabelTypeface = Typeface.DEFAULT_BOLD;
            this.nameLabelFontSize = 15.0f * this.zoomRate;
            this.priceTitleLabelTypeface = Typeface.DEFAULT_BOLD;
            this.priceTitleLabelFontSize = 15.0f * this.zoomRate;
            this.countTitleLabelTypeface = Typeface.DEFAULT_BOLD;
            this.countTitleLabelFontSize = 15.0f * this.zoomRate;
        } else {
            this.nameLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.nameLabelFontSize = 16.0f * this.zoomRate;
            this.priceTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.priceTitleLabelFontSize = 16.0f * this.zoomRate;
            this.countTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
            this.countTitleLabelFontSize = 16.0f * this.zoomRate;
        }
        this.nameLabelStroke1Width = 0.2f * this.zoomRate;
        this.nameLabelStroke2Width = 3.0f * this.zoomRate;
        this.nameLabelOffsetX = this.offsetX + (60.0f * this.zoomRate);
        this.nameLabelOffsetY = this.offsetY + (4.0f * this.zoomRate) + this.nameLabelFontSize;
        this.priceTitleLabelStroke1Width = 0.2f * this.zoomRate;
        newPaint.setTypeface(this.priceTitleLabelTypeface);
        newPaint.setTextSize(this.priceTitleLabelFontSize);
        this.priceTitleLabelString = " " + this.appDelegate.getResources().getString(R.string.Price) + ":";
        this.priceTitleLabelOffsetX = this.offsetX + (60.0f * this.zoomRate);
        this.priceTitleLabelOffsetY = this.offsetY + (24.0f * this.zoomRate) + this.priceTitleLabelFontSize;
        this.priceLabelStroke1Width = 0.2f * this.zoomRate;
        this.priceLabelStroke2Width = 3.0f * this.zoomRate;
        this.priceLabelOffsetX = this.offsetX + (105.0f * this.zoomRate);
        this.priceLabelOffsetY = this.offsetY + (24.0f * this.zoomRate) + this.priceTitleLabelFontSize;
        this.countTitleLabelStroke1Width = 0.2f * this.zoomRate;
        newPaint.setTypeface(this.countTitleLabelTypeface);
        newPaint.setTextSize(this.countTitleLabelFontSize);
        this.countTitleLabelString = " " + this.appDelegate.getResources().getString(R.string.QTY) + ":";
        this.countTitleLabelOffsetX = this.offsetX + (60.0f * this.zoomRate);
        this.countTitleLabelOffsetY = this.offsetY + (44.0f * this.zoomRate) + this.countTitleLabelFontSize;
        this.countLabelStroke1Width = 0.2f * this.zoomRate;
        this.countLabelStroke2Width = 3.0f * this.zoomRate;
        this.countLabelOffsetX = this.offsetX + (105.0f * this.zoomRate);
        this.countLabelOffsetY = this.offsetY + (44.0f * this.zoomRate) + this.countTitleLabelFontSize;
        this.selectedCountBackRectOffsetX = this.offsetX + (183.0f * this.zoomRate);
        this.selectedCountBackRectOffsetY = this.offsetY + (39.0f * this.zoomRate);
        this.selectedCountBackRectWidth = 55.0f * this.zoomRate;
        this.selectedCountBackRectHeight = 28.0f * this.zoomRate;
        this.selectedCountBackRectColor0 = -2236963;
        this.selectedCountBackRectStrokeWidth1 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectedCountBackRectColor1 = 0;
        this.selectedCountBackRectStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectedCountBackRectColor2 = 0;
        this.selectedCountBackRectStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.selectedCountBackRectColor3 = 0;
        this.selectedCountBackRectRadius = 6.0f * this.zoomRate;
        this.selectedCountLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.selectedCountLabelFontSize = 16.0f * this.zoomRate;
        this.selectedCountLabelColor0 = -1;
        this.selectedCountLabelStroke1Width = 0.2f * this.zoomRate;
        this.selectedCountLabelColor1 = -1;
        this.selectedCountLabelStroke2Width = 3.0f * this.zoomRate;
        this.selectedCountLabelColor2 = FluctConstants.FRAME_ALPHA_COLOR;
        newPaint.setTypeface(this.selectedCountLabelTypeface);
        newPaint.setTextSize(this.selectedCountLabelFontSize);
        this.selectedCountLabelOffsetX = this.selectedCountBackRectOffsetX + ((this.selectedCountBackRectWidth - newPaint.measureText("")) / 2.0f);
        this.selectedCountLabelOffsetY = (this.selectedCountBackRectOffsetY + this.selectedCountBackRectHeight) - (this.selectedCountLabelFontSize * 0.5f);
        this.buttonWidth = 28.0f * this.zoomRate;
        this.buttonHeight = 28.0f * this.zoomRate;
        this.button0OffsetX = this.offsetX + (154.0f * this.zoomRate);
        this.button1OffsetX = this.offsetX + (239.0f * this.zoomRate);
        this.buttonOffsetY = this.offsetY + (39.0f * this.zoomRate);
        this.buttonColor0 = -31230;
        this.buttonStrokeWidth1 = 2.0f * this.zoomRate;
        this.buttonColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonStrokeWidth2 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buttonColor2 = 0;
        this.buttonStrokeWidth3 = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buttonColor3 = 0;
        this.buttonRadius = 6.0f * this.zoomRate;
        this.button0TitleLabelString = "-";
        this.button1TitleLabelString = "+";
        this.buttonTitleLabelTypeface = this.appDelegate.typeface_FONTNAME_00;
        this.buttonTitleLabelFontSize = 30.0f * this.zoomRate;
        this.buttonTitleLabelColor0 = -1;
        this.buttonTitleLabelStroke1Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buttonTitleLabelColor1 = FluctConstants.FRAME_ALPHA_COLOR;
        this.buttonTitleLabelStroke2Width = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.buttonTitleLabelColor2 = 0;
        this.button0TitleLabelOffsetX = BitmapDescriptorFactory.HUE_RED;
        newPaint.setTypeface(this.buttonTitleLabelTypeface);
        newPaint.setTextSize(this.buttonTitleLabelFontSize);
        this.button0TitleLabelOffsetX = this.button0OffsetX + ((this.buttonWidth - newPaint.measureText(this.button0TitleLabelString)) / 2.0f);
        this.button1TitleLabelOffsetX = this.button1OffsetX + ((this.buttonWidth - newPaint.measureText(this.button1TitleLabelString)) / 2.0f);
        this.buttonTitleLabelOffsetY = this.buttonOffsetY + ((this.buttonHeight + (this.buttonTitleLabelFontSize * 0.7f)) / 2.0f);
        this.cellSpaceY = 70.0f * this.zoomRate;
        this.cellSpaceLineHeight = 1.0f * this.zoomRate;
        this.myDraw = new MyDraw();
    }

    public void selectWithType(short _selectType) {
        Log.d("FarmListScrollLayout", "reload:" + ((int) this.tag));
        int characterUnitDictionarysArrayCountWithEggId = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId(this.tag);
        clearAllButton();
        if (this.countsArray == null && characterUnitDictionarysArrayCountWithEggId > 0) {
            this.countsArray = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, characterUnitDictionarysArrayCountWithEggId, 2);
            for (int i = 0; i < characterUnitDictionarysArrayCountWithEggId; i++) {
                Log.d("countsArray", "appMainActivity.getToolDictionarysArrayCountWithTypeId((short)1)" + i);
                this.countsArray[i][0] = 0;
                this.countsArray[i][1] = 0;
            }
        }
        if (this.buttonsStatusArray == null && characterUnitDictionarysArrayCountWithEggId > 0) {
            this.buttonsStatusArray = (short[][]) Array.newInstance((Class<?>) Short.TYPE, characterUnitDictionarysArrayCountWithEggId, 2);
            for (int i2 = 0; i2 < characterUnitDictionarysArrayCountWithEggId; i2++) {
                Log.d("buyButtonsStstusArray", "appMainActivity.getToolDictionarysArrayCountWithTypeId((short)1)" + i2);
                this.buttonsStatusArray[i2][0] = -1;
                this.buttonsStatusArray[i2][1] = -1;
            }
        }
        if (this.buttonsStatusArray != null) {
            for (int i3 = 0; i3 < characterUnitDictionarysArrayCountWithEggId && i3 < this.buttonsStatusArray.length; i3++) {
                FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(this.tag, (short) i3);
                if (unitDictionary != null) {
                    int countShort = (int) unitDictionary.getCount();
                    if (countShort > 0) {
                        this.countsArray[i3][0] = 0;
                        this.countsArray[i3][1] = countShort;
                        this.buttonsStatusArray[i3][0] = -1;
                        this.buttonsStatusArray[i3][1] = -1;
                        if (_selectType == 2) {
                            this.countsArray[i3][0] = countShort;
                        } else if (_selectType == 1 && countShort >= 2) {
                            this.countsArray[i3][0] = countShort - 1;
                        }
                        if (this.countsArray[i3][0] < this.countsArray[i3][1]) {
                            this.buttonsStatusArray[i3][1] = 0;
                        }
                        if (this.countsArray[i3][0] > 0) {
                            this.buttonsStatusArray[i3][0] = 0;
                        }
                    } else {
                        this.countsArray[i3][0] = 0;
                        this.countsArray[i3][1] = 0;
                        this.buttonsStatusArray[i3][0] = -1;
                        this.buttonsStatusArray[i3][1] = -1;
                    }
                }
            }
        }
    }

    public void reload() {
        Log.d("FarmListScrollLayout", "reload:" + ((int) this.tag));
        int characterUnitDictionarysArrayCountWithEggId = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId(this.tag);
        setPreScrollPoint(-9999.0f, -9999.0f);
        clearAllButton();
        if (this.countsArray == null && characterUnitDictionarysArrayCountWithEggId > 0) {
            this.countsArray = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, characterUnitDictionarysArrayCountWithEggId, 2);
            for (int i = 0; i < characterUnitDictionarysArrayCountWithEggId; i++) {
                Log.d("countsArray", "appMainActivity.getToolDictionarysArrayCountWithTypeId((short)1)" + i);
                this.countsArray[i][0] = 0;
                this.countsArray[i][1] = 0;
            }
        }
        if (this.buttonsStatusArray == null && characterUnitDictionarysArrayCountWithEggId > 0) {
            this.buttonsStatusArray = (short[][]) Array.newInstance((Class<?>) Short.TYPE, characterUnitDictionarysArrayCountWithEggId, 2);
            for (int i2 = 0; i2 < characterUnitDictionarysArrayCountWithEggId; i2++) {
                Log.d("buyButtonsStstusArray", "appMainActivity.getToolDictionarysArrayCountWithTypeId((short)1)" + i2);
                this.buttonsStatusArray[i2][0] = -1;
                this.buttonsStatusArray[i2][1] = -1;
            }
        }
        if (this.buttonsStatusArray != null) {
            for (int i3 = 0; i3 < characterUnitDictionarysArrayCountWithEggId && i3 < this.buttonsStatusArray.length; i3++) {
                FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(this.tag, (short) i3);
                if (unitDictionary != null) {
                    int countShort = (int) unitDictionary.getCount();
                    if (countShort > 0) {
                        this.countsArray[i3][0] = 0;
                        this.countsArray[i3][1] = countShort;
                        this.buttonsStatusArray[i3][0] = -1;
                        this.buttonsStatusArray[i3][1] = 0;
                    } else {
                        this.countsArray[i3][0] = 0;
                        this.countsArray[i3][1] = 0;
                        this.buttonsStatusArray[i3][0] = -1;
                        this.buttonsStatusArray[i3][1] = -1;
                    }
                }
            }
        }
        this.finalHeight = this.originHeight;
        this.offsetScrollYMax = BitmapDescriptorFactory.HUE_RED;
        if (characterUnitDictionarysArrayCountWithEggId > 3) {
            this.finalHeight = this.cellSpaceY * characterUnitDictionarysArrayCountWithEggId;
            this.offsetScrollYMax = this.finalHeight - this.originHeight;
            Log.d("FarmListScrollLayout", String.valueOf((int) this.tag) + ":finalHeight:" + this.finalHeight);
        }
        doScrollViewAutoOffset(this.offsetScrollY);
    }

    public void clearAllButton() {
        this.selectedButtonIndex0 = (short) -1;
        this.selectedButtonIndex1 = (short) -1;
        this.buyButtonClickCnt = (short) -1;
        if (this.buttonsStatusArray != null) {
            for (int i = 0; i < this.buttonsStatusArray.length; i++) {
                this.buttonsStatusArray[i][0] = -1;
                this.buttonsStatusArray[i][1] = -1;
            }
        }
    }

    public void unclickAllButton() {
        this.selectedButtonIndex0 = (short) -1;
        this.selectedButtonIndex1 = (short) -1;
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

    public void changeCounts() {
        if (this.buttonsStatusArray != null && this.countsArray != null && this.selectedButtonIndex0 >= 0 && this.selectedButtonIndex0 < this.buttonsStatusArray.length && this.selectedButtonIndex1 >= 0 && this.selectedButtonIndex1 <= 1 && this.buttonsStatusArray[this.selectedButtonIndex0][this.selectedButtonIndex1] == 0 && this.selectedButtonIndex0 >= 0 && this.selectedButtonIndex0 < this.countsArray.length) {
            if (this.countsArray[this.selectedButtonIndex0][1] <= 0) {
                this.countsArray[this.selectedButtonIndex0][0] = 0;
                unclickAllButton();
                return;
            }
            int addUnit = 1;
            if (this.buyButtonClickCnt >= 12) {
                addUnit = 10;
            }
            if (this.selectedButtonIndex1 == 0) {
                int[] iArr = this.countsArray[this.selectedButtonIndex0];
                iArr[0] = iArr[0] - addUnit;
                if (this.countsArray[this.selectedButtonIndex0][0] < this.countsArray[this.selectedButtonIndex0][1]) {
                    this.buttonsStatusArray[this.selectedButtonIndex0][1] = 0;
                }
                if (this.countsArray[this.selectedButtonIndex0][0] <= 0) {
                    this.countsArray[this.selectedButtonIndex0][0] = 0;
                    this.buttonsStatusArray[this.selectedButtonIndex0][0] = -1;
                    unclickAllButton();
                }
                if (!this.farmUnit.hidden) {
                    this.appDelegate.doSoundPoolPlay(2);
                }
                this.farmUnit.refreshTotal();
                return;
            }
            if (this.selectedButtonIndex1 == 1) {
                int[] iArr2 = this.countsArray[this.selectedButtonIndex0];
                iArr2[0] = iArr2[0] + addUnit;
                if (this.countsArray[this.selectedButtonIndex0][0] > 0) {
                    this.buttonsStatusArray[this.selectedButtonIndex0][0] = 0;
                }
                if (this.countsArray[this.selectedButtonIndex0][0] >= this.countsArray[this.selectedButtonIndex0][1]) {
                    this.countsArray[this.selectedButtonIndex0][0] = this.countsArray[this.selectedButtonIndex0][1];
                    this.buttonsStatusArray[this.selectedButtonIndex0][1] = -1;
                    unclickAllButton();
                }
                if (!this.farmUnit.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                this.farmUnit.refreshTotal();
            }
        }
    }

    public void readySell() {
    }

    public int getTotal() {
        CharacterDataDictionary characterDataDictionary;
        int countShort;
        int totalCP = 0;
        if (this.countsArray != null) {
            for (int i = 0; i < this.countsArray.length; i++) {
                if (this.countsArray[i][0] > 0 && this.countsArray[i][0] <= this.countsArray[i][1] && (characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(this.tag, (short) i)) != null) {
                    int price = characterDataDictionary.getCp1();
                    FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(this.tag, (short) i);
                    if (unitDictionary != null && (countShort = (int) unitDictionary.getCount()) > 0 && price > 0) {
                        if (this.countsArray[i][1] != countShort) {
                            this.countsArray[i][1] = countShort;
                        }
                        if (this.countsArray[i][0] >= countShort) {
                            this.countsArray[i][0] = countShort;
                        }
                        totalCP += this.countsArray[i][0] * price;
                    }
                }
            }
        }
        return totalCP;
    }

    public void doLoop() {
        if (this.selectedButtonIndex0 >= 0 && this.selectedButtonIndex0 < this.buttonsStatusArray.length && this.selectedButtonIndex1 >= 0 && this.selectedButtonIndex1 < 2) {
            if (this.buyButtonClickCnt >= 0) {
                this.buyButtonClickCnt = (short) (this.buyButtonClickCnt + 1);
                if (this.buyButtonClickCnt > 2) {
                    changeCounts();
                    return;
                }
                return;
            }
            unclickAllButton();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        FarmUnitDictionary unitDictionary;
        if (event.getX() < this.offsetX || event.getX() > this.clipRectWidth || event.getY() < this.offsetY || event.getY() > this.clipRectHeight) {
            unclickAllButton();
            return false;
        }
        if (event.getAction() == 1) {
            unclickAllButton();
        } else if (event.getAction() == 0) {
            this.selectedButtonIndex0 = (short) -1;
            this.selectedButtonIndex1 = (short) -1;
            this.buyButtonClickCnt = (short) -1;
            this.preScrollY = event.getY();
            if (event.getX() <= this.smallImageTouchOffsetX || event.getX() >= this.smallImageTouchOffsetX + this.smallImageTouchWidth) {
                if (event.getX() <= this.button0OffsetX || event.getX() >= this.button0OffsetX + this.buttonWidth) {
                    if (event.getX() > this.button1OffsetX && event.getX() < this.button1OffsetX + this.buttonWidth && event.getY() > BitmapDescriptorFactory.HUE_RED && event.getY() < this.finalHeight) {
                        short buttonIndex = (short) (((this.offsetScrollY + event.getY()) - this.offsetY) / this.cellSpaceY);
                        float checkOffsetY = ((this.offsetScrollY + event.getY()) - this.offsetY) - (this.cellSpaceY * buttonIndex);
                        if (checkOffsetY > this.buttonOffsetY - this.offsetY && checkOffsetY < (this.buttonOffsetY + this.buttonHeight) - this.offsetY && this.buttonsStatusArray != null && buttonIndex >= 0 && buttonIndex < this.buttonsStatusArray.length && this.buttonsStatusArray[buttonIndex][1] == 0) {
                            this.selectedButtonIndex0 = buttonIndex;
                            this.selectedButtonIndex1 = (short) 1;
                            this.buyButtonClickCnt = (short) 0;
                            changeCounts();
                            return true;
                        }
                    }
                } else if (event.getY() >= this.offsetY && event.getY() <= this.clipRectHeight) {
                    short buttonIndex2 = (short) (((this.offsetScrollY + event.getY()) - this.offsetY) / this.cellSpaceY);
                    float checkOffsetY2 = ((this.offsetScrollY + event.getY()) - this.offsetY) - (this.cellSpaceY * buttonIndex2);
                    if (checkOffsetY2 > this.buttonOffsetY - this.offsetY && checkOffsetY2 < (this.buttonOffsetY + this.buttonHeight) - this.offsetY) {
                        Log.d("FarmListScrollLayout", "buttonIndex:" + ((int) buttonIndex2));
                        if (this.buttonsStatusArray != null && buttonIndex2 >= 0 && buttonIndex2 < this.buttonsStatusArray.length && this.buttonsStatusArray[buttonIndex2][0] == 0) {
                            this.selectedButtonIndex0 = buttonIndex2;
                            this.selectedButtonIndex1 = (short) 0;
                            this.buyButtonClickCnt = (short) 0;
                            changeCounts();
                            return true;
                        }
                    }
                }
            } else if (event.getY() >= this.offsetY && event.getY() <= this.clipRectHeight) {
                short smallImageIndex = (short) (((this.offsetScrollY + event.getY()) - this.offsetY) / this.cellSpaceY);
                float checkOffsetY3 = ((this.offsetScrollY + event.getY()) - this.offsetY) - (this.cellSpaceY * smallImageIndex);
                if (checkOffsetY3 > this.smallImageTouchOffsetY - this.offsetY && checkOffsetY3 < (this.smallImageTouchOffsetY + this.smallImageTouchHeight) - this.offsetY && this.appDelegate != null && (unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(this.tag, smallImageIndex)) != null) {
                    int totalCountInt = (int) unitDictionary.getTotalCount();
                    if (totalCountInt >= 1 && this.farmUnit != null && !this.hidden) {
                        this.farmUnit.doPopContentPopUnit(this.tag, smallImageIndex);
                        return true;
                    }
                }
            }
            Log.d("FarmListScrollLayout", "ACTION_DOWN   X=" + event.getX() + ", Y=  " + event.getY());
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
            Log.d("FarmListScrollLayout", "ACTION_MOVE   X=" + event.getX() + ", Y=  " + event.getY());
            Log.d("FarmListScrollLayout", "preScrollY =  " + this.preScrollY);
            Log.d("FarmListScrollLayout", "offsetScrollY =  " + this.offsetScrollY);
        }
        return false;
    }

    public void gameDraw(Canvas canvas) {
        String nameString;
        String nameString2;
        short drowType;
        canvas.save();
        canvas.clipRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight);
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(-16);
        canvas.drawRect(this.offsetX, this.offsetY, this.clipRectWidth, this.clipRectHeight, bitmapPaint);
        float drawOffsetScrollY = this.offsetScrollY;
        if (this.appDelegate != null) {
            short characterUnitDictionarysArrayListCount = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId(this.tag);
            int indexMin = (int) (drawOffsetScrollY / this.cellSpaceY);
            if (indexMin < 0) {
                indexMin = 0;
            } else if (indexMin > characterUnitDictionarysArrayListCount - 5) {
                indexMin = characterUnitDictionarysArrayListCount - 5;
            }
            int indexMax = indexMin + 5;
            if (indexMax > characterUnitDictionarysArrayListCount) {
                indexMax = characterUnitDictionarysArrayListCount;
            }
            for (int i = indexMin; i < indexMax; i++) {
                FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(this.tag, (short) i);
                if (unitDictionary != null) {
                    int countShort = (int) unitDictionary.getCount();
                    CharacterDataDictionary characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(this.tag, (short) i);
                    if (characterDataDictionary != null) {
                        String languageString = this.appDelegate.getLocaleLanguage();
                        String countString = " -";
                        if (this.tag == 1) {
                            if (i < 9) {
                                nameString = "D0" + (i + 1) + "  ";
                            } else {
                                nameString = "D" + (i + 1) + "  ";
                            }
                        } else if (i < 9) {
                            nameString = "C0" + (i + 1) + "  ";
                        } else {
                            nameString = "C" + (i + 1) + "  ";
                        }
                        if (countShort < 0) {
                            nameString2 = String.valueOf(nameString) + "???";
                        } else {
                            countString = " " + countShort;
                            if (languageString.equals("ja-JP")) {
                                nameString2 = String.valueOf(nameString) + characterDataDictionary.getTitleJa();
                            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                                nameString2 = String.valueOf(nameString) + characterDataDictionary.getTitleZhTW();
                            } else if (languageString.equals("zh-CN")) {
                                nameString2 = String.valueOf(nameString) + characterDataDictionary.getTitleZhCN();
                            } else {
                                nameString2 = String.valueOf(nameString) + characterDataDictionary.getTitleEn();
                            }
                        }
                        String priceString = " -";
                        if (countShort >= 0) {
                            short price = characterDataDictionary.getCp1();
                            priceString = " " + ((int) price);
                        }
                        Bitmap characterBitmap = null;
                        if (countShort < 0) {
                            if (this.tag == 0) {
                                if (this.appDelegate.character0Image0ArrayList != null && this.appDelegate.character0Image0ArrayList.size() > i) {
                                    characterBitmap = this.appDelegate.character0Image0ArrayList.get(i);
                                }
                            } else if (this.tag == 1 && this.appDelegate.character1Image0ArrayList != null && this.appDelegate.character1Image0ArrayList.size() > i) {
                                characterBitmap = this.appDelegate.character1Image0ArrayList.get(i);
                            }
                            drowType = -1;
                        } else {
                            if (this.tag == 0) {
                                if (this.appDelegate.character0Image0ArrayList != null && this.appDelegate.character0Image0ArrayList.size() > i && (characterBitmap = this.appDelegate.character0Image0ArrayList.get(i)) != null) {
                                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                }
                            } else if (this.tag == 1 && this.appDelegate.character1Image0ArrayList != null && this.appDelegate.character1Image0ArrayList.size() > i && (characterBitmap = this.appDelegate.character1Image0ArrayList.get(i)) != null) {
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                            }
                            drowType = 0;
                        }
                        float cellOffsetY = (this.cellSpaceY * i) - drawOffsetScrollY;
                        if (characterBitmap != null) {
                            int imageOffsetY = (int) (this.smallImageOffsetY + cellOffsetY);
                            if (drowType == -1) {
                                Paint paintMask = new Paint();
                                ColorMatrix cm = new ColorMatrix(new float[]{BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 1.0f, BitmapDescriptorFactory.HUE_RED, 1.0f, 1.0f, 1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED});
                                paintMask.setColorFilter(new ColorMatrixColorFilter(cm));
                                paintMask.setAlpha(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                                canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), paintMask);
                                if (this.appDelegate.tool_locked_mark_Bitmap != null) {
                                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                    canvas.drawBitmap(this.appDelegate.tool_locked_mark_Bitmap, new Rect(0, 0, this.appDelegate.tool_locked_mark_Bitmap.getWidth(), this.appDelegate.tool_locked_mark_Bitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), bitmapPaint);
                                }
                            } else {
                                canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect((int) this.smallImageOffsetX, imageOffsetY, (int) (this.smallImageOffsetX + this.smallImageWidth), ((int) this.smallImageHeight) + imageOffsetY), bitmapPaint);
                            }
                        }
                        if (drowType == 0) {
                            MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY + cellOffsetY, this.nameLabelTypeface, " " + nameString2, this.nameLabelFontSize, -1, this.nameLabelStroke1Width, -1, this.nameLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                            MyDraw.drawStrokeText(canvas, this.priceTitleLabelOffsetX, this.priceTitleLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, this.priceTitleLabelString, this.priceTitleLabelFontSize, FluctConstants.FRAME_ALPHA_COLOR, this.priceTitleLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.priceLabelOffsetX, this.priceLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, priceString, this.priceTitleLabelFontSize, -1, this.priceLabelStroke1Width, -1, this.priceLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                            MyDraw.drawStrokeText(canvas, this.countTitleLabelOffsetX, this.countTitleLabelOffsetY + cellOffsetY, this.countTitleLabelTypeface, this.countTitleLabelString, this.countTitleLabelFontSize, FluctConstants.FRAME_ALPHA_COLOR, this.countTitleLabelStroke1Width, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.countLabelOffsetX, this.countLabelOffsetY + cellOffsetY, this.countTitleLabelTypeface, countString, this.countTitleLabelFontSize, -1, this.countLabelStroke1Width, -1, this.countLabelStroke2Width, FluctConstants.FRAME_ALPHA_COLOR);
                            if (this.countsArray != null && i < this.countsArray.length && this.countsArray[i][1] > 0) {
                                String selectedCountLabelString = new StringBuilder().append(this.countsArray[i][0]).toString();
                                MyDraw.drawStrokeRect(canvas, this.selectedCountBackRectOffsetX, this.selectedCountBackRectOffsetY + cellOffsetY, this.selectedCountBackRectWidth, this.selectedCountBackRectHeight, this.selectedCountBackRectColor0, this.selectedCountBackRectStrokeWidth1, this.selectedCountBackRectColor1, this.selectedCountBackRectStrokeWidth2, this.selectedCountBackRectColor2, this.selectedCountBackRectStrokeWidth3, this.selectedCountBackRectColor3, this.selectedCountBackRectRadius);
                                Paint newPaint = new Paint(257);
                                newPaint.setTypeface(this.selectedCountLabelTypeface);
                                newPaint.setTextSize(this.selectedCountLabelFontSize);
                                this.selectedCountLabelOffsetX = this.selectedCountBackRectOffsetX + ((this.selectedCountBackRectWidth - newPaint.measureText(selectedCountLabelString)) / 2.0f);
                                MyDraw.drawStrokeText(canvas, this.selectedCountLabelOffsetX, this.selectedCountLabelOffsetY + cellOffsetY, this.selectedCountLabelTypeface, selectedCountLabelString, this.selectedCountLabelFontSize, this.selectedCountLabelColor0, this.selectedCountLabelStroke1Width, this.selectedCountLabelColor1, this.selectedCountLabelStroke2Width, this.selectedCountLabelColor2);
                            }
                        } else if (drowType == -1) {
                            MyDraw.drawStrokeText(canvas, this.nameLabelOffsetX, this.nameLabelOffsetY + cellOffsetY, this.nameLabelTypeface, " " + nameString2, this.nameLabelFontSize, -6710887, this.nameLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.priceTitleLabelOffsetX, this.priceTitleLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, this.priceTitleLabelString, this.priceTitleLabelFontSize, -6710887, this.priceTitleLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.priceLabelOffsetX, this.priceLabelOffsetY + cellOffsetY, this.priceTitleLabelTypeface, " -", this.priceTitleLabelFontSize, -6710887, this.priceLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.countTitleLabelOffsetX, this.countTitleLabelOffsetY + cellOffsetY, this.countTitleLabelTypeface, this.countTitleLabelString, this.countTitleLabelFontSize, -6710887, this.countTitleLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                            MyDraw.drawStrokeText(canvas, this.countLabelOffsetX, this.countLabelOffsetY + cellOffsetY, this.countTitleLabelTypeface, " -", this.countTitleLabelFontSize, -6710887, this.countLabelStroke1Width, -6710887, BitmapDescriptorFactory.HUE_RED, 0);
                        }
                        if (this.buttonsStatusArray != null && i < this.buttonsStatusArray.length) {
                            if (this.buttonsStatusArray[i][0] == 0) {
                                MyDraw.drawStrokeRect(canvas, this.button0OffsetX, this.buttonOffsetY + cellOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius);
                                MyDraw.drawStrokeText(canvas, this.button0TitleLabelOffsetX, this.buttonTitleLabelOffsetY + cellOffsetY, this.buttonTitleLabelTypeface, this.button0TitleLabelString, this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
                                if (i == this.selectedButtonIndex0 && this.selectedButtonIndex1 == 0) {
                                    MyDraw.drawStrokeRect(canvas, this.button0OffsetX, this.buttonOffsetY + cellOffsetY, this.buttonWidth, this.buttonHeight, -1140850689, this.buttonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
                                }
                            }
                            if (this.buttonsStatusArray[i][1] == 0) {
                                MyDraw.drawStrokeRect(canvas, this.button1OffsetX, this.buttonOffsetY + cellOffsetY, this.buttonWidth, this.buttonHeight, this.buttonColor0, this.buttonStrokeWidth1, this.buttonColor1, this.buttonStrokeWidth2, this.buttonColor2, this.buttonStrokeWidth3, this.buttonColor3, this.buttonRadius);
                                MyDraw.drawStrokeText(canvas, this.button1TitleLabelOffsetX, this.buttonTitleLabelOffsetY + cellOffsetY, this.buttonTitleLabelTypeface, this.button1TitleLabelString, this.buttonTitleLabelFontSize, this.buttonTitleLabelColor0, this.buttonTitleLabelStroke1Width, this.buttonTitleLabelColor1, this.buttonTitleLabelStroke2Width, this.buttonTitleLabelColor2);
                                if (i == this.selectedButtonIndex0 && this.selectedButtonIndex1 == 1) {
                                    MyDraw.drawStrokeRect(canvas, this.button1OffsetX, this.buttonOffsetY + cellOffsetY, this.buttonWidth, this.buttonHeight, -1140850689, this.buttonStrokeWidth1, 0, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, this.buttonRadius);
                                }
                            }
                        }
                        MyDraw.drawOneRect(canvas, this.offsetX, ((this.offsetY + (this.cellSpaceY * i)) - this.cellSpaceLineHeight) - drawOffsetScrollY, this.finalWidth, this.cellSpaceLineHeight, 570425344, BitmapDescriptorFactory.HUE_RED);
                    }
                } else {
                    MyDraw.drawOneRect(canvas, this.offsetX, ((this.offsetY + (this.cellSpaceY * i)) - this.cellSpaceLineHeight) - drawOffsetScrollY, this.finalWidth, this.cellSpaceLineHeight, 570425344, BitmapDescriptorFactory.HUE_RED);
                }
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
        if (this.farmUnit != null) {
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
            if (this.farmUnit != null) {
                this.farmUnit.changeListViewFastScrollDragViewOffset(offsetY, contentY);
            }
        }
    }

    public void onDestroy() {
        this.myDraw = null;
        this.farmUnit = null;
        this.appDelegate = null;
    }
}
