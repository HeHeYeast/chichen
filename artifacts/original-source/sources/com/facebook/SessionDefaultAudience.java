package com.facebook;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum SessionDefaultAudience {
    NONE(null),
    ONLY_ME("SELF"),
    FRIENDS("ALL_FRIENDS"),
    EVERYONE("EVERYONE");

    private final String nativeProtocolAudience;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static SessionDefaultAudience[] valuesCustom() {
        SessionDefaultAudience[] sessionDefaultAudienceArrValuesCustom = values();
        int length = sessionDefaultAudienceArrValuesCustom.length;
        SessionDefaultAudience[] sessionDefaultAudienceArr = new SessionDefaultAudience[length];
        System.arraycopy(sessionDefaultAudienceArrValuesCustom, 0, sessionDefaultAudienceArr, 0, length);
        return sessionDefaultAudienceArr;
    }

    SessionDefaultAudience(String protocol) {
        this.nativeProtocolAudience = protocol;
    }

    String getNativeProtocolAudience() {
        return this.nativeProtocolAudience;
    }
}
