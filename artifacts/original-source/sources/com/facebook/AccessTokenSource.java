package com.facebook;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum AccessTokenSource {
    NONE(false),
    FACEBOOK_APPLICATION_WEB(true),
    FACEBOOK_APPLICATION_NATIVE(true),
    FACEBOOK_APPLICATION_SERVICE(true),
    WEB_VIEW(false),
    TEST_USER(true),
    CLIENT_TOKEN(true);

    private final boolean canExtendToken;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static AccessTokenSource[] valuesCustom() {
        AccessTokenSource[] accessTokenSourceArrValuesCustom = values();
        int length = accessTokenSourceArrValuesCustom.length;
        AccessTokenSource[] accessTokenSourceArr = new AccessTokenSource[length];
        System.arraycopy(accessTokenSourceArrValuesCustom, 0, accessTokenSourceArr, 0, length);
        return accessTokenSourceArr;
    }

    AccessTokenSource(boolean canExtendToken) {
        this.canExtendToken = canExtendToken;
    }

    boolean canExtendToken() {
        return this.canExtendToken;
    }
}
