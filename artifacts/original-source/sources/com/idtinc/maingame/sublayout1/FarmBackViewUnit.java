package com.idtinc.maingame.sublayout1;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.custom.MyDraw;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmBackViewUnit {
    private float CHARACTERDISPLAYVIEW_OFFSET_Y;
    private float CHARACTERUNITVIEW_OFFSET_X;
    private float CHARACTERUNITVIEW_OFFSET_X_MAX;
    private float CHARACTERUNITVIEW_OFFSET_X_MIN;
    private float CHARACTERUNITVIEW_OFFSET_Y;
    private float CHARACTERUNITVIEW_SIZE_X;
    private float CHARACTERUNITVIEW_SIZE_Y;
    private float CHARACTERUNITVIEW_SPACE_X;
    private short CHARACTERUNITVIEW_SPACE_X_RAND_RANGE;
    private float CHARACTERUNITVIEW_SPACE_Y;
    private short CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE;
    private AppDelegate appDelegate;
    private float backGroundBitmapDrawWidth;
    public short backGroundIndex;
    private float backGroundOffsetX;
    private float backGroundOffsetXMax;
    private float backGroundOffsetXMin;
    private short buttonClickCnt;
    private float characterOffsetX;
    private float farmFrontViewUnitHeight;
    private float farmFrontViewUnitOffsetY;
    public float farmHouseButtonHeight;
    public float farmHouseButtonOffsetX;
    public float farmHouseButtonOffsetY;
    private short farmHouseButtonStatus;
    public float farmHouseButtonWidth;
    private FarmUnit farmUnit;
    private float finalHeight;
    private float finalWidth;
    public float jinjaHouseButtonHeight;
    public float jinjaHouseButtonOffsetX;
    public float jinjaHouseButtonOffsetY;
    private short jinjaHouseButtonStatus;
    public float jinjaHouseButtonWidth;
    public float monsterVillageButtonHeight;
    public float monsterVillageButtonOffsetX;
    public float monsterVillageButtonOffsetY;
    public short monsterVillageButtonStatus;
    public float monsterVillageButtonWidth;
    private MyDraw myDraw;
    public ArrayList<CharacterDisplayUnit> nowCharacterDisplayViewsArrayList;
    private short refreshCount;
    public float timeDoorButtonHeight;
    public float timeDoorButtonOffsetX;
    public float timeDoorButtonOffsetY;
    public short timeDoorButtonStatus;
    public float timeDoorButtonWidth;
    private short touchButtonIndex;
    private float zoomRate;
    private float preScrollX = -9999.0f;
    private Bitmap backGroundBitmap = null;
    private Bitmap jinjaHouseButtonBitmap0 = null;
    private Bitmap jinjaHouseButtonBitmap1 = null;
    private Bitmap farmHouseButtonBitmap0 = null;
    private Bitmap farmHouseButtonBitmap1 = null;
    private Bitmap timeDoorButtonBitmap0 = null;
    private Bitmap timeDoorButtonBitmap1 = null;
    private Bitmap monsterVillageButtonBitmap0 = null;
    private Bitmap monsterVillageButtonBitmap1 = null;

    public FarmBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, FarmUnit _farmUnit, AppDelegate _appDelegate) {
        this.CHARACTERDISPLAYVIEW_OFFSET_Y = 185.0f;
        this.CHARACTERUNITVIEW_OFFSET_X = -10.0f;
        this.CHARACTERUNITVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.CHARACTERUNITVIEW_SPACE_X = 44.0f;
        this.CHARACTERUNITVIEW_SPACE_Y = 20.0f;
        this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE = (short) 22;
        this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE = (short) 20;
        this.CHARACTERUNITVIEW_SIZE_X = 32.0f;
        this.CHARACTERUNITVIEW_SIZE_Y = 32.0f;
        this.CHARACTERUNITVIEW_OFFSET_X_MIN = -60.0f;
        this.CHARACTERUNITVIEW_OFFSET_X_MAX = 320.0f;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.refreshCount = (short) 0;
        this.backGroundIndex = (short) -1;
        this.backGroundOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backGroundOffsetXMin = BitmapDescriptorFactory.HUE_RED;
        this.backGroundOffsetXMax = BitmapDescriptorFactory.HUE_RED;
        this.backGroundBitmapDrawWidth = BitmapDescriptorFactory.HUE_RED;
        this.characterOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.farmFrontViewUnitOffsetY = 465.0f;
        this.farmFrontViewUnitHeight = 55.0f;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.farmHouseButtonStatus = (short) -1;
        this.jinjaHouseButtonStatus = (short) -1;
        this.timeDoorButtonStatus = (short) -1;
        this.monsterVillageButtonStatus = (short) -1;
        this.monsterVillageButtonOffsetX = 2.0f;
        this.monsterVillageButtonOffsetY = 136.0f;
        this.monsterVillageButtonWidth = 90.0f;
        this.monsterVillageButtonHeight = 90.0f;
        this.farmHouseButtonOffsetX = 2.0f;
        this.farmHouseButtonOffsetY = 136.0f;
        this.farmHouseButtonWidth = 90.0f;
        this.farmHouseButtonHeight = 90.0f;
        this.jinjaHouseButtonOffsetX = 235.0f;
        this.jinjaHouseButtonOffsetY = 150.0f;
        this.jinjaHouseButtonWidth = 65.0f;
        this.jinjaHouseButtonHeight = 65.0f;
        this.timeDoorButtonOffsetX = 130.0f;
        this.timeDoorButtonOffsetY = 143.0f;
        this.timeDoorButtonWidth = 65.0f;
        this.timeDoorButtonHeight = 65.0f;
        this.nowCharacterDisplayViewsArrayList = null;
        this.appDelegate = _appDelegate;
        this.farmUnit = _farmUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.refreshCount = (short) 0;
        this.backGroundIndex = (short) -1;
        this.backGroundOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backGroundOffsetXMin = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backGroundOffsetXMax = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.backGroundBitmapDrawWidth = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.characterOffsetX = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.CHARACTERDISPLAYVIEW_OFFSET_Y = 187.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.CHARACTERUNITVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.CHARACTERUNITVIEW_SPACE_X = 37.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_SPACE_Y = 26.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE = (short) (9.0f * this.zoomRate);
        this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE = (short) (10.0f * this.zoomRate);
        this.CHARACTERUNITVIEW_SIZE_X = 48.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_SIZE_Y = 48.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_OFFSET_X_MIN = (-60.0f) * this.zoomRate;
        this.CHARACTERUNITVIEW_OFFSET_X_MAX = 320.0f * this.zoomRate;
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
        this.farmHouseButtonStatus = (short) 0;
        this.jinjaHouseButtonStatus = (short) 0;
        this.timeDoorButtonStatus = (short) -1;
        this.monsterVillageButtonStatus = (short) 0;
        this.farmFrontViewUnitOffsetY = 465.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.farmFrontViewUnitOffsetY -= 88.0f * this.zoomRate;
        }
        this.farmFrontViewUnitHeight = this.farmFrontViewUnitOffsetY + (55.0f * this.zoomRate);
        this.farmHouseButtonOffsetX = 2.0f * this.zoomRate;
        this.farmHouseButtonOffsetY = 136.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.farmHouseButtonOffsetY -= this.appDelegate.offset44;
        }
        this.farmHouseButtonWidth = 90.0f * this.zoomRate;
        this.farmHouseButtonHeight = 90.0f * this.zoomRate;
        this.jinjaHouseButtonOffsetX = 355.0f * this.zoomRate;
        this.jinjaHouseButtonOffsetY = 148.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.jinjaHouseButtonOffsetY -= this.appDelegate.offset44;
        }
        this.jinjaHouseButtonWidth = 65.0f * this.zoomRate;
        this.jinjaHouseButtonHeight = 65.0f * this.zoomRate;
        this.timeDoorButtonOffsetX = 130.0f * this.zoomRate;
        this.timeDoorButtonOffsetY = 143.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.timeDoorButtonOffsetY -= this.appDelegate.offset44;
        }
        this.timeDoorButtonWidth = 65.0f * this.zoomRate;
        this.timeDoorButtonHeight = 65.0f * this.zoomRate;
        this.monsterVillageButtonOffsetX = 235.0f * this.zoomRate;
        this.monsterVillageButtonOffsetY = 146.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.monsterVillageButtonOffsetY -= this.appDelegate.offset44;
        }
        this.monsterVillageButtonWidth = 65.0f * this.zoomRate;
        this.monsterVillageButtonHeight = 65.0f * this.zoomRate;
        short positionIndex = 0;
        this.nowCharacterDisplayViewsArrayList = new ArrayList<>();
        CharacterDisplayUnit character_0_68_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_68_DisplayUnit.init(500.0f * this.zoomRate, 106.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_68_DisplayUnit.init(500.0f * this.zoomRate, 150.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_68_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_68_DisplayUnit.frameOffsetX, character_0_68_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_68_DisplayUnit.shadowViewOriginY = (10.0f * this.zoomRate) + (character_0_68_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_0_68_DisplayUnit);
        CharacterDisplayUnit character_1_36_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_1_36_DisplayUnit.init(540.0f * this.zoomRate, 106.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_1_36_DisplayUnit.init(540.0f * this.zoomRate, 150.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_1_36_DisplayUnit.setWithEggID((short) -1, (short) -1, character_1_36_DisplayUnit.frameOffsetX, character_1_36_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_1_36_DisplayUnit.shadowViewOriginY = (10.0f * this.zoomRate) + (character_1_36_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_1_36_DisplayUnit);
        int characterDisplayViewTotal = this.appDelegate.isRetina4 ? 218 : 218 - 44;
        for (int i = 0; i < characterDisplayViewTotal; i++) {
            positionIndex = positionIndex == 0 ? (short) (positionIndex + 2) : positionIndex;
            short offsetIndexX = (short) (positionIndex % 22);
            short offsetIndexY = (short) (positionIndex / 22);
            float offsetYFloat = this.CHARACTERUNITVIEW_SPACE_Y * offsetIndexY;
            float charOffsetX = this.CHARACTERUNITVIEW_OFFSET_X + (this.CHARACTERUNITVIEW_SPACE_X * offsetIndexX);
            float charOffsetY = this.CHARACTERDISPLAYVIEW_OFFSET_Y + this.CHARACTERUNITVIEW_OFFSET_Y + offsetYFloat;
            if (!this.appDelegate.isRetina4) {
                charOffsetY -= 44.0f * this.zoomRate;
            }
            CharacterDisplayUnit characterDisplayUnit = new CharacterDisplayUnit();
            characterDisplayUnit.init(charOffsetX, charOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, this.zoomRate);
            characterDisplayUnit.setWithEggID((short) -1, (short) -1, charOffsetX, charOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
            this.nowCharacterDisplayViewsArrayList.add(characterDisplayUnit);
            positionIndex = (short) (positionIndex + 1);
        }
        CharacterDisplayUnit character_0_104_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_104_DisplayUnit.init(140.0f * this.zoomRate, 56.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_104_DisplayUnit.init(140.0f * this.zoomRate, 100.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_104_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_104_DisplayUnit.frameOffsetX, character_0_104_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_104_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_104_DisplayUnit);
        CharacterDisplayUnit character_0_32_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_32_DisplayUnit.init(18.0f * this.zoomRate, 71.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_32_DisplayUnit.init(18.0f * this.zoomRate, 115.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_32_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_32_DisplayUnit.frameOffsetX, character_0_32_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_32_DisplayUnit.shadowViewOriginY = (15.0f * this.zoomRate) + (character_0_32_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_0_32_DisplayUnit);
        CharacterDisplayUnit character_0_52_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_52_DisplayUnit.init(267.0f * this.zoomRate, 30.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_52_DisplayUnit.init(267.0f * this.zoomRate, 74.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_52_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_52_DisplayUnit.frameOffsetX, character_0_52_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_52_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_52_DisplayUnit);
        CharacterDisplayUnit character_0_64_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_64_DisplayUnit.init(660.0f * this.zoomRate, 26.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_64_DisplayUnit.init(660.0f * this.zoomRate, 70.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_64_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_64_DisplayUnit.frameOffsetX, character_0_64_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_64_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_64_DisplayUnit);
        CharacterDisplayUnit character_0_80_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_80_DisplayUnit.init(450.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_80_DisplayUnit.init(450.0f * this.zoomRate, 90.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_80_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_80_DisplayUnit.frameOffsetX, character_0_80_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_80_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_80_DisplayUnit);
        CharacterDisplayUnit character_0_70_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_0_70_DisplayUnit.init(520.0f * this.zoomRate, 76.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_0_70_DisplayUnit.init(520.0f * this.zoomRate, 120.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_0_70_DisplayUnit.setWithEggID((short) -1, (short) -1, character_0_70_DisplayUnit.frameOffsetX, character_0_70_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_70_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_70_DisplayUnit);
        CharacterDisplayUnit character_1_38_DisplayUnit = new CharacterDisplayUnit();
        if (!this.appDelegate.isRetina4) {
            character_1_38_DisplayUnit.init(550.0f * this.zoomRate, 76.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        } else {
            character_1_38_DisplayUnit.init(550.0f * this.zoomRate, 120.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        }
        character_1_38_DisplayUnit.setWithEggID((short) -1, (short) -1, character_1_38_DisplayUnit.frameOffsetX, character_1_38_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_1_38_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_1_38_DisplayUnit);
        this.myDraw = new MyDraw();
    }

    /* JADX WARN: Removed duplicated region for block: B:287:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x018f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void refreshCharacterDisplayViewsArray() {
        /*
            Method dump skipped, instructions count: 1568
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idtinc.maingame.sublayout1.FarmBackViewUnit.refreshCharacterDisplayViewsArray():void");
    }

    public void clearBackgroundBitmap() {
        this.backGroundIndex = (short) -1;
        this.backGroundBitmapDrawWidth = BitmapDescriptorFactory.HUE_RED;
        if (this.backGroundBitmap != null) {
            if (!this.backGroundBitmap.isRecycled()) {
                this.backGroundBitmap.recycle();
            }
            this.backGroundBitmap = null;
        }
        if (this.farmHouseButtonBitmap0 != null) {
            if (!this.farmHouseButtonBitmap0.isRecycled()) {
                this.farmHouseButtonBitmap0.recycle();
            }
            this.farmHouseButtonBitmap0 = null;
        }
        if (this.farmHouseButtonBitmap1 != null) {
            if (!this.farmHouseButtonBitmap1.isRecycled()) {
                this.farmHouseButtonBitmap1.recycle();
            }
            this.farmHouseButtonBitmap1 = null;
        }
        if (this.jinjaHouseButtonBitmap0 != null) {
            if (!this.jinjaHouseButtonBitmap0.isRecycled()) {
                this.jinjaHouseButtonBitmap0.recycle();
            }
            this.jinjaHouseButtonBitmap0 = null;
        }
        if (this.jinjaHouseButtonBitmap1 != null) {
            if (!this.jinjaHouseButtonBitmap1.isRecycled()) {
                this.jinjaHouseButtonBitmap1.recycle();
            }
            this.jinjaHouseButtonBitmap1 = null;
        }
        if (this.timeDoorButtonBitmap0 != null) {
            if (!this.timeDoorButtonBitmap0.isRecycled()) {
                this.timeDoorButtonBitmap0.recycle();
            }
            this.timeDoorButtonBitmap0 = null;
        }
        if (this.timeDoorButtonBitmap1 != null) {
            if (!this.timeDoorButtonBitmap1.isRecycled()) {
                this.timeDoorButtonBitmap1.recycle();
            }
            this.timeDoorButtonBitmap1 = null;
        }
        if (this.monsterVillageButtonBitmap0 != null) {
            if (!this.monsterVillageButtonBitmap0.isRecycled()) {
                this.monsterVillageButtonBitmap0.recycle();
            }
            this.monsterVillageButtonBitmap0 = null;
        }
        if (this.monsterVillageButtonBitmap1 != null) {
            if (!this.monsterVillageButtonBitmap1.isRecycled()) {
                this.monsterVillageButtonBitmap1.recycle();
            }
            this.monsterVillageButtonBitmap1 = null;
        }
    }

    public void refreshBackgroundBitmap() throws IOException {
        short newBackGroundIndex;
        if (this.appDelegate != null && (newBackGroundIndex = this.appDelegate.getNowTimeIndex()) != this.backGroundIndex) {
            clearBackgroundBitmap();
            this.backGroundIndex = newBackGroundIndex;
            if (this.backGroundIndex >= 0) {
                AssetManager asm = this.appDelegate.getAssets();
                BitmapFactory.Options opt2 = new BitmapFactory.Options();
                opt2.inJustDecodeBounds = false;
                opt2.inSampleSize = 1;
                opt2.inPreferredConfig = Bitmap.Config.RGB_565;
                opt2.inPurgeable = true;
                opt2.inInputShareable = true;
                try {
                    InputStream inputStream = asm.open("png/Tool/Tool0/tool_0_1_" + ((int) this.backGroundIndex) + "_0.jpg");
                    this.backGroundBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);
                    this.backGroundOffsetXMax = this.backGroundBitmap.getWidth() - 640.0f;
                    this.backGroundBitmapDrawWidth = 640.0f;
                    inputStream.close();
                } catch (IOException e) {
                }
                try {
                    InputStream inputStream2 = asm.open("png/Farm/farm_house_" + ((int) this.backGroundIndex) + "_0.png");
                    this.farmHouseButtonBitmap0 = BitmapFactory.decodeStream(inputStream2, null, opt2);
                    inputStream2.close();
                } catch (IOException e2) {
                }
                try {
                    InputStream inputStream3 = asm.open("png/Farm/farm_house_" + ((int) this.backGroundIndex) + "_1.png");
                    this.farmHouseButtonBitmap1 = BitmapFactory.decodeStream(inputStream3, null, opt2);
                    inputStream3.close();
                } catch (IOException e3) {
                }
                try {
                    InputStream inputStream4 = asm.open("png/Farm/jinja_house_" + ((int) this.backGroundIndex) + "_0.png");
                    this.jinjaHouseButtonBitmap0 = BitmapFactory.decodeStream(inputStream4, null, opt2);
                    inputStream4.close();
                } catch (IOException e4) {
                }
                try {
                    InputStream inputStream5 = asm.open("png/Farm/jinja_house_" + ((int) this.backGroundIndex) + "_1.png");
                    this.jinjaHouseButtonBitmap1 = BitmapFactory.decodeStream(inputStream5, null, opt2);
                    inputStream5.close();
                } catch (IOException e5) {
                }
                try {
                    InputStream inputStream6 = asm.open("png/Farm/time_door_" + ((int) this.backGroundIndex) + "_0.png");
                    this.timeDoorButtonBitmap0 = BitmapFactory.decodeStream(inputStream6, null, opt2);
                    inputStream6.close();
                } catch (IOException e6) {
                }
                try {
                    InputStream inputStream7 = asm.open("png/Farm/time_door_" + ((int) this.backGroundIndex) + "_1.png");
                    this.timeDoorButtonBitmap1 = BitmapFactory.decodeStream(inputStream7, null, opt2);
                    inputStream7.close();
                } catch (IOException e7) {
                }
                try {
                    InputStream inputStream8 = asm.open("png/Farm/monster_village_" + ((int) this.backGroundIndex) + "_0.png");
                    this.monsterVillageButtonBitmap0 = BitmapFactory.decodeStream(inputStream8, null, opt2);
                    inputStream8.close();
                } catch (IOException e8) {
                }
                try {
                    InputStream inputStream9 = asm.open("png/Farm/monster_village_" + ((int) this.backGroundIndex) + "_1.png");
                    this.monsterVillageButtonBitmap1 = BitmapFactory.decodeStream(inputStream9, null, opt2);
                    inputStream9.close();
                } catch (IOException e9) {
                }
                refreshCharacterDisplayViewsArray();
            }
        }
    }

    public void doRefreshLoop() throws IOException {
        refreshBackgroundBitmap();
        this.refreshCount = (short) 100;
    }

    public void doLoop() throws IOException {
        this.refreshCount = (short) (this.refreshCount - 1);
        if (this.refreshCount <= 0) {
            doRefreshLoop();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmBackView", "onTouchEvent");
        if (event.getAction() == 0) {
            this.preScrollX = event.getX();
            if (this.farmHouseButtonStatus == 0) {
                float farmHouseButtonTouchOffsetX = this.farmHouseButtonOffsetX + this.characterOffsetX;
                if (event.getY() > this.farmHouseButtonOffsetY && event.getY() < this.farmHouseButtonOffsetY + this.farmHouseButtonHeight && event.getX() > farmHouseButtonTouchOffsetX && event.getX() < this.farmHouseButtonWidth + farmHouseButtonTouchOffsetX) {
                    this.touchButtonIndex = (short) 0;
                    this.buttonClickCnt = (short) 3;
                    Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                    Log.d("FarmBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmBackViewUnit.1
                        @Override // java.lang.Runnable
                        public void run() {
                            FarmBackViewUnit.this.doClick();
                        }
                    }, 200L);
                    return true;
                }
            }
            if (this.jinjaHouseButtonStatus == 0) {
                float jinjaHouseButtonTouchOffsetX = this.jinjaHouseButtonOffsetX + this.characterOffsetX;
                if (event.getY() > this.jinjaHouseButtonOffsetY && event.getY() < this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight && event.getX() > jinjaHouseButtonTouchOffsetX && event.getX() < this.timeDoorButtonWidth + jinjaHouseButtonTouchOffsetX) {
                    this.touchButtonIndex = (short) 1;
                    this.buttonClickCnt = (short) 3;
                    Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                    Log.d("FarmBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmBackViewUnit.2
                        @Override // java.lang.Runnable
                        public void run() {
                            FarmBackViewUnit.this.doClick();
                        }
                    }, 200L);
                    return true;
                }
            }
            if (this.timeDoorButtonStatus == 0) {
                float timeDoorButtonTouchOffsetX = this.timeDoorButtonOffsetX + this.characterOffsetX;
                if (event.getY() > this.timeDoorButtonOffsetY && event.getY() < this.timeDoorButtonOffsetY + this.timeDoorButtonHeight && event.getX() > timeDoorButtonTouchOffsetX && event.getX() < this.timeDoorButtonWidth + timeDoorButtonTouchOffsetX) {
                    this.touchButtonIndex = (short) 2;
                    this.buttonClickCnt = (short) 3;
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmBackViewUnit.3
                        @Override // java.lang.Runnable
                        public void run() {
                            FarmBackViewUnit.this.doClick();
                        }
                    }, 200L);
                    return true;
                }
            }
            if (this.monsterVillageButtonStatus == 0) {
                float monsterVillageButtonTouchOffsetX = this.monsterVillageButtonOffsetX + this.characterOffsetX;
                if (event.getY() > this.monsterVillageButtonOffsetY && event.getY() < this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight && event.getX() > monsterVillageButtonTouchOffsetX && event.getX() < this.monsterVillageButtonWidth + monsterVillageButtonTouchOffsetX) {
                    this.touchButtonIndex = (short) 3;
                    this.buttonClickCnt = (short) 3;
                    Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
                    Log.d("FarmBackView", "touchButtonIndex:" + ((int) this.touchButtonIndex));
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmBackViewUnit.4
                        @Override // java.lang.Runnable
                        public void run() {
                            FarmBackViewUnit.this.doClick();
                        }
                    }, 200L);
                    return true;
                }
            }
        } else if (event.getAction() == 2) {
            float addOffScrollX = this.preScrollX - event.getX();
            this.backGroundOffsetX += addOffScrollX;
            if (this.backGroundOffsetX < this.backGroundOffsetXMin) {
                this.backGroundOffsetX = this.backGroundOffsetXMin;
            } else if (this.backGroundOffsetX > this.backGroundOffsetXMax) {
                this.backGroundOffsetX = this.backGroundOffsetXMax;
            }
            if (this.backGroundBitmapDrawWidth > BitmapDescriptorFactory.HUE_RED) {
                this.characterOffsetX = (((-1.0f) * this.backGroundOffsetX) * this.finalWidth) / this.backGroundBitmapDrawWidth;
            } else {
                this.characterOffsetX = BitmapDescriptorFactory.HUE_RED;
            }
            Log.d("FarmListScrollLayout", "backGroundOffsetXMax=" + this.backGroundOffsetXMax);
            Log.d("FarmListScrollLayout", "backGroundOffsetX=" + this.backGroundOffsetX);
            this.preScrollX = event.getX();
        }
        return false;
    }

    public void doClick() {
        if (this.touchButtonIndex == 0) {
            this.farmUnit.openListLayout();
            this.appDelegate.doSoundPoolPlay(1);
        } else if (this.touchButtonIndex == 1) {
            this.farmUnit.goToCKKB();
            this.appDelegate.doSoundPoolPlay(1);
        } else if (this.touchButtonIndex == 2) {
            this.farmUnit.displayCheckReceiveChicksFromCKAlert();
        } else if (this.touchButtonIndex == 3) {
            this.farmUnit.goToCKMV();
            this.appDelegate.doSoundPoolPlay(1);
        }
        this.touchButtonIndex = (short) -1;
        this.buttonClickCnt = (short) -1;
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.appDelegate != null) {
            float drawBackGroundOffsetX = this.backGroundOffsetX;
            float drawCharacterOffsetX = this.characterOffsetX;
            float drawButtonOffsetX = this.characterOffsetX;
            int drawBackGroundBitmapDrawWidth = (int) (this.backGroundBitmapDrawWidth + drawBackGroundOffsetX);
            if (this.backGroundBitmap != null) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                if (!this.appDelegate.isRetina4) {
                    canvas.drawBitmap(this.backGroundBitmap, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
                } else {
                    canvas.drawBitmap(this.backGroundBitmap, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
                }
            }
            if (this.farmHouseButtonStatus == 0) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                float farmHouseButtonDrawOffsetX = this.farmHouseButtonOffsetX + drawButtonOffsetX;
                if (this.touchButtonIndex == 0) {
                    if (this.farmHouseButtonBitmap1 != null) {
                        canvas.drawBitmap(this.farmHouseButtonBitmap1, new Rect(0, 0, this.farmHouseButtonBitmap1.getWidth(), this.farmHouseButtonBitmap1.getHeight()), new Rect((int) farmHouseButtonDrawOffsetX, (int) this.farmHouseButtonOffsetY, (int) (this.farmHouseButtonWidth + farmHouseButtonDrawOffsetX), (int) (this.farmHouseButtonOffsetY + this.farmHouseButtonHeight)), bitmapPaint);
                    }
                } else if (this.farmHouseButtonBitmap0 != null) {
                    canvas.drawBitmap(this.farmHouseButtonBitmap0, new Rect(0, 0, this.farmHouseButtonBitmap0.getWidth(), this.farmHouseButtonBitmap0.getHeight()), new Rect((int) farmHouseButtonDrawOffsetX, (int) this.farmHouseButtonOffsetY, (int) (this.farmHouseButtonWidth + farmHouseButtonDrawOffsetX), (int) (this.farmHouseButtonOffsetY + this.farmHouseButtonHeight)), bitmapPaint);
                }
            }
            if (this.jinjaHouseButtonStatus == 0) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                float jinjaHouseButtonDrawOffsetX = this.jinjaHouseButtonOffsetX + drawButtonOffsetX;
                if (this.touchButtonIndex == 1) {
                    if (this.jinjaHouseButtonBitmap1 != null) {
                        canvas.drawBitmap(this.jinjaHouseButtonBitmap1, new Rect(0, 0, this.jinjaHouseButtonBitmap1.getWidth(), this.jinjaHouseButtonBitmap1.getHeight()), new Rect((int) jinjaHouseButtonDrawOffsetX, (int) this.jinjaHouseButtonOffsetY, (int) (this.jinjaHouseButtonWidth + jinjaHouseButtonDrawOffsetX), (int) (this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight)), bitmapPaint);
                    }
                } else if (this.jinjaHouseButtonBitmap0 != null) {
                    canvas.drawBitmap(this.jinjaHouseButtonBitmap0, new Rect(0, 0, this.jinjaHouseButtonBitmap0.getWidth(), this.jinjaHouseButtonBitmap0.getHeight()), new Rect((int) jinjaHouseButtonDrawOffsetX, (int) this.jinjaHouseButtonOffsetY, (int) (this.jinjaHouseButtonWidth + jinjaHouseButtonDrawOffsetX), (int) (this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight)), bitmapPaint);
                }
            }
            if (this.timeDoorButtonStatus == 0) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                float timeDoorButtonDrawOffsetX = this.timeDoorButtonOffsetX + drawButtonOffsetX;
                if (this.touchButtonIndex == 2) {
                    if (this.timeDoorButtonBitmap1 != null) {
                        canvas.drawBitmap(this.timeDoorButtonBitmap1, new Rect(0, 0, this.timeDoorButtonBitmap1.getWidth(), this.timeDoorButtonBitmap1.getHeight()), new Rect((int) timeDoorButtonDrawOffsetX, (int) this.timeDoorButtonOffsetY, (int) (this.timeDoorButtonWidth + timeDoorButtonDrawOffsetX), (int) (this.timeDoorButtonOffsetY + this.timeDoorButtonHeight)), bitmapPaint);
                    }
                } else if (this.timeDoorButtonBitmap0 != null) {
                    canvas.drawBitmap(this.timeDoorButtonBitmap0, new Rect(0, 0, this.timeDoorButtonBitmap0.getWidth(), this.timeDoorButtonBitmap0.getHeight()), new Rect((int) timeDoorButtonDrawOffsetX, (int) this.timeDoorButtonOffsetY, (int) (this.timeDoorButtonWidth + timeDoorButtonDrawOffsetX), (int) (this.timeDoorButtonOffsetY + this.timeDoorButtonHeight)), bitmapPaint);
                }
            }
            if (this.monsterVillageButtonStatus == 0) {
                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                float monsterVillageButtonDrawOffsetX = this.monsterVillageButtonOffsetX + drawButtonOffsetX;
                if (this.touchButtonIndex == 3) {
                    if (this.monsterVillageButtonBitmap1 != null) {
                        canvas.drawBitmap(this.monsterVillageButtonBitmap1, new Rect(0, 0, this.monsterVillageButtonBitmap1.getWidth(), this.monsterVillageButtonBitmap1.getHeight()), new Rect((int) monsterVillageButtonDrawOffsetX, (int) this.monsterVillageButtonOffsetY, (int) (this.monsterVillageButtonWidth + monsterVillageButtonDrawOffsetX), (int) (this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight)), bitmapPaint);
                    }
                } else if (this.monsterVillageButtonBitmap0 != null) {
                    canvas.drawBitmap(this.monsterVillageButtonBitmap0, new Rect(0, 0, this.monsterVillageButtonBitmap0.getWidth(), this.monsterVillageButtonBitmap0.getHeight()), new Rect((int) monsterVillageButtonDrawOffsetX, (int) this.monsterVillageButtonOffsetY, (int) (this.monsterVillageButtonWidth + monsterVillageButtonDrawOffsetX), (int) (this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight)), bitmapPaint);
                }
            }
            if (this.nowCharacterDisplayViewsArrayList != null) {
                for (int i = 0; i < this.nowCharacterDisplayViewsArrayList.size(); i++) {
                    CharacterDisplayUnit characterDisplayUnit = this.nowCharacterDisplayViewsArrayList.get(i);
                    if (characterDisplayUnit != null) {
                        float offsetXFloat = drawCharacterOffsetX + characterDisplayUnit.frameOffsetX;
                        if (offsetXFloat >= this.CHARACTERUNITVIEW_OFFSET_X_MIN && offsetXFloat < this.CHARACTERUNITVIEW_OFFSET_X_MAX && characterDisplayUnit.eggID >= 0) {
                            characterDisplayUnit.animeLoop();
                            Bitmap shadowImageBitmap = null;
                            if (characterDisplayUnit.eggID == 0) {
                                if (characterDisplayUnit.characterID == 32) {
                                    shadowImageBitmap = this.appDelegate.shadow_1_Bitmap;
                                } else {
                                    shadowImageBitmap = this.appDelegate.shadow_0_Bitmap;
                                }
                            } else if (characterDisplayUnit.eggID == 1) {
                                shadowImageBitmap = this.appDelegate.shadow_0_Bitmap;
                            }
                            if (shadowImageBitmap != null) {
                                int imgOffsetX = (int) (characterDisplayUnit.frameOffsetX + drawCharacterOffsetX + characterDisplayUnit.shadowViewOriginX);
                                int imgOffsetY = (int) (characterDisplayUnit.frameOffsetY + characterDisplayUnit.shadowViewOriginY);
                                bitmapPaint.setAlpha(178);
                                canvas.drawBitmap(shadowImageBitmap, new Rect(0, 0, shadowImageBitmap.getWidth(), shadowImageBitmap.getHeight()), new Rect(imgOffsetX, imgOffsetY, (int) (imgOffsetX + characterDisplayUnit.shadowViewSizeHeight), (int) (imgOffsetY + characterDisplayUnit.shadowViewSizeWidth)), bitmapPaint);
                            }
                            Bitmap characterBitmap = null;
                            short characterBitmapDrawType = 0;
                            if (characterDisplayUnit.eggID == 0) {
                                if (this.appDelegate.character0Image0ArrayList != null && characterDisplayUnit.characterID >= 0 && characterDisplayUnit.characterID < this.appDelegate.character0Image0ArrayList.size()) {
                                    short characterID = characterDisplayUnit.characterID;
                                    if (characterDisplayUnit.imageView0TransformX <= BitmapDescriptorFactory.HUE_RED) {
                                        if (characterID == 84) {
                                            short characterImageIndex = (short) (characterID - 84);
                                            if (characterImageIndex >= 0 && characterImageIndex < this.appDelegate.character0RevImage0ArrayList.size()) {
                                                characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex);
                                            }
                                        } else if (characterID == 84 || characterID == 89 || characterID == 90 || characterID == 91 || characterID == 92 || characterID == 93 || characterID == 94 || characterID == 95 || characterID == 96 || characterID == 97 || characterID == 98 || characterID == 99 || characterID == 100 || characterID == 101 || characterID == 102 || characterID == 103) {
                                            short characterImageIndex2 = (short) ((characterID - 89) + 1);
                                            if (characterImageIndex2 >= 0 && characterImageIndex2 < this.appDelegate.character0RevImage0ArrayList.size()) {
                                                characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex2);
                                            }
                                        } else {
                                            characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID);
                                            characterBitmapDrawType = 1;
                                        }
                                    } else {
                                        characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID);
                                    }
                                }
                            } else if (characterDisplayUnit.eggID == 1 && this.appDelegate.character1Image0ArrayList != null && characterDisplayUnit.characterID >= 0 && characterDisplayUnit.characterID < this.appDelegate.character1Image0ArrayList.size()) {
                                characterBitmap = this.appDelegate.character1Image0ArrayList.get(characterDisplayUnit.characterID);
                                if (characterDisplayUnit.imageView0TransformX <= BitmapDescriptorFactory.HUE_RED) {
                                    characterBitmapDrawType = 1;
                                }
                            }
                            if (characterBitmap != null) {
                                int imgOffsetX2 = (int) (characterDisplayUnit.frameOffsetX + drawCharacterOffsetX + characterDisplayUnit.imageView0OriginX);
                                int imgOffsetY2 = (int) (characterDisplayUnit.frameOffsetY + characterDisplayUnit.imageView0OriginY);
                                bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                                canvas.save();
                                if (characterBitmapDrawType == 1) {
                                    canvas.scale(-1.0f, 1.0f, imgOffsetX2 + (characterDisplayUnit.imageView0SizeWidth / 2.0f), BitmapDescriptorFactory.HUE_RED);
                                }
                                canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect(imgOffsetX2, imgOffsetY2, (int) (imgOffsetX2 + characterDisplayUnit.imageView0SizeWidth), (int) (imgOffsetY2 + characterDisplayUnit.imageView0SizeHeight)), bitmapPaint);
                                canvas.restore();
                            }
                        }
                    }
                }
            }
            if (this.farmUnit != null && this.farmUnit.farmFrontViewUnit != null) {
                if (this.farmUnit.farmFrontViewUnit.farmFrontBitmapIndex == 0) {
                    if (this.farmUnit.farmFrontViewUnit.farmFrontBitmap0 != null) {
                        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                        if (!this.appDelegate.isRetina4) {
                            canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap0, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap0.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
                        } else {
                            canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap0, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap0.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
                        }
                    }
                } else if (this.farmUnit.farmFrontViewUnit.farmFrontBitmapIndex == 1 && this.farmUnit.farmFrontViewUnit.farmFrontBitmap1 != null) {
                    bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
                    if (!this.appDelegate.isRetina4) {
                        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap1, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap1.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
                    } else {
                        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap1, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap1.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
                    }
                }
            }
        }
    }

    public void onDestroy() {
        clearBackgroundBitmap();
        this.myDraw = null;
        if (this.nowCharacterDisplayViewsArrayList != null) {
            this.nowCharacterDisplayViewsArrayList.clear();
            this.nowCharacterDisplayViewsArrayList = null;
        }
        this.farmUnit = null;
        this.appDelegate = null;
    }
}
