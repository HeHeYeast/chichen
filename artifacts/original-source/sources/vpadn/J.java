package vpadn;

import com.vpadn.ads.VpadnAdRequest;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface J {
    void onControllerWebViewReady(int i, int i2);

    void onLeaveExpandMode();

    void onPrepareExpandMode();

    void onVponAdFailed(VpadnAdRequest.VpadnErrorCode vpadnErrorCode);

    void onVponAdReceived();

    void onVponDismiss();

    void onVponLeaveApplication();

    void onVponPresent();
}
