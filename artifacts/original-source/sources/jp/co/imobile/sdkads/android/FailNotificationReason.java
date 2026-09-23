package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum FailNotificationReason {
    PARAM,
    AUTHORITY,
    RESPONSE,
    NETWORK_NOT_READY,
    NETWORK,
    UNKNOWN,
    AD_NOT_READY,
    NOT_DELIVERY_AD;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static FailNotificationReason[] valuesCustom() {
        FailNotificationReason[] failNotificationReasonArrValuesCustom = values();
        int length = failNotificationReasonArrValuesCustom.length;
        FailNotificationReason[] failNotificationReasonArr = new FailNotificationReason[length];
        System.arraycopy(failNotificationReasonArrValuesCustom, 0, failNotificationReasonArr, 0, length);
        return failNotificationReasonArr;
    }
}
