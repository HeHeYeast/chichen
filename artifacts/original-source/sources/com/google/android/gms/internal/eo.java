package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.GooglePlayServicesUtil;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class eo {
    public static final Intent c(Intent intent) {
        intent.setData(Uri.fromParts("version", Integer.toString(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE), null));
        return intent;
    }
}
