package com.idtinc.maingame.sublayout0;

import android.content.SharedPreferences;
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
    private HorizontalScrollView tool_1_SelectScrolView;
    private Tool_1_SelectScrollUnit tool_1_SelectScrollUnit;
    private Tool_2_SelectListUnit tool_2_SelectListUnit;
    public Tool_2_SelectView tool_2_SelectView;
    private float zoomRate;

    static /* synthetic */ short access$0(MainGameUnit r1) {
        return r1.clearEggsSubTag;
    }

    public MainGameUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameViewController, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.clearEggsSubTag = -1;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED;
        this.MAINGAMEBACK_TOUCH_RANGE_X_MAX = 320.0f;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MIN = 52.0f;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 348.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MAX = 320.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 348.0f;
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX = 430.0f;
        this.nowStatus = -1;
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
        this.tool_1_SelectScrolView = null;
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
        if (this.appDelegate.isRetina4 == true) goto L28;
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 348.0f * this.zoomRate;
    L5:
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MIN = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_TOUCH_RANGE_X_MAX = 320.0f * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L29;
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 348.0f * this.zoomRate;
    L8:
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX = this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN + (82.0f * this.zoomRate);
        this.nowStatus = -1;
        this.clearEggsSubTag = -1;
        this.GAMEZONEVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L30;
        this.GAMEZONEVIEW_OFFSET_Y = 140.0f * this.zoomRate;
    L11:
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
        this.nowCharacterUnitViewsArrayList = new ArrayList();
        int i = 0;
    L13:
        if (i >= this.appDelegate.CHARACTERUNITVIEW_TOTAL) goto L14;
        CharacterUnit characterUnit = new CharacterUnit();
        characterUnit.init(-999.0f, -999.0f, this.CHARACTERUNITVIEW_WIDTH, this.CHARACTERUNITVIEW_HEIGHT, this.zoomRate, this);
        characterUnit.tag = (short) i;
        characterUnit.reset();
        this.nowCharacterUnitViewsArrayList.add(characterUnit);
        i = i + 1;
        goto L13
    L14:
        this.mainGameBackViewUnit = new MainGameBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.tool_2_SelectView = new Tool_2_SelectView(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.eggSelectUnit = new EggSelectUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.TOOL_1_SELECT_BACKVIEW_WIDTH = 320.0f * this.zoomRate;
        this.TOOL_1_SELECT_BACKVIEW_HEIGHT = 132.0f * this.zoomRate;
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        if (this.appDelegate.isRetina4 == true) goto L32;
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_Y = 348.0f * this.zoomRate;
    L17:
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_X = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_OFFSET_Y = BitmapDescriptorFactory.HUE_RED * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_WIDTH = 320.0f * this.zoomRate;
        this.TOOL_1_SELECT_SCROLLVIEW_HEIGHT = this.TOOL_1_SELECT_BACKVIEW_HEIGHT;
        this.tool_1_SelectScrollUnit = new Tool_1_SelectScrollUnit((int) this.TOOL_1_SELECT_SCROLLVIEW_WIDTH, (int) this.TOOL_1_SELECT_SCROLLVIEW_HEIGHT, this.zoomRate, this, this.appDelegate);
        if (this.appDelegate.isRetina4 == true) goto L33;
        this.bottomBlockLayout = new BottomBlockLayout((int) (320.0f * this.zoomRate), (int) (480.0f * this.zoomRate), this.zoomRate, this.appDelegate);
    L20:
        this.cpDisplayUnit = new CPDisplayUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (this.appDelegate.isRetina4 == true) goto L34;
        this.cpDisplayUnit.setBackViewParams(210.0f, 311.0f);
    L23:
        this.tool_2_SelectListUnit = new Tool_2_SelectListUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (this.appDelegate.isRetina4 == true) goto L35;
        this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
    L26:
        this.alertUnitType0.delegate = this;
        refreshBitmap();
        return;
    L35:
        this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        goto L26
    L34:
        this.cpDisplayUnit.setBackViewParams(210.0f, 355.0f);
        goto L23
    L33:
        this.bottomBlockLayout = new BottomBlockLayout((int) (320.0f * this.zoomRate), (int) (568.0f * this.zoomRate), this.zoomRate, this.appDelegate);
        goto L20
    L32:
        this.TOOL_1_SELECT_BACKVIEW_OFFSET_Y = 436.0f * this.zoomRate;
        goto L17
    L30:
        this.GAMEZONEVIEW_OFFSET_Y = 184.0f * this.zoomRate;
        goto L11
    L29:
        this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN = 436.0f * this.zoomRate;
        goto L8
    L28:
        this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX = 436.0f * this.zoomRate;
        goto L5
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

    public void doDisplay() {
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
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        directCharacterToCP();
    }

    public void displayInitManual() {
        if (this.appDelegate.defaultSharedPreferences != null) goto L6;
        return;
    L6:
        if (this.appDelegate.defaultSharedPreferences.getBoolean("init_manual_kitchen", false) == false) goto L20;
        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
        editor.putBoolean("init_manual_kitchen", false);
        editor.commit();
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L12;
        String titleLabelString = "";
        String contentLabelString0 = "";
        String contentLabelString1 = "";
        String contentLabelString2 = "『台所』の操作解説を見ますか？";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
    L10:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        String r4 = contentLabelString0;
        String r5 = contentLabelString1;
        String r6 = contentLabelString2;
        String r7 = contentLabelString3;
        String r8 = contentLabelString4;
        this.alertUnitType0.setContentLabelParams(r4, r5, r6, r7, r8, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = -100;
        this.alertUnitType0.subTag = -1;
        popAlert();
        return;
    L12:
        if (languageString.equals("zh-TW") == false) goto L14;
    L15:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "你想要看看『廚房』的教學說明嗎？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L10
    L14:
        if (languageString.equals("zh-HK") == true) goto L15;
        if (languageString.equals("zh-CN") == false) goto L19;
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "你想要看看『厨房』的教学说明吗？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L10
    L19:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "Do you want to read ";
        contentLabelString2 = "the \"Kitchen\" manual?";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L10
    }

    public void cancelInitManual() {
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L8;
        String titleLabelString = "";
        String contentLabelString0 = "";
        String contentLabelString1 = "解説を見たい時、『その他』を";
        String contentLabelString2 = "タップして下さい。";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = 10.0f;
    L5:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        String r4 = contentLabelString0;
        String r5 = contentLabelString1;
        String r6 = contentLabelString2;
        String r7 = contentLabelString3;
        String r8 = contentLabelString4;
        this.alertUnitType0.setContentLabelParams(r4, r5, r6, r7, r8, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
        this.alertUnitType0.tag = -99;
        this.alertUnitType0.subTag = -1;
        popAlert();
        return;
    L8:
        if (languageString.equals("zh-TW") == false) goto L10;
    L11:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "當你想看說明時,就點選『其他』吧。";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L5
    L10:
        if (languageString.equals("zh-HK") == true) goto L11;
        if (languageString.equals("zh-CN") == false) goto L15;
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "当你想看说明时,就点击『其他』吧。";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L5
    L15:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "Tap \"Other\" when you want";
        contentLabelString2 = " to red the manuals.";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L5
    }

    public void setCookAlarmOn() {
        if (this.appDelegate.timeSaveDictionary != null) goto L5;
        return;
    L5:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        short nowButtonIndexShort = this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex();
        if (nowButtonIndexShort >= 0) goto L8;
        nowButtonIndexShort = -1;
    L8:
        String startDateString = this.appDelegate.timeSaveDictionary.getTool1SelectViewStartDate();
        float endSecondsFloat = this.appDelegate.timeSaveDictionary.getTool1SelectViewEndSeconds();
        if (startDateString == null) goto L59;
        if (startDateString.length() <= 0) goto L59;
        if (endSecondsFloat <= 1.0f) goto L59;
        Date startDate = null;
        startDate = sdf.parse(startDateString);     // Catch: ParseException -> L55
    L16:
        Date endDate = null;
        if (startDate == null) goto L19;
        endDate = new Date(startDate.getTime() + ((long) (1000.0f * endSecondsFloat)));
    L19:
        if (endDate == null) goto L59;
        if (endDate.after(nowDate) == false) goto L59;
        ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(1, nowButtonIndexShort);
        String tool1NameString = "";
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L34;
        if (toolDataDictionary == null) goto L28;
        String nameString = toolDataDictionary.getTitleJa();
        if (nameString.length() <= 0) goto L28;
        tool1NameString = String.valueOf(nameString) + "で";
    L28:
        String alarmBodyString = "☆ " + tool1NameString + "調理完了しました！ ☆";
    L29:
        short nowCookingEggID = this.appDelegate.getNowCookingEggID();
        if (nowCookingEggID != 1) goto L54;
        this.appDelegate.setCookAlarmOnWithFireDateString(sdf.format(endDate), alarmBodyString, "1");
        goto L59
    L54:
        this.appDelegate.setCookAlarmOnWithFireDateString(sdf.format(endDate), alarmBodyString, "0");
        goto L59
    L34:
        if (languageString.equals("zh-TW") == false) goto L36;
    L37:
        if (toolDataDictionary == null) goto L41;
        String nameString2 = toolDataDictionary.getTitleZhTW();
        if (nameString2.length() <= 0) goto L41;
        tool1NameString = "使用" + nameString2;
    L41:
        alarmBodyString = "☆ " + tool1NameString + "調理已經完成了,趕快來收成吧！ ☆";
        goto L29
    L36:
        if (languageString.equals("zh-HK") == true) goto L37;
        if (languageString.equals("zh-CN") == false) goto L49;
        if (toolDataDictionary == null) goto L48;
        String nameString3 = toolDataDictionary.getTitleZhCN();
        if (nameString3.length() <= 0) goto L48;
        tool1NameString = "使用" + nameString3;
    L48:
        alarmBodyString = "☆ " + tool1NameString + "调理已经完成了,赶快来收成吧！ ☆";
        goto L29
    L49:
        if (toolDataDictionary == null) goto L53;
        String nameString4 = toolDataDictionary.getTitleEn();
        if (nameString4.length() <= 0) goto L53;
        tool1NameString = "(" + nameString4 + ")";
    L53:
        alarmBodyString = "☆ Hatching completed！ " + tool1NameString + " ☆";
    }

    public void checkCookAlarm() {
        if (this.appDelegate.defaultSharedPreferences != null) goto L6;
        return;
    L6:
        if (this.appDelegate.defaultSharedPreferences.getBoolean("cook_alarm", false) == false) goto L8;
        this.appDelegate.removeAlarmNotificationWithNotificationID("cook_alarm");
        setCookAlarmOn();
        return;
    L8:
        this.appDelegate.removeAlarmNotificationWithNotificationID("cook_alarm");
    }

    public void readyCheckTimeDoorOpenFromCK() {
        if (this.hidden == false) goto L5;
        return;
    L5:
        new Handler().postDelayed(new AnonymousClass1(this), 2500);
    }

    public void checkTimeDoorOpenFromCK() {
        if (this.hidden == false) goto L6;
        return;
    L6:
        if (this.alertUnitType0 == null) goto L11;
        if (this.alertUnitType0.hidden == true) goto L11;
        readyCheckTimeDoorOpenFromCK();
        return;
    L11:
        if (this.appDelegate != null) goto L13;
        return;
    L13:
        if (this.appDelegate.defaultSharedPreferences != null) goto L16;
        AppDelegate r1 = this.appDelegate;
        r1.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
    L16:
        if (this.appDelegate.defaultSharedPreferences == null) goto L22;
        String ck_send_kitchen_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_kitchen_string", "");
        if (ck_send_kitchen_string.length() <= 0) goto L23;
        displayCheckReceiveKitchenFromCKAlert();
        return;
    L23:
        return;
    }

    public void displayCheckReceiveKitchenFromCKAlert() {
        if (this.hidden == false) goto L5;
        return;
    L5:
        HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
        if (tool_1_HashMap == null) goto L67;
        if (tool_1_HashMap.size() != 7) goto L67;
        hiddenAlert();
        int tool_1_KindCnt = 0;
        int kitchenLevelShort = -1;
        if (tool_1_HashMap.get("-1") == null) goto L21;
        kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
        tool_1_HashMap.remove("-1");
        if (kitchenLevelShort >= 0) goto L14;
    L41:
        kitchenLevelShort = -1;
        goto L21
    L14:
        if (kitchenLevelShort > 2) goto L41;
        String infosString = "";
        int i = 0;
    L17:
        if (i >= 6) goto L19;
        if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) == null) goto L39;
        int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
        if (tool_1_levelShort < 0) goto L39;
        if (tool_1_levelShort > 2) goto L39;
        if (infosString.length() > 0) goto L40;
        infosString = String.valueOf(i) + "_" + tool_1_levelShort;
        goto L39
    L40:
        infosString = String.valueOf(infosString) + "," + i + "_" + tool_1_levelShort;
    L39:
        i = i + 1;
        goto L17
    L19:
        if (infosString.length() <= 0) goto L21;
        tool_1_KindCnt = this.alertUnitType0.set_Tool_1_Images_View_Infos(infosString);
    L21:
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L44;
        String titleLabelString = "台所Lv." + (kitchenLevelShort + 1);
        String contentLabelString0 = "";
        String contentLabelString1 = "";
        String contentLabelString2 = "";
        String contentLabelString3 = "この台所と同期します。よろしいですか？";
        String contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L42;
        float contentLabelLanguageOffsetY = 8.0f;
    L26:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = 10;
        this.alertUnitType0.subTag = -1;
        popAlert();
        if (this.hidden == true) goto L67;
        this.appDelegate.doSoundPoolPlay(16);
        goto L67
    L42:
        contentLabelLanguageOffsetY = 16.0f;
        goto L26
    L44:
        if (languageString.equals("zh-TW") == false) goto L46;
    L47:
        titleLabelString = "廚房Lv." + (kitchenLevelShort + 1);
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "是否要同步從時空門傳送來的廚房？";
        contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L50;
        contentLabelLanguageOffsetY = 8.0f;
        goto L26
    L50:
        contentLabelLanguageOffsetY = 16.0f;
        goto L26
    L46:
        if (languageString.equals("zh-HK") == true) goto L47;
        if (languageString.equals("zh-CN") == false) goto L57;
        titleLabelString = "厨房Lv." + (kitchenLevelShort + 1);
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "是否要同步从时空门传送来的厨房？";
        contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L56;
        contentLabelLanguageOffsetY = 8.0f;
        goto L26
    L56:
        contentLabelLanguageOffsetY = 16.0f;
        goto L26
    L57:
        titleLabelString = "Kitchen Lv." + (kitchenLevelShort + 1);
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "Do you want to sync with this kitchen?";
        contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L60;
        contentLabelLanguageOffsetY = 8.0f;
        goto L26
    L60:
        contentLabelLanguageOffsetY = 16.0f;
    }

    public void displayCheckLoseTool1Alert() {
        if (this.hidden == false) goto L5;
        return;
    L5:
        HashMap<String, String> lose_tool_1_HashMap = getLose_Tool_1_HashMap();
        int lose_Tool_1_Cnt = 0;
        if (lose_tool_1_HashMap == null) goto L8;
        lose_Tool_1_Cnt = lose_tool_1_HashMap.size();
    L8:
        if (lose_Tool_1_Cnt <= 0) goto L56;
        hiddenAlert();
        int tool_1_KindCnt = 0;
        if (lose_tool_1_HashMap == null) goto L17;
        String infosString = "";
        int i = 0;
    L13:
        if (i >= 8) goto L15;
        if (lose_tool_1_HashMap.get(new StringBuilder().append(i).toString()) == null) goto L35;
        int tool_1_levelShort = Integer.valueOf(lose_tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
        if (tool_1_levelShort < 0) goto L35;
        if (tool_1_levelShort > 2) goto L35;
        if (infosString.length() > 0) goto L36;
        infosString = String.valueOf(i) + "_" + tool_1_levelShort;
        goto L35
    L36:
        infosString = String.valueOf(infosString) + "," + i + "_" + tool_1_levelShort;
    L35:
        i = i + 1;
        goto L13
    L15:
        if (infosString.length() <= 0) goto L17;
        tool_1_KindCnt = this.alertUnitType0.set_Tool_1_Images_View_Infos(infosString);
    L17:
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L39;
        String titleLabelString = "調理器具がなくなる";
        String contentLabelString0 = "";
        String contentLabelString1 = "";
        String contentLabelString2 = "";
        String contentLabelString3 = "同期すると、以上の調理器具がな";
        String contentLabelString4 = "くなります。よろしいですか？";
        if (tool_1_KindCnt > 5) goto L37;
        float contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
    L22:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = 11;
        this.alertUnitType0.subTag = -1;
        popAlert();
        if (this.hidden == true) goto L63;
        this.appDelegate.doSoundPoolPlay(4);
    L63:
        return;
    L37:
        contentLabelLanguageOffsetY = 6.0f;
        goto L22
    L39:
        if (languageString.equals("zh-TW") == false) goto L41;
    L42:
        titleLabelString = "失去調理用具";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "將會失去以上調理用具。確定要同步廚房？";
        contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L45;
        contentLabelLanguageOffsetY = 8.0f;
        goto L22
    L45:
        contentLabelLanguageOffsetY = 16.0f;
        goto L22
    L41:
        if (languageString.equals("zh-HK") == true) goto L42;
        if (languageString.equals("zh-CN") == false) goto L52;
        titleLabelString = "失去調理用具";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "将会失去以上调理用具。确定要同步厨房？";
        contentLabelString4 = "";
        if (tool_1_KindCnt > 5) goto L51;
        contentLabelLanguageOffsetY = 8.0f;
        goto L22
    L51:
        contentLabelLanguageOffsetY = 16.0f;
        goto L22
    L52:
        titleLabelString = "Lose Kitchenware";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "";
        contentLabelString3 = "You will lose these kitchenware.";
        contentLabelString4 = "Do you want to sync?";
        if (tool_1_KindCnt > 5) goto L55;
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L22
    L55:
        contentLabelLanguageOffsetY = 6.0f;
        goto L22
    L56:
        displayResetCharacterUnitViewsArrayAlert();
        goto L63
    }

    public void displayResetCharacterUnitViewsArrayAlert() {
        if (this.hidden == false) goto L6;
        return;
    L6:
        if (this.appDelegate == null) goto L44;
        boolean cancelAllF = false;
        short tool_1_selectview_nowbuttonindex = this.appDelegate.getShort_tool_1_selectview_nowbuttonindex();
        if (tool_1_selectview_nowbuttonindex < 0) goto L20;
        short tool_1_selectview_nowbuttonindex_level = this.appDelegate.getTool1LevelWithIndex(tool_1_selectview_nowbuttonindex);
        if (tool_1_selectview_nowbuttonindex_level < 0) goto L20;
        HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
        if (tool_1_HashMap == null) goto L20;
        if (tool_1_selectview_nowbuttonindex >= 6) goto L28;
        if (tool_1_HashMap.get(new StringBuilder().append(tool_1_selectview_nowbuttonindex).toString()) == null) goto L20;
        int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(tool_1_selectview_nowbuttonindex).toString())).intValue();
        if (tool_1_levelShort >= tool_1_selectview_nowbuttonindex_level) goto L20;
        cancelAllF = true;
        goto L20
    L28:
        if (tool_1_selectview_nowbuttonindex != 6) goto L20;
        if (tool_1_HashMap.get("-1") == null) goto L20;
        int kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
        if (kitchenLevelShort >= 3) goto L20;
        cancelAllF = true;
    L20:
        if (cancelAllF == false) goto L43;
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L35;
        String titleLabelString = "調理中止";
        String contentLabelString0 = "";
        String contentLabelString1 = "調理を中止し、同期します。";
        String contentLabelString2 = "よろしいですか？";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = 10.0f;
    L24:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        String r5 = contentLabelString0;
        String r6 = contentLabelString1;
        String r7 = contentLabelString2;
        String r8 = contentLabelString3;
        String r9 = contentLabelString4;
        this.alertUnitType0.setContentLabelParams(r5, r6, r7, r8, r9, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = 12;
        this.alertUnitType0.subTag = -1;
        popAlert();
        if (this.hidden == true) goto L45;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L45:
        return;
    L35:
        if (languageString.equals("zh-TW") == false) goto L37;
    L38:
        titleLabelString = "調理中斷";
        contentLabelString0 = "";
        contentLabelString1 = "你確定要中斷目前的調理,";
        contentLabelString2 = "然後同步廚房嗎？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L24
    L37:
        if (languageString.equals("zh-HK") == true) goto L38;
        if (languageString.equals("zh-CN") == false) goto L42;
        titleLabelString = "調理中斷";
        contentLabelString0 = "";
        contentLabelString1 = "你确定要中断目前的调理,";
        contentLabelString2 = "然後同步厨房吗？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L24
    L42:
        titleLabelString = "Interrupt Hatch";
        contentLabelString0 = "";
        contentLabelString1 = "Are you sure you want to interrupt";
        contentLabelString2 = "current hatch,and then sync?";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L24
    L43:
        doReceiveKitchenFromCK();
        return;
    }

    public void doReceiveKitchenFromCK() {
        if (this.appDelegate != null) goto L6;
        return;
    L6:
        if (this.appDelegate.timeSaveDictionary != null) goto L8;
        return;
    L8:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null) goto L10;
        return;
    L10:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() <= 1) goto L88;
        boolean saveF = false;
        int kitchenLevelShort = -1;
        HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
        if (tool_1_HashMap != null) goto L14;
    L18:
        if (tool_1_HashMap == null) goto L35;
        if (kitchenLevelShort < 0) goto L35;
        if (kitchenLevelShort > 2) goto L35;
        ArrayList<ToolUnitDictionary> tool0DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0);
        if (tool0DictionarysArrayList == null) goto L35;
        if (tool0DictionarysArrayList.size() <= 0) goto L35;
        ToolUnitDictionary tool0Dictionary = tool0DictionarysArrayList.get(0);
        if (tool0Dictionary == null) goto L35;
        tool0Dictionary.setLevel((short) kitchenLevelShort);
        saveF = true;
        ArrayList<ToolUnitDictionary> tool1DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1);
        if (tool1DictionarysArrayList == null) goto L35;
        if (tool1DictionarysArrayList.size() <= 6) goto L35;
        short tool_1_selectview_nowbuttonindex = this.appDelegate.getShort_tool_1_selectview_nowbuttonindex();
        int i = 0;
    L34:
        if (i >= tool1DictionarysArrayList.size()) goto L35;
        ToolUnitDictionary tool1Dictionary = tool1DictionarysArrayList.get(i);
        if (tool1Dictionary == null) goto L68;
        if (i >= 6) goto L70;
        if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) == null) goto L67;
        int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
        if (tool_1_levelShort >= (-2)) goto L60;
    L61:
        tool_1_levelShort = -2;
    L62:
        if (i != tool_1_selectview_nowbuttonindex) goto L66;
        short now_tool_1_levelShort = this.appDelegate.getTool1LevelWithIndex((short) i);
        if (now_tool_1_levelShort <= tool_1_levelShort) goto L66;
        resetCharacterUnitViewsArray();
    L66:
        tool1Dictionary.setLevel((short) tool_1_levelShort);
        saveF = true;
        goto L67
    L60:
        if (tool_1_levelShort <= 2) goto L62;
    L67:
        this.appDelegate.displayFullAdView();
        goto L68
    L70:
        if (i != 6) goto L77;
        if (kitchenLevelShort >= 3) goto L67;
        if (i != tool_1_selectview_nowbuttonindex) goto L75;
        resetCharacterUnitViewsArray();
    L75:
        tool1Dictionary.setLevel(-2);
        saveF = true;
        goto L67
    L77:
        if (i != 7) goto L67;
        if (kitchenLevelShort >= 3) goto L67;
        if (i != tool_1_selectview_nowbuttonindex) goto L82;
        resetCharacterUnitViewsArray();
    L82:
        tool1Dictionary.setLevel(-2);
        saveF = true;
    L68:
        i = i + 1;
    L35:
        if (saveF == true) goto L37;
    L45:
        if (tool_1_HashMap != null) goto L48;
    L48:
        if (this.hidden == true) goto L50;
        this.appDelegate.doSoundPoolPlay(17);
    L50:
        cleanCkSendKitchenString();
        return;
    L37:
        if (this.appDelegate.timeSaveDictionary == null) goto L40;
        this.mainGameViewController.refreshAndSave();
    L40:
        if (this.mainGameBackViewUnit == null) goto L43;
        this.mainGameBackViewUnit.refreshBitmap();
    L43:
        if (this.tool_1_SelectScrollUnit == null) goto L45;
        this.tool_1_SelectScrollUnit.refresh();
        goto L45
    L14:
        if (tool_1_HashMap.size() != 7) goto L18;
        if (tool_1_HashMap.get("-1") == null) goto L18;
        kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
        goto L18
    }

    public void cleanCkSendKitchenString() {
        if (this.appDelegate != null) goto L6;
        return;
    L6:
        if (this.appDelegate.defaultSharedPreferences != null) goto L9;
        AppDelegate r1 = this.appDelegate;
        r1.defaultSharedPreferences = this.appDelegate.getSharedPreferences("default", 0);
    L9:
        if (this.appDelegate.defaultSharedPreferences == null) goto L11;
        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
        editor.putString("ck_send_kitchen_string", "");
        editor.commit();
        return;
    }

    public HashMap<String, String> getLose_Tool_1_HashMap() {
        HashMap<String, String> lose_tool_1_HashMap = null;
        if (this.appDelegate != null) goto L7;
        return null;
    L7:
        if (this.appDelegate.timeSaveDictionary != null) goto L10;
        return null;
    L10:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null) goto L13;
        return null;
    L13:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() <= 1) goto L76;
        int kitchenLevelShort = -1;
        HashMap<String, String> tool_1_HashMap = getReceive_Tool_1_HashMap();
        if (tool_1_HashMap != null) goto L18;
    L22:
        if (tool_1_HashMap == null) goto L37;
        if (kitchenLevelShort < 0) goto L37;
        if (kitchenLevelShort > 2) goto L37;
        ArrayList<ToolUnitDictionary> tool0DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(0);
        if (tool0DictionarysArrayList == null) goto L37;
        if (tool0DictionarysArrayList.size() <= 0) goto L37;
        ToolUnitDictionary tool0Dictionary = tool0DictionarysArrayList.get(0);
        if (tool0Dictionary == null) goto L37;
        ArrayList<ToolUnitDictionary> tool1DictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1);
        if (tool1DictionarysArrayList == null) goto L37;
        lose_tool_1_HashMap = new HashMap();
        int i = 0;
    L36:
        if (i >= tool1DictionarysArrayList.size()) goto L37;
        short tool1Level = this.appDelegate.getTool1LevelWithIndex((short) i);
        if (tool1Level < 0) goto L53;
        if (i >= 6) goto L55;
        if (tool_1_HashMap.get(new StringBuilder().append(i).toString()) == null) goto L53;
        int tool_1_levelShort = Integer.valueOf(tool_1_HashMap.get(new StringBuilder().append(i).toString())).intValue();
        if (tool_1_levelShort >= tool1Level) goto L53;
        lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append(tool1Level).toString());
        goto L53
    L55:
        if (i != 6) goto L60;
        if (kitchenLevelShort >= 3) goto L53;
        lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append(tool1Level).toString());
        goto L53
    L60:
        if (i != 7) goto L53;
        if (kitchenLevelShort >= 3) goto L53;
        lose_tool_1_HashMap.put(new StringBuilder().append(i).toString(), new StringBuilder().append(tool1Level).toString());
    L53:
        i = i + 1;
    L37:
        if (tool_1_HashMap != null) goto L39;
    L39:
        if (lose_tool_1_HashMap == null) goto L77;
        if (lose_tool_1_HashMap.size() > 0) goto L77;
        lose_tool_1_HashMap = null;
    L77:
        return lose_tool_1_HashMap;
    L18:
        if (tool_1_HashMap.size() != 7) goto L22;
        if (tool_1_HashMap.get("-1") == null) goto L22;
        kitchenLevelShort = Integer.valueOf(tool_1_HashMap.get("-1")).intValue();
        goto L22
    L76:
        return null;
    }

    public HashMap<String, String> getReceive_Tool_1_HashMap() {
        HashMap<String, String> tool_1_HashMap = null;
        if (this.appDelegate != null) goto L5;
    L31:
        if (tool_1_HashMap != null) goto L33;
        return tool_1_HashMap;
    L33:
        if (tool_1_HashMap.size() == 7) goto L64;
        return null;
    L64:
        return tool_1_HashMap;
    L5:
        if (this.appDelegate.defaultSharedPreferences == null) goto L31;
        String ck_send_kitchen_string = this.appDelegate.defaultSharedPreferences.getString("ck_send_kitchen_string", "");
        if (ck_send_kitchen_string.length() <= 0) goto L31;
        String[] sendStringItems = ck_send_kitchen_string.split("=");
        if (sendStringItems == null) goto L31;
        if (sendStringItems.length != 3) goto L31;
        if (sendStringItems[1] == null) goto L31;
        if (sendStringItems[2] == null) goto L31;
        int kitchenLevelShort = Integer.valueOf(sendStringItems[1]).intValue();
        if (kitchenLevelShort < 0) goto L31;
        if (kitchenLevelShort > 2) goto L31;
        tool_1_HashMap = new HashMap();
        tool_1_HashMap.put("-1", new StringBuilder().append(kitchenLevelShort).toString());
        String tool_1_String = sendStringItems[2];
        if (tool_1_String == null) goto L31;
        if (tool_1_String.length() <= 0) goto L31;
        String[] tool_1_StringItems = tool_1_String.split(",");
        if (tool_1_StringItems == null) goto L31;
        if (tool_1_StringItems.length != 6) goto L31;
        int i = 0;
    L30:
        if (i >= tool_1_StringItems.length) goto L31;
        if (tool_1_StringItems[i] == null) goto L31;
        if (tool_1_StringItems[i].length() <= 0) goto L31;
        String[] tool_1_StringItem = tool_1_StringItems[i].split("_");
        if (tool_1_StringItem == null) goto L54;
        if (tool_1_StringItem.length != 2) goto L54;
        int tool_1_idShort = Integer.valueOf(tool_1_StringItem[0]).intValue();
        if (tool_1_idShort < 0) goto L54;
        if (tool_1_idShort >= 6) goto L54;
        int tool_1_levelShort = Integer.valueOf(tool_1_StringItem[1]).intValue();
        if (tool_1_levelShort >= (-2)) goto L51;
    L52:
        tool_1_levelShort = -2;
    L53:
        tool_1_HashMap.put(new StringBuilder().append(tool_1_idShort).toString(), new StringBuilder().append(tool_1_levelShort).toString());
        goto L54
    L51:
        if (tool_1_levelShort <= 2) goto L53;
    L54:
        i = i + 1;
        goto L30
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

    public ArrayList<Short> getRateArrayWithID(short _eggID, short _tool1_ID, short _tool2_0_ID, short _tool2_1_ID) {
        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex(0);
        float totalChars0Cnt = BitmapDescriptorFactory.HUE_RED;
        float totalChars1Cnt = BitmapDescriptorFactory.HUE_RED;
        if (this.appDelegate.timeSaveDictionary != null) goto L5;
    L9:
        float totalCharsAllCnt = totalChars0Cnt + totalChars1Cnt;
        ArrayList<Short> allRateArrayList = new ArrayList();
        ArrayList<Short> returnRateArrayList = null;
        Log.d("MainGameLayout", "_tool1_ID:" + _tool1_ID);
        if (_eggID != 0) goto L806;
        if (_tool1_ID != 0) goto L275;
        short tool_1_level = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 0, allRateArrayList);
        if (totalChars0Cnt < 1000.0d) goto L16;
        addCharacterToAllRateArrayList(_eggID, 31, allRateArrayList);
    L16:
        if (totalChars0Cnt < 2000.0d) goto L19;
        addCharacterToAllRateArrayList(_eggID, 81, allRateArrayList);
    L19:
        if (this.appDelegate.defaultSharedPreferences == null) goto L24;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_48", false) == false) goto L24;
        Log.d("MainGameLayout", "campaign_char_0_48: YES");
        addCharacterToAllRateArrayList(_eggID, 48, allRateArrayList);
    L24:
        if (this.appDelegate.defaultSharedPreferences == null) goto L29;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_49", false) == false) goto L29;
        Log.d("MainGameLayout", "campaign_char_0_49: YES");
        addCharacterToAllRateArrayList(_eggID, 49, allRateArrayList);
    L29:
        if (this.appDelegate.defaultSharedPreferences == null) goto L34;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_60", false) == false) goto L34;
        Log.d("MainGameLayout", "campaign_char_0_60: YES");
        addCharacterToAllRateArrayList(_eggID, 60, allRateArrayList);
    L34:
        if (this.appDelegate.defaultSharedPreferences == null) goto L39;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_61", false) == false) goto L39;
        Log.d("MainGameLayout", "campaign_char_0_61: YES");
        addCharacterToAllRateArrayList(_eggID, 61, allRateArrayList);
    L39:
        if (this.appDelegate.defaultSharedPreferences == null) goto L44;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_62", false) == false) goto L44;
        Log.d("MainGameLayout", "campaign_char_0_62: YES");
        addCharacterToAllRateArrayList(_eggID, 62, allRateArrayList);
    L44:
        if (this.appDelegate.defaultSharedPreferences == null) goto L49;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_63", false) == false) goto L49;
        Log.d("MainGameLayout", "campaign_char_0_63: YES");
        addCharacterToAllRateArrayList(_eggID, 63, allRateArrayList);
    L49:
        if (this.appDelegate.defaultSharedPreferences == null) goto L54;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_64", false) == false) goto L54;
        Log.d("MainGameLayout", "campaign_char_0_64: YES");
        addCharacterToAllRateArrayList(_eggID, 64, allRateArrayList);
    L54:
        if (this.appDelegate.defaultSharedPreferences == null) goto L59;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_65", false) == false) goto L59;
        Log.d("MainGameLayout", "campaign_char_0_65: YES");
        addCharacterToAllRateArrayList(_eggID, 65, allRateArrayList);
    L59:
        if (this.appDelegate.defaultSharedPreferences == null) goto L64;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_66", false) == false) goto L64;
        Log.d("MainGameLayout", "campaign_char_0_66: YES");
        addCharacterToAllRateArrayList(_eggID, 66, allRateArrayList);
    L64:
        if (this.appDelegate.defaultSharedPreferences == null) goto L69;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_67", false) == false) goto L69;
        Log.d("MainGameLayout", "campaign_char_0_67: YES");
        addCharacterToAllRateArrayList(_eggID, 67, allRateArrayList);
    L69:
        if (this.appDelegate.defaultSharedPreferences == null) goto L74;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_78", false) == false) goto L74;
        Log.d("MainGameLayout", "campaign_char_0_78: YES");
        addCharacterToAllRateArrayList(_eggID, 78, allRateArrayList);
    L74:
        if (this.appDelegate.defaultSharedPreferences == null) goto L79;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_79", false) == false) goto L79;
        Log.d("MainGameLayout", "campaign_char_0_79: YES");
        addCharacterToAllRateArrayList(_eggID, 79, allRateArrayList);
    L79:
        if (this.appDelegate.defaultSharedPreferences == null) goto L84;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_80", false) == false) goto L84;
        Log.d("MainGameLayout", "campaign_char_0_80: YES");
        addCharacterToAllRateArrayList(_eggID, 80, allRateArrayList);
    L84:
        if (this.appDelegate.defaultSharedPreferences == null) goto L89;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_83", false) == false) goto L89;
        Log.d("MainGameLayout", "campaign_char_0_83: YES");
        addCharacterToAllRateArrayList(_eggID, 83, allRateArrayList);
    L89:
        if (this.appDelegate.defaultSharedPreferences == null) goto L94;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_84", false) == false) goto L94;
        Log.d("MainGameLayout", "campaign_char_0_84: YES");
        addCharacterToAllRateArrayList(_eggID, 84, allRateArrayList);
    L94:
        if (this.appDelegate.defaultSharedPreferences == null) goto L99;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_85", false) == false) goto L99;
        Log.d("MainGameLayout", "campaign_char_0_85: YES");
        addCharacterToAllRateArrayList(_eggID, 85, allRateArrayList);
    L99:
        if (this.appDelegate.defaultSharedPreferences == null) goto L104;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_86", false) == false) goto L104;
        Log.d("MainGameLayout", "campaign_char_0_86: YES");
        addCharacterToAllRateArrayList(_eggID, 86, allRateArrayList);
    L104:
        if (this.appDelegate.defaultSharedPreferences == null) goto L109;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_87", false) == false) goto L109;
        Log.d("MainGameLayout", "campaign_char_0_87: YES");
        addCharacterToAllRateArrayList(_eggID, 87, allRateArrayList);
    L109:
        if (this.appDelegate.defaultSharedPreferences == null) goto L114;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_88", false) == false) goto L114;
        Log.d("MainGameLayout", "campaign_char_0_88: YES");
        addCharacterToAllRateArrayList(_eggID, 88, allRateArrayList);
    L114:
        if (this.appDelegate.defaultSharedPreferences == null) goto L119;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_105", false) == false) goto L119;
        Log.d("MainGameLayout", "campaign_char_0_105: YES");
        addCharacterToAllRateArrayList(_eggID, 105, allRateArrayList);
    L119:
        if (this.tool_2_SelectView.tool_2_0 != 0) goto L121;
    L124:
        addCharacterToAllRateArrayList(_eggID, 5, allRateArrayList);
    L126:
        if (this.tool_2_SelectView.tool_2_0 != 11) goto L128;
    L131:
        addCharacterToAllRateArrayList(_eggID, 20, allRateArrayList);
    L133:
        if (this.tool_2_SelectView.tool_2_0 != 15) goto L135;
    L138:
        addCharacterToAllRateArrayList(_eggID, 25, allRateArrayList);
        if (this.appDelegate.defaultSharedPreferences == null) goto L144;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_26", false) == false) goto L144;
        Log.d("MainGameLayout", "campaign_char_0_26: YES");
        addCharacterToAllRateArrayList(_eggID, 26, allRateArrayList);
    L144:
        if (this.appDelegate.defaultSharedPreferences != null) goto L146;
    L149:
        if (this.tool_2_SelectView.tool_2_0 != 18) goto L151;
    L154:
        addCharacterToAllRateArrayList(_eggID, 30, allRateArrayList);
    L156:
        if (this.tool_2_SelectView.tool_2_0 != 36) goto L158;
    L161:
        addCharacterToAllRateArrayList(_eggID, 50, allRateArrayList);
    L163:
        if (tool_1_level < 1) goto L166;
        addCharacterToAllRateArrayList(_eggID, 33, allRateArrayList);
    L166:
        if (tool_1_level < 2) goto L178;
        short randIndex = (short) (Math.random() * 10.0d);
        if (randIndex != 0) goto L178;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        Date nowDate = new Date();
        int hourInt = Integer.parseInt(sdf.format(nowDate));
        if (hourInt != 10) goto L172;
    L175:
        addCharacterToAllRateArrayList(_eggID, 52, allRateArrayList);
        goto L178
    L172:
        if (hourInt == 11) goto L175;
        if (hourInt == 12) goto L175;
        addCharacterToAllRateArrayList(_eggID, 51, allRateArrayList);
    L178:
        if (tool_0_0_level < 3) goto L181;
        addCharacterToAllRateArrayList(_eggID, 108, allRateArrayList);
    L181:
        if (this.tool_2_SelectView.tool_2_0 != 35) goto L183;
    L186:
        addCharacterToAllRateArrayList(_eggID, 47, allRateArrayList);
    L187:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 68) goto L190;
    L193:
        short idShort = -1;
        if (this.appDelegate == null) goto L199;
        if (this.appDelegate.defaultSharedPreferences == null) goto L199;
        idShort = (short) this.appDelegate.defaultSharedPreferences.getInt("gift_tool_2_68_character_id", -1);
    L199:
        if (idShort >= 89) goto L201;
    L202:
        idShort = -1;
    L203:
        if (idShort >= 0) goto L205;
        short randNum = (short) (Math.random() * 100.0d);
        if (randNum >= 1) goto L238;
        idShort = 89;
    L209:
        returnRateArrayList.remove(0);
        short randInsertIndex = (short) (Math.random() * returnRateArrayList.size());
        returnRateArrayList.add(randInsertIndex, Short.valueOf(idShort));
    L211:
        return returnRateArrayList;
    L238:
        if (randNum >= 4) goto L241;
        idShort = 90;
        goto L209
    L241:
        if (randNum >= 10) goto L244;
        idShort = 91;
        goto L209
    L244:
        if (randNum >= 19) goto L247;
        idShort = 92;
        goto L209
    L247:
        if (randNum >= 28) goto L250;
        idShort = 93;
        goto L209
    L250:
        if (randNum >= 37) goto L253;
        idShort = 97;
        goto L209
    L253:
        if (randNum >= 46) goto L256;
        idShort = 94;
        goto L209
    L256:
        if (randNum >= 55) goto L259;
        idShort = 98;
        goto L209
    L259:
        if (randNum >= 64) goto L262;
        idShort = 99;
        goto L209
    L262:
        if (randNum >= 73) goto L265;
        idShort = 95;
        goto L209
    L265:
        if (randNum >= 82) goto L268;
        idShort = 100;
        goto L209
    L268:
        if (randNum >= 91) goto L271;
        idShort = 101;
        goto L209
    L271:
        if (randNum >= 97) goto L273;
        idShort = 102;
        goto L209
    L273:
        idShort = 96;
        goto L209
    L205:
        if (this.appDelegate == null) goto L209;
        if (this.appDelegate.defaultSharedPreferences == null) goto L209;
        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
        editor.putInt("gift_tool_2_68_egg_id", -1);
        editor.putInt("gift_tool_2_68_character_id", -1);
        editor.commit();
        goto L209
    L201:
        if (idShort <= 103) goto L203;
    L190:
        if (this.tool_2_SelectView.tool_2_1 == 68) goto L193;
        if (this.tool_2_SelectView.tool_2_2 != 68) goto L211;
    L183:
        if (this.tool_2_SelectView.tool_2_1 == 35) goto L186;
        if (this.tool_2_SelectView.tool_2_2 != 35) goto L187;
    L158:
        if (this.tool_2_SelectView.tool_2_1 == 36) goto L161;
        if (this.tool_2_SelectView.tool_2_2 != 36) goto L163;
    L151:
        if (this.tool_2_SelectView.tool_2_1 == 18) goto L154;
        if (this.tool_2_SelectView.tool_2_2 != 18) goto L156;
    L146:
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_27", false) == false) goto L149;
        Log.d("MainGameLayout", "campaign_char_0_27: YES");
        addCharacterToAllRateArrayList(_eggID, 27, allRateArrayList);
        goto L149
    L135:
        if (this.tool_2_SelectView.tool_2_1 == 15) goto L138;
        if (this.tool_2_SelectView.tool_2_2 != 15) goto L149;
    L128:
        if (this.tool_2_SelectView.tool_2_1 == 11) goto L131;
        if (this.tool_2_SelectView.tool_2_2 != 11) goto L133;
    L121:
        if (this.tool_2_SelectView.tool_2_1 == 0) goto L124;
        if (this.tool_2_SelectView.tool_2_2 != 0) goto L126;
    L275:
        if (_tool1_ID != 1) goto L347;
        short tool_1_level2 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 3, allRateArrayList);
        addCharacterToAllRateArrayList(_eggID, 4, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 1) goto L279;
    L282:
        addCharacterToAllRateArrayList(_eggID, 6, allRateArrayList);
    L284:
        if (this.tool_2_SelectView.tool_2_0 != 2) goto L286;
    L289:
        addCharacterToAllRateArrayList(_eggID, 7, allRateArrayList);
    L291:
        if (tool_1_level2 >= 1) goto L293;
    L339:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 69) goto L342;
    L345:
        returnRateArrayList.remove(0);
        short randInsertIndex2 = (short) (Math.random() * returnRateArrayList.size());
        returnRateArrayList.add(randInsertIndex2, (short) 106);
        goto L211
    L342:
        if (this.tool_2_SelectView.tool_2_1 == 69) goto L345;
        if (this.tool_2_SelectView.tool_2_2 != 69) goto L211;
    L293:
        if (this.tool_2_SelectView.tool_2_0 == 19) goto L299;
        if (this.tool_2_SelectView.tool_2_1 == 19) goto L299;
        if (this.tool_2_SelectView.tool_2_2 == 19) goto L299;
    L306:
        if (this.tool_2_SelectView.tool_2_0 == 30) goto L312;
        if (this.tool_2_SelectView.tool_2_1 == 30) goto L312;
        if (this.tool_2_SelectView.tool_2_2 == 30) goto L312;
    L319:
        if (tool_1_level2 < 2) goto L339;
        if (this.tool_2_SelectView.tool_2_0 == 25) goto L327;
        if (this.tool_2_SelectView.tool_2_1 == 25) goto L327;
        if (this.tool_2_SelectView.tool_2_2 != 25) goto L339;
    L327:
        if (this.tool_2_SelectView.tool_2_0 == 37) goto L333;
        if (this.tool_2_SelectView.tool_2_1 == 37) goto L333;
        if (this.tool_2_SelectView.tool_2_2 != 37) goto L339;
    L333:
        if (this.tool_2_SelectView.tool_2_0 != 38) goto L335;
    L338:
        addCharacterToAllRateArrayList(_eggID, 55, allRateArrayList);
        goto L339
    L335:
        if (this.tool_2_SelectView.tool_2_1 == 38) goto L338;
        if (this.tool_2_SelectView.tool_2_2 != 38) goto L339;
    L312:
        if (this.tool_2_SelectView.tool_2_0 != 31) goto L314;
    L317:
        addCharacterToAllRateArrayList(_eggID, 43, allRateArrayList);
        goto L319
    L314:
        if (this.tool_2_SelectView.tool_2_1 == 31) goto L317;
        if (this.tool_2_SelectView.tool_2_2 != 31) goto L319;
    L299:
        if (this.tool_2_SelectView.tool_2_0 != 20) goto L301;
    L304:
        addCharacterToAllRateArrayList(_eggID, 36, allRateArrayList);
        goto L306
    L301:
        if (this.tool_2_SelectView.tool_2_1 == 20) goto L304;
        if (this.tool_2_SelectView.tool_2_2 != 20) goto L306;
    L286:
        if (this.tool_2_SelectView.tool_2_1 == 2) goto L289;
        if (this.tool_2_SelectView.tool_2_2 != 2) goto L291;
    L279:
        if (this.tool_2_SelectView.tool_2_1 == 1) goto L282;
        if (this.tool_2_SelectView.tool_2_2 != 1) goto L284;
    L347:
        if (_tool1_ID != 2) goto L443;
        short tool_1_level3 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 8, allRateArrayList);
        addCharacterToAllRateArrayList(_eggID, 9, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 3) goto L351;
    L354:
        addCharacterToAllRateArrayList(_eggID, 10, allRateArrayList);
    L356:
        if (this.tool_2_SelectView.tool_2_0 != 4) goto L358;
    L361:
        addCharacterToAllRateArrayList(_eggID, 11, allRateArrayList);
    L363:
        if (this.tool_2_SelectView.tool_2_0 != 17) goto L365;
    L368:
        addCharacterToAllRateArrayList(_eggID, 29, allRateArrayList);
        if (this.appDelegate.defaultSharedPreferences != null) goto L371;
    L374:
        if (tool_1_level3 >= 1) goto L376;
    L435:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 70) goto L438;
    L441:
        returnRateArrayList.remove(0);
        short randInsertIndex3 = (short) (Math.random() * returnRateArrayList.size());
        returnRateArrayList.add(randInsertIndex3, (short) 107);
        goto L211
    L438:
        if (this.tool_2_SelectView.tool_2_1 == 70) goto L441;
        if (this.tool_2_SelectView.tool_2_2 != 70) goto L211;
    L376:
        if (this.tool_2_SelectView.tool_2_0 == 21) goto L382;
        if (this.tool_2_SelectView.tool_2_1 == 21) goto L382;
        if (this.tool_2_SelectView.tool_2_2 == 21) goto L382;
    L389:
        if (this.tool_2_SelectView.tool_2_0 == 20) goto L395;
        if (this.tool_2_SelectView.tool_2_1 == 20) goto L395;
        if (this.tool_2_SelectView.tool_2_2 == 20) goto L395;
    L402:
        if (this.tool_2_SelectView.tool_2_0 == 9) goto L408;
        if (this.tool_2_SelectView.tool_2_1 == 9) goto L408;
        if (this.tool_2_SelectView.tool_2_2 == 9) goto L408;
    L415:
        if (tool_1_level3 < 2) goto L435;
        if (this.tool_2_SelectView.tool_2_0 == 19) goto L423;
        if (this.tool_2_SelectView.tool_2_1 == 19) goto L423;
        if (this.tool_2_SelectView.tool_2_2 != 19) goto L435;
    L423:
        if (this.tool_2_SelectView.tool_2_0 == 39) goto L429;
        if (this.tool_2_SelectView.tool_2_1 == 39) goto L429;
        if (this.tool_2_SelectView.tool_2_2 != 39) goto L435;
    L429:
        if (this.tool_2_SelectView.tool_2_0 != 40) goto L431;
    L434:
        addCharacterToAllRateArrayList(_eggID, 56, allRateArrayList);
        goto L435
    L431:
        if (this.tool_2_SelectView.tool_2_1 == 40) goto L434;
        if (this.tool_2_SelectView.tool_2_2 != 40) goto L435;
    L408:
        if (this.tool_2_SelectView.tool_2_0 != 24) goto L410;
    L413:
        addCharacterToAllRateArrayList(_eggID, 39, allRateArrayList);
        goto L415
    L410:
        if (this.tool_2_SelectView.tool_2_1 == 24) goto L413;
        if (this.tool_2_SelectView.tool_2_2 != 24) goto L415;
    L395:
        if (this.tool_2_SelectView.tool_2_0 != 23) goto L397;
    L400:
        addCharacterToAllRateArrayList(_eggID, 38, allRateArrayList);
        goto L402
    L397:
        if (this.tool_2_SelectView.tool_2_1 == 23) goto L400;
        if (this.tool_2_SelectView.tool_2_2 != 23) goto L402;
    L382:
        if (this.tool_2_SelectView.tool_2_0 != 22) goto L384;
    L387:
        addCharacterToAllRateArrayList(_eggID, 37, allRateArrayList);
        goto L389
    L384:
        if (this.tool_2_SelectView.tool_2_1 == 22) goto L387;
        if (this.tool_2_SelectView.tool_2_2 != 22) goto L389;
    L371:
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_32", false) == false) goto L374;
        Log.d("MainGameLayout", "campaign_char_0_32: YES");
        addCharacterToAllRateArrayList(_eggID, 32, allRateArrayList);
        goto L374
    L365:
        if (this.tool_2_SelectView.tool_2_1 == 17) goto L368;
        if (this.tool_2_SelectView.tool_2_2 != 17) goto L374;
    L358:
        if (this.tool_2_SelectView.tool_2_1 == 4) goto L361;
        if (this.tool_2_SelectView.tool_2_2 != 4) goto L363;
    L351:
        if (this.tool_2_SelectView.tool_2_1 == 3) goto L354;
        if (this.tool_2_SelectView.tool_2_2 != 3) goto L356;
    L443:
        if (_tool1_ID != 3) goto L516;
        short tool_1_level4 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 12, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 5) goto L447;
    L450:
        addCharacterToAllRateArrayList(_eggID, 13, allRateArrayList);
    L452:
        if (this.tool_2_SelectView.tool_2_0 != 6) goto L454;
    L457:
        addCharacterToAllRateArrayList(_eggID, 14, allRateArrayList);
    L459:
        if (this.tool_2_SelectView.tool_2_0 != 7) goto L461;
    L464:
        addCharacterToAllRateArrayList(_eggID, 15, allRateArrayList);
    L466:
        if (tool_1_level4 >= 1) goto L468;
    L514:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L468:
        if (this.tool_2_SelectView.tool_2_0 == 14) goto L474;
        if (this.tool_2_SelectView.tool_2_1 == 14) goto L474;
        if (this.tool_2_SelectView.tool_2_2 == 14) goto L474;
    L481:
        if (this.tool_2_SelectView.tool_2_0 == 25) goto L487;
        if (this.tool_2_SelectView.tool_2_1 == 25) goto L487;
        if (this.tool_2_SelectView.tool_2_2 == 25) goto L487;
    L494:
        if (tool_1_level4 < 2) goto L514;
        if (this.tool_2_SelectView.tool_2_0 == 9) goto L502;
        if (this.tool_2_SelectView.tool_2_1 == 9) goto L502;
        if (this.tool_2_SelectView.tool_2_2 != 9) goto L514;
    L502:
        if (this.tool_2_SelectView.tool_2_0 == 30) goto L508;
        if (this.tool_2_SelectView.tool_2_1 == 30) goto L508;
        if (this.tool_2_SelectView.tool_2_2 != 30) goto L514;
    L508:
        if (this.tool_2_SelectView.tool_2_0 != 41) goto L510;
    L513:
        addCharacterToAllRateArrayList(_eggID, 57, allRateArrayList);
        goto L514
    L510:
        if (this.tool_2_SelectView.tool_2_1 == 41) goto L513;
        if (this.tool_2_SelectView.tool_2_2 != 41) goto L514;
    L487:
        if (this.tool_2_SelectView.tool_2_0 != 32) goto L489;
    L492:
        addCharacterToAllRateArrayList(_eggID, 44, allRateArrayList);
        goto L494
    L489:
        if (this.tool_2_SelectView.tool_2_1 == 32) goto L492;
        if (this.tool_2_SelectView.tool_2_2 != 32) goto L494;
    L474:
        if (this.tool_2_SelectView.tool_2_0 != 25) goto L476;
    L479:
        addCharacterToAllRateArrayList(_eggID, 40, allRateArrayList);
        goto L481
    L476:
        if (this.tool_2_SelectView.tool_2_1 == 25) goto L479;
        if (this.tool_2_SelectView.tool_2_2 != 25) goto L481;
    L461:
        if (this.tool_2_SelectView.tool_2_1 == 7) goto L464;
        if (this.tool_2_SelectView.tool_2_2 != 7) goto L466;
    L454:
        if (this.tool_2_SelectView.tool_2_1 == 6) goto L457;
        if (this.tool_2_SelectView.tool_2_2 != 6) goto L459;
    L447:
        if (this.tool_2_SelectView.tool_2_1 == 5) goto L450;
        if (this.tool_2_SelectView.tool_2_2 != 5) goto L452;
    L516:
        if (_tool1_ID != 4) goto L603;
        short tool_1_level5 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 16, allRateArrayList);
        if (this.appDelegate.defaultSharedPreferences == null) goto L525;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_0_82", false) == false) goto L525;
        if (totalChars0Cnt < 2000.0d) goto L525;
        addCharacterToAllRateArrayList(_eggID, 82, allRateArrayList);
    L525:
        if (this.tool_2_SelectView.tool_2_0 != 8) goto L527;
    L530:
        addCharacterToAllRateArrayList(_eggID, 17, allRateArrayList);
    L532:
        if (this.tool_2_SelectView.tool_2_0 != 9) goto L534;
    L537:
        addCharacterToAllRateArrayList(_eggID, 18, allRateArrayList);
    L539:
        if (this.tool_2_SelectView.tool_2_0 != 10) goto L541;
    L544:
        addCharacterToAllRateArrayList(_eggID, 19, allRateArrayList);
    L546:
        if (this.tool_2_SelectView.tool_2_0 != 16) goto L548;
    L551:
        addCharacterToAllRateArrayList(_eggID, 28, allRateArrayList);
    L553:
        if (tool_1_level5 >= 1) goto L555;
    L601:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L555:
        if (this.tool_2_SelectView.tool_2_0 == 26) goto L561;
        if (this.tool_2_SelectView.tool_2_1 == 26) goto L561;
        if (this.tool_2_SelectView.tool_2_2 == 26) goto L561;
    L568:
        if (this.tool_2_SelectView.tool_2_0 == 23) goto L574;
        if (this.tool_2_SelectView.tool_2_1 == 23) goto L574;
        if (this.tool_2_SelectView.tool_2_2 == 23) goto L574;
    L581:
        if (tool_1_level5 < 2) goto L601;
        if (this.tool_2_SelectView.tool_2_0 == 27) goto L589;
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L589;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L601;
    L589:
        if (this.tool_2_SelectView.tool_2_0 == 41) goto L595;
        if (this.tool_2_SelectView.tool_2_1 == 41) goto L595;
        if (this.tool_2_SelectView.tool_2_2 != 41) goto L601;
    L595:
        if (this.tool_2_SelectView.tool_2_0 != 42) goto L597;
    L600:
        addCharacterToAllRateArrayList(_eggID, 58, allRateArrayList);
        goto L601
    L597:
        if (this.tool_2_SelectView.tool_2_1 == 42) goto L600;
        if (this.tool_2_SelectView.tool_2_2 != 42) goto L601;
    L574:
        if (this.tool_2_SelectView.tool_2_0 != 33) goto L576;
    L579:
        addCharacterToAllRateArrayList(_eggID, 45, allRateArrayList);
        goto L581
    L576:
        if (this.tool_2_SelectView.tool_2_1 == 33) goto L579;
        if (this.tool_2_SelectView.tool_2_2 != 33) goto L581;
    L561:
        if (this.tool_2_SelectView.tool_2_0 != 27) goto L563;
    L566:
        addCharacterToAllRateArrayList(_eggID, 41, allRateArrayList);
        goto L568
    L563:
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L566;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L568;
    L548:
        if (this.tool_2_SelectView.tool_2_1 == 16) goto L551;
        if (this.tool_2_SelectView.tool_2_2 != 16) goto L553;
    L541:
        if (this.tool_2_SelectView.tool_2_1 == 10) goto L544;
        if (this.tool_2_SelectView.tool_2_2 != 10) goto L546;
    L534:
        if (this.tool_2_SelectView.tool_2_1 == 9) goto L537;
        if (this.tool_2_SelectView.tool_2_2 != 9) goto L539;
    L527:
        if (this.tool_2_SelectView.tool_2_1 == 8) goto L530;
        if (this.tool_2_SelectView.tool_2_2 != 8) goto L532;
    L603:
        if (_tool1_ID != 5) goto L676;
        short tool_1_level6 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 21, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 12) goto L607;
    L610:
        addCharacterToAllRateArrayList(_eggID, 22, allRateArrayList);
    L612:
        if (this.tool_2_SelectView.tool_2_0 != 13) goto L614;
    L617:
        addCharacterToAllRateArrayList(_eggID, 23, allRateArrayList);
    L619:
        if (this.tool_2_SelectView.tool_2_0 != 14) goto L621;
    L624:
        addCharacterToAllRateArrayList(_eggID, 24, allRateArrayList);
    L626:
        if (tool_1_level6 >= 1) goto L628;
    L674:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L628:
        if (this.tool_2_SelectView.tool_2_0 == 28) goto L634;
        if (this.tool_2_SelectView.tool_2_1 == 28) goto L634;
        if (this.tool_2_SelectView.tool_2_2 == 28) goto L634;
    L641:
        if (this.tool_2_SelectView.tool_2_0 == 23) goto L647;
        if (this.tool_2_SelectView.tool_2_1 == 23) goto L647;
        if (this.tool_2_SelectView.tool_2_2 == 23) goto L647;
    L654:
        if (tool_1_level6 < 2) goto L674;
        if (this.tool_2_SelectView.tool_2_0 == 22) goto L662;
        if (this.tool_2_SelectView.tool_2_1 == 22) goto L662;
        if (this.tool_2_SelectView.tool_2_2 != 22) goto L674;
    L662:
        if (this.tool_2_SelectView.tool_2_0 == 24) goto L668;
        if (this.tool_2_SelectView.tool_2_1 == 24) goto L668;
        if (this.tool_2_SelectView.tool_2_2 != 24) goto L674;
    L668:
        if (this.tool_2_SelectView.tool_2_0 != 43) goto L670;
    L673:
        addCharacterToAllRateArrayList(_eggID, 59, allRateArrayList);
        goto L674
    L670:
        if (this.tool_2_SelectView.tool_2_1 == 43) goto L673;
        if (this.tool_2_SelectView.tool_2_2 != 43) goto L674;
    L647:
        if (this.tool_2_SelectView.tool_2_0 != 34) goto L649;
    L652:
        addCharacterToAllRateArrayList(_eggID, 46, allRateArrayList);
        goto L654
    L649:
        if (this.tool_2_SelectView.tool_2_1 == 34) goto L652;
        if (this.tool_2_SelectView.tool_2_2 != 34) goto L654;
    L634:
        if (this.tool_2_SelectView.tool_2_0 != 29) goto L636;
    L639:
        addCharacterToAllRateArrayList(_eggID, 42, allRateArrayList);
        goto L641
    L636:
        if (this.tool_2_SelectView.tool_2_1 == 29) goto L639;
        if (this.tool_2_SelectView.tool_2_2 != 29) goto L641;
    L621:
        if (this.tool_2_SelectView.tool_2_1 == 14) goto L624;
        if (this.tool_2_SelectView.tool_2_2 != 14) goto L626;
    L614:
        if (this.tool_2_SelectView.tool_2_1 == 13) goto L617;
        if (this.tool_2_SelectView.tool_2_2 != 13) goto L619;
    L607:
        if (this.tool_2_SelectView.tool_2_1 == 12) goto L610;
        if (this.tool_2_SelectView.tool_2_2 != 12) goto L612;
    L676:
        if (_tool1_ID != 6) goto L760;
        short tool_1_level7 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 69, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 56) goto L680;
    L683:
        addCharacterToAllRateArrayList(_eggID, 71, allRateArrayList);
        addCharacterToAllRateArrayList(_eggID, 72, allRateArrayList);
    L685:
        if (tool_1_level7 >= 1) goto L687;
    L758:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L687:
        if (this.tool_2_SelectView.tool_2_0 == 56) goto L693;
        if (this.tool_2_SelectView.tool_2_1 == 56) goto L693;
        if (this.tool_2_SelectView.tool_2_2 == 56) goto L693;
    L700:
        if (tool_1_level7 < 2) goto L758;
        if (this.tool_2_SelectView.tool_2_0 == 56) goto L708;
        if (this.tool_2_SelectView.tool_2_1 == 56) goto L708;
        if (this.tool_2_SelectView.tool_2_2 == 56) goto L708;
    L721:
        if (this.tool_2_SelectView.tool_2_0 == 56) goto L727;
        if (this.tool_2_SelectView.tool_2_1 == 56) goto L727;
        if (this.tool_2_SelectView.tool_2_2 == 56) goto L727;
    L740:
        if (this.tool_2_SelectView.tool_2_0 == 56) goto L746;
        if (this.tool_2_SelectView.tool_2_1 == 56) goto L746;
        if (this.tool_2_SelectView.tool_2_2 != 56) goto L758;
    L746:
        if (this.tool_2_SelectView.tool_2_0 == 61) goto L752;
        if (this.tool_2_SelectView.tool_2_1 == 61) goto L752;
        if (this.tool_2_SelectView.tool_2_2 != 61) goto L758;
    L752:
        if (this.tool_2_SelectView.tool_2_0 != 62) goto L754;
    L757:
        addCharacterToAllRateArrayList(_eggID, 77, allRateArrayList);
        goto L758
    L754:
        if (this.tool_2_SelectView.tool_2_1 == 62) goto L757;
        if (this.tool_2_SelectView.tool_2_2 != 62) goto L758;
    L727:
        if (this.tool_2_SelectView.tool_2_0 == 59) goto L733;
        if (this.tool_2_SelectView.tool_2_1 == 59) goto L733;
        if (this.tool_2_SelectView.tool_2_2 != 59) goto L740;
    L733:
        if (this.tool_2_SelectView.tool_2_0 != 60) goto L735;
    L738:
        addCharacterToAllRateArrayList(_eggID, 76, allRateArrayList);
        goto L740
    L735:
        if (this.tool_2_SelectView.tool_2_1 == 60) goto L738;
        if (this.tool_2_SelectView.tool_2_2 != 60) goto L740;
    L708:
        if (this.tool_2_SelectView.tool_2_0 == 57) goto L714;
        if (this.tool_2_SelectView.tool_2_1 == 57) goto L714;
        if (this.tool_2_SelectView.tool_2_2 != 57) goto L721;
    L714:
        if (this.tool_2_SelectView.tool_2_0 != 58) goto L716;
    L719:
        addCharacterToAllRateArrayList(_eggID, 75, allRateArrayList);
        goto L721
    L716:
        if (this.tool_2_SelectView.tool_2_1 == 58) goto L719;
        if (this.tool_2_SelectView.tool_2_2 != 58) goto L721;
    L693:
        if (this.tool_2_SelectView.tool_2_0 != 57) goto L695;
    L698:
        addCharacterToAllRateArrayList(_eggID, 73, allRateArrayList);
        addCharacterToAllRateArrayList(_eggID, 74, allRateArrayList);
        goto L700
    L695:
        if (this.tool_2_SelectView.tool_2_1 == 57) goto L698;
        if (this.tool_2_SelectView.tool_2_2 != 57) goto L700;
    L680:
        if (this.tool_2_SelectView.tool_2_1 == 56) goto L683;
        if (this.tool_2_SelectView.tool_2_2 != 56) goto L685;
    L760:
        if (_tool1_ID != 7) goto L211;
        short tool_1_level8 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 109, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 37) goto L764;
    L767:
        addCharacterToAllRateArrayList(_eggID, 111, allRateArrayList);
    L769:
        if (tool_1_level8 >= 1) goto L771;
    L804:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L771:
        if (this.tool_2_SelectView.tool_2_0 == 71) goto L777;
        if (this.tool_2_SelectView.tool_2_1 == 71) goto L777;
        if (this.tool_2_SelectView.tool_2_2 == 71) goto L777;
    L784:
        if (tool_1_level8 < 2) goto L804;
        if (this.tool_2_SelectView.tool_2_0 == 4) goto L792;
        if (this.tool_2_SelectView.tool_2_1 == 4) goto L792;
        if (this.tool_2_SelectView.tool_2_2 != 4) goto L804;
    L792:
        if (this.tool_2_SelectView.tool_2_0 == 22) goto L798;
        if (this.tool_2_SelectView.tool_2_1 == 22) goto L798;
        if (this.tool_2_SelectView.tool_2_2 != 22) goto L804;
    L798:
        if (this.tool_2_SelectView.tool_2_0 != 45) goto L800;
    L803:
        addCharacterToAllRateArrayList(_eggID, 113, allRateArrayList);
        goto L804
    L800:
        if (this.tool_2_SelectView.tool_2_1 == 45) goto L803;
        if (this.tool_2_SelectView.tool_2_2 != 45) goto L804;
    L777:
        if (this.tool_2_SelectView.tool_2_0 != 72) goto L779;
    L782:
        addCharacterToAllRateArrayList(_eggID, 112, allRateArrayList);
        goto L784
    L779:
        if (this.tool_2_SelectView.tool_2_1 == 72) goto L782;
        if (this.tool_2_SelectView.tool_2_2 != 72) goto L784;
    L764:
        if (this.tool_2_SelectView.tool_2_1 == 37) goto L767;
        if (this.tool_2_SelectView.tool_2_2 != 37) goto L769;
    L806:
        if (_eggID != 1) goto L211;
        if (_tool1_ID != 0) goto L874;
        short tool_1_level9 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 0, allRateArrayList);
        if (totalChars1Cnt < 1000.0d) goto L812;
        addCharacterToAllRateArrayList(_eggID, 16, allRateArrayList);
    L812:
        if (this.appDelegate.defaultSharedPreferences == null) goto L817;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_35", false) == false) goto L817;
        Log.d("MainGameLayout", "campaign_char_1_35: YES");
        addCharacterToAllRateArrayList(_eggID, 35, allRateArrayList);
    L817:
        if (this.appDelegate.defaultSharedPreferences == null) goto L822;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_47", false) == false) goto L822;
        Log.d("MainGameLayout", "campaign_char_1_47: YES");
        addCharacterToAllRateArrayList(_eggID, 47, allRateArrayList);
    L822:
        if (this.appDelegate.defaultSharedPreferences == null) goto L827;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_48", false) == false) goto L827;
        Log.d("MainGameLayout", "campaign_char_1_48: YES");
        addCharacterToAllRateArrayList(_eggID, 48, allRateArrayList);
    L827:
        if (this.appDelegate.defaultSharedPreferences == null) goto L832;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_49", false) == false) goto L832;
        Log.d("MainGameLayout", "campaign_char_1_49: YES");
        addCharacterToAllRateArrayList(_eggID, 49, allRateArrayList);
    L832:
        if (this.appDelegate.defaultSharedPreferences == null) goto L837;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_51", false) == false) goto L837;
        Log.d("MainGameLayout", "campaign_char_1_51: YES");
        addCharacterToAllRateArrayList(_eggID, 51, allRateArrayList);
    L837:
        if (this.tool_2_SelectView.tool_2_0 != 27) goto L839;
    L842:
        addCharacterToAllRateArrayList(_eggID, 4, allRateArrayList);
    L844:
        if (this.tool_2_SelectView.tool_2_0 != 11) goto L846;
    L849:
        addCharacterToAllRateArrayList(_eggID, 14, allRateArrayList);
    L851:
        if (this.tool_2_SelectView.tool_2_0 != 18) goto L853;
    L856:
        addCharacterToAllRateArrayList(_eggID, 15, allRateArrayList);
    L858:
        if (this.tool_2_SelectView.tool_2_0 != 36) goto L860;
    L863:
        addCharacterToAllRateArrayList(_eggID, 26, allRateArrayList);
    L865:
        if (tool_1_level9 < 1) goto L868;
        addCharacterToAllRateArrayList(_eggID, 17, allRateArrayList);
        addCharacterToAllRateArrayList(_eggID, 18, allRateArrayList);
    L868:
        if (tool_1_level9 < 2) goto L872;
        short randIndex2 = (short) (Math.random() * 8.0d);
        if (randIndex2 != 0) goto L872;
        addCharacterToAllRateArrayList(_eggID, 27, allRateArrayList);
    L872:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L860:
        if (this.tool_2_SelectView.tool_2_1 == 36) goto L863;
        if (this.tool_2_SelectView.tool_2_2 != 36) goto L865;
    L853:
        if (this.tool_2_SelectView.tool_2_1 == 18) goto L856;
        if (this.tool_2_SelectView.tool_2_2 != 18) goto L858;
    L846:
        if (this.tool_2_SelectView.tool_2_1 == 11) goto L849;
        if (this.tool_2_SelectView.tool_2_2 != 11) goto L851;
    L839:
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L842;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L844;
    L874:
        if (_tool1_ID != 1) goto L920;
        short tool_1_level10 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 3, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 44) goto L878;
    L881:
        addCharacterToAllRateArrayList(_eggID, 5, allRateArrayList);
    L883:
        if (tool_1_level10 >= 1) goto L885;
    L918:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L885:
        if (this.tool_2_SelectView.tool_2_0 == 23) goto L891;
        if (this.tool_2_SelectView.tool_2_1 == 23) goto L891;
        if (this.tool_2_SelectView.tool_2_2 == 23) goto L891;
    L898:
        if (tool_1_level10 < 2) goto L918;
        if (this.tool_2_SelectView.tool_2_0 == 34) goto L906;
        if (this.tool_2_SelectView.tool_2_1 == 34) goto L906;
        if (this.tool_2_SelectView.tool_2_2 != 34) goto L918;
    L906:
        if (this.tool_2_SelectView.tool_2_0 == 41) goto L912;
        if (this.tool_2_SelectView.tool_2_1 == 41) goto L912;
        if (this.tool_2_SelectView.tool_2_2 != 41) goto L918;
    L912:
        if (this.tool_2_SelectView.tool_2_0 != 50) goto L914;
    L917:
        addCharacterToAllRateArrayList(_eggID, 30, allRateArrayList);
        goto L918
    L914:
        if (this.tool_2_SelectView.tool_2_1 == 50) goto L917;
        if (this.tool_2_SelectView.tool_2_2 != 50) goto L918;
    L891:
        if (this.tool_2_SelectView.tool_2_0 != 46) goto L893;
    L896:
        addCharacterToAllRateArrayList(_eggID, 21, allRateArrayList);
        goto L898
    L893:
        if (this.tool_2_SelectView.tool_2_1 == 46) goto L896;
        if (this.tool_2_SelectView.tool_2_2 != 46) goto L898;
    L878:
        if (this.tool_2_SelectView.tool_2_1 == 44) goto L881;
        if (this.tool_2_SelectView.tool_2_2 != 44) goto L883;
    L920:
        if (_tool1_ID != 2) goto L966;
        short tool_1_level11 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 6, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 27) goto L924;
    L927:
        addCharacterToAllRateArrayList(_eggID, 7, allRateArrayList);
    L929:
        if (tool_1_level11 >= 1) goto L931;
    L964:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L931:
        if (this.tool_2_SelectView.tool_2_0 == 47) goto L937;
        if (this.tool_2_SelectView.tool_2_1 == 47) goto L937;
        if (this.tool_2_SelectView.tool_2_2 == 47) goto L937;
    L944:
        if (tool_1_level11 < 2) goto L964;
        if (this.tool_2_SelectView.tool_2_0 == 46) goto L952;
        if (this.tool_2_SelectView.tool_2_1 == 46) goto L952;
        if (this.tool_2_SelectView.tool_2_2 != 46) goto L964;
    L952:
        if (this.tool_2_SelectView.tool_2_0 == 51) goto L958;
        if (this.tool_2_SelectView.tool_2_1 == 51) goto L958;
        if (this.tool_2_SelectView.tool_2_2 != 51) goto L964;
    L958:
        if (this.tool_2_SelectView.tool_2_0 != 52) goto L960;
    L963:
        addCharacterToAllRateArrayList(_eggID, 31, allRateArrayList);
        goto L964
    L960:
        if (this.tool_2_SelectView.tool_2_1 == 52) goto L963;
        if (this.tool_2_SelectView.tool_2_2 != 52) goto L964;
    L937:
        if (this.tool_2_SelectView.tool_2_0 != 48) goto L939;
    L942:
        addCharacterToAllRateArrayList(_eggID, 22, allRateArrayList);
        goto L944
    L939:
        if (this.tool_2_SelectView.tool_2_1 == 48) goto L942;
        if (this.tool_2_SelectView.tool_2_2 != 48) goto L944;
    L924:
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L927;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L929;
    L966:
        if (_tool1_ID != 3) goto L1012;
        short tool_1_level12 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 8, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 45) goto L970;
    L973:
        addCharacterToAllRateArrayList(_eggID, 9, allRateArrayList);
    L975:
        if (tool_1_level12 >= 1) goto L977;
    L1010:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L977:
        if (this.tool_2_SelectView.tool_2_0 == 25) goto L983;
        if (this.tool_2_SelectView.tool_2_1 == 25) goto L983;
        if (this.tool_2_SelectView.tool_2_2 == 25) goto L983;
    L990:
        if (tool_1_level12 < 2) goto L1010;
        if (this.tool_2_SelectView.tool_2_0 == 29) goto L998;
        if (this.tool_2_SelectView.tool_2_1 == 29) goto L998;
        if (this.tool_2_SelectView.tool_2_2 != 29) goto L1010;
    L998:
        if (this.tool_2_SelectView.tool_2_0 == 46) goto L1004;
        if (this.tool_2_SelectView.tool_2_1 == 46) goto L1004;
        if (this.tool_2_SelectView.tool_2_2 != 46) goto L1010;
    L1004:
        if (this.tool_2_SelectView.tool_2_0 != 53) goto L1006;
    L1009:
        addCharacterToAllRateArrayList(_eggID, 32, allRateArrayList);
        goto L1010
    L1006:
        if (this.tool_2_SelectView.tool_2_1 == 53) goto L1009;
        if (this.tool_2_SelectView.tool_2_2 != 53) goto L1010;
    L983:
        if (this.tool_2_SelectView.tool_2_0 != 32) goto L985;
    L988:
        addCharacterToAllRateArrayList(_eggID, 23, allRateArrayList);
        goto L990
    L985:
        if (this.tool_2_SelectView.tool_2_1 == 32) goto L988;
        if (this.tool_2_SelectView.tool_2_2 != 32) goto L990;
    L970:
        if (this.tool_2_SelectView.tool_2_1 == 45) goto L973;
        if (this.tool_2_SelectView.tool_2_2 != 45) goto L975;
    L1012:
        if (_tool1_ID != 4) goto L1075;
        short tool_1_level13 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 10, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 33) goto L1016;
    L1019:
        addCharacterToAllRateArrayList(_eggID, 11, allRateArrayList);
    L1021:
        if (tool_1_level13 >= 1) goto L1023;
    L1073:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L1023:
        if (this.tool_2_SelectView.tool_2_0 == 26) goto L1029;
        if (this.tool_2_SelectView.tool_2_1 == 26) goto L1029;
        if (this.tool_2_SelectView.tool_2_2 == 26) goto L1029;
    L1036:
        if (this.appDelegate.defaultSharedPreferences == null) goto L1053;
        if (this.appDelegate.defaultSharedPreferences.getBoolean("campaign_char_1_50", false) == false) goto L1053;
        if (this.tool_2_SelectView.tool_2_0 == 9) goto L1046;
        if (this.tool_2_SelectView.tool_2_1 == 9) goto L1046;
        if (this.tool_2_SelectView.tool_2_2 != 9) goto L1053;
    L1046:
        if (this.tool_2_SelectView.tool_2_0 != 29) goto L1048;
    L1051:
        addCharacterToAllRateArrayList(_eggID, 50, allRateArrayList);
        goto L1053
    L1048:
        if (this.tool_2_SelectView.tool_2_1 == 29) goto L1051;
        if (this.tool_2_SelectView.tool_2_2 == 29) goto L1051;
    L1053:
        if (tool_1_level13 < 2) goto L1073;
        if (this.tool_2_SelectView.tool_2_0 == 46) goto L1061;
        if (this.tool_2_SelectView.tool_2_1 == 46) goto L1061;
        if (this.tool_2_SelectView.tool_2_2 != 46) goto L1073;
    L1061:
        if (this.tool_2_SelectView.tool_2_0 == 54) goto L1067;
        if (this.tool_2_SelectView.tool_2_1 == 54) goto L1067;
        if (this.tool_2_SelectView.tool_2_2 != 54) goto L1073;
    L1067:
        if (this.tool_2_SelectView.tool_2_0 != 55) goto L1069;
    L1072:
        addCharacterToAllRateArrayList(_eggID, 33, allRateArrayList);
        goto L1073
    L1069:
        if (this.tool_2_SelectView.tool_2_1 == 55) goto L1072;
        if (this.tool_2_SelectView.tool_2_2 != 55) goto L1073;
    L1029:
        if (this.tool_2_SelectView.tool_2_0 != 27) goto L1031;
    L1034:
        addCharacterToAllRateArrayList(_eggID, 24, allRateArrayList);
        goto L1036
    L1031:
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L1034;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L1036;
    L1016:
        if (this.tool_2_SelectView.tool_2_1 == 33) goto L1019;
        if (this.tool_2_SelectView.tool_2_2 != 33) goto L1021;
    L1075:
        if (_tool1_ID != 5) goto L1121;
        short tool_1_level14 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 12, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 27) goto L1079;
    L1082:
        addCharacterToAllRateArrayList(_eggID, 13, allRateArrayList);
    L1084:
        if (tool_1_level14 >= 1) goto L1086;
    L1119:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L1086:
        if (this.tool_2_SelectView.tool_2_0 == 45) goto L1092;
        if (this.tool_2_SelectView.tool_2_1 == 45) goto L1092;
        if (this.tool_2_SelectView.tool_2_2 == 45) goto L1092;
    L1099:
        if (tool_1_level14 < 2) goto L1119;
        if (this.tool_2_SelectView.tool_2_0 == 12) goto L1107;
        if (this.tool_2_SelectView.tool_2_1 == 12) goto L1107;
        if (this.tool_2_SelectView.tool_2_2 != 12) goto L1119;
    L1107:
        if (this.tool_2_SelectView.tool_2_0 == 28) goto L1113;
        if (this.tool_2_SelectView.tool_2_1 == 28) goto L1113;
        if (this.tool_2_SelectView.tool_2_2 != 28) goto L1119;
    L1113:
        if (this.tool_2_SelectView.tool_2_0 != 29) goto L1115;
    L1118:
        addCharacterToAllRateArrayList(_eggID, 34, allRateArrayList);
        goto L1119
    L1115:
        if (this.tool_2_SelectView.tool_2_1 == 29) goto L1118;
        if (this.tool_2_SelectView.tool_2_2 != 29) goto L1119;
    L1092:
        if (this.tool_2_SelectView.tool_2_0 != 49) goto L1094;
    L1097:
        addCharacterToAllRateArrayList(_eggID, 25, allRateArrayList);
        goto L1099
    L1094:
        if (this.tool_2_SelectView.tool_2_1 == 49) goto L1097;
        if (this.tool_2_SelectView.tool_2_2 != 49) goto L1099;
    L1079:
        if (this.tool_2_SelectView.tool_2_1 == 27) goto L1082;
        if (this.tool_2_SelectView.tool_2_2 != 27) goto L1084;
    L1121:
        if (_tool1_ID != 6) goto L1226;
        short tool_1_level15 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 37, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 63) goto L1125;
    L1128:
        addCharacterToAllRateArrayList(_eggID, 39, allRateArrayList);
    L1130:
        if (this.tool_2_SelectView.tool_2_0 != 64) goto L1132;
    L1135:
        addCharacterToAllRateArrayList(_eggID, 40, allRateArrayList);
    L1137:
        if (this.tool_2_SelectView.tool_2_0 != 3) goto L1139;
    L1142:
        addCharacterToAllRateArrayList(_eggID, 41, allRateArrayList);
    L1144:
        if (tool_1_level15 >= 1) goto L1146;
    L1224:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L1146:
        if (this.tool_2_SelectView.tool_2_0 == 1) goto L1152;
        if (this.tool_2_SelectView.tool_2_1 == 1) goto L1152;
        if (this.tool_2_SelectView.tool_2_2 == 1) goto L1152;
    L1159:
        if (this.tool_2_SelectView.tool_2_0 == 57) goto L1165;
        if (this.tool_2_SelectView.tool_2_1 == 57) goto L1165;
        if (this.tool_2_SelectView.tool_2_2 == 57) goto L1165;
    L1172:
        if (this.tool_2_SelectView.tool_2_0 == 65) goto L1178;
        if (this.tool_2_SelectView.tool_2_1 == 65) goto L1178;
        if (this.tool_2_SelectView.tool_2_2 == 65) goto L1178;
    L1185:
        if (tool_1_level15 < 2) goto L1224;
        if (this.tool_2_SelectView.tool_2_0 == 59) goto L1193;
        if (this.tool_2_SelectView.tool_2_1 == 59) goto L1193;
        if (this.tool_2_SelectView.tool_2_2 == 59) goto L1193;
    L1206:
        if (this.tool_2_SelectView.tool_2_0 == 29) goto L1212;
        if (this.tool_2_SelectView.tool_2_1 == 29) goto L1212;
        if (this.tool_2_SelectView.tool_2_2 != 29) goto L1224;
    L1212:
        if (this.tool_2_SelectView.tool_2_0 == 57) goto L1218;
        if (this.tool_2_SelectView.tool_2_1 == 57) goto L1218;
        if (this.tool_2_SelectView.tool_2_2 != 57) goto L1224;
    L1218:
        if (this.tool_2_SelectView.tool_2_0 != 63) goto L1220;
    L1223:
        addCharacterToAllRateArrayList(_eggID, 46, allRateArrayList);
        goto L1224
    L1220:
        if (this.tool_2_SelectView.tool_2_1 == 63) goto L1223;
        if (this.tool_2_SelectView.tool_2_2 != 63) goto L1224;
    L1193:
        if (this.tool_2_SelectView.tool_2_0 == 60) goto L1199;
        if (this.tool_2_SelectView.tool_2_1 == 60) goto L1199;
        if (this.tool_2_SelectView.tool_2_2 != 60) goto L1206;
    L1199:
        if (this.tool_2_SelectView.tool_2_0 != 67) goto L1201;
    L1204:
        addCharacterToAllRateArrayList(_eggID, 45, allRateArrayList);
        goto L1206
    L1201:
        if (this.tool_2_SelectView.tool_2_1 == 67) goto L1204;
        if (this.tool_2_SelectView.tool_2_2 != 67) goto L1206;
    L1178:
        if (this.tool_2_SelectView.tool_2_0 != 66) goto L1180;
    L1183:
        addCharacterToAllRateArrayList(_eggID, 44, allRateArrayList);
        goto L1185
    L1180:
        if (this.tool_2_SelectView.tool_2_1 == 66) goto L1183;
        if (this.tool_2_SelectView.tool_2_2 != 66) goto L1185;
    L1165:
        if (this.tool_2_SelectView.tool_2_0 != 63) goto L1167;
    L1170:
        addCharacterToAllRateArrayList(_eggID, 43, allRateArrayList);
        goto L1172
    L1167:
        if (this.tool_2_SelectView.tool_2_1 == 63) goto L1170;
        if (this.tool_2_SelectView.tool_2_2 != 63) goto L1172;
    L1152:
        if (this.tool_2_SelectView.tool_2_0 != 63) goto L1154;
    L1157:
        addCharacterToAllRateArrayList(_eggID, 42, allRateArrayList);
        goto L1159
    L1154:
        if (this.tool_2_SelectView.tool_2_1 == 63) goto L1157;
        if (this.tool_2_SelectView.tool_2_2 != 63) goto L1159;
    L1139:
        if (this.tool_2_SelectView.tool_2_1 == 3) goto L1142;
        if (this.tool_2_SelectView.tool_2_2 != 3) goto L1144;
    L1132:
        if (this.tool_2_SelectView.tool_2_1 == 64) goto L1135;
        if (this.tool_2_SelectView.tool_2_2 != 64) goto L1137;
    L1125:
        if (this.tool_2_SelectView.tool_2_1 == 63) goto L1128;
        if (this.tool_2_SelectView.tool_2_2 != 63) goto L1130;
    L1226:
        if (_tool1_ID != 7) goto L211;
        short tool_1_level16 = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        addCharacterToAllRateArrayList(_eggID, 52, allRateArrayList);
        if (this.tool_2_SelectView.tool_2_0 != 57) goto L1230;
    L1233:
        addCharacterToAllRateArrayList(_eggID, 54, allRateArrayList);
    L1235:
        if (tool_1_level16 >= 1) goto L1237;
    L1270:
        returnRateArrayList = getFinalRateArrayWithAllRateArray(allRateArrayList);
        goto L211
    L1237:
        if (this.tool_2_SelectView.tool_2_0 == 73) goto L1243;
        if (this.tool_2_SelectView.tool_2_1 == 73) goto L1243;
        if (this.tool_2_SelectView.tool_2_2 == 73) goto L1243;
    L1250:
        if (tool_1_level16 < 2) goto L1270;
        if (this.tool_2_SelectView.tool_2_0 == 23) goto L1258;
        if (this.tool_2_SelectView.tool_2_1 == 23) goto L1258;
        if (this.tool_2_SelectView.tool_2_2 != 23) goto L1270;
    L1258:
        if (this.tool_2_SelectView.tool_2_0 == 24) goto L1264;
        if (this.tool_2_SelectView.tool_2_1 == 24) goto L1264;
        if (this.tool_2_SelectView.tool_2_2 != 24) goto L1270;
    L1264:
        if (this.tool_2_SelectView.tool_2_0 != 47) goto L1266;
    L1269:
        addCharacterToAllRateArrayList(_eggID, 56, allRateArrayList);
        goto L1270
    L1266:
        if (this.tool_2_SelectView.tool_2_1 == 47) goto L1269;
        if (this.tool_2_SelectView.tool_2_2 != 47) goto L1270;
    L1243:
        if (this.tool_2_SelectView.tool_2_0 != 74) goto L1245;
    L1248:
        addCharacterToAllRateArrayList(_eggID, 55, allRateArrayList);
        goto L1250
    L1245:
        if (this.tool_2_SelectView.tool_2_1 == 74) goto L1248;
        if (this.tool_2_SelectView.tool_2_2 != 74) goto L1250;
    L1230:
        if (this.tool_2_SelectView.tool_2_1 == 57) goto L1233;
        if (this.tool_2_SelectView.tool_2_2 != 57) goto L1235;
    L5:
        if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList == null) goto L9;
        int i = 0;
    L8:
        if (i >= this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size()) goto L9;
        if (i >= this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size()) goto L219;
        ArrayList<FarmUnitDictionary> farmUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(i);
        if (farmUnitDictionarysArrayList == null) goto L219;
        int j = 0;
    L218:
        if (j >= farmUnitDictionarysArrayList.size()) goto L219;
        if (j >= farmUnitDictionarysArrayList.size()) goto L229;
        FarmUnitDictionary farmUnitDictionary = farmUnitDictionarysArrayList.get(j);
        if (farmUnitDictionary == null) goto L229;
        float totalCountFloat = farmUnitDictionary.getTotalCount();
        if (totalCountFloat <= 0.0d) goto L228;
        if (i != 0) goto L231;
        totalChars0Cnt = totalChars0Cnt + totalCountFloat;
        goto L228
    L231:
        if (i != 1) goto L228;
        totalChars1Cnt = totalChars1Cnt + totalCountFloat;
    L228:
        Log.d("MainGameLayout", "i:" + i + ",j:" + j + ",totalCountFloat:" + ((int) totalCountFloat));
    L229:
        j = j + 1;
    L219:
        i = i + 1;
        goto L8
    }

    public void addCharacterToAllRateArrayList(short _eggID, short _characterID, ArrayList<Short> _addArrayList) {
        if (_addArrayList != null) goto L4;
        return;
    L4:
        CharacterDataDictionary characterDataDictionary = this.appDelegate.getCharacterDataDictionaryWithId(_eggID, _characterID);
        if (characterDataDictionary == null) goto L8;
        short characterID = characterDataDictionary.getId();
        Log.d("MainGameLayout", "============characterID:" + characterID + "===========");
        short rate = characterDataDictionary.getRate();
        Log.d("MainGameLayout", "============rate:" + rate + "===========");
        addIDtoArray(characterID, rate, _addArrayList);
    }

    public void addIDtoArray(short _id, short _totalCnt, ArrayList<Short> _addArrayList) {
        if (_totalCnt <= 0) goto L6;
        if (_id < 0) goto L9;
        int i = 0;
    L5:
        if (i >= _totalCnt) goto L10;
        _addArrayList.add(new Short(_id));
        Log.d("MainGameLayout", "============(" + _addArrayList.size() + ")characterID:" + _id + "===========");
        i = i + 1;
        goto L5
    L10:
        return;
    L9:
        return;
    }

    public ArrayList<Short> getFinalRateArrayWithAllRateArray(ArrayList<Short> _allRateArrayList) {
        ArrayList<Short> returnRateArray = new ArrayList();
        if (_allRateArrayList.size() < this.appDelegate.CHARACTERUNITVIEW_TOTAL) goto L7;
        short subTotalCnt = (short) (_allRateArrayList.size() / this.appDelegate.CHARACTERUNITVIEW_TOTAL);
        int i = 0;
    L6:
        if (i >= this.appDelegate.CHARACTERUNITVIEW_TOTAL) goto L7;
        short randIndex = (short) (Math.random() * _allRateArrayList.size());
        Short idNum = _allRateArrayList.get(randIndex);
        short idShort = idNum.shortValue();
        Log.d("MainGameLayout", "============" + i + ")characterID:" + idShort + "===========");
        returnRateArray.add(new Short(idShort));
        _allRateArrayList.remove(randIndex);
        short subCnt = (short) (subTotalCnt - 1);
        int j = 0;
    L10:
        if (j >= _allRateArrayList.size()) goto L11;
        Short checkIdNum = _allRateArrayList.get(j);
        short checkIdShort = checkIdNum.shortValue();
        if (checkIdShort != idShort) goto L15;
        subCnt = (short) (subCnt - 1);
        j = j - 1;
    L15:
        if (subCnt <= 0) goto L11;
        j = j + 1;
    L11:
        i = i + 1;
    L7:
        return returnRateArray;
    }

    public void putEggsWithTool1(short _tool1_ID) {
        Log.d("MainGameLayout", "tool1_ID:" + _tool1_ID);
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        if (_tool1_ID >= 0) goto L5;
        return;
    L5:
        if (_tool1_ID < this.appDelegate.TOOL_1_ALL_CNT) goto L8;
        return;
    L8:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList != null) goto L10;
        return;
    L10:
        if (this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.size() < 2) goto L126;
        ArrayList<ToolUnitDictionary> toolUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.toolUnitDictionarysArrayList.get(1);
        if (toolUnitDictionarysArrayList != null) goto L14;
        return;
    L14:
        if (_tool1_ID >= toolUnitDictionarysArrayList.size()) goto L128;
        ToolUnitDictionary r43 = toolUnitDictionarysArrayList.get(_tool1_ID);
        if (toolUnitDictionarysArrayList == null) goto L129;
        short levelShort = this.appDelegate.getTool1LevelWithIndex(_tool1_ID);
        if (levelShort < 0) goto L130;
        int cookCP = -1;
        if (levelShort < 0) goto L30;
        ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(1, _tool1_ID);
        if (toolDataDictionary == null) goto L30;
        if (toolDataDictionary.toolLevelsArrayList == null) goto L30;
        if (levelShort >= toolDataDictionary.toolLevelsArrayList.size()) goto L30;
        ToolLevelDictionary newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(levelShort);
        if (newToolLevelDictionary == null) goto L30;
        cookCP = newToolLevelDictionary.getLvCookCp();
    L30:
        if (cookCP < 0) goto L131;
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint >= 0.0d) goto L35;
        nowPoint = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L35:
        if (nowPoint >= cookCP) goto L37;
        noCPAlert();
        return;
    L37:
        this.tool_2_SelectView.refresh();
        short eggIDSelectIndex = this.appDelegate.timeSaveDictionary.getEggIDSelectIndex();
        if (eggIDSelectIndex >= 0) goto L40;
        eggIDSelectIndex = 0;
    L40:
        ArrayList<Short> rateArrayList = getRateArrayWithID(eggIDSelectIndex, _tool1_ID, -1, -1);
        Log.i("MainGameLayout", "rateArrayList.size():" + rateArrayList.size());
        short cookMin = -1;
        ToolDataDictionary toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId(1, _tool1_ID);
        if (toolDataDictionary2 == null) goto L46;
        if (toolDataDictionary2.toolLevelsArrayList == null) goto L46;
        cookMin = toolDataDictionary2.toolLevelsArrayList.get(levelShort).getLvMin();
    L46:
        if (rateArrayList.size() != this.appDelegate.CHARACTERUNITVIEW_TOTAL) goto L48;
        if (cookMin <= 0) goto L48;
        boolean checkCookAlarmF = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        Date startDate = new Date(nowDate.getTime());
        if (this.appDelegate.timeSaveDictionary == null) goto L53;
        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate(sdf.format(startDate));
    L53:
        ArrayList<Point> randPositionsArrayList = new ArrayList();
        ArrayList<Float> randOpenTimeArrayList = new ArrayList();
        int i = 0;
    L55:
        if (i >= this.appDelegate.CHARACTERUNITVIEW_TOTAL) goto L56;
        short offsetX = (short) (i % 6);
        short offsetY = (short) (i / 6);
        short randX = (short) ((this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE / 2.0f) - (Math.random() * ((short) this.CHARACTERUNITVIEW_SPACE_X_RAND_RANGE)));
        short randY = (short) ((this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE / 2.0f) - (Math.random() * ((short) this.CHARACTERUNITVIEW_SPACE_Y_RAND_RANGE)));
        short centerX = (short) ((this.CHARACTERUNITVIEW_CENTER_X + (this.CHARACTERUNITVIEW_SPACE_X * offsetX)) + randX);
        short centerY = (short) ((this.CHARACTERUNITVIEW_CENTER_Y + (this.CHARACTERUNITVIEW_SPACE_Y * offsetY)) + randY);
        Point randPosition = new Point(centerX, centerY);
        short insertIndex = 0;
        int j = 0;
    L74:
        if (j >= randPositionsArrayList.size()) goto L75;
        Point checkPosition = randPositionsArrayList.get(j);
        if (centerY >= checkPosition.y) goto L75;
        insertIndex = (short) (j + 1);
        j = j + 1;
    L75:
        Log.d("MainGameLayout", "insertIndex:" + insertIndex);
        randPositionsArrayList.add(insertIndex, randPosition);
        randOpenTimeArrayList.add(new Float(((cookMin * 55.0d) / 23.0d) * i));
        i = i + 1;
        goto L55
    L56:
        Log.d("MainGameLayout", "randPositionsArray.count:" + randOpenTimeArrayList.size());
        float endSecondsFloat = cookMin * 60.0f;
        if (this.appDelegate.timeSaveDictionary == null) goto L59;
        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(endSecondsFloat);
        checkCookAlarmF = true;
    L59:
        int positionIndex = 0;
        int i2 = this.nowCharacterUnitViewsArrayList.size() - 1;
    L60:
        if (i2 < 0) goto L61;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i2);
        if (characterUnit == null) goto L81;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L81;
        if (positionIndex >= randPositionsArrayList.size()) goto L61;
        characterUnit.setNewStatus(-1);
        Point position = randPositionsArrayList.get(positionIndex);
        int characterUnitViewCenterX = position.x;
        int characterUnitViewCenterY = position.y;
        nowCharacterUnitDictionary.setOffsetX(characterUnitViewCenterX);
        nowCharacterUnitDictionary.setOffsetY(characterUnitViewCenterY);
        characterUnit.setCenter(characterUnitViewCenterX, characterUnitViewCenterY);
        nowCharacterUnitDictionary.setEggId(eggIDSelectIndex);
        short idShort = -1;
        if (rateArrayList.size() <= 0) goto L92;
        Short idNum = rateArrayList.get(0);
        if (idNum == null) goto L91;
        idShort = idNum.shortValue();
    L91:
        rateArrayList.remove(0);
    L92:
        nowCharacterUnitDictionary.setCharacterId(idShort);
        nowCharacterUnitDictionary.setPutDate(sdf.format(startDate));
        float openTimeFloat = BitmapDescriptorFactory.HUE_RED;
        if (randOpenTimeArrayList.size() <= 0) goto L98;
        short randOpenTimeIndex = (short) (Math.random() * randOpenTimeArrayList.size());
        Float openTimeNum = randOpenTimeArrayList.get(randOpenTimeIndex);
        if (openTimeNum == null) goto L98;
        openTimeFloat = openTimeNum.floatValue();
    L98:
        if (openTimeFloat >= BitmapDescriptorFactory.HUE_RED) goto L100;
        openTimeFloat = BitmapDescriptorFactory.HUE_RED;
    L100:
        nowCharacterUnitDictionary.setOpenSeconds(endSecondsFloat - openTimeFloat);
        nowCharacterUnitDictionary.setEndSeconds(endSecondsFloat);
        nowCharacterUnitDictionary.setBlackSeconds((3.0f * endSecondsFloat) - openTimeFloat);
        if (this.tool_2_SelectView.tool_2_0 != 36) goto L103;
    L106:
        nowCharacterUnitDictionary.setBlackSeconds(-1.0f);
    L108:
        if (this.tool_2_SelectView.tool_2_0 != 18) goto L110;
    L113:
        nowCharacterUnitDictionary.setSicknessPrevention(1);
    L114:
        characterUnit.setNewStatus(0);
        positionIndex = positionIndex + 1;
        goto L81
    L110:
        if (this.tool_2_SelectView.tool_2_1 == 18) goto L113;
        if (this.tool_2_SelectView.tool_2_2 == 18) goto L113;
        nowCharacterUnitDictionary.setSicknessPrevention(0);
        goto L114
    L103:
        if (this.tool_2_SelectView.tool_2_1 == 36) goto L106;
        if (this.tool_2_SelectView.tool_2_2 != 36) goto L108;
    L81:
        i2 = i2 - 1;
    L61:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(_tool1_ID, this.tool_2_SelectView.tool_2_0, this.tool_2_SelectView.tool_2_1, this.tool_2_SelectView.tool_2_2);
        this.tool_2_SelectView.useSelectedTool2();
        float nowPoint2 = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint2 >= BitmapDescriptorFactory.HUE_RED) goto L65;
        nowPoint2 = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L65:
        if (nowPoint2 < cookCP) goto L67;
        nowPoint2 = nowPoint2 - cookCP;
    L67:
        this.appDelegate.timeSaveDictionary.setPoint(nowPoint2);
        refreshPoint();
        if (this.hidden == true) goto L70;
        this.appDelegate.doSoundPoolPlay(1);
    L70:
        if (checkCookAlarmF == true) goto L71;
    L49:
        this.mainGameViewController.refreshAndSave();
        return;
    L71:
        checkCookAlarm();
    L48:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        goto L49
    L131:
        return;
    L130:
        return;
    L129:
        return;
    L128:
        return;
    }

    public void resetCharacterUnitViewsArray() {
        int i = 0;
    L4:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L5;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L9;
        characterUnit.setNewStatus(-1);
    L9:
        i = i + 1;
        goto L4
    }

    public short checkCharacterUnitViewsArrayActiveCnt() {
        int i = 0;
    L4:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L6;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L11;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L11;
        if (nowCharacterUnitDictionary.getNowStatus() < 0) goto L11;
        short activeCnt = (short) 1;
        return activeCnt;
    L11:
        i = i + 1;
        goto L4
    L6:
        return 0;
    }

    public void noCPAlert() {
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L10;
        String titleLabelString = "";
        String contentLabelString0 = "";
        String contentLabelString1 = "";
        String contentLabelString2 = "cpが足りないようです。";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
    L5:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        String r4 = contentLabelString0;
        String r5 = contentLabelString1;
        String r6 = contentLabelString2;
        String r7 = contentLabelString3;
        String r8 = contentLabelString4;
        this.alertUnitType0.setContentLabelParams(r4, r5, r6, r7, r8, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = 99;
        popAlert();
        if (this.hidden == true) goto L18;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L18:
        return;
    L10:
        if (languageString.equals("zh-TW") == false) goto L12;
    L13:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "你好像沒有足夠的cp。";
        contentLabelString3 = "";
        contentLabelString4 = "";
        goto L5
    L12:
        if (languageString.equals("zh-HK") == true) goto L13;
        if (languageString.equals("zh-CN") == false) goto L17;
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "你好像没有足够的cp。";
        contentLabelString3 = "";
        contentLabelString4 = "";
        goto L5
    L17:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "";
        contentLabelString2 = "You don't have enough cp.";
        contentLabelString3 = "";
        contentLabelString4 = "";
        goto L5
    }

    public void noCPAlertWithNeedFixKitchenCP(int _needcp) {
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L10;
        String titleLabelString = "";
        String contentLabelString0 = "";
        String contentLabelString1 = "台所の掃除は" + _needcp + "cpがかかります。";
        String contentLabelString2 = "";
        String contentLabelString3 = "cpが足りないようです。";
        String contentLabelString4 = "";
    L5:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = 99;
        popAlert();
        if (this.hidden == true) goto L18;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L18:
        return;
    L10:
        if (languageString.equals("zh-TW") == false) goto L12;
    L13:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "打掃廚房需要花費" + _needcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你好像沒有足夠的cp。";
        contentLabelString4 = "";
        goto L5
    L12:
        if (languageString.equals("zh-HK") == true) goto L13;
        if (languageString.equals("zh-CN") == false) goto L17;
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "打扫厨房需要花费" + _needcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你好像没有足够的cp。";
        contentLabelString4 = "";
        goto L5
    L17:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "You need " + _needcp + "cp to clean the kitchen.";
        contentLabelString2 = "";
        contentLabelString3 = "You don't have enough cp.";
        contentLabelString4 = "";
        goto L5
    }

    public void noCPAlertWithNeedLevelUpKitchenCP(int _needcp) {
        hiddenAlert();
        short nowLevel = this.appDelegate.getTool0LevelWithIndex(0);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L10;
        String titleLabelString = "";
        String contentLabelString0 = "台所Lv." + (nowLevel + 2) + "にレベルアップは";
        String contentLabelString1 = String.valueOf(_needcp) + "cpがかかります。";
        String contentLabelString2 = "";
        String contentLabelString3 = "cpが足りないようです。";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = 10.0f;
    L5:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = 99;
        popAlert();
        if (this.hidden == true) goto L18;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L18:
        return;
    L10:
        if (languageString.equals("zh-TW") == false) goto L12;
    L13:
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "升級至廚房Lv." + (nowLevel + 2) + "需要花費" + _needcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你好像沒有足夠的cp。";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L5
    L12:
        if (languageString.equals("zh-HK") == true) goto L13;
        if (languageString.equals("zh-CN") == false) goto L17;
        titleLabelString = "";
        contentLabelString0 = "";
        contentLabelString1 = "升级至厨房Lv." + (nowLevel + 2) + "需要花费" + _needcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你好像没有足够的cp。";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L5
    L17:
        titleLabelString = "";
        contentLabelString0 = "You need " + _needcp + "cp to level up";
        contentLabelString1 = "the kitchen(Lv." + (nowLevel + 2) + ").";
        contentLabelString2 = "";
        contentLabelString3 = "You don't have enough cp.";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L5
    }

    public void fixKitchenWithCP(int _fixcp) {
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint >= BitmapDescriptorFactory.HUE_RED) goto L6;
        nowPoint = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L6:
        if (nowPoint >= _fixcp) goto L9;
        noCPAlertWithNeedFixKitchenCP(_fixcp);
        return;
    L9:
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L16;
        String titleLabelString = "台所掃除";
        String contentLabelString0 = "";
        String contentLabelString1 = "台所の掃除は" + _fixcp + "cpがかかります。";
        String contentLabelString2 = "";
        String contentLabelString3 = "よろしいですか？";
        String contentLabelString4 = "";
    L12:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + BitmapDescriptorFactory.HUE_RED, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = 1;
        this.alertUnitType0.subTag = (short) _fixcp;
        popAlert();
        if (this.hidden == true) goto L24;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L24:
        return;
    L16:
        if (languageString.equals("zh-TW") == false) goto L18;
    L19:
        titleLabelString = "打掃廚房";
        contentLabelString0 = "";
        contentLabelString1 = "打掃廚房需要花費" + _fixcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你確定要打掃嗎？";
        contentLabelString4 = "";
        goto L12
    L18:
        if (languageString.equals("zh-HK") == true) goto L19;
        if (languageString.equals("zh-CN") == false) goto L23;
        titleLabelString = "打掃廚房";
        contentLabelString0 = "";
        contentLabelString1 = "打扫厨房需要花费" + _fixcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你确定要打扫吗？";
        contentLabelString4 = "";
        goto L12
    L23:
        titleLabelString = "Kitchen Cleaning";
        contentLabelString0 = "";
        contentLabelString1 = "You need " + _fixcp + "cp to clean the kitchen.";
        contentLabelString2 = "";
        contentLabelString3 = "Are you sure you want to clear?";
        contentLabelString4 = "";
        goto L12
    }

    public void levelUpKitchenWithCP(int _levelupcp) {
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint >= 0.0d) goto L6;
        nowPoint = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L6:
        if (nowPoint >= _levelupcp) goto L9;
        noCPAlertWithNeedLevelUpKitchenCP(_levelupcp);
        return;
    L9:
        hiddenAlert();
        short nowLevel = this.appDelegate.getTool0LevelWithIndex(0);
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L16;
        String titleLabelString = "台所レベルアップ";
        String contentLabelString0 = "台所Lv." + (nowLevel + 2) + "にレベルアップは";
        String contentLabelString1 = String.valueOf(_levelupcp) + "cpがかかります。";
        String contentLabelString2 = "";
        String contentLabelString3 = "よろしいですか？";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = 10.0f;
    L12:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = 2;
        this.alertUnitType0.subTag = (short) _levelupcp;
        popAlert();
        if (this.hidden == true) goto L24;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L24:
        return;
    L16:
        if (languageString.equals("zh-TW") == false) goto L18;
    L19:
        titleLabelString = "升級廚房";
        contentLabelString0 = "";
        contentLabelString1 = "升級至廚房Lv." + (nowLevel + 2) + "需要花費" + _levelupcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你確定要升級嗎？";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L12
    L18:
        if (languageString.equals("zh-HK") == true) goto L19;
        if (languageString.equals("zh-CN") == false) goto L23;
        titleLabelString = "升級廚房";
        contentLabelString0 = "";
        contentLabelString1 = "升级至厨房Lv." + (nowLevel + 2) + "需要花费" + _levelupcp + "cp。";
        contentLabelString2 = "";
        contentLabelString3 = "你确定要升级吗？";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L12
    L23:
        titleLabelString = "Kitchen Cleaning";
        contentLabelString0 = "You need " + _levelupcp + "cp to level up";
        contentLabelString1 = "the kitchen(Lv." + (nowLevel + 2) + ").";
        contentLabelString2 = "";
        contentLabelString3 = "Are you sure you want to clear?";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L12
    }

    public boolean tool_1_ButtonChangeWithIndex(short _buttonIndex) {
        if (_buttonIndex >= 0) goto L4;
    L5:
        return false;
    L4:
        if (_buttonIndex >= this.appDelegate.TOOL_1_ALL_CNT) goto L5;
        hiddenAlert();
        ToolUnitDictionary toolUnitDictionary = this.appDelegate.getToolDictionaryWithId(1, _buttonIndex);
        if (toolUnitDictionary != null) goto L10;
        return false;
    L10:
        short levelShort = this.appDelegate.getTool1LevelWithIndex(_buttonIndex);
        if (levelShort >= 0) goto L13;
        return false;
    L13:
        String languageString = this.appDelegate.getLocaleLanguage();
        short cookCP = -1;
        if (levelShort < 0) goto L25;
        ToolDataDictionary toolDataDictionary = this.appDelegate.getToolDataDictionaryWithId(1, _buttonIndex);
        if (toolDataDictionary == null) goto L25;
        if (toolDataDictionary.toolLevelsArrayList == null) goto L25;
        if (levelShort >= toolDataDictionary.toolLevelsArrayList.size()) goto L25;
        ToolLevelDictionary newToolLevelDictionary = toolDataDictionary.toolLevelsArrayList.get(levelShort);
        if (newToolLevelDictionary == null) goto L25;
        cookCP = (short) newToolLevelDictionary.getLvCookCp();
    L25:
        if (cookCP < 0) goto L62;
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint >= BitmapDescriptorFactory.HUE_RED) goto L30;
        nowPoint = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L30:
        if (nowPoint >= cookCP) goto L32;
        noCPAlert();
        return false;
    L32:
        String lvCookCPString = String.valueOf(cookCP) + "cp";
        short cookMin = -1;
        if (levelShort < 0) goto L44;
        ToolDataDictionary toolDataDictionary2 = this.appDelegate.getToolDataDictionaryWithId(1, _buttonIndex);
        if (toolDataDictionary2 == null) goto L44;
        if (toolDataDictionary2.toolLevelsArrayList == null) goto L44;
        if (levelShort >= toolDataDictionary2.toolLevelsArrayList.size()) goto L44;
        ToolLevelDictionary newToolLevelDictionary2 = toolDataDictionary2.toolLevelsArrayList.get(levelShort);
        if (newToolLevelDictionary2 == null) goto L44;
        cookMin = newToolLevelDictionary2.getLvMin();
    L44:
        if (cookMin >= 60) goto L46;
        if (cookMin > 0) goto L86;
        return false;
    L86:
        if (languageString.equals("ja-JP") == false) goto L89;
        String lvMinString = String.valueOf(cookMin) + "分";
    L50:
        String nameString = "-";
        ToolDataDictionary toolDataDictionary3 = this.appDelegate.getToolDataDictionaryWithId(1, _buttonIndex);
        if (toolDataDictionary3 == null) goto L56;
        if (languageString.equals("ja-JP") == false) goto L99;
        nameString = toolDataDictionary3.getTitleJa();
        goto L56
    L99:
        if (languageString.equals("zh-TW") == false) goto L101;
    L102:
        nameString = toolDataDictionary3.getTitleZhTW();
        goto L56
    L101:
        if (languageString.equals("zh-HK") == true) goto L102;
        if (languageString.equals("zh-CN") == false) goto L106;
        nameString = toolDataDictionary3.getTitleZhCN();
        goto L56
    L106:
        nameString = toolDataDictionary3.getTitleEn();
    L56:
        if (languageString.equals("ja-JP") == false) goto L108;
        String titleLabelString = "調理開始";
        String contentLabelString0 = String.valueOf(nameString) + " Lv." + (levelShort + 1) + "（" + lvMinString + "）で";
        String contentLabelString1 = "調理は" + lvCookCPString + "がかかります。";
        String contentLabelString2 = "";
        String contentLabelString3 = "よろしいですか？";
        String contentLabelString4 = "";
    L58:
        Log.i("_buttonIndex", "_buttonIndex=" + _buttonIndex);
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        Log.i("_buttonIndex", "_buttonIndex=" + _buttonIndex);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 10.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        Log.i("_buttonIndex", "_buttonIndex=" + _buttonIndex);
        this.alertUnitType0.tag = 0;
        this.alertUnitType0.subTag = _buttonIndex;
        popAlert();
        if (this.hidden == true) goto L61;
        this.appDelegate.doSoundPoolPlay(4);
    L61:
        return true;
    L108:
        if (languageString.equals("zh-TW") == false) goto L110;
    L111:
        titleLabelString = "調理開始";
        contentLabelString0 = "使用" + nameString + " Lv." + (levelShort + 1) + "（" + lvMinString + "）";
        contentLabelString1 = "調理,需要花費" + lvCookCPString + "。";
        contentLabelString2 = "";
        contentLabelString3 = "你確定要使用嗎？";
        contentLabelString4 = "";
        goto L58
    L110:
        if (languageString.equals("zh-HK") == true) goto L111;
        if (languageString.equals("zh-CN") == false) goto L115;
        titleLabelString = "調理開始";
        contentLabelString0 = "使用" + nameString + " Lv." + (levelShort + 1) + "（" + lvMinString + "）";
        contentLabelString1 = "调理,需要花费" + lvCookCPString + "。";
        contentLabelString2 = "";
        contentLabelString3 = "你确定要使用吗？";
        contentLabelString4 = "";
        goto L58
    L115:
        titleLabelString = "Start Hatching";
        contentLabelString0 = "You need to pay " + lvCookCPString + " to use";
        contentLabelString1 = String.valueOf(nameString) + " Lv." + (levelShort + 1) + " (" + lvMinString + ").";
        contentLabelString2 = "";
        contentLabelString3 = "Are you sure you want to use this?";
        contentLabelString4 = "";
        goto L58
    L89:
        if (languageString.equals("zh-TW") == false) goto L91;
    L92:
        lvMinString = String.valueOf(cookMin) + "分鐘";
        goto L50
    L91:
        if (languageString.equals("zh-HK") == true) goto L92;
        if (languageString.equals("zh-CN") == false) goto L96;
        lvMinString = String.valueOf(cookMin) + "分钟";
        goto L50
    L96:
        lvMinString = String.valueOf(cookMin) + "m";
        goto L50
    L46:
        if ((cookMin % 60) != 0) goto L73;
        if (languageString.equals("ja-JP") == false) goto L64;
        lvMinString = String.valueOf(cookMin / 60) + "時間";
        goto L50
    L64:
        if (languageString.equals("zh-TW") == false) goto L66;
    L67:
        lvMinString = String.valueOf(cookMin / 60) + "小時";
        goto L50
    L66:
        if (languageString.equals("zh-HK") == true) goto L67;
        if (languageString.equals("zh-CN") == false) goto L71;
        lvMinString = String.valueOf(cookMin / 60) + "小时";
        goto L50
    L71:
        lvMinString = String.valueOf(cookMin / 60) + "hr";
        goto L50
    L73:
        if (languageString.equals("ja-JP") == false) goto L76;
        lvMinString = String.valueOf(cookMin / 60) + "時間" + (cookMin % 60) + "分";
        goto L50
    L76:
        if (languageString.equals("zh-TW") == false) goto L78;
    L79:
        lvMinString = String.valueOf(cookMin / 60) + "小時" + (cookMin % 60) + "分鐘";
        goto L50
    L78:
        if (languageString.equals("zh-HK") == true) goto L79;
        if (languageString.equals("zh-CN") == false) goto L83;
        lvMinString = String.valueOf(cookMin / 60) + "小時" + (cookMin % 60) + "分钟";
        goto L50
    L83:
        lvMinString = String.valueOf(cookMin / 60) + "hr " + (cookMin % 60) + "m";
        goto L50
    L62:
        return false;
    }

    public void clearEggsAlert(short _subtag) {
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP") == false) goto L10;
        String titleLabelString = "調理中止";
        String contentLabelString0 = "";
        String contentLabelString1 = "調理を中止し、次の調理をはじめます。";
        String contentLabelString2 = "よろしいですか？";
        String contentLabelString3 = "";
        String contentLabelString4 = "";
        float contentLabelLanguageOffsetY = 10.0f;
    L5:
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        String r4 = contentLabelString0;
        String r5 = contentLabelString1;
        String r6 = contentLabelString2;
        String r7 = contentLabelString3;
        String r8 = contentLabelString4;
        this.alertUnitType0.setContentLabelParams(r4, r5, r6, r7, r8, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType(0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, true);
        this.alertUnitType0.tag = -1;
        this.alertUnitType0.subTag = _subtag;
        popAlert();
        if (this.hidden == true) goto L18;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L18:
        return;
    L10:
        if (languageString.equals("zh-TW") == false) goto L12;
    L13:
        titleLabelString = "調理中斷";
        contentLabelString0 = "";
        contentLabelString1 = "你確定要中斷目前的調理,";
        contentLabelString2 = "然後開始新的調理嗎？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L5
    L12:
        if (languageString.equals("zh-HK") == true) goto L13;
        if (languageString.equals("zh-CN") == false) goto L17;
        titleLabelString = "調理中斷";
        contentLabelString0 = "";
        contentLabelString1 = "你确定要中断目前的调理,";
        contentLabelString2 = "然後开始新的调理吗？";
        contentLabelString3 = "";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = 10.0f;
        goto L5
    L17:
        titleLabelString = "Interrupt Hatch";
        contentLabelString0 = "";
        contentLabelString1 = "Are you sure you want to interrupt";
        contentLabelString2 = "current hatch,and then start a new";
        contentLabelString3 = "one?";
        contentLabelString4 = "";
        contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        goto L5
    }

    public void tool1SelectViewCheckCookAlarm() {
        checkCookAlarm();
    }

    public void getPointWithCharacter(CharacterUnit _characterUnit, boolean _directToCPF) {
        if (this.appDelegate.charactersDataFilesArrayList != null) goto L5;
        this.appDelegate.initCharactersDataFilesArray();
    L5:
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(_characterUnit.tag);
        if (nowCharacterUnitDictionary != null) goto L8;
        return;
    L8:
        short addPoint = 0;
        if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList == null) goto L35;
        short eggId = nowCharacterUnitDictionary.getEggId();
        if (eggId < 0) goto L35;
        if (eggId >= this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.size()) goto L35;
        ArrayList<FarmUnitDictionary> farmUnitDictionarysArrayList = (ArrayList) this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList.get(eggId);
        if (farmUnitDictionarysArrayList == null) goto L35;
        short characterId = nowCharacterUnitDictionary.getCharacterId();
        if (characterId < 0) goto L35;
        if (characterId >= farmUnitDictionarysArrayList.size()) goto L35;
        FarmUnitDictionary farmUnitDictionary = farmUnitDictionarysArrayList.get(characterId);
        if (farmUnitDictionary == null) goto L35;
        addPoint = 1;
        float countFloat = farmUnitDictionary.getCount();
        if (countFloat >= BitmapDescriptorFactory.HUE_RED) goto L25;
        countFloat = BitmapDescriptorFactory.HUE_RED;
    L25:
        float countFloat2 = countFloat + 1.0f;
        if (countFloat2 <= 99999.0f) goto L28;
        countFloat2 = 99999.0f;
    L28:
        farmUnitDictionary.setCount(countFloat2);
        float totalCountFloat = farmUnitDictionary.getTotalCount();
        if (totalCountFloat >= BitmapDescriptorFactory.HUE_RED) goto L31;
        totalCountFloat = BitmapDescriptorFactory.HUE_RED;
    L31:
        float totalCountFloat2 = totalCountFloat + 1.0f;
        if (totalCountFloat2 <= 99999.0f) goto L34;
        totalCountFloat2 = 99999.0f;
    L34:
        farmUnitDictionary.setTotalCount(totalCountFloat2);
    L35:
        float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
        if (nowPoint >= 0.0d) goto L38;
        nowPoint = BitmapDescriptorFactory.HUE_RED;
        this.appDelegate.timeSaveDictionary.setPoint(BitmapDescriptorFactory.HUE_RED);
        this.mainGameViewController.refreshAndSave();
    L38:
        this.appDelegate.timeSaveDictionary.setPoint(nowPoint + addPoint);
        _characterUnit.setNewStatus(-1);
        if (checkCharacterUnitViewsArrayActiveCnt() > 0) goto L41;
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(-1, -1, -1, -1);
    L41:
        if (_directToCPF == true) goto L43;
        this.mainGameViewController.refreshAndSave();
    L43:
        refreshPoint();
    }

    public void reload() {
        if (this.appDelegate.timeSaveDictionary != null) goto L5;
        return;
    L5:
        this.mainGameBackViewUnit.refresh();
        if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() >= 0) goto L18;
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(-1, -1, -1, -1);
        if (this.appDelegate.timeSaveDictionary == null) goto L11;
        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate("");
        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(-1.0f);
    L11:
        if (this.nowCharacterUnitViewsArrayList == null) goto L15;
        int i = 0;
    L14:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L15;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L34;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L34;
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
        if (putDateString == null) goto L52;
        nowCharacterUnitDictionary.setPutDate(putDateString);
    L40:
        float endSecondsFloat = nowCharacterUnitDictionary.getEndSeconds();
        if (endSecondsFloat <= 1.0f) goto L53;
        nowCharacterUnitDictionary.setEndSeconds(endSecondsFloat);
    L43:
        short nowStatusInt = nowCharacterUnitDictionary.getNowStatus();
        float openSecondsFloat = nowCharacterUnitDictionary.getOpenSeconds();
        if (openSecondsFloat <= 1.0f) goto L54;
        nowCharacterUnitDictionary.setOpenSeconds(openSecondsFloat);
    L46:
        float blackSecondsFloat = nowCharacterUnitDictionary.getBlackSeconds();
        if (blackSecondsFloat <= 1.0f) goto L55;
        nowCharacterUnitDictionary.setBlackSeconds(blackSecondsFloat);
    L49:
        if (nowStatusInt >= 0) goto L57;
        characterUnit.setNewStatus(-1);
    L51:
        characterUnit.randDirWithEggID(eggID, characterID, true);
        goto L34
    L57:
        if (nowStatusInt >= 1) goto L80;
        short openType = checkOpenWithPutDateString(nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getOpenSeconds());
        if (openType != (-1)) goto L62;
        characterUnit.setNewStatus(-1);
        goto L51
    L62:
        if (openType == 1) goto L66;
        if (openType == 2) goto L66;
        characterUnit.setNewStatus(0);
    L66:
        if (openType == 2) goto L68;
    L70:
        short sickness_prevention_Short = nowCharacterUnitDictionary.getSicknessPrevention();
        if (this.mainGameBackViewUnit.hp == 0) goto L73;
    L77:
        characterUnit.setNewStatus(3);
        goto L51
    L73:
        if (sickness_prevention_Short == 1) goto L77;
        short r27 = nowCharacterUnitDictionary.getEggId();
        short r28 = nowCharacterUnitDictionary.getCharacterId();
        this.appDelegate.getClass();
        short returnID = checkSickWithID(r27, r28, 400);
        if (returnID <= 0) goto L77;
        nowCharacterUnitDictionary.setCharacterId(returnID);
        goto L77
    L68:
        if (nowCharacterUnitDictionary.getCharacterId() != 9) goto L70;
        nowCharacterUnitDictionary.setCharacterId(8);
        goto L70
    L80:
        if (nowStatusInt >= 4) goto L83;
        characterUnit.setNewStatus(3);
        goto L51
    L83:
        if (nowStatusInt >= 10) goto L51;
        characterUnit.directToCP();
        goto L51
    L55:
        nowCharacterUnitDictionary.setBlackSeconds(-1.0f);
        goto L49
    L54:
        nowCharacterUnitDictionary.setOpenSeconds(-1.0f);
        goto L46
    L53:
        nowCharacterUnitDictionary.setEndSeconds(-1.0f);
        goto L43
    L52:
        nowCharacterUnitDictionary.setPutDate("");
    L34:
        i = i + 1;
    L15:
        refreshPoint();
        checkCookAlarm();
        String dateString = this.appDelegate.timeSaveDictionary.getDate();
        Log.d("MainGameLayout", "==============================reload:" + dateString + "==============================");
        if (this.appDelegate.timeSaveDictionary.farmUnitDictionarysArrayList != null) goto L85;
        Log.d("MainGameLayout", "===========================farmUnitDictionarysArrayList == null==============================");
        return;
    L85:
        Log.d("MainGameLayout", "===========================farmUnitDictionarysArrayList != null==============================");
        return;
    L18:
        boolean okF = false;
        String startDateString = this.appDelegate.timeSaveDictionary.getTool1SelectViewStartDate();
        float endSecondsFloat2 = this.appDelegate.timeSaveDictionary.getTool1SelectViewEndSeconds();
        if (startDateString != null) goto L21;
    L25:
        if (okF == false) goto L29;
        short tool_2_0 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_0Index();
        short tool_2_1 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_1Index();
        short tool_2_2 = this.appDelegate.timeSaveDictionary.getTool1SelectViewTool2_2Index();
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex(), tool_2_0, tool_2_1, tool_2_2);
        if (this.appDelegate.timeSaveDictionary == null) goto L11;
        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate(startDateString);
        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(endSecondsFloat2);
        goto L11
    L29:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(-1, -1, -1, -1);
        if (this.appDelegate.timeSaveDictionary == null) goto L11;
        this.appDelegate.timeSaveDictionary.setTool1SelectViewStartDate("");
        this.appDelegate.timeSaveDictionary.setTool1SelectViewEndSeconds(-1.0f);
        goto L11
    L21:
        if (startDateString.length() <= 0) goto L25;
        if (endSecondsFloat2 <= 1.0d) goto L25;
        okF = true;
        goto L25
    }

    public void checkGameLoop() {
        if (this.appDelegate.timeSaveDictionary != null) goto L6;
        return;
    L6:
        if (this.mainGameViewController.nowStatus != 0) goto L57;
        boolean saveF = false;
        this.mainGameBackViewUnit.refresh();
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        new Date();
        int i = this.nowCharacterUnitViewsArrayList.size() - 1;
    L8:
        if (i < 0) goto L10;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L18;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L18;
        if (nowCharacterUnitDictionary.getNowStatus() != 0) goto L44;
        short openType = checkOpenWithPutDateString(nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getOpenSeconds());
        if (openType != (-1)) goto L27;
        characterUnit.setNewStatus(-1);
        saveF = true;
        goto L18
    L27:
        if (openType == 1) goto L31;
        if (openType != 2) goto L18;
    L31:
        if (openType == 2) goto L33;
    L35:
        short sickness_prevention_Short = nowCharacterUnitDictionary.getSicknessPrevention();
        if (this.mainGameBackViewUnit.hp == 0) goto L38;
    L42:
        characterUnit.setNewStatus(1);
        saveF = true;
        goto L18
    L38:
        if (sickness_prevention_Short == 1) goto L42;
        short r9 = nowCharacterUnitDictionary.getEggId();
        short r10 = nowCharacterUnitDictionary.getCharacterId();
        this.appDelegate.getClass();
        short returnID = checkSickWithID(r9, r10, 400);
        if (returnID <= 0) goto L42;
        nowCharacterUnitDictionary.setCharacterId(returnID);
        goto L42
    L33:
        if (nowCharacterUnitDictionary.getCharacterId() != 9) goto L35;
        nowCharacterUnitDictionary.setCharacterId(8);
        goto L35
    L44:
        if (nowCharacterUnitDictionary.getNowStatus() != 3) goto L18;
        short returnID2 = checkBlackWithID(nowCharacterUnitDictionary.getEggId(), nowCharacterUnitDictionary.getCharacterId(), nowCharacterUnitDictionary.getPutDate(), nowCharacterUnitDictionary.getBlackSeconds());
        if (returnID2 <= 0) goto L18;
        nowCharacterUnitDictionary.setCharacterId(returnID2);
        characterUnit.setNewStatus(3);
        saveF = true;
    L18:
        i = i - 1;
        goto L8
    L10:
        if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() >= 0) goto L12;
    L14:
        if (saveF == false) goto L58;
        this.mainGameViewController.refreshAndSave();
        return;
    L58:
        return;
    L12:
        if (checkCharacterUnitViewsArrayActiveCnt() > 0) goto L14;
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(-1, -1, -1, -1);
        saveF = true;
        goto L14
    }

    public short checkOpenWithPutDateString(String _putDateString, float _openSeconds) {
        short openType = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        if (_putDateString == null) goto L15;
        if (_openSeconds <= 1.0f) goto L15;
        if (_putDateString.length() <= 0) goto L15;
        Date putDate = null;
        putDate = sdf.parse(_putDateString);     // Catch: ParseException -> L23
    L10:
        if (putDate == null) goto L15;
        if (putDate.before(new Date(nowDate.getTime() - ((1 * ((long) (10.0f + _openSeconds))) * 1000))) == false) goto L17;
        openType = 2;
        goto L15
    L17:
        if (putDate.before(new Date(nowDate.getTime() - ((1 * ((long) _openSeconds)) * 1000))) == false) goto L20;
        openType = 1;
        goto L15
    L20:
        if (putDate.before(new Date(nowDate.getTime() + 10)) == false) goto L22;
        openType = 0;
        goto L15
    L22:
        openType = 2;
    L15:
        return openType;
    }

    public short checkSickWithID(short _eggID, short _characterID, short _rate) {
        if (_eggID != 0) goto L43;
        if (_characterID != 1) goto L7;
        return -1;
    L7:
        if (_characterID != 2) goto L9;
        return -1;
    L9:
        if (_characterID != 20) goto L11;
        return -1;
    L11:
        if (_characterID != 30) goto L13;
        return -1;
    L13:
        if (_characterID != 34) goto L15;
        return -1;
    L15:
        if (_characterID != 35) goto L17;
        return -1;
    L17:
        if (_characterID != 51) goto L19;
        return -1;
    L19:
        if (_characterID != 52) goto L21;
        return -1;
    L21:
        if (_characterID != 53) goto L23;
        return -1;
    L23:
        if (_characterID != 54) goto L25;
        return -1;
    L25:
        if (_characterID == 68) goto L89;
        short randRate = (short) (Math.random() * 1000.0d);
        if (randRate >= _rate) goto L90;
        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex(0);
        if (tool_0_0_level < 1) goto L91;
        short rand34Index = (short) (Math.random() * 10.0d);
        if (rand34Index == 0) goto L33;
        return 1;
    L33:
        if (tool_0_0_level < 2) goto L93;
        short rand53Index = (short) (Math.random() * 3.0d);
        if (rand53Index == 0) goto L37;
        return 34;
    L37:
        if (tool_0_0_level < 3) goto L95;
        short rand68Index = (short) (Math.random() * 2.0d);
        if (rand68Index != 0) goto L96;
        return 68;
    L96:
        return 53;
    L95:
        return 53;
    L93:
        return 34;
    L91:
        return 1;
    L90:
        return -1;
    L89:
        return -1;
    L43:
        if (_eggID == 1) goto L45;
        return -1;
    L45:
        if (_characterID != 1) goto L47;
        return -1;
    L47:
        if (_characterID != 2) goto L49;
        return -1;
    L49:
        if (_characterID != 14) goto L51;
        return -1;
    L51:
        if (_characterID != 15) goto L53;
        return -1;
    L53:
        if (_characterID != 19) goto L55;
        return -1;
    L55:
        if (_characterID != 20) goto L57;
        return -1;
    L57:
        if (_characterID != 27) goto L59;
        return -1;
    L59:
        if (_characterID != 28) goto L61;
        return -1;
    L61:
        if (_characterID != 29) goto L63;
        return -1;
    L63:
        if (_characterID == 36) goto L107;
        short randRate2 = (short) (Math.random() * 1000.0d);
        if (randRate2 >= _rate) goto L108;
        short tool_0_0_level2 = this.appDelegate.getTool0LevelWithIndex(0);
        if (tool_0_0_level2 < 1) goto L109;
        short rand34Index2 = (short) (Math.random() * 10.0d);
        if (rand34Index2 == 0) goto L71;
        return 1;
    L71:
        if (tool_0_0_level2 < 2) goto L111;
        short rand53Index2 = (short) (Math.random() * 3.0d);
        if (rand53Index2 == 0) goto L75;
        return 19;
    L75:
        if (tool_0_0_level2 < 3) goto L113;
        short rand36Index = (short) (Math.random() * 2.0d);
        if (rand36Index != 0) goto L114;
        return 36;
    L114:
        return 29;
    L113:
        return 29;
    L111:
        return 19;
    L109:
        return 1;
    L108:
        return -1;
    L107:
        return -1;
    }

    public short checkBlackWithID(short _eggID, short _characterID, String _putDateString, float _blackSeconds) {
        short returnID = -1;
        if (_blackSeconds < 1.0f) goto L5;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date nowDate = new Date();
        if (_eggID != 0) goto L90;
        if (_characterID == 0) goto L179;
        if (_characterID == 2) goto L179;
        if (_characterID == 5) goto L179;
        if (_characterID == 20) goto L179;
        if (_characterID == 25) goto L179;
        if (_characterID == 26) goto L179;
        if (_characterID == 27) goto L179;
        if (_characterID == 30) goto L179;
        if (_characterID == 31) goto L179;
        if (_characterID == 33) goto L179;
        if (_characterID == 34) goto L179;
        if (_characterID == 35) goto L179;
        if (_characterID == 53) goto L179;
        if (_characterID == 54) goto L179;
        if (_characterID == 68) goto L179;
        if (_characterID == 70) goto L179;
        if (_characterID == 110) goto L179;
        if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() <= 0) goto L179;
        if (_putDateString == null) goto L179;
        if (_blackSeconds <= 1.0d) goto L179;
        if (_putDateString.length() <= 0) goto L179;
        Date putDate = null;
        putDate = sdf.parse(_putDateString);     // Catch: ParseException -> L171
    L50:
        if (putDate == null) goto L179;
        if (putDate.before(new Date(nowDate.getTime() - ((1 * ((long) _blackSeconds)) * 1000))) == false) goto L179;
        if (_characterID != 109) goto L56;
    L61:
        returnID = 110;
        goto L179
    L56:
        if (_characterID == 111) goto L61;
        if (_characterID == 112) goto L61;
        if (_characterID == 113) goto L61;
        if (_characterID != 69) goto L66;
    L79:
        returnID = 70;
        goto L179
    L66:
        if (_characterID == 71) goto L79;
        if (_characterID == 72) goto L79;
        if (_characterID == 73) goto L79;
        if (_characterID == 74) goto L79;
        if (_characterID == 75) goto L79;
        if (_characterID == 76) goto L79;
        if (_characterID == 77) goto L79;
        returnID = 2;
        short tool_0_0_level = this.appDelegate.getTool0LevelWithIndex(0);
        if (tool_0_0_level < 1) goto L179;
        short rand35Index = (short) (Math.random() * 50.0d);
        if (rand35Index != 0) goto L179;
        returnID = 35;
        if (tool_0_0_level < 2) goto L179;
        short rand54Index = (short) (Math.random() * 3.0d);
        if (rand54Index != 0) goto L179;
        returnID = 54;
    L179:
        return returnID;
    L90:
        if (_eggID != 1) goto L179;
        if (_characterID == 0) goto L179;
        if (_characterID == 2) goto L179;
        if (_characterID == 4) goto L179;
        if (_characterID == 14) goto L179;
        if (_characterID == 15) goto L179;
        if (_characterID == 16) goto L179;
        if (_characterID == 17) goto L179;
        if (_characterID == 18) goto L179;
        if (_characterID == 19) goto L179;
        if (_characterID == 20) goto L179;
        if (_characterID == 27) goto L179;
        if (_characterID == 28) goto L179;
        if (_characterID == 29) goto L179;
        if (_characterID == 36) goto L179;
        if (_characterID == 38) goto L179;
        if (_characterID == 53) goto L179;
        if (this.appDelegate.timeSaveDictionary.getTool1SelectViewNowButtonIndex() <= 0) goto L179;
        if (_putDateString == null) goto L179;
        if (_blackSeconds <= 1.0d) goto L179;
        if (_putDateString.length() <= 0) goto L179;
        Date putDate2 = null;
        putDate2 = sdf.parse(_putDateString);     // Catch: ParseException -> L173
    L131:
        if (putDate2 == null) goto L179;
        if (putDate2.before(new Date(nowDate.getTime() - ((1 * ((long) _blackSeconds)) * 1000))) == false) goto L179;
        if (_characterID != 52) goto L137;
    L142:
        returnID = 53;
        goto L179
    L137:
        if (_characterID == 54) goto L142;
        if (_characterID == 55) goto L142;
        if (_characterID == 56) goto L142;
        if (_characterID != 37) goto L146;
    L161:
        returnID = 38;
        goto L179
    L146:
        if (_characterID == 39) goto L161;
        if (_characterID == 40) goto L161;
        if (_characterID == 41) goto L161;
        if (_characterID == 42) goto L161;
        if (_characterID == 43) goto L161;
        if (_characterID == 44) goto L161;
        if (_characterID == 45) goto L161;
        if (_characterID == 46) goto L161;
        returnID = 2;
        short tool_0_0_level2 = this.appDelegate.getTool0LevelWithIndex(0);
        if (tool_0_0_level2 < 1) goto L179;
        short rand35Index2 = (short) (Math.random() * 50.0d);
        if (rand35Index2 != 0) goto L179;
        returnID = 20;
        if (tool_0_0_level2 < 2) goto L179;
        short rand54Index2 = (short) (Math.random() * 20.0d);
        if (rand54Index2 != 0) goto L179;
        returnID = 28;
        goto L179
    L5:
        return -1;
    }

    public void animeGameLoop() {
        if (this.mainGameViewController.nowStatus == 0) goto L6;
        return;
    L6:
        if (this.nowCharacterUnitViewsArrayList == null) goto L10;
        int i = 0;
    L9:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L10;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L14;
        characterUnit.animeLoop();
    L14:
        i = i + 1;
    L10:
        this.mainGameBackViewUnit.doLoop();
        this.tool_1_SelectScrollUnit.doLoop();
    }

    public void getCharacterCheck(float _touchX, float _touchY) {
        if (checkCharacterUnitViewsArrayActiveCnt() > 0) goto L5;
        return;
    L5:
        int i = 0;
    L7:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L33;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L10;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L10;
        if (nowCharacterUnitDictionary.getNowStatus() != 3) goto L10;
        if (_touchX < characterUnit.frameOriginX) goto L10;
        if (_touchX >= (characterUnit.frameOriginX + characterUnit.frameSizeWidth)) goto L10;
        if (_touchY < characterUnit.frameOriginY) goto L10;
        if (_touchY >= (characterUnit.frameOriginY + characterUnit.frameSizeHeight)) goto L10;
        characterUnit.setNewStatus(4);
        this.mainGameViewController.refreshAndSave();
    L10:
        i = i + 1;
        goto L7
    }

    public void directCharacterToCP() {
        boolean saveF = false;
        int i = 0;
    L4:
        if (i >= this.nowCharacterUnitViewsArrayList.size()) goto L5;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(i);
        if (characterUnit == null) goto L10;
        CharacterUnitDictionary nowCharacterUnitDictionary = this.appDelegate.getCharacterUnitDictionaryWithIndex(characterUnit.tag);
        if (nowCharacterUnitDictionary == null) goto L10;
        if (nowCharacterUnitDictionary.getNowStatus() < 4) goto L10;
        if (nowCharacterUnitDictionary.getNowStatus() >= 9) goto L10;
        characterUnit.directToCP();
        saveF = true;
    L10:
        i = i + 1;
        goto L4
    L5:
        if (saveF == false) goto L24;
        this.mainGameViewController.refreshAndSave();
        return;
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) {
        if (_tag != (-100)) goto L12;
        if (_buttonIndex != 0) goto L7;
        new Handler().postDelayed(new AnonymousClass2(this), 100);
        return;
    L7:
        if (_buttonIndex != 1) goto L75;
        this.mainGameViewController.doManualLayoutDisplay(0);
        if (this.hidden == true) goto L76;
        this.appDelegate.doSoundPoolPlay(4);
        return;
    L76:
        return;
    L75:
        return;
    L12:
        if (_tag != (-99)) goto L16;
        if (_buttonIndex != 0) goto L78;
    L78:
        return;
    L16:
        if (_tag != (-1)) goto L24;
        if (_buttonIndex == 0) goto L23;
        if (_buttonIndex != 1) goto L23;
        if (_subtag < 0) goto L23;
        if (_subtag >= this.appDelegate.TOOL_1_ALL_CNT) goto L23;
        putEggsWithTool1(_subtag);
        return;
    L23:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        return;
    L24:
        if (_tag != 0) goto L35;
        if (_buttonIndex == 0) goto L34;
        if (_buttonIndex != 1) goto L34;
        if (checkCharacterUnitViewsArrayActiveCnt() > 0) goto L33;
        if (_subtag < 0) goto L34;
        if (_subtag >= this.appDelegate.TOOL_1_ALL_CNT) goto L34;
        putEggsWithTool1(_subtag);
        return;
    L33:
        this.clearEggsSubTag = _subtag;
        new Handler().postDelayed(new AnonymousClass3(this), 100);
    L34:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        return;
    L35:
        if (_tag != 1) goto L45;
        if (_buttonIndex == 0) goto L83;
        if (_buttonIndex == 1) goto L39;
        return;
    L39:
        if (this.mainGameBackViewUnit.fixKitchenWithCP(_subtag) == true) goto L41;
        return;
    L41:
        if (this.hidden == true) goto L43;
        this.appDelegate.doSoundPoolPlay(9);
    L43:
        this.mainGameViewController.refreshAndSave();
        refreshPoint();
        return;
    L83:
        return;
    L45:
        if (_tag != 2) goto L54;
        if (_buttonIndex == 0) goto L87;
        if (_buttonIndex == 1) goto L49;
        return;
    L49:
        if (this.mainGameBackViewUnit.levelUpKitchenWithCP(_subtag) == true) goto L51;
        return;
    L51:
        if (this.hidden == true) goto L53;
        this.appDelegate.doSoundPoolPlay(10);
    L53:
        this.mainGameViewController.refreshAndSave();
        refreshPoint();
        return;
    L87:
        return;
    L54:
        if (_tag != 10) goto L60;
        if (_buttonIndex != 0) goto L57;
        cleanCkSendKitchenString();
        return;
    L57:
        if (_buttonIndex != 1) goto L92;
        new Handler().postDelayed(new AnonymousClass4(this), 100);
        return;
    L92:
        return;
    L60:
        if (_tag != 11) goto L66;
        if (_buttonIndex != 0) goto L63;
        cleanCkSendKitchenString();
        return;
    L63:
        if (_buttonIndex != 1) goto L95;
        new Handler().postDelayed(new AnonymousClass5(this), 100);
        return;
    L95:
        return;
    L66:
        if (_tag != 12) goto L72;
        if (_buttonIndex != 0) goto L69;
        cleanCkSendKitchenString();
        return;
    L69:
        if (_buttonIndex != 1) goto L98;
        new Handler().postDelayed(new AnonymousClass6(this), 5);
        return;
    L98:
        return;
    L72:
        if (_tag != 99) goto L100;
        if (_buttonIndex != 0) goto L74;
    L74:
        this.tool_1_SelectScrollUnit.changeButtonWithIndex(this.appDelegate.getShort_tool_1_selectview_nowbuttonindex(), -2, -2, -2);
        return;
    }

    public void clearBitmap() {
        if (this.tool_1_SelectScrollUnit == null) goto L6;
        this.tool_1_SelectScrollUnit.clearBitmap();
    L6:
        if (this.mainGameBackViewUnit == null) goto L9;
        this.mainGameBackViewUnit.clearBitmap();
        return;
    }

    public void refreshBitmap() {
        if (this.tool_1_SelectScrollUnit == null) goto L6;
        this.tool_1_SelectScrollUnit.refreshBitmap();
    L6:
        if (this.mainGameBackViewUnit == null) goto L9;
        this.mainGameBackViewUnit.refreshBitmap();
        return;
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.alertUnitType0 == null) goto L9;
        if (this.alertUnitType0.hidden == true) goto L9;
        this.alertUnitType0.gameOnTouch(event);
        return true;
    L9:
        if (this.tool_2_SelectListUnit == null) goto L14;
        if (this.tool_2_SelectListUnit.hidden == true) goto L14;
        boolean returnF2 = this.tool_2_SelectListUnit.gameOnTouch(event);
        return returnF2;
    L14:
        if (this.eggSelectUnit == null) goto L16;
        returnF = this.eggSelectUnit.gameOnTouch(event);
    L16:
        short touchIndex = -1;
        if (event.getY() < this.MAINGAMEBACK_TOUCH_RANGE_Y_MIN) goto L32;
        if (event.getY() >= this.MAINGAMEBACK_TOUCH_RANGE_Y_MAX) goto L32;
        if (this.mainGameBackViewUnit == null) goto L23;
        this.mainGameBackViewUnit.gameOnTouch(event);
    L23:
        touchIndex = 0;
        if (this.tool_1_SelectScrollUnit == null) goto L26;
        this.tool_1_SelectScrollUnit.unclickAllButton();
    L26:
        if (touchIndex == 1) goto L40;
        if (this.tool_1_SelectScrollUnit == null) goto L40;
        this.tool_1_SelectScrollUnit.unclickAllButton();
    L40:
        return returnF;
    L32:
        if (event.getY() < this.TOOL_1_SELECT_TOUCH_RANGE_Y_MIN) goto L26;
        if (event.getY() >= this.TOOL_1_SELECT_TOUCH_RANGE_Y_MAX) goto L26;
        if (this.tool_1_SelectScrollUnit == null) goto L38;
        this.tool_1_SelectScrollUnit.gameOnTouch(event);
    L38:
        touchIndex = 1;
        goto L26
    }

    public void gameDraw(Canvas canvas) {
        if (this.mainGameBackViewUnit == null) goto L6;
        this.mainGameBackViewUnit.gameDraw(canvas);
    L6:
        if (this.cpDisplayUnit == null) goto L9;
        this.cpDisplayUnit.gameDraw(canvas);
    L9:
        if (this.tool_1_SelectScrollUnit == null) goto L12;
        this.tool_1_SelectScrollUnit.gameDraw(canvas);
    L12:
        if (this.bottomBlockLayout == null) goto L15;
        this.bottomBlockLayout.gameDraw(canvas);
    L15:
        if (this.eggSelectUnit == null) goto L18;
        this.eggSelectUnit.gameDraw(canvas);
    L18:
        if (this.tool_2_SelectListUnit == null) goto L23;
        if (this.tool_2_SelectListUnit.hidden == true) goto L23;
        this.tool_2_SelectListUnit.gameDraw(canvas);
    L23:
        if (this.alertUnitType0 != null) goto L25;
        return;
    L25:
        if (this.alertUnitType0.hidden == true) goto L29;
        this.alertUnitType0.gameDraw(canvas);
        return;
    }

    public void onDestroy() {
        if (this.alertUnitType0 == null) goto L6;
        this.alertUnitType0.onDestroy();
        this.alertUnitType0 = null;
    L6:
        if (this.bottomBlockLayout == null) goto L9;
        this.bottomBlockLayout.onDestroy();
        this.bottomBlockLayout = null;
    L9:
        if (this.tool_2_SelectListUnit == null) goto L12;
        this.tool_2_SelectListUnit.onDestroy();
        this.tool_2_SelectListUnit = null;
    L12:
        if (this.cpDisplayUnit == null) goto L15;
        this.cpDisplayUnit.onDestroy();
        this.cpDisplayUnit = null;
    L15:
        if (this.tool_1_SelectScrollUnit == null) goto L18;
        this.tool_1_SelectScrollUnit.onDestroy();
        this.tool_1_SelectScrollUnit = null;
    L18:
        if (this.tool_1_SelectScrolView == null) goto L21;
        this.tool_1_SelectScrolView.removeAllViews();
        this.tool_1_SelectScrolView = null;
    L21:
        if (this.eggSelectUnit == null) goto L24;
        this.eggSelectUnit.onDestroy();
        this.eggSelectUnit = null;
    L24:
        if (this.tool_2_SelectView == null) goto L27;
        this.tool_2_SelectView.onDestroy();
        this.tool_2_SelectView = null;
    L27:
        if (this.mainGameBackViewUnit == null) goto L30;
        this.mainGameBackViewUnit.onDestroy();
        this.mainGameBackViewUnit = null;
    L30:
        if (this.nowCharacterUnitViewsArrayList != null) goto L32;
    L34:
        this.mainGameViewController = null;
        this.appDelegate = null;
        return;
    L32:
        if (this.nowCharacterUnitViewsArrayList.size() == 0) goto L33;
        CharacterUnit characterUnit = this.nowCharacterUnitViewsArrayList.get(0);
        if (characterUnit == null) goto L39;
        characterUnit.onDestroy();
    L39:
        this.nowCharacterUnitViewsArrayList.remove(0);
        goto L32
    L33:
        this.nowCharacterUnitViewsArrayList = null;
        goto L34
    }
}
