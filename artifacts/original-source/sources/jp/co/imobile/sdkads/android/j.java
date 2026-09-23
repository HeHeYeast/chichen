package jp.co.imobile.sdkads.android;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
enum j {
    NONE,
    LODING,
    DISPLAYABLE,
    ERROR,
    SCRIPT_ERROR,
    DISPLAYED,
    DISPLAING,
    EXPIRED;

    public static j[] a() {
        j[] jVarArrValues = values();
        int length = jVarArrValues.length;
        j[] jVarArr = new j[length];
        System.arraycopy(jVarArrValues, 0, jVarArr, 0, length);
        return jVarArr;
    }
}
