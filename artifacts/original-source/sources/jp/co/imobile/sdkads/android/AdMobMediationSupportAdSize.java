package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum AdMobMediationSupportAdSize {
    BUNNER(320, 50),
    LARGE_BANNER(320, 100),
    MEDIUM_RECTANGLE(300, 250);

    private final int a;
    private final int b;

    AdMobMediationSupportAdSize(int width, int height) {
        this.b = width;
        this.a = height;
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static AdMobMediationSupportAdSize[] valuesCustom() {
        AdMobMediationSupportAdSize[] adMobMediationSupportAdSizeArrValuesCustom = values();
        int length = adMobMediationSupportAdSizeArrValuesCustom.length;
        AdMobMediationSupportAdSize[] adMobMediationSupportAdSizeArr = new AdMobMediationSupportAdSize[length];
        System.arraycopy(adMobMediationSupportAdSizeArrValuesCustom, 0, adMobMediationSupportAdSizeArr, 0, length);
        return adMobMediationSupportAdSizeArr;
    }

    public final int getHeight() {
        return this.a;
    }

    public final int getWidth() {
        return this.b;
    }
}
