package com.idtinc.onlinegame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class OnlineGameViewController extends RelativeLayout {
    public AccountLoginUnit accountLoginUnit;
    public Boolean accountLoginUnitEditTextHiddenF;
    public Boolean accountRegisterUnitEditTextHiddenF;
    private AppDelegate appDelegate;
    public AppMainActivity appMainActivity;
    public DownloadSaveFileUnit downloadSaveFileUnit;
    private float finalHeight;
    private float finalWidth;
    public short nextActiveStatus;
    public short nowStatus;
    public OnlineGameActiveView onlineGameActiveView;
    public UploadSaveFileUnit uploadSaveFileUnit;
    private float zoomRate;

    public OnlineGameViewController(Context context, float _finalwidth, float _finalheight, float _zoomrate, AppMainActivity _appMainActivity) {
        super(context);
        this.finalWidth = BitmapDescriptorFactory.HUE_RED;
        this.finalHeight = BitmapDescriptorFactory.HUE_RED;
        this.zoomRate = 1.0f;
        this.nowStatus = (short) -1;
        this.nextActiveStatus = (short) -1;
        this.accountLoginUnitEditTextHiddenF = true;
        this.accountRegisterUnitEditTextHiddenF = true;
        this.appDelegate = null;
        this.appMainActivity = null;
        this.onlineGameActiveView = null;
        this.accountLoginUnit = null;
        this.uploadSaveFileUnit = null;
        this.downloadSaveFileUnit = null;
        this.appDelegate = (AppDelegate) context.getApplicationContext();
        this.appMainActivity = _appMainActivity;
        this.finalWidth = _finalwidth;
        this.finalHeight = _finalheight;
        this.zoomRate = _zoomrate;
        this.nowStatus = (short) -1;
        this.nextActiveStatus = (short) -1;
        this.accountLoginUnit = new AccountLoginUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.uploadSaveFileUnit = new UploadSaveFileUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.downloadSaveFileUnit = new DownloadSaveFileUnit(this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.onlineGameActiveView = new OnlineGameActiveView(context, this.finalWidth, this.finalHeight, this.zoomRate, this, this.appDelegate);
        this.onlineGameActiveView.setBackgroundColor(0);
        this.onlineGameActiveView.setVisibility(0);
        addView(this.onlineGameActiveView, (int) this.finalWidth, (int) this.finalHeight);
        if (this.accountLoginUnit.accountIDEditText != null) {
            RelativeLayout.LayoutParams accountIDEditTextLayoutParams = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.ACCOUNTIDTEXTFIELD_WIDTH, (int) this.accountLoginUnit.ACCOUNTIDTEXTFIELD_HEIGHT);
            accountIDEditTextLayoutParams.setMargins((int) this.accountLoginUnit.ACCOUNTIDTEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.ACCOUNTIDTEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountIDEditText, accountIDEditTextLayoutParams);
        }
        if (this.accountLoginUnit.accountPWEditText != null) {
            RelativeLayout.LayoutParams accountPWEditTextLayoutParams = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.ACCOUNTPWTEXTFIELD_WIDTH, (int) this.accountLoginUnit.ACCOUNTPWTEXTFIELD_HEIGHT);
            accountPWEditTextLayoutParams.setMargins((int) this.accountLoginUnit.ACCOUNTPWTEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.ACCOUNTPWTEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountPWEditText, accountPWEditTextLayoutParams);
        }
        if (this.accountLoginUnit.accountRegisterUnit.accountIDEditText != null) {
            RelativeLayout.LayoutParams accountIDEditTextLayoutParams2 = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTIDTEXTFIELD_WIDTH, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTIDTEXTFIELD_HEIGHT);
            accountIDEditTextLayoutParams2.setMargins((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTIDTEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTIDTEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountRegisterUnit.accountIDEditText, accountIDEditTextLayoutParams2);
        }
        if (this.accountLoginUnit.accountRegisterUnit.accountPWEditText != null) {
            RelativeLayout.LayoutParams accountPWEditTextLayoutParams2 = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTPWTEXTFIELD_WIDTH, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTPWTEXTFIELD_HEIGHT);
            accountPWEditTextLayoutParams2.setMargins((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTPWTEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTPWTEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountRegisterUnit.accountPWEditText, accountPWEditTextLayoutParams2);
        }
        if (this.accountLoginUnit.accountRegisterUnit.accountRePWEditText != null) {
            RelativeLayout.LayoutParams accountRePWEditTextLayoutParams = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTREPWTEXTFIELD_WIDTH, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTREPWTEXTFIELD_HEIGHT);
            accountRePWEditTextLayoutParams.setMargins((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTREPWTEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTREPWTEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountRegisterUnit.accountRePWEditText, accountRePWEditTextLayoutParams);
        }
        if (this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText != null) {
            RelativeLayout.LayoutParams accountFirstNameEditTextLayoutParams = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTFIRSTNAMETEXTFIELD_WIDTH, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTFIRSTNAMETEXTFIELD_HEIGHT);
            accountFirstNameEditTextLayoutParams.setMargins((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTFIRSTNAMETEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText, accountFirstNameEditTextLayoutParams);
        }
        if (this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText != null) {
            RelativeLayout.LayoutParams accountLastNameEditTextLayoutParams = new RelativeLayout.LayoutParams((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTLASTNAMETEXTFIELD_WIDTH, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTLASTNAMETEXTFIELD_HEIGHT);
            accountLastNameEditTextLayoutParams.setMargins((int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTLASTNAMETEXTFIELD_OFFSET_X, (int) this.accountLoginUnit.accountRegisterUnit.ACCOUNTLASTNAMETEXTFIELD_OFFSET_Y, 0, 0);
            addView(this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText, accountLastNameEditTextLayoutParams);
        }
        hiddenAccountLoginUnitEditText();
        hiddenAccountRegisterUnitUnitEditText();
    }

    public void displayAccountLoginUnitEditText() {
        if (this.accountLoginUnit != null) {
            if (this.accountLoginUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountIDEditText.setVisibility(0);
            }
            if (this.accountLoginUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountPWEditText.setVisibility(0);
            }
        }
        this.accountLoginUnitEditTextHiddenF = false;
    }

    public void hiddenAccountLoginUnitEditText() {
        this.accountLoginUnitEditTextHiddenF = true;
        if (this.accountLoginUnit != null) {
            if (this.accountLoginUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountIDEditText.setVisibility(8);
            }
            if (this.accountLoginUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountPWEditText.setVisibility(8);
            }
        }
    }

    public void displayAccountRegisterUnitUnitEditText() {
        if (this.accountLoginUnit.accountRegisterUnit != null) {
            if (this.accountLoginUnit.accountRegisterUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountIDEditText.setVisibility(0);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountPWEditText.setVisibility(0);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountRePWEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountRePWEditText.setVisibility(0);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText.setVisibility(0);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText.setVisibility(0);
            }
        }
        this.accountRegisterUnitEditTextHiddenF = false;
    }

    public void hiddenAccountRegisterUnitUnitEditText() {
        this.accountRegisterUnitEditTextHiddenF = true;
        if (this.accountLoginUnit.accountRegisterUnit != null) {
            if (this.accountLoginUnit.accountRegisterUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountIDEditText.setVisibility(8);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountPWEditText.setVisibility(8);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountRePWEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountRePWEditText.setVisibility(8);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountFirstNameEditText.setVisibility(8);
            }
            if (this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText != null) {
                this.accountLoginUnit.accountRegisterUnit.accountLastNameEditText.setVisibility(8);
            }
        }
    }

    public void clear() {
        this.nowStatus = (short) -1;
        this.nextActiveStatus = (short) -1;
    }

    public void reset() {
        setVisibility(8);
        clear();
        if (this.onlineGameActiveView != null) {
            this.onlineGameActiveView.reset();
        }
        this.uploadSaveFileUnit.reset();
        this.downloadSaveFileUnit.reset();
        this.accountLoginUnit.reset();
    }

    public void close() {
        setVisibility(8);
        reset();
        if (this.appMainActivity != null) {
            this.appMainActivity.hiddenOnlineGameViewController();
        }
    }

    public void doWillTerminate() {
        if (this.accountLoginUnit.accountRegisterUnit.hidden.booleanValue()) {
            close();
        }
    }

    public void openWithAutoLogIn(Boolean _autoLogInF, short _nextActiveStstus) {
        reset();
        this.nowStatus = (short) 0;
        this.nextActiveStatus = _nextActiveStstus;
        this.accountLoginUnit.openWithAutoLogIn(_autoLogInF);
        setVisibility(0);
    }

    public void clicked(short _index) {
        if (_index == 0) {
            this.appDelegate.doSoundPoolPlay(2);
            close();
        }
    }

    public void doNextActiveStatus(short _index) {
        if (_index != -1) {
            if (_index == 0) {
                if (this.nextActiveStatus == 100) {
                    this.nowStatus = this.nextActiveStatus;
                    this.nextActiveStatus = (short) -1;
                    this.uploadSaveFileUnit.openWithAutoUpload(false);
                    return;
                } else if (this.nextActiveStatus == 200) {
                    this.nowStatus = this.nextActiveStatus;
                    this.nextActiveStatus = (short) -1;
                    this.downloadSaveFileUnit.openWithAutoDownload(true);
                    return;
                }
            } else if (_index != 100) {
            }
        }
        close();
    }

    public void doLoop() {
        if (getVisibility() == 0 && this.onlineGameActiveView != null) {
            this.onlineGameActiveView.doLoop();
        }
    }

    public void startLoading(String _loadingLabelString) {
        if (this.accountLoginUnit != null) {
            if (this.accountLoginUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountIDEditText.setFocusableInTouchMode(false);
                this.accountLoginUnit.accountIDEditText.setFocusable(false);
            }
            if (this.accountLoginUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountPWEditText.setFocusableInTouchMode(false);
                this.accountLoginUnit.accountPWEditText.setFocusable(false);
            }
        }
        if (this.onlineGameActiveView != null) {
            this.onlineGameActiveView.startLoading(_loadingLabelString);
        }
    }

    public void stopLoading() {
        this.onlineGameActiveView.stopLoading();
        if (this.accountLoginUnit != null) {
            if (this.accountLoginUnit.accountIDEditText != null) {
                this.accountLoginUnit.accountIDEditText.setFocusableInTouchMode(true);
                this.accountLoginUnit.accountIDEditText.setFocusable(true);
            }
            if (this.accountLoginUnit.accountPWEditText != null) {
                this.accountLoginUnit.accountPWEditText.setFocusableInTouchMode(true);
                this.accountLoginUnit.accountPWEditText.setFocusable(true);
            }
        }
    }

    public boolean gameOnTouch(MotionEvent event) {
        if (this.onlineGameActiveView.loadingLabelString == null || this.onlineGameActiveView.loadingLabelString.length() <= 0) {
            return false;
        }
        return true;
    }

    public void gameDraw(Canvas canvas) {
    }

    public void onDestroy() {
        if (this.downloadSaveFileUnit != null) {
            this.downloadSaveFileUnit.onDestroy();
            this.downloadSaveFileUnit = null;
        }
        if (this.uploadSaveFileUnit != null) {
            this.uploadSaveFileUnit.onDestroy();
            this.uploadSaveFileUnit = null;
        }
        if (this.accountLoginUnit != null) {
            this.accountLoginUnit.onDestroy();
            this.accountLoginUnit = null;
        }
        if (this.onlineGameActiveView != null) {
            this.onlineGameActiveView.onDestroy();
            this.onlineGameActiveView = null;
        }
        this.appMainActivity = null;
        this.appDelegate = null;
    }
}
