package com.idtinc.maingame.sublayout0;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.ckunit.CharacterDataDictionary;
import com.idtinc.ckunit.CharacterUnitDictionary;
import com.idtinc.ckunit.FarmUnitDictionary;
import com.idtinc.ckunit.ToolDataDictionary;
import com.idtinc.ckunit.ToolLevelDictionary;
import com.idtinc.ckunit.ToolUnitDictionary;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import com.idtinc.custom.CPDisplayUnit;
import com.idtinc.maingame.MainGameViewController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainGameUnit implements AlertUnitType0Delegate {
    private float CHARACTERUNITVIEW_CENTER_X;
    private float CHARACTERUNITVIEW_CENTER_Y;
    private float CHARACTERUNITVIEW_HEIGHT;
    private float CHARACTERUNITVIEW_SPACE_X;
    private float CHARACTERUNITVIEW_SPACE_X_RAND_RANGE;
    private float CHARACTERUNITVIEW_SPACE_Y;
    private float CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE;
    private float CHARACTERUNITVIEW_WIDTH;
    public float GAMEZONEVIEW_HEIGHT;
    public float GAMEZONEVIEW_OFFSET_X;
    public float GAMEZONEVIEW_OFFSET_Y;
    public float GAMEZONEVIEW_WIDTH;
    public float MAINGAMEBACK_TOUCH_RANGE_X_MAX;
    public float MAINGAMEBACK_TOUCH_RANGE_X_MIN;
    public float MAINGAMEBACK_TOUCH_RANGE_Y_MAX;
    public float MAINGAMEBACK_TOUCH_RANGE_Y_MIN;
    private float TOOL_1_SELECT_BACKVIEW_HEIGHT;
    private float TOOL_1_SELECT_BACKVIEW_OFFSET_X;
    private float TOOL_1_SELECT_BACKVIEW_OFFSET_Y;
    private float TOOL_1_SELECT_BACKVIEW_WIDTH;
    private float TOOL_1_SELECT_SCROLLVIEW_HEIGHT;
    private float TOOL_1_SELECT_SCROLLVIEW_OFFSET_X;
    private float TOOL_1_SELECT_SCROLLVIEW_OFFSET_Y;
    public float TOOL_1_SELECT_SCROLLVIEW_WIDTH;
    public float TOOL_1_SELECT_TOUCH_RANGE_X_MAX;
    public float TOOL_1_SELECT_TOUCH_RANGE_X_MIN;
    public float TOOL_1_SELECT_TOUCH_RANGE_Y_MAX;
    public float TOOL_1_SELECT_TOUCH_RANGE_Y_MIN;
    private AlertUnitType0 alertUnitType0;
    protected AppDelegate appDelegate;
    BottomBlockLayout bottomBlockLayout;
    private short clearEggsSubTag;
    private CPDisplayUnit cpDisplayUnit;
    private EggSelectUnit eggSelectUnit;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    private MainGameBackViewUnit mainGameBackViewUnit;
    private MainGameViewController mainGameViewController;
    public ArrayList<CharacterUnit> nowCharacterUnitViewsArrayList;
    public short nowStatus;
    private HorizontalScrollView tool_1_SelectScrolView = null;
    private Tool_1_SelectScrollUnit tool_1_SelectScrollUnit;
    private Tool_2_SelectListUnit tool_2_SelectListUnit;
    public Tool_2_SelectView tool_2_SelectView;
    private float zoomRate;

    public MainGameUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.clearEggsSubTag = (short) -1;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MAX = 320.0f;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MIN = 52.0f;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 348.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MAX = 320.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 348.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX = 430.0f;
        this.nowStatus = (short) -1;
        this.GAMEZONEVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.GAMEZONEVIEW_OFFSET_Y = 140.0f;
        this.GAMEZONEVIEW_WIDTH = 320.0f;
        this.GAMEZONEVIEW_HEIGHT = 140.0f;
        this.CHARACTERUNITVIEW_CENTER_X = 80.5f;
        this.CHARACTERUNITVIEW_CENTER_Y = 15.0f;
        this.CHARACTERUNITVIEW_SPACE_X = 31.0f;
        this.CHARACTERUNITVIEW_SPACE_Y = 26.0f;
        this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE = 6.0f;
        this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE = 6.0f;
        this.CHARACTERUNITVIEW_WIDTH = 60.0f;
        this.CHARACTERUNITVIEW_HEIGHT = 60.0f;
        this.TOOL_1_SELECT_BACKVIEW_WIDTH = 320.0f;
        this.TOOL_1_SELECT_BACKVIEW_HEIGHT = 132.0f;
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_Y = 348.0f;
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED;
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED;
        this.TOOL_1_SELECT_SCROLLVIEW_WIDTH = 320.0f;
        this.TOOL_1_SELECT_SCROLLVIEW_HEIGHT = 132.0f;
        this.appDelegate = null;
        this.mainGameViewController = null;
        this.nowCharacterUnitViewsArrayList = null;
        this.mainGameBackViewUnit = null;
        this.tool_2_SelectView = null;
        this.eggSelectUnit = null;
        this.tool_1_SelectScrollUnit = null;
        this.tool_2_SelectListUnit = null;
        this.cpDisplayUnit = null;
        this.bottomBlockLayout = null;
        this.appDelegate = _appDelegate;
        this.mainGameViewController = _mainGameViewController;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MAX = 320.0f * this.zoomRate;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MIN = 52.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 348.0f * this.zoomRate;
        } else {
            this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 436.0f * this.zoomRate;
        }
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MAX = 320.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 348.0f * this.zoomRate;
        } else {
            this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 436.0f * this.zoomRate;
        }
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX = this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN + (82.0f * this.zoomRate);
        this.nowStatus = (short) -1;
        this.clearEggsSubTag = (short) -1;
        this.GAMEZONEVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.GAMEZONEVIEW_OFFSET_Y = 140.0f * this.zoomRate;
        } else {
            this.GAMEZONEVIEW_OFFSET_Y = 184.0f * this.zoomRate;
        }
        this.GAMEZONEVIEW_WIDTH = 320.0f * this.zoomRate;
        this.GAMEZONEVIEW_HEIGHT = 140.0f * this.zoomRate;
        this.CHARACTERUNITVIEW_CENTER_X = 80.5f;
        this.CHARACTERUNITVIEW_CENTER_Y = 15.0f;
        this.CHARACTERUNITVIEW_SPACE_X = 31.0f;
        this.CHARACTERUNITVIEW_SPACE_Y = 26.0f;
        this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE = 6.0f;
        this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE = 6.0f;
        this.CHARACTERUNITVIEW_WIDTH = 60.0f;
        this.CHARACTERUNITVIEW_HEIGHT = 60.0f;
        this.nowCharacterUnitViewsArrayList = new ArrayList<>();
        for (int i = 0; i < this.appDelegate.CHARACTERUNITVIEW_TOTAL; i++) {
            CharacterUnit characterUnit = new CharacterUnit();
            characterUnit.init(-999.0f, -999.0f, this.CHARACTERUNITVIEW_WIDTH, this.CHARACTERUNITVIEW_HEIGHT, this.zoomRate, this);
            characterUnit.tag = (short) i;
            characterUnit.reset();
            this.nowCharacterUnitViewsArrayList.add(characterUnit);
        }
        this.mainGameBackViewUnit = new MainGameBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.tool_2_SelectView = new Tool_2_SelectView(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.eggSelectUnit = new EggSelectUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.TOOL_1_SELECT_BACKVIEW_WIDTH = 320.0f * this.zoomRate;
        this.TOOL_1_SELECT_BACKVIEW_HEIGHT = 132.0f * this.zoomRate;
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.TOOL_1_SELECT_BACKVIEW_OFFSET_Y = 348.0f * this.zoomRate;
        } else {
            this.TOOL_1_SELECT_BACKVIEW_OFFSET_Y = 436.0f * this.zoomRate;
        }
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_WIDTH = 320.0f * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_HEIGHT = this.TOOL_1_SELECT_BACKVIEW_HEIGHT;
        this.tool_1_SelectScrollUnit = new Tool_1_SelectScrollUnit((int) this.TOOL_1_SELECT_SCROLLVIEW_WIDTH, (int) this.TOOL_1_SELECT_SCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.bottomBlockLayout = new BottomBlockLayout((int) (320.0f * this.zoomRate), (int) (480.0f * this.zoomRate), this.zoomRate, this.appDelegate);
        } else {
            this.bottomBlockLayout = new BottomBlockLayout((int) (320.0f * this.zoomRate), (int) (568.0f * this.zoomRate), this.zoomRate, this.appDelegate);
        }
        this.cpDisplayUnit = new CPDisplayUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.cpDisplayUnit.setBackViewParams(210.0f, 311.0f);
        } else {
            this.cpDisplayUnit.setBackViewParams(210.0f, 355.0f);
        }
        this.tool_2_SelectListUnit = new Tool_2_SelectListUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
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
    }

    public void doInit() {
        this.tool_1_SelectScrollUnit.refresh();
        this.tool_2_SelectView.refresh();
        closeTool_2_SelectListLayout(false);
        reload();
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    public void doDisplay() throws Resources.NotFoundException {
        hiddenAlert();
        readyCheckTimeDoorOpenFromCK();
        refreshBitmap();
        this.tool_1_SelectScrollUnit.refresh();
        this.tool_2_SelectView.refresh();
        this.eggSelectUnit.refresh();
        closeTool_2_SelectListLayout(false);
        refreshPoint();
        displayInitManual();
    }

    public void doHidden() {
        hiddenAlert();
        closeTool_2_SelectListLayout(false);
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
        directCharacterToCP();
    }

    public void displayInitManual() throws Resources.NotFoundException {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("init_manual_kitchen", false)) {
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
            editor.putBoolean("init_manual_kitchen", false);
            editor.commit();
            hiddenAlert();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "『台所』の操作解説を見ますか？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『廚房』的教學說明嗎？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "";
                contentLabelString2 = "你想要看看『厨房』的教学说明吗？";
                contentLabelString3 = "";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else {
                titleLabelString = "";
                contentLabelString0 = "";
                contentLabelString1 = "Do you want to read ";
                contentLabelString2 = "the \"Kitchen\" manual?";
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

    public void setCookAlarmOn() {
        String alarmBodyString;
        if (this.appDelegate.timeSaveDictionary != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            short nowButtonIndexShort = this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex();
            if (nowButtonIndexShort < 0) {
                nowButtonIndexShort = -1;
            }
            String startDateString = this.appDelegate.timeSaveDictionary.getTool1SelectViewStartDate();
            float endSecondsFloat = this.appDelegate.timeSaveDictionary.getTool1SelectViewEndSeconds();
            if (startDateString != null && startDateString.length() > 0 && endSecondsFloat > 1.0f) {
                Date startDate = null;
                try {
                    startDate = sdf.parse(startDateString);
                } catch (ParseException e) {
                }
                Date endDate = null;
                if (startDate != null) {
                    endDate = new Date(startDate.getTime() + ((long) (1000.0f * endSecondsFloat)));
                }
                if (endDate != null && endDate.after(nowDate)) {
                    ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, nowButtonIndexShort);
                    String tool1NameString = "";
                    String languageString = this.appDelegate.getLocaleLanguage();
                    if (languageString.equals("ja-JP")) {
                        if (toolDataDictionary != null) {
                            String nameString = toolDataDictionary.getTitleJa();
                            if (nameString.length() > 0) {
                                tool1NameString = String.valueOf(nameString) + "で";
                            }
                        }
                        alarmBodyString = "☆ " + tool1NameString + "調理完了しました！ ☆";
                    } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                        if (toolDataDictionary != null) {
                            String nameString2 = toolDataDictionary.getTitleZhTW();
                            if (nameString2.length() > 0) {
                                tool1NameString = "使用" + nameString2;
                            }
                        }
                        alarmBodyString = "☆ " + tool1NameString + "調理已經完成了,趕快來收成吧！ ☆";
                    } else if (languageString.equals("zh-CN")) {
                        if (toolDataDictionary != null) {
                            String nameString3 = toolDataDictionary.getTitleZhCN();
                            if (nameString3.length() > 0) {
                                tool1NameString = "使用" + nameString3;
                            }
                        }
                        alarmBodyString = "☆ " + tool1NameString + "调理已经完成了,赶快来收成吧！ ☆";
                    } else {
                        if (toolDataDictionary != null) {
                            String nameString4 = toolDataDictionary.getTitleEn();
                            if (nameString4.length() > 0) {
                                tool1NameString = "(" + nameString4 + ")";
                            }
                        }
                        alarmBodyString = "☆ Hatching completed！ " + tool1NameString + " ☆";
                    }
                    short nowCookingEggID = this.appDelegate.getNowCookingEggID();
                    if (nowCookingEggID == 1) {
                        this.appDelegate.setCookAlarmOnWithFireDateString(sdf.format(endDate), alarmBodyString, "1");
                    } else {
                        this.appDelegate.setCookAlarmOnWithFireDateString(sdf.format(endDate), alarmBodyString, "0");
                    }
                }
            }
        }
    }

    public void checkCookAlarm() {
        if (this.appDelegate.defaultSharedPreferences != null) {
            if (this.appDelegate.defaultSharedPreferences.getBoolean("cook_alarm", false)) {
                this.appDelegate.removeAlarmNotificationWithNotificationID("cook_alarm");
                setCookAlarmOn();
            } else {
                this.appDelegate.removeAlarmNotificationWithNotificationID("cook_alarm");
            }
        }
    }

    public void readyCheckTimeDoorOpenFromCK() {
        if (!this.hidden) {
            new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.1
                @Override // java.lang.Runnable
                public void run() {
                    MainGameUnit.this.checkTimeDoorOpenFromCK();
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
                if (this.appDelegate.defaultSharedPreferences != null) {
                    String ck_send_kitchen_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_kitchen_string", "");
                    if (ck_send_kitchen_string.length() > 0) {
                        displayCheckReceiveKitchenFromCKAlert();
                    }
                }
            }
        }
    }

    public void displayCheckReceiveKitchenFromCKAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        int tool_1_levelShort;
        if (!this.hidden) {
            HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
            if (tool_1_HashMap != null && tool_1_HashMap.size() == 7) {
                hiddenAlert();
                int tool_1_KindCnt = 0;
                int kitchenLevelShort = -1;
                if (tool_1_HashMap.get("-1") != null) {
                    kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
                    tool_1_HashMap.remove("-1");
                    if (kitchenLevelShort >= 0 && kitchenLevelShort <= 2) {
                        String infosString = "";
                        for (int i = 0; i < 6; i++) {
                            if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) != null && (tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue()) >= 0 && tool_1_levelShort <= 2) {
                                if (infosString.length() <= 0) {
                                    infosString = String.valueOf(i) + "_" + tool_1_levelShort;
                                } else {
                                    infosString = String.valueOf(infosString) + "," + i + "_" + tool_1_levelShort;
                                }
                            }
                        }
                        if (infosString.length() > 0) {
                            tool_1_KindCnt = this.alertUnitType0.set_Tool_1_Images_View_Infos(infosString);
                        }
                    } else {
                        kitchenLevelShort = -1;
                    }
                }
                String languageString = this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    titleLabelString = "台所Lv." + (kitchenLevelShort + 1);
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "この台所と同期します。よろしいですか？";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString = "廚房Lv." + (kitchenLevelShort + 1);
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "是否要同步從時空門傳送來的廚房？";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString = "厨房Lv." + (kitchenLevelShort + 1);
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "是否要同步从时空门传送来的厨房？";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                } else {
                    titleLabelString = "Kitchen Lv." + (kitchenLevelShort + 1);
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "Do you want to sync with this kitchen?";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                }
                this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
                this.alertUnitType0.tag = (short) 10;
                this.alertUnitType0.subTag = (short) -1;
                popAlert();
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(16);
                }
            }
        }
    }

    public void displayCheckLoseTool1Alert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        int tool_1_levelShort;
        if (!this.hidden) {
            HashMap<String, String> lose_tool_1_HashMap = getLose_Tool_1_HashMap();
            int lose_Tool_1_Cnt = 0;
            if (lose_tool_1_HashMap != null) {
                lose_Tool_1_Cnt = lose_tool_1_HashMap.size();
            }
            if (lose_Tool_1_Cnt > 0) {
                hiddenAlert();
                int tool_1_KindCnt = 0;
                if (lose_tool_1_HashMap != null) {
                    String infosString = "";
                    for (int i = 0; i < 8; i++) {
                        if (lose_tool_1_HashMap.get(new StringBuilder().append(i).toString()) != null && (tool_1_levelShort = Integer.valueOf(lose_tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue()) >= 0 && tool_1_levelShort <= 2) {
                            if (infosString.length() <= 0) {
                                infosString = String.valueOf(i) + "_" + tool_1_levelShort;
                            } else {
                                infosString = String.valueOf(infosString) + "," + i + "_" + tool_1_levelShort;
                            }
                        }
                    }
                    if (infosString.length() > 0) {
                        tool_1_KindCnt = this.alertUnitType0.set_Tool_1_Images_View_Infos(infosString);
                    }
                }
                String languageString = this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    titleLabelString = "調理器具がなくなる";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "同期すると、以上の調理器具がな";
                    contentLabelString4 = "くなります。よろしいですか？";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
                    } else {
                        contentLabelLanguageOffsetY = 6.0f;
                    }
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString = "失去調理用具";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "將會失去以上調理用具。確定要同步廚房？";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString = "失去調理用具";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "将会失去以上调理用具。确定要同步厨房？";
                    contentLabelString4 = "";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = 8.0f;
                    } else {
                        contentLabelLanguageOffsetY = 16.0f;
                    }
                } else {
                    titleLabelString = "Lose Kitchenware";
                    contentLabelString0 = "";
                    contentLabelString1 = "";
                    contentLabelString2 = "";
                    contentLabelString3 = "You will lose these kitchenware.";
                    contentLabelString4 = "Do you want to sync?";
                    if (tool_1_KindCnt <= 5) {
                        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
                    } else {
                        contentLabelLanguageOffsetY = 6.0f;
                    }
                }
                this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
                this.alertUnitType0.tag = (short) 11;
                this.alertUnitType0.subTag = (short) -1;
                popAlert();
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
            } else {
                displayResetCharacterUnitViewsArrayAlert();
            }
        }
    }

    public void displayResetCharacterUnitViewsArrayAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        short tool_1_selectview_nowbuttonindex_level;
        HashMap<String, String> tool_1_HashMap;
        if (!this.hidden && this.appDelegate != null) {
            boolean cancelAllF = false;
            short tool_1_selectview_nowbuttonindex = this.appDelegate.getShort_tool_1_selectview_nowbuttonindex();
            if (tool_1_selectview_nowbuttonindex >= 0 && (tool_1_selectview_nowbuttonindex_level = this.appDelegate.getTool1LevelWithIndex(tool_1_selectview_nowbuttonindex)) >= 0 && (tool_1_HashMap = getReceive_Tool_1_HashMap()) != null) {
                if (tool_1_selectview_nowbuttonindex < 6) {
                    if (tool_1_HashMap.get(new StringBuilder().append((int) tool_1_selectview_nowbuttonindex).toString()) != null) {
                        int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append((int) tool_1_selectview_nowbuttonindex).toString())).intValue();
                        if (tool_1_levelShort < tool_1_selectview_nowbuttonindex_level) {
                            cancelAllF = true;
                        }
                    }
                } else if (tool_1_selectview_nowbuttonindex == 6 && tool_1_HashMap.get("-1") != null) {
                    int kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
                    if (kitchenLevelShort < 3) {
                        cancelAllF = true;
                    }
                }
            }
            if (cancelAllF) {
                hiddenAlert();
                String languageString = this.appDelegate.getLocaleLanguage();
                if (languageString.equals("ja-JP")) {
                    titleLabelString = "調理中止";
                    contentLabelString0 = "";
                    contentLabelString1 = "調理を中止し、同期します。";
                    contentLabelString2 = "よろしいですか？";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    titleLabelString = "調理中斷";
                    contentLabelString0 = "";
                    contentLabelString1 = "你確定要中斷目前的調理,";
                    contentLabelString2 = "然後同步廚房嗎？";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                } else if (languageString.equals("zh-CN")) {
                    titleLabelString = "調理中斷";
                    contentLabelString0 = "";
                    contentLabelString1 = "你确定要中断目前的调理,";
                    contentLabelString2 = "然後同步厨房吗？";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                } else {
                    titleLabelString = "Interrupt Hatch";
                    contentLabelString0 = "";
                    contentLabelString1 = "Are you sure you want to interrupt";
                    contentLabelString2 = "current hatch,and then sync?";
                    contentLabelString3 = "";
                    contentLabelString4 = "";
                    contentLabelLanguageOffsetY = 10.0f;
                }
                this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
                this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
                this.alertUnitType0.tag = (short) 12;
                this.alertUnitType0.subTag = (short) -1;
                popAlert();
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                    return;
                }
                return;
            }
            doReceiveKitchenFromCK();
        }
    }

    public void doReceiveKitchenFromCK() {
        ArrayList<ToolUnitDictionary> tool0DictionarysArrayList;
        ToolUnitDictionary tool0Dictionary;
        if (this.appDelegate != null && this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 1) {
            boolean saveF = false;
            int kitchenLevelShort = -1;
            HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
            if (tool_1_HashMap != null && tool_1_HashMap.size() == 7 && tool_1_HashMap.get("-1") != null) {
                kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
            }
            if (tool_1_HashMap != null && kitchenLevelShort >= 0 && kitchenLevelShort <= 2 && (tool0DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && tool0DictionarysArrayList.size() > 0 && (tool0Dictionary = tool0DictionarysArrayList.get(0)) != null) {
                tool0Dictionary.setLevel((short) kitchenLevelShort);
                saveF = true;
                ArrayList<ToolUnitDictionary> tool1DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1);
                if (tool1DictionarysArrayList != null && tool1DictionarysArrayList.size() > 6) {
                    short tool_1_selectview_nowbuttonindex = this.appDelegate.getShort_tool_1_selectview_nowbuttonindex();
                    for (int i = 0; i < tool1DictionarysArrayList.size(); i++) {
                        ToolUnitDictionary tool1Dictionary = tool1DictionarysArrayList.get(i);
                        if (tool1Dictionary != null) {
                            if (i < 6) {
                                if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) != null) {
                                    int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
                                    if (tool_1_levelShort < -2 || tool_1_levelShort > 2) {
                                        tool_1_levelShort = -2;
                                    }
                                    if (i == tool_1_selectview_nowbuttonindex) {
                                        short now_tool_1_levelShort = this.appDelegate.getTool1LevelWithIndex((short) i);
                                        if (now_tool_1_levelShort > tool_1_levelShort) {
                                            resetCharacterUnitViewsArray();
                                        }
                                    }
                                    tool1Dictionary.setLevel((short) tool_1_levelShort);
                                    saveF = true;
                                }
                            } else if (i == 6) {
                                if (kitchenLevelShort < 3) {
                                    if (i == tool_1_selectview_nowbuttonindex) {
                                        resetCharacterUnitViewsArray();
                                    }
                                    tool1Dictionary.setLevel((short) -2);
                                    saveF = true;
                                }
                            } else if (i == 7 && kitchenLevelShort < 3) {
                                if (i == tool_1_selectview_nowbuttonindex) {
                                    resetCharacterUnitViewsArray();
                                }
                                tool1Dictionary.setLevel((short) -2);
                                saveF = true;
                            }
                            this.appDelegate.displayFullAdView();
                        }
                    }
                }
            }
            if (saveF) {
                if (this.appDelegate.timeSaveDictionary != null) {
                    this.mainGameViewController.refreshAndSave();
                }
                if (this.mainGameBackViewUnit != null) {
                    this.mainGameBackViewUnit.refreshBitmap();
                }
                if (this.tool_1_SelectScrollUnit != null) {
                    this.tool_1_SelectScrollUnit.refresh();
                }
            }
            if (tool_1_HashMap != null) {
            }
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(17);
            }
            cleanCkSendKitchenString();
        }
    }

    public void cleanCkSendKitchenString() {
        if (this.appDelegate != null) {
            if (this.appDelegate.defaultSharedPreferences == null) {
                this.appDelegate.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
            }
            if (this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                editor.putString("ck_send_kitchen_string", "");
                editor.commit();
            }
        }
    }

    public HashMap<String, String> getLose_Tool_1_HashMap() {
        ArrayList<ToolUnitDictionary> tool0DictionarysArrayList;
        ArrayList<ToolUnitDictionary> tool1DictionarysArrayList;
        HashMap<String, String> lose_tool_1_HashMap = null;
        if (this.appDelegate != null && this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() > 1) {
            int kitchenLevelShort = -1;
            HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
            if (tool_1_HashMap != null && tool_1_HashMap.size() == 7 && tool_1_HashMap.get("-1") != null) {
                kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
            }
            if (tool_1_HashMap != null && kitchenLevelShort >= 0 && kitchenLevelShort <= 2 && (tool0DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0)) != null && tool0DictionarysArrayList.size() > 0) {
                ToolUnitDictionary tool0Dictionary = tool0DictionarysArrayList.get(0);
                if (tool0Dictionary != null && (tool1DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1)) != null) {
                    lose_tool_1_HashMap = new HashMap<>();
                    for (int i = 0; i < tool1DictionarysArrayList.size(); i++) {
                        short tool1Level = this.appDelegate.getTool1LevelWithIndex((short) i);
                        if (tool1Level >= 0) {
                            if (i < 6) {
                                if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) != null) {
                                    int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
                                    if (tool_1_levelShort < tool1Level) {
                                        lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append((int) tool1Level).toString());
                                    }
                                }
                            } else if (i == 6) {
                                if (kitchenLevelShort < 3) {
                                    lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append((int) tool1Level).toString());
                                }
                            } else if (i == 7 && kitchenLevelShort < 3) {
                                lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append((int) tool1Level).toString());
                            }
                        }
                    }
                }
            }
            if (tool_1_HashMap != null) {
            }
            if (lose_tool_1_HashMap != null && lose_tool_1_HashMap.size() <= 0) {
                lose_tool_1_HashMap = null;
            }
            return lose_tool_1_HashMap;
        }
        return null;
    }

    public HashMap<String, String> getReceive_Tool_1_HashMap() {
        String[] sendStringItems;
        int kitchenLevelShort;
        String[] tool_1_StringItems;
        int tool_1_idShort;
        HashMap<String, String> tool_1_HashMap = null;
        if (this.appDelegate != null && this.appDelegate.defaultSharedPreferences != null) {
            String ck_send_kitchen_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_kitchen_string", "");
            if (ck_send_kitchen_string.length() > 0 && (sendStringItems = ck_send_kitchen_string.split("=")) != null && sendStringItems.length == 3 && sendStringItems[1] != null && sendStringItems[2] != null && (kitchenLevelShort = Integer.valueOf(sendStringItems[1]).intValue()) >= 0 && kitchenLevelShort <= 2) {
                tool_1_HashMap = new HashMap<>();
                tool_1_HashMap.put("-1", new StringBuilder().append(kitchenLevelShort).toString());
                String tool_1_String = sendStringItems[2];
                if (tool_1_String != null && tool_1_String.length() > 0 && (tool_1_StringItems = tool_1_String.split(",")) != null && tool_1_StringItems.length == 6) {
                    for (int i = 0; i < tool_1_StringItems.length && tool_1_StringItems[i] != null && tool_1_StringItems[i].length() > 0; i++) {
                        String[] tool_1_StringItem = tool_1_StringItems[i].split("_");
                        if (tool_1_StringItem != null && tool_1_StringItem.length == 2 && (tool_1_idShort = Integer.valueOf(tool_1_StringItem[0]).intValue()) >= 0 && tool_1_idShort < 6) {
                            int tool_1_levelShort = Integer.valueOf(tool_1_StringItem[1]).intValue();
                            if (tool_1_levelShort < -2 || tool_1_levelShort > 2) {
                                tool_1_levelShort = -2;
                            }
                            tool_1_HashMap.put(new StringBuilder().append(tool_1_idShort).toString(), new StringBuilder().append(tool_1_levelShort).toString());
                        }
                    }
                }
            }
        }
        if (tool_1_HashMap != null && tool_1_HashMap.size() != 7) {
            return null;
        }
        return tool_1_HashMap;
    }

    public void refreshPoint() {
        this.cpDisplayUnit.refresh();
    }

    public void doWillTerminate() {
        Log.d("FarmLayout", "doWillTerminate");
        hiddenAlert();
    }

    public void doWillEnterForeground() {
        Log.d("mainGameLayout", "doWillEnterForeground");
        hiddenAlert();
        this.tool_1_SelectScrollUnit.refresh();
        this.tool_2_SelectView.refresh();
        closeTool_2_SelectListLayout(false);
        reload();
    }

    public void openTool_2_SelectListLayout() {
        this.tool_2_SelectListUnit.open();
        this.tool_2_SelectListUnit.hidden = false;
    }

    public void closeTool_2_SelectListLayout(boolean _animeF) {
        this.tool_2_SelectListUnit.hidden = true;
    }

    public void tool_2_SelectListViewSelected() {
        this.mainGameViewController.refreshAndSave();
        this.tool_2_SelectView.refresh();
    }

    public ArrayList<Short> getRateArrayWithID(short _eggID, short _tool1_ID, short _tool2_0_ID, short _tool2_1_ID) throws NumberFormatException {
        ArrayList<FarmUnitDictionary> farmUnitDictionarysArrayList;
        FarmUnitDictionary farmUnitDictionary;
        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
        float totalChars0Cnt = BitmapDescriptorFactory.HUE_RED;
        float totalChars1Cnt = BitmapDescriptorFactory.HUE_RED;
        if (this.appDelegate.timeSaveDictionary != null && this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList != null) {
            for (int i = 0; i < this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size(); i++) {
                if (i < this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size() && (farmUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(i)) != null) {
                    for (int j = 0; j < farmUnitDictionarysArrayList.size(); j++) {
                        if (j < farmUnitDictionarysArrayList.size() && (farmUnitDictionary = farmUnitDictionarysArrayList.get(j)) != null) {
                            float totalCountFloat = farmUnitDictionary.getTotalCount();
                            if (totalCountFloat > 0.0d) {
                                if (i == 0) {
                                    totalChars0Cnt += totalCountFloat;
                                } else if (i == 1) {
                                    totalChars1Cnt += totalCountFloat;
                                }
                            }
                            Log.d("MainGameLayout", "i:" + i + ",j:" + j + ",totalCountFloat:" + ((int) totalCountFloat));
                        }
                    }
                }
            }
        }
        float totalCharsAllCnt = totalChars0Cnt + totalChars1Cnt;
        ArrayList<Short> allRateArrayList = new ArrayList<>();
        ArrayList<Short> returnRateArrayList = null;
        Log.d("MainGameLayout", "_tool1_ID:" + ((int) _tool1_ID));
        if (_eggID == 0) {
            if (_tool1_ID == 0) {
                short tool_1_level = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 0, allRateArrayList);
                if (totalChars0Cnt >= 1000.0d) {
                    addCharacterToAllRateArrayList(_eggID, (short) 31, allRateArrayList);
                }
                if (totalChars0Cnt >= 2000.0d) {
                    addCharacterToAllRateArrayList(_eggID, (short) 81, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_48", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_48: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 48, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_49", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_49: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 49, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_60", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_60: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 60, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_61", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_61: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 61, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_62", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_62: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 62, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_63", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_63: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 63, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_64", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_64: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 64, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_65", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_65: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 65, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_66", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_66: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 66, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_67", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_67: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 67, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_78", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_78: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 78, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_79", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_79: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 79, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_80", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_80: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 80, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_83", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_83: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 83, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_84", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_84: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 84, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_85", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_85: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 85, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_86", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_86: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 86, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_87", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_87: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 87, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_88", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_88: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 88, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_105", false)) {
                    Log.d("MainGameLayout", "campaign_char_0_105: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 105, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 0 || this.tool_2_SelectView.tool_2_1 == 0 || this.tool_2_SelectView.tool_2_2 == 0) {
                    addCharacterToAllRateArrayList(_eggID, (short) 5, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 11 || this.tool_2_SelectView.tool_2_1 == 11 || this.tool_2_SelectView.tool_2_2 == 11) {
                    addCharacterToAllRateArrayList(_eggID, (short) 20, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 15 || this.tool_2_SelectView.tool_2_1 == 15 || this.tool_2_SelectView.tool_2_2 == 15) {
                    addCharacterToAllRateArrayList(_eggID, (short) 25, allRateArrayList);
                    if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_26", false)) {
                        Log.d("MainGameLayout", "campaign_char_0_26: YES");
                        addCharacterToAllRateArrayList(_eggID, (short) 26, allRateArrayList);
                    }
                    if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_27", false)) {
                        Log.d("MainGameLayout", "campaign_char_0_27: YES");
                        addCharacterToAllRateArrayList(_eggID, (short) 27, allRateArrayList);
                    }
                }
                if (this.tool_2_SelectView.tool_2_0 == 18 || this.tool_2_SelectView.tool_2_1 == 18 || this.tool_2_SelectView.tool_2_2 == 18) {
                    addCharacterToAllRateArrayList(_eggID, (short) 30, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 36 || this.tool_2_SelectView.tool_2_1 == 36 || this.tool_2_SelectView.tool_2_2 == 36) {
                    addCharacterToAllRateArrayList(_eggID, (short) 50, allRateArrayList);
                }
                if (tool_1_level >= 1) {
                    addCharacterToAllRateArrayList(_eggID, (short) 33, allRateArrayList);
                }
                if (tool_1_level >= 2) {
                    short randIndex = (short) (Math.random() * 10.0d);
                    if (randIndex == 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH");
                        Date nowDate = new Date();
                        int hourInt = Integer.parseInt(sdf.format(nowDate));
                        if (hourInt == 10 || hourInt == 11 || hourInt == 12) {
                            addCharacterToAllRateArrayList(_eggID, (short) 52, allRateArrayList);
                        } else {
                            addCharacterToAllRateArrayList(_eggID, (short) 51, allRateArrayList);
                        }
                    }
                }
                if (tool_0_0_level >= 3) {
                    addCharacterToAllRateArrayList(_eggID, (short) 108, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 35 || this.tool_2_SelectView.tool_2_1 == 35 || this.tool_2_SelectView.tool_2_2 == 35) {
                    addCharacterToAllRateArrayList(_eggID, (short) 47, allRateArrayList);
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 68 || this.tool_2_SelectView.tool_2_1 == 68 || this.tool_2_SelectView.tool_2_2 == 68) {
                    short idShort = -1;
                    if (this.appDelegate != null && this.appDelegate.defaultSharedPreferences != null) {
                        idShort = (short) this.appDelegate.defaultSharedPreferences.getInt("gift_tool_2_68_character_id", -1);
                    }
                    if (idShort < 89 || idShort > 103) {
                        idShort = -1;
                    }
                    if (idShort >= 0) {
                        if (this.appDelegate != null && this.appDelegate.defaultSharedPreferences != null) {
                            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                            editor.putInt("gift_tool_2_68_egg_id", -1);
                            editor.putInt("gift_tool_2_68_character_id", -1);
                            editor.commit();
                        }
                    } else {
                        short randNum = (short) (Math.random() * 100.0d);
                        if (randNum < 1) {
                            idShort = 89;
                        } else if (randNum < 4) {
                            idShort = 90;
                        } else if (randNum < 10) {
                            idShort = 91;
                        } else if (randNum < 19) {
                            idShort = 92;
                        } else if (randNum < 28) {
                            idShort = 93;
                        } else if (randNum < 37) {
                            idShort = 97;
                        } else if (randNum < 46) {
                            idShort = 94;
                        } else if (randNum < 55) {
                            idShort = 98;
                        } else if (randNum < 64) {
                            idShort = 99;
                        } else if (randNum < 73) {
                            idShort = 95;
                        } else if (randNum < 82) {
                            idShort = 100;
                        } else if (randNum < 91) {
                            idShort = 101;
                        } else if (randNum < 97) {
                            idShort = 102;
                        } else {
                            idShort = 96;
                        }
                    }
                    returnRateArrayList.remove(0);
                    short randInsertIndex = (short) (Math.random() * returnRateArrayList.size());
                    returnRateArrayList.add(randInsertIndex, Short.valueOf(idShort));
                }
            } else if (_tool1_ID == 1) {
                short tool_1_level2 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 3, allRateArrayList);
                addCharacterToAllRateArrayList(_eggID, (short) 4, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 1 || this.tool_2_SelectView.tool_2_1 == 1 || this.tool_2_SelectView.tool_2_2 == 1) {
                    addCharacterToAllRateArrayList(_eggID, (short) 6, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 2 || this.tool_2_SelectView.tool_2_1 == 2 || this.tool_2_SelectView.tool_2_2 == 2) {
                    addCharacterToAllRateArrayList(_eggID, (short) 7, allRateArrayList);
                }
                if (tool_1_level2 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 19 || this.tool_2_SelectView.tool_2_1 == 19 || this.tool_2_SelectView.tool_2_2 == 19) && (this.tool_2_SelectView.tool_2_0 == 20 || this.tool_2_SelectView.tool_2_1 == 20 || this.tool_2_SelectView.tool_2_2 == 20)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 36, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 30 || this.tool_2_SelectView.tool_2_1 == 30 || this.tool_2_SelectView.tool_2_2 == 30) && (this.tool_2_SelectView.tool_2_0 == 31 || this.tool_2_SelectView.tool_2_1 == 31 || this.tool_2_SelectView.tool_2_2 == 31)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 43, allRateArrayList);
                    }
                    if (tool_1_level2 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 25 || this.tool_2_SelectView.tool_2_1 == 25 || this.tool_2_SelectView.tool_2_2 == 25) && ((this.tool_2_SelectView.tool_2_0 == 37 || this.tool_2_SelectView.tool_2_1 == 37 || this.tool_2_SelectView.tool_2_2 == 37) && (this.tool_2_SelectView.tool_2_0 == 38 || this.tool_2_SelectView.tool_2_1 == 38 || this.tool_2_SelectView.tool_2_2 == 38)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 55, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 69 || this.tool_2_SelectView.tool_2_1 == 69 || this.tool_2_SelectView.tool_2_2 == 69) {
                    returnRateArrayList.remove(0);
                    short randInsertIndex2 = (short) (Math.random() * returnRateArrayList.size());
                    returnRateArrayList.add(randInsertIndex2, (short) 106);
                }
            } else if (_tool1_ID == 2) {
                short tool_1_level3 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 8, allRateArrayList);
                addCharacterToAllRateArrayList(_eggID, (short) 9, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 3 || this.tool_2_SelectView.tool_2_1 == 3 || this.tool_2_SelectView.tool_2_2 == 3) {
                    addCharacterToAllRateArrayList(_eggID, (short) 10, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 4 || this.tool_2_SelectView.tool_2_1 == 4 || this.tool_2_SelectView.tool_2_2 == 4) {
                    addCharacterToAllRateArrayList(_eggID, (short) 11, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 17 || this.tool_2_SelectView.tool_2_1 == 17 || this.tool_2_SelectView.tool_2_2 == 17) {
                    addCharacterToAllRateArrayList(_eggID, (short) 29, allRateArrayList);
                    if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_32", false)) {
                        Log.d("MainGameLayout", "campaign_char_0_32: YES");
                        addCharacterToAllRateArrayList(_eggID, (short) 32, allRateArrayList);
                    }
                }
                if (tool_1_level3 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 21 || this.tool_2_SelectView.tool_2_1 == 21 || this.tool_2_SelectView.tool_2_2 == 21) && (this.tool_2_SelectView.tool_2_0 == 22 || this.tool_2_SelectView.tool_2_1 == 22 || this.tool_2_SelectView.tool_2_2 == 22)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 37, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 20 || this.tool_2_SelectView.tool_2_1 == 20 || this.tool_2_SelectView.tool_2_2 == 20) && (this.tool_2_SelectView.tool_2_0 == 23 || this.tool_2_SelectView.tool_2_1 == 23 || this.tool_2_SelectView.tool_2_2 == 23)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 38, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 9 || this.tool_2_SelectView.tool_2_1 == 9 || this.tool_2_SelectView.tool_2_2 == 9) && (this.tool_2_SelectView.tool_2_0 == 24 || this.tool_2_SelectView.tool_2_1 == 24 || this.tool_2_SelectView.tool_2_2 == 24)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 39, allRateArrayList);
                    }
                    if (tool_1_level3 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 19 || this.tool_2_SelectView.tool_2_1 == 19 || this.tool_2_SelectView.tool_2_2 == 19) && ((this.tool_2_SelectView.tool_2_0 == 39 || this.tool_2_SelectView.tool_2_1 == 39 || this.tool_2_SelectView.tool_2_2 == 39) && (this.tool_2_SelectView.tool_2_0 == 40 || this.tool_2_SelectView.tool_2_1 == 40 || this.tool_2_SelectView.tool_2_2 == 40)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 56, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 70 || this.tool_2_SelectView.tool_2_1 == 70 || this.tool_2_SelectView.tool_2_2 == 70) {
                    returnRateArrayList.remove(0);
                    short randInsertIndex3 = (short) (Math.random() * returnRateArrayList.size());
                    returnRateArrayList.add(randInsertIndex3, (short) 107);
                }
            } else if (_tool1_ID == 3) {
                short tool_1_level4 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 12, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 5 || this.tool_2_SelectView.tool_2_1 == 5 || this.tool_2_SelectView.tool_2_2 == 5) {
                    addCharacterToAllRateArrayList(_eggID, (short) 13, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 6 || this.tool_2_SelectView.tool_2_1 == 6 || this.tool_2_SelectView.tool_2_2 == 6) {
                    addCharacterToAllRateArrayList(_eggID, (short) 14, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 7 || this.tool_2_SelectView.tool_2_1 == 7 || this.tool_2_SelectView.tool_2_2 == 7) {
                    addCharacterToAllRateArrayList(_eggID, (short) 15, allRateArrayList);
                }
                if (tool_1_level4 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 14 || this.tool_2_SelectView.tool_2_1 == 14 || this.tool_2_SelectView.tool_2_2 == 14) && (this.tool_2_SelectView.tool_2_0 == 25 || this.tool_2_SelectView.tool_2_1 == 25 || this.tool_2_SelectView.tool_2_2 == 25)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 40, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 25 || this.tool_2_SelectView.tool_2_1 == 25 || this.tool_2_SelectView.tool_2_2 == 25) && (this.tool_2_SelectView.tool_2_0 == 32 || this.tool_2_SelectView.tool_2_1 == 32 || this.tool_2_SelectView.tool_2_2 == 32)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 44, allRateArrayList);
                    }
                    if (tool_1_level4 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 9 || this.tool_2_SelectView.tool_2_1 == 9 || this.tool_2_SelectView.tool_2_2 == 9) && ((this.tool_2_SelectView.tool_2_0 == 30 || this.tool_2_SelectView.tool_2_1 == 30 || this.tool_2_SelectView.tool_2_2 == 30) && (this.tool_2_SelectView.tool_2_0 == 41 || this.tool_2_SelectView.tool_2_1 == 41 || this.tool_2_SelectView.tool_2_2 == 41)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 57, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 4) {
                short tool_1_level5 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 16, allRateArrayList);
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_82", false) && totalChars0Cnt >= 2000.0d) {
                    addCharacterToAllRateArrayList(_eggID, (short) 82, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 8 || this.tool_2_SelectView.tool_2_1 == 8 || this.tool_2_SelectView.tool_2_2 == 8) {
                    addCharacterToAllRateArrayList(_eggID, (short) 17, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 9 || this.tool_2_SelectView.tool_2_1 == 9 || this.tool_2_SelectView.tool_2_2 == 9) {
                    addCharacterToAllRateArrayList(_eggID, (short) 18, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 10 || this.tool_2_SelectView.tool_2_1 == 10 || this.tool_2_SelectView.tool_2_2 == 10) {
                    addCharacterToAllRateArrayList(_eggID, (short) 19, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 16 || this.tool_2_SelectView.tool_2_1 == 16 || this.tool_2_SelectView.tool_2_2 == 16) {
                    addCharacterToAllRateArrayList(_eggID, (short) 28, allRateArrayList);
                }
                if (tool_1_level5 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 26 || this.tool_2_SelectView.tool_2_1 == 26 || this.tool_2_SelectView.tool_2_2 == 26) && (this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 41, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 23 || this.tool_2_SelectView.tool_2_1 == 23 || this.tool_2_SelectView.tool_2_2 == 23) && (this.tool_2_SelectView.tool_2_0 == 33 || this.tool_2_SelectView.tool_2_1 == 33 || this.tool_2_SelectView.tool_2_2 == 33)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 45, allRateArrayList);
                    }
                    if (tool_1_level5 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27) && ((this.tool_2_SelectView.tool_2_0 == 41 || this.tool_2_SelectView.tool_2_1 == 41 || this.tool_2_SelectView.tool_2_2 == 41) && (this.tool_2_SelectView.tool_2_0 == 42 || this.tool_2_SelectView.tool_2_1 == 42 || this.tool_2_SelectView.tool_2_2 == 42)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 58, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 5) {
                short tool_1_level6 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 21, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 12 || this.tool_2_SelectView.tool_2_1 == 12 || this.tool_2_SelectView.tool_2_2 == 12) {
                    addCharacterToAllRateArrayList(_eggID, (short) 22, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 13 || this.tool_2_SelectView.tool_2_1 == 13 || this.tool_2_SelectView.tool_2_2 == 13) {
                    addCharacterToAllRateArrayList(_eggID, (short) 23, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 14 || this.tool_2_SelectView.tool_2_1 == 14 || this.tool_2_SelectView.tool_2_2 == 14) {
                    addCharacterToAllRateArrayList(_eggID, (short) 24, allRateArrayList);
                }
                if (tool_1_level6 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 28 || this.tool_2_SelectView.tool_2_1 == 28 || this.tool_2_SelectView.tool_2_2 == 28) && (this.tool_2_SelectView.tool_2_0 == 29 || this.tool_2_SelectView.tool_2_1 == 29 || this.tool_2_SelectView.tool_2_2 == 29)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 42, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 23 || this.tool_2_SelectView.tool_2_1 == 23 || this.tool_2_SelectView.tool_2_2 == 23) && (this.tool_2_SelectView.tool_2_0 == 34 || this.tool_2_SelectView.tool_2_1 == 34 || this.tool_2_SelectView.tool_2_2 == 34)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 46, allRateArrayList);
                    }
                    if (tool_1_level6 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 22 || this.tool_2_SelectView.tool_2_1 == 22 || this.tool_2_SelectView.tool_2_2 == 22) && ((this.tool_2_SelectView.tool_2_0 == 24 || this.tool_2_SelectView.tool_2_1 == 24 || this.tool_2_SelectView.tool_2_2 == 24) && (this.tool_2_SelectView.tool_2_0 == 43 || this.tool_2_SelectView.tool_2_1 == 43 || this.tool_2_SelectView.tool_2_2 == 43)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 59, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 6) {
                short tool_1_level7 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 69, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 56 || this.tool_2_SelectView.tool_2_1 == 56 || this.tool_2_SelectView.tool_2_2 == 56) {
                    addCharacterToAllRateArrayList(_eggID, (short) 71, allRateArrayList);
                    addCharacterToAllRateArrayList(_eggID, (short) 72, allRateArrayList);
                }
                if (tool_1_level7 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 56 || this.tool_2_SelectView.tool_2_1 == 56 || this.tool_2_SelectView.tool_2_2 == 56) && (this.tool_2_SelectView.tool_2_0 == 57 || this.tool_2_SelectView.tool_2_1 == 57 || this.tool_2_SelectView.tool_2_2 == 57)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 73, allRateArrayList);
                        addCharacterToAllRateArrayList(_eggID, (short) 74, allRateArrayList);
                    }
                    if (tool_1_level7 >= 2) {
                        if ((this.tool_2_SelectView.tool_2_0 == 56 || this.tool_2_SelectView.tool_2_1 == 56 || this.tool_2_SelectView.tool_2_2 == 56) && ((this.tool_2_SelectView.tool_2_0 == 57 || this.tool_2_SelectView.tool_2_1 == 57 || this.tool_2_SelectView.tool_2_2 == 57) && (this.tool_2_SelectView.tool_2_0 == 58 || this.tool_2_SelectView.tool_2_1 == 58 || this.tool_2_SelectView.tool_2_2 == 58))) {
                            addCharacterToAllRateArrayList(_eggID, (short) 75, allRateArrayList);
                        }
                        if ((this.tool_2_SelectView.tool_2_0 == 56 || this.tool_2_SelectView.tool_2_1 == 56 || this.tool_2_SelectView.tool_2_2 == 56) && ((this.tool_2_SelectView.tool_2_0 == 59 || this.tool_2_SelectView.tool_2_1 == 59 || this.tool_2_SelectView.tool_2_2 == 59) && (this.tool_2_SelectView.tool_2_0 == 60 || this.tool_2_SelectView.tool_2_1 == 60 || this.tool_2_SelectView.tool_2_2 == 60))) {
                            addCharacterToAllRateArrayList(_eggID, (short) 76, allRateArrayList);
                        }
                        if ((this.tool_2_SelectView.tool_2_0 == 56 || this.tool_2_SelectView.tool_2_1 == 56 || this.tool_2_SelectView.tool_2_2 == 56) && ((this.tool_2_SelectView.tool_2_0 == 61 || this.tool_2_SelectView.tool_2_1 == 61 || this.tool_2_SelectView.tool_2_2 == 61) && (this.tool_2_SelectView.tool_2_0 == 62 || this.tool_2_SelectView.tool_2_1 == 62 || this.tool_2_SelectView.tool_2_2 == 62))) {
                            addCharacterToAllRateArrayList(_eggID, (short) 77, allRateArrayList);
                        }
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 7) {
                short tool_1_level8 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 109, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 37 || this.tool_2_SelectView.tool_2_1 == 37 || this.tool_2_SelectView.tool_2_2 == 37) {
                    addCharacterToAllRateArrayList(_eggID, (short) 111, allRateArrayList);
                }
                if (tool_1_level8 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 71 || this.tool_2_SelectView.tool_2_1 == 71 || this.tool_2_SelectView.tool_2_2 == 71) && (this.tool_2_SelectView.tool_2_0 == 72 || this.tool_2_SelectView.tool_2_1 == 72 || this.tool_2_SelectView.tool_2_2 == 72)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 112, allRateArrayList);
                    }
                    if (tool_1_level8 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 4 || this.tool_2_SelectView.tool_2_1 == 4 || this.tool_2_SelectView.tool_2_2 == 4) && ((this.tool_2_SelectView.tool_2_0 == 22 || this.tool_2_SelectView.tool_2_1 == 22 || this.tool_2_SelectView.tool_2_2 == 22) && (this.tool_2_SelectView.tool_2_0 == 45 || this.tool_2_SelectView.tool_2_1 == 45 || this.tool_2_SelectView.tool_2_2 == 45)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 113, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            }
        } else if (_eggID == 1) {
            if (_tool1_ID == 0) {
                short tool_1_level9 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 0, allRateArrayList);
                if (totalChars1Cnt >= 1000.0d) {
                    addCharacterToAllRateArrayList(_eggID, (short) 16, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_35", false)) {
                    Log.d("MainGameLayout", "campaign_char_1_35: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 35, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_47", false)) {
                    Log.d("MainGameLayout", "campaign_char_1_47: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 47, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_48", false)) {
                    Log.d("MainGameLayout", "campaign_char_1_48: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 48, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_49", false)) {
                    Log.d("MainGameLayout", "campaign_char_1_49: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 49, allRateArrayList);
                }
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_51", false)) {
                    Log.d("MainGameLayout", "campaign_char_1_51: YES");
                    addCharacterToAllRateArrayList(_eggID, (short) 51, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27) {
                    addCharacterToAllRateArrayList(_eggID, (short) 4, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 11 || this.tool_2_SelectView.tool_2_1 == 11 || this.tool_2_SelectView.tool_2_2 == 11) {
                    addCharacterToAllRateArrayList(_eggID, (short) 14, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 18 || this.tool_2_SelectView.tool_2_1 == 18 || this.tool_2_SelectView.tool_2_2 == 18) {
                    addCharacterToAllRateArrayList(_eggID, (short) 15, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 36 || this.tool_2_SelectView.tool_2_1 == 36 || this.tool_2_SelectView.tool_2_2 == 36) {
                    addCharacterToAllRateArrayList(_eggID, (short) 26, allRateArrayList);
                }
                if (tool_1_level9 >= 1) {
                    addCharacterToAllRateArrayList(_eggID, (short) 17, allRateArrayList);
                    addCharacterToAllRateArrayList(_eggID, (short) 18, allRateArrayList);
                }
                if (tool_1_level9 >= 2) {
                    short randIndex2 = (short) (Math.random() * 8.0d);
                    if (randIndex2 == 0) {
                        addCharacterToAllRateArrayList(_eggID, (short) 27, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 1) {
                short tool_1_level10 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 3, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 44 || this.tool_2_SelectView.tool_2_1 == 44 || this.tool_2_SelectView.tool_2_2 == 44) {
                    addCharacterToAllRateArrayList(_eggID, (short) 5, allRateArrayList);
                }
                if (tool_1_level10 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 23 || this.tool_2_SelectView.tool_2_1 == 23 || this.tool_2_SelectView.tool_2_2 == 23) && (this.tool_2_SelectView.tool_2_0 == 46 || this.tool_2_SelectView.tool_2_1 == 46 || this.tool_2_SelectView.tool_2_2 == 46)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 21, allRateArrayList);
                    }
                    if (tool_1_level10 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 34 || this.tool_2_SelectView.tool_2_1 == 34 || this.tool_2_SelectView.tool_2_2 == 34) && ((this.tool_2_SelectView.tool_2_0 == 41 || this.tool_2_SelectView.tool_2_1 == 41 || this.tool_2_SelectView.tool_2_2 == 41) && (this.tool_2_SelectView.tool_2_0 == 50 || this.tool_2_SelectView.tool_2_1 == 50 || this.tool_2_SelectView.tool_2_2 == 50)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 30, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 2) {
                short tool_1_level11 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 6, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27) {
                    addCharacterToAllRateArrayList(_eggID, (short) 7, allRateArrayList);
                }
                if (tool_1_level11 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 47 || this.tool_2_SelectView.tool_2_1 == 47 || this.tool_2_SelectView.tool_2_2 == 47) && (this.tool_2_SelectView.tool_2_0 == 48 || this.tool_2_SelectView.tool_2_1 == 48 || this.tool_2_SelectView.tool_2_2 == 48)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 22, allRateArrayList);
                    }
                    if (tool_1_level11 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 46 || this.tool_2_SelectView.tool_2_1 == 46 || this.tool_2_SelectView.tool_2_2 == 46) && ((this.tool_2_SelectView.tool_2_0 == 51 || this.tool_2_SelectView.tool_2_1 == 51 || this.tool_2_SelectView.tool_2_2 == 51) && (this.tool_2_SelectView.tool_2_0 == 52 || this.tool_2_SelectView.tool_2_1 == 52 || this.tool_2_SelectView.tool_2_2 == 52)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 31, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 3) {
                short tool_1_level12 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 8, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 45 || this.tool_2_SelectView.tool_2_1 == 45 || this.tool_2_SelectView.tool_2_2 == 45) {
                    addCharacterToAllRateArrayList(_eggID, (short) 9, allRateArrayList);
                }
                if (tool_1_level12 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 25 || this.tool_2_SelectView.tool_2_1 == 25 || this.tool_2_SelectView.tool_2_2 == 25) && (this.tool_2_SelectView.tool_2_0 == 32 || this.tool_2_SelectView.tool_2_1 == 32 || this.tool_2_SelectView.tool_2_2 == 32)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 23, allRateArrayList);
                    }
                    if (tool_1_level12 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 29 || this.tool_2_SelectView.tool_2_1 == 29 || this.tool_2_SelectView.tool_2_2 == 29) && ((this.tool_2_SelectView.tool_2_0 == 46 || this.tool_2_SelectView.tool_2_1 == 46 || this.tool_2_SelectView.tool_2_2 == 46) && (this.tool_2_SelectView.tool_2_0 == 53 || this.tool_2_SelectView.tool_2_1 == 53 || this.tool_2_SelectView.tool_2_2 == 53)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 32, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 4) {
                short tool_1_level13 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 10, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 33 || this.tool_2_SelectView.tool_2_1 == 33 || this.tool_2_SelectView.tool_2_2 == 33) {
                    addCharacterToAllRateArrayList(_eggID, (short) 11, allRateArrayList);
                }
                if (tool_1_level13 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 26 || this.tool_2_SelectView.tool_2_1 == 26 || this.tool_2_SelectView.tool_2_2 == 26) && (this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 24, allRateArrayList);
                    }
                    if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_50", false) && ((this.tool_2_SelectView.tool_2_0 == 9 || this.tool_2_SelectView.tool_2_1 == 9 || this.tool_2_SelectView.tool_2_2 == 9) && (this.tool_2_SelectView.tool_2_0 == 29 || this.tool_2_SelectView.tool_2_1 == 29 || this.tool_2_SelectView.tool_2_2 == 29))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 50, allRateArrayList);
                    }
                    if (tool_1_level13 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 46 || this.tool_2_SelectView.tool_2_1 == 46 || this.tool_2_SelectView.tool_2_2 == 46) && ((this.tool_2_SelectView.tool_2_0 == 54 || this.tool_2_SelectView.tool_2_1 == 54 || this.tool_2_SelectView.tool_2_2 == 54) && (this.tool_2_SelectView.tool_2_0 == 55 || this.tool_2_SelectView.tool_2_1 == 55 || this.tool_2_SelectView.tool_2_2 == 55)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 33, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 5) {
                short tool_1_level14 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 12, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 27 || this.tool_2_SelectView.tool_2_1 == 27 || this.tool_2_SelectView.tool_2_2 == 27) {
                    addCharacterToAllRateArrayList(_eggID, (short) 13, allRateArrayList);
                }
                if (tool_1_level14 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 45 || this.tool_2_SelectView.tool_2_1 == 45 || this.tool_2_SelectView.tool_2_2 == 45) && (this.tool_2_SelectView.tool_2_0 == 49 || this.tool_2_SelectView.tool_2_1 == 49 || this.tool_2_SelectView.tool_2_2 == 49)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 25, allRateArrayList);
                    }
                    if (tool_1_level14 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 12 || this.tool_2_SelectView.tool_2_1 == 12 || this.tool_2_SelectView.tool_2_2 == 12) && ((this.tool_2_SelectView.tool_2_0 == 28 || this.tool_2_SelectView.tool_2_1 == 28 || this.tool_2_SelectView.tool_2_2 == 28) && (this.tool_2_SelectView.tool_2_0 == 29 || this.tool_2_SelectView.tool_2_1 == 29 || this.tool_2_SelectView.tool_2_2 == 29)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 34, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 6) {
                short tool_1_level15 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 37, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 63 || this.tool_2_SelectView.tool_2_1 == 63 || this.tool_2_SelectView.tool_2_2 == 63) {
                    addCharacterToAllRateArrayList(_eggID, (short) 39, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 64 || this.tool_2_SelectView.tool_2_1 == 64 || this.tool_2_SelectView.tool_2_2 == 64) {
                    addCharacterToAllRateArrayList(_eggID, (short) 40, allRateArrayList);
                }
                if (this.tool_2_SelectView.tool_2_0 == 3 || this.tool_2_SelectView.tool_2_1 == 3 || this.tool_2_SelectView.tool_2_2 == 3) {
                    addCharacterToAllRateArrayList(_eggID, (short) 41, allRateArrayList);
                }
                if (tool_1_level15 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 1 || this.tool_2_SelectView.tool_2_1 == 1 || this.tool_2_SelectView.tool_2_2 == 1) && (this.tool_2_SelectView.tool_2_0 == 63 || this.tool_2_SelectView.tool_2_1 == 63 || this.tool_2_SelectView.tool_2_2 == 63)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 42, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 57 || this.tool_2_SelectView.tool_2_1 == 57 || this.tool_2_SelectView.tool_2_2 == 57) && (this.tool_2_SelectView.tool_2_0 == 63 || this.tool_2_SelectView.tool_2_1 == 63 || this.tool_2_SelectView.tool_2_2 == 63)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 43, allRateArrayList);
                    }
                    if ((this.tool_2_SelectView.tool_2_0 == 65 || this.tool_2_SelectView.tool_2_1 == 65 || this.tool_2_SelectView.tool_2_2 == 65) && (this.tool_2_SelectView.tool_2_0 == 66 || this.tool_2_SelectView.tool_2_1 == 66 || this.tool_2_SelectView.tool_2_2 == 66)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 44, allRateArrayList);
                    }
                    if (tool_1_level15 >= 2) {
                        if ((this.tool_2_SelectView.tool_2_0 == 59 || this.tool_2_SelectView.tool_2_1 == 59 || this.tool_2_SelectView.tool_2_2 == 59) && ((this.tool_2_SelectView.tool_2_0 == 60 || this.tool_2_SelectView.tool_2_1 == 60 || this.tool_2_SelectView.tool_2_2 == 60) && (this.tool_2_SelectView.tool_2_0 == 67 || this.tool_2_SelectView.tool_2_1 == 67 || this.tool_2_SelectView.tool_2_2 == 67))) {
                            addCharacterToAllRateArrayList(_eggID, (short) 45, allRateArrayList);
                        }
                        if ((this.tool_2_SelectView.tool_2_0 == 29 || this.tool_2_SelectView.tool_2_1 == 29 || this.tool_2_SelectView.tool_2_2 == 29) && ((this.tool_2_SelectView.tool_2_0 == 57 || this.tool_2_SelectView.tool_2_1 == 57 || this.tool_2_SelectView.tool_2_2 == 57) && (this.tool_2_SelectView.tool_2_0 == 63 || this.tool_2_SelectView.tool_2_1 == 63 || this.tool_2_SelectView.tool_2_2 == 63))) {
                            addCharacterToAllRateArrayList(_eggID, (short) 46, allRateArrayList);
                        }
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            } else if (_tool1_ID == 7) {
                short tool_1_level16 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
                addCharacterToAllRateArrayList(_eggID, (short) 52, allRateArrayList);
                if (this.tool_2_SelectView.tool_2_0 == 57 || this.tool_2_SelectView.tool_2_1 == 57 || this.tool_2_SelectView.tool_2_2 == 57) {
                    addCharacterToAllRateArrayList(_eggID, (short) 54, allRateArrayList);
                }
                if (tool_1_level16 >= 1) {
                    if ((this.tool_2_SelectView.tool_2_0 == 73 || this.tool_2_SelectView.tool_2_1 == 73 || this.tool_2_SelectView.tool_2_2 == 73) && (this.tool_2_SelectView.tool_2_0 == 74 || this.tool_2_SelectView.tool_2_1 == 74 || this.tool_2_SelectView.tool_2_2 == 74)) {
                        addCharacterToAllRateArrayList(_eggID, (short) 55, allRateArrayList);
                    }
                    if (tool_1_level16 >= 2 && ((this.tool_2_SelectView.tool_2_0 == 23 || this.tool_2_SelectView.tool_2_1 == 23 || this.tool_2_SelectView.tool_2_2 == 23) && ((this.tool_2_SelectView.tool_2_0 == 24 || this.tool_2_SelectView.tool_2_1 == 24 || this.tool_2_SelectView.tool_2_2 == 24) && (this.tool_2_SelectView.tool_2_0 == 47 || this.tool_2_SelectView.tool_2_1 == 47 || this.tool_2_SelectView.tool_2_2 == 47)))) {
                        addCharacterToAllRateArrayList(_eggID, (short) 56, allRateArrayList);
                    }
                }
                returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
            }
        }
        return returnRateArrayList;
    }

    public void addCharacterToAllRateArrayList(short _eggID, short _characterID, ArrayList<Short> _addArrayList) {
        if (_addArrayList != null) {
            CharacterDataDictionary characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(_eggID, _characterID);
            if (characterDataDictionary != null) {
                short characterID = characterDataDictionary.getId();
                Log.d("MainGameLayout", "============characterID:" + ((int) characterID) + "===========");
                short rate = characterDataDictionary.getRate();
                Log.d("MainGameLayout", "============rate:" + ((int) rate) + "===========");
                addIDtoArray(characterID, rate, _addArrayList);
            }
        }
    }

    public void addIDtoArray(short _id, short _totalCnt, ArrayList<Short> _addArrayList) {
        if (_totalCnt > 0 && _id >= 0) {
            for (int i = 0; i < _totalCnt; i++) {
                _addArrayList.add(new Short(_id));
                Log.d("MainGameLayout", "============(" + _addArrayList.size() + ")characterID:" + ((int) _id) + "===========");
            }
        }
    }

    public ArrayList<Short> getFinalRateArrayWithAllRateArray(ArrayList<Short> _allRateArrayList) {
        int j;
        ArrayList<Short> returnRateArray = new ArrayList<>();
        if (_allRateArrayList.size() >= this.appDelegate.CHARACTERUNITVIEW_TOTAL) {
            short subTotalCnt = (short) (_allRateArrayList.size() / this.appDelegate.CHARACTERUNITVIEW_TOTAL);
            for (int i = 0; i < this.appDelegate.CHARACTERUNITVIEW_TOTAL; i++) {
                short randIndex = (short) (Math.random() * _allRateArrayList.size());
                Short idNum = _allRateArrayList.get(randIndex);
                short idShort = idNum.shortValue();
                Log.d("MainGameLayout", "============" + i + ")characterID:" + ((int) idShort) + "===========");
                returnRateArray.add(new Short(idShort));
                _allRateArrayList.remove(randIndex);
                short subCnt = (short) (subTotalCnt - 1);
                while (j < _allRateArrayList.size()) {
                    Short checkIdNum = _allRateArrayList.get(j);
                    short checkIdShort = checkIdNum.shortValue();
                    if (checkIdShort == idShort) {
                        subCnt = (short) (subCnt - 1);
                        j--;
                    }
                    j = subCnt > 0 ? j + 1 : 0;
                }
            }
        }
        return returnRateArray;
    }

    public void putEggsWithTool1(short _tool1_ID) throws NumberFormatException {
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList;
        short levelShort;
        CharacterUnitDictionary nowCharacterUnitDictionary;
        ToolDataDictionary toolDataDictionary;
        ToolLevelDictionary newToolLevelDictionary;
        Log.d("MainGameLayout", "tool1_ID:" + ((int) _tool1_ID));
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
        if (_tool1_ID >= 0 && _tool1_ID < this.appDelegate.TOOL_1_ALL_CNT && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null && this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() >= 2 && (toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1)) != null && _tool1_ID < toolUnitDictionarysArrayList.size()) {
            toolUnitDictionarysArrayList.get(_tool1_ID);
            if (toolUnitDictionarysArrayList != null && (levelShort = this.appDelegate.getTool1LevelWithIndex(_tool1_ID)) >= 0) {
                int cookCP = -1;
                if (levelShort >= 0 && (toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, _tool1_ID)) != null && toolDataDictionary.toolLevelsArrayList != null && levelShort < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(levelShort)) != null) {
                    cookCP = newToolLevelDictionary.getLvCookCp();
                }
                if (cookCP >= 0) {
                    float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                    if (nowPoint < 0.0d) {
                        nowPoint = BitmapDescriptorFactory.HUE_RED;
                        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
                        this.mainGameViewController.refreshAndSave();
                    }
                    if (nowPoint < cookCP) {
                        noCPAlert();
                        return;
                    }
                    this.tool_2_SelectView.refresh();
                    short eggIDSelectIndex = this.appDelegate.timeSaveDictionary.getEggIDSelectIndex();
                    if (eggIDSelectIndex < 0) {
                        eggIDSelectIndex = 0;
                    }
                    ArrayList<Short> rateArrayList = getRateArrayWithID(eggIDSelectIndex, _tool1_ID, (short) -1, (short) -1);
                    Log.i("MainGameLayout", "rateArrayList.size():" + rateArrayList.size());
                    short cookMin = -1;
                    ToolDataDictionary toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId((short) 1, _tool1_ID);
                    if (toolDataDictionary2 != null && toolDataDictionary2.toolLevelsArrayList != null) {
                        cookMin = toolDataDictionary2.toolLevelsArrayList.get(levelShort).getLvMin();
                    }
                    if (rateArrayList.size() != this.appDelegate.CHARACTERUNITVIEW_TOTAL || cookMin <= 0) {
                        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
                    } else {
                        boolean checkCookAlarmF = false;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date nowDate = new Date();
                        Date startDate = new Date(nowDate.getTime());
                        if (this.appDelegate.timeSaveDictionary != null) {
                            this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate(sdf.format(startDate));
                        }
                        ArrayList<Point> randPositionsArrayList = new ArrayList<>();
                        ArrayList<Float> randOpenTimeArrayList = new ArrayList<>();
                        for (int i = 0; i < this.appDelegate.CHARACTERUNITVIEW_TOTAL; i++) {
                            short offsetX = (short) (i % 6);
                            short offsetY = (short) (i / 6);
                            short randX = (short) ((this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE / 2.0f) - (Math.random() * ((short) this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE)));
                            short randY = (short) ((this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE / 2.0f) - (Math.random() * ((short) this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE)));
                            short centerX = (short) (this.CHARACTERUNITVIEW_CENTER_X + (this.CHARACTERUNITVIEW_SPACE_X * offsetX) + randX);
                            short centerY = (short) (this.CHARACTERUNITVIEW_CENTER_Y + (this.CHARACTERUNITVIEW_SPACE_Y * offsetY) + randY);
                            Point randPosition = new Point(centerX, centerY);
                            short insertIndex = 0;
                            for (int j = 0; j < randPositionsArrayList.size(); j++) {
                                Point checkPosition = randPositionsArrayList.get(j);
                                if (centerY < checkPosition.y) {
                                    insertIndex = (short) (j + 1);
                                }
                            }
                            Log.d("MainGameLayout", "insertIndex:" + ((int) insertIndex));
                            randPositionsArrayList.add(insertIndex, randPosition);
                            randOpenTimeArrayList.add(new Float(((cookMin * 55.0d) / 23.0d) * i));
                        }
                        Log.d("MainGameLayout", "randPositionsArray.count:" + randOpenTimeArrayList.size());
                        float endSecondsFloat = cookMin * 60.0f;
                        if (this.appDelegate.timeSaveDictionary != null) {
                            this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(endSecondsFloat);
                            checkCookAlarmF = true;
                        }
                        int positionIndex = 0;
                        for (int i2 = this.nowCharacterUnitViewsArrayList.size() - 1; i2 >= 0; i2--) {
                            CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i2);
                            if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null) {
                                if (positionIndex >= randPositionsArrayList.size()) {
                                    break;
                                }
                                characterUnit.setNewStatus((short) -1);
                                Point position = randPositionsArrayList.get(positionIndex);
                                int characterUnitViewCenterX = position.x;
                                int characterUnitViewCenterY = position.y;
                                nowCharacterUnitDictionary.setOffsetX(characterUnitViewCenterX);
                                nowCharacterUnitDictionary.setOffsetY(characterUnitViewCenterY);
                                characterUnit.setCenter(characterUnitViewCenterX, characterUnitViewCenterY);
                                nowCharacterUnitDictionary.setEggId(eggIDSelectIndex);
                                short idShort = -1;
                                if (rateArrayList.size() > 0) {
                                    Short idNum = rateArrayList.get(0);
                                    if (idNum != null) {
                                        idShort = idNum.shortValue();
                                    }
                                    rateArrayList.remove(0);
                                }
                                nowCharacterUnitDictionary.setCharacterId(idShort);
                                nowCharacterUnitDictionary.setPutDate(sdf.format(startDate));
                                float openTimeFloat = BitmapDescriptorFactory.HUE_RED;
                                if (randOpenTimeArrayList.size() > 0) {
                                    short randOpenTimeIndex = (short) (Math.random() * randOpenTimeArrayList.size());
                                    Float openTimeNum = randOpenTimeArrayList.get(randOpenTimeIndex);
                                    if (openTimeNum != null) {
                                        openTimeFloat = openTimeNum.floatValue();
                                    }
                                }
                                if (openTimeFloat < BitmapDescriptorFactory.HUE_RED) {
                                    openTimeFloat = BitmapDescriptorFactory.HUE_RED;
                                }
                                nowCharacterUnitDictionary.setOpenSeconds(endSecondsFloat - openTimeFloat);
                                nowCharacterUnitDictionary.setEndSeconds(endSecondsFloat);
                                nowCharacterUnitDictionary.setBlackSeconds((3.0f * endSecondsFloat) - openTimeFloat);
                                if (this.tool_2_SelectView.tool_2_0 == 36 || this.tool_2_SelectView.tool_2_1 == 36 || this.tool_2_SelectView.tool_2_2 == 36) {
                                    nowCharacterUnitDictionary.setBlackSeconds(-1.0f);
                                }
                                if (this.tool_2_SelectView.tool_2_0 == 18 || this.tool_2_SelectView.tool_2_1 == 18 || this.tool_2_SelectView.tool_2_2 == 18) {
                                    nowCharacterUnitDictionary.setSicknessPrevention((short) 1);
                                } else {
                                    nowCharacterUnitDictionary.setSicknessPrevention((short) 0);
                                }
                                characterUnit.setNewStatus((short) 0);
                                positionIndex++;
                            }
                        }
                        this.tool_1_SelectScrollUnit.changeButtonWithIndex(_tool1_ID, this.tool_2_SelectView.tool_2_0, this.tool_2_SelectView.tool_2_1, this.tool_2_SelectView.tool_2_2);
                        this.tool_2_SelectView.useSelectedTool2();
                        float nowPoint2 = this.appDelegate.timeSaveDictionary.getPoint();
                        if (nowPoint2 < BitmapDescriptorFactory.HUE_RED) {
                            nowPoint2 = BitmapDescriptorFactory.HUE_RED;
                            this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
                            this.mainGameViewController.refreshAndSave();
                        }
                        if (nowPoint2 >= cookCP) {
                            nowPoint2 -= cookCP;
                        }
                        this.appDelegate.timeSaveDictionary.setPoint(nowPoint2);
                        refreshPoint();
                        if (!this.hidden) {
                            this.appDelegate.doSoundPoolPlay(1);
                        }
                        if (checkCookAlarmF) {
                            checkCookAlarm();
                        }
                    }
                    this.mainGameViewController.refreshAndSave();
                }
            }
        }
    }

    public void resetCharacterUnitViewsArray() {
        for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
            CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
            if (characterUnit != null) {
                characterUnit.setNewStatus((short) -1);
            }
        }
    }

    public short checkCharacterUnitViewsArrayActiveCnt() {
        CharacterUnitDictionary nowCharacterUnitDictionary;
        for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
            CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
            if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null && nowCharacterUnitDictionary.getNowStatus() >= 0) {
                short activeCnt = (short) 1;
                return activeCnt;
            }
        }
        return (short) 0;
    }

    public void noCPAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "cpが足りないようです。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你好像沒有足夠的cp。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你好像没有足够的cp。";
            contentLabelString3 = "";
            contentLabelString4 = "";
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "You don't have enough cp.";
            contentLabelString3 = "";
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

    public void noCPAlertWithNeedFixKitchenCP(int _needcp) {
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
            contentLabelString1 = "台所の掃除は" + _needcp + "cpがかかります。";
            contentLabelString2 = "";
            contentLabelString3 = "cpが足りないようです。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "打掃廚房需要花費" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像沒有足夠的cp。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "打扫厨房需要花费" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像没有足够的cp。";
            contentLabelString4 = "";
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "You need " + _needcp + "cp to clean the kitchen.";
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

    public void noCPAlertWithNeedLevelUpKitchenCP(int _needcp) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        hiddenAlert();
        short nowLevel = this.appDelegate.getTool0LevelWithIndex((short) 0);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "台所Lv." + (nowLevel + 2) + "にレベルアップは";
            contentLabelString1 = String.valueOf(_needcp) + "cpがかかります。";
            contentLabelString2 = "";
            contentLabelString3 = "cpが足りないようです。";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "升級至廚房Lv." + (nowLevel + 2) + "需要花費" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像沒有足夠的cp。";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "升级至厨房Lv." + (nowLevel + 2) + "需要花费" + _needcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你好像没有足够的cp。";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "You need " + _needcp + "cp to level up";
            contentLabelString1 = "the kitchen(Lv." + (nowLevel + 2) + ").";
            contentLabelString2 = "";
            contentLabelString3 = "You don't have enough cp.";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = (short) 99;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void fixKitchenWithCP(int _fixcp) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint < BitmapDescriptorFactory.HUE_RED) {
            nowPoint = BitmapDescriptorFactory.HUE_RED;
            this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
            this.mainGameViewController.refreshAndSave();
        }
        if (nowPoint < _fixcp) {
            noCPAlertWithNeedFixKitchenCP(_fixcp);
            return;
        }
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "台所掃除";
            contentLabelString0 = "";
            contentLabelString1 = "台所の掃除は" + _fixcp + "cpがかかります。";
            contentLabelString2 = "";
            contentLabelString3 = "よろしいですか？";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "打掃廚房";
            contentLabelString0 = "";
            contentLabelString1 = "打掃廚房需要花費" + _fixcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你確定要打掃嗎？";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "打掃廚房";
            contentLabelString0 = "";
            contentLabelString1 = "打扫厨房需要花费" + _fixcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你确定要打扫吗？";
            contentLabelString4 = "";
        } else {
            titleLabelString = "Kitchen Cleaning";
            contentLabelString0 = "";
            contentLabelString1 = "You need " + _fixcp + "cp to clean the kitchen.";
            contentLabelString2 = "";
            contentLabelString3 = "Are you sure you want to clear?";
            contentLabelString4 = "";
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = (short) 1;
        this.alertUnitType0.subTag = (short) _fixcp;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void levelUpKitchenWithCP(int _levelupcp) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint < 0.0d) {
            nowPoint = BitmapDescriptorFactory.HUE_RED;
            this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
            this.mainGameViewController.refreshAndSave();
        }
        if (nowPoint < _levelupcp) {
            noCPAlertWithNeedLevelUpKitchenCP(_levelupcp);
            return;
        }
        hiddenAlert();
        short nowLevel = this.appDelegate.getTool0LevelWithIndex((short) 0);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "台所レベルアップ";
            contentLabelString0 = "台所Lv." + (nowLevel + 2) + "にレベルアップは";
            contentLabelString1 = String.valueOf(_levelupcp) + "cpがかかります。";
            contentLabelString2 = "";
            contentLabelString3 = "よろしいですか？";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "升級廚房";
            contentLabelString0 = "";
            contentLabelString1 = "升級至廚房Lv." + (nowLevel + 2) + "需要花費" + _levelupcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你確定要升級嗎？";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "升級廚房";
            contentLabelString0 = "";
            contentLabelString1 = "升级至厨房Lv." + (nowLevel + 2) + "需要花费" + _levelupcp + "cp。";
            contentLabelString2 = "";
            contentLabelString3 = "你确定要升级吗？";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "Kitchen Cleaning";
            contentLabelString0 = "You need " + _levelupcp + "cp to level up";
            contentLabelString1 = "the kitchen(Lv." + (nowLevel + 2) + ").";
            contentLabelString2 = "";
            contentLabelString3 = "Are you sure you want to clear?";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = (short) 2;
        this.alertUnitType0.subTag = (short) _levelupcp;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public boolean tool_1_ButtonChangeWithIndex(short _buttonIndex) {
        short levelShort;
        String lvMinString;
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        ToolDataDictionary toolDataDictionary;
        ToolLevelDictionary newToolLevelDictionary;
        ToolDataDictionary toolDataDictionary2;
        ToolLevelDictionary newToolLevelDictionary2;
        if (_buttonIndex < 0 || _buttonIndex >= this.appDelegate.TOOL_1_ALL_CNT) {
            return false;
        }
        hiddenAlert();
        ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId((short) 1, _buttonIndex);
        if (toolUnitDictionary == null || (levelShort = this.appDelegate.getTool1LevelWithIndex(_buttonIndex)) < 0) {
            return false;
        }
        String languageString = this.appDelegate.getLocaleLanguage();
        short cookCP = -1;
        if (levelShort >= 0 && (toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId((short) 1, _buttonIndex)) != null && toolDataDictionary2.toolLevelsArrayList != null && levelShort < toolDataDictionary2.toolLevelsArrayList.size() && (newToolLevelDictionary2 = toolDataDictionary2.toolLevelsArrayList.get(levelShort)) != null) {
            cookCP = (short) newToolLevelDictionary2.getLvCookCp();
        }
        if (cookCP >= 0) {
            float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
            if (nowPoint < BitmapDescriptorFactory.HUE_RED) {
                nowPoint = BitmapDescriptorFactory.HUE_RED;
                this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
                this.mainGameViewController.refreshAndSave();
            }
            if (nowPoint < cookCP) {
                noCPAlert();
                return false;
            }
            String lvCookCPString = String.valueOf((int) cookCP) + "cp";
            short cookMin = -1;
            if (levelShort >= 0 && (toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId((short) 1, _buttonIndex)) != null && toolDataDictionary.toolLevelsArrayList != null && levelShort < toolDataDictionary.toolLevelsArrayList.size() && (newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(levelShort)) != null) {
                cookMin = newToolLevelDictionary.getLvMin();
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
                    lvMinString = String.valueOf(cookMin / 60) + "小時" + (cookMin % 60) + "分钟";
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
            } else {
                return false;
            }
            String nameString = "-";
            ToolDataDictionary toolDataDictionary3 = this.appDelegate.getToolDataDictionaryWithId((short) 1, _buttonIndex);
            if (toolDataDictionary3 != null) {
                if (languageString.equals("ja-JP")) {
                    nameString = toolDataDictionary3.getTitleJa();
                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                    nameString = toolDataDictionary3.getTitleZhTW();
                } else if (languageString.equals("zh-CN")) {
                    nameString = toolDataDictionary3.getTitleZhCN();
                } else {
                    nameString = toolDataDictionary3.getTitleEn();
                }
            }
            if (languageString.equals("ja-JP")) {
                titleLabelString = "調理開始";
                contentLabelString0 = String.valueOf(nameString) + " Lv." + (levelShort + 1) + "（" + lvMinString + "）で";
                contentLabelString1 = "調理は" + lvCookCPString + "がかかります。";
                contentLabelString2 = "";
                contentLabelString3 = "よろしいですか？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                titleLabelString = "調理開始";
                contentLabelString0 = "使用" + nameString + " Lv." + (levelShort + 1) + "（" + lvMinString + "）";
                contentLabelString1 = "調理,需要花費" + lvCookCPString + "。";
                contentLabelString2 = "";
                contentLabelString3 = "你確定要使用嗎？";
                contentLabelString4 = "";
            } else if (languageString.equals("zh-CN")) {
                titleLabelString = "調理開始";
                contentLabelString0 = "使用" + nameString + " Lv." + (levelShort + 1) + "（" + lvMinString + "）";
                contentLabelString1 = "调理,需要花费" + lvCookCPString + "。";
                contentLabelString2 = "";
                contentLabelString3 = "你确定要使用吗？";
                contentLabelString4 = "";
            } else {
                titleLabelString = "Start Hatching";
                contentLabelString0 = "You need to pay " + lvCookCPString + " to use";
                contentLabelString1 = String.valueOf(nameString) + " Lv." + (levelShort + 1) + " (" + lvMinString + ").";
                contentLabelString2 = "";
                contentLabelString3 = "Are you sure you want to use this?";
                contentLabelString4 = "";
            }
            Log.i("_buttonIndex", "_buttonIndex=" + ((int) _buttonIndex));
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            Log.i("_buttonIndex", "_buttonIndex=" + ((int) _buttonIndex));
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 10.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
            Log.i("_buttonIndex", "_buttonIndex=" + ((int) _buttonIndex));
            this.alertUnitType0.tag = (short) 0;
            this.alertUnitType0.subTag = _buttonIndex;
            popAlert();
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(4);
            }
            return true;
        }
        return false;
    }

    public void clearEggsAlert(short _subtag) {
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
            titleLabelString = "調理中止";
            contentLabelString0 = "";
            contentLabelString1 = "調理を中止し、次の調理をはじめます。";
            contentLabelString2 = "よろしいですか？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "調理中斷";
            contentLabelString0 = "";
            contentLabelString1 = "你確定要中斷目前的調理,";
            contentLabelString2 = "然後開始新的調理嗎？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "調理中斷";
            contentLabelString0 = "";
            contentLabelString1 = "你确定要中断目前的调理,";
            contentLabelString2 = "然後开始新的调理吗？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else {
            titleLabelString = "Interrupt Hatch";
            contentLabelString0 = "";
            contentLabelString1 = "Are you sure you want to interrupt";
            contentLabelString2 = "current hatch,and then start a new";
            contentLabelString3 = "one?";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
        this.alertUnitType0.tag = (short) -1;
        this.alertUnitType0.subTag = _subtag;
        popAlert();
        if (!this.hidden) {
            this.appDelegate.doSoundPoolPlay(4);
        }
    }

    public void tool1SelectViewCheckCookAlarm() {
        checkCookAlarm();
    }

    public void getPointWithCharacter(CharacterUnit _characterUnit, boolean _directToCPF) {
        short eggId;
        ArrayList<FarmUnitDictionary> farmUnitDictionarysArrayList;
        short characterId;
        FarmUnitDictionary farmUnitDictionary;
        if (this.appDelegate.charactersDataFilesArrayList == null) {
            this.appDelegate.initCharactersDataFilesArray();
        }
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(_characterUnit.tag);
        if (nowCharacterUnitDictionary != null) {
            short addPoint = 0;
            if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList != null && (eggId = nowCharacterUnitDictionary.getEggId()) >= 0 && eggId < this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size() && (farmUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(eggId)) != null && (characterId = nowCharacterUnitDictionary.getCharacterId()) >= 0 && characterId < farmUnitDictionarysArrayList.size() && (farmUnitDictionary = farmUnitDictionarysArrayList.get(characterId)) != null) {
                addPoint = 1;
                float countFloat = farmUnitDictionary.getCount();
                if (countFloat < BitmapDescriptorFactory.HUE_RED) {
                    countFloat = BitmapDescriptorFactory.HUE_RED;
                }
                float countFloat2 = countFloat + 1.0f;
                if (countFloat2 > 99999.0f) {
                    countFloat2 = 99999.0f;
                }
                farmUnitDictionary.setCount(countFloat2);
                float totalCountFloat = farmUnitDictionary.getTotalCount();
                if (totalCountFloat < BitmapDescriptorFactory.HUE_RED) {
                    totalCountFloat = BitmapDescriptorFactory.HUE_RED;
                }
                float totalCountFloat2 = totalCountFloat + 1.0f;
                if (totalCountFloat2 > 99999.0f) {
                    totalCountFloat2 = 99999.0f;
                }
                farmUnitDictionary.setTotalCount(totalCountFloat2);
            }
            float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
            if (nowPoint < 0.0d) {
                nowPoint = BitmapDescriptorFactory.HUE_RED;
                this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
                this.mainGameViewController.refreshAndSave();
            }
            this.appDelegate.timeSaveDictionary.setPoint(nowPoint + addPoint);
            _characterUnit.setNewStatus((short) -1);
            if (checkCharacterUnitViewsArrayActiveCnt() <= 0) {
                this.tool_1_SelectScrollUnit.changeButtonWithIndex((short) -1, (short) -1, (short) -1, (short) -1);
            }
            if (!_directToCPF) {
                this.mainGameViewController.refreshAndSave();
            }
            refreshPoint();
        }
    }

    public void reload() {
        CharacterUnitDictionary nowCharacterUnitDictionary;
        if (this.appDelegate.timeSaveDictionary != null) {
            this.mainGameBackViewUnit.refresh();
            if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() < 0) {
                this.tool_1_SelectScrollUnit.changeButtonWithIndex((short) -1, (short) -1, (short) -1, (short) -1);
                if (this.appDelegate.timeSaveDictionary != null) {
                    this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate("");
                    this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(-1.0f);
                }
            } else {
                boolean okF = false;
                String startDateString = this.appDelegate.timeSaveDictionary.getTool1SelectViewStartDate();
                float endSecondsFloat = this.appDelegate.timeSaveDictionary.getTool1SelectViewEndSeconds();
                if (startDateString != null && startDateString.length() > 0 && endSecondsFloat > 1.0d) {
                    okF = true;
                }
                if (okF) {
                    short tool_2_0 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_0Index();
                    short tool_2_1 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_1Index();
                    short tool_2_2 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_2Index();
                    this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex(), tool_2_0, tool_2_1, tool_2_2);
                    if (this.appDelegate.timeSaveDictionary != null) {
                        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate(startDateString);
                        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(endSecondsFloat);
                    }
                } else {
                    this.tool_1_SelectScrollUnit.changeButtonWithIndex((short) -1, (short) -1, (short) -1, (short) -1);
                    if (this.appDelegate.timeSaveDictionary != null) {
                        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate("");
                        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(-1.0f);
                    }
                }
            }
            if (this.nowCharacterUnitViewsArrayList != null) {
                for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
                    CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
                    if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null) {
                        characterUnit.reset();
                        String putDateString = nowCharacterUnitDictionary.getPutDate();
                        short eggID = nowCharacterUnitDictionary.getEggId();
                        short characterID = nowCharacterUnitDictionary.getCharacterId();
                        nowCharacterUnitDictionary.setEggId(eggID);
                        nowCharacterUnitDictionary.setCharacterId(characterID);
                        int characterUnitViewCenterX = nowCharacterUnitDictionary.getOffsetX();
                        int characterUnitViewCenterY = nowCharacterUnitDictionary.getOffsetY();
                        nowCharacterUnitDictionary.setOffsetX(characterUnitViewCenterX);
                        nowCharacterUnitDictionary.setOffsetY(characterUnitViewCenterY);
                        characterUnit.setCenter(characterUnitViewCenterX, characterUnitViewCenterY);
                        Log.d("MainGameLayout", "lll===centerX:" + characterUnitViewCenterX + ",centerY;" + characterUnitViewCenterY);
                        if (putDateString != null) {
                            nowCharacterUnitDictionary.setPutDate(putDateString);
                        } else {
                            nowCharacterUnitDictionary.setPutDate("");
                        }
                        float endSecondsFloat2 = nowCharacterUnitDictionary.getEndSeconds();
                        if (endSecondsFloat2 > 1.0f) {
                            nowCharacterUnitDictionary.setEndSeconds(endSecondsFloat2);
                        } else {
                            nowCharacterUnitDictionary.setEndSeconds(-1.0f);
                        }
                        short nowStatusInt = nowCharacterUnitDictionary.getNowStatus();
                        float openSecondsFloat = nowCharacterUnitDictionary.getOpenSeconds();
                        if (openSecondsFloat > 1.0f) {
                            nowCharacterUnitDictionary.setOpenSeconds(openSecondsFloat);
                        } else {
                            nowCharacterUnitDictionary.setOpenSeconds(-1.0f);
                        }
                        float blackSecondsFloat = nowCharacterUnitDictionary.getBlackSeconds();
                        if (blackSecondsFloat > 1.0f) {
                            nowCharacterUnitDictionary.setBlackSeconds(blackSecondsFloat);
                        } else {
                            nowCharacterUnitDictionary.setBlackSeconds(-1.0f);
                        }
                        if (nowStatusInt < 0) {
                            characterUnit.setNewStatus((short) -1);
                        } else if (nowStatusInt < 1) {
                            short openType = checkOpenWithPutDateString(nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getOpenSeconds());
                            if (openType == -1) {
                                characterUnit.setNewStatus((short) -1);
                            } else if (openType == 1 || openType == 2) {
                                if (openType == 2 && nowCharacterUnitDictionary.getCharacterId() == 9) {
                                    nowCharacterUnitDictionary.setCharacterId((short) 8);
                                }
                                short sickness_prevention_Short = nowCharacterUnitDictionary.getSicknessPrevention();
                                if (this.mainGameBackViewUnit.hp == 0 && sickness_prevention_Short != 1) {
                                    short eggId = nowCharacterUnitDictionary.getEggId();
                                    short characterId = nowCharacterUnitDictionary.getCharacterId();
                                    this.appDelegate.getClass();
                                    short returnID = checkSickWithID(eggId, characterId, (short) 400);
                                    if (returnID > 0) {
                                        nowCharacterUnitDictionary.setCharacterId(returnID);
                                    }
                                }
                                characterUnit.setNewStatus((short) 3);
                            } else {
                                characterUnit.setNewStatus((short) 0);
                            }
                        } else if (nowStatusInt < 4) {
                            characterUnit.setNewStatus((short) 3);
                        } else if (nowStatusInt < 10) {
                            characterUnit.directToCP();
                        }
                        characterUnit.randDirWithEggID(eggID, characterID, true);
                    }
                }
            }
            refreshPoint();
            checkCookAlarm();
            String dateString = this.appDelegate.timeSaveDictionary.getDate();
            Log.d("MainGameLayout", "==============================reload:" + dateString + "==============================");
            if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList == null) {
                Log.d("MainGameLayout", "===========================farmUnitDictionarysArrayList == null==============================");
            } else {
                Log.d("MainGameLayout", "===========================farmUnitDictionarysArrayList != null==============================");
            }
        }
    }

    public void checkGameLoop() {
        CharacterUnitDictionary nowCharacterUnitDictionary;
        short returnID;
        if (this.appDelegate.timeSaveDictionary != null && this.mainGameViewController.nowStatus == 0) {
            boolean saveF = false;
            this.mainGameBackViewUnit.refresh();
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            new Date();
            for (int i = this.nowCharacterUnitViewsArrayList.size() - 1; i >= 0; i--) {
                CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
                if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null) {
                    if (nowCharacterUnitDictionary.getNowStatus() == 0) {
                        short openType = checkOpenWithPutDateString(nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getOpenSeconds());
                        if (openType == -1) {
                            characterUnit.setNewStatus((short) -1);
                            saveF = true;
                        } else if (openType == 1 || openType == 2) {
                            if (openType == 2 && nowCharacterUnitDictionary.getCharacterId() == 9) {
                                nowCharacterUnitDictionary.setCharacterId((short) 8);
                            }
                            short sickness_prevention_Short = nowCharacterUnitDictionary.getSicknessPrevention();
                            if (this.mainGameBackViewUnit.hp == 0 && sickness_prevention_Short != 1) {
                                short eggId = nowCharacterUnitDictionary.getEggId();
                                short characterId = nowCharacterUnitDictionary.getCharacterId();
                                this.appDelegate.getClass();
                                short returnID2 = checkSickWithID(eggId, characterId, (short) 400);
                                if (returnID2 > 0) {
                                    nowCharacterUnitDictionary.setCharacterId(returnID2);
                                }
                            }
                            characterUnit.setNewStatus((short) 1);
                            saveF = true;
                        }
                    } else if (nowCharacterUnitDictionary.getNowStatus() == 3 && (returnID = checkBlackWithID(nowCharacterUnitDictionary.getEggId(), nowCharacterUnitDictionary.getCharacterId(), nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getBlackSeconds())) > 0) {
                        nowCharacterUnitDictionary.setCharacterId(returnID);
                        characterUnit.setNewStatus((short) 3);
                        saveF = true;
                    }
                }
            }
            if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() >= 0 && checkCharacterUnitViewsArrayActiveCnt() <= 0) {
                this.tool_1_SelectScrollUnit.changeButtonWithIndex((short) -1, (short) -1, (short) -1, (short) -1);
                saveF = true;
            }
            if (saveF) {
                this.mainGameViewController.refreshAndSave();
            }
        }
    }

    public short checkOpenWithPutDateString(String _putDateString, float _openSeconds) {
        short openType = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        if (_putDateString != null && _openSeconds > 1.0f && _putDateString.length() > 0) {
            Date putDate = null;
            try {
                putDate = sdf.parse(_putDateString);
            } catch (ParseException e) {
            }
            if (putDate != null) {
                openType = putDate.before(new Date(nowDate.getTime() - ((1 * ((long) (10.0f + _openSeconds))) * 1000))) ? (short) 2 : putDate.before(new Date(nowDate.getTime() - ((1 * ((long) _openSeconds)) * 1000))) ? (short) 1 : putDate.before(new Date(nowDate.getTime() + 10)) ? (short) 0 : (short) 2;
            }
        }
        return openType;
    }

    public short checkSickWithID(short _eggID, short _characterID, short _rate) {
        if (_eggID == 0) {
            if (_characterID == 1 || _characterID == 2 || _characterID == 20 || _characterID == 30 || _characterID == 34 || _characterID == 35 || _characterID == 51 || _characterID == 52 || _characterID == 53 || _characterID == 54 || _characterID == 68) {
                return (short) -1;
            }
            short randRate = (short) (Math.random() * 1000.0d);
            if (randRate >= _rate) {
                return (short) -1;
            }
            short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
            if (tool_0_0_level < 1) {
                return (short) 1;
            }
            short rand34Index = (short) (Math.random() * 10.0d);
            if (rand34Index != 0) {
                return (short) 1;
            }
            if (tool_0_0_level < 2) {
                return (short) 34;
            }
            short rand53Index = (short) (Math.random() * 3.0d);
            if (rand53Index != 0) {
                return (short) 34;
            }
            if (tool_0_0_level < 3) {
                return (short) 53;
            }
            short rand68Index = (short) (Math.random() * 2.0d);
            if (rand68Index != 0) {
                return (short) 53;
            }
            return (short) 68;
        }
        if (_eggID != 1 || _characterID == 1 || _characterID == 2 || _characterID == 14 || _characterID == 15 || _characterID == 19 || _characterID == 20 || _characterID == 27 || _characterID == 28 || _characterID == 29 || _characterID == 36) {
            return (short) -1;
        }
        short randRate2 = (short) (Math.random() * 1000.0d);
        if (randRate2 >= _rate) {
            return (short) -1;
        }
        short tool_0_0_level2 = this.appDelegate.getTool0LevelWithIndex((short) 0);
        if (tool_0_0_level2 < 1) {
            return (short) 1;
        }
        short rand34Index2 = (short) (Math.random() * 10.0d);
        if (rand34Index2 != 0) {
            return (short) 1;
        }
        if (tool_0_0_level2 < 2) {
            return (short) 19;
        }
        short rand53Index2 = (short) (Math.random() * 3.0d);
        if (rand53Index2 != 0) {
            return (short) 19;
        }
        if (tool_0_0_level2 < 3) {
            return (short) 29;
        }
        short rand36Index = (short) (Math.random() * 2.0d);
        if (rand36Index != 0) {
            return (short) 29;
        }
        return (short) 36;
    }

    public short checkBlackWithID(short _eggID, short _characterID, String _putDateString, float _blackSeconds) {
        short returnID = -1;
        if (_blackSeconds < 1.0f) {
            return (short) -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        if (_eggID == 0) {
            if (_characterID != 0 && _characterID != 2 && _characterID != 5 && _characterID != 20 && _characterID != 25 && _characterID != 26 && _characterID != 27 && _characterID != 30 && _characterID != 31 && _characterID != 33 && _characterID != 34 && _characterID != 35 && _characterID != 53 && _characterID != 54 && _characterID != 68 && _characterID != 70 && _characterID != 110 && this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() > 0 && _putDateString != null && _blackSeconds > 1.0d && _putDateString.length() > 0) {
                Date putDate = null;
                try {
                    putDate = sdf.parse(_putDateString);
                } catch (ParseException e) {
                }
                if (putDate != null && putDate.before(new Date(nowDate.getTime() - ((1 * ((long) _blackSeconds)) * 1000)))) {
                    if (_characterID == 109 || _characterID == 111 || _characterID == 112 || _characterID == 113) {
                        returnID = 110;
                    } else if (_characterID == 69 || _characterID == 71 || _characterID == 72 || _characterID == 73 || _characterID == 74 || _characterID == 75 || _characterID == 76 || _characterID == 77) {
                        returnID = 70;
                    } else {
                        returnID = 2;
                        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex((short) 0);
                        if (tool_0_0_level >= 1) {
                            short rand35Index = (short) (Math.random() * 50.0d);
                            if (rand35Index == 0) {
                                returnID = 35;
                                if (tool_0_0_level >= 2) {
                                    short rand54Index = (short) (Math.random() * 3.0d);
                                    if (rand54Index == 0) {
                                        returnID = 54;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (_eggID == 1 && _characterID != 0 && _characterID != 2 && _characterID != 4 && _characterID != 14 && _characterID != 15 && _characterID != 16 && _characterID != 17 && _characterID != 18 && _characterID != 19 && _characterID != 20 && _characterID != 27 && _characterID != 28 && _characterID != 29 && _characterID != 36 && _characterID != 38 && _characterID != 53 && this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() > 0 && _putDateString != null && _blackSeconds > 1.0d && _putDateString.length() > 0) {
            Date putDate2 = null;
            try {
                putDate2 = sdf.parse(_putDateString);
            } catch (ParseException e2) {
            }
            if (putDate2 != null && putDate2.before(new Date(nowDate.getTime() - ((1 * ((long) _blackSeconds)) * 1000)))) {
                if (_characterID == 52 || _characterID == 54 || _characterID == 55 || _characterID == 56) {
                    returnID = 53;
                } else if (_characterID == 37 || _characterID == 39 || _characterID == 40 || _characterID == 41 || _characterID == 42 || _characterID == 43 || _characterID == 44 || _characterID == 45 || _characterID == 46) {
                    returnID = 38;
                } else {
                    returnID = 2;
                    short tool_0_0_level2 = this.appDelegate.getTool0LevelWithIndex((short) 0);
                    if (tool_0_0_level2 >= 1) {
                        short rand35Index2 = (short) (Math.random() * 50.0d);
                        if (rand35Index2 == 0) {
                            returnID = 20;
                            if (tool_0_0_level2 >= 2) {
                                short rand54Index2 = (short) (Math.random() * 20.0d);
                                if (rand54Index2 == 0) {
                                    returnID = 28;
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnID;
    }

    public void animeGameLoop() {
        if (this.mainGameViewController.nowStatus == 0) {
            if (this.nowCharacterUnitViewsArrayList != null) {
                for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
                    CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
                    if (characterUnit != null) {
                        characterUnit.animeLoop();
                    }
                }
            }
            this.mainGameBackViewUnit.doLoop();
            this.tool_1_SelectScrollUnit.doLoop();
        }
    }

    public void getCharacterCheck(float _touchX, float _touchY) {
        CharacterUnitDictionary nowCharacterUnitDictionary;
        if (checkCharacterUnitViewsArrayActiveCnt() > 0) {
            for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
                CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
                if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null && nowCharacterUnitDictionary.getNowStatus() == 3 && _touchX >= characterUnit.frameOriginX && _touchX < characterUnit.frameOriginX + characterUnit.frameSizeWidth && _touchY >= characterUnit.frameOriginY && _touchY < characterUnit.frameOriginY + characterUnit.frameSizeHeight) {
                    characterUnit.setNewStatus((short) 4);
                    this.mainGameViewController.refreshAndSave();
                }
            }
        }
    }

    public void directCharacterToCP() {
        CharacterUnitDictionary nowCharacterUnitDictionary;
        boolean saveF = false;
        for (int i = 0; i < this.nowCharacterUnitViewsArrayList.size(); i++) {
            CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
            if (characterUnit != null && (nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag)) != null && nowCharacterUnitDictionary.getNowStatus() >= 4 && nowCharacterUnitDictionary.getNowStatus() < 9) {
                characterUnit.directToCP();
                saveF = true;
            }
        }
        if (saveF) {
            this.mainGameViewController.refreshAndSave();
        }
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws NumberFormatException {
        if (_tag == -100) {
            if (_buttonIndex == 0) {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        MainGameUnit.this.cancelInitManual();
                    }
                }, 100L);
                return;
            } else {
                if (_buttonIndex == 1) {
                    this.mainGameViewController.doManualLayoutDisplay((short) 0);
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
        if (_tag == -1) {
            if (_buttonIndex != 0 && _buttonIndex == 1 && _subtag >= 0 && _subtag < this.appDelegate.TOOL_1_ALL_CNT) {
                putEggsWithTool1(_subtag);
                return;
            } else {
                this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
                return;
            }
        }
        if (_tag == 0) {
            if (_buttonIndex != 0 && _buttonIndex == 1) {
                if (checkCharacterUnitViewsArrayActiveCnt() <= 0) {
                    if (_subtag >= 0 && _subtag < this.appDelegate.TOOL_1_ALL_CNT) {
                        putEggsWithTool1(_subtag);
                        return;
                    }
                } else {
                    this.clearEggsSubTag = _subtag;
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.3
                        @Override // java.lang.Runnable
                        public void run() {
                            MainGameUnit.this.clearEggsAlert(MainGameUnit.this.clearEggsSubTag);
                        }
                    }, 100L);
                }
            }
            this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
            return;
        }
        if (_tag == 1) {
            if (_buttonIndex != 0 && _buttonIndex == 1 && this.mainGameBackViewUnit.fixKitchenWithCP(_subtag)) {
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(9);
                }
                this.mainGameViewController.refreshAndSave();
                refreshPoint();
                return;
            }
            return;
        }
        if (_tag == 2) {
            if (_buttonIndex != 0 && _buttonIndex == 1 && this.mainGameBackViewUnit.levelUpKitchenWithCP(_subtag)) {
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
            if (_buttonIndex == 0) {
                cleanCkSendKitchenString();
                return;
            } else {
                if (_buttonIndex == 1) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.4
                        @Override // java.lang.Runnable
                        public void run() {
                            MainGameUnit.this.displayCheckLoseTool1Alert();
                        }
                    }, 100L);
                    return;
                }
                return;
            }
        }
        if (_tag == 11) {
            if (_buttonIndex == 0) {
                cleanCkSendKitchenString();
                return;
            } else {
                if (_buttonIndex == 1) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.5
                        @Override // java.lang.Runnable
                        public void run() {
                            MainGameUnit.this.displayResetCharacterUnitViewsArrayAlert();
                        }
                    }, 100L);
                    return;
                }
                return;
            }
        }
        if (_tag == 12) {
            if (_buttonIndex == 0) {
                cleanCkSendKitchenString();
                return;
            } else {
                if (_buttonIndex == 1) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout0.MainGameUnit.6
                        @Override // java.lang.Runnable
                        public void run() {
                            MainGameUnit.this.doReceiveKitchenFromCK();
                        }
                    }, 5L);
                    return;
                }
                return;
            }
        }
        if (_tag == 99) {
            if (_buttonIndex != 0) {
            }
            this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), (short) -2, (short) -2, (short) -2);
        }
    }

    public void clearBitmap() {
        if (this.tool_1_SelectScrollUnit != null) {
            this.tool_1_SelectScrollUnit.clearBitmap();
        }
        if (this.mainGameBackViewUnit != null) {
            this.mainGameBackViewUnit.clearBitmap();
        }
    }

    public void refreshBitmap() {
        if (this.tool_1_SelectScrollUnit != null) {
            this.tool_1_SelectScrollUnit.refreshBitmap();
        }
        if (this.mainGameBackViewUnit != null) {
            this.mainGameBackViewUnit.refreshBitmap();
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
            this.alertUnitType0.gameOnTouch(event);
            return true;
        }
        if (this.tool_2_SelectListUnit != null && !this.tool_2_SelectListUnit.hidden) {
            boolean returnF2 = this.tool_2_SelectListUnit.gameOnTouch(event);
            return returnF2;
        }
        if (this.eggSelectUnit != null) {
            returnF = this.eggSelectUnit.gameOnTouch(event);
        }
        short touchIndex = -1;
        if (event.getY() >= this.MAINGAMEBACK_TOUCH_RANGE_Y_MIN && event.getY() < this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX) {
            if (this.mainGameBackViewUnit != null) {
                this.mainGameBackViewUnit.gameOnTouch(event);
            }
            touchIndex = 0;
            if (this.tool_1_SelectScrollUnit != null) {
                this.tool_1_SelectScrollUnit.unclickAllButton();
            }
        } else if (event.getY() >= this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN && event.getY() < this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX) {
            if (this.tool_1_SelectScrollUnit != null) {
                this.tool_1_SelectScrollUnit.gameOnTouch(event);
            }
            touchIndex = 1;
        }
        if (touchIndex != 1 && this.tool_1_SelectScrollUnit != null) {
            this.tool_1_SelectScrollUnit.unclickAllButton();
        }
        return returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.mainGameBackViewUnit != null) {
            this.mainGameBackViewUnit.gameDraw(canvas);
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.gameDraw(canvas);
        }
        if (this.tool_1_SelectScrollUnit != null) {
            this.tool_1_SelectScrollUnit.gameDraw(canvas);
        }
        if (this.bottomBlockLayout != null) {
            this.bottomBlockLayout.gameDraw(canvas);
        }
        if (this.eggSelectUnit != null) {
            this.eggSelectUnit.gameDraw(canvas);
        }
        if (this.tool_2_SelectListUnit != null && !this.tool_2_SelectListUnit.hidden) {
            this.tool_2_SelectListUnit.gameDraw(canvas);
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
        if (this.bottomBlockLayout != null) {
            this.bottomBlockLayout.onDestroy();
            this.bottomBlockLayout = null;
        }
        if (this.tool_2_SelectListUnit != null) {
            this.tool_2_SelectListUnit.onDestroy();
            this.tool_2_SelectListUnit = null;
        }
        if (this.cpDisplayUnit != null) {
            this.cpDisplayUnit.onDestroy();
            this.cpDisplayUnit = null;
        }
        if (this.tool_1_SelectScrollUnit != null) {
            this.tool_1_SelectScrollUnit.onDestroy();
            this.tool_1_SelectScrollUnit = null;
        }
        if (this.tool_1_SelectScrolView != null) {
            this.tool_1_SelectScrolView.removeAllViews();
            this.tool_1_SelectScrolView = null;
        }
        if (this.eggSelectUnit != null) {
            this.eggSelectUnit.onDestroy();
            this.eggSelectUnit = null;
        }
        if (this.tool_2_SelectView != null) {
            this.tool_2_SelectView.onDestroy();
            this.tool_2_SelectView = null;
        }
        if (this.mainGameBackViewUnit != null) {
            this.mainGameBackViewUnit.onDestroy();
            this.mainGameBackViewUnit = null;
        }
        if (this.nowCharacterUnitViewsArrayList != null) {
            while (this.nowCharacterUnitViewsArrayList.size() != 0) {
                CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(0);
                if (characterUnit != null) {
                    characterUnit.onDestroy();
                }
                this.nowCharacterUnitViewsArrayList.remove(0);
            }
            this.nowCharacterUnitViewsArrayList = null;
        }
        this.mainGameViewController = null;
        this.appDelegate = null;
    }
}
