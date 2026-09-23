package com.google.android.gms.internal;

import android.view.View;
import android.webkit.WebChromeClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class cu extends cs {
    public cu(cq cqVar) {
        super(cqVar);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback customViewCallback) {
        a(view, requestedOrientation, customViewCallback);
    }
}
