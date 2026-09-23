package vpadn;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum G {
    API_ERR_UNKNOWN_ERROR(-1),
    API_ERR_NO_AD_FOUND_IN_ONLINE_QUEUE(-3),
    API_ERR_SDK_DEPRECATED(-5),
    API_ERR_DEVICE_NOT_SUPPORTED(-6),
    API_ERR_UNABLE_TO_DETEMINE_QUADKEY(-7),
    API_ERR_UNKNOWN_LICENSE_KEY(-8),
    API_ERR_BLOCKED_LICENSE_KEY(-9),
    API_ERR_INVALID_LICENSE_KEY(-10),
    API_ERR_WRONG_APP_STATUS(-13),
    API_ERR_TARGET_OS_NOT_SUPPORTED(-15),
    API_ERR_APP_IS_APPROVING(-17),
    API_ERR_NO_MATCHED_SCREEN_SIZE(-21),
    API_ERR_NO_MATCHED_AD_IMAGE(-22),
    API_ERR_DATABASE_DATA_ACCESS(-23),
    API_ERR_INVALID_IMPRESSION_AD_ID(-24),
    API_ERR_INVALID_REQ_PARAMETERS(-2),
    API_ERR_WRONG_AD_TYPE(-4),
    API_ERR_WRONG_CLICK_TYPE(-11),
    API_ERR_GEO_DECODING_FAILED(-12),
    API_ERR_ONLINE_QUEUED_AD_NOT_FOUND(-14),
    API_ERR_TEST_AD_NOT_FOUND(-16),
    API_ERR_INVALID_TS(-101),
    API_ERR_PERMISSION(-1001),
    API_ERR_IMP_CLICK_LIVE_AD_WEIGHT_ZERO(-104),
    API_SUCCESS(0);

    private int z;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static G[] valuesCustom() {
        G[] gArrValuesCustom = values();
        int length = gArrValuesCustom.length;
        G[] gArr = new G[length];
        System.arraycopy(gArrValuesCustom, 0, gArr, 0, length);
        return gArr;
    }

    G(int i) {
        this.z = 0;
        this.z = i;
    }

    public static G a(int i) {
        switch (i) {
            case -1001:
                break;
            case -104:
                break;
            case -101:
                break;
            case -24:
                break;
            case -23:
                break;
            case -22:
                break;
            case -21:
                break;
            case -17:
                break;
            case -16:
                break;
            case -15:
                break;
            case -14:
                break;
            case -13:
                break;
            case -12:
                break;
            case -11:
                break;
            case -10:
                break;
            case -9:
                break;
            case -8:
                break;
            case -7:
                break;
            case -6:
                break;
            case -5:
                break;
            case -4:
                break;
            case -3:
                break;
            case -2:
                break;
            case -1:
                break;
            case 0:
                break;
        }
        return API_ERR_UNKNOWN_ERROR;
    }

    public final int a() {
        return this.z;
    }
}
