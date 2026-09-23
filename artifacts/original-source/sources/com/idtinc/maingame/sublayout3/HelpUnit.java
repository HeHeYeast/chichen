package com.idtinc.maingame.sublayout3;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.R;
import com.idtinc.custom.AlertUnitType0;
import com.idtinc.custom.AlertUnitType0Delegate;
import com.idtinc.maingame.MainGameViewController;
import java.io.File;
import java.io.IOException;
import jp.co.voyagegroup.android.fluct.jar.util.FluctConstants;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class HelpUnit implements AlertUnitType0Delegate {
    private float BIGBACKVIEW_HEIGHT;
    private float BIGBACKVIEW_OFFSET_X;
    private float BIGBACKVIEW_OFFSET_Y;
    private float BIGBACKVIEW_WIDTH;
    private float SCROLLVIEW_CONTENT_HEIGHT;
    private float SCROLLVIEW_HEIGHT;
    private float SCROLLVIEW_OFFSET_X;
    private float SCROLLVIEW_OFFSET_Y;
    private float SCROLLVIEW_WIDTH;
    private AlertUnitType0 alertUnitType0;
    private AppDelegate appDelegate;
    private float finalHeight;
    private float finalWidth;
    private HelpBackViewUnit helpBackViewUnit;
    private HelpFrontViewUnit helpFrontViewUnit;
    private HelpScrollViewUnit helpScrollViewUnit;
    public boolean hidden;
    private MainGameViewController mainGameControllerLayout;
    public short nowStatus;
    private float preScrollX = -9999.0f;
    private float preScrollY = -9999.0f;
    private boolean soundToggleButtonCheckedSoundF = true;
    private float zoomRate;

    public HelpUnit(float _finalwidth, float _finalheight, float _zoomrate, MainGameViewController _mainGameControllerLayout, AppDelegate _appDelegate) {
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.BIGBACKVIEW_OFFSET_X = 21.0f;
        this.BIGBACKVIEW_OFFSET_Y = 40.0f;
        this.BIGBACKVIEW_WIDTH = 278.0f;
        this.BIGBACKVIEW_HEIGHT = 415.0f - this.BIGBACKVIEW_OFFSET_Y;
        this.SCROLLVIEW_OFFSET_X = this.BIGBACKVIEW_OFFSET_X + 4.0f;
        this.SCROLLVIEW_OFFSET_Y = this.BIGBACKVIEW_OFFSET_Y + 9.0f;
        this.SCROLLVIEW_WIDTH = this.BIGBACKVIEW_WIDTH - 8.0f;
        this.SCROLLVIEW_HEIGHT = this.BIGBACKVIEW_HEIGHT - 18.0f;
        this.SCROLLVIEW_CONTENT_HEIGHT = 790.0f;
        this.appDelegate = _appDelegate;
        this.mainGameControllerLayout = _mainGameControllerLayout;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.hidden = false;
        this.nowStatus = (short) -1;
        this.BIGBACKVIEW_OFFSET_X = 21.0f * this.zoomRate;
        this.BIGBACKVIEW_OFFSET_Y = 40.0f * this.zoomRate;
        this.BIGBACKVIEW_WIDTH = 278.0f * this.zoomRate;
        if (!this.appDelegate.isRetina4) {
            this.BIGBACKVIEW_HEIGHT = (415.0f * this.zoomRate) - this.BIGBACKVIEW_OFFSET_Y;
        } else {
            this.BIGBACKVIEW_HEIGHT = (503.0f * this.zoomRate) - this.BIGBACKVIEW_OFFSET_Y;
        }
        this.SCROLLVIEW_OFFSET_X = this.BIGBACKVIEW_OFFSET_X + (4.0f * this.zoomRate);
        this.SCROLLVIEW_OFFSET_Y = this.BIGBACKVIEW_OFFSET_Y + (8.0f * this.zoomRate);
        this.SCROLLVIEW_WIDTH = this.BIGBACKVIEW_WIDTH - (8.0f * this.zoomRate);
        this.SCROLLVIEW_HEIGHT = this.BIGBACKVIEW_HEIGHT - (16.0f * this.zoomRate);
        this.SCROLLVIEW_CONTENT_HEIGHT = 790.0f * this.zoomRate;
        this.helpBackViewUnit = new HelpBackViewUnit(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.helpScrollViewUnit = new HelpScrollViewUnit(this.SCROLLVIEW_OFFSET_X, this.SCROLLVIEW_OFFSET_Y, this.SCROLLVIEW_WIDTH, this.SCROLLVIEW_HEIGHT, this.SCROLLVIEW_CONTENT_HEIGHT, this.zoomRate, this, this.appDelegate);
        this.helpFrontViewUnit = new HelpFrontViewUnit(this.appDelegate, this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        this.alertUnitType0 = new AlertUnitType0(this.finalWidth, this.finalHeight, this.zoomRate, this.appDelegate);
        if (!this.appDelegate.isRetina4) {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 140.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        } else {
            this.alertUnitType0.setBackViewParams(BitmapDescriptorFactory.HUE_RED, 184.0f, 320.0f, 200.0f, -16, 3.0f, -7576502, 3.0f, -16, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, 20.0f);
        }
        this.alertUnitType0.delegate = this;
    }

    public void doButtonClick(short _tag) {
        Log.d("HelpLayout", "clicked" + ((int) _tag));
        if (this.appDelegate != null && !this.hidden) {
            if (_tag == 0) {
                Log.d("HelpLayout", "manualButton00 clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                this.mainGameControllerLayout.doManualLayoutDisplay((short) 0);
                return;
            }
            if (_tag == 1) {
                Log.d("HelpLayout", "manualButton01 clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                this.mainGameControllerLayout.doManualLayoutDisplay((short) 1);
                return;
            }
            if (_tag == 2) {
                Log.d("HelpLayout", "manualButton02 clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                this.mainGameControllerLayout.doManualLayoutDisplay((short) 2);
                return;
            }
            if (_tag == 10) {
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences != null) {
                    if (this.appDelegate.defaultSharedPreferences.getBoolean("bgm_switch", false)) {
                        this.appDelegate.doSePoolPlay(2);
                        SharedPreferences.Editor editor = this.appDelegate.defaultSharedPreferences.edit();
                        editor.putBoolean("bgm_switch", false);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor2 = this.appDelegate.defaultSharedPreferences.edit();
                        editor2.putBoolean("bgm_switch", true);
                        editor2.commit();
                        this.appDelegate.doSePoolPlay(1);
                    }
                    if (this.helpScrollViewUnit != null) {
                        this.helpScrollViewUnit.refreshBGMSwitchButton();
                        return;
                    }
                    return;
                }
                return;
            }
            if (_tag == 11) {
                if (this.appDelegate.defaultSharedPreferences != null && this.appDelegate.defaultSharedPreferences != null) {
                    if (this.appDelegate.defaultSharedPreferences.getBoolean("sound_switch", false)) {
                        this.appDelegate.doSoundPoolPlay(2);
                        SharedPreferences.Editor editor3 = this.appDelegate.defaultSharedPreferences.edit();
                        editor3.putBoolean("sound_switch", false);
                        editor3.commit();
                    } else {
                        SharedPreferences.Editor editor4 = this.appDelegate.defaultSharedPreferences.edit();
                        editor4.putBoolean("sound_switch", true);
                        editor4.commit();
                        this.appDelegate.doSoundPoolPlay(1);
                    }
                    if (this.helpScrollViewUnit != null) {
                        this.helpScrollViewUnit.refreshSoundSwitchButton();
                        return;
                    }
                    return;
                }
                return;
            }
            if (_tag == 12) {
                if (this.appDelegate != null) {
                    if (this.appDelegate.checkInterNet()) {
                        if (this.mainGameControllerLayout != null) {
                            this.mainGameControllerLayout.goToSlosPage();
                        }
                    } else {
                        this.appDelegate.showNoInternetAlertDialog();
                    }
                }
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                    return;
                }
                return;
            }
            if (_tag == 20) {
                Log.d("HelpLayout", "uploadButton clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                this.appDelegate.openOnlineGameViewControllerWithAutoLogIn(true, (short) 100);
                return;
            }
            if (_tag == 30) {
                Log.d("HelpLayout", "facebookButton clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                shareToFacebook();
                return;
            }
            if (_tag == 31) {
                Log.d("HelpLayout", "line clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                shareToLine();
                return;
            }
            if (_tag == 32) {
                Log.d("HelpLayout", "mailButton clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                shareToMail();
                return;
            }
            if (_tag == 40) {
                Log.d("HelpLayout", "readyDoiDTLogout clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                readyDoIdtLogout();
                return;
            }
            if (_tag == 41) {
                Log.d("HelpLayout", "readyDoFacebookLogout clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                readyDoFacebookLogout();
                return;
            }
            if (_tag == 50) {
                Log.d("HelpLayout", "backToMainMenuButton clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(4);
                }
                readyDoBackToMainMenu();
                return;
            }
            if (_tag == 60) {
                Log.d("HelpLayout", "contactUsButton clicked");
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                emailUs();
            }
        }
    }

    @Override // com.idtinc.custom.AlertUnitType0Delegate
    public void buttonClick(short _tag, short _subtag, short _buttonIndex) throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (_tag == -99) {
            if (_buttonIndex != 0) {
            }
            return;
        }
        if (_tag == 400) {
            if (_buttonIndex == 0) {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpUnit.1
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                }, 100L);
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(2);
                    return;
                }
                return;
            }
            if (_buttonIndex == 1) {
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpUnit.2
                    @Override // java.lang.Runnable
                    public void run() {
                        HelpUnit.this.doIdtLogout();
                    }
                }, 200L);
                return;
            }
            return;
        }
        if (_tag == 600) {
            if (_buttonIndex == 0) {
                new Handler().postDelayed(new Runnable() { // from class: com.idtinc.maingame.sublayout3.HelpUnit.3
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                }, 100L);
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(2);
                    return;
                }
                return;
            }
            if (_buttonIndex == 1) {
                if (!this.hidden) {
                    this.appDelegate.doSoundPoolPlay(1);
                }
                doBackToMainMenu();
            }
        }
    }

    public void shareToFacebook() {
        if (this.appDelegate != null) {
            if (this.appDelegate.checkInterNet()) {
                this.appDelegate.readyDoShareImage((short) 0);
            } else {
                this.appDelegate.showNoInternetAlertDialog();
            }
        }
    }

    public void shareToLine() {
        if (this.appDelegate != null) {
            String sendText = String.valueOf(this.appDelegate.getResources().getString(R.string.ChickKitchen2)) + " (Free App)\n\niOS: http://www.idtfun.com/apps/chickkitchen_cd_ap0.html\n\nAndroid: http://www.idtfun.com/apps/chickkitchen_cd_gp0.html\n";
            this.appDelegate.shareToLineWithText(sendText);
        }
    }

    public void shareToMail() throws Resources.NotFoundException {
        sendPhotoMail();
    }

    public void sendPhotoMail() throws Resources.NotFoundException {
        String intentTitle = this.appDelegate.getResources().getString(R.string.TellFriendsCK2);
        String subject = this.appDelegate.getResources().getString(R.string.ChickKitchen2);
        String text = String.valueOf(this.appDelegate.getResources().getString(R.string.ChickKitchen2)) + " (Free App)\n\niOS: http://www.idtfun.com/apps/chickkitchen_cd_ap0.html\n\nAndroid: http://www.idtfun.com/apps/chickkitchen_cd_gp0.html\n";
        String filePath = "/data/data/" + this.appDelegate.getPackageName() + "/files";
        File file = new File(String.valueOf(filePath) + File.separator + "icon.png");
        this.appDelegate.shareMailWithUriImage(intentTitle, subject, text, Uri.fromFile(file));
    }

    public void readyDoIdtLogout() {
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
            contentLabelString1 = "";
            contentLabelString2 = "iDTアカウントからログアウトしますか？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要登出iDT帳號？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要登出iDT帐号？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Do you want to log out";
            contentLabelString2 = "from iDT account?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = (short) 400;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
    }

    public void doIdtLogout() {
        String titleLabelString;
        String contentLabelString0;
        String contentLabelString1;
        String contentLabelString2;
        String contentLabelString3;
        String contentLabelString4;
        float contentLabelLanguageOffsetY;
        if (this.appDelegate != null) {
            this.appDelegate.do_idt_account_logout();
        }
        hiddenAlert();
        String languageString = this.appDelegate.getLocaleLanguage();
        if (languageString.equals("ja-JP")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "iDTアカウントからログアウトしました。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "已經從iDT帳號登出。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "已经从iDT帐号登出。";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Logged out from";
            contentLabelString2 = "iDT account.";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 1, "", this.appDelegate.getResources().getString(R.string.OK), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, -1, 1, true);
        this.alertUnitType0.tag = (short) -99;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
    }

    public void readyDoFacebookLogout() {
        if (this.appDelegate != null) {
            this.appDelegate.readyDoFacebookLogout();
        }
    }

    public void readyDoBackToMainMenu() {
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
            contentLabelString1 = "";
            contentLabelString2 = "メインメニューに戻りますか？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-TW") || languageString.equals("zh-HK")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要返回主選單？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else if (languageString.equals("zh-CN")) {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "";
            contentLabelString2 = "你是否要返回主选单？";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = BitmapDescriptorFactory.HUE_RED;
        } else {
            titleLabelString = "";
            contentLabelString0 = "";
            contentLabelString1 = "Do you want to back";
            contentLabelString2 = "to main menu?";
            contentLabelString3 = "";
            contentLabelString4 = "";
            contentLabelLanguageOffsetY = 10.0f;
        }
        this.alertUnitType0.setTitleLabelParams(titleLabelString, this.appDelegate.typeface_FONTNAME_00, BitmapDescriptorFactory.HUE_RED, 10.0f, 28.0f, -436207872, 3.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setContentLabelParams(contentLabelString0, contentLabelString1, contentLabelString2, contentLabelString3, contentLabelString4, Typeface.DEFAULT_BOLD, BitmapDescriptorFactory.HUE_RED, 48.0f + contentLabelLanguageOffsetY, 20.0f, 14.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0);
        this.alertUnitType0.setType((short) 0, this.appDelegate.getResources().getString(R.string.No1), this.appDelegate.getResources().getString(R.string.Yes1), this.appDelegate.typeface_FONTNAME_00, 22.0f, FluctConstants.FRAME_ALPHA_COLOR, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, 0, -16, 2.0f, -436207872, 2.0f, -227838, 2.0f, FluctConstants.FRAME_ALPHA_COLOR, 10.0f, 2, -1, false);
        this.alertUnitType0.tag = (short) 600;
        this.alertUnitType0.subTag = (short) -1;
        popAlert();
    }

    public void doBackToMainMenu() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.mainGameControllerLayout != null && this.mainGameControllerLayout != null) {
            this.mainGameControllerLayout.doBackToMainMenu();
        }
    }

    public void emailUs() {
        if (this.appDelegate != null) {
            this.appDelegate.emailUs();
        }
    }

    public void checkIdtLogoutF() {
        this.helpScrollViewUnit.iDTLogoutButtonStatus = (short) -1;
        if (this.appDelegate != null && this.appDelegate.get_idt_account_logged_in().booleanValue()) {
            this.helpScrollViewUnit.iDTLogoutButtonStatus = (short) 0;
        }
    }

    public void checkFacebookLogoutF() {
        this.helpScrollViewUnit.facebookLogoutButtonStatus = (short) -1;
        if (this.appDelegate != null && !this.appDelegate.getLogoutF()) {
            this.helpScrollViewUnit.facebookLogoutButtonStatus = (short) 0;
        }
    }

    public void doDisplay() {
        reset();
    }

    public void doHidden() {
        hiddenAlert();
    }

    public void popAlert() {
        this.alertUnitType0.pop();
    }

    public void hiddenAlert() {
        this.alertUnitType0.reset();
    }

    public void doHiddenAllSurfaceViews() {
    }

    public void reset() {
        if (this.helpScrollViewUnit != null) {
            this.helpScrollViewUnit.reset();
        }
    }

    public void doInit() {
        reset();
    }

    public void doWillTerminate() {
        Log.d("HelpLayout", "doWillTerminate");
        hiddenAlert();
    }

    public void doWillEnterForeground() {
        Log.d("HelpLayout", "doWillEnterForeground");
    }

    public void doLoop() {
        checkIdtLogoutF();
        checkFacebookLogoutF();
    }

    public boolean gameOnTouch(MotionEvent event) {
        boolean returnF = false;
        if (this.alertUnitType0 == null || this.alertUnitType0.hidden) {
            return (this.helpScrollViewUnit == null || !(returnF = this.helpScrollViewUnit.gameOnTouch(event))) ? returnF : returnF;
        }
        this.alertUnitType0.gameOnTouch(event);
        return true;
    }

    public void gameDraw(Canvas canvas) {
        if (this.helpBackViewUnit != null) {
            this.helpBackViewUnit.gameDraw(canvas);
        }
        if (this.helpScrollViewUnit != null) {
            this.helpScrollViewUnit.gameDraw(canvas);
        }
        if (this.helpFrontViewUnit != null) {
            this.helpFrontViewUnit.gameDraw(canvas);
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
        if (this.helpFrontViewUnit != null) {
            this.helpFrontViewUnit.onDestroy();
            this.helpFrontViewUnit = null;
        }
        if (this.helpScrollViewUnit != null) {
            this.helpScrollViewUnit.onDestroy();
            this.helpScrollViewUnit = null;
        }
        if (this.helpBackViewUnit != null) {
            this.helpBackViewUnit.onDestroy();
            this.helpBackViewUnit = null;
        }
        this.mainGameControllerLayout = null;
        this.appDelegate = null;
    }
}
