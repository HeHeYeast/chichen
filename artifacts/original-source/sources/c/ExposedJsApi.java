package c;

import android.webkit.JavascriptInterface;
import org.json.JSONException;
import vpadn.C0099m;
import vpadn.C0107u;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ExposedJsApi {
    private C0107u a;
    private C0099m b;

    public ExposedJsApi(C0107u c0107u, C0099m c0099m) {
        this.a = c0107u;
        this.b = c0099m;
    }

    @JavascriptInterface
    public String exec(String str, String str2, String str3, String str4) throws JSONException {
        this.b.a(true);
        try {
            this.a.a(str, str2, str3, str4);
            return this.b.b();
        } finally {
            this.b.a(false);
        }
    }

    @JavascriptInterface
    public void setNativeToJsBridgeMode(int i) {
        this.b.a(i);
    }

    @JavascriptInterface
    public String retrieveJsMessages() {
        return this.b.b();
    }
}
