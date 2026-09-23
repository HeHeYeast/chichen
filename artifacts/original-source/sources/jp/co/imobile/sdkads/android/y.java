package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class y extends Exception {
    private FailNotificationReason a;

    y(FailNotificationReason failNotificationReason) {
        this.a = failNotificationReason;
    }

    final FailNotificationReason a() {
        return this.a;
    }
}
