package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class UserRecoverableException extends Exception {
    private final Intent mIntent;

    public UserRecoverableException(String msg, Intent intent) {
        super(msg);
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return new Intent(this.mIntent);
    }
}
