package com.idtinc.maingame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ck_bonus.BonusPage;
import com.idtinc.ck_bonus.BonusUnitView;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;
import com.idtinc.ckchickandduck.R;
import com.idtinc.maingame.sublayout0.MainGameUnit;
import com.idtinc.maingame.sublayout1.FarmUnit;
import com.idtinc.maingame.sublayout2.StoreUnit;
import com.idtinc.maingame.sublayout3.HelpUnit;
import com.idtinc.manual.ManualLayout;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class MainGameViewController extends RelativeLayout {
    private AppDelegate appDelegate;
    public BonusUnitView bonusUnitView;
    private short checkGameLoopCount;
    private FarmUnit farmUnit;
    private float finalHeight;
    private float finalWidth;
    private HelpUnit helpUnit;
    private MainGameUnit mainGameUnit;
    public ManualLayout manualLayout;
    private Boolean needSaveMainSavesF;
    private Boolean needSaveTimeSaveF;
    public short nowStatus;
    private short saveCnt;
    private StoreUnit storeUnit;
    private TopButtonsUnit topButtonsUnit;
    private float zoomRate;

    public MainGameViewController(Context context, float _finalwidth, float _finalheight, float _zoomrate, AppMainActivity _appMainActivity) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowStatus = (short) -1;
        this.needSaveTimeSaveF = false;
        this.needSaveMainSavesF = false;
        this.checkGameLoopCount = (short) 0;
        this.saveCnt = (short) 0;
        this.appDelegate = null;
        this.helpUnit = null;
        this.storeUnit = null;
        this.farmUnit = null;
        this.mainGameUnit = null;
        this.topButtonsUnit = null;
        this.manualLayout = null;
        this.bonusUnitView = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.needSaveTimeSaveF = false;
        this.needSaveMainSavesF = false;
        this.checkGameLoopCount = (short) 0;
        this.saveCnt = (short) 0;
        this.nowStatus = (short) -1;
        this.mainGameUnit = new MainGameUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.mainGameUnit.reset();
        initFarmUnit();
        initStoreUnit();
        initHelpLayout();
        this.topButtonsUnit = new TopButtonsUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.topButtonsUnit.reset();
        changeNowStatus(-1);
    }

    public void initFarmUnit() {
        if (this.farmUnit == null) {
            this.farmUnit = new FarmUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
            this.farmUnit.hidden = true;
            this.farmUnit.reset();
            this.farmUnit.doInit();
        }
    }

    public void initStoreUnit() {
        if (this.storeUnit == null) {
            this.storeUnit = new StoreUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
            this.storeUnit.hidden = true;
            this.storeUnit.reset();
            this.storeUnit.doInit();
            if (this.storeUnit.storeBackViewUnit.giftInputEditText != null) {
                RelativeLayout.LayoutParams giftInputEditTextLayoutParams = new RelativeLayout.LayoutParams((int) (186.0f * this.zoomRate), (int) (34.0f * this.zoomRate));
                giftInputEditTextLayoutParams.setMargins((int) (51.0f * this.zoomRate), (int) (44.0f * this.zoomRate), 0, 0);
                addView(this.storeUnit.storeBackViewUnit.giftInputEditText, giftInputEditTextLayoutParams);
            }
        }
    }

    public void initHelpLayout() {
        if (this.helpUnit == null) {
            this.helpUnit = new HelpUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
            this.helpUnit.hidden = true;
            this.helpUnit.reset();
            this.helpUnit.doInit();
        }
    }

    public void initManualLayout() {
        if (this.manualLayout == null) {
            this.manualLayout = new ManualLayout(getContext(), this.finalWidth, this.finalHeight, this.zoomRate, this);
            this.manualLayout.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            this.manualLayout.setVisibility(8);
            addView(this.manualLayout, (int) this.finalWidth, (int) this.finalHeight);
        }
    }

    public void initBonusUnitLayout() {
        if (this.bonusUnitView == null) {
            this.bonusUnitView = new BonusUnitView(getContext(), this.finalWidth, this.finalHeight, this.zoomRate, this);
            this.bonusUnitView.setBackgroundColor(FluctConstants.FRAME_ALPHA_COLOR);
            this.bonusUnitView.setVisibility(8);
            addView(this.bonusUnitView, (int) this.finalWidth, (int) this.finalHeight);
        }
    }

    public void nowRefreshAndSave() {
        if (this.appDelegate.doSaveTimeSaveDictionaryOperation()) {
            this.needSaveTimeSaveF = false;
            Log.d("MainGameControllerLayout", "appMainActivity.doSaveTimeSaveDictionaryOperation()");
        } else {
            this.needSaveTimeSaveF = true;
        }
        if (this.appDelegate.doSaveMainSavesDictionaryOperationWithType((short) 1)) {
            this.needSaveMainSavesF = false;
            Log.d("MainGameControllerLayout", "appMainActivity.doSaveMainSavesDictionaryOperationWithType((short)1) ");
        } else {
            this.needSaveMainSavesF = true;
        }
    }

    public void refreshAndSave() {
        this.needSaveTimeSaveF = true;
        this.needSaveMainSavesF = true;
    }

    public void doInit() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        changeNowStatus(0);
        if (this.helpUnit != null) {
            this.helpUnit.doInit();
        }
        if (this.farmUnit != null) {
            this.farmUnit.doInit();
        }
        if (this.storeUnit != null) {
            this.storeUnit.doInit();
        }
        if (this.mainGameUnit != null) {
            this.mainGameUnit.doInit();
        }
        if (this.topButtonsUnit != null) {
            this.topButtonsUnit.start();
            this.topButtonsUnit.selectButtonWithIndex((short) 0, (short) 1);
        }
        this.checkGameLoopCount = (short) 0;
        this.saveCnt = (short) 0;
        refreshAndSave();
        startGame();
    }

    public void returnToMainGame() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        doWillEnterForeground();
    }

    public void startGame() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        changeNowStatus(0);
        checkGetBonus();
    }

    public void timeoutGame() {
        changeNowStatus(-1);
    }

    public void doBackToMainMenu() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        this.topButtonsUnit.selectButtonWithIndex((short) 0, (short) 1);
        doWillTerminate();
        if (this.appDelegate != null) {
            this.appDelegate.backToMainMenu();
        }
    }

    public void doWillTerminate() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.nowStatus == 0) {
            nowRefreshAndSave();
            timeoutGame();
        }
        doBonusLayoutHidden(false);
        doManualLayoutHidden(false);
        if (this.mainGameUnit != null) {
            this.mainGameUnit.doWillTerminate();
        }
        if (this.farmUnit != null) {
            this.farmUnit.doWillTerminate();
        }
        if (this.storeUnit != null) {
            this.storeUnit.doWillTerminate();
        }
        if (this.helpUnit != null) {
            this.helpUnit.doWillTerminate();
        }
        if (this.nowStatus == -1) {
            this.appDelegate.backToSavesCheck();
        }
        if (this.appDelegate != null) {
            this.appDelegate.doBGMStop();
        }
    }

    public void doWillEnterForeground() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        Log.d("doWillEnterForeground", "doWillEnterForeground ggg");
        if (this.appDelegate.getNowStatus() == 1) {
            if (this.mainGameUnit != null) {
                this.mainGameUnit.doWillEnterForeground();
            }
            if (this.farmUnit != null) {
                this.farmUnit.doWillEnterForeground();
            }
            if (this.storeUnit != null) {
                this.storeUnit.doWillEnterForeground();
            }
            if (this.helpUnit != null) {
                this.helpUnit.doWillEnterForeground();
            }
            this.checkGameLoopCount = (short) 0;
            this.saveCnt = (short) 0;
            refreshAndSave();
            startGame();
            doViewChange();
        }
    }

    public void changeNowStatus(int _newStatus) {
        Log.d("MainGameControllerLayout", "changeNowStatus " + _newStatus);
        if (_newStatus == -1) {
            setVisibility(8);
            this.nowStatus = (short) -1;
            this.checkGameLoopCount = (short) 0;
            this.saveCnt = (short) 0;
            return;
        }
        if (_newStatus == 0 && this.nowStatus == -1) {
            this.nowStatus = (short) 0;
            setVisibility(0);
        }
    }

    public void doLoop() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.nowStatus == 0) {
            if (this.bonusUnitView != null && this.bonusUnitView.getVisibility() == 0) {
                this.bonusUnitView.doLoop();
            }
            if (this.manualLayout != null && this.manualLayout.getVisibility() == 0) {
                this.manualLayout.doLoop();
            }
            if (this.topButtonsUnit != null) {
                this.topButtonsUnit.doLoop();
                if (this.topButtonsUnit.nowButtonIndex == 0 && this.mainGameUnit != null && !this.mainGameUnit.hidden) {
                    this.mainGameUnit.animeGameLoop();
                }
                this.checkGameLoopCount = (short) (this.checkGameLoopCount - 1);
                if (this.checkGameLoopCount <= 0) {
                    this.checkGameLoopCount = this.appDelegate.CHECK_GAME_LOOP_CNT;
                    checkGetCoins();
                    if (this.mainGameUnit != null) {
                        this.mainGameUnit.checkGameLoop();
                    }
                    if (this.needSaveTimeSaveF.booleanValue()) {
                        if (this.appDelegate.doSaveTimeSaveDictionaryOperation()) {
                            this.needSaveTimeSaveF = false;
                        } else {
                            this.needSaveTimeSaveF = true;
                        }
                    }
                    this.saveCnt = (short) (this.saveCnt - 1);
                    if (this.saveCnt <= 0) {
                        this.saveCnt = this.appDelegate.SAVE_CNT_MAX;
                        if (this.needSaveMainSavesF.booleanValue()) {
                            if (this.appDelegate.doSaveMainSavesDictionaryOperationWithType((short) 1)) {
                                this.needSaveMainSavesF = false;
                            } else {
                                this.needSaveMainSavesF = true;
                            }
                        }
                    }
                    if (this.topButtonsUnit.nowButtonIndex == 3 && this.helpUnit != null && !this.helpUnit.hidden) {
                        this.helpUnit.doLoop();
                    }
                }
                if (this.topButtonsUnit.nowButtonIndex == 1 && this.farmUnit != null && !this.farmUnit.hidden) {
                    this.farmUnit.doLoop();
                }
            }
        }
    }

    public void doViewChange() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.appDelegate != null) {
            this.appDelegate.doBGMStop();
            if (this.topButtonsUnit != null) {
                if (this.helpUnit != null) {
                    this.helpUnit.hidden = true;
                }
                if (this.storeUnit != null) {
                    this.storeUnit.hidden = true;
                }
                if (this.farmUnit != null) {
                    this.farmUnit.hidden = true;
                }
                if (this.mainGameUnit != null) {
                    this.mainGameUnit.hidden = true;
                }
                if (this.topButtonsUnit.oldButtonIndex == 0) {
                    if (this.mainGameUnit != null) {
                        this.mainGameUnit.hidden = true;
                        this.mainGameUnit.doHidden();
                    }
                } else if (this.topButtonsUnit.oldButtonIndex == 1) {
                    if (this.farmUnit != null) {
                        this.farmUnit.hidden = true;
                        this.farmUnit.doHidden();
                    }
                } else if (this.topButtonsUnit.oldButtonIndex == 2) {
                    if (this.storeUnit != null) {
                        this.storeUnit.hidden = true;
                        this.storeUnit.doHidden();
                    }
                } else if (this.topButtonsUnit.oldButtonIndex == 3 && this.helpUnit != null) {
                    this.helpUnit.hidden = true;
                    this.helpUnit.doHidden();
                }
                if (this.topButtonsUnit.nowButtonIndex == 0) {
                    if (this.mainGameUnit != null) {
                        this.mainGameUnit.hidden = false;
                        this.mainGameUnit.doDisplay();
                        this.appDelegate.doBGMPlay(0);
                    }
                } else if (this.topButtonsUnit.nowButtonIndex == 1) {
                    if (this.farmUnit != null) {
                        this.farmUnit.hidden = false;
                        this.farmUnit.doDisplay();
                        this.appDelegate.doBGMPlay(1);
                    }
                } else if (this.topButtonsUnit.nowButtonIndex == 2) {
                    if (this.storeUnit != null) {
                        this.storeUnit.hidden = false;
                        this.storeUnit.doDisplay();
                        if (this.topButtonsUnit.oldButtonIndex != 2) {
                            this.storeUnit.aotoOpenBonusPage();
                        }
                        this.appDelegate.doBGMPlay(2);
                    }
                } else if (this.topButtonsUnit.nowButtonIndex == 3 && this.helpUnit != null) {
                    this.helpUnit.hidden = false;
                    this.helpUnit.doDisplay();
                }
                this.topButtonsUnit.hidden = false;
                this.topButtonsUnit.refreshControlF();
                this.topButtonsUnit.oldButtonIndex = this.topButtonsUnit.nowButtonIndex;
            }
        }
    }

    public void doManualLayoutDisplay(short _manualIndex) {
        if (this.manualLayout == null) {
            initManualLayout();
        }
        this.manualLayout.changeManualIndex(_manualIndex);
        this.manualLayout.setVisibility(0);
        if (this.bonusUnitView != null) {
            this.bonusUnitView.doHidden();
        }
        if (this.topButtonsUnit != null) {
            this.topButtonsUnit.refreshControlF();
        }
        Animation translateAnimation = new TranslateAnimation(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalHeight, BitmapDescriptorFactory.HUE_RED);
        translateAnimation.setDuration(400L);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.maingame.MainGameViewController.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (MainGameViewController.this.helpUnit != null) {
                    MainGameViewController.this.helpUnit.hidden = true;
                }
                if (MainGameViewController.this.storeUnit != null) {
                    MainGameViewController.this.storeUnit.hidden = true;
                }
                if (MainGameViewController.this.farmUnit != null) {
                    MainGameViewController.this.farmUnit.hidden = true;
                }
                if (MainGameViewController.this.mainGameUnit != null) {
                    MainGameViewController.this.mainGameUnit.hidden = true;
                }
                if (MainGameViewController.this.topButtonsUnit != null) {
                    MainGameViewController.this.topButtonsUnit.hidden = true;
                }
            }
        });
        this.manualLayout.startAnimation(translateAnimation);
    }

    public void doManualLayoutHidden(boolean animeF) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.manualLayout != null && this.manualLayout.getVisibility() == 0) {
            this.manualLayout.setVisibility(8);
            doViewChange();
            if (animeF) {
                Animation translateAnimation = new TranslateAnimation(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalHeight);
                translateAnimation.setDuration(500L);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.maingame.MainGameViewController.2
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        MainGameViewController.this.manualLayout.doHidden();
                    }
                });
                this.manualLayout.startAnimation(translateAnimation);
            }
        }
    }

    public void goToGiftPage(String gift_key, String gift_url) {
        if (gift_url != null && gift_url.length() >= 10) {
            if (this.appDelegate.defaultSharedPreferences != null) {
                SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                editor.putBoolean("gift_tool_2_35", true);
                editor.commit();
            }
            Uri uri = Uri.parse(gift_url);
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            getContext().startActivity(intent);
        }
    }

    public void goToGetWithIndex(BonusPage _bonusPage, short type) {
        String titleString;
        String contentString;
        String titleString2;
        String contentString2;
        String titleString3;
        String contentString3;
        if (type == 0) {
            if (this.appDelegate != null) {
                if (this.appDelegate.checkInterNet()) {
                    if (_bonusPage.openType == 1000) {
                        if (this.appDelegate.checkAdColonyV4VCF()) {
                            this.appDelegate.showAdColonyV4VCF();
                            if (this.appDelegate.defaultSharedPreferences != null) {
                                SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                                editor.putBoolean("get_coins_active", false);
                                String languageString = this.appDelegate.getLocaleLanguage();
                                if (languageString.equals("ja-JP")) {
                                    titleString3 = "cp獲得";
                                    contentString3 = String.valueOf(_bonusPage.bonus) + "cp獲得しました。";
                                } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
                                    titleString3 = "獲得cp";
                                    contentString3 = "獲得了" + _bonusPage.bonus + "cp。";
                                } else if (languageString.equals("zh-CN")) {
                                    titleString3 = "获得cp";
                                    contentString3 = "获得了" + _bonusPage.bonus + "cp。";
                                } else {
                                    titleString3 = "Got cp";
                                    contentString3 = "You got " + _bonusPage.bonus + "cp.";
                                }
                                editor.putString("get_coins_title", titleString3);
                                editor.putString("get_coins_content", contentString3);
                                editor.putInt("get_coins_coins", _bonusPage.bonus);
                                editor.commit();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (_bonusPage.openType == 100) {
                        if (_bonusPage.openDateKeyString != null && _bonusPage.openDateKeyString.length() >= 3 && _bonusPage.bonus >= 0 && this.appDelegate.defaultSharedPreferences != null) {
                            SharedPreferences.Editor editor2 = this.appDelegate.defaultSharedPreferences.edit();
                            editor2.putBoolean("get_bonus_active", false);
                            editor2.putString("get_bonus_openDateKey", _bonusPage.openDateKeyString);
                            String languageString2 = this.appDelegate.getLocaleLanguage();
                            if (languageString2.equals("ja-JP")) {
                                titleString2 = "cp獲得";
                                contentString2 = String.valueOf(_bonusPage.bonus) + "cp獲得しました。";
                            } else if (languageString2.equals("zh-TW") || languageString2.equals("zh-HK")) {
                                titleString2 = "獲得cp";
                                contentString2 = "獲得了" + _bonusPage.bonus + "cp。";
                            } else if (languageString2.equals("zh-CN")) {
                                titleString2 = "获得cp";
                                contentString2 = "获得了" + _bonusPage.bonus + "cp。";
                            } else {
                                titleString2 = "Got cp";
                                contentString2 = "You got " + _bonusPage.bonus + "cp.";
                            }
                            editor2.putString("get_bonus_title", titleString2);
                            editor2.putString("get_bonus_content", contentString2);
                            editor2.putInt("get_bonus_bonus", _bonusPage.bonus);
                            editor2.commit();
                        }
                        this.appDelegate.readyDoShareImage((short) 0);
                        return;
                    }
                    if (_bonusPage.urlString != null && _bonusPage.urlString.length() >= 10) {
                        Uri uri = Uri.parse(_bonusPage.urlString);
                        Intent intent = new Intent("android.intent.action.VIEW", uri);
                        getContext().startActivity(intent);
                        if (_bonusPage.openDateKeyString != null && _bonusPage.openDateKeyString.length() >= 3 && _bonusPage.bonus >= 0 && this.appDelegate.defaultSharedPreferences != null) {
                            SharedPreferences.Editor editor3 = this.appDelegate.defaultSharedPreferences.edit();
                            editor3.putBoolean("get_bonus_active", true);
                            editor3.putString("get_bonus_openDateKey", _bonusPage.openDateKeyString);
                            String languageString3 = this.appDelegate.getLocaleLanguage();
                            if (languageString3.equals("ja-JP")) {
                                titleString = "cp獲得";
                                contentString = String.valueOf(_bonusPage.bonus) + "cp獲得しました。";
                            } else if (languageString3.equals("zh-TW") || languageString3.equals("zh-HK")) {
                                titleString = "獲得cp";
                                contentString = "獲得了" + _bonusPage.bonus + "cp。";
                            } else if (languageString3.equals("zh-CN")) {
                                titleString = "获得cp";
                                contentString = "获得了" + _bonusPage.bonus + "cp。";
                            } else {
                                titleString = "Got cp";
                                contentString = "You got " + _bonusPage.bonus + "cp.";
                            }
                            editor3.putString("get_bonus_title", titleString);
                            editor3.putString("get_bonus_content", contentString);
                            editor3.putInt("get_bonus_bonus", _bonusPage.bonus);
                            editor3.commit();
                            return;
                        }
                        return;
                    }
                    return;
                }
                this.appDelegate.showNoInternetAlertDialog();
                return;
            }
            return;
        }
        if (this.appDelegate != null) {
            if (this.appDelegate.checkInterNet()) {
                if (_bonusPage.openType == 100) {
                    this.appDelegate.readyDoShareImage((short) 0);
                    return;
                } else {
                    if (_bonusPage.urlString != null && _bonusPage.urlString.length() >= 10) {
                        Uri uri2 = Uri.parse(_bonusPage.urlString);
                        Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                        getContext().startActivity(intent2);
                        return;
                    }
                    return;
                }
            }
            this.appDelegate.showNoInternetAlertDialog();
        }
    }

    public void goToSlosPage() {
        if (this.appDelegate != null) {
            this.appDelegate.goToSlosPage();
        }
    }

    public boolean checkGetCoins() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        boolean getCoinsF = false;
        if (this.appDelegate.timeSaveDictionary == null) {
            return false;
        }
        if (this.appDelegate.defaultSharedPreferences != null) {
            if (!this.appDelegate.defaultSharedPreferences.getBoolean("get_coins_active", false)) {
                return false;
            }
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
            int gotCoins = this.appDelegate.defaultSharedPreferences.getInt("get_coins_coins", -1);
            if (gotCoins >= 0) {
                getCoinsF = true;
                if (1 != 0) {
                    float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                    if (nowPoint < BitmapDescriptorFactory.HUE_RED) {
                        nowPoint = BitmapDescriptorFactory.HUE_RED;
                    }
                    this.appDelegate.timeSaveDictionary.setPoint(nowPoint + gotCoins);
                    refreshAndSave();
                    doViewChange();
                    new AlertDialog.Builder(getContext()).setTitle(this.appDelegate.defaultSharedPreferences.getString("get_coins_title", "")).setMessage(this.appDelegate.defaultSharedPreferences.getString("get_coins_content", "")).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() { // from class: com.idtinc.maingame.MainGameViewController.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            MainGameViewController.this.appDelegate.doSoundPoolPlay(1);
                        }
                    }).show();
                    if (getVisibility() == 0) {
                        this.appDelegate.doSoundPoolPlay(8);
                    }
                }
            }
            editor.putBoolean("get_coins_active", false);
            editor.putString("get_coins_title", "");
            editor.putString("get_coins_content", "");
            editor.putInt("get_coins_coins", -1);
            editor.commit();
        }
        return getCoinsF;
    }

    public boolean checkGetBonus() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        boolean getBonusF = false;
        if (this.appDelegate.timeSaveDictionary == null) {
            return false;
        }
        if (this.appDelegate.defaultSharedPreferences != null) {
            if (!this.appDelegate.defaultSharedPreferences.getBoolean("get_bonus_active", false)) {
                return false;
            }
            SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
            int gotBonus = this.appDelegate.defaultSharedPreferences.getInt("get_bonus_bonus", -1);
            if (gotBonus >= 0) {
                String openDateKeyString = this.appDelegate.defaultSharedPreferences.getString("get_bonus_openDateKey", "");
                if (openDateKeyString.length() >= 3) {
                    String openDateString = this.appDelegate.defaultSharedPreferences.getString(openDateKeyString, "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    SimpleDateFormat sdfday = new SimpleDateFormat("yyyy/MM/dd");
                    Date nowDate = new Date();
                    if (openDateString.length() >= 0) {
                        String openDateDayString = "";
                        try {
                            openDateDayString = sdfday.format(sdf.parse(openDateString));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String nowDateString = sdfday.format(nowDate);
                        if (openDateDayString.equals(nowDateString)) {
                            editor.putString(openDateKeyString, sdf.format(nowDate));
                        } else {
                            editor.putString(openDateKeyString, sdf.format(nowDate));
                            getBonusF = true;
                        }
                    } else {
                        editor.putString(openDateKeyString, sdf.format(nowDate));
                        getBonusF = true;
                    }
                }
                if (getBonusF) {
                    if (this.appDelegate != null) {
                        this.appDelegate.displayFullAdView();
                    }
                    float nowPoint = this.appDelegate.timeSaveDictionary.getPoint();
                    if (nowPoint < BitmapDescriptorFactory.HUE_RED) {
                        nowPoint = BitmapDescriptorFactory.HUE_RED;
                    }
                    this.appDelegate.timeSaveDictionary.setPoint(nowPoint + gotBonus);
                    refreshAndSave();
                    doViewChange();
                    new AlertDialog.Builder(getContext()).setTitle(this.appDelegate.defaultSharedPreferences.getString("get_bonus_title", "")).setMessage(this.appDelegate.defaultSharedPreferences.getString("get_bonus_content", "")).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() { // from class: com.idtinc.maingame.MainGameViewController.4
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            MainGameViewController.this.appDelegate.doSoundPoolPlay(1);
                        }
                    }).show();
                    if (getVisibility() == 0) {
                        this.appDelegate.doSoundPoolPlay(8);
                    }
                }
            }
            editor.putBoolean("get_bonus_active", false);
            editor.putString("get_bonus_openDateKey", "");
            editor.putString("get_bonus_title", "");
            editor.putString("get_bonus_content", "");
            editor.putInt("get_bonus_bonus", -1);
            editor.commit();
        }
        return getBonusF;
    }

    public void doBonusLayoutDisplay() throws IOException {
        initBonusUnitLayout();
        if (this.bonusUnitView != null) {
            this.bonusUnitView.doDisplay();
            if (this.manualLayout != null) {
                this.manualLayout.doHidden();
                this.manualLayout.setVisibility(8);
            }
            if (this.topButtonsUnit != null) {
                this.topButtonsUnit.refreshControlF();
            }
            if (this.manualLayout != null && this.manualLayout.getVisibility() == 0) {
                this.manualLayout.doHidden();
                this.manualLayout.setVisibility(8);
            }
            Animation translateAnimation = new TranslateAnimation(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalHeight, BitmapDescriptorFactory.HUE_RED);
            translateAnimation.setDuration(400L);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.maingame.MainGameViewController.5
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    if (MainGameViewController.this.helpUnit != null) {
                        MainGameViewController.this.helpUnit.hidden = true;
                    }
                    if (MainGameViewController.this.storeUnit != null) {
                        MainGameViewController.this.storeUnit.hidden = true;
                    }
                    if (MainGameViewController.this.farmUnit != null) {
                        MainGameViewController.this.farmUnit.hidden = true;
                    }
                    if (MainGameViewController.this.mainGameUnit != null) {
                        MainGameViewController.this.mainGameUnit.hidden = true;
                    }
                    if (MainGameViewController.this.topButtonsUnit != null) {
                        MainGameViewController.this.topButtonsUnit.hidden = true;
                    }
                }
            });
            this.bonusUnitView.startAnimation(translateAnimation);
        }
    }

    public void doBonusLayoutHidden(boolean animeF) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.bonusUnitView != null && this.bonusUnitView.getVisibility() == 0) {
            this.bonusUnitView.doHidden();
            doViewChange();
            if (animeF) {
                Animation translateAnimation = new TranslateAnimation(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, this.finalHeight);
                translateAnimation.setDuration(500L);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.idtinc.maingame.MainGameViewController.6
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                this.bonusUnitView.startAnimation(translateAnimation);
            }
        }
    }

    public void hiddenSubViews() {
        if (this.storeUnit.storeBackViewUnit.giftInputEditText != null) {
            this.storeUnit.storeBackViewUnit.hiddenSoftInputGiftInputEditText();
            this.storeUnit.storeBackViewUnit.giftInputEditText.setVisibility(8);
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.bonusUnitView != null && this.bonusUnitView.getVisibility() == 0) {
            return true;
        }
        if (this.manualLayout != null && this.manualLayout.getVisibility() == 0) {
            return true;
        }
        if (this.topButtonsUnit == null) {
            return false;
        }
        boolean returnF = this.topButtonsUnit.gameOnTouch(event);
        if (returnF) {
            return returnF;
        }
        if (this.topButtonsUnit.nowButtonIndex == 0) {
            if (this.mainGameUnit != null && !this.mainGameUnit.hidden) {
                this.mainGameUnit.gameOnTouch(event);
            }
        } else if (this.topButtonsUnit.nowButtonIndex == 1) {
            if (this.farmUnit != null && !this.farmUnit.hidden) {
                this.farmUnit.gameOnTouch(event);
            }
        } else if (this.topButtonsUnit.nowButtonIndex == 2) {
            if (this.storeUnit != null && !this.storeUnit.hidden) {
                this.storeUnit.gameOnTouch(event);
            }
        } else if (this.topButtonsUnit.nowButtonIndex == 3 && this.helpUnit != null && !this.helpUnit.hidden) {
            this.helpUnit.gameOnTouch(event);
        }
        return returnF;
    }

    public void gameDraw(Canvas canvas) {
        if (this.topButtonsUnit != null) {
            if (this.topButtonsUnit.nowButtonIndex == 0) {
                if (this.mainGameUnit != null && !this.mainGameUnit.hidden) {
                    this.mainGameUnit.gameDraw(canvas);
                }
            } else if (this.topButtonsUnit.nowButtonIndex == 1) {
                if (this.farmUnit != null && !this.farmUnit.hidden) {
                    this.farmUnit.gameDraw(canvas);
                }
            } else if (this.topButtonsUnit.nowButtonIndex == 2) {
                if (this.storeUnit != null && !this.storeUnit.hidden) {
                    this.storeUnit.gameDraw(canvas);
                }
            } else if (this.topButtonsUnit.nowButtonIndex == 3 && this.helpUnit != null && !this.helpUnit.hidden) {
                this.helpUnit.gameDraw(canvas);
            }
            this.topButtonsUnit.gameDraw(canvas);
        }
    }

    public void onDestroy() {
        if (this.bonusUnitView != null) {
            this.bonusUnitView.onDestroy();
            this.bonusUnitView = null;
        }
        if (this.manualLayout != null) {
            this.manualLayout.onDestroy();
            this.manualLayout = null;
        }
        if (this.helpUnit != null) {
            this.helpUnit.onDestroy();
            this.helpUnit = null;
        }
        if (this.storeUnit != null) {
            this.storeUnit.onDestroy();
            this.storeUnit = null;
        }
        if (this.farmUnit != null) {
            this.farmUnit.onDestroy();
            this.farmUnit = null;
        }
        if (this.mainGameUnit != null) {
            this.mainGameUnit.onDestroy();
            this.mainGameUnit = null;
        }
        if (this.topButtonsUnit != null) {
            this.topButtonsUnit.onDestroy();
            this.topButtonsUnit = null;
        }
        this.appDelegate = null;
    }
}
