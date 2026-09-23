package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum AdOrientation {
    AUTO,
    PORTRAIT,
    LANDSCAPE;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static AdOrientation[] valuesCustom() {
        AdOrientation[] adOrientationArrValuesCustom = values();
        int length = adOrientationArrValuesCustom.length;
        AdOrientation[] adOrientationArr = new AdOrientation[length];
        System.arraycopy(adOrientationArrValuesCustom, 0, adOrientationArr, 0, length);
        return adOrientationArr;
    }
}
