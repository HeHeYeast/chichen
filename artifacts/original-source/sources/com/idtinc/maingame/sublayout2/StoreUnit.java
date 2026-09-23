package com.idtinc.maingame.sublayout2;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.FarmUnitDictionary;
import com.idtinc.ckunit.ToolDataDictionary;
import com.idtinc.ckunit.ToolLevelDictionary;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import com.idtinc.custom.CPDisplayUnit;
import com.idtinc.maingame.MainGameViewController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class StoreUnit implements AlertUnitType0Delegate {
    private float LISTBACKVIEW_HEIGHT;
    private float LISTBACKVIEW_OFFSET_X;
    private float LISTBACKVIEW_OFFSET_Y;
    private float LISTBACKVIEW_WIDTH;
    private float LISTSCROLLVIEW_HEIGHT;
    private float LISTSCROLLVIEW_OFFSET_X;
    private float LISTSCROLLVIEW_OFFSET_Y;
    private float LISTSCROLLVIEW_WIDTH;
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private CPDisplayUnit cpDisplayUnit;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MainGameViewController mainGameViewController;
    public short nowStatus;
    public StoreBackViewUnit storeBackViewUnit;
    private StoreListFrontViewUnit storeListFrontViewUnit;
    private StoreListScrollViewUnit01 storeListScrollViewUnit01;
    private StoreListScrollViewUnit02 storeListScrollViewUnit02;
    private StoreListScrollViewUnit03 storeListScrollViewUnit03;
    public StoreListSelectUnit storeListSelectUnit;
    private StoreListUnit storeListUnit;
    private float zoomRate;

    public StoreUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameViewController, AppDelegate _appDelegate) throws IOException {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.LISTBACKVIEW_OFFSET_X = 12.0f;
        this.LISTBACKVIEW_OFFSET_Y = 86.0f;
        this.LISTBACKVIEW_WIDTH = 296.0f;
        this.LISTBACKVIEW_HEIGHT = 364.0f;
        this.LISTSCROLLVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + 13.0f;
        this.LISTSCROLLVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + 35.0f + 17.0f;
        this.LISTSCROLLVIEW_WIDTH = 270.0f;
        this.LISTSCROLLVIEW_HEIGHT = 300.0f;
        this.appDelegate = _appDelegate;
        this.mainGameViewController = _mainGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.LISTBACKVIEW_OFFSET_X = 12.0f * this.zoomRate;
        this.LISTBACKVIEW_OFFSET_Y = 86.0f * this.zoomRate;
        this.LISTBACKVIEW_WIDTH = 296.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTBACKVIEW_HEIGHT = 304.0f * this.zoomRate;
        } else {
            this.LISTBACKVIEW_HEIGHT = 364.0f * this.zoomRate;
        }
        this.LISTSCROLLVIEW_OFFSET_X = this.LISTBACKVIEW_OFFSET_X + (13.0f * this.zoomRate);
        this.LISTSCROLLVIEW_OFFSET_Y = this.LISTBACKVIEW_OFFSET_Y + (52.0f * this.zoomRate);
        this.LISTSCROLLVIEW_WIDTH = 270.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTSCROLLVIEW_HEIGHT = 240.0f * this.zoomRate;
        } else {
            this.LISTSCROLLVIEW_HEIGHT = 300.0f * this.zoomRate;
        }
        this.storeBackViewUnit = new StoreBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.storeListUnit = new StoreListUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.storeListSelectUnit = new StoreListSelectUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.finalWidth, this.finalWidth, this.zoomRate, this, this.appDelegate);
        this.storeListScrollViewUnit01 = new StoreListScrollViewUnit01(this.LISTSCROLLVIEW_OFFSET_X, this.LISTSCROLLVIEW_OFFSET_Y, this.LISTSCROLLVIEW_WIDTH, this.LISTSCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.storeListScrollViewUnit01.hidden = true;
        this.storeListScrollViewUnit02 = new StoreListScrollViewUnit02(this.LISTSCROLLVIEW_OFFSET_X, this.LISTSCROLLVIEW_OFFSET_Y, this.LISTSCROLLVIEW_WIDTH, this.LISTSCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.storeListScrollViewUnit02.hidden = true;
        this.storeListScrollViewUnit03 = new StoreListScrollViewUnit03(this.LISTSCROLLVIEW_OFFSET_X, this.LISTSCROLLVIEW_OFFSET_Y, this.LISTSCROLLVIEW_WIDTH, this.LISTSCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.storeListScrollViewUnit03.hidden = true;
        this.storeListFrontViewUnit = new StoreListFrontViewUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.cpDisplayUnit = new CPDisplayUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.cpDisplayUnit.setBackViewParams(210.0f, 400.0f);
        } else {
            this.cpDisplayUnit.setBackViewParams(210.0f, 460.0f);
        }
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        }
        this.alertUnitType0.delegate = this;
        refreshBitmap();
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void doInit() {
        start();
    }

    public void start() throws Resources.NotFoundException {
        changeNowStatus(0);
    }

    public void stop() throws Resources.NotFoundException {
        changeNowStatus(1);
    }

    public void changeNowStatus(int _newStatus) throws Resources.NotFoundException {
        Log.d("HelpLayout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            this.hidden = true;
            this.nowStatus = (short) -1;
        } else if (_newStatus == 0) {
            this.nowStatus = (short) 0;
            reload();
            this.hidden = false;
        } else if (_newStatus == 1) {
            this.hidden = true;
            this.nowStatus = (short) 1;
        }
    }

    public void reload() throws Resources.NotFoundException {
        refreshToolUnitDictionarysArray();
        refreshListView();
        refreshPoint();
    }

    public void doDisplay() {
        refreshToolUnitDictionarysArray();
        refreshListView();
        refreshPoint();
        changeListTo((short) 2, false);
        displayInitManual();
        readtDoCheckAndShowStoreMessagesArray0WithDelay(1000);
    }

    public void aotoOpenBonusPage() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("aoto_open_bonus_page", false) && this.appDelegate.checkAdColonyV4VCF()) {
                openBonusLayout();
            }
        }
    }

    public void doHidden() {
        hiddenAlert();
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.changGiftButtonStatus((short) -1);
        }
    }

    public void displayInitManual() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("init_manual_store", false)) {
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
            editor.putBoolean("init_manual_store", false);
            editor.commit();
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『ストア』の操作解説を見ますか？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『商店』的教學說明嗎？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『商店』的教学说明吗？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "Do you want to read ";
                contentLabelString2 = "the \"Store\" manual?";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
            this.alertUnitType0.tag = (short) -100;
            this.alertUnitType0.subTag = (short) -1;
            popAlert();
        }
    }

    public void cancelInitManual() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "解説を見たい時、『その他』を";
            contentLabelString2 = "タップして下さい。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "當你想看說明時,就點選『其他』吧。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "当你想看说明时,就点击『其他』吧。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Tap \"Other\" when you want";
            contentLabelString2 = " to red the manuals.";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
        this.alertUnitType0.tag = (short) -99;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    public void doWillTerminate() {
        Log.d("StoreLayout", "doWillTerminate");
        hiddenAlert();
    }

    public void doWillEnterForeground() {
        Log.d("StoreLayout", "doWillEnterForeground");
        reload();
    }

    public void refreshToolUnitDictionarysArray() throws Resources.NotFoundException {
        short countShort;
        FarmUnitDictionary unitDictionary;
        FarmUnitDictionary unitDictionary2;
        ToolUnitDictionary toolUnitDictionary;
        ToolUnitDictionary toolUnitDictionary2;
        if (this.appDelegate.timeSaveDictionary != null) {
            boolean saveF = false;
            if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null) {
                short tool_1_6_level = -1;
                for (int i = 0; i < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size(); i++) {
                    ArrayList<ToolUnitDictionary> toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(i);
                    Log.d("StoreLayout", "toolDictionarysArray=" + i);
                    if (i != 0) {
                        if (i == 1) {
                            short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
                            if (toolDictionarysArrayList != null) {
                                short levelShort = -999;
                                for (int j = 0; j < toolDictionarysArrayList.size(); j++) {
                                    Boolean unlockF = false;
                                    if (j < 6) {
                                        short preLevelShort = levelShort;
                                        levelShort = -999;
                                        ToolUnitDictionary toolUnitDictionary3 = toolDictionarysArrayList.get(j);
                                        if (toolUnitDictionary3 != null) {
                                            levelShort = toolUnitDictionary3.getLevel();
                                            if (preLevelShort >= 0 && levelShort == -2) {
                                                levelShort = -1;
                                                toolUnitDictionary3.setLevel((short) -1);
                                                unlockF = true;
                                                saveF = true;
                                            }
                                        }
                                    } else if (j == 6) {
                                        if (tool_0_0_level >= 3 && (toolUnitDictionary2 = toolDictionarysArrayList.get(j)) != null && (levelShort = toolUnitDictionary2.getLevel()) == -2) {
                                            levelShort = -1;
                                            toolUnitDictionary2.setLevel((short) -1);
                                            unlockF = true;
                                            saveF = true;
                                        }
                                    } else if (j == 7 && tool_0_0_level >= 3 && tool_1_6_level >= 2 && (toolUnitDictionary = toolDictionarysArrayList.get(j)) != null && (levelShort = toolUnitDictionary.getLevel()) == -2) {
                                        levelShort = -1;
                                        toolUnitDictionary.setLevel((short) -1);
                                        unlockF = true;
                                        saveF = true;
                                    }
                                    if (unlockF.booleanValue()) {
                                        this.appDelegate.addMessageToStoreMessagesArray0((short) 1, (short) j);
                                        this.appDelegate.displayFullAdView();
                                    }
                                    if (j != 0 && j != 1 && j != 2 && j != 3 && j != 4 && j != 5) {
                                        if (j == 6) {
                                            tool_1_6_level = levelShort;
                                        } else if (j == 7) {
                                        }
                                    }
                                }
                            }
                        } else if (i == 2) {
                            short tool_0_0_level2 = this.appDelegate.getTool0LevelWithIndex((short) 0);
                            short tool_1_0_level = this.appDelegate.getTool1LevelWithIndex((short) 0);
                            short tool_1_1_level = this.appDelegate.getTool1LevelWithIndex((short) 1);
                            short tool_1_2_level = this.appDelegate.getTool1LevelWithIndex((short) 2);
                            short tool_1_3_level = this.appDelegate.getTool1LevelWithIndex((short) 3);
                            short tool_1_4_level = this.appDelegate.getTool1LevelWithIndex((short) 4);
                            short tool_1_5_level = this.appDelegate.getTool1LevelWithIndex((short) 5);
                            tool_1_6_level = this.appDelegate.getTool1LevelWithIndex((short) 6);
                            short tool_1_7_level = this.appDelegate.getTool1LevelWithIndex((short) 7);
                            float totalCharsCnt = BitmapDescriptorFactory.HUE_RED;
                            if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList != null) {
                                for (int j2 = 0; j2 < this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size(); j2++) {
                                    ArrayList<FarmUnitDictionary> unitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(j2);
                                    if (unitDictionarysArrayList != null) {
                                        for (int k = 0; k < unitDictionarysArrayList.size(); k++) {
                                            FarmUnitDictionary farmUnitDictionary = unitDictionarysArrayList.get(k);
                                            if (farmUnitDictionary != null) {
                                                float totalCountFloat = farmUnitDictionary.getTotalCount();
                                                if (totalCountFloat > 0.0d) {
                                                    totalCharsCnt += totalCountFloat;
                                                }
                                                Log.d("StoreLayout", String.valueOf(i) + "," + j2 + "," + k + ":" + totalCountFloat);
                                            }
                                        }
                                    }
                                }
                            }
                            Log.d("StoreLayout", "totalCharsCnt:" + totalCharsCnt);
                            Boolean character_1_0_UnlockF = false;
                            FarmUnitDictionary check_1_0_unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId((short) 1, (short) 0);
                            if (check_1_0_unitDictionary != null) {
                                float totalCountFloat2 = check_1_0_unitDictionary.getTotalCount();
                                if (totalCountFloat2 >= 1.0f) {
                                    character_1_0_UnlockF = true;
                                }
                            }
                            if (toolDictionarysArrayList != null) {
                                for (int j3 = 0; j3 < toolDictionarysArrayList.size(); j3++) {
                                    ToolUnitDictionary toolUnitDictionary4 = toolDictionarysArrayList.get(j3);
                                    if (toolUnitDictionary4 != null) {
                                        short countShort2 = toolUnitDictionary4.getCount();
                                        if (countShort2 <= -1) {
                                            if (j3 == 1 || j3 == 2) {
                                                if (tool_1_1_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 3) {
                                                if (tool_1_2_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 4) {
                                                if (tool_1_2_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 5 || j3 == 6 || j3 == 7) {
                                                if (tool_1_3_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 8 || j3 == 9 || j3 == 10) {
                                                if (tool_1_4_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                                if (j3 == 9 && tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 11) {
                                                if (countShort2 != 0 && (unitDictionary2 = this.appDelegate.getCharacterUnitDictionaryWithId((short) 0, (short) 1)) != null) {
                                                    float totalCountFloat3 = unitDictionary2.getTotalCount();
                                                    if (totalCountFloat3 >= 1.0f) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                                if (countShort2 != 0 && (unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId((short) 1, (short) 1)) != null) {
                                                    float totalCountFloat4 = unitDictionary.getTotalCount();
                                                    if (totalCountFloat4 >= 1.0f) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 12) {
                                                if (tool_1_5_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_5_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 13) {
                                                if (tool_1_5_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 14) {
                                                if (tool_1_5_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                                if (tool_1_3_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 15) {
                                                if (totalCharsCnt >= 100.0d) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 16) {
                                                FarmUnitDictionary unitDictionary3 = this.appDelegate.getCharacterUnitDictionaryWithId((short) 0, (short) 18);
                                                if (unitDictionary3 != null) {
                                                    float totalCountFloat5 = unitDictionary3.getTotalCount();
                                                    if (totalCountFloat5 >= 1.0f) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 17) {
                                                if (totalCharsCnt >= 300.0d) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 18) {
                                                if (totalCharsCnt >= 500.0d) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 19) {
                                                if (tool_1_1_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 20) {
                                                if (tool_1_1_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 21 || j3 == 22 || j3 == 24) {
                                                if (tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 23) {
                                                if (tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (tool_1_4_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (tool_1_5_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_1_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 25) {
                                                if (tool_1_3_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 26) {
                                                FarmUnitDictionary unitDictionary4 = this.appDelegate.getCharacterUnitDictionaryWithId((short) 0, (short) 35);
                                                if (unitDictionary4 != null) {
                                                    float totalCountFloat6 = unitDictionary4.getTotalCount();
                                                    if (totalCountFloat6 >= 1.0f) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 27) {
                                                if (tool_1_4_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue()) {
                                                    if (tool_1_0_level >= 0) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_2_level >= 0) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_5_level >= 0) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 28) {
                                                if (tool_1_5_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_5_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 29) {
                                                if (tool_1_5_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue()) {
                                                    if (tool_1_3_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_4_level >= 1) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_5_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_6_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 30 || j3 == 31) {
                                                if (tool_1_1_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 32) {
                                                if (tool_1_3_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 33) {
                                                if (tool_1_4_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_4_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 34) {
                                                if (tool_1_5_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_1_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 35) {
                                                if (totalCharsCnt >= 2000.0d) {
                                                    countShort2 = 0;
                                                    if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("gift_tool_2_35", false)) {
                                                        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                                                        editor.putBoolean("gift_tool_2_35", false);
                                                        editor.commit();
                                                    }
                                                }
                                            } else if (j3 == 36) {
                                                if (tool_0_0_level2 >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 37) {
                                                if (tool_1_1_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 38) {
                                                if (tool_1_1_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 39) {
                                                if (tool_1_2_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 40) {
                                                if (tool_1_2_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 41) {
                                                if (tool_1_3_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                                if (tool_1_4_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_1_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 42) {
                                                if (tool_1_4_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 43) {
                                                if (tool_1_5_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 44) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_1_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 45) {
                                                if (character_1_0_UnlockF.booleanValue()) {
                                                    if (tool_1_3_level >= 0) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_5_level >= 1) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 46) {
                                                if (character_1_0_UnlockF.booleanValue()) {
                                                    if (tool_1_1_level >= 1) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_2_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_3_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                    if (tool_1_4_level >= 2) {
                                                        countShort2 = 0;
                                                    }
                                                }
                                            } else if (j3 == 47) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 48) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_2_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 49) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_5_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 50) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_1_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 51) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_2_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 52) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_2_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 53) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_3_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 54) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_4_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 55) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_4_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 56) {
                                                if (tool_1_6_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 57) {
                                                if (tool_1_6_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 58) {
                                                if (tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 59) {
                                                if (tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 60) {
                                                if (tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 61) {
                                                if (tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 62) {
                                                if (tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 63) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 64) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 0) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 65) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 66) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 67) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_6_level >= 2) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 71) {
                                                if (tool_1_7_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 72) {
                                                if (tool_1_7_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 73) {
                                                if (character_1_0_UnlockF.booleanValue() && tool_1_7_level >= 1) {
                                                    countShort2 = 0;
                                                }
                                            } else if (j3 == 74 && character_1_0_UnlockF.booleanValue() && tool_1_7_level >= 1) {
                                                countShort2 = 0;
                                            }
                                            if (countShort2 == 0) {
                                                this.appDelegate.addMessageToStoreMessagesArray0((short) 2, (short) j3);
                                                this.appDelegate.displayFullAdView();
                                                toolUnitDictionary4.setCount(countShort2);
                                                saveF = true;
                                            }
                                        }
                                        if (j3 == 68 && this.appDelegate.defaultSharedPreferences != null) {
                                            if (countShort2 <= 0) {
                                                SharedPreferences.Editor editor2 = this.appDelegate.defaultSharedPreferences.edit();
                                                if (this.appDelegate.defaultSharedPreferences.getBoolean("gift_tool_2_68", false)) {
                                                    if (this.appDelegate.getTool2TotalPurchasedCount() >= 30) {
                                                        tool2TotalPurchasedCountOverAlert();
                                                    } else {
                                                        countShort2 = 1;
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                                        String preDateString = "";
                                                        if (this.appDelegate.defaultSharedPreferences.getString("gift_tool_2_68_date", "") != null) {
                                                            preDateString = this.appDelegate.defaultSharedPreferences.getString("gift_tool_2_68_date", "");
                                                        }
                                                        String nowDateString = sdf.format(new Date());
                                                        if (!preDateString.equals(nowDateString)) {
                                                            editor2.putString("gift_tool_2_68_date", nowDateString);
                                                            toolUnitDictionary4.setCount((short) 1);
                                                            saveF = true;
                                                            this.appDelegate.addMessageToStoreMessagesArray0((short) 2, (short) 68);
                                                            this.appDelegate.displayFullAdView();
                                                        }
                                                    }
                                                }
                                                editor2.commit();
                                            }
                                            this.appDelegate.set_gift_tool_2((short) 68, false);
                                        }
                                        if (j3 == 69 && this.appDelegate.defaultSharedPreferences != null) {
                                            if (countShort2 <= 0) {
                                                SharedPreferences.Editor editor3 = this.appDelegate.defaultSharedPreferences.edit();
                                                if (this.appDelegate.defaultSharedPreferences.getBoolean("gift_tool_2_69", false)) {
                                                    if (this.appDelegate.getTool2TotalPurchasedCount() >= 30) {
                                                        tool2TotalPurchasedCountOverAlert();
                                                    } else {
                                                        countShort2 = 1;
                                                        toolUnitDictionary4.setCount((short) 1);
                                                        saveF = true;
                                                        this.appDelegate.addMessageToStoreMessagesArray0((short) 2, (short) 69);
                                                        this.appDelegate.displayFullAdView();
                                                    }
                                                }
                                                editor3.commit();
                                            }
                                            this.appDelegate.set_gift_tool_2((short) 69, false);
                                        }
                                        if (j3 == 70 && this.appDelegate.defaultSharedPreferences != null) {
                                            if (countShort2 <= 0) {
                                                SharedPreferences.Editor editor4 = this.appDelegate.defaultSharedPreferences.edit();
                                                if (this.appDelegate.defaultSharedPreferences.getBoolean("gift_tool_2_70", false)) {
                                                    if (this.appDelegate.getTool2TotalPurchasedCount() >= 30) {
                                                        tool2TotalPurchasedCountOverAlert();
                                                    } else {
                                                        toolUnitDictionary4.setCount((short) 1);
                                                        saveF = true;
                                                        this.appDelegate.addMessageToStoreMessagesArray0((short) 2, (short) 70);
                                                        this.appDelegate.displayFullAdView();
                                                    }
                                                }
                                                editor4.commit();
                                            }
                                            this.appDelegate.set_gift_tool_2((short) 70, false);
                                        }
                                    }
                                }
                            }
                        } else if (i == 3 && toolDictionarysArrayList != null) {
                            for (int j4 = 0; j4 < toolDictionarysArrayList.size(); j4++) {
                                ToolUnitDictionary toolUnitDictionary5 = toolDictionarysArrayList.get(j4);
                                if (toolUnitDictionary5 != null && (countShort = toolUnitDictionary5.getCount()) <= -1) {
                                    if (j4 != 0 && j4 == 1 && (this.appDelegate.get_idt_account_logged_in().booleanValue() || this.appDelegate.get_fb_account_logged_in().booleanValue())) {
                                        countShort = 0;
                                        this.appDelegate.addMessageToStoreMessagesArray0((short) 3, (short) 1);
                                        this.appDelegate.displayFullAdView();
                                    }
                                    if (countShort == 0) {
                                        toolUnitDictionary5.setCount(countShort);
                                        saveF = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (saveF) {
                this.mainGameViewController.refreshAndSave();
                readtDoCheckAndShowStoreMessagesArray0WithDelay(100);
            }
        }
    }

    public void unLockItem_2_AlertWithTypeId(short _typeID, short _toolID) throws Resources.NotFoundException {
        ToolDataDictionary toolDataDictionary;
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        String nameString = "";
        if ((_typeID == 1 || _typeID == 2 || _typeID == 3) && (toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID)) != null) {
            nameString = languageString.equals("ja-JP") ? toolDataDictionary.getTitleJa() : (languageString.equals("zh-TW") || languageString.equals("zh-HK")) ? toolDataDictionary.getTitleZhTW() : languageString.equals("zh-CN") ? toolDataDictionary.getTitleZhCN() : toolDataDictionary.getTitleEn();
        }
        if (nameString == null) {
            nameString = "";
        }
        String titleLabelString = "";
        String contentLabelString0 = "";
        String contentLabelString1 = "";
        String contentLabelString2 = "";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
        if (_typeID == 1) {
            titleLabelString = this.appDelegate.getResources().getString(R.string.Kitchenware);
            if (languageString.equals("ja-JP")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『" + nameString + "』が購入できるます。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已經可以購買『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已经可以购买『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "\"" + nameString + "\" is unlocked.";
                contentLabelString3 = "";
                contentLabelString4 = "";
            }
        } else if (_typeID == 2) {
            titleLabelString = this.appDelegate.getResources().getString(R.string.Ingredient);
            if (_toolID == 68 || _toolID == 69 || _toolID == 70) {
                if (languageString.equals("ja-JP")) {
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "『" + nameString + "』を獲得しました。";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK") || languageString.equals("zh-CN")) {
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "得到了『" + nameString + "』。";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                } else {
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "You got a \"" + nameString + "\".";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                }
            } else if (languageString.equals("ja-JP")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『" + nameString + "』が購入できるます。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已經可以購買『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已经可以购买『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "\"" + nameString + "\" is unlocked.";
                contentLabelString3 = "";
                contentLabelString4 = "";
            }
        } else if (_typeID == 3) {
            titleLabelString = this.appDelegate.getResources().getString(R.string.Egg);
            if (languageString.equals("ja-JP")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『" + nameString + "』が購入できるます。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已經可以購買『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你已经可以购买『" + nameString + "』了。";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else {
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "\"" + nameString + "\" is unlocked.";
                contentLabelString3 = "";
                contentLabelString4 = "";
            }
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 12.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
        if (_typeID == 1) {
            if (this.appDelegate.tool1Level0Image0ArrayList != null && _toolID < this.appDelegate.tool1Level0Image0ArrayList.size()) {
                this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool1Level0Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
            }
            this.alertUnitType0.smallImage0OffsetX = this.alertUnitType0.backViewOffsetX + (130.0f * this.zoomRate);
            this.alertUnitType0.smallImage0OffsetY = this.alertUnitType0.backViewOffsetY + (40.0f * this.zoomRate);
        } else if (_typeID == 2) {
            if (this.appDelegate.tool2Level0Image0ArrayList != null && _toolID < this.appDelegate.tool2Level0Image0ArrayList.size()) {
                this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool2Level0Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
            }
            this.alertUnitType0.smallImage0OffsetX = this.alertUnitType0.backViewOffsetX + (130.0f * this.zoomRate);
            this.alertUnitType0.smallImage0OffsetY = this.alertUnitType0.backViewOffsetY + (40.0f * this.zoomRate);
        } else if (_typeID == 3) {
            if (_toolID == 0) {
                if (this.appDelegate.egg0ImageArrayList != null && _toolID < this.appDelegate.egg0ImageArrayList.size()) {
                    this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.egg0ImageArrayList.get(0), MotionEventCompat.ACTION_MASK);
                }
            } else if (_toolID == 1 && this.appDelegate.egg1ImageArrayList != null && _toolID < this.appDelegate.egg1ImageArrayList.size()) {
                this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.egg1ImageArrayList.get(0), MotionEventCompat.ACTION_MASK);
            }
            this.alertUnitType0.smallImage0OffsetX = this.alertUnitType0.backViewOffsetX + (130.0f * this.zoomRate);
            this.alertUnitType0.smallImage0OffsetY = this.alertUnitType0.backViewOffsetY + (35.0f * this.zoomRate);
        }
        this.alertUnitType0.tag = (short) 100;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void refreshListView() {
        if (this.storeListScrollViewUnit01 != null) {
            this.storeListScrollViewUnit01.reload();
        }
        if (this.storeListScrollViewUnit02 != null) {
            this.storeListScrollViewUnit02.reload();
        }
        if (this.storeListScrollViewUnit03 != null) {
            this.storeListScrollViewUnit03.reload();
        }
    }

    public void refreshPoint() {
        this.cpDisplayUnit.refresh();
    }

    public void readyBuyWithId(short _typeID, short _toolID) throws Resources.NotFoundException {
        String titleLabelString;
        String contentLabelString1;
        String contentLabelString3;
        String contentLabelString4;
        String titleLabelString2;
        String contentLabelString0;
        String contentLabelString12;
        String contentLabelString2;
        String contentLabelString32;
        String contentLabelString42;
        String titleLabelString3;
        String contentLabelString02;
        String contentLabelString13;
        String contentLabelString22;
        String contentLabelString33;
        String contentLabelString43;
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        ToolUnitDictionary toolUnitDictionary;
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList2;
        ToolLevelDictionary newToolLevelDictionary;
        ToolLevelDictionary newToolLevelDictionary2;
        ToolLevelDictionary newToolLevelDictionary3;
        Log.d("StoreLayout", "typeID=" + ((int) _typeID) + "toolID=" + ((int) _toolID));
        if (this.appDelegate.timeSaveDictionary != null) {
            if (this.storeBackViewUnit != null) {
                this.storeBackViewUnit.changGiftButtonStatus((short) -1);
            }
            short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
            String languageString = this.appDelegate.getLocaleLanguage();
            short lvBuyCpIndex = -1;
            int buyCp = -1;
            String lvMinString = "-";
            String lvCookCPString = "-";
            if (_typeID != 0) {
                if (_typeID == 1) {
                    short levelShort = this.appDelegate.getTool1LevelWithIndex(_toolID);
                    if (levelShort == -1) {
                        lvBuyCpIndex = 0;
                    } else if (levelShort == 0) {
                        if (tool_0_0_level > 0) {
                            lvBuyCpIndex = 1;
                        }
                    } else if (levelShort == 1 && tool_0_0_level > 1) {
                        lvBuyCpIndex = 2;
                    }
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary != null && lvBuyCpIndex >= 0 && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary3 = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex)) != null) {
                        buyCp = newToolLevelDictionary3.getLvBuyCp();
                    }
                    short cookMin = -1;
                    if (lvBuyCpIndex >= 0 && toolDataDictionary != null && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary2 = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex)) != null) {
                        cookMin = newToolLevelDictionary2.getLvMin();
                    }
                    if (cookMin >= 60) {
                        if (cookMin % 60 == 0) {
                            if (languageString.equals("ja-JP")) {
                                lvMinString = String.valueOf(cookMin / 60) + "時間";
                            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                                lvMinString = String.valueOf(cookMin / 60) + "小時";
                            } else if (languageString.equals("zh-CN")) {
                                lvMinString = String.valueOf(cookMin / 60) + "小时";
                            } else {
                                lvMinString = String.valueOf(cookMin / 60) + "hr";
                            }
                        } else if (languageString.equals("ja-JP")) {
                            lvMinString = String.valueOf(cookMin / 60) + "時間" + (cookMin % 60) + "分";
                        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                            lvMinString = String.valueOf(cookMin / 60) + "小時" + (cookMin % 60) + "分鐘";
                        } else if (languageString.equals("zh-CN")) {
                            lvMinString = String.valueOf(cookMin / 60) + "小时" + (cookMin % 60) + "分钟";
                        } else {
                            lvMinString = String.valueOf(cookMin / 60) + "hr " + (cookMin % 60) + "m";
                        }
                    } else if (cookMin > 0) {
                        if (languageString.equals("ja-JP")) {
                            lvMinString = String.valueOf((int) cookMin) + "分";
                        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                            lvMinString = String.valueOf((int) cookMin) + "分鐘";
                        } else if (languageString.equals("zh-CN")) {
                            lvMinString = String.valueOf((int) cookMin) + "分钟";
                        } else {
                            lvMinString = String.valueOf((int) cookMin) + "m";
                        }
                    }
                    short cookCP = -1;
                    if (lvBuyCpIndex >= 0 && toolDataDictionary != null && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex)) != null) {
                        cookCP = (short) newToolLevelDictionary.getLvCookCp();
                    }
                    if (cookCP >= 0) {
                        lvCookCPString = String.valueOf((int) cookCP) + " cp";
                    }
                } else if (_typeID == 2) {
                    if (this.appDelegate.getTool2TotalPurchasedCount() >= 30) {
                        tool2TotalPurchasedCountOverAlert();
                        return;
                    }
                    short countShort = -1;
                    if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && _typeID < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() && (toolUnitDictionarysArrayList2 = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && _toolID < toolUnitDictionarysArrayList2.size()) {
                        ToolUnitDictionary toolUnitDictionary2 = toolUnitDictionarysArrayList2.get(_toolID);
                        if (toolUnitDictionary2 != null && (countShort = toolUnitDictionary2.getCount()) < 0) {
                            countShort = -1;
                        }
                    }
                    ToolDataDictionary toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary2 != null && countShort >= 0) {
                        buyCp = toolDataDictionary2.getBuyCp();
                    }
                } else if (_typeID == 3) {
                    short countShort2 = -1;
                    if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && _typeID < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && _toolID < toolUnitDictionarysArrayList.size() && (toolUnitDictionary = toolUnitDictionarysArrayList.get(_toolID)) != null && (countShort2 = toolUnitDictionary.getCount()) < 0) {
                        countShort2 = -1;
                    }
                    ToolDataDictionary toolDataDictionary3 = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary3 != null && countShort2 >= 0) {
                        buyCp = toolDataDictionary3.getBuyCp();
                    }
                }
            }
            if (buyCp > 0) {
                hiddenAlert();
                if (_typeID == 1) {
                    if (languageString.equals("ja-JP")) {
                        titleLabelString3 = "購入";
                        contentLabelString02 = " 購入値段:  " + buyCp + " cp";
                        contentLabelString13 = " 調理費用:  " + lvCookCPString;
                        contentLabelString22 = " 調理時間:  " + lvMinString;
                        contentLabelString33 = "購入します。よろしいですか？";
                        contentLabelString43 = "";
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        titleLabelString3 = "購買";
                        contentLabelString02 = " 購買價格:  " + buyCp + " cp";
                        contentLabelString13 = " 調理費用:  " + lvCookCPString;
                        contentLabelString22 = " 調理時間:  " + lvMinString;
                        contentLabelString33 = "你確定要購買嗎？";
                        contentLabelString43 = "";
                    } else if (languageString.equals("zh-CN")) {
                        titleLabelString3 = "購買";
                        contentLabelString02 = " 购买价格:  " + buyCp + " cp";
                        contentLabelString13 = " 调理费用:  " + lvCookCPString;
                        contentLabelString22 = " 调理时间:  " + lvMinString;
                        contentLabelString33 = "你确定要购买吗？";
                        contentLabelString43 = "";
                    } else {
                        titleLabelString3 = "Buy";
                        contentLabelString02 = "Price: " + buyCp + " cp";
                        contentLabelString13 = "Hatching Cost: " + lvCookCPString;
                        contentLabelString22 = "Hatching Time: " + lvMinString;
                        contentLabelString33 = "Are you sure you want to buy?";
                        contentLabelString43 = "";
                    }
                    this.alertUnitType0.setTitleLabelParams(titleLabelString3, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setContentLabelParams(contentLabelString02, contentLabelString13, contentLabelString22, contentLabelString33, contentLabelString43, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 8.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
                    this.alertUnitType0.contentLabelOffsetX0 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX1 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX2 = 125.0f * this.zoomRate;
                    if (lvBuyCpIndex == 0) {
                        if (this.appDelegate.tool1Level0Image0ArrayList != null && _toolID < this.appDelegate.tool1Level0Image0ArrayList.size()) {
                            this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool1Level0Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
                        }
                    } else if (lvBuyCpIndex == 1) {
                        if (this.appDelegate.tool1Level1Image0ArrayList != null && _toolID < this.appDelegate.tool1Level1Image0ArrayList.size()) {
                            this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool1Level1Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
                        }
                    } else if (lvBuyCpIndex == 2 && this.appDelegate.tool1Level2Image0ArrayList != null && _toolID < this.appDelegate.tool1Level2Image0ArrayList.size()) {
                        this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool1Level2Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
                    }
                    this.alertUnitType0.tag = _typeID;
                    this.alertUnitType0.subTag = _toolID;
                    popAlert();
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(4);
                        return;
                    }
                    return;
                }
                if (_typeID == 2) {
                    if (languageString.equals("ja-JP")) {
                        titleLabelString2 = "購入";
                        contentLabelString0 = " 購入値段:  " + buyCp + " cp";
                        if (_toolID == 18) {
                            contentLabelString12 = " 効果:  病気予防、調味";
                            contentLabelString2 = "";
                        } else if (_toolID == 36) {
                            contentLabelString12 = " 効果:  焦げ防止、調味";
                            contentLabelString2 = "";
                        } else {
                            contentLabelString12 = " 効果:  調味";
                            contentLabelString2 = "";
                        }
                        contentLabelString32 = "購入します。よろしいですか？";
                        contentLabelString42 = "";
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        titleLabelString2 = "購買";
                        contentLabelString0 = " 購買價格:  " + buyCp + "cp";
                        if (_toolID == 18) {
                            contentLabelString12 = " 功能:  預防生病、調味";
                            contentLabelString2 = "";
                        } else if (_toolID == 36) {
                            contentLabelString12 = " 功能:  防止燒焦、調味";
                            contentLabelString2 = "";
                        } else {
                            contentLabelString12 = " 功能:  調味";
                            contentLabelString2 = "";
                        }
                        contentLabelString32 = "你確定要購買嗎？";
                        contentLabelString42 = "";
                    } else if (languageString.equals("zh-CN")) {
                        titleLabelString2 = "購買";
                        contentLabelString0 = " 购买价格:  " + buyCp + "cp";
                        if (_toolID == 18) {
                            contentLabelString12 = " 功能:  预防生病丶调味";
                            contentLabelString2 = "";
                        } else if (_toolID == 36) {
                            contentLabelString12 = " 功能:  防止烧焦丶调味";
                            contentLabelString2 = "";
                        } else {
                            contentLabelString12 = " 功能:  调味";
                            contentLabelString2 = "";
                        }
                        contentLabelString32 = "你确定要购买吗？";
                        contentLabelString42 = "";
                    } else {
                        titleLabelString2 = "Buy";
                        contentLabelString0 = "Price: " + buyCp + "cp";
                        if (_toolID == 18) {
                            contentLabelString12 = "Effect: Flavoring &";
                            contentLabelString2 = "Sickness Prevention";
                        } else if (_toolID == 36) {
                            contentLabelString12 = "Effect: Flavoring &";
                            contentLabelString2 = "Burn Prevention";
                        } else {
                            contentLabelString12 = "Effect: Flavoring";
                            contentLabelString2 = "";
                        }
                        contentLabelString32 = "Are you sure you want to buy?";
                        contentLabelString42 = "";
                    }
                    this.alertUnitType0.setTitleLabelParams(titleLabelString2, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString12, contentLabelString2, contentLabelString32, contentLabelString42, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 8.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
                    this.alertUnitType0.contentLabelOffsetX0 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX1 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX2 = 125.0f * this.zoomRate;
                    if (this.appDelegate.tool2Level0Image0ArrayList != null && _toolID < this.appDelegate.tool2Level0Image0ArrayList.size()) {
                        this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.tool2Level0Image0ArrayList.get(_toolID), MotionEventCompat.ACTION_MASK);
                    }
                    this.alertUnitType0.tag = _typeID;
                    this.alertUnitType0.subTag = _toolID;
                    popAlert();
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(4);
                        return;
                    }
                    return;
                }
                if (_typeID == 3) {
                    if (languageString.equals("ja-JP")) {
                        titleLabelString = "購入";
                        contentLabelString1 = " 購入値段:  " + buyCp + " cp";
                        contentLabelString3 = "購入します。よろしいですか？";
                        contentLabelString4 = "";
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        titleLabelString = "購買";
                        contentLabelString1 = " 購買價格:  " + buyCp + "cp";
                        contentLabelString3 = "你確定要購買嗎？";
                        contentLabelString4 = "";
                    } else if (languageString.equals("zh-CN")) {
                        titleLabelString = "購買";
                        contentLabelString1 = " 购买价格:  " + buyCp + "cp";
                        contentLabelString3 = "你确定要购买吗？";
                        contentLabelString4 = "";
                    } else {
                        titleLabelString = "Buy";
                        contentLabelString1 = "Price: " + buyCp + "cp";
                        contentLabelString3 = "Are you sure you want to buy?";
                        contentLabelString4 = "";
                    }
                    this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setContentLabelParams("", contentLabelString1, "", contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 8.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
                    this.alertUnitType0.contentLabelOffsetX0 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX1 = 125.0f * this.zoomRate;
                    this.alertUnitType0.contentLabelOffsetX2 = 125.0f * this.zoomRate;
                    if (_toolID == 0) {
                        if (this.appDelegate.egg0ImageArrayList != null && this.appDelegate.egg0ImageArrayList.size() > 0) {
                            this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.egg0ImageArrayList.get(0), MotionEventCompat.ACTION_MASK);
                        }
                    } else if (_toolID == 1 && this.appDelegate.egg1ImageArrayList != null && this.appDelegate.egg1ImageArrayList.size() > 0) {
                        this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.egg1ImageArrayList.get(0), MotionEventCompat.ACTION_MASK);
                    }
                    this.alertUnitType0.tag = _typeID;
                    this.alertUnitType0.subTag = _toolID;
                    popAlert();
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(4);
                    }
                }
            }
        }
    }

    public void tool2TotalPurchasedCountOverAlert() throws Resources.NotFoundException {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "調味料は30個まで購入できます。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "調味料的購買上限是30個。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "调味料的购买上限是30个。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "You can only purchase 30 Ingredients.";
            contentLabelString3 = "";
            contentLabelString4 = "";
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
        this.alertUnitType0.tag = (short) 99;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void doBuyWithId(short _typeID, short _toolID) throws Resources.NotFoundException {
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList;
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList2;
        ToolLevelDictionary newToolLevelDictionary;
        ArrayList<ToolUnitDictionary> toolDictionarysArrayList3;
        Log.d("StoreLayout", "doBuy typeID=" + ((int) _typeID) + ",toolID" + ((int) _toolID));
        if (this.appDelegate.timeSaveDictionary != null && _typeID >= 0 && _typeID <= 3 && _toolID >= 0) {
            short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
            if (_typeID != 0) {
                if (_typeID == 1) {
                    short levelShort = this.appDelegate.getTool1LevelWithIndex(_toolID);
                    ToolUnitDictionary toolUnitDictionary = null;
                    if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && _typeID < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() && (toolDictionarysArrayList3 = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && _toolID < toolDictionarysArrayList3.size() && (toolUnitDictionary = toolDictionarysArrayList3.get(_toolID)) == null) {
                        levelShort = -2;
                    }
                    short lvBuyCpIndex = -1;
                    if (levelShort == -1) {
                        lvBuyCpIndex = 0;
                    } else if (levelShort == 0) {
                        if (tool_0_0_level > 0) {
                            lvBuyCpIndex = 1;
                        }
                    } else if (levelShort == 1 && tool_0_0_level > 1) {
                        lvBuyCpIndex = 2;
                    }
                    int buyCp = -1;
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary != null && lvBuyCpIndex >= 0 && toolDataDictionary.toolLevelsArrayList != null && lvBuyCpIndex < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(lvBuyCpIndex)) != null) {
                        buyCp = newToolLevelDictionary.getLvBuyCp();
                    }
                    if (buyCp > 0) {
                        Log.d("StoreLayout", "doBuy levelShort=" + ((int) levelShort) + ",buyCp=" + buyCp);
                        if (this.appDelegate.timeSaveDictionary != null) {
                            float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                            if (buyCp <= nowPoint && toolUnitDictionary != null && lvBuyCpIndex >= 0 && lvBuyCpIndex <= 2) {
                                toolUnitDictionary.setLevel(lvBuyCpIndex);
                                this.appDelegate.timeSaveDictionary.setPoint(nowPoint - buyCp);
                                this.mainGameViewController.refreshAndSave();
                                if (!this.hidden) {
                                    this.appDelegate.doSoundPoolPlay(7);
                                }
                            }
                        }
                    }
                } else if (_typeID == 2) {
                    ToolUnitDictionary toolUnitDictionary2 = null;
                    short countShort = -1;
                    if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && _typeID < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() && (toolDictionarysArrayList2 = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && _toolID < toolDictionarysArrayList2.size() && (toolUnitDictionary2 = toolDictionarysArrayList2.get(_toolID)) != null && (countShort = toolUnitDictionary2.getCount()) < 0) {
                        countShort = -1;
                    }
                    int buyCp2 = -1;
                    ToolDataDictionary toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary2 != null && countShort >= 0) {
                        buyCp2 = toolDataDictionary2.getBuyCp();
                    }
                    if (buyCp2 > 0) {
                        Log.d("StoreLayout", "doBuy countShort=" + ((int) countShort) + ",buyCp=" + buyCp2);
                        if (this.appDelegate.timeSaveDictionary != null) {
                            float nowPoint2 = this.appDelegate.timeSaveDictionary.getPoint();
                            if (buyCp2 <= nowPoint2 && toolUnitDictionary2 != null) {
                                if (countShort >= 0) {
                                    toolUnitDictionary2.setCount((short) (countShort + 1));
                                }
                                this.appDelegate.timeSaveDictionary.setPoint(nowPoint2 - buyCp2);
                                this.mainGameViewController.refreshAndSave();
                                if (!this.hidden) {
                                    this.appDelegate.doSoundPoolPlay(7);
                                }
                            }
                        }
                    }
                } else if (_typeID == 3) {
                    ToolUnitDictionary toolUnitDictionary3 = null;
                    short countShort2 = -1;
                    if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && _typeID < this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() && (toolDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(_typeID)) != null && _toolID < toolDictionarysArrayList.size() && (toolUnitDictionary3 = toolDictionarysArrayList.get(_toolID)) != null && (countShort2 = toolUnitDictionary3.getCount()) < 0) {
                        countShort2 = -1;
                    }
                    int buyCp3 = -1;
                    ToolDataDictionary toolDataDictionary3 = this.appDelegate.getToolDataDictionaryWithId(_typeID, _toolID);
                    if (toolDataDictionary3 != null && countShort2 >= 0) {
                        buyCp3 = toolDataDictionary3.getBuyCp();
                    }
                    if (buyCp3 > 0) {
                        Log.d("StoreLayout", "doBuy countShort=" + ((int) countShort2) + ",buyCp=" + buyCp3);
                        if (this.appDelegate.timeSaveDictionary != null) {
                            float nowPoint3 = this.appDelegate.timeSaveDictionary.getPoint();
                            if (buyCp3 <= nowPoint3 && toolUnitDictionary3 != null) {
                                if (countShort2 >= 0) {
                                    toolUnitDictionary3.setCount((short) (countShort2 + 1));
                                }
                                this.appDelegate.timeSaveDictionary.setPoint(nowPoint3 - buyCp3);
                                this.mainGameViewController.refreshAndSave();
                                if (!this.hidden) {
                                    this.appDelegate.doSoundPoolPlay(7);
                                }
                            }
                        }
                    }
                }
            }
            reload();
        }
    }

    public void changeListTo(short _listIndex, boolean _animeF) {
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.changGiftButtonStatus((short) -1);
        }
        if (_listIndex == 1) {
            this.storeListSelectUnit.changeListIndex((short) 1);
            if (this.storeListScrollViewUnit01 != null) {
                this.storeListScrollViewUnit01.hidden = false;
                this.storeListScrollViewUnit01.doScrollViewAutoOffset(this.storeListScrollViewUnit01.offsetScrollY);
            }
            if (this.storeListScrollViewUnit02 != null) {
                this.storeListScrollViewUnit02.hidden = true;
            }
            if (this.storeListScrollViewUnit03 != null) {
                this.storeListScrollViewUnit03.hidden = true;
                return;
            }
            return;
        }
        if (_listIndex == 2) {
            this.storeListSelectUnit.changeListIndex((short) 0);
            if (this.storeListScrollViewUnit02 != null) {
                this.storeListScrollViewUnit02.hidden = false;
                this.storeListScrollViewUnit02.doScrollViewAutoOffset(this.storeListScrollViewUnit02.offsetScrollY);
            }
            if (this.storeListScrollViewUnit01 != null) {
                this.storeListScrollViewUnit01.hidden = true;
            }
            if (this.storeListScrollViewUnit03 != null) {
                this.storeListScrollViewUnit03.hidden = true;
                return;
            }
            return;
        }
        if (_listIndex == 3) {
            this.storeListSelectUnit.changeListIndex((short) 2);
            if (this.storeListScrollViewUnit03 != null) {
                this.storeListScrollViewUnit03.hidden = false;
                this.storeListScrollViewUnit03.doScrollViewAutoOffset(this.storeListScrollViewUnit03.offsetScrollY);
            }
            if (this.storeListScrollViewUnit01 != null) {
                this.storeListScrollViewUnit01.hidden = true;
            }
            if (this.storeListScrollViewUnit02 != null) {
                this.storeListScrollViewUnit02.hidden = true;
            }
        }
    }

    public void readtDoCheckAndShowStoreMessagesArray0WithDelay(int _delayTime) {
        if (!this.hidden) {
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreUnit.1
                @Override // java.lang.Runnable
                public void run() throws Resources.NotFoundException, NumberFormatException {
                    StoreUnit.this.checkAndShowStoreMessagesArray0();
                }
            }, _delayTime);
        }
    }

    public void checkAndShowStoreMessagesArray0() throws Resources.NotFoundException, NumberFormatException {
        if (!this.hidden) {
            if (!this.alertUnitType0.hidden) {
                readtDoCheckAndShowStoreMessagesArray0WithDelay(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION);
                return;
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String store_messages_string0 = null;
                String storeMessagesString0 = this.appDelegate.defaultSharedPreferences.getString("store_messages_string0", null);
                if (storeMessagesString0 != null && storeMessagesString0.length() > 0) {
                    String[] message0s = storeMessagesString0.split(",");
                    for (int i = 0; i < message0s.length; i++) {
                        Log.d("sitenopp", i + "=" + message0s[i]);
                        if (i == 0) {
                            int messageInt = Integer.parseInt(message0s[i]);
                            short type_id = (short) (messageInt / 1000);
                            short tool_id = (short) (messageInt % 1000);
                            unLockItem_2_AlertWithTypeId(type_id, tool_id);
                        } else if (i == 1) {
                            store_messages_string0 = message0s[i];
                        } else {
                            store_messages_string0 = String.valueOf(store_messages_string0) + "," + message0s[i];
                        }
                    }
                    if (store_messages_string0 != null && store_messages_string0.length() <= 0) {
                        store_messages_string0 = null;
                    }
                    SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                    editor.putString("store_messages_string0", store_messages_string0);
                    editor.commit();
                    if (store_messages_string0 != null && store_messages_string0.length() > 0) {
                        readtDoCheckAndShowStoreMessagesArray0WithDelay(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION);
                    }
                }
            }
        }
    }

    public void openBonusLayout() {
        if (this.mainGameViewController != null) {
            this.mainGameViewController.doBonusLayoutDisplay();
        }
    }

    public void goToGiftPage(String gift_key, String gift_url) {
        if (this.mainGameViewController != null) {
            this.mainGameViewController.goToGiftPage(gift_key, gift_url);
        }
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws Resources.NotFoundException {
        if (_tag == -100) {
            if (_buttonIndex == 0) {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout2.StoreUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        StoreUnit.this.cancelInitManual();
                    }
                }, 100L);
                return;
            } else {
                if (_buttonIndex == 1) {
                    this.mainGameViewController.doManualLayoutDisplay((short) 2);
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(4);
                        return;
                    }
                    return;
                }
                return;
            }
        }
        if (_tag == -99) {
            if (_buttonIndex != 0) {
            }
            return;
        }
        if (_tag == 1) {
            if (_buttonIndex != 0 && _buttonIndex == 1) {
                doBuyWithId(_tag, _subtag);
                return;
            }
            return;
        }
        if (_tag == 2) {
            if (_buttonIndex != 0 && _buttonIndex == 1) {
                doBuyWithId(_tag, _subtag);
                return;
            }
            return;
        }
        if (_tag == 3) {
            if (_buttonIndex != 0 && _buttonIndex == 1) {
                doBuyWithId(_tag, _subtag);
                return;
            }
            return;
        }
        if (_tag == 99) {
            if (_buttonIndex != 0) {
            }
        } else {
            if (_tag != 100 || _buttonIndex != 0) {
            }
        }
    }

    public void clearBitmap() {
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.clearBitmap();
        }
    }

    public void refreshBitmap() throws IOException {
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.refreshBitmap();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameOnTouch(event);
            return true;
        }
        if (this.storeListUnit != null) {
            if (this.storeListFrontViewUnit != null && (returnF = this.storeListFrontViewUnit.gameOnTouch(event))) {
                return returnF;
            }
            if (this.storeListSelectUnit != null && (returnF = this.storeListSelectUnit.gameOnTouch(event))) {
                return returnF;
            }
            if (this.storeListScrollViewUnit01 != null && !this.storeListScrollViewUnit01.hidden && (returnF = this.storeListScrollViewUnit01.gameOnTouch(event))) {
                return returnF;
            }
            if (this.storeListScrollViewUnit02 != null && !this.storeListScrollViewUnit02.hidden && (returnF = this.storeListScrollViewUnit02.gameOnTouch(event))) {
                return returnF;
            }
            if (this.storeListScrollViewUnit03 != null && !this.storeListScrollViewUnit03.hidden && (returnF = this.storeListScrollViewUnit03.gameOnTouch(event))) {
                return returnF;
            }
        }
        return (this.storeBackViewUnit == null || !(returnF = this.storeBackViewUnit.gameOnTouch(event))) ? returnF : returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.gameDraw(canvas);
        }
        if (this.storeListUnit != null) {
            this.storeListUnit.gameDraw(canvas);
            if (this.storeListSelectUnit != null) {
                this.storeListSelectUnit.gameDraw(canvas);
            }
            if (this.storeListScrollViewUnit01 != null && !this.storeListScrollViewUnit01.hidden) {
                this.storeListScrollViewUnit01.gameDraw(canvas);
            }
            if (this.storeListScrollViewUnit02 != null && !this.storeListScrollViewUnit02.hidden) {
                this.storeListScrollViewUnit02.gameDraw(canvas);
            }
            if (this.storeListScrollViewUnit03 != null && !this.storeListScrollViewUnit03.hidden) {
                this.storeListScrollViewUnit03.gameDraw(canvas);
            }
            if (this.storeListFrontViewUnit != null) {
                this.storeListFrontViewUnit.gameDraw(canvas);
            }
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.gameDraw(canvas);
        }
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.alertUnitType0 != null) {
            this.alertUnitType0.onDestroy();
            this.alertUnitType0 = null;
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.onDestroy();
            this.cpDisplayUnit = null;
        }
        if (this.storeListFrontViewUnit != null) {
            this.storeListFrontViewUnit.onDestroy();
            this.storeListFrontViewUnit = null;
        }
        if (this.storeListScrollViewUnit03 != null) {
            this.storeListScrollViewUnit03.onDestroy();
            this.storeListScrollViewUnit03 = null;
        }
        if (this.storeListScrollViewUnit02 != null) {
            this.storeListScrollViewUnit02.onDestroy();
            this.storeListScrollViewUnit02 = null;
        }
        if (this.storeListScrollViewUnit01 != null) {
            this.storeListScrollViewUnit01.onDestroy();
            this.storeListScrollViewUnit01 = null;
        }
        if (this.storeListSelectUnit != null) {
            this.storeListSelectUnit.onDestroy();
            this.storeListSelectUnit = null;
        }
        if (this.storeListUnit != null) {
            this.storeListUnit.onDestroy();
            this.storeListUnit = null;
        }
        if (this.storeBackViewUnit != null) {
            this.storeBackViewUnit.onDestroy();
            this.storeBackViewUnit = null;
        }
        this.mainGameViewController = null;
        this.appDelegate = null;
    }

    public void changeListViewFastScrollDragViewOffset(float _offsetY, float _contentY, short _index) {
        if (this.storeListFrontViewUnit != null && this.storeListSelectUnit != null && this.storeListSelectUnit.nowListIndex == _index) {
            this.storeListFrontViewUnit.changeListViewFastScrollDragViewOffset(_offsetY, _contentY);
        }
    }

    public void doStoreListScrollViewUnitScroll(float _scrollRate) {
        if (this.storeListSelectUnit != null) {
            float scrollRate = _scrollRate;
            if (scrollRate < BitmapDescriptorFactory.HUE_RED) {
                scrollRate = BitmapDescriptorFactory.HUE_RED;
            } else if (scrollRate > 1.0f) {
                scrollRate = 1.0f;
            }
            if (this.storeListSelectUnit.nowListIndex == 1 && this.storeListScrollViewUnit01 != null) {
                if (!this.storeListScrollViewUnit01.hidden) {
                    float offsetScrollY = scrollRate * this.storeListScrollViewUnit01.offsetScrollYMax;
                    if (offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
                        offsetScrollY = BitmapDescriptorFactory.HUE_RED;
                    } else if (offsetScrollY > this.storeListScrollViewUnit01.offsetScrollYMax) {
                        offsetScrollY = this.storeListScrollViewUnit01.offsetScrollYMax;
                    }
                    this.storeListScrollViewUnit01.doScrollViewAutoOffset(offsetScrollY);
                    return;
                }
                return;
            }
            if (this.storeListSelectUnit.nowListIndex == 0 && this.storeListScrollViewUnit02 != null) {
                if (!this.storeListScrollViewUnit02.hidden) {
                    float offsetScrollY2 = scrollRate * this.storeListScrollViewUnit02.offsetScrollYMax;
                    if (offsetScrollY2 < BitmapDescriptorFactory.HUE_RED) {
                        offsetScrollY2 = BitmapDescriptorFactory.HUE_RED;
                    } else if (offsetScrollY2 > this.storeListScrollViewUnit02.offsetScrollYMax) {
                        offsetScrollY2 = this.storeListScrollViewUnit02.offsetScrollYMax;
                    }
                    this.storeListScrollViewUnit02.doScrollViewAutoOffset(offsetScrollY2);
                    return;
                }
                return;
            }
            if (this.storeListSelectUnit.nowListIndex == 2 && this.storeListScrollViewUnit03 != null && !this.storeListScrollViewUnit03.hidden) {
                float offsetScrollY3 = scrollRate * this.storeListScrollViewUnit03.offsetScrollYMax;
                if (offsetScrollY3 < BitmapDescriptorFactory.HUE_RED) {
                    offsetScrollY3 = BitmapDescriptorFactory.HUE_RED;
                } else if (offsetScrollY3 > this.storeListScrollViewUnit03.offsetScrollYMax) {
                    offsetScrollY3 = this.storeListScrollViewUnit03.offsetScrollYMax;
                }
                this.storeListScrollViewUnit03.doScrollViewAutoOffset(offsetScrollY3);
            }
        }
    }
}
