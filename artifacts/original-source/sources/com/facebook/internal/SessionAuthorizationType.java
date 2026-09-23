package com.facebook.internal;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum SessionAuthorizationType {
    READ,
    PUBLISH;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static SessionAuthorizationType[] valuesCustom() {
        SessionAuthorizationType[] sessionAuthorizationTypeArrValuesCustom = values();
        int length = sessionAuthorizationTypeArrValuesCustom.length;
        SessionAuthorizationType[] sessionAuthorizationTypeArr = new SessionAuthorizationType[length];
        System.arraycopy(sessionAuthorizationTypeArrValuesCustom, 0, sessionAuthorizationTypeArr, 0, length);
        return sessionAuthorizationTypeArr;
    }
}
