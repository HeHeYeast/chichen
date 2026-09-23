package c;

import android.util.Base64;
import org.json.JSONException;
import vpadn.C0089c;
import vpadn.C0101o;
import vpadn.C0103q;
import vpadn.C0108v;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Echo extends C0103q {
    @Override // vpadn.C0103q
    public boolean execute(String str, C0089c c0089c, final C0101o c0101o) throws JSONException {
        if ("echo".equals(str)) {
            c0101o.a(c0089c.b(0) ? null : c0089c.a(0));
            return true;
        }
        if ("echoAsync".equals(str)) {
            final String strA = c0089c.b(0) ? null : c0089c.a(0);
            this.cordova.e().execute(new Runnable(this) { // from class: c.Echo.1
                @Override // java.lang.Runnable
                public final void run() {
                    c0101o.a(strA);
                }
            });
            return true;
        }
        if (!"echoArrayBuffer".equals(str)) {
            return false;
        }
        c0101o.a(new C0108v(C0108v.a.OK, Base64.decode(c0089c.a.getString(0), 0)));
        return true;
    }
}
