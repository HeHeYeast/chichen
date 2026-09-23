package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
enum am {
    NONE,
    LODING,
    START,
    PAUSE,
    STOP,
    ERROR;

    public static am[] a() {
        am[] amVarArrValues = values();
        int length = amVarArrValues.length;
        am[] amVarArr = new am[length];
        System.arraycopy(amVarArrValues, 0, amVarArr, 0, length);
        return amVarArr;
    }
}
