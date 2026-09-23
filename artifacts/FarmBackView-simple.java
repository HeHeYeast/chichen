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
import com.idtinc.ckunit.FarmUnitDictionary;
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
    private Bitmap backGroundBitmap;
    private float backGroundBitmapDrawWidth;
    public short backGroundIndex;
    private float backGroundOffsetX;
    private float backGroundOffsetXMax;
    private float backGroundOffsetXMin;
    private short buttonClickCnt;
    private float characterOffsetX;
    private float farmFrontViewUnitHeight;
    private float farmFrontViewUnitOffsetY;
    private Bitmap farmHouseButtonBitmap0;
    private Bitmap farmHouseButtonBitmap1;
    public float farmHouseButtonHeight;
    public float farmHouseButtonOffsetX;
    public float farmHouseButtonOffsetY;
    private short farmHouseButtonStatus;
    public float farmHouseButtonWidth;
    private FarmUnit farmUnit;
    private float finalHeight;
    private float finalWidth;
    private Bitmap jinjaHouseButtonBitmap0;
    private Bitmap jinjaHouseButtonBitmap1;
    public float jinjaHouseButtonHeight;
    public float jinjaHouseButtonOffsetX;
    public float jinjaHouseButtonOffsetY;
    private short jinjaHouseButtonStatus;
    public float jinjaHouseButtonWidth;
    private Bitmap monsterVillageButtonBitmap0;
    private Bitmap monsterVillageButtonBitmap1;
    public float monsterVillageButtonHeight;
    public float monsterVillageButtonOffsetX;
    public float monsterVillageButtonOffsetY;
    public short monsterVillageButtonStatus;
    public float monsterVillageButtonWidth;
    private MyDraw myDraw;
    public ArrayList<CharacterDisplayUnit> nowCharacterDisplayViewsArrayList;
    private float preScrollX;
    private short refreshCount;
    private Bitmap timeDoorButtonBitmap0;
    private Bitmap timeDoorButtonBitmap1;
    public float timeDoorButtonHeight;
    public float timeDoorButtonOffsetX;
    public float timeDoorButtonOffsetY;
    public short timeDoorButtonStatus;
    public float timeDoorButtonWidth;
    private short touchButtonIndex;
    private float zoomRate;

    public FarmBackViewUnit(float _finalwidth, float _finalheight, float _zoomrate, FarmUnit _farmUnit, AppDelegate _appDelegate) {
        this.CHARACTERDISPLAYVIEW_OFFSET_Y = 185.0f;
        this.CHARACTERUNITVIEW_OFFSET_X = -10.0f;
        this.CHARACTERUNITVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.CHARACTERUNITVIEW_SPACE_X = 44.0f;
        this.CHARACTERUNITVIEW_SPACE_Y = 20.0f;
        this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE = 22;
        this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE = 20;
        this.CHARACTERUNITVIEW_SIZE_X = 32.0f;
        this.CHARACTERUNITVIEW_SIZE_Y = 32.0f;
        this.CHARACTERUNITVIEW_OFFSET_X_MIN = -60.0f;
        this.CHARACTERUNITVIEW_OFFSET_X_MAX = 320.0f;
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.preScrollX = -9999.0f;
        this.refreshCount = 0;
        this.backGroundIndex = -1;
        this.backGroundOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.backGroundOffsetXMin = BitmapDescriptorFactory.HUE_RED;
        this.backGroundOffsetXMax = BitmapDescriptorFactory.HUE_RED;
        this.backGroundBitmapDrawWidth = BitmapDescriptorFactory.HUE_RED;
        this.characterOffsetX = BitmapDescriptorFactory.HUE_RED;
        this.farmFrontViewUnitOffsetY = 465.0f;
        this.farmFrontViewUnitHeight = 55.0f;
        this.touchButtonIndex = -1;
        this.buttonClickCnt = -1;
        this.farmHouseButtonStatus = -1;
        this.jinjaHouseButtonStatus = -1;
        this.timeDoorButtonStatus = -1;
        this.monsterVillageButtonStatus = -1;
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
        this.backGroundBitmap = null;
        this.jinjaHouseButtonBitmap0 = null;
        this.jinjaHouseButtonBitmap1 = null;
        this.farmHouseButtonBitmap0 = null;
        this.farmHouseButtonBitmap1 = null;
        this.timeDoorButtonBitmap0 = null;
        this.timeDoorButtonBitmap1 = null;
        this.monsterVillageButtonBitmap0 = null;
        this.monsterVillageButtonBitmap1 = null;
        this.appDelegate = _appDelegate;
        this.farmUnit = _farmUnit;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.refreshCount = 0;
        this.backGroundIndex = -1;
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
        this.touchButtonIndex = -1;
        this.buttonClickCnt = -1;
        this.farmHouseButtonStatus = 0;
        this.jinjaHouseButtonStatus = 0;
        this.timeDoorButtonStatus = -1;
        this.monsterVillageButtonStatus = 0;
        this.farmFrontViewUnitOffsetY = 465.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L5;
        this.farmFrontViewUnitOffsetY -= 88.0f * this.zoomRate;
    L5:
        this.farmFrontViewUnitHeight = this.farmFrontViewUnitOffsetY + (55.0f * this.zoomRate);
        this.farmHouseButtonOffsetX = 2.0f * this.zoomRate;
        this.farmHouseButtonOffsetY = 136.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L8;
        this.farmHouseButtonOffsetY -= this.appDelegate.offset44;
    L8:
        this.farmHouseButtonWidth = 90.0f * this.zoomRate;
        this.farmHouseButtonHeight = 90.0f * this.zoomRate;
        this.jinjaHouseButtonOffsetX = 355.0f * this.zoomRate;
        this.jinjaHouseButtonOffsetY = 148.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L11;
        this.jinjaHouseButtonOffsetY -= this.appDelegate.offset44;
    L11:
        this.jinjaHouseButtonWidth = 65.0f * this.zoomRate;
        this.jinjaHouseButtonHeight = 65.0f * this.zoomRate;
        this.timeDoorButtonOffsetX = 130.0f * this.zoomRate;
        this.timeDoorButtonOffsetY = 143.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L14;
        this.timeDoorButtonOffsetY -= this.appDelegate.offset44;
    L14:
        this.timeDoorButtonWidth = 65.0f * this.zoomRate;
        this.timeDoorButtonHeight = 65.0f * this.zoomRate;
        this.monsterVillageButtonOffsetX = 235.0f * this.zoomRate;
        this.monsterVillageButtonOffsetY = 146.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L17;
        this.monsterVillageButtonOffsetY -= this.appDelegate.offset44;
    L17:
        this.monsterVillageButtonWidth = 65.0f * this.zoomRate;
        this.monsterVillageButtonHeight = 65.0f * this.zoomRate;
        short positionIndex = 0;
        this.nowCharacterDisplayViewsArrayList = new ArrayList();
        CharacterDisplayUnit character_0_68_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L52;
        character_0_68_DisplayUnit.init(500.0f * this.zoomRate, 106.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L20:
        character_0_68_DisplayUnit.setWithEggID(-1, -1, character_0_68_DisplayUnit.frameOffsetX, character_0_68_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_68_DisplayUnit.shadowViewOriginY = (10.0f * this.zoomRate) + (character_0_68_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_0_68_DisplayUnit);
        CharacterDisplayUnit character_1_36_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L53;
        character_1_36_DisplayUnit.init(540.0f * this.zoomRate, 106.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L23:
        character_1_36_DisplayUnit.setWithEggID(-1, -1, character_1_36_DisplayUnit.frameOffsetX, character_1_36_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_1_36_DisplayUnit.shadowViewOriginY = (10.0f * this.zoomRate) + (character_1_36_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_1_36_DisplayUnit);
        int characterDisplayViewTotal = 218;
        if (this.appDelegate.isRetina4 == true) goto L26;
        characterDisplayViewTotal = 218 - 44;
    L26:
        int i = 0;
    L28:
        if (i >= characterDisplayViewTotal) goto L29;
        if (positionIndex != 0) goto L56;
        positionIndex = (short) (positionIndex + 2);
    L56:
        short offsetIndexX = (short) (positionIndex % 22);
        short offsetIndexY = (short) (positionIndex / 22);
        float offsetYFloat = this.CHARACTERUNITVIEW_SPACE_Y * offsetIndexY;
        float charOffsetX = this.CHARACTERUNITVIEW_OFFSET_X + (this.CHARACTERUNITVIEW_SPACE_X * offsetIndexX);
        float charOffsetY = (this.CHARACTERDISPLAYVIEW_OFFSET_Y + this.CHARACTERUNITVIEW_OFFSET_Y) + offsetYFloat;
        if (this.appDelegate.isRetina4 == true) goto L59;
        charOffsetY = charOffsetY - (44.0f * this.zoomRate);
    L59:
        CharacterDisplayUnit characterDisplayUnit = new CharacterDisplayUnit();
        characterDisplayUnit.init(charOffsetX, charOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, this.zoomRate);
        characterDisplayUnit.setWithEggID(-1, -1, charOffsetX, charOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        this.nowCharacterDisplayViewsArrayList.add(characterDisplayUnit);
        positionIndex = (short) (positionIndex + 1);
        i = i + 1;
        goto L28
    L29:
        CharacterDisplayUnit character_0_104_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L60;
        character_0_104_DisplayUnit.init(140.0f * this.zoomRate, 56.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L32:
        character_0_104_DisplayUnit.setWithEggID(-1, -1, character_0_104_DisplayUnit.frameOffsetX, character_0_104_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_104_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_104_DisplayUnit);
        CharacterDisplayUnit character_0_32_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L61;
        character_0_32_DisplayUnit.init(18.0f * this.zoomRate, 71.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L35:
        character_0_32_DisplayUnit.setWithEggID(-1, -1, character_0_32_DisplayUnit.frameOffsetX, character_0_32_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_32_DisplayUnit.shadowViewOriginY = (15.0f * this.zoomRate) + (character_0_32_DisplayUnit.frameSizeHeight / 20.0f);
        this.nowCharacterDisplayViewsArrayList.add(character_0_32_DisplayUnit);
        CharacterDisplayUnit character_0_52_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L62;
        character_0_52_DisplayUnit.init(267.0f * this.zoomRate, 30.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L38:
        character_0_52_DisplayUnit.setWithEggID(-1, -1, character_0_52_DisplayUnit.frameOffsetX, character_0_52_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_52_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_52_DisplayUnit);
        CharacterDisplayUnit character_0_64_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L63;
        character_0_64_DisplayUnit.init(660.0f * this.zoomRate, 26.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L41:
        character_0_64_DisplayUnit.setWithEggID(-1, -1, character_0_64_DisplayUnit.frameOffsetX, character_0_64_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_64_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_64_DisplayUnit);
        CharacterDisplayUnit character_0_80_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L64;
        character_0_80_DisplayUnit.init(450.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L44:
        character_0_80_DisplayUnit.setWithEggID(-1, -1, character_0_80_DisplayUnit.frameOffsetX, character_0_80_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_80_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_80_DisplayUnit);
        CharacterDisplayUnit character_0_70_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L65;
        character_0_70_DisplayUnit.init(520.0f * this.zoomRate, 76.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L47:
        character_0_70_DisplayUnit.setWithEggID(-1, -1, character_0_70_DisplayUnit.frameOffsetX, character_0_70_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_0_70_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_0_70_DisplayUnit);
        CharacterDisplayUnit character_1_38_DisplayUnit = new CharacterDisplayUnit();
        if (this.appDelegate.isRetina4 == true) goto L66;
        character_1_38_DisplayUnit.init(550.0f * this.zoomRate, 76.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
    L50:
        character_1_38_DisplayUnit.setWithEggID(-1, -1, character_1_38_DisplayUnit.frameOffsetX, character_1_38_DisplayUnit.frameOffsetY, this.CHARACTERUNITVIEW_SIZE_X, this.CHARACTERUNITVIEW_SIZE_Y, false);
        character_1_38_DisplayUnit.shadowViewOriginY = (-9999.0f) * this.zoomRate;
        this.nowCharacterDisplayViewsArrayList.add(character_1_38_DisplayUnit);
        this.myDraw = new MyDraw();
        return;
    L66:
        character_1_38_DisplayUnit.init(550.0f * this.zoomRate, 120.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L50
    L65:
        character_0_70_DisplayUnit.init(520.0f * this.zoomRate, 120.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L47
    L64:
        character_0_80_DisplayUnit.init(450.0f * this.zoomRate, 90.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L44
    L63:
        character_0_64_DisplayUnit.init(660.0f * this.zoomRate, 70.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L41
    L62:
        character_0_52_DisplayUnit.init(267.0f * this.zoomRate, 74.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L38
    L61:
        character_0_32_DisplayUnit.init(18.0f * this.zoomRate, 115.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L35
    L60:
        character_0_104_DisplayUnit.init(140.0f * this.zoomRate, 100.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L32
    L53:
        character_1_36_DisplayUnit.init(540.0f * this.zoomRate, 150.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L23
    L52:
        character_0_68_DisplayUnit.init(500.0f * this.zoomRate, 150.0f * this.zoomRate, 46.0f * this.zoomRate, 46.0f * this.zoomRate, this.zoomRate);
        goto L20
    }

    public void refreshCharacterDisplayViewsArray() {
        if (this.appDelegate.timeSaveDictionary != null) goto L5;
        return;
    L5:
        ArrayList<Short> indexArray = new ArrayList();
        int i = 0;
    L7:
        if (i >= this.nowCharacterDisplayViewsArrayList.size()) goto L9;
        CharacterDisplayUnit characterDisplayUnit = this.nowCharacterDisplayViewsArrayList.get(i);
        if (characterDisplayUnit == null) goto L21;
        characterDisplayUnit.setWithEggID(-1, -1, characterDisplayUnit.frameOffsetX, characterDisplayUnit.frameOffsetY, characterDisplayUnit.frameSizeWidth, characterDisplayUnit.frameSizeHeight, false);
        if (i <= 1) goto L21;
        if (i >= (this.nowCharacterDisplayViewsArrayList.size() - 10)) goto L21;
        indexArray.add(new Short((short) i));
    L21:
        i = i + 1;
        goto L7
    L9:
        if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList == null) goto L13;
        int i2 = 0;
    L12:
        if (i2 >= this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size()) goto L13;
        if (i2 >= this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size()) goto L29;
        ArrayList<FarmUnitDictionary> unitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(i2);
        if (unitDictionarysArrayList == null) goto L29;
        int j = 0;
    L28:
        if (j >= unitDictionarysArrayList.size()) goto L29;
        if (j >= unitDictionarysArrayList.size()) goto L49;
        FarmUnitDictionary farmUnitDictionary = unitDictionarysArrayList.get(j);
        if (farmUnitDictionary == null) goto L49;
        float countFloat = farmUnitDictionary.getCount();
        if (countFloat < 1.0f) goto L49;
        if (i2 != 0) goto L281;
        if (j != 104) goto L51;
        String ck_send_chick_string = "";
        if (this.appDelegate == null) goto L48;
        if (this.appDelegate.defaultSharedPreferences != null) goto L45;
        this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
    L45:
        if (this.appDelegate.defaultSharedPreferences == null) goto L48;
        ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
    L48:
        if (ck_send_chick_string.length() <= 0) goto L49;
    L100:
        if (j != 32) goto L209;
        CharacterDisplayUnit characterDisplayUnit2 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 6));
        if (characterDisplayUnit2 == null) goto L49;
        characterDisplayUnit2.setWithEggID((short) i2, (short) j, characterDisplayUnit2.frameOffsetX, characterDisplayUnit2.frameOffsetY, characterDisplayUnit2.frameSizeWidth, characterDisplayUnit2.frameSizeHeight, false);
        goto L49
    L209:
        if (j != 37) goto L211;
    L214:
        short index = -1;
        if (j != 37) goto L224;
        index = (short) (this.nowCharacterDisplayViewsArrayList.size() - 10);
    L217:
        if (index < 0) goto L49;
        if (index >= (this.nowCharacterDisplayViewsArrayList.size() - 1)) goto L49;
        CharacterDisplayUnit characterDisplayUnit3 = this.nowCharacterDisplayViewsArrayList.get(index);
        if (characterDisplayUnit3 == null) goto L49;
        characterDisplayUnit3.setWithEggID((short) i2, (short) j, characterDisplayUnit3.frameOffsetX, characterDisplayUnit3.frameOffsetY, characterDisplayUnit3.frameSizeWidth, characterDisplayUnit3.frameSizeHeight, true);
        goto L49
    L224:
        if (j != 38) goto L227;
        index = (short) (this.nowCharacterDisplayViewsArrayList.size() - 9);
        goto L217
    L227:
        if (j != 39) goto L217;
        index = (short) (this.nowCharacterDisplayViewsArrayList.size() - 8);
        goto L217
    L211:
        if (j == 38) goto L214;
        if (j == 39) goto L214;
        if (j != 52) goto L235;
        CharacterDisplayUnit characterDisplayUnit4 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 5));
        if (characterDisplayUnit4 == null) goto L49;
        characterDisplayUnit4.setWithEggID((short) i2, (short) j, characterDisplayUnit4.frameOffsetX, characterDisplayUnit4.frameOffsetY, characterDisplayUnit4.frameSizeWidth, characterDisplayUnit4.frameSizeHeight, false);
        goto L49
    L235:
        if (j != 78) goto L240;
        CharacterDisplayUnit characterDisplayUnit5 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 5));
        if (characterDisplayUnit5 == null) goto L49;
        characterDisplayUnit5.setWithEggID((short) i2, (short) j, characterDisplayUnit5.frameOffsetX, characterDisplayUnit5.frameOffsetY, characterDisplayUnit5.frameSizeWidth, characterDisplayUnit5.frameSizeHeight, false);
        goto L49
    L240:
        if (j != 64) goto L245;
        CharacterDisplayUnit characterDisplayUnit6 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 4));
        if (characterDisplayUnit6 == null) goto L49;
        characterDisplayUnit6.setWithEggID((short) i2, (short) j, characterDisplayUnit6.frameOffsetX, characterDisplayUnit6.frameOffsetY, characterDisplayUnit6.frameSizeWidth, characterDisplayUnit6.frameSizeHeight, false);
        goto L49
    L245:
        if (j != 80) goto L250;
        CharacterDisplayUnit characterDisplayUnit7 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 3));
        if (characterDisplayUnit7 == null) goto L49;
        characterDisplayUnit7.setWithEggID((short) i2, (short) j, characterDisplayUnit7.frameOffsetX, characterDisplayUnit7.frameOffsetY, characterDisplayUnit7.frameSizeWidth, characterDisplayUnit7.frameSizeHeight, false);
        goto L49
    L250:
        if (j != 70) goto L255;
        CharacterDisplayUnit characterDisplayUnit8 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 2));
        if (characterDisplayUnit8 == null) goto L49;
        characterDisplayUnit8.setWithEggID((short) i2, (short) j, characterDisplayUnit8.frameOffsetX, characterDisplayUnit8.frameOffsetY, characterDisplayUnit8.frameSizeWidth, characterDisplayUnit8.frameSizeHeight, false);
        goto L49
    L255:
        if (j != 68) goto L260;
        CharacterDisplayUnit characterDisplayUnit9 = this.nowCharacterDisplayViewsArrayList.get(0);
        if (characterDisplayUnit9 == null) goto L49;
        characterDisplayUnit9.setWithEggID((short) i2, (short) j, characterDisplayUnit9.frameOffsetX, characterDisplayUnit9.frameOffsetY, characterDisplayUnit9.frameSizeWidth, characterDisplayUnit9.frameSizeHeight, false);
        goto L49
    L260:
        if (j != 104) goto L265;
        CharacterDisplayUnit characterDisplayUnit10 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 7));
        if (characterDisplayUnit10 == null) goto L49;
        characterDisplayUnit10.setWithEggID((short) i2, (short) j, characterDisplayUnit10.frameOffsetX, characterDisplayUnit10.frameOffsetY, characterDisplayUnit10.frameSizeWidth, characterDisplayUnit10.frameSizeHeight, false);
        goto L49
    L265:
        if (indexArray.size() <= 0) goto L49;
        short rand = (short) (Math.random() * indexArray.size());
        Short indexShort = indexArray.get(rand);
        if (indexShort == null) goto L49;
        short index2 = indexShort.shortValue();
        if (index2 < 0) goto L49;
        if (index2 >= (this.nowCharacterDisplayViewsArrayList.size() - 1)) goto L49;
        CharacterDisplayUnit characterDisplayUnit11 = this.nowCharacterDisplayViewsArrayList.get(index2);
        if (characterDisplayUnit11 == null) goto L49;
        characterDisplayUnit11.setWithEggID((short) i2, (short) j, characterDisplayUnit11.frameOffsetX, characterDisplayUnit11.frameOffsetY, characterDisplayUnit11.frameSizeWidth, characterDisplayUnit11.frameSizeHeight, true);
        if (j != 67) goto L277;
    L278:
        characterDisplayUnit11.shadowViewOriginY = (-9999.0f) * this.zoomRate;
    L279:
        indexArray.remove(rand);
        goto L49
    L277:
        if (j != 106) goto L279;
    L51:
        if (this.backGroundIndex != 0) goto L105;
        if (j == 0) goto L100;
        if (j == 20) goto L100;
        if (j == 25) goto L100;
        if (j == 70) goto L100;
        if (j == 83) goto L100;
        if (j == 84) goto L100;
        if (j == 85) goto L100;
        if (j == 88) goto L100;
        if (j == 89) goto L100;
        if (j == 90) goto L100;
        if (j == 91) goto L100;
        if (j == 92) goto L100;
        if (j == 93) goto L100;
        if (j == 94) goto L100;
        if (j == 95) goto L100;
        if (j == 96) goto L100;
        if (j == 97) goto L100;
        if (j == 98) goto L100;
        if (j == 99) goto L100;
        if (j == 100) goto L100;
        if (j == 101) goto L100;
        if (j == 102) goto L100;
        if (j == 103) goto L100;
        if (j != 108) goto L49;
    L105:
        if (this.backGroundIndex != 20) goto L121;
        if (j == 0) goto L100;
        if (j == 20) goto L100;
        if (j == 25) goto L100;
        if (j == 68) goto L100;
        if (j == 83) goto L100;
        if (j == 84) goto L100;
        if (j == 85) goto L100;
    L121:
        if (this.backGroundIndex != 30) goto L154;
        if (j == 20) goto L100;
        if (j == 25) goto L100;
        if (j == 26) goto L100;
        if (j == 27) goto L100;
        if (j == 30) goto L100;
        if (j == 64) goto L100;
        if (j == 67) goto L100;
        if (j == 68) goto L100;
        if (j == 78) goto L100;
        if (j == 80) goto L100;
        if (j == 83) goto L100;
        if (j == 84) goto L100;
        if (j == 105) goto L100;
        if (j == 106) goto L100;
        if (j == 107) goto L100;
    L154:
        if (j == 26) goto L49;
        if (j == 27) goto L49;
        if (j == 30) goto L49;
        if (j == 64) goto L49;
        if (j == 67) goto L49;
        if (j == 68) goto L49;
        if (j == 78) goto L49;
        if (j == 80) goto L49;
        if (j == 88) goto L49;
        if (j == 89) goto L49;
        if (j == 90) goto L49;
        if (j == 91) goto L49;
        if (j == 92) goto L49;
        if (j == 93) goto L49;
        if (j == 94) goto L49;
        if (j == 95) goto L49;
        if (j == 96) goto L49;
        if (j == 97) goto L49;
        if (j == 98) goto L49;
        if (j == 99) goto L49;
        if (j == 100) goto L49;
        if (j == 101) goto L49;
        if (j == 102) goto L49;
        if (j == 103) goto L49;
        if (j == 105) goto L49;
        if (j == 106) goto L49;
        if (j != 107) goto L100;
    L281:
        if (i2 != 1) goto L49;
        if (this.backGroundIndex != 0) goto L293;
        if (j == 0) goto L288;
        if (j != 38) goto L49;
    L288:
        if (j != 36) goto L321;
        CharacterDisplayUnit characterDisplayUnit12 = this.nowCharacterDisplayViewsArrayList.get(1);
        if (characterDisplayUnit12 == null) goto L49;
        characterDisplayUnit12.setWithEggID((short) i2, (short) j, characterDisplayUnit12.frameOffsetX, characterDisplayUnit12.frameOffsetY, characterDisplayUnit12.frameSizeWidth, characterDisplayUnit12.frameSizeHeight, false);
        goto L49
    L321:
        if (j != 38) goto L326;
        CharacterDisplayUnit characterDisplayUnit13 = this.nowCharacterDisplayViewsArrayList.get((short) (this.nowCharacterDisplayViewsArrayList.size() - 1));
        if (characterDisplayUnit13 == null) goto L49;
        characterDisplayUnit13.setWithEggID((short) i2, (short) j, characterDisplayUnit13.frameOffsetX, characterDisplayUnit13.frameOffsetY, characterDisplayUnit13.frameSizeWidth, characterDisplayUnit13.frameSizeHeight, false);
        goto L49
    L326:
        if (indexArray.size() <= 0) goto L49;
        short rand2 = (short) (Math.random() * indexArray.size());
        Short indexShort2 = indexArray.get(rand2);
        if (indexShort2 == null) goto L49;
        short index3 = indexShort2.shortValue();
        if (index3 < 0) goto L49;
        if (index3 >= (this.nowCharacterDisplayViewsArrayList.size() - 1)) goto L49;
        CharacterDisplayUnit characterDisplayUnit14 = this.nowCharacterDisplayViewsArrayList.get(index3);
        if (characterDisplayUnit14 == null) goto L49;
        characterDisplayUnit14.setWithEggID((short) i2, (short) j, characterDisplayUnit14.frameOffsetX, characterDisplayUnit14.frameOffsetY, characterDisplayUnit14.frameSizeWidth, characterDisplayUnit14.frameSizeHeight, true);
        indexArray.remove(rand2);
        goto L49
    L293:
        if (this.backGroundIndex != 20) goto L303;
        if (j == 0) goto L288;
        if (j == 36) goto L288;
        if (j == 48) goto L288;
        if (j == 49) goto L288;
    L303:
        if (this.backGroundIndex != 30) goto L312;
        if (j == 15) goto L288;
        if (j == 28) goto L288;
        if (j == 36) goto L288;
    L312:
        if (j == 15) goto L49;
        if (j == 28) goto L49;
        if (j == 36) goto L49;
        if (j != 49) goto L288;
    L49:
        j = j + 1;
    L29:
        i2 = i2 + 1;
    L13:
        indexArray.clear();
    }

    public void clearBackgroundBitmap() {
        this.backGroundIndex = -1;
        this.backGroundBitmapDrawWidth = BitmapDescriptorFactory.HUE_RED;
        if (this.backGroundBitmap == null) goto L9;
        if (this.backGroundBitmap.isRecycled() == true) goto L7;
        this.backGroundBitmap.recycle();
    L7:
        this.backGroundBitmap = null;
    L9:
        if (this.farmHouseButtonBitmap0 == null) goto L15;
        if (this.farmHouseButtonBitmap0.isRecycled() == true) goto L13;
        this.farmHouseButtonBitmap0.recycle();
    L13:
        this.farmHouseButtonBitmap0 = null;
    L15:
        if (this.farmHouseButtonBitmap1 == null) goto L21;
        if (this.farmHouseButtonBitmap1.isRecycled() == true) goto L19;
        this.farmHouseButtonBitmap1.recycle();
    L19:
        this.farmHouseButtonBitmap1 = null;
    L21:
        if (this.jinjaHouseButtonBitmap0 == null) goto L27;
        if (this.jinjaHouseButtonBitmap0.isRecycled() == true) goto L25;
        this.jinjaHouseButtonBitmap0.recycle();
    L25:
        this.jinjaHouseButtonBitmap0 = null;
    L27:
        if (this.jinjaHouseButtonBitmap1 == null) goto L33;
        if (this.jinjaHouseButtonBitmap1.isRecycled() == true) goto L31;
        this.jinjaHouseButtonBitmap1.recycle();
    L31:
        this.jinjaHouseButtonBitmap1 = null;
    L33:
        if (this.timeDoorButtonBitmap0 == null) goto L39;
        if (this.timeDoorButtonBitmap0.isRecycled() == true) goto L37;
        this.timeDoorButtonBitmap0.recycle();
    L37:
        this.timeDoorButtonBitmap0 = null;
    L39:
        if (this.timeDoorButtonBitmap1 == null) goto L45;
        if (this.timeDoorButtonBitmap1.isRecycled() == true) goto L43;
        this.timeDoorButtonBitmap1.recycle();
    L43:
        this.timeDoorButtonBitmap1 = null;
    L45:
        if (this.monsterVillageButtonBitmap0 == null) goto L51;
        if (this.monsterVillageButtonBitmap0.isRecycled() == true) goto L49;
        this.monsterVillageButtonBitmap0.recycle();
    L49:
        this.monsterVillageButtonBitmap0 = null;
    L51:
        if (this.monsterVillageButtonBitmap1 != null) goto L53;
        return;
    L53:
        if (this.monsterVillageButtonBitmap1.isRecycled() == true) goto L55;
        this.monsterVillageButtonBitmap1.recycle();
    L55:
        this.monsterVillageButtonBitmap1 = null;
    }

    public void refreshBackgroundBitmap() {
        if (this.appDelegate != null) goto L5;
        return;
    L5:
        short newBackGroundIndex = this.appDelegate.getNowTimeIndex();
        if (newBackGroundIndex == this.backGroundIndex) goto L56;
        clearBackgroundBitmap();
        this.backGroundIndex = newBackGroundIndex;
        if (this.backGroundIndex < 0) goto L57;
        AssetManager asm = this.appDelegate.getAssets();
        BitmapFactory.Options opt2 = new BitmapFactory.Options();
        opt2.inJustDecodeBounds = false;
        opt2.inSampleSize = 1;
        opt2.inPreferredConfig = Bitmap.Config.RGB_565;
        opt2.inPurgeable = true;
        opt2.inInputShareable = true;
        InputStream inputStream = asm.open("png/Tool/Tool0/tool_0_1_" + this.backGroundIndex + "_0.jpg");     // Catch: IOException -> L36
        this.backGroundBitmap = BitmapFactory.decodeStream(inputStream, null, opt2);     // Catch: IOException -> L36
        this.backGroundOffsetXMax = this.backGroundBitmap.getWidth() - 640.0f;     // Catch: IOException -> L36
        this.backGroundBitmapDrawWidth = 640.0f;     // Catch: IOException -> L36
        inputStream.close();     // Catch: IOException -> L36
    L42:
        InputStream inputStream2 = asm.open("png/Farm/farm_house_" + this.backGroundIndex + "_0.png");     // Catch: IOException -> L34
        this.farmHouseButtonBitmap0 = BitmapFactory.decodeStream(inputStream2, null, opt2);     // Catch: IOException -> L34
        inputStream2.close();     // Catch: IOException -> L34
    L48:
        InputStream inputStream3 = asm.open("png/Farm/farm_house_" + this.backGroundIndex + "_1.png");     // Catch: IOException -> L32
        this.farmHouseButtonBitmap1 = BitmapFactory.decodeStream(inputStream3, null, opt2);     // Catch: IOException -> L32
        inputStream3.close();     // Catch: IOException -> L32
    L50:
        InputStream inputStream4 = asm.open("png/Farm/jinja_house_" + this.backGroundIndex + "_0.png");     // Catch: IOException -> L30
        this.jinjaHouseButtonBitmap0 = BitmapFactory.decodeStream(inputStream4, null, opt2);     // Catch: IOException -> L30
        inputStream4.close();     // Catch: IOException -> L30
    L52:
        InputStream inputStream5 = asm.open("png/Farm/jinja_house_" + this.backGroundIndex + "_1.png");     // Catch: IOException -> L28
        this.jinjaHouseButtonBitmap1 = BitmapFactory.decodeStream(inputStream5, null, opt2);     // Catch: IOException -> L28
        inputStream5.close();     // Catch: IOException -> L28
    L54:
        InputStream inputStream6 = asm.open("png/Farm/time_door_" + this.backGroundIndex + "_0.png");     // Catch: IOException -> L26
        this.timeDoorButtonBitmap0 = BitmapFactory.decodeStream(inputStream6, null, opt2);     // Catch: IOException -> L26
        inputStream6.close();     // Catch: IOException -> L26
    L38:
        InputStream inputStream7 = asm.open("png/Farm/time_door_" + this.backGroundIndex + "_1.png");     // Catch: IOException -> L24
        this.timeDoorButtonBitmap1 = BitmapFactory.decodeStream(inputStream7, null, opt2);     // Catch: IOException -> L24
        inputStream7.close();     // Catch: IOException -> L24
    L44:
        InputStream inputStream8 = asm.open("png/Farm/monster_village_" + this.backGroundIndex + "_0.png");     // Catch: IOException -> L22
        this.monsterVillageButtonBitmap0 = BitmapFactory.decodeStream(inputStream8, null, opt2);     // Catch: IOException -> L22
        inputStream8.close();     // Catch: IOException -> L22
    L46:
        InputStream inputStream9 = asm.open("png/Farm/monster_village_" + this.backGroundIndex + "_1.png");     // Catch: IOException -> L20
        this.monsterVillageButtonBitmap1 = BitmapFactory.decodeStream(inputStream9, null, opt2);     // Catch: IOException -> L20
        inputStream9.close();     // Catch: IOException -> L20
    L19:
        refreshCharacterDisplayViewsArray();
        return;
    L57:
        return;
    }

    public void doRefreshLoop() {
        refreshBackgroundBitmap();
        this.refreshCount = 100;
    }

    public void doLoop() {
        this.refreshCount = (short) (this.refreshCount - 1);
        if (this.refreshCount > 0) goto L6;
        doRefreshLoop();
        return;
    }

    public boolean gameOnTouch(MotionEvent event) {
        Log.d("FarmBackView", "onTouchEvent");
        if (event.getAction() != 0) goto L50;
        this.preScrollX = event.getX();
        if (this.farmHouseButtonStatus != 0) goto L17;
        float farmHouseButtonTouchOffsetX = this.farmHouseButtonOffsetX + this.characterOffsetX;
        if (event.getY() <= this.farmHouseButtonOffsetY) goto L17;
        if (event.getY() >= (this.farmHouseButtonOffsetY + this.farmHouseButtonHeight)) goto L17;
        if (event.getX() <= farmHouseButtonTouchOffsetX) goto L17;
        if (event.getX() >= (this.farmHouseButtonWidth + farmHouseButtonTouchOffsetX)) goto L17;
        this.touchButtonIndex = 0;
        this.buttonClickCnt = 3;
        Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("FarmBackView", "touchButtonIndex:" + this.touchButtonIndex);
        new Handler().postDelayed(new AnonymousClass1(this), 200);
        return true;
    L17:
        if (this.jinjaHouseButtonStatus != 0) goto L28;
        float jinjaHouseButtonTouchOffsetX = this.jinjaHouseButtonOffsetX + this.characterOffsetX;
        if (event.getY() <= this.jinjaHouseButtonOffsetY) goto L28;
        if (event.getY() >= (this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight)) goto L28;
        if (event.getX() <= jinjaHouseButtonTouchOffsetX) goto L28;
        if (event.getX() >= (this.timeDoorButtonWidth + jinjaHouseButtonTouchOffsetX)) goto L28;
        this.touchButtonIndex = 1;
        this.buttonClickCnt = 3;
        Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("FarmBackView", "touchButtonIndex:" + this.touchButtonIndex);
        new Handler().postDelayed(new AnonymousClass2(this), 200);
        return true;
    L28:
        if (this.timeDoorButtonStatus != 0) goto L39;
        float timeDoorButtonTouchOffsetX = this.timeDoorButtonOffsetX + this.characterOffsetX;
        if (event.getY() <= this.timeDoorButtonOffsetY) goto L39;
        if (event.getY() >= (this.timeDoorButtonOffsetY + this.timeDoorButtonHeight)) goto L39;
        if (event.getX() <= timeDoorButtonTouchOffsetX) goto L39;
        if (event.getX() >= (this.timeDoorButtonWidth + timeDoorButtonTouchOffsetX)) goto L39;
        this.touchButtonIndex = 2;
        this.buttonClickCnt = 3;
        new Handler().postDelayed(new AnonymousClass3(this), 200);
        return true;
    L39:
        if (this.monsterVillageButtonStatus != 0) goto L66;
        float monsterVillageButtonTouchOffsetX = this.monsterVillageButtonOffsetX + this.characterOffsetX;
        if (event.getY() <= this.monsterVillageButtonOffsetY) goto L66;
        if (event.getY() >= (this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight)) goto L66;
        if (event.getX() <= monsterVillageButtonTouchOffsetX) goto L66;
        if (event.getX() >= (this.monsterVillageButtonWidth + monsterVillageButtonTouchOffsetX)) goto L66;
        this.touchButtonIndex = 3;
        this.buttonClickCnt = 3;
        Log.d("FarmBackView", "X=" + event.getX() + ", Y=  " + event.getY());
        Log.d("FarmBackView", "touchButtonIndex:" + this.touchButtonIndex);
        new Handler().postDelayed(new AnonymousClass4(this), 200);
        return true;
    L66:
        return false;
    L50:
        if (event.getAction() != 2) goto L66;
        float addOffScrollX = this.preScrollX - event.getX();
        this.backGroundOffsetX += addOffScrollX;
        if (this.backGroundOffsetX >= this.backGroundOffsetXMin) goto L60;
        this.backGroundOffsetX = this.backGroundOffsetXMin;
    L55:
        if (this.backGroundBitmapDrawWidth <= BitmapDescriptorFactory.HUE_RED) goto L62;
        this.characterOffsetX = (((-1.0f) * this.backGroundOffsetX) * this.finalWidth) / this.backGroundBitmapDrawWidth;
    L57:
        Log.d("FarmListScrollLayout", "backGroundOffsetXMax=" + this.backGroundOffsetXMax);
        Log.d("FarmListScrollLayout", "backGroundOffsetX=" + this.backGroundOffsetX);
        this.preScrollX = event.getX();
        goto L66
    L62:
        this.characterOffsetX = BitmapDescriptorFactory.HUE_RED;
        goto L57
    L60:
        if (this.backGroundOffsetX <= this.backGroundOffsetXMax) goto L55;
        this.backGroundOffsetX = this.backGroundOffsetXMax;
        goto L55
    }

    public void doClick() {
        if (this.touchButtonIndex != 0) goto L8;
        this.farmUnit.openListLayout();
        this.appDelegate.doSoundPoolPlay(1);
    L5:
        this.touchButtonIndex = -1;
        this.buttonClickCnt = -1;
        return;
    L8:
        if (this.touchButtonIndex != 1) goto L11;
        this.farmUnit.goToCKKB();
        this.appDelegate.doSoundPoolPlay(1);
        goto L5
    L11:
        if (this.touchButtonIndex != 2) goto L14;
        this.farmUnit.displayCheckReceiveChicksFromCKAlert();
        goto L5
    L14:
        if (this.touchButtonIndex != 3) goto L5;
        this.farmUnit.goToCKMV();
        this.appDelegate.doSoundPoolPlay(1);
        goto L5
    }

    public void gameDraw(Canvas canvas) {
        Paint bitmapPaint = new Paint();
        bitmapPaint.setColor(FluctConstants.FRAME_ALPHA_COLOR);
        canvas.drawRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalWidth, this.finalHeight, bitmapPaint);
        if (this.appDelegate != null) goto L5;
        return;
    L5:
        float drawBackGroundOffsetX = this.backGroundOffsetX;
        float drawCharacterOffsetX = this.characterOffsetX;
        float drawButtonOffsetX = this.characterOffsetX;
        int drawBackGroundBitmapDrawWidth = (int) (this.backGroundBitmapDrawWidth + drawBackGroundOffsetX);
        if (this.backGroundBitmap == null) goto L11;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        if (this.appDelegate.isRetina4 == true) goto L55;
        canvas.drawBitmap(this.backGroundBitmap, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.backGroundBitmap.getHeight()), new Rect(0, (int) (0.0d - this.appDelegate.offset44), (int) this.finalWidth, (int) (this.appDelegate.isRetina4Height - this.appDelegate.offset44)), bitmapPaint);
        goto L11
    L55:
        canvas.drawBitmap(this.backGroundBitmap, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.backGroundBitmap.getHeight()), new Rect(0, 0, (int) this.finalWidth, (int) this.appDelegate.isRetina4Height), bitmapPaint);
    L11:
        if (this.farmHouseButtonStatus != 0) goto L18;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        float farmHouseButtonDrawOffsetX = this.farmHouseButtonOffsetX + drawButtonOffsetX;
        if (this.touchButtonIndex != 0) goto L57;
        if (this.farmHouseButtonBitmap1 == null) goto L18;
        canvas.drawBitmap(this.farmHouseButtonBitmap1, new Rect(0, 0, this.farmHouseButtonBitmap1.getWidth(), this.farmHouseButtonBitmap1.getHeight()), new Rect((int) farmHouseButtonDrawOffsetX, (int) this.farmHouseButtonOffsetY, (int) (this.farmHouseButtonWidth + farmHouseButtonDrawOffsetX), (int) (this.farmHouseButtonOffsetY + this.farmHouseButtonHeight)), bitmapPaint);
        goto L18
    L57:
        if (this.farmHouseButtonBitmap0 == null) goto L18;
        canvas.drawBitmap(this.farmHouseButtonBitmap0, new Rect(0, 0, this.farmHouseButtonBitmap0.getWidth(), this.farmHouseButtonBitmap0.getHeight()), new Rect((int) farmHouseButtonDrawOffsetX, (int) this.farmHouseButtonOffsetY, (int) (this.farmHouseButtonWidth + farmHouseButtonDrawOffsetX), (int) (this.farmHouseButtonOffsetY + this.farmHouseButtonHeight)), bitmapPaint);
    L18:
        if (this.jinjaHouseButtonStatus != 0) goto L25;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        float jinjaHouseButtonDrawOffsetX = this.jinjaHouseButtonOffsetX + drawButtonOffsetX;
        if (this.touchButtonIndex != 1) goto L60;
        if (this.jinjaHouseButtonBitmap1 == null) goto L25;
        canvas.drawBitmap(this.jinjaHouseButtonBitmap1, new Rect(0, 0, this.jinjaHouseButtonBitmap1.getWidth(), this.jinjaHouseButtonBitmap1.getHeight()), new Rect((int) jinjaHouseButtonDrawOffsetX, (int) this.jinjaHouseButtonOffsetY, (int) (this.jinjaHouseButtonWidth + jinjaHouseButtonDrawOffsetX), (int) (this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight)), bitmapPaint);
        goto L25
    L60:
        if (this.jinjaHouseButtonBitmap0 == null) goto L25;
        canvas.drawBitmap(this.jinjaHouseButtonBitmap0, new Rect(0, 0, this.jinjaHouseButtonBitmap0.getWidth(), this.jinjaHouseButtonBitmap0.getHeight()), new Rect((int) jinjaHouseButtonDrawOffsetX, (int) this.jinjaHouseButtonOffsetY, (int) (this.jinjaHouseButtonWidth + jinjaHouseButtonDrawOffsetX), (int) (this.jinjaHouseButtonOffsetY + this.jinjaHouseButtonHeight)), bitmapPaint);
    L25:
        if (this.timeDoorButtonStatus != 0) goto L32;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        float timeDoorButtonDrawOffsetX = this.timeDoorButtonOffsetX + drawButtonOffsetX;
        if (this.touchButtonIndex != 2) goto L63;
        if (this.timeDoorButtonBitmap1 == null) goto L32;
        canvas.drawBitmap(this.timeDoorButtonBitmap1, new Rect(0, 0, this.timeDoorButtonBitmap1.getWidth(), this.timeDoorButtonBitmap1.getHeight()), new Rect((int) timeDoorButtonDrawOffsetX, (int) this.timeDoorButtonOffsetY, (int) (this.timeDoorButtonWidth + timeDoorButtonDrawOffsetX), (int) (this.timeDoorButtonOffsetY + this.timeDoorButtonHeight)), bitmapPaint);
        goto L32
    L63:
        if (this.timeDoorButtonBitmap0 == null) goto L32;
        canvas.drawBitmap(this.timeDoorButtonBitmap0, new Rect(0, 0, this.timeDoorButtonBitmap0.getWidth(), this.timeDoorButtonBitmap0.getHeight()), new Rect((int) timeDoorButtonDrawOffsetX, (int) this.timeDoorButtonOffsetY, (int) (this.timeDoorButtonWidth + timeDoorButtonDrawOffsetX), (int) (this.timeDoorButtonOffsetY + this.timeDoorButtonHeight)), bitmapPaint);
    L32:
        if (this.monsterVillageButtonStatus != 0) goto L39;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        float monsterVillageButtonDrawOffsetX = this.monsterVillageButtonOffsetX + drawButtonOffsetX;
        if (this.touchButtonIndex != 3) goto L66;
        if (this.monsterVillageButtonBitmap1 == null) goto L39;
        canvas.drawBitmap(this.monsterVillageButtonBitmap1, new Rect(0, 0, this.monsterVillageButtonBitmap1.getWidth(), this.monsterVillageButtonBitmap1.getHeight()), new Rect((int) monsterVillageButtonDrawOffsetX, (int) this.monsterVillageButtonOffsetY, (int) (this.monsterVillageButtonWidth + monsterVillageButtonDrawOffsetX), (int) (this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight)), bitmapPaint);
        goto L39
    L66:
        if (this.monsterVillageButtonBitmap0 == null) goto L39;
        canvas.drawBitmap(this.monsterVillageButtonBitmap0, new Rect(0, 0, this.monsterVillageButtonBitmap0.getWidth(), this.monsterVillageButtonBitmap0.getHeight()), new Rect((int) monsterVillageButtonDrawOffsetX, (int) this.monsterVillageButtonOffsetY, (int) (this.monsterVillageButtonWidth + monsterVillageButtonDrawOffsetX), (int) (this.monsterVillageButtonOffsetY + this.monsterVillageButtonHeight)), bitmapPaint);
    L39:
        if (this.nowCharacterDisplayViewsArrayList == null) goto L44;
        int i = 0;
    L42:
        if (i >= this.nowCharacterDisplayViewsArrayList.size()) goto L44;
        CharacterDisplayUnit characterDisplayUnit = this.nowCharacterDisplayViewsArrayList.get(i);
        if (characterDisplayUnit == null) goto L105;
        float offsetXFloat = drawCharacterOffsetX + characterDisplayUnit.frameOffsetX;
        if (offsetXFloat < this.CHARACTERUNITVIEW_OFFSET_X_MIN) goto L105;
        if (offsetXFloat >= this.CHARACTERUNITVIEW_OFFSET_X_MAX) goto L105;
        if (characterDisplayUnit.eggID < 0) goto L105;
        characterDisplayUnit.animeLoop();
        Bitmap shadowImageBitmap = null;
        if (characterDisplayUnit.eggID != 0) goto L108;
        if (characterDisplayUnit.characterID != 32) goto L106;
        shadowImageBitmap = this.appDelegate.shadow_1_Bitmap;
    L81:
        if (shadowImageBitmap == null) goto L83;
        int imgOffsetX = (int) ((characterDisplayUnit.frameOffsetX + drawCharacterOffsetX) + characterDisplayUnit.shadowViewOriginX);
        int imgOffsetY = (int) (characterDisplayUnit.frameOffsetY + characterDisplayUnit.shadowViewOriginY);
        bitmapPaint.setAlpha(178);
        canvas.drawBitmap(shadowImageBitmap, new Rect(0, 0, shadowImageBitmap.getWidth(), shadowImageBitmap.getHeight()), new Rect(imgOffsetX, imgOffsetY, (int) (imgOffsetX + characterDisplayUnit.shadowViewSizeHeight), (int) (imgOffsetY + characterDisplayUnit.shadowViewSizeWidth)), bitmapPaint);
    L83:
        Bitmap characterBitmap = null;
        short characterBitmapDrawType = 0;
        if (characterDisplayUnit.eggID != 0) goto L150;
        if (this.appDelegate.character0Image0ArrayList != null) goto L88;
    L100:
        if (characterBitmap == null) goto L105;
        int imgOffsetX2 = (int) ((characterDisplayUnit.frameOffsetX + drawCharacterOffsetX) + characterDisplayUnit.imageView0OriginX);
        int imgOffsetY2 = (int) (characterDisplayUnit.frameOffsetY + characterDisplayUnit.imageView0OriginY);
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        canvas.save();
        if (characterBitmapDrawType != 1) goto L104;
        canvas.scale(-1.0f, 1.0f, imgOffsetX2 + (characterDisplayUnit.imageView0SizeWidth / 2.0f), BitmapDescriptorFactory.HUE_RED);
    L104:
        canvas.drawBitmap(characterBitmap, new Rect(0, 0, characterBitmap.getWidth(), characterBitmap.getHeight()), new Rect(imgOffsetX2, imgOffsetY2, (int) (imgOffsetX2 + characterDisplayUnit.imageView0SizeWidth), (int) (imgOffsetY2 + characterDisplayUnit.imageView0SizeHeight)), bitmapPaint);
        canvas.restore();
        goto L105
    L88:
        if (characterDisplayUnit.characterID < 0) goto L100;
        if (characterDisplayUnit.characterID >= this.appDelegate.character0Image0ArrayList.size()) goto L100;
        short characterID = characterDisplayUnit.characterID;
        if (characterDisplayUnit.imageView0TransformX <= BitmapDescriptorFactory.HUE_RED) goto L94;
        characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID);
        goto L100
    L94:
        if (characterID != 84) goto L111;
        short characterImageIndex = (short) (characterID - 84);
        if (characterImageIndex < 0) goto L100;
        if (characterImageIndex >= this.appDelegate.character0RevImage0ArrayList.size()) goto L100;
        characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex);
        goto L100
    L111:
        if (characterID != 84) goto L113;
    L142:
        short characterImageIndex2 = (short) ((characterID - 89) + 1);
        if (characterImageIndex2 < 0) goto L100;
        if (characterImageIndex2 >= this.appDelegate.character0RevImage0ArrayList.size()) goto L100;
        characterBitmap = this.appDelegate.character0RevImage0ArrayList.get(characterImageIndex2);
        goto L100
    L113:
        if (characterID == 89) goto L142;
        if (characterID == 90) goto L142;
        if (characterID == 91) goto L142;
        if (characterID == 92) goto L142;
        if (characterID == 93) goto L142;
        if (characterID == 94) goto L142;
        if (characterID == 95) goto L142;
        if (characterID == 96) goto L142;
        if (characterID == 97) goto L142;
        if (characterID == 98) goto L142;
        if (characterID == 99) goto L142;
        if (characterID == 100) goto L142;
        if (characterID == 101) goto L142;
        if (characterID == 102) goto L142;
        if (characterID == 103) goto L142;
        characterBitmap = this.appDelegate.character0Image0ArrayList.get(characterID);
        characterBitmapDrawType = 1;
        goto L100
    L150:
        if (characterDisplayUnit.eggID != 1) goto L100;
        if (this.appDelegate.character1Image0ArrayList == null) goto L100;
        if (characterDisplayUnit.characterID < 0) goto L100;
        if (characterDisplayUnit.characterID >= this.appDelegate.character1Image0ArrayList.size()) goto L100;
        characterBitmap = this.appDelegate.character1Image0ArrayList.get(characterDisplayUnit.characterID);
        if (characterDisplayUnit.imageView0TransformX > BitmapDescriptorFactory.HUE_RED) goto L100;
        characterBitmapDrawType = 1;
        goto L100
    L106:
        shadowImageBitmap = this.appDelegate.shadow_0_Bitmap;
        goto L81
    L108:
        if (characterDisplayUnit.eggID != 1) goto L81;
        shadowImageBitmap = this.appDelegate.shadow_0_Bitmap;
    L105:
        i = i + 1;
    L44:
        if (this.farmUnit == null) goto L176;
        if (this.farmUnit.farmFrontViewUnit == null) goto L176;
        if (this.farmUnit.farmFrontViewUnit.farmFrontBitmapIndex != 0) goto L162;
        if (this.farmUnit.farmFrontViewUnit.farmFrontBitmap0 == null) goto L176;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        if (this.appDelegate.isRetina4 == true) goto L160;
        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap0, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap0.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
        goto L176
    L160:
        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap0, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap0.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
        goto L176
    L162:
        if (this.farmUnit.farmFrontViewUnit.farmFrontBitmapIndex != 1) goto L176;
        if (this.farmUnit.farmFrontViewUnit.farmFrontBitmap1 == null) goto L176;
        bitmapPaint.setAlpha(MotionEventCompat.ACTION_MASK);
        if (this.appDelegate.isRetina4 == true) goto L168;
        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap1, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap1.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
        goto L176
    L168:
        canvas.drawBitmap(this.farmUnit.farmFrontViewUnit.farmFrontBitmap1, new Rect((int) drawBackGroundOffsetX, 0, drawBackGroundBitmapDrawWidth, this.farmUnit.farmFrontViewUnit.farmFrontBitmap1.getHeight()), new Rect(0, (int) this.farmFrontViewUnitOffsetY, (int) this.finalWidth, (int) this.farmFrontViewUnitHeight), bitmapPaint);
    }

    public void onDestroy() {
        clearBackgroundBitmap();
        this.myDraw = null;
        if (this.nowCharacterDisplayViewsArrayList == null) goto L5;
        this.nowCharacterDisplayViewsArrayList.clear();
        this.nowCharacterDisplayViewsArrayList = null;
    L5:
        this.farmUnit = null;
        this.appDelegate = null;
    }
}
