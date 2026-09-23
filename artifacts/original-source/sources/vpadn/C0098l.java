package vpadn;

import android.net.Uri;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import c.CordovaWebView;
import java.io.IOException;

/* renamed from: vpadn.l, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0098l extends C0092f {
    public C0098l(InterfaceC0102p interfaceC0102p, CordovaWebView cordovaWebView) {
        super(interfaceC0102p, cordovaWebView);
    }

    @Override // android.webkit.WebViewClient
    public final WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        return (str.contains("?") || str.contains("#")) ? a(str) : super.shouldInterceptRequest(webView, str);
    }

    private WebResourceResponse a(String str) {
        if (str.startsWith("file:///android_asset/")) {
            String strReplaceFirst = str.replaceFirst("file:///android_asset/", "");
            if (strReplaceFirst.contains("?")) {
                strReplaceFirst = strReplaceFirst.split("\\?")[0];
            } else if (strReplaceFirst.contains("#")) {
                strReplaceFirst = strReplaceFirst.split("#")[0];
            }
            try {
                return new WebResourceResponse(strReplaceFirst.endsWith(".html") ? "text/html" : null, "UTF-8", this.a.a().getAssets().open(Uri.parse(strReplaceFirst).getPath(), 2));
            } catch (IOException e) {
                C0104r.b("generateWebResourceResponse", e.getMessage(), e);
            }
        }
        return null;
    }
}
