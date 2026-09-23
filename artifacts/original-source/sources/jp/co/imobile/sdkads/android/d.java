package jp.co.imobile.sdkads.android;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class d extends WebViewClient {
    final /* synthetic */ a a;
    private final /* synthetic */ z b;

    d(a aVar, z zVar) {
        this.a = aVar;
        this.b = zVar;
    }

    @Override // android.webkit.WebViewClient
    public final void onPageFinished(WebView webView, String url) {
        if (url.equals(this.b.f())) {
            new StringBuilder("url:").append(url);
            x.a(null);
            this.a.d = true;
            if (this.a.e != null) {
                this.a.e.onAdReadyCompleted();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x056c  */
    @Override // android.webkit.WebViewClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldOverrideUrlLoading(android.webkit.WebView r14, java.lang.String r15) throws org.json.JSONException, jp.co.imobile.sdkads.android.y, java.io.UnsupportedEncodingException {
        /*
            Method dump skipped, instructions count: 1441
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: jp.co.imobile.sdkads.android.d.shouldOverrideUrlLoading(android.webkit.WebView, java.lang.String):boolean");
    }
}
