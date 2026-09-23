package com.idtinc.maingame.sublayout1;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.CharacterDataDictionary;
import com.idtinc.ckunit.FarmUnitDictionary;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import com.idtinc.custom.CPDisplayUnit;
import com.idtinc.custom.ContentPopUnit;
import com.idtinc.custom.ContentPopUnitDelegate;
import com.idtinc.maingame.MainGameViewController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FarmUnit implements AlertUnitType0Delegate, ContentPopUnitDelegate {
    float LISTBACKVIEW_HEIGHT;
    float LISTBACKVIEW_OFFSET_X;
    float LISTBACKVIEW_OFFSET_Y;
    float LISTBACKVIEW_WIDTH;
    float LISTSCROLLVIEW_HEIGHT;
    float LISTSCROLLVIEW_OFFSET_X;
    float LISTSCROLLVIEW_OFFSET_Y;
    float LISTSCROLLVIEW_WIDTH;
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private ContentPopUnit contentPopUnit;
    private CPDisplayUnit cpDisplayUnit;
    public EggListSelectUnit eggListSelectUnit;
    private FarmBackViewUnit farmBackViewUnit;
    public FarmFrontViewUnit farmFrontViewUnit;
    private FarmListFrontViewUnit farmListFrontViewUnit;
    public ArrayList<FarmListScrollViewUnit> farmListScrollViewUnitsArrayList = null;
    private FarmListUnit farmListUnit;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MainGameViewController mainGameViewController;
    public short nowStatus;
    private float zoomRate;

    public FarmUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.LISTBACKVIEW_OFFSET_X = 2.0f;
        this.LISTBACKVIEW_OFFSET_Y = 40.0f;
        this.LISTBACKVIEW_WIDTH = 316.0f;
        this.LISTBACKVIEW_HEIGHT = 304.0f;
        this.LISTSCROLLVIEW_OFFSET_X = 13.0f;
        this.LISTSCROLLVIEW_OFFSET_Y = 52.0f;
        this.LISTSCROLLVIEW_WIDTH = 290.0f;
        this.LISTSCROLLVIEW_HEIGHT = 210.0f;
        this.nowStatus = (short) -1;
        this.appDelegate = _appDelegate;
        this.mainGameViewController = _mainGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.LISTBACKVIEW_OFFSET_X = 2.0f * this.zoomRate;
        this.LISTBACKVIEW_OFFSET_Y = 73.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.LISTBACKVIEW_OFFSET_Y -= 44.0f * this.zoomRate;
        }
        this.LISTBACKVIEW_WIDTH = 316.0f * this.zoomRate;
        this.LISTBACKVIEW_HEIGHT = 364.0f * this.zoomRate;
        this.LISTSCROLLVIEW_OFFSET_X = 13.0f * this.zoomRate;
        this.LISTSCROLLVIEW_OFFSET_Y = 50.0f * this.zoomRate;
        this.LISTSCROLLVIEW_WIDTH = 290.0f * this.zoomRate;
        this.LISTSCROLLVIEW_HEIGHT = 280.0f * this.zoomRate;
        this.nowStatus = (short) -1;
        this.farmBackViewUnit = new FarmBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.farmListUnit = new FarmListUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.LISTBACKVIEW_WIDTH, this.LISTBACKVIEW_HEIGHT, this.zoomRate, this.appDelegate);
        this.farmListUnit.hidden = true;
        this.eggListSelectUnit = new EggListSelectUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.finalWidth, this.finalWidth, this.zoomRate, this, this.appDelegate);
        initFarmListScrollViewUnitsArrayList();
        this.farmFrontViewUnit = new FarmFrontViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.cpDisplayUnit = new CPDisplayUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.cpDisplayUnit.setBackViewParams(210.0f, 398.0f);
        } else {
            this.cpDisplayUnit.setBackViewParams(210.0f, 486.0f);
        }
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        }
        this.alertUnitType0.delegate = this;
        this.contentPopUnit = new ContentPopUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.contentPopUnit.setBackViewParams(5.0f, 40.0f, 310.0f, 294.0f, -200082, 2.0f, -10751, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, 8.0f);
        } else {
            this.contentPopUnit.setBackViewParams(5.0f, 84.0f, 310.0f, 294.0f, -200082, 2.0f, -10751, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, FluctConstants.FRAME_ALPHA_COLOR, 8.0f);
        }
        this.contentPopUnit.delegate = this;
        refreshBitmap();
    }

    public void doPopContentPopUnit(short _bigKind, short _smallKind) {
        FarmUnitDictionary unitDictionary;
        CharacterDataDictionary characterDataDictionary;
        String titleLabelString;
        String titleLabelString2;
        String contentLabelString0;
        String contentLabelString2;
        hiddenContentPopUnit();
        if (this.appDelegate != null && (unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(_bigKind, _smallKind)) != null) {
            int totalCountInt = (int) unitDictionary.getTotalCount();
            short characterUnitDictionarysArrayListCount = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId(_bigKind);
            if (characterUnitDictionarysArrayListCount > 0 && _smallKind >= 0 && _smallKind < characterUnitDictionarysArrayListCount && (characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(_bigKind, _smallKind)) != null) {
                String languageString = this.appDelegate.getLocaleLanguage();
                if (_bigKind == 1) {
                    if (_smallKind < 9) {
                        titleLabelString = "D0" + (_smallKind + 1) + " ";
                    } else {
                        titleLabelString = "D" + (_smallKind + 1) + " ";
                    }
                } else if (_smallKind < 9) {
                    titleLabelString = "C0" + (_smallKind + 1) + " ";
                } else {
                    titleLabelString = "C" + (_smallKind + 1) + " ";
                }
                if (languageString.equals("ja-JP")) {
                    titleLabelString2 = String.valueOf(titleLabelString) + characterDataDictionary.getTitleJa();
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString2 = String.valueOf(titleLabelString) + characterDataDictionary.getTitleZhTW();
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString2 = String.valueOf(titleLabelString) + characterDataDictionary.getTitleZhCN();
                } else {
                    titleLabelString2 = String.valueOf(titleLabelString) + characterDataDictionary.getTitleEn();
                }
                short price = characterDataDictionary.getCp1();
                if (price > 0) {
                    contentLabelString0 = " " + this.appDelegate.getResources().getString(R.string.Price) + ":  " + ((int) price) + " cp";
                } else {
                    contentLabelString0 = " " + this.appDelegate.getResources().getString(R.string.Price) + ":  - cp";
                }
                String contentLabelString22 = " " + this.appDelegate.getResources().getString(R.string.Hatched) + ":  ";
                if (totalCountInt >= 0) {
                    contentLabelString2 = String.valueOf(contentLabelString22) + totalCountInt;
                } else {
                    contentLabelString2 = String.valueOf(contentLabelString22) + "-";
                }
                this.contentPopUnit.setTitleLabelParams(titleLabelString2, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 16.0f, -1, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.contentPopUnit.setContentLabelParams(contentLabelString0, "", contentLabelString2, "", "", this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 57.0f, 18.0f, 16.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.contentPopUnit.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.Back), this.appDelegate.typeface_FONTNAME_00, 22.0f, -1, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, -227838, 2.0f, -436207632, 2.0f, -7576502, 1.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
                if (_bigKind == 0) {
                    if (this.appDelegate.character0Image0ArrayList != null && _smallKind < this.appDelegate.character0Image0ArrayList.size()) {
                        this.contentPopUnit.changeDrawableBitmap0(this.appDelegate.character0Image0ArrayList.get(_smallKind), MotionEventCompat.ACTION_MASK);
                    }
                } else if (_bigKind == 1 && this.appDelegate.character1Image0ArrayList != null && _smallKind < this.appDelegate.character1Image0ArrayList.size()) {
                    this.contentPopUnit.changeDrawableBitmap0(this.appDelegate.character1Image0ArrayList.get(_smallKind), MotionEventCompat.ACTION_MASK);
                }
                this.contentPopUnit.tag = (short) 0;
                this.contentPopUnit.subTag = (short) 0;
                popContentPopUnit();
                this.appDelegate.doSoundPoolPlay(1);
            }
        }
    }

    public void initFarmListScrollViewUnitsArrayList() {
        if (this.farmListScrollViewUnitsArrayList == null) {
            this.farmListScrollViewUnitsArrayList = new ArrayList<>();
            int i = 0;
            while (true) {
                this.appDelegate.getClass();
                if (i < 2) {
                    FarmListScrollViewUnit farmListScrollViewUnit = new FarmListScrollViewUnit(this.LISTBACKVIEW_OFFSET_X + this.LISTSCROLLVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y + this.LISTSCROLLVIEW_OFFSET_Y, (int) this.LISTSCROLLVIEW_WIDTH, ((int) this.LISTSCROLLVIEW_HEIGHT) + 1, this.zoomRate, this, this.appDelegate);
                    farmListScrollViewUnit.tag = (short) i;
                    this.farmListScrollViewUnitsArrayList.add(farmListScrollViewUnit);
                    i++;
                } else {
                    this.farmListFrontViewUnit = new FarmListFrontViewUnit(this.LISTBACKVIEW_OFFSET_X, this.LISTBACKVIEW_OFFSET_Y, this.LISTBACKVIEW_WIDTH, this.LISTBACKVIEW_WIDTH, this.zoomRate, this, this.appDelegate);
                    refreshListUnit();
                    changeListTo((short) 0, false);
                    return;
                }
            }
        }
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void doInit() {
        start();
    }

    public void start() throws IOException {
        changeNowStatus(0);
    }

    public void stop() throws IOException {
        changeNowStatus(1);
    }

    public void changeNowStatus(int _newStatus) throws IOException {
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

    public void reload() throws IOException {
        refreshListUnit();
        this.farmBackViewUnit.refreshBackgroundBitmap();
        this.farmBackViewUnit.refreshCharacterDisplayViewsArray();
        refreshPoint();
    }

    public void doDisplay() {
        hiddenAlert();
        readyCheckTimeDoorOpenFromCK();
        this.farmFrontViewUnit.doRefreshLoop();
        this.farmFrontViewUnit.checkLoseCharacters();
        refreshListUnit();
        this.farmBackViewUnit.refreshCharacterDisplayViewsArray();
        refreshPoint();
        displayInitManual();
    }

    public void doHidden() {
        closeListLayout();
        hiddenAlert();
        hiddenContentPopUnit();
        stopAllScrollWithHidden(true);
    }

    public void stopAllScrollWithHidden(boolean _hiddenF) {
        doFarmListScrollViewUnitScroll(BitmapDescriptorFactory.HUE_RED);
    }

    public void displayInitManual() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("init_manual_farm", false)) {
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
            editor.putBoolean("init_manual_farm", false);
            editor.commit();
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『ファーム』の操作解説を見ますか？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『農場』的教學說明嗎？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『农场』的教学说明吗？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "Do you want to read ";
                contentLabelString2 = "the \"Farm\" manual?";
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

    public void popContentPopUnit() {
        this.contentPopUnit.pop();
    }

    public void hiddenContentPopUnit() {
        this.contentPopUnit.reset();
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    public void refreshPoint() {
        this.cpDisplayUnit.refresh();
    }

    public void doWillTerminate() {
        Log.d("FarmLayout", "doWillTerminate");
        hiddenAlert();
    }

    public void doWillEnterForeground() {
        Log.d("Farmayout", "doWillEnterForeground");
        hiddenAlert();
        this.farmFrontViewUnit.doRefreshLoop();
        reload();
    }

    public void doListViewSelectWithType(short _selectType) {
        if (this.appDelegate.timeSaveDictionary != null && this.farmListScrollViewUnitsArrayList != null) {
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollViewUnit != null) {
                    farmListScrollViewUnit.selectWithType((short) 0);
                    if (_selectType != 0) {
                        if (_selectType == 1) {
                            if (!farmListScrollViewUnit.hidden) {
                                farmListScrollViewUnit.selectWithType(_selectType);
                            }
                        } else if (_selectType == 2 && !farmListScrollViewUnit.hidden) {
                            farmListScrollViewUnit.selectWithType(_selectType);
                        }
                    }
                }
            }
            refreshTotal();
        }
    }

    public void refreshListUnit() {
        if (this.appDelegate.timeSaveDictionary != null && this.farmListScrollViewUnitsArrayList != null) {
            if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList != null) {
                for (int i = 0; i < this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size(); i++) {
                    FarmListScrollViewUnit farmListScrollViewUnit = null;
                    if (this.farmListScrollViewUnitsArrayList != null && i < this.farmListScrollViewUnitsArrayList.size()) {
                        farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                    }
                    if (farmListScrollViewUnit != null) {
                        farmListScrollViewUnit.reload();
                    }
                }
            } else {
                Log.d("FarmLayout", "pppppppppppppppppp farm View farmUnitDictionarysArray==nil");
            }
            this.farmListFrontViewUnit.reset();
        }
    }

    public int refreshTotal() {
        int totalCP = 0;
        if (this.farmListScrollViewUnitsArrayList != null) {
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollViewUnit != null) {
                    int farmListScrollLayoutTotalPrice = farmListScrollViewUnit.getTotal();
                    totalCP += farmListScrollLayoutTotalPrice;
                }
            }
        }
        this.farmListFrontViewUnit.changeTotal(totalCP);
        return totalCP;
    }

    public void readySell() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        hiddenAlert();
        int totalCP = refreshTotal();
        if (totalCP > 0) {
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "売出";
                contentLabelString0 = "";
                contentLabelString1 = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ": " + totalCP + "cp";
                contentLabelString2 = "";
                contentLabelString3 = "売り出します。よろしいですか？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "賣出";
                contentLabelString0 = "";
                contentLabelString1 = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ": " + totalCP + "cp";
                contentLabelString2 = "";
                contentLabelString3 = "你確定要賣出嗎？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "賣出";
                contentLabelString0 = "";
                contentLabelString1 = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ": " + totalCP + "cp";
                contentLabelString2 = "";
                contentLabelString3 = "你确定要卖出吗？";
                contentLabelString4 = "";
            } else {
                titleLabelString = "Sell";
                contentLabelString0 = "";
                contentLabelString1 = String.valueOf(this.appDelegate.getResources().getString(R.string.Total)) + ": " + totalCP + "cp";
                contentLabelString2 = "";
                contentLabelString3 = "Are you sure you want to sell?";
                contentLabelString4 = "";
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
            this.alertUnitType0.tag = (short) 0;
            popAlert();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(4);
            }
        }
    }

    public void doSell() throws IOException {
        short price;
        float totalCP = BitmapDescriptorFactory.HUE_RED;
        if (this.farmListScrollViewUnitsArrayList != null) {
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollLayout = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollLayout != null) {
                    short characterUnitDictionarysArrayListCount = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId(farmListScrollLayout.tag);
                    if (farmListScrollLayout.countsArray != null && characterUnitDictionarysArrayListCount > 0) {
                        for (int j = 0; j < farmListScrollLayout.countsArray.length; j++) {
                            FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId(farmListScrollLayout.tag, (short) j);
                            if (j < farmListScrollLayout.countsArray.length && unitDictionary != null) {
                                int countInt = (int) unitDictionary.getCount();
                                if (farmListScrollLayout.countsArray[j][0] > 0 && farmListScrollLayout.countsArray[j][0] <= countInt) {
                                    int newCountInt = countInt - farmListScrollLayout.countsArray[j][0];
                                    if (newCountInt <= 0) {
                                        newCountInt = 0;
                                    }
                                    CharacterDataDictionary characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(farmListScrollLayout.tag, (short) j);
                                    if (characterDataDictionary != null && (price = characterDataDictionary.getCp1()) > 0) {
                                        totalCP += farmListScrollLayout.countsArray[j][0] * price;
                                        unitDictionary.setCount(newCountInt);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (totalCP >= 1.0f && this.appDelegate.timeSaveDictionary != null) {
            float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
            this.appDelegate.timeSaveDictionary.setPoint(nowPoint + totalCP);
            this.mainGameViewController.refreshAndSave();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(8);
            }
        }
        reload();
        Log.d("FarmLayout", "totalCP:" + totalCP);
    }

    public void readyCheckTimeDoorOpenFromCK() {
        if (!this.hidden && this.farmBackViewUnit != null) {
            this.farmBackViewUnit.timeDoorButtonStatus = (short) -1;
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmUnit.1
                @Override // java.lang.Runnable
                public void run() {
                    FarmUnit.this.checkTimeDoorOpenFromCK();
                }
            }, 2500L);
        }
    }

    public void checkTimeDoorOpenFromCK() {
        if (!this.hidden) {
            if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
                readyCheckTimeDoorOpenFromCK();
                return;
            }
            if (this.appDelegate != null) {
                if (this.appDelegate.defaultSharedPreferences == null) {
                    this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.farmBackViewUnit != null && this.farmBackViewUnit.timeDoorButtonStatus != 0) {
                    this.farmBackViewUnit.timeDoorButtonStatus = (short) -1;
                    String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                    if (ck_send_chick_string.length() > 0) {
                        this.farmBackViewUnit.timeDoorButtonStatus = (short) 0;
                        displayTimeDoorOpenAlert();
                    }
                }
            }
        }
    }

    public void displayTimeDoorOpenAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (!this.hidden) {
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "タイムドアが開き";
                contentLabelString0 = "";
                contentLabelString1 = "タイムドアが開いてます！タイムドアを";
                contentLabelString2 = "クリックして、トリたちを受け取って";
                contentLabelString3 = "ください  ~♪";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "時空門開啓";
                contentLabelString0 = "";
                contentLabelString1 = "時空門開啓了！按一下時空門";
                contentLabelString2 = "準備接收小雞吧 ~♪";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "時空門開啓";
                contentLabelString0 = "";
                contentLabelString1 = "时空门开啓了！按一下时空门";
                contentLabelString2 = "准备接收小鸡吧 ~♪";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            } else {
                titleLabelString = "Time Door is Open";
                contentLabelString0 = "";
                contentLabelString1 = "Time Door is open！Click \"Time Door\"";
                contentLabelString2 = "to receive chicks.";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 1, "", "OK", this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
            this.alertUnitType0.tag = (short) -2;
            this.alertUnitType0.subTag = (short) -1;
            popAlert();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(15);
            }
        }
    }

    public void displayCheckReceiveChicksFromCKAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (!this.hidden && this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                if (ck_send_chick_string.length() > 0) {
                    short characterKindCnt = 0;
                    String infosString = "";
                    String[] ck_send_chick_string_Items0 = ck_send_chick_string.split("=");
                    if (ck_send_chick_string_Items0 != null && ck_send_chick_string_Items0.length == 2 && ck_send_chick_string_Items0[1] != null && ck_send_chick_string_Items0[1].length() > 0) {
                        characterKindCnt = this.alertUnitType0.set_Character_Images_View_Infos(ck_send_chick_string_Items0[1]);
                        infosString = ck_send_chick_string_Items0[1];
                    }
                    hiddenAlert();
                    String languageString = this.appDelegate.getLocaleLanguage();
                    if (languageString.equals("ja-JP")) {
                        titleLabelString = "受け取り";
                        contentLabelString0 = "";
                        contentLabelString1 = "";
                        contentLabelString2 = "";
                        contentLabelString3 = "トリを受け取りますか？";
                        contentLabelString4 = "";
                        if (characterKindCnt <= 5) {
                            contentLabelLanguageOffsetY = 8.0f;
                        } else {
                            contentLabelLanguageOffsetY = 16.0f;
                        }
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        titleLabelString = "接收";
                        contentLabelString0 = "";
                        contentLabelString1 = "";
                        contentLabelString2 = "";
                        contentLabelString3 = "確定要接收從時空門傳送來的小雞？";
                        contentLabelString4 = "";
                        if (characterKindCnt <= 5) {
                            contentLabelLanguageOffsetY = 8.0f;
                        } else {
                            contentLabelLanguageOffsetY = 16.0f;
                        }
                    } else if (languageString.equals("zh-CN")) {
                        titleLabelString = "接收";
                        contentLabelString0 = "";
                        contentLabelString1 = "";
                        contentLabelString2 = "";
                        contentLabelString3 = "确定要接收从时空门传送来的小鸡？";
                        contentLabelString4 = "";
                        if (characterKindCnt <= 5) {
                            contentLabelLanguageOffsetY = 8.0f;
                        } else {
                            contentLabelLanguageOffsetY = 16.0f;
                        }
                    } else {
                        titleLabelString = "Receive";
                        contentLabelString0 = "";
                        contentLabelString1 = "";
                        contentLabelString2 = "";
                        contentLabelString3 = "Are you sure you want to receive?";
                        contentLabelString4 = "";
                        if (characterKindCnt <= 5) {
                            contentLabelLanguageOffsetY = 8.0f;
                        } else {
                            contentLabelLanguageOffsetY = 16.0f;
                        }
                    }
                    this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                    this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
                    this.alertUnitType0.tag = (short) 10;
                    this.alertUnitType0.subTag = (short) -1;
                    this.alertUnitType0.set_Character_Images_View_Infos(infosString);
                    popAlert();
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(16);
                        return;
                    }
                    return;
                }
                if (this.farmBackViewUnit != null) {
                    this.farmBackViewUnit.timeDoorButtonStatus = (short) -1;
                }
            }
        }
    }

    public void doReceiveChicksFromCK() throws IOException {
        String[] sendStringItems1;
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                String ck_send_chick_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_chick_string", "");
                if (ck_send_chick_string.length() > 0) {
                    boolean saveF = false;
                    short count = 0;
                    short type = 0;
                    String[] sendStringItems0 = ck_send_chick_string.split("=");
                    if (sendStringItems0 != null && sendStringItems0.length == 2 && sendStringItems0[1] != null && (sendStringItems1 = sendStringItems0[1].split(",")) != null && sendStringItems1.length > 0) {
                        for (String str : sendStringItems1) {
                            String[] sendStringItems2 = str.split("_");
                            if (sendStringItems2 != null && sendStringItems2.length == 2) {
                                int characterID = -1;
                                if (sendStringItems2[0] != null) {
                                    characterID = Integer.valueOf(sendStringItems2[0]).intValue();
                                }
                                int getCount = -1;
                                if (sendStringItems2[1] != null) {
                                    getCount = Integer.valueOf(sendStringItems2[1]).intValue();
                                }
                                if (characterID == 0 && getCount >= 1) {
                                    int randInt = (int) (Math.random() * 10.0d);
                                    if (randInt < 1) {
                                        type = 1;
                                        getCount--;
                                        FarmUnitDictionary unit104Dictionary = this.appDelegate.getCharacterUnitDictionaryWithId((short) 0, (short) 104);
                                        if (unit104Dictionary != null) {
                                            float countFloat = unit104Dictionary.getCount();
                                            count = (short) (count + 1);
                                            if (countFloat < BitmapDescriptorFactory.HUE_RED) {
                                                countFloat = BitmapDescriptorFactory.HUE_RED;
                                            }
                                            float newCountFloat = countFloat + 1.0f;
                                            if (newCountFloat <= 0.0d) {
                                                newCountFloat = BitmapDescriptorFactory.HUE_RED;
                                            }
                                            unit104Dictionary.setCount(newCountFloat);
                                            saveF = true;
                                        }
                                    }
                                }
                                FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId((short) 0, (short) characterID);
                                if (unitDictionary != null) {
                                    float countFloat2 = unitDictionary.getCount();
                                    if (getCount >= 1.0d) {
                                        this.appDelegate.getClass();
                                        if (getCount <= 10) {
                                            count = (short) (((short) getCount) + count);
                                            if (countFloat2 < BitmapDescriptorFactory.HUE_RED) {
                                                countFloat2 = BitmapDescriptorFactory.HUE_RED;
                                            }
                                            float newCountFloat2 = countFloat2 + getCount;
                                            if (newCountFloat2 <= BitmapDescriptorFactory.HUE_RED) {
                                                newCountFloat2 = BitmapDescriptorFactory.HUE_RED;
                                            }
                                            unitDictionary.setCount(newCountFloat2);
                                            saveF = true;
                                        }
                                    }
                                }
                                this.appDelegate.displayFullAdView();
                            }
                        }
                    }
                    SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                    editor.putString("ck_send_chick_string", "");
                    editor.commit();
                    displayReceiveChicksFromCKAlertWithCount(count, type);
                    if (saveF) {
                        if (this.appDelegate.timeSaveDictionary != null) {
                            this.mainGameViewController.refreshAndSave();
                        }
                        reload();
                    }
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(17);
                    }
                }
                if (this.farmBackViewUnit != null) {
                    this.farmBackViewUnit.timeDoorButtonStatus = (short) -1;
                }
            }
        }
    }

    public void displayReceiveChicksFromCKAlertWithCount(short _count, short _type) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        String titleLabelString2;
        String contentLabelString02;
        String contentLabelString12;
        String contentLabelString22;
        String contentLabelString32;
        String contentLabelString42;
        float contentLabelLanguageOffsetY;
        if (!this.hidden && _count > 0) {
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (_type == 1) {
                if (languageString.equals("ja-JP")) {
                    titleLabelString2 = "受け取り成功";
                    contentLabelString02 = "";
                    contentLabelString12 = "";
                    contentLabelString22 = "トリを" + ((int) _count) + "羽受け取りました。";
                    contentLabelString32 = "1羽のチっちゃんが転じて";
                    contentLabelString42 = "タイムチキとなる。";
                    contentLabelLanguageOffsetY = 2.0f;
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString2 = "接收成功";
                    contentLabelString02 = "";
                    contentLabelString12 = "";
                    contentLabelString22 = "接收了" + ((int) _count) + "隻小雞。其中1隻雞寶";
                    contentLabelString32 = "穿越時空門後,變成了時空雞。";
                    contentLabelString42 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString2 = "接收成功";
                    contentLabelString02 = "";
                    contentLabelString12 = "";
                    contentLabelString22 = "接收了" + ((int) _count) + "只小鸡。其中1只鸡宝";
                    contentLabelString32 = "穿越时空门後,变成了时空鸡。";
                    contentLabelString42 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                } else {
                    titleLabelString2 = "Receive Success";
                    contentLabelString02 = "";
                    contentLabelString12 = "";
                    if (_count <= 1) {
                        contentLabelString22 = "You received " + ((int) _count) + " chick. A \"Chick\"";
                    } else {
                        contentLabelString22 = "You received " + ((int) _count) + " chicks. A \"Chick\"";
                    }
                    contentLabelString32 = "transformed into a \"Time Chick\"";
                    contentLabelString42 = "because through the Time Door.";
                    contentLabelLanguageOffsetY = 2.0f;
                }
                this.alertUnitType0.setTitleLabelParams(titleLabelString2, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setContentLabelParams(contentLabelString02, contentLabelString12, contentLabelString22, contentLabelString32, contentLabelString42, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setType((short) 1, "", "OK", this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
                this.alertUnitType0.smallImage0OffsetX = this.alertUnitType0.backViewOffsetX + (130.0f * this.zoomRate);
                this.alertUnitType0.smallImage0OffsetY = this.alertUnitType0.backViewOffsetY + (28.0f * this.zoomRate);
                if (this.appDelegate.character0Image0ArrayList != null && this.appDelegate.character0Image0ArrayList.size() > 0) {
                    this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.character0Image0ArrayList.get(0), MotionEventCompat.ACTION_MASK);
                }
                this.alertUnitType0.tag = (short) -104;
                this.alertUnitType0.subTag = (short) -1;
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmUnit.this.doChick0toChick104Anime();
                    }
                }, 1000L);
            } else {
                if (languageString.equals("ja-JP")) {
                    titleLabelString = "受け取り成功";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "トリを" + ((int) _count) + "羽受け取りました。";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString = "接收成功";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "接收了" + ((int) _count) + "隻小雞。";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString = "接收成功";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "接收了" + ((int) _count) + "只小鸡。";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                } else {
                    titleLabelString = "Receive Success";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    if (_count <= 1) {
                        contentLabelString2 = "You received " + ((int) _count) + " chick.";
                    } else {
                        contentLabelString2 = "You received " + ((int) _count) + " chicks.";
                    }
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                }
                this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setType((short) 1, "", "OK", this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
                this.alertUnitType0.tag = (short) -10;
                this.alertUnitType0.subTag = (short) -1;
            }
            popAlert();
        }
    }

    public void doChick0toChick104Anime() {
        if (!this.hidden && this.alertUnitType0 != null && !this.alertUnitType0.hidden && this.alertUnitType0.tag == -104) {
            this.alertUnitType0.smallImage0OffsetX = this.alertUnitType0.backViewOffsetX + (127.0f * this.zoomRate);
            this.alertUnitType0.smallImage0OffsetY = this.alertUnitType0.backViewOffsetY + (this.zoomRate * 28.0f);
            if (this.appDelegate.character0Image0ArrayList != null && 104 < this.appDelegate.character0Image0ArrayList.size()) {
                this.alertUnitType0.changeDrawableBitmap0(this.appDelegate.character0Image0ArrayList.get(LocationRequest.PRIORITY_LOW_POWER), MotionEventCompat.ACTION_MASK);
            }
            this.alertUnitType0.smallImage1FadeOutAlpha = MotionEventCompat.ACTION_MASK;
            this.alertUnitType0.smallImage1OffsetX = this.alertUnitType0.backViewOffsetX + (130.0f * this.zoomRate);
            this.alertUnitType0.smallImage1OffsetY = this.alertUnitType0.backViewOffsetY + (this.zoomRate * 28.0f);
            if (this.appDelegate.character0Image0ArrayList != null && this.appDelegate.character0Image0ArrayList.size() > 0) {
                this.alertUnitType0.changeDrawableBitmap1(this.appDelegate.character0Image0ArrayList.get(0), MotionEventCompat.ACTION_MASK);
            }
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(16);
            }
        }
    }

    public void noCPAlertWithNeedCP(int _needcp) {
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
            contentLabelString1 = "ファームの修繕は" + _needcp + "cpがかかります。";
            contentLabelString2 = "";
            contentLabelString3 = "cpが足りないようです。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "整修農場需要花費" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像沒有足夠的cp。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "整修农场需要花费" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像没有足够的cp。";
            contentLabelString4 = "";
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "You need " + _needcp + "cp to repair the farm.";
            contentLabelString2 = "";
            contentLabelString3 = "You don't have enough cp.";
            contentLabelString4 = "";
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = (short) 99;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void fixFarmWithCP(float _fixcp) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        hiddenAlert();
        if (this.appDelegate.timeSaveDictionary != null) {
            float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
            if (nowPoint < BitmapDescriptorFactory.HUE_RED) {
                nowPoint = BitmapDescriptorFactory.HUE_RED;
                this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
                this.mainGameViewController.refreshAndSave();
            }
            if (nowPoint < _fixcp) {
                noCPAlertWithNeedCP((int) _fixcp);
                return;
            }
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "ファーム修繕";
                contentLabelString0 = "";
                contentLabelString1 = "ファームの修繕は" + ((int) _fixcp) + "cpがかかります。";
                contentLabelString2 = "";
                contentLabelString3 = "よろしいですか？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "整修農場";
                contentLabelString0 = "";
                contentLabelString1 = "整修農場需要花費" + ((int) _fixcp) + "cp。";
                contentLabelString2 = "";
                contentLabelString3 = "你確定要整修嗎？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "整修農場";
                contentLabelString0 = "";
                contentLabelString1 = "整修农场需要花费" + ((int) _fixcp) + "cp。";
                contentLabelString2 = "";
                contentLabelString3 = "你确定要整修吗？";
                contentLabelString4 = "";
            } else {
                titleLabelString = "Farm Repair";
                contentLabelString0 = "";
                contentLabelString1 = "You need " + ((int) _fixcp) + "cp to repair the farm.";
                contentLabelString2 = "";
                contentLabelString3 = "Are you sure you want to repair？";
                contentLabelString4 = "";
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
            this.alertUnitType0.tag = (short) 1;
            this.alertUnitType0.subTag = (short) _fixcp;
            popAlert();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(4);
            }
        }
    }

    public int doLoseCharactersWithRate(int _lossRate) throws IOException {
        short unitDictionarysArrayCount;
        int loseCnt = 0;
        int lossRate = _lossRate;
        if (lossRate <= 0) {
            return 0;
        }
        if (lossRate > 100) {
            lossRate = 100;
        }
        if (this.farmListScrollViewUnitsArrayList != null) {
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollLayout = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollLayout != null && (unitDictionarysArrayCount = this.appDelegate.getCharacterUnitDictionarysArrayCountWithEggId((short) i)) > 0) {
                    for (int j = 0; j < unitDictionarysArrayCount; j++) {
                        FarmUnitDictionary unitDictionary = this.appDelegate.getCharacterUnitDictionaryWithId((short) i, (short) j);
                        if (unitDictionary != null) {
                            float countFloat = unitDictionary.getCount();
                            if (countFloat >= 1.0f) {
                                int originTotalCountInt = (int) countFloat;
                                int totalCountInt = originTotalCountInt;
                                int nokoriCountInt = 0;
                                if (totalCountInt >= 10) {
                                    nokoriCountInt = ((100 - lossRate) * totalCountInt) / 100;
                                } else {
                                    while (totalCountInt > 0) {
                                        int randInt = (int) (Math.random() * 100.0d);
                                        Log.d("FarmLayout", "randInt:" + randInt);
                                        if (randInt < 100 - lossRate) {
                                            nokoriCountInt++;
                                        }
                                        totalCountInt--;
                                    }
                                }
                                if (nokoriCountInt < 0) {
                                    nokoriCountInt = 0;
                                }
                                if (nokoriCountInt > originTotalCountInt) {
                                    nokoriCountInt = (int) countFloat;
                                }
                                Log.d("FarmLayout", "nokoriCountInt:" + nokoriCountInt);
                                loseCnt += originTotalCountInt - nokoriCountInt;
                                unitDictionary.setCount(nokoriCountInt);
                            }
                        }
                    }
                }
            }
        }
        if (loseCnt > 0) {
            this.mainGameViewController.refreshAndSave();
            reload();
        }
        return loseCnt;
    }

    public void loseCharactersWithRate(int _lossRate) throws IOException {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        hiddenAlert();
        int loseCnt = doLoseCharactersWithRate(_lossRate);
        Log.i("FarmLayout", "loseRate:" + _lossRate);
        Log.i("FarmLayout", "loseCnt:" + loseCnt);
        if (loseCnt > 0) {
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "脱走事件";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = String.valueOf(loseCnt) + "羽が脱走しました!";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "脱逃事件";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "逃走了" + loseCnt + "隻雞!";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "脱逃事件";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "逃走了" + loseCnt + "只鸡!";
                contentLabelString3 = "";
                contentLabelString4 = "";
            } else {
                titleLabelString = "Escape Incident";
                contentLabelString0 = "";
                contentLabelString1 = "";
                if (loseCnt <= 1) {
                    contentLabelString2 = "You lose " + loseCnt + " chicken!";
                } else {
                    contentLabelString2 = "You lose " + loseCnt + " chickens!";
                }
                contentLabelString3 = "";
                contentLabelString4 = "";
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 1, "", "OK", this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
            this.alertUnitType0.tag = (short) -1;
            popAlert();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(6);
            }
        }
    }

    public void doLoop() {
        if (this.farmListScrollViewUnitsArrayList != null) {
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollViewUnit != null) {
                    farmListScrollViewUnit.doLoop();
                }
            }
        }
        if (this.farmBackViewUnit != null) {
            this.farmBackViewUnit.doLoop();
        }
        if (this.farmFrontViewUnit != null) {
            this.farmFrontViewUnit.doLoop();
        }
    }

    public void openListLayout() {
        doListLayoutDisplay();
    }

    public void goToCKKB() {
        if (this.appDelegate != null) {
            this.appDelegate.goToCKKB();
        }
    }

    public void goToCKMV() {
        if (this.appDelegate != null) {
            this.appDelegate.goToCKMV();
        }
    }

    public void doListLayoutDisplay() {
        this.farmListUnit.hidden = false;
    }

    public void closeListLayout() {
        this.farmListUnit.hidden = true;
    }

    public void shareToFacebook() {
        if (this.appDelegate != null) {
            if (this.appDelegate.checkInterNet()) {
                this.appDelegate.readyDoShareImage((short) 1);
            } else {
                this.appDelegate.showNoInternetAlertDialog();
            }
        }
    }

    public void shareToLine() {
        if (getScreenShot()) {
            sendPhotoLine();
        }
    }

    public void sendPhotoLine() {
        if (this.appDelegate != null) {
            String filePath = "/data/data/" + this.appDelegate.getPackageName() + "/files";
            File file = new File(String.valueOf(filePath) + File.separator + "character_pic.png");
            this.appDelegate.shareToLineWithImage(Uri.fromFile(file));
        }
    }

    public void shareToMail() throws Resources.NotFoundException {
        if (getScreenShot()) {
            sendPhotoMail();
        }
    }

    public void sendPhotoMail() throws Resources.NotFoundException {
        String intentTitle = this.appDelegate.getResources().getString(R.string.TellFriendsCK2);
        String subject = this.appDelegate.getResources().getString(R.string.ChickKitchen2);
        String text = String.valueOf(this.appDelegate.getResources().getString(R.string.ChickKitchen2)) + " (Free App)\n\niOS: http://www.idtfun.com/apps/chickkitchen_cd_ap0.html\n\nAndroid: http://www.idtfun.com/apps/chickkitchen_cd_gp0.html\n";
        String filePath = "/data/data/" + this.appDelegate.getPackageName() + "/files";
        File file = new File(String.valueOf(filePath) + File.separator + "character_pic.png");
        this.appDelegate.shareMailWithUriImage(intentTitle, subject, text, Uri.fromFile(file));
    }

    public boolean getScreenShot() {
        Bitmap shareBitmap;
        if (!this.appDelegate.isRetina4) {
            shareBitmap = this.appDelegate.takeScreenShot(this.zoomRate * 10.0f, 55.0f * this.zoomRate, this.zoomRate * 300.0f, this.zoomRate * 235.0f);
        } else {
            shareBitmap = this.appDelegate.takeScreenShot(this.zoomRate * 10.0f, 99.0f * this.zoomRate, this.zoomRate * 300.0f, this.zoomRate * 235.0f);
        }
        if (shareBitmap == null) {
            return false;
        }
        String filePath = "/data/data/" + this.appDelegate.getPackageName() + "/files";
        boolean successF = this.appDelegate.savePic(shareBitmap, filePath, "character_pic.png");
        return successF;
    }

    @Override // com.idtinc.custom.ContentPopUnitDelegate
    public void contentPopUnitButtonClick(short _buttonIndex) throws Resources.NotFoundException {
        if (this.appDelegate != null) {
            if (_buttonIndex == 10) {
                shareToFacebook();
            } else if (_buttonIndex == 11) {
                shareToLine();
            } else if (_buttonIndex == 12) {
                shareToMail();
            }
        }
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws IOException {
        if (_tag == -100) {
            if (_buttonIndex == 0) {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FarmUnit.this.cancelInitManual();
                    }
                }, 100L);
                return;
            } else {
                if (_buttonIndex == 1) {
                    this.mainGameViewController.doManualLayoutDisplay((short) 1);
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
        if (_tag != -1) {
            if (_tag == 0) {
                if (_buttonIndex != 0 && _buttonIndex == 1) {
                    doSell();
                    return;
                }
                return;
            }
            if (_tag == 1) {
                if (_buttonIndex != 0 && _buttonIndex == 1 && this.farmFrontViewUnit.fixFarmWithCP(_subtag)) {
                    if (!this.hidden) {
                        this.appDelegate.doSoundPoolPlay(10);
                    }
                    this.mainGameViewController.refreshAndSave();
                    refreshPoint();
                    return;
                }
                return;
            }
            if (_tag == 10) {
                if (_buttonIndex != 0 && _buttonIndex == 1) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout1.FarmUnit.4
                        @Override // java.lang.Runnable
                        public void run() throws IOException {
                            FarmUnit.this.doReceiveChicksFromCK();
                        }
                    }, 5L);
                    return;
                }
                return;
            }
            if (_tag != 99 || _buttonIndex == 0) {
            }
        }
    }

    public void clearBitmap() {
        if (this.farmFrontViewUnit != null) {
            this.farmFrontViewUnit.clearBitmap();
        }
    }

    public void refreshBitmap() {
        if (this.farmFrontViewUnit != null) {
            this.farmFrontViewUnit.refreshBitmap();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameOnTouch(event);
            return true;
        }
        if (this.contentPopUnit != null && !this.contentPopUnit.hidden) {
            this.contentPopUnit.gameOnTouch(event);
            return true;
        }
        if (this.farmFrontViewUnit != null && (returnF = this.farmFrontViewUnit.gameOnTouch(event))) {
            return returnF;
        }
        if (this.farmListUnit == null || this.farmListUnit.hidden) {
            return (this.farmBackViewUnit == null || !(returnF = this.farmBackViewUnit.gameOnTouch(event))) ? returnF : returnF;
        }
        if (this.farmListFrontViewUnit != null && (returnF = this.farmListFrontViewUnit.gameOnTouch(event))) {
            return returnF;
        }
        if (this.eggListSelectUnit != null && (returnF = this.eggListSelectUnit.gameOnTouch(event))) {
            return returnF;
        }
        if (this.farmListScrollViewUnitsArrayList == null) {
            return true;
        }
        for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
            FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
            if (farmListScrollViewUnit != null && !farmListScrollViewUnit.hidden) {
                farmListScrollViewUnit.gameOnTouch(event);
                if (returnF) {
                    return returnF;
                }
            }
        }
        return true;
    }

    public void gameDraw(Canvas canvas) {
        if (this.farmBackViewUnit != null) {
            this.farmBackViewUnit.gameDraw(canvas);
        }
        if (this.farmListUnit != null && !this.farmListUnit.hidden) {
            this.farmListUnit.gameDraw(canvas);
            if (this.eggListSelectUnit != null) {
                this.eggListSelectUnit.gameDraw(canvas);
            }
            if (this.farmListScrollViewUnitsArrayList != null) {
                for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                    FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                    if (farmListScrollViewUnit != null && !farmListScrollViewUnit.hidden) {
                        farmListScrollViewUnit.gameDraw(canvas);
                    }
                }
            }
            if (this.farmListFrontViewUnit != null) {
                this.farmListFrontViewUnit.gameDraw(canvas);
            }
        }
        if (this.farmFrontViewUnit != null) {
            this.farmFrontViewUnit.gameDraw(canvas);
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.gameDraw(canvas);
        }
        if (this.contentPopUnit != null && !this.contentPopUnit.hidden) {
            this.contentPopUnit.gameDraw(canvas);
        }
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.farmListFrontViewUnit != null) {
            this.farmListFrontViewUnit.onDestroy();
            this.farmListFrontViewUnit = null;
        }
        if (this.farmListScrollViewUnitsArrayList != null) {
            while (this.farmListScrollViewUnitsArrayList.size() > 0) {
                FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(0);
                if (farmListScrollViewUnit != null) {
                    farmListScrollViewUnit.onDestroy();
                    this.farmListScrollViewUnitsArrayList.remove(0);
                }
            }
        }
        if (this.alertUnitType0 != null) {
            this.alertUnitType0.onDestroy();
            this.alertUnitType0 = null;
        }
        if (this.contentPopUnit != null) {
            this.contentPopUnit.onDestroy();
            this.contentPopUnit = null;
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.onDestroy();
            this.cpDisplayUnit = null;
        }
        if (this.farmFrontViewUnit != null) {
            this.farmFrontViewUnit.onDestroy();
            this.farmFrontViewUnit = null;
        }
        if (this.eggListSelectUnit != null) {
            this.eggListSelectUnit.onDestroy();
            this.eggListSelectUnit = null;
        }
        if (this.farmListUnit != null) {
            this.farmListUnit.onDestroy();
            this.farmListUnit = null;
        }
        if (this.farmBackViewUnit != null) {
            this.farmBackViewUnit.onDestroy();
            this.farmBackViewUnit = null;
        }
        this.mainGameViewController = null;
        this.appDelegate = null;
    }

    public void changeListTo(short _listIndex, boolean _animeF) {
        if (this.farmListScrollViewUnitsArrayList != null && this.farmListScrollViewUnitsArrayList.size() >= 2) {
            if (_listIndex == 0) {
                this.eggListSelectUnit.changeListIndex((short) 0);
                FarmListScrollViewUnit farmListScrollViewUnit0 = this.farmListScrollViewUnitsArrayList.get(0);
                if (farmListScrollViewUnit0 != null) {
                    farmListScrollViewUnit0.hidden = false;
                    farmListScrollViewUnit0.doScrollViewAutoOffset(farmListScrollViewUnit0.offsetScrollY);
                }
                FarmListScrollViewUnit farmListScrollViewUnit1 = this.farmListScrollViewUnitsArrayList.get(1);
                if (farmListScrollViewUnit1 != null) {
                    farmListScrollViewUnit1.hidden = true;
                }
            } else if (_listIndex == 1) {
                this.eggListSelectUnit.changeListIndex((short) 1);
                FarmListScrollViewUnit farmListScrollViewUnit12 = this.farmListScrollViewUnitsArrayList.get(1);
                if (farmListScrollViewUnit12 != null) {
                    farmListScrollViewUnit12.hidden = false;
                    farmListScrollViewUnit12.doScrollViewAutoOffset(farmListScrollViewUnit12.offsetScrollY);
                }
                FarmListScrollViewUnit farmListScrollViewUnit02 = this.farmListScrollViewUnitsArrayList.get(0);
                if (farmListScrollViewUnit02 != null) {
                    farmListScrollViewUnit02.hidden = true;
                }
            }
            doListViewSelectWithType((short) 0);
        }
    }

    public void changeListViewFastScrollDragViewOffset(float _offsetY, float _contentY) {
        if (this.farmListFrontViewUnit != null) {
            this.farmListFrontViewUnit.changeListViewFastScrollDragViewOffset(_offsetY, _contentY);
        }
    }

    public void doFarmListScrollViewUnitScroll(float _scrollRate) {
        if (this.farmListScrollViewUnitsArrayList != null) {
            float scrollRate = _scrollRate;
            if (scrollRate < BitmapDescriptorFactory.HUE_RED) {
                scrollRate = BitmapDescriptorFactory.HUE_RED;
            } else if (scrollRate > 1.0f) {
                scrollRate = 1.0f;
            }
            for (int i = 0; i < this.farmListScrollViewUnitsArrayList.size(); i++) {
                FarmListScrollViewUnit farmListScrollViewUnit = this.farmListScrollViewUnitsArrayList.get(i);
                if (farmListScrollViewUnit != null && !farmListScrollViewUnit.hidden) {
                    float offsetScrollY = scrollRate * farmListScrollViewUnit.offsetScrollYMax;
                    if (offsetScrollY < BitmapDescriptorFactory.HUE_RED) {
                        offsetScrollY = BitmapDescriptorFactory.HUE_RED;
                    } else if (offsetScrollY > farmListScrollViewUnit.offsetScrollYMax) {
                        offsetScrollY = farmListScrollViewUnit.offsetScrollYMax;
                    }
                    farmListScrollViewUnit.doScrollViewAutoOffset(offsetScrollY);
                    return;
                }
            }
        }
    }
}
