package com.facebook;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public enum LoggingBehavior {
    REQUESTS,
    INCLUDE_ACCESS_TOKENS,
    INCLUDE_RAW_RESPONSES,
    CACHE,
    DEVELOPER_ERRORS;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static LoggingBehavior[] valuesCustom() {
        LoggingBehavior[] loggingBehaviorArrValuesCustom = values();
        int length = loggingBehaviorArrValuesCustom.length;
        LoggingBehavior[] loggingBehaviorArr = new LoggingBehavior[length];
        System.arraycopy(loggingBehaviorArrValuesCustom, 0, loggingBehaviorArr, 0, length);
        return loggingBehaviorArr;
    }
}
