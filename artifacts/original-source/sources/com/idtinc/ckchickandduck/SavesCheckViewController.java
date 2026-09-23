package com.idtinc.ckchickandduck;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckunit.TimeSaveDictionary;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;
import org.json.JSONException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class SavesCheckViewController implements AlertUnitType0Delegate {
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private short backSubTag;
    private float finalHeight;
    private float finalWidth;
    public boolean hidden;
    public short nowStatus;
    private SavesCheckViewUnit savesCheckViewUnit;
    private float zoomRate;

    public SavesCheckViewController(float _finalwidth, float _finalheight, float _zoomrate, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.backSubTag = (short) -1;
        this.appDelegate = null;
        this.appDelegate = _appDelegate;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.backSubTag = (short) -1;
        this.savesCheckViewUnit = new SavesCheckViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        }
        this.alertUnitType0.delegate = this;
    }

    public void reset() {
        changeNowStatus(-1);
    }

    public void restart() {
        changeNowStatus(0);
        if (this.appDelegate != null) {
            this.appDelegate.initImage();
        }
        checkTimeSavesGameStart();
    }

    public void popAlert() {
        changeNowStatus(1);
    }

    public void goToMainGame() {
        changeNowStatus(2);
    }

    public void timeErrorAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        this.alertUnitType0.reset();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            titleLabelString = "タイムエラー";
            contentLabelString0 = "現在時刻: " + sdf.format(new Date());
            contentLabelString1 = "";
            contentLabelString2 = "端末の日付と時刻が正しくないようです。正";
            contentLabelString3 = "しく設定して、もう一度実行してください。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            titleLabelString = "日期時間錯誤";
            contentLabelString0 = "現在時間: " + sdf2.format(new Date());
            contentLabelString1 = "";
            contentLabelString2 = "現在的日期與時間是不正確的,";
            contentLabelString3 = "請調整後再次執行遊戲。";
            contentLabelString4 = "";
        } else if (languageString.equals("zh-CN")) {
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            titleLabelString = "日期時間錯誤";
            contentLabelString0 = "现在时间: " + sdf3.format(new Date());
            contentLabelString1 = "";
            contentLabelString2 = "现在的日期与时间是不正确的,";
            contentLabelString3 = "请调整後再次执行游戏。";
            contentLabelString4 = "";
        } else {
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            titleLabelString = "Date and Time Error!";
            contentLabelString0 = "Current Time: " + sdf4.format(new Date());
            contentLabelString1 = "";
            contentLabelString2 = "Please correct the system date";
            contentLabelString3 = "and time,and then try again.";
            contentLabelString4 = "";
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + 10.0f, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", "OK", this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 2, true);
        this.alertUnitType0.tag = (short) -100;
        popAlert();
    }

    public void formatAlert() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        this.alertUnitType0.reset();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "初期化";
            contentLabelString0 = "";
            contentLabelString1 = "初期化を行います。";
            contentLabelString2 = "";
            contentLabelString3 = "よろしいですか？";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "開始新遊戲";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你確定要開始新的遊戲?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "開始新遊戲";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你确定要开始新的游戏?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "Restart";
            contentLabelString0 = "";
            contentLabelString1 = "Are you sure you want to delete current";
            contentLabelString2 = "record and restart new game?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, true);
        this.alertUnitType0.tag = (short) -1;
        popAlert();
    }

    public void earlierFileAlertWithSubTag(short _subtag) {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        this.alertUnitType0.reset();
        SimpleDateFormat checkSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date backDate = null;
        String backDateString = "";
        if (this.appDelegate != null && this.appDelegate.mainSavesDictionary != null && this.appDelegate.mainSavesDictionary.timeSavesArrayList != null && this.appDelegate.mainSavesDictionary.timeSavesArrayList.size() > _subtag) {
            TimeSaveDictionary checkTimeSaveDictionary = this.appDelegate.mainSavesDictionary.timeSavesArrayList.get(_subtag);
            String originDateString = null;
            if (checkTimeSaveDictionary != null) {
                originDateString = checkTimeSaveDictionary.getDate();
            }
            if (originDateString != null) {
                try {
                    backDate = checkSdf.parse(originDateString);
                } catch (ParseException e) {
                }
            }
        }
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            if (backDate != null) {
                backDateString = sdf.format(backDate);
            }
            titleLabelString = "ゲーム再開";
            contentLabelString0 = "";
            contentLabelString1 = String.valueOf(backDateString) + "のセーブ";
            contentLabelString2 = "データを再開します。よろしいですか？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            if (backDate != null) {
                backDateString = sdf2.format(backDate);
            }
            titleLabelString = "繼續遊戲";
            contentLabelString0 = "";
            contentLabelString1 = "你確定要回到" + backDateString;
            contentLabelString2 = "的記錄並繼續遊戲?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else if (languageString.equals("zh-CN")) {
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            if (backDate != null) {
                backDateString = sdf3.format(backDate);
            }
            titleLabelString = "繼續遊戲";
            contentLabelString0 = "";
            contentLabelString1 = "你确定要回到" + backDateString;
            contentLabelString2 = "的记录并继续游戏?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        } else {
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            if (backDate != null) {
                backDateString = sdf4.format(backDate);
            }
            titleLabelString = "Reload";
            contentLabelString0 = "";
            contentLabelString1 = "Are you sure you want to";
            contentLabelString2 = "reload previous save file";
            contentLabelString3 = "(" + backDateString + ")?";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, true);
        this.alertUnitType0.tag = (short) 2;
        this.alertUnitType0.subTag = _subtag;
        popAlert();
    }

    public void checkTimeSavesGameStart() throws JSONException, NumberFormatException, IOException {
        String timeSaveDictionaryJSONString;
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        String titleLabelString2;
        String contentLabelString02;
        String contentLabelString12;
        String contentLabelString22;
        String contentLabelString32;
        String contentLabelString42;
        float contentLabelLanguageOffsetY2;
        String titleLabelString3;
        String contentLabelString03;
        String contentLabelString13;
        String contentLabelString23;
        String contentLabelString33;
        String contentLabelString43;
        String titleLabelString4;
        String contentLabelString04;
        String contentLabelString14;
        String contentLabelString24;
        String contentLabelString34;
        String contentLabelString44;
        float contentLabelLanguageOffsetY3;
        short timeRangeIndex = this.appDelegate.checkTimeRangeWithTimeSaveDictionary(new Date());
        if (timeRangeIndex < 0) {
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(5);
            }
            timeErrorAlert();
            return;
        }
        if (timeRangeIndex > 0) {
            if (!this.hidden) {
                this.appDelegate.doSoundPoolPlay(5);
            }
            timeErrorAlert();
            return;
        }
        Log.d("appDelegate.mainSavesDictionary.timeSavesArrayList.size()", new StringBuilder().append(this.appDelegate.mainSavesDictionary.timeSavesArrayList.size()).toString());
        short saveIndex = this.appDelegate.checkTimeWithTimeSaveDictionary(new Date());
        if (saveIndex == -2 || saveIndex == -3) {
            Boolean checkF = true;
            String checkDateString = "";
            if (saveIndex == -2) {
                this.appDelegate.loadTimeSaveDictionary();
                if (this.appDelegate.timeSaveDictionary != null) {
                    checkDateString = this.appDelegate.timeSaveDictionary.getDate();
                } else {
                    return;
                }
            } else if (saveIndex == -3) {
                boolean returnF = true;
                if (this.appDelegate.defaultSharedPreferences != null && (timeSaveDictionaryJSONString = this.appDelegate.defaultSharedPreferences.getString("time_save_dictionary", "")) != null && timeSaveDictionaryJSONString.length() > 10) {
                    checkDateString = this.appDelegate.getTimeSaveDictionaryJSONStringDate(timeSaveDictionaryJSONString);
                    returnF = false;
                }
                if (returnF) {
                    return;
                }
            }
            if (checkDateString != null && checkDateString.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date nowDate = new Date();
                Log.d("checkTimeSavesGameStart", "nowDate-60s:" + sdf.format(new Date(nowDate.getTime() - 60000)));
                Log.d("checkTimeSavesGameStart", "nowDate:" + sdf.format(nowDate));
                Log.d("checkTimeSavesGameStart", "checkDate:" + checkDateString);
                Date checkDate = null;
                try {
                    checkDate = sdf.parse(checkDateString);
                } catch (ParseException e) {
                }
                if (checkDate != null) {
                    if (checkDate.before(nowDate)) {
                        Log.d("checkTimeSavesGameStart", checkDate + "before" + nowDate);
                        if (checkDate.after(new Date(nowDate.getTime() - (((this.appDelegate.NEED_RELOAD_HOURS * 60) * 60) * 1000)))) {
                            Log.d("checkTimeSavesGameStart", checkDate + "before" + nowDate);
                            checkF = false;
                        }
                    } else {
                        Log.d("checkTimeSavesGameStart", checkDate + "after" + nowDate);
                    }
                }
            }
            if (!checkF.booleanValue()) {
                if (saveIndex == -2) {
                    normalInitGame(0);
                    return;
                } else {
                    if (saveIndex == -3) {
                        normalInitGame(1);
                        return;
                    }
                    return;
                }
            }
            this.appDelegate.doSoundPoolPlay(4);
            this.alertUnitType0.reset();
            String languageString = this.appDelegate.getLocaleLanguage();
            if (languageString.equals("ja-JP")) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                titleLabelString = "ゲーム再開";
                contentLabelString0 = "現在時刻: " + sdf2.format(new Date());
                contentLabelString1 = "";
                contentLabelString2 = "現在時刻から、ゲームを再開します。";
                contentLabelString3 = "よろしいですか？";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                titleLabelString = "繼續遊戲";
                contentLabelString0 = "";
                contentLabelString1 = "現在時間: " + sdf3.format(new Date());
                contentLabelString2 = "";
                contentLabelString3 = "是否從現在時間起繼續進行遊戲?";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString.equals("zh-CN")) {
                SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                titleLabelString = "繼續遊戲";
                contentLabelString0 = "";
                contentLabelString1 = "现在时间: " + sdf4.format(new Date());
                contentLabelString2 = "";
                contentLabelString3 = "是否从现在时间起继续进行游戏?";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
            } else {
                SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                titleLabelString = "Continue Game";
                contentLabelString0 = "Current Date: " + sdf5.format(new Date());
                contentLabelString1 = "";
                contentLabelString2 = "Do you want to continue game";
                contentLabelString3 = "from current date?";
                contentLabelString4 = "";
                contentLabelLanguageOffsetY = 10.0f;
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, true);
            this.alertUnitType0.tag = (short) 0;
            if (saveIndex == -2) {
                this.alertUnitType0.subTag = (short) 0;
            } else if (saveIndex == -3) {
                this.alertUnitType0.subTag = (short) 1;
            }
            popAlert();
            return;
        }
        if (saveIndex == -1) {
            this.appDelegate.doSoundPoolPlay(5);
            this.alertUnitType0.reset();
            String languageString2 = this.appDelegate.getLocaleLanguage();
            if (languageString2.equals("ja-JP")) {
                SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                titleLabelString4 = "タイムエラー";
                contentLabelString04 = "現在時刻: " + sdf6.format(new Date());
                contentLabelString14 = "";
                contentLabelString24 = "端末の日付と時刻が正しくないようです。";
                contentLabelString34 = "セーブデータを破棄して初期化が必要です。";
                contentLabelString44 = "よろしいですか？";
                contentLabelLanguageOffsetY3 = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString2.equals("zh-TW") || languageString2.equals("zh-HK")) {
                SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                titleLabelString4 = "日期時間錯誤";
                contentLabelString04 = "現在時間: " + sdf7.format(new Date());
                contentLabelString14 = "";
                contentLabelString24 = "現在的日期與時間是不正確的,是否";
                contentLabelString34 = "刪除遊戲記錄並開始新的遊戲?";
                contentLabelString44 = "";
                contentLabelLanguageOffsetY3 = 10.0f;
            } else if (languageString2.equals("zh-CN")) {
                SimpleDateFormat sdf8 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                titleLabelString4 = "日期時間錯誤";
                contentLabelString04 = "现在时间: " + sdf8.format(new Date());
                contentLabelString14 = "";
                contentLabelString24 = "现在的日期与时间是不正确的,是否";
                contentLabelString34 = "删除游戏记录并开始新的游戏?";
                contentLabelString44 = "";
                contentLabelLanguageOffsetY3 = 10.0f;
            } else {
                SimpleDateFormat sdf9 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                titleLabelString4 = "Date and Time Error!";
                contentLabelString04 = "Current Time: " + sdf9.format(new Date());
                contentLabelString14 = "";
                contentLabelString24 = "Do you want to delete current";
                contentLabelString34 = "record and restart new game?";
                contentLabelString44 = "";
                contentLabelLanguageOffsetY3 = 10.0f;
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString4, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString04, contentLabelString14, contentLabelString24, contentLabelString34, contentLabelString44, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY3, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, false);
            this.alertUnitType0.tag = (short) -2;
            popAlert();
            return;
        }
        if (saveIndex >= 0) {
            this.appDelegate.doSoundPoolPlay(5);
            this.alertUnitType0.reset();
            float contentLabelLanguageOffsetY4 = BitmapDescriptorFactory.HUE_RED;
            SimpleDateFormat checkSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date backDate = null;
            String backDateString = "";
            if (this.appDelegate != null && this.appDelegate.mainSavesDictionary != null && this.appDelegate.mainSavesDictionary.timeSavesArrayList != null && this.appDelegate.mainSavesDictionary.timeSavesArrayList.size() > saveIndex) {
                TimeSaveDictionary checkTimeSaveDictionary = this.appDelegate.mainSavesDictionary.timeSavesArrayList.get(saveIndex);
                String originDateString = null;
                if (checkTimeSaveDictionary != null) {
                    originDateString = checkTimeSaveDictionary.getDate();
                }
                if (originDateString != null) {
                    try {
                        backDate = checkSdf.parse(originDateString);
                    } catch (ParseException e2) {
                    }
                }
            }
            String languageString3 = this.appDelegate.getLocaleLanguage();
            if (languageString3.equals("ja-JP")) {
                SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                if (backDate != null) {
                    backDateString = sdf10.format(backDate);
                }
                titleLabelString3 = "タイムエラー";
                contentLabelString03 = "現在時刻: " + sdf10.format(new Date());
                contentLabelString13 = "";
                contentLabelString23 = "端末の日付と時刻が正しくないようです。";
                contentLabelString33 = String.valueOf(backDateString) + "のセーブ";
                contentLabelString43 = "データを再開します。よろしいですか？";
                contentLabelLanguageOffsetY4 = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString3.equals("zh-TW") || languageString3.equals("zh-HK")) {
                SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
                if (backDate != null) {
                    backDateString = sdf11.format(backDate);
                }
                titleLabelString3 = "日期時間錯誤";
                contentLabelString03 = "現在時間:" + sdf11.format(new Date());
                contentLabelString13 = "";
                contentLabelString23 = "現在的日期與時間是不正確的,是否回到";
                contentLabelString33 = String.valueOf(backDateString) + "的記錄";
                contentLabelString43 = "並繼續遊戲?";
                contentLabelLanguageOffsetY4 = BitmapDescriptorFactory.HUE_RED;
            } else if (languageString3.equals("zh-CN")) {
                SimpleDateFormat sdf12 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                if (backDate != null) {
                    backDateString = sdf12.format(backDate);
                }
                titleLabelString3 = "日期時間錯誤";
                contentLabelString03 = "现在时间:" + sdf12.format(new Date());
                contentLabelString13 = "";
                contentLabelString23 = "现在的日期与时间是不正确的,是否回到";
                contentLabelString33 = String.valueOf(backDateString) + "的记录";
                contentLabelString43 = "并继续游戏?";
            } else {
                SimpleDateFormat sdf13 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                if (backDate != null) {
                    backDateString = sdf13.format(new Date());
                }
                titleLabelString3 = "Date and Time Error!";
                contentLabelString03 = "Current Time: " + sdf13.format(new Date());
                contentLabelString13 = "";
                contentLabelString23 = "Do you want to reload previous";
                contentLabelString33 = "save file(" + backDateString + ")?";
                contentLabelString43 = "";
                contentLabelLanguageOffsetY4 = 10.0f;
            }
            this.alertUnitType0.setTitleLabelParams(titleLabelString3, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -6106, 3.0f, -65536, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setContentLabelParams(contentLabelString03, contentLabelString13, contentLabelString23, contentLabelString33, contentLabelString43, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY4, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
            this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, false);
            this.alertUnitType0.tag = (short) 1;
            this.alertUnitType0.subTag = saveIndex;
            popAlert();
            return;
        }
        this.appDelegate.doSoundPoolPlay(4);
        this.alertUnitType0.reset();
        String languageString4 = this.appDelegate.getLocaleLanguage();
        if (languageString4.equals("ja-JP")) {
            SimpleDateFormat sdf14 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            titleLabelString2 = "新規ゲーム開始";
            contentLabelString02 = "現在時刻: " + sdf14.format(new Date());
            contentLabelString12 = "";
            contentLabelString22 = "現在時刻から、新規にゲームを始めます。";
            contentLabelString32 = "よろしいですか？";
            contentLabelString42 = "";
            contentLabelLanguageOffsetY2 = 10.0f;
        } else if (languageString4.equals("zh-TW") || languageString4.equals("zh-HK")) {
            SimpleDateFormat sdf15 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
            titleLabelString2 = "開始新遊戲";
            contentLabelString02 = "";
            contentLabelString12 = "現在時間: " + sdf15.format(new Date());
            contentLabelString22 = "";
            contentLabelString32 = "從現在時間開始新的遊戲?";
            contentLabelString42 = "";
            contentLabelLanguageOffsetY2 = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString4.equals("zh-CN")) {
            SimpleDateFormat sdf16 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            titleLabelString2 = "開始新遊戲";
            contentLabelString02 = "";
            contentLabelString12 = "现在时间: " + sdf16.format(new Date());
            contentLabelString22 = "";
            contentLabelString32 = "从现在时间开始新的游戏?";
            contentLabelString42 = "";
            contentLabelLanguageOffsetY2 = BitmapDescriptorFactory.HUE_RED;
        } else {
            SimpleDateFormat sdf17 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            titleLabelString2 = "Game Start";
            contentLabelString02 = "Current Time: " + sdf17.format(new Date());
            contentLabelString12 = "";
            contentLabelString22 = "Do you want to start a new game";
            contentLabelString32 = "from current date?";
            contentLabelString42 = "";
            contentLabelLanguageOffsetY2 = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString2, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString02, contentLabelString12, contentLabelString22, contentLabelString32, contentLabelString42, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY2, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No), this.appDelegate.getResources().getString(R.string.Yes), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, 1, true);
        this.alertUnitType0.tag = (short) -99;
        popAlert();
    }

    public void doWillTerminate() {
        this.appDelegate.backToMainMenu();
    }

    public void doWillEnterForeground() {
        String checkDateString;
        Log.d("savesCheckLayout", "doWillEnterForeground");
        boolean reloadF = true;
        if (this.appDelegate.timeSaveDictionary != null && (checkDateString = this.appDelegate.timeSaveDictionary.getDate()) != null && checkDateString.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date nowDate = new Date();
            Date checkDate = null;
            try {
                checkDate = sdf.parse(checkDateString);
            } catch (ParseException e) {
            }
            if (checkDate != null) {
                if (checkDate.before(nowDate)) {
                    Log.d("checkTimeSavesGameStart", checkDate + "before" + nowDate);
                    if (checkDate.after(new Date(nowDate.getTime() - (((this.appDelegate.NEED_RELOAD_HOURS * 60) * 60) * 1000)))) {
                        Log.d("checkTimeSavesGameStart", checkDate + "before" + nowDate);
                        reloadF = false;
                    }
                } else {
                    Log.d("checkTimeSavesGameStart", checkDate + "after" + nowDate);
                }
            }
        }
        changeNowStatus(0);
        Log.d("checkTimeSavesGameStart", "reloadF:" + reloadF);
        if (reloadF) {
            checkTimeSavesGameStart();
        } else {
            this.appDelegate.returnToMainGame();
        }
    }

    @SuppressLint({"NewApi", "SimpleDateFormat"})
    public void changeNowStatus(int _newStatus) {
        Log.d("SavesCheckLayout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            this.nowStatus = (short) -1;
            this.alertUnitType0.reset();
            this.savesCheckViewUnit.hidden = false;
            this.hidden = true;
            return;
        }
        if (_newStatus == 0) {
            if (this.nowStatus == -1) {
                this.nowStatus = (short) 0;
                this.alertUnitType0.reset();
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 1) {
            if (this.nowStatus == 0 || this.nowStatus == 1) {
                this.nowStatus = (short) 1;
                this.alertUnitType0.pop();
                this.hidden = false;
                return;
            }
            return;
        }
        if (_newStatus == 2) {
            if (this.nowStatus == 0 || this.nowStatus == 1) {
                this.nowStatus = (short) 2;
                this.alertUnitType0.reset();
                this.appDelegate.goToMainGame();
                this.hidden = true;
            }
        }
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws IOException {
        Log.d("_tag", "_tag" + ((int) _tag));
        Log.d("_subtag", "_subtag" + ((int) _subtag));
        Log.d("_buttonIndex", "_buttonIndex" + ((int) _buttonIndex));
        if (_tag == -100) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            } else {
                if (_buttonIndex == 1) {
                    this.appDelegate.backToMainMenu();
                    return;
                }
                return;
            }
        }
        if (_tag == -99) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            }
            if (_buttonIndex == 1) {
                this.appDelegate.timeSaveDictionary = null;
                if (this.appDelegate.mainSavesDictionary != null) {
                    if (this.appDelegate.mainSavesDictionary.timeSavesArrayList != null) {
                        this.appDelegate.mainSavesDictionary.timeSavesArrayList.clear();
                    }
                } else {
                    this.appDelegate.initMainSavesDictionary();
                }
                this.appDelegate.initTimeSaveDictionary();
                this.appDelegate.doSaveTimeSaveDictionaryOperation();
                Log.d("appDelegate", "getDate =" + this.appDelegate.timeSaveDictionary.getDate());
                Log.d("appDelegate", "getPoint =" + this.appDelegate.timeSaveDictionary.getPoint());
                if (this.appDelegate.defaultSharedPreferences != null) {
                    SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                    editor.putString("time_save_dictionary", "");
                    editor.putString("store_messages_string0", null);
                    editor.putBoolean("init_manual_kitchen", true);
                    editor.putBoolean("init_manual_farm", true);
                    editor.putBoolean("init_manual_store", true);
                    this.appDelegate.set_get_cp_block_date("");
                    editor.putBoolean("gift_tool_2_35", false);
                    editor.commit();
                }
                doInitGame();
                return;
            }
            return;
        }
        if (_tag == -2) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            } else {
                if (_buttonIndex == 1) {
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.SavesCheckViewController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SavesCheckViewController.this.formatAlert();
                        }
                    }, 100L);
                    return;
                }
                return;
            }
        }
        if (_tag == -1) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            }
            if (_buttonIndex == 1) {
                this.appDelegate.timeSaveDictionary = null;
                if (this.appDelegate.mainSavesDictionary != null) {
                    if (this.appDelegate.mainSavesDictionary.timeSavesArrayList != null) {
                        this.appDelegate.mainSavesDictionary.timeSavesArrayList.clear();
                    }
                } else {
                    this.appDelegate.initMainSavesDictionary();
                }
                this.appDelegate.initTimeSaveDictionary();
                this.appDelegate.doSaveTimeSaveDictionaryOperation();
                Log.d("appDelegate", "getDate =" + this.appDelegate.timeSaveDictionary.getDate());
                Log.d("appDelegate", "getPoint =" + this.appDelegate.timeSaveDictionary.getPoint());
                if (this.appDelegate.defaultSharedPreferences != null) {
                    SharedPreferences.Editor editor2 = this.appDelegate.defaultSharedPreferences.edit();
                    editor2.putString("time_save_dictionary", "");
                    editor2.putString("store_messages_string0", null);
                    editor2.putBoolean("init_manual_kitchen", true);
                    editor2.putBoolean("init_manual_farm", true);
                    editor2.putBoolean("init_manual_store", true);
                    this.appDelegate.set_get_cp_block_date("");
                    editor2.putBoolean("gift_tool_2_35", false);
                    editor2.commit();
                }
                doInitGame();
                return;
            }
            return;
        }
        if (_tag == 0) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            }
            if (_buttonIndex == 1) {
                if (_subtag == 0) {
                    normalInitGame(_subtag);
                    return;
                } else {
                    if (_subtag == 1) {
                        normalInitGame(_subtag);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (_tag == 1) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            } else {
                if (_buttonIndex == 1) {
                    this.backSubTag = _subtag;
                    new Handler().postDelayed(new Runnable() { // from class: com.idtinc.ckchickandduck.SavesCheckViewController.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SavesCheckViewController.this.earlierFileAlertWithSubTag(SavesCheckViewController.this.backSubTag);
                        }
                    }, 100L);
                    return;
                }
                return;
            }
        }
        if (_tag == 2) {
            if (_buttonIndex == 0) {
                this.appDelegate.backToMainMenu();
                return;
            }
            if (_buttonIndex == 1) {
                this.appDelegate.timeSaveDictionary = null;
                if (this.appDelegate.mainSavesDictionary == null) {
                    this.appDelegate.initMainSavesDictionary();
                }
                this.appDelegate.deleteTimeSaveDictionaryToIndex(_subtag);
                if (this.appDelegate.mainSavesDictionary.timeSavesArrayList.size() > 0) {
                    TimeSaveDictionary copyTimeSaveDictionary = this.appDelegate.mainSavesDictionary.timeSavesArrayList.get(0);
                    try {
                        this.appDelegate.timeSaveDictionary = copyTimeSaveDictionary.m7clone();
                    } catch (CloneNotSupportedException e) {
                        Log.d("CloneNotSupportedException", "CloneNotSupportedException CloneNotSupportedException");
                    }
                    if (this.appDelegate != null) {
                        if (this.appDelegate.timeSaveDictionary != null) {
                            this.appDelegate.timeSaveDictionary.checkTimeSaveDictionaryWithAppDelegate(this.appDelegate);
                        }
                        this.appDelegate.doSaveTimeSaveDictionaryOperation();
                        Log.d("appDelegate", "getDate =" + this.appDelegate.timeSaveDictionary.getDate());
                        Log.d("appDelegate", "getPoint =" + this.appDelegate.timeSaveDictionary.getPoint());
                    } else {
                        return;
                    }
                } else {
                    if (this.appDelegate.mainSavesDictionary != null) {
                        if (this.appDelegate.mainSavesDictionary.timeSavesArrayList != null) {
                            this.appDelegate.mainSavesDictionary.timeSavesArrayList.clear();
                        }
                    } else {
                        this.appDelegate.initMainSavesDictionary();
                    }
                    this.appDelegate.initTimeSaveDictionary();
                    this.appDelegate.doSaveTimeSaveDictionaryOperation();
                    Log.d("appDelegate", "getDate =" + this.appDelegate.timeSaveDictionary.getDate());
                    Log.d("appDelegate", "getPoint =" + this.appDelegate.timeSaveDictionary.getPoint());
                }
                if (this.appDelegate.defaultSharedPreferences != null) {
                    SharedPreferences.Editor editor3 = this.appDelegate.defaultSharedPreferences.edit();
                    editor3.putString("time_save_dictionary", "");
                    editor3.putString("store_messages_string0", null);
                    editor3.putBoolean("gift_tool_2_35", false);
                    editor3.commit();
                }
                doInitGame();
            }
        }
    }

    public void normalInitGame(int _loadTypeIndex) throws IOException {
        if (this.appDelegate != null) {
            if (_loadTypeIndex == 0) {
                this.appDelegate.loadTimeSaveDictionary();
                if (this.appDelegate.timeSaveDictionary == null) {
                    this.appDelegate.initTimeSaveDictionary();
                }
            } else if (_loadTypeIndex == 1 && this.appDelegate.defaultSharedPreferences != null) {
                String time_save_dictionary_String = this.appDelegate.defaultSharedPreferences.getString("time_save_dictionary", "");
                if (time_save_dictionary_String.length() > 10) {
                    this.appDelegate.timeSaveDictionary = new TimeSaveDictionary(this.appDelegate, time_save_dictionary_String);
                }
            }
            if (this.appDelegate.timeSaveDictionary != null) {
                this.appDelegate.timeSaveDictionary.checkTimeSaveDictionaryWithAppDelegate(this.appDelegate);
            }
            doInitGame();
        }
    }

    public void doInitGame() {
        goToMainGame();
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.alertUnitType0 != null) {
            this.alertUnitType0.gameOnTouch(event);
            return false;
        }
        return false;
    }

    public void gameDraw(Canvas canvas) {
        if (!this.hidden && this.appDelegate != null) {
            if (this.savesCheckViewUnit != null && !this.savesCheckViewUnit.hidden) {
                this.savesCheckViewUnit.gameDraw(canvas);
            }
            if (this.alertUnitType0 != null && !this.alertUnitType0.hidden) {
                this.alertUnitType0.gameDraw(canvas);
            }
        }
    }

    public void onDestroy() {
        if (this.alertUnitType0 != null) {
            this.alertUnitType0.onDestroy();
            this.alertUnitType0 = null;
        }
        if (this.savesCheckViewUnit != null) {
            this.savesCheckViewUnit.onDestroy();
            this.savesCheckViewUnit = null;
        }
        this.appDelegate = null;
    }
}
