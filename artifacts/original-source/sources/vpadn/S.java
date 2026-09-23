package vpadn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class S {
    private static S b = new S();
    private Map<String, StringBuffer> a = Collections.synchronizedMap(new HashMap());

    private S() {
    }

    public static S a() {
        return b;
    }

    private void a(String str, String str2, boolean z) {
        if (!z) {
            this.a.remove(str);
        }
        StringBuffer stringBuffer = this.a.get(str);
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }
        if (z) {
            stringBuffer.append("-----------------------------------------------------------\n");
        } else {
            stringBuffer.append("\n");
        }
        stringBuffer.append(str2);
        this.a.put(str, stringBuffer);
    }

    private void a(String str, HttpResponse httpResponse, boolean z) {
        if (!z) {
            this.a.remove(str);
        }
        StringBuffer stringBuffer = this.a.get(str);
        StringBuffer stringBuffer2 = stringBuffer == null ? new StringBuffer() : stringBuffer;
        if (z) {
            stringBuffer2.append("-----------------------------------------------------------\n");
        } else {
            stringBuffer2.append("\n");
        }
        for (Header header : httpResponse.getAllHeaders()) {
            stringBuffer2.append(String.valueOf(header.toString()) + "\n");
        }
        this.a.put(str, stringBuffer2);
    }

    public final void a(HttpResponse httpResponse, boolean z) {
        a("OK_key", httpResponse, z);
    }

    public final void b(HttpResponse httpResponse, boolean z) {
        a("ERR_key", httpResponse, z);
    }

    public final void a(String str, boolean z) {
        a("OK_key", str, true);
    }

    public final void b(String str, boolean z) {
        a("ERR_key", str, true);
    }

    public final void b() {
        this.a.clear();
    }

    public final void c() {
    }

    public final void d() {
    }

    public final void a(String str) {
    }

    public final void b(String str) {
    }
}
