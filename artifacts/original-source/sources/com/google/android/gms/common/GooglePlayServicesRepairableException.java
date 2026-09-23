package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GooglePlayServicesRepairableException extends UserRecoverableException {
    private final int iL;

    GooglePlayServicesRepairableException(int connectionStatusCode, String msg, Intent intent) {
        super(msg, intent);
        this.iL = connectionStatusCode;
    }

    public int getConnectionStatusCode() {
        return this.iL;
    }
}
