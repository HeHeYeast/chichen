package com.google.android.gms.common;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class GooglePlayServicesNotAvailableException extends Exception {
    public final int errorCode;

    public GooglePlayServicesNotAvailableException(int errorCode) {
        this.errorCode = errorCode;
    }
}
