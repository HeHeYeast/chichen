package com.facebook;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum SessionLoginBehavior {
    SSO_WITH_FALLBACK(true, true),
    SSO_ONLY(true, false),
    SUPPRESS_SSO(false, true);

    private final boolean allowsKatanaAuth;
    private final boolean allowsWebViewAuth;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static SessionLoginBehavior[] valuesCustom() {
        SessionLoginBehavior[] sessionLoginBehaviorArrValuesCustom = values();
        int length = sessionLoginBehaviorArrValuesCustom.length;
        SessionLoginBehavior[] sessionLoginBehaviorArr = new SessionLoginBehavior[length];
        System.arraycopy(sessionLoginBehaviorArrValuesCustom, 0, sessionLoginBehaviorArr, 0, length);
        return sessionLoginBehaviorArr;
    }

    SessionLoginBehavior(boolean allowsKatanaAuth, boolean allowsWebViewAuth) {
        this.allowsKatanaAuth = allowsKatanaAuth;
        this.allowsWebViewAuth = allowsWebViewAuth;
    }

    boolean allowsKatanaAuth() {
        return this.allowsKatanaAuth;
    }

    boolean allowsWebViewAuth() {
        return this.allowsWebViewAuth;
    }
}
