package com.idtinc.ckchickandduck;

import android.graphics.Bitmap;
import android.net.Uri;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface AppDelegateInterface {
    void backToMainMenu();

    void backToSavesCheck();

    boolean checkAdColonyV4VCF();

    void delayDisplayFullAdView(int i);

    void displayFullAdView();

    void doMainLoop();

    void emailUs();

    boolean getLogoutF();

    void goToCKKB();

    void goToCKMV();

    void goToMainGame();

    void goToSavesCheck();

    void goToSlosPage();

    void hiddenAdControlLayout(boolean z);

    void hiddenBonusUnitViewAd(boolean z);

    boolean isInstallSoftware(String str);

    void loadAdMobFullScreenAd();

    void openOnlineGameViewControllerWithAutoLogIn(Boolean bool, short s);

    void readyDoFacebookLogout();

    void readyDoFbLogin(short s);

    void readyDoInitViews();

    void readyDoShareImage(short s);

    void returnToMainGame();

    boolean savePic(Bitmap bitmap, String str, String str2);

    void shareMailWithUriImage(String str, String str2, String str3, Uri uri);

    boolean showAdColonyV4VCF();

    void showNoInternetAlertDialog();

    Bitmap takeScreenShot(float f, float f2, float f3, float f4);
}
