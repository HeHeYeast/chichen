package com.google.android.gms.auth;

import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GooglePlayServicesAvailabilityException extends UserRecoverableAuthException {
    private final int iL;

    GooglePlayServicesAvailabilityException(int connectionStatusCode, String msg, Intent intent) {
        super(msg, intent);
        this.iL = connectionStatusCode;
    }

    public int getConnectionStatusCode() {
        return this.iL;
    }
}
