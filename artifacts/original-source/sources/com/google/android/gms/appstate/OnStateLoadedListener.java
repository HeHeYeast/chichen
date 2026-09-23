package com.google.android.gms.appstate;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface OnStateLoadedListener {
    void onStateConflict(int i, String str, byte[] bArr, byte[] bArr2);

    void onStateLoaded(int i, int i2, byte[] bArr);
}
